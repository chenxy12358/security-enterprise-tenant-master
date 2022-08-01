package io.renren.modules.discernConfig.service;

import cn.hutool.json.JSONObject;
import io.renren.common.service.CrudService;
import io.renren.modules.discernConfig.dto.KxDiscernConfigHdDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigHdEntity;

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
     *
     */
    void analysisImg(JSONObject json,Long deviceId, JSONObject msgInfo);


    @Override
    void update(KxDiscernConfigHdDTO dto);

    @Override
    void save(KxDiscernConfigHdDTO dto);


}