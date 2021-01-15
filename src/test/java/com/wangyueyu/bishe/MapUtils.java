package com.wangyueyu.bishe;

import java.math.BigDecimal;


/**
 * ?* Created with IntelliJ IDEA.
 * ?* User: wanglulu
 * ?* Date: 13-10-14
 * ?* Time: 下午6:21
 * ?* To change this template use File | Settings | File Templates.
 * ?
 */
// 判断两坐标距离
public class MapUtils {


    private static final double EARTH_RADIUS = 6378.137;


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * ?* 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * ?*
     * ?* @param lng1
     * ?* @param lat1
     * ?* @param lng2
     * ?* @param lat2
     * ?* @return
     * ?
     */
    public static Double GetDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
        if (null == lng1 || null == lat1 || null == lng2 || null == lat2) {
            return null;
        }


        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // s = Math.round(s * 10000) / 10000;


        BigDecimal decimal = new BigDecimal(s * 1000);
        if (null != decimal) {
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }


        return null;
        //return s;
    }



    public static void main(String[] args) {
        double distance;
        distance = GetDistance(113.55797,34.833271, 113.557961,34.833338);
        System.out.println("Distance is:" + distance);
    }


}