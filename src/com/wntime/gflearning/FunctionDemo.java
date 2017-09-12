package com.wntime.gflearning;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;
import com.wntime.gflearning.functions.MultiGetFunction;
import com.wntime.gflearning.functions.MyArrayListResultCollector;
import com.wntime.gflearning.query.Person;

public class FunctionDemo {

	public static void start(String[] args) throws Exception {
		
		ClientCache client = Utils.connectAsClient(null);
		Region<String, Person> region = client.<String, Person>createClientRegionFactory(ClientRegionShortcut.PROXY).create("Person");
		
		Utils.initPersonData(region);
		
		Set<String> keysFilter = new HashSet<String>();
		keysFilter.add("zhao");
		keysFilter.add("qian");
		keysFilter.add("sun");
		keysFilter.add("li");
		keysFilter.add("zhou");
		keysFilter.add("wu");
		keysFilter.add("zheng");
		keysFilter.add("wang");
		
		Execution execution = FunctionService.onRegion(region)
				.withFilter(keysFilter)
				.withCollector(new MyArrayListResultCollector());
		ResultCollector rc = execution.execute(MultiGetFunction.getIdStatic());
		List result = (List)rc.getResult();
		
		System.out.println("Result count:" + result.size());
		for(Object o : result) {
			//Person p = (Person)o;
			System.out.println(o);
		}
		
		region.close();
		client.close();
	}
}
