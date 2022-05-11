package io.renren.modules.discernConfig.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.discernConfig.dto.KxDiscernConfigDTO;
import io.renren.modules.discernConfig.entity.KxDiscernConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 识别参数配置
*
* @author cxy 
* @since 3.0 2022-03-08
*/
@Mapper
public interface KxDiscernConfigDao extends BaseDao<KxDiscernConfigEntity> {

    KxDiscernConfigDTO getBydeviceId( Long deviceId);
}