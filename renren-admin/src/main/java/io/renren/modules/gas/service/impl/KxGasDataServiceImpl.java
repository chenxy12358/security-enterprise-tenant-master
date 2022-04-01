package io.renren.modules.gas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.gas.dao.KxGasDataDao;
import io.renren.modules.gas.dto.KxGasDataDTO;
import io.renren.modules.gas.entity.KxGasDataEntity;
import io.renren.modules.gas.service.KxGasDataService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 燃气信息 
 *
 * @author WEI 
 * @since 3.0 2022-02-18
 */
@Service
public class KxGasDataServiceImpl extends CrudServiceImpl<KxGasDataDao, KxGasDataEntity, KxGasDataDTO> implements KxGasDataService {

    @Override
    public QueryWrapper<KxGasDataEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxGasDataEntity> wrapper = new QueryWrapper<>();


        String tenantCode = (String)params.get("tenantCode");
        wrapper.eq(StringUtils.isNotBlank(tenantCode), "tenant_code", tenantCode);

        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));

        return wrapper;
    }


    @Override
    public PageData<KxGasDataDTO> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "stationName");
        paramsToLike(params, "deviceName");
        //查询
        List<KxGasDataDTO> list = baseDao.getList(params);
        return  new PageData<>(list, list.size());
    }
}