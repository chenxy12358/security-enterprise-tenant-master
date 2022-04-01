package io.renren.modules.device.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.utils.StringUtil;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.excel.KxDeviceExcel;
import io.renren.modules.device.service.KxDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;


/**
 * 设备信息
 *
 * @author cxy
 * @since 3.0 2022-02-16
 */
@RestController
@RequestMapping("device/kxdevice")
@Api(tags = "设备信息 ")
public class KxDeviceController {
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
    @RequiresPermissions("device:kxdevice:page")
    public Result<PageData<KxDeviceDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<KxDeviceDTO> page = kxDeviceService.page(params);

        return new Result<PageData<KxDeviceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("station:kxstation:info")
    public Result<KxDeviceDTO> get(@PathVariable("id") Long id) {
        KxDeviceDTO data = kxDeviceService.get(id);

        return new Result<KxDeviceDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("station:kxstation:save")
    public Result save(@RequestBody KxDeviceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        return kxDeviceService.add(dto);
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("station:kxstation:update")
//    @RequiresPermissions("device:kxdevice:update")
    public Result update(@RequestBody KxDeviceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        return kxDeviceService.modify(dto);
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("station:kxstation:delete")
    public Result delete(@PathVariable("id") Long id) {
        //效验数据
        AssertUtils.isNull(id, "id");
        kxDeviceService.delete(id);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("device:kxdevice:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxDeviceDTO> list = kxDeviceService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "设备信息 ", list, KxDeviceExcel.class);
    }

}