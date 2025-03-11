CREATE TABLE vefuture.employee
(
    id bigserial NOT NULL,
    name character varying(200),
    age smallint,
    gender character(1) NOT NULL,
    hiredate time with time zone,
    salary numeric(18, 4),
    create_id bigint,
    create_time time with time zone,
    update_id bigint,
    update_time time with time zone,
    PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS vefuture.employee
    OWNER to postgres;

COMMENT ON TABLE vefuture.employee
    IS '测试表employee';