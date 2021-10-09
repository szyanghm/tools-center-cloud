package com.tools.center.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tools.center.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tc_menu")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 父级id
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单等级
     */
    private Integer level;

    /**
     * 是否已删除1：已删除，0：未删除
     */
    private Boolean deleted;


}
