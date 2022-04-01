/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.devtools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.devtools.entity.TemplateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 模板管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface TemplateDao extends BaseMapper<TemplateEntity> {

    int updateStatusBatch(Map<String, Object> map);
}