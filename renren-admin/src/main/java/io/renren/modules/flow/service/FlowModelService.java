/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.modules.flow.dao.FlowModelDao;
import io.renren.modules.flow.entity.FlowModelEntity;
import io.renren.modules.security.user.SecurityUser;
import lombok.AllArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 模型管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
@AllArgsConstructor
public class FlowModelService extends BaseServiceImpl<FlowModelDao, FlowModelEntity> {
    private final RepositoryService repositoryService;
    private final ModelService modelService;
    private final ModelRepository modelRepository;

    public PageData<FlowModelEntity> page(Map<String, Object> params) {
        //分页
        IPage<FlowModelEntity> page = getPage(params, "created", false);

        //查询
        List<FlowModelEntity> list = baseDao.getList(params);

        return new PageData<>(list, page.getTotal());
    }

    /**
     * 部署
     * @param modelId  模型ID
     */
    public String deploymentByModelId(String modelId) {
        Model model = modelService.getModel(modelId);
        BpmnModel bpmnModel = modelService.getBpmnModel(model);
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addBpmnModel(model.getKey() + ".bpmn20.xml", bpmnModel).deploy();
        return deployment.getId();
    }


    /**
     * 将部署的流程转换为模型
     * @param id 流程ID
     */
    public void convertToModel(String id) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, StandardCharsets.UTF_8);
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = new Model();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getName());

        int version = (int)repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1;
        modelData.setVersion(version);
        modelData.setModelType(0);
        modelData.setCreatedBy(SecurityUser.getUser().getUsername());
        modelData.setLastUpdatedBy(SecurityUser.getUser().getUsername());
        modelData.setLastUpdated(new Date());

        List<String> names = repositoryService.getDeploymentResourceNames(processDefinition.getDeploymentId());
        for (String name : names) {
            if(name.contains("png")) {
                InputStream is = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), name);

                byte[] b = new byte[is.available()];
                IOUtils.readFully(is, b);

                modelData.setThumbnail(b);

                IOUtils.closeQuietly(is);
            }
        }

        modelData.setModelEditorJson(modelNode.toString());
        modelRepository.save(modelData);
    }
}
