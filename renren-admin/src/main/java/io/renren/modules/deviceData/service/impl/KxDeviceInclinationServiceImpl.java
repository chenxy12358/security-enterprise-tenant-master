package io.renren.modules.deviceData.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.constant.Constant;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.deviceData.dao.KxDeviceInclinationDao;
import io.renren.modules.deviceData.dto.KxDeviceInclinationDTO;
import io.renren.modules.deviceData.entity.KxDeviceInclinationEntity;
import io.renren.modules.deviceData.service.KxDeviceInclinationService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 设备倾斜数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
@Service
public class KxDeviceInclinationServiceImpl extends CrudServiceImpl<KxDeviceInclinationDao, KxDeviceInclinationEntity, KxDeviceInclinationDTO> implements KxDeviceInclinationService {

    @Override
    public QueryWrapper<KxDeviceInclinationEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxDeviceInclinationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }

    @Override
    public PageData<KxDeviceInclinationDTO> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "stationName");
        paramsToLike(params, "deviceName");
        IPage<KxDeviceInclinationEntity> page = null;
        String orderField = (String)params.get("orderField");
        //分页
        if(!StringUtils.isNotBlank(orderField)){
            page = getPage(params, Constant.CREATE_DATE, false);
        }else {
            page = getPage(params, null, false);
        }
        //查询
        List<KxDeviceInclinationDTO> list = baseDao.getList(params);
        return  new PageData<>(list, page.getTotal());
    }
    @Override
    public KxDeviceInclinationDTO get(Long id) {
        KxDeviceInclinationDTO entity = baseDao.getById(id);
        return entity;
    }


}