package com.wangyueyu.bishe.controller.thymeleaf;

import com.wangyueyu.bishe.service.BikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * description
 *
 * @author 25721 2021/01/13 15:12
 */
@Controller
public class RedirectController {
    @Autowired
    private BikeService bikeService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/map/huizhi")
    public String loginGet(Model model) {
        return "map/huiZhi";
    }
    @GetMapping("/map/changchunmap")
    public String changchun(Model model) {
        logger.info("#############");
        return "/map/changchunmap";
    }
    @GetMapping("/map/huatu")
    public String huatu(Model model) {
        logger.info("#############");
        return "/map/根据一串坐标绘制";
    }
    @GetMapping("/user/addRegion")
    public String addRegion(Model model) {
        logger.info("#############");
        return "/map/huiZhi";
    }
    @GetMapping("/map/ceshi")
    public String ceshi(){
        return "/map/ceshi";
    }
    @GetMapping("/map/ceshi2")
    public String ceshi2(){
        return "/map/ceshi2";
    }
    @GetMapping("/map/showBike")
    public String showBike(){
        return "/map/showBike";
    }
}
