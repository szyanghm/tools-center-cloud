package com.tools.center.controller;

import com.tools.center.common.UserInfo;
import com.tools.center.utils.HttpUtils;
import com.tools.center.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {

    @Autowired
    private RedisUtil redisUtil;

    public static String getIpAddr(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return HttpUtils.getIpAddr(request);
    }

    /**
     * 通过用户id获取用户账号
     * @param userId
     * @return
     */
    public String getUserName(String userId){
        UserInfo userInfo = (UserInfo)redisUtil.get(userId);
        return userInfo.getUsername();
    }

    /**
     * 通过用户id获取用户昵称
     * @param userId
     * @return
     */
    public String getNickName(String userId){
        UserInfo userInfo = (UserInfo)redisUtil.get(userId);
        return userInfo.getNickname();
    }

}
