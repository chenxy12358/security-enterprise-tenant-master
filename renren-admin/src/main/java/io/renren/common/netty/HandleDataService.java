package io.renren.common.netty;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.common.utils.StringUtil;
import io.renren.modules.common.constant.DeviceInterfaceConstants;
import io.renren.modules.common.constant.KxAiBoundary;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.discernBoundary.service.KxDiscernBoundaryService;
import io.renren.websocket.WebSocketServer;
import io.renren.websocket.data.MessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

/**
 * @author cxy
 * @create 2022/10/20
 */

@Service("handleDataService")
@Slf4j
public class HandleDataService {
    @Autowired
    private KxDeviceService kxDeviceService;
    @Autowired
    private KxDiscernBoundaryService kxDiscernBoundaryService;
    @Autowired
    private WebSocketServer webSocketServer;


    /**
     * 处理命令响应
     * @param deviceSn
     * @param senderInfo
     * @param msgInfoJsonObject
     * @throws ParseException
     */
    public void rcvCmmdReply(String deviceSn, JSONObject senderInfo, JSONObject msgInfoJsonObject, String session) throws ParseException {
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

            // 更新标记框发送状态   Msg:{"Code":6145,"Result":"Ok"} or {"ErrorMsg":"Param Error","Result":"Failed"}
            String method = senderInfo.getStr("Method");
            String destObject = senderInfo.getStr("DestObject");
            if ("Emd.Service.DLDetect.E0".equals(destObject)) {
                if (DeviceInterfaceConstants.METHOD_SETDETECTAREAS.equals(method)) {
                    if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                        if (StringUtil.isNotEmpty(session) && session.contains(KxAiBoundary.PRESET_SEND)) { //如果是发送预置位命令
                            kxDiscernBoundaryService.updatePresetPicInfo(deviceSn, msgInfoJsonObject, session);
                        }
                    }
                }
            }
            // 更新标记框发送状态  end 2022年8月1日16:04:23

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
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("type", "SendVideoStream");
                        msgInfoJsonObject.putOpt("session", session);
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            }
            else if ("Emd.Service.Vpn.E0".equals(senderInfo.get("DestObject"))) {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if (DeviceInterfaceConstants.METHOD_VPN_CONNECT.equals(senderInfo.get("Method"))) {
                        JSONObject resultValue = JSONUtil.parseObj(msgInfoJsonObject.get("ResultValue"));
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_VPN_CONNECT);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
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
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
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
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
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
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "fail");
                        msgInfoJsonObject.putOpt("msg", msgInfoJsonObject.get("ErrorMsg"));
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            }
            // 喇叭开关、报警音频铃声功能 返回数据
            else if ("Emd.Service.Audio.E0".equals(senderInfo.get("DestObject"))) {
                MessageData<Object> message = new MessageData<>();
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if (DeviceInterfaceConstants.METHOD_GETPARAM.equals(senderInfo.get("Method"))) {
                        JSONObject resultValue = JSONUtil.parseObj(msgInfoJsonObject.get("ResultValue"));
                        JSONArray jsonArray = resultValue.getJSONArray("Speaker");
                        boolean enable = false;
                        if (null != jsonArray && !jsonArray.isEmpty()) {
                            JSONObject json= jsonArray.getJSONObject(0);
                            enable=json.getBool("Enable");
                        }
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_GETPARAM);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("Enable", enable);
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "ok");
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    } else if (DeviceInterfaceConstants.METHOD_SETPARAM.equals(senderInfo.get("Method"))) {
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_SETPARAM);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "ok");
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }else if (DeviceInterfaceConstants.METHOD_GETAUDIOLIST.equals(senderInfo.get("Method"))) {
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("deviceSn", deviceSn);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("deviceName", deviceDTO.getName());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_GETAUDIOLIST);
                        msgInfoJsonObject.putOpt("session", session);
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                } else {
                    if (DeviceInterfaceConstants.METHOD_GETPARAM.equals(senderInfo.get("Method"))) {
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_GETPARAM);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "fail");
                        msgInfoJsonObject.putOpt("msg", "获取喇叭开关状态失败，"+msgInfoJsonObject.get("ErrorMsg"));
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                    else if (DeviceInterfaceConstants.METHOD_SETPARAM.equals(senderInfo.get("Method"))) {
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_SETPARAM);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("session", session);
                        msgInfoJsonObject.putOpt("result", "fail");
                        msgInfoJsonObject.putOpt("msg", "设置喇叭开关状态失败，"+msgInfoJsonObject.get("ErrorMsg"));
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }
                }
            }
            else {
                if ("Ok".equals(msgInfoJsonObject.get("Result"))) {
                    if (DeviceInterfaceConstants.METHOD_PTZCONTROL.equals(senderInfo.get("Method"))) {
                        log.debug("========================");
                        log.debug(senderInfo.toString());
                        log.debug(msgInfoJsonObject.toString());
                        MessageData<Object> message = new MessageData<>();
                        message.setType(1);
                        msgInfoJsonObject.putOpt("time", new Date());
                        msgInfoJsonObject.putOpt("type", DeviceInterfaceConstants.METHOD_PTZCONTROL);
                        msgInfoJsonObject.putOpt("deviceID", deviceDTO.getId()+"");
                        msgInfoJsonObject.putOpt("cameraName", senderInfo.get("DestObject"));
                        msgInfoJsonObject.putOpt("session", session);
                        message.setData(msgInfoJsonObject);
                        webSocketServer.sendMessageAll(message);
                    }

                    // osd 返回数据
                    if (senderInfo.get("DestObject").toString().contains("Emd.Device.Camera")) {

                        JSONObject resultValue = JSONUtil.parseObj(msgInfoJsonObject.get("ResultValue"));
                        MessageData<Object> message = new MessageData<>();
                        if (DeviceInterfaceConstants.METHOD_FETCHOSD.equals(senderInfo.get("Method"))) {  //获取OSD信息
                            message.setType(1);
                            msgInfoJsonObject.putOpt("session", session);
                            msgInfoJsonObject.putOpt("Channel", resultValue.get("Channel"));
                            msgInfoJsonObject.putOpt("ICCID", resultValue.get("ICCID"));
                            msgInfoJsonObject.putOpt("DateAndTime", resultValue.get("DateAndTime"));
                            message.setData(msgInfoJsonObject);
                            webSocketServer.sendMessageAll(message);
                        } else if (DeviceInterfaceConstants.METHOD_PITCHOSD.equals(senderInfo.get("Method"))) {//设置OSD信息
                            message.setType(1);
                            message.setData(msgInfoJsonObject);
                            msgInfoJsonObject.putOpt("session", session);
                            webSocketServer.sendMessageAll(message);
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("rcvCmmdReply", e);
            e.printStackTrace();
        }
    }

}
