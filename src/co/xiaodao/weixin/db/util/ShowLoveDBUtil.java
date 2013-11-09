package co.xiaodao.weixin.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.xiaodao.weixin.db.pojo.ShowLove;

/**
 * 表白表的数据库交互
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-5-12
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ShowLoveDBUtil {

	public static void main(String[] sra) {

	}

	/**
	 * 插入表白信息
	 * 
	 * @param showLove
	 * @return
	 */
	public static boolean insertShowLove(ShowLove showLove) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String insertSql = "insert into tb_show_love(from_open_id,from_name,to_name,content,date,create_date_time) values('"
				+ showLove.getFromOpenID()
				+ "','"
				+ showLove.getFromName()
				+ "','"
				+ showLove.getToName()
				+ "','"
				+ showLove.getContent()
				+ "','"
				+ showLove.getDate()
				+ "','"
				+ showLove.getCreateDateTime() + "')";
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
	 * 根据name获取表白信息
	 * 
	 * @param name
	 * @param isFormName
	 * @return
	 */
	public static List<ShowLove> getShowLoveByName(String name,
			boolean isFormName) {
		String checkSql = "";
		List<ShowLove> showLoveList = new ArrayList<ShowLove>();
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		if (isFormName) {
			checkSql = "select * from tb_show_love where from_name = '" + name
					+ "'";
		} else {
			checkSql = "select * from tb_show_love where to_name = '" + name
					+ "'";
		}
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				showLoveList.add(new ShowLove(rs.getString("from_open_id"), rs
						.getString("from_name"), rs.getString("to_name"), rs
						.getString("content"), rs.getString("date"), rs
						.getString("create_date_time")));
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
		return showLoveList;
	}

	/**
	 * 获取今天的所有表白内容
	 * 
	 * @param name
	 * @param isFormName
	 * @return
	 */
	public static List<ShowLove> getTodayShowLove(String date) {
		List<ShowLove> showLoveList = new ArrayList<ShowLove>();
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select * from tb_show_love where date = '" + date
				+ "'";
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				showLoveList.add(new ShowLove(rs.getString("from_open_id"), rs
						.getString("from_name"), rs.getString("to_name"), rs
						.getString("content"), rs.getString("date"), rs
						.getString("create_date_time")));
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
		return showLoveList;
	}

}
