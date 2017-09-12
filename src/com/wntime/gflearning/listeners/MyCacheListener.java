package com.wntime.gflearning.listeners;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;

public class MyCacheListener extends CacheListenerAdapter<String, String> implements Declarable {

	private static final Log log = LogFactory.getLog(MyCacheListener.class);
	
	@Override
	public void afterCreate(EntryEvent<String, String> event) {
		String key = event.getKey();
		String newValue = event.getNewValue();
		String oldValue = event.getOldValue();
		
		log.info("Key:" + key);
		log.info("New Value:" + newValue);
		log.info("Old Value:" + oldValue);
	}

	@Override
	public void init(Properties arg0) {
		// TODO Auto-generated method stub
		
	}
}
