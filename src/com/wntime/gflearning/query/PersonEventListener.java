package com.wntime.gflearning.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqStatusListener;

import com.wntime.gflearning.model.Person;

public class PersonEventListener implements CqStatusListener {

	private static final Log log = LogFactory.getLog(PersonEventListener.class);
	
	@Override
	public void close() {
		log.info("CQ Closed.");
	}

	@Override
	public void onError(CqEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(CqEvent event) {
		Operation operation = event.getQueryOperation();
		Object key = event.getKey();
		Person person = (Person)event.getNewValue();
		
		if(operation.isCreate()) {
			log.info("Create Person [" + key + "]: " + person.toString());
		}
		else if(operation.isUpdate()) {
			log.info("Update Person [" + key + "]: " + person.toString());
		}
		else if(operation.isDestroy()) {
			log.info("Destory Person [" + key + "]: " + person.toString());
		}
	}

	@Override
	public void onCqConnected() {
		log.info("CQ Connected.");
	}

	@Override
	public void onCqDisconnected() {
		log.info("CQ Disconnected.");
	}

}
