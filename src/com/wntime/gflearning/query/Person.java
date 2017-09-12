package com.wntime.gflearning.query;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

public class Person implements PdxSerializable {

	private static final long serialVersionUID = -6890443263685431111L;

	private String name;
	
	private int age;

	public Person() {}
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "Name:" + name + "  Age:" + age;
	}

	@Override
	public void fromData(PdxReader in) {
		this.name = in.readString("name");
		this.age = in.readInt("age");
	}

	@Override
	public void toData(PdxWriter out) {
		out.writeString("name", this.name);
		out.writeInt("age", this.age);
	}
}
