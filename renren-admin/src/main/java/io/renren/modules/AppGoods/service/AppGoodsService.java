package io.renren.modules.AppGoods.service;

import io.renren.common.service.CrudService;
import io.renren.modules.AppGoods.dto.AppGoodsDTO;
import io.renren.modules.AppGoods.entity.AppGoodsEntity;

import java.util.List;

/**
 * 商品管理
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
public interface AppGoodsService extends CrudService<AppGoodsEntity, AppGoodsDTO> {
    List<AppGoodsDTO> getGoodsList();


}