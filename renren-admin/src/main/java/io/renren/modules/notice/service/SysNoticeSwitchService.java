package io.renren.modules.notice.service;

import io.renren.common.service.CrudService;
import io.renren.modules.notice.dto.SysNoticeSwitchDTO;
import io.renren.modules.notice.entity.SysNoticeSwitchEntity;

/**
 * notice
 *
 * @author WEI
 * @since 3.0 2022-05-23
 */
public interface SysNoticeSwitchService extends CrudService<SysNoticeSwitchEntity, SysNoticeSwitchDTO> {
    void changeStatus(String flag);
    SysNoticeSwitchEntity getByUserId();

    SysNoticeSwitchEntity getByUserId(Long id);

}