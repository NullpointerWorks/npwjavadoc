package com.nullpointerworks.generator;

public enum Java
{
	INTERFACE("Interface"),
	CLASS("Class"),
	ENUM("Enumeration");
	
	private final String s;
	private Java(String s) {this.s=s;}
	public String getString() {return s;}
}
