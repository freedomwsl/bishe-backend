package com.wangyueyu.bishe.service;

import com.wangyueyu.bishe.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-13
 */
public interface UserService extends IService<User> {

    User selectByNameAndPwd(User user);

    void selectIsName(User user);

    void insert(User user);

    String selectPasswordByName(User user);

    int update(User user);
}
