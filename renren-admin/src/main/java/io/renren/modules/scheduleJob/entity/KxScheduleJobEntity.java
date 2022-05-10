package io.renren.modules.scheduleJob.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 计划任务
 *
 * @author cxy 
 * @since 3.0 2022-02-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_schedule_job")
public class KxScheduleJobEntity  {
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
	* 名称
	*/
	private String name;
	/**
	* 设备id
	*/
	private Long deviceId;
	/**
	* 设备类型id
	*/
	private Long deviceTypeId;
	/**
	* 设备模式id
	*/
	private Long deviceModelId;
	/**
	* 属性
	*/
	private Object properties;
	/**
	* 事件类型
	*/
	private String itemType;
	/**
	* 版本
	*/
	private String version;
	/**
	* 内容
	*/
	private Object content;
	/**
	* 状态
	*/
	private String status;// 0 未下发，1下发
	/**
	* 备注
	*/
	private String remark;
	/**
	* 机构id
	*/
	private Long orgId;
	/**
	* 是否删除
	*/
	@TableLogic(value="f",delval="t")
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
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date deletedTime;


	private String dest;
	/**
	 * 图片大小
	 */
	private String pictureSize;
	/**
	 * 相机名字
	 */
	private String camera;
	/**
	 * 周期
	 */
	private String period;
	/**
	 * 预置位
	 */
	private Integer presetId;
	/**
	 * 时间选择类型
	 */
	private String timeType;
	/**
	 * 时间间隔（分钟）
	 */
	private Integer timeInterval;
	/**
	 * 工作时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="HH:mm:ss")
	private Date workTime;
	/**
	 * 起始时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="HH:mm:ss")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="HH:mm:ss")
	private Date endTime;
}