package com.wangyueyu.bishe.mapper;

import com.wangyueyu.bishe.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectByNameAndPwd(User user);

    int insert(User user);

    int update(User user);

    int selectIsName(User user);

    String selectPasswordByName(User user);
}
