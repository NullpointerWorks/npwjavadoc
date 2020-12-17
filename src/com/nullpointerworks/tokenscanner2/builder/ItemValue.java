package com.nullpointerworks.tokenscanner2.builder;

public enum ItemValue 
{
	NULL("null");
	
	private final String s;
	
	private ItemValue(String s) 
	{
		this.s=s;
	}
	
	public String getString() 
	{
		return s;
	}
}
