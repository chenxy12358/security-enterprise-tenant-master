package io.renren.modules.device.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_device")
public class KxDeviceEntity {
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
	* 名称
	*/
	private String name;
	/**
	* 序号
	*/
	private String serialNo;
	/**
	* 制造厂
	*/
	private String manufactory;
	/**
	* 协议类型
	*/
	private String proType;
	/**
	* 协议
	*/
	private Object protocols;
	/**
	* 基本信息
	*/
	private Object baseInfo;
	/**
	* 状态
	*/
	private Object status;
	/**
	* 机构id
	*/
	private Long orgId;
	/**
	* 站点id
	*/
	private Long stationId;
	/**
	* 设备类型id
	*/
	private Long deviceTypeId;
	/**
	* 设备模式id
	*/
	private Long deviceModelId;
	/**
	* 任务id
	*/
	private Long scheduleJobId;
	/**
	* 属性
	*/
	private Object properties;
	/**
	* 备注
	*/
	private String remark;
	/**
	* 是否删除
	*/
	private String deleted;
	/**
	* 是否可用
	*/
	private String enable;
	/**
	* 删除者
	*/
	private Long deleter;
	/**
	* 删除时间
	*/
	private Date deletedTime;

	private String ip;

	@TableField(exist=false)
	private String orgName;
	@TableField(exist=false)
	private String stationName;
}