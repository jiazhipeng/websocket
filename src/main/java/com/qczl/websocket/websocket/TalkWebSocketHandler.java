package com.qczl.websocket.websocket;

import com.alibaba.fastjson.JSON;
import com.qczl.websocket.entity.Message;
import com.qczl.websocket.filter.SensitivewordFilter;
import com.qczl.websocket.proc.MessageProcessor;
import com.qczl.websocket.service.TalkService;
import com.qczl.websocket.constans.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket处理器
 */
@Component
public class TalkWebSocketHandler implements WebSocketHandler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//key 房间号 value 连接session
	Map<String,Set<WebSocketSession>> map = new ConcurrentHashMap<>();

	@Autowired
	private MessageProcessor messageProcessor;

	@Autowired
	private SensitivewordFilter sensitivewordFilter;

	@Autowired
	private TalkService talkService;

	/**
	 * 建立连接后
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {

		//获取房间号
		String roomId = session.getAttributes().get(Constants.ROOM_ID).toString();

		//获取昵称
		String userName = session.getAttributes().get(Constants.USER_NAME).toString();

		Set<WebSocketSession> set = map.get(roomId);

		if (CollectionUtils.isEmpty(set)) {
			set = new HashSet<>();
		}

		set.add(session);

		map.put(roomId, set);

		Message msg = new Message();
		msg.setUserName(userName);
		msg.setRoomId(roomId);
		msg.setDate(new Date());

		msg.setText(Constants.WELCOME);
		//向本房间号的所有成员发送消息
		sendMessageToRoom(roomId, JSON.toJSONString(msg));

		logger.info("userName:{},加入房间:{}的时间为:{}",userName,roomId,new Date().getTime());
	}

	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if(message.getPayloadLength() == 0) return;

		Message msg = JSON.parseObject(message.getPayload().toString(),Message.class);

		msg.setDate(new Date());
		//获取房间号
		String roomId = session.getAttributes().get(Constants.ROOM_ID).toString();
		//获取昵称
		String userName = session.getAttributes().get(Constants.USER_NAME).toString();

		if (Message.MessageTypeEnum.MESSAGETYPE_TALK.getValue() == msg.getMessageType()){

			if (StringUtils.isBlank(msg.getText())) return;

			//文字消息过滤敏感词
			String text = sensitivewordFilter.doFilter(msg.getText());
			msg.setText(text);
		}

		msg.setUserName(userName);
		msg.setRoomId(roomId);

		this.sendMessageToRoom(roomId,JSON.toJSONString(msg));

		//数据存入redis
		talkService.messageAddRedis(msg);
	}

	/**
	 * 消息传输错误处理
	 */
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		logger.info("Socket会话[{}]出现异常将被移除:[{}]",session,exception);

		if (session.isOpen()) {
			session.close();
		}
		String roomId = String.valueOf(session.getAttributes().get(Constants.ROOM_ID));

		Set<WebSocketSession> set = map.get(roomId);

		if(CollectionUtils.isEmpty(set)) {
			map.remove(roomId);
		}

		set.remove(session);
	}

	/**
	 * 关闭连接后
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {

		String roomId = String.valueOf(session.getAttributes().get(Constants.ROOM_ID));

		Set<WebSocketSession> set = map.get(roomId);

		if(CollectionUtils.isEmpty(set)) {
			map.remove(roomId);
		}

		set.remove(session);

		logger.info("Socket会话已经移除:{}",session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给某个直播间用户发送消息
	 * @param roomId
	 * @param message
	 * @throws IOException
	 */
	public void sendMessageToRoom(String roomId, String message) {

		Set<WebSocketSession> set = map.get(roomId);

		if(CollectionUtils.isEmpty(set)) return;

		Iterator<WebSocketSession> it =  set.iterator();

		while (it.hasNext()){
			WebSocketSession session = it.next();
			if (session != null){
				if (session.isOpen()){

					Map<String,Object> map = this.processParams(message,session);
					messageProcessor.addMessageTask(map);

				}else{
					it.remove();
				}
			}
		}
	}

	private Map<String,Object> processParams(String message,WebSocketSession session){
		Map<String,Object> map = new ConcurrentHashMap<>();
		map.put(Constants.MESSAGE_KEY,message);
		map.put(Constants.SOCKETSESSION_KEY,session);
		return map;
	}

}
