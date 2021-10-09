package com.tools.center.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TravelApplyDto implements Serializable {

    private String id;

    private String taskId;
    /**
     * 流程实例id
     */
    private String processId;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 审批人
     */
    private String approver;
    /**
     * 出差申请单名称
     */
    private String name;
    /**
     * 出差天数
     */
    private String num;
    /**
     * 预计开始时间
     */
    private Date beginDate;
    /**
     * 预计结束时间
     */
    private Date endDate;
    /**
     * 出发地
     */
    private List<String> startAddress;
    /**
     * 目的地
     */
    private List<String> endAddress;
    /**
     * 出差事由
     */
    private String description;

    /**
     * 流程定义key
     */
    private String processKey;

    /**
     * 0:进行中，1:已结束,2:已撤回
     */
    private int state;

    /**
     * 分页参数
     */
    private PageDto pageDto;
}
