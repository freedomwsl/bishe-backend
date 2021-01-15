package com.wangyueyu.bishe.service.impl;

import com.wangyueyu.bishe.entity.User;
import com.wangyueyu.bishe.mapper.UserMapper;
import com.wangyueyu.bishe.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User selectByNameAndPwd(User user) {
        return userMapper.selectByNameAndPwd(user);
    }

    @Override
    public void selectIsName(User user) {
        userMapper.selectIsName(user);
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public String selectPasswordByName(User user) {
        return userMapper.selectPasswordByName(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }
}
