package io.renren.modules.newLoadData.controller;

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
import io.renren.modules.newLoadData.dto.KxNewUploadDataDTO;
import io.renren.modules.newLoadData.excel.KxNewUploadDataExcel;
import io.renren.modules.newLoadData.service.KxNewUploadDataService;
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
* 最新上传数据
*
* @author WEI 
* @since 3.0 2022-04-14
*/
@RestController
@RequestMapping("newLoadData/kxnewuploaddata")
@Api(tags="最新上传数据")
public class KxNewUploadDataController {
    @Autowired
    private KxNewUploadDataService kxNewUploadDataService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("newLoadData:kxnewuploaddata:page")
    public Result<PageData<KxNewUploadDataDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxNewUploadDataDTO> page = kxNewUploadDataService.page(params);

        return new Result<PageData<KxNewUploadDataDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("newLoadData:kxnewuploaddata:info")
    public Result<KxNewUploadDataDTO> get(@PathVariable("id") Long id){
        KxNewUploadDataDTO data = kxNewUploadDataService.get(id);

        return new Result<KxNewUploadDataDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("newLoadData:kxnewuploaddata:save")
    public Result save(@RequestBody KxNewUploadDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxNewUploadDataService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("newLoadData:kxnewuploaddata:update")
    public Result update(@RequestBody KxNewUploadDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxNewUploadDataService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("newLoadData:kxnewuploaddata:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxNewUploadDataService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("newLoadData:kxnewuploaddata:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxNewUploadDataDTO> list = kxNewUploadDataService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "最新上传数据", list, KxNewUploadDataExcel.class);
    }

}