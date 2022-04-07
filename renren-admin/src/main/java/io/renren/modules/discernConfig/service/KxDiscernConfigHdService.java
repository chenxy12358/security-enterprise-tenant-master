package io.renren.modules.discernConfig.service;

import io.renren.common.service.CrudService;
import io.renren.modules.discernConfig.dto.KxDiscernConfigHdDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigHdEntity;

import java.util.Date;

/**
 * 识别参数配置
 *
 * @author cxy 
 * @since 3.0 2022-03-25
 */
public interface KxDiscernConfigHdService extends CrudService<KxDiscernConfigHdEntity, KxDiscernConfigHdDTO> {


    KxDiscernConfigHdDTO getBydeviceId(Long deviceId);

    /**
     * 图片分析处理
     * @param imgFilePath 图片地址
     * @param outImgFilePath 分析后图片保存地址
     * @param deviceID 设备编号
     * @param picDate 拍照时间
     */
    void analysisImg(String imgFilePath,String outImgFilePath,Long deviceID, Date picDate);


    @Override
    void update(KxDiscernConfigHdDTO dto);

    @Override
    void save(KxDiscernConfigHdDTO dto);

}