package io.renren.modules.deviceData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.deviceData.dto.KxDeviceHumidityDTO;
import io.renren.modules.deviceData.entity.KxDeviceHumidityEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 设备湿度数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Mapper
public interface KxDeviceHumidityDao extends BaseDao<KxDeviceHumidityEntity> {

    List<KxDeviceHumidityDTO> getList(Map<String, Object> params);

    KxDeviceHumidityDTO getById(Long id);
	
}