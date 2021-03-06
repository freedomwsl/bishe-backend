package com.wangyueyu.bishe.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * description
 *
 * @author yueyu.wang@hand-china.com 2021/03/06 16:12
 */
@Data
public class HotParkingDTO {
    @ApiModelProperty(value = "停车地点id")
    private Integer parkingPlaceId;

    @ApiModelProperty(value = "热点开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String hotBeginTime;

    @ApiModelProperty(value = "热点结束时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String hotFinalTime;

    @ApiModelProperty(value = "热点原因")
    private String hotReason;
}
