package com.wangyueyu.bishe.mapper;

import com.wangyueyu.bishe.entity.HotParking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangyueyu.bishe.entity.vo.HotParkingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Mapper
public interface HotParkingMapper extends BaseMapper<HotParking> {

    List<HotParkingVO> getHotParkingJoinPlace(String hour);
}
