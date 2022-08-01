package io.renren.modules.discernBoundary.service;

import cn.hutool.json.JSONObject;
import io.renren.common.service.CrudService;
import io.renren.modules.discernBoundary.dto.KxDiscernBoundaryDTO;
import io.renren.modules.discernBoundary.entity.KxDiscernBoundaryEntity;

import java.util.List;

/**
 * 识别边界标定
 *
 * @author cxy 
 * @since 3.0 2022-07-19
 */
public interface KxDiscernBoundaryService extends CrudService<KxDiscernBoundaryEntity, KxDiscernBoundaryDTO> {
    void savePresetPic(String deviceSn, JSONObject senderInfo, JSONObject msgInfo,String session);

    /**
     * 收藏第一次保存
     * @param json
     */
    void saveFirst(JSONObject json);

    /**
     * 通过边界信息查询
     * @param json
     * @return
     */
    List<KxDiscernBoundaryEntity> getKxDiscernBoundaryDTO(JSONObject json);


    /**
     * 查找已标记没发送的
     * @param deviceId
     * @return
     */
    List<KxDiscernBoundaryDTO> getBydeviceId(Long deviceId);

    void updatePresetPicInfo(String deviceSn, JSONObject msgInfo, String session);
}