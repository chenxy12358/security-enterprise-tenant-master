package io.renren.modules.AppUser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.AppUser.dao.AppUserDao;
import io.renren.modules.AppUser.dto.AppUserDTO;
import io.renren.modules.AppUser.entity.AppUserEntity;
import io.renren.modules.AppUser.service.AppUserService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * APP用户
 *
 * @author WEI 
 * @since 3.0 2022-08-22
 */
@Service
public class AppUserServiceImpl extends CrudServiceImpl<AppUserDao, AppUserEntity, AppUserDTO> implements AppUserService {

    @Override
    public QueryWrapper<AppUserEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<AppUserEntity> wrapper = new QueryWrapper<>();




        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));












        return wrapper;
    }


}