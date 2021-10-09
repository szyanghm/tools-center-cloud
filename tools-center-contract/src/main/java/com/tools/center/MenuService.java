package com.tools.center;


import com.tools.center.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tools-center-service")
@RequestMapping(value = "/menu")
public interface MenuService {

    /**
     * 查询一级菜单列表
     * @return
     */
    @GetMapping(value = "/findList")
    ResultVO findList();

    /**
     * 根据id查询子集菜单列表
     * @param id
     * @return
     */
    @GetMapping(value = "/getByIdList")
    ResultVO getByIdList(@RequestParam("id") String id);
}
