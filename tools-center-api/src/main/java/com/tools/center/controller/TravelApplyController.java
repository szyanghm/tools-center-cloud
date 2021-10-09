package com.tools.center.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tools.center.TravelApplyService;
import com.tools.center.common.IPage;
import com.tools.center.common.UserInfo;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.entity.TravelApply;
import com.tools.center.vo.TravelApplyVO;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/beAwayApply")
public class TravelApplyController extends BaseController{

    @Autowired
    private TravelApplyService travelApplyService;

    @PostMapping(value = "/list", produces = {"application/json;charset=UTF-8"})
    public ResultVO<IPage<TravelApplyVO>> add(@RequestBody TravelApplyDto dto) {
        if (ObjectUtil.isNotNull(dto)) {
            UserInfo user = getUser();
            dto.setUserId(user.getUserId());
            Page<TravelApplyVO> vo = travelApplyService.findList(dto);
            return ResultVO.success(vo);
        }
        return ResultVO.fail();
    }

    @GetMapping(value = "/editApply", produces = {"application/json;charset=UTF-8"})
    public ResultVO editApply(@RequestParam("id") String id){
        return travelApplyService.getById(id);
    }

    public ResultVO getByProcessId(){
        return null;
    }
}
