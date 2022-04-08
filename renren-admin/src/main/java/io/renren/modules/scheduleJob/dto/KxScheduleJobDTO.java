package io.renren.modules.scheduleJob.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 计划任务
*
* @author cxy 
* @since 3.0 2022-02-27
*/
@Data
@ApiModel(value = "计划任务")
public class KxScheduleJobDTO implements Serializable {
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
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "设备类型id")
    private Long deviceTypeId;
    @ApiModelProperty(value = "设备模式id")
    private Long deviceModelId;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "事件类型")
    private String itemType;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    @ApiModelProperty(value = "是否删除")
    private String deleted;
    @ApiModelProperty(value = "是否可用")
    private String enable;
    @ApiModelProperty(value = "删除者")
    private Long deleter;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "删除时间")
    private Date deletedTime;
    @ApiModelProperty(value = "目标名称")
    private String dest;
    @ApiModelProperty(value = "图片大小")
    private String pictureSize;
    @ApiModelProperty(value = "相机名字")
    private String camera;
    @ApiModelProperty(value = "周期")
    private String[] period;
//    private String period;
    @ApiModelProperty(value = "预置位")
    private Integer presetId;
    @ApiModelProperty(value = "时间选择类型")
    private String timeType;
    @ApiModelProperty(value = "时间间隔（分钟）")
    private Integer timeInterval;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="HH:mm:ss")
    @ApiModelProperty(value = "工作时间")
    private Date workTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="HH:mm:ss")
    @ApiModelProperty(value = "起始时间")
    private Date startTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}