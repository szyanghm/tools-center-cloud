package com.tools.center.enums;

import com.tools.center.api.ErrorType;
import lombok.Getter;

@Getter
public enum SystemEnum implements ErrorType {

    OK(2000, "处理成功"),

    SYSTEM_ERROR(-1, "系统异常"),
    SYSTEM_BUSY(100001, "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE(110404, "服务未找到"),
    GATEWAY_ERROR(110500, "网关异常"),
    GATEWAY_CONNECT_TIME_OUT(110002, "网关超时"),

    ARGUMENT_NOT_VALID(120000, "请求参数校验不通过"),
    INVALID_TOKEN(120001, "无效token"),

    DUPLICATE_PRIMARY_KEY(130000,"唯一键冲突"),

    USER_CHECK_FAILED(600001,"user check failed"),

    USER_ALREADY_EXISTS(600002,"用户已存在"),

    USER_LOCK_ING(600003,"user is lock...."),

    USER_EXISTS(300001,"user Already exists"),

    NETTY_PING_MESSAGE(100015,"netty ping message"),

    RECEIVE_PONG_MESSAGE_ADDRESS(100016,"receive pong message, address,获取Channel的远程IP地址"),

    UPLOAD_FILE_SIZE_LIMIT(100060, "上传文件大小超过限制"),

    UPLOAD_NOT_FOUND(100061,"上传的文件对象不存在..."),

    FILE_TYPE_NOT(100062,"文件类型未定义不能上传..."),

    NORMAL_MESSAGE(100086,"Normal message"),

    FLOW_DEPLOY_ERROR(5001,"流程部署失败！"),
    FLOW_START_ERROR(5002,"流程启动失败！"),
    FLOW_TASK_COMPLETE_ERROR(5002,"任务完成失败！"),

    QUERY_ERROR(8001,"查询失败！"),
    DELETE_ERROR(8002,"删除失败！"),
    UPDATE_ERROR(8003,"更新失败！"),

    PASSWORD_ERROR(4003,"账号或者密码不正确！");

    /**
     * 错误类型码
     */
    private Integer code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
