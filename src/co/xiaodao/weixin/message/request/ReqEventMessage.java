package co.xiaodao.weixin.message.request;

import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 用户请求的事件消息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ReqEventMessage extends ReqBaseMessage {
	// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件)
	private String Event;
	// 事件KEY值，与自定义菜单接口中KEY值对应
	private String EventKey;

	public ReqEventMessage(String fromUserName, String createTime,
			String event, String eventKey) {
		super(fromUserName, createTime, WeixinUtil.REQ_MESSAGE_TYPE_EVENT);
		Event = event;
		EventKey = eventKey;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEvent() {
		return Event;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getEventKey() {
		return EventKey;
	}

}