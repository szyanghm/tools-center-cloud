package com.tools.center.provider;

import com.tools.center.common.UserInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LocalProvider {

    private static ContextProvider contextProvider;
    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    public static void init(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo){
        contextProvider = new ContextProvider(request,response);
        if(userInfo!=null){
            log.info("当前登录用户id:{}",userInfo.getUserId());
            USER_INFO.set(userInfo);
        }
    }

    public static UserInfo getUser(){
        return USER_INFO.get();
    }

    public static void destroy(){
        USER_INFO.remove();
    }

    public static HttpServletRequest getRequest(){
        return contextProvider.getRequest();
    }
    public static HttpServletResponse getResponse(){
        return contextProvider.getResponse();
    }

    public static ServletContext getServletContext(){
        return getRequest().getServletContext();
    }
}
