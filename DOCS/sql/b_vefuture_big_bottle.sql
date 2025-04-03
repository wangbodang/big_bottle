create table vefuture.b_vefuture_big_bottle
(
    id                     bigserial
        primary key,
    wallet_address         varchar(100) not null,
    img_url                text         not null,
    retinfo_is_availd      boolean,
    retinfo_drink_name     text,
    retinfo_drink_capacity integer,
    retinfo_drink_amout    integer,
    retinfo_receipt_time   timestamp(0),
    is_time_threshold      boolean,
    remark                 text,
    is_delete              varchar(1) default 0,
    create_id              bigint,
    create_time            timestamp(0),
    update_id              bigint,
    update_time            timestamp(0)

);

comment on table vefuture.b_vefuture_big_bottle is 'vefuture bigbottle 基本信息表';

comment on column vefuture.b_vefuture_big_bottle.id is '主键长整型自增';

comment on column vefuture.b_vefuture_big_bottle.wallet_address is '钱包地址';

comment on column vefuture.b_vefuture_big_bottle.img_url is '图片地址';

comment on column vefuture.b_vefuture_big_bottle.retinfo_is_availd is '判定结果是否可用';

comment on column vefuture.b_vefuture_big_bottle.retinfo_drink_name is '饮料名称';

comment on column vefuture.b_vefuture_big_bottle.retinfo_drink_capacity is '饮料容量';

comment on column vefuture.b_vefuture_big_bottle.retinfo_drink_amout is '饮料数量';

comment on column vefuture.b_vefuture_big_bottle.retinfo_receipt_time is '小票打印时间';

comment on column vefuture.b_vefuture_big_bottle.remark is '备注';

comment on column vefuture.b_vefuture_big_bottle.create_id is '创建人ID';

comment on column vefuture.b_vefuture_big_bottle.create_time is '创建时间';

comment on column vefuture.b_vefuture_big_bottle.update_id is '修改人ID';

comment on column vefuture.b_vefuture_big_bottle.is_time_threshold is '布尔值，用来检验小票时间和上传到系统时间的差值（比如我们规定小票时间是本周内的，就需要做这个判断）';

alter table vefuture.b_vefuture_big_bottle
    owner to postgres;

create index index_wallet_address
    on vefuture.b_vefuture_big_bottle (wallet_address);

