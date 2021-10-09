package com.tools.center.controller;

import com.alibaba.fastjson.JSONObject;
import com.tools.center.Test;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @PostMapping("/a01")
    public ResultVO test(@RequestBody Test test){
        log.info("测试打印id:{},name:{}",test.getId(),test.getName());
        return ResultVO.success(test);
    }

    @PostMapping("/cityArr")
    public ResultVO arr(@RequestBody List<Test> list){
        System.out.println(JSONObject.toJSONString(list.get(0)));
        return ResultVO.success();
    }

}
