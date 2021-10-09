package com.tools.center.controller;

import com.tools.center.FlowService;
import com.tools.center.dto.FlowDto;
import com.tools.center.service.impl.ActFlowCommService;
import com.tools.center.vo.FlowVO;
import com.tools.center.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zjialin<br>
 * @version 1.0<br>
 * @createDate 2019/08/30 17:34 <br>
 * @Description <p> 部署流程、删除流程 </p>
 */

@RestController
@Api(tags = "部署流程、删除流程")
@Slf4j
@RequestMapping(value = "/flow")
public class DeployController extends BaseController {

    @Autowired
    private FlowService flowService;

    @Autowired
    private ActFlowCommService actFlowCommService;
    /**
     * 查询所有流程模型
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/v1/findList",produces = {"application/json;charset=UTF-8"})
    public ResultVO findAllFlow(@RequestParam(value = "name") String name){
        return flowService.findList(name);
    }

    /**
     * 查询所有流程定义
     * @return
     */
    @PostMapping(value = "/v1/findAll",produces = {"application/json;charset=UTF-8"})
    public ResultVO findList(@RequestParam(value = "name") String name){
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        return ResultVO.success(list);
    }
    /**
     * 部署流程
     * @param
     * @return 0-部署失败  1- 部署成功  2- 已经部署过
     */
    @PostMapping(path = "/v1/deploy")
    @ApiOperation(value = "根据modelId部署流程", notes = "根据modelId部署流程")
    public ResultVO deploy(@RequestBody FlowDto dto) {
        return actFlowCommService.deploy(dto);
    }

    /**
     * 删除流程定义
     * @param dto
     * @return
     */
    @PostMapping(path = "/v1/delete")
    @ApiOperation(value = "根据部署ID删除流程", notes = "根据部署ID删除流程")
    public ResultVO deleteProcess(@RequestBody FlowDto dto) {
        FlowVO flowVO = flowService.getByIdFlow(dto.getId());
        if(!flowVO.isState()){
            repositoryService.deleteModel(flowVO.getModelId());
            flowService.delete(dto);
        }
        return ResultVO.success();
    }
}