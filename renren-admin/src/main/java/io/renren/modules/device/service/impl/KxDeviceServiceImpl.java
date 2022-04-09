package io.renren.modules.device.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.common.utils.Result;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.common.utils.HttpClientUtil;
import io.renren.modules.device.dao.KxDeviceDao;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.entity.KxDeviceEntity;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.discernConfig.service.KxDiscernConfigHdService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备信息
 *
 * @author cxy
 * @since 3.0 2022-02-16
 */
@Service
@EnableAsync
public class KxDeviceServiceImpl extends CrudServiceImpl<KxDeviceDao, KxDeviceEntity, KxDeviceDTO> implements KxDeviceService {

    @Autowired
    private KxDiscernConfigHdService kxDiscernConfigHdService;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public QueryWrapper<KxDeviceEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        deleteById(id);
    }

    @Override
    public List<KxDeviceDTO> getListByStationId(Long sid) {

        List<KxDeviceEntity> deviceList = baseDao.getListByStationId(sid);

        return ConvertUtils.sourceToTarget(deviceList, KxDeviceDTO.class);
    }

    @Override
    public KxDeviceDTO get(Long id) {
        KxDeviceEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, KxDeviceDTO.class);
    }


    /**
     * 接收服务器 通过设备序号获取对应设备
     *
     * @param serialNo
     * @return
     */
    @Override
    public KxDeviceDTO getBySerialNo(String serialNo) {
        KxDeviceEntity entity = baseDao.getBySerialNo(serialNo);
        return ConvertUtils.sourceToTarget(entity, KxDeviceDTO.class);
    }

    @Override
    public Result add(KxDeviceDTO vo) {
        String msg = this.checkData(vo);
        if (StringUtils.isNotBlank(msg)) {
            return new Result().error(msg);
        }
        this.save(vo);
        return new Result();
    }

    @Override
    public Result modify(KxDeviceDTO vo) {
        String msg = this.checkData(vo);
        if (StringUtils.isNotBlank(msg)) {
            return new Result().error(msg);
        }
        this.update(vo);
        return new Result();
    }

    @Override
    @Async
    public void analysisImg(JSONObject json,Long deviceId) {
        try {
            Date picDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (json.get("DateTime") != null) {
                picDate = formatter.parse(json.get("DateTime").toString());
            }
            // url /job-data/Data-Jpeg/KX-V22P40-AI00010/2022-31/2022-04-01/2022-04-01_10-51-42.jpeg
            String url=json.get("Uri").toString();
            String imgFilePath = KxConstants.IMG_UPLOAD+url;

            String filePath="";
            if(imgFilePath.contains("\\")){
                imgFilePath=imgFilePath.replaceAll("\\\\","/");
            }
            filePath=imgFilePath.substring(0,imgFilePath.lastIndexOf("/")+1);
            String outImgFilePath = filePath.replace(KxConstants.IMG_JOB,KxConstants.IMG_ALARM);

            // 后台分析图片 本地
            logger.debug("后台分析图片开始");
            kxDiscernConfigHdService.analysisImg(imgFilePath,outImgFilePath,deviceId,picDate);
            logger.debug("后台分析图片结束");

//            String interfacePath = KxConstants.IMG_SERVER_URL+ "discernConfig/kxdiscernconfighd/analysisImg";
//            com.alibaba.fastjson.JSONObject parameters = new com.alibaba.fastjson.JSONObject();
//            parameters.put("imgFilePath", imgFilePath);
//            parameters.put("outImgFilePath", outImgFilePath);
//            parameters.put("deviceId", deviceId);
//            parameters.put("picDate", picDate);
//            String str = HttpClientUtil.postMethod(interfacePath, parameters);
//            System.err.println(str);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error"+e);
            logger.error("图片分析出错"+e);
        }
    }

    /**
     * 效验数据
     *
     * @param vo
     * @return
     */
    private String checkData(KxDeviceDTO vo) {
        String msg = "";
        if (this.isExist(vo)) {
            msg = vo.getStationName() + "站点已存在相同的设备名称";
        }
        if (this.isExistNo(vo)) {
            msg = "已存在相同的设备编号";
        }
        return msg;

    }

    /**
     * @param vo
     * @return
     */
    private boolean isExist(KxDeviceDTO vo) {
        boolean flag = false;
        QueryWrapper<KxDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("name", vo.getName());
        wrapper.eq("station_id", vo.getStationId());
        if (null != vo.getId()) {
            wrapper.ne("id", vo.getId());
        }
        List<KxDeviceEntity> list = baseDao.selectList(wrapper);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;

    }

    /**
     * @param vo
     * @return
     */
    private boolean isExistNo(KxDeviceDTO vo) {
        boolean flag = false;
        QueryWrapper<KxDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("serial_no", vo.getSerialNo());
        if (null != vo.getId()) {
            wrapper.ne("id", vo.getId());
        }
        List<KxDeviceEntity> list = baseDao.selectList(wrapper);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;

    }

}