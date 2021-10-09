package com.tools.center;

import com.tools.center.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "www.tools-center.eureka.com")
public interface TestService {

    @PostMapping("/test/a01")
    ResultVO test(@RequestBody Test test);
}
