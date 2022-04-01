package io.renren.modules.scheduleItem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 计划任务拍照结果
*
* @author cxy 
* @since 3.0 2022-03-05
*/
@Data
@ApiModel(value = "计划任务拍照结果")
public class KxScheduleItemDTO implements Serializable {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date updateDate;
    @ApiModelProperty(value = "设备编码")
    private String deviceNo;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "计划任务id")
    private Long scheduleJobId;
    @ApiModelProperty(value = "类型")
    private String itemType;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "信号")
    private String signalType;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否删除")
    private String deleted;

    @ApiModelProperty(value = "图片名称")
    private String pictureName;
    @ApiModelProperty(value = "图片缩略图")
    private String picturUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "删除时间")
    private String fileTime;
    @ApiModelProperty(value = "设备编号")
    private String deviceNum;
    @ApiModelProperty(value = "站点名称")
    private String stationName;
    @ApiModelProperty(value = "站点名称")
    private List<String> picNameList;

}