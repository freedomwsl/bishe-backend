package com.wangyueyu.bishe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.entity.constant.GeoHashKey;
import com.wangyueyu.bishe.service.BikeService;
import com.wangyueyu.bishe.service.IMailService;
import com.wangyueyu.bishe.service.ParkingRegionService;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;



@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendemailApplicationTests {
    public static final String REGION_REDIS_KEY = "parkingRegion";
    public static final String REGION_DETAIL_REDIS_KEY = "parkingRegionDetail";
    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ParkingRegionService regionService;
    @Autowired
    private BikeService bikeService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        mailService.sendSimpleMail("yueyu.wang@hand-china.com", "englishenglisht", "内容：第一封邮件");
    }

    @Test
    public void sendmailHtml() {
        mailService.sendHtmlMail("yueyu.wang@hand-china.com", "主题：你好html邮件", "<h1>内容：第一封html邮件</h1>");
    }

    @Test
    public void testRedis() {
        ParkingRegion parkingRegionDetail = (ParkingRegion) redisTemplate.opsForHash().get("parkingRegionDetail", 33);
        System.out.println(parkingRegionDetail);
    }

    /**
     * 将mysql中的停车区域数据存放在redis中
     */

    @Test
    public void mysqlRegionToRedis() {
        List<ParkingRegion> list = regionService.list(null);
        log.info("listSize{}", list.size());
        for (ParkingRegion region : list) {
            // 随机设置一个已使用容量
            Random random = new Random();
            int i = random.nextInt(region.getParkingRegionCapacity());
            region.setUsedCapacity(i);
            List<Double> center = RandomLocationUtil.getCenter(region.getParkingRegionLongLati());
            redisTemplate.opsForHash().put(REGION_DETAIL_REDIS_KEY, region.getParkingRegionId().toString(), region);
            redisTemplate.opsForGeo().add(REGION_REDIS_KEY, new Point(center.get(0), center.get(1)), region.getParkingRegionId().toString());
        }
    }

    @Test
    public void initialBike() {
        ArrayList<Bike> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Bike bike = new Bike();
            bike.setBeginTime(new Date());
            String s = RandomLocationUtil.randomLonLat("113.557471,34.836515", 0.5, 0.5);
            String substring = s.substring(1, s.length() - 1);
            bike.setLongLati(substring);
            Random random = new Random();
            int times = random.nextInt(50);
            bike.setUsedTimes(times);
            String flag = times % 2 == 0 ? "Y" : "N";
            bike.setIsUsing(flag);
            list.add(bike);
        }
        bikeService.saveBatch(list);
        // 取出mysql所有的同步到redis
        List<Bike> bikeList = bikeService.list(null);
        for (Bike bike : bikeList) {
            String longLati = bike.getLongLati();

            String[] split = longLati.split(",");
            Double lng=Double.parseDouble(split[0]);
            Double lat = Double.parseDouble(split[1]);
            redisTemplate.opsForHash().put(GeoHashKey.BIKE_DETAIL_REDIS_KEY, bike.getBikeId().toString(), bike);
            redisTemplate.opsForGeo().add(GeoHashKey.BIKE_REDIS_KEY, new Point(lng, lat), bike.getBikeId().toString());
        }
    }
}

