package com.wangyueyu.bishe.service;

import com.wangyueyu.bishe.entity.Bike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
public interface BikeService extends IService<Bike> {

    void saveBike(Bike bike);
}
