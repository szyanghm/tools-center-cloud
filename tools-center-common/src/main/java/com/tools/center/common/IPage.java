package com.tools.center.common;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class IPage<T> extends Page<T> {

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);// 实现json序列化
    }
}
