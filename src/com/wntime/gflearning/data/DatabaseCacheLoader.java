package com.wntime.gflearning.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.LoaderHelper;

public class DatabaseCacheLoader implements CacheLoader<String, String>, Declarable {

	private static final Log log = LogFactory.getLog(DatabaseCacheLoader.class);
	
	private Connection con = null;
	
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
	private String tableName;
	
	@Override
	public void init(Properties props) {
		this.jdbcUrl = props.getProperty("url");
		this.username = props.getProperty("user");
		this.password = props.getProperty("password");
		this.tableName = props.getProperty("table");
	}

	@Override
	public String load(LoaderHelper<String, String> helper) throws CacheLoaderException {
		String key = helper.getKey();
		log.info("Load value from database: key--" + key);
		
		try {
			if(con == null || con.isClosed()) {
				con = DBUtils.getDBConnection(jdbcUrl, username, password);
			}
			String value = DBUtils.queryKeyValue(con, tableName, key);
			return value;
		} catch (Exception e) {
			log.error("Error query from DB.", e);
		}
		
		return null;
	}

}
