//package com.tools.center.config;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Slf4j
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public UserDetailsService myUserDetailsService(){
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        //这里添加用户
//        String[][] usersGroupsAndRoles = {
//                {"user1", "password", "ROLE_ACTIVITI_USER","GROUP_ACTIVITI_TEAM"},//用户，密码，角色权限，权限所属组
//                {"user2", "password", "ROLE_ACTIVITI_ADMIN","GROUP_ACTIVITI_ADMIN"},
//        };
//        for (String[] user : usersGroupsAndRoles) {
//            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
//            log.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]");
//            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
//                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
//        }
//        return inMemoryUserDetailsManager;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//}
