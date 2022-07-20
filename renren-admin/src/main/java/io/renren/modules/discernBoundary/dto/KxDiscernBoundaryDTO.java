package io.renren.modules.discernBoundary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 识别边界标定
 *
 * @author cxy
 * @since 3.0 2022-07-20
 */
@Data
@ApiModel(value = "识别边界标定")
public class KxDiscernBoundaryDTO implements Serializable {
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
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;
    @ApiModelProperty(value = "相机名称")
    private String cameraName;
    @ApiModelProperty(value = "预置位")
    private String presetNo;
    @ApiModelProperty(value = "照片宽度")
    private Integer pictureWidth;
    @ApiModelProperty(value = "照片高度")
    private Integer pictureHeight;
    @ApiModelProperty(value = "基本信息")
    private Object boundaryCoordinates;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "识别码")
    private String sessionTime;
    @ApiModelProperty(value = "是否可用")
    private String enable;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否删除")
    private String deleted;
    @ApiModelProperty(value = "删除者")
    private Long deleter;
    @ApiModelProperty(value = "删除时间")
    private Date deletedTime;


    @ApiModelProperty(value = "图片名称")
    private String pictureName;
    @ApiModelProperty(value = "图片缩略图")
    private String picturUrl;
    @ApiModelProperty(value = "站点名称")
    private List<String> picNameList;


}