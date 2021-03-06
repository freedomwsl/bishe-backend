package com.wangyueyu.bishe.service;

import com.wangyueyu.bishe.entity.HotParking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
public interface HotParkingService extends IService<HotParking> {

    List<HotParkingVO> getHotParkingJoinPlace(String hour);
}
