package io.renren.modules.stationTrack.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 桩点位移轨迹
 *
 * @author cxy 
 * @since 3.0 2022-04-19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_station_track")
public class KxStationTrackEntity {
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
	* 桩点id
	*/
	private Long stationId;
	/**
	* 经度
	*/
	private String lng;
	/**
	* 维度
	*/
	private String lat;
	/**
	* 位置
	*/
	private String position;
	/**
	* 备注
	*/
	private String remark;
	/**
	* 是否删除
	*/
	private String deleted;
}