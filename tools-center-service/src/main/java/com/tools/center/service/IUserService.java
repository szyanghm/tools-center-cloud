package com.tools.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.center.common.UserInfo;
import com.tools.center.entity.User;
import com.tools.center.vo.ResultVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-06-17
 */
public interface IUserService extends IService<User> {

    ResultVO add(User tcUser);

    List<User> findList();

    User getUserById(String id);

    User getUserByName(String userName);

    List<UserInfo> findAllUserInfo();

    Map<String,Object> findAssignee();
}
