package co.xiaodao.weixin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.xiaodao.weixin.json.weather.WeatherJson;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * 天气预报查询工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class WeatherUtil {

	/**
	 * 天气预报查询地址<br>
	 * 例如，贵阳的天气预报信息：http://m.weather.com.cn/data/101260101.html<br>
	 * 3G版：http://wap.weather.com.cn/wap/weather/101260101.shtml
	 */
	public final static String URL_WEATHER = "http://m.weather.com.cn/data/{city_code}.html";

	// 气象图片地址
	public final static String URL_PIC = "http://m.weather.com.cn/img/a{pic_name}.gif";

	// 天气预报链接地址
	public static final String WEATHER_URL = "http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5NzU0MDIyMQ==&appmsgid=10000022&itemidx=1#wechat_redirect";

	
	/**
	 * 将json格式的天气信息转化成java对象
	 * 
	 * @param json
	 *            天气信息
	 * @return
	 */
	public static WeatherJson jsonToWeather(String json) {
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.alias("weatherinfo", WeatherJson.class);
		WeatherJson weather = (WeatherJson) xs.fromXML(json);
		return weather;
	}

	/**
	 * 通过diff计算指定日期的前一天（后一天）
	 * 
	 * @param date
	 *            指定日期
	 * @param diff
	 * @return
	 */
	public static String getDateByDiff(String date, int diff) {
		String dateStr = "";
		try {
			DateFormat df1 = new SimpleDateFormat("yyyy年M月d日");
			Calendar c = Calendar.getInstance();
			c.setTime(df1.parse(date));
			c.add(Calendar.DAY_OF_YEAR, diff);

			DateFormat df2 = new SimpleDateFormat("M月d日");
			dateStr = df2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	public static void main(String[] args) {
		System.out.println(getDateByDiff("2013年4月8日", +1));
	}

}
