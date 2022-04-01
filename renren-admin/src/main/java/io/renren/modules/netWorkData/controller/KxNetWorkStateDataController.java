package io.renren.modules.netWorkData.controller;

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
import io.renren.modules.netWorkData.dto.KxNetWorkStateDataDTO;
import io.renren.modules.netWorkData.excel.KxNetWorkStateDataExcel;
import io.renren.modules.netWorkData.service.KxNetWorkStateDataService;
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
* 网络状态信息 
*
* @author cxy 
* @since 3.0 2022-02-19
*/
@RestController
@RequestMapping("netWorkData/kxnetworkstatedata")
@Api(tags="网络状态信息 ")
public class KxNetWorkStateDataController {
    @Autowired
    private KxNetWorkStateDataService kxNetWorkStateDataService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("netWorkData:kxnetworkstatedata:page")
    public Result<PageData<KxNetWorkStateDataDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxNetWorkStateDataDTO> page = kxNetWorkStateDataService.page(params);

        return new Result<PageData<KxNetWorkStateDataDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("netWorkData:kxnetworkstatedata:info")
    public Result<KxNetWorkStateDataDTO> get(@PathVariable("id") Long id){
        KxNetWorkStateDataDTO data = kxNetWorkStateDataService.get(id);

        return new Result<KxNetWorkStateDataDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("netWorkData:kxnetworkstatedata:save")
    public Result save(@RequestBody KxNetWorkStateDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxNetWorkStateDataService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("netWorkData:kxnetworkstatedata:update")
    public Result update(@RequestBody KxNetWorkStateDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxNetWorkStateDataService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("netWorkData:kxnetworkstatedata:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxNetWorkStateDataService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("netWorkData:kxnetworkstatedata:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxNetWorkStateDataDTO> list = kxNetWorkStateDataService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "网络状态信息 ", list, KxNetWorkStateDataExcel.class);
    }

}