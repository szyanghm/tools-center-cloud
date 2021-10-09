package com.tools.center.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tools.center.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tc_roles")
public class Roles extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色code
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 简介
     */
    private String description;


}
