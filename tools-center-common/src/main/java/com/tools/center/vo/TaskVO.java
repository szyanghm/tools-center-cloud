package com.tools.center.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaskVO {

    //任务id
    private String id;
    //流程实例id
    private String processInstanceId;
    //流程定义id
    private String processDefinitionId;
    //任务名称
    private String name;
    //流程名称
    private String processName;
    //负责人
    private String assignee;
    //用户id
    private String userId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    private String startUserId;//任务发起人，创建者
    private int state;//1:通过,0:待审批,2驳回
    private String comment;
}
