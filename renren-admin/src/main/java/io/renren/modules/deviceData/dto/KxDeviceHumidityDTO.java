package io.renren.modules.deviceData.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 设备湿度数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Data
@ApiModel(value = "设备湿度数据")
public class KxDeviceHumidityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
    @ApiModelProperty(value = "当前值")
    private Double currentValue;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "传感器编号")
    private Long sensorNo;
    @ApiModelProperty(value = "报警状态")
    private String alarmStatus;
    @ApiModelProperty(value = "一级告警设定值")
    private Double firsLevelAlarm;
    @ApiModelProperty(value = "二级告警设定值")
    private Double secondaryLevelAlarm;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "备注")
    private String remark;
    private String deviceName;
    private String stationName;
    private String deptName;
    @ApiModelProperty(value = "创建者")
    private String creatorName;
    @ApiModelProperty(value = "更新者")
    private String updaterName;

}