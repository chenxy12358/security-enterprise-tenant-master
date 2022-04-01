/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */
package io.renren.modules.tenant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.renren.common.utils.DateUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 租户管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "租户管理")
public class SysTenantDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@ApiModelProperty(value = "租户编码")
	@NotNull(message="{tenant.tenantCode.require}", groups = DefaultGroup.class)
	private Long tenantCode;

	@ApiModelProperty(value = "租户名称")
	@NotBlank(message="{tenant.tenantName.require}", groups = DefaultGroup.class)
	private String tenantName;

	@ApiModelProperty(value = "登录账号")
	@NotBlank(message="{tenant.username.require}", groups = DefaultGroup.class)
	private String username;

	@JsonIgnore
	private Long userId;

	@ApiModelProperty(value = "登录密码")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="{tenant.password.require}", groups = AddGroup.class)
	private String password;

	@ApiModelProperty(value = "姓名", required = true)
	@NotBlank(message="{sysuser.realname.require}", groups = DefaultGroup.class)
	private String realName;

	@ApiModelProperty(value = "邮箱", required = true)
	@NotBlank(message="{sysuser.email.require}", groups = DefaultGroup.class)
	@Email(message="{sysuser.email.error}", groups = DefaultGroup.class)
	private String email;

	@ApiModelProperty(value = "手机号", required = true)
	@NotBlank(message="{sysuser.mobile.require}", groups = DefaultGroup.class)
	private String mobile;

	@ApiModelProperty(value = "角色ID列表")
	private List<Long> roleIdList;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "状态  0：停用    1：正常", required = true)
	@Range(min=0, max=1, message = "{tenant.status.range}", groups = DefaultGroup.class)
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createDate;


}