package com.wntime.gflearning.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

private static final String DRIVER_NAME = "org.postgresql.Driver";
	
	public static Connection getDBConnection(String jdbcUrl, String username, String password) throws Exception {
		Class.forName(DRIVER_NAME);
		return DriverManager.getConnection(jdbcUrl, username, password);
	}
	
	public static int insertKeyValue(Connection con, String tableName, String key, String value) throws SQLException {
		String sql = "insert into " + tableName + " (key, value) values (?,?) on conflict(key) do update set value=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, key);
			pstmt.setString(2, value);
			pstmt.setString(3, value);
			return pstmt.executeUpdate();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String queryKeyValue(Connection con, String tableName, String key) throws SQLException {
		String sql = "select value from " + tableName + " where key=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, key);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String value = rs.getString(1);
				return value;
			}
			return null;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int deleteKeyValue(Connection con, String tableName, String key) throws SQLException {
		String sql = "delete from " + tableName + " where key=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, key);
			return pstmt.executeUpdate();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
