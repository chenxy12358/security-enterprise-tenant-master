package io.renren.modules.discernBoundary.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.discernBoundary.entity.KxDiscernBoundaryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 识别边界标定
*
* @author cxy 
* @since 3.0 2022-07-19
*/
@Mapper
public interface KxDiscernBoundaryDao extends BaseDao<KxDiscernBoundaryEntity> {
	
}