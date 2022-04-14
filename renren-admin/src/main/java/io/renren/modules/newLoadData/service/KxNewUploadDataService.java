package io.renren.modules.newLoadData.service;

import io.renren.common.service.CrudService;
import io.renren.modules.newLoadData.dto.KxNewUploadDataDTO;
import io.renren.modules.newLoadData.entity.KxNewUploadDataEntity;

/**
 * 最新上传数据
 *
 * @author WEI
 * @since 3.0 2022-04-14
 */
public interface KxNewUploadDataService extends CrudService<KxNewUploadDataEntity, KxNewUploadDataDTO> {


    void deleleByDeviceId(Long id);


}