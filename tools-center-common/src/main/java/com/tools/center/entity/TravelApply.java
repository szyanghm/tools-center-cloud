package com.tools.center.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tools.center.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 出差申请
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tc_travel_apply")
public class TravelApply extends BaseEntity implements Serializable {

    /**
     * 流程实例id
     */
    private String processId;
    /**
     * 任务id
     */
    private String taskId;
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
    private String startAddress;
    /**
     * 目的地
     */
    private String endAddress;
    /**
     * 出差事由
     */
    private String description;

    /**
     * 0:进行中，1:已结束,2:已撤回
     */
    private int state;
}
