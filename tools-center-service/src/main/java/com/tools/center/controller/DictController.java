package com.tools.center.controller;


import com.tools.center.service.impl.DictService;
import com.tools.center.vo.DictVO;
import com.tools.center.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author yhm
 * @since 2021-06-28
 */
@Api(tags = "字典工具API接口")
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "系统字典查询",notes = "系统字典查询",httpMethod = "POST",produces = "application/json")
    @PostMapping(value = "/getDict")
    public ResultVO getDict(@ApiParam(name = "key",value = "字典key",required = true) @RequestParam("key")String key){
        List<DictVO> list = dictService.getKey(key);
        return ResultVO.success(list);
    }
}
