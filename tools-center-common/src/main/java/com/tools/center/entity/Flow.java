package com.tools.center.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tools.center.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 流程表
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tc_flow")
public class Flow extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义的模型id
     */
    private String modelId;

    /**
     * 部署的流程id
     */
    private String flowId;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 流程key
     */
    private String flowKey;

    /**
     * 流程bpm文件路径
     */
    private String flowPath;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 是否已删除1：已删除，0：未删除
     */
    private Boolean deleted;


}
