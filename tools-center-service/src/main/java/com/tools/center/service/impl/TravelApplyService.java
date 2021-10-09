package com.tools.center.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.dto.TravelApplyDto;
import com.tools.center.dto.PageDto;
import com.tools.center.entity.TravelApply;
import com.tools.center.mapper.TravelApplyMapper;
import com.tools.center.service.ITravelApplyService;
import com.tools.center.vo.TravelApplyVO;
import com.tools.center.vo.ResultVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TravelApplyService extends ServiceImpl<TravelApplyMapper, TravelApply> implements ITravelApplyService {

    @Override
    public IPage<TravelApplyVO> findPageList(String userId, PageDto page) {
        return baseMapper.findPageList(userId, new Page<>(page.getPageNo(), page.getPageSize()));
    }

    @Transactional
    public ResultVO updateStateById(TravelApplyDto dto){
        TravelApply beAwayApply = new TravelApply();
        if(ObjectUtil.isNull(dto)){
          return ResultVO.fail();
        }
        beAwayApply.setId(dto.getId());
        beAwayApply.setState(dto.getState());
        if(baseMapper.updateById(beAwayApply)>0){
            return ResultVO.success();
        }
        return ResultVO.fail();
    }

    @Transactional
    public ResultVO updateState(String processId,int state) {
        if(baseMapper.updateState(processId,state)>0){
            return ResultVO.success();
        }
        return ResultVO.fail();
    }

}
