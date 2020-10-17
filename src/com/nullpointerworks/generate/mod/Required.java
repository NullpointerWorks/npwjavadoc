package com.nullpointerworks.generate.mod;

public class Required
{
	private final String name;
	private final String modifier;
	
	public Required(String m, String n)
	{
		name = n;
		modifier = m;
	}
	public String getName(){return name;}
	public String getModifier(){return modifier;}
}
