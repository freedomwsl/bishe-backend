package com.wangyueyu.bishe.controller.thymeleaf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.service.ParkingRegionService;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.jasper.PageUtil;
import org.joda.time.ReadableInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description
 *
 * @author 25721 2021/01/14 22:46
 */
@Controller
public class RegionController {
    @Autowired
    private ParkingRegionService regionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/user/regionManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String itemManage(@PathVariable Integer pageCurrent, @PathVariable Integer pageSize,
                             @PathVariable Integer pageCount, Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;
        //创建page对象
        Page<ParkingRegion> page = new Page<>(pageCurrent,pageSize);
        //调用方法实现分页
        regionService.page(page,null);
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        long total = page.getTotal();//总记录数
        List<ParkingRegion> regionList = page.getRecords(); //数据list集合
        int rows = regionService.count(null);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        model.addAttribute("regionList", regionList);
        String pageHTML = PageUtil.getPageContent("regionManage_{pageCurrent}_{pageSize}_{pageCount}" , pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        return "oa/regionManage";
    }
    @GetMapping("/user/displayWithMap")
    public String displayWithMap(Integer id,Model model){
        QueryWrapper<ParkingRegion> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_region_id", id);
        ParkingRegion region = regionService.getOne(wrapper);
        model.addAttribute("region",region);
        return "oa/displayWithLongLati";
    }
    @ResponseBody
    @PostMapping("/map/addRegion")
    public R addRegion(@RequestBody ParkingRegion parkingRegion){
        logger.info("{}",parkingRegion);
        return R.success().message("添加成功");
    }
}
