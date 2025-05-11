create table vefuture.b_vef_black_list
(
    id                     bigserial primary key,

    wallet_address         varchar(100) not null,
    black_type             int default 0,

    remark                 text,
    is_delete              boolean default false,
    create_id              bigint,
    create_time            timestamp with time zone,
    update_id              bigint,
    update_time            timestamp with time zone

);

comment on table vefuture.b_vef_black_list is 'vefuture 黑名单列表';

comment on column vefuture.b_vef_black_list.id is '主键长整型自增';

comment on column vefuture.b_vef_black_list.wallet_address is '钱包地址';

comment on column vefuture.b_vef_black_list.black_type is '黑名单类型 0-默认值目前无意义 1-本系统自己拦黑 3-外部导入 5-存疑';

comment on column vefuture.b_vef_black_list.remark is '备注';

comment on column vefuture.b_vef_black_list.create_id is '创建人ID';

comment on column vefuture.b_vef_black_list.create_time is '创建时间';

comment on column vefuture.b_vef_black_list.update_id is '修改人ID';

comment on column vefuture.b_vef_black_list.update_time is '修改时间';







