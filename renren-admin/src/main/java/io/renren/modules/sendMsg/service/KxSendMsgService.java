package io.renren.modules.sendMsg.service;

import cn.hutool.json.JSONObject;

/**
 * @author cxy
 * @create 2022/10/20
 */
public interface KxSendMsgService {

    /**
     * 获取喇叭声音状态
     * @param params
     */
    void getAudioStat(JSONObject params);

    /**
     * 开关喇叭声音
     * @param params
     */
    void switchAudio(JSONObject params);
}
