package io.renren.modules.battery.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.battery.entity.KxBatteryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 电池信息 
*
* @author zhengweicheng 
* @since 3.0 2022-02-08
*/
@Mapper
public interface KxBatteryDao extends BaseDao<KxBatteryEntity> {
	
}