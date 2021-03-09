package com.wangyueyu.bishe.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.entity.constant.GeoHashKey;
import com.wangyueyu.bishe.entity.vo.HeatVo;
import com.wangyueyu.bishe.mapper.BikeMapper;
import com.wangyueyu.bishe.service.BikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Service
@Slf4j
public class BikeServiceImpl extends ServiceImpl<BikeMapper, Bike> implements BikeService {
    public static final String BIKE_REDIS_KEY="bike";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BikeMapper bikeMapper;
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

    @Override
    public List<Bike> getBikesBylnglat(ArrayList<Double> lonAndLati) {
        Double longitude = lonAndLati.get(0);
        Double latitude = lonAndLati.get(1);
        log.info("{}{}", longitude, latitude);
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending();
        Distance radius = new Distance(0.2, Metrics.KILOMETERS);
        Point point = new Point(longitude, latitude);
        GeoResults<RedisGeoCommands.GeoLocation<String>> redis = redisTemplate.opsForGeo().radius(GeoHashKey.BIKE_REDIS_KEY, new Circle(point, radius), args);
        log.info("redis{}", redis);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = redis.getContent();
        List<Bike> bikeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geo : list) {
                String bikeId = geo.getContent().getName();
                Bike bike = (Bike) redisTemplate.opsForHash().get(GeoHashKey.BIKE_DETAIL_REDIS_KEY, bikeId);
                bikeList.add(bike);
            }
        }
        return bikeList;
    }

    @Override
    public List<Bike> getBikesByTime() {
        log.info("xxxxxxxxxxxxxx");
        List<Bike> list=bikeMapper.getBikesByTime();
        return list;
    }

    @Override
    public List<HeatVo> getHeat() {
        ArrayList<HeatVo> heatVos = new ArrayList<HeatVo>();
        for(int i=0;i<50;i++){
            // 随机一个坐标
            List<Double> doubles = RandomLocationUtil.randomLnglat("113.557471,34.836515", 0.5, 0.5);
            Double longitude = doubles.get(0);
            Double latitude = doubles.get(1);
            log.info("{}{}", longitude, latitude);
            // 查坐标附近的点数
            RedisGeoCommands.GeoRadiusCommandArgs args =
                    RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending();
            Distance radius = new Distance(0.2, Metrics.KILOMETERS);
            Point point = new Point(longitude, latitude);
            GeoResults<RedisGeoCommands.GeoLocation<String>> redis = redisTemplate.opsForGeo().radius(GeoHashKey.BIKE_REDIS_KEY, new Circle(point, radius), args);
            log.info("redis{}", redis.getContent().size());
            HeatVo heatVo = new HeatVo();
            heatVo.setLng(doubles.get(0));
            heatVo.setLat(doubles.get(1));
            heatVo.setCount(redis.getContent().size());
            heatVos.add(heatVo);
//            List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = redis.getContent();
//            List<Bike> bikeList = new ArrayList<>();
//            if (CollectionUtils.isNotEmpty(list)) {
//                for (GeoResult<RedisGeoCommands.GeoLocation<String>> geo : list) {
//                    String bikeId = geo.getContent().getName();
//                    Bike bike = (Bike) redisTemplate.opsForHash().get(GeoHashKey.BIKE_DETAIL_REDIS_KEY, bikeId);
//                    bikeList.add(bike);
//                }
//            }
        }
        return heatVos;
    }

}
