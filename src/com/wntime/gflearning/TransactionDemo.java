package com.wntime.gflearning;

import java.util.Scanner;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.CacheTransactionManager;
import org.apache.geode.cache.CommitConflictException;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.TypeMismatchException;

import com.wntime.gflearning.model.Person;

public class TransactionDemo {

	public static void start(String[] args) throws Exception {
		Cache cache = new CacheFactory()
				.set("log-level", "info")
				.create();
		
		Region<String, Person> region1 = cache.<String,Person>createRegionFactory()
				.setDataPolicy(DataPolicy.REPLICATE)
				.create("region1");
		
		Region<String, Person> region2 = cache.<String,Person>createRegionFactory()
				.setDataPolicy(DataPolicy.REPLICATE)
				.create("region2");
		
		CacheTransactionManager txmgr = cache.getCacheTransactionManager();
		
		try(Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print(">");
				String input = scanner.nextLine();
				
				if ("quit".equals(input)) {
	                System.out.println("Exit!");
	                break;
	            }
				else if ("put".equals(input)) {
					txmgr.begin();
					Person p = Utils.inputPerson(scanner);
					
					region1.put(p.getName(), p);
					System.out.println(p.toString() + " added to " + region1.getName());
					
					region2.put(p.getName(), p);
					System.out.println(p.toString() + " added to " + region2.getName());
				}
				else if ("commit".equals(input)) {
					txmgr.commit();
					System.out.println("Committed.");
				}
				else if ("rollback".equals(input)) {
					txmgr.rollback();
					System.out.println("Rollback!");
				}
				else if ("print".equals(input)) {
					printRegionData(cache, "region1");
					System.out.println();
					printRegionData(cache, "region2");
				}
				else {
					if(input.length() > 0) {
						System.out.println("Unknown command.");
					}
				}
			}
		}
		catch (CommitConflictException ex) {
			ex.printStackTrace();
			txmgr.rollback();
		}
 		
		region1.close();
		region2.close();
		cache.close();
	}
	
	public static void printRegionData(Cache cache, String regionName) throws FunctionDomainException, TypeMismatchException, NameResolutionException, QueryInvocationTargetException {
		String oql = "select * from /" + regionName;
		QueryService qs = cache.getQueryService();
		Query query = qs.newQuery(oql);
		SelectResults<Person> rs = (SelectResults<Person>)query.execute();
		int size = rs.size();
		if(size > 0) {
			System.out.println(regionName + ":");
			for(Person p : rs) {
				System.out.println(p.toString());
			}
		}
		else {
			System.out.println(regionName + " is empty.");
		}
	}
}
