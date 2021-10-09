package com.tools.center.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tools.center.common.UserInfo;
import com.tools.center.provider.LocalProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        UserInfo userInfo = LocalProvider.getUser();
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class,LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject,"updatedTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"createdBy", String.class,userInfo.getUserId());
        this.strictInsertFill(metaObject,"updatedBy", String.class,userInfo.getUserId());
        this.strictInsertFill(metaObject,"deleted", Boolean.class,false);
        log.info("end insert fill ....");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserInfo userInfo = LocalProvider.getUser();
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject,"createdTime", LocalDateTime.class,LocalDateTime.now());
        this.strictUpdateFill(metaObject,"updatedTime", LocalDateTime.class,LocalDateTime.now());
        this.strictUpdateFill(metaObject,"createdBy", String.class,userInfo.getUserId());
        this.strictUpdateFill(metaObject,"updatedBy", String.class,userInfo.getUserId());
        log.info("end update fill ....");
    }

}
