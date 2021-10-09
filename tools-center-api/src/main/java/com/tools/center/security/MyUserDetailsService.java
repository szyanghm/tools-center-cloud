package com.tools.center.security;

import com.tools.center.RolesService;
import com.tools.center.UserService;
import com.tools.center.common.Role;
import com.tools.center.entity.User;
import com.tools.center.utils.DateUtil;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RolesService rolesService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByName(username);
        if(user==null){
            return null;
        }
        log.info("user=={}",user);
        Set<GrantedAuthority> authority = grantedAuthorities(user);

        return new LoginUser(
                DateUtil.getCurrentTime(),
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getMobile(),
                user.getAddr(),
                user.getPassword(),
                authority
                );
    }

    //取得用户的权限
    private Set<GrantedAuthority> grantedAuthorities(User user) {
        Set<GrantedAuthority> authSet = new HashSet<>();
        List<Role> list = rolesService.findList(user.getId());
        list.forEach(e -> {
            String role = "ROLE_" + e.getCode();
            authSet.add(new SimpleGrantedAuthority(role));
        });
        return authSet;
    }

}
