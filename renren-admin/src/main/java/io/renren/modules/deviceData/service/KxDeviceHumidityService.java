package io.renren.modules.deviceData.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.deviceData.dto.KxDeviceHumidityDTO;
import io.renren.modules.deviceData.entity.KxDeviceHumidityEntity;

import java.util.Map;

/**
 * 设备湿度数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
public interface KxDeviceHumidityService extends CrudService<KxDeviceHumidityEntity, KxDeviceHumidityDTO> {

    PageData<KxDeviceHumidityDTO> page(Map<String, Object> params);

    KxDeviceHumidityDTO get(Long id);


}