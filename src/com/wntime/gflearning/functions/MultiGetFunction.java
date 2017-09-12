package com.wntime.gflearning.functions;

import java.util.Iterator;
import java.util.Set;

import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

public class MultiGetFunction extends FunctionAdapter {

	private static final long serialVersionUID = -69496960546900248L;

	@Override
	public void execute(FunctionContext fc) {
		if(! (fc instanceof RegionFunctionContext)) {
			throw new FunctionException("Call this function using FunctionService.onRegion");
		}
		
		RegionFunctionContext rfc = (RegionFunctionContext)fc;
		Set<?> keys = rfc.getFilter();
		Iterator<?> it = keys.iterator();
		for(int i = 0; i < keys.size() - 1; i++) {
			Object key = it.next();
			Object value = PartitionRegionHelper.getLocalDataForContext(rfc).get(key);
			rfc.getResultSender().sendResult(value);
		}
		
		Object lastKey = it.next();
		Object lastValue = PartitionRegionHelper.getLocalDataForContext(rfc).get(lastKey);
		rfc.getResultSender().lastResult(lastValue);
	}

	@Override
	public String getId() {
		return this.getClass().getName();
	}

	@Override
	public boolean isHA() {
		return false;
	}
	
	@Override
	public boolean hasResult() {
		return true;
	}
	
	public static String getIdStatic() {
		return MultiGetFunction.class.getName();
	}
}
