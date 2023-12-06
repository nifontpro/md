CREATE SCHEMA IF NOT EXISTS gender;;

CREATE TABLE IF NOT EXISTS gender.male
(
    firstname text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT male_pkey PRIMARY KEY (firstname)
);;

CREATE TABLE IF NOT EXISTS gender.female
(
    firstname text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT female_pkey PRIMARY KEY (firstname)
);;

CREATE TABLE IF NOT EXISTS gender.male_lastname
(
    lastname text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT male_lastname_pkey PRIMARY KEY (lastname)
);;

CREATE OR REPLACE FUNCTION gender.get_gender(
    firstname text,
    lastname text)
    RETURNS text
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    firstname_p text := $1;
    lastname_p text := $2;

BEGIN

    IF (SELECT COUNT(*) FROM gender.male s where upper(s.firstname) like upper(firstname_p)) > 0 THEN
        RETURN 'M';
    END IF;
    IF (SELECT COUNT(*) FROM gender.female s where upper(s.firstname) like upper(firstname_p)) > 0 THEN
        RETURN 'F';
    END IF;
    IF (SELECT COUNT(*) FROM gender.male_lastname s where upper(s.lastname) like upper(lastname_p)) > 0 THEN
        RETURN 'M';
    END IF;

    RETURN null;

END;
$BODY$;;
