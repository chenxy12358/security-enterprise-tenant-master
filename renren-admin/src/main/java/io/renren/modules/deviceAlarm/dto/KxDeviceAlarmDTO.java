package io.renren.modules.deviceAlarm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 可视化告警
*
* @author cxy 
* @since 3.0 2022-02-25
*/
@Data
@ApiModel(value = "可视化告警")
public class KxDeviceAlarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "拍照时间")
    private Date pictureDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @ApiModelProperty(value = "创建时间")
    private Date updateDate;
    @ApiModelProperty(value = "设备编码")
    private String deviceNo;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "级别")
    private Integer level;
    @ApiModelProperty(value = "处理类型")
    private String handleType;
    @ApiModelProperty(value = "信号")
    private String signalType;
    @ApiModelProperty(value = "发送信息")
    private Object senderInfo;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "备注")
    private String remark;

}