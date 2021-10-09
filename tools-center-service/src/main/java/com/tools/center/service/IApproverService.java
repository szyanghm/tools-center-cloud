package com.tools.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.entity.Approver;
import com.tools.center.vo.ApproverVO;

import java.util.List;

/**
 * <p>
 * 审批人表 服务类
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
public interface IApproverService extends IService<Approver> {

    List<ApproverVO> findList(String name);
}
