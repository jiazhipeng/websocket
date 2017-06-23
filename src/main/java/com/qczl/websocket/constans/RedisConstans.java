package com.qczl.websocket.constans;

public interface RedisConstans {

    int TWO_HOURS = 7200;
    int FOUR_HOURS = 14400;
    int TWO_DAYS = 172800;
    int ONE_DAYS = 86400;

    String PROJECT_NAME = "webSocket:";

    String ROOM_KEY = PROJECT_NAME + "roomTalk:roomId_";

    String SENSITIVEWORD_LIST= PROJECT_NAME + "sensitiveword_List";
}
