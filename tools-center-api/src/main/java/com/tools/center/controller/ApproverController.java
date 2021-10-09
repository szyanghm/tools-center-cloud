package com.tools.center.controller;


import com.tools.center.ApproverService;
import com.tools.center.vo.ApproverVO;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    public ResultVO findList(@RequestBody Map<String,String> map) {
        List<ApproverVO> list = approverService.findList(map.get("name"));
        return ResultVO.success(list);
    }
}
