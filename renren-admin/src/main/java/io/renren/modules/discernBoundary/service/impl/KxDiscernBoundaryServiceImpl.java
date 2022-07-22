package io.renren.modules.discernBoundary.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.common.constant.DeviceInterfaceConstants;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.entity.KxDeviceEntity;
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
import java.util.List;
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
    public void savePresetPic(String deviceSn, JSONObject senderInfo, JSONObject msgInfo,String session) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("保存预置位图片，未找到对应设备数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }


            KxDiscernBoundaryDTO kdbDTO = new KxDiscernBoundaryDTO();
            JSONObject params=new JSONObject();
            Object obj = msgInfo.get("ResultValue");
            if (obj != null) {
                JSONObject resultValue = JSONUtil.parseObj(obj);
                JSONObject taskInfo =resultValue.getJSONObject("TaskInfo");
                int width =taskInfo.getInt("Height");
                int height =taskInfo.getInt("Width");
                kdbDTO.setPictureWidth(width);
                kdbDTO.setPictureHeight(height);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            kdbDTO.setUpdateDate(formatter.parse(formatter.format(new Date())));

            params.putOpt("deviceSn", deviceSn);
            params.putOpt("sessionTime", session);
            KxDiscernBoundaryEntity entity=this.getKxDiscernBoundaryDTO(params);
            if(null !=entity){
                kdbDTO=ConvertUtils.sourceToTarget(entity, KxDiscernBoundaryDTO.class);
            }
            msgInfo.remove("Code");
            msgInfo.remove("Result");
            msgInfo.remove("ResultValue");

            kdbDTO.setContent(String.valueOf(msgInfo));
            kdbDTO.setDeleted(KxConstants.NO);

            if(null !=kdbDTO.getId()){
                this.update(kdbDTO);
            }else {
                this.save(kdbDTO);
            }
        } catch (Exception e) {
            log.error("savePresetPic", e);
            e.printStackTrace();
        }
    }

    @Override
    public void saveFirst(JSONObject json) {
        KxDiscernBoundaryDTO dto =new KxDiscernBoundaryDTO();

        dto.setDeviceNo(json.getStr("deviceSn"));
        dto.setDeviceId(json.getLong("deviceId"));
        dto.setStationId(json.getLong("stationId"));
        dto.setCameraName(json.getStr("cameraName"));
        dto.setPictureHeight(json.getInt("Height"));
        dto.setPictureWidth(json.getInt("Width"));
        dto.setPresetNo(json.getStr("PresetId"));
        dto.setSessionTime(json.getStr("currentTime"));

        dto.setRemark("saveFirst");
        this.save(dto);

    }

    @Override
    public KxDiscernBoundaryEntity getKxDiscernBoundaryDTO(JSONObject json) {
        QueryWrapper<KxDiscernBoundaryEntity> wrapper = new QueryWrapper<>();
        String deviceSn =json.getStr("deviceSn");
        String cameraName =json.getStr("cameraName");
        String presetId =json.getStr("PresetId");
        String sessionTime =json.getStr("sessionTime");

        if(StringUtils.isNotBlank(deviceSn)){
            wrapper.eq("device_no", deviceSn);
        }
        if(StringUtils.isNotBlank(cameraName)){
            wrapper.eq("camera_name", cameraName);;
        }
        if(StringUtils.isNotBlank(presetId)){
            wrapper.eq("preset_no",presetId);
        }
        if(StringUtils.isNotBlank(sessionTime)){
            wrapper.eq("session_time", sessionTime);
            log.error("-------测试 session_time[{}]，deviceSn[{}]",
                    sessionTime,
                    deviceSn); //todo cxy
        }
        wrapper.eq("deleted", KxConstants.NO);
        List<KxDiscernBoundaryEntity> list = baseDao.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}