package io.renren.modules.deviceData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.deviceData.dto.KxDeviceInclinationDTO;
import io.renren.modules.deviceData.entity.KxDeviceInclinationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 设备倾斜数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Mapper
public interface KxDeviceInclinationDao extends BaseDao<KxDeviceInclinationEntity> {

    List<KxDeviceInclinationDTO> getList(Map<String, Object> params);

    KxDeviceInclinationDTO getById(Long id);
	
}