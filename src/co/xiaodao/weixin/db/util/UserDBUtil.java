package co.xiaodao.weixin.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.xiaodao.weixin.db.pojo.User;
import co.xiaodao.weixin.util.secret.AES;

/**
 * 用户表的数据库交互
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class UserDBUtil {

	/**
	 * 插入用户
	 * 
	 * @param user
	 * @return
	 */
	public static boolean insertUser(User user) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String insertSql = "insert into tb_user(open_id,user_name,password,create_time,update_time,name,student_num,student_pwd,student_info,student_dept,student_major,user_log,is_subscribe) values('"
				+ user.getOpenID()
				+ "','"
				+ user.getUsername()
				+ "','"
				+ user.getPassword()
				+ "','"
				+ user.getCreateTime()
				+ "','"
				+ user.getCreateTime()
				+ "','"
				+ user.getName()
				+ "','"
				+ user.getStudentNum()
				+ "','"
				+ user.getStudentPwd()
				+ "','"
				+ user.getStudentInfo()
				+ "','"
				+ user.getStudentDept()
				+ "','"
				+ user.getStudentMajor()
				+ "','"
				+ user.getUserLog()
				+ "','"
				+ user.getIsSubscribe() + "')";
		try {
			pstmt = conn.prepareStatement(insertSql);
			if (pstmt != null && pstmt.executeUpdate() > 0) {
				return true;
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return false;
	}

	/**
	 * 更新用户关注状态
	 * 
	 * @param user
	 * @return
	 */
	public static boolean updateUser(User user) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String updateSql = "update tb_user set update_time = '"
				+ user.getUpdateTime() + "',is_subscribe ='"
				+ user.getIsSubscribe() + "' where open_id = '"
				+ user.getOpenID() + "'";
		try {
			pstmt = conn.prepareStatement(updateSql);
			if (pstmt != null && pstmt.executeUpdate() > 0) {
				return true;
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return false;
	}

	/**
	 * 海大学生学号绑定后插入数据
	 * 
	 * @param user
	 * @return
	 */
	public static boolean updateUserForStu(User user) {
		Pattern p0 = Pattern.compile("(.*)( )(.*)( )(.*)\\((.*)");
		Matcher m0 = p0.matcher(user.getStudentInfo());
		if (m0.matches()) {
			user.setStudentDept(m0.group(1));
			user.setStudentMajor(m0.group(3));
			user.setName(m0.group(5));
		} else {
			user.setStudentDept("");
			user.setStudentMajor("");
			user.setName("");
		}
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String updateSql = "update tb_user set " + "student_num = '"
				+ user.getStudentNum() + "',student_pwd ='"
				+ user.getStudentPwd() + "',student_dept ='"
				+ user.getStudentDept() + "',student_major ='"
				+ user.getStudentMajor() + "',name ='" + user.getName()
				+ "',student_info ='" + user.getStudentInfo()
				+ "',update_time ='" + user.getUpdateTime()
				+ "' where open_id = '" + user.getOpenID() + "'";
		try {
			pstmt = conn.prepareStatement(updateSql);
			if (pstmt != null && pstmt.executeUpdate() > 0) {
				return true;
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return false;
	}

	/**
	 * 判断用户是否存在
	 * 
	 * @param openID
	 * @return
	 */
	public static boolean isUserExist(String openID) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select open_id from tb_user where open_id = '"
				+ openID + "'";
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return false;
	}

	/**
	 * 根据openID获取学号和密码
	 * 
	 * @param openID
	 * @return
	 */
	public static User getUserByOpenID(String openID) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select student_num,student_pwd from tb_user where open_id = '"
				+ openID + "'";
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new User(openID, "", "", "", "", "", "",
						rs.getString("student_num"), AES.decryptAES(
								rs.getString("student_pwd"), AES.ENCRYPT_KEY),
						"", "", "", "");
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return null;
	}

	/**
	 * 根据openID更新用户的活动日志
	 * 
	 * @param openID
	 * @return
	 */
	public static boolean updateLog(String openID, String newLog) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String updateSql = "update tb_user set user_log=concat(user_log,'"
				+ newLog + "') where open_id = '" + openID + "'";
		try {
			pstmt = conn.prepareStatement(updateSql);
			if (pstmt != null && pstmt.executeUpdate() > 0) {
				return true;
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return false;
	}

	/**
	 * 根据openID获取某列数据
	 * 
	 * @param openID
	 * @param colName
	 * @return
	 */
	public static String getUserDataByOpenID(String openID, String colName) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select " + colName
				+ " from tb_user where open_id = '" + openID + "'";
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(colName);
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return "";
	}
}
