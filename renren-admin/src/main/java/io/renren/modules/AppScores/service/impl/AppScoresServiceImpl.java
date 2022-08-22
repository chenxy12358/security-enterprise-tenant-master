package io.renren.modules.AppScores.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.modules.AppGoods.dto.AppGoodsDTO;
import io.renren.modules.AppGoods.service.AppGoodsService;
import io.renren.modules.AppOrder.dto.AppOrderDTO;
import io.renren.modules.AppOrder.service.AppOrderService;
import io.renren.modules.AppScores.dao.AppScoresDao;
import io.renren.modules.AppScores.dto.AppScoresDTO;
import io.renren.modules.AppScores.dto.SaveSocresDTO;
import io.renren.modules.AppScores.dto.ScoresDTO;
import io.renren.modules.AppScores.entity.AppScoresEntity;
import io.renren.modules.AppScores.service.AppScoresService;
import io.renren.modules.security.user.SecurityUser;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 积分记录
 *
 * @author WEI
 * @since 3.0 2022-08-13
 */
@Service
public class AppScoresServiceImpl extends CrudServiceImpl<AppScoresDao, AppScoresEntity, AppScoresDTO> implements AppScoresService {

    @Autowired
    private AppGoodsService appGoodsService;


    @Autowired
    private AppOrderService appOrderService;

    @Override
    public QueryWrapper<AppScoresEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<AppScoresEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    public ScoresDTO getScores(Long id) {
        ScoresDTO scores = this.baseDao.getScores(id);
        return scores;
    }

    @Override
    public Result addScoresRecord(SaveSocresDTO dto) {

        AppGoodsDTO appGoodsDTO = appGoodsService.get(dto.getGoodsId());
        if (appGoodsDTO == null) {
            return new Result<String>().error("该商品已经下架");
        } else {
            if (appGoodsDTO.getRemainCount() < dto.getGoodsCount()) {
                return new Result<String>().error("库存不够了，积极备货中...");
            }
        }
        ScoresDTO scores = getScores(dto.getUserId());
        if (scores.getNormalScore() < dto.getTotalNamorl() || scores.getSpecialScore() < dto.getTotalSpecial()) {
            return new Result<String>().error("很遗憾！积分不够啦!!!");
        }
        // 添加积分记录
        AppScoresDTO appScoresDTO = new AppScoresDTO();
        appScoresDTO.setUserId(dto.getUserId());
        appScoresDTO.setNormalScore(-dto.getTotalNamorl()); //减少 -
        appScoresDTO.setSpecialScore(-dto.getTotalSpecial());
        appScoresDTO.setType("DH");// 兑换
        this.save(appScoresDTO);
        // 添加兑换记录
        AppOrderDTO appOrderDTO = new AppOrderDTO();
        appOrderDTO.setUserId(dto.getUserId());
        appOrderDTO.setStatus("WDH");
        appOrderDTO.setGoodsName(dto.getGoodsName());
        appOrderDTO.setUrl(appGoodsDTO.getUrl());
        appOrderDTO.setTotalNamorl(dto.getTotalNamorl());
        appOrderDTO.setTotalSpecial(dto.getTotalSpecial());
        appOrderDTO.setGoodsCount(dto.getGoodsCount());
        appOrderService.save(appOrderDTO);
        //减少存存 增加销售量
        appGoodsDTO.setRemainCount(appGoodsDTO.getRemainCount() - dto.getGoodsCount());
        appGoodsDTO.setSaleCount(appGoodsDTO.getSaleCount() + dto.getGoodsCount());
        appGoodsService.update(appGoodsDTO);
        return new Result<String>().ok("成功，已生成兑换订单！");
    }

    @Override
    public List<AppScoresDTO> getScoreList(Long id) {
        return this.baseDao.getScoresList(id);
    }

    @Override
    public ScoresDTO getMaxScores(Long id) {
        return this.baseDao.getMaxScores(id);
    }
}