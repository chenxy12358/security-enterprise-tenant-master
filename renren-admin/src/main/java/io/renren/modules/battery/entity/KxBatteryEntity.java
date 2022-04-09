package io.renren.modules.battery.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电池信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_battery")
public class KxBatteryEntity {
	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
	@TableId
	private Long id;
	/**
	* 租户编码
	*/
	@TableField(fill = FieldFill.INSERT)
	private Long tenantCode;
	/**
	* 创建者
	*/
	@TableField(fill = FieldFill.INSERT)
	private Long creator;
	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createDate;
	/**
	* 更新者
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateDate;
	/**
	* 设备id
	*/
	private Long deviceId;
	/**
	* 站点id
	*/
	private Long stationId;
	/**
	* 内容
	*/
	private Object content;
	/**
	* 属性
	*/
	private Object properties;
	/**
	* 电池编号
	*/
	private Long batteryId;
	/**
	* 剩余电量
	*/
	private BigDecimal batSoc;
	/**
	* 充电电压
	*/
	private BigDecimal chargeVotage;
	/**
	* 充电电流
	*/
	private BigDecimal chargeCurrent;
	/**
	* 输出电压
	*/
	private BigDecimal outputVotage;
	/**
	* 输出电流
	*/
	private String outputCurrent;
	/**
	* 电压等级
	*/
	private BigDecimal voltageLevel;
	/**
	* 充电状态
	*/
	private String chargeSwitch;
	/**
	* 备注
	*/
	private String remark;
	@TableField(exist=false)
	private String deviceName;
	@TableField(exist=false)
	private String sName;
	@TableField(exist=false)
	private String depName;
}