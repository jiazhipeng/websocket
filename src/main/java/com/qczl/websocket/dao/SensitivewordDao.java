package com.qczl.websocket.dao;

import com.qczl.websocket.entity.Sensitiveword;

import java.util.List;

public interface SensitivewordDao {
    List<Sensitiveword> selectAll();
}
