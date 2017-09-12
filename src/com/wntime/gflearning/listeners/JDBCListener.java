package com.wntime.gflearning.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;
import com.wntime.gflearning.data.DBUtils;

public class JDBCListener extends CacheListenerAdapter<String, String> implements Declarable {

	private String jdbcUrl;
	private String username;
	private String password;
	
	@Override
	public void init(Properties props) {
		this.jdbcUrl = props.getProperty("url");
		this.username = props.getProperty("user");
		this.password = props.getProperty("password");
	}
	
	@Override
	public void afterCreate(EntryEvent<String, String> event) {
		Connection con = null;
		try {
			con = DBUtils.getDBConnection(jdbcUrl, username, password);
			DBUtils.insertKeyValue(con, "region1", event.getKey(), event.getNewValue());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
