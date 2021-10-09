package com.tools.center.dto;

import lombok.Data;

@Data
public class FlowDto {

    private String id;
    private String modelId;
    private String flowId;
    private String flowName;
    private String flowKey;
    private String flowPath;
    /**
     * true- 部署成功  false- 没有部署
     */
    private boolean state;
    private String description;
}
