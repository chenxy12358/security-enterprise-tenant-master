package io.renren.modules.discernBoundary.service;

import cn.hutool.json.JSONObject;
import io.renren.common.service.CrudService;
import io.renren.modules.discernBoundary.dto.KxDiscernBoundaryDTO;
import io.renren.modules.discernBoundary.entity.KxDiscernBoundaryEntity;

/**
 * 识别边界标定
 *
 * @author cxy 
 * @since 3.0 2022-07-19
 */
public interface KxDiscernBoundaryService extends CrudService<KxDiscernBoundaryEntity, KxDiscernBoundaryDTO> {
    void savePresetPic(String deviceSn, JSONObject senderInfo, JSONObject msgInfo,String session);

    void saveFirst(JSONObject jsonP);

    /**
     * 通过边界信息查询
     * @param json
     * @return
     */
    KxDiscernBoundaryEntity getKxDiscernBoundaryDTO(JSONObject json);

}