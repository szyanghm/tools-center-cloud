package com.tools.center;

import com.tools.center.vo.ApproverVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "tools-center-service")
@RequestMapping(value = "/approver")
public interface ApproverService {

    @GetMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    List<ApproverVO> findList(@RequestParam("name") String name);
}
