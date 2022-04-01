package io.renren.modules.deviceData.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.deviceData.dto.KxDeviceGpsDTO;
import io.renren.modules.deviceData.entity.KxDeviceGpsEntity;

import java.util.Map;

/**
 * 设备位置数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
public interface KxDeviceGpsService extends CrudService<KxDeviceGpsEntity, KxDeviceGpsDTO> {

    PageData<KxDeviceGpsDTO> page(Map<String, Object> params);

    KxDeviceGpsDTO get(Long id);

}