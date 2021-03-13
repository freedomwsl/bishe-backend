package com.wangyueyu.bishe.controller.thymeleaf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.*;
import com.wangyueyu.bishe.entity.vo.AddBikeVo;
import com.wangyueyu.bishe.entity.vo.HeatVo;
import com.wangyueyu.bishe.entity.vo.HotParkingDTO;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import com.wangyueyu.bishe.service.BikeService;
import com.wangyueyu.bishe.service.HotParkingService;
import com.wangyueyu.bishe.service.ParkingPlaceService;
import com.wangyueyu.bishe.service.UcenterMemberService;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.jasper.PageUtil;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * description
 *
 * @author 25721 2021/01/14 10:30
 */
@Controller
public class Bike1Controller {
    @Autowired
    private BikeService bikeService;
    @Autowired
    private ParkingPlaceService parkingPlaceService;
    @Autowired
    private HotParkingService hotParkingService;
    @Autowired
    private UcenterMemberService ucenterMemberService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/user/bikeManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String itemManage(@PathVariable Integer pageCurrent, @PathVariable Integer pageSize,
                             @PathVariable Integer pageCount, Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;
        //创建page对象
        Page<Bike> page = new Page<>(pageCurrent,pageSize);
        //调用方法实现分页
        bikeService.page(page,null);
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        long total = page.getTotal();//总记录数
        List<Bike> bikeList = page.getRecords(); //数据list集合
        int rows = bikeService.count(null);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        model.addAttribute("bikeList", bikeList);
        String pageHTML = PageUtil.getPageContent("bikeManage_{pageCurrent}_{pageSize}_{pageCount}" , pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        return "oa/bikeManage";
    }

    @GetMapping("/user/parkingPlaceManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String parkingPlaceManage(@PathVariable Integer pageCurrent, @PathVariable Integer pageSize,
                             @PathVariable Integer pageCount, Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;
        //创建page对象
        Page<ParkingPlace> page = new Page<>(pageCurrent,pageSize);
        //调用方法实现分页
        parkingPlaceService.page(page,null);
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        long total = page.getTotal();//总记录数
        List<ParkingPlace> parkingPlaces = page.getRecords(); //数据list集合
        int rows = parkingPlaceService.count(null);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        model.addAttribute("parkingPlaces", parkingPlaces);
        String pageHTML = PageUtil.getPageContent("parkingPlaceManage_{pageCurrent}_{pageSize}_{pageCount}" , pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        return "oa/parkingPlaceManage";
    }
    @GetMapping("/user/hotParkingPlaceManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String hotParkingPlaceManage(@PathVariable Integer pageCurrent, @PathVariable Integer pageSize,
                                     @PathVariable Integer pageCount, Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;
        //创建page对象
        Page<HotParking> page = new Page<>(pageCurrent,pageSize);
        //调用方法实现分页
        hotParkingService.page(page,null);
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        long total = page.getTotal();//总记录数
        List<HotParking> hotParkings = page.getRecords(); //数据list集合
        int rows = parkingPlaceService.count(null);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        model.addAttribute("hotParkings", hotParkings);
        String pageHTML = PageUtil.getPageContent("hotParkingPlaceManage_{pageCurrent}_{pageSize}_{pageCount}" , pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        return "oa/hotParkingPlaceManage";
    }
    @GetMapping("/user/bikeEdit")
    public String itemEditGet(Model model, @RequestParam Integer id) {

        if (id != 0) {
            QueryWrapper<Bike> wrapper = new QueryWrapper<>();
            wrapper.eq("bike_id",id);
            Bike bike1 = bikeService.getOne(wrapper);
            model.addAttribute("bike", bike1);
        }
        return "oa/bikeEdit";
    }
    @PostMapping("/user/bikeEdit")
    public String itemEditPost(Bike bike) {
        if (bike.getBikeId() != 0) {
            bikeService.updateById(bike);
        } else {
            bikeService.save(bike);
        }
        return "redirect:itemManage_0_0_0";
    }
    @ResponseBody
    @PostMapping("/user/bikeEditState")
    public String itemEditState(Integer id) {
        bikeService.removeById(id);
        return "成功";
    }
    @ResponseBody
    @GetMapping("/test/getRandomBike")
    public R getRandomBike(){
        QueryWrapper<Bike> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("long_lati");
        wrapper.eq("is_using","N");
        List<Bike> list = bikeService.list(wrapper);
        Random random = new Random();
        int i = random.nextInt(list.size());
        Bike bike = list.get(i);
        logger.info("getRandomBike");
        return R.success().data("bike",bike);
    }
    @ResponseBody
    @PostMapping("/bike/addBike")
    public R addBike(@RequestBody Bike bike){
        bikeService.saveBike(bike);
        return R.success();
    }
    @ResponseBody
    @GetMapping("/bike/showBike/{lng}/{lat}")
    public R getBikes(@PathVariable Double lng,@PathVariable Double lat){
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(lng);
        doubles.add(lat);
        List<Bike> bikes = bikeService.getBikesBylnglat(doubles);
        return R.success().data("bikes", bikes);
    }
    @ResponseBody
    @GetMapping("/bike/showUselessBike")
    public R getUselessBike(HttpSession session){
        final Object user = session.getAttribute("user");
        System.out.println(user);
        List<Bike> list = bikeService.getBikesByTime();
        return R.success().data("bikes",list);
    }

    /**
     * 停车点新增
     * @param model
     * @return
     */
    @GetMapping("/user/parkingPlaceEdit")
    public String itemEditGet(Model model) {
        final Bike bike = new Bike();
        model.addAttribute("bike",bike);
        return "oa/parkingPlaceEdit";
    }
    @PostMapping("/user/parkingPlaceEdit")
    public String itemEditPost(Model model,ParkingPlace parkingPlace) {
        parkingPlaceService.save(parkingPlace);
        return "redirect:parkingPlaceManage_0_0_0";
    }

    /**
     * 停车热点新增
     * @param model
     * @return
     */
    @GetMapping("/user/hotParkingPlaceEdit")
    public String hotParking(Model model) {

        model.addAttribute("hotParking",new HotParking());
        final List<ParkingPlace> list = parkingPlaceService.list(null);
        model.addAttribute("parkingPlaces",list);
        return "oa/hotParkingPlaceEdit";
    }
    @PostMapping("/user/hotParkingPlaceEdit")
    public String hotParkingPost(Model model, HotParking hotParking) {
        hotParkingService.save(hotParking);
        return "redirect:hotParkingPlaceManage_0_0_0";
    }

    @PostMapping("/user/addBikes")
    public String addBikes(Model model,AddBikeVo addBikeVo){
        logger.info("addbikevo: {}",addBikeVo);
        final ArrayList<Bike> bikes = new ArrayList<>();
        for(int i=0;i<addBikeVo.getBikeNumber();i++){
            Bike bike = new Bike();
            bike.setLongLati(addBikeVo.getBikeLocation());
            bike.setIsUsing("N");
            bike.setBeginTime(new Date());
            bike.setLastUse(new Date());
            bike.setUsedTimes(0);
            bikes.add(bike);
        }
        final boolean b = bikeService.saveBatch(bikes);
        return "redirect:bikeManage_0_0_0";
    }

    @GetMapping("/user/sortUser")
    @ResponseBody
    public R sortUser(){
        final QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("recommend_times");
        final List<UcenterMember> list = ucenterMemberService.list(wrapper);
        logger.info("{}",list);
        return R.success().data("list",list);
    }
    @GetMapping("/getHeat/{stop}")
    @ResponseBody
    public R getHeat(@PathVariable String stop){
        List<HeatVo> heatVoList = bikeService.getHeat(stop);
        return R.success().data("heatVo",heatVoList);
    }
    @GetMapping("/saveToRedis/{lng}/{lat}")
    @ResponseBody
    public R saveToRedis(@PathVariable Double lng,@PathVariable Double lat){
        bikeService.saveToRedis(lng,lat);
        return R.success();
    }
}
