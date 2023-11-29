CREATE SCHEMA IF NOT EXISTS rew;;

CREATE TABLE IF NOT EXISTS rew.medal
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name text COLLATE pg_catalog."default" NOT NULL DEFAULT ''::text,
    dept_id bigint NOT NULL,
    score integer NOT NULL DEFAULT 1,
    main_img text COLLATE pg_catalog."default",
    norm_img text COLLATE pg_catalog."default",
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