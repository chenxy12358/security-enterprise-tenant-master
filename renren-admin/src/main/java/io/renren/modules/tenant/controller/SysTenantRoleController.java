/**
 * Copyright (c) 2018 人人开源 All rights reserved.
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
import io.renren.modules.sys.dto.SysRoleDTO;
import io.renren.modules.sys.service.SysRoleMenuService;
import io.renren.modules.tenant.service.SysTenantRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户角色管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/tenant/role")
@Api(tags="租户角色管理")
public class SysTenantRoleController {
	@Autowired
	private SysTenantRoleService sysTenantRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@GetMapping("page")
	@ApiOperation("分页")
	@ApiImplicitParams({
		@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
		@ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
		@ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = "name", value = "角色名", paramType = "query", dataType="String")
	})
	@RequiresPermissions("tenant:role:page")
	public Result<PageData<SysRoleDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
		PageData<SysRoleDTO> page = sysTenantRoleService.page(params);

		return new Result<PageData<SysRoleDTO>>().ok(page);
	}

	@GetMapping("list")
	@ApiOperation("列表")
	@RequiresPermissions("tenant:role:list")
	public Result<List<SysRoleDTO>> list(){
		List<SysRoleDTO> data = sysTenantRoleService.list(new HashMap<>(1));

		return new Result<List<SysRoleDTO>>().ok(data);
	}

	@GetMapping("{id}")
	@ApiOperation("信息")
	@RequiresPermissions("tenant:role:info")
	public Result<SysRoleDTO> get(@PathVariable("id") Long id){
		SysRoleDTO data = sysTenantRoleService.get(id);

		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.getMenuIdList(id);
		data.setMenuIdList(menuIdList);

		return new Result<SysRoleDTO>().ok(data);
	}

	@PostMapping
	@ApiOperation("保存")
	@LogOperation("保存")
	@RequiresPermissions("tenant:role:save")
	public Result save(@RequestBody SysRoleDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

		sysTenantRoleService.save(dto);

		return new Result();
	}

	@PutMapping
	@ApiOperation("修改")
	@LogOperation("修改")
	@RequiresPermissions("tenant:role:update")
	public Result update(@RequestBody SysRoleDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

		sysTenantRoleService.update(dto);

		return new Result();
	}

	@DeleteMapping
	@ApiOperation("删除")
	@LogOperation("删除")
	@RequiresPermissions("tenant:role:delete")
	public Result delete(@RequestBody Long[] ids){
		//效验数据
		AssertUtils.isArrayEmpty(ids, "id");

		sysTenantRoleService.delete(ids);

		return new Result();
	}
}