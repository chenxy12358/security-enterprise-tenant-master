package io.renren.modules.pub.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.pub.entity.SocketEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通讯记录表
 *
 * @author zhengweicheng
 */
@Mapper
public interface SocketDao extends BaseDao<SocketEntity> {
	
}