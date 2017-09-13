package com.wntime.gflearning;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wntime.gflearning.model.Person;
import com.wntime.gflearning.query.PersonQuery;

public class QueryDemo {

	private static final Log log = LogFactory.getLog(QueryDemo.class);
	
	public static void start(String[] args) throws Exception {
		
		try (Scanner scanner = new Scanner(System.in);
			 PersonQuery personQuery = new PersonQuery()) {
			while (true) {
				System.out.print(">");
				String input = scanner.nextLine();
				
				if ("quit".equals(input)) {
	                System.out.println("Exit!");
	                break;
	            }
				else if("q".equals(input)) {
					System.out.print("OQL:");
					String query = scanner.nextLine();
					List<Person> result = personQuery.query(query);
					System.out.println("Result:");
					for(Person p : result) {
						System.out.println(p.toString());
					}
				}
				else if("cq".equals(input)) {
					System.out.print("OQL:");
					String query = scanner.nextLine();
					List<Person> result = personQuery.startCq(query);
					System.out.println("Result:");
					for(Person p : result) {
						System.out.println(p.toString());
					}
				}
				else if("stop".equals(input)) {
					System.out.println("Stopping Cq...");
					personQuery.closeCq();
				}
				else if("init".equals(input)) {
					personQuery.initRegion();
					log.info("Init finished.");
				}
				else if("put".equals(input)) {
					Person p = Utils.inputPerson(scanner);
					personQuery.put(p);
				}
				else {
					System.out.println("Unknown command.");
				}
			}
		}
		
	}
	
}
