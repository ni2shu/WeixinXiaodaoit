package co.xiaodao.weixin.util;

import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.xiaodao.weixin.message.pojo.Article;
import co.xiaodao.weixin.message.response.RespMusicMessage;
import co.xiaodao.weixin.message.response.RespNewsMessage;
import co.xiaodao.weixin.message.response.RespTextMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 微信工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class WeixinUtil {
	
	public static void main(String[] sre){
		System.out.println(SMU_SHOWLOVE_HELP);
	}

	public static final String TOKEN = "xiaodaoit525396193";

	// 是否是请求信息
	public static final String IS_REQUEST = "1";
	public static final String IS_RESPONSE = "0";

	// 是否订阅
	public static final String IS_SUBSCRIBE = "1";
	public static final String IS_UNSUBSCRIBE = "0";

	// QQ表情正则表达式
	public static final String REGEX_QQ_FACE = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";

	// 返回消息类型：文本
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	// 返回消息类型：音乐
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	// 返回消息类型：图文
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	// 请求消息类型：文本
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	// 请求消息类型：图片
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	// 请求消息类型：链接
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	// 请求消息类型：地理位置
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	// 请求消息类型：音频
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	// 请求消息类型：事件
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	// 问题的类型：学校
	public static final String QUESTION_CATEGORY_SCHOOL = "school";
	// 问题的类型：待处理
	public static final String QUESTION_CATEGORY_TEMP = "temp";

	// 欢迎信息
	public static final String MSG_CONTENT_WELCOME = "欢迎关注『小道IT』[鼓掌]\n\n " + getHelpMsgContent();

	// 为雅安使用帮助
	// public static final String MSG_CONTENT_YAAN_HELP =
	// "[爱心]『为雅安』操作指南：\n\n查看灾区救援信息：\n发送：救援+地址\n例如发送：救援雅安\n也可以直接发送：救援\n\n查看灾区寻人信息：\n发送：寻人+人名\n例如发送：寻人马天号\n也可以直接发送：寻人\n\n查看某人是否平安：\n发送：平安+人名\n例如发送：平安刘立才\n也可以直接发送：平安";

	// 听歌曲使用帮助
	public static final String MSG_CONTENT_MUSIC_HELP = "[胜利]『听音乐』操作指南：\n\n发送：歌曲+歌名\n例如：歌曲存在\n\n发送：歌曲+歌名@歌手\n例如：歌曲存在@汪峰\n\n为了让你有更好的体验，请在WIFI环境下使用。";

	// 查天气使用帮助
	public static final String MSG_CONTENT_WEATHER_HELP = "[胜利]『查天气』操作指南：\n\n发送：天气+城市名\n例如：天气上海";

	// 查百科使用帮助
	public static final String MSG_CONTENT_BAIKE_HELP = "[胜利]『查百科』操作指南：\n\n发送：你想查的信息\n例如：微信";

	// 测人脸使用帮助
	public static final String MSG_CONTENT_FACE_HELP = "[胜利]『测人脸』操作指南：\n\n发送一张含有清晰人脸的图片就可以了。";

	// 美食的使用帮助
	public static final String MSG_CONTENT_DIANPING_HELP = "[胜利]『找美食』操作指南：\n\n点击下方信息输入框旁边的『+』按钮，然后选择『位置』，最后发送你的位置信息就可以找美食啦[微笑]";

	// 快递的使用帮助
	public static final String MSG_CONTENT_EXPRESS_HELP = "[胜利]『查快递』操作指南：\n\n发送：快递+单号@公司\n例如发送：\n快递3147403986@圆通";

	// 绑定学号的使用帮助
	public static final String MSG_CONTENT_BIND_STUID_HELP = "发送：绑定+学号@密码\n例如发送：\n绑定200810314021@cdz";

	// 海大的使用帮助_通用
	// public static final String MSG_CONTENT_SHMTU_HELP =
	// "发送【111】找空教室\n发送【222】教务公告\n发送【333】图书检索\n发送【444】新书通报\n发送【555】信工就业\n【666】海大图片新闻\n【777】海大校园动态\n【888】海大通知公告\n【999】海大最新资讯";
	public static final String MSG_CONTENT_SHMTU_HELP = "同学们:发送以下关键字或对应的数字就可以呦.[鼓掌]\n"
			+ "01找空教室  02教务公告\n03图书检索  11课表显示\n12已修课程  13最新成绩\n14早起签到  15向TA表白\n16电话查询  17海大食堂";// \n13最新成绩(需绑定学号)
	
//	public static final String MSG_CONTENT_SHMTU_HELP = "同学们:发送以下关键字或对应的数字就可以呦.[鼓掌]\n"
//		+ "01找空教室  02教务公告\n03图书检索  04新书通报\n05海大新闻  06校园动态\n07通知公告  08信工就业\n09最新校报  10海大广播\n11课表显示\n12已修课程\n13最新成绩[强][鼓掌]\n14早起签到  15向TA表白\n16电话查询  17海大食堂";// \n13最新成绩(需绑定学号)

	
	// 其他实用功能使用帮助
	public static final String MSG_CONTENT_OTHER_HELP = "---『其他实用功能』---\n发送以下关键字或对应的数字都会有结果呦.[鼓掌]\n1休闲   2点歌   3天气\n4美食   5广播   6历史\n7百科   8人脸   9快递";

	// 海大的使用帮助_内部
	public static final String MSG_CONTENT_SHMTU_INTERNAL_HELP = "『海大资讯』操作指南\n\n"
			+ MSG_CONTENT_SHMTU_HELP;

	// 海大的使用帮助_外部
	public static final String MSG_CONTENT_SHMTU_INDEX_HELP = "---『上海海事大学』---\n"
			+ MSG_CONTENT_SHMTU_HELP;

	// 找自习教室简短的使用帮助
	public static final String MSG_CONTENT_SHMTU_CLASSROOM_SHORT_HELP = "发送【空教室现在】\n -->查现在空闲的教室\n"
			+ "发送【空教室全天】\n -->查全天空闲的教室\n"
			+ "发送【空教室上午】\n -->查上午空闲的教室\n"
			+ "发送【空教室下午】\n -->查下午空闲的教室\n"
			+ "发送【空教室晚上】\n -->查晚上空闲的教室\n"
			+ "发送【空教室15】\n -->查第一大节和第五大节空闲的教室，也可以查其他的节次，可用的节次有124567（因为第3大节是午休时间）。";

	// 找自习教室其他的使用帮助
	
	public static final String MSG_CONTENT_SHMTU_CLASSROOM_OTHER_HELP = "发送【空教室全天】\n -->查全天空闲的教室\n"
			+ "发送【空教室上午】\n -->查上午空闲的教室\n"
			+ "发送【空教室下午】\n -->查下午空闲的教室\n"
			+ "发送【空教室晚上】\n -->查晚上空闲的教室\n"
			+ "发送【空教室15】\n -->查第一大节和第五大节空闲的教室，也可以查其他的节次，可用的节次有124567（因为第3大节是午休时间）。";

//	//考试周空教室查询
//	public static final String MSG_CONTENT_SHMTU_CLASSROOM_OTHER_19_HELP = "查询考试周（7月1日至7月5日）的空教室\n\n考试周每天安排四场考试：\n"
//		+ "第一场：08:20—10:05；\n"
//		+ "第二场：10:25—12:10；\n"
//		+ "第三场：13:10—14:55；\n"
//		+ "第四场：15:15—17:00；\n\n"
//		+ "发送【空教室现在】\n -->查现在空闲的教室\n"
//		+ "发送【空教室全天】\n -->查全天空闲的教室\n"
//		+ "发送【空教室上午】\n -->查上午空闲的教室\n"
//		+ "发送【空教室下午】\n -->查下午空闲的教室\n"
//		+ "发送【空教室晚上】\n -->查晚上空闲的教室\n"
//		+ "发送【空教室12】\n -->查询当天第一场和第二场空闲的教室，也可以查其他的场次，可用的场次有1234。";
//	
//	public static final String MSG_CONTENT_SHMTU_CLASSROOM_SHORT_19_HELP = "查询考试周（7月1日至7月5日）的空教室\n\n考试周每天安排四场考试：\n"
//		+ "第一场：08:20—10:05；\n"
//		+ "第二场：10:25—12:10；\n"
//		+ "第三场：13:10—14:55；\n"
//		+ "第四场：15:15—17:00；\n\n"
//		+ "发送【空教室全天】\n -->查全天空闲的教室\n"
//		+ "发送【空教室上午】\n -->查上午空闲的教室\n"
//		+ "发送【空教室下午】\n -->查下午空闲的教室\n"
//		+ "发送【空教室晚上】\n -->查晚上空闲的教室\n"
//		+ "发送【空教室12】\n -->查询当天第一场和第二场空闲的教室，也可以查其他的场次，可用的场次有1234。";
//	
	
	// 找自习教室的使用帮助
	// 你是否有这样的困惑：当你在一个教室里自习的时候，发现这个教室有课，导致你不得不换教室。\n\n
	public static final String MSG_CONTENT_SHMTU_CLASSROOM_HELP = "『找空教室』操作指南\n\n"
			+ MSG_CONTENT_SHMTU_CLASSROOM_SHORT_HELP;
	
//	public static final String MSG_CONTENT_SHMTU_CLASSROOM_HELP_19 = "『找空教室』操作指南\n\n"
//		+ MSG_CONTENT_SHMTU_CLASSROOM_SHORT_19_HELP;

	// 图书查询的使用帮助
	public static final String MSG_CONTENT_SHMTU_LAB_HELP = "『图书检索』操作指南\n\n发送：检索+关键字\n例如：检索iOS";

	// 查天气不存在
	public static final String MSG_CONTENT_ON_FOUND_WEATHER_CITY = "真的存在『{city_name}』这个城市吗？[疑问]可是小道找不到哦！你再检查一下是不是写错啦[微笑]";

	// 音频消息的提示内容
	public static final String MSG_CONTENT_VOICE = "声音小道已经收到了，如有必要，小道会在后台回复你。";

	// 用户发送其它不能识别类型的消息时
	public static final String MSG_CONTENT_OTHER_TYPE = "你发送的信息有点特殊，如有必要，小道会在后台回复你。\n\n发送?查看操作指南。[微笑]";

	// 电话查询帮助
	public final static String SMU_TELEPHONE_COMMON_HELP = "发送：电话+关键字\n\n关键字可以是中文字、英文字母的全部或部分。\n\n例如发送：电话信息工程\n或发送：电话xxgc";
	public final static String SMU_TELEPHONE_HELP = "『电话查询』操作指南\n\n"
			+ SMU_TELEPHONE_COMMON_HELP;

	// 表白帮助
	public final static String SMU_SHOWLOVE_COMMON_HELP = "---『发送表白信息』---\n发送：@表白对象#表白内容#表白人\n例如发送：@小西#我那个什么你#小东"
			+ "\n\n---『查看表白信息』---\n①发送：表白对象+名字\n例如发送：表白对象小西\n -->查看所有向小西表白的信息。\n②发送：表白人+名字\n例如发送：表白人小东\n -->查看小东发出的所有表白信息。\n③发送：表白今天\n -->查看今天的所有表白信息[调皮]";
	public final static String SMU_SHOWLOVE_HELP = "『向TA表白』操作指南\n\n"
			+ SMU_SHOWLOVE_COMMON_HELP;

	/**
	 * 操作指南 小道IT微信号：xiaodaoit
	 * 
	 * @return
	 */
	public static String getHelpMsgContent() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[胜利]『小道IT』操作指南:").append("\n\n");
		// buffer.append("发送【0】为雅安[爱心]").append("\n");
		buffer.append(MSG_CONTENT_SHMTU_INDEX_HELP).append("\n\n");
		buffer.append("发送?返回主菜单").append("\n\n");
		buffer.append(MSG_CONTENT_OTHER_HELP);
		// buffer.append("发送【2】我要点歌").append("\n");
		// buffer.append("发送【3】天气预报").append("\n");
		// buffer.append("发送【4】我是吃货").append("\n");
		// buffer.append("发送【5】听广播").append("\n");
		// buffer.append("发送【6】看历史").append("\n");
		// buffer.append("发送【7】查百科").append("\n");
		// buffer.append("发送【8】测人脸").append("\n");
		// buffer.append("发送【9】查快递").append("\n");
		// buffer.append(MSG_CONTENT_SHMTU_INDEX_HELP).append("\n");
		return buffer.toString();
	}
	
	public static String getHelpMsgContentForMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[胜利]『小道IT』更多服务:").append("\n\n");
		buffer.append("---『上海海事大学』---\n" +
				"同学们:发送以下关键字或对应的数字就可以呦.[鼓掌]\n" +
				"14早起签到  15向TA表白\n" +
				"16电话查询  03图书检索 ").append("\n\n");
		buffer.append("发送?返回主菜单").append("\n\n");
		buffer.append("---『其他实用功能』---\n发送以下关键字或对应的数字都会有结果呦.[鼓掌]\n1休闲 5广播 6历史 7百科");
		return buffer.toString();
	}

	/**
	 * 没有找到歌曲
	 * 
	 * @param song
	 * @param singer
	 * @return
	 */
	public static String notFoundMusic(String song, String singer) {
		if (singer == "") {
			return "歌曲名:『" + song + "』\n这首歌没有找到哦.\n请检查是否输入正确[疑问]\n也可以换首歌试一下.";
		} else {
			return "歌曲名:『" + song + "』\n演唱者:『" + singer
					+ "』\n这首歌没有找到哦.\n请检查是否输入正确[疑问]\n也可以换首歌试一下.";
		}
	}

	/**
	 * 输入信息不完整的提示
	 * 
	 * @return
	 */
	public static String pleaseInputMsg(int key) {
		String weixin_please_input = "";
		switch (key) {
		case 1:
			weixin_please_input = "你忘记发送关键字啦.\n\n发送：检索+关键字\n例如：检索iOS";
			break;
		case 2:
			weixin_please_input = "你忘记发送歌曲名啦.\n\n发送：歌曲+歌名\n例如：歌曲存在\n\n发送：歌曲+歌名@歌手\n例如：歌曲存在@汪峰";
			break;
		case 3:
			weixin_please_input = "你忘记发送城市名啦.\n\n发送：天气+城市名\n例如：天气上海";
			break;
		case 4:
			weixin_please_input = "你发送的信息不完整.\n\n发送：快递+单号@公司\n例如发送：\n快递3147403986@圆通";
			break;
		case 5:
			weixin_please_input = "你发送的信息不完整.\n\n"
					+ MSG_CONTENT_BIND_STUID_HELP;
			break;
		case 6:
			weixin_please_input = "你发送的信息不符合.\n\n"
					+ SMU_SHOWLOVE_COMMON_HELP;
			break;
		}
		return weixin_please_input;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(RespTextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(RespMusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(RespNewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 判断是否是QQ表情
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isQqFace(String content) {
		boolean result = false;
		Pattern p = Pattern.compile(REGEX_QQ_FACE);
		Matcher m = p.matcher(content);
		if (m.matches()) {
			result = true;
		}
		return result;
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = false;

				public void startNode(String name,
						@SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
					cdata = (name.equals("ToUserName")
							|| name.equals("FromUserName")
							|| name.equals("MsgType") || name.equals("Title")
							|| name.equals("Description")
							|| name.equals("MusicUrl")
							|| name.equals("HQMusicUrl") || name.equals("Url")
							|| name.equals("PicUrl") || name.equals("Content"));
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
		String[] arr = new String[] { WeixinUtil.TOKEN, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = XiaoDaoUtil.byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
}
