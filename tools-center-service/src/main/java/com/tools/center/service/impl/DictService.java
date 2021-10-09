package com.tools.center.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.entity.Dict;
import com.tools.center.mapper.DictMapper;
import com.tools.center.service.IDictService;
import com.tools.center.vo.DictVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2021-06-28
 */
@Service
public class DictService extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Override
    @Cacheable(key = "#key",cacheNames = "dict")
    public List<DictVO> getKey(String key) {
        return baseMapper.getKey(key);
    }
}
