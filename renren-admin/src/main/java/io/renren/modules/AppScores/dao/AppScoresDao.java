package io.renren.modules.AppScores.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.AppScores.dto.AppScoresDTO;
import io.renren.modules.AppScores.dto.ScoresDTO;
import io.renren.modules.AppScores.entity.AppScoresEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 积分记录
*
* @author WEI 
* @since 3.0 2022-08-13
*/
@Mapper
public interface AppScoresDao extends BaseDao<AppScoresEntity> {
    ScoresDTO getScores(Long id);
    List<AppScoresDTO> getScoresList (Long id);

    ScoresDTO getMaxScores(Long id);
}