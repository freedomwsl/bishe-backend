package com.wangyueyu.bishe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangyueyu.bishe.entity.ParkingRegion;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendemailApplicationTests {
    public static final String REGION_REDIS_KEY="parkingRegion";
    public static final String REGION_DETAIL_REDIS_KEY="parkingRegionDetail";
    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ParkingRegionService regionService;
    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        mailService.sendSimpleMail("yueyu.wang@hand-china.com","englishenglisht","内容：第一封邮件");
    }

    @Test
    public void sendmailHtml(){
        mailService.sendHtmlMail("yueyu.wang@hand-china.com","主题：你好html邮件","<h1>内容：第一封html邮件</h1>");
    }
    @Test
    public void testRedis(){
        ParkingRegion parkingRegionDetail = (ParkingRegion)redisTemplate.opsForHash().get("parkingRegionDetail", 33);
        System.out.println(parkingRegionDetail);
    }
    //将mysql中的停车区域数据存放在redis中
    @Test
    public void test1(){
        List<ParkingRegion> list = regionService.list(null);
        log.info("listSize{}",list.size());
        for (ParkingRegion region : list) {
            List<Double> center = RandomLocationUtil.getCenter(region.getParkingRegionLongLati());
            redisTemplate.opsForHash().put(REGION_DETAIL_REDIS_KEY,region.getParkingRegionId().toString(),region);
            redisTemplate.opsForGeo().add(REGION_REDIS_KEY, new Point(center.get(0), center.get(1)), region.getParkingRegionId().toString());
        }
    }
}

