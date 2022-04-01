/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.renren.modules.ureport.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.ureport.entity.UReportDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UReportDataDao extends BaseDao<UReportDataEntity> {
}
