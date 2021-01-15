package com.wangyueyu.bishe.controller;


import com.wangyueyu.bishe.util.msm.MessageUtil;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.msm.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description="短信服务")
@RestController
@RequestMapping("/sortMessage")
public class MsmController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //发送短信的方法
    @ApiOperation(value = "发送短信")
    @GetMapping("send/{phoneNumber}")
    public R sendMsm(@PathVariable String phoneNumber){
        //1 从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phoneNumber);
        if(!StringUtils.isEmpty(code)) {
            return R.success();
        }

        //2 如果redis获取 不到，进行阿里云发送
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);

        //调用service发送短信的方法
//        boolean isSend = msmService.send(param,phoneNumber);
        boolean isSend = MessageUtil.send(phoneNumber,code);
        if(isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(phoneNumber,code,5, TimeUnit.MINUTES);
            return R.success();
        } else {
            return R.error().message("短信发送失败");
        }
    }
}
