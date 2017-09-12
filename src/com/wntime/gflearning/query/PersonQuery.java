package com.wntime.gflearning.query;

import java.util.ArrayList;
import java.util.List;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.query.CqAttributes;
import com.gemstone.gemfire.cache.query.CqAttributesFactory;
import com.gemstone.gemfire.cache.query.CqClosedException;
import com.gemstone.gemfire.cache.query.CqException;
import com.gemstone.gemfire.cache.query.CqExistsException;
import com.gemstone.gemfire.cache.query.CqQuery;
import com.gemstone.gemfire.cache.query.CqResults;
import com.gemstone.gemfire.cache.query.Query;
import com.gemstone.gemfire.cache.query.QueryInvalidException;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.SelectResults;
import com.gemstone.gemfire.cache.query.Struct;
import com.wntime.gflearning.Utils;

import jline.internal.Log;

public class PersonQuery implements AutoCloseable {

	private ClientCache client;
	
	private Region<String, Person> region;
	
	public PersonQuery() {
		this.client = Utils.connectAsClient(null);
		this.region = client.<String, Person>createClientRegionFactory(ClientRegionShortcut.PROXY).create("Person");
		
		CqAttributesFactory cqf = new CqAttributesFactory();
		cqf.addCqListener(new PersonEventListener());
		this.cqAttributes = cqf.create();
	}
	
	public List<Person> query(String oql) throws Exception {
		QueryService service = client.getQueryService();
		Query query = service.newQuery(oql);
		SelectResults<Person> result = (SelectResults<Person>)query.execute();
		int size = result.size();
		ArrayList<Person> list = new ArrayList<>(size);
		for(Person p : result) {
			list.add(p);
		}
		return list;
	}
	
	private CqAttributes cqAttributes;
	private CqQuery personTracker;
	
	public List<Person> startCq(String oql) throws Exception {
		if(personTracker != null) {
			Log.error("Another Cq exists.");
			return null;
		}
		
		String cqName = "personTracker";
		QueryService service = client.getQueryService();
		personTracker = service.newCq(cqName, oql, cqAttributes);
		CqResults result = personTracker.executeWithInitialResults();
		int size = result.size();
		
		ArrayList<Person> list = new ArrayList<>(size);
		for(Object o : result) {
			Struct s = (Struct)o;
			Person p = (Person)s.get("value");
			list.add(p);
		}
		return list;
	}
	
	public void closeCq() throws Exception {
		if(this.personTracker != null) {
			this.personTracker.close();
			this.personTracker = null;
		}
	}

	@Override
	public void close() throws Exception {
		if(region != null) {
			region.close();
		}
		
		if(client != null) {
			client.close();
		}
	}
	
	public void put(Person p) {
		region.put(p.getName(), p);
	}
	
	public void initRegion() {
		Utils.initPersonData(region);
	}

}
