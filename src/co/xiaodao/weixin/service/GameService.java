package co.xiaodao.weixin.service;

import java.util.ArrayList;
import java.util.List;

import co.xiaodao.weixin.db.util.BaseDBUtil;
import co.xiaodao.weixin.message.pojo.Article;

/**
 * 游戏服务
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */

public class GameService {

	// 游戏URL前缀
	public static final String GAME_URL_PRE = "http://resource.duopao.com/duopao/games/small_games/weixingame/";

	public static List<Article> makeArticlesByGame() {

		List<Article> articles = new ArrayList<Article>();

		Article article1 = new Article();
		article1.setTitle("『玩游戏』点击你想玩的游戏:");
		article1.setDescription("");
		article1.setUrl(null);
		article1.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/game/game_head1.jpg");

		Article article2 = new Article();
		article2.setTitle("『空间先生』\n一款胆战心惊肉跳跳的游戏");
		article2.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/game/game_kjxs.jpg");
		article2.setDescription("");
		article2.setUrl(GAME_URL_PRE + "spaceman/spaceman.html");

//		Article article3 = new Article();
//		article3.setTitle("雷霆战机");
//		article3.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
//				+ "/WeixinXiaodaoit/image/game/game_ltzj.jpg");
//		article3.setDescription("");
//		article3.setUrl(GAME_URL_PRE + "X-Type2/X-Type.htm");

//		Article article4 = new Article();
//		article4.setTitle("蜘蛛纸牌");
//		article4.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
//				+ "/WeixinXiaodaoit/image/game/game_zzzp.jpg");
//		article4.setDescription("");
//		article4.setUrl(GAME_URL_PRE + "SpiderSolitaire/SpiderSolitaire.htm");

		Article article5 = new Article();
		article5.setTitle("『斗地主』\n微信上也能斗地主");
		article5.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/game/game_ddz.jpg");
		article5.setDescription("");
		article5.setUrl(GAME_URL_PRE + "Doudizhu/doudizhu.htm");

		Article article6 = new Article();
		article6.setTitle("『俄罗斯方块』\n超级经典的一款游戏");
		article6.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/game/game_elsfk.jpg");
		article6.setDescription("");
		article6.setUrl(GAME_URL_PRE + "blockDream/blockDream.htm");

//		Article article7 = new Article();
//		article7.setTitle("泊车高手\n你的驾驶技巧过关吗？");
//		article7.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
//				+ "/WeixinXiaodaoit/image/game/game_pcgs.jpg");
//		article7.setDescription("");
//		article7.setUrl(GAME_URL_PRE + "ParkingTrainee/ParkingTrainee.htm");

		Article article8 = new Article();
		article8.setTitle("『宝石连环战』\n看看你的眼力好不好");
		article8.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/game/game_bslhz.jpg");
		article8.setDescription("");
		article8.setUrl(GAME_URL_PRE + "JewelJive/JewelJive.htm");

//		Article article9 = new Article();
//		article9.setTitle("横扫千军");
//		article9.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
//				+ "/WeixinXiaodaoit/image/game/game_hsqj.jpg");
//		article9.setDescription("");
//		article9.setUrl(GAME_URL_PRE + "Cubium/Cubium.htm");

		Article article10 = new Article();
		article10.setTitle("『台球大师』\n台球游戏的一大力作");
		article10.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/game/game_tqds.jpg");
		article10.setDescription("");
		article10.setUrl(GAME_URL_PRE + "BilliardsMaster/BilliardsMaster.htm");

		articles.add(article1);
		articles.add(article2);
//		articles.add(article3);
//		articles.add(article4);
		articles.add(article5);
		articles.add(article6);
//		articles.add(article7);
		articles.add(article8);
//		articles.add(article9);
		articles.add(article10);

		return articles;
	}

	public static void main(String[] args) {

	}
}
