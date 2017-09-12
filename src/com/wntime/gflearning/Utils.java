package com.wntime.gflearning;

import java.util.Map;
import java.util.Scanner;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.wntime.gflearning.query.Person;

public class Utils {

	public static Cache connect(Map<String, String> props) {
		CacheFactory factory = new CacheFactory();
		if (props != null) {
			for (String key : props.keySet()) {
				factory.set(key, props.get(key));
			} 
		}
		Cache cache = factory.create();
		return cache;
	}
	
	public static ClientCache connectAsClient(Map<String, String> props) {
		ClientCacheFactory factory = new ClientCacheFactory();
		if(props != null) {
			for(String key : props.keySet()) {
				factory.set(key, props.get(key));
			}
		}
		
		factory.addPoolLocator("localhost", 10334);
		factory.setPoolSubscriptionEnabled(true);
		factory.setPoolSubscriptionRedundancy(1);
		
		ClientCache client = factory.create();
		return client;
	}
	
	
	public static Person inputPerson(Scanner scanner) {
		System.out.print("Name:");
		String name = scanner.nextLine();
		System.out.print("Age:");
		int age = scanner.nextInt();
		Person p = new Person(name, age);
		return p;
	}
	
	public static void initPersonData(Region<String, Person> region) {
		region.put("zhao", new Person("zhao", 5));
		region.put("qian", new Person("qian", 10));
		region.put("sun", new Person("sun", 15));
		region.put("li", new Person("li", 20));
		region.put("zhou", new Person("zhou", 25));
		region.put("wu", new Person("wu", 30));
		region.put("zheng", new Person("zheng", 35));
		region.put("wang", new Person("wang", 40));
	}
}
