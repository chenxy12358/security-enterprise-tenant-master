package io.renren.modules.scheduleJob.controller;

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
import io.renren.modules.scheduleJob.dto.KxScheduleJobDTO;
import io.renren.modules.scheduleJob.dto.KxScheduleJobPageDTO;
import io.renren.modules.scheduleJob.excel.KxScheduleJobExcel;
import io.renren.modules.scheduleJob.service.KxScheduleJobService;
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
* 计划任务
*
* @author cxy 
* @since 3.0 2022-02-27
*/
@RestController
@RequestMapping("scheduleJob/kxschedulejob")
@Api(tags="计划任务")
public class KxScheduleJobController {
    @Autowired
    private KxScheduleJobService kxScheduleJobService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("scheduleJob:kxschedulejob:page")
    public Result<PageData<KxScheduleJobPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxScheduleJobPageDTO> page = kxScheduleJobService.pageNew(params);
        return new Result<PageData<KxScheduleJobPageDTO>>().ok(page);
    }
//    @RequiresPermissions("scheduleJob:kxschedulejob:page")
//    public Result<PageData<KxScheduleJobDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
//        PageData<KxScheduleJobDTO> page = kxScheduleJobService.page(params);
//
//        return new Result<PageData<KxScheduleJobDTO>>().ok(page);
//    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("scheduleJob:kxschedulejob:info")
    public Result<KxScheduleJobDTO> get(@PathVariable("id") Long id){
        KxScheduleJobDTO dto = kxScheduleJobService.get(id);
        return new Result<KxScheduleJobDTO>().ok(dto);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("scheduleJob:kxschedulejob:save")
    public Result save(@RequestBody KxScheduleJobDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxScheduleJobService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("scheduleJob:kxschedulejob:update")
    public Result update(@RequestBody KxScheduleJobDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxScheduleJobService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("scheduleJob:kxschedulejob:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxScheduleJobService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("scheduleJob:kxschedulejob:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxScheduleJobDTO> list = kxScheduleJobService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "计划任务", list, KxScheduleJobExcel.class);
    }

}