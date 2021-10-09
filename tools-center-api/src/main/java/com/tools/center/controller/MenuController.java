package com.tools.center.controller;


import com.tools.center.MenuService;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询一级菜单列表
     * @return
     */
    @GetMapping(value = "/v1/findList",produces = {"application/json;charset=UTF-8"})
    public ResultVO findList(){
        return menuService.findList();
    }


    /**
     * 根据id查询子集菜单列表
     * @param id
     * @return
     */
    @GetMapping(value = "/getByIdList",produces = {"application/json;charset=UTF-8"})
    public ResultVO getByIdList(@RequestParam("id") String id){
        return ResultVO.success(menuService.getByIdList(id));
    }
}
