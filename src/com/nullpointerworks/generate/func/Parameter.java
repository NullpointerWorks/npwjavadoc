package com.nullpointerworks.generate.func;

public class Parameter
{
	private String name = "";
	private String type = "";
	private String desc = "";
	public Parameter(String t, String n, String d)
	{
		type=t;
		name=n;
		desc=d;
	}
	public String getName(){return name;}
	public String getType(){return type;}
	public String getDescription() {return desc;}
}
