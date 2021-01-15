package com.wangyueyu.bishe.controller.thymeleaf;


import com.wangyueyu.bishe.util.jasper.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;


/**
 * 商品管理
 */
@Controller
public class ItemController {

//    @Autowired
//    private ItemMapper itemMapper;
//
//    @Autowired
//    private ItemCategoryMapper itemCategoryMapper;
//
//    @Autowired
//    private ReItemMapper reItemMapper;

    public static final String ROOT = "src/main/resources/static/img/item/";

//    MongoUtil mongoUtil = new MongoUtil();

    private final ResourceLoader resourceLoader;

    @Autowired
    public ItemController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

//    List<Item> itemList;

    File getFile = null;

    @RequestMapping("/user/itemManage_{pageCurrent}_{pageSize}_{pageCount}")
//    public String itemManage(Item item, @PathVariable Integer pageCurrent,
//                             @PathVariable Integer pageSize,
//                             @PathVariable Integer pageCount,
//                             Model model) {
    public String itemManage(){
//        if (pageSize == 0) pageSize = 50;
//        if (pageCurrent == 0) pageCurrent = 1;
//
//        int rows = itemMapper.count(item);
//        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
//        item.setStart((pageCurrent - 1) * pageSize);
//        item.setEnd(pageSize);
//        itemList = itemMapper.list(item);
//        for (Item i : itemList) {
//            i.setUpdatedStr(DateUtil.getDateStr(i.getUpdated()));
//        }
//        ItemCategory itemCategory = new ItemCategory();
//        itemCategory.setStart(0);
//        itemCategory.setEnd(Integer.MAX_VALUE);
//        List<ItemCategory> itemCategoryList = itemCategoryMapper.list(itemCategory);
//        Integer minPrice = item.getMinPrice();
//        Integer maxPrice = item.getMaxPrice();
//        model.addAttribute("itemCategoryList", itemCategoryList);
//        model.addAttribute("itemList", itemList);
//        String pageHTML = PageUtil.getPageContent("itemManage_{pageCurrent}_{pageSize}_{pageCount}?title=" + item.getTitle() + "&cid=" + item.getCid() + "&minPrice" + minPrice + "&maxPrice" + maxPrice, pageCurrent, pageSize, pageCount);
//        model.addAttribute("pageHTML", pageHTML);
//        model.addAttribute("item", item);
        return "item/itemManage";
    }

    @RequestMapping("/user/download1")
    public void postItemExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //导出excel
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("id", "商品id");
        fieldMap.put("title", "商品标题");
        fieldMap.put("sellPoint", "商品卖点");
        fieldMap.put("price", "商品价格");
        fieldMap.put("num", "库存数量");
        fieldMap.put("image", "商品图片");
        fieldMap.put("cid", "所属类目，叶子类目");
        fieldMap.put("status", "商品状态，1-正常，2-下架，3-删除");
        fieldMap.put("created", "创建时间");
        fieldMap.put("updated", "更新时间");
        String sheetName = "商品管理报表";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=ItemManage.xls");//默认Excel名称
        response.flushBuffer();
        OutputStream fos = response.getOutputStream();
        try {
//            ExcelUtil.listToExcel(itemList, fieldMap, sheetName, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String imageName = null;


}
