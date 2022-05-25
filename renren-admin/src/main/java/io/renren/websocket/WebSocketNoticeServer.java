/**
 * Copyright (c) 2016-2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.websocket;

import com.alibaba.fastjson.JSON;
import io.renren.common.constant.Constant;
import io.renren.modules.notice.entity.SysNoticeSwitchEntity;
import io.renren.modules.notice.service.SysNoticeSwitchService;
import io.renren.websocket.config.WebSocketConfig;
import io.renren.websocket.data.MessageData;
import io.renren.websocket.data.WebSocketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocketNotice
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocketNotice", configurator = WebSocketConfig.class)
public class WebSocketNoticeServer {
    /**
     * 客户端连接信息
     */
    private static Map<String, WebSocketData> servers = new ConcurrentHashMap<>();
    @Autowired
    private SysNoticeSwitchService sysNoticeSwitchService;


    @OnOpen
    public void open(Session session) {
        Long userId = (Long) session.getUserProperties().get(Constant.USER_KEY);

        servers.put(session.getId(), new WebSocketData(userId, session));
    }

    @OnClose
    public void onClose(Session session) {
        //客户端断开连接
        servers.remove(session.getId());
        log.debug("websocketNotice close, session id：" + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        servers.remove(session.getId());
        log.error(throwable.getMessage(), throwable);
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        log.info("session id: " + session.getId() + "， message：" + msg);
    }

    /**
     * 发送信息
     *
     * @param userIdList 用户ID列表
     * @param message    消息内容
     */
    public void sendMessage(List<Long> userIdList, MessageData<String> message) {
        userIdList.forEach(userId -> sendMessage(userId, message));
    }

    /**
     * 发送信息
     *
     * @param userId  用户ID
     * @param message 消息内容
     */
    public void sendMessage(Long userId, MessageData<String> message) {
        servers.values().forEach(info -> {
            if (userId.equals(info.getUserId())) {
                SysNoticeSwitchEntity byUserId = sysNoticeSwitchService.getByUserId(userId);
                message.setData(byUserId.getStatus());
                sendMessage(info.getSession(), message);
            }
        });
    }

    /**
     * 发送信息给全部用户
     *
     * @param message 消息内容
     */
    public void sendMessageAll(MessageData<String> message) {
        servers.values().forEach(info ->
                {
                    System.err.println("用户id："+info.getUserId());
                    SysNoticeSwitchEntity byUserId = sysNoticeSwitchService.getByUserId(info.getUserId());
                    message.setData(byUserId.getStatus());
                    sendMessage(info.getSession(), message);
                }


        );
    }


    public void sendMessage(Session session, MessageData<?> message) {
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(message));
        } catch (IOException e) {
            log.error("send message error，" + e.getMessage(), e);
        }
    }
}