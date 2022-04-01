/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.flow.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.flow.entity.FlowModelEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 模型管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface FlowModelDao extends BaseDao<FlowModelEntity> {

    List<FlowModelEntity> getList(Map<String, Object> params);

}
