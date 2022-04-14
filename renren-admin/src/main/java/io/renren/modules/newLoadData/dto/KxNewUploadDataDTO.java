package io.renren.modules.newLoadData.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 最新上传数据
*
* @author WEI 
* @since 3.0 2022-04-14
*/
@Data
@ApiModel(value = "最新上传数据")
public class KxNewUploadDataDTO implements Serializable {
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
    private String type;

}