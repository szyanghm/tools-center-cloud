package com.tools.center.common;

public class Contents {

    public static int DEFAULT_PORT = 9688;
    public static String WEBSOCKET_URL="ws://localhost:8099/websocket";

    public static final String PING_CODE = "100015";//netty ping message
    public static final String PONG_CODE = "100016";//receive pong message, address,获取Channel的远程IP地址

    public static final String JOIN_CODE = "100020";//加入房间 message
    public static final String MESS_CODE = "100086";//rmal message

    public static final String AUTH_MSG = "用户帐号已被锁定";


    /**
     * 系统消息类型
     */
    public static final int SYS_USER_COUNT = 20001; // 在线用户数
    public static final int SYS_AUTH_STATE = 20002; // 认证结果
    public static final int SYS_OTHER_INFO = 20003; // 系统消息
}
