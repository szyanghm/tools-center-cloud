package com.tools.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.entity.Approver;
import com.tools.center.mapper.ApproverMapper;
import com.tools.center.service.IApproverService;
import com.tools.center.vo.ApproverVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批人表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@Service
public class ApproverService extends ServiceImpl<ApproverMapper, Approver> implements IApproverService {


    @Override
    public List<ApproverVO> findList(String name) {
        return baseMapper.findList(name);
    }
}
