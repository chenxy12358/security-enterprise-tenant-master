package io.renren.modules.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.pub.dao.SocketDao;
import io.renren.modules.pub.dto.SocketDTO;
import io.renren.modules.pub.entity.SocketEntity;
import io.renren.modules.pub.service.SocketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 通讯记录表
 *
 * @author zhengweicheng
 */
@Service
public class SocketServiceImpl extends CrudServiceImpl<SocketDao, SocketEntity, SocketDTO> implements SocketService {

    @Override
    public QueryWrapper<SocketEntity> getWrapper(Map<String, Object> params){
        String type = (String)params.get("type");
        String content = (String)params.get("content");
        QueryWrapper<SocketEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(type), "type", type);
        wrapper.like(StringUtils.isNotBlank(content), "content", content);
        //wrapper.eq(Constant.TENANT_CODE, SecurityUser.getTenantCode());
        return wrapper;
    }


}