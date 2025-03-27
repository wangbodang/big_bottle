package com.vefuture.big_bottle.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mybatis-plus自动填充数据插件
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        //this.strictInsertFill(metaObject, "createUser", String.class, getCurrentUser());
        //this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        //this.strictInsertFill(metaObject, "updateUser", String.class, getCurrentUser());
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        //this.strictUpdateFill(metaObject, "updateUser", String.class, getCurrentUser());
        // 更新操作的填充逻辑
    }

    private Long getCurrentUser() {
        // 获取当前用户的逻辑，例如从安全上下文中获取
        return 10000L;
    }
}
