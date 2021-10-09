package com.tools.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tools.center.common.Role;
import com.tools.center.entity.Roles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@Mapper
public interface RolesMapper extends BaseMapper<Roles> {

    List<Role> findList(String userId);
}
