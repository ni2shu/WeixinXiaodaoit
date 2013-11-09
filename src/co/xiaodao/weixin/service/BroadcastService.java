package co.xiaodao.weixin.service;

import java.util.ArrayList;
import java.util.List;

import co.xiaodao.weixin.db.util.BaseDBUtil;
import co.xiaodao.weixin.message.pojo.Article;

/**
 * 广播电台服务
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */

public class BroadcastService {

	public static List<Article> makeArticlesByBroadcast() {

		List<Article> articles = new ArrayList<Article>();

		Article article1 = new Article();
		article1.setTitle("『听广播』点击下面的播放键:");
		article1.setDescription("");
		article1.setUrl(null);
		article1.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/broadcast_head.jpg");

//		Article article2 = new Article();
//		article2.setTitle("浙江交通之声");
//		article2.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
//				+ "/WeixinXiaodaoit/image/broadcast_zjjtzs.jpg");
//		article2.setDescription("");
//		article2.setUrl("http://www.unitrue.com.cn/h5app/");

		Article article3 = new Article();
		article3.setTitle("清晨音乐电台");
		article3.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/broadcast_qcyydt.jpg");
		article3.setDescription("");
		article3.setUrl("http://fmradio.unitrue.com.cn/h5app/home.aspx?sid=9");

		articles.add(article1);
		//articles.add(article2);
		articles.add(article3);

		return articles;
	}

	public static void main(String[] args) {

	}
}
