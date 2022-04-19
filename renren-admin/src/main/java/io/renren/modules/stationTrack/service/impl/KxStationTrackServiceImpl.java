package io.renren.modules.stationTrack.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.Result;
import io.renren.common.utils.StringUtil;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.stationTrack.dao.KxStationTrackDao;
import io.renren.modules.stationTrack.dto.KxStationTrackDTO;
import io.renren.modules.stationTrack.entity.KxStationTrackEntity;
import io.renren.modules.stationTrack.service.KxStationTrackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 桩点位移轨迹
 *
 * @author cxy
 * @since 3.0 2022-04-19
 */
@Service
public class KxStationTrackServiceImpl extends CrudServiceImpl<KxStationTrackDao, KxStationTrackEntity, KxStationTrackDTO> implements KxStationTrackService {

    @Override
    public QueryWrapper<KxStationTrackEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxStationTrackEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public List<KxStationTrackDTO> getTrackListInfo(Long stationId) {
        List<KxStationTrackDTO> list = baseDao.getTrackListInfoByStationId(stationId);
        return list;
    }

    @Override
    public Result add(JSONObject msgInfo, KxDeviceDTO kxDeviceDTO) {
        String latitude = msgInfo.get("Latitude").toString();//纬度
        String longitude = msgInfo.get("Longitude").toString();//经度
        List<KxStationTrackDTO> list = this.getTrackListInfo(kxDeviceDTO.getStationId());
        if (StringUtil.isNotEmpty(latitude) && StringUtil.isNotEmpty(longitude)) {
            if (null != list && list.size() > 0) {
                KxStationTrackDTO kxStationTrackDTO = list.get(0);
                if (!latitude.equals(kxStationTrackDTO.getLat()) || !longitude.equals(kxStationTrackDTO.getLng())) {
                    //  保存轨迹坐标点
                    this.saveInfo(kxDeviceDTO,latitude,longitude);
                }
            }else {
                //桩点第一次的经纬度保存
                this.saveInfo(kxDeviceDTO,latitude,longitude);
            }

        }
        return new Result();
    }

    /**
     * 保存轨迹坐标点
     * @param kxDeviceDTO
     * @param latitude
     * @param longitude
     */
    private void saveInfo(KxDeviceDTO kxDeviceDTO, String latitude, String longitude) {
        KxStationTrackDTO kxStationTrack = new KxStationTrackDTO();
        kxStationTrack.setStationId(kxDeviceDTO.getStationId());
        kxStationTrack.setRemark(kxDeviceDTO.getId().toString());// 保存设备id
        kxStationTrack.setLat(latitude);
        kxStationTrack.setLng(longitude);
        this.save(kxStationTrack);

    }
}