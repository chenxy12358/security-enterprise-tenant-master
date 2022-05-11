package io.renren.modules.discernConfig.controller;

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
import io.renren.modules.discernConfig.dto.KxAIPzVO;
import io.renren.modules.discernConfig.dto.KxDiscernConfigDTO;
import io.renren.modules.discernConfig.service.KxDiscernConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
* 识别参数配置
*
* @author cxy 
* @since 3.0 2022-03-08
*/
@RestController
@RequestMapping("discernConfig/kxdiscernconfig")
@Api(tags="识别参数配置")
public class KxDiscernConfigController {
    @Autowired
    private KxDiscernConfigService kxDiscernConfigService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("discernConfig:kxdiscernconfig:page")
    public Result<PageData<KxDiscernConfigDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxDiscernConfigDTO> page = kxDiscernConfigService.page(params);

        return new Result<PageData<KxDiscernConfigDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("discernConfig:kxdiscernconfig:info")
    public Result<KxDiscernConfigDTO> get(@PathVariable Long id){
        KxDiscernConfigDTO data = kxDiscernConfigService.get(id);
        //todo 删除
        List<KxAIPzVO> list =new ArrayList();
        KxAIPzVO kxAIPzVO =new KxAIPzVO();
        kxAIPzVO.setCode("aeroplane");
        kxAIPzVO.setName("飞机");
        kxAIPzVO.setThresh(new BigDecimal(0.1));
        kxAIPzVO.setRingSwitch(true);
        list.add(kxAIPzVO);
        KxAIPzVO kxAIPzVO1 =new KxAIPzVO();
        kxAIPzVO1.setCode("bear");
        kxAIPzVO1.setName("熊");
        kxAIPzVO1.setThresh(new BigDecimal(0.88));
        kxAIPzVO1.setEnable(true);
        list.add(kxAIPzVO1);
        KxAIPzVO kxAIPzVO2 =new KxAIPzVO();
        kxAIPzVO2.setCode("bench");
        kxAIPzVO2.setName("长凳");
        kxAIPzVO2.setEnable(true);
        kxAIPzVO2.setThresh(new BigDecimal(0.2));
        list.add(kxAIPzVO2);
        data.setDiscernList(list);
        return new Result<KxDiscernConfigDTO>().ok(data);
    }

    @GetMapping("/getBydeviceId")
    @ApiOperation("信息")
//    @RequiresPermissions("discernConfig:kxdiscernconfig:info")
    public Result<KxDiscernConfigDTO> getBydeviceId(@RequestParam Long deviceId){
        KxDiscernConfigDTO data = kxDiscernConfigService.getBydeviceId(deviceId);
        return new Result<KxDiscernConfigDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("discernConfig:kxdiscernconfig:save")
    public Result save(@RequestBody KxDiscernConfigDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        kxDiscernConfigService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("discernConfig:kxdiscernconfig:update")
    public Result update(@RequestBody KxDiscernConfigDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxDiscernConfigService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("discernConfig:kxdiscernconfig:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxDiscernConfigService.delete(ids);

        return new Result();
    }


}