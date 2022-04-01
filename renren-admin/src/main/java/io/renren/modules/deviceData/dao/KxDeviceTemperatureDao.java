package io.renren.modules.deviceData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.deviceData.dto.KxDeviceTemperatureDTO;
import io.renren.modules.deviceData.entity.KxDeviceTemperatureEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 设备温度数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Mapper
public interface KxDeviceTemperatureDao extends BaseDao<KxDeviceTemperatureEntity> {


    List<KxDeviceTemperatureDTO> getList(Map<String, Object> params);

    KxDeviceTemperatureDTO getById(Long id);
}