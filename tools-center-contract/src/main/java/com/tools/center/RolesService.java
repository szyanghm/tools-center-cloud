package com.tools.center;

import com.tools.center.common.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@FeignClient(name = "tools-center-service")
@RequestMapping(value = "/roles")
public interface RolesService{

    @GetMapping(value = "/v1/findList", produces = {"application/json;charset=UTF-8"})
    List<Role> findList(@RequestParam("userId")String userId);
}
