package com.tools.center;


import com.tools.center.entity.User;
import com.tools.center.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "tools-center-service")
@RequestMapping(value = "/user")
public interface UserService {

    @PostMapping("/v1/add")
    ResultVO test(@RequestBody Test test);

    @PostMapping(value = "/v1/findList")
    ResultVO findUserList();

    @GetMapping(value = "/v1/getUserById")
    User getUserById(@RequestParam String id);

    @GetMapping(value = "/v1/getUserByName")
    User getUserByName(@RequestParam String userName);

    @GetMapping(value = "/findAssignee")
    Map<String, Object> findAssignee();
}
