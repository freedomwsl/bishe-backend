package com.wangyueyu.bishe.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.entity.constant.GeoHashKey;
import com.wangyueyu.bishe.mapper.ParkingRegionMapper;
import com.wangyueyu.bishe.service.ParkingRegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongToIntFunction;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Service
@Slf4j
public class ParkingRegionServiceImpl extends ServiceImpl<ParkingRegionMapper, ParkingRegion> implements ParkingRegionService {
    public static final String REGION_REDIS_KEY = "parkingRegion";
    public static final String REGION_DETAIL_REDIS_KEY = "parkingRegionDetail";
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveToRedisAndMysql(ParkingRegion parkingRegion) {
        if ("".equals(parkingRegion.getParkingRegionName())) {
            String str = parkingRegion.getProvince() + parkingRegion.getCity() +
                    parkingRegion.getDistrict() + parkingRegion.getStreet() +
                    parkingRegion.getStreetNumber();
            parkingRegion.setParkingRegionName(str);
        }
        if (parkingRegion.getParkingRegionCapacity() == null) {
            parkingRegion.setParkingRegionCapacity(100);
        }
        this.save(parkingRegion);
        log.info("存入mysql后regionId={}", parkingRegion.getParkingRegionId());
        List<Double> centerLocation = parkingRegion.getCenterLocation();
        if (parkingRegion.getParkingRegionId() != null) {
            redisTemplate.opsForHash().put(REGION_DETAIL_REDIS_KEY, parkingRegion.getParkingRegionId().toString(), parkingRegion);
            Long add = redisTemplate.opsForGeo().add(REGION_REDIS_KEY, new Point(centerLocation.get(0), centerLocation.get(1)),
                    parkingRegion.getParkingRegionId().toString());
            log.info("存redisgeo之后的返参是+{}", add);
        }

    }

    @Override
    public List<ParkingRegion> getRegionsByEnd(List<Double> lonAndLati, Integer id) {
        Double longitude = lonAndLati.get(0);
        Double latitude = lonAndLati.get(1);
        log.info("{}{}", longitude, latitude);
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(10).sortAscending();
        Distance radius = new Distance(0.3, Metrics.KILOMETERS);
        Point point = new Point(longitude, latitude);
        GeoResults<RedisGeoCommands.GeoLocation<String>> redis = redisTemplate.opsForGeo().radius(GeoHashKey.REGION_REDIS_KEY, new Circle(point, radius), args);
        log.info("redis{}", redis);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = redis.getContent();
        List<ParkingRegion> pointIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            BigDecimal rate = new BigDecimal("1");
            Integer useless = 0;
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geo : list) {
                String pointId = geo.getContent().getName();
                ParkingRegion parkingRegion = (ParkingRegion) redisTemplate.opsForHash().get(GeoHashKey.REGION_DETAIL_REDIS_KEY, pointId);
                final BigDecimal usedCapacity = new BigDecimal(parkingRegion.getUsedCapacity());
                final BigDecimal capacity = new BigDecimal((parkingRegion.getParkingRegionCapacity()));
                BigDecimal tempRate = usedCapacity.divide(capacity);
                if (tempRate.compareTo(rate) < 0) {
                    rate = tempRate;
                    useless = parkingRegion.getParkingRegionId();
                }
                pointIdList.add(parkingRegion);
            }
            // 设置推荐停车点
            for (ParkingRegion parkingRegion : pointIdList) {
                if (useless == parkingRegion.getParkingRegionId()) {
                    parkingRegion.setIsRecommend("Y");
                    redisTemplate.opsForHash().put(GeoHashKey.USERID_RECOMMEND_REDIS_KEY, id, useless);
                }

            }

        }
        return pointIdList;
    }

    @Override
    public String stopBike(ArrayList<Double> lonAndLati, Integer id) {
        Double longitude = lonAndLati.get(0);
        Double latitude = lonAndLati.get(1);

        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(10).sortAscending();
        Distance radius = new Distance(0.02, Metrics.KILOMETERS);
        Point point = new Point(longitude, latitude);
        GeoResults<RedisGeoCommands.GeoLocation<String>> redis = redisTemplate.opsForGeo().radius(GeoHashKey.REGION_REDIS_KEY, new Circle(point, radius), args);
        log.info("redis{}", redis);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = redis.getContent();
        Integer recommendRegionId = (Integer) redisTemplate.opsForHash().get(GeoHashKey.USERID_RECOMMEND_REDIS_KEY, id);
        if (list.size() == 0) {
            redisTemplate.opsForHash().delete(GeoHashKey.USERID_RECOMMEND_REDIS_KEY, id);
            return "文明城市建设中，请停放在指定停车区域，谢谢配合";
        } else {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> region : list) {
                if (region.getContent().getName().equals(recommendRegionId.toString())) {
                    redisTemplate.opsForHash().delete(GeoHashKey.USERID_RECOMMEND_REDIS_KEY, id);
                    return "停放在推荐地点，奖励即将到账";
                }
            }
        }
        redisTemplate.opsForHash().delete(GeoHashKey.USERID_RECOMMEND_REDIS_KEY, id);
        return "停放在了停车区域内，但是未停在推荐地点。（温馨提示：停放在推荐地点处是会有奖励的哦）";
    }

    @Override
    public List<ParkingRegion> getRegionsByHotParkingPlace(List<Double> lonAndLati) {
        Double longitude = lonAndLati.get(0);
        Double latitude = lonAndLati.get(1);
        log.info("{}{}", longitude, latitude);
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(10).sortAscending();
        Distance radius = new Distance(2, Metrics.KILOMETERS);
        Point point = new Point(longitude, latitude);
        GeoResults<RedisGeoCommands.GeoLocation<String>> redis = redisTemplate.opsForGeo().radius(GeoHashKey.REGION_REDIS_KEY, new Circle(point, radius), args);
        log.info("redis{}", redis);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = redis.getContent();
        List<ParkingRegion> pointIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geo : list) {
                String pointId = geo.getContent().getName();
                ParkingRegion parkingRegion = (ParkingRegion) redisTemplate.opsForHash().get(GeoHashKey.REGION_DETAIL_REDIS_KEY, pointId);
                pointIdList.add(parkingRegion);
            }
        }
        return pointIdList;
    }

    @Override
    public Boolean saveRegionsByExcel(MultipartFile file) throws IOException, InvalidFormatException {
        InputStream ins = null;
        File toFile = null;
        ins = file.getInputStream();
        toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        inputStreamToFile(ins, toFile);
        Workbook workbook = WorkbookFactory.create(toFile);
        Sheet sheet = workbook.getSheetAt(0);
        final List<ParkingRegion> regions = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum() - 1; i++) {//跳过第一行
            Row row = sheet.getRow(i);//取得第i行数据
            String[] str = new String[row.getLastCellNum()];
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);//取得第j列数据
                cell.setCellType(CellType.STRING);
                str[j] = cell.getStringCellValue().trim();
                System.out.print(str[j] + " ");
            }
            final ParkingRegion parkingRegion = new ParkingRegion();
            parkingRegion.setParkingRegionName(str[0]);
            parkingRegion.setParkingRegionCapacity(Integer.parseInt(str[1]));
            parkingRegion.setParkingRegionLongLati(str[2]);
            parkingRegion.setProvince(str[3]);
            parkingRegion.setCity(str[4]);
            parkingRegion.setDistrict(str[5]);
            parkingRegion.setCenterLocation(RandomLocationUtil.getCenter(str[2]));
            parkingRegion.setUsedCapacity(0);
            regions.add(parkingRegion);
        }
        for (ParkingRegion region : regions) {
            this.saveToRedisAndMysql(region);
        }
        return Boolean.TRUE;
    }

    @Override
    public void removeRedis(Integer id) {
        final Long remove = redisTemplate.opsForGeo().remove(GeoHashKey.REGION_REDIS_KEY, id.toString());
        log.info("removeregion: {}",remove);
        final Long delete = redisTemplate.opsForHash().delete(GeoHashKey.REGION_DETAIL_REDIS_KEY, id.toString());
        log.info("delete:{}",delete);
    }


    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
