package co.xiaodao.weixin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.xiaodao.weixin.db.pojo.GetUpSign;
import co.xiaodao.weixin.db.pojo.MsgCache;
import co.xiaodao.weixin.db.pojo.Question;
import co.xiaodao.weixin.db.pojo.ShowLove;
import co.xiaodao.weixin.db.pojo.User;
import co.xiaodao.weixin.db.util.ClassroomDBUtil;
import co.xiaodao.weixin.db.util.GetUpSignDBUtil;
import co.xiaodao.weixin.db.util.MessageDBUtil;
import co.xiaodao.weixin.db.util.MsgCacheDBUtil;
import co.xiaodao.weixin.db.util.QuestionDBUtil;
import co.xiaodao.weixin.db.util.ShowLoveDBUtil;
import co.xiaodao.weixin.db.util.UserDBUtil;
import co.xiaodao.weixin.json.weather.WeatherJson;
import co.xiaodao.weixin.message.pojo.Article;
import co.xiaodao.weixin.message.pojo.Music;
import co.xiaodao.weixin.message.request.ReqEventMessage;
import co.xiaodao.weixin.message.request.ReqImageMessage;
import co.xiaodao.weixin.message.request.ReqLocationMessage;
import co.xiaodao.weixin.message.request.ReqTextMessage;
import co.xiaodao.weixin.message.response.RespMusicMessage;
import co.xiaodao.weixin.message.response.RespNewsMessage;
import co.xiaodao.weixin.message.response.RespTextMessage;
import co.xiaodao.weixin.service.BaiduBaikeCrawler;
import co.xiaodao.weixin.service.BaiduMusicService;
import co.xiaodao.weixin.service.BroadcastService;
import co.xiaodao.weixin.service.ClassroomService;
import co.xiaodao.weixin.service.DianPingService;
import co.xiaodao.weixin.service.ExpressService;
import co.xiaodao.weixin.service.FaceService;
import co.xiaodao.weixin.service.GameService;
import co.xiaodao.weixin.service.ShmtuAuthService;
import co.xiaodao.weixin.service.ShmtuService;
import co.xiaodao.weixin.service.TodayService;
import co.xiaodao.weixin.service.WeatherService;
import co.xiaodao.weixin.util.DianPingUtil;
import co.xiaodao.weixin.util.ShmtuUtil;
import co.xiaodao.weixin.util.WeixinUtil;
import co.xiaodao.weixin.util.XiaoDaoUtil;
import co.xiaodao.weixin.util.secret.AES;

/**
 * 与微信公众平台交互
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-29
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class WeixinServlet extends HttpServlet {

	private static final long serialVersionUID = -4267408236898837036L;

	private static Logger log = LoggerFactory.getLogger(WeixinServlet.class);

	/**
	 * 处理微信公众平台的验证<br>
	 * 通过检验signature对请求进行校验:若确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效；否则接入失败。
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		if (WeixinUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	/**
	 * 处理普通用户发送的信息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			Map<String, String> requestMap = XiaoDaoUtil.parseXml(request);
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 请求时间
			String requestCreateTime = requestMap.get("CreateTime");
			String dbRequestCreateTime = XiaoDaoUtil.getFormatTime(new Date(Long.parseLong(requestCreateTime + "000")),"yyyy-MM-dd HH:mm:ss");
			// 消息ID
			String msgId = requestMap.get("MsgId");

			// 响应时间
			long responseCreateTime = new Date().getTime();
			String dbResponseCreateTime = XiaoDaoUtil.getFormatTime(new Date(responseCreateTime), "yyyy-MM-dd HH:mm:ss");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 图文信息的XML
			String articleXML = "";

			// 文本消息
			if (WeixinUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
				// 消息内容
				String content = requestMap.get("Content").trim();
				log.info("『文本消息』【{}】【{}】", fromUserName, content);

				MessageDBUtil.insertReqTextMsg(new ReqTextMessage(fromUserName, dbRequestCreateTime, msgId, content));

				// 回复文本消息
				RespTextMessage textMessage = new RespTextMessage(toUserName, fromUserName, dbResponseCreateTime, "0", "");

				// 海大资讯的帮助信息
				if ("0".equals(content) || "00".equals(content)
						|| "海大".equals(content) || "海大资讯".equals(content)) {
					textMessage
							.setContent(WeixinUtil.MSG_CONTENT_SHMTU_INTERNAL_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 使用帮助,返回主菜单
				else if ("?".equals(content) || "？".equals(content)
						|| "h".equalsIgnoreCase(content)
						|| "帮助".equals(content)
						|| "help".equalsIgnoreCase(content)) {
					textMessage.setContent(WeixinUtil.getHelpMsgContent());
					out.print(WeixinUtil.textMessageToXml(textMessage));

					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 海大资讯-找自习教室
				else if ("01".equals(content) || "111".equals(content)
						|| "找空教室".equals(content) || "空教室".equals(content)
						|| "找自习教室".equals(content) || "教室".equals(content)
						|| "自习".equals(content) || "找自习".equals(content)) {
					textMessage.setContent(WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_HELP);
					//textMessage.setContent(WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_HELP_19);
					//textMessage.setContent("同学们：找空教室的功能暂时关闭，待补选结束之后再开放。\n\n-陈小道");
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 海大资讯-教务公告
				else if ("02".equals(content) || "222".equals(content)
						|| "教务公告".equals(content) || "教务处".equals(content)) {
					List<Article> articles = ShmtuService.makeArticlesForJWC();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大资讯-图书检索
				else if ("03".equals(content) || "333".equals(content)
						|| "图书检索".equals(content) || "借书".equals(content)
						|| "图书馆检索".equals(content) || "图书".equals(content)
						|| "找图书".equals(content)) {
					textMessage
							.setContent(WeixinUtil.MSG_CONTENT_SHMTU_LAB_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 海大资讯-新书通报
/*
				else if ("04".equals(content) || "444".equals(content)
						|| "新书通报".equals(content) || "新书".equals(content)) {
					List<Article> articles = ShmtuService
							.makeArticlesForNewBook();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大图片新闻
				else if ("05".equals(content) || "666".equals(content)
						|| "海大新闻".equals(content) || "新闻".equals(content)
						|| "图片新闻".equals(content) || "海大图片新闻".equals(content)) {
					List<Article> articles = ShmtuService
							.makeArticlesForImageNews();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大校内动态
				else if ("06".equals(content) || "777".equals(content)
						|| "校园动态".equals(content) || "动态".equals(content)
						|| "海大校园动态".equals(content)) {
					List<Article> articles = ShmtuService.makeArticlesForNews();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大通知公告
				else if ("07".equals(content) || "888".equals(content)
						|| "通知公告".equals(content) || "海大通知公告".equals(content)) {
					List<Article> articles = ShmtuService
							.makeArticlesForNotes();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大资讯-足迹网信息
				else if ("08".equals(content) || "555".equals(content)
						|| "信工就业".equals(content) || "足迹网".equals(content)
						|| "信工就业信息".equals(content) || "足迹网信息".equals(content)) {
					List<Article> articles = ShmtuService.makeArticlesForXGJY();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大资讯-最新校报
				else if ("09".equals(content) || "校报".equals(content)
						|| "最新校报".equals(content)) {
					List<Article> articles = ShmtuService
							.makeArticlesForNewspaper();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大资讯-海大广播
				else if ("10".equals(content) || "海大广播".equals(content)) {
					List<Article> articles = ShmtuService
							.makeArticlesForBroadcast();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
*/
				// 课表显示
				else if ("11".equals(content) || "课表".equals(content)
						|| "查课表".equals(content) || "课表显示".equals(content)) {
					String str[] = ShmtuAuthService
							.getClassSchedule(fromUserName);
					if (!str[0].equals("0")) {
						str[1] = UserDBUtil.getUserDataByOpenID(fromUserName,
								"student_info") + "你本学期课表如下：\n\n" + str[1];
					}
					str[1] = XiaoDaoUtil.byteSubstring(str[1]);
					textMessage.setContent(str[1]);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 通过课程
				else if ("12".equals(content) || "已修课程".equals(content)
						|| "绩点".equals(content) || "GPA".equals(content)
						|| "已修".equals(content) || "通过课程".equals(content)) {
					String str[] = ShmtuAuthService.getPassScore(fromUserName);
					if (!str[0].equals("0")) {
						str[1] = UserDBUtil.getUserDataByOpenID(fromUserName,
								"student_info") + "你已修的课程如下：\n\n" + str[1];
					}
					str[1] = XiaoDaoUtil.byteSubstring(str[1]);
					textMessage.setContent(str[1]);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 最新成绩
				else if ("13".equals(content) || "查分".equals(content)
						|| "查分数".equals(content) || "查成绩".equals(content)
						|| "成绩".equals(content) || "最新成绩".equals(content)) {
					String str[] = ShmtuAuthService.getNewScore(fromUserName);
					if (!str[0].equals("0")) {
						str[1] = UserDBUtil.getUserDataByOpenID(fromUserName,
								"student_info") + "你本学期的成绩如下：\n\n" + str[1];
					}
					str[1] = XiaoDaoUtil.byteSubstring(str[1]);
					//System.out.println(str[1] );
					textMessage.setContent(str[1]);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
					
//					textMessage.setContent("这是查最新成绩的功能，但是本学期的成绩都还没有出来，如果你有已经出来的成绩，请告诉小道一下，小道个人微信号：cdztop");
//					out.print(WeixinUtil.textMessageToXml(textMessage));
//					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 早起签到
				else if ("14".equals(content) || "早起签到".equals(content)
						|| "早起".equals(content) || "签到".equals(content)
						|| "早安".equals(content)) {
					String respStr = "";
					String rankAll = "";
					String rankSomeOne = "";
					String weatherStr = "";
					String bindStr = "";
					String strArr[] = XiaoDaoUtil
							.getNowDateTimeByFormatForGetUpSign();
					if (!UserDBUtil.isUserExist(fromUserName)) {
						User user = new User(fromUserName, "", "",
								dbRequestCreateTime, dbRequestCreateTime,
								WeixinUtil.IS_SUBSCRIBE, "", "", "", "", "",
								"", dbRequestCreateTime + "\n补写关注信息\n\n");
						UserDBUtil.insertUser(user);
					}

					if (strArr[1].compareTo("06:00:00") >= 0
							&& strArr[1].compareTo("08:00:00") <= 0) {
						GetUpSign getUpSign = GetUpSignDBUtil
								.getGetUpSignByOpenID(fromUserName, strArr[0]);
						if (getUpSign == null) {
							if (GetUpSignDBUtil.insertGetUpSign(new GetUpSign(
									fromUserName, strArr[0], strArr[1]),
									strArr[0])) {
								getUpSign = GetUpSignDBUtil
										.getGetUpSignByOpenID(fromUserName,
												strArr[0]);
								rankSomeOne = "起床成功！你是海大今天第"
										+ getUpSign.getRank()
										+ "个参加早起签到的同学！\n\n";
							} else {
								rankSomeOne = "起床失败！请联系开发者微信号：cdztop\n\n";
							}
						} else {
							rankSomeOne = "你在" + getUpSign.getTime()
									+ "已经签到过了！你是海大今天第" + getUpSign.getRank()
									+ "个参加早起签到的同学！\n\n";
						}

						if (Integer.parseInt(getUpSign.getRank()) <= 10
								&& UserDBUtil.getUserDataByOpenID(fromUserName,
										"name").equals("")) {
							String bindUrl = ShmtuAuthService.auth_url_bind_pre
									+ fromUserName;
							bindStr = "你有机会进入今天的早起排行榜，但是你还没有绑定学号，请点击下面的链接去绑定。\n\n"
									+ "<a href=\""
									+ bindUrl
									+ "\">——>去绑定学号</a>\n\n如果你点击上面的链接无法完成绑定，请尝试下面的这种方式：\n"
									+ WeixinUtil.MSG_CONTENT_BIND_STUID_HELP
									+ "\n\n";

						}
						rankAll = ShmtuService.getTodayAllSignTop(strArr[0]);
						weatherStr = WeatherService.getTodayShmtuWeather();
						respStr = rankAll + rankSomeOne + bindStr + weatherStr;
					} else {
						respStr = "我们的早起签到时间是早上6:00-8:00！同学你是按错了还是调戏我呢，你真顽皮啊";
					}
					textMessage.setContent(respStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 向TA表白
				else if ("15".equals(content) || "向TA表白".equals(content)
						|| "表白".equals(content) || "看表白".equals(content)) {
					textMessage.setContent(WeixinUtil.SMU_SHOWLOVE_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 电话查询
				else if ("16".equals(content) || "电话查询".equals(content)
						|| "电话".equals(content)) {
					textMessage.setContent(WeixinUtil.SMU_TELEPHONE_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 海大食堂
				else if ("17".equals(content) || "海大食堂".equals(content)
						|| "食堂".equals(content)) {
					textMessage.setContent(ShmtuUtil.CANTEEN_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 海大最新资讯
//				else if ("999".equals(content) || "海大最新资讯".equals(content)
//						|| "最新资讯".equals(content)) {
//					textMessage.setContent(ShmtuService.shmtuGetAll());
//					out.print(WeixinUtil.textMessageToXml(textMessage));
//					MessageDBUtil.insertRespTextMsg(textMessage);
//				}

				// 其他使用功能
				else if ("1".equals(content) || "休闲".equals(content)
						|| "游戏".equals(content) || "玩游戏".equals(content)
						|| "game".equalsIgnoreCase(content)) {
					List<Article> games = GameService.makeArticlesByGame();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(games.size()), games);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 点歌的帮助信息
				else if ("2".equals(content) || "点歌".equals(content)
						|| "听歌".equals(content) || "听音乐".equals(content)
						|| "音乐".equals(content)
						|| "music".equalsIgnoreCase(content)) {
					textMessage.setContent(WeixinUtil.MSG_CONTENT_MUSIC_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 天气预报的帮助信息
				else if ("3".equals(content) || "天气".equals(content) || "天气预报".equals(content) || "查天气".equals(content)) {
					if(MsgCacheDBUtil.isMsgCacheExist(fromUserName)){
						MsgCache msgCache = MsgCacheDBUtil.getMsgCacheByOpenID(fromUserName);
						String msgCacheWeather = msgCache.getWeather();
						if(msgCacheWeather != null && !msgCacheWeather.equals("")){
							String cityCode = WeatherService.findCityCodeByName(msgCacheWeather);
							WeatherJson weather = WeatherService.queryWeather(cityCode);
							// 根据天气信息组装图文消息
							List<Article> articles = WeatherService.makeArticlesByWeather(weather, msgCacheWeather);
							RespNewsMessage newsMessage = new RespNewsMessage(
									toUserName, fromUserName, dbResponseCreateTime,
									"0", String.valueOf(articles.size()), articles);
							articleXML = WeixinUtil.newsMessageToXml(newsMessage);
							out.print(articleXML);
							MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
						}else{
							textMessage.setContent(WeixinUtil.MSG_CONTENT_WEATHER_HELP);
							out.print(WeixinUtil.textMessageToXml(textMessage));
							MessageDBUtil.insertRespTextMsg(textMessage);
						}
					}else{
						textMessage.setContent(WeixinUtil.MSG_CONTENT_WEATHER_HELP);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}
				}
				// 找美食的帮助信息
				else if ("4".equals(content) || "美食".equals(content)
						|| "吃货".equals(content) || "找美食".equals(content)) {
					textMessage
							.setContent(WeixinUtil.MSG_CONTENT_DIANPING_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 听广播的帮助信息
				else if ("5".equals(content) || "广播".equals(content)
						|| "听广播".equals(content)) {
					List<Article> broadcasts = BroadcastService
							.makeArticlesByBroadcast();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(broadcasts.size()), broadcasts);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 看历史
				else if ("6".equals(content) || "历史".equals(content)
						|| "看历史".equals(content)) {
					List<String> list = TodayService.queryToday();
					String todayHistory = "";
					if (list != null && list.size() > 0) {
						for (String today : list) {
							todayHistory += today + "\n\n";
						}
						String historyToday = "[胜利]历史是一面镜子\n【回顾历史上的今天】"
								+ todayHistory;
						// 截取
						historyToday = XiaoDaoUtil.byteSubstring(historyToday);
						textMessage.setContent(historyToday);
					} else {
						textMessage
								.setContent("[胜利]历史是一面镜子\n【回顾历史上的今天】\n\n服务器出问题啦！\n\n请联系微信号：cdztop");
					}
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				// 查百科的帮助信息
				else if ("7".equals(content) || "百科".equals(content)
						|| "查百科".equals(content)) {
					textMessage.setContent(WeixinUtil.MSG_CONTENT_BAIKE_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);

				}
				// 测人脸的帮助信息
				else if ("8".equals(content) || "人脸".equals(content)
						|| "测人脸".equals(content)) {
					textMessage.setContent(WeixinUtil.MSG_CONTENT_FACE_HELP);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);

				}
				// 查快递的帮助信息
				else if ("9".equals(content) || "快递".equals(content) || "查快递".equals(content) || "物流".equals(content) || "查物流".equals(content)) {
					if(MsgCacheDBUtil.isMsgCacheExist(fromUserName)){
						MsgCache msgCache = MsgCacheDBUtil.getMsgCacheByOpenID(fromUserName);
						String msgCacheExpress = msgCache.getExpress();
						if(msgCacheExpress != null && !msgCacheExpress.equals("")){
							String[] expressArr = msgCacheExpress.split("@");
							String order_num = expressArr[0].trim();
							String expressName = msgCacheExpress.substring(expressArr[0].length() + 1).trim();
							textMessage.setContent(ExpressService.queryExpress(order_num, expressName, fromUserName));
							out.print(WeixinUtil.textMessageToXml(textMessage));
							MessageDBUtil.insertRespTextMsg(textMessage);
						}else{
							textMessage.setContent(WeixinUtil.MSG_CONTENT_EXPRESS_HELP);
							out.print(WeixinUtil.textMessageToXml(textMessage));
							MessageDBUtil.insertRespTextMsg(textMessage);
						}
					}else{
						textMessage.setContent(WeixinUtil.MSG_CONTENT_EXPRESS_HELP);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}
				}

				// 查空教室
				else if (content.startsWith("空教室")) {
					String time = content.substring(3).trim();
					String returnStr = null;
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date(); 
					String DateStr = df.format(date);
					SimpleDateFormat nowTimeDateFm = new SimpleDateFormat(
							"HH:mm");
					String nowTime = nowTimeDateFm.format(date);
					// 获取第几周
					String weekly = ClassroomService.getWeekly(DateStr);

					if (weekly == null) {
						// 放假时间
						returnStr = "同学，现在是放假时间，请开学后再使用该功能吧。";
					} 
					else if ((nowTime.compareTo("23:00") >= 0 && nowTime
							.compareTo("24:59") <= 0)
							|| (nowTime.compareTo("00:00") >= 0 && nowTime
									.compareTo("06:00") <= 0)) {
						returnStr = "同学，现在时间是" + nowTime
								+ "\n不要熬夜，好好休息，明天早起继续奋斗。";
					} 
					else {
						List<String> classroomListAllTemp = new ArrayList<String>();
						classroomListAllTemp
								.addAll(ClassroomService.classroomListAll);
						List<String> classroomInUseList = new ArrayList<String>();

						SimpleDateFormat dateXinqi = new SimpleDateFormat(
								"EEEE");
						String zoucistr = "今天是『" + DateStr + "』\n第" + weekly
								+ "周  " + dateXinqi.format(date) + "\n\n";
						String xinqiNum = ClassroomService.getXinQI(date);
						String kongxianStr = "{kongxian}空闲的教室：\n\n";
						if (time.equals("现在")) {
							String jieChi = ClassroomService.getJieChi(date);
							if (jieChi == null) {
								returnStr = "现在不是上课时间，请输入下面的规则来查询。\n\n"
										+ WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_OTHER_HELP; 
							} else {
								// 只返回124567中的一个
								kongxianStr = kongxianStr.replace("{kongxian}",
										time);
								classroomInUseList.addAll(ClassroomDBUtil
										.getInUseClassroom(xinqiNum + jieChi,
												weekly));

								classroomListAllTemp
										.removeAll(classroomInUseList);
								returnStr = zoucistr
										+ kongxianStr
										+ ClassroomService
												.getClassroomStr(classroomListAllTemp);
							}
						} else if (time.equals("全天") || time.equals("今天")) {// 124567
							kongxianStr = kongxianStr.replace("{kongxian}",
									time);
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "1", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "2", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "4", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "5", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "6", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "7", weekly));

							classroomListAllTemp.removeAll(classroomInUseList);
							returnStr = zoucistr
									+ kongxianStr
									+ ClassroomService
											.getClassroomStr(classroomListAllTemp);

						} else if (time.equals("上午")) {// 12
							kongxianStr = kongxianStr.replace("{kongxian}",
									time);
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "1", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "2", weekly));
							classroomListAllTemp.removeAll(classroomInUseList);
							returnStr = zoucistr
									+ kongxianStr
									+ ClassroomService
											.getClassroomStr(classroomListAllTemp);

						} else if (time.equals("下午")) {// 45
							kongxianStr = kongxianStr.replace("{kongxian}",
									time);
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "4", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "5", weekly));
							classroomListAllTemp.removeAll(classroomInUseList);
							returnStr = zoucistr
									+ kongxianStr
									+ ClassroomService
											.getClassroomStr(classroomListAllTemp);
						} else if (time.equals("晚上")) {// 67
							kongxianStr = kongxianStr.replace("{kongxian}",
									time);
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "6", weekly));
							classroomInUseList.addAll(ClassroomDBUtil
									.getInUseClassroom(xinqiNum + "7", weekly));
							classroomListAllTemp.removeAll(classroomInUseList);
							returnStr = zoucistr
									+ kongxianStr
									+ ClassroomService
											.getClassroomStr(classroomListAllTemp);
						} else if (ClassroomService.isTrueNumFormat(time)) {
							String jieciTemp = "";
							char[] strArr = time.toCharArray();
							for (int i = 0; i < strArr.length; i++) {
								jieciTemp += strArr[i] + "、";
								classroomInUseList.addAll(ClassroomDBUtil
										.getInUseClassroom(
												xinqiNum + strArr[i], weekly));
							}
							kongxianStr = kongxianStr.replace(
									"{kongxian}",
									"第"
											+ jieciTemp.substring(0,
													jieciTemp.length() - 1)
											+ "大节");

							classroomListAllTemp.removeAll(classroomInUseList);
							returnStr = zoucistr
									+ kongxianStr
									+ ClassroomService
											.getClassroomStr(classroomListAllTemp);
						} else {
							// 格式不对
							returnStr = "你输入的规则不正确，请按照下面的规则来查询。\n\n"
									+ WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_SHORT_HELP;
						}
					}

					textMessage.setContent(returnStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
				
				// 查空教室
//				else if (content.startsWith("空教室")) {
//					textMessage.setContent("同学们：找空教室的功能暂时关闭，待补选结束之后再开放。\n\n-陈小道");
//					out.print(WeixinUtil.textMessageToXml(textMessage));
//					MessageDBUtil.insertRespTextMsg(textMessage);
//					String time = content.substring(3).trim();
//					String returnStr = null;
//					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//					Date date = new Date();
//					String DateStr = df.format(date);
//					SimpleDateFormat nowTimeDateFm = new SimpleDateFormat("HH:mm");
//					String nowTime = nowTimeDateFm.format(date);
//					// 获取第几周
//					//String weekly = "19";
//					if (DateStr.compareTo("2013-07-01") < 0){
//						returnStr = "同学你好，考试周查询空教室仅限7月1日至7月5日使用。";
//					}
//					else if (DateStr.compareTo("2013-07-05") > 0){
//						returnStr = "同学你好，本学期已经放假了，先回家陪陪家人，休息几天。然后再投入到学习中，好好珍惜暑假的这两个月时间，" +
//								"去找个实习，或是去参加暑期社会实践，或是学习一些技术，或是学习英语等等。";
//					}
//					else if ((nowTime.compareTo("00:00") >= 0 && nowTime.compareTo("05:00") <= 0)) {
//						returnStr = "同学，现在时间是" + nowTime + "\n不要熬夜，好好休息，明天早起再复习吧。";
//					} else {
//						List<String> classroomListAllTemp = new ArrayList<String>();
//						classroomListAllTemp.addAll(ClassroomService.classroomListAll);
//						List<String> classroomInUseList = new ArrayList<String>();
//
//						SimpleDateFormat dateXinqi = new SimpleDateFormat("EEEE");
//						String zoucistr = "今天是『" + DateStr + "』\n第19周（考试周）\n" + dateXinqi.format(date) + "\n\n";
//						String xinqiNum = ClassroomService.getXinQI(date);
//						String kongxianStr = "{kongxian}空闲的教室：\n\n";
//						if (time.equals("现在")) {
//							String jieChi = ClassroomService.getJieChi(date);
//							if (jieChi == null) {
//								returnStr = "现在不是考试时间，请输入下面的规则来查询。\n\n" + WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_SHORT_19_HELP;
//							} else {
//								// 只返回1234中的一个
//								kongxianStr = kongxianStr.replace("{kongxian}", time);
//								classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + jieChi));
//								classroomListAllTemp.removeAll(classroomInUseList);
//								returnStr = zoucistr + kongxianStr + ClassroomService.getClassroomStr(classroomListAllTemp);
//							}
//						} else if (time.equals("全天") || time.equals("今天")) {// 124567
//							kongxianStr = kongxianStr.replace("{kongxian}",time);
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "1"));
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "2"));
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "3"));
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "4"));
//							classroomListAllTemp.removeAll(classroomInUseList);
//							returnStr = zoucistr + kongxianStr + ClassroomService.getClassroomStr(classroomListAllTemp);
//
//						} else if (time.equals("上午")) {// 12
//							kongxianStr = kongxianStr.replace("{kongxian}",time);
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "1"));
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "2"));
//							classroomListAllTemp.removeAll(classroomInUseList);
//							returnStr = zoucistr + kongxianStr + ClassroomService.getClassroomStr(classroomListAllTemp);
//
//						} else if (time.equals("下午")) {// 34
//							kongxianStr = kongxianStr.replace("{kongxian}",time);
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "3"));
//							classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + "4"));
//							classroomListAllTemp.removeAll(classroomInUseList);
//							returnStr = zoucistr + kongxianStr + ClassroomService.getClassroomStr(classroomListAllTemp);
//							
//						} else if (time.equals("晚上")) {// 67
//							kongxianStr = kongxianStr.replace("{kongxian}",time);
//							returnStr = "考试周晚上没有安排考试，所以所有教室都空闲！";
//						} else if (ClassroomService.isTrueNumFormat(time)) {
//							String jieciTemp = "";
//							char[] strArr = time.toCharArray();
//							for (int i = 0; i < strArr.length; i++) {
//								jieciTemp += strArr[i] + "、";
//								classroomInUseList.addAll(ClassroomDBUtil.getInUseClassroom(xinqiNum + strArr[i]));
//							}
//							kongxianStr = kongxianStr.replace("{kongxian}","第" + jieciTemp.substring(0,jieciTemp.length() - 1)+ "场");
//
//							classroomListAllTemp.removeAll(classroomInUseList);
//							returnStr = zoucistr + kongxianStr + ClassroomService.getClassroomStr(classroomListAllTemp);
//						} else {
//							// 格式不对
//							returnStr = "你输入的规则不正确，请按照下面的规则来查询。\n\n" + WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_OTHER_19_HELP;
//						}
//					}
//
//					textMessage.setContent(returnStr);
//					out.print(WeixinUtil.textMessageToXml(textMessage));
//					MessageDBUtil.insertRespTextMsg(textMessage);
//				}

				// 图书馆检索
				
				else if (content.startsWith("检索")) {
					String bookNameKey = content.substring(2).trim();
					if ("".equals(bookNameKey)) {
						textMessage.setContent(WeixinUtil.pleaseInputMsg(1));
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					} else {
						List<Article> articles = ShmtuService
								.makeArticlesForBook(bookNameKey);
						if (articles.size() == 0) {
							textMessage.setContent("你输入的书名关键字：\n『"
									+ bookNameKey + "』\n查找不到记录，请更换关键字。");
							out.print(WeixinUtil.textMessageToXml(textMessage));
							MessageDBUtil.insertRespTextMsg(textMessage);
						} else {
							RespNewsMessage newsMessage = new RespNewsMessage(
									toUserName, fromUserName,
									dbResponseCreateTime, "0",
									String.valueOf(articles.size()), articles);
							articleXML = WeixinUtil
									.newsMessageToXml(newsMessage);
							out.print(articleXML);
							MessageDBUtil.insertRespNewsMsg(newsMessage,
									articleXML);
						}
					}
				}
				

				// 绑定
				else if (content.startsWith("绑定")) {
					String returnStr = "";
					String stuid_pwd = content.substring(2).trim();
					String[] stuid_pwd_Arr = stuid_pwd.split("@");
					if ("".equals(stuid_pwd) || stuid_pwd_Arr.length < 2) {
						returnStr = WeixinUtil.pleaseInputMsg(5);
					} else {
						String stuid = stuid_pwd_Arr[0].trim();// 学号
						Pattern p1 = Pattern.compile("^20\\d{10}$");
						Matcher m1 = p1.matcher(stuid);
						if (!m1.matches()) {
							returnStr = "请发送正确的学号。";
						} else {
							String pwd = "";
							pwd = stuid_pwd.substring(
									stuid_pwd_Arr[0].length() + 1).trim();
							String stuInfo = ShmtuAuthService
									.getStuInfoByHttpGet(stuid, pwd);
							if (stuInfo != null) {
								if (!UserDBUtil.isUserExist(fromUserName)) {
									User user = new User(fromUserName, "", "",
											dbRequestCreateTime,
											dbRequestCreateTime,
											WeixinUtil.IS_SUBSCRIBE, "", "",
											"", "", "", "", dbRequestCreateTime
													+ "\n补写关注信息\n\n");
									UserDBUtil.insertUser(user);
								}
								String AESPwd = AES.encryptAES(pwd,
										AES.ENCRYPT_KEY);
								if (UserDBUtil.updateUserForStu(new User(
										fromUserName, "", "", "",
										dbResponseCreateTime, "", "", stuid,
										AESPwd, stuInfo, "", "", ""))) {
									UserDBUtil.updateLog(fromUserName,
											dbResponseCreateTime + "\n绑定学号："
													+ stuid + "\n\n");
									returnStr = "恭喜你！绑定成功了。";
								} else {
									returnStr = "服务器罢工了，请联系开发者微信号：cdztop";
								}
							} else {
								returnStr = "绑定失败，你的学号或者密码貌似有错误哟。重新绑定一下哟。";
							}

						}
					}
					textMessage.setContent(returnStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 歌曲
				else if (content.startsWith("歌曲")) {
					// 音乐名称
					String song = content.substring(2).trim();
					if ("".equals(song)) {
						textMessage.setContent(WeixinUtil.pleaseInputMsg(2));
						out.print(WeixinUtil.textMessageToXml(textMessage));

						MessageDBUtil.insertRespTextMsg(textMessage);
					} else {
						String[] songArr = song.split("@");
						// 歌名
						String musicTitle = songArr[0].trim();
						// 歌者
						String musicAuthor = "";

						if (songArr.length >= 2) {
							musicAuthor = song.substring(
									songArr[0].length() + 1).trim();
						}

						// 搜索音乐
						Music music = BaiduMusicService.searchMusic(musicTitle,
								musicAuthor);
						// 没有搜索到
						if (null == music) {
							textMessage.setContent(WeixinUtil.notFoundMusic(
									musicTitle, musicAuthor));
							out.print(WeixinUtil.textMessageToXml(textMessage));

							MessageDBUtil.insertRespTextMsg(textMessage);
						} else {
							// 音乐消息
							RespMusicMessage musicMessage = new RespMusicMessage(
									toUserName, fromUserName,
									dbResponseCreateTime, "0", music);
							out.print(WeixinUtil
									.musicMessageToXml(musicMessage));

							MessageDBUtil.insertRespMusicMsg(musicMessage);
						}
					}
				}

				// 发送表白信息
				else if (content.startsWith("@")) {
					String[] strArr = content.split("#");
					String respStr = "";
					if (strArr.length != 3) {
						respStr = WeixinUtil.pleaseInputMsg(6);
					} else {
						String strArrDate[] = XiaoDaoUtil
								.getNowDateTimeByFormatForGetUpSign();
						String toName = strArr[0].trim().substring(1).trim();
						String loveContent = strArr[1].trim();
						String fromName = strArr[2].trim();
						if (ShowLoveDBUtil.insertShowLove(new ShowLove(
								fromUserName, fromName, toName, loveContent,
								strArrDate[0], dbRequestCreateTime))) {
							respStr = "表白成功！";
						} else {
							respStr = "表白失败！请联系开发者微信号：cdztop";
						}
					}
					textMessage.setContent(respStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 查看表白信息-表白对象
				else if (content.startsWith("表白对象")) {
					String toName = content.substring(4).trim();
					String respStr = "";
					if (toName.equals("")) {
						respStr = WeixinUtil.pleaseInputMsg(6);
					} else {
						respStr = XiaoDaoUtil.byteSubstring(ShmtuService
								.getShowLoveByNameToStr(toName, false));
					}
					textMessage.setContent(respStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 查看表白信息-表白人
				else if (content.startsWith("表白人")) {
					String fromName = content.substring(3).trim();
					String respStr = "";
					if (fromName.equals("")) {
						respStr = WeixinUtil.pleaseInputMsg(6);
					} else {
						respStr = XiaoDaoUtil.byteSubstring(ShmtuService
								.getShowLoveByNameToStr(fromName, true));
					}
					textMessage.setContent(respStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 查看表白信息-今天
				else if (content.equals("表白今天")) {
					String strArr[] = XiaoDaoUtil
							.getNowDateTimeByFormatForGetUpSign();
					textMessage.setContent(XiaoDaoUtil
							.byteSubstring(ShmtuService
									.getTodayAllShowLove(strArr[0])));
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 电话查询
				else if (content.startsWith("电话")) {
					// 关键字
					String keyword = content.substring(2).trim();
					String respStr = XiaoDaoUtil.byteSubstring(ShmtuService.searchTelephone(keyword));
					textMessage.setContent(respStr);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 天气预报 支持格式：天气贵阳、贵阳天气、贵阳的天气
				else if (content.startsWith("天气") || content.endsWith("天气")) {
					// 城市名称
					String cityName = "";
					if (content.startsWith("天气")) {
						cityName = content.substring(2).replace("的", "");
					} else {
						cityName = content.substring(0, content.length() - 2).replace("的", "");
					}
					// 根据城市名称查找城市代码
					String cityCode = WeatherService.findCityCodeByName(cityName.trim());
					// 用户仅输入了“天气”2字
					if ("".equals(cityName)) {
						textMessage.setContent(WeixinUtil.pleaseInputMsg(3));
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
						// 未找到用户输入的城市信息
					} else if (null == cityCode) {
						textMessage.setContent(WeixinUtil.MSG_CONTENT_ON_FOUND_WEATHER_CITY.replace("{city_name}", cityName));
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					} else {
						MsgCache msgCache = MsgCacheDBUtil.getMsgCacheByOpenID(fromUserName);
						if(msgCache != null){
							msgCache.setWeather(cityName);
							MsgCacheDBUtil.updateMsgCache(msgCache);
						}else{
							MsgCacheDBUtil.insertMsgCache(new MsgCache(fromUserName,cityName,""));
						}
						WeatherJson weather = WeatherService.queryWeather(cityCode);
						// 根据天气信息组装图文消息
						List<Article> articles = WeatherService.makeArticlesByWeather(weather, cityName);

						RespNewsMessage newsMessage = new RespNewsMessage(toUserName, fromUserName, dbResponseCreateTime,
								"0", String.valueOf(articles.size()), articles);

						articleXML = WeixinUtil.newsMessageToXml(newsMessage);
						out.print(articleXML);
						MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
					}
				}

				// 快递
				else if (content.startsWith("快递")) {
					String express = content.substring(2);
					if (express.split("@").length < 2) {
						textMessage.setContent(WeixinUtil.pleaseInputMsg(4));
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					} else {
						String[] expressArr = express.split("@");// 单号
						String order_num = expressArr[0].trim();// 快递公司
						String expressName = express.substring(expressArr[0].length() + 1).trim();
						textMessage.setContent(ExpressService.queryExpress(order_num, expressName, fromUserName));
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}
				}

				// 海大食堂-海馨
				else if ("171".equals(content) || "海馨".equals(content)
						|| "海馨食堂".equals(content) || "海馨楼食堂".equals(content)
						|| "海馨楼".equals(content)) {
					List<Article> articles = ShmtuService
							.makeCanteenHXForNewspaper();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大食堂-海琴
				else if ("172".equals(content) || "海琴".equals(content)
						|| "海琴食堂".equals(content) || "海琴楼食堂".equals(content)
						|| "海琴楼".equals(content)) {
					List<Article> articles = ShmtuService
							.makeCanteenHQForNewspaper();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
				// 海大食堂-海联
				else if ("173".equals(content) || "海联".equals(content)
						|| "海联食堂".equals(content) || "海联楼食堂".equals(content)
						|| "海联楼".equals(content)) {
					List<Article> articles = ShmtuService
							.makeCanteenHLForNewspaper();
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(articles.size()), articles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}

				// 单个QQ表情（原样返回）
				else if (WeixinUtil.isQqFace(content)) {
					textMessage.setContent(content);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 生日快乐的飘雪效果
				else if (content.equals("生日快乐")) {
					textMessage.setContent("生日快乐[蛋糕]");
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				}

				// 聊天
				else {
					// 1.从问题数据库表里面搜索
					String answer = QuestionDBUtil.getAnswer(content);
					// 2.如果问题数据库里面没有找到
					if (answer == null || answer.equals("")) {
						// 3.从百度百科的网页搜索
						answer = BaiduBaikeCrawler.queryBaike(content, false);
						// 4.如果百度百科网页不能正常获取
						if (answer == null || answer.equals("")) {
							// // 5.从索引中查找
							// String[] answerArr = AutoReplyService.search(
							// content).split("//");
							// answer = answerArr[AutoReplyUtil
							// .getRandomNumber(answerArr.length)];
							// answer = answer + "..";
							textMessage
									.setContent(WeixinUtil.MSG_CONTENT_OTHER_TYPE);
							textMessage.setFuncFlag("1");
							answer = WeixinUtil.MSG_CONTENT_OTHER_TYPE;
						} else {
							QuestionDBUtil.insertQuestion(new Question(
									"fromUser", content, answer, fromUserName,
									dbRequestCreateTime));
							answer = XiaoDaoUtil.byteSubstring(answer);
							textMessage.setContent(answer);
							textMessage.setFuncFlag("0");
						}

						// // 4.如果百度百科网页不能正常获取
						// if (answer == null || answer.equals("")) {
						// // 5.从索引中查找
						// String[] answerArr = AutoReplyService.search(
						// content).split("//");
						// answer = answerArr[AutoReplyUtil
						// .getRandomNumber(answerArr.length)];
						// answer = answer + "..";
						// } else {
						// QuestionDBUtil.insertQuestion(new Question(
						// "fromUser", content, answer, fromUserName,
						// dbRequestCreateTime));
						// }
						// // 6.如果索引中找到了
						// if
						// (!answer.equals(WeixinUtil.MSG_CONTENT_OTHER_TYPE)) {
						// // 截取
						// answer = XiaoDaoUtil.byteSubstring(answer);
						// textMessage.setContent(answer);
						// textMessage.setFuncFlag("0");
						// } else {
						// textMessage
						// .setContent(WeixinUtil.MSG_CONTENT_OTHER_TYPE);
						// textMessage.setFuncFlag("1");
						// }
					} else {
						// 截取
						answer = XiaoDaoUtil.byteSubstring(answer);
						textMessage.setContent(answer + ".");
						textMessage.setFuncFlag("0");
					}
					out.print(WeixinUtil.textMessageToXml(textMessage));
					// log.info("『应答消息』【{}】【{}】【{}】", new Object[] {
					// fromUserName,
					// content, answer });
					MessageDBUtil.insertRespTextMsg(textMessage);
				}
			}

			// 地理位置
			else if (WeixinUtil.REQ_MESSAGE_TYPE_LOCATION.equals(msgType)) {
				// 位置信息
				String label = requestMap.get("Label");
				// 地图缩放大小
				String scale = requestMap.get("Scale");
				// 纬度
				String lat = requestMap.get("Location_X");
				// 经度
				String lng = requestMap.get("Location_Y");

				log.info("『位置消息』【{}】经度【{}】纬度【{}】位置【{}】", new Object[] {
						fromUserName, lng, lat, label });

				MessageDBUtil.insertReqLocationMsg(new ReqLocationMessage(
						fromUserName, dbRequestCreateTime, msgId, lat, lng,
						scale, label));

				List<Article> dianPingArticles = DianPingService
						.makeArticlesByDianPingJson(
								DianPingUtil.DIAN_PING_API_URL,
								DianPingUtil.DIAN_PING_API_KEY,
								DianPingUtil.DIAN_PING_SECRET,
								DianPingUtil.getParamMap(lat, lng, ""));

				// 周边未搜索到美食信息
				if (null == dianPingArticles || dianPingArticles.size() < 2) {
					RespTextMessage textMessage = new RespTextMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", "");
					textMessage
							.setContent("在你的2公里范围内没有搜索到美食哦，请再次发送你的位置，或到别处再试哦。");
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);
				} else {
					// 根据美食信息组装图文消息
					RespNewsMessage newsMessage = new RespNewsMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", String.valueOf(dianPingArticles.size()),
							dianPingArticles);
					articleXML = WeixinUtil.newsMessageToXml(newsMessage);
					out.print(articleXML);
					MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
				}
			}

			// 图片
			else if (WeixinUtil.REQ_MESSAGE_TYPE_IMAGE.equals(msgType)) {
				String picUrl = requestMap.get("PicUrl");
				log.info("『图片消息』【{}】【{}】", fromUserName, picUrl);
				MessageDBUtil.insertReqImageMsg(new ReqImageMessage(
						fromUserName, dbRequestCreateTime, msgId, picUrl));

				String returnStr = FaceService.queryFace(picUrl);
				// 截取
				returnStr = XiaoDaoUtil.byteSubstring(returnStr);
				// 回复文本消息
				RespTextMessage textMessage = new RespTextMessage(toUserName,
						fromUserName, dbResponseCreateTime, "0", returnStr);
				out.print(WeixinUtil.textMessageToXml(textMessage));
				MessageDBUtil.insertRespTextMsg(textMessage);
			}

			// 音频
			else if (WeixinUtil.REQ_MESSAGE_TYPE_VOICE.equals(msgType)) {
				log.info("『音频消息』【{}】", fromUserName);

				ReqTextMessage reqTextMessage = new ReqTextMessage(
						fromUserName, dbRequestCreateTime, msgId, "音频信息");
				reqTextMessage.setMsgType(WeixinUtil.REQ_MESSAGE_TYPE_VOICE);
				MessageDBUtil.insertReqTextMsg(reqTextMessage);

				// 回复文本消息
				RespTextMessage textMessage = new RespTextMessage(toUserName,
						fromUserName, dbResponseCreateTime, "0",
						WeixinUtil.MSG_CONTENT_VOICE);
				out.print(WeixinUtil.textMessageToXml(textMessage));
				MessageDBUtil.insertRespTextMsg(textMessage);
			}

			// 事件
			else if (WeixinUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
				String event = requestMap.get("Event");
				String eventKey = requestMap.get("EventKey");

				MessageDBUtil.insertReqEventMsg(new ReqEventMessage(
						fromUserName, dbRequestCreateTime, event, eventKey));

				if ("subscribe".equals(event)) {
					log.info("『订阅消息』【{}】", fromUserName);
					// 回复文本消息
					RespTextMessage textMessage = new RespTextMessage(
							toUserName, fromUserName, dbResponseCreateTime,
							"0", WeixinUtil.MSG_CONTENT_WELCOME);
					out.print(WeixinUtil.textMessageToXml(textMessage));
					MessageDBUtil.insertRespTextMsg(textMessage);

					User user = null;
					if (UserDBUtil.isUserExist(fromUserName)) {
						user = new User(fromUserName, "", "", "",
								dbRequestCreateTime, WeixinUtil.IS_SUBSCRIBE,
								"", "", "", "", "", "", "");
						UserDBUtil.updateUser(user);
						UserDBUtil.updateLog(fromUserName, dbRequestCreateTime
								+ "\n重新关注\n\n");
					} else {
						user = new User(fromUserName, "", "",
								dbRequestCreateTime, dbRequestCreateTime,
								WeixinUtil.IS_SUBSCRIBE, "", "", "", "", "",
								"", dbRequestCreateTime + "\n第一次关注\n\n");
						UserDBUtil.insertUser(user);
					}
				}
				// unsubscribe(取消订阅)
				else if ("unsubscribe".equals(event)) {
					User user = null;
					if (UserDBUtil.isUserExist(fromUserName)) {
						user = new User(fromUserName, "", "", "",
								dbRequestCreateTime, WeixinUtil.IS_UNSUBSCRIBE,
								"", "", "", "", "", "", "");
						UserDBUtil.updateUser(user);
						UserDBUtil.updateLog(fromUserName, dbRequestCreateTime
								+ "\n取消关注\n\n");
					} else {
						user = new User(fromUserName, "", "",
								dbRequestCreateTime, dbRequestCreateTime,
								WeixinUtil.IS_UNSUBSCRIBE, "", "", "", "", "",
								"", dbRequestCreateTime + "\n取消关注\n\n");
						UserDBUtil.insertUser(user);
					}
				} else if ("CLICK".equals(event)) {
					RespTextMessage textMessage = new RespTextMessage(toUserName, fromUserName, dbResponseCreateTime, "0", "");
					if(eventKey.equals("11")){//听听歌曲2
						textMessage.setContent(WeixinUtil.MSG_CONTENT_MUSIC_HELP);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("12")){//看看天气3
						if(MsgCacheDBUtil.isMsgCacheExist(fromUserName)){
							MsgCache msgCache = MsgCacheDBUtil.getMsgCacheByOpenID(fromUserName);
							String msgCacheWeather = msgCache.getWeather();
							if(msgCacheWeather != null && !msgCacheWeather.equals("")){
								String cityCode = WeatherService.findCityCodeByName(msgCacheWeather);
								WeatherJson weather = WeatherService.queryWeather(cityCode);
								// 根据天气信息组装图文消息
								List<Article> articles = WeatherService.makeArticlesByWeather(weather, msgCacheWeather);
								RespNewsMessage newsMessage = new RespNewsMessage(
										toUserName, fromUserName, dbResponseCreateTime,
										"0", String.valueOf(articles.size()), articles);
								articleXML = WeixinUtil.newsMessageToXml(newsMessage);
								out.print(articleXML);
								MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
							}else{
								textMessage.setContent(WeixinUtil.MSG_CONTENT_WEATHER_HELP);
								out.print(WeixinUtil.textMessageToXml(textMessage));
								MessageDBUtil.insertRespTextMsg(textMessage);
							}
						}else{
							textMessage.setContent(WeixinUtil.MSG_CONTENT_WEATHER_HELP);
							out.print(WeixinUtil.textMessageToXml(textMessage));
							MessageDBUtil.insertRespTextMsg(textMessage);
						}
					}else if(eventKey.equals("13")){//找找美食4
						textMessage.setContent(WeixinUtil.MSG_CONTENT_DIANPING_HELP);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("14")){//查查快递9
						if(MsgCacheDBUtil.isMsgCacheExist(fromUserName)){
							MsgCache msgCache = MsgCacheDBUtil.getMsgCacheByOpenID(fromUserName);
							String msgCacheExpress = msgCache.getExpress();
							if(msgCacheExpress != null && !msgCacheExpress.equals("")){
								String[] expressArr = msgCacheExpress.split("@");
								String order_num = expressArr[0].trim();
								String expressName = msgCacheExpress.substring(expressArr[0].length() + 1).trim();
								textMessage.setContent(ExpressService.queryExpress(order_num, expressName, fromUserName));
								out.print(WeixinUtil.textMessageToXml(textMessage));
								MessageDBUtil.insertRespTextMsg(textMessage);
							}else{
								textMessage.setContent(WeixinUtil.MSG_CONTENT_EXPRESS_HELP);
								out.print(WeixinUtil.textMessageToXml(textMessage));
								MessageDBUtil.insertRespTextMsg(textMessage);
							}
						}else{
							textMessage.setContent(WeixinUtil.MSG_CONTENT_EXPRESS_HELP);
							out.print(WeixinUtil.textMessageToXml(textMessage));
							MessageDBUtil.insertRespTextMsg(textMessage);
						}
					}else if(eventKey.equals("15")){//测测人脸8
						textMessage.setContent(WeixinUtil.MSG_CONTENT_FACE_HELP);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("21")){//找空教室01
						textMessage.setContent(WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_HELP);
						//textMessage.setContent(WeixinUtil.MSG_CONTENT_SHMTU_CLASSROOM_HELP_19);
						//textMessage.setContent("同学们：找空教室的功能暂时关闭，待补选结束之后再开放。\n\n-陈小道");
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("22")){//教务公告02
						List<Article> articles = ShmtuService.makeArticlesForJWC();
						RespNewsMessage newsMessage = new RespNewsMessage(toUserName, fromUserName, dbResponseCreateTime, "0", String.valueOf(articles.size()), articles);
						articleXML = WeixinUtil.newsMessageToXml(newsMessage);
						out.print(articleXML);
						MessageDBUtil.insertRespNewsMsg(newsMessage, articleXML);
					}else if(eventKey.equals("23")){//我的课表11
						String str[] = ShmtuAuthService.getClassSchedule(fromUserName);
						if (!str[0].equals("0")) {
							str[1] = UserDBUtil.getUserDataByOpenID(fromUserName, "student_info") + "你本学期课表如下：\n\n" + str[1];
						}
						str[1] = XiaoDaoUtil.byteSubstring(str[1]);
						textMessage.setContent(str[1]);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("24")){//已修课程12
						String str[] = ShmtuAuthService.getPassScore(fromUserName);
						if (!str[0].equals("0")) {
							str[1] = UserDBUtil.getUserDataByOpenID(fromUserName, "student_info") + "你已修的课程如下：\n\n" + str[1];
						}
						str[1] = XiaoDaoUtil.byteSubstring(str[1]);
						textMessage.setContent(str[1]);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("25")){//最新成绩13
						String str[] = ShmtuAuthService.getNewScore(fromUserName);
						if (!str[0].equals("0")) {
							str[1] = UserDBUtil.getUserDataByOpenID(fromUserName, "student_info") + "你本学期的成绩如下：\n\n" + str[1];
						}
						str[1] = XiaoDaoUtil.byteSubstring(str[1]);
						textMessage.setContent(str[1]);
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}else if(eventKey.equals("30")){//更多服务h
						textMessage.setContent(WeixinUtil.getHelpMsgContentForMenu());
						out.print(WeixinUtil.textMessageToXml(textMessage));
						MessageDBUtil.insertRespTextMsg(textMessage);
					}

				}
			}

			// 其它类型
			else {
				System.out.println(msgType);
				log.info("『其他未知类型的消息』【{}】", fromUserName);
				// 回复文本消息
				// RespTextMessage textMessage = new RespTextMessage(toUserName,
				// fromUserName, dbResponseCreateTime, "1",
				// WeixinUtil.MSG_CONTENT_OTHER_TYPE);
				// out.print(WeixinUtil.textMessageToXml(textMessage));
				// MessageDBUtil.insertRespTextMsg(textMessage);
			}

		} catch (Exception e) {
			log.error("{}", e);
		}

		out.close();
		out = null;
	}

	public static void main(String[] args) {

	}

}
