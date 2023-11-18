CREATE SCHEMA IF NOT EXISTS env;;

CREATE TABLE IF NOT EXISTS env.user_event
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    event_date timestamp without time zone NOT NULL DEFAULT now(),
    user_id bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_env_pkey PRIMARY KEY (id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users.user_data (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS env.dept_event
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    event_date timestamp without time zone NOT NULL DEFAULT now(),
    dept_id bigint,
    name text COLLATE pg_catalog."default",
    CONSTRAINT dept_env_pkey PRIMARY KEY (id),
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;