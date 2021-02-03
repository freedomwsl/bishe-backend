package com.wangyueyu.bishe.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.mapper.ParkingRegionMapper;
import com.wangyueyu.bishe.service.ParkingRegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongToIntFunction;

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
public class ParkingRegionServiceImpl extends ServiceImpl<ParkingRegionMapper, ParkingRegion> implements ParkingRegionService {
    public static final String REGION_REDIS_KEY="parkingRegion";
    public static final String REGION_DETAIL_REDIS_KEY="parkingRegionDetail";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void saveToRedisAndMysql(ParkingRegion parkingRegion) {
        if("".equals(parkingRegion.getParkingRegionName())){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(parkingRegion.getProvince()).append(parkingRegion.getCity())
                    .append(parkingRegion.getDistrict()).append(parkingRegion.getStreet())
                    .append(parkingRegion.getStreetNumber());
            String str=stringBuffer.toString();
            parkingRegion.setParkingRegionName(str);
        }
        if(parkingRegion.getParkingRegionCapacity()==null){
            parkingRegion.setParkingRegionCapacity(100);
        }
        this.save(parkingRegion);
        log.info("存入mysql后regionId={}"+parkingRegion.getParkingRegionId());
        List<Double> centerLocation = parkingRegion.getCenterLocation();
        if(parkingRegion.getParkingRegionId()!=null){
            redisTemplate.opsForHash().put(REGION_DETAIL_REDIS_KEY,parkingRegion.getParkingRegionId().toString(),parkingRegion);
            Long add = redisTemplate.opsForGeo().add(REGION_REDIS_KEY, new Point(centerLocation.get(0), centerLocation.get(1)),
                    parkingRegion.getParkingRegionId().toString());
            log.info("存redisgeo之后的返参是+{}",add);
        }

    }

    @Override
    public List<ParkingRegion> getRegionsByEnd(List<Double> lonAndLati) {
        Double longitude=lonAndLati.get(0);
        Double latitude=lonAndLati.get(1);
        log.info("{}{}", longitude,latitude);
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(10).sortAscending();
        Distance radius = new Distance(0.5, Metrics.KILOMETERS);
        Point point = new Point(longitude,latitude);
        GeoResults<RedisGeoCommands.GeoLocation<String>> redis = redisTemplate.opsForGeo().radius(REGION_REDIS_KEY, new Circle(point,radius),args);
        log.info("redis{}",redis);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = redis.getContent();
        List<ParkingRegion> pointIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(GeoResult<RedisGeoCommands.GeoLocation<String>> geo : list){
                String pointId = geo.getContent().getName();
                ParkingRegion parkingRegion = (ParkingRegion)redisTemplate.opsForHash().get(REGION_DETAIL_REDIS_KEY, pointId);
                pointIdList.add(parkingRegion);
            }
        }
        return pointIdList;
    }
}
