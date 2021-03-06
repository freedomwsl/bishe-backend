package com.wangyueyu.bishe.service.impl;

import com.wangyueyu.bishe.entity.HotParking;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import com.wangyueyu.bishe.mapper.HotParkingMapper;
import com.wangyueyu.bishe.service.HotParkingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Service
public class HotParkingServiceImpl extends ServiceImpl<HotParkingMapper, HotParking> implements HotParkingService {
    @Autowired
    private HotParkingMapper hotParkingMapper;
    @Override
    public List<HotParkingVO> getHotParkingJoinPlace(Integer hour) {
        return hotParkingMapper.getHotParkingJoinPlace(hour-1);
    }
}
