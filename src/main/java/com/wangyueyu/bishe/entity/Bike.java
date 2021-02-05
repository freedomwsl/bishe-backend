package com.wangyueyu.bishe.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;
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
@ApiModel(value="Bike对象", description="")
@TableName("bike")
public class Bike implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单车id")
    @TableId(value = "bike_id", type = IdType.AUTO)
    private Integer bikeId;

    @ApiModelProperty(value = "投入使用日期")
    private Date beginTime;

    @ApiModelProperty(value = "被使用次数")
    private Integer usedTimes;

    @ApiModelProperty(value = "单车位置（经纬度）")
    private String longLati;

    @ApiModelProperty(value = "上次被使用时间点")
    private Date lastUse;

    @ApiModelProperty(value = "是否正在被使用")
    private String isUsing;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
