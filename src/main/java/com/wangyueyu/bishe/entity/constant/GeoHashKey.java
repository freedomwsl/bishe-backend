package com.wangyueyu.bishe.entity.constant;

/**
 * description
 *
 * @author yueyu.wang@hand-china.com 2021/02/03 17:06
 */
public interface GeoHashKey {
    /**
     * 停车区域id与中点位置rediskey
     */
    String REGION_REDIS_KEY = "parkingRegion";
    /**
     * 停车区域详情rediskey
     */
    String REGION_DETAIL_REDIS_KEY = "parkingRegionDetail";
    /**
     * 单车id与单车位置rediskey
     */
    String BIKE_REDIS_KEY="bike";
    /**
     * 单车id单车详情rediskey
     */
    String BIKE_DETAIL_REDIS_KEY="bikeDetail";
    /**
     * 用户id与推荐停车点rediskey
     */
    String USERID_RECOMMEND_REDIS_KEY = "userIdRecommend";

    String STOP_BIKE_LOCATION="stopBikeLocation";
}
