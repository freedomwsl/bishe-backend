package com.wangyueyu.bishe.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value="HotParking对象", description="")
public class HotParking implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "热点表id")
    @TableId(value = "hot_parking_id", type = IdType.AUTO)
    private Integer hotParkingId;

    @ApiModelProperty(value = "关联的外键，停车点id")
    private Integer parkingPlaceId;

    @ApiModelProperty(value = "热点开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String hotBeginTime;

    @ApiModelProperty(value = "热点结束时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String hotFinalTime;

    @ApiModelProperty(value = "热点原因")
    private String hotReason;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
