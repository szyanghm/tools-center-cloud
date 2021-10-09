package com.tools.center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.dto.PageDto;
import com.tools.center.entity.TravelApply;
import com.tools.center.vo.TravelApplyVO;
import com.tools.center.vo.ResultVO;

public interface ITravelApplyService extends IService<TravelApply> {

    IPage<TravelApplyVO> findPageList(String userId, PageDto page);

    ResultVO updateStateById(TravelApplyDto dto);

    ResultVO updateState(String processId,int state);
}
