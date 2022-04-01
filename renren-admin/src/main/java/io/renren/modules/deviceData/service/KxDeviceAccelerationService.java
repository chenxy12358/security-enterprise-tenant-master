package io.renren.modules.deviceData.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.deviceData.dto.KxDeviceAccelerationDTO;
import io.renren.modules.deviceData.entity.KxDeviceAccelerationEntity;

import java.util.Map;

/**
 * 设备加速度数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
public interface KxDeviceAccelerationService extends CrudService<KxDeviceAccelerationEntity, KxDeviceAccelerationDTO> {

    PageData<KxDeviceAccelerationDTO> page(Map<String, Object> params);

    KxDeviceAccelerationDTO get(Long id);


}