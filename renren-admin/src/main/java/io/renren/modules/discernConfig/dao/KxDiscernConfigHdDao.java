package io.renren.modules.discernConfig.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.discernConfig.dto.KxDiscernConfigHdDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigHdEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 识别参数配置
*
* @author cxy 
* @since 3.0 2022-03-25
*/
@Mapper
public interface KxDiscernConfigHdDao extends BaseDao<KxDiscernConfigHdEntity> {
    KxDiscernConfigHdDTO getBydeviceId(Long deviceId);
}