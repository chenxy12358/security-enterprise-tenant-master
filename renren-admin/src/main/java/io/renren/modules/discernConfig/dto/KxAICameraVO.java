package io.renren.modules.discernConfig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 识别参数配置
 *
 * @author cxy
 * @since 3.0 2022年3月10日17:55:19
 */
@Data
@ApiModel(value = "相机参数配置")
public class KxAICameraVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "presetNo")
    private String presetNo;
    @ApiModelProperty(value = "是否启用")
    private boolean enable;
    @ApiModelProperty(value = "时间")
    private int duration;

}