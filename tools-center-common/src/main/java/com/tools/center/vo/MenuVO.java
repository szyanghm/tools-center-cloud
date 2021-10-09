package com.tools.center.vo;

import com.tools.center.base.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * 菜单类
 */
@Data
public class MenuVO{

    private String id;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单等级
     */
    private int level;

    /**
     * 排序-序号
     */
    private int sort;

    /**
     * 路由
     */
    private String path;

    /**
     * 子集菜单
     */
    private List<MenuVO> children=null;

    public void add(MenuVO node){
        children.add(node);
    }
}
