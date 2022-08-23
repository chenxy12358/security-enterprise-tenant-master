package io.renren.modules.AppUser.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * APP用户
 *
 * @author WEI 
 * @since 3.0 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("app_user")
public class AppUserEntity {
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
	* 人员类型
	*/
	private String type;
	/**
	* 昵称
	*/
	private String nickName;
	/**
	* 手机号码
	*/
	private String phoneNum;
	/**
	* 学校名称
	*/
	private String schoolName;
	/**
	* 专业
	*/
	private String professional;
	/**
	* 班级
	*/
	private String classes;
	/**
	* 班級
	*/
	private String studentNum;
}