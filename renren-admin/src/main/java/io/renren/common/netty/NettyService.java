package io.renren.common.netty;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.renren.common.exception.RenException;
import io.renren.common.utils.*;
import io.renren.modules.battery.dto.KxBatteryDTO;
import io.renren.modules.battery.service.KxBatteryService;
import io.renren.modules.common.constant.DeviceInterfaceConstants;
import io.renren.modules.common.constant.KxConstants;
import io.renren.modules.common.utils.CoordinateTransform;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.deviceAlarm.dto.KxDeviceAlarmDTO;
import io.renren.modules.deviceAlarm.service.KxDeviceAlarmService;
import io.renren.modules.deviceData.dto.*;
import io.renren.modules.deviceData.service.*;
import io.renren.modules.discernConfig.dto.KxDiscernConfigDTO;
import io.renren.modules.discernConfig.service.KxDiscernConfigService;
import io.renren.modules.gas.dto.KxGasDataDTO;
import io.renren.modules.gas.service.KxGasDataService;
import io.renren.modules.netWorkData.dto.KxNetWorkStateDataDTO;
import io.renren.modules.netWorkData.service.KxNetWorkStateDataService;
import io.renren.modules.newLoadData.dto.KxNewUploadDataDTO;
import io.renren.modules.newLoadData.service.KxNewUploadDataService;
import io.renren.modules.notice.dto.SysNoticeDTO;
import io.renren.modules.notice.service.SysNoticeService;
import io.renren.modules.scheduleItem.dto.KxScheduleItemDTO;
import io.renren.modules.scheduleItem.service.KxScheduleItemService;
import io.renren.modules.scheduleJob.entity.KxScheduleJobEntity;
import io.renren.modules.scheduleJob.service.KxScheduleJobService;
import io.renren.modules.stationTrack.service.KxStationTrackService;
import io.renren.modules.sys.entity.SysDictDataEntity;
import io.renren.modules.sys.service.SysDictDataService;
import io.renren.websocket.WebSocketLiveServer;
import io.renren.websocket.WebSocketServer;
import io.renren.websocket.data.MessageData;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("nettyService")
@Slf4j
public class NettyService {
    @Autowired
    private KxDeviceService kxDeviceService;
    @Autowired
    private KxDeviceAlarmService kxDeviceAlarmService;
    @Autowired
    private KxGasDataService KxGasDataService;
    @Autowired
    private KxNetWorkStateDataService kxNetWorkStateDataService;
    @Autowired
    private KxBatteryService kxBatteryService;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private KxScheduleJobService kxScheduleJobService;
    @Autowired
    private KxScheduleItemService kxScheduleItemService;
    @Autowired
    private KxDiscernConfigService kxDiscernConfigService;
    @Autowired
    private KxDeviceGpsService kxDeviceGpsService;
    @Autowired
    private KxDeviceHumidityService kxDeviceHumidityService;
    @Autowired
    private KxDeviceInclinationService kxDeviceInclinationService;
    @Autowired
    private KxDeviceTemperatureService kxDeviceTemperatureService;
    @Autowired
    private KxNewUploadDataService kxNewUploadDataService;
    @Autowired
    private KxStationTrackService kxStationTrackService;
    private Map<String, Channel> map = new HashMap<String, Channel>();
    private Map<String, String> ServerMap = new HashMap<String, String>();
    @Autowired
    private SysNoticeService sysNoticeService;
    @Autowired
    private WebSocketLiveServer webSocketLiveServer;

    @Autowired
    private SysDictDataService sysDictDataService;

    /**
     * 日志处理
     */
    private Logger logger = LoggerFactory.getLogger(NettyService.class);

    /**
     * 初始化基本信息和状态
     * baseInfo：先查看缓存中是否有该设备的基本信息，如果有则直接取，保存到设备的baseInfo字段，如果没有则下发获取基本信息的命令给设备。
     * status：发送命令取最新全量状态
     *
     * @param dto
     * @param channel
     */
    public void initBaseInfoAndStatus(KxDeviceDTO dto, SocketChannel channel) {
        try {
            // todo  暂时发命令取 测试命令
            JSONObject destInfo = new JSONObject();
            destInfo.putOpt("DestObject", "Emd.Service.SysMonitor.E0");
//            destInfo.putOpt("Method", "GetSysBaseInfo");
//            destInfo.putOpt("Interface", "Emd.Method.Normal");
            destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_GETSYSBASEINFO);
            destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
            // todo 设置session
            JSONObject param = new JSONObject();
            param.putOpt("_session", 1);
            //获取基本信息
            byte[] d = HexUtil.sendCmmd(dto.getSerialNo(), destInfo.toString(), param.toString(), "", 3);
            ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
            respLengthBuf.writeBytes(d);
            channel.writeAndFlush(respLengthBuf);
            //获取状态
            destInfo.putOpt("Method", "GetSysStat");
            byte[] d2 = HexUtil.sendCmmd(dto.getSerialNo(), destInfo.toString(), param.toString(), "", 3);
            ByteBuf respLengthBuf2 = PooledByteBufAllocator.DEFAULT.buffer(4);
            respLengthBuf2.writeBytes(d2);
            channel.writeAndFlush(respLengthBuf2);
        } catch (Exception e) {
            logger.error("initBaseInfoAndStatus", e);
            e.printStackTrace();
        }
    }

    /**
     * 发送初始化消息
     *
     * @param deviceSn
     * @param channel
     */
    public void sendInitInfo(String deviceSn, SocketChannel channel) {
        //不查询被删数据
        KxDeviceDTO dto = kxDeviceService.getBySerialNo(deviceSn);
        if (dto == null) {
            log.error("处理心跳数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
            return;
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        JSONObject dataJson = new JSONObject();
        dataJson.putOpt("DateTime", dateString);
        //发送初始化数据
        byte[] d = HexUtil.sendInitInfo(deviceSn, dataJson.toString());
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 发送初始化数据");
        ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
        respLengthBuf.writeBytes(d);
        channel.writeAndFlush(respLengthBuf);
    }

    /**
     * 回复上传数据的确认消息
     *
     * @param deviceSn
     */
    public void sendUploadDataRep(String deviceSn, SocketChannel channel) {
        KxDeviceDTO dto = kxDeviceService.getBySerialNo(deviceSn);
        if (dto == null) {
            log.error("处理心跳数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
            return;
        }
        byte[] d = HexUtil.sendUploadDataRep(deviceSn);
        ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
        respLengthBuf.writeBytes(d);
        channel.writeAndFlush(respLengthBuf);
    }

    /**
     * 发送相机命令给设备端 目的：获取播放信息
     *
     * @param deviceSn
     * @param cameraName
     */
    public void sendCmdCamera(String deviceSn, String cameraName, Long currentTime) {
        try {
            KxDeviceDTO dto = kxDeviceService.getBySerialNo(deviceSn);
            if (dto == null) {
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.VideoSender.E0");
//                destInfo.putOpt("Method", "SendVideoStream");
//                destInfo.putOpt("Interface", "Emd.Method.Normal");
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_SENDVIDEOSTREAM);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                // todo 设置session
                JSONObject param = new JSONObject();
                param.putOpt("Camera", cameraName);
                param.putOpt("Nic", "Emd.Device.Nic.E0");
                param.putOpt("ServerAddr", "117.175.158.169");
                param.putOpt("ServerPort", 1935);
                param.putOpt("Channel", 2);
                param.putOpt("StreamType", "Rtmp");
                param.putOpt("Protocol", "tcp");
                param.putOpt("_session", currentTime);
                //获取基本信息
                byte[] d = HexUtil.sendCmmd(dto.getSerialNo(), destInfo.toString(), param.toString(), "", 3);
                ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
                respLengthBuf.writeBytes(d);
                channel.writeAndFlush(respLengthBuf);
                log.error("发送相机命令给设备端 目的：获取播放信息");
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
        }
    }

    /**
     * 发送保持命令
     *
     * @param deviceSn
     * @param taskId
     */
    public void keepStream(String deviceSn, int taskId) {
        try {
            KxDeviceDTO dto = kxDeviceService.getBySerialNo(deviceSn);
            if (dto == null) {
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.VideoSender.E0");
//                destInfo.putOpt("Method", "KeepVideoStream");
//                destInfo.putOpt("Interface", "Emd.Method.Normal");
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_KEEPVIDEOSTREAM);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                // todo 设置session
                JSONObject param = new JSONObject();
                param.putOpt("TaskId", taskId);
                //获取基本信息
                byte[] d = HexUtil.sendCmmd(dto.getSerialNo(), destInfo.toString(), param.toString(), "", 3);
                ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
                respLengthBuf.writeBytes(d);
                channel.writeAndFlush(respLengthBuf);
                log.error("发送保持推送命令");
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
        }
    }

    /**
     * PtzControl方法
     *
     * @param params
     */
    public void sendCmdPtzControl(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.getBySerialNo(String.valueOf(params.get("deviceSn")));
            if (null != params.get("deviceId")) {
                dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            }
            if (dto == null) {
                logger.error("sendCmdPtzControl====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", String.valueOf(params.get("cameraName")));
//                destInfo.putOpt("Method", "PtzControl");
//                destInfo.putOpt("Interface", "Emd.Method.Ctrl");
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_PTZCONTROL);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_CTRL);
                JSONObject param = new JSONObject();
                param.putOpt("Command", String.valueOf(params.get("command")));
                param.putOpt("AutoScanParam", null);
                if (params.get("PresetId") != null) {
                    param.putOpt("PresetId", params.get("PresetId"));
                }
                if (params.get("PresetName") != null) {
                    String PresetName = String.valueOf(params.get("PresetName"));
                    param.putOpt("PresetName", PresetName);
                }
                if (params.get("TourID") != null) {
                    param.putOpt("TourID", params.get("TourID"));
                }
                if (params.get("Speed") != null) {
                    param.putOpt("Speed", params.get("speed"));
                }
                if (params.get("Duration") != null) {
                    param.putOpt("Duration", params.get("Duration"));
                }
                param.putOpt("_session", Long.valueOf(params.get("currentTime").toString()));
                //获取基本信息
                byte[] d = HexUtil.sendCmmd(dto.getSerialNo(), destInfo.toString(), new String(param.toString().getBytes(), "UTF-8"), "", 3);
                ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
                respLengthBuf.writeBytes(d);
                channel.writeAndFlush(respLengthBuf);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sendCmdPtzControl", e);
        }
    }


    /**
     * @param params
     */
    public void sendTakePicture(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.getBySerialNo(String.valueOf(params.get("deviceSn")));
            if (null != params.get("deviceId")) {
                dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            }
            if (dto == null) {
                logger.error("sendTakePicture====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.PicCapture.E0");
                destInfo.putOpt("Method", "PicCapture");
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                param.putOpt("Camera", params.get("cameraName"));
                param.putOpt("Height", params.get("Height"));
                param.putOpt("Width", params.get("Width"));
                param.putOpt("Quality", params.get("Quality"));
                param.putOpt("_session", Long.valueOf(params.get("currentTime").toString()));
                //获取基本信息
                byte[] d = HexUtil.sendCmmd(dto.getSerialNo(), destInfo.toString(), new String(param.toString().getBytes(), "UTF-8"), "", 3);
                ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
                respLengthBuf.writeBytes(d);
                channel.writeAndFlush(respLengthBuf);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sendCmdPtzControl", e);
        }
    }


    /**
     * PtzControl方法
     *
     * @param params
     */
    public void getPresetList(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                logger.error("getPresetList====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", String.valueOf(params.get("cameraName")));
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_PTZCONTROL);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_CTRL);
                JSONObject param = new JSONObject();
                param.putOpt("Command", String.valueOf(params.get("command")));
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sendCmdPtzControl", e);
        }
    }

    /**
     * PtzControl方法
     *
     * @param params
     */
    public void getAudioList(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.getBySerialNo(String.valueOf(params.get("deviceSn")));
            if (null != params.get("deviceId")) {
                dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            }
            if (dto == null) {
                logger.error("getAudioList====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.Audio.E0"); //todo
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_GETAUDIOLIST);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
//                param.putOpt("_session", params.get("currentTime").toString());
                param.putOpt("_session", Long.valueOf(params.get("currentTime").toString()));
                //获取基本信息
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getAudioList", e);
        }
    }

    /**
     * 发送计划任务
     *
     * @param deviceId
     */
    public void sendJobInfo(Long deviceId) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(deviceId);
            if (dto == null) {
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.TimerTask.E0");// todo 暂时固定写法
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_SETCONFIG);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                List<KxScheduleJobEntity> jobList = kxScheduleJobService.getInfoByDeviceId(deviceId);
                if (null != jobList && jobList.size() > 0) {
                    for (KxScheduleJobEntity kxScheduleJobEntity : jobList) {
                        JSONObject json = JSONUtil.parseObj(kxScheduleJobEntity.getContent());
                        String jobconf = json.get("TaskSchedule").toString();
                        jsonArray.addAll(JSONUtil.parseArray(jobconf));
                        kxScheduleJobEntity.setStatus("1");//已发送
                        kxScheduleJobService.updateById(kxScheduleJobEntity);
                    }
                }
                param.putOpt("TaskSchedule", jsonArray);
                System.err.println(param);
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("发送计划任务失败,无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送计划任务失败=");
            log.error("send message error，" + e.getMessage(), e);
        }
    }

    /**
     * 发送AI任务 todo
     *
     * @param deviceId
     */
    public void sendAIConfig(Long deviceId) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(deviceId);
            if (dto == null) {
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                KxDiscernConfigDTO kxDiscernConfigDTO = kxDiscernConfigService.getBydeviceId(deviceId);
                if (null != kxDiscernConfigDTO.getCameraConfig()) { //相机任务
                    JSONObject param = new JSONObject();
                    JSONArray cameraJson = JSONUtil.parseArray(kxDiscernConfigDTO.getCameraConfig());
                    JSONObject destInfo = new JSONObject();
                    destInfo.putOpt("DestObject", "Emd.Service.DLDetect.E0");// todo 暂时固定写法
                    destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_SETAITASK);
                    destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                    param.putOpt("Tasks", cameraJson);
                    //发送指令
                    SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
                }
                if (null != kxDiscernConfigDTO.getDistinguishConfig()) { //AI配置
                    JSONObject param = new JSONObject();
                    ;
                    JSONObject disConfigJson = JSONUtil.parseObj(kxDiscernConfigDTO.getDistinguishConfig());
                    JSONObject destInfo = new JSONObject();
                    destInfo.putOpt("DestObject", "Emd.Service.DLDetect.E0");// todo 暂时固定写法
                    destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_SETALARMPARAM);
                    destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(disConfigJson);
                    param.putOpt("AlarmParam", jsonArray);
                    //发送指令
                    SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
                    SendMsgUtils.sendMsg(dto.getSerialNo(),destInfo.toString(),param.toString(),channel);
                    kxDiscernConfigDTO.setStatus("1");//已发送
                    kxDiscernConfigService.update(kxDiscernConfigDTO);

                }
            } else {
                log.error("发送AI配置失败,无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send AI message error，" + e.getMessage(), e);
        }
    }

    /**
     * 获取vpn状态
     *
     * @param params
     */
    public void getVPNStat(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                logger.error("获取vpn状态====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.Vpn.E0");
                destInfo.putOpt("Method", "GetStat");
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sendCmdPtzControl", e);
        }
    }

    /**
     * 开启vpn
     *
     * @param params
     */
    public void openVpn(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                logger.error("开启vpn====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.Vpn.E0");
                destInfo.putOpt("Method", "Connect");
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                param.putOpt("ServerAddr", params.get("ServerAddr"));
                param.putOpt("Port", params.get("Port"));
                param.putOpt("VpnAddr", params.get("VpnAddr"));
                param.putOpt("VpnType", params.get("VpnType"));
                param.putOpt("VpnTime", params.get("VpnTime"));
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sendCmdPtzControl", e);
        }
    }

    /**
     * 关闭vpn
     *
     * @param params
     */
    public void closeVpn(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                logger.error("关闭vpn====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.Vpn.E0");
                destInfo.putOpt("Method", "Close");
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sendCmdPtzControl", e);
        }
    }

    /**
     * 接收心跳数据
     *
     * @param deviceSn
     * @param data
     * @param channel
     */
    public void rcvHeartBeat(String deviceSn, String data, SocketChannel channel) {
        //不查询被删数据
        KxDeviceDTO dto = kxDeviceService.getBySerialNo(deviceSn);
        if (dto == null) {
            log.error("处理心跳数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn + "--心跳值：" + data);
            return;
        }
        //做通道绑定
        addServer(channel.id().toString(), deviceSn);
        //修改设备状态
        if (!"t".equals(dto.getEnable())) {
            dto.setEnable("t");
            kxDeviceService.update(dto);
        }
        //回复心跳
        byte[] d = HexUtil.makeHeartReply(deviceSn, data);
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 发送心跳回复:" + JSONUtil.toJsonStr(d));
        ByteBuf respLengthBuf = PooledByteBufAllocator.DEFAULT.buffer(4);
        respLengthBuf.writeBytes(d);
        channel.writeAndFlush(respLengthBuf);
    }

    /**
     * 处理接收的文件
     *
     * @param array
     * @param type
     * @param DataLen
     * @param data
     */
    public JSONArray rcvFilesData(String deviceSn, JSONArray array, String type, int DataLen, String data, String Interface) throws ParseException, IOException {
        if (!checkFiles(array, DataLen)) {
            logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 文件长度值和文件list的长度总和不相等，请查看数据！");
            return null;
        }
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            String typePath = KxConstants.IMG_JOB;
            if ("Emd.Msg.Alarm".equals(Interface)) {
                typePath = KxConstants.IMG_ALARM;
            }
            String fileName = KxConstants.IMG_UPLOAD + typePath + "/" + getUploadFilename(deviceSn, type, array.get(i), "");
            String pathName = fileName.substring(0, fileName.lastIndexOf("/"));
            File dir = new File(pathName);
            if (!dir.exists()) {// 判断目录是否存在
                dir.mkdirs();
            }
            FileUtil.hexToFile(data, fileName);
            JSONObject jsonObject = JSONUtil.parseObj(array.get(i));
            //  String filename160 = fileName.substring(0, fileName.lastIndexOf(".")) + "-160" + fileName.substring(fileName.lastIndexOf("."));
            String filename320 = fileName.substring(0, fileName.lastIndexOf(".")) + "-320" + fileName.substring(fileName.lastIndexOf("."));
            String filename640 = fileName.substring(0, fileName.lastIndexOf(".")) + "-640" + fileName.substring(fileName.lastIndexOf("."));
            //Thumbnails.of(new File(fileName)).size(160, 120).toFile(new File(filename160));
            Thumbnails.of(new File(fileName)).size(320, 240).toFile(new File(filename320));
            Thumbnails.of(new File(fileName)).size(640, 480).toFile(new File(filename640));
            jsonObject.putOpt("Uri", fileName.replace(KxConstants.IMG_UPLOAD, ""));
            // jsonObject.putOpt("Uri160", filename160.replace(KxConstants.IMG_UPLOAD, ""));
            jsonObject.putOpt("Uri320", filename320.replace(KxConstants.IMG_UPLOAD, ""));
            jsonObject.putOpt("Uri640", filename640.replace(KxConstants.IMG_UPLOAD, ""));
            newArray.add(jsonObject);
            System.err.println("图片生成完成！========================================================");
        }
        return newArray;
    }

    /**
     * 校验文件描述信息数组的合法性：
     * 1. 所有文件描述信息应为数组类型
     * 2. 所有文件描述信息中文件长度总和是否等于DataLen
     *
     * @param array
     * @param DataLen
     */
    public boolean checkFiles(JSONArray array, int DataLen) {
        try {
            int fileTotalLen = 0;
            for (int i = 0; i < array.size(); i++) {
                JSONObject parseObj = JSONUtil.parseObj(array.get(i));
                Object length = parseObj.get("Length");
                int num = Integer.parseInt(length.toString());
                fileTotalLen += num;
            }
            if (fileTotalLen == DataLen / 2) {
                return true;
            }
            return false;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 拼接文件路径
     *
     * @param deviceSn
     * @param fileType
     * @param item
     * @param extType
     * @return
     */
    public String getUploadFilename(String deviceSn, String fileType, Object item, String extType) throws ParseException {
        String extName = "";
        JSONObject parseObj = JSONUtil.parseObj(item);
        if (parseObj.get("Format") != null) {
            extName = parseObj.get("Format").toString().toLowerCase();
        } else if (parseObj.get("Name") != null) {
            String name = parseObj.get("Name").toString();
            extName = name.substring(name.lastIndexOf(".") + 1);
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (parseObj.get("DateTime") != null) {
            currentTime = formatter.parse(parseObj.get("DateTime").toString());
        } else {
            String format = formatter.format(currentTime);
            currentTime = formatter.parse(format);
        }
        String ret = fileType + "-" + parseObj.get("Format") +
                "/" + deviceSn +
                "/" + DateUtil.year(currentTime) + "-" + DateUtil.month(currentTime) + 1 +
                "/" + new SimpleDateFormat("yyyy-MM-dd").format(currentTime) +
                "/" + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(currentTime);
        if (StringUtil.isNotEmpty(extType)) {
            ret = ret + '-' + extType;
        }
        return ret + "." + extName;
    }

    /**
     * 处理命令响应
     *
     * @param deviceSn
     * @param senderInfo
     * @param msgInfoJsonObject
     * @param channel
     * @throws ParseException
     */
    public void rcvCmmdReply(String deviceSn, JSONObject senderInfo, JSONObject msgInfoJsonObject, SocketChannel channel, String session) throws ParseException {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("处理一般上传数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            // TODO: 2022/3/7  没有通过session 判断
            if ("Pending".equals(msgInfoJsonObject.get("Result"))) {
                return;
            }
            if ("Emd.Service.SysMonitor.E0".equals(senderInfo.get("DestObject"))) {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if ("GetSysBaseInfo".equals(senderInfo.get("Method"))) {
                        deviceDTO.setBaseInfo(String.valueOf(msgInfoJsonObject.get("ResultValue")));
                        kxDeviceService.update(deviceDTO);
                    } else if ("GetSysStat".equals(senderInfo.get("Method"))) {
                        deviceDTO.setStatus(String.valueOf(msgInfoJsonObject.get("ResultValue")));
                        kxDeviceService.update(deviceDTO);
                    }
                }
            } else if ("Emd.Service.VideoSender.E0".equals(senderInfo.get("DestObject"))) {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if ("SendVideoStream".equals(senderInfo.get("Method"))) {
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("deviceSn", deviceSn);
                        msgInfoJsonObject.putOpt("deviceName", deviceDTO.getName());
                        msgInfoJsonObject.putOpt("type", "SendVideoStream");
                        msgInfoJsonObject.putOpt("session", session);
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            } else if ("Emd.Service.Audio.E0".equals(senderInfo.get("DestObject"))) {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if (DeviceInterfaceConstants.METHOD_GETAUDIOLIST.equals(senderInfo.get("Method"))) {
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("deviceSn", deviceSn);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId());
                        msgInfoJsonObject.putOpt("deviceName", deviceDTO.getName());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_GETAUDIOLIST);
                        msgInfoJsonObject.putOpt("session", session);
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            } else if ("Emd.Service.Vpn.E0".equals(senderInfo.get("DestObject"))) {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if (DeviceInterfaceConstants.METHOD_VPN_CONNECT.equals(senderInfo.get("Method"))) {
                        JSONObject resultValue = JSONUtil.parseObj(msgInfoJsonObject.get("ResultValue"));
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_VPN_CONNECT);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId());
                        msgInfoJsonObject.putOpt("Connected", resultValue.get("Connected"));
                        msgInfoJsonObject.putOpt("VpnIP", resultValue.get("VpnIP"));
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "ok");
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    } else if (DeviceInterfaceConstants.METHOD_VPN_GETSTAT.equals(senderInfo.get("Method"))) {
                        JSONObject resultValue = JSONUtil.parseObj(msgInfoJsonObject.get("ResultValue"));
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_VPN_GETSTAT);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId());
                        msgInfoJsonObject.putOpt("Connected", resultValue.get("Connected"));
                        msgInfoJsonObject.putOpt("VpnIP", resultValue.get("VpnIP"));
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "ok");
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    } else if (DeviceInterfaceConstants.METHOD_VPN_CLOSE.equals(senderInfo.get("Method"))) {
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_VPN_CLOSE);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId());
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "ok");
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                } else {
                    if (DeviceInterfaceConstants.METHOD_VPN_CONNECT.equals(senderInfo.get("Method"))) {
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_VPN_CONNECT);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId());
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "fail");
                        msgInfoJsonObject.putOpt("msg", msgInfoJsonObject.get("ErrorMsg"));
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            } else {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if (DeviceInterfaceConstants.METHOD_PTZCONTROL.equals(senderInfo.get("Method"))) {
                        logger.debug("========================");
                        logger.debug(senderInfo.toString());
                        logger.debug(msgInfoJsonObject.toString());
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_PTZCONTROL);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId());
                        msgInfoJsonObject.putOpt("cameraName", senderInfo.get("DestObject"));
                        msgInfoJsonObject.putOpt("session", session);
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("rcvCmmdReply", e);
            e.printStackTrace();
        }
    }

    /**
     * 处理上传data
     *
     * @param senderInfo
     * @param msgInfo
     * @param channel
     */
    public void rcvUploadData(String deviceSn, JSONObject senderInfo, JSONObject msgInfo, SocketChannel channel) throws
            ParseException {
        if (senderInfo.get("SenderObject") == null || msgInfo == null) {
            log.error("错误的上传数据：senderInfo：" + senderInfo + "----msgInfoJson:" + msgInfo);
            return;
        }
        String Interface = String.valueOf(senderInfo.get("Interface"));
        if ("Emd.Msg.Stat".equals(Interface)) {
            updateDeviceStat(deviceSn, senderInfo, msgInfo);
        } else if ("Emd.Msg.Data".equals(Interface)) {
            if ("AccessPicture".equals(senderInfo.get("Signal"))) {
            } else {
                saveData(deviceSn, senderInfo, msgInfo);
            }
        } else if ("Emd.Msg.Alarm".equals(Interface)) {
            saveAlarm(deviceSn, senderInfo, msgInfo);
        }
    }

    /**
     * 保存非报警图片
     *
     * @param deviceSn
     * @param senderInfo
     * @param msgInfo
     */
    public void savePic(String deviceSn, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("计划任务，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            KxScheduleItemDTO itemDTO = new KxScheduleItemDTO();
            itemDTO.setDeviceNo(String.valueOf(senderInfo.get("SerialNo")));
            itemDTO.setSignalType(String.valueOf(senderInfo.get("Signal")));
            itemDTO.setDeviceId(deviceDTO.getId());
            Object taskInfo = msgInfo.get("TaskInfo");
            if (taskInfo !=null) {
                JSONObject jsonObject = JSONUtil.parseObj(taskInfo);
                if (jsonObject.get("ScheduleItemId") != null) {
                    itemDTO.setScheduleJobId((Long)jsonObject.get("ScheduleItemId"));
                }
                if (jsonObject.get("Type") != null) {
                    itemDTO.setItemType(jsonObject.get("Type") + "");
                }
            }
            itemDTO.setDeleted("f");
            itemDTO.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdateTime").toString())) {
                itemDTO.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                itemDTO.setUpdateDate(formatter.parse(String.valueOf(msgInfo.get("UpdateTime"))));
            }
            kxScheduleItemService.save(itemDTO);
            // todo   向总线的告警消息组发送通知信息，其它模块可以获取做后续处理，如通知前端、短信、微信发送等
            //  计划用消息队列，订阅的方式通知其他模块
        } catch (Exception e) {
            logger.error("savePic", e);
            e.printStackTrace();
        }
    }

    /**
     * 更新本地的设备status
     *
     * @param deviceSn
     * @param senderInfo
     * @param msgInfo
     */
    public void updateDeviceStat(String deviceSn, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("处理一般上传数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            JSONObject statusJson = JSONUtil.parseObj(deviceDTO.getStatus());
            String senderObject = String.valueOf(senderInfo.get("SenderObject"));
            if (StringUtil.isNotEmpty(senderObject)) {
                String[] strings = senderObject.split("\\.");
                if (strings.length != 4) {
                    return;
                }
                if (!"StreamSendTask".equals(senderInfo.get("Signal"))) {
                    Object level1 = statusJson.get(strings[1]);
                    if (level1 != null) {
                        JSONObject jsonObject = JSONUtil.parseObj(level1);
                        Object level2 = jsonObject.get(strings[2]);
                        JSONArray array = JSONUtil.parseArray(level2.toString(), false);
                        if (array.size() > 0) {
                            JSONArray newArray = new JSONArray();
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject parseObj = JSONUtil.parseObj(array.get(i));
                                if (senderObject.equals(parseObj.get("Name"))) {
                                    parseObj.putOpt("Name", msgInfo.get("InstanceName"));
                                    parseObj.putOpt("Stat", msgInfo.get("Stat"));
                                }
                                newArray.add(parseObj);
                            }
                            jsonObject.putOpt(strings[2], newArray);
                            statusJson.putOpt(strings[1], jsonObject);
                        }
                    }
                    deviceDTO.setStatus(String.valueOf(statusJson));
                    kxDeviceService.update(deviceDTO);
                }
            }
        } catch (Exception e) {
            logger.error("updateDeviceStat", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存一般上传数据
     *
     * @param deviceSn
     * @param senderInfo
     * @param msgInfo
     */
    public void saveData(String deviceSn, JSONObject senderInfo, JSONObject msgInfo) throws ParseException {
        KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
        if (deviceDTO == null) {
            log.error("处理一般上传数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
            return;
        }
        Object Signal = senderInfo.get("Signal");
        if ("TaskSchedule".equals(Signal)) {
            savePic(deviceSn, senderInfo, msgInfo);
            Object files = msgInfo.get("Files");
            if (files != null) {
                JSONArray jsonArray = JSONUtil.parseArray(files.toString());
                JSONObject json = jsonArray.getJSONObject(0);
                kxDeviceService.analysisImg(json, deviceDTO.getId());
            }
        } else if ("GasTransmitter".equals(Signal)) {
            saveGasData(deviceDTO, senderInfo, msgInfo);
        } else if ("BatteryState".equals(Signal)) {
            saveBatteryStateData(deviceDTO, senderInfo, msgInfo);
        } else if ("NetWorkState".equals(Signal)) {
            saveNetWorkStateData(deviceDTO, senderInfo, msgInfo);
        } /*else if ("".equals(Signal)) {
            saveAccelerationData(deviceDTO, senderInfo, msgInfo);
        } */ else if ("GPSsensors".equals(Signal)) {
            saveGpsData(deviceDTO, senderInfo, msgInfo);
        } else if ("Humiditysensors".equals(Signal)) {
            saveHumidityData(deviceDTO, senderInfo, msgInfo);
        } else if ("Inclinationsensors".equals(Signal)) {
            saveInclinationData(deviceDTO, senderInfo, msgInfo);
        } else if ("Tempsensors".equals(Signal)) {
            saveTemperatureData(deviceDTO, senderInfo, msgInfo);
        }
    }

    /**
     * 保存电池状态
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveBatteryStateData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) throws
            ParseException {
        try {
            KxBatteryDTO dto = new KxBatteryDTO();
            dto.setDeviceId(deviceDTO.getId());
            dto.setStationId(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            //dto.setCreateDate((Date) senderInfo.get("CreatedTime"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdataTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdataTime").toString())) {
                dto.setCreateDate(formatter.parse(formatter.format(new Date())));
            } else {
                dto.setCreateDate(formatter.parse(String.valueOf(msgInfo.get("CreatedTime"))));
            }
            dto.setBatSoc(new BigDecimal(msgInfo.get("BatSoc").toString()));
            dto.setBatteryId(Long.valueOf(msgInfo.get("BatteryId").toString()));
            dto.setChargeSwitch(msgInfo.get("ChargeSwitch").toString());
            dto.setChargeVotage(new BigDecimal(msgInfo.get("ChargeVotage").toString()));
            dto.setVoltageLevel(new BigDecimal(msgInfo.get("VoltageLevel").toString()));
            dto.setOutputVotage(new BigDecimal(msgInfo.get("OutputVoltage").toString()));
            kxBatteryService.save(dto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 保存网络状态数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveNetWorkStateData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxNetWorkStateDataDTO dto = new KxNetWorkStateDataDTO();
            dto.setDeviceid(deviceDTO.getId());
            dto.setStationid(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            // TODO: 2022/3/1  拆分数据
            kxNetWorkStateDataService.save(dto);
        } catch (Exception e) {
            logger.error("saveNetWorkStateData", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存Gps数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveGpsData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceGpsDTO dto = new KxDeviceGpsDTO();
            dto.setDeviceId(deviceDTO.getId());
            dto.setStationId(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("CreatedTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("CreatedTime").toString())) {
                dto.setCreateDate(formatter.parse(formatter.format(new Date())));
            } else {
                dto.setCreateDate(formatter.parse(String.valueOf(msgInfo.get("CreatedTime"))));
            }
            String lat = msgInfo.get("Latitude").toString();
            String lng = msgInfo.get("Longitude").toString();
            lat = CoordinateTransform.transferGps(lat);
            lng = CoordinateTransform.transferGps(lng);
            dto.setLongitude(lng);
            dto.setLatitude(lat);
            dto.setHdop(msgInfo.get("Hdop").toString());
            dto.setAltitude(msgInfo.get("Altitude").toString());
            dto.setEastWest(msgInfo.get("East/West").toString());
            dto.setNorthSouth(msgInfo.get("North/South").toString());
            dto.setSensorNo(Long.valueOf(msgInfo.get("SensorId").toString()));
            kxDeviceGpsService.save(dto);
            // 保存gps轨迹路径
            kxStationTrackService.add(dto, deviceDTO);
        } catch (Exception e) {
            logger.error("saveGpsData", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存湿度数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveHumidityData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceHumidityDTO dto = new KxDeviceHumidityDTO();
            dto.setDeviceId(deviceDTO.getId());
            dto.setStationId(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdateTime").toString())) {
                dto.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                dto.setUpdateDate(formatter.parse(String.valueOf(msgInfo.get("UpdateTime"))));
            }
            // TODO: 2022/3/1  拆分数据
            dto.setUnit(String.valueOf(msgInfo.get("Unit")));
            dto.setCurrentValue(new Double(String.valueOf(msgInfo.get("CurrentValue"))));
            dto.setSensorNo(Long.valueOf(String.valueOf(msgInfo.get("SensorId"))));
            dto.setAlarmStatus(String.valueOf(msgInfo.get("AlarmStatus")));
            dto.setFirsLevelAlarm(new Double(String.valueOf(msgInfo.get("FirstLevelAlarm"))));
            dto.setSecondaryLevelAlarm(new Double(String.valueOf(msgInfo.get("SecondaryLevelAlarm"))));
            kxDeviceHumidityService.save(dto);
            String alarmStatus = dto.getAlarmStatus();
            if (!"Normal".equals(alarmStatus)) {
                String title = "您有新的湿度数据预警通知，请及时查看！";
                String type = "湿度预警";
                String deviceName = dto.getDeviceName();
                String deviceCode = deviceDTO.getSerialNo();
                List<SysDictDataEntity> list = sysDictDataService.getListByDictName("kx_data_status");
                if (list.size() > 0) {
                    for (SysDictDataEntity data : list) {
                        alarmStatus.equals(data.getDictValue());
                        alarmStatus = data.getDictLabel();
                        break;
                    }
                }
                sendNoticeData(title, type, deviceName, deviceCode, alarmStatus, dto.getCurrentValue() + dto.getUnit(), formatter.format(dto.getUpdateDate()));
                KxNewUploadDataDTO dataDTO = new KxNewUploadDataDTO();
                dataDTO.setDeviceId(deviceDTO.getId());
                dataDTO.setStationId(deviceDTO.getStationId());
                dataDTO.setCreateDate(dto.getCreateDate());
                dataDTO.setType(type);
                kxNewUploadDataService.deleleByDeviceId(dataDTO.getDeviceId());
                kxNewUploadDataService.save(dataDTO);
                //发送统计页面刷新指令
                sendLiveRefresh();
            }
        } catch (Exception e) {
            logger.error("saveHumidityData", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存设备倾斜数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveInclinationData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceInclinationDTO dto = new KxDeviceInclinationDTO();
            dto.setDeviceId(deviceDTO.getId());
            dto.setStationId(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdateTime").toString())) {
                dto.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                dto.setUpdateDate(formatter.parse(String.valueOf(msgInfo.get("UpdateTime"))));
            }
            // TODO: 2022/3/1  拆分数据
            dto.setUnit(String.valueOf(msgInfo.get("Unit")));
            dto.setCurrentValue(new Double(String.valueOf(msgInfo.get("CurrentValue"))));
            dto.setSensorNo(Long.valueOf(String.valueOf(msgInfo.get("SensorId"))));
            dto.setAlarmStatus(String.valueOf(msgInfo.get("AlarmStatus")));
            dto.setFirsLevelAlarm(new Double(String.valueOf(msgInfo.get("FirstLevelAlarm"))));
            dto.setSecondaryLevelAlarm(new Double(String.valueOf(msgInfo.get("SecondaryLevelAlarm"))));
            kxDeviceInclinationService.save(dto);
            String alarmStatus = dto.getAlarmStatus();
            if (!"Normal".equals(alarmStatus)) {
                String title = "您有新的设备倾斜预警通知，请及时查看！";
                String type = "倾斜预警";
                String deviceName = dto.getDeviceName();
                String deviceCode = deviceDTO.getSerialNo();
                List<SysDictDataEntity> list = sysDictDataService.getListByDictName("kx_data_status");
                if (list.size() > 0) {
                    for (SysDictDataEntity data : list) {
                        alarmStatus.equals(data.getDictValue());
                        alarmStatus = data.getDictLabel();
                        break;
                    }
                }
                sendNoticeData(title, type, deviceName, deviceCode, alarmStatus, dto.getCurrentValue() + dto.getUnit(), formatter.format(dto.getUpdateDate()));
                KxNewUploadDataDTO dataDTO = new KxNewUploadDataDTO();
                dataDTO.setDeviceId(deviceDTO.getId());
                dataDTO.setStationId(deviceDTO.getStationId());
                dataDTO.setCreateDate(dto.getCreateDate());
                dataDTO.setType(type);
                kxNewUploadDataService.deleleByDeviceId(dataDTO.getDeviceId());
                kxNewUploadDataService.save(dataDTO);
                sendLiveRefresh();
            }
        } catch (Exception e) {
            logger.error("saveInclinationData", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存温度数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveTemperatureData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceTemperatureDTO dto = new KxDeviceTemperatureDTO();
            dto.setDeviceId(deviceDTO.getId());
            dto.setStationId(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdateTime").toString())) {
                dto.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                dto.setUpdateDate(formatter.parse(String.valueOf(msgInfo.get("UpdateTime"))));
            }
            // TODO: 2022/3/1  拆分数据
            dto.setUnit(String.valueOf(msgInfo.get("Unit")));
            dto.setCurrentValue(new Double(String.valueOf(msgInfo.get("CurrentValue"))));
            dto.setSensorNo(Long.valueOf(String.valueOf(msgInfo.get("SensorId"))));
            dto.setAlarmStatus(String.valueOf(msgInfo.get("AlarmStatus")));
            dto.setFirsLevelAlarm(new Double(String.valueOf(msgInfo.get("FirstLevelAlarm"))));
            dto.setSecondaryLevelAlarm(new Double(String.valueOf(msgInfo.get("SecondaryLevelAlarm"))));
            kxDeviceTemperatureService.save(dto);
            String alarmStatus = dto.getAlarmStatus();
            if (!"Normal".equals(alarmStatus)) {
                String title = "您有新的温度数据预警通知，请及时查看！";
                String type = "温度预警";
                String deviceName = dto.getDeviceName();
                String deviceCode = deviceDTO.getSerialNo();
                List<SysDictDataEntity> list = sysDictDataService.getListByDictName("kx_data_status");
                if (list.size() > 0) {
                    for (SysDictDataEntity data : list) {
                        alarmStatus.equals(data.getDictValue());
                        alarmStatus = data.getDictLabel();
                        break;
                    }
                }
                sendNoticeData(title, type, deviceName, deviceCode, alarmStatus, dto.getCurrentValue() + dto.getUnit(), formatter.format(dto.getUpdateDate()));
                KxNewUploadDataDTO dataDTO = new KxNewUploadDataDTO();
                dataDTO.setDeviceId(deviceDTO.getId());
                dataDTO.setStationId(deviceDTO.getStationId());
                dataDTO.setCreateDate(dto.getCreateDate());
                dataDTO.setType(type);
                kxNewUploadDataService.deleleByDeviceId(dataDTO.getDeviceId());
                kxNewUploadDataService.save(dataDTO);
                sendLiveRefresh();
            }
        } catch (Exception e) {
            logger.error("saveTemperatureData", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存天然气数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    public void saveGasData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) throws ParseException {
        try {
            KxGasDataDTO KxGasDataDTO = new KxGasDataDTO();
            KxGasDataDTO.setDeviceId(deviceDTO.getId());
            KxGasDataDTO.setStationId(deviceDTO.getStationId());
            KxGasDataDTO.setContent(String.valueOf(msgInfo));
            KxGasDataDTO.setUnit(String.valueOf(msgInfo.get("Unit")));
            KxGasDataDTO.setGasType(String.valueOf(msgInfo.get("GasType")));
            KxGasDataDTO.setSensorId(Long.valueOf(String.valueOf(msgInfo.get("SensorId"))));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (msgInfo.get("UpdateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("UpdateTime").toString())) {
                KxGasDataDTO.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                KxGasDataDTO.setUpdateDate(formatter.parse(String.valueOf(msgInfo.get("UpdateTime"))));
            }
            KxGasDataDTO.setAlarmStatus(String.valueOf(msgInfo.get("AlarmStatus")));
            KxGasDataDTO.setFirsLevelAlarm(new Double(String.valueOf(msgInfo.get("FirstLevelAlarm"))));
            KxGasDataDTO.setConcentrationValue(new Double(String.valueOf(msgInfo.get("ConcentrationValue"))));
            KxGasDataDTO.setSecondaryLevelAlarm(new Double(String.valueOf(msgInfo.get("SecondaryLevelAlarm"))));
            KxGasDataDTO.setTenantCode(deviceDTO.getTenantCode());
            KxGasDataDTO.setCreator(deviceDTO.getCreator());
            KxGasDataDTO.setUpdater(deviceDTO.getUpdater());
            KxGasDataService.save(KxGasDataDTO);
            String alarmStatus = KxGasDataDTO.getAlarmStatus();
            if (!"Normal".equals(alarmStatus)) {
                String title = "您有新的燃气数据预警通知，请及时查看！";
                String type = "燃气预警";
                String deviceName = KxGasDataDTO.getDeviceName();
                String deviceCode = deviceDTO.getSerialNo();
                List<SysDictDataEntity> list = sysDictDataService.getListByDictName("kx_data_status");
                if (list.size() > 0) {
                    for (SysDictDataEntity data : list) {
                        alarmStatus.equals(data.getDictValue());
                        alarmStatus = data.getDictLabel();
                        break;
                    }
                }
                sendNoticeData(title, type, deviceName, deviceCode, alarmStatus, KxGasDataDTO.getConcentrationValue() + KxGasDataDTO.getUnit(), formatter.format(KxGasDataDTO.getUpdateDate()));
                // 保存最新数据
                KxNewUploadDataDTO dto = new KxNewUploadDataDTO();
                dto.setDeviceId(deviceDTO.getId());
                dto.setStationId(deviceDTO.getStationId());
                dto.setCreateDate(KxGasDataDTO.getCreateDate());
                dto.setType(type);
                kxNewUploadDataService.deleleByDeviceId(dto.getDeviceId());
                kxNewUploadDataService.save(dto);
            }
        } catch (Exception e) {
            logger.error("saveGasData", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存告警数据
     *
     * @param deviceSn
     * @param senderInfo
     * @param msgInfo
     */
    public void saveAlarm(String deviceSn, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxDeviceDTO deviceDTO = kxDeviceService.getBySerialNo(deviceSn);
            if (deviceDTO == null) {
                log.error("处理告警数据，未找到对应数据，丢弃数据，设备编号:" + deviceSn);
                return;
            }
            KxDeviceAlarmDTO alarmDTO = new KxDeviceAlarmDTO();
            alarmDTO.setDeviceNo(String.valueOf(senderInfo.get("SerialNo")));
            alarmDTO.setSignalType(String.valueOf(senderInfo.get("Signal")));
            alarmDTO.setLevel((Integer) msgInfo.get("Level"));
            alarmDTO.setDeviceId(deviceDTO.getId());
            alarmDTO.setStationId(deviceDTO.getStationId());
            alarmDTO.setSenderInfo(String.valueOf(senderInfo));
            alarmDTO.setContent(String.valueOf(msgInfo));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Object files = msgInfo.get("Files");
            if (files != null) {
                JSONArray jsonArray = JSONUtil.parseArray(files.toString());
                JSONObject json = jsonArray.getJSONObject(0);
                if (msgInfo.get("DateTime") == null || "NULL".equalsIgnoreCase(msgInfo.get("DateTime").toString())) {
                    alarmDTO.setPictureDate(formatter.parse(formatter.format(new Date())));
                } else {
                    alarmDTO.setPictureDate(formatter.parse(String.valueOf(json.get("DateTime"))));
                }
            }
            if (senderInfo.get("CreatedTime") == null) {
                alarmDTO.setUpdateDate(formatter.parse(formatter.format(new Date())));
            } else {
                alarmDTO.setUpdateDate(formatter.parse(String.valueOf(senderInfo.get("CreatedTime"))));
            }
            kxDeviceAlarmService.save(alarmDTO);
            // todo   向总线的告警消息组发送通知信息，其它模块可以获取做后续处理，如通知前端、短信、微信发送等
            //  计划用消息队列，订阅的方式通知其他模块
            String title = "您有新的预警图片通知，请及时处理！";
            String type = "图片预警";
            String deviceName = deviceDTO.getName();
            String deviceCode = deviceDTO.getSerialNo();
            sendNoticeData(title, type, deviceName, deviceCode, "", "", formatter.format(alarmDTO.getUpdateDate()));
            KxNewUploadDataDTO dataDTO = new KxNewUploadDataDTO();
            dataDTO.setDeviceId(deviceDTO.getId());
            dataDTO.setStationId(deviceDTO.getStationId());
            dataDTO.setCreateDate(alarmDTO.getCreateDate());
            dataDTO.setType(type);
            kxNewUploadDataService.deleleByDeviceId(dataDTO.getDeviceId());
            kxNewUploadDataService.save(dataDTO);
            sendLiveRefresh();
        } catch (Exception e) {
            logger.error("saveAlarm", e);
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * 将预警信息，发送到公告消息
     *
     * @param
     */
    public void sendNoticeData(String title, String type, String deviceName, String deviceCode, String level, String currentValue, String dateTime) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("<p><strong style=\"color: rgb(136, 136, 136);\">");
            sb.append("预警类型：");
            sb.append("</strong>");
            sb.append(type);
            sb.append("</p>");
            sb.append("<p>");
            sb.append("<strong style=\"color: rgb(136, 136, 136);\">");
            sb.append("设备名称：");
            sb.append("</strong>");
            sb.append(deviceName);
            sb.append("</p>");
            sb.append("<p>");
            sb.append("<strong style=\"color: rgb(136, 136, 136);\">");
            sb.append("设备编号：");
            sb.append("</strong>");
            sb.append(deviceCode);
            sb.append("</p>");
            if (!"图片预警".equals(type)) {
                sb.append("<p><strong style=\"color: rgb(136, 136, 136);\">预警等级：</strong>");
                sb.append(level);
                sb.append("</p>");
                sb.append("<p><strong style=\"color: rgb(136, 136, 136);\">当前数值：</strong>");
                sb.append(currentValue);
                sb.append("</p>");
            }
            sb.append("<p>");
            sb.append("<strong style=\"color: rgb(136, 136, 136);\">");
            sb.append("预警时间：");
            sb.append("</strong>");
            sb.append(dateTime);
            sb.append("</p>");
            SysNoticeDTO dto = new SysNoticeDTO();
            dto.setType(0);
            dto.setTitle(title);
            dto.setContent(sb.toString());
            dto.setReceiverType(0);
            dto.setReceiverTypeList(new ArrayList<>());
            dto.setStatus(1);
            dto.setCreator(0L);
            sysNoticeService.save(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通道
     *
     * @param key
     * @return
     */
    public Channel getChannel(String key) {
        return map.get(key);
    }

    public void remove(String key) {
        log.info("删除缓存连接：" + key + "--");
        map.remove(key);
        //断开服务器
        String diviceId = ServerMap.get(key);
        if (StringUtil.isNotEmpty(diviceId)) {
            try {
                KxDeviceDTO dto = kxDeviceService.getBySerialNo(diviceId);
                if (dto != null) {
                    dto.setEnable("f");
                    kxDeviceService.update(dto);
                }
            } catch (Exception e) {
                logger.error("remove", e);
                e.printStackTrace();
            }
        }
        removeServer(key);
    }

    /**
     * 统计界面数据刷新
     */
    public void sendLiveRefresh() {
        /*try {
            MessageData<Object> message = new MessageData<>();
            message.setType(1);
            message.setData(new Date());
            System.err.println("统计界面数据刷新");
            log.info("统计界面数据刷新" + DateUtil2.getCurrentDateTime());
            webSocketLiveServer.sendMessageAll(message);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void add(String key, Channel channel) {
        log.info("添加缓存连接：" + key + "--" + DateUtil2.getCurrentDateTime());
        map.put(key, channel);
        sendLiveRefresh();
    }

    public void removeServer(String key) {
        log.info("删除缓存连接：" + key + "--" + DateUtil2.getCurrentDateTime());
        ServerMap.remove(key);
        printNettyLog();
        sendLiveRefresh();
    }

    public void addServer(String key, String value) {
        log.info("添加缓存连接：" + key + "--" + DateUtil2.getCurrentDateTime());
        ServerMap.put(key, value);
        printNettyLog();
    }

    private void printNettyLog() {
        StringBuffer sb = new StringBuffer("网关对应通道情况：");
        for (Map.Entry<String, String> entry : ServerMap.entrySet()) {
            sb.append("/n网关代码：" + entry.getValue());
            sb.append("--通道ID：").append(entry.getKey());
        }
        log.debug(DateUtil2.getCurrentDateTime() + sb.toString());
    }

    //根据map的value获取map的key
    private String getServer(String value) {
        String key = "";
        for (Map.Entry<String, String> entry : ServerMap.entrySet()) {
            if (value.equals(entry.getValue())) {
                key = entry.getKey();
            }
        }
        return key;
    }
}
