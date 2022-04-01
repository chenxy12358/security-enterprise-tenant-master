package io.renren.modules.deviceAlarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.deviceAlarm.dao.KxDeviceAlarmDao;
import io.renren.modules.deviceAlarm.dto.KxDeviceAlarmDTO;
import io.renren.modules.deviceAlarm.entity.KxDeviceAlarmEntity;
import io.renren.modules.deviceAlarm.service.KxDeviceAlarmService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 可视化告警
 *
 * @author cxy
 * @since 3.0 2022-02-25
 */
@Service
public class KxDeviceAlarmServiceImpl extends CrudServiceImpl<KxDeviceAlarmDao, KxDeviceAlarmEntity, KxDeviceAlarmDTO> implements KxDeviceAlarmService {

    @Override
    public QueryWrapper<KxDeviceAlarmEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxDeviceAlarmEntity> wrapper = new QueryWrapper<>();



        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));














        return wrapper;
    }


}