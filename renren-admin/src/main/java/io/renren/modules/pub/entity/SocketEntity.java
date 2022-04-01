package io.renren.modules.pub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通讯记录表
 *
 * @author zhengweicheng
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("pub_socket")
public class SocketEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 协议类型
     */
	private String type;
    /**
     * 位置
     */
	private String addr;
    /**
     * 协议内容
     */
	private String content;
	
}