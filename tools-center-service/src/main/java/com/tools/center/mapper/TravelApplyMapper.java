package com.tools.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tools.center.entity.TravelApply;
import com.tools.center.vo.TravelApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TravelApplyMapper extends BaseMapper<TravelApply> {


    IPage<TravelApplyVO> findPageList(String userId, Page<TravelApply> page);

    int updateState(@Param("processId") String processId,@Param("state") int state);
}
