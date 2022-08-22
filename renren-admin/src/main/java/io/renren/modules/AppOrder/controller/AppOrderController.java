package io.renren.modules.AppOrder.controller;

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
import io.renren.modules.AppOrder.dto.AppOrderDTO;
import io.renren.modules.AppOrder.excel.AppOrderExcel;
import io.renren.modules.AppOrder.service.AppOrderService;
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
* 兑换记录
*
* @author WEI 
* @since 3.0 2022-08-13
*/
@RestController
@RequestMapping("AppOrder/apporder")
@Api(tags="兑换记录")
public class AppOrderController {
    @Autowired
    private AppOrderService appOrderService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("AppOrder:apporder:page")
    public Result<PageData<AppOrderDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<AppOrderDTO> page = appOrderService.page(params);

        return new Result<PageData<AppOrderDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("AppOrder:apporder:info")
    public Result<AppOrderDTO> get(@PathVariable("id") Long id){
        AppOrderDTO data = appOrderService.get(id);

        return new Result<AppOrderDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("AppOrder:apporder:save")
    public Result save(@RequestBody AppOrderDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        appOrderService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("AppOrder:apporder:update")
    public Result update(@RequestBody AppOrderDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        appOrderService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("AppOrder:apporder:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        appOrderService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("AppOrder:apporder:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<AppOrderDTO> list = appOrderService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "兑换记录", list, AppOrderExcel.class);
    }

}