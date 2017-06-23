package com.qczl.websocket.service;

import com.qczl.websocket.entity.Message;

import java.util.List;

public interface TalkService {
    void messageAddRedis(Message message);

    List<Message> getMessage(String roomId, int pageNo, int pageSize, Long score);
}
