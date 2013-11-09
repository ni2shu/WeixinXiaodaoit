package co.xiaodao.weixin.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.xiaodao.weixin.service.ClassroomService;

/**
 * 查教室的数据库交互
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ClassroomDBUtil {

	/**
	 * 判断用户是否存在
	 * 
	 * @param openID
	 * @return
	*/
	public static List<String> getInUseClassroom(String time, String weekly) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select * from tb_class_schedule where time = '"
				+ time + "'";
		List<String> classroomList = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery(); 
			while (rs.next()) {
				if (ClassroomService.haveWeekly(weekly, rs.getString("weekly"))) {
					classroomList.add(rs.getString("classroom"));
				}
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
		return classroomList;
	} 
	
//	public static List<String> getInUseClassroom(String time) {
//		Connection conn = BaseDBUtil.getCon();
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String checkSql = "select * from tb_class_schedule19 where time = '"
//				+ time + "'";
//		List<String> classroomList = new ArrayList<String>();
//		try {
//			pstmt = conn.prepareStatement(checkSql);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				classroomList.add(rs.getString("classroom"));
//			}
//			if (pstmt != null) {
//				pstmt.close();
//			}
//			if (rs != null) {
//				rs.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			BaseDBUtil.closeCon(conn);
//		}
//		return classroomList;
//	}

	public static void main(String[] args) {
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// String DateTime = df.format(new Date());
		// System.out.println(DateTime);
		String jieciTemp = "1、2、3、";
		System.out.println(jieciTemp.substring(0, jieciTemp.length() - 1));

	}
}
