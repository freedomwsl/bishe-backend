package com.wangyueyu.bishe.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ParkingPlace对象", description="")
public class ParkingPlace implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "停车地点id")
    @TableId(value = "parking_place_id", type = IdType.AUTO)
    private Integer parkingPlaceId;

    @ApiModelProperty(value = "停车地点名称")
    private String parkingPlaceName;

    @ApiModelProperty(value = "停车点位置（经纬度）")
    private String parkingPlaceLongLati;

    @ApiModelProperty(value = "停车点类型")
    private String parkingPlaceType;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
