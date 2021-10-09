package com.tools.center.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.center.common.Assignee;
import com.tools.center.common.UserInfo;
import com.tools.center.entity.User;
import com.tools.center.mapper.UserMapper;
import com.tools.center.service.IUserService;
import com.tools.center.vo.ResultVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-06-17
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public ResultVO add(User user) {
        if(baseMapper.insert(user)>0){
            return ResultVO.success();
        }
        return ResultVO.fail();
    }

    public List<User> findList(){
        return baseMapper.findList();
    }

    public User getUserById(String id){
        return baseMapper.getUserById(id);
    }

    public User getUserByName(String userName){
        return baseMapper.getUserByName(userName);
    }
    public List<UserInfo> findAllUserInfo(){
        return baseMapper.findAllUserInfo();
    }

    @Override
    public Map<String, Object> findAssignee() {
        Map<String, Object> map = new HashMap<>();
        List<Assignee> assignee = baseMapper.findAssignee();
        for (Assignee ag: assignee){
            map.put(ag.getId(),ag.getUserId());
        }
        return map;
    }
}
