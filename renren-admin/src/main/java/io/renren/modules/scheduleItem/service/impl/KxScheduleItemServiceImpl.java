package io.renren.modules.scheduleItem.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.scheduleItem.dao.KxScheduleItemDao;
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.entity.KxScheduleItemEntity;
import io.renren.modules.scheduleItem.service.KxScheduleItemService;
import io.renren.modules.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 计划任务拍照结果
 *
 * @author cxy 
 * @since 3.0 2022-03-05
 */
@Slf4j
@Service
public class KxScheduleItemServiceImpl extends CrudServiceImpl<KxScheduleItemDao, KxScheduleItemEntity, KxScheduleItemDTO> implements KxScheduleItemService {

    @Autowired
    private KxDeviceService kxDeviceService;
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

    @Override
    public void savePic(String deviceSn, cn.hutool.json.JSONObject senderInfo, cn.hutool.json.JSONObject msgInfo) {

        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("手动抓图，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            KxScheduleItemDTO itemDTO = new KxScheduleItemDTO();
            itemDTO.setDeviceNo(deviceSn);
            itemDTO.setSignalType("ManualPicCapture");
            itemDTO.setDeviceId(deviceDTO.getId());
            itemDTO.setItemType("ManualPicCapture");

            Object obj = msgInfo.get("ResultValue");
            if (obj != null) {
                cn.hutool.json.JSONObject resultValue = JSONUtil.parseObj(obj);
                cn.hutool.json.JSONObject taskInfo =resultValue.getJSONObject("TaskInfo");
                if(null !=taskInfo){
                    msgInfo.putOpt("TaskInfo", taskInfo);
                }
            }

            msgInfo.remove("Code");
            msgInfo.remove("Result");
            msgInfo.remove("ResultValue");

            itemDTO.setContent(String.valueOf(msgInfo));
            itemDTO.setDeleted(KxConstants.NO);
            this.save(itemDTO);
            Object files = msgInfo.get("Files");
            if (files != null) {
                cn.hutool.json.JSONArray jsonArray = JSONUtil.parseArray(files.toString());
                cn.hutool.json.JSONObject json = jsonArray.getJSONObject(0);
                kxDeviceService.analysisImg(json, deviceDTO.getId(),msgInfo);
            }
        } catch (Exception e) {
            log.error("savePic", e);
            e.printStackTrace();
        }


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