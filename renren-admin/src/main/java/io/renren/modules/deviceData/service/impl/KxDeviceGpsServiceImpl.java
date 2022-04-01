package io.renren.modules.deviceData.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.deviceData.dao.KxDeviceGpsDao;
import io.renren.modules.deviceData.dto.KxDeviceGpsDTO;
import io.renren.modules.deviceData.entity.KxDeviceGpsEntity;
import io.renren.modules.deviceData.service.KxDeviceGpsService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 设备位置数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
@Service
public class KxDeviceGpsServiceImpl extends CrudServiceImpl<KxDeviceGpsDao, KxDeviceGpsEntity, KxDeviceGpsDTO> implements KxDeviceGpsService {

    @Override
    public QueryWrapper<KxDeviceGpsEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxDeviceGpsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }

    @Override
    public PageData<KxDeviceGpsDTO> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "stationName");
        paramsToLike(params, "deviceName");
        //分页
        IPage<KxDeviceGpsEntity> page = getPage(params, "null", false);
        //查询
        List<KxDeviceGpsDTO> list = baseDao.getList(params);
        return  new PageData<>(list, page.getTotal());
    }
    @Override
    public KxDeviceGpsDTO get(Long id) {
        KxDeviceGpsDTO entity = baseDao.getById(id);
        return entity;
    }


}