package io.renren.modules.discernConfig.controller;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.discernConfig.dto.KxDiscernConfigHdDTO;
import io.renren.modules.discernConfig.service.KxDiscernConfigHdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.Map;


/**
* 识别参数配置
*
* @author cxy 
* @since 3.0 2022-03-25
*/
@RestController
@RequestMapping("discernConfig/kxdiscernconfighd")
@Api(tags="识别参数配置")
public class KxDiscernConfigHdController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private KxDiscernConfigHdService kxDiscernConfigHdService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    public Result<PageData<KxDiscernConfigHdDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<KxDiscernConfigHdDTO> page = kxDiscernConfigHdService.page(params);

        return new Result<PageData<KxDiscernConfigHdDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<KxDiscernConfigHdDTO> get(@PathVariable("id") Long id){
        KxDiscernConfigHdDTO data = kxDiscernConfigHdService.get(id);

        return new Result<KxDiscernConfigHdDTO>().ok(data);
    }

    @GetMapping("/getBydeviceId")
    @ApiOperation("信息")
    public Result<KxDiscernConfigHdDTO> getBydeviceId(@RequestParam Long deviceId){
        KxDiscernConfigHdDTO data = kxDiscernConfigHdService.getBydeviceId(deviceId);
        return new Result<KxDiscernConfigHdDTO>().ok(data);
    }

    @PostMapping("/analysisImg")
    @ApiOperation("分析图片")
    @LogOperation("分析图片")
    public void analysisImg(@RequestBody JSONObject jsonObject){
        String imgFilePath = jsonObject.getString("imgFilePath");
        String outImgFilePath = jsonObject.getString("outImgFilePath");
        Long deviceId = jsonObject.getLongValue("deviceId");
        Date picDate = jsonObject.getDate("picDate");
        if(null ==picDate){
            picDate=new Date();
        }
        if(StringUtils.isEmpty(imgFilePath) || StringUtils.isEmpty(outImgFilePath) || null ==deviceId ){
            logger.error("参数错误");
        }else {
            kxDiscernConfigHdService.analysisImg(imgFilePath,outImgFilePath,deviceId,picDate);
        }
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public Result save(@RequestBody KxDiscernConfigHdDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        kxDiscernConfigHdService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public Result update(@RequestBody KxDiscernConfigHdDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        kxDiscernConfigHdService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        kxDiscernConfigHdService.delete(ids);

        return new Result();
    }


}