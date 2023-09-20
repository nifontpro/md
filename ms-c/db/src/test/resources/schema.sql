CREATE SCHEMA IF NOT EXISTS dep;;
CREATE SCHEMA IF NOT EXISTS users;;
CREATE SCHEMA IF NOT EXISTS md;;
CREATE SCHEMA IF NOT EXISTS env;;
CREATE SCHEMA IF NOT EXISTS rew;;

-- DEPTS

CREATE TABLE IF NOT EXISTS dep.dept
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    parent_id bigint,
    name text COLLATE pg_catalog."default" NOT NULL,
    code text COLLATE pg_catalog."default" NOT NULL DEFAULT 'N'::text,
    classname text COLLATE pg_catalog."default",
    main_img text COLLATE pg_catalog."default",
    top_level boolean NOT NULL DEFAULT false,
    level integer NOT NULL DEFAULT 0,
    CONSTRAINT dept_pkey PRIMARY KEY (id),
    CONSTRAINT parent_id_fkey FOREIGN KEY (parent_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;

CREATE OR REPLACE FUNCTION dep.get_level(
    dept_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
-- Является ли верхний отдел предком нижнего (Для авторизации)

DECLARE
    curent_id bigint := $1;
    lev int := 0;
    c_id bigint;
    curent_code text;

BEGIN
    WHILE curent_id IS NOT NULL LOOP
            SELECT id, parent_id, code INTO c_id, curent_id, curent_code from dep.dept where id = curent_id;
            lev = lev + 1;
    END LOOP;
    RETURN lev - 1;
END;
$BODY$;;

CREATE OR REPLACE FUNCTION dep.calc_level()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    update dep.dept set level = dep.get_level(new.id) where id = new.id;

    update dep.dept set top_level = level < 3 where id = new.id;

    return new;
END
$BODY$;;

CREATE OR REPLACE TRIGGER add_dept
    AFTER INSERT OR UPDATE OF parent_id
    ON dep.dept
    FOR EACH ROW
EXECUTE FUNCTION dep.calc_level();;


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
        ON DELETE CASCADE
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
    archive boolean NOT NULL DEFAULT false,
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

CREATE TABLE IF NOT EXISTS users.settings
(
    user_id bigint NOT NULL,
    show_onb boolean NOT NULL DEFAULT false,
    page_onb integer,
    CONSTRAINT settings_pkey PRIMARY KEY (user_id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

-- AWARDS

CREATE TABLE IF NOT EXISTS md.award
(
    id bigserial NOT NULL primary key,
    dept_id bigint NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    type_code text COLLATE pg_catalog."default" NOT NULL DEFAULT 'N'::text,
    main_img text COLLATE pg_catalog."default",
    score integer NOT NULL DEFAULT 1,
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;

CREATE TABLE IF NOT EXISTS md.award_details
(
    award_id bigint NOT NULL,
    description text COLLATE pg_catalog."default",
    criteria text COLLATE pg_catalog."default",
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT award_details_pkey PRIMARY KEY (award_id),
    CONSTRAINT award_id_fkey FOREIGN KEY (award_id)
        REFERENCES md.award (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS md.award_image
(
    id bigserial NOT NULL primary key,
    award_id bigint NOT NULL,
    CONSTRAINT award_id_fkey FOREIGN KEY (award_id)
        REFERENCES md.award (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    INHERITS (md.base_image)
;;

CREATE TABLE IF NOT EXISTS md.activity
(
    id bigserial NOT NULL primary key,
    date timestamp without time zone NOT NULL DEFAULT now(),
    user_id bigint NOT NULL,
    award_id bigint NOT NULL,
    action_code text COLLATE pg_catalog."default" NOT NULL DEFAULT 'N'::text,
    is_activ boolean NOT NULL,
    dept_id bigint NOT NULL,
    auth_id bigint NOT NULL,
    CONSTRAINT auth_id_fkey FOREIGN KEY (auth_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT award_id_fkey FOREIGN KEY (award_id)
        REFERENCES md.award (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT,
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;


CREATE OR REPLACE FUNCTION md.activ_count(
    root_id bigint)
    RETURNS TABLE(did bigint, award_count bigint, nominee_count bigint, delete_count bigint)
    LANGUAGE 'sql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

SELECT did,
       (select count(*) from md.activity i where i.dept_id = did and i.is_activ and i.action_code='A') as award_count,
       (select count(*) from md.activity i where i.dept_id = did and i.is_activ and i.action_code='P') as nominee_count,
       (select count(*) from md.activity i where i.dept_id = did and i.is_activ and i.action_code='D') as delete_count
from dep.sub_tree_ids(root_id) as did;

$BODY$;;

CREATE TABLE IF NOT EXISTS rew.medal
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name text COLLATE pg_catalog."default" NOT NULL DEFAULT ''::text,
    dept_id bigint NOT NULL,
    score integer NOT NULL DEFAULT 1,
    main_img text COLLATE pg_catalog."default",
    CONSTRAINT medal_pkey PRIMARY KEY (id),
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;

CREATE TABLE IF NOT EXISTS rew.medal_details
(
    medal_id bigint NOT NULL,
    description text COLLATE pg_catalog."default",
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT medal_details_pkey PRIMARY KEY (medal_id),
    CONSTRAINT medal_id_fkey FOREIGN KEY (medal_id)
        REFERENCES rew.medal (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS rew.medal_image
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    medal_id bigint NOT NULL,
    CONSTRAINT medal_image_pkey PRIMARY KEY (id),
    CONSTRAINT medal_id_fkey FOREIGN KEY (medal_id)
        REFERENCES rew.medal (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    INHERITS (md.base_image);;











