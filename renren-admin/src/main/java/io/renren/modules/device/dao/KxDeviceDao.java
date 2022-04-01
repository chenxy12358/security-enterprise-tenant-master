package io.renren.modules.device.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.device.entity.KxDeviceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 设备信息 
*
* @author cxy 
* @since 3.0 2022-02-16
*/
@Mapper
public interface KxDeviceDao extends BaseDao<KxDeviceEntity> {

    /**
     * 查询设备信息
     *
     * @param station_id
     * @return
     */
    List<KxDeviceEntity> getListByStationId(@Param("station_id") Long station_id);

    KxDeviceEntity getById(Long id);
    KxDeviceEntity getBySerialNo(String serialNo);
}