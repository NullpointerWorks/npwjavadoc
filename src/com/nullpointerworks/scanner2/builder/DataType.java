package com.nullpointerworks.scanner2.builder;

public enum DataType 
{
	NULL("null");
	
	private final String s;
	
	private DataType(String s) 
	{
		this.s=s;
	}
	
	public String getString() 
	{
		return s;
	}
}
