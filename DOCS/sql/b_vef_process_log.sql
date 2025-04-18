-- Table: vefuture.b_vef_process_log

-- DROP TABLE IF EXISTS vefuture.b_vef_process_log;

CREATE TABLE IF NOT EXISTS vefuture.b_vef_process_log
(
    id bigserial primary key,
    process_id character(36) COLLATE pg_catalog."default",
    wallet_address character varying(100) COLLATE pg_catalog."default" NOT NULL,
    img_url text COLLATE pg_catalog."default" NOT NULL,
    ai_type character varying(100) COLLATE pg_catalog."default",
    ai_process_start_time timestamp(0) with time zone,
    ai_process_end_time timestamp(0) with time zone,
    ai_return_msg text COLLATE pg_catalog."default",
    remark text COLLATE pg_catalog."default",
    is_delete boolean DEFAULT false,
    create_id bigint,
    create_time timestamp(0) with time zone DEFAULT now(),
    update_id bigint,
    update_time timestamp(0) with time zone DEFAULT now()
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS vefuture.b_vef_process_log
    OWNER to postgres;

COMMENT ON COLUMN vefuture.b_vef_process_log.id
    IS '主键, 序列自增';

COMMENT ON COLUMN vefuture.b_vef_process_log.process_id
    IS '流程ID，全流程使用';

COMMENT ON COLUMN vefuture.b_vef_process_log.wallet_address
    IS '钱包地址';

COMMENT ON COLUMN vefuture.b_vef_process_log.img_url
    IS '图片URL';

COMMENT ON COLUMN vefuture.b_vef_process_log.ai_type
    IS 'AI模型类型';

COMMENT ON COLUMN vefuture.b_vef_process_log.ai_process_start_time
    IS 'AI流程 开始时间';

COMMENT ON COLUMN vefuture.b_vef_process_log.ai_process_end_time
    IS 'AI流程结束时间';

COMMENT ON COLUMN vefuture.b_vef_process_log.ai_return_msg
    IS 'AI模型返回信息';

COMMENT ON COLUMN vefuture.b_vef_process_log.remark
    IS '备注';

COMMENT ON COLUMN vefuture.b_vef_process_log.is_delete
    IS '删除标志, Boolean类型';

COMMENT ON COLUMN vefuture.b_vef_process_log.create_id
    IS '创建人ID';

COMMENT ON COLUMN vefuture.b_vef_process_log.create_time
    IS '创建时间';

COMMENT ON COLUMN vefuture.b_vef_process_log.update_id
    IS '修改人ID';

COMMENT ON COLUMN vefuture.b_vef_process_log.update_time
    IS '修改时间';