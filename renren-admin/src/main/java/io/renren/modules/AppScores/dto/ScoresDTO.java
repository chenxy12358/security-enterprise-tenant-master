package io.renren.modules.AppScores.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScoresDTO {
    @ApiModelProperty(value = "环保积分")
    private Integer normalScore;
    @ApiModelProperty(value = "实践积分")
    private Integer specialScore;
}
