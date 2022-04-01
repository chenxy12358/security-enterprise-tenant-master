package io.renren.modules.deviceData.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.deviceData.dto.KxDeviceAccelerationDTO;
import io.renren.modules.deviceData.excel.KxDeviceAccelerationExcel;
import io.renren.modules.deviceData.service.KxDeviceAccelerationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
* 设备加速度数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@RestController
@RequestMapping("deviceData/kxdeviceacceleration")
@Api(tags="设备加速度数据")
public class KxDeviceAccelerationController {
    @Autowired
    private KxDeviceAccelerationService kxDeviceAccelerationService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("deviceData:kxdeviceacceleration:page")
    public Result<PageData<KxDeviceAccelerationDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxDeviceAccelerationDTO> page = kxDeviceAccelerationService.page(params);

        return new Result<PageData<KxDeviceAccelerationDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("deviceData:kxdeviceacceleration:info")
    public Result<KxDeviceAccelerationDTO> get(@PathVariable("id") Long id){
        KxDeviceAccelerationDTO data = kxDeviceAccelerationService.get(id);

        return new Result<KxDeviceAccelerationDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("deviceData:kxdeviceacceleration:save")
    public Result save(@RequestBody KxDeviceAccelerationDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxDeviceAccelerationService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("deviceData:kxdeviceacceleration:update")
    public Result update(@RequestBody KxDeviceAccelerationDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxDeviceAccelerationService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("deviceData:kxdeviceacceleration:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxDeviceAccelerationService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("deviceData:kxdeviceacceleration:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxDeviceAccelerationDTO> list = kxDeviceAccelerationService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "设备加速度数据", list, KxDeviceAccelerationExcel.class);
    }

}