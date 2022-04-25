package io.renren.modules.stationTrack.service;

import cn.hutool.json.JSONObject;
import io.renren.common.service.CrudService;
import io.renren.common.utils.ConvertUtils;
import io.renren.common.utils.Result;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.deviceData.dto.KxDeviceGpsDTO;
import io.renren.modules.stationTrack.dto.KxStationTrackDTO;
import io.renren.modules.stationTrack.entity.KxStationTrackEntity;

import java.util.List;

/**
 * 桩点位移轨迹
 *
 * @author cxy 
 * @since 3.0 2022-04-19
 */
public interface KxStationTrackService extends CrudService<KxStationTrackEntity, KxStationTrackDTO> {

    List<KxStationTrackEntity> getTrackListInfo(Long stationId);

    /**
     *  新增
     * @param dto
     * @param kxDeviceDTO
     * @return
     */
    Result add(KxDeviceGpsDTO dto, KxDeviceDTO kxDeviceDTO);
}