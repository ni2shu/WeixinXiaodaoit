package co.xiaodao.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class XiaoDaoUtil {

	public static void main(String[] args) {

	}

	/**
	 * 早起签到用到的
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String[] getNowDateTimeByFormatForGetUpSign() {
		String returnStr[] = getNowDateTimeByFormat("yyyy-MM-dd HH:mm:ss")
				.split(" ");
		return returnStr;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	// yyyy年m月d日 HH点m分
	// yyyy年M月d日
	public static String getNowDateTimeByFormat(String dataFormat) {
		DateFormat df = new SimpleDateFormat(dataFormat);
		return df.format(new Date());
	}

	public static String getFormatTime(Date date, String formatStr) {
		DateFormat df = new SimpleDateFormat(formatStr);
		return df.format(date);
	}

	/**
	 * 对指定日期格式化
	 * 
	 * @param date
	 *            指定日期
	 * @return
	 */
	public static String formatDate(String date) {
		String result = "";
		try {
			DateFormat df1 = new SimpleDateFormat("yyyy年M月d日");
			Calendar c = Calendar.getInstance();
			c.setTime(df1.parse(date));

			DateFormat df2 = new SimpleDateFormat("yyyy年");
			result = df2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param bytearray
	 * @return
	 */
	public static String byteToStr(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexStr(bytearray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param ib
	 * @return
	 */
	public static String byteToHexStr(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];

		String s = new String(ob);
		return s;
	}

	/**
	 * XML转换成map
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		// 输入流
		InputStream inputStream = request.getInputStream();
		// 通过SAX解析输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 根据字节截取字符串
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String byteSubstring(String s, int length) {
		String returnStr = null;
		byte[] bytes = null;
		int i = 2; // 要截取的字节数，从第3个字节开始
		try {
			bytes = s.getBytes("Unicode");
			int n = 0; // 表示当前的字节数
			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}
			}
			// 如果i为奇数时，处理成偶数
			if (i % 2 == 1) {
				// 该UCS2字符是汉字时，去掉这个截一半的汉字
				if (bytes[i - 1] != 0)
					i = i - 1;
				// 该UCS2字符是字母或数字，则保留该字符
				else
					i = i + 1;
			}
			returnStr = new String(bytes, 0, i, "Unicode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	public static String byteSubstring(String str) {
		try {
			if (str.getBytes("utf-8").length > 2037) {
				str = XiaoDaoUtil.byteSubstring(str, 1358) + "\n......";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 通过HTTP获取JSON字符串
	 * 
	 * @param requestUrl
	 * @param charFormat
	 * @return
	 */
	public static String getJsonByHttp(String requestUrl, String charFormat) {
		StringBuffer returnStrBuffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, charFormat);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				returnStrBuffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStrBuffer.toString();
	}

}
