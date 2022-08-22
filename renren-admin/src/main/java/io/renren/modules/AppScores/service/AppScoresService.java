package io.renren.modules.AppScores.service;

import io.renren.common.service.CrudService;
import io.renren.common.utils.Result;
import io.renren.modules.AppScores.dto.AppScoresDTO;
import io.renren.modules.AppScores.dto.SaveSocresDTO;
import io.renren.modules.AppScores.dto.ScoresDTO;
import io.renren.modules.AppScores.entity.AppScoresEntity;

import java.util.List;

/**
 * 积分记录
 *
 * @author WEI
 * @since 3.0 2022-08-13
 */
public interface AppScoresService extends CrudService<AppScoresEntity, AppScoresDTO> {
    ScoresDTO getScores(Long id);
    Result addScoresRecord(SaveSocresDTO dto);
    List<AppScoresDTO> getScoreList(Long id);

    ScoresDTO getMaxScores(Long id);


}