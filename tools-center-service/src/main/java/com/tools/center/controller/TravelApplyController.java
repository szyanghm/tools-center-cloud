package com.tools.center.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tools.center.common.UserInfo;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.entity.TravelApply;
import com.tools.center.enums.SystemEnum;
import com.tools.center.service.impl.TravelApplyService;
import com.tools.center.utils.DateUtil;
import com.tools.center.utils.RedisUtil;
import com.tools.center.vo.ResultVO;
import com.tools.center.vo.TravelApplyVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/beAway/apply")
public class TravelApplyController extends BaseController{

    @Autowired
    private TravelApplyService travelApplyService;

    @PostMapping(value = "/add",produces = {"application/json;charset=UTF-8"})
    public ResultVO add(@RequestBody TravelApplyDto dto){
        if(ObjectUtil.isNotNull(dto)){
            TravelApply ba = new TravelApply();
            BeanUtils.copyProperties(dto,ba);
            ba.setStartAddress(JSONObject.toJSONString(dto.getStartAddress()));
            ba.setEndAddress(JSONObject.toJSONString(dto.getEndAddress()));
            travelApplyService.saveOrUpdate(ba);
        }
        return ResultVO.success();
    }

    @PostMapping(value = "/updateStateById",produces = {"application/json;charset=UTF-8"})
    public ResultVO updateStateById(@RequestBody TravelApplyDto dto){
        return travelApplyService.updateStateById(dto);
    }

    @GetMapping(value = "/updateState",produces = {"application/json;charset=UTF-8"})
    public ResultVO updateState(@RequestParam("processId") String processId,@RequestParam("state")int state){
        return travelApplyService.updateState(processId,state);
    }

    @GetMapping(value = "/getById",produces = {"application/json;charset=UTF-8"})
    public ResultVO<TravelApplyVO> getById(@RequestParam("id") String id){
        if(StringUtils.isNotBlank(id)){
            TravelApply ta = travelApplyService.getById(id);
            TravelApplyVO vo = new TravelApplyVO();
            if(ta!=null){
                BeanUtils.copyProperties(ta,vo);
                vo.setCreatedTime(DateUtil.formatterLocalDateTime(ta.getCreatedTime(),DateUtil.DEFAULT_DATE_PATTERN));
                vo.setUsername(getUserName(ta.getUserId()));
//                List<String> startList = JSONObject.parseArray(ta.getStartAddress(), String.class);
//                List<String> endList = JSONObject.parseArray(ta.getEndAddress(), String.class);
//                vo.setStartAddress(startList);
//                vo.setEndAddress(endList);
                String userName = getNickName(ta.getApprover());
                vo.setApproverName(userName);
            }
            return new ResultVO<>(SystemEnum.OK,vo);
        }
        return ResultVO.fail();
    }

    @PostMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    public IPage<TravelApplyVO> findList(@RequestBody TravelApplyDto dto){
        if(ObjectUtil.isNotNull(dto)){
            IPage<TravelApplyVO> list  = travelApplyService.findPageList(dto.getUserId(), dto.getPageDto());
            return list;
        }
        return null;
    }
}
