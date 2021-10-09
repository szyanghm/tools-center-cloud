package com.tools.center.entty;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserInfoVO {

    //主键id
    private String id;
    //用户头像
    private String avatarUrl;
    //用户名称
    private String nickName;
    //城市
    private String city;
    //用户性别
    private String gender;
    //用户介绍备注
    private String remarks;
    //标签
    private List<String> listTag;
    //关注状态
    private int followState;
    //粉丝数量
    private int fansNum;
    //用户详情图片
    private List<Map<String,String>> list;
}
