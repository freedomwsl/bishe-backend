package com.wangyueyu.bishe.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangyueyu.bishe.entity.*;
import com.wangyueyu.bishe.entity.constant.GeoHashKey;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import com.wangyueyu.bishe.service.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisTypeMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private BikeService bikeService;
    @Autowired
    private UserService userService;
    @Autowired
    private HotParkingService hotParkingService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;
    @Autowired
    private ParkingRegionJobRecordService parkingRegionJobRecordService;

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
        emailContent.append("等车辆已经两天以上未被使用了，请查看单车情况,查看详情请打开网页：localhost:8083/map/showUnused");
        final String s = emailContent.toString();
        for (User user : userList) {
            String email = user.getEmail();
            mailService.sendSimpleMail(email, "长期未使用单车推送", s);
        }

    }

    /**
     * 热点停车点，占用率较高停车区域，占用率较低区域推送，每小时推送
     */
    @Scheduled(cron = "0 0/60 * * * ?")
    private void hotParking(){
        List<User> userList = userService.list(null);
        final HashMap<String, List> map = new HashMap<>();
        final Calendar instance = Calendar.getInstance();
        int hour=instance.get(Calendar.HOUR_OF_DAY);
        List<HotParkingVO> list = hotParkingService.getHotParkingJoinPlace(hour);
        map.put("hotParkingList",list);
        final StringBuilder emailContent = new StringBuilder();
        emailContent.append("你好，接下来一小时有这些热点停车点需要单车，请合理搬运单车");
        for (HotParkingVO hotParkingVO : list) {
           emailContent.append(hotParkingVO.getHotParkingId()).append(",");
        }
        emailContent.append("打开链接查看详情localhost:8083/map/showHotParking");
        final String s = emailContent.toString();
        for (User user : userList) {
            String email = user.getEmail();
            mailService.sendSimpleMail(email, "热点停车点推送", s);
        }
    }
    /**
     * 每天晚上23点记录每个停车区域占用率，用来统计热点情况
     */
    @Scheduled(cron="0 0 23 * * ?")
    private void recordUseRate(){
        final ArrayList<ParkingRegionJobRecord> list = new ArrayList<>();
        final Map<String,ParkingRegion> entries = redisTemplate.opsForHash().entries(GeoHashKey.REGION_DETAIL_REDIS_KEY);
        for (String integer : entries.keySet()) {
            final ParkingRegion parkingRegion = entries.get(integer);
            final BigDecimal usedCapacity = new BigDecimal(parkingRegion.getUsedCapacity());
            final BigDecimal capacity = new BigDecimal(parkingRegion.getParkingRegionCapacity());
            final BigDecimal useRate = usedCapacity.divide(capacity);
            final ParkingRegionJobRecord parkingRegionJobRecord = new ParkingRegionJobRecord();
            parkingRegionJobRecord.setUseRate(useRate);
            parkingRegionJobRecord.setRecordDate(new Date());
            parkingRegionJobRecord.setParkingRegionId(parkingRegion.getParkingRegionId());
            list.add(parkingRegionJobRecord);
        }
        parkingRegionJobRecordService.saveBatch(list);
    }
}