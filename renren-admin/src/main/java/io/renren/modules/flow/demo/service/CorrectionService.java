/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.demo.service;

import io.renren.common.service.CrudService;
import io.renren.modules.flow.demo.dto.CorrectionDTO;
import io.renren.modules.flow.demo.entity.CorrectionEntity;

/**
 * 转正申请
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface CorrectionService extends CrudService<CorrectionEntity, CorrectionDTO> {

    void updateInstanceId(String instanceId, Long id);
}