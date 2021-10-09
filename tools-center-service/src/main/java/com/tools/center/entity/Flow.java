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
     * 流程定义模型id
     */
    private String modelId;

    /**
     * 流程id
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
     * 流程版本号
     */
    private String version;

    /**
     * 状态 true:开启，false:关闭
     */
    private Boolean state;

    /**
     * 是否已删除1：已删除，0：未删除
     */
    private Boolean deleted;


}
