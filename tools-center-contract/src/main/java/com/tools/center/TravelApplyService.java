package com.tools.center;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.entity.TravelApply;
import com.tools.center.vo.TravelApplyVO;
import com.tools.center.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tools-center-service")
@RequestMapping(value = "/beAway/apply")
public interface TravelApplyService {

    @PostMapping(value = "/add",produces = {"application/json;charset=UTF-8"})
    ResultVO add(@RequestBody TravelApplyDto dto);

    @GetMapping(value = "/getById",produces = {"application/json;charset=UTF-8"})
    ResultVO<TravelApplyVO> getById(@RequestParam("id") String id);

    @PostMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    Page<TravelApplyVO> findList(@RequestBody TravelApplyDto dto);

    @PostMapping(value = "/updateStateById",produces = {"application/json;charset=UTF-8"})
    ResultVO updateStateById(@RequestBody TravelApplyDto dto);

    @GetMapping(value = "/updateState",produces = {"application/json;charset=UTF-8"})
    ResultVO updateState(@RequestParam("processId") String processId,@RequestParam("state")int state);
}
