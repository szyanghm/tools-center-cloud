package com.tools.center.interceptor;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.tools.center.utils.IDUtils;
import org.springframework.stereotype.Component;

@Component
public class GustomIdGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        return IDUtils.uuId();
    }
}
