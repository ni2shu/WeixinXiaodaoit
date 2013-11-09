package co.xiaodao.weixin.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.xiaodao.weixin.pojo.AccessToken;
import co.xiaodao.weixin.util.WeixinMenuUtil;

public class TokenThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(TokenThread.class);
	public static String appid = "";
	public static String appsecret = "";
	public static AccessToken accessToken = null;

	public TokenThread() {
		
	}

	public void run() {
		do
			try {
				do {
					accessToken = WeixinMenuUtil.getAccessToken(appid,appsecret);
					if (accessToken != null) {
						log.info("获取access_token成功，有效时长{}秒 token:{}", Integer.valueOf(accessToken.getExpiresIn()), accessToken.getToken());
						Thread.sleep((accessToken.getExpiresIn() - 120) * 1000);
					} else {
						Thread.sleep(60000L);
					}
				} while (true);
			} catch (InterruptedException e) {
				try {
					Thread.sleep(60000L);//60s
				} catch (InterruptedException e1) {
					log.error("{}", e1);
				}
				log.error("{}", e);
			}
		while (true);
	}

}
