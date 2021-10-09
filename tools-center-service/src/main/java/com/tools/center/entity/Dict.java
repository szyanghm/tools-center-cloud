package com.tools.center.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tools.center.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author yhm
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tc_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 键
     */
    private String sysKey;

    /**
     * 值
     */
    private String sysVal;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 字典状态
     */
    private Integer status;

    /**
     * 是否已删除1：已删除，0：未删除
     */
    private Boolean deleted;


}
