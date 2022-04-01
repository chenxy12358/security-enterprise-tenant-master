package io.renren.modules.gas.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 燃气信息 
*
* @author WEI 
* @since 3.0 2022-02-18
*/
@Data
@ApiModel(value = "燃气信息 ")
public class KxGasDataDTO implements Serializable {
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
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "浓度单位")
    private String unit;
    @ApiModelProperty(value = "类型")
    private String gasType;
    @ApiModelProperty(value = "传感器编号")
    private Long sensorId;
    @ApiModelProperty(value = "报警状态")
    private String alarmStatus;
    @ApiModelProperty(value = "一级告警设定值")
    private Double firsLevelAlarm;
    @ApiModelProperty(value = "当前浓度值")
    private Double concentrationValue;
    @ApiModelProperty(value = "二级告警设定值")
    private Double secondaryLevelAlarm;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "备注")
    private String remark;
    private String deviceName;
    private String stationName;
    private String deptName;



}