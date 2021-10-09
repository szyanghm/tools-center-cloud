package com.tools.center.activiti;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ProcessInstanceDiagramCmd implements Command<InputStream> {


    private RuntimeService runtimeService;

    private RepositoryService repositoryService;

    private ProcessEngineFactoryBean processEngine;

    private HistoryService historyService;

    private String processId;

    public ProcessInstanceDiagramCmd(String processId,
                                     RuntimeService runtimeService, RepositoryService repositoryService,
                                     ProcessEngineFactoryBean processEngine,
                                     HistoryService historyService) {
        this.processId = processId;
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.processEngine = processEngine;
        this.historyService = historyService;
    }
    @Override
    public InputStream execute(CommandContext commandContext) {
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processId).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processId).singleResult();
        if (processInstance == null && historicProcessInstance == null) {
            return null;
        }
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
                .getProcessDefinition(processInstance == null ? historicProcessInstance
                        .getProcessDefinitionId() : processInstance
                        .getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition
                .getId());
        List<String> activeActivityIds = new ArrayList<String>();
        if (processInstance != null) {
            activeActivityIds = runtimeService
                    .getActiveActivityIds(processInstance
                            .getProcessInstanceId());
        } else {
            activeActivityIds.add(historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processId)
                    .activityType("endEvent").singleResult().getActivityId());
        }
        InputStream imageStream = new DefaultProcessDiagramGenerator()
                .generateDiagram(bpmnModel, activeActivityIds,new ArrayList<>(),"宋体","宋体","宋体"
                   );
        return imageStream;
    }

}
