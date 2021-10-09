package com.tools.center.controller;

import com.tools.center.TravelApplyService;
import com.tools.center.common.UserInfo;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.enums.SystemEnum;
import com.tools.center.service.impl.ActFlowCommService;
import com.tools.center.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjialin<br>
 * @version 1.0<br>
 * @createDate 2019/08/30 10:31 <br>
 * @Description <p> 启动流程实例 </p>
 */

@RestController
@Api(tags = "启动流程实例")
@Slf4j
public class StartController extends BaseController {

    @Autowired
    private TravelApplyService beAwayApplyService;
    @Autowired
    private ActFlowCommService actFlowCommService;

    @PostMapping(path = "start")
    @ApiOperation(value = "根据流程key启动流程", notes = "每一个流程有对应的一个key这个是某一个流程内固定的写在bpmn内的")
    public ResultVO start(@RequestBody TravelApplyDto dto) {
        ResultVO<String> start = actFlowCommService.start(dto, getUser().getUserId());
        if(start.getCode().equals(ResultVO.SUCCESSFUL_CODE)){
            //默认完成第一个任务
            UserInfo userInfo = getUser();
            Task task = taskService.createTaskQuery().processInstanceId(start.getData()).active().singleResult();
            dto.setProcessId(start.getData());
            dto.setTaskId(task.getId());
            beAwayApplyService.add(dto);
            taskService.setVariableLocal(task.getId(),"state",1);
            taskService.setAssignee(task.getId(), userInfo.getUserId());
            taskService.complete(task.getId());
            Task tasks = taskService.createTaskQuery().processInstanceId(start.getData()).active().singleResult();
            taskService.setAssignee(tasks.getId(),dto.getApprover());
            return ResultVO.success();
        }
        return ResultVO.fail();
    }


    @PostMapping(path = "searchByKey")
    @ApiOperation(value = "根据流程key查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程key", dataType = "String", paramType = "query"),
    })
    public ResultVO searchProcessInstance(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        List<ProcessInstance> runningList = new ArrayList<>();
        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            log.error("根据流程key查询流程实例,异常:{}", e);
            return ResultVO.fail(SystemEnum.QUERY_ERROR, e.getMessage());
        }

        if (CollectionUtil.isNotEmpty(runningList)) {
            List<Map<String, String>> resultList = new ArrayList<>();
            runningList.forEach(s -> {
                Map<String, String> resultMap = new HashMap<>();
                // 流程实例ID
                resultMap.put("processId", s.getId());
                // 流程定义ID
                resultMap.put("processDefinitionKey", s.getProcessDefinitionId());
                resultList.add(resultMap);
            });
            return ResultVO.success();
        }
        return ResultVO.fail();
    }


    @PostMapping(path = "searchById")
    @ApiOperation(value = "根据流程ID查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例ID", dataType = "String", paramType = "query"),
    })
    public ResultVO searchByID(@RequestParam("processId") String processId) {
        ProcessInstance pi = null;
        try {
            pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        } catch (Exception e) {
            log.error("根据流程ID查询流程实例,异常:{}", e);
            return ResultVO.fail(SystemEnum.QUERY_ERROR, e.getMessage());
        }

        if (pi != null) {
            Map<String, String> resultMap = new HashMap<>(2);
            // 流程实例ID
            resultMap.put("processID", pi.getId());
            // 流程定义ID
            resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
            return ResultVO.success();
        }
        return ResultVO.fail();
    }


    @PostMapping(path = "deleteProcessInstanceByID")
    @ApiOperation(value = "根据流程实例ID删除流程实例", notes = "根据流程实例ID删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例ID", dataType = "String", paramType = "query"),
    })
    public ResultVO deleteProcessInstanceByID(@RequestParam("processId") String processId) {
        try {
            runtimeService.deleteProcessInstance(processId, "删除" + processId);
        } catch (Exception e) {
            log.error("根据流程实例ID删除流程实例,异常:{}", e);
            return ResultVO.fail(SystemEnum.DELETE_ERROR,e.getMessage());
        }
        return ResultVO.success();
    }

    /**
     * 根据流程实例id挂起流程
     * @param processId
     * @return
     */
    @PostMapping(value = "/suspend")
    public ResultVO suspendProcessInstance(@RequestParam("processId") String processId) {
        try {
            runtimeService.suspendProcessInstanceById(processId);
        } catch (Exception e) {
            log.error("根据流程实例id挂起流程,异常:{}", e);
            return ResultVO.fail(SystemEnum.DELETE_ERROR,e.getMessage());
        }
        return ResultVO.success();
    }

    /**
     * 根据流程实例id激活流程
     * @param processId
     * @return
     */
    @PostMapping(value = "/activate")
    public ResultVO activateProcessInstance(@RequestParam("processId") String processId) {
        try {
            runtimeService.activateProcessInstanceById(processId);
        } catch (Exception e) {
            log.error("根据流程实例id激活流程,异常:{}", e);
            return ResultVO.fail(SystemEnum.DELETE_ERROR,e.getMessage());
        }
        return ResultVO.success();
    }


    @PostMapping(path = "deleteProcessInstanceByKey")
    @ApiOperation(value = "根据流程实例key删除流程实例", notes = "根据流程实例key删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程实例Key", dataType = "String", paramType = "query"),
    })
    public ResultVO deleteProcessInstanceByKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        List<ProcessInstance> runningList = new ArrayList<>();
        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            log.error("根据流程实例key删除流程实例,异常:{}", e);
            return ResultVO.fail(SystemEnum.DELETE_ERROR,e.getMessage());
        }

        if (CollectionUtil.isNotEmpty(runningList)) {
            List<Map<String, String>> resultList = new ArrayList<>();
            runningList.forEach(s -> runtimeService.deleteProcessInstance(s.getId(), "删除"));
            return ResultVO.success();
        }
        return ResultVO.fail();
    }

}