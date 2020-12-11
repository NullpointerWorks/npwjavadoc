package com.nullpointerworks.examples;

public enum ExampleEnum2 
{
	VALUE1(""),
	VALUE2("");
	
	private final String s;
	
	private ExampleEnum2(String s) 
	{
		this.s=s;
	}
	
	public String getString() 
	{
		return s;
	}
}
