package co.xiaodao.weixin.util;

/**
 * 海大资讯接口工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-27
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ShmtuUtil {

	// 教务公告
	public static String SHMTU_URL_JWC = "http://jwc.shmtu.edu.cn/";
	// 图书查询
	public static String SHMTU_URL_LAB_SEARCH = "http://58.40.126.44/opac/websearch/bookSearch?cmdACT=resultToRSS&sortSign=pubdate_sort&orderSign=true&pageNo=1&raws=10&filter=%28title%3A{book_name}%29+AND+%28hasholding%3Ay%29";
	// 检索显示页面
	public static String SHMTU_URL_LAB_SEARCH_ALL = "http://58.40.126.44/opac/websearch/bookSearch?pageNo=1&filter=%28title%3A{book_name}%29+AND+%28hasholding%3Ay%29&cmdACT=list&xsl=BOOK_list.xsl&mod=oneXSL&columnID=1&searchSign=&bookType=all%3Aundefined&marcType=&libNUM=1&ISFASTSEARCH=true&hasholding=y&col1=title&val1={book_name}&matching=radiobutton&booktype=all&startPubdate=&endPubdate=&sortSign=pubdate_sort&orderSign=true&raws=10&hasholdingCheckbox=y&multiSelectLibcode=";

	// 图书馆主页
	public static String SHMTU_URL_LAB_INDEX = "http://www.library.shmtu.edu.cn/";
	// 图书馆新书通报RRS页面
	public static String SHMTU_URL_LAB_NEW_BOOKS_RRS = "http://opac.library.shmtu.edu.cn/opac/websearch/WebBookNew?cmdACT=bookNewRSS&limitTime=31&libcode=&classno=&booktype=1&hasholding=y&shouldHaveStates=2&shouldHaveStates=3";
	// 图书馆新书通报Web页面
	public static String SHMTU_URL_LAB_NEW_BOOKS = "http://opac.library.shmtu.edu.cn/opac/websearch/WebBookNew?cmdACT=list_catalogNew&xsl=bookNewSearch.xsl&filter=&columnID=5&cataname=%27%27&showpage=&limitTimeFF=31&libcodeFF=999&classnoFF=&classno=&booktypeFF=1&rows=10&pageNo=1&totalNUM=0&rowNUM=10&totalRows=0&hasholding=y&shouldHaveStates=2&shouldHaveStates=3&picbook=&index_in=&limitTime=31&limitTimeSelect=31&libcode=&booktype=1&index_in_bottom=";

	// 信工就业信息
	public static String SHMTU_URL_XG_JY = "http://cie.shmtu.edu.cn:8080/jiuye/";

	public static String SHMTU_LAB_NO_FIND = "你查询的『{nokeyword}』暂时没有信息。";

	// 海大首页
	public static String SHMTU_URL_INDEX = "http://www.shmtu.edu.cn";
	// 海大图片新闻
	public static String SHMTU_URL_IMAGE_NEWS = "http://www.shmtu.edu.cn/imagenews/";
	// 海大校园动态
	public static String SHMTU_URL_NEWS = "http://www.shmtu.edu.cn/news";
	// 海大通知公告
	public static String SHMTU_URL_NOTES = "http://www.shmtu.edu.cn/notes";
	// 海大最新院报
	public static String SHMTU_URL_NEWSPAPER = "http://shmtu.edu.cn/newspaper";
	public static String SHMTU_URL_NEWSPAPER2 = "http://www.shmtu.edu.cn/sites/shmtu.edu.cn/files/files/newspaper/{num}.pdf";

	// 海大最新广播
	public static String SHMTU_URL_BROADCAST = "http://www.tudou.com/home/item_u110357186s0p1.html";

	// 图文条数
	public final static int ARTICLES_COUNTS = 5;
	public final static int ARTICLES_COUNTS_BOOK = 6;

	public final static String ELEMENT_RRS = "rrs";

	public final static String ELEMENT_CHANNEL = "channel";

	public final static String ELEMENT_ITEM = "item";

	public final static String ELEMENT_TITLE = "title";

	public final static String ELEMENT_LINK = "link";

	public final static String ELEMENT_AUTHOR = "author";

	public final static String ELEMENT_PUBDATE = "pubDate";

	public final static String ELEMENT_DESC = "description";

	public final static String ELEMENT_CHANNEL_DESC = "description";

	public final static String CANTEEN_HELP = "『海大食堂』查询指南\n\n同学们:发送以下的关键字或对应的数字就可以呦.[鼓掌]\n\n171 海馨\n172 海琴\n173 海联";

	public final static String CANTEEN_URL_BASE = "http://lsc.shmtu.edu.cn/content/ContentServlet.html?method=getNewsInfo2&id=";

	public final static String CANTEEN_URL_A1 = CANTEEN_URL_BASE + "hxa1st";
	public final static String CANTEEN_URL_A2 = CANTEEN_URL_BASE + "hxa2st";
	public final static String CANTEEN_URL_A3 = CANTEEN_URL_BASE + "hxa3st";
	public final static String CANTEEN_URL_A4 = CANTEEN_URL_BASE + "hxa4st";
	public final static String CANTEEN_URL_A5 = CANTEEN_URL_BASE + "hxa5st";

	public final static String CANTEEN_URL_B1 = CANTEEN_URL_BASE + "hqb1st";
	public final static String CANTEEN_URL_B2 = CANTEEN_URL_BASE + "hqb2st";
	public final static String CANTEEN_URL_B3 = CANTEEN_URL_BASE + "hqb3st";

	public final static String CANTEEN_URL_C1 = CANTEEN_URL_BASE + "hlc1st";
	public final static String CANTEEN_URL_C2 = CANTEEN_URL_BASE + "hlc2zx";
	public final static String CANTEEN_URL_C3 = CANTEEN_URL_BASE + "hlc2zd";

	public static void main(String[] args) {

	}

}
