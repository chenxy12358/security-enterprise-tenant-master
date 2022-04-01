package io.renren.modules.scheduleJob.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.scheduleJob.entity.KxScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 计划任务
*
* @author cxy 
* @since 3.0 2022-02-27
*/
@Mapper
public interface KxScheduleJobDao extends BaseDao<KxScheduleJobEntity> {
	
}