package com.nullpointerworks.xmlmaker.tokenscanner.codebuilder;

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
