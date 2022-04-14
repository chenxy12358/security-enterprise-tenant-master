package io.renren.modules.newLoadData.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.newLoadData.dao.KxNewUploadDataDao;
import io.renren.modules.newLoadData.dto.KxNewUploadDataDTO;
import io.renren.modules.newLoadData.entity.KxNewUploadDataEntity;
import io.renren.modules.newLoadData.service.KxNewUploadDataService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 最新上传数据
 *
 * @author WEI 
 * @since 3.0 2022-04-14
 */
@Service
public class KxNewUploadDataServiceImpl extends CrudServiceImpl<KxNewUploadDataDao, KxNewUploadDataEntity, KxNewUploadDataDTO> implements KxNewUploadDataService {

    @Override
    public QueryWrapper<KxNewUploadDataEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxNewUploadDataEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public void deleleByDeviceId(Long id) {
        baseDao.delByDeviceId(id);
    }
}