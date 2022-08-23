package io.renren.modules.AppUser.service;

import io.renren.common.service.CrudService;
import io.renren.modules.AppUser.dto.AppUserDTO;
import io.renren.modules.AppUser.entity.AppUserEntity;

/**
 * APP用户
 *
 * @author WEI 
 * @since 3.0 2022-08-22
 */
public interface AppUserService extends CrudService<AppUserEntity, AppUserDTO> {

}