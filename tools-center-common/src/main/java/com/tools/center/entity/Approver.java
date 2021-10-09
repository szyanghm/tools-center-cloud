package com.tools.center.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tools.center.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批人表
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tc_approver")
public class Approver extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;
    
}
