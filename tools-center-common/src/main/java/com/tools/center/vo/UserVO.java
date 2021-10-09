package com.tools.center.vo;

import com.tools.center.base.BaseVO;
import lombok.Data;

@Data
public class UserVO extends BaseVO {


    private String username;
    private String nickname;
    private String assignee;
}
