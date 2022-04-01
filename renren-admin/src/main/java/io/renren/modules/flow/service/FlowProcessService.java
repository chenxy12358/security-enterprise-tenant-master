/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.service;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
@AllArgsConstructor
public class FlowProcessService {
    private final RepositoryService repositoryService;

    /**
     * 流程列表
     */
    public PageData<Map<String, Object>> page(Map<String, Object> params) {
        String key = (String)params.get("key");
        String processName = (String)params.get("processName");
        boolean isLatestVersion = params.get("isLatestVersion")== null? false : (boolean)params.get("isLatestVersion");


        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionId().desc().orderByProcessDefinitionKey().desc();
        if(isLatestVersion){
            processDefinitionQuery.latestVersion();
        }
        if(StringUtils.isNotBlank(key)){
            processDefinitionQuery.processDefinitionKeyLike("%" + key + "%");
        }
        if(StringUtils.isNotBlank(processName)){
            processDefinitionQuery.processDefinitionNameLike("%" + processName + "%");
        }

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(PageUtils.getPageOffset(params), PageUtils.getPageLimit(params));

        List<Map<String, Object>> objectList = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            objectList.add(processDefinitionConvert(processDefinition));
        }

        return new PageData<>(objectList, (int)processDefinitionQuery.count());
    }

    /**
     * 流程定义信息
     */
    private Map<String, Object> processDefinitionConvert(ProcessDefinition processDefinition) {
        String deploymentId = processDefinition.getDeploymentId();
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

        Map<String, Object> map = new HashMap<>(9);
        map.put("suspended", processDefinition.isSuspended());
        map.put("id", processDefinition.getId());
        map.put("deploymentId", processDefinition.getDeploymentId());
        map.put("name", processDefinition.getName());
        map.put("key", processDefinition.getKey());
        map.put("version", processDefinition.getVersion());
        map.put("resourceName", processDefinition.getResourceName());
        map.put("diagramResourceName", processDefinition.getDiagramResourceName());
        map.put("deploymentTime", DateUtils.format(deployment.getDeploymentTime(), DateUtils.DATE_TIME_PATTERN));

        return map;
    }

    /**
     * 部署
     * @param file 文件
     */
    public void deploy(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        if("zip".equalsIgnoreCase(extension)) {
            ZipInputStream zip = new ZipInputStream(file.getInputStream());
            repositoryService.createDeployment().addZipInputStream(zip).deploy();
        }else if(fileName.indexOf("bpmn20.xml") != -1){
            repositoryService.createDeployment().addInputStream(fileName, file.getInputStream()).deploy();
        }else if("bpmn".equalsIgnoreCase(extension)){
            repositoryService.createDeployment().addInputStream(fileName, file.getInputStream()).deploy();
        }else{
            throw new RenException(ErrorCode.ACT_DEPLOY_FORMAT_ERROR);
        }
    }

    /**
     * 激活流程
     * @param id 流程ID
     */
    public void active(String id){
        repositoryService.activateProcessDefinitionById(id, true, null);
    }

    /**
     * 挂起流程
     * @param id 流程ID
     */
    public void suspend(String id){
        repositoryService.suspendProcessDefinitionById(id, true, null);
    }

    /**
     * 获取资源文件
     * @param deploymentId  部署ID
     * @param resourceName 资源名称
     */
    public InputStream getResourceAsStream(String deploymentId, String resourceName) {
        return repositoryService.getResourceAsStream(deploymentId, resourceName);
    }

    /**
     * 删除部署
     * @param deploymentId  部署ID
     */
    public void deleteDeployment(String deploymentId){
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
