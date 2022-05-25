package io.renren.modules.notice.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.notice.entity.SysNoticeSwitchEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* notice
*
* @author WEI 
* @since 3.0 2022-05-23
*/
@Mapper
public interface SysNoticeSwitchDao extends BaseDao<SysNoticeSwitchEntity> {
    SysNoticeSwitchEntity getByUserId(Long id);

	
}