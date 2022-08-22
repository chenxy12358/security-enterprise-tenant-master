package io.renren.modules.AppGoods.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.AppGoods.dto.AppGoodsDTO;
import io.renren.modules.AppGoods.entity.AppGoodsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 商品管理
*
* @author WEI 
* @since 3.0 2022-08-13
*/
@Mapper
public interface AppGoodsDao extends BaseDao<AppGoodsEntity> {
    List<AppGoodsDTO> getGoodsList();
}