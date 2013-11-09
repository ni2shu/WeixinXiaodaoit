package co.xiaodao.weixin.message.request;

import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 用户请求的图片消息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ReqImageMessage extends ReqBaseMessage {
	// 图片链接
	private String PicUrl;
	// 消息id，64位整型
	private String MsgId;

	public ReqImageMessage(String fromUserName, String createTime,
			String msgId, String picUrl) {
		super(fromUserName, createTime, WeixinUtil.REQ_MESSAGE_TYPE_IMAGE);
		PicUrl = picUrl;
		MsgId = msgId;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
