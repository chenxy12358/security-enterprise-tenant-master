/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.tenant.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.service.SysRoleUserService;
import io.renren.modules.tenant.dto.SysTenantDTO;
import io.renren.modules.tenant.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 租户管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/tenant")
@Api(tags="租户管理")
public class SysTenantController {
	@Autowired
	private SysTenantService sysTenantService;
	@Autowired
	private SysRoleUserService sysRoleUserService;

	@GetMapping("page")
	@ApiOperation("分页")
	@ApiImplicitParams({
		@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
		@ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
		@ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = "tenantCode", value = "租户编码", paramType = "query", dataType="String"),
		@ApiImplicitParam(name = "tenantName", value = "租户名", paramType = "query", dataType="String"),
		@ApiImplicitParam(name = "sysTenant", value = "是否查询系统租户 0：查询  其他值不查询", paramType = "query", dataType="String")
	})
	@RequiresPermissions("sys:tenant:page")
	public Result<PageData<SysTenantDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
		PageData<SysTenantDTO> page = sysTenantService.page(params);

		return new Result<PageData<SysTenantDTO>>().ok(page);
	}

	@GetMapping("{id}")
	@ApiOperation("信息")
	@RequiresPermissions("sys:tenant:info")
	public Result<SysTenantDTO> get(@PathVariable("id") Long id){
		SysTenantDTO data = sysTenantService.get(id);

		//用户角色列表
		List<Long> roleIdList = sysRoleUserService.getRoleIdList(data.getUserId());
		data.setRoleIdList(roleIdList);

		return new Result<SysTenantDTO>().ok(data);
	}

	@PostMapping
	@ApiOperation("保存")
	@LogOperation("保存")
	@RequiresPermissions("sys:tenant:save")
	public Result save(@RequestBody SysTenantDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

		sysTenantService.save(dto);

		return new Result();
	}

	@PutMapping
	@ApiOperation("修改")
	@LogOperation("修改")
	@RequiresPermissions("sys:tenant:update")
	public Result update(@RequestBody SysTenantDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

		sysTenantService.update(dto);

		return new Result();
	}

	@DeleteMapping
	@ApiOperation("删除")
	@LogOperation("删除")
	@RequiresPermissions("sys:tenant:delete")
	public Result delete(@RequestBody Long[] ids){
		//效验数据
		AssertUtils.isArrayEmpty(ids, "id");

		sysTenantService.deleteBatchIds(Arrays.asList(ids));

		return new Result();
	}
}