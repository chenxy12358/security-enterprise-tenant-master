package io.renren.modules.AppScores.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 积分记录
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("app_scores")
public class AppScoresEntity {
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
	* 用户id
	*/
	private Long userId;
	/**
	* 环保积分
	*/
	private Integer normalScore;
	/**
	* 实践积分
	*/
	private Integer specialScore;
	/**
	 * 积分类型
	 */
	private String type;
}