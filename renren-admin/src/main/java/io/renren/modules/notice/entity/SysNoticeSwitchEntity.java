package io.renren.modules.notice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * notice
 *
 * @author WEI 
 * @since 3.0 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_notice_switch")
public class SysNoticeSwitchEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long userId;
	private String status;
}