package io.renren.modules.discernConfig.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.discernConfig.dao.KxDiscernConfigDao;
import io.renren.modules.discernConfig.dto.KxAICameraVO;
import io.renren.modules.discernConfig.dto.KxAIPzVO;
import io.renren.modules.discernConfig.dto.KxCameraNameVO;
import io.renren.modules.discernConfig.dto.KxDiscernConfigDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigEntity;
import io.renren.modules.discernConfig.service.KxDiscernConfigService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.sys.entity.SysDictDataEntity;
import io.renren.modules.sys.service.SysDictDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 识别参数配置
 *
 * @author cxy
 * @since 3.0 2022-03-08
 */
@Service
public class KxDiscernConfigServiceImpl extends CrudServiceImpl<KxDiscernConfigDao, KxDiscernConfigEntity, KxDiscernConfigDTO> implements KxDiscernConfigService {

    @Autowired
    private SysDictDataService sysDictDataService;

    @Autowired
    private KxDeviceService kxDeviceService;

    @Override
    public QueryWrapper<KxDiscernConfigEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxDiscernConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }

    @Override
    public KxDiscernConfigDTO getBydeviceId(Long deviceId) {
        KxDiscernConfigDTO dto = baseDao.getBydeviceId(deviceId);
        dto = this.getCameraAndSbConfig(dto, deviceId);
        return dto;
    }

    @Override
    public void save(KxDiscernConfigDTO dto) {
        dto.setDeleted("f");
        dto = this.setSbConfig(dto);
        dto = this.setCameraConfig(dto);
        super.save(dto);
    }

    @Override
    public void update(KxDiscernConfigDTO dto) {
        dto = this.setSbConfig(dto);
        dto = this.setCameraConfig(dto);
        super.update(dto);
    }


    /**
     * 设置相似度配置json信息
     *
     * @param dto
     * @return
     */
    private KxDiscernConfigDTO setSbConfig(KxDiscernConfigDTO dto) {
        List<KxAIPzVO> list = dto.getDiscernList(); // 识别配置
        JSONObject json = new JSONObject();
        for (KxAIPzVO kxAIPzVO : list) {
            JSONObject jsonMap = new JSONObject();
            jsonMap.put("Enable", kxAIPzVO.isEnable());
            jsonMap.put("Thresh", kxAIPzVO.getThresh());
            jsonMap.put("RingSwitch", kxAIPzVO.isRingSwitch());
            jsonMap.put("Name", kxAIPzVO.getName());
            jsonMap.put("RingType", kxAIPzVO.getRingType() == null ? "" : kxAIPzVO.getRingType());
            json.put(kxAIPzVO.getCode(), jsonMap);
        }
        dto.setDistinguishConfig(json.toString());
        return dto;
    }

    /**
     * 设置相机预置位、值守时间等json信息
     *
     * @param dto
     * @return
     */
    private KxDiscernConfigDTO setCameraConfig(KxDiscernConfigDTO dto) {
        List<KxCameraNameVO> list = dto.getCameraList(); // 相机配置
        JSONArray jSONArray = new JSONArray();
        for (KxCameraNameVO kxCameraNameVO : list) {
            JSONObject jsonMap = new JSONObject();
            jsonMap.put("Camera", kxCameraNameVO.getCamera());
            List<KxAICameraVO> aiCameraList = kxCameraNameVO.getListC();
            JSONArray jar = new JSONArray();
            for (KxAICameraVO kxAICameraVO : aiCameraList) {
                JSONObject json = new JSONObject();
                json.put("Enable", kxAICameraVO.isEnable());
                json.put("Duration", kxAICameraVO.getDuration());
                String preset=kxAICameraVO.getPresetNo();
                if(StrUtil.isNotBlank(preset) && preset.contains("-")){
                    String[] str=preset.split("-");
                    json.put("Id", Integer.parseInt(str[0]));
                    json.put("Name", preset);
                }
                jar.add(json);
            }
            jsonMap.put("Presets", jar);
            jSONArray.add(jsonMap);
        }
        if(jSONArray.size()>0){
            dto.setCameraConfig(jSONArray.toString());
        }

        return dto;
    }


    /**
     * 获取 相机、相似度配置详情
     *
     * @param dto
     * @return
     */
    private KxDiscernConfigDTO getCameraAndSbConfig(KxDiscernConfigDTO dto, Long deviceId) {
        List<KxAIPzVO> discernList = new ArrayList();
        List<KxCameraNameVO> cameraList = new ArrayList();
        if (null == dto) {
            dto = this.getFirstCameraAndSbConfig(discernList, cameraList, deviceId); // 未保存时
        } else {
            dto = this.getDetailCameraAndSbConfig(dto, discernList, cameraList, deviceId); //已保存时
        }

        return dto;

    }

    /**
     * 未保存时 第一次查询baseinfo 与 码表值
     *
     * @param discernList
     * @param cameraList
     * @return
     */
    private KxDiscernConfigDTO getFirstCameraAndSbConfig(List<KxAIPzVO> discernList, List<KxCameraNameVO> cameraList, Long deviceId) {

        // 说明没有保存过，查询处所有的类型 相似度参数设置开始
        KxDiscernConfigDTO dto = new KxDiscernConfigDTO();
        List<SysDictDataEntity> list = sysDictDataService.getListByDictName(KxConstants.KX_DISCERN_TYPE);
        for (SysDictDataEntity sysDictDataEntity : list) {
            KxAIPzVO kxAIPzVO = new KxAIPzVO();
            kxAIPzVO.setCode(sysDictDataEntity.getDictValue());
            kxAIPzVO.setName(sysDictDataEntity.getDictLabel());
            discernList.add(kxAIPzVO);
        }
        dto.setDiscernList(discernList);
        // 相似度参数设置结束

        //相机初始化参数开始
        KxDeviceDTO device = kxDeviceService.get(deviceId);
        if (null != device) {
            String baseInfo = (String) device.getBaseInfo();
            if (StringUtils.isNotBlank(baseInfo)) {
                JSONObject baseInfoJson = JSONObject.parseObject(baseInfo);
                JSONObject dev = JSONObject.parseObject(baseInfoJson.getString("Device"));
                JSONArray jsonArray = dev.getJSONArray("Camera");
                if (null != jsonArray && !jsonArray.isEmpty()) {
                    for (int j = 0; j < jsonArray.size(); j++) {
                        List<KxAICameraVO> AICameraList = new ArrayList();
                        KxCameraNameVO kxCameraNameVO = new KxCameraNameVO();
                        JSONObject jSONObject = jsonArray.getJSONObject(j);
                        String cameraName = jSONObject.getString("Name");
                        String cameraType = jSONObject.getString("Ability");
                        if(StrUtil.isNotBlank(cameraType)&&StrUtil.startWith(cameraType, "Q")){ //球机
                            kxCameraNameVO.setCamera(cameraName);
                            kxCameraNameVO.setListC(AICameraList);
                            cameraList.add(kxCameraNameVO);
                        }
                    }
                }
                dto.setCameraList(cameraList);
            }
        }
        //相机参数结束
        return dto;
    }

    /**
     * 已保存查询详情
     *
     * @param dto
     * @param discernList
     * @param cameraList
     * @return1
     */
    private KxDiscernConfigDTO getDetailCameraAndSbConfig(KxDiscernConfigDTO dto, List<KxAIPzVO> discernList, List<KxCameraNameVO> cameraList, Long deviceId) {
        List<SysDictDataEntity> list = sysDictDataService.getListByDictName(KxConstants.KX_DISCERN_TYPE);
        for (SysDictDataEntity sysDictDataEntity : list) {
            boolean flag = true;
            KxAIPzVO kxAIPzVO = new KxAIPzVO();
            // 相似度参数设置开始
            String cotent = (String) dto.getDistinguishConfig();
            JSONObject jsonObject = JSON.parseObject(cotent);
            if (null != jsonObject) {
                for (Map.Entry entry : jsonObject.entrySet()) {
                    if (sysDictDataEntity.getDictValue().equals(entry.getKey().toString())) {
                        kxAIPzVO.setCode(entry.getKey().toString());
                        JSONObject json = (JSONObject) entry.getValue();
                        kxAIPzVO.setEnable(json.get("Enable") == null ? false : (boolean) json.get("Enable"));
                        kxAIPzVO.setRingSwitch(json.get("RingSwitch") == null ? false : (boolean) json.get("RingSwitch"));
                        kxAIPzVO.setRingType(json.get("RingType") == null ? "" : json.get("RingType").toString());
                        kxAIPzVO.setThresh(json.get("Thresh") == null ? BigDecimal.ZERO : new BigDecimal(json.get("Thresh").toString()));
//                        kxAIPzVO.setName(json.get("Name") == null ? "" : json.get("Name").toString());
                        kxAIPzVO.setName(sysDictDataEntity.getDictLabel() == null ? "" : sysDictDataEntity.getDictLabel() );
                        flag = false;
                    }
                }
            }
            if (flag) {
                kxAIPzVO.setCode(sysDictDataEntity.getDictValue());
                kxAIPzVO.setName(sysDictDataEntity.getDictLabel());

            }
            discernList.add(kxAIPzVO);
        }
        dto.setDiscernList(discernList);
        // 相似度参数设置结束

        // 获取相机配置
        getCameraInfo(dto, cameraList, deviceId);

        return dto;
    }

    /**
     * 获取相机配置
     *
     * @param dto
     * @param cameraList
     * @return
     */
    private KxDiscernConfigDTO getCameraInfo(KxDiscernConfigDTO dto, List<KxCameraNameVO> cameraList, Long deviceId) {
        //相机参数 业务数据表
        String config = (String) dto.getCameraConfig();
        JSONArray jsonC = JSONArray.parseArray(config);
        //相机初始化参数开始
        KxDeviceDTO device = kxDeviceService.get(deviceId);
        if (null != device) {
            String baseInfo = (String) device.getBaseInfo();
            if (StringUtils.isNotBlank(baseInfo)) {
                JSONObject baseInfoJson = JSONObject.parseObject(baseInfo);
                JSONObject dev = JSONObject.parseObject(baseInfoJson.getString("Device"));
                JSONArray jsonArray = dev.getJSONArray("Camera");
                if (null != jsonArray && !jsonArray.isEmpty()) {
                    for (int j = 0; j < jsonArray.size(); j++) { //baseinfo
                        boolean flag = true;
                        List<KxAICameraVO> AICameraList = new ArrayList();
                        KxCameraNameVO kxCameraNameVO = new KxCameraNameVO();
                        JSONObject jSONObject = jsonArray.getJSONObject(j);
                        String cameraName = jSONObject.getString("Name");
                        String cameraType = jSONObject.getString("Ability");
                        if(StrUtil.startWith(cameraType, "Q")){  //球机才能配置
                            if (null != jsonC && !jsonC.isEmpty() && StrUtil.isNotBlank(cameraType)) {
                                for (int i = 0; i < jsonC.size(); i++) {
                                    JSONObject jsonO = jsonC.getJSONObject(i);
                                    String cameraNameB = jsonO.getString("Camera");
                                    if (cameraName.equals(cameraNameB)) {
                                        JSONArray jsonArrayB = jsonO.getJSONArray("Presets");
                                        List<KxAICameraVO> AICameraListB = new ArrayList();
                                        if (null != jsonArrayB && !jsonArrayB.isEmpty()) {
                                            for (int k = 0; k < jsonArrayB.size(); k++) {
                                                JSONObject jSONObjectB = jsonArrayB.getJSONObject(k);
                                                KxAICameraVO kxAICameraVO = new KxAICameraVO();
                                                kxAICameraVO.setDuration(jSONObjectB.get("Duration") == null ? 0 : Integer.parseInt(jSONObjectB.getString("Duration")));
                                                kxAICameraVO.setEnable(jSONObjectB.get("Enable") == null ? false : (boolean) jSONObjectB.get("Enable"));
//                                                kxAICameraVO.setPresetNo(jSONObjectB.get("Id") == null ? 0 : Integer.parseInt(jSONObjectB.getString("Id")));
                                                kxAICameraVO.setPresetNo(jSONObjectB.get("Name") == null ? "" : jSONObjectB.getString("Name"));
                                                AICameraListB.add(kxAICameraVO);
                                                kxCameraNameVO.setListC(AICameraListB);
                                            }
                                        } else {
                                            kxCameraNameVO.setListC(AICameraListB);
                                        }
                                        kxCameraNameVO.setCamera(cameraNameB);
                                        flag = false;
                                    }
                                }
                            }
                            if (flag) {
                                kxCameraNameVO.setCamera(cameraName);
                                kxCameraNameVO.setListC(AICameraList);
                            }
                            cameraList.add(kxCameraNameVO);
                        }
                    }
                }
                dto.setCameraList(cameraList);
            }
        }
        //相机参数结束
        return dto;
    }


}