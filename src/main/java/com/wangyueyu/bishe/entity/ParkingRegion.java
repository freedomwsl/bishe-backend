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
import org.springframework.data.annotation.Transient;

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
@ApiModel(value="ParkingRegion对象", description="")
public class ParkingRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "停车区域id")
    @TableId(value = "parking_region_id", type = IdType.AUTO)
    private Integer parkingRegionId;

    @ApiModelProperty(value = "停车区域名称")
    private String parkingRegionName;

    @ApiModelProperty(value = "停车区域容量")
    private Integer parkingRegionCapacity;

    @ApiModelProperty(value = "停车区域位置信息，一串经纬度以;隔开")
    private String parkingRegionLongLati;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;
    @TableField(exist = false)
    @ApiModelProperty(value = "街道")
    private String street;

    @TableField(exist = false)
    @ApiModelProperty(value = "号码")
    private String streetNumber;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
