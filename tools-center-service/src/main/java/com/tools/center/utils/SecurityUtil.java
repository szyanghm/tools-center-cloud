//package com.tools.center.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.context.SecurityContextImpl;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//
//@Slf4j
//@Component
//public class SecurityUtil {
//
//    @Autowired
//    @Qualifier("myUserDetailsService")
//    private UserDetailsService userDetailsService;
//
//    public void logInAs(String username){
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if(userDetails==null){
//            throw new IllegalStateException("user"+username+"用户不存在");
//        }
//        log.info("> Loggerd in as:"+username);
//        SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return userDetails.getAuthorities();
//            }
//
//            @Override
//            public Object getCredentials() {
//                return userDetails.getPassword();
//            }
//
//            @Override
//            public Object getDetails() {
//                return userDetails;
//            }
//
//            @Override
//            public Object getPrincipal() {
//                return userDetails;
//            }
//
//            @Override
//            public boolean isAuthenticated() {
//                return true;
//            }
//
//            @Override
//            public void setAuthenticated(boolean b) throws IllegalArgumentException {
//
//            }
//
//            @Override
//            public String getName() {
//                return userDetails.getUsername();
//            }
//        }));
//        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
//    }
//
//}
