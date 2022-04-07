package io.renren.modules.deviceAlarm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.deviceAlarm.dao.KxDeviceAlarmDao;
import io.renren.modules.deviceAlarm.dto.KxDeviceAlarmDTO;
import io.renren.modules.deviceAlarm.entity.KxDeviceAlarmEntity;
import io.renren.modules.deviceAlarm.service.KxDeviceAlarmService;
import io.renren.modules.security.user.SecurityUser;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 可视化告警
 *
 * @author cxy
 * @since 3.0 2022-02-25
 */
@Service
public class KxDeviceAlarmServiceImpl extends CrudServiceImpl<KxDeviceAlarmDao, KxDeviceAlarmEntity, KxDeviceAlarmDTO> implements KxDeviceAlarmService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public QueryWrapper<KxDeviceAlarmEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxDeviceAlarmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public void saveAnalysisImg(List listInfo,Long deviceID, Date picData) {
        String imgPath=listInfo.get(0).toString();
        String fileName="";
        String filePath="";
        if(imgPath.contains("\\")){
            imgPath=imgPath.replaceAll("\\\\","/");
        }
        fileName=imgPath.substring(imgPath.lastIndexOf("/")+1);
        filePath=imgPath.substring(0,imgPath.lastIndexOf("/")+1);

        String filename160=filePath+fileName.replace(".","-160.");
        String filename320=filePath+fileName.replace(".","-320.");
        String filename640=filePath+fileName.replace(".","-640.");
        List<Map<String, String>> listFile=new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map map =new HashMap();
        map.put("Uri",imgPath);
        map.put("name",imgPath);
        map.put("Uri160",filename160);
        map.put("Uri320",filename320);
        map.put("Uri640",filename640);
        map.put("DateTime",formatter.format(picData));
        listFile.add(map);
        try {
            Thumbnails.of(new File(imgPath)).size(160, 120).toFile(new File(filename160));
            Thumbnails.of(new File(imgPath)).size(320, 240).toFile(new File(filename320));
            Thumbnails.of(new File(imgPath)).size(640, 480).toFile(new File(filename640));
        } catch (IOException e) {
            logger.error("压缩图片失败",e);
            e.printStackTrace();

        }
        List BrokenObject= (List) listInfo.get(1);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("Type","SensorTriggerMove");
        jsonMap.put("Files", listFile);
        jsonMap.put("Level","1");
        jsonMap.put("DataType","FileList");
        jsonMap.put("BrokenObject", BrokenObject);
        KxDeviceAlarmDTO alarmDTO = new KxDeviceAlarmDTO();
        alarmDTO.setContent(String.valueOf(jsonMap));
        alarmDTO.setDeviceId(deviceID);
        alarmDTO.setHandleType("0");
        alarmDTO.setRemark("后台分析数据");
        alarmDTO.setPictureDate(picData);
        this.save(alarmDTO);

    }

}