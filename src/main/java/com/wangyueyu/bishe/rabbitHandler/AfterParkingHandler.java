package com.wangyueyu.bishe.rabbitHandler;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 入参为{userId:xxxx,}
 *
 * @author 25721 2021/01/12 10:43
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "afterParking"))
public class AfterParkingHandler {
    @RabbitHandler
    public void cosumeAfterParking(String message){

    }
}
