package io.renren.modules.deviceAlarm.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.deviceAlarm.entity.KxDeviceAlarmEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 可视化告警
*
* @author cxy 
* @since 3.0 2022-02-25
*/
@Mapper
public interface KxDeviceAlarmDao extends BaseDao<KxDeviceAlarmEntity> {
	
}