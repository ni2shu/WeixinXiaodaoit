package co.xiaodao.weixin.message.request;

import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 用户请求的链接消息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ReqLinkMessage extends ReqBaseMessage {
	// 消息标题
	private String Title;
	// 消息描述
	private String Description;
	// 消息链接
	private String Url;
	// 消息id，64位整型
	private String MsgId;

	public ReqLinkMessage(String fromUserName, String createTime, String msgId,
			String title, String description, String url) {
		super(fromUserName, createTime, WeixinUtil.REQ_MESSAGE_TYPE_LINK);
		Title = title;
		Description = description;
		Url = url;
		MsgId = msgId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
