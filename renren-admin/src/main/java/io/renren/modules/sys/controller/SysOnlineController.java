/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.sys.controller;

import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.modules.security.service.SysUserTokenService;
import io.renren.modules.sys.entity.SysOnlineEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 在线用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/online")
@Api(tags="在线用户")
public class SysOnlineController {
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
    })
    @RequiresPermissions("sys:online:all")
    public Result<PageData<SysOnlineEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SysOnlineEntity> page = sysUserTokenService.onlinePage(params);

        return new Result<PageData<SysOnlineEntity>>().ok(page);
    }

    @PostMapping("logout")
    @ApiOperation(value = "踢出")
    @RequiresPermissions("sys:online:all")
    public Result logout(Long id) {
        //效验数据
        AssertUtils.isNull(id, "id");

        //退出
        sysUserTokenService.logout(id);

        return new Result();
    }
}
