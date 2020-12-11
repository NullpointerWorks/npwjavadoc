package com.nullpointerworks.generator.func;

public class Parameter
{
	private String name = "";
	private String type = "";
	private String desc = "";
	public Parameter() {}
	public Parameter(String t, String n, String d)
	{
		type=t;
		name=n;
		desc=d;
	}
	public String getName(){return name;}
	public String getType(){return type;}
	public String getDescription() {return desc;}
	public void setName(String name){this.name=name;}
	public void setType(String type){this.type=type;}
	public void setDescription(String desc) {this.desc=desc;}
}
