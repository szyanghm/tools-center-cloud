package com.tools.center.common;

import lombok.Data;

@Data
public class Assignee {

    /**
     * 负责人key如:${assignee0}
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
}
