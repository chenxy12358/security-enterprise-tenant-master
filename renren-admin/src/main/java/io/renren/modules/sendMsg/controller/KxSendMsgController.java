package io.renren.modules.sendMsg.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.common.netty.NettyService;
import io.renren.common.utils.Result;
import io.renren.common.utils.StringUtil;
import io.renren.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 接收前端消息
 *
 * @since 3.0 2022-02-25
 */
@RestController
@RequestMapping("sendMsg")
@Api(tags = "接收前端发送指令")
public class KxSendMsgController {
    @Autowired
    private NettyService nettyService;
    @Autowired
    private WebSocketServer webSocketServerVideo;

    @PostMapping("sendCmdCamera")
    @ApiOperation("发送相机命令给设备端")
    public Result sendCmdCamera(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        if (StringUtil.isNotEmpty(String.valueOf(params.get("cameraName"))) && StringUtil.isNotEmpty(String.valueOf(params.get("deviceSn")))) {
            nettyService.sendCmdCamera(params.get("deviceSn").toString(), params.get("cameraName").toString(), Long.valueOf(params.get("currentTime").toString()));
            params.putOpt("command", "GetPresetsDetail");
            sendCmdPtzControl(params);
        }
        System.err.println("发送相机命令给设备端");
        return new Result();
    }


    @PostMapping("keepStream")
    @ApiOperation("发送保持推送命令")
    public Result keepStream(@RequestBody Object object) {
        JSONArray array = new JSONArray(object);
        for (int i = 0; i < array.size(); i++) {
            Object o = array.get(i);
            JSONObject jsonObject = JSONUtil.parseObj(o);
            Object deviceSn = jsonObject.get("deviceSn");
            Object taskId = jsonObject.get("taskId");
            if (deviceSn != null && taskId != null) {
                System.err.println("发送保持推送命令deviceSn" + deviceSn + "taskId" + taskId);
                nettyService.keepStream(deviceSn.toString(), Integer.parseInt(taskId.toString()));
            }
        }
        System.err.println("发送保持推送命令");
        return new Result();
    }


    @PostMapping("sendCmdPtzControl")
    @ApiOperation("发送指令")
    public Result sendCmdPtzControl(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.sendCmdPtzControl(params);
        return new Result();
    }



    @PostMapping("sendTakePicture")
    @ApiOperation("抓取图片")
    public Result sendTakePicture(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.sendTakePicture(params);
        return new Result();
    }




    @PostMapping("getPresetList")
    @ApiOperation("获取已设置的所有预置点编号")
    public Result getPresetList(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.getPresetList(params);
        return new Result();
    }


    @PostMapping("getAudioList")
    @ApiOperation("获取音频列表")
    public Result getAudioList(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.getAudioList(params);
        return new Result();
    }


    @PostMapping("sendJobInfo")
    @ApiOperation("发送计划任务到设备端")
    public Result sendJobInfo(@RequestParam Long deviceId) {
        nettyService.sendJobInfo(deviceId);
        return new Result();
    }


    @PostMapping("sendAIConfig")
    @ApiOperation("发送ai配置到设备端")
    public Result sendAIConfig(@RequestParam Long deviceId) {
        nettyService.sendAIConfig(deviceId);
        return new Result();
    }


    @PostMapping("getVPNStat")
    @ApiOperation("获取vpn状态")
    public Result getVPNStat(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.getVPNStat(params);
        return new Result();
    }


    @PostMapping("openVpn")
    @ApiOperation("开启vpn")
    public Result openVpn(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.openVpn(params);
        System.err.println("发送命令：开启vpn");
        return new Result();
    }

    @PostMapping("closeVpn")
    @ApiOperation("关闭vpn")
    public Result closeVpn(@RequestBody Object object) {
        JSONObject params = JSONUtil.parseObj(object);
        nettyService.closeVpn(params);
        System.err.println("发送命令：关闭vpn");
        return new Result();
    }

}