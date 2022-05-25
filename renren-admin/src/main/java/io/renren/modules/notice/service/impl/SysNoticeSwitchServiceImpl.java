package io.renren.modules.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.notice.dao.SysNoticeSwitchDao;
import io.renren.modules.notice.dto.SysNoticeSwitchDTO;
import io.renren.modules.notice.entity.SysNoticeSwitchEntity;
import io.renren.modules.notice.service.SysNoticeSwitchService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * notice
 *
 * @author WEI 
 * @since 3.0 2022-05-23
 */
@Service
public class SysNoticeSwitchServiceImpl extends CrudServiceImpl<SysNoticeSwitchDao, SysNoticeSwitchEntity, SysNoticeSwitchDTO> implements SysNoticeSwitchService {

    @Override
    public QueryWrapper<SysNoticeSwitchEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<SysNoticeSwitchEntity> wrapper = new QueryWrapper<>();




        return wrapper;
    }


    @Override
    public void changeStatus(String flag) {
        UserDetail user = SecurityUser.getUser();
        SysNoticeSwitchEntity byUserId = this.baseDao.getByUserId(user.getId());
        if (byUserId != null) {
            byUserId.setStatus(flag);
            this.baseDao.updateById(byUserId);
        }else {
            SysNoticeSwitchEntity dto = new SysNoticeSwitchEntity();
            dto.setUserId(user.getId());
            dto.setStatus(flag);
            this.baseDao.insert(dto);
        }
    }

    @Override
    public SysNoticeSwitchEntity getByUserId() {
        UserDetail user = SecurityUser.getUser();
        SysNoticeSwitchEntity re = new SysNoticeSwitchEntity();
        re.setStatus("true");
        SysNoticeSwitchEntity byUserId = this.baseDao.getByUserId(user.getId());
        if (byUserId != null) {
            re.setStatus(byUserId.getStatus());
        }
        return byUserId;
    }

    @Override
    public SysNoticeSwitchEntity getByUserId(Long id) {
        SysNoticeSwitchEntity re = new SysNoticeSwitchEntity();
        re.setStatus("true");
        SysNoticeSwitchEntity byUserId = this.baseDao.getByUserId(id);
        if (byUserId != null) {
            re.setStatus(byUserId.getStatus());
        }
        return byUserId;
    }
}