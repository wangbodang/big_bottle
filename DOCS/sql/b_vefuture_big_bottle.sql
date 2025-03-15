create table vefuture.b_vefuture_big_bottle (
                                                id bigserial primary key not null, -- 主键长整型自增
                                                wallet_address character varying(100) not null, -- 钱包地址
                                                img_url text not null, -- 图片地址
                                                retinfo_is_availd boolean, -- 判定结果是否可用
                                                retinfo_drink_name text, -- 饮料名称
                                                retinfo_drink_capacity integer, -- 饮料容量
                                                retinfo_drink_amout integer, -- 饮料数量
                                                retinfo_receipt_time timestamp(0) without time zone, -- 小票打印时间
                                                remark text, -- 备注
                                                is_delete character varying(1) default 0,
                                                create_id bigint, -- 创建人ID
                                                create_time timestamp(0) without time zone, -- 创建时间
                                                update_id bigint, -- 修改人ID
                                                update_time timestamp(0) without time zone
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

