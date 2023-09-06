CREATE SCHEMA IF NOT EXISTS dep;
CREATE SCHEMA IF NOT EXISTS md;

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
);

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
);

CREATE TABLE IF NOT EXISTS md.base_image
(
    image_url text COLLATE pg_catalog."default",
    image_key text COLLATE pg_catalog."default",
    type_code text COLLATE pg_catalog."default",
    main boolean,
    created_at timestamp without time zone,
    mini_url text COLLATE pg_catalog."default",
    mini_key text COLLATE pg_catalog."default"
);

CREATE TABLE IF NOT EXISTS dep.dept_image
(
    id bigserial NOT NULL primary key,
    dept_id bigint NOT NULL,
    CONSTRAINT dept_id_fk FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    INHERITS (md.base_image);

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
) SELECT tmp.id FROM tmp
$BODY$;