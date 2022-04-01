package io.renren.modules.scheduleItem.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.scheduleItem.dao.KxScheduleItemDao;
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.entity.KxScheduleItemEntity;
import io.renren.modules.scheduleItem.service.KxScheduleItemService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 计划任务拍照结果
 *
 * @author cxy 
 * @since 3.0 2022-03-05
 */
@Service
public class KxScheduleItemServiceImpl extends CrudServiceImpl<KxScheduleItemDao, KxScheduleItemEntity, KxScheduleItemDTO> implements KxScheduleItemService {

    @Override
    public QueryWrapper<KxScheduleItemEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxScheduleItemEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }
    @Override
    public PageData<KxScheduleItemDTO> page(Map<String, Object> params) {
        //转换成like

        return  null;
    }


    @Override
    public KxScheduleItemDTO get(Long id) {
        KxScheduleItemDTO entity = baseDao.getById(id);
        entity=this.getTranslateInfo(entity);
        return entity;
    }
    /**
     * 信息转换
     * @param entity
     * @return
     */
    private KxScheduleItemDTO getTranslateInfo(KxScheduleItemDTO entity) {
        String cotent = (String) entity.getContent();
        JSONObject cotentJson = JSONObject.parseObject(cotent);
        JSONArray jsonArray = cotentJson.getJSONArray("Files");
        JSONObject files = jsonArray.getJSONObject(0);
        String pictureName = files.getString("Uri");
        String picturUrl = files.getString("Uri320");
        String fileTime = files.getString("DateTime");
        entity.setPictureName(pictureName);
        entity.setPicturUrl(picturUrl);
        entity.setFileTime(fileTime);
        return entity;

    }


}