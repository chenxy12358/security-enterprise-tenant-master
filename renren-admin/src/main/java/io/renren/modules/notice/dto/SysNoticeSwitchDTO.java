package io.renren.modules.notice.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
* notice
*
* @author WEI 
* @since 3.0 2022-05-23
*/
@Data
@ApiModel(value = "notice")
public class SysNoticeSwitchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String status;

}