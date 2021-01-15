package com.wangyueyu.bishe.service;

import com.wangyueyu.bishe.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyueyu.bishe.entity.vo.ChangeVo;
import com.wangyueyu.bishe.entity.vo.LoginVo;
import com.wangyueyu.bishe.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    //登录的方法
    String login(LoginVo loginVo);

    //注册的方法
    void register(RegisterVo registerVo);

    //根据openid判断是否有相同微信数据
    UcenterMember getOpenIdMember(String openid);

    //查询某天注册人数
    Integer countRegisterDay(String day);

    //更改密码
    void changePasswd(ChangeVo changeVo);
}
