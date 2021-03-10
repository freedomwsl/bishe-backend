package com.wangyueyu.bishe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.entity.ParkingRegionJobRecord;
import com.wangyueyu.bishe.entity.constant.GeoHashKey;
import com.wangyueyu.bishe.entity.vo.AwardPojo;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import com.wangyueyu.bishe.mapper.BikeMapper;
import com.wangyueyu.bishe.service.*;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendemailApplicationTests {
    public static final String REGION_REDIS_KEY = "parkingRegion";
    public static final String REGION_DETAIL_REDIS_KEY = "parkingRegionDetail";
    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ParkingRegionService regionService;
    @Autowired
    private BikeService bikeService;
    @Autowired
    private ParkingRegionJobRecordService parkingRegionJobRecordService;
    @Autowired
    private HotParkingService hotParkingService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        mailService.sendSimpleMail("yueyu.wang@hand-china.com", "englishenglisht", "内容：第一封邮件");
    }

    @Test
    public void sendmailHtml() {
        mailService.sendHtmlMail("yueyu.wang@hand-china.com", "主题：你好html邮件", "<h1>内容：第一封html邮件</h1>");
    }

    @Test
    public void testRedis() {
        ParkingRegion parkingRegionDetail = (ParkingRegion) redisTemplate.opsForHash().get("parkingRegionDetail", 33);
        System.out.println(parkingRegionDetail);
    }

    /**
     * 将mysql中的停车区域数据存放在redis中
     */

    @Test
    public void mysqlRegionToRedis() {
        List<ParkingRegion> list = regionService.list(null);
        log.info("listSize{}", list.size());
        for (ParkingRegion region : list) {
            // 随机设置一个已使用容量
            Random random = new Random();
            int i = random.nextInt(region.getParkingRegionCapacity());
            region.setUsedCapacity(i);
            List<Double> center = RandomLocationUtil.getCenter(region.getParkingRegionLongLati());
            redisTemplate.opsForHash().put(REGION_DETAIL_REDIS_KEY, region.getParkingRegionId().toString(), region);
            redisTemplate.opsForGeo().add(REGION_REDIS_KEY, new Point(center.get(0), center.get(1)), region.getParkingRegionId().toString());
        }
    }

    /**
     * 初始化单车数据
     */
    @Test
    public void initialBike() {
//        ArrayList<Bike> list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Bike bike = new Bike();
//            bike.setBeginTime(new Date());
//            String s = RandomLocationUtil.randomLonLat("113.557471,34.836515", 0.5, 0.5);
//            String substring = s.substring(1, s.length() - 1);
//            bike.setLongLati(substring);
//            Random random = new Random();
//            int times = random.nextInt(50);
//            bike.setUsedTimes(times);
//            String flag = times % 2 == 0 ? "Y" : "N";
//            bike.setIsUsing(flag);
//            list.add(bike);
//        }
//        bikeService.saveBatch(list);
        // 取出mysql所有的同步到redis
        List<Bike> bikeList = bikeService.list(null);
        for (Bike bike : bikeList) {
            String longLati = bike.getLongLati();

            String[] split = longLati.split(",");
            double lng= Double.parseDouble(split[0]);
            double lat = Double.parseDouble(split[1]);
            redisTemplate.opsForHash().put(GeoHashKey.BIKE_DETAIL_REDIS_KEY, bike.getBikeId().toString(), bike);
            redisTemplate.opsForGeo().add(GeoHashKey.BIKE_REDIS_KEY, new Point(lng, lat), bike.getBikeId().toString());
        }
    }

    /**
     * 测试定时记录停车区域job
     */
    @Test
    public void getBikes(){
        final ArrayList<ParkingRegionJobRecord> list = new ArrayList<>();
        final Map<String,ParkingRegion> entries = redisTemplate.opsForHash().entries(GeoHashKey.REGION_DETAIL_REDIS_KEY);
        for (String integer : entries.keySet()) {
            final ParkingRegion parkingRegion = entries.get(integer);
            final BigDecimal usedCapacity = new BigDecimal(parkingRegion.getUsedCapacity());
            final BigDecimal capacity = new BigDecimal(parkingRegion.getParkingRegionCapacity());
            final BigDecimal useRate = usedCapacity.divide(capacity,2,RoundingMode.HALF_UP);

            final ParkingRegionJobRecord parkingRegionJobRecord = new ParkingRegionJobRecord();
            parkingRegionJobRecord.setUseRate(useRate);
            parkingRegionJobRecord.setRecordDate(new Date());
            parkingRegionJobRecord.setParkingRegionId(parkingRegion.getParkingRegionId());
            list.add(parkingRegionJobRecord);
        }
        parkingRegionJobRecordService.saveBatch(list);
    }
    @Test
    public void testxxxs(){
        final HashMap<String, List> map = new HashMap<>();
        final String time = RandomLocationUtil.getTime();
        List<HotParkingVO> list = hotParkingService.getHotParkingJoinPlace(time);
        map.put("hotParkingList",list);
        System.out.println(list);
    }
    @Test
    public void testOptional(){
        final boolean equals = new BigDecimal("0.0").equals(new BigDecimal("0.00"));
        System.out.println(equals);
    }
    @Test
    public void testExcel() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("F:\\工作簿1.xlsx"));
        System.out.println("sheets" + workbook.getNumberOfSheets());
        //获取一张表
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet.getLastRowNum());
        for (int i=1;i<=sheet.getLastRowNum()-1;i++) {//跳过第一行
            Row row=sheet.getRow(i);//取得第i行数据
            System.out.println("----------"+row.getLastCellNum());
            String [] str=new String[row.getLastCellNum()];
            for (int j=0;j<row.getLastCellNum();j++) {
                Cell cell=row.getCell(j);//取得第j列数据
                cell.setCellType(CellType.STRING);
                str[j]=cell.getStringCellValue().trim();
                System.out.print(str[j]+" ");
            }
            final ParkingRegion parkingRegion = new ParkingRegion();
            parkingRegion.setParkingRegionName(str[0]);
            System.out.println(str[0]);
            System.out.println(str[1]);
            parkingRegion.setParkingRegionCapacity(Integer.parseInt(str[1]));
            System.out.println(str[1]);
            parkingRegion.setParkingRegionLongLati(str[2]);
            System.out.println(str[2]);
            parkingRegion.setProvince(str[3]);
            System.out.println(str[3]);
            parkingRegion.setCity(str[4]);
            System.out.println(str[4]);
            parkingRegion.setStreet(str[5]);
            System.out.println(str[5]);
            parkingRegion.setCenterLocation(RandomLocationUtil.getCenter(str[2]));
            System.out.println(str[6]);
            parkingRegion.setUsedCapacity(0);
            System.out.println(parkingRegion);
        }


    }

    private String encryptStr(String str, String channel){
        final String TB = "TB";
        final int ELEVEN = 11;
        final int EIGHT = 8;
        if(TB.equals(channel)){
            return encryptStr(str);
        }
        else{
            if(str.length() >= ELEVEN){
                return str.substring(0, str.length() - 8) + "****" + str.substring(str.length() - 4, str.length());
            }
            else if(str.length() >= EIGHT){
                return str.substring(0, str.length() - 4) + "****";
            }else {
                return str;
            }
        }
    }
    private String encryptStr(String str) {

        final String GBK = "GBK";
        final int TWO = 2;

        String encryptStr = str;
        try {
            byte[] byteArr = str.getBytes(GBK);

            int firstNum = 4;
            int secondNum = 5;

            int firstIndexInOriginal = 0;
            int secondIndexInOriginal = 0;

            List<Integer> index = new LinkedList<>();

            for (int i = 0; i < byteArr.length; i++) {
                if (!str.contains(new String(new byte[]{byteArr[i]}, GBK))) {
                    if (i < firstNum) {
                        firstIndexInOriginal--;
                    }
                    if (i < secondNum) {
                        secondIndexInOriginal--;
                    }
                    index.add(i);
                }
            }

            if (index.size() > 0 && index.size() == (str.getBytes(GBK).length - str.length()) * TWO) {
                if (index.contains(firstNum)) {
                    int firstIndexInByte = index.indexOf(firstNum);
                    if (firstIndexInByte % TWO == 0) {
                        secondNum++;
                        secondIndexInOriginal--;
                        firstIndexInOriginal = firstNum - (firstIndexInByte / TWO);
                    } else {
                        firstIndexInOriginal = firstNum - (firstIndexInByte / TWO) - 1;
                    }
                }
                if (index.contains(secondNum)) {
                    int secondInByte = index.indexOf(secondNum);
                    if (secondInByte % TWO == 0) {
                        secondIndexInOriginal = secondNum - (secondInByte / TWO);
                    } else {
                        secondIndexInOriginal = secondNum - (secondInByte / TWO) - 1;
                    }
                }
            }

            if (firstIndexInOriginal > 0) {
                encryptStr = encryptStr.substring(0, firstIndexInOriginal) + "*" + encryptStr.substring
                        (firstIndexInOriginal + 1, encryptStr.length());
            } else if (str.length() > (firstNum + firstIndexInOriginal / TWO)) {
                encryptStr = encryptStr.substring(0, firstNum + firstIndexInOriginal / TWO) + "*" + encryptStr.substring
                        (firstNum + firstIndexInOriginal / TWO + 1, encryptStr.length());
            }
            if (secondIndexInOriginal > 0) {
                encryptStr = encryptStr.substring(0, secondIndexInOriginal) + "*" + encryptStr.substring
                        (secondIndexInOriginal + 1, encryptStr.length());
            } else if (str.length() > (secondNum + secondIndexInOriginal / TWO)) {
                encryptStr = encryptStr.substring(0, secondNum + secondIndexInOriginal / TWO) + "*" + encryptStr
                        .substring(secondNum + secondIndexInOriginal / TWO + 1, encryptStr.length());
            }
        } catch (UnsupportedEncodingException e) {
            return str;
        }

        return encryptStr;
    }

    @Test
    public void testMap(){
        Map<String, String> awardLists = new HashMap<>();
        awardLists.put("1","[\"12\",\"dfsd\",\"sdfs\",\"sdfd\"]");
        awardLists.put("2","[\"12\",\"54\"]");
        String channel ="fkdsjld";

        List<AwardPojo> awardPojos = new ArrayList<AwardPojo>() {{
            awardLists.forEach((buyer, orderList) -> {
                    JSONObject.parseArray(orderList, String.class).forEach(order ->
                    add(new AwardPojo(buyer, order)));
            });
        }}.stream().sorted(Comparator.comparing(AwardPojo::getEcsOrderId)).collect(Collectors.toList());
        for (AwardPojo awardPojo : awardPojos) {
            System.out.println(awardPojo);
        }

        final HashMap<String, String> map = new HashMap<>();
        map.put("1","11,112,13,14,51");
        map.put("2","22,28,27,26,23");
        List<Person> list = new ArrayList<Person>(){
            {
                map.forEach((str1, str2) -> Arrays.asList(str2.split(",")).forEach(str3 -> add(new Person(str3, str1))));
            }
        }.stream().sorted(Comparator.comparing(Person::getName)).collect(Collectors.toList());
        for (Person person : list) {
            System.out.println(person);
        }


    }
    @Test
    public void getHeat(){
        bikeService.getHeat();
        String [] strings={"11","22","33"};

    }
}

