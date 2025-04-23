-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
DROP TABLE IF EXISTS sys_oper_log;
CREATE TABLE sys_oper_log (
                              id           bigserial             PRIMARY KEY,
                              title             varchar(50)   NOT NULL DEFAULT ''        ,
                              business_type     smallint      NOT NULL DEFAULT 0         ,
                              method            varchar(200)  NOT NULL DEFAULT ''       ,
                              request_method    varchar(10)   NOT NULL DEFAULT ''       ,
                              operator_type     smallint      NOT NULL DEFAULT 0         ,
                              oper_name         varchar(50)   NOT NULL DEFAULT ''       ,
                              dept_name         varchar(50)   NOT NULL DEFAULT ''       ,
                              oper_url          varchar(255)  NOT NULL DEFAULT ''       ,
                              oper_ip           varchar(128)  NOT NULL DEFAULT ''       ,
                              oper_location     varchar(255)  NOT NULL DEFAULT ''       ,
                              oper_param        varchar(2000) NOT NULL DEFAULT ''       ,
                              json_result       varchar(2000) NOT NULL DEFAULT ''       ,
                              status            smallint      NOT NULL DEFAULT 0         ,
                              error_msg         varchar(2000) NOT NULL DEFAULT ''       ,
                              oper_time         timestamp without time zone            ,
                              cost_time         bigint        NOT NULL DEFAULT 0
);

-- 索引
CREATE INDEX idx_sys_oper_log_bt ON sys_oper_log(business_type);
CREATE INDEX idx_sys_oper_log_s  ON sys_oper_log(status);
CREATE INDEX idx_sys_oper_log_ot ON sys_oper_log(oper_time);

-- 注释
COMMENT ON TABLE sys_oper_log IS '操作日志记录';
COMMENT ON COLUMN sys_oper_log.id        IS '日志主键';
COMMENT ON COLUMN sys_oper_log.title          IS '模块标题';
COMMENT ON COLUMN sys_oper_log.business_type  IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT ON COLUMN sys_oper_log.method         IS '方法名称';
COMMENT ON COLUMN sys_oper_log.request_method IS '请求方式';
COMMENT ON COLUMN sys_oper_log.operator_type  IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT ON COLUMN sys_oper_log.oper_name      IS '操作人员';
COMMENT ON COLUMN sys_oper_log.dept_name      IS '部门名称';
COMMENT ON COLUMN sys_oper_log.oper_url       IS '请求URL';
COMMENT ON COLUMN sys_oper_log.oper_ip        IS '主机地址';
COMMENT ON COLUMN sys_oper_log.oper_location  IS '操作地点';
COMMENT ON COLUMN sys_oper_log.oper_param     IS '请求参数';
COMMENT ON COLUMN sys_oper_log.json_result    IS '返回参数';
COMMENT ON COLUMN sys_oper_log.status         IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN sys_oper_log.error_msg      IS '错误消息';
COMMENT ON COLUMN sys_oper_log.oper_time      IS '操作时间';
COMMENT ON COLUMN sys_oper_log.cost_time      IS '消耗时间';


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
DROP TABLE IF EXISTS sys_login_info;
CREATE TABLE sys_login_info (
                                id        bigserial             PRIMARY KEY,
                                user_name      varchar(50)   NOT NULL DEFAULT ''   ,
                                ipaddr         varchar(128)  NOT NULL DEFAULT ''   ,
                                login_location varchar(255)  NOT NULL DEFAULT ''   ,
                                browser        varchar(50)   NOT NULL DEFAULT ''   ,
                                os             varchar(50)   NOT NULL DEFAULT ''   ,
                                status         char(1)       NOT NULL DEFAULT '0'  ,
                                msg            varchar(255)  NOT NULL DEFAULT ''   ,
                                login_time     timestamp without time zone
);

-- 索引
CREATE INDEX idx_sys_login_info_s  ON sys_login_info(status);
CREATE INDEX idx_sys_login_info_lt ON sys_login_info(login_time);

-- 注释
COMMENT ON TABLE sys_login_info IS '系统访问记录';
COMMENT ON COLUMN sys_login_info.id        IS '访问ID';
COMMENT ON COLUMN sys_login_info.user_name      IS '用户账号';
COMMENT ON COLUMN sys_login_info.ipaddr         IS '登录IP地址';
COMMENT ON COLUMN sys_login_info.login_location IS '登录地点';
COMMENT ON COLUMN sys_login_info.browser        IS '浏览器类型';
COMMENT ON COLUMN sys_login_info.os             IS '操作系统';
COMMENT ON COLUMN sys_login_info.status         IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN sys_login_info.msg            IS '提示消息';
COMMENT ON COLUMN sys_login_info.login_time     IS '访问时间';
