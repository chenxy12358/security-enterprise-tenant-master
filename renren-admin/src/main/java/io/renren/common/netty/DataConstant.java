package io.renren.common.netty;

import org.springframework.stereotype.Component;

@Component
public class DataConstant {
    //固定头长度
    public final static int KX_HEAD_LEN = 27;
    //协议头标志
    public final static String KX_FLAG = "HYVR";
    //协议版本
    public final static int KX_VERSION = 1;
	/**
	 * 	1 - 心跳
	 * 2 - 心跳回应
	 * 3 - 初始化
	 * 4 - 初始化回应
	 * 5 - 命令
	 * 6 - 命令回应
	 * 7 - 数据上传
	 * 8 - 数据上传确认
	 */
	public final static int PACK_TYPE_HEART_BEAT = 1;
    public final static int PACK_TYPE_HEART_BEAT_REPLY = 2;
    public final static int PACK_TYPE_INIT = 3;
    public final static int PACK_TYPE_INIT_REPLY = 4;
    public final static int PACK_TYPE_CMMD = 5;
    public final static int PACK_TYPE_CMMD_REPLY = 6;
    public final static int PACK_TYPE_DATA = 7;
    public final static int PACK_TYPE_DATA_REPLY = 8;





    public final static int SUCCESS = 1;//命令发送成功


}
