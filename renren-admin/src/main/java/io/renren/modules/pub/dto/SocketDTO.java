package io.renren.modules.pub.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 通讯记录表
 *
 * @author zhengweicheng
 */
@Data
@ApiModel(value = "通讯记录表")
public class SocketDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "协议类型")
	private String type;

	@ApiModelProperty(value = "位置")
	private String addr;

	@ApiModelProperty(value = "协议内容")
	private String content;
	@ApiModelProperty(value = "创建时间")
	private Date createDate;

}