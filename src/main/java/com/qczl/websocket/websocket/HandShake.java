
package com.qczl.websocket.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;


/**
 * Socket建立连接（握手）和断开
 */
public class HandShake extends HttpSessionHandshakeInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//建立连接
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		if (request instanceof ServletServerHttpRequest) {

			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

			HttpServletRequest httpRequest = servletRequest.getServletRequest();

			HttpSession session = httpRequest.getSession(false);

			Enumeration<String> names = httpRequest.getParameterNames();

			while (names.hasMoreElements()){
				String name = names.nextElement();
				attributes.put(name,httpRequest.getParameter(name));
			}
		}
		super.beforeHandshake(request,response,wsHandler,attributes);
		return true;
	}

	//断开连接
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
	}

}
