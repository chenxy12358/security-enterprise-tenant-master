package io.renren.modules.deviceData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.deviceData.dto.KxDeviceAccelerationDTO;
import io.renren.modules.deviceData.entity.KxDeviceAccelerationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 设备加速度数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Mapper
public interface KxDeviceAccelerationDao extends BaseDao<KxDeviceAccelerationEntity> {

    List<KxDeviceAccelerationDTO> getList(Map<String, Object> params);

    KxDeviceAccelerationDTO getById(Long id);
	
}