package com.tools.center.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.center.TravelApplyService;
import com.tools.center.FlowService;
import com.tools.center.UserService;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.dto.FlowDto;
import com.tools.center.entity.TravelApply;
import com.tools.center.entity.User;
import com.tools.center.enums.SystemEnum;
import com.tools.center.service.IActFlowCustomService;
import com.tools.center.utils.SpringContextUtil;
import com.tools.center.vo.TravelApplyVO;
import com.tools.center.vo.FlowVO;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@EnableAutoConfiguration
public class ActFlowCommService {

    @Autowired
    private FlowService flowService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;
    @Autowired
    private UserService userService;

    @Autowired
    private TravelApplyService beAwayApplyService;

    public ResultVO updateFlow(String id,boolean state){
        FlowVO flowVO = flowService.getByIdFlow(id);
        FlowDto dto = new FlowDto();
        if(flowVO.isState()==state){
            return ResultVO.fail();
        }
        dto.setState(state);
        if(ObjectUtil.isNotNull(flowVO)){
            //关闭状态:更新删除
            dto.setId(flowVO.getId());
            if(!state){
                dto.setFlowId("");
                flowService.updateState(dto);
                return ResultVO.success();
            }
        }
        return ResultVO.success();
    }

    public ResultVO<String> start(TravelApplyDto dto, String userId) {
        Map<String, Object> variables = userService.findAssignee();
        try {
            Authentication.setAuthenticatedUserId(userId);
            ProcessInstance instance = runtimeService.startProcessInstanceByKey(dto.getProcessKey(), variables);
            if (instance != null) {
                Map<String, String> result = new HashMap<>();
                // 流程实例ID
                result.put("processId", instance.getId());
                // 流程定义ID
                result.put("processDefinitionKey", instance.getProcessDefinitionId());
                return new ResultVO<>(SystemEnum.OK, instance.getId());
            }
        } catch (Exception e) {
            log.error("根据流程key启动流程,异常:{}", e);
            return ResultVO.fail(SystemEnum.FLOW_START_ERROR,e.getMessage());
        }
        return ResultVO.fail();
    }

    /**
     * 设置出差申请的 流程变量
     *
     * @return
     */
    public Map<String, Object> setVariables(TravelApplyDto dto ) {
        //设置流程变量
        TravelApplyVO beAwayApplyVO = new TravelApplyVO();
        BeanUtils.copyProperties(dto,beAwayApplyVO);
        Map<String, Object> variables = new HashMap<>();
        //variables.put("assignee3", "903dd349e0c805e89b81b933b3173e79");
        variables.put("beAwayApply", beAwayApplyVO);
        return variables;
    }

    /**
     * 部署流程
     * @param dto
     * @return
     */
    public ResultVO deploy(FlowDto dto) {
        FlowVO flowVO = flowService.getByIdFlow(dto.getId());
        if(ObjectUtil.isNotNull(flowVO)&&flowVO.isState()){
            flowService.updateState(dto);
            if(!dto.isState()){
                /**不带级联的删除：只能删除没有启动的流程，如果流程启动，就会抛出异常*/
                repositoryService.deleteDeployment(flowVO.getFlowId());
            }
            return ResultVO.success();
        }
        try {
            byte[] sourceBytes = repositoryService.getModelEditorSource(flowVO.getModelId());
            JsonNode editorNode = new ObjectMapper().readTree(sourceBytes);
            BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
            BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(editorNode);
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                    .name(flowVO.getFlowName())
                    .key(flowVO.getFlowKey())
                    .enableDuplicateFiltering()
                    .addBpmnModel(flowVO.getFlowName().concat(".bpmn20.xml"), bpmnModel);
            Deployment deployment = deploymentBuilder.deploy();
            if (deployment != null) {
                Map<String, String> result = new HashMap<>(2);
                result.put("deploymentId", deployment.getId());
                result.put("deploymentName", deployment.getName());
                System.out.println("流程部署id：" + deployment.getId());
                System.out.println("流程部署key：" + deployment.getKey());
                System.out.println("流程部署名称：" + deployment.getName());
                dto.setFlowId(deployment.getId());
                flowService.updateState(dto);
                return ResultVO.success();
            }
        } catch (Exception e) {
            log.error("根据modelId部署流程,异常:{}", e);
            return ResultVO.fail(SystemEnum.FLOW_DEPLOY_ERROR,e.getMessage());
        }
        return ResultVO.fail();
    }

    /**
     * 部署流程定义
     */
    public Deployment saveNewDeploy(FlowDto dto) {
        Deployment deployment = repositoryService.createDeployment().name("出差申请")
                .addClasspathResource(dto.getFlowPath()) // 添加bpmn资源
                .name(dto.getFlowKey())
                .deploy();
//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署key：" + deployment.getKey());
        System.out.println("流程部署名称：" + deployment.getName());
        return deployment;
    }

    /**
     * 启动流程实例
     */
    public ProcessInstance startProcess(String formKey, String beanName, String bussinessKey, Long id) {
        IActFlowCustomService customService = (IActFlowCustomService) SpringContextUtil.getBean(beanName);
//		修改业务的状态
        customService.startRunTask(id);
        Map<String, Object> variables = customService.setvariables(id);
        variables.put("bussinessKey", bussinessKey);
//		启动流程
        log.info("【启动流程】，formKey ：{},bussinessKey:{}", formKey, bussinessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(formKey, bussinessKey, variables);
//		流程实例ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("【启动流程】- 成功，processDefinitionId：{}", processDefinitionId);
        return processInstance;
    }

    /**
     * 查看个人任务列表
     */
    public List<Map<String, Object>> myTaskList(String userid) {

        /**
         * 根据负责人id  查询任务
         */
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userid);

        List<Task> list = taskQuery.orderByTaskCreateTime().desc().list();

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (Task task : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", task.getId());
            map.put("taskname", task.getName());
            map.put("description", task.getDescription());
            map.put("priority", task.getPriority());
            map.put("owner", task.getOwner());
            map.put("assignee", task.getAssignee());
            map.put("delegationState", task.getDelegationState());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("createTime", task.getCreateTime());
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            map.put("dueDate", task.getDueDate());
            map.put("category", task.getCategory());
            map.put("parentTaskId", task.getParentTaskId());
            map.put("tenantId", task.getTenantId());

            User user = userService.getUserById(task.getAssignee());
            map.put("assigneeUser", user.getUsername());
            listmap.add(map);
        }

        return listmap;
    }

    /**
     * 查看个人任务信息
     */
    public List<Map<String, Object>> myTaskInfoList(String userid) {

        /**
         * 根据负责人id  查询任务
         */
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userid);

        List<Task> list = taskQuery.orderByTaskCreateTime().desc().list();

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (Task task : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", task.getId());
            map.put("assignee", task.getAssignee());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("createTime", task.getCreateTime());
            ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            if (processInstance != null) {
                String businessKey = processInstance.getBusinessKey();
                if (!StringUtils.isBlank(businessKey)) {
                    String type = businessKey.split(":")[0];
                    String id = businessKey.split(":")[1];
                    if (type.equals("evection")) {
                        ResultVO<TravelApplyVO> resultVO = beAwayApplyService.getById(id);
                        map.put("flowUserName", resultVO.getData().getUsername());
                        map.put("flowType", "出差申请");
                        map.put("flowcontent", "出差" + resultVO.getData().getNum() + "天");
                    }
                }
            }
            listmap.add(map);
        }

        return listmap;
    }


    /**
     * 完成提交任务
     */
    public void completeProcess(String remark, String taskId, String userId) {


        //任务Id 查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            log.error("completeProcess - task is null!!");
            return;
        }


        //任务对象  获取流程实例Id
        String processInstanceId = task.getProcessInstanceId();

        //设置审批人的userId
        Authentication.setAuthenticatedUserId(userId);

        //添加记录
        taskService.addComment(taskId, processInstanceId, remark);
        System.out.println("-----------完成任务操作 开始----------");
        System.out.println("任务Id=" + taskId);
        System.out.println("负责人id=" + userId);
        System.out.println("流程实例id=" + processInstanceId);
        //完成办理
        taskService.complete(taskId);
        System.out.println("-----------完成任务操作 结束----------");
    }

    /**
     * 查询历史记录
     *
     * @param businessKey
     */
    public void searchHistory(String businessKey) {
        List<HistoricProcessInstance> list1 = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        if (CollectionUtils.isEmpty(list1)) {
            return;
        }
        String processDefinitionId = list1.get(0).getProcessDefinitionId();
        // 历史相关Service
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processDefinitionId(processDefinitionId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance hiact : list) {
            if (StringUtils.isBlank(hiact.getAssignee())) {
                continue;
            }
            System.out.println("活动ID:" + hiact.getId());
            System.out.println("流程实例ID:" + hiact.getProcessInstanceId());
            User user = userService.getUserById(hiact.getAssignee());
            System.out.println("办理人ID：" + hiact.getAssignee());
            System.out.println("办理人名字：" + user.getUsername());
            System.out.println("开始时间：" + hiact.getStartTime());
            System.out.println("结束时间：" + hiact.getEndTime());
            System.out.println("==================================================================");
        }
    }

}