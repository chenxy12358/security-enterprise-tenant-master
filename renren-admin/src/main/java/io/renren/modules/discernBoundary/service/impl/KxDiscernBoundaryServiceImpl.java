package io.renren.modules.discernBoundary.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.common.constant.KxAiBoundary;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.discernBoundary.dao.KxDiscernBoundaryDao;
import io.renren.modules.discernBoundary.dto.KxDiscernBoundaryDTO;
import io.renren.modules.discernBoundary.entity.KxDiscernBoundaryEntity;
import io.renren.modules.discernBoundary.service.KxDiscernBoundaryService;
import io.renren.modules.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
     *
     * @param deviceSn
     * @param senderInfo
     * @param msgInfo
     */
    @Override
    public void savePresetPic(String deviceSn, JSONObject senderInfo, JSONObject msgInfo, String session) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("保存预置位图片，未找到对应设备数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            KxDiscernBoundaryDTO kdbDTO = new KxDiscernBoundaryDTO();
            JSONObject params = new JSONObject();
            Object obj = msgInfo.get("ResultValue");
            if (obj != null) {
                JSONObject resultValue = JSONUtil.parseObj(obj);
                JSONObject taskInfo = resultValue.getJSONObject("TaskInfo");
                int width = taskInfo.getInt("Height");
                int height = taskInfo.getInt("Width");
                kdbDTO.setPictureWidth(width);
                kdbDTO.setPictureHeight(height);
            }

            params.putOpt("deviceSn", deviceSn);
            params.putOpt("sessionTime", session);
            List<KxDiscernBoundaryEntity> list = this.getKxDiscernBoundaryDTO(params);
            if (null != list && list.size() > 0) {
                KxDiscernBoundaryEntity entity = list.get(0);
                kdbDTO = ConvertUtils.sourceToTarget(entity, KxDiscernBoundaryDTO.class);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            kdbDTO.setUpdateDate(formatter.parse(formatter.format(new Date())));

            msgInfo.remove("Code");
            msgInfo.remove("Result");
            msgInfo.remove("ResultValue");

            kdbDTO.setContent(String.valueOf(msgInfo));
            kdbDTO.setDeleted(KxConstants.NO);

            if (null != kdbDTO.getId()) {
                this.update(kdbDTO);
            } else {
                this.save(kdbDTO);
            }
        } catch (Exception e) {
            log.error("savePresetPic", e);
            e.printStackTrace();
        }
    }

    @Override
    public void saveFirst(JSONObject json) {
        KxDiscernBoundaryDTO dto = new KxDiscernBoundaryDTO();


        String deviceSn = json.getStr("deviceSn");
        String cameraName = json.getStr("cameraName");
        String presetId = json.getStr("PresetId");
        String deviceId = json.getStr("deviceId");

        JSONObject params = new JSONObject();
        params.putOpt("deviceSn", deviceSn);
        params.putOpt("cameraName", cameraName);
        params.putOpt("PresetId", presetId);
        params.putOpt("deviceId", deviceId);
        List<KxDiscernBoundaryEntity> list = this.getKxDiscernBoundaryDTO(params);
        if (null != list && list.size() > 0) {
            KxDiscernBoundaryEntity entity = list.get(0);
            dto = ConvertUtils.sourceToTarget(entity, KxDiscernBoundaryDTO.class);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dto.setUpdateDate(formatter.parse(formatter.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dto.setDeviceNo(deviceSn);
        dto.setDeviceId(json.getLong("deviceId"));
        dto.setStationId(json.getLong("stationId"));
        dto.setCameraName(cameraName);
        dto.setPictureHeight(json.getInt("Height"));
        dto.setPictureWidth(json.getInt("Width"));
        dto.setPresetNo(presetId);
        dto.setPresetName(json.getStr("PresetName"));
        dto.setSessionTime(json.getStr("currentTime"));
        dto.setContent(null);//清空
        dto.setStatus("");
        if (null != dto.getId()) {
            this.update(dto);
        } else {
            this.save(dto);
        }
    }

    @Override
    public List<KxDiscernBoundaryEntity> getKxDiscernBoundaryDTO(JSONObject json) {
        QueryWrapper<KxDiscernBoundaryEntity> wrapper = new QueryWrapper<>();
        String deviceSn = json.getStr("deviceSn");
        String cameraName = json.getStr("cameraName");
        String presetId = json.getStr("PresetId");
        String sessionTime = json.getStr("sessionTime");
        String deviceID = json.getStr("deviceID");
        String status = json.getStr("status");

        if (StringUtils.isNotBlank(deviceID)) {
            wrapper.eq("device_id", deviceID);
        }
        if (StringUtils.isNotBlank(deviceSn)) {
            wrapper.eq("device_no", deviceSn);
        }
        if (StringUtils.isNotBlank(cameraName)) {
            wrapper.eq("camera_name", cameraName);
        }
        if (StringUtils.isNotBlank(presetId)) {
            wrapper.eq("preset_no", presetId);
        }
        if (StringUtils.isNotBlank(sessionTime)) {
            wrapper.eq("session_time", sessionTime);
        }
        if (StringUtils.isNotBlank(status)) {
            if(KxAiBoundary.BOUNDARY_STATUS_MARK.equals(status) || KxAiBoundary.BOUNDARY_STATUS_SEND.equals(status)){
                wrapper.eq("status", status);
            }else {
                wrapper.and(queryWrapper -> queryWrapper.eq("status", KxAiBoundary.BOUNDARY_STATUS_MARK)
                        .or().eq("status", KxAiBoundary.BOUNDARY_STATUS_SEND));
            }
        }
//        if (StringUtils.isEmpty(status)) {
//            wrapper.isNull(true,"status");
//        }
        wrapper.eq("deleted", KxConstants.NO);
        List<KxDiscernBoundaryEntity> list = baseDao.selectList(wrapper);
        return list;
    }

    @Override
    public List<KxDiscernBoundaryDTO> getBydeviceId(Long deviceId) {
        JSONObject params = new JSONObject();
        params.putOpt("deviceID", deviceId);
        params.putOpt("status",  "sendAndMark"); // 有标记的
        List<KxDiscernBoundaryEntity> list = this.getKxDiscernBoundaryDTO(params);
        return ConvertUtils.sourceToTarget(list, KxDiscernBoundaryDTO.class);
    }

    /**
     *  更新标记框状态 msgInfo:{"Code":6145,"Result":"Ok"} or {"ErrorMsg":"Param Error","Result":"Failed"}
     * @param deviceSn
     * @param msgInfo
     * @param session
     */
    @Override
    public void updatePresetPicInfo(String deviceSn, JSONObject msgInfo, String session) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("保存预置位图片，未找到对应设备数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            JSONObject params = new JSONObject();
            params.putOpt("deviceSn", deviceSn);
            params.putOpt("sessionTime", session);
            List<KxDiscernBoundaryEntity> list = this.getKxDiscernBoundaryDTO(params);
            if (null != list && list.size() > 0) {
                KxDiscernBoundaryEntity entity = list.get(0);
                KxDiscernBoundaryDTO kdbDTO = ConvertUtils.sourceToTarget(entity, KxDiscernBoundaryDTO.class);
                kdbDTO.setStatus(KxAiBoundary.BOUNDARY_STATUS_SEND);
                this.update(kdbDTO);
            }
        } catch (Exception e) {
            log.error("updatePresetPicInfo", e);
            e.printStackTrace();
        }

    }

}