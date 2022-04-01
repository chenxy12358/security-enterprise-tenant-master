/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.flow.demo.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.flow.demo.entity.CorrectionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 转正申请
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface CorrectionDao extends BaseDao<CorrectionEntity> {

    void updateInstanceId(String instanceId, Long id);
}