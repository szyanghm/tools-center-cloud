package com.tools.center.controller;


import com.tools.center.service.impl.MenuService;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author yhm
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单列表
     * @return
     */
    @GetMapping(value = "/findList")
    public ResultVO findList(){
        return ResultVO.success(menuService.findMenuList());
    }


    /**
     * 根据id查询子集菜单列表
     * @param id
     * @return
     */
    @GetMapping(value = "/getByIdList")
    public ResultVO getByIdList(@RequestParam("id") String id){
        return ResultVO.success(menuService.getByIdList(id));
    }
}
