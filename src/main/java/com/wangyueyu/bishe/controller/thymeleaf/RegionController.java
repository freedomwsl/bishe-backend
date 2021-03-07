package com.wangyueyu.bishe.controller.thymeleaf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.entity.User;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import com.wangyueyu.bishe.mapper.ParkingRegionMapper;
import com.wangyueyu.bishe.service.HotParkingService;
import com.wangyueyu.bishe.service.ParkingRegionService;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.jasper.PageUtil;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.ReadableInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * description
 *
 * @author 25721 2021/01/14 22:46
 */
@Controller
public class RegionController {
    @Autowired
    private ParkingRegionService regionService;
    @Autowired
    private HotParkingService hotParkingService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/user/regionManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String itemManage(@PathVariable Integer pageCurrent, @PathVariable Integer pageSize,
                             @PathVariable Integer pageCount, Model model) {
        if (pageSize == 0) pageSize = 20;
        if (pageCurrent == 0) pageCurrent = 1;
        //创建page对象
        Page<ParkingRegion> page = new Page<>(pageCurrent, pageSize);
        //调用方法实现分页
        final QueryWrapper<ParkingRegion> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        regionService.page(page, wrapper);
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        long total = page.getTotal();//总记录数
        List<ParkingRegion> regionList = page.getRecords(); //数据list集合
        int rows = regionService.count(null);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        model.addAttribute("regionList", regionList);
        String pageHTML = PageUtil.getPageContent("regionManage_{pageCurrent}_{pageSize}_{pageCount}", pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        return "oa/regionManage";
    }

    /**
     * 根据id获取停车区域信息传给前端展示在地图
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/user/displayWithMap")
    public String displayWithMap(Integer id, Model model) {
        QueryWrapper<ParkingRegion> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_region_id", id);
        ParkingRegion region = regionService.getOne(wrapper);
        model.addAttribute("region", region);
        return "oa/displayWithLongLati";
    }

    /**
     * 添加停车区域
     * @param parkingRegion
     * @return
     */
    @ResponseBody
    @PostMapping("/user/addRegion")
    public R addRegion(@RequestBody ParkingRegion parkingRegion) {
        logger.info("{}", parkingRegion);
        regionService.saveToRedisAndMysql(parkingRegion);
        return R.success().message("添加成功");
    }

    /**
     * 根据终点获取周围的停车区域
     * @param lon
     * @param lat
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/map/getRegionsByEnd/{lon}/{lat}")
    public R getRegionsByEnd(@PathVariable Double lon, @PathVariable Double lat, HttpSession session) {
        final User user = (User)session.getAttribute("user");
        Integer id=0;
        if(user!=null){
            id = user.getId();
        }else{
            return R.error().message("请先登录");
        }
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(lon);
        doubles.add(lat);
        List<ParkingRegion> regions = regionService.getRegionsByEnd(doubles,id);
        return R.success().data("regions", regions);
    }

    /**
     * 模拟停车
     * @param session
     * @param lng
     * @param lat
     * @return
     */
    @ResponseBody
    @GetMapping("/map/stopBike/{lng}/{lat}")
    public R stopBike(HttpSession session,@PathVariable Double lng,@PathVariable Double lat){
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(lng);
        doubles.add(lat);
        final User user = (User)session.getAttribute("user");
        final Integer id = user.getId();
        String content=regionService.stopBike(doubles,id);
        return R.success().message(content);
    }

    /**
     * 需要热点停车点的数据，停车区域数据
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/showHotParking")
    public R showHotParking(){
        final HashMap<String, Object> map = new HashMap<>();
        final String time = RandomLocationUtil.getTime();
        List<HotParkingVO> list = hotParkingService.getHotParkingJoinPlace(time);
        map.put("hotParkingList",list);
        List<ParkingRegion> regionList=new ArrayList<>();
        for (HotParkingVO hotParkingVO : list) {
            final String parkingPlaceLongLati = hotParkingVO.getParkingPlaceLongLati();
            final List<Double> doubles = RandomLocationUtil.stringToDoubleList(parkingPlaceLongLati);
            List<ParkingRegion> regions = regionService.getRegionsByHotParkingPlace(doubles);
            regionList.addAll(regions);
        }
        map.put("regionList",regionList);
        return R.success().data(map);
    }
    @PostMapping("/addRegions")
    public String addRegionsByExcel(MultipartFile file) throws IOException, InvalidFormatException {
        Boolean b= regionService.saveRegionsByExcel(file);
        return "redirect:/user/regionManage_0_0_0";
    }
    @RequestMapping("/user/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename, HttpSession session) throws IOException {
        ServletContext servletContext = session.getServletContext();
        File file=new File("F:\\工作簿1.xlsx");
        //读文件
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes=new byte[fileInputStream.available()];
        fileInputStream.read(bytes) ;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition","attachment;filename"+filename);
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

        return responseEntity ;
    }
    @DeleteMapping("/user/regionDelete")
    @ResponseBody
    public R deleteRegion(@RequestParam Integer id){
        logger.info("{}",id);
        final ParkingRegion parkingRegion = new ParkingRegion();
        parkingRegion.setParkingRegionId(id);
        regionService.removeById(parkingRegion);
        regionService.removeRedis(id);
        return R.success();
    }

    /**
     * dashboard获取所有的数据
     * @return
     */
    @GetMapping("/getAll/{lng}/{lat}")
    @ResponseBody
    public R getAll(@PathVariable Double lng,@PathVariable Double lat){
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(lng);
        doubles.add(lat);
        Map<String,Object> allData = regionService.getAllByLngLat(doubles);
        return R.success().data(allData);
    }


}