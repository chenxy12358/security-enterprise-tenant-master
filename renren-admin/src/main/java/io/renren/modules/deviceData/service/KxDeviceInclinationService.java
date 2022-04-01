package io.renren.modules.deviceData.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.deviceData.dto.KxDeviceInclinationDTO;
import io.renren.modules.deviceData.entity.KxDeviceInclinationEntity;

import java.util.Map;

/**
 * 设备倾斜数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
public interface KxDeviceInclinationService extends CrudService<KxDeviceInclinationEntity, KxDeviceInclinationDTO> {

    PageData<KxDeviceInclinationDTO> page(Map<String, Object> params);

    KxDeviceInclinationDTO get(Long id);

}