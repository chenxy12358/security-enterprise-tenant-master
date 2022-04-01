package io.renren.modules.battery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.battery.dao.KxBatteryDao;
import io.renren.modules.battery.dto.KxBatteryDTO;
import io.renren.modules.battery.entity.KxBatteryEntity;
import io.renren.modules.battery.service.KxBatteryService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 电池信息 
 *
 * @author zhengweicheng 
 * @since 3.0 2022-02-08
 */
@Service
public class KxBatteryServiceImpl extends CrudServiceImpl<KxBatteryDao, KxBatteryEntity, KxBatteryDTO> implements KxBatteryService {

    @Override
    public QueryWrapper<KxBatteryEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxBatteryEntity> wrapper = new QueryWrapper<>();



        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));










        return wrapper;
    }


}