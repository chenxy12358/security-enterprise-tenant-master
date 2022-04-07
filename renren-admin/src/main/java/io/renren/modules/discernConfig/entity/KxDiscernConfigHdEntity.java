package io.renren.modules.discernConfig.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 识别参数配置
 *
 * @author cxy 
 * @since 3.0 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_discern_config_hd")
public class KxDiscernConfigHdEntity {
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
	* 方案名称
	*/
	private String schemeNo;
	/**
	* 方案路径
	*/
	private String schemePath;
	/**
	* 图片尺寸
	*/
	private String picSize;
	/**
	* 识别参数配置
	*/
	private Object distinguishConfig;
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
	private Long deviceId;
	/**
	* 设备编号
	*/
	private String deviceNo;
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