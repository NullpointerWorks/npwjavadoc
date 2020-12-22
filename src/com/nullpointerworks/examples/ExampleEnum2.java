package com.nullpointerworks.examples;

/**
 * enums can never be abstract
 * 
 */
public enum ExampleEnum2 
{
	VALUE1("") 
	{
		@Override
		public String getName() 
		{
			return "value1";
		}
	},
	
	VALUE2("") 
	{
		@Override
		public String getName() 
		{
			return "value2";
		}
	};
	
	private final String s;
	
	private ExampleEnum2(String s) 
	{
		this.s=s;
	}
	
	public abstract String getName();
	
	public String getString() 
	{
		return s;
	}
}
