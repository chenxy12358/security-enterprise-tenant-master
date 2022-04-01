package io.renren.modules.netWorkData.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.netWorkData.dao.KxNetWorkStateDataDao;
import io.renren.modules.netWorkData.dto.KxNetWorkStateDataDTO;
import io.renren.modules.netWorkData.entity.KxNetWorkStateDataEntity;
import io.renren.modules.netWorkData.service.KxNetWorkStateDataService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 网络状态信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-19
 */
@Service
public class KxNetWorkStateDataServiceImpl extends CrudServiceImpl<KxNetWorkStateDataDao, KxNetWorkStateDataEntity, KxNetWorkStateDataDTO> implements KxNetWorkStateDataService {

    @Override
    public QueryWrapper<KxNetWorkStateDataEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxNetWorkStateDataEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }
    @Override
    public PageData<KxNetWorkStateDataDTO> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "deviceName");
        paramsToLike(params, "sName");
        //分页
        IPage<KxNetWorkStateDataEntity> page = getPage(params, "null", false);

        //查询
        List<KxNetWorkStateDataDTO> list = baseDao.getList(params);
        return  new PageData<>(list, page.getTotal());
    }


}