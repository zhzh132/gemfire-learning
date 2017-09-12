package com.wntime.gflearning;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class GemfireConnect {

	public static final String LOCATORS     = "192.168.56.1[30001]";
	public static final String LOCATOR_HOST = "192.168.56.1";
	public static final int    LOCATOR_PORT = 30001;
	
	public static void start(String[] args) throws Exception {
		Cache cache = new CacheFactory()
				.set("locators", LOCATORS)
				.set("name", "MyCache")
				.create();
		Region r = cache.createRegionFactory(RegionShortcut.REPLICATE).create("customers");
		r.put("name", "abc");
		
		cache.close();
	}
	
	public static void start1(String[] args) throws Exception {
		Cache cache = new CacheFactory()
				.set("locators", LOCATORS)
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
				.addPoolLocator(LOCATOR_HOST, LOCATOR_PORT)
				.create();
		Region r = cache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("region2");
		r.put("name", "abc");
		
		cache.close();
	}
}
