/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.flow.demo.dao.CorrectionDao;
import io.renren.modules.flow.demo.dto.CorrectionDTO;
import io.renren.modules.flow.demo.entity.CorrectionEntity;
import io.renren.modules.flow.demo.service.CorrectionService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 转正申请
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class CorrectionServiceImpl extends CrudServiceImpl<CorrectionDao, CorrectionEntity, CorrectionDTO> implements CorrectionService {

    @Override
    public QueryWrapper<CorrectionEntity> getWrapper(Map<String, Object> params){

        return null;
    }

    @Override
    public void updateInstanceId(String instanceId, Long id) {
        baseDao.updateInstanceId(instanceId, id);
    }
}