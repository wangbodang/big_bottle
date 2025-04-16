-- 用户表
CREATE TABLE sys_users (
                       id BIGINT PRIMARY KEY,
                       username VARCHAR(200) UNIQUE NOT NULL,
                       password VARCHAR(500) NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE,

                       is_delete BOOLEAN DEFAULT FALSE,
                       remark VARCHAR(255),
                       create_id BIGINT,
                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       update_id BIGINT,
                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE sys_roles (
                       id BIGINT PRIMARY KEY,
                       role_name VARCHAR(200) NOT NULL,
                       role_code VARCHAR(500) NOT NULL UNIQUE,

                       is_delete BOOLEAN DEFAULT FALSE,
                       remark VARCHAR(255),
                       create_id BIGINT,
                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       update_id BIGINT,
                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户-角色中间表（多对多）
CREATE TABLE sys_user_roles (
                            id BIGINT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,

                            is_delete BOOLEAN DEFAULT FALSE,
                            remark VARCHAR(255),
                            create_id BIGINT,
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_id BIGINT,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            UNIQUE (user_id, role_id)
);
CREATE TABLE sys_resources (
                           id BIGINT PRIMARY KEY,
                           resource_name VARCHAR(200) NOT NULL,         -- 资源名称
                           resource_code VARCHAR(200) NOT NULL UNIQUE,  -- 权限标识，如 sys:user:view
                           resource_type VARCHAR(100),                   -- 类型：menu, button, api
                           url VARCHAR(500),                            -- 接口路径（可选）

                           is_delete BOOLEAN DEFAULT FALSE,
                           remark VARCHAR(255),
                           create_id BIGINT,
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           update_id BIGINT,
                           update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE sys_role_resources (
                                id BIGINT PRIMARY KEY,
                                role_id BIGINT NOT NULL,
                                resource_id BIGINT NOT NULL,

                                is_delete BOOLEAN DEFAULT FALSE,
                                remark VARCHAR(255),
                                create_id BIGINT,
                                create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                update_id BIGINT,
                                update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                UNIQUE (role_id, resource_id)
);

-- =====================================
-- 插入一个用户（密码是 "123456" 的 BCrypt 加密结果）
INSERT INTO sys_users (id, username, password, enabled, is_delete, create_time)
VALUES (1001, 'admin', '$2a$10$OqzUKRAcGKRH2ZYuSp0pCuH1CPe9k2jKq6QmL39gWXp2zLym9fVdq', TRUE, FALSE, NOW());


INSERT INTO sys_roles (id, role_name, role_code, is_delete, create_time)
VALUES (2001, '系统管理员', 'ROLE_ADMIN', FALSE, NOW());

INSERT INTO sys_resources (id, resource_name, resource_code, resource_type, url, is_delete, create_time)
VALUES
    (3001, '用户查询', 'sys:user:view', 'api', '/user/list', FALSE, NOW());


INSERT INTO sys_user_roles (id, user_id, role_id, is_delete, create_time)
VALUES (4001, 1001, 2001, FALSE, NOW());


INSERT INTO sys_role_resources (id, role_id, resource_id, is_delete, create_time)
VALUES (5001, 2001, 3001, FALSE, NOW());
