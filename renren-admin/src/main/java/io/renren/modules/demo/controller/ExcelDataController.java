package io.renren.modules.demo.controller;

import com.alibaba.excel.EasyExcel;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelDataListener;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.demo.dto.ExcelDataDTO;
import io.renren.modules.demo.excel.ExcelDataExcel;
import io.renren.modules.demo.service.ExcelDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
* Excel导入演示
*
* @author Mark sunlightcs@gmail.com
*/
@RestController
@RequestMapping("demo/excel")
@Api(tags="Excel导入演示")
public class ExcelDataController {
    @Autowired
    private ExcelDataService excelDataService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("demo:excel:all")
    public Result<PageData<ExcelDataDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ExcelDataDTO> page = excelDataService.page(params);

        return new Result<PageData<ExcelDataDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("demo:excel:all")
    public Result<ExcelDataDTO> get(@PathVariable("id") Long id){
        ExcelDataDTO data = excelDataService.get(id);

        return new Result<ExcelDataDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @RequiresPermissions("demo:excel:all")
    public Result save(@RequestBody ExcelDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        excelDataService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @RequiresPermissions("demo:excel:all")
    public Result update(@RequestBody ExcelDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        excelDataService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @RequiresPermissions("demo:excel:all")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        excelDataService.delete(ids);

        return new Result();
    }

    @PostMapping("import")
    @ApiOperation("导入")
    @RequiresPermissions("demo:excel:all")
    @ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataType="file")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        //解析并保存到数据库
        EasyExcel.read(file.getInputStream(), ExcelDataExcel.class, new ExcelDataListener<>(excelDataService)).sheet().doRead();

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @RequiresPermissions("demo:excel:all")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ExcelDataDTO> list = excelDataService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Excel导入演示", list, ExcelDataExcel.class);
    }

}