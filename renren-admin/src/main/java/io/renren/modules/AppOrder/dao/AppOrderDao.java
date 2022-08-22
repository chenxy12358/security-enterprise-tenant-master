package io.renren.modules.AppOrder.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.AppOrder.dto.AppOrderDTO;
import io.renren.modules.AppOrder.entity.AppOrderEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 兑换记录
*
* @author WEI 
* @since 3.0 2022-08-17
*/
@Mapper
public interface AppOrderDao extends BaseDao<AppOrderEntity> {

    List<AppOrderDTO> getOrderList(Long id);
}