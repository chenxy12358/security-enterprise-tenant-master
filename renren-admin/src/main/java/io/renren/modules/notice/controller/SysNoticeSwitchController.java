package io.renren.modules.notice.controller;

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
import io.renren.modules.notice.dto.SysNoticeSwitchDTO;
import io.renren.modules.notice.excel.SysNoticeSwitchExcel;
import io.renren.modules.notice.service.SysNoticeSwitchService;
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
* notice
*
* @author WEI 
* @since 3.0 2022-05-23
*/
@RestController
@RequestMapping("notice/sysnoticeswitch")
@Api(tags="notice")
public class SysNoticeSwitchController {
    @Autowired
    private SysNoticeSwitchService sysNoticeSwitchService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("notice:sysnoticeswitch:page")
    public Result<PageData<SysNoticeSwitchDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SysNoticeSwitchDTO> page = sysNoticeSwitchService.page(params);

        return new Result<PageData<SysNoticeSwitchDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("notice:sysnoticeswitch:info")
    public Result<SysNoticeSwitchDTO> get(@PathVariable("id") Long id){
        SysNoticeSwitchDTO data = sysNoticeSwitchService.get(id);

        return new Result<SysNoticeSwitchDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("notice:sysnoticeswitch:save")
    public Result save(@RequestBody SysNoticeSwitchDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysNoticeSwitchService.save(dto);

        return new Result();
    }


    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("notice:sysnoticeswitch:update")
    public Result update(@RequestBody SysNoticeSwitchDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysNoticeSwitchService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("notice:sysnoticeswitch:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysNoticeSwitchService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("notice:sysnoticeswitch:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysNoticeSwitchDTO> list = sysNoticeSwitchService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "notice", list, SysNoticeSwitchExcel.class);
    }

}