package io.renren.modules.AppScores.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 积分记录
*
* @author WEI 
* @since 3.0 2022-08-13
*/
@Data
@ApiModel(value = "积分记录")
public class AppScoresDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "环保积分")
    private Integer normalScore;
    @ApiModelProperty(value = "实践积分")
    private Integer specialScore;
    @ApiModelProperty(value = "积分产生类型")
    private String type;
}