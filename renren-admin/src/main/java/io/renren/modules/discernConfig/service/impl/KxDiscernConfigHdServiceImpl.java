package io.renren.modules.discernConfig.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.StringUtil;
import io.renren.modules.common.constant.KxAiBoundary;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.common.utils.Gpu.HandleImgHkCpu;
import io.renren.modules.deviceAlarm.service.KxDeviceAlarmService;
import io.renren.modules.discernBoundary.entity.KxDiscernBoundaryEntity;
import io.renren.modules.discernBoundary.service.KxDiscernBoundaryService;
import io.renren.modules.discernConfig.dao.KxDiscernConfigHdDao;
import io.renren.modules.discernConfig.dto.KxAIPzVO;
import io.renren.modules.discernConfig.dto.KxDiscernConfigHdDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigHdEntity;
import io.renren.modules.discernConfig.service.KxDiscernConfigHdService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.sys.entity.SysDictDataEntity;
import io.renren.modules.sys.service.SysDictDataService;
import org.opencv.core.Rect2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 识别参数配置
 *
 * @author cxy
 * @since 3.0 2022-03-25
 */
@Service
public class KxDiscernConfigHdServiceImpl extends CrudServiceImpl<KxDiscernConfigHdDao, KxDiscernConfigHdEntity, KxDiscernConfigHdDTO> implements KxDiscernConfigHdService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysDictDataService sysDictDataService;
    @Autowired
    private KxDiscernBoundaryService kxDiscernBoundaryService;
    @Autowired
    private KxDeviceAlarmService kxDeviceAlarmService;

    @Override
    public QueryWrapper<KxDiscernConfigHdEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxDiscernConfigHdEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public void save(KxDiscernConfigHdDTO dto) {
        dto.setDeleted("f");
        dto = this.setSbConfig(dto);
        super.save(dto);
    }

    @Override
    public void update(KxDiscernConfigHdDTO dto) {
        dto = this.setSbConfig(dto);
        super.update(dto);
    }

    /**
     * 设置相似度配置json信息
     *
     * @param dto
     * @return
     */
    private KxDiscernConfigHdDTO setSbConfig(KxDiscernConfigHdDTO dto) {
        List<KxAIPzVO> list = dto.getDiscernList(); // 识别配置
        JSONObject json = new JSONObject();
        for (KxAIPzVO kxAIPzVO : list) {
            JSONObject jsonMap = new JSONObject();
            jsonMap.put("Enable", kxAIPzVO.isEnable());
            jsonMap.put("Thresh", kxAIPzVO.getThresh());
            jsonMap.put("Name", kxAIPzVO.getName());
            jsonMap.put("Sort", kxAIPzVO.getSort());
            json.put(kxAIPzVO.getCode(), jsonMap);
        }
        dto.setDistinguishConfig(json.toString());
        return dto;
    }

    @Override
    public KxDiscernConfigHdDTO getBydeviceId(Long deviceId) {
        KxDiscernConfigHdDTO dto = baseDao.getBydeviceId(deviceId);
        dto = this.getCameraAndSbConfig(dto);
        return dto;
    }


    @Override
    @Async
    public void analysisImg(cn.hutool.json.JSONObject json, Long deviceID, cn.hutool.json.JSONObject msgInfo) {
        try {

            Date picDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (json.get("DateTime") != null) {
                picDate = formatter.parse(json.get("DateTime").toString());
            }
            logger.info("后台AI识别开始，设备id[{}]", deviceID);
            // url /job-data/Data-Jpeg/KX-V22P40-AI00010/2022-31/2022-04-01/2022-04-01_10-51-42.jpeg
            String url = json.get("Uri").toString();
            String imgFilePath = KxConstants.IMG_UPLOAD + url;

            String filePath = "";
            if (imgFilePath.contains("\\")) {
                imgFilePath = imgFilePath.replaceAll("\\\\", "/");
            }
            filePath = imgFilePath.substring(0, imgFilePath.lastIndexOf("/") + 1);
            String outImgFilePath = filePath.replace(KxConstants.IMG_JOB, KxConstants.IMG_ALARM);


            // 后台分析图片 本地
            Object taskInfo = msgInfo.get("TaskInfo");
            String camera = "";
            String presetId = "";
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(taskInfo);
            if (jsonObject.get("Camera") != null) {
                camera = jsonObject.getStr("Camera");

            }
            if (jsonObject.get("PresetId") != null) {
                presetId = jsonObject.getStr("PresetId");
            }


            KxDiscernConfigHdDTO kxDiscernConfigHdDTO = getBydeviceId(deviceID);
            // 有配置信息才处理
            if (null != kxDiscernConfigHdDTO && KxConstants.YSC.equals(kxDiscernConfigHdDTO.getEnable())) {
                //查询ai范围标记框信息
                cn.hutool.json.JSONObject params = new cn.hutool.json.JSONObject();
                params.putOpt("cameraName", camera);
                params.putOpt("PresetId", presetId);
                params.putOpt("deviceID", deviceID);
                params.putOpt("status", KxAiBoundary.BOUNDARY_STATUS_SEND); // 已发送
                cn.hutool.json.JSONObject jsonBoubdary=new cn.hutool.json.JSONObject();
                if (StringUtil.isNotEmpty(camera) && StringUtil.isNotEmpty(presetId) && null != deviceID) {
                    List<KxDiscernBoundaryEntity> boundaryList = kxDiscernBoundaryService.getKxDiscernBoundaryDTO(params);
                    if (null != boundaryList && boundaryList.size() > 0) {
                        KxDiscernBoundaryEntity entity = boundaryList.get(0);
                        cn.hutool.json.JSONObject disAIMarkConfigJson = JSONUtil.parseObj(entity.getContent());
                        JSONArray array = disAIMarkConfigJson.getJSONArray("BoundaryCoordinates");
                        if (null != array) {
                            List<Rect2d> rectList=new ArrayList<Rect2d>();
                            for (int i = 0; i < array.size(); i++) {
                                cn.hutool.json.JSONObject parseObj = JSONUtil.parseObj(array.get(i));
                                cn.hutool.json.JSONObject leftXy=parseObj.getJSONObject("LeftTop");
                                if(null != leftXy){
                                    int leftX=leftXy.getInt("x");
                                    int leftY=leftXy.getInt("y");
                                    int width =parseObj.getInt("rectW");
                                    int height=parseObj.getInt("rectH");
                                    String type=parseObj.getStr("Type");
                                    Rect2d rect = new Rect2d(leftX, leftY, width, height);
                                    rectList.add(rect);
                                    jsonBoubdary.putOpt("type",type);
                                    jsonBoubdary.putOpt("rectList",rectList);
                                }
                            }
                        }
                    }
                } else {
                    logger.info("查询ai范围标记框信息参数错误，设备id[{}]，或者预置位[{}]，相机[{}]",
                            deviceID,
                            presetId,
                            camera);
                }
                //查询ai范围标记框信息end

                String pictureSize = kxDiscernConfigHdDTO.getPicSize();
                String[] picArr = pictureSize.split("x");
                String pictureWidth = picArr[0];
                String pictureHeight = picArr[1];
                int picWidth = Integer.parseInt(pictureWidth);
                int picHeight = Integer.parseInt(pictureHeight);
                String planFilePath = kxDiscernConfigHdDTO.getSchemeNo();
                //配置信息
                List<KxAIPzVO> list = kxDiscernConfigHdDTO.getDiscernList();
                try {
                    logger.info("图片：{" + imgFilePath + "} ，分析开始");
                    List listInfo = HandleImgHkCpu.analysisImgByCPU(imgFilePath, planFilePath, outImgFilePath, picWidth, picHeight, list,
                            false, jsonBoubdary);
                    logger.info("图片：{" + imgFilePath + "} ，分析结束");
                    logger.info("listInfo：" + listInfo.size());
                    if (!listInfo.get(0).toString().isEmpty()) {
                        logger.info("listInfo：{" + listInfo.get(0).toString());
                        kxDeviceAlarmService.saveAnalysisImg(listInfo, deviceID, picDate);

                        logger.info("保存完成");
                    } else {
                        logger.info("图片：{" + imgFilePath + "} ，未识别到配置对象");
                    }
                } catch (Exception e) {
                    System.err.println("分析图片执行失败，设备ID：{" + deviceID + "} ，图片路径：{" + imgFilePath + "}");
                    logger.error("分析图片执行失败，设备ID：{" + deviceID + "} ，图片路径：{" + imgFilePath + "}", e);
                    e.printStackTrace();
                }
            } else {
                System.err.println("设备ID：{" + deviceID + "} ，未找到配置信息");
                logger.info("设备ID：{" + deviceID + "} ，未找到配置信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error" + e);
            logger.error("图片分析出错" + e);
        }
    }

    /**
     * 获取 相机、相似度配置详情
     *
     * @param dto
     * @return
     */
    private KxDiscernConfigHdDTO getCameraAndSbConfig(KxDiscernConfigHdDTO dto) {
        List<KxAIPzVO> discernList = new ArrayList();
        List<SysDictDataEntity> list = sysDictDataService.getListByDictName(KxConstants.KX_DISCERN_TYPE);
        if (null == dto) {
            dto = this.getFirstSbConfig(discernList, list); // 未保存时
        } else {
            dto = this.getDetailSbConfig(dto, discernList, list); //已保存时
        }

        return dto;

    }


    /**
     * 未保存时 第一次查询  码表值
     *
     * @param discernList
     * @return
     */
    private KxDiscernConfigHdDTO getFirstSbConfig(List<KxAIPzVO> discernList, List<SysDictDataEntity> list) {

        // 说明没有保存过，查询处所有的类型 相似度参数设置开始
        KxDiscernConfigHdDTO dto = new KxDiscernConfigHdDTO();
        for (SysDictDataEntity sysDictDataEntity : list) {
            KxAIPzVO kxAIPzVO = new KxAIPzVO();
            kxAIPzVO.setCode(sysDictDataEntity.getDictValue());
            kxAIPzVO.setName(sysDictDataEntity.getDictLabel());
            kxAIPzVO.setSort(sysDictDataEntity.getSort());
            discernList.add(kxAIPzVO);
        }
        dto.setDiscernList(discernList);
        dto.setSchemeNo("");
        dto.setPicSize("");
        return dto;
    }

    /**
     * 已保存查询详情
     *
     * @param dto
     * @param discernList
     * @return1
     */
    private KxDiscernConfigHdDTO getDetailSbConfig(KxDiscernConfigHdDTO dto, List<KxAIPzVO> discernList, List<SysDictDataEntity> list) {
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
                        kxAIPzVO.setThresh(json.get("Thresh") == null ? BigDecimal.ZERO : new BigDecimal(json.get("Thresh").toString()));
                        kxAIPzVO.setName(sysDictDataEntity.getDictLabel() == null ? "" : sysDictDataEntity.getDictLabel());
                        kxAIPzVO.setSort(sysDictDataEntity.getSort());
                        flag = false;
                    }
                }
            }
            if (flag) {
                kxAIPzVO.setCode(sysDictDataEntity.getDictValue());
                kxAIPzVO.setName(sysDictDataEntity.getDictLabel());
                kxAIPzVO.setSort(sysDictDataEntity.getSort());

            }
            discernList.add(kxAIPzVO);
        }
        dto.setDiscernList(discernList);
        // 相似度参数设置结束
        return dto;
    }


}