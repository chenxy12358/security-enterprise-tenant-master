package io.renren.modules.AppGoods.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 商品管理
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("app_goods")
public class AppGoodsEntity {
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
	* 学校编号
	*/
	private Long schoolCode;
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
	* 环保积分
	*/
	private Integer normalPrice;
	/**
	* 组合-环保积分
	*/
	private Integer comNormalPrice;
	/**
	* 组合-实践积分
	*/
	private Integer comSpecialPrice;
	/**
	* 库存数量
	*/
	private Integer remainCount;
	/**
	* 已售数量
	*/
	private Integer saleCount;
	/**
	* 商品名称
	*/
	private String name;
	/**
	* 图片路径
	*/
	private String url;
}