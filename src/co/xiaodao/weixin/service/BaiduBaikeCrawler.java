package co.xiaodao.weixin.service;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.xiaodao.weixin.util.XiaoDaoUtil;

/**
 * 百度百科数据的爬取器
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class BaiduBaikeCrawler {

	/**
	 * 百度百科查询
	 * 
	 * @return
	 */
	public static String queryBaike(String word, boolean isGetSql) {
		String baikeStr = null;
		try {
			String requestUrl = "http://baike.baidu.com/search/word?enc=utf8&sug=1&pic=1&word="
					+ URLEncoder.encode(word, "utf-8");
			String returnStr = XiaoDaoUtil.getJsonByHttp(requestUrl, "utf-8");
			baikeStr = extract(returnStr);
			if (isGetSql && baikeStr != null && !baikeStr.equals("")) {
				baikeStr = "INSERT INTO `tb_baike`(question, answer) VALUES ('"
						+ word + "','" + baikeStr + "');";
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("-------------------------------");
		}
		if (isGetSql) {
			System.out.println(baikeStr);
		}
		return baikeStr;
	}

	/**
	 * 从html中抽取信息
	 * 
	 * @param html
	 * @return
	 */
	private static String extract(String html) {
		String result = null;
		Pattern p = Pattern
				.compile("(.*)(<div class=\"card-summary-content\"><div class=\"para\">)(.*?)(</div>)(.*)");
		Matcher m = p.matcher(html);
		if (m.matches()) {
			result = m.group(3).replaceAll("<[^>]*>", "")
					.replaceAll("[(/>)<]", "");
		}
		return result;
	}

}
