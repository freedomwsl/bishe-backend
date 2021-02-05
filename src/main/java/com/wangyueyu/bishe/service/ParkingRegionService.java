package com.wangyueyu.bishe.service;

import com.wangyueyu.bishe.entity.ParkingRegion;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;

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
}
