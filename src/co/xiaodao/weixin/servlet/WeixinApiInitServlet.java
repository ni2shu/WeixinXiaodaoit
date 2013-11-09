package co.xiaodao.weixin.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.xiaodao.weixin.thread.TokenThread;

public class WeixinApiInitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WeixinApiInitServlet.class);
	public static String token = "";

	public WeixinApiInitServlet() {
		
	}

	public void init() throws ServletException { 
		token = getInitParameter("token");
		TokenThread.appid = getInitParameter("appid");
		TokenThread.appsecret = getInitParameter("appsecret");
		log.info("weixin api token:{}", token);
		log.info("weixin api appid:{}", TokenThread.appid);
		log.info("weixin api appsecret:{}", TokenThread.appsecret);
		if (TokenThread.appid == null || "".equals(TokenThread.appid) || TokenThread.appsecret == null || "".equals(TokenThread.appsecret)){
			log.error("appid and appsecret configuration error, please check carefully.");	
		}else{
			(new Thread(new TokenThread())).start();
		}
	}

}
