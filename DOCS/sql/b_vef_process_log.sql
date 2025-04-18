create table vefuture.b_vef_process_log
(
    id                     bigserial primary key,
    process_id             char(36),
    wallet_address         varchar(100) not null,
    img_url                text         not null,
    llm_type               varchar(100),
    llm_start_time         timestamp(0) WITH TIME ZONE,
    llm_end_time           timestamp(0) WITH TIME ZONE,
    llm_return_msg         text,

    remark                 text,
    is_delete              boolean default false,
    create_id              bigint,
    create_time            timestamp(0) WITH TIME ZONE default now(),
    update_id              bigint,
    update_time            timestamp(0) WITH TIME ZONE default now()

);

comment on table vefuture.b_vef_process_log is 'vefuture bigbottle 基本信息表';

comment on column vefuture.b_vef_process_log.id is '主键长整型自增';

comment on column vefuture.b_vef_process_log.wallet_address is '钱包地址';

comment on column vefuture.b_vef_process_log.img_url is '图片地址';



comment on column vefuture.b_vef_process_log.remark is '备注';

comment on column vefuture.b_vef_process_log.create_id is '创建人ID';

comment on column vefuture.b_vef_process_log.create_time is '创建时间';

comment on column vefuture.b_vef_process_log.update_id is '修改人ID';

comment on column vefuture.b_vef_process_log.is_time_threshold is '布尔值，用来检验小票时间和上传到系统时间的差值（比如我们规定小票时间是本周内的，就需要做这个判断）';

alter table vefuture.b_vef_process_log
    owner to postgres;

create index index_wallet_address
    on vefuture.b_vef_process_log (process_id);