package com.wntime.gflearning.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.Operation;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import com.wntime.gflearning.data.DBUtils;

public class MyAsyncEventListener implements AsyncEventListener, Declarable {

	private static final Log log = LogFactory.getLog(MyAsyncEventListener.class);
	
	private String jdbcUrl;
	private String username;
	private String password;
	
	private Connection con = null;
	
	@Override
	public void close() {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				log.error("Error closing connection.", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvents(@SuppressWarnings("rawtypes") List<AsyncEvent> events) {
		log.info("Size of List<GatewayEvent> = " + events.size());
		
		try {
			for(AsyncEvent<String, String> event : events) {
				log.info("Operation:" + event.getOperation().toString());
				
				if(event.getOperation().equals(Operation.CREATE)) {
					String key = event.getKey();
					String value = event.getDeserializedValue();
					DBUtils.insertKeyValue(con, "async_queue", key, value);
					log.info("From MyAsyncEventListener: CREATE key--" + key + "  value--" + value);
				}
				else if(event.getOperation().equals(Operation.UPDATE)) {
					String key = event.getKey();
					String value = event.getDeserializedValue();
					DBUtils.insertKeyValue(con, "async_queue", key, value);
					log.info("From MyAsyncEventListener: UPDATE key--" + key + "  value--" + value);
				}
				else if(event.getOperation().equals(Operation.DESTROY)) {
					String key = event.getKey();
					DBUtils.deleteKeyValue(con, "async_queue", key);
					log.info("From MyAsyncEventListener: REMOVE key--" + key);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Error insert into DB:", e);
		}
		return false;
	}

	@Override
	public void init(Properties props) {
		this.jdbcUrl = props.getProperty("url");
		this.username = props.getProperty("user");
		this.password = props.getProperty("password");
		try {
			con = DBUtils.getDBConnection(jdbcUrl, username, password);
		} catch (Exception e) {
			log.error("Error getting connection:", e);
		}
	}

}
