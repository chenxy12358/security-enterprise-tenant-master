package io.renren.modules.scheduleItem.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.entity.KxScheduleItemEntity;

import java.util.Map;

/**
 * 计划任务拍照结果
 *
 * @author cxy 
 * @since 3.0 2022-03-05
 */
public interface KxScheduleItemService extends CrudService<KxScheduleItemEntity, KxScheduleItemDTO> {

    /**
     * 列表查询
     * @param params
     * @return
     */
    PageData<KxScheduleItemDTO> page(Map<String, Object> params);


    /**
     *
     * @param id
     * @return
     */
    KxScheduleItemDTO get(Long id);

}