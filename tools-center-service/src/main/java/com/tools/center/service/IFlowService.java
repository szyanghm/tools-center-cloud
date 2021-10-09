package com.tools.center.service;

import com.tools.center.dto.FlowDto;
import com.tools.center.entity.Flow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.vo.FlowVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程表 服务类
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
public interface IFlowService extends IService<Flow> {

    /**
     * 查询所有流程
     * @return
     */
    List<FlowVO> findList(String name);

    /**
     * 查询单个流程
     * @param id
     * @return
     */
    FlowVO getByIdFlow(String id);

    /**
     * 更新部署状态
     * @param dto
     * @return
     */
    int updateState(FlowDto dto);

    /**
     * 根据模型id更新key,name,description
     * @param dto
     * @return
     */
    int updateByModelId(FlowDto dto);

    FlowVO findKey(String id);

}
