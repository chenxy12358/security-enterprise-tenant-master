package io.renren.modules.scheduleItem.controller;

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
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.excel.KxScheduleItemExcel;
import io.renren.modules.scheduleItem.service.KxScheduleItemService;
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
* 计划任务拍照结果
*
* @author cxy 
* @since 3.0 2022-03-05
*/
@RestController
@RequestMapping("scheduleItem/kxscheduleitem")
@Api(tags="计划任务拍照结果")
public class KxScheduleItemController {
    @Autowired
    private KxScheduleItemService kxScheduleItemService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("scheduleItem:kxscheduleitem:page")
    public Result<PageData<KxScheduleItemDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxScheduleItemDTO> page = kxScheduleItemService.page(params);

        return new Result<PageData<KxScheduleItemDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("scheduleItem:kxscheduleitem:info")
    public Result<KxScheduleItemDTO> get(@PathVariable("id") Long id){
        KxScheduleItemDTO data = kxScheduleItemService.get(id);

        return new Result<KxScheduleItemDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("scheduleItem:kxscheduleitem:save")
    public Result save(@RequestBody KxScheduleItemDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxScheduleItemService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("scheduleItem:kxscheduleitem:update")
    public Result update(@RequestBody KxScheduleItemDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxScheduleItemService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("scheduleItem:kxscheduleitem:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxScheduleItemService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("scheduleItem:kxscheduleitem:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxScheduleItemDTO> list = kxScheduleItemService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "计划任务拍照结果", list, KxScheduleItemExcel.class);
    }

}