package com.tools.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tools.center.entity.Approver;
import com.tools.center.vo.ApproverVO;

import java.util.List;

/**
 * <p>
 * 审批人表 Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
public interface ApproverMapper extends BaseMapper<Approver> {

    List<ApproverVO> findList(String name);
}
