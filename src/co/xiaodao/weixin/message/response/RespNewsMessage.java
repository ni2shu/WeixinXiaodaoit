package co.xiaodao.weixin.message.response;

import java.util.List;

import co.xiaodao.weixin.message.pojo.Article;
import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 响应图文消息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class RespNewsMessage extends RespBaseMessage {
	// 图文消息个数，限制为10条以内
	private String ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public RespNewsMessage(String fromUserName, String toUserName,
			String createTime, String funcFlag, String articleCount,
			List<Article> articles) {
		super(fromUserName, toUserName, createTime,
				WeixinUtil.RESP_MESSAGE_TYPE_NEWS, funcFlag);
		ArticleCount = articleCount;
		Articles = articles;
	}

	public String getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(String articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
}