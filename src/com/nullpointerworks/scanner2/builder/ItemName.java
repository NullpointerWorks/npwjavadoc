package com.nullpointerworks.scanner2.builder;

public enum ItemName 
{
	NULL("null");
	
	private final String s;
	
	private ItemName(String s) 
	{
		this.s=s;
	}
	
	public String getString() 
	{
		return s;
	}
}
