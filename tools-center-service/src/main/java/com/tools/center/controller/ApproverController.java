package com.tools.center.controller;


import com.tools.center.service.impl.ApproverService;
import com.tools.center.vo.ApproverVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 审批人表 前端控制器
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/approver")
public class ApproverController extends BaseController {

    @Autowired
    private ApproverService approverService;

    @GetMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    public List<ApproverVO> findList(@RequestParam("name") String name) {
        return approverService.findList(name);
    }
}
