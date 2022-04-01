/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.flow.entity.FlowModelEntity;
import io.renren.modules.flow.service.FlowModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 模型管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/flow/model")
@AllArgsConstructor
@Api(tags="模型管理")
public class FlowModelController {
    private final FlowModelService flowModelService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "key", value = "key", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "name", value = "name", paramType = "query", dataType="String")
    })
    @RequiresPermissions("sys:model:all")
    public Result<PageData<FlowModelEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<FlowModelEntity> page = flowModelService.page(params);

        return new Result<PageData<FlowModelEntity>>().ok(page);
    }

    @PostMapping("deploy/{id}")
    @ApiOperation("部署")
    @LogOperation("部署")
    @RequiresPermissions("sys:model:all")
    public Result deploy(@PathVariable("id") String id) {
        flowModelService.deploymentByModelId(id);
        return new Result();
    }
}
