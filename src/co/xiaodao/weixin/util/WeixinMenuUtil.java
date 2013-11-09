package co.xiaodao.weixin.util;

import co.xiaodao.weixin.pojo.*;
import co.xiaodao.weixin.menu.Menu;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.List;
import javax.net.ssl.*;
import net.sf.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinMenuUtil {
	
	private static Logger log = LoggerFactory.getLogger(WeixinMenuUtil.class);
	public static final String filePath = (new StringBuilder(String.valueOf(WeixinMenuUtil.class.getResource("/").getPath().replaceAll("%20", " ")))).append("media_file/").toString();
	public static final String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	public static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	public static String user_info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
	public static String media_url = "http://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	public static String send_media_url = "http://api.weixin.qq.com/cgi-bin/media/send?access_token=ACCESS_TOKEN&type=TYPE&touser=OPENID";
	public static String user_list_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

	public WeixinMenuUtil() {
	}

	/**
	 * 获取AccessToken
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET".replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		if (jsonObject != null){
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				log.error("获取token失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * 创建菜单
	 * @param menu
	 * @param accessToken
	 * @return
	 */
	public static int createMenu(Menu menu, String accessToken) {
		//Menu menu = getMenu();
		int result = 0;
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		String jsonMenu = JSONObject.fromObject(menu).toString();
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
		if (jsonObject != null && jsonObject.getInt("errcode") != 0) {
			result = jsonObject.getInt("errcode");
			log.error("创建菜单失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
		}
		return result;
	}

	/**
	 * 获取用户信息
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static WeixinUserInfo getUserInfo(String openId, String accessToken) {
		WeixinUserInfo user = null;
		String url = user_info_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		JSONObject jsonObject = httpRequest(url, "GET", null);
		if (jsonObject != null){
			try {
				user = new WeixinUserInfo();
				user.setOpenId(jsonObject.getString("openid"));
				user.setSubscribe(jsonObject.getInt("subscribe"));
				user.setNickname(jsonObject.getString("nickname"));
				user.setSex(1 != jsonObject.getInt("sex") ? "女" : "男");
				user.setLanguage(jsonObject.getString("language"));
				user.setCountry(jsonObject.getString("country"));
				user.setProvince(jsonObject.getString("province"));
				user.setCity(jsonObject.getString("city"));
			} catch (JSONException e) {
				if (user.getOpenId() == null) {
					user = null;
					log.error("获取用户信息失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
				} else if (user.getSubscribe() == 0)
					log.error("用户{}已取消关注", user.getOpenId());
			}
		}
		return user;
	}

	/**
	 * 获取公众账户关注用户列表
	 * @param accessToken
	 * @param nextOpenId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static UserList getUserList(String accessToken, String nextOpenId) {
		UserList userList = null;
		if (nextOpenId == null){
			nextOpenId = "";
		}
		String url = user_list_url.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPENID", nextOpenId);
		JSONObject jsonObject = httpRequest(url, "GET", null);
		if (jsonObject != null){
			try {
				userList = new UserList();
				userList.setTotal(jsonObject.getInt("total"));
				userList.setCount(jsonObject.getInt("count"));
				userList.setNextOpenId(jsonObject.getString("next_openid"));
				JSONObject dataObject = (JSONObject) jsonObject.get("data");
				userList.setData(JSONArray.toList(dataObject.getJSONArray("openid"), List.class));
			} catch (JSONException e) {
				e.printStackTrace();
				log.error("拉取公众账户关注用户列表 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
			}
		}
		return userList;
	}

	public static int pushTextMsg(String openId, String content, String accessToken) {
		int result = 0;
		String url = send_message_url.replace("ACCESS_TOKEN", accessToken);
		if (content != null){
			content = content.replace("\"", "\\\"");
		}
		String jsonMsg = null;
		if (content != null && !"".equals(content)){
			jsonMsg = String.format("{\"touser\":\"%s\",\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"}}",new Object[] { openId, content });
			System.out.println(jsonMsg);
		}
		if (jsonMsg != null) {
			JSONObject jsonObject = httpRequest(url, "POST", jsonMsg);
			if (jsonObject != null)
				if (jsonObject.getInt("errcode") == 0) {
					result = 1;
					log.info("{} 推送成功", openId);
				} else {
					result = 0;
					log.error("发送信息失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
				}
		}
		return result;
	}

	public static int pushNewsMsg(String openId, List<?> articleList, String accessToken) {
		int result = 0;
		String url = send_message_url.replace("ACCESS_TOKEN", accessToken);
		String jsonMsg = null;
		if (articleList != null) {
			jsonMsg = JSONArray.fromObject(articleList).toString().replaceAll("picUrl", "picurl").replaceAll("\"", "\\\"");
			jsonMsg = String.format("{\"touser\":\"%s\",\"msgtype\":\"news\",\"news\":{\"articles\":%s}}", new Object[] { openId, jsonMsg });
		}
		if (jsonMsg != null) {
			JSONObject jsonObject = httpRequest(url, "POST", jsonMsg);
			if (jsonObject != null)
				if (jsonObject.getInt("errcode") == 0) {
					result = 1;
					log.info("{} 推送成功", openId);
				} else {
					result = 0;
					log.error("发送信息失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
				}
		}
		return result;
	}

	public static int pushImageFile(String openId, String imageUrl, String accessToken) {
		int result = 0;
		String requestUrl = send_media_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId).replace("TYPE", "image");
		StringBuffer buffer = new StringBuffer();
		String boundary = "-----------------------------7da2e536604c8";
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setConnectTimeout(5000);
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConn.setRequestProperty("Charset", "UTF-8");
			httpUrlConn.setRequestProperty("Content-Type", (new StringBuilder("multipart/form-data;boundary=")).append(boundary).toString());
			OutputStream outputStream = httpUrlConn.getOutputStream();
			outputStream.write((new StringBuilder("--")).append(boundary).append("\r\n").toString().getBytes());
			outputStream.write("Content-Disposition: form-data; name=\"file1\"; filename=\"file1.jpg\"\r\n".getBytes());
			outputStream.write("Content-Type: image/jpg\r\n\r\n".getBytes());
			URL mediaURL = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) mediaURL.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			byte buf[] = new byte[8096];
			for (int size = 0; (size = bis.read(buf)) != -1;)
				outputStream.write(buf, 0, size);

			outputStream.write((new StringBuilder("\r\n--")).append(boundary).append("--\r\n").toString().getBytes());
			outputStream.close();
			bis.close();
			conn.disconnect();
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			for (String str = null; (str = bufferedReader.readLine()) != null;)
				buffer.append(str);

			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			result = jsonObject.getInt("errcode");
			log.info("推送图片文件 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
		} catch (Exception e) {
			log.error("推送媒体文件异常：{}",e);
		}
		return result;
	}

	public static int pushVoiceFile(String openId, String voiceUrl, String accessToken) {
		int result = 0;
		String requestUrl = send_media_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId).replace("TYPE", "voice");
		StringBuffer buffer = new StringBuffer();
		String boundary = "-----------------------------7da2e536604c8";
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setConnectTimeout(5000);
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConn.setRequestProperty("Charset", "UTF-8");
			httpUrlConn.setRequestProperty("Content-Type", (new StringBuilder("multipart/form-data;boundary=")).append(boundary).toString());
			OutputStream outputStream = httpUrlConn.getOutputStream();
			outputStream.write((new StringBuilder("--")).append(boundary).append("\r\n").toString().getBytes());
			outputStream.write("Content-Disposition: form-data; name=\"file1\"; filename=\"file1.AMR\"\r\n".getBytes());
			outputStream.write("Content-Type: audio/AMR\r\n\r\n".getBytes());
			URL voiceURL = new URL(voiceUrl);
			HttpURLConnection conn = (HttpURLConnection) voiceURL.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			byte buf[] = new byte[8096];
			for (int size = 0; (size = bis.read(buf)) != -1;)
				outputStream.write(buf, 0, size);

			outputStream.write((new StringBuilder("\r\n--")).append(boundary).append("--\r\n").toString().getBytes());
			outputStream.close();
			bis.close();
			conn.disconnect();
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			for (String str = null; (str = bufferedReader.readLine()) != null;)
				buffer.append(str);

			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			result = jsonObject.getInt("errcode");
			log.info("推送音频文件 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
		} catch (Exception e) {
			log.error("推送媒体文件异常：{}", e);
		}
		return result;
	}

	public static String getMediaFile(String token, String mediaId, String format) {
		String fileName = (new StringBuilder(String.valueOf(filePath))).append(mediaId).append(".").append(format).toString();
		String requestUrl = media_url.replace("ACCESS_TOKEN", token).replace("MEDIA_ID", mediaId);
		File amrFile = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			BufferedInputStream bis = new BufferedInputStream(httpUrlConn.getInputStream());
			amrFile = new File(fileName);
			if (!amrFile.exists())
				amrFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			byte buf[] = new byte[8096];
			for (int size = 0; (size = bis.read(buf)) != -1;)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();
			httpUrlConn.disconnect();
		} catch (Exception e) {
			fileName = null;
			if (amrFile.exists())
				amrFile.delete();
			log.error("获取媒体文件异常：{}",e);
		}
		return fileName;
	}

	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager tm[] = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			if (outputStr != null) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			for (String str = null; (str = bufferedReader.readLine()) != null;)
				buffer.append(str);

			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

}
