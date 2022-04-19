package io.renren.modules.scheduleJob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.scheduleJob.dao.KxScheduleJobDao;
import io.renren.modules.scheduleJob.dto.KxScheduleJobDTO;
import io.renren.modules.scheduleJob.dto.KxScheduleJobPageDTO;
import io.renren.modules.scheduleJob.entity.KxScheduleJobEntity;
import io.renren.modules.scheduleJob.service.KxScheduleJobService;
import io.renren.modules.security.user.SecurityUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 计划任务
 *
 * @author cxy
 * @since 3.0 2022-02-27
 */
@Service
public class KxScheduleJobServiceImpl extends CrudServiceImpl<KxScheduleJobDao, KxScheduleJobEntity, KxScheduleJobDTO> implements KxScheduleJobService {

    @Override
    public QueryWrapper<KxScheduleJobEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxScheduleJobEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        String deviceId = (String) params.get("deviceId");
        wrapper.eq("device_id", deviceId);
        System.err.println(params);
        // 1. entrySet遍历，在键和值都需要时使用（最常用）
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            System.err.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//        }
        return wrapper;
    }


    @Override
    public PageData<KxScheduleJobPageDTO> pageNew(Map<String, Object> params) {
        IPage<KxScheduleJobEntity> page = baseDao.selectPage(
                getPage(params, null, false),
                getWrapper(params)
        );
        return getPageData(page, KxScheduleJobPageDTO.class);

    }

    @Override
    public List<KxScheduleJobEntity> getInfoByDeviceId(Long deviceId) {
        QueryWrapper<KxScheduleJobEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("device_id", deviceId);
        List<KxScheduleJobEntity> list =baseDao.selectList(wrapper);
        return list;
    }


    @Override
    public void update(KxScheduleJobDTO dto) {

        KxScheduleJobEntity kxScheduleJobEntity = new KxScheduleJobEntity();
        kxScheduleJobEntity = this.setPeriod(dto, kxScheduleJobEntity);
        this.updateById(kxScheduleJobEntity);
    }





    /**
     * 设置周期
     *
     * @param dto
     * @param kxScheduleJobEntity
     * @return
     */
    private KxScheduleJobEntity setPeriod(KxScheduleJobDTO dto, KxScheduleJobEntity kxScheduleJobEntity) {
        Arrays.sort(dto.getPeriod());
        String str = String.join(",", dto.getPeriod());

        BeanUtils.copyProperties(dto, kxScheduleJobEntity, "period");
        kxScheduleJobEntity.setPeriod(str);
        //copy主键值到dto
        BeanUtils.copyProperties(kxScheduleJobEntity, dto);

        return kxScheduleJobEntity;

    }
}