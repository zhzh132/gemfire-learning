package com.wntime.gflearning.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheWriterAdapter;

public class DatabaseCacheWriter extends CacheWriterAdapter<String, String> implements Declarable {

	private static final Log log = LogFactory.getLog(DatabaseCacheWriter.class);
	
	private Connection con = null;
	
	@Override
	public void beforeCreate(EntryEvent<String, String> event) {
		try {
			if(con == null || con.isClosed()) {
				con = DBUtils.getDBConnection(jdbcUrl, username, password);
			}
			
			DBUtils.insertKeyValue(con, "region1", event.getKey(), event.getNewValue());
		} catch (Exception e) {
			log.error("Error insert into DB.", e);
		} 
	}
	
	@Override
	public void close() {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String jdbcUrl;
	private String username;
	private String password;
	
	@Override
	public void init(Properties props) {
		this.jdbcUrl = props.getProperty("url");
		this.username = props.getProperty("user");
		this.password = props.getProperty("password");
	}

}
