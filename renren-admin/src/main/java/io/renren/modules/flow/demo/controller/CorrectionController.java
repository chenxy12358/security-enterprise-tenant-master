/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.demo.controller;

import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.modules.flow.demo.dto.CorrectionDTO;
import io.renren.modules.flow.demo.service.CorrectionService;
import io.renren.modules.flow.dto.ProcessInstanceDTO;
import io.renren.modules.flow.service.FlowRunningService;
import io.renren.modules.security.user.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 转正申请
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("flow/demo/correction")
@AllArgsConstructor
@Api(tags="转正申请")
public class CorrectionController {
    private final CorrectionService correctionService;
    private final FlowRunningService flowRunningService;

    @PostMapping("start")
    @ApiOperation("启动流程")
    @RequiresPermissions("sys:flow:all")
    public Result startProcess(@RequestBody CorrectionDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        //保存表单
        correctionService.save(dto);

        Map<String, Object> variables = new HashMap<>();
        variables.put("USERNAME", SecurityUser.getUserId().toString());
        ProcessInstanceDTO process = flowRunningService.startProcessInstanceById
                (dto.getProcessDefinitionId(), dto.getId() + "", variables);

        dto.setInstanceId(process.getProcessInstanceId());

        //更新流程实例ID
        correctionService.updateInstanceId(process.getProcessInstanceId(), dto.getId());

        return new Result();
    }

    @GetMapping("{id}")
    @ApiOperation("表单信息")
    @RequiresPermissions("sys:flow:all")
    public Result<CorrectionDTO> info(@PathVariable("id") Long id){
        CorrectionDTO correction = correctionService.get(id);

        return new Result<CorrectionDTO>().ok(correction);
    }

}
