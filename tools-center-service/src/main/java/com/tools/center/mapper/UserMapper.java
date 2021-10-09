package com.tools.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tools.center.common.Assignee;
import com.tools.center.common.UserInfo;
import com.tools.center.entity.User;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-06-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from tc_user")
    List<User> findList();

    @Select("select * from tc_user where id=#{id}")
    User getUserById(String id);

    @Select("select * from tc_user where username=#{userName}")
    User getUserByName(String userName);

    List<UserInfo> findAllUserInfo();

    /**
     * 查询固定审批人
     * ${assignee0}:总经理
     * ${assignee1}:财务
     * ${assignee2}:部门经理
     */
    List<Assignee> findAssignee();
}
