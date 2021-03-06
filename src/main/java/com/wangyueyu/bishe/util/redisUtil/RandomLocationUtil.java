package com.wangyueyu.bishe.util.redisUtil;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * description
 *
 * @author yueyu.wang@hand-china.com 2021/02/02 11:45
 */
@Slf4j
public class RandomLocationUtil {
    /**
     * 随机获取一个指定经纬度附近的一个坐标
     * @param lngAndLat 指定经纬度
     * @param length 东西跨度（单位千米近似）
     * @param width 南北跨度
     * @return
     */
    public static String randomLonLat(String lngAndLat,Double length,Double width) {
        String substring = lngAndLat.substring(0, lngAndLat.length() - 1);
        String[] split = substring.split(",");
        double lng1 = Double.parseDouble(split[0]);
        double lat1 = Double.parseDouble(split[1]);
        double MinLon = lng1 - 0.01*length;
        double MaxLon = lng1 + 0.01*length;
        double MaxLat = lat1 + 0.005*width;
        double MinLat = lat1 - 0.005*width;
        Random random = new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        String s = "(" + lon + "," + lat + ")";
        return s;
    }

    /**
     * 处理百度地图格式的一串经纬度为double数组
     *
     * @param locationStr 百度地图的一串坐标数组
     * @return
     */
    public static List<Double> dealWithLocationArray(String locationStr) {
        ArrayList<Double> doubles = new ArrayList<>();
        String replace = locationStr.replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("lng", "")
                .replace("lat", "")
                .replace("\"", "")
                .replace(":", "");
        System.out.println(replace);
        String[] split = replace.split(",");
        for (String s : split) {
            Double v = Double.parseDouble(s);
            doubles.add(v);
        }
        return doubles;
    }

    /**
     * 获取一串坐标的中点
     */
    public static List<Double> getCenter(String locationStr) {
        ArrayList<Double> returnDoubles = new ArrayList<>();
        List<Double> doubles = dealWithLocationArray(locationStr);

        double longi = 0;
        double lati = 0;
        for (int i = 0; i < doubles.size(); i++) {
            if (i % 2 == 0) {
                longi += doubles.get(i);
            } else lati += doubles.get(i);
        }
        final double centerLongi = longi / doubles.size();
        final double centerLati = lati / doubles.size();
        returnDoubles.add(centerLongi*2);
        returnDoubles.add(centerLati*2);
        return returnDoubles;
    }

    /**
     * 12.2222,23.1111格式字符串经纬度转List<double>
     */
    public static List<Double> stringToDoubleList(String longlati){
        final List<Double> doubles = new ArrayList<>();
        final String[] split = longlati.split(",");
        final double lng = Double.parseDouble(split[0]);
        final double lat = Double.parseDouble(split[1]);
        doubles.add(lng);
        doubles.add(lat);
        return doubles;
    }
    public static String getTime(){
        final Calendar instance = Calendar.getInstance();
        final int hour = instance.get(Calendar.HOUR_OF_DAY)-1;
        final int minute = instance.get(Calendar.MINUTE);
        final int second = instance.get(Calendar.SECOND);
        return hour+":"+minute+":"+second;
    }
}
