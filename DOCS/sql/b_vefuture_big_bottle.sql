create table b_vefuture_big_bottle
(
    id                     bigserial
        primary key,
    process_id             char(36),
    wallet_address         varchar(100) not null,
    img_url                text         not null,
    retinfo_is_availd      boolean,
    retinfo_drink_name     text,
    retinfo_drink_capacity integer,
    retinfo_drink_amout    integer,
    retinfo_receipt_time   timestamp(0),
    is_time_threshold      boolean,
    de_plastic             numeric(18, 4),
    exif_type              integer,
    exif_device_type       integer,
    remark                 text,
    is_delete              varchar(1) default 0,
    create_id              bigint,
    create_time            timestamp(0),
    update_id              bigint,
    update_time            timestamp(0)

);

comment on table b_vefuture_big_bottle is 'vefuture bigbottle 基本信息表';

comment on column b_vefuture_big_bottle.id is '主键长整型自增';

comment on column b_vefuture_big_bottle.wallet_address is '钱包地址';

comment on column b_vefuture_big_bottle.img_url is '图片地址';

comment on column b_vefuture_big_bottle.retinfo_is_availd is '判定结果是否可用';

comment on column b_vefuture_big_bottle.retinfo_drink_name is '饮料名称';

comment on column b_vefuture_big_bottle.retinfo_drink_capacity is '饮料容量';

comment on column b_vefuture_big_bottle.retinfo_drink_amout is '饮料数量';

comment on column b_vefuture_big_bottle.retinfo_receipt_time is '小票打印时间';

comment on column b_vefuture_big_bottle.is_time_threshold is '布尔值，用来检验小票时间和上传到系统时间的差值（比如我们规定小票时间是本周内的，就需要做这个判断）';

comment on column b_vefuture_big_bottle.remark is '备注';

comment on column b_vefuture_big_bottle.create_id is '创建人ID';

comment on column b_vefuture_big_bottle.create_time is '创建时间';

comment on column b_vefuture_big_bottle.update_id is '修改人ID';

comment on column b_vefuture_big_bottle.process_id is '流程ID';

comment on column b_vefuture_big_bottle.de_plastic is '减塑量';

comment on column b_vefuture_big_bottle.exif_type is '根据Exif判断图片的类型 目前：0-AI_GENERATED, 1-CAMERA_CAPTURE,2-UNKNOWN';

comment on column b_vefuture_big_bottle.exif_device_type is '根据EXIF推测设备类型:0-AI_GENERATOR, 1-SMARTPHONE, 2-MIRRORLESS_DSLR, 3-ACTION_CAM, 4-UNKNOWN';

alter table b_vefuture_big_bottle
    owner to postgres;

create index index_wallet_address
    on b_vefuture_big_bottle (wallet_address);

