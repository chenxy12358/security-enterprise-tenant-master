package io.renren.modules.AppOrder.service;

import io.renren.common.service.CrudService;
import io.renren.modules.AppOrder.dto.AppOrderDTO;
import io.renren.modules.AppOrder.entity.AppOrderEntity;

import java.util.List;

/**
 * 兑换记录
 *
 * @author WEI 
 * @since 3.0 2022-08-17
 */
public interface AppOrderService extends CrudService<AppOrderEntity, AppOrderDTO> {
    List<AppOrderDTO> getOrderList(Long id);
}