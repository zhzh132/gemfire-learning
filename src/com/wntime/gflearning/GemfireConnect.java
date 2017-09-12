package com.wntime.gflearning;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

public class GemfireConnect {

	public static void start1(String[] args) throws Exception {
		Cache cache = new CacheFactory()
				.set("locators", "localhost[10334]")
				.set("name", "MyCache")
				.create();
		Region r = cache.createRegionFactory(RegionShortcut.REPLICATE).create("customers");
		r.put("name", "abc");
		
		cache.close();
	}
	
	public static void start(String[] args) throws Exception {
		Cache cache = new CacheFactory()
				.set("locators", "localhost[10334]")
				.set("name", "MyCache")
				.set("cache-xml-file", "Cache1.xml")
				.set("log-level", "info")
				.create();
		Region r = cache.getRegion("TransCommon");
		r.put("key1", "123456");
		
		Region r2 = cache.getRegion("TransDetail");
		r2.close();
		r.close();
		cache.close();
	}
	
	public static void start2(String[] args) throws Exception {
		ClientCache cache = new ClientCacheFactory()
				.addPoolLocator("localhost", 10334)
				.create();
		Region r = cache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("region2");
		r.put("name", "abc");
		
		cache.close();
	}
}
