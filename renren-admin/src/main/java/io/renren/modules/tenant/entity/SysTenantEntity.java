/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.tenant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 租户管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_tenant")
public class SysTenantEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 租户编码
     */
	private Long tenantCode;
    /**
     * 租户名称
     */
	private String tenantName;
    /**
     * 状态  0：停用    1：正常
     */
	private Integer status;
    /**
     * 备注
     */
	private String remark;
    /**
     * 登录账号ID
     */
	private Long userId;
    /**
     * 登录账号
     */
	private String username;
    /**
     * 删除标识 0：未删除    1：删除
     */
	private Integer delFlag;
	/**
	 * 系统租户   0：否   1：是
	 */
	private Integer sysTenant;
    /**
     * 更新者
     */
	private String updater;
    /**
     * 更新时间
     */
	private Date updateDate;

	/**
	 * 邮箱
	 */

	@TableField(exist = false)
	private String email;
	/**
	 * 手机号
	 */
	@TableField(exist = false)
	private String mobile;
	/**
	 * 真实姓名
	 */
	@TableField(exist = false)
	private String realName;
}