package com.wangyueyu.bishe.controller.thymeleaf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.service.BikeService;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.jasper.PageUtil;
import com.wangyueyu.bishe.util.redisUtil.RandomLocationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * description
 *
 * @author 25721 2021/01/14 10:30
 */
@Controller
public class Bike1Controller {
    @Autowired
    private BikeService bikeService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/user/bikeManage_{pageCurrent}_{pageSize}_{pageCount}")
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
        Bike bike = list.get(0);
        logger.info("getRandomBike");
        return R.success().data("bike",bike);
    }
    @ResponseBody
    @PostMapping("/bike/addBike")
    public R addBike(@RequestBody Bike bike){
        bikeService.saveBike(bike);
        return R.success();
    }

}
