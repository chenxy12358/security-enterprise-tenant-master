package io.renren.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.renren.common.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServerDecoder extends ByteToMessageDecoder {
    private int m = 0;
    private int n = 0;
    private boolean first = true;
    private int beginReader = 0;
    private int len = 0;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //超过最大长度
        if (byteBuf.readableBytes() > 0) {
            if (byteBuf.readableBytes() > 19944) {
                log.debug("消息长度超出最大值!" + byteBuf.readableBytes());
                byteBuf.skipBytes(byteBuf.readableBytes());

            }
            //寻找包头
            // 记录包头开始的index
//            int beginReader = 0;
//            int len = 0;

            while (true) {
                int r = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                byte t = byteBuf.readByte();
             //   String s = HexUtil.byte2hex(t);

                String s = "";
                log.info(channelHandlerContext.channel().remoteAddress() + "收到消息：" + s);
                if (s.equals("7E")) {
                    if (m == 1 && !first) {
                        //包尾和下一个包头
                        log.info("找到包尾");
                        if (len == 1) {
                            log.info("数据长度不正确，重新确定开始位置");
                            beginReader = r;
                            len--;
                        } else {
                            log.info("正常结束");
                            m++;
                        }
                    } else {
                        log.info("找到包头");
                        first = false;
                        if (m == 0) {
                            beginReader = r;

                            //System.out.println("找到包头"+beginReader);
                        } else {
                            // System.out.println("找到包尾");
                        }
                        m++;
                    }
                } else if (s.equals("4C") && first) {
                    beginReader = r;
                    n = 1;
                } else if (s.equals("31") && first && n == 1) {
                    n = 2;
                } else if (s.equals("31") && first && n == 2) {
                    n = 3;
                }
                if (n >= 1) {
                    len++;
                }
                if (n == 3) {
                    log.debug(channelHandlerContext.channel().remoteAddress() + "收到设备上电消息，丢弃!");
                    n = 0;
                    first = true;
                    break;
                }
                if (m >= 1) {
                    len++;
                }
                if (m == 2) {
                    m = 0;
                    break;
                }
                byteBuf.resetReaderIndex();
                // 缓存指针向后移动一个字节
                byteBuf.readByte();
                //System.out.println("剩余包长度："+byteBuf.readableBytes());
                if (byteBuf.readableBytes() == 0) {
                    if (m == 1) {
                        System.out.println("剩余包长度：" + beginReader + "--" + len);
                        byteBuf.readerIndex(beginReader);//指针回到包头位子
                        m = 0;
                        len = 0;
                        return;//数据没有接收完成，等待接收
                    }
                }
            }
            log.debug(channelHandlerContext.channel().remoteAddress() + "收到消息：" + beginReader + "--" + len);
            // 读取data数据
            byteBuf.readerIndex(beginReader);//指针回到包头位子
            byte[] data = new byte[len];
            try {
                byteBuf.readBytes(data);
                len = 0;
                beginReader = 0;
            } catch (Exception e) {
                len = 0;
                beginReader = 0;
                e.printStackTrace();
                log.error("读取消息错误" + e.getMessage());
            }
            list.add(data);
        } else {
            return;
        }


    }
}
