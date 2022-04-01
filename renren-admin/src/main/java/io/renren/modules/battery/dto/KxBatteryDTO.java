package io.renren.modules.battery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 电池信息 
*
* @author zhengweicheng 
* @since 3.0 2022-02-08
*/
@Data
@ApiModel(value = "电池信息 ")
public class KxBatteryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @ApiModelProperty(value = "创建时间")
    private Date updateDate;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "备注")
    private String remark;

}