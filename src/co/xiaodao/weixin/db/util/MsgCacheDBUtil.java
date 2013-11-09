package co.xiaodao.weixin.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.xiaodao.weixin.db.pojo.MsgCache;

/**
 * ÐÅÏ¢»º´æ±í
 * @author dao-zheng.chen
 *
 */
public class MsgCacheDBUtil {

	public static boolean insertMsgCache(MsgCache msgCache) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String insertSql = "insert into tb_msg_cache(open_id,weather,express) values('"
				+ msgCache.getOpenID()
				+ "','"
				+ msgCache.getWeather()
				+ "','"
				+ msgCache.getExpress() + "')";
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
	
	public static boolean updateMsgCache(MsgCache msgCache) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		String updateSql = "update tb_msg_cache set weather = '"
				+ msgCache.getWeather() + "',express ='"
				+ msgCache.getExpress() + "' where open_id = '"
				+ msgCache.getOpenID() + "'";
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
	
	public static boolean isMsgCacheExist(String openID) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select open_id from tb_msg_cache where open_id = '" + openID + "'";
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
	
	public static MsgCache getMsgCacheByOpenID(String openID) {
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select * from tb_msg_cache where open_id = '"
				+ openID + "'";
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new MsgCache(openID, rs.getString("weather"), rs.getString("express"));
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
	
	public static boolean insertOrUpdateMsgCache(MsgCache msgCache) {
		if(isMsgCacheExist(msgCache.getOpenID())){
			 return updateMsgCache(msgCache);
		}else{
			return insertMsgCache(msgCache);
		}
	}
}
