package io.renren.modules.discernConfig.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
//    @Autowired
//    private KxDiscernBoundaryService kxDiscernBoundaryService;
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
    public void analysisImg(String imgFilePath, String outImgFilePath, Long deviceID, Date picDate, cn.hutool.json.JSONObject msgInfo) {

        Object taskInfo = msgInfo.get("TaskInfo");
        String camera="";
        String presetId="";
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(taskInfo);
        if (jsonObject.get("Camera") != null) {
            camera=msgInfo.getStr("Camera");

        }
        if (jsonObject.get("PresetId") != null) {
            presetId=msgInfo.getStr("PresetId");
        }


        KxDiscernConfigHdDTO kxDiscernConfigHdDTO =getBydeviceId(deviceID);
        if(null !=kxDiscernConfigHdDTO && KxConstants.YSC.equals(kxDiscernConfigHdDTO.getEnable())){ // 有配置信息才处理


            //查询ai范围标记框信息
            cn.hutool.json.JSONObject params = new cn.hutool.json.JSONObject();
            params.putOpt("cameraName", camera);
            params.putOpt("PresetId", presetId);
            params.putOpt("deviceId", deviceID);
            params.putOpt("status",  KxAiBoundary.BOUNDARY_STATUS_SEND); // 已发送
            KxDiscernBoundaryEntity entity=null;
            if(StringUtil.isNotEmpty(camera) && StringUtil.isNotEmpty(presetId) && null != deviceID){
//                List<KxDiscernBoundaryEntity> boundaryList = kxDiscernBoundaryService.getKxDiscernBoundaryDTO(params);
//                if (null != boundaryList && boundaryList.size() > 0) {
//                    entity = boundaryList.get(0);
//                }
            }else {
                logger.info("查询ai范围标记框信息参数错误，设备id[{}]，或者预置位[{}]，相机[{}]",
                        deviceID,
                        presetId,
                        camera);
            }

            String pictureSize=kxDiscernConfigHdDTO.getPicSize();
            String[] picArr = pictureSize.split("x");
            String pictureWidth = picArr[0];
            String pictureHeight = picArr[1];
            int picWidth=Integer.parseInt(pictureWidth);
            int picHeight=Integer.parseInt(pictureHeight);
            String planFilePath= kxDiscernConfigHdDTO.getSchemeNo();
            //配置信息
            List<KxAIPzVO> list = kxDiscernConfigHdDTO.getDiscernList();
            try {
                logger.info("图片：{"+imgFilePath+"} ，分析开始");
//                List listInfo= HandleImg.analysisImgImg(imgFilePath,planFilePath,outImgFilePath,picWidth,picHeight,list,false);
                List listInfo= HandleImgHkCpu.analysisImgByCPU(imgFilePath,planFilePath,outImgFilePath,picWidth,picHeight,list,false,entity);
                logger.info("图片：{"+imgFilePath+"} ，分析结束");
                logger.info("listInfo："+listInfo.size());
                if(!listInfo.get(0).toString().isEmpty()){
                    logger.info("listInfo：{"+listInfo.get(0).toString());
                    kxDeviceAlarmService.saveAnalysisImg(listInfo,deviceID,picDate);
                    logger.info("保存完成");
                }else {
                    logger.info("图片：{"+imgFilePath+"} ，未识别到配置对象");
                }
            } catch (Exception e) {
                System.err.println("分析图片执行失败，设备ID：{"+deviceID+"} ，图片路径：{"+imgFilePath+"}");
                logger.error("分析图片执行失败，设备ID：{"+deviceID+"} ，图片路径：{"+imgFilePath+"}", e);
                e.printStackTrace();
            }
        }else {
            System.err.println("设备ID：{"+deviceID+"} ，未找到配置信息");
            logger.info("设备ID：{"+deviceID+"} ，未找到配置信息");
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
            dto = this.getFirstSbConfig(discernList,list); // 未保存时
        } else {
            dto = this.getDetailSbConfig(dto, discernList,list); //已保存时
        }

        return dto;

    }


    /**
     * 未保存时 第一次查询  码表值
     *
     * @param discernList
     * @return
     */
    private KxDiscernConfigHdDTO getFirstSbConfig(List<KxAIPzVO> discernList,List<SysDictDataEntity> list) {

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
    private KxDiscernConfigHdDTO getDetailSbConfig(KxDiscernConfigHdDTO dto, List<KxAIPzVO> discernList,List<SysDictDataEntity> list) {
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
                        kxAIPzVO.setName(sysDictDataEntity.getDictLabel() == null ? "" : sysDictDataEntity.getDictLabel() );
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