package com.tools.center;

import com.tools.center.dto.FlowDto;
import com.tools.center.vo.FlowVO;
import com.tools.center.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tools-center-service")
@RequestMapping(value = "/flow")
public interface FlowService {

    /**
     * 新增流程定义
     * @param dto
     * @return
     */
    @PostMapping(value = "/saveFlow")
    int saveOrUpdateFlow(@RequestBody FlowDto dto);

    /**
     * 更新流程定义
     * @param dto
     * @return
     */
    @PostMapping(value = "/updateByModelId",produces = {"application/json;charset=UTF-8"})
    ResultVO updateByModelId(@RequestBody FlowDto dto);

    /**
     * 根据模型id查找key
     * @param id
     * @return
     */
    @GetMapping(value = "/findKey",produces = {"application/json;charset=UTF-8"})
    FlowVO findKey(@RequestParam String id);

    /**
     * 删除流程定义
     * @param dto
     * @return
     */
    @PostMapping(value = "/delete",produces = {"application/json;charset=UTF-8"})
    ResultVO delete(@RequestBody FlowDto dto);

    /**
     * 查询所有流程
     * @return
     */
    @PostMapping(value = "/findList",produces = {"application/json;charset=UTF-8"})
    ResultVO findList(@RequestParam(value = "name") String name);
    /**
     * 查询单个流程
     * @param id
     * @return
     */
    @GetMapping(value = "/getByIdFlow",produces = {"application/json;charset=UTF-8"})
    FlowVO getByIdFlow(@RequestParam String id);
    /**
     * 更新部署状态
     * @param dto
     * @return
     */
    @PostMapping(value = "/updateState",produces = {"application/json;charset=UTF-8"})
    int updateState(@RequestBody FlowDto dto);
}
