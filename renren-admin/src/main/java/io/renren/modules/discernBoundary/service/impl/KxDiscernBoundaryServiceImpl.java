package io.renren.modules.discernBoundary.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.discernBoundary.dao.KxDiscernBoundaryDao;
import io.renren.modules.discernBoundary.dto.KxDiscernBoundaryDTO;
import io.renren.modules.discernBoundary.entity.KxDiscernBoundaryEntity;
import io.renren.modules.discernBoundary.service.KxDiscernBoundaryService;
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.service.KxScheduleItemService;
import io.renren.modules.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 识别边界标定
 *
 * @author cxy
 * @since 3.0 2022-07-19
 */
@Service
@Slf4j
public class KxDiscernBoundaryServiceImpl extends CrudServiceImpl<KxDiscernBoundaryDao, KxDiscernBoundaryEntity, KxDiscernBoundaryDTO> implements KxDiscernBoundaryService {

    @Autowired
    private KxScheduleItemService kxScheduleItemService;
    @Autowired
    private KxDeviceService kxDeviceService;

    @Override
    public QueryWrapper<KxDiscernBoundaryEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxDiscernBoundaryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    /**
     * 保存预置位图片
     * @param deviceSn
     * @param senderInfo
     * @param msgInfo
     */
    @Override
    public void savePresetPic(String deviceSn, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("计划任务，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            KxScheduleItemDTO itemDTO = new KxScheduleItemDTO();
            itemDTO.setDeviceNo(String.valueOf(senderInfo.get("SerialNo")));
            itemDTO.setSignalType(String.valueOf(senderInfo.get("Signal")));
            itemDTO.setDeviceId(deviceDTO.getId());
            Object taskInfo = msgInfo.get("TaskInfo");
            if (taskInfo != null) {
                JSONObject jsonObject = JSONUtil.parseObj(taskInfo);
                if (jsonObject.get("ScheduleItemId") != null) {
                    itemDTO.setScheduleJobId((Long) jsonObject.get("ScheduleItemId"));
                }
                if (jsonObject.get("Type") != null) {
                    itemDTO.setItemType("savePresetPic");
                }
            }
            itemDTO.setDeleted("f");
            itemDTO.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdateTime").toString())) {
                itemDTO.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                itemDTO.setUpdateDate(formatter.parse(String.valueOf(msgInfo.get("UpdateTime"))));
            }
            kxScheduleItemService.save(itemDTO);
            // todo   向总线的告警消息组发送通知信息，其它模块可以获取做后续处理，如通知前端、短信、微信发送等
            //  计划用消息队列，订阅的方式通知其他模块
        } catch (Exception e) {
            log.error("savePic", e);
            e.printStackTrace();
        }
    }
}