package com.tools.center.controller;


import com.tools.center.dto.FlowDto;
import com.tools.center.entity.Flow;
import com.tools.center.service.impl.FlowService;
import com.tools.center.vo.FlowVO;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 流程表 前端控制器
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/flow")
public class FlowController extends BaseController {

    @Autowired
    private FlowService flowService;


    @PostMapping(value = "/saveFlow")
    public int saveFlow(@RequestBody FlowDto dto){
        Flow flow = new Flow();
        BeanUtils.copyProperties(dto,flow);
        return flowService.saveOrUpdate(flow)?1:0;
    }

    @PostMapping(value = "/delete",produces = {"application/json;charset=UTF-8"})
    public ResultVO delete(@RequestBody FlowDto dto){
        flowService.removeById(dto.getId());
        return ResultVO.success();
    }

    @PostMapping(value = "/updateByModelId",produces = {"application/json;charset=UTF-8"})
    public ResultVO updateByModelId(@RequestBody FlowDto dto) {
        flowService.updateByModelId(dto);
        return ResultVO.success();
    }

    @GetMapping(value = "/findKey",produces = {"application/json;charset=UTF-8"})
    public FlowVO findKey(@RequestParam String id) {
        return flowService.findKey(id);
    }
    /**
     * 查询所有流程
     * @return
     */
    @PostMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    public ResultVO findList(@RequestParam(value = "name") String name) {
        return ResultVO.success(flowService.findList(name));
    }

    /**
     * 查询单个流程
     * @param id
     * @return
     */
    @GetMapping(value = "/getByIdFlow",produces = {"application/json;charset=UTF-8"})
    public FlowVO getByIdFlow(@RequestParam String id) {
        return flowService.getByIdFlow(id);
    }

    /**
     * 更新部署状态
     * @param dto
     * @return
     */
    @PostMapping(value = "/updateState",produces = {"application/json;charset=UTF-8"})
    public int updateState(@RequestBody FlowDto dto){
        return flowService.updateState(dto);
    }
}
