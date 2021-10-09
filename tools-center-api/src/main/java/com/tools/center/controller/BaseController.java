package com.tools.center.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.tools.center.common.UserInfo;
import com.tools.center.security.LoginUser;
import com.tools.center.utils.AESUtil;
import com.tools.center.utils.RedisUtil;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskAdminRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
public class BaseController {
    @Autowired
    public TaskService taskService;
    @Autowired
    public RuntimeService runtimeService;
    @Autowired
    public HistoryService historyService;
    @Autowired
    public RepositoryService repositoryService;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
     * 需要ACTIVITI_USER权限
     */
    @Autowired
    public ProcessRuntime processRuntime;

    /**
     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
     * 需要ACTIVITI_ADMIN权限
     */
    @Autowired
    public ProcessAdminRuntime processAdminRuntime;

    /**
     * 类内部调用taskService
     * 需要ACTIVITI_USER权限
     */
    @Autowired
    public TaskRuntime taskRuntime;

    @Autowired
    ProcessEngineFactoryBean processEngineFactoryBean;
    @Autowired
    public ManagementService managementService;

    /**
     * 类内部调用taskService
     * 需要ACTIVITI_ADMIN权限
     */
    @Autowired
    public TaskAdminRuntime taskAdminRuntime;

    /**
     * 获取当前登录用户信息
     * @return
     */
    public UserInfo getUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo userInfo = new UserInfo();
        if(principal instanceof UserDetails){
            LoginUser loginUser = (LoginUser) principal;
            userInfo.setUserId(loginUser.getUserId());
            userInfo.setAvatar("https://yhm-1259466197.cos.ap-shenzhen-fsi.myqcloud.com/billowing/me.png");
            userInfo.setUsername(loginUser.getUsername());
            return userInfo;
        }
        return null;
    }

    /**
     * 通过用户id获取用户名称
     * @param userId
     * @return
     */
    public String getUserName(String userId){
        UserInfo userInfo = (UserInfo)redisUtil.get(userId);
        return userInfo.getUsername();
    }


}
