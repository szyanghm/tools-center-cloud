//package com.tools.center;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.tools.center.utils.SecurityUtil;
//import lombok.extern.slf4j.Slf4j;
//import net.bytebuddy.asm.Advice;
//import org.activiti.api.process.model.ProcessDefinition;
//import org.activiti.api.process.model.ProcessInstance;
//import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
//import org.activiti.api.process.runtime.ProcessRuntime;
//import org.activiti.api.runtime.shared.query.Page;
//import org.activiti.api.runtime.shared.query.Pageable;
//import org.activiti.api.task.model.Task;
//import org.activiti.api.task.model.builders.TaskPayloadBuilder;
//import org.activiti.api.task.runtime.TaskRuntime;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TestActiviti {
//
//    @Autowired
//    private ProcessRuntime processRuntime;
//
//    @Autowired
//    private TaskRuntime taskRuntime;
//    @Autowired
//    private SecurityUtil securityUtil;
//
//    /**
//     * 查看流程内容
//     * Activiti7可以自动部署流程
//     */
//    @Test
//    public void findProcess(){
//        securityUtil.logInAs("user1");
//        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
//        log.info("可用流程定义总数:{}",processDefinitionPage.getTotalItems());
//        for (ProcessDefinition processDefinition: processDefinitionPage.getContent()){
//            log.info("流程定义内容:{}", JSONObject.toJSONString(processDefinition));
//        }
//    }
//
//    @Test
//    public void startProcess(){
//        securityUtil.logInAs("user1");
//        ProcessInstance myEvection = processRuntime.start(ProcessPayloadBuilder
//                .start().withProcessDefinitionKey("myEvection").build());
//        log.info("流程实例的内容:{}",JSONObject.toJSONString(myEvection));
//    }
//
//    /**
//     * 执行任务
//     */
//    @Test
//    public void doTask(){
//        //登录用户
//        securityUtil.logInAs("user1");
//        //查询任务
//        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
//        if(tasks!=null&&tasks.getTotalItems()>0){
//            for (Task task:tasks.getContent()){
//                //拾起任务
//                //taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
//                log.info("任务内容,{}",JSONObject.toJSONString(task));
//                //完成任务
//                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
//            }
//        }
//    }
//}
