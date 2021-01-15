package com.wangyueyu.bishe.controller.baseController;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangyueyu.bishe.entity.Bike;
import com.wangyueyu.bishe.entity.ParkingRegion;
import com.wangyueyu.bishe.service.ParkingRegionService;
import com.wangyueyu.bishe.util.R;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/parking-region")
public class ParkingRegionController {
    @Autowired
    private ParkingRegionService parkingRegionService;
    @GetMapping("list/{current}/{limit}")
    public R getByPage(@PathVariable Integer current,@PathVariable Integer limit){
        Page<ParkingRegion> page = new Page<>(current, limit);
        parkingRegionService.page(page, null);
        long total = page.getTotal();
        List<ParkingRegion> records = page.getRecords();
        return R.success().data("total",total).data("rows",records);
    }
    @GetMapping("getAll")
    public R getAll(){
        List<ParkingRegion> list = parkingRegionService.list(null);
        return R.success().data("data",list);
    }
    @PutMapping("/insert")
    public R insertParkingRegion(@RequestBody ParkingRegion parkingRegion){
        return (parkingRegionService.save(parkingRegion))?R.success().message("添加成功"):R.error().message("添加失败");

    }
    @DeleteMapping("/{parkingRegionId")
    public R delete(@RequestBody ParkingRegion parkingRegion){

        return (parkingRegionService.removeById(parkingRegion))?R.success().message("删除成功"):R.error().message("删除失败");

    }

}

