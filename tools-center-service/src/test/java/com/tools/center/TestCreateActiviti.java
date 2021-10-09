//package com.tools.center;
//
//import org.activiti.engine.*;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.junit.Test;
//
//import java.util.List;
//
//public class TestCreateActiviti {
//
//    /**
//     * 使用activiti提供的默认方式来创建mysql表
//     */
//    //@Test
//    public void testCreateDbTable(){
//        //getDefaultProcessEngine()会默认从resources下读取名字activiti.cfg.xml的文件
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        System.out.println(processEngine);
//    }
//
//    /**
//     * 创建流程实例
//     */
//    //@Test
//    public void testDeployment(){
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        Deployment deploy = repositoryService.createDeployment().name("流程名称")
//                .addClasspathResource("bpmn/tools-center.bpmn")
//                .addClasspathResource("bpmn/tools-center.png").deploy();
//        System.out.println("流程部署id="+deploy.getId());
//        System.out.println("流程部署名字="+deploy.getName());
//    }
//
//    /**
//     * 启动流程实例
//     */
//    //@Test
//    public void testStartProcess(){
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        ProcessInstance myEvection = runtimeService.startProcessInstanceByKey("myEvection");
//        System.out.println("流程定义id:"+myEvection.getProcessDefinitionId());
//        System.out.println("流程实例id:"+myEvection.getId());
//        System.out.println("当前活动id:"+myEvection.getActivityId());
//    }
//    /**
//     * 查询个人待执行的任务
//     *
//     */
//    @Test
//    public void testFindPersonalTaskList(){
//        //获取流程引擎
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        //获取taskService
//        TaskService taskService = processEngine.getTaskService();
//        //根据流程key和任务的负责人 查询任务
//        List<Task> list = taskService.createTaskQuery().processDefinitionKey("myEvection")//流程key
//                .taskAssignee("user1")//负责人
//                .list();//任务
//
//        //输出
//        for (Task task : list){
//            System.out.println("流程实例id:"+task.getProcessInstanceId());
//            System.out.println("任务id:"+task.getId());
//            System.out.println("负责人:"+task.getAssignee());
//            System.out.println("任务名称:"+task.getName());
//        }
//    }
//}
