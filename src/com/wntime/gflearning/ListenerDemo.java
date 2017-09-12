package com.wntime.gflearning;

import java.util.HashMap;
import java.util.Scanner;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;

public class ListenerDemo {

	public static void start(String[] args) throws Exception {
		HashMap<String, String> props = new HashMap<>();
		props.put("locators", "localhost[10334]");
		props.put("name", "MyCache");
		props.put("cache-xml-file", "cluster.xml");
		props.put("log-level", "info");
		Cache cache = Utils.connect(props);
		
		Region<String, String> r = cache.getRegion("region1");
//				cache.<String, String>createRegionFactory(RegionShortcut.PARTITION)
//				.addCacheListener(new MyCacheListener())
//				.create("region1");
		

		try(Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print(">");
				String input = scanner.nextLine();
				
				if ("quit".equals(input)) {
	                System.out.println("Exit!");
	                break;
	            }
				else if("put".equals(input)) {
					System.out.print("key:");
					String key = scanner.nextLine();
					System.out.print("value:");
					String value = scanner.nextLine();
					r.put(key, value);
				}
				else if("get".equals(input)) {
					System.out.print("key:");
					String key = scanner.nextLine();
					String value = r.get(key);
					System.out.println("Value:" + value);
				}
				else if("remove".equals(input)) {
					System.out.print("key:");
					String key = scanner.nextLine();
					String value = r.remove(key);
					System.out.println("Removed:" + value);
				}
				else {
					System.out.println("Unknown command.");
				}
			}
		}
		
		r.close();
		cache.close();
	}
}
