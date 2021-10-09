package com.tools.center.service.impl;

import com.tools.center.dto.FlowDto;
import com.tools.center.entity.Flow;
import com.tools.center.mapper.FlowMapper;
import com.tools.center.service.IFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.vo.FlowVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@Service
public class FlowService extends ServiceImpl<FlowMapper, Flow> implements IFlowService {

    /**
     * 查询所有流程
     * @return
     */
    public List<FlowVO> findList(String name) {
        return baseMapper.findList(name);
    }

    /**
     * 查询单个流程
     * @param id
     * @return
     */
    public FlowVO getByIdFlow(String id) {
        return baseMapper.getByIdFlow(id);
    }

    /**
     * 更新部署状态
     * @param dto
     * @return
     */
    public int updateState(FlowDto dto){
        return baseMapper.updateState(dto);
    }

    @Override
    public int updateByModelId(FlowDto dto) {
        return baseMapper.updateByModelId(dto);
    }

    @Override
    public FlowVO findKey(String id) {
        return baseMapper.findKey(id);
    }

}
