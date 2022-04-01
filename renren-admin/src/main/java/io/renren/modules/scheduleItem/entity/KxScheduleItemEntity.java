package io.renren.modules.scheduleItem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 计划任务拍照结果
 *
 * @author cxy 
 * @since 3.0 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_schedule_item_result")
public class KxScheduleItemEntity {
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
	* 计划任务id
	*/
	private Long scheduleJobId;
	/**
	* 类型
	*/
	private String itemType;
	/**
	* 版本
	*/
	private String version;
	/**
	* 信号
	*/
	private String signalType;
	/**
	* 属性
	*/
	private Object properties;
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