package io.renren.modules.netWorkData.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 网络状态信息 
*
* @author cxy 
* @since 3.0 2022-02-19
*/
@Data
@ApiModel(value = "网络状态信息 ")
public class KxNetWorkStateDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "设备id")
    private Long deviceid;
    @ApiModelProperty(value = "桩点名称")
    private Long stationid;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "网卡名称")
    private String nicName;
    @ApiModelProperty(value = "网络模式")
    private String accessTech;
    @ApiModelProperty(value = "运营商")
    private String operatorName;
    @ApiModelProperty(value = "信号质量")
    private String signalQuality;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "站点名称")
    private String sName;
    @ApiModelProperty(value = "机构名称")
    private String depName;

}