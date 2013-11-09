package co.xiaodao.weixin.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.xiaodao.weixin.message.pojo.Music;
import co.xiaodao.weixin.util.BaiduMusicUtil;

/**
 * 百度音乐搜索服务
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class BaiduMusicService {
	private static Logger log = LoggerFactory
			.getLogger(BaiduMusicService.class);

	/**
	 * 根据名称搜索音乐
	 * 
	 * @param musicTitle
	 *            音乐名称
	 * @return
	 */
	public static Music searchMusic(String musicTitle, String musicAuthor) {
		Music music = null;
		// http://box.zhangmen.baidu.com/x?op=12&count=1&title=%E5%AD%98%E5%9C%A8$$$$$$
		// 百度音乐搜索的地址（歌名中的空格替换成%20）
		String requestUrl = BaiduMusicUtil.URL
				.replace("{music_title}", musicTitle)
				.replace("{music_author}", musicAuthor).replaceAll(" +", "%20");
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			InputStream inputStream = httpUrlConn.getInputStream();
			music = parseMusicFromInputStream(inputStream);

			// 当成功搜索到音乐后，设置音乐名称
			if (null != music) {
				music.setTitle(musicTitle);
				if (!"".equals(musicAuthor))
					music.setDescription("演唱者:" + musicAuthor + "\n来自『小道IT』");
				else
					music.setDescription("来自『小道IT』");
			}

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (Exception e) {
			log.error("{}", e);
		}
		return music;
	}

	/**
	 * 根据百度音乐返回的流解析出音乐信息
	 * 
	 * @param inputStream
	 *            输入流
	 * @return Music
	 */
	@SuppressWarnings("unchecked")
	private static Music parseMusicFromInputStream(InputStream inputStream) {
		Music music = null;
		try {
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到xml根元素
			Element root = document.getRootElement();
			// count表示搜索到的歌曲数
			Element countElement = root.element(BaiduMusicUtil.ELEMENT_COUNT);
			String count = countElement.getText();

			// 当搜索到的歌曲数大于0时
			if (!"0".equals(count)) {
				// 普通音质
				List<Element> urlList = root
						.elements(BaiduMusicUtil.ELEMENT_URL);
				// 高级音质
				List<Element> durlList = root
						.elements(BaiduMusicUtil.ELEMENT_DURL);

				// 得到普通音质的url
				String urlEncode = urlList.get(0)
						.element(BaiduMusicUtil.ELEMENT_ENCODE).getText();
				String urlDecode = urlList.get(0)
						.element(BaiduMusicUtil.ELEMENT_DECODE).getText();

				// 普通音质url
				String url = urlEncode.substring(0,
						urlEncode.lastIndexOf("/") + 1) + urlDecode;
				if (-1 != urlDecode.lastIndexOf("&"))
					url = urlEncode
							.substring(0, urlEncode.lastIndexOf("/") + 1)
							+ urlDecode
									.substring(0, urlDecode.lastIndexOf("&"));

				// 默认情况下，高音质的url = 低品质的url
				String durl = url;
				Element durlElement = durlList.get(0).element(
						BaiduMusicUtil.ELEMENT_ENCODE);
				// 如果结果中存在高音质的节点
				if (null != durlElement) {
					String durlEncode = durlList.get(0)
							.element(BaiduMusicUtil.ELEMENT_ENCODE).getText();
					String durlDecode = durlList.get(0)
							.element(BaiduMusicUtil.ELEMENT_DECODE).getText();
					// 高级音质url
					durl = durlEncode.substring(0,
							durlEncode.lastIndexOf("/") + 1) + durlDecode;
					if (-1 != durlDecode.lastIndexOf("&"))
						durl = durlEncode.substring(0,
								durlEncode.lastIndexOf("/") + 1)
								+ durlDecode.substring(0,
										durlDecode.lastIndexOf("&"));
				}

				music = new Music();
				music.setMusicUrl(url);
				music.setHQMusicUrl(durl);
			}
		} catch (Exception e) {
			log.error("{}", e);
		}

		return music;
	}

	public static void main(String[] args) {
		Music music = searchMusic("存在", "");
		System.out.println(music.getTitle());
		System.out.println(music.getDescription());
		System.out.println(music.getMusicUrl());
		System.out.println(music.getHQMusicUrl());
	}
}
