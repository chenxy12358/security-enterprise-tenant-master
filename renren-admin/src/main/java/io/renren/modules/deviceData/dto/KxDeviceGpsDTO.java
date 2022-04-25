package io.renren.modules.deviceData.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 设备位置数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Data
@ApiModel(value = "设备位置数据")
public class KxDeviceGpsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "经度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;
    @ApiModelProperty(value = "东/西经")
    private String eastWest;
    @ApiModelProperty(value = "南/北纬")
    private String northSouth;
    @ApiModelProperty(value = "海波（米）")
    private String altitude;
    @ApiModelProperty(value = "精度")
    private String hdop;
    @ApiModelProperty(value = "传感器编号")
    private Long sensorNo;
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