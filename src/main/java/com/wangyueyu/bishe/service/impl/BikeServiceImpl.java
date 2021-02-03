package com.wangyueyu.bishe.service.impl;

import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.mapper.BikeMapper;
import com.wangyueyu.bishe.service.BikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Service
public class BikeServiceImpl extends ServiceImpl<BikeMapper, Bike> implements BikeService {
    public static final String BIKE_REDIS_KEY="bike";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void saveBike(Bike bike) {
        bike.setIsUsing("N");
        bike.setUsedTimes(0);
        bike.setBeginTime(new Date());
        this.save(bike);
        String longLati = bike.getLongLati();
        String[] split = longLati.split(",");
        double lon = Double.parseDouble(split[0]);
        double lat = Double.parseDouble(split[1]);
        redisTemplate.opsForGeo().add(BIKE_REDIS_KEY,new Point(lon,lat),bike.getBikeId());
    }
}
