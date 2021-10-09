package com.tools.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.entity.Dict;
import com.tools.center.vo.DictVO;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author yhm
 * @since 2021-06-28
 */
public interface IDictService extends IService<Dict> {

    List<DictVO> getKey(String key);
}
