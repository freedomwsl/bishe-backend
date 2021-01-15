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
@ApiModel(value="Message对象", description="")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "站内信id")
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否已被阅读")
    private String isRead;

    @ApiModelProperty(value = "站内信内容")
    private String messageContent;

    @ApiModelProperty(value = "发信时间")
    private Date sendTime;

    @ApiModelProperty(value = "信类型")
    private String messageType;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
