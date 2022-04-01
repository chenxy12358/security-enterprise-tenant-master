/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.notice.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.utils.StringUtil;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.notice.dto.SysNoticeDTO;
import io.renren.modules.notice.service.SysNoticeService;
import io.renren.modules.notice.service.SysNoticeUserService;
import io.renren.modules.security.user.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.Array;
import java.util.Map;


/**
 * 通知管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/notice")
@Api(tags = "通知管理")
public class SysNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;
    @Autowired
    private SysNoticeUserService sysNoticeUserService;
    @Autowired
    private KxDeviceService kxDeviceService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @RequiresPermissions("sys:notice:all")
    public Result<PageData<SysNoticeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysNoticeDTO> page = sysNoticeService.page(params);

        return new Result<PageData<SysNoticeDTO>>().ok(page);
    }

    @GetMapping("user/page")
    @ApiOperation("获取被通知的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
    })
    @RequiresPermissions("sys:notice:all")
    public Result<PageData<SysNoticeDTO>> userPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysNoticeDTO> page = sysNoticeService.getNoticeUserPage(params);

        return new Result<PageData<SysNoticeDTO>>().ok(page);
    }

    @GetMapping("mynotice/page")
    @ApiOperation("获取我的通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
    })
    public Result<PageData<SysNoticeDTO>> myNoticePage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysNoticeDTO> page = sysNoticeService.getMyNoticePage(params);


        //////////////

        /*//test  -----------测试设置状态
        KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo("KX-V22P40-AI00010");
        JSONObject statusJson = JSONUtil.parseObj(deviceDTO.getStatus());

        String senderObject = "Emd.Device.Camera.E0";
        if (StringUtil.isNotEmpty(senderObject)) {
            String[] strings = senderObject.split("\\.");
            if (strings.length != 4) {
                return null;
            }
            Object level1 = statusJson.get(strings[1]);
            if (level1 != null) {
                JSONObject jsonObject = JSONUtil.parseObj(level1);
                Object level2 = jsonObject.get(strings[2]);
                JSONArray array = JSONUtil.parseArray(level2.toString(), false);
                if (array.size() > 0) {
                    JSONArray newArray = new JSONArray();
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject parseObj = JSONUtil.parseObj(array.get(i));
                        if ("Emd.Device.Camera.E0".equals(parseObj.get("Name"))) {
                            parseObj.putOpt("Name", "郑威呈");
                        }
                        newArray.add(parseObj);
                    }
                    //String s = JSONUtil.toJsonStr(newArray);
                    jsonObject.putOpt(strings[2], newArray);
                    statusJson.putOpt(strings[1], jsonObject);
                }
                System.err.println(statusJson);
            }


        }*/
        /////////////


        return new Result<PageData<SysNoticeDTO>>().ok(page);
    }

    @PutMapping("mynotice/read/{noticeId}")
    @ApiOperation("标记我的通知为已读")
    public Result read(@PathVariable("noticeId") Long noticeId) {
        sysNoticeUserService.updateReadStatus(SecurityUser.getUserId(), noticeId);

        return new Result();
    }

    @GetMapping("mynotice/unread")
    @ApiOperation("我的通知未读读")
    public Result<Integer> unRead() {
        int count = sysNoticeUserService.getUnReadNoticeCount(SecurityUser.getUserId());

        return new Result<Integer>().ok(count);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("sys:notice:all")
    public Result<SysNoticeDTO> get(@PathVariable("id") Long id) {
        SysNoticeDTO data = sysNoticeService.get(id);

        return new Result<SysNoticeDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("sys:notice:all")
    public Result save(@RequestBody SysNoticeDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysNoticeService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("sys:notice:all")
    public Result update(@RequestBody SysNoticeDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysNoticeService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:notice:all")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysNoticeService.delete(ids);

        return new Result();
    }

}