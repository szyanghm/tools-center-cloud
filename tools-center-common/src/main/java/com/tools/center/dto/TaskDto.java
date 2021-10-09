package com.tools.center.dto;

import lombok.Data;

@Data
public class TaskDto {

    private String beAwayApplyId;
    //流程实例id
    private String processInstanceId;
    //任务id
    private String taskId;
    //任务状态
    private int state;
    //审批意见
    private String comment;
    //3撤回，2驳回
    private int type;
}
