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
            //????????????:????????????
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
                // ????????????ID
                result.put("processId", instance.getId());
                // ????????????ID
                result.put("processDefinitionKey", instance.getProcessDefinitionId());
                return new ResultVO<>(SystemEnum.OK, instance.getId());
            }
        } catch (Exception e) {
            log.error("????????????key????????????,??????:{}", e);
            return ResultVO.fail(SystemEnum.FLOW_START_ERROR,e.getMessage());
        }
        return ResultVO.fail();
    }

    /**
     * ????????????????????? ????????????
     *
     * @return
     */
    public Map<String, Object> setVariables(TravelApplyDto dto ) {
        //??????????????????
        TravelApplyVO beAwayApplyVO = new TravelApplyVO();
        BeanUtils.copyProperties(dto,beAwayApplyVO);
        Map<String, Object> variables = new HashMap<>();
        //variables.put("assignee3", "903dd349e0c805e89b81b933b3173e79");
        variables.put("beAwayApply", beAwayApplyVO);
        return variables;
    }

    /**
     * ????????????
     * @param dto
     * @return
     */
    public ResultVO deploy(FlowDto dto) {
        FlowVO flowVO = flowService.getByIdFlow(dto.getId());
        if(ObjectUtil.isNotNull(flowVO)&&flowVO.isState()){
            flowService.updateState(dto);
            if(!dto.isState()){
                /**???????????????????????????????????????????????????????????????????????????????????????????????????*/
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
                System.out.println("????????????id???" + deployment.getId());
                System.out.println("????????????key???" + deployment.getKey());
                System.out.println("?????????????????????" + deployment.getName());
                dto.setFlowId(deployment.getId());
                flowService.updateState(dto);
                return ResultVO.success();
            }
        } catch (Exception e) {
            log.error("??????modelId????????????,??????:{}", e);
            return ResultVO.fail(SystemEnum.FLOW_DEPLOY_ERROR,e.getMessage());
        }
        return ResultVO.fail();
    }

    /**
     * ??????????????????
     */
    public Deployment saveNewDeploy(FlowDto dto) {
        Deployment deployment = repositoryService.createDeployment().name("????????????")
                .addClasspathResource(dto.getFlowPath()) // ??????bpmn??????
                .name(dto.getFlowKey())
                .deploy();
//        4?????????????????????
        System.out.println("????????????id???" + deployment.getId());
        System.out.println("????????????key???" + deployment.getKey());
        System.out.println("?????????????????????" + deployment.getName());
        return deployment;
    }

    /**
     * ??????????????????
     */
    public ProcessInstance startProcess(String formKey, String beanName, String bussinessKey, Long id) {
        IActFlowCustomService customService = (IActFlowCustomService) SpringContextUtil.getBean(beanName);
//		?????????????????????
        customService.startRunTask(id);
        Map<String, Object> variables = customService.setvariables(id);
        variables.put("bussinessKey", bussinessKey);
//		????????????
        log.info("?????????????????????formKey ???{},bussinessKey:{}", formKey, bussinessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(formKey, bussinessKey, variables);
//		????????????ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("??????????????????- ?????????processDefinitionId???{}", processDefinitionId);
        return processInstance;
    }

    /**
     * ????????????????????????
     */
    public List<Map<String, Object>> myTaskList(String userid) {

        /**
         * ???????????????id  ????????????
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
     * ????????????????????????
     */
    public List<Map<String, Object>> myTaskInfoList(String userid) {

        /**
         * ???????????????id  ????????????
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
                        map.put("flowType", "????????????");
                        map.put("flowcontent", "??????" + resultVO.getData().getNum() + "???");
                    }
                }
            }
            listmap.add(map);
        }

        return listmap;
    }


    /**
     * ??????????????????
     */
    public void completeProcess(String remark, String taskId, String userId) {


        //??????Id ??????????????????
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            log.error("completeProcess - task is null!!");
            return;
        }


        //????????????  ??????????????????Id
        String processInstanceId = task.getProcessInstanceId();

        //??????????????????userId
        Authentication.setAuthenticatedUserId(userId);

        //????????????
        taskService.addComment(taskId, processInstanceId, remark);
        System.out.println("-----------?????????????????? ??????----------");
        System.out.println("??????Id=" + taskId);
        System.out.println("?????????id=" + userId);
        System.out.println("????????????id=" + processInstanceId);
        //????????????
        taskService.complete(taskId);
        System.out.println("-----------?????????????????? ??????----------");
    }

    /**
     * ??????????????????
     *
     * @param businessKey
     */
    public void searchHistory(String businessKey) {
        List<HistoricProcessInstance> list1 = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        if (CollectionUtils.isEmpty(list1)) {
            return;
        }
        String processDefinitionId = list1.get(0).getProcessDefinitionId();
        // ????????????Service
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
            System.out.println("??????ID:" + hiact.getId());
            System.out.println("????????????ID:" + hiact.getProcessInstanceId());
            User user = userService.getUserById(hiact.getAssignee());
            System.out.println("?????????ID???" + hiact.getAssignee());
            System.out.println("??????????????????" + user.getUsername());
            System.out.println("???????????????" + hiact.getStartTime());
            System.out.println("???????????????" + hiact.getEndTime());
            System.out.println("==================================================================");
        }
    }

}