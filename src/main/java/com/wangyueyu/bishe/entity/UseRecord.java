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
@ApiModel(value="UseRecord对象", description="")
public class UseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "使用记录表id")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "单车id")
    private Integer bikeId;

    @ApiModelProperty(value = "起始位置（经纬度）")
    private String beginLongLati;

    @ApiModelProperty(value = "终点位置（经纬度）")
    private String finalLongLati;

    @ApiModelProperty(value = "是否停放在推荐位置")
    private String isRecommend;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
