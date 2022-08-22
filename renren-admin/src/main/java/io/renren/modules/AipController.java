package io.renren.modules;

import io.renren.common.utils.Result;
import io.renren.modules.AppArticles.dto.AppArticlesDTO;
import io.renren.modules.AppArticles.service.AppArticlesService;
import io.renren.modules.AppGoods.dto.AppGoodsDTO;
import io.renren.modules.AppGoods.service.AppGoodsService;
import io.renren.modules.AppOrder.dto.AppOrderDTO;
import io.renren.modules.AppOrder.service.AppOrderService;
import io.renren.modules.AppScores.dto.*;
import io.renren.modules.AppScores.service.AppScoresService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AipController {

    @Autowired
    private AppGoodsService appGoodsService;
    @Autowired
    private AppScoresService appScoresService;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private AppArticlesService appArticlesService;

    @PostMapping("getGoodsList")
    @ApiOperation("获取商品列表")
    public Result<List<AppGoodsDTO>> getGoodsList() {
        List<AppGoodsDTO> data = appGoodsService.getGoodsList();
        return new Result<List<AppGoodsDTO>>().ok(data);
    }

    @PostMapping("getOrderList")
    @ApiOperation("获取订单列表")
    public Result<List<AppOrderDTO>> getOrderList(@RequestBody IdDTO dto) {

        List<AppOrderDTO> data = appOrderService.getOrderList(dto.getId());
        return new Result<List<AppOrderDTO>>().ok(data);

    }

    @PostMapping("getScoreList")
    @ApiOperation("获取积分列表")
    public Result<List<AppScoresDTO>> getScoreList(@RequestBody IdDTO dto) {
        List<AppScoresDTO> data = appScoresService.getScoreList(dto.getId());
        return new Result<List<AppScoresDTO>>().ok(data);
    }


    @PostMapping("getMaxScore")
    @ApiOperation("获取最高积分")
    public Result<ScoresDTO> getMaxScore(@RequestBody IdDTO dto) {
        ScoresDTO data = appScoresService.getMaxScores(dto.getId());
        return new Result<ScoresDTO>().ok(data);
    }



    @PostMapping("getArticlesList")
    @ApiOperation("获取文章列表")
    public Result<List<AppArticlesDTO>> getArticlesList(@RequestBody TypeDTO dto) {

        List<AppArticlesDTO> data = appArticlesService.getArticlesList(dto.getType());
        return new Result<List<AppArticlesDTO>>().ok(data);
    }

    @PostMapping("getArticleDetail")
    @ApiOperation("获取文章详情")
    public Result<AppArticlesDTO> getArticleDetail(@RequestBody IdDTO dto) {

        AppArticlesDTO appArticlesDTO = appArticlesService.get(dto.getId());
        return new Result<AppArticlesDTO>().ok(appArticlesDTO);
    }


    @PostMapping("getScores")
    @ApiOperation("获取用户积分-环保积分和实践积分")
    public Result<ScoresDTO> getScores(@RequestBody IdDTO dto) {
        ScoresDTO data = appScoresService.getScores(dto.getId());
        return new Result<ScoresDTO>().ok(data);
    }

    @PostMapping("addScoresRecord")
    @ApiOperation("产生积分记录")
    public Result<String> addScoresRecord(@RequestBody SaveSocresDTO dto) {
        return appScoresService.addScoresRecord(dto);

    }


}
