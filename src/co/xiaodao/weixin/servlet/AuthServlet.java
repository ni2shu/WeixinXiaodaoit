package co.xiaodao.weixin.servlet;

import java.io.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import co.xiaodao.weixin.db.pojo.User;
import co.xiaodao.weixin.db.util.UserDBUtil;
import co.xiaodao.weixin.service.ShmtuAuthService;
import co.xiaodao.weixin.util.WeixinUtil;
import co.xiaodao.weixin.util.XiaoDaoUtil;
import co.xiaodao.weixin.util.secret.AES;

/**
 * 绑定的web页面
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-5-4
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");

		String stuid = req.getParameter("stuid").trim();
		String pwd = req.getParameter("pwd").trim();
		String openID = req.getParameter("openID").trim();

		// System.out.println(openID);
		// System.out.println(stuid);
		// System.out.println(pwd);

		String stuInfo = ShmtuAuthService.getStuInfoByHttpGet(stuid, pwd);

		if (!openID.equals("") && stuInfo != null) {
			long updateTime = new Date().getTime();
			String dbUpdateTime = XiaoDaoUtil.getFormatTime(
					new Date(updateTime), "yyyy-MM-dd HH:mm:ss");

			if (!UserDBUtil.isUserExist(openID)) {
				User user = new User(openID, "", "", dbUpdateTime,
						dbUpdateTime, WeixinUtil.IS_SUBSCRIBE, "", "", "", "",
						"", "", dbUpdateTime + "\n补写关注信息\n\n");
				UserDBUtil.insertUser(user);
			}
			String AESPwd = AES.encryptAES(pwd, AES.ENCRYPT_KEY);
			if (UserDBUtil.updateUserForStu(new User(openID, "", "", "",
					dbUpdateTime, "", "", stuid, AESPwd, stuInfo, "", "", ""))) {
				UserDBUtil.updateLog(openID, dbUpdateTime + "\n绑定学号：" + stuid
						+ "\n\n");
				req.getRequestDispatcher("/bind/auth_succ.jsp").forward(req,
						res);
			} else {
				req.getRequestDispatcher("/bind/db_error.jsp")
						.forward(req, res);
			}

		} else {
			req.getRequestDispatcher("/bind/auth_failed.jsp").forward(req, res);
		}
	}
}