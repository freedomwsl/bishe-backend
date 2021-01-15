package com.wangyueyu.bishe.controller;


import com.wangyueyu.bishe.util.R;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * description
 *
 * @author 25721 2021/01/11 20:50
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/test")
    public R testRabbit(){

//        HashMap<String, Object> map = new HashMap<>();
//        map.put("wangyueyu",user);
//        map.put("hhh",111);
//        rabbitTemplate.convertAndSend("wangyueyu",map);
        return R.success().message("发送消息成功");
    }
}
