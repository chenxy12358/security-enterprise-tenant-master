package io.renren.modules.stationTrack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.Result;
import io.renren.common.utils.StringUtil;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.deviceData.dto.KxDeviceGpsDTO;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.stationTrack.dao.KxStationTrackDao;
import io.renren.modules.stationTrack.dto.KxStationTrackDTO;
import io.renren.modules.stationTrack.entity.KxStationTrackEntity;
import io.renren.modules.stationTrack.service.KxStationTrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public QueryWrapper<KxStationTrackEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxStationTrackEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public List<KxStationTrackEntity> getTrackListInfo(Long stationId) {
        List<KxStationTrackEntity> list = baseDao.getTrackListInfoByStationId(stationId);
        return list;
    }

    @Override
    public Result add(KxDeviceGpsDTO dto, KxDeviceDTO kxDeviceDTO) {
        try {
            String latitude = dto.getLatitude();//纬度
            String longitude = dto.getLongitude();//经度
            String latNew = String.format("%.4f",Double.parseDouble(latitude));
            String lngNew = String.format("%.4f", Double.parseDouble(longitude));
            List<KxStationTrackEntity> list = this.getTrackListInfo(kxDeviceDTO.getStationId());
            if (StringUtil.isNotEmpty(latitude) && StringUtil.isNotEmpty(longitude)) {
                if (null != list && list.size() > 0) {
                    KxStationTrackEntity kxStationTrack = list.get(0);
                    String latOld = String.format("%.4f",Double.parseDouble(kxStationTrack.getLat()));
                    String lngOld = String.format("%.4f", Double.parseDouble(kxStationTrack.getLng()));
                    BigDecimal latB=new BigDecimal(latNew).subtract(new BigDecimal(latOld)).abs();
                    BigDecimal lngB=new BigDecimal(lngNew).subtract(new BigDecimal(lngOld)).abs();
                    if (lngB.compareTo(new BigDecimal("0.0001")) == 1 || latB.compareTo(new BigDecimal("0.0001")) == 1 ) {
                        //  保存轨迹坐标点
                        this.saveInfo(kxDeviceDTO,latitude,longitude);
                    }
                }else {
                    //桩点第一次的经纬度保存
                    this.saveInfo(kxDeviceDTO,latitude,longitude);
                }

            }
            return new Result();

        }catch (Exception e){
            logger.error("保存桩点路径失败",e);
            logger.error("保存桩点路径失败,content=",dto.getContent()+";DeviceId="+dto.getDeviceId());
            return new Result().error("保存路径失败");
        }
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
        kxStationTrack.setPosition(longitude+","+latitude);
        this.save(kxStationTrack);

    }
}