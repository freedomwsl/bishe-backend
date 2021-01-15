package com.wangyueyu.bishe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbUser对象", description="")
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userName")
    private String userName;

    private String password;

    @TableField("realName")
    private String realName;

    private String business;

    private String email;

    @TableField("headPicture")
    private String headPicture;

    @TableField("addDate")
    private Date addDate;

    @TableField("updateDate")
    private Date updateDate;

    @ApiModelProperty(value = "1：正常 2：冻结 3：删除")
    private Integer state;


}
