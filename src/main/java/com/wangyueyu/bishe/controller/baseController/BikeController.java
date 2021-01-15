package com.wangyueyu.bishe.controller.baseController;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.service.BikeService;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.jasper.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */

@RestController
@RequestMapping("/bike")
public class BikeController {
    private final Logger logger = LoggerFactory.getLogger(BikeController.class);
    @Autowired
    private BikeService bikeService;
    @GetMapping("/list/{current}/{limit}")
    public R bikeList(@PathVariable Integer current, @PathVariable Integer limit){
        Page<Bike> page = new Page<>(current, limit);
        bikeService.page(page, null);
        long total = page.getTotal();
        List<Bike> records = page.getRecords();
        return R.success().data("total",total).data("rows",records);
    }
    @PostMapping("/add")
    public R addBike(@RequestBody Bike bike){
        return (bikeService.save(bike))?R.success().message("添加成功"):R.error().message("添加失败");
    }
    @DeleteMapping("/delete")
    public R deleteBike(@RequestBody Bike bike){
        return (bikeService.removeById(bike))?R.success().message("删除成功"):R.error().message("删除失败");
    }
    @GetMapping("getAll")
    public R getAll(){
        List<Bike> list = bikeService.list(null);
        return R.success().data("data",list);
    }


}

