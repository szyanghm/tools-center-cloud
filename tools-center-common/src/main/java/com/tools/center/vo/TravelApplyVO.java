package com.tools.center.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tools.center.base.BaseVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TravelApplyVO extends BaseVO implements Serializable {

    private String id;
    /**
     * 流程实例id
     */
    private String processId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 审批人
     */
    private String approver;

    private String approverName;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginDate;
    /**
     * 预计结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
     * 审批状态0进行中，1通过，2撤回
     */
    private int state;
    /**
     * 创建时间
     */
    private String createdTime;

}
