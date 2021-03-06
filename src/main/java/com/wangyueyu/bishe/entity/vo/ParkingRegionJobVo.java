package com.wangyueyu.bishe.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author yueyu.wang@hand-china.com 2021/02/20 11:13
 */
@Data
public class ParkingRegionJobVo {
    @ApiModelProperty(value = "记录id")
    private Integer recordId;
    @ApiModelProperty(value = "停车区域id")
    private Integer parkingRegionId;

    @ApiModelProperty(value = "停车地点名称")
    private String parkingPlaceName;

    @ApiModelProperty(value = "停车点位置（经纬度）")
    private String parkingPlaceLongLati;
}
