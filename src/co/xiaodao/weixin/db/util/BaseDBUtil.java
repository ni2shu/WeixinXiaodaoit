package co.xiaodao.weixin.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接与关闭
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class BaseDBUtil {

	// 本地测试
	public static String WEB_HOSE = "cdztop.eicp.net";
	private static String DBUsername = "root";
	private static String DBPassword = "admin";
	private static String DBHost = "localhost";
	private static String DBName = "xiaodaoit2";  

	// 正式服务器

	

	// 生成索引的时候用
	// public static String WEB_HOSE = "xiaodaoit.gotoip2.com";
	// private static String DBUsername = "xiaodaoit";
	// private static String DBPassword = "woaiwoym";
	// private static String DBHost = "xiaodaoit.gotoip2.com";
	// private static String DBName = "xiaodaoit";

	public static Connection getCon() {// 得到数据库连接的方法
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dbUrl = "jdbc:mysql://" + DBHost + "/" + DBName
					+ "?useUnicode=true&characterEncoding=utf-8";
			con = DriverManager.getConnection(dbUrl, DBUsername, DBPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return con;
	}

	public static void closeCon(Connection con) {// 关闭数据库连接方法
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}

}
