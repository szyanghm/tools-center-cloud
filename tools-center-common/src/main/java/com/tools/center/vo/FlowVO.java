package com.tools.center.vo;

import com.tools.center.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlowVO extends BaseVO {

    private String modelId;
    private String flowId;
    private String flowName;
    private String flowKey;
    private String flowPath;
    private String updatedTime;
    private String version;
    /**
     * true- 没有部署  false- 已经部署
     */
    private boolean state;
}
