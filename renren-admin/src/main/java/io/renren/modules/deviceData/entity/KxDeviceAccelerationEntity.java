package io.renren.modules.deviceData.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备加速度数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_device_acceleration")
public class KxDeviceAccelerationEntity {
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
	private Date createDate;
	/**
	* 更新者
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
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
	* 当前值
	*/
	private Double currentValue;
	/**
	* 单位
	*/
	private String unit;
	/**
	* 传感器编号
	*/
	private Long sensorNo;
	/**
	* 报警状态
	*/
	private String alarmStatus;
	/**
	* 一级告警设定值
	*/
	private Double firsLevelAlarm;
	/**
	* 二级告警设定值
	*/
	private Double secondaryLevelAlarm;
	/**
	* 内容
	*/
	private Object content;
	/**
	* 备注
	*/
	private String remark;
}