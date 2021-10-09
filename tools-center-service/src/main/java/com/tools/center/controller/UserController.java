package com.tools.center.controller;


import com.tools.center.entity.User;
import com.tools.center.service.impl.UserService;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-06-17
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/v1/add")
    public ResultVO test(@RequestBody User tcUser){
        tcUser.setAddr(getIpAddr());
        tcUser.setLoginTime(LocalDateTime.now());
        tcUser.setAccountNonExpired(false);
        tcUser.setAccountNonLocked(false);
        tcUser.setEnabled(true);
        tcUser.setCredentialsNonExpired(false);
        return userService.add(tcUser);
    }

    @PostMapping(value = "/v1/findList")
    public ResultVO findUserList(){
        List<User> userList = userService.findList();
        return ResultVO.success(userList);
    }

    @GetMapping(value = "/v1/getUserById")
    public User getUserById(@RequestParam String id){
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping(value = "/v1/getUserByName")
    public User getUserByName(@RequestParam String userName){
        User user = userService.getUserByName(userName);
        return user;
    }

    @GetMapping(value = "/findAssignee")
    public Map<String, Object> findAssignee() {
        return userService.findAssignee();
    }

}
