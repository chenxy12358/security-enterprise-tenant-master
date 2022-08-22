package io.renren.modules.AppScores.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveSocresDTO {


    private String goodsName;
    /**
     * 商品數量
     */
    private Integer goodsCount;

    /**
     * 总环保积分
     */
    private Integer totalNamorl;
    /**
     * 总实践积分
     */
    private Integer totalSpecial;
    /**
     * 用户id
     */
    private Long userId;
    private Long goodsId;
}
