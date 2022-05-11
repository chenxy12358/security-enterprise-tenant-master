package io.renren.modules.discernConfig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 识别参数配置
*
* @author cxy 
* @since 3.0 2022年3月10日17:55:19
*/
@Data
@ApiModel(value = "相机参数配置")
public class KxCameraNameVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "Camera")
    private String camera;
    @ApiModelProperty(value = "时间")
    private List<KxAICameraVO> listC;

}