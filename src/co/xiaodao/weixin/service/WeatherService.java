package co.xiaodao.weixin.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.xiaodao.weixin.db.util.BaseDBUtil;
import co.xiaodao.weixin.json.weather.WeatherJson;
import co.xiaodao.weixin.message.pojo.Article;
import co.xiaodao.weixin.util.WeatherUtil;
import co.xiaodao.weixin.util.XiaoDaoUtil;

/**
 * 天气预报服务
 * 
 * @author liufeng
 * @date 2013-03-11
 */
@SuppressWarnings("unchecked")
public class WeatherService {
	private static Logger log = LoggerFactory.getLogger(WeatherService.class);

	// classpath
	public static final String classpath = WeatherService.class
			.getResource("/").getPath().replaceAll("%20", " ");
	// 城市代码配置文件
	public static String PATH_CONF = classpath + "weather-conf.xml";

	public static Map<String, String> cityMap = new HashMap<String, String>();

	// 静态代码块加载城市代码配置信息
	static {
		try {
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(PATH_CONF));
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到root子节点集合
			List<Element> urlList = root.elements("city");
			for (Element e : urlList)
				cityMap.put(e.attributeValue("name"), e.attributeValue("id"));
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	public static String getTodayShmtuWeather() {
		String returnStr = "";
		WeatherJson weather = queryWeather("101020600");
		returnStr += "海大天气预报：\n今天 " + weather.getTemp1() + " "
				+ weather.getWeather1() + "\n明天 " + weather.getTemp2() + " "
				+ weather.getWeather2();
		return returnStr;
	}

	/**
	 * 根据城市代码查询天气信息
	 * 
	 * @param cityCode
	 *            城市代码
	 * @return
	 */
	public static WeatherJson queryWeather(String cityCode) {
		WeatherJson weather = null;
		// 中央气象的天气预报地址
		String requestUrl = WeatherUtil.URL_WEATHER.replace("{city_code}",
				cityCode);
		String returnJsonStr = XiaoDaoUtil.getJsonByHttp(requestUrl, "UTF-8");
		weather = WeatherUtil.jsonToWeather(returnJsonStr);
		return weather;
	}

	/**
	 * 根据城市名称查询城市代码
	 * 
	 * @param cityName
	 *            城市名称
	 * @return
	 */
	public static String findCityCodeByName(String cityName) {
		String cityCode = null;

		// 如果通过用户输入的名称无匹配项，尝试移除末尾的“市”、“县”、“区”、“旗”或在末尾追加“市”、“县”、“区”、“旗”后再查找
		if (cityMap.containsKey(cityName))
			cityCode = cityMap.get(cityName);
		else if (cityMap.containsKey(cityName.replace("市", "")))
			cityCode = cityMap.get(cityName.replace("市", ""));
		else if (cityMap.containsKey(cityName + "市"))
			cityCode = cityMap.get(cityName + "市");
		else if (cityMap.containsKey(cityName.replace("县", "")))
			cityCode = cityMap.get(cityName.replace("县", ""));
		else if (cityMap.containsKey(cityName + "县"))
			cityCode = cityMap.get(cityName + "县");
		else if (cityMap.containsKey(cityName.replace("区", "")))
			cityCode = cityMap.get(cityName.replace("区", ""));
		else if (cityMap.containsKey(cityName + "区"))
			cityCode = cityMap.get(cityName + "区");
		else if (cityMap.containsKey(cityName.replace("旗", "")))
			cityCode = cityMap.get(cityName.replace("旗", ""));
		else if (cityMap.containsKey(cityName + "旗"))
			cityCode = cityMap.get(cityName + "旗");
		else if (cityMap.containsKey(cityName.replace("省", "")))
			cityCode = cityMap.get(cityName.replace("省", ""));
		else if (cityMap.containsKey(cityName + "省"))
			cityCode = cityMap.get(cityName + "省");
		return cityCode;
	}

	/**
	 * 根据Weather信息组装图文消息
	 * 
	 * @return List<Article>
	 */
	public static List<Article> makeArticlesByWeather(WeatherJson weather, String cityName) {
		List<Article> articles = new ArrayList<Article>();
		// String date_y = weather.getDate_y();
		Article article1 = new Article();

		article1.setTitle("『" + cityName + "』" + "天气预报");

		article1.setDescription("");
		article1.setUrl(WeatherUtil.WEATHER_URL);
		article1.setPicUrl("http://" + BaseDBUtil.WEB_HOSE
				+ "/WeixinXiaodaoit/image/weather_head.jpg");

		// 发布时间=18时，天气预报明天及以后的；发布时间<18时，天气预报为今天及以后的
		int dayDiff = 0;
		if (weather.getFchh().compareTo("18") >= 0) {
			dayDiff = 1;
		}
		String day1 = "";
		String day2 = "";
		String day3 = "";
		if (dayDiff == 0) {
			day1 = "今天";
			day2 = "明天";
			day3 = "后天";
		} else {
			day1 = "明天";
			day2 = "后天";
			day3 = "大后天";
		}

		// 第一天
		Article article2 = new Article();
		// WeatherUtil.getDateByDiff(date_y, +dayDiff)
		article2.setTitle(day1 + " " + weather.getTemp1() + " "
				+ weather.getWeather1() + " " + weather.getWind1());
		article2.setPicUrl(WeatherUtil.URL_PIC.replace("{pic_name}",
				weather.getImg1()));
		article2.setDescription("");
		article2.setUrl(WeatherUtil.WEATHER_URL);
		// 第二天
		Article article3 = new Article();
		// WeatherUtil.getDateByDiff(date_y, +(dayDiff + 1))
		article3.setTitle(day2 + " " + weather.getTemp2() + " "
				+ weather.getWeather2() + " " + weather.getWind2());
		article3.setPicUrl(WeatherUtil.URL_PIC.replace("{pic_name}",
				weather.getImg3()));
		article3.setDescription("");
		article3.setUrl(WeatherUtil.WEATHER_URL);
		// 第三天
		Article article4 = new Article();
		// WeatherUtil.getDateByDiff(date_y, +(dayDiff + 2))
		article4.setTitle(day3 + " " + weather.getTemp3() + " "
				+ weather.getWeather3() + " " + weather.getWind3());
		article4.setPicUrl(WeatherUtil.URL_PIC.replace("{pic_name}",
				weather.getImg5()));
		article4.setDescription("");
		article4.setUrl(WeatherUtil.WEATHER_URL);
		// 穿衣指数
		Article article5 = new Article();
		article5.setTitle("穿衣建议：" + weather.getIndex_d() + "\n" + "舒适指数："
				+ weather.getIndex_co() + "\n" + "旅游指数："
				+ weather.getIndex_tr());
		article5.setDescription("");
		article5.setUrl(WeatherUtil.WEATHER_URL);
		article5.setPicUrl("");

		// 查询其他天气
		Article article6 = new Article();
		article6.setTitle("\n如要查询其他城市的天气：\n\n发送：天气+城市名\n例如：天气上海");
		article6.setDescription("");
		article6.setUrl(WeatherUtil.WEATHER_URL);
		article6.setPicUrl("");
		
		articles.add(article1);
		articles.add(article2);
		articles.add(article3);
		articles.add(article4);
		articles.add(article5);
		articles.add(article6);

		return articles;
	}

	public static void main(String[] args) {
		// WeatherJson weather = queryWeather(findCityCodeByName("上海"));
		System.out.println(getTodayShmtuWeather());
	}
}
