package com.qczl.websocket.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * 消息类
 */
public class Message implements Serializable{

	private static final long serialVersionUID = 1300672035406605802L;
	/**发送者*/
	@NotNull(message = "userId不能为空")
	public Long userId;

	/**发送者名称*/
	@NotBlank(message = "userName不能为空")
	public String userName;

	/**发送的文本*/
	public String text;

	/**发送日期*/
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date date;

	/**房间号*/
	@NotBlank(message = "roomId不能为空")
	private String roomId;

	/**消息类型*/
	private Integer messageType;

	public static enum MessageTypeEnum {

		MESSAGETYPE_TALK(1, "文字消息"),
		MESSAGETYPE_PICTURE(2, "图片消息");

		private int value;
		private String name;

		MessageTypeEnum(int value, String name) {
			this.name = name;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
