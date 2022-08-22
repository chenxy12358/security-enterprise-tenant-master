package io.renren.modules.AppArticles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.AppArticles.dao.AppArticlesDao;
import io.renren.modules.AppArticles.dto.AppArticlesDTO;
import io.renren.modules.AppArticles.entity.AppArticlesEntity;
import io.renren.modules.AppArticles.service.AppArticlesService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
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
public class AppArticlesServiceImpl extends CrudServiceImpl<AppArticlesDao, AppArticlesEntity, AppArticlesDTO> implements AppArticlesService {

    @Override
    public QueryWrapper<AppArticlesEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<AppArticlesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));

        return wrapper;
    }


    @Override
    public List<AppArticlesDTO> getArticlesList(String type) {
        return this.baseDao.getArticlesList(type);
    }
}