package com.tools.center.controller;


import com.tools.center.common.Role;
import com.tools.center.service.impl.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author yhm
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/roles")
public class RolesController extends BaseController {

    @Autowired
    private RolesService rolesService;

    @GetMapping(value = "/v1/findList", produces = {"application/json;charset=UTF-8"})
    public List<Role> findList(@RequestParam("userId") String userId) {
        return rolesService.findList(userId);
    }
}
