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
                log.error("开启vpn====>未查到相关设备信息");
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
            log.error("openVpn", e);
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
        // TODO: 2022/10/20  
    }
}
