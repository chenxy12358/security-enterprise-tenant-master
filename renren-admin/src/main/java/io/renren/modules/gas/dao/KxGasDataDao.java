package io.renren.modules.gas.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.gas.dto.KxGasDataDTO;
import io.renren.modules.gas.entity.KxGasDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 燃气信息 
*
* @author WEI 
* @since 3.0 2022-02-18
*/
@Mapper
public interface KxGasDataDao extends BaseDao<KxGasDataEntity> {
    List<KxGasDataDTO> getList(Map<String, Object> params);
}