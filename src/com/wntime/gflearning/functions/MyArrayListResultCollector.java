package com.wntime.gflearning.functions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;

public class MyArrayListResultCollector<T, S> implements ResultCollector<T, S> {

	private static final Log log = LogFactory.getLog(MyArrayListResultCollector.class);
	
	private ArrayList<T> results = new ArrayList<T>();
	
	private boolean ended = false;
	
	@Override
	public void addResult(DistributedMember member, T obj) {
		log.info("Got result from " + member.getName() + ", Object:" + obj);
		if (obj != null) {
			results.add(obj);
		}
	}

	@Override
	public void clearResults() {
		log.info("Clear results.");
		results.clear();
	}

	@Override
	public void endResults() {
		this.ended = true;
	}

	@Override
	public S getResult() throws FunctionException {
		return (S)results;
	}

	@Override
	public S getResult(long arg0, TimeUnit arg1) throws FunctionException, InterruptedException {
		
		return (S)results;
	}

}
