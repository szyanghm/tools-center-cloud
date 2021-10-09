package com.tools.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tools.center.entity.Menu;
import com.tools.center.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询一级菜单列表
     * @return
     */
    List<MenuVO> findMenuList(Set<String> roles);

    /**
     * 根据id查询子集菜单列表
     * @param id
     * @return
     */
    List<MenuVO> getByIdList(String id);
}
