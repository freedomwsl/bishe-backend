package com.wangyueyu.bishe.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.User;
import com.wangyueyu.bishe.service.BikeService;
import com.wangyueyu.bishe.service.IMailService;
import com.wangyueyu.bishe.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private BikeService bikeService;
    @Autowired
    private UserService userService;
    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;

    //3.添加定时任务
    @Scheduled(cron = "0 0 12 * * ?")
    private void configureTasks() {
        List<User> userList = userService.list(null);
        List<Bike> list = bikeService.getBikesByTime();
        ArrayList<Integer> bikeIdList = new ArrayList<>();
        for (Bike bike : list) {
            bikeIdList.add(bike.getBikeId());
        }
        final StringBuilder emailContent = new StringBuilder();
        emailContent.append("你好，");
        int flag = 0;
        for (Integer integer : bikeIdList) {
            emailContent.append(integer).append(",");
            flag++;
            if (flag > 10) break;
        }
        emailContent.append("等车辆已经两天以上未被使用了，请查看单车情况,查看详情请打开网页：localhost:8083/bike/showUnused");
        final String s = emailContent.toString();
        for (User user : userList) {
            String email = user.getEmail();
            mailService.sendSimpleMail(email, "长期未使用单车推送", s);
        }

    }
}