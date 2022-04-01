package io.renren.modules.gas.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 燃气信息 
 *
 * @author WEI 
 * @since 3.0 2022-02-18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_gas_data")
public class KxGasDataEntity {
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
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
	* 浓度单位
	*/
	private String unit;
	/**
	* 类型
	*/
	private String gasType;
	/**
	* 传感器编号
	*/
	private Long sensorId;
	/**
	* 报警状态
	*/
	private String alarmStatus;
	/**
	* 一级告警设定值
	*/
	private Double firsLevelAlarm;
	/**
	* 当前浓度值
	*/
	private Double concentrationValue;
	/**
	* 二级告警设定值
	*/
	private Double secondaryLevelAlarm;
	/**
	* 属性
	*/
	private Object properties;
	/**
	* 备注
	*/
	private String remark;

	/**
	 * 忽略和表的映射 ，只做查询
	 */
	@TableField(exist = false)
	private String deviceName;
	@TableField(exist = false)
	private String stationName;
	@TableField(exist = false)
	private String deptName;

}