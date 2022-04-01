package io.renren.modules.netWorkData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.netWorkData.dto.KxNetWorkStateDataDTO;
import io.renren.modules.netWorkData.entity.KxNetWorkStateDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 网络状态信息 
*
* @author cxy 
* @since 3.0 2022-02-19
*/
@Mapper
public interface KxNetWorkStateDataDao extends BaseDao<KxNetWorkStateDataEntity> {

    List<KxNetWorkStateDataDTO> getList(Map<String, Object> params);
	
}