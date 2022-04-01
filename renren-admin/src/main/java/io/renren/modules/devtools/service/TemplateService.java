/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.devtools.service;


import io.renren.common.page.PageData;
import io.renren.common.service.BaseService;
import io.renren.modules.devtools.entity.TemplateEntity;

import java.util.List;
import java.util.Map;

/**
 * 模板管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface TemplateService extends BaseService<TemplateEntity> {

    PageData<TemplateEntity> page(Map<String, Object> params);

    List<TemplateEntity> list();

    void updateStatusBatch(Long[] ids, int status);

}