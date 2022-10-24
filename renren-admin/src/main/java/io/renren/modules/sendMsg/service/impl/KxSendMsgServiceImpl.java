package io.renren.modules.sendMsg.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.netty.channel.Channel;
import io.renren.common.exception.RenException;
import io.renren.common.netty.NettyService;
import io.renren.common.utils.SendMsgUtils;
import io.renren.common.utils.StringUtil;
import io.renren.modules.common.constant.DeviceInterfaceConstants;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.sendMsg.service.KxSendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cxy
 * @create 2022/10/20
 */
@Service
@Slf4j
public class KxSendMsgServiceImpl implements KxSendMsgService {
    @Autowired
    private KxDeviceService kxDeviceService;
    @Autowired
    private NettyService nettyService;

    @Override
    public void getAudioStat(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                log.error("获取喇叭状态====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = nettyService.getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = nettyService.getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.Audio.E0");
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_GETPARAM);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                nettyService.printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getAudioStat", e);
        }

    }

    @Override
    public void switchAudio(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                log.error("开关喇叭====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = nettyService.getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = nettyService.getChannel(key);

                JSONArray jsonArray = params.getJSONArray("Speaker");

                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", "Emd.Service.Audio.E0");
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_SETPARAM);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);

                JSONArray paramArray = new JSONArray();
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject jSONObject = jsonArray.getJSONObject(j);
                    jSONObject.putOpt("Enable", params.getBool("Enable"));
                    paramArray.add(jSONObject);
                }
                JSONObject param = new JSONObject();
                param.putOpt("Speaker",paramArray);
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                nettyService.printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("switchAudio", e);
        }

    }

    @Override
    public void upgradeDevice(JSONObject params) {
        // TODO: 2022/10/20  
    }

    @Override
    public void downloadSystem(JSONObject params) {
        // TODO: 2022/10/20  
    }

    @Override
    public void setOsdInfo(JSONObject params) {
        try {
            KxDeviceDTO dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            if (dto == null) {
                log.error("设置osd====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = nettyService.getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = nettyService.getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", params.get("cameraName"));
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_PITCHOSD);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);

                //ICCID
                JSONObject iccid_json = new JSONObject();
                JSONObject iccid_position = new JSONObject();
                iccid_position.putOpt("x", 0.70);
                iccid_position.putOpt("y", 0.95);
                iccid_json.putOpt("Enable", true);
                iccid_json.putOpt("FontSize", 24);
                iccid_json.putOpt("PlainText", params.get("ICCID"));
                iccid_json.putOpt("Position", iccid_position);
                JSONObject param = new JSONObject();
                param.putOpt("ICCID",iccid_json);
                param.putOpt("_session", params.get("currentTime").toString());

                //channel
                JSONObject json = new JSONObject();
                JSONObject position = new JSONObject();
                position.putOpt("x", 0.01);
                position.putOpt("y", 0.95);
                json.putOpt("Enable", true);
                json.putOpt("FontSize", 24);
                json.putOpt("PlainText", params.get("channel"));
                json.putOpt("Position", position);
                JSONObject paramC = new JSONObject();
                param.putOpt("Channel",json);
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), paramC.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                nettyService.printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("setOsdInfo", e);
        }
    }

    @Override
    public void getOsdInfo(JSONObject params) {

        try {
            KxDeviceDTO dto = kxDeviceService.getBySerialNo(String.valueOf(params.get("deviceSn")));
            if (null != params.get("deviceId")) {
                dto = kxDeviceService.get(Long.valueOf(String.valueOf(params.get("deviceId"))));
            }
            if (dto == null) {
                log.error("获取osd信息====>未查到相关设备信息");
                return;
            }
            //获取通讯通道
            String key = nettyService.getServer(dto.getSerialNo());
            if (StringUtil.isNotEmpty(key)) {
                Channel channel = nettyService.getChannel(key);
                JSONObject destInfo = new JSONObject();
                destInfo.putOpt("DestObject", params.get("cameraName"));
                destInfo.putOpt("Method", DeviceInterfaceConstants.METHOD_FETCHOSD);
                destInfo.putOpt("Interface", DeviceInterfaceConstants.INTERFACE_NORMAL);
                JSONObject param = new JSONObject();
                param.putOpt("_session", params.get("currentTime").toString());
                //发送指令
                SendMsgUtils.sendMsg(dto.getSerialNo(), destInfo.toString(), param.toString(), channel);
            } else {
                log.error("无相应的通讯通道");
                nettyService.printNettyLog();
                throw new RenException("通道-设备数据异常，请检查设备!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getOsdInfo", e);
        }

    }
}
