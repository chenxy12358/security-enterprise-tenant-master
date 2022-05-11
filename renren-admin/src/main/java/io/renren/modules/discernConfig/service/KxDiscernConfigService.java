package io.renren.modules.discernConfig.service;

import io.renren.common.service.CrudService;
import io.renren.modules.discernConfig.dto.KxDiscernConfigDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigEntity;

/**
 * 识别参数配置
 *
 * @author cxy 
 * @since 3.0 2022-03-08
 */
public interface KxDiscernConfigService extends CrudService<KxDiscernConfigEntity, KxDiscernConfigDTO> {

    KxDiscernConfigDTO getBydeviceId(Long deviceId);

    @Override
    void update(KxDiscernConfigDTO dto);

    @Override
    void save(KxDiscernConfigDTO dto);

}