package io.renren.modules.AppOrder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.AppArticles.dto.AppArticlesDTO;
import io.renren.modules.AppArticles.entity.AppArticlesEntity;
import io.renren.modules.AppArticles.service.AppArticlesService;
import io.renren.modules.AppOrder.dao.AppOrderDao;
import io.renren.modules.AppOrder.dto.AppOrderDTO;
import io.renren.modules.AppOrder.entity.AppOrderEntity;
import io.renren.modules.AppOrder.service.AppOrderService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 文章管理
 *
 * @author WEI 
 * @since 3.0 2022-08-17
 */
@Service
public class AppOrderServiceImpl extends CrudServiceImpl<AppOrderDao, AppOrderEntity, AppOrderDTO> implements AppOrderService {

    @Override
    public QueryWrapper<AppOrderEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<AppOrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public List<AppOrderDTO> getOrderList(Long id) {
        return this.baseDao.getOrderList(id);
    }
}