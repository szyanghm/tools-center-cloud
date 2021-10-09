package com.tools.center.controller;

import com.tools.center.TravelApplyService;
import com.tools.center.activiti.ProcessInstanceDiagramCmd;
import com.tools.center.common.UserInfo;
import com.tools.center.dto.TaskDto;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.dto.UserDto;
import com.tools.center.enums.SystemEnum;
import com.tools.center.utils.FileUtil;
import com.tools.center.vo.ResultVO;
import com.tools.center.vo.TaskVO;
import com.tools.center.vo.TravelApplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjialin<br>
 * @version 1.0<br>
 * @createDate 2019/08/30 11:59 <br>
 * @Description <p> 任务相关接口 </p>
 */

@RestController
@Api(tags = "任务相关接口")
@Slf4j
@RequestMapping(value = "/task")
public class TaskController extends BaseController {

    @Autowired
    private TravelApplyService travelApplyService;

    @PostMapping(path = "findTaskByAssignee")
    @ApiOperation(value = "根据流程assignee查询当前人的个人任务", notes = "根据流程assignee查询当前人的个人任务")
    public ResultVO findTaskByAssignee(@RequestBody UserDto dto) {
        List<TaskVO> resultList = new ArrayList<>();
        try {
            if(StringUtils.isBlank(dto.getAssignee())){
                dto.setAssignee(getUser().getUserId());
            }
            //指定个人任务查询
            List<Task> taskList = taskService.createTaskQuery().taskAssignee(dto.getAssignee()).list();
            if (CollectionUtil.isNotEmpty(taskList)) {
                for (Task task : taskList) {
                    TaskVO vo = new TaskVO();
                    /* 任务ID */
                    vo.setId(task.getId());
                    /* 任务名称 */
                    vo.setName(task.getName());
                    /* 任务的创建时间 */
                    vo.setStartTime(task.getCreateTime());
                    /* 任务的办理人 */
                    vo.setAssignee(getUserName(task.getAssignee()));
                    /* 流程实例ID */
                    vo.setProcessInstanceId(task.getProcessInstanceId());
                    /* 执行对象ID */
                    //resultMap.put("executionId", task.getExecutionId());
                    /* 流程定义ID */
                    vo.setProcessDefinitionId(task.getProcessDefinitionId());
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                    if(StringUtils.isBlank(historicProcessInstance.getStartUserId())){
                        continue;
                    }
                    vo.setStartUserId(getUserName(historicProcessInstance.getStartUserId()));
                    vo.setProcessName(historicProcessInstance.getName());
                    resultList.add(vo);
                }
            }
        } catch (Exception e) {
            log.error("根据流程assignee查询当前人的个人任务,异常:{}", e);
            return ResultVO.fail(SystemEnum.QUERY_ERROR,e.getMessage());
        }
        return ResultVO.success(resultList);
    }


    @PostMapping(path = "completeTask")
    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
    public ResultVO completeTask(@RequestBody TaskDto dto) {
        UserInfo userInfo = getUser();
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("state",dto.getState());
            variables.put("comment",dto.getComment());
//            taskService.setAssignee(dto.getTaskId(),userInfo.getUserId());
            taskService.addComment(dto.getTaskId(),dto.getProcessInstanceId(),dto.getComment());
            taskService.complete(dto.getTaskId(), variables);
            Task task = taskService.createTaskQuery().processInstanceId(dto.getProcessInstanceId()).active().singleResult();
            if(task==null||StringUtils.isBlank(task.getId())){
                return travelApplyService.updateState(dto.getProcessInstanceId(),1);
            }
        } catch (Exception e) {
            log.error("完成任务,异常:{}", e);
            return ResultVO.fail(SystemEnum.FLOW_TASK_COMPLETE_ERROR,e.getMessage());
        }
        return ResultVO.success(dto.getTaskId());
    }

    /**
     * 撤回申请，结束任务
     * @param dto
     */
    @PostMapping(value = "recallApply")
    public ResultVO endTask(@RequestBody TravelApplyDto dto) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(dto.getProcessId()).singleResult();
        UserInfo userInfo = getUser();
        //判断发起人是不是为当前用户
        if(!historicProcessInstance.getStartUserId().equals(userInfo.getUserId())){
            return ResultVO.fail();
        }
        //  当前任务
        Task task = taskService.createTaskQuery().processInstanceId(dto.getProcessId()).active().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);
        taskService.setVariableLocal(task.getId(),"state",0);
        //撤回
        travelApplyService.updateState(dto.getProcessId(),2);
        //  完成当前任务
        taskService.complete(task.getId());
        return ResultVO.success();
        //  可以不用恢复原始方向，不影响其它的流程
//        currentFlowNode.setOutgoingFlows(originalSequenceFlowList);
    }

    /**
     * 获取当前节点及需要返回的节点
     * @param processInstanceId 流程实例id
     * @param type 1撤回，2驳回
     * @return
     */
    public Map<String,HistoricTaskInstance> historicTaskQuery(String processInstanceId,int type){
        Map<String,HistoricTaskInstance> map = new HashMap<>();
        // 取得所有历史任务按时间降序排序
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();
        // list里第一条代表当前任务
        HistoricTaskInstance curTask = htiList.get(0);
        map.put("curTask",curTask);
        if(htiList==null || htiList.size()<2){
            return map;
        }
        if(type==2){//驳回
            // list里的第二条代表上一个任务
            HistoricTaskInstance lastTask = htiList.get(1);
            map.put("lastTask",lastTask);
        }else{//撤回
            // list里的最大值减一就是第一个任务
            HistoricTaskInstance lastTask = htiList.get(htiList.size()-1);
            map.put("lastTask",lastTask);
        }
        return map;
    }
    /**
     * 驳回，退回到上一节点
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/rollBack")
    public ResultVO rollBack(@RequestBody TaskDto dto) throws Exception {
        String processInstanceId = dto.getProcessInstanceId();
        Map<String, HistoricTaskInstance> map = historicTaskQuery(processInstanceId, dto.getType());
        // 需要回退到的节点任务
        HistoricTaskInstance lastTask = map.get("lastTask");
        // list里第一条代表当前任务
        HistoricTaskInstance curTask = map.get("curTask");
        // 当前任务id
        String taskId = curTask.getId();
        // 当前节点的executionId
        String curExecutionId = curTask.getExecutionId();
        // 上个节点的taskId
        String lastTaskId = lastTask.getId();
        // 上个节点的executionId
        String lastExecutionId = lastTask.getExecutionId();
        if (null == lastTaskId) {
            throw new Exception("LAST TASK IS NULL");
        }
        String processDefinitionId = lastTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String lastActivityId = null;
        List<HistoricActivityInstance> haiFinishedList = historyService.createHistoricActivityInstanceQuery()
                .executionId(lastExecutionId).finished().list();
        for (HistoricActivityInstance hai : haiFinishedList) {
            if (lastTaskId.equals(hai.getTaskId())) {
                // 得到ActivityId，只有HistoricActivityInstance对象里才有此方法
                lastActivityId = hai.getActivityId();
                break;
            }
        }
        // 得到上个节点的信息
        FlowNode lastFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(lastActivityId);
        // 取得当前节点的信息
        Execution execution = runtimeService.createExecutionQuery().executionId(curExecutionId).singleResult();
        String curActivityId = execution.getActivityId();
        FlowNode curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(curActivityId);
        //记录当前节点的原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(curFlowNode.getOutgoingFlows());
        //清理活动方向
        curFlowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(curFlowNode);
        newSequenceFlow.setTargetFlowElement(lastFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        curFlowNode.setOutgoingFlows(newSequenceFlowList);
//        if(dto.getType()!=2){
//            //撤回
//            BeAwayApplyDto beAwayApplyDto = new BeAwayApplyDto();
//            beAwayApplyDto.setId(dto.getBeAwayApplyId());
//            beAwayApplyDto.setState(2);
//            beAwayApplyService.updateState(beAwayApplyDto);
//        }
        taskService.setVariableLocal(taskId,"state",dto.getType());
        // 完成任务
        taskService.complete(taskId);
        //恢复原方向
        curFlowNode.setOutgoingFlows(oriSequenceFlows);
        org.activiti.engine.task.Task nextTask = taskService
                .createTaskQuery().processInstanceId(processInstanceId).singleResult();
        // 设置执行人
        if(nextTask!=null) {
            taskService.setAssignee(nextTask.getId(), lastTask.getAssignee());
        }
        return ResultVO.success();
    }

//    @PostMapping(value = "/submitApply")
//    public ResultVO submitApply(TaskDto dto){
//        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(dto.getProcessInstanceId()).singleResult();
//        UserInfo userInfo = getUser();
//        Task task = null;
//        //判断发起人是不是为当前用户
//        if(historicProcessInstance.getStartUserId().equals(userInfo.getUserId())){
//          task = taskService.createTaskQuery().processInstanceId(dto.getProcessInstanceId()).active().singleResult();
//        }
//        //判断当前任务id是否为申请人第一个任务id
//        if(ObjectUtil.isNotNull(task)&&task.getId().equals(dto.getTaskId())){
//            //完成任务
//            BeAwayApplyDto beAwayApplyDto = new BeAwayApplyDto();
//            beAwayApplyDto.setId(dto.getBeAwayApplyId());
//            beAwayApplyDto.setState(0);
//            beAwayApplyService.updateState(beAwayApplyDto);
//            taskService.complete(dto.getTaskId());
//            return ResultVO.success();
//        }
//        return ResultVO.fail();
//    }

    @PostMapping(value = "/turnTask")
    @ApiOperation(value = "任务转办", notes = "任务转办，把任务交给别人处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userKey", value = "用户Key", dataType = "String", paramType = "query"),
    })
    public ResultVO turnTask(@RequestParam("taskId") String taskId, @RequestParam("userKey") String userKey) {
        try {
            taskService.setAssignee(taskId, userKey);
        } catch (Exception e) {
            log.error("任务转办,异常:{}", e);
            return ResultVO.fail(SystemEnum.FLOW_TASK_COMPLETE_ERROR,e.getMessage());
        }
        return ResultVO.success(taskId);
    }

    /**
     * 查询历史活动
     * @return
     */
    @GetMapping(value = "/historic",produces = {"application/json;charset=UTF-8"})
    public ResultVO historic(){
        Command<InputStream> cmd = new ProcessInstanceDiagramCmd("7501", runtimeService, repositoryService,processEngineFactoryBean,historyService);
        InputStream inputStream = managementService.executeCommand(cmd);
        File file = new File("D:\\a.svg");
        FileUtil.inputStreamToFile(inputStream,file);
        List<HistoricActivityInstance>  list=historyService.createHistoricActivityInstanceQuery() // 创建历史活动实例查询
                .processInstanceId("7501") // 执行流程实例id
                .finished()
                .list();
        return ResultVO.success(list);
    }

    @GetMapping(value = "/history",produces = {"application/json;charset=UTF-8"})
    public ResultVO historyTaskList(@RequestParam("processId")String processId,@RequestParam("applyId")String applyId){
        List<HistoricTaskInstance> list=historyService // 历史相关Service
                .createHistoricTaskInstanceQuery() // 创建历史任务实例查询
                .processInstanceId(processId) // 用流程实例id查询
                .finished()// 查询已经完成的任务
                .list();
        List<TaskVO> tasks = new ArrayList<>();
        for(HistoricTaskInstance hti:list){
            TaskVO vo = new TaskVO();
            vo.setId(hti.getId());
            vo.setAssignee(getUserName(hti.getAssignee()));
            vo.setName(hti.getName());
            vo.setStartTime(hti.getStartTime());
            vo.setEndTime(hti.getEndTime());
            //获取任务变量信息
            List<HistoricVariableInstance> states = historyService.createHistoricVariableInstanceQuery().executionId(hti.getExecutionId()).variableName("state").list();
            states.forEach(state ->{
                if(state!=null&&state.getTaskId().equals(vo.getId())){
                    vo.setState(Integer.parseInt(state.getValue().toString()));
                }
            });

            List<Comment> taskComments = taskService.getTaskComments(hti.getId());
            if(taskComments!=null&&taskComments.size()>0){
                vo.setComment(taskComments.get(0).getFullMessage());
            }
            tasks.add(vo);
        }
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        if(task!=null){
            TaskVO vo = new TaskVO();
            vo.setName(task.getName());
            vo.setAssignee(getUserName(task.getAssignee()));
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setStartTime(task.getCreateTime());
            vo.setState(0);
            tasks.add(vo);
        }
        ResultVO<TravelApplyVO> vo = travelApplyService.getById(applyId);
        Map<String,Object> map = new HashMap();
        map.put("apply",vo.getData());
        map.put("list",tasks);
        return ResultVO.success(map);
    }

    @GetMapping(value = "/current",produces = {"application/json;charset=UTF-8"})
    public ResultVO current(){
        Task task = taskService.createTaskQuery().processInstanceId("10001").active().singleResult();
        return ResultVO.success(task);
    }

}