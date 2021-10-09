package com.tools.center.listener;


import com.tools.center.security.LoginUser;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * activiti 任务监听器
 */
public class ActivitiTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = "";
        if(principal instanceof UserDetails) {
            LoginUser loginUser = (LoginUser) principal;
            userId = loginUser.getUserId();
        }
//        if(delegateTask.getTaskDefinitionKey().equals("_3")&& StringUtils.isNotBlank(userId)&&"create".equals(delegateTask.getEventName())){
//            delegateTask.setAssignee(userId);
//        }
        if(delegateTask.getName().equals("总经理审批")){
            delegateTask.setAssignee("903dd349e0c805e89b81b933b3173e79");
        }
    }

}
