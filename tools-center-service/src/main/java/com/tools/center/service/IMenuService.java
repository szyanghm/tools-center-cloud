package com.tools.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.entity.Menu;
import com.tools.center.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 查询一级菜单列表
     * @return
     */
    List<MenuVO> findMenuList();

    /**
     * 根据id查询子集菜单列表
     * @param id
     * @return
     */
    List<MenuVO> getByIdList(String id);
}
