package com.wangyueyu.bishe.controller.baseController;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingPlace;
import com.wangyueyu.bishe.service.ParkingPlaceService;
import com.wangyueyu.bishe.util.R;
import com.wangyueyu.bishe.util.jasper.PageUtil;
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
@RequestMapping("/parking-place")
public class ParkingPlaceController {
    @Autowired
    private ParkingPlaceService parkingPlaceService;
    @GetMapping("/getAll")
    public R getAll(){
        List<ParkingPlace> list = parkingPlaceService.list(null);
        return R.success().data("data",list);
    }
    @PutMapping("/add")
    public R addParkingPlace(@RequestBody ParkingPlace parkingPlace){
        return (parkingPlaceService.save(parkingPlace))?R.success().message("添加成功"):R.error().message("添加失败");
    }
    @DeleteMapping("delete")
    public R deleteParkingPlace(@RequestBody ParkingPlace parkingPlace){
        return (parkingPlaceService.removeById(parkingPlace))?R.success().message("添加成功"):R.error().message("添加失败");
    }


}

