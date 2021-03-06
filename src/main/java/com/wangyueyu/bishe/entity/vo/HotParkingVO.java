package com.wangyueyu.bishe.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * description
 *
 * @author yueyu.wang@hand-china.com 2021/02/05 20:43
 */
@Data
public class HotParkingVO {
    @ApiModelProperty(value = "停车地点id")
    private Integer parkingPlaceId;
    @ApiModelProperty(value = "热点表id")
    private Integer hotParkingId;

    @ApiModelProperty(value = "停车地点名称")
    private String parkingPlaceName;

    @ApiModelProperty(value = "停车点位置（经纬度）")
    private String parkingPlaceLongLati;

    @ApiModelProperty(value = "停车点类型")
    private String parkingPlaceType;

    @ApiModelProperty(value = "热点开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String hotBeginTime;

    @ApiModelProperty(value = "热点结束时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String hotFinalTime;

    @ApiModelProperty(value = "热点原因")
    private String hotReason;
}
