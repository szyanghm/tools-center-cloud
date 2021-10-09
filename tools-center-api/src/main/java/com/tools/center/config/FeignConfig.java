package com.tools.center.config;

import cn.hutool.crypto.SecureUtil;
import com.tools.center.common.UserInfo;
import com.tools.center.security.LoginUser;
import com.tools.center.utils.AESUtil;
import com.tools.center.utils.RedisUtil;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class FeignConfig {

    @Autowired
    private RedisUtil redisUtil;

    @Bean
    public RequestInterceptor headerInterceptor(){
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes ==null){
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(request.getSession()!=null&&authentication!=null){
                Object principal = authentication.getPrincipal();
                UserInfo userInfo = new UserInfo();
                if(principal instanceof UserDetails){
                    LoginUser loginUser = (LoginUser) principal;
                    Set<String> set = getRole(loginUser);
                    userInfo.setAvatar("https://yhm-1259466197.cos.ap-shenzhen-fsi.myqcloud.com/billowing/me.png");
                    userInfo.setNickname(loginUser.getNickname());
                    userInfo.setUsername(loginUser.getUsername());
                    userInfo.setUserId(loginUser.getUserId());
                    userInfo.setRoles(set);
                }
                String token = AESUtil.encrypt(userInfo.getUserId(),AESUtil.password);
                redisUtil.set(userInfo.getUserId(),userInfo);
                requestTemplate.header("token",token);
            }
        };
    }

    protected Set<String> getRole(LoginUser loginUser){
        Set<String> setRole = new HashSet<>();
        Set<GrantedAuthority> set = (Set<GrantedAuthority>)loginUser.getAuthorities();
        for (GrantedAuthority authority:set){
            String role = authority.getAuthority();
            role = role.replace("ROLE_","").trim();
            setRole.add(role);
        }
        return setRole;
    }

}
