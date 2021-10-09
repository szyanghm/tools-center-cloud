package com.tools.center.controller;

import com.tools.center.utils.RedisUtil;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping(value = "/getUser",produces = {"application/json;charset=UTF-8"})
    public ResultVO getUserName(){
        return ResultVO.success();
    }
}
