package io.renren.modules.netWorkData.entity;

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
 * 网络状态信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("kx_net_work_state_data")
public class KxNetWorkStateDataEntity {
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
	private Long deviceid;
	/**
	* 桩点名称
	*/
	private Long stationid;
	/**
	* 内容
	*/
	private Object content;
	/**
	* 网卡名称
	*/
	private String nicName;
	/**
	* 网络模式
	*/
	private String accessTech;
	/**
	* 运营商
	*/
	private String operatorName;
	/**
	* 信号质量
	*/
	private String signalQuality;
	/**
	* 属性
	*/
	private Object properties;
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