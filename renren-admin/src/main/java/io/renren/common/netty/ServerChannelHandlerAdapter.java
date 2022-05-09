package io.renren.common.netty;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import io.renren.common.config.NettyConfig;
import io.renren.common.utils.DateUtil2;
import io.renren.common.utils.HexUtil;
import io.renren.common.utils.StringUtil;
import io.renren.common.utils.Util16;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.pub.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 多线程共享
 */
@Slf4j
@Component
@Sharable
public class ServerChannelHandlerAdapter extends ChannelInboundHandlerAdapter {
    @Autowired
    private SocketService socketService;
    @Autowired
    private NettyService nettyService;
    @Resource
    private NettyConfig nettyConfig;

    private int loss_connect_time = 0;

    @Autowired
    private KxDeviceService kxDeviceService;


    /**
     * 日志处理
     */
    private Logger logger = LoggerFactory.getLogger(ServerChannelHandlerAdapter.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("收到连接：" + ctx.channel().id());
        logger.info("收到连接：" + ctx.channel().id() + "IP:" + ctx.channel().localAddress().toString() + "--" + DateUtil2.getCurrentDateTime());
        nettyService.add(ctx.channel().id().toString(), ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开连接：" + ctx.channel().id());
        logger.info("断开连接：" + ctx.channel().id() + "IP:" + ctx.channel().localAddress().toString() + "--" + DateUtil2.getCurrentDateTime());
        nettyService.remove(ctx.channel().id().toString());



    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO Auto-generated method stub
        if (evt instanceof IdleStateEvent) {
            //服务端对应着读事件，当为READER_IDLE时触发
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                System.out.println("接收消息超时");
                if (loss_connect_time > 5) {
                    System.out.println("关闭不活动的链接");
                    loss_connect_time = 0;
                    nettyService.remove(ctx.channel().id().toString());
                    ctx.channel().close();
                }
            } else {
                loss_connect_time = 0;
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        logger.error(cause.getMessage());
        logger.error("异常断开连接：" + ctx.channel().id() + "IP:" + ctx.channel().localAddress().toString() + "--" + DateUtil2.getCurrentDateTime());
        nettyService.remove(ctx.channel().id().toString());


        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf result = (ByteBuf) msg;
            byte[] result1 = new byte[result.readableBytes()];
            result.readBytes(result1);
            String body = HexUtil.byte2HexStr(result1);
            logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收到消息：" + body);
            String flag = "";
            int ver = 0;
            int type = 0;
            int payloadLen = 0;
            String deviceSn = "";
            flag = Util16.hexStringToString(body.substring(0, 8));
            //判断协议头KXVR
            if (DataConstant.KX_FLAG.equals(flag)) {
                SocketChannel channel = (SocketChannel) ctx.channel();
                ver = Util16.hexToInt(body.substring(8, 10));
                type = Util16.hexToInt(body.substring(10, 12));
                deviceSn = Util16.hexStringToString(body.substring(12, 46));

                KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
                if (deviceDTO == null) {
                    log.error("总接受入口，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                    return;
                }
                payloadLen = Util16.hexToInt(body.substring(46, 54));
                //1:心跳数据
                if (DataConstant.PACK_TYPE_HEART_BEAT == type) {
                    int data = Util16.hexToInt(body.substring(54, 62));
                    nettyService.rcvHeartBeat(deviceSn, Integer.toString(data), channel);
                    deviceDTO.setEnable("t");
                    kxDeviceService.update(deviceDTO);
                    //发送初始化消息
                } else if (DataConstant.PACK_TYPE_INIT_REPLY == type) {
                    String senderInfo = Util16.hexStringToString(body.substring(54, 54 + payloadLen * 2));
                    logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 收到数据初始化回复，暂时没有其他业务操作：" + senderInfo);
                    //初始化响应过后再对baseInfo和status初始化
                    if (deviceDTO.getBaseInfo() == null || deviceDTO.getStatus() == null) {
                        nettyService.initBaseInfoAndStatus(deviceDTO, channel);
                    }
                }
                //7:数据上传   6：命令响应
                else if (DataConstant.PACK_TYPE_DATA == type || DataConstant.PACK_TYPE_CMMD_REPLY == type) {
                    // SenderInfoLen +  SessionLen + MsgLen + DataLen  =16
                    int DATA_HEAD_LEN = 16;
                    if (payloadLen < DATA_HEAD_LEN) {
                        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 数据上传载荷长度小于描述字节长度，抛弃！！");
                        return;
                    }
                    // 比如00占一个字节 所以*2
                    int SenderInfoLen = Util16.hexToInt(body.substring(54, 62)) * 2;
                    int SessionLen = Util16.hexToInt(body.substring(62, 70)) * 2;
                    int MsgLen = Util16.hexToInt(body.substring(70, 78)) * 2;
                    int DataLen = Util16.hexToInt(body.substring(78, 86)) * 2;
                    logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 数据上传--SenderInfoLen：" + SenderInfoLen + "--SessionLen:" + SessionLen + "--MsgLen" + MsgLen + "--DataLen" + DataLen);
                    //Json格式的发送者信息
                    String senderInfo = "";
                    JSONObject senderInfoJsonObject = new JSONObject();
                    if (SenderInfoLen > 0) {
                        senderInfo = Util16.hexStringToString(body.substring(86, 86 + SenderInfoLen));
                        if (StringUtil.isNotEmpty(senderInfo)) {
                            senderInfoJsonObject = JSONUtil.parseObj(senderInfo);
                        }
                    }
                    //回传调用时Param中所带的“_session”对象，用于服务器辨认会话
                    String session = "";
                    // -1 发现多了一位空格
                    if (SessionLen > 0) {
                        session = Util16.hexStringToString(body.substring(86 + SenderInfoLen, 86 + SenderInfoLen + SessionLen-1));

                    }
                    // 通过类型和session判断是否是-命令回应
                    Boolean isCmmdReply = false;
                    if (DataConstant.PACK_TYPE_CMMD_REPLY == type) {
                        isCmmdReply = true;
                    }
                    //	Json格式的消息体，具体格式见各模块详细设计
                    String msgInfo = "";
                    JSONObject msgInfoJsonObject = new JSONObject();
                    JSONArray array = new JSONArray();
                    if (MsgLen > 0) {
                        msgInfo = Util16.hexStringToString(body.substring(86 + SenderInfoLen + SessionLen, 86 + SenderInfoLen + SessionLen + MsgLen));
                        if (StringUtil.isNotEmpty(msgInfo)) {
                            msgInfoJsonObject = JSONUtil.parseObj(msgInfo);
                            if (isCmmdReply) {
                                Object obj = msgInfoJsonObject.get("ResultValue");
                                if (obj != null) {
                                    JSONObject resultJoson = JSONUtil.parseObj(obj);
                                    array = JSONUtil.parseArray(resultJoson.get("Files"));
                                }
                            } else {
                                Object files = msgInfoJsonObject.get("Files");
                                array = JSONUtil.parseArray(files);
                            }
                        }
                    }
                    //附加数据
                    String data = "";
                    if (DataLen > 0) {
                        data = body.substring(86 + SenderInfoLen + SessionLen + MsgLen, 86 + SenderInfoLen + SessionLen + MsgLen + DataLen);
                    }
                    if (array.size() > 0) {
                        String fileType = "";
                        if (senderInfoJsonObject.get("Signal") != null) {
                            if ("AccessPicture".equals(senderInfoJsonObject.get("Signal"))) {
                                fileType = "Access";
                            } else {
                                fileType = "Data";
                            }
                        }
                        if (isCmmdReply) {
                            fileType = "Result";
                        }
                        //保存文件

                        String Interface = String.valueOf(senderInfoJsonObject.get("Interface"));
                        JSONArray jsonArray = nettyService.rcvFilesData(deviceSn, array, fileType, DataLen, data,Interface);
                        if (jsonArray != null && jsonArray.size() > 0) {
                            if (isCmmdReply) {
                                Object obj = msgInfoJsonObject.get("ResultValue");
                                if (obj != null) {
                                    JSONObject resultJoson = JSONUtil.parseObj(obj);
                                    JSONObject jsonObject = resultJoson.putOpt("Files", jsonArray);
                                    msgInfoJsonObject.putOpt("ResultValue", jsonObject);
                                }
                            } else {
                                msgInfoJsonObject.putOpt("Files", jsonArray);

                         /*       //后台分析图片 todo cxy 位置？
                                if (null != jsonArray && !jsonArray.isEmpty()) {
                                    JSONObject json = jsonArray.getJSONObject(0);
                                    kxDeviceService.analysisImg(json,deviceDTO.getId());
                                }
*/
                            }
                        }

                    }
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = formatter.format(currentTime);
                    senderInfoJsonObject.putOpt("ReceiveTime", dateString);
                    if (senderInfoJsonObject.get("CreatedTime") == null) {
                        senderInfoJsonObject.putOpt("CreatedTime", dateString);
                    }
                    if (isCmmdReply) {
                        nettyService.rcvCmmdReply(deviceSn, senderInfoJsonObject, msgInfoJsonObject, channel,session);
                    } else {
                        //判断Json格式的消息体不是空
                        if (StringUtil.isNotEmpty(msgInfo)) {
                            //处理接收的上传数据
                            nettyService.rcvUploadData(deviceSn, senderInfoJsonObject, msgInfoJsonObject, channel);
                            // 数据确认回复
                            nettyService.sendUploadDataRep(deviceSn, channel);
                        }
                    }
                }
            } else {
                logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 协议头错误:" + flag);

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误：" + e.getMessage());
        } finally {
            ReferenceCountUtil.release(msg);
            System.out.println("消息回收机制！---------------");
        }


    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        logger.info("链接报告开始" + ctx.channel().id());
        logger.info("链接报告信息：有一客户端链接到本服务端");
        logger.info("链接报告IP:{}", channel.localAddress().getHostString());
        logger.info("链接报告Port:{}", channel.localAddress().getPort());
        logger.info("链接报告完毕");
        //通知客户端链接建立成功

        //String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        //ctx.writeAndFlush(str);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端断开链接{}", ctx.channel().localAddress().toString() + "---" + ctx.channel().id());
    }


}
