package io.renren.common.utils;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;

public class SendMsgUtils {

	/**
	 *  发送指令
	 * @param serialNo
	 * @param destInfo
	 * @param param
	 * @param channel
	 * @throws Exception
	 */
	public static void sendMsg(String serialNo, String destInfo, String param, Channel channel) throws Exception {
		byte[] d = HexUtil.sendCmmd(serialNo, destInfo, new String(param.getBytes(), "UTF-8"), "", 3);
		ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
		respLengthBuf.writeBytes(d);
		channel.writeAndFlush(respLengthBuf);
	}

}
