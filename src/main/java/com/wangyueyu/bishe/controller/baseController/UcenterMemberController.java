package com.wangyueyu.bishe.controller.baseController;


import com.wangyueyu.bishe.config.MSException;
import com.wangyueyu.bishe.entity.UcenterMember;
import com.wangyueyu.bishe.entity.vo.ChangeVo;
import com.wangyueyu.bishe.entity.vo.LoginVo;
import com.wangyueyu.bishe.entity.vo.RegisterVo;
import com.wangyueyu.bishe.entity.vo.UcenterMemberOrder;
import com.wangyueyu.bishe.service.UcenterMemberService;
import com.wangyueyu.bishe.util.token.JwtInfo;
import com.wangyueyu.bishe.util.token.JwtUtils;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.ResultCodeEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@RestController
@RequestMapping("/ucenter-member")
@Slf4j
public class UcenterMemberController {
    @Autowired
    UcenterMemberService ucenterMemberService;

    //登录
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = ucenterMemberService.login(loginVo);
        return R.success().data("token", token).message("登录成功");
    }

    //注册
    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        ucenterMemberService.register(registerVo);
        return R.success().message("注册成功");
    }

    //更改密码
    @ApiOperation(value = "更改密码")
    @PostMapping("change")
    public R changePassword(@RequestBody ChangeVo changeVo) {
        ucenterMemberService.changePasswd(changeVo);
        return R.success().message("修改密码成功");
    }

    //根据token获取用户信息
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getMemberInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.success().data("userInfo", jwtInfo);
        } catch (Exception e) {
            log.error("解析用户信息失败：" + e.getMessage());
            throw new MSException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    //根据用户id获取用户信息
    @ApiOperation(value = "根据用户id获取用户信息 课程评论用")
    @PostMapping("getUserInfoOrder/{id}")
//    public R getUserInfoOrder(@PathVariable String id) {
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
//        return R.success().data("memberInfo",ucenterMemberOrder);
    }

    //根据用户id获取用户信息
    @ApiOperation(value = "根据用户id获取用户信息 个人中心用")
    @PostMapping("getUserInfo/{id}")
    public R getUserInfo(@PathVariable String id) {
        //  public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        //       return ucenterMemberOrder;
        return R.success().data("memberInfo",ucenterMemberOrder);
    }

    //用户信息修改功能
    @ApiOperation(value = "用户信息修改")
    @PostMapping("updateMember")
    public R updateMember(@RequestBody UcenterMember ucenterMember) {
        boolean flag = ucenterMemberService.updateById(ucenterMember);
        if(flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //查询某天注册人数
    @ApiOperation(value = "查询某天注册人数")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = ucenterMemberService.countRegisterDay(day);
        return R.success().data("countRegister",count);
    }
}

