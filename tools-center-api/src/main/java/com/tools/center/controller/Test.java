package com.tools.center.controller;

import lombok.Data;

import java.util.List;

@Data
public class Test {

    private String label;
    private String value;
    private String parent="0";

    /**
     * 子集菜单
     */
    private List<Test> children=null;

    public void add(Test node){
        children.add(node);
    }
}
