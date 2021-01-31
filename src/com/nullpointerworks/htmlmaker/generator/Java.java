package com.nullpointerworks.htmlmaker.generator;

public enum Java
{
	INTERFACE("Interface"),
	CLASS("Class"),
	ENUM("Enumeration"),
	ANNOTATION("Annotation"),
	RECORD("Record");
	private final String s;
	private Java(String s) {this.s=s;}
	public String getString() {return s;}
}
