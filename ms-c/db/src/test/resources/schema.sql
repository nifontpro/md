CREATE SCHEMA IF NOT EXISTS dep;;
CREATE SCHEMA IF NOT EXISTS users;;
CREATE SCHEMA IF NOT EXISTS md;;
CREATE SCHEMA IF NOT EXISTS env;;

-- DEPTS

CREATE TABLE IF NOT EXISTS dep.dept
(
    id bigserial NOT NULL primary key,
    parent_id bigint,
    name text COLLATE pg_catalog."default" NOT NULL,
    code text COLLATE pg_catalog."default" NOT NULL DEFAULT 'N'::text,
    classname text COLLATE pg_catalog."default",
    main_img text COLLATE pg_catalog."default",
    top_level boolean NOT NULL DEFAULT false,
    CONSTRAINT parent_id_fkey FOREIGN KEY (parent_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;

CREATE TABLE IF NOT EXISTS dep.dept_details
(
    dept_id bigint NOT NULL,
    address text COLLATE pg_catalog."default",
    email text COLLATE pg_catalog."default",
    phone text COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT dept_details_pkey PRIMARY KEY (dept_id),
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS md.base_image
(
    image_url text COLLATE pg_catalog."default",
    image_key text COLLATE pg_catalog."default",
    type_code text COLLATE pg_catalog."default",
    main boolean,
    created_at timestamp without time zone,
    mini_url text COLLATE pg_catalog."default",
    mini_key text COLLATE pg_catalog."default"
);;

CREATE TABLE IF NOT EXISTS dep.dept_image
(
    id bigserial NOT NULL primary key,
    dept_id bigint NOT NULL,
    CONSTRAINT dept_id_fk FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    INHERITS (md.base_image);;

CREATE OR REPLACE FUNCTION dep.sub_tree_ids(
    dept_id bigint)
    RETURNS TABLE(id bigint)
    LANGUAGE 'sql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
-- Получить ids поддерева подразделений от текущего узла
WITH RECURSIVE tmp AS (
    SELECT dept.id FROM dep.dept WHERE id = dept_id

    UNION

    SELECT dept.id FROM dep.dept
    JOIN tmp ON dept.parent_id = tmp.id
) SELECT tmp.id FROM tmp;
$BODY$;;

CREATE OR REPLACE FUNCTION dep.up_tree_has_id(
    down_id bigint,
    up_id bigint)
    RETURNS boolean
    LANGUAGE plpgsql
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
-- Является ли верхний отдел предком нижнего (Для авторизации)
DECLARE
    ret boolean := false;
    curent_id bigint := $1;
    find_id bigint := $2;
BEGIN
    WHILE curent_id IS NOT NULL LOOP
            SELECT parent_id INTO curent_id from dep.dept where id = curent_id;

            IF curent_id = find_id
            THEN
                ret := true;
                EXIT;
            END IF;

        END LOOP;
    RETURN ret;
END;
$BODY$;;

CREATE OR REPLACE FUNCTION dep.check_user_child(
    user_id bigint,
    up_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
-- Является ли сотрудник потомком отдела (Для авторизации)

DECLARE
    ret boolean := false;
    user_id bigint := $1;
    find_id bigint := $2;
    curent_id bigint;
BEGIN

    SELECT dept_id INTO curent_id FROM users.user_data WHERE id = user_id;

    IF (curent_id = find_id) THEN RETURN true;
    END IF;

    WHILE curent_id IS NOT NULL LOOP
            SELECT parent_id INTO curent_id from dep.dept where id = curent_id;

            IF curent_id = find_id
            THEN
                ret := true;
                EXIT;
            END IF;

        END LOOP;
    RETURN ret;
END;
$BODY$;;

CREATE OR REPLACE FUNCTION dep.get_root_id(
    dept_id bigint)
    RETURNS bigint
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
-- Является ли верхний отдел предком нижнего (Для авторизации)

DECLARE
    curent_id bigint := $1;
    root_id bigint;
    c_id bigint;
    curent_code text;

BEGIN
    WHILE curent_id IS NOT NULL LOOP
            SELECT id, parent_id, code INTO c_id, curent_id, curent_code from dep.dept where id = curent_id;

            IF curent_code = 'U'
            THEN
                root_id := c_id;
                EXIT;
            END IF;

        END LOOP;
    RETURN root_id;
END;

$BODY$;;

CREATE OR REPLACE FUNCTION dep.get_top_level_id(
    dept_id bigint)
    RETURNS bigint
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
-- Является ли верхний отдел предком нижнего (Для авторизации)

DECLARE
    curent_id bigint := $1;
    top_level_id bigint;
    c_id bigint;
    curent_code text;
    curent_top_level boolean;

BEGIN
    WHILE curent_id IS NOT NULL LOOP
            SELECT id, parent_id, code, top_level
            INTO c_id, curent_id, curent_code, curent_top_level
            FROM dep.dept where id = curent_id;

            IF curent_top_level OR curent_code = 'U'
            THEN
                top_level_id := c_id;
                EXIT;
            END IF;

        END LOOP;
    RETURN top_level_id;
END;

$BODY$;;

-- USERS

CREATE TABLE IF NOT EXISTS users.user_data
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    firstname text COLLATE pg_catalog."default" NOT NULL,
    lastname text COLLATE pg_catalog."default",
    patronymic text COLLATE pg_catalog."default",
    gender_code text COLLATE pg_catalog."default",
    auth_email text COLLATE pg_catalog."default",
    dept_id bigint NOT NULL,
    post text COLLATE pg_catalog."default",
    main_img text COLLATE pg_catalog."default",
    CONSTRAINT user_data_pkey PRIMARY KEY (id),
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;

CREATE TABLE IF NOT EXISTS users.user_details
(
    user_id bigint NOT NULL,
    phone text COLLATE pg_catalog."default",
    address text COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT user_profile_pkey PRIMARY KEY (user_id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS users.user_roles
(
    id bigserial NOT NULL primary key,
    user_id bigint NOT NULL,
    role_code text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_id_fk FOREIGN KEY (user_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS users.user_image
(
    id bigserial NOT NULL primary key,
    user_id bigint NOT NULL,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    INHERITS (md.base_image);

