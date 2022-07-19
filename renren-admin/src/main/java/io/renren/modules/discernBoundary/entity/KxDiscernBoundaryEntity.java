package io.renren.modules.discernBoundary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 识别边界标定
 *
 * @author cxy 
 * @since 3.0 2022-07-19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_discern_boundary")
public class KxDiscernBoundaryEntity {
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
	* 站点id
	*/
	private Long stationId;
	/**
	* 设备类型id
	*/
	private Long deviceId;
	/**
	* 设备编号
	*/
	private String deviceNo;
	/**
	* 相机名称
	*/
	private String cameraName;
	/**
	* 预置位
	*/
	private String presetNo;
	/**
	* 图片尺寸
	*/
	private String sessionTime;
	/**
	* 是否可用
	*/
	private String enable;
	/**
	* 备注
	*/
	private String remark;
	/**
	* 是否删除
	*/
	private String deleted;
	/**
	* 删除者
	*/
	private Long deleter;
	/**
	* 删除时间
	*/
	private Date deletedTime;
}