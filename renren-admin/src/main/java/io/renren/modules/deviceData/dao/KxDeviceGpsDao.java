package io.renren.modules.deviceData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.deviceData.dto.KxDeviceGpsDTO;
import io.renren.modules.deviceData.entity.KxDeviceGpsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 设备位置数据
*
* @author cxy 
* @since 3.0 2022-03-18
*/
@Mapper
public interface KxDeviceGpsDao extends BaseDao<KxDeviceGpsEntity> {

    List<KxDeviceGpsDTO> getList(Map<String, Object> params);

    KxDeviceGpsDTO getById(Long id);
}