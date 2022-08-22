package io.renren.modules.AppGoods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 商品管理
*
* @author WEI 
* @since 3.0 2022-08-13
*/
@Data
@ApiModel(value = "商品管理")
public class AppGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "学校编号")
    private Long schoolCode;
    @ApiModelProperty(value = "租户编码")
    private Long tenantCode;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long updater;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "环保积分")
    private Integer normalPrice;
    @ApiModelProperty(value = "组合-环保积分")
    private Integer comNormalPrice;
    @ApiModelProperty(value = "组合-实践积分")
    private Integer comSpecialPrice;
    @ApiModelProperty(value = "库存数量")
    private Integer remainCount;
    @ApiModelProperty(value = "已售数量")
    private Integer saleCount;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "图片路径")
    private String url;

}