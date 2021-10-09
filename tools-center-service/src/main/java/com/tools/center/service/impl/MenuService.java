package com.tools.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.common.UserInfo;
import com.tools.center.entity.Menu;
import com.tools.center.mapper.MenuMapper;
import com.tools.center.provider.LocalProvider;
import com.tools.center.service.IMenuService;
import com.tools.center.utils.TreeUtil;
import com.tools.center.vo.MenuVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<MenuVO> findMenuList() {
        UserInfo userInfo = LocalProvider.getUser();
        Set<String> roles = userInfo.getRoles();
        if(roles.contains("ADMIN")){
            userInfo.setRoles(null);
        }
        List<MenuVO> list = baseMapper.findMenuList(roles);
        List<MenuVO> voList = TreeUtil.bulid(list, "0");
        return voList;
    }

    @Override
    public List<MenuVO> getByIdList(String id) {
        return baseMapper.getByIdList(id);
    }
}
