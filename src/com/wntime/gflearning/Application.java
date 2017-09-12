package com.wntime.gflearning;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Application {

	private static final Log log = LogFactory.getLog(Application.class);
	
	public static void main(String[] args) {
		if(args.length < 1) {
			log.error("Command missing.");
			System.exit(1);
		}
		
		try {
			switch (args[0]) {
			case "connect":
				GemfireConnect.start(Arrays.copyOfRange(args, 1, args.length));
				break;
			case "listen":
				ListenerDemo.start(Arrays.copyOfRange(args, 1, args.length));
				break;
			case "query":
				QueryDemo.start(Arrays.copyOfRange(args, 1, args.length));
				break;
			case "tx":
				TransactionDemo.start(Arrays.copyOfRange(args, 1, args.length));
				break;
			case "func":
				FunctionDemo.start(Arrays.copyOfRange(args, 1, args.length));
				break;
			default:
				log.error("Unknown command: " + args[0]);
				System.exit(1);
			}
		} catch (Throwable e) {
			log.error("Error!", e);
			System.exit(1);
		}

	}

}
