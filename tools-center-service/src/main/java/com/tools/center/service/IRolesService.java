package com.tools.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.common.Role;
import com.tools.center.entity.Roles;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
public interface IRolesService extends IService<Roles> {

    List<Role> findList(String userId);
}
