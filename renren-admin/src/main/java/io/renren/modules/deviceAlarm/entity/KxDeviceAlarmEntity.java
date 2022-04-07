package io.renren.modules.deviceAlarm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 可视化告警
 *
 * @author cxy
 * @since 3.0 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_device_alarm")
public class KxDeviceAlarmEntity {
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
	 * 拍照时间
	 */
	private Date pictureDate;
	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
	/**
	 * 设备编码
	 */
	private String deviceNo;
	/**
	 * 设备id
	 */
	private Long deviceId;
	/**
	 * 站点id
	 */
	private Long stationId;
	/**
	 * 级别
	 */
	private Integer level;
	/**
	 * 处理类型
	 */
	private String handleType;
	/**
	 * 信号
	 */
	private String signalType;
	/**
	 * 发送信息
	 */
	private Object senderInfo;
	/**
	 * 内容
	 */
	private Object content;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 是否删除
	 */
	@TableLogic(value="f",delval="t")
	private String deleted;
}