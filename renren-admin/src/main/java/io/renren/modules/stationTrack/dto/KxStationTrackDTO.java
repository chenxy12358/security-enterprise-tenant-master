package io.renren.modules.stationTrack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* 桩点位移轨迹
*
* @author cxy 
* @since 3.0 2022-04-19
*/
@Data
@ApiModel(value = "桩点位移轨迹")
public class KxStationTrackDTO implements Serializable {
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
    @ApiModelProperty(value = "桩点id")
    private Long stationId;
    @ApiModelProperty(value = "经度")
    private String lng;
    @ApiModelProperty(value = "维度")
    private String lat;
    @ApiModelProperty(value = "位置")
    private String position;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否删除")
    private String deleted;

}