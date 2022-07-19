package io.renren.modules.discernBoundary.controller;

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
import io.renren.modules.discernBoundary.dto.KxDiscernBoundaryDTO;
import io.renren.modules.discernBoundary.excel.KxDiscernBoundaryExcel;
import io.renren.modules.discernBoundary.service.KxDiscernBoundaryService;
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
* 识别边界标定
*
* @author cxy 
* @since 3.0 2022-07-19
*/
@RestController
@RequestMapping("discernBoundary/kxdiscernboundary")
@Api(tags="识别边界标定")
public class KxDiscernBoundaryController {
    @Autowired
    private KxDiscernBoundaryService kxDiscernBoundaryService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("discernBoundary:kxdiscernboundary:page")
    public Result<PageData<KxDiscernBoundaryDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxDiscernBoundaryDTO> page = kxDiscernBoundaryService.page(params);

        return new Result<PageData<KxDiscernBoundaryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("discernBoundary:kxdiscernboundary:info")
    public Result<KxDiscernBoundaryDTO> get(@PathVariable("id") Long id){
        KxDiscernBoundaryDTO data = kxDiscernBoundaryService.get(id);

        return new Result<KxDiscernBoundaryDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("discernBoundary:kxdiscernboundary:save")
    public Result save(@RequestBody KxDiscernBoundaryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxDiscernBoundaryService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("discernBoundary:kxdiscernboundary:update")
    public Result update(@RequestBody KxDiscernBoundaryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxDiscernBoundaryService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("discernBoundary:kxdiscernboundary:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxDiscernBoundaryService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("discernBoundary:kxdiscernboundary:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxDiscernBoundaryDTO> list = kxDiscernBoundaryService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "识别边界标定", list, KxDiscernBoundaryExcel.class);
    }

}