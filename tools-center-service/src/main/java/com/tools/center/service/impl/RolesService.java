package com.tools.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.common.Role;
import com.tools.center.entity.Roles;
import com.tools.center.mapper.RolesMapper;
import com.tools.center.service.IRolesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@Service
public class RolesService extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

    @Override
    public List<Role> findList(String userId) {
        return baseMapper.findList(userId);
    }
}
