package io.renren.modules.discernConfig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 识别参数配置
 *
 * @author cxy
 * @since 3.0 2022-03-08
 */
@Data
@ApiModel(value = "识别参数配置")
public class KxAIPzVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "code")
    private String code;
    @ApiModelProperty(value = "sort")
    private Integer sort;
    @ApiModelProperty(value = "是否启用")
    private boolean enable;
    @ApiModelProperty(value = "是否启用铃声")
    private boolean ringSwitch;
    @ApiModelProperty(value = "启用铃声路径")
    private String ringType;
    @ApiModelProperty(value = "相似度")
    private BigDecimal thresh;

}