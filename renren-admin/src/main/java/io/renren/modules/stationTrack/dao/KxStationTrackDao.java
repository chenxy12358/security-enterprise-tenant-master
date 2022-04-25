package io.renren.modules.stationTrack.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.stationTrack.dto.KxStationTrackDTO;
import io.renren.modules.stationTrack.entity.KxStationTrackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 桩点位移轨迹
*
* @author cxy 
* @since 3.0 2022-04-19
*/
@Mapper
public interface KxStationTrackDao extends BaseDao<KxStationTrackEntity> {

    List<KxStationTrackEntity> getTrackListInfoByStationId(@Param("stationId") Long stationId);
}