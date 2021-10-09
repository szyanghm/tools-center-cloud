package com.tools.center.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class,LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject,"updatedTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"createdBy", String.class,"1");
        this.strictInsertFill(metaObject,"updatedBy", String.class,"1");
        this.strictInsertFill(metaObject,"deleted", String.class,"N");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject,"createdTime", LocalDateTime.class,LocalDateTime.now());
        this.strictUpdateFill(metaObject,"updatedTime", LocalDateTime.class,LocalDateTime.now());
        this.strictUpdateFill(metaObject,"createdBy", String.class,"1");
        this.strictUpdateFill(metaObject,"updatedBy", String.class,"1");
    }

}
