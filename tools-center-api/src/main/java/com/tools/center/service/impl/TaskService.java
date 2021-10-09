package com.tools.center.service.impl;

import com.tools.center.UserService;
import com.tools.center.entity.User;
import com.tools.center.vo.FlowVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private ActFlowCommService actFlowCommService;
    /**
     * 查询用户任务
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findUserTask(Long userId) {
        List<Map<String, Object>> list = actFlowCommService.myTaskList(userId.toString());
        return list;
    }


    /**
     * 完成任务
     * @param userId
     */
    public void completeTask(String taskId,Long userId) {
        actFlowCommService.completeProcess("同意",taskId,userId.toString());
    }

    @Autowired
    private UserService userService;
    @Autowired
    private SiteMessageService siteMessageService;
    /**
     * 任务创建事件
     * @param delegateTask
     */
    public void createTaskEvent(DelegateTask delegateTask) {
        log.info("delegateTask=={}",delegateTask);
//        负责人
        String assignee = delegateTask.getAssignee();
//        获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        String userId = user.getId();
//        任务id
        String taskId = delegateTask.getId();
        if(!assignee.equals(userId)){
            int type =1;
            siteMessageService.sendMsg(Long.valueOf(assignee),taskId,type,1);
        }

    }

    /**
     * 查询任务详细信息
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findTaskInfo(Long userId) {
        List<Map<String, Object>> list = actFlowCommService.myTaskInfoList(userId.toString());
        return list;
    }
}
