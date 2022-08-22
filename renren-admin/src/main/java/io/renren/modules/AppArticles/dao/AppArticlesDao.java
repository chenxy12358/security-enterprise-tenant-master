package io.renren.modules.AppArticles.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.AppArticles.dto.AppArticlesDTO;
import io.renren.modules.AppArticles.entity.AppArticlesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 文章管理
*
* @author WEI 
* @since 3.0 2022-08-17
*/
@Mapper
public interface AppArticlesDao extends BaseDao<AppArticlesEntity> {
    List<AppArticlesDTO> getArticlesList(String type);
}