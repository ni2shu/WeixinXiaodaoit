package co.xiaodao.weixin.message.request;

import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 用户请求的文本消息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ReqTextMessage extends ReqBaseMessage {
	// 消息内容
	private String Content;
	// 消息id，64位整型
	private String MsgId;

	public ReqTextMessage(String fromUserName, String createTime, String msgId,
			String content) {
		super(fromUserName, createTime, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
		Content = content;
		MsgId = msgId;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}