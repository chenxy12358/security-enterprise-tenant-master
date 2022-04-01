package io.renren.modules.scheduleItem.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.entity.KxScheduleItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 计划任务拍照结果
*
* @author cxy 
* @since 3.0 2022-03-05
*/
@Mapper
public interface KxScheduleItemDao extends BaseDao<KxScheduleItemEntity> {

    List<KxScheduleItemDTO> getList(Map<String, Object> params);

    KxScheduleItemDTO getById(Long id);
	
}