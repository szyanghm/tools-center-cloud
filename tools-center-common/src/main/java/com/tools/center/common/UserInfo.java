package com.tools.center.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class UserInfo implements Serializable {

    private String userId;
    private String username;
    private String nickname;
    private String avatar;
    /**
     * 用户权限
     */
    private Set<String> roles;

}
