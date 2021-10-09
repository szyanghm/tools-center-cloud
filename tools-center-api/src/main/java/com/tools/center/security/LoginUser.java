package com.tools.center.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class LoginUser extends User {

    private String loginTime;//登录时间
    private String userId;   //用户id
    private String username; //账号
    private String nickname;     //用户名称
    private String phone;    //电话
    private String addr;     //ip地址

    public LoginUser(String loginTime, String userId, String username,String nickname, String phone, String addr, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginTime = loginTime;
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.addr = addr;
        this.phone = phone;
    }
}
