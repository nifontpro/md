CREATE SCHEMA IF NOT EXISTS shop;;

CREATE TABLE IF NOT EXISTS shop.product
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    dept_id bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    price integer NOT NULL,
    main_img text COLLATE pg_catalog."default",
    norm_img text COLLATE pg_catalog."default",
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT dept_id_fkey FOREIGN KEY (dept_id)
        REFERENCES dep.dept (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);;

CREATE TABLE IF NOT EXISTS shop.product_details
(
    product_id bigint NOT NULL,
    description text COLLATE pg_catalog."default",
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    site_url text COLLATE pg_catalog."default",
    CONSTRAINT product_details_pkey PRIMARY KEY (product_id),
    CONSTRAINT product_id_fkey FOREIGN KEY (product_id)
        REFERENCES shop.product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);;

CREATE TABLE IF NOT EXISTS shop.product_image
(
    -- Inherited from table md.base_image: image_url text COLLATE pg_catalog."default",
    -- Inherited from table md.base_image: image_key text COLLATE pg_catalog."default",
    -- Inherited from table md.base_image: type_code text COLLATE pg_catalog."default" NOT NULL DEFAULT 'N'::text,
    -- Inherited from table md.base_image: main boolean NOT NULL DEFAULT false,
    -- Inherited from table md.base_image: created_at timestamp without time zone NOT NULL DEFAULT now(),
    -- Inherited from table md.base_image: mini_url text COLLATE pg_catalog."default",
    -- Inherited from table md.base_image: mini_key text COLLATE pg_catalog."default",
    -- Inherited from table md.base_image: origin_url text COLLATE pg_catalog."default",
    -- Inherited from table md.base_image: origin_key text COLLATE pg_catalog."default",
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    product_id bigint NOT NULL,
    CONSTRAINT product_image_pkey PRIMARY KEY (id),
    CONSTRAINT product_id_fkey FOREIGN KEY (product_id)
        REFERENCES shop.product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    INHERITS (md.base_image);;