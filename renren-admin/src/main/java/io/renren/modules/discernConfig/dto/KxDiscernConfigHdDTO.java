package io.renren.modules.discernConfig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 识别参数配置
*
* @author cxy 
* @since 3.0 2022-03-25
*/
@Data
@ApiModel(value = "识别参数配置")
public class KxDiscernConfigHdDTO implements Serializable {
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
    @ApiModelProperty(value = "方案名称")
    private String schemeNo;
    @ApiModelProperty(value = "方案路径")
    private String schemePath;
    @ApiModelProperty(value = "图片尺寸")
    private String picSize;
    @ApiModelProperty(value = "识别参数配置")
    private Object distinguishConfig;
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "设备类型id")
    private Long deviceId;
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;
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
    @ApiModelProperty(value = "ai配置信息")
    private List<KxAIPzVO> discernList ;

}