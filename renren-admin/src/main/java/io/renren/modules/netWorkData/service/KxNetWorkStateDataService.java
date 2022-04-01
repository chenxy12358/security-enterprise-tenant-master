package io.renren.modules.netWorkData.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.netWorkData.dto.KxNetWorkStateDataDTO;
import io.renren.modules.netWorkData.entity.KxNetWorkStateDataEntity;

import java.util.Map;

/**
 * 网络状态信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-19
 */
public interface KxNetWorkStateDataService extends CrudService<KxNetWorkStateDataEntity, KxNetWorkStateDataDTO> {
    PageData<KxNetWorkStateDataDTO> page(Map<String, Object> params);

}