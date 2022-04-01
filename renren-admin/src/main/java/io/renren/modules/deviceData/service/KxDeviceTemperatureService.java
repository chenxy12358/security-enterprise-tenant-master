package io.renren.modules.deviceData.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.deviceData.dto.KxDeviceTemperatureDTO;
import io.renren.modules.deviceData.entity.KxDeviceTemperatureEntity;

import java.util.Map;

/**
 * 设备温度数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
public interface KxDeviceTemperatureService extends CrudService<KxDeviceTemperatureEntity, KxDeviceTemperatureDTO> {
    PageData<KxDeviceTemperatureDTO> page(Map<String, Object> params);

    KxDeviceTemperatureDTO get(Long id);

}