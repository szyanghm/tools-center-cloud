package com.tools.center.controller;

import com.tools.center.FlowService;
import com.tools.center.service.impl.ActFlowCommService;
import com.tools.center.service.impl.TaskService;
import com.tools.center.vo.FlowVO;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 流程管理
 */
@RestController
public class FlowController {


    @Autowired
    private TaskService taskService;
    @Autowired
    private ActFlowCommService actFlowCommService;





    /**
     * 查询用户任务
     * @param request
     * @return
     */
    @GetMapping("/findUserTask")
    public List<Map<String,Object>> findUserTask(HttpServletRequest request){
        Long userId = (Long)request.getSession().getAttribute("userid");
        return taskService.findUserTask(userId);
    }

    /**
     * 查询任务详细信息
     * @param request
     * @return
     */
    @GetMapping("/findTaskInfo")
    public List<Map<String,Object>> findTaskInfo(HttpServletRequest request){
        Long userId = (Long)request.getSession().getAttribute("userid");
        return taskService.findTaskInfo(userId);
    }

    /**
     * 完成任务
     * @param request
     */
    @PutMapping("/completeTask/{taskId}")
    public void completeTask(HttpServletRequest request,@PathVariable("taskId")String taskId){
        Long userId = (Long)request.getSession().getAttribute("userid");
        taskService.completeTask(taskId,userId);
    }

    /**
     * 查询
     * @return
     */
    @GetMapping("/findFlowTask/{id}")
    public Map<String,Object> findFlowTask(@PathVariable(name = "id")Long id){
        String businessKey = "evection:"+id;
        actFlowCommService.searchHistory(businessKey);
        return null;
    }
}
