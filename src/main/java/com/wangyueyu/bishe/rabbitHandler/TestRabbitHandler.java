package com.wangyueyu.bishe.rabbitHandler;


import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * description
 *
 * @author 25721 2021/01/11 20:57
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "wangyueyu"))
public class TestRabbitHandler {
    @RabbitHandler
    public void handTest(Map<String,Object> map){
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
}
