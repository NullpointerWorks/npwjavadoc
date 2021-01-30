package com.nullpointerworks.htmlmaker.generator.clazz;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.htmlmaker.generator.Additional;

public class Field
{
	private String mods = "";
	private String type = "";
	private String name = "";
	private String value = "";
	private String desc = "";
	private List<Additional> adds;
	
	public Field()
	{
		adds=new ArrayList<Additional>();
	}
	
	public Field(String t, String n)
	{
		this();
		setType(t);
		setName(n);
	}
	
	public void setModifier(String m) {mods=m;}
	public void setType(String t) {type=t;}
	public void setName(String n) {name=n;}
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
