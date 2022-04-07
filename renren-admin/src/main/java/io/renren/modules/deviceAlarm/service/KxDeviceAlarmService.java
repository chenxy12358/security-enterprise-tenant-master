package io.renren.modules.deviceAlarm.service;

import io.renren.common.service.CrudService;
import io.renren.modules.deviceAlarm.dto.KxDeviceAlarmDTO;
import io.renren.modules.deviceAlarm.entity.KxDeviceAlarmEntity;

import java.util.Date;
import java.util.List;

/**
 * 可视化告警
 *
 * @author cxy 
 * @since 3.0 2022-02-25
 */
public interface KxDeviceAlarmService extends CrudService<KxDeviceAlarmEntity, KxDeviceAlarmDTO> {
    /**
     *
     * @param listInfo
     * @param deviceID
     * @param picDate
     */
    void saveAnalysisImg(List listInfo, Long deviceID, Date picDate);

}