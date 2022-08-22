package io.renren.modules.AppArticles.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 文章管理
 *
 * @author WEI 
 * @since 3.0 2022-08-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("app_articles")
public class AppArticlesEntity {
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
	* 标题
	*/
	private String title;
	/**
	* 内容
	*/
	private String content;
	/**
	* 发布时间
	*/
	private Date pubDate;
	/**
	* 阅读次数
	*/
	private Integer readCount;
	/**
	* 文章类型
	*/
	private String type;
	private String url;
}