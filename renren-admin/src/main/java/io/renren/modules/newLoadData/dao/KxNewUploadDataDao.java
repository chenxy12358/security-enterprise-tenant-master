package io.renren.modules.newLoadData.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.newLoadData.entity.KxNewUploadDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 最新上传数据
*
* @author WEI 
* @since 3.0 2022-04-14
*/
@Mapper
public interface KxNewUploadDataDao extends BaseDao<KxNewUploadDataEntity> {
    void delByDeviceId(Long id);

}