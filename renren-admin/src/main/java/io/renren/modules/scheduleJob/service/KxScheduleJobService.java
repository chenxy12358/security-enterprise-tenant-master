package io.renren.modules.scheduleJob.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.scheduleJob.dto.KxScheduleJobDTO;
import io.renren.modules.scheduleJob.dto.KxScheduleJobPageDTO;
import io.renren.modules.scheduleJob.entity.KxScheduleJobEntity;

import java.util.List;
import java.util.Map;

/**
 * 计划任务
 *
 * @author cxy 
 * @since 3.0 2022-02-27
 */
public interface KxScheduleJobService extends CrudService<KxScheduleJobEntity, KxScheduleJobDTO> {


    @Override
    void update(KxScheduleJobDTO dto);

    @Override
    void save(KxScheduleJobDTO dto);

    PageData<KxScheduleJobPageDTO> pageNew(Map<String, Object> params);
    @Override
    KxScheduleJobDTO get(Long id);


    /**
     * 通过设备id取得对应配置信息
     * @param deviceId 设备id
     */
    List<KxScheduleJobEntity> getInfoByDeviceId(Long deviceId);


}