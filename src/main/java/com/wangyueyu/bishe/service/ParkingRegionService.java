package com.wangyueyu.bishe.service;

import com.wangyueyu.bishe.entity.ParkingRegion;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
public interface ParkingRegionService extends IService<ParkingRegion> {

    void saveToRedisAndMysql(ParkingRegion parkingRegion);

    List<ParkingRegion> getRegionsByEnd(List<Double> lonAndLati, Integer id);

    String stopBike(ArrayList<Double> doubles, Integer id);

    List<ParkingRegion> getRegionsByHotParkingPlace(List<Double> doubles);

    Boolean saveRegionsByExcel(MultipartFile file) throws IOException, InvalidFormatException;

    void removeRedis(Integer id);

    Map<String, Object> getAllByLngLat(ArrayList<Double> doubles);
}
