package com.tools.center.mapper;

import com.tools.center.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tools.center.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2021-06-28
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    List<DictVO> getKey(@Param("key") String key);
}
