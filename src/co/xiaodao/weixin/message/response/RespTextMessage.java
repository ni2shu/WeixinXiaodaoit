package co.xiaodao.weixin.message.response;

import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 响应文本消息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class RespTextMessage extends RespBaseMessage {
	// 回复的消息内容
	private String Content;

	public RespTextMessage(String fromUserName, String toUserName,
			String createTime, String funcFlag, String content) {
		super(fromUserName, toUserName, createTime,
				WeixinUtil.RESP_MESSAGE_TYPE_TEXT, funcFlag);
		Content = content;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}