package com.qczl.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.qczl.websocket.util.RedisUtil;
import com.qczl.websocket.constans.RedisConstans;
import com.qczl.websocket.entity.Message;
import com.qczl.websocket.service.TalkService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TalkServiceImpl implements TalkService{

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void messageAddRedis(Message message) {

        Map<String,Double> map = new HashMap<>();
        //webSocket:roomTalk:
        String key = RedisConstans.ROOM_KEY + message.getRoomId();

        Long score = message.getDate().getTime();

        map.put(JSON.toJSONString(message),new Double(score));

        redisUtil.zadd(key,map);
    }

    @Override
    public List<Message> getMessage(String roomId, int pageNo, int pageSize, Long score){

        Set<String> set = redisUtil.zrevrangeByScore(RedisConstans.ROOM_KEY + roomId ,score,0,(pageNo - 1) * pageSize,pageSize);

        if(CollectionUtils.isEmpty(set))
            return null;

        return JSON.parseArray(set.toString(),Message.class);
    }
}
