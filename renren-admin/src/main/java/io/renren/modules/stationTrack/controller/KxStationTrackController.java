package io.renren.modules.stationTrack.controller;

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
import io.renren.modules.stationTrack.dto.KxStationTrackDTO;
import io.renren.modules.stationTrack.entity.KxStationTrackEntity;
import io.renren.modules.stationTrack.excel.KxStationTrackExcel;
import io.renren.modules.stationTrack.service.KxStationTrackService;
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
* 桩点位移轨迹
*
* @author cxy 
* @since 3.0 2022-04-19
*/
@RestController
@RequestMapping("stationTrack/kxstationtrack")
@Api(tags="桩点位移轨迹")
public class KxStationTrackController {
    @Autowired
    private KxStationTrackService kxStationTrackService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("stationTrack:kxstationtrack:page")
    public Result<PageData<KxStationTrackDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxStationTrackDTO> page = kxStationTrackService.page(params);

        return new Result<PageData<KxStationTrackDTO>>().ok(page);
    }

    @GetMapping("/getTrackListInfo/{stationId}")
    @ApiOperation(value = "轨迹数据")
    public Result<List<KxStationTrackEntity>> getTrackListInfo(@PathVariable Long stationId) {
        List<KxStationTrackEntity> list = kxStationTrackService.getTrackListInfo(stationId);
        return new Result<List<KxStationTrackEntity>>().ok(list);
    }


    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("stationTrack:kxstationtrack:info")
    public Result<KxStationTrackDTO> get(@PathVariable("id") Long id){
        KxStationTrackDTO data = kxStationTrackService.get(id);

        return new Result<KxStationTrackDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("stationTrack:kxstationtrack:save")
    public Result save(@RequestBody KxStationTrackDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxStationTrackService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("stationTrack:kxstationtrack:update")
    public Result update(@RequestBody KxStationTrackDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxStationTrackService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("stationTrack:kxstationtrack:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxStationTrackService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("stationTrack:kxstationtrack:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<KxStationTrackDTO> list = kxStationTrackService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "桩点位移轨迹", list, KxStationTrackExcel.class);
    }

}