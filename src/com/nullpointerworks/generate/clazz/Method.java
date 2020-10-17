package com.nullpointerworks.generate.clazz;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.generate.Additional;
import com.nullpointerworks.generate.func.Parameter;

public class Method 
{
	private List<Parameter> params;
	private List<Additional> adds;

	private String mods = "";
	private String type = "";
	private String name = "";
	private String desc = "";
	
	public Method()
	{
		params = new ArrayList<Parameter>();
		adds = new ArrayList<Additional>();
	}
	
	public Method(String t, String n)
	{
		this();
		setDetails(t,n);
	}
	
	public void setParameter(String t, String n, String d)
	{
		params.add(new Parameter(t,n,d));
	}
	
	public void setParameter(Parameter e)
	{
		params.add(e);
	}
	
	public void setSince(String s) 
	{
		setAdditional("Since",s);
	}
	
	public void setReturns(String r) 
	{
		setAdditional("Returns",r);
	}
	
	public void setAdditional(String t, String s) 
	{
		adds.add( new Additional(t,s) );
	}
	
	public void setDetails(String t, String n) {type=t;name=n;}
	public void setModifier(String m) {mods=m;}
	public void setDescription(String d) {desc=d;}
	public String getModifier() {return mods;}
	public String getName() {return name;}
	public String getType() {return type;}
	public String getDescription() {return desc;}

	public List<Parameter> getParameters()
	{
		return params;
	}
	
	public List<Additional> getAdditionals()
	{
		return adds;
	}
	
	public String getReturns() 
	{
		for (Additional d : adds)
		{
			if (d.getType().equalsIgnoreCase("returns"))
			{
				return d.getText();
			}
		}
		return "";
	}

	public String getSimpleName()
	{
		String name = this.name+"(";
		for (int i=0,l=params.size(); i<l; i++)
		{
			Parameter p = params.get(i);
			if (l==1 || i==(l-1))
			{
				name+=p.getType();
			}
			else
			{
				name+=p.getType()+", ";
			}
		}
		return name+")";
	}
	
	public String getComplexName()
	{
		String name = this.name+"(";
		for (int i=0,l=params.size(); i<l; i++)
		{
			Parameter p = params.get(i);
			if (l==1 || i==(l-1))
			{
				name+=p.getType() + " " + p.getName();
			}
			else
			{
				name+=p.getType() + " " + p.getName() + ", ";
			}
		}
		return name+")";
	}
}
