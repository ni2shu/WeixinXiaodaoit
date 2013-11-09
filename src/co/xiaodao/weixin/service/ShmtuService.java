package co.xiaodao.weixin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import co.xiaodao.weixin.db.pojo.GetUpSign;
import co.xiaodao.weixin.db.pojo.ShowLove;
import co.xiaodao.weixin.db.util.BaseDBUtil;
import co.xiaodao.weixin.db.util.GetUpSignDBUtil;
import co.xiaodao.weixin.db.util.ShowLoveDBUtil;
import co.xiaodao.weixin.db.util.UserDBUtil;
import co.xiaodao.weixin.message.pojo.Article;
import co.xiaodao.weixin.message.pojo.ShmtuBook;
import co.xiaodao.weixin.message.pojo.ShmtuImageNew;
import co.xiaodao.weixin.message.pojo.ShmtuTitleUrl;
import co.xiaodao.weixin.util.ShmtuUtil;
import co.xiaodao.weixin.util.XiaoDaoUtil;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

/**
 * 海大资讯服务类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ShmtuService {

	public static void main(String[] args) {
		// searchTelephone("信息工程学院");
		System.out.println(searchTelephone("xxgcxy"));
	}

	/**
	 * 获取今天所有表白内容
	 * 
	 * @param date
	 * @return
	 */
	public static String getTodayAllShowLove(String date) {
		String returnStr = "";
		List<ShowLove> showLoveList = ShowLoveDBUtil.getTodayShowLove(date);
		if (showLoveList.size() > 0) {
			returnStr = "今天" + date + "\n\n所有表白信息：\n\n";
			for (ShowLove sl : showLoveList) {
				returnStr += sl.getCreateDateTime() + "\n[" + sl.getFromName()
						+ "]向[" + sl.getToName() + "]表白：\n" + sl.getContent()
						+ "\n\n";
			}
		} else {
			returnStr = "今天" + date + "\n\n还没有人发表白信息，要不你来发一个[调皮]";
		}
		return returnStr;
	}

	/**
	 * 获取指定人的表白信息
	 * 
	 * @param name
	 * @param isFormName
	 * @return
	 */
	public static String getShowLoveByNameToStr(String name, boolean isFormName) {
		String returnStr = "";
		List<ShowLove> showLoveList = ShowLoveDBUtil.getShowLoveByName(name,
				isFormName);
		if (showLoveList.size() > 0) {
			if (isFormName) {
				returnStr = "[" + name + "]发出的表白信息：\n\n";
				for (ShowLove sl : showLoveList) {
					returnStr += sl.getCreateDateTime() + "\n["
							+ sl.getFromName() + "]向[" + sl.getToName() + "]表白：\n"
							+ sl.getContent() + "\n\n";
				}
			} else {
				returnStr = "[" + name + "]收到的表白信息：\n\n";
				for (ShowLove sl : showLoveList) {
					returnStr += sl.getCreateDateTime() + "\n["
							+ sl.getFromName() + "]向[" + sl.getToName() + "]表白：\n"
							+ sl.getContent() + "\n\n";
				}
			}
		} else {
			if (isFormName) {
				returnStr = "『"
						+ name
						+ "』还没有发表任何表白信息，可能TA是一个比较内向的同学，要不你去叫TA来发个表白，说不定TA的表白对象会看到哦[调皮]";
			} else {
				returnStr = "『"
						+ name
						+ "』还没有收到任何表白信息，不过将来一定会有的，为什么你对TA有没有收到表白信息感兴趣呢，难道......[调皮]";
			}
		}
		return returnStr;
	}

	/**
	 * 早起排行榜
	 * 
	 * @return
	 */
	public static String getTodayAllSignTop(String date) {
		String returnStr = "";
		List<GetUpSign> getUpSignList = GetUpSignDBUtil.getTodayAllSign(date);
		String nameTemp = "";
		int topNum = 1;
		for (GetUpSign list : getUpSignList) {
			nameTemp = UserDBUtil.getUserDataByOpenID(list.getOpenID(), "name");
			if (nameTemp != null && !nameTemp.equals("")) {
				returnStr += "TOP" + topNum + " " + nameTemp + " "
						+ list.getTime() + "\n";
				topNum++;
			}
			if (topNum == 11) {
				break;
			}
		}
		if (!returnStr.equals("")) {
			returnStr = date + "早起排行榜\n\n" + returnStr + "\n";
		}
		return returnStr;
	}

	/**
	 * 电话查询
	 * 
	 * @param keyword
	 * @return
	 */
	public static String searchTelephone(String keyword) {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

		PostMethod postMethod = new PostMethod("http://58.40.126.8/tel/");
		String returnStr = "";
		try {
			NameValuePair[] data = { new NameValuePair("keyword", keyword) };
			postMethod.setRequestBody(data);
			StringBuffer returnStrBuffer = new StringBuffer();
			httpClient.executeMethod(postMethod);
			InputStream inputStream = postMethod.getResponseBodyAsStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				returnStrBuffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			String strTemp = returnStrBuffer.toString();
			Pattern p1 = Pattern
					.compile("(.*)(<th>部门</th><th>OFFICE</th><th>PHONE</th>)(.*)(<script type=\"text/javascript\">)(.*)");
			Matcher m1 = p1.matcher(strTemp);
			if (!strTemp.equals("")
					&& m1.matches()
					&& !(m1.group(3).trim())
							.equals("</tr>            </table>    </div>")) {
				Pattern p2 = Pattern
						.compile("(.*)(<tr class=)(.*)(<td>)(.*)(</td><td>)(.*)(</td><td><a href=\"tel:)(.*)(\">)(.*)");
				Matcher m2 = null;
				returnStr += "关键字：" + keyword + "\n\n电话查询结果：\n\n";
				for (String info : (m1.group(3).trim()).split("</tr>")) {
					m2 = p2.matcher(info);
					if (m2.matches()) {
						returnStr += "部门：" + m2.group(5).trim() + "\n办公室："
								+ m2.group(7).trim() + "\n电话："
								+ m2.group(9).trim();
						returnStr += "\n\n";
					}
				}
			} else {
				returnStr = "关键字：" + keyword + "\n\n电话查询结果：无\n\n请尝试换一个关键字再试试。";
			}
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return returnStr;
	}

	// 食堂-海馨
	public static List<Article> makeCanteenHXForNewspaper() {
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  海馨楼食堂");
		article.setUrl("");
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/HX2.jpg");
		articles.add(article);

		Article article1 = new Article();
		article1.setTitle("海馨楼A1食堂");
		article1.setUrl(ShmtuUtil.CANTEEN_URL_A1);
		article1.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/A1.jpg");
		articles.add(article1);

		Article article2 = new Article();
		article2.setTitle("海馨楼A2食堂");
		article2.setUrl(ShmtuUtil.CANTEEN_URL_A2);
		article2.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/A2.jpg");
		articles.add(article2);

		Article article3 = new Article();
		article3.setTitle("海馨楼A3食堂");
		article3.setUrl(ShmtuUtil.CANTEEN_URL_A3);
		article3.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/A3.jpg");
		articles.add(article3);

		Article article4 = new Article();
		article4.setTitle("海馨楼A4食堂");
		article4.setUrl(ShmtuUtil.CANTEEN_URL_A4);
		article4.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/A4.jpg");
		articles.add(article4);

		Article article5 = new Article();
		article5.setTitle("海馨楼A5清真");
		article5.setUrl(ShmtuUtil.CANTEEN_URL_A5);
		article5.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/A5.jpg");
		articles.add(article5);

		return articles;
	}

	// 食堂-海琴
	public static List<Article> makeCanteenHQForNewspaper() {
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  海琴楼食堂");
		article.setUrl("");
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/HQ2.jpg");
		articles.add(article);

		Article article1 = new Article();
		article1.setTitle("海琴楼B1食堂");
		article1.setUrl(ShmtuUtil.CANTEEN_URL_B1);
		article1.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/B1.jpg");
		articles.add(article1);

		Article article2 = new Article();
		article2.setTitle("海琴楼B2食堂");
		article2.setUrl(ShmtuUtil.CANTEEN_URL_B2);
		article2.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/B2.jpg");
		articles.add(article2);

		Article article3 = new Article();
		article3.setTitle("海琴楼B3食堂");
		article3.setUrl(ShmtuUtil.CANTEEN_URL_B3);
		article3.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/B3.jpg");
		articles.add(article3);

		return articles;
	}

	// 食堂-海联
	public static List<Article> makeCanteenHLForNewspaper() {
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学 海联楼食堂");
		article.setUrl("");
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/HL2.jpg");
		articles.add(article);

		Article article1 = new Article();
		article1.setTitle("海联楼C1食堂");
		article1.setUrl(ShmtuUtil.CANTEEN_URL_C1);
		article1.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/C1.jpg");
		articles.add(article1);

		Article article2 = new Article();
		article2.setTitle("海联楼C2自选");
		article2.setUrl(ShmtuUtil.CANTEEN_URL_C2);
		article2.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/C2zx.jpg");
		articles.add(article2);

		Article article3 = new Article();
		article3.setTitle("海联楼C2招待");
		article3.setUrl(ShmtuUtil.CANTEEN_URL_C3);
		article3.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_canteen/C2zd.jpg");
		articles.add(article3);

		return articles;
	}

	// 最新广播
	public static ShmtuImageNew extractBroadcast() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_BROADCAST,
				"gb2312");
		Pattern p1 = Pattern
				.compile("(.*)(<div class=\"container\">)(.*)(<div class=\"showcase\">)(.*)(<div class=\"page_nav\">)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<div class=\"pack pack_video_card\"><div class=\"pic\"><a class=\"inner\" target=\"new\" title=\")(.*)(\" href=\")(.*)(\"><img width=)(.*)(<ul class=\"info\"><li>)(.*)(</li><li>时间: )(.*)(</li><li>)(.*)");
		Matcher m2 = null;
		ShmtuImageNew shmtuImageNew = new ShmtuImageNew();
		if (m1.matches()) {
			for (String info : m1.group(5).split("<div class=\"item\">")) {
				m2 = p2.matcher(info);
				if (m2.matches()) {
					shmtuImageNew.setDate(m2.group(11)
							.replaceAll("&nbsp;", " ").trim());
					shmtuImageNew.setTitle(m2.group(3)
							.replaceAll("&nbsp;", " ").trim());
					String link = m2.group(5).replaceAll("&nbsp;", " ").trim();
					shmtuImageNew.setLink(link);
					break;
				}
			}
		}
		return shmtuImageNew;
	}

	public static List<Article> makeArticlesForBroadcast() {
		ShmtuImageNew shmtuImageNew = extractBroadcast();
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  最新广播节目");
		article.setDescription("本期话题：" + shmtuImageNew.getTitle() + "\n发布时间："
				+ shmtuImageNew.getDate() + "\n为了有更好的体验，请在WIFI环境下打开。");
		article.setUrl(shmtuImageNew.getLink());
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_broadcast_logo7.jpg");
		articles.add(article);
		return articles;
	}

	// 最新院报
	public static ShmtuImageNew extractNewspaper() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_NEWSPAPER,
				"utf-8");
		Pattern p1 = Pattern
				.compile("(.*)(<div class=\"view-content\">)(.*)(<ul>)(.*)(</ul>)(.*)(</div>)(.*)(<div id=\"footer\">)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<li class=\"views-row)(.*)(clearfix\">)(.*)(<a href=\")(.*)(\" target=\"_blank\">)(.*)(</a>)(.*)(<span class=\"date-display-single\">)(.*)(</span>)(.*)");
		Matcher m2 = null;
		ShmtuImageNew shmtuImageNew = new ShmtuImageNew();
		if (m1.matches()) {
			for (String info : m1.group(5).split("</li>")) {
				m2 = p2.matcher(info);
				if (m2.matches()) {
					shmtuImageNew.setTitle(m2.group(9)
							.replaceAll("&nbsp;", " ").trim());
					shmtuImageNew.setDesc(m2.group(13)
							.replaceAll("&nbsp;", " ").trim());
					break;
				}
			}
		}
		return shmtuImageNew;
	}

	public static List<Article> makeArticlesForNewspaper() {
		ShmtuImageNew shmtuImageNew = extractNewspaper();
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  最新校报");
		article.setDescription("总第" + shmtuImageNew.getTitle() + "期『"
				+ shmtuImageNew.getDesc()
				+ "』\n为了有更好的体验，请在WIFI环境下打开。\n打不开可能与设备的操作系统有关。");
		article.setUrl(ShmtuUtil.SHMTU_URL_NEWSPAPER2.replace("{num}",
				shmtuImageNew.getTitle()+"qi_"));
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_newspaper_title1.jpg");
		articles.add(article);
		return articles;
	}

	// 新书通报
	public static List<ShmtuBook> searchNewBook() {
		List<ShmtuBook> newBookList = new ArrayList<ShmtuBook>();

		try {
			String requestUrl = ShmtuUtil.SHMTU_URL_LAB_NEW_BOOKS_RRS;
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			InputStream inputStream = httpUrlConn.getInputStream();
			newBookList = parseBookFromInputStream(inputStream, true);
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBookList;
	}

	public static List<Article> makeArticlesForNewBook() {
		List<ShmtuBook> newBookList = searchNewBook();
		List<Article> articles = new ArrayList<Article>();
		if (newBookList.size() > 0) {
			Article article = new Article();
			article.setTitle("上海海事大学图书馆新书通报");
			article.setDescription("");
			article.setUrl(ShmtuUtil.SHMTU_URL_LAB_NEW_BOOKS);
			article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
					+ "/WeixinXiaodaoit/image/smu_lab6.jpg");
			articles.add(article);

			Article articleTemp = null;
			int count = 0;
			if (newBookList.size() == ShmtuUtil.ARTICLES_COUNTS_BOOK) {
				count = ShmtuUtil.ARTICLES_COUNTS_BOOK - 1;
			} else {
				count = newBookList.size();
			}
			for (int i = 0; i < count; i++) {
				articleTemp = new Article();
				articleTemp.setTitle("书名：" + newBookList.get(i).getTitle()
						+ "\n" + "出版时间：" + newBookList.get(i).getPubDate()
						+ "\n" + newBookList.get(i).getDescription().trim());
				articleTemp.setDescription("");
				articleTemp.setUrl(newBookList.get(i).getLink());
				articleTemp.setPicUrl("");
				articles.add(articleTemp);
				articleTemp = null;
			}
			if (newBookList.size() == ShmtuUtil.ARTICLES_COUNTS_BOOK) {
				Article article9 = new Article();
				article9.setTitle("点这里显示更多新书......\n");
				article9.setDescription("");
				article9.setUrl(ShmtuUtil.SHMTU_URL_LAB_NEW_BOOKS);
				article9.setPicUrl("");
				articles.add(article9);
			}
		}
		return articles;
	}

	// 海大所有资讯
	public static String shmtuGetAll() {
		int count = 2;
		String returnStr = "";
		String imageNewsStr = "-『海大最新图片新闻』-\n";
		String newsStr = "-『海大最新校内动态』-\n";
		String notesStr = "-『海大最新通知公告』-\n";

		// 图片新闻
		List<ShmtuImageNew> listImageNews = extractImageNews();

		for (int i = 0; i < count; i++) {
			imageNewsStr += "[" + String.valueOf(i + 1) + "] "
					+ listImageNews.get(i).getDate() + "  \n" + "<a href=\""
					+ listImageNews.get(i).getLink() + "\">"
					+ listImageNews.get(i).getTitle() + "</a>\n";
		}
		imageNewsStr += "\n";
		// 校内动态
		List<ShmtuTitleUrl> listNews = extractNews();
		for (int i = 0; i < count; i++) {
			newsStr += "[" + String.valueOf(i + 1) + "] "
					+ listNews.get(i).getDate() + "  \n" + "<a href=\""
					+ listNews.get(i).getUrl() + "\">"
					+ listNews.get(i).getTitle() + "</a>\n";
		}
		newsStr += "\n";
		// 通知公告
		List<ShmtuTitleUrl> listNotes = extractNotes();
		for (int i = 0; i < count; i++) {
			notesStr += "[" + String.valueOf(i + 1) + "] "
					+ listNotes.get(i).getDate() + "  \n" + "<a href=\""
					+ listNotes.get(i).getUrl() + "\">"
					+ listNotes.get(i).getTitle() + "</a>\n"
					+ listNotes.get(i).getDepartment() + "\n";
		}
		returnStr = imageNewsStr + newsStr + notesStr;
		return returnStr;
	}

	// 海大通知公告
	public static List<ShmtuTitleUrl> extractNotes() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_NOTES,
				"utf-8");
		Pattern p1 = Pattern.compile("(.*)(<tbody>)(.*)(</tbody>)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<tr class=\")(.*)(class=\"date-display-single\">)(.*)(</span></span>)(.*)(<div><a href=\")(.*)(\">)(.*)(</a></div>)(.*)(<td class=\"department\")(.*)(<div>)(.*)(</div>)(.*)");
		Matcher m2 = null;
		List<ShmtuTitleUrl> list = new ArrayList<ShmtuTitleUrl>();
		if (m1.matches()) {
			for (String info : m1.group(3).split("</tr>")) {
				m2 = p2.matcher(info);
				if (m2.matches()) {
					list.add(new ShmtuTitleUrl(m2.group(11).replaceAll(
							"&nbsp;", " "), ShmtuUtil.SHMTU_URL_INDEX
							+ m2.group(9).replaceAll("&nbsp;", " "), m2
							.group(5).replaceAll("&nbsp;", " "), "发布部门："
							+ m2.group(17).replaceAll("&nbsp;", " ")));
					if (list.size() == ShmtuUtil.ARTICLES_COUNTS) {
						break;
					}
				}

			}
		}
		return list;
	}

	public static List<Article> makeArticlesForNotes() {
		List<ShmtuTitleUrl> list = extractNotes();

		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  通知公告");
		article.setDescription("");
		article.setUrl(ShmtuUtil.SHMTU_URL_NOTES);
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_gate.jpg");
		articles.add(article);

		Article articleTemp = null;
		for (int i = 0; i < list.size(); i++) {
			articleTemp = new Article();
			articleTemp.setTitle(list.get(i).getDate() + "\n"
					+ list.get(i).getTitle() + "\n"
					+ list.get(i).getDepartment());
			articleTemp.setDescription("");
			articleTemp.setUrl(list.get(i).getUrl());
			articleTemp.setPicUrl("");
			articles.add(articleTemp);
			articleTemp = null;
		}
		return articles;
	}

	// 海大校园动态
	public static List<ShmtuTitleUrl> extractNews() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_NEWS,
				"utf-8");
		Pattern p1 = Pattern
				.compile("(.*)(<div class=\"view-content\">)(.*)(<h2 class=\"element-invisible\">)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<li class=\"views-row)(.*)(<span class=\"title\"><a href=\")(.*)(\">)(.*)(</a></span>)(.*)(class=\"date-display-single\">)(.*)(</span></span>)(.*)");
		Matcher m2 = null;
		List<ShmtuTitleUrl> list = new ArrayList<ShmtuTitleUrl>();
		if (m1.matches()) {
			for (String info : m1.group(3).split("</li>")) {
				m2 = p2.matcher(info);
				if (m2.matches()) {
					list.add(new ShmtuTitleUrl(m2.group(7).replaceAll("&nbsp;",
							" "), ShmtuUtil.SHMTU_URL_INDEX
							+ m2.group(5).replaceAll("&nbsp;", " "), m2.group(
							11).replaceAll("&nbsp;", " ")));
					if (list.size() == ShmtuUtil.ARTICLES_COUNTS) {
						break;
					}
				}

			}
		}
		return list;
	}

	public static List<Article> makeArticlesForNews() {
		List<ShmtuTitleUrl> list = extractNews();

		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  校园动态");
		article.setDescription("");
		article.setUrl(ShmtuUtil.SHMTU_URL_NEWS);
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_gate.jpg");
		articles.add(article);

		Article articleTemp = null;
		for (int i = 0; i < list.size(); i++) {
			articleTemp = new Article();
			articleTemp.setTitle(list.get(i).getDate() + "\n"
					+ list.get(i).getTitle());
			articleTemp.setDescription("");
			articleTemp.setUrl(list.get(i).getUrl());
			articleTemp.setPicUrl("");
			articles.add(articleTemp);
			articleTemp = null;
		}
		return articles;
	}

	// 海大图片新闻
	public static List<ShmtuImageNew> extractImageNews() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_IMAGE_NEWS,
				"utf-8");
		Pattern p1 = Pattern
				.compile("(.*)(<div class=\"view-content\">)(.*)(<h2 class=\"element-invisible\">)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<li class=\"views-row)(.*)(<img src=\")(.*)(\" width=)(.*)(<div class=\"title\"><a href=\")(.*)(\">)(.*)(</a></div>)(.*)(<span class=\"abstract\">(.*)(</span>))(.*)(class=\"date-display-single\">)(.*)(</span></span>)(.*)");
		Matcher m2 = null;
		List<ShmtuImageNew> list = new ArrayList<ShmtuImageNew>();
		if (m1.matches()) {
			for (String info : m1.group(3).split("</li>")) {
				m2 = p2.matcher(info);
				if (m2.matches()) {
					list.add(new ShmtuImageNew(m2.group(11).replaceAll(
							"&nbsp;", " "), m2.group(15).replaceAll("&nbsp;",
							" "), ShmtuUtil.SHMTU_URL_INDEX
							+ m2.group(9).replaceAll("&nbsp;", " "), m2
							.group(5).replaceAll("&nbsp;", " "), m2.group(19)
							.replaceAll("&nbsp;", " ")));
					if (list.size() == ShmtuUtil.ARTICLES_COUNTS) {
						break;
					}
				}
			}
		}
		return list;
	}

	public static List<Article> makeArticlesForImageNews() {
		List<ShmtuImageNew> list = extractImageNews();

		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle(list.get(0).getTitle());
		article.setDescription("");
		article.setUrl(list.get(0).getLink());
		article.setPicUrl(list.get(0).getImageLink());
		articles.add(article);

		Article articleTemp = null;
		for (int i = 1; i < list.size(); i++) {
			articleTemp = new Article();
			articleTemp
					.setTitle(list.get(i).getDate() + "\n"
							+ list.get(i).getTitle() + "\n【摘要】"
							+ list.get(i).getDesc());
			articleTemp.setDescription("");
			articleTemp.setUrl(list.get(i).getLink());
			articleTemp.setPicUrl(list.get(i).getImageLink());
			articles.add(articleTemp);
			articleTemp = null;
		}
		return articles;
	}

	// 图书查询
	public static List<ShmtuBook> searchBook(String bookName) {
		List<ShmtuBook> bookList = new ArrayList<ShmtuBook>();

		try {
			String requestUrl = ShmtuUtil.SHMTU_URL_LAB_SEARCH.replace(
					"{book_name}", URLEncoder.encode(bookName, "utf-8"))
					.replaceAll(" +", "%20");
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			InputStream inputStream = httpUrlConn.getInputStream();
			bookList = parseBookFromInputStream(inputStream, false);
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookList;
	}

	public static List<Article> makeArticlesForBook(String bookName) {
		List<ShmtuBook> bookList = searchBook(bookName);
		List<Article> articles = new ArrayList<Article>();
		if (bookList.size() > 0) {
			Article article = new Article();
			String bookNameTemp = bookName;
			if (bookName.getBytes().length >= 14) {
				bookNameTemp = XiaoDaoUtil.byteSubstring(bookName, 12) + "...";
			}
			article.setTitle("上海海事大学 馆藏书目检索\n" + "书名关键字：" + bookNameTemp);
			article.setDescription("");
			article.setUrl(ShmtuUtil.SHMTU_URL_LAB_INDEX);
			article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
					+ "/WeixinXiaodaoit/image/smu_lab6.jpg");
			articles.add(article);

			Article articleTemp = null;
			int count = 0;
			if (bookList.size() == ShmtuUtil.ARTICLES_COUNTS_BOOK) {
				count = ShmtuUtil.ARTICLES_COUNTS_BOOK - 1;
			} else {
				count = bookList.size();
			}
			for (int i = 0; i < count; i++) {
				articleTemp = new Article();
				articleTemp.setTitle("书名：" + bookList.get(i).getTitle() + "\n"
						+ "出版时间：" + bookList.get(i).getPubDate() + "\n"
						+ bookList.get(i).getDescription().trim());
				articleTemp.setDescription("");
				articleTemp.setUrl(bookList.get(i).getLink());
				articleTemp.setPicUrl("");
				articles.add(articleTemp);
				articleTemp = null;
			}
			if (bookList.size() == ShmtuUtil.ARTICLES_COUNTS_BOOK) {
				Article article9 = new Article();
				article9.setTitle("点这里显示更多检索结果......\n");
				article9.setDescription("");
				try {
					article9.setUrl(ShmtuUtil.SHMTU_URL_LAB_SEARCH_ALL
							.replace("{book_name}",
									URLEncoder.encode(bookName, "utf-8"))
							.replaceAll(" +", "%20"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				article9.setPicUrl("");
				articles.add(article9);
			}
		}
		return articles;
	}

	@SuppressWarnings("unchecked")
	private static List<ShmtuBook> parseBookFromInputStream(
			InputStream inputStream, boolean isForNewBook) {
		List<ShmtuBook> bookList = new ArrayList<ShmtuBook>();
		try {
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到xml根元素
			Element root = document.getRootElement();
			Element channel = root.element(ShmtuUtil.ELEMENT_CHANNEL);
			String isNoFound = channel.element(ShmtuUtil.ELEMENT_CHANNEL_DESC)
					.getText();

			if (!isForNewBook ? isNoFound.equals("检索出的书目列表") : isForNewBook) {
				List<Element> itemList = channel
						.elements(ShmtuUtil.ELEMENT_ITEM);
				String book_title = "";
				String book_link = "";
				String book_author = "";
				String book_pubDate = "";
				String book_desc = "";
				int count = 0;
				for (Element item : itemList) {
					book_title = item.element(ShmtuUtil.ELEMENT_TITLE)
							.getText();
					book_link = item.element(ShmtuUtil.ELEMENT_LINK).getText();
					book_author = item.element(ShmtuUtil.ELEMENT_AUTHOR)
							.getText();
					book_pubDate = item.element(ShmtuUtil.ELEMENT_PUBDATE)
							.getText();
					book_desc = item.element(ShmtuUtil.ELEMENT_DESC).getText();
					bookList.add(new ShmtuBook(book_title, book_link,
							book_author, book_pubDate, book_desc));
					count++;
					if (count == ShmtuUtil.ARTICLES_COUNTS_BOOK) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}

	// 教务公告
	public static List<ShmtuTitleUrl> extractJWC() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_JWC,
				"utf-8");
		Pattern p1 = Pattern
				.compile("(.*)(<div class=\"top_center_list_2\")(.*)(<ul>)(.*)(</ul>)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<li class=\"setTop\">)(.*)(<a target=\"_blank\" href=')(.*)(')(.*)(title=')(.*)('>)(.*)(</a>)(.*)");
		Pattern p3 = Pattern
				.compile("(.*)(<li>)(.*)(<a target=\"_blank\" href=')(.*)(')(.*)(title=')(.*)('>)(.*)(</a>)(.*)");
		Matcher m2 = null;
		Matcher m3 = null;
		List<ShmtuTitleUrl> list = new ArrayList<ShmtuTitleUrl>();
		if (m1.matches()) {
			for (String info : m1.group(5).split("</li>")) {
				m2 = p2.matcher(info);
				m3 = p3.matcher(info);
				if (m2.matches()) {
					list.add(new ShmtuTitleUrl(m2.group(3).replaceAll("&nbsp;",
							" ")
							+ m2.group(9).replaceAll("&nbsp;", " ")
							+ m2.group(13).replaceAll("&nbsp;", " "),
							ShmtuUtil.SHMTU_URL_JWC
									+ m2.group(5).replaceAll("&nbsp;", " ")));
					if (list.size() == ShmtuUtil.ARTICLES_COUNTS) {
						break;
					}
				} else if (m3.matches()) {
					list.add(new ShmtuTitleUrl(m3.group(3).replaceAll("&nbsp;",
							" ")
							+ m3.group(9).replaceAll("&nbsp;", " ")
							+ m3.group(13).replaceAll("&nbsp;", " "),
							ShmtuUtil.SHMTU_URL_JWC
									+ m3.group(5).replaceAll("&nbsp;", " ")));
					if (list.size() == ShmtuUtil.ARTICLES_COUNTS) {
						break;
					}
				}
			}
		}
		return list;
	}

	public static List<Article> makeArticlesForJWC() {
		List<ShmtuTitleUrl> list = extractJWC();

		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("上海海事大学  教务公告");
		article.setDescription("");
		article.setUrl(ShmtuUtil.SHMTU_URL_JWC);
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_gate.jpg");
		articles.add(article);

		Article articleTemp = null;
		for (int i = 0; i < list.size(); i++) {
			articleTemp = new Article();
			articleTemp.setTitle(list.get(i).getTitle());
			articleTemp.setDescription("");
			articleTemp.setUrl(list.get(i).getUrl());
			articleTemp.setPicUrl("");
			articles.add(articleTemp);
			articleTemp = null;
		}
		return articles;
	}

	// 信工就业信息
	public static List<ShmtuTitleUrl> extractXGJY() {
		String html = XiaoDaoUtil.getJsonByHttp(ShmtuUtil.SHMTU_URL_XG_JY,
				"gb2312");
		Pattern p1 = Pattern
				.compile("(.*)(<TABLE)(.*)(信息直通车</span></TD>)(.*)(</TABLE>)(.*)");
		Matcher m1 = p1.matcher(html);
		Pattern p2 = Pattern
				.compile("(.*)(<a href=\")(.*)(\" target=\"_blank\">)(.*)(</a></div>)(.*)");
		Matcher m2 = null;
		List<ShmtuTitleUrl> list = new ArrayList<ShmtuTitleUrl>();
		if (m1.matches()) {
			for (String info : m1.group(5).split("<div class=\"div2\">")) {
				m2 = p2.matcher(info);
				if (m2.matches()) {
					list.add(new ShmtuTitleUrl(m2.group(5).replaceAll("&nbsp;",
							" "), ShmtuUtil.SHMTU_URL_XG_JY
							+ m2.group(3).replaceAll("&nbsp;", " ")));
					if (list.size() == ShmtuUtil.ARTICLES_COUNTS) {
						break;
					}
				}

			}
		}
		return list;
	}

	public static List<Article> makeArticlesForXGJY() {
		List<ShmtuTitleUrl> list = extractXGJY();

		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("信息工程学院\n就业资讯网  信息直通车");
		article.setDescription("");
		article.setUrl(ShmtuUtil.SHMTU_URL_XG_JY);
		article.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/smu_xgjy5.jpg");
		articles.add(article);

		Article articleTemp = null;
		for (int i = 0; i < list.size(); i++) {
			articleTemp = new Article();
			articleTemp.setTitle(list.get(i).getTitle());
			articleTemp.setDescription("");
			articleTemp.setUrl(list.get(i).getUrl());
			articleTemp.setPicUrl("");
			articles.add(articleTemp);
			articleTemp = null;
		}
		return articles;
	}

}
