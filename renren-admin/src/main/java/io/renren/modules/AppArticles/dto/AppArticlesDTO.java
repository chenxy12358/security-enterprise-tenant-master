package io.renren.modules.AppArticles.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 文章管理
*
* @author WEI 
* @since 3.0 2022-08-17
*/
@Data
@ApiModel(value = "文章管理")
public class AppArticlesDTO implements Serializable {
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
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "发布时间")
    private Date pubDate;
    @ApiModelProperty(value = "阅读次数")
    private Integer readCount;
    @ApiModelProperty(value = "文章类型")
    private String type;
    @ApiModelProperty(value = "图片路径")
    private String url;

}