package io.renren.modules.battery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* 电池信息 
*
* @author cxy 
* @since 3.0 2022-02-21
*/
@Data
@ApiModel(value = "电池信息 ")
public class KxBatteryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateDate;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "站点id")
    private Long stationId;
    @ApiModelProperty(value = "内容")
    private Object content;
    @ApiModelProperty(value = "属性")
    private Object properties;
    @ApiModelProperty(value = "电池编号")
    private Long batteryId;
    @ApiModelProperty(value = "剩余电量")
    private BigDecimal batSoc;
    @ApiModelProperty(value = "充电电压")
    private BigDecimal chargeVotage;
    @ApiModelProperty(value = "充电电流")
    private BigDecimal chargeCurrent;
    @ApiModelProperty(value = "输出电压")
    private BigDecimal outputVotage;
    @ApiModelProperty(value = "输出电流")
    private String outputCurrent;
    @ApiModelProperty(value = "电压等级")
    private BigDecimal voltageLevel;
    @ApiModelProperty(value = "充电状态")
    private String chargeSwitch;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "站点名称")
    private String sName;
    @ApiModelProperty(value = "机构名称")
    private String depName;

}