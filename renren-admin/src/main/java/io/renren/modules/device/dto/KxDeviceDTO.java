package io.renren.modules.device.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 设备信息 
*
* @author cxy 
* @since 3.0 2022-02-16
*/
@Data
@ApiModel(value = "设备信息 ")
public class KxDeviceDTO implements Serializable {
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
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "序号")
    private String serialNo;
    @ApiModelProperty(value = "制造厂")
    private String manufactory;
    @ApiModelProperty(value = "协议类型")
    private String proType;
    @ApiModelProperty(value = "协议")
    private Object protocols;
    @ApiModelProperty(value = "基本信息")
    private Object baseInfo;
    @ApiModelProperty(value = "状态")
    private Object status;
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "设备类型id")
    private Long deviceTypeId;
    @ApiModelProperty(value = "设备模式id")
    private Long deviceModelId;
    @ApiModelProperty(value = "任务id")
    private Long scheduleJobId;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否删除")
    private String deleted;
    @ApiModelProperty(value = "是否可用")
    private String enable;
    @ApiModelProperty(value = "删除者")
    private Long deleter;
    @ApiModelProperty(value = "删除时间")
    private Date deletedTime;
    @ApiModelProperty(value = "编号")
    private String deptName;
    @ApiModelProperty(value = "机构名称")
    private String orgName;
    @ApiModelProperty(value = "站点名称")
    private String stationName;
    @ApiModelProperty(value = "IP")
    private String ip;

}