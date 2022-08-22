package io.renren.modules.AppOrder.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 兑换记录
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("app_order")
public class AppOrderEntity {
	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
	@TableId
	private Long id;
	/**
	* 备注
	*/
	private String remark;
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
	* 订单编号
	*/
	private String transactionId;
	/**
	* 订单状态
	*/
	private String status;
	/**
	* 商品名称
	*/
	private String goodsName;
	/**
	* 商品數量
	*/
	private Integer goodsCount;
	/**
	* 图片地址
	*/
	private String url;
	/**
	* 总环保积分
	*/
	private Integer totalNamorl;
	/**
	* 总实践积分
	*/
	private Integer totalSpecial;
	/**
	* 用户id
	*/
	private Long userId;
}