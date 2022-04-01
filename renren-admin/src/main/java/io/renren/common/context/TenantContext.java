/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.common.context;

import io.renren.common.utils.HttpContextUtils;
import io.renren.modules.security.user.UserDetail;
import io.renren.modules.sys.enums.SuperAdminEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 租户
 *
 * @author Mark sunlightcs@gmail.com
 */
public class TenantContext {

    public static Long getTenantCode(UserDetail user){
        if(user.getTenantCode() == null){
            return null;
        }

        String tenantCode = HttpContextUtils.getTenantCode();
        //超级管理员，才可以切换租户
        if(user.getSuperAdmin() == SuperAdminEnum.YES.value()){
            if(StringUtils.isNotBlank(tenantCode)){
                return Long.parseLong(tenantCode);
            }
        }
        return user.getTenantCode();
    }
}
