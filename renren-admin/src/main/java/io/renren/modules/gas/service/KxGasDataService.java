package io.renren.modules.gas.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.gas.dto.KxGasDataDTO;
import io.renren.modules.gas.entity.KxGasDataEntity;

import java.util.Map;

/**
 * 燃气信息 
 *
 * @author WEI 
 * @since 3.0 2022-02-18
 */
public interface KxGasDataService extends CrudService<KxGasDataEntity, KxGasDataDTO> {

    PageData<KxGasDataDTO> page(Map<String, Object> params);

}