package com.tools.center.mapper;

import com.tools.center.dto.FlowDto;
import com.tools.center.entity.Flow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tools.center.vo.FlowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 流程表 Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@Mapper
public interface FlowMapper extends BaseMapper<Flow> {


    /**
     * 查询所有流程
     * @param name 根据名称查询
     * @return
     */
    List<FlowVO> findList(String name);

    /**
     * 更新流程状态
     * @param dto
     * @return
     */
    int updateState(@Param("dto") FlowDto dto);

    /**
     * 根据模型id更新key,name,description
     * @param dto
     * @return
     */
    int updateByModelId(@Param("dto") FlowDto dto);


    FlowVO findKey(String id);


    /**
     * 根据流程id查询流程
     * @param id
     * @return
     */
    FlowVO getByIdFlow(String id);
}
