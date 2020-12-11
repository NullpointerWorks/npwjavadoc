package com.nullpointerworks.generator.clazz;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.generator.Additional;

public class Field
{
	private String mods = "";
	private String type = "";
	private String name = "";
	private String value = "";
	private String desc = "";
	private List<Additional> adds;
	
	public Field(String t, String n)
	{
		type=t;
		name=n;
		adds=new ArrayList<Additional>();
	}
	
	public Field(String t, String n, String v)
	{
		type=t;
		name=n;
		value=v;
		adds=new ArrayList<Additional>();
		setAdditional("Value",v);
	}

	public void setModifier(String m) {mods=m;}
	public void setValue(String v) {value=v;}
	public void setDescription(String d) {desc = d;}
	public void setAdditional(String t, String s) 
	{
		adds.add( new Additional(t,s) );
	}
	
	public String getModifier() {return mods;}
	public String getType() {return type;}
	public String getName() {return name;}
	public String getValue() {return value;}
	public String getDescription()
	{
		return desc;
	}
	
	public List<Additional> getAdditionals()
	{
		return adds;
	}

	public void setSince(String s) 
	{
		setAdditional("Since",s);
	}
}
