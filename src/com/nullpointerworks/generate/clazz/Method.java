package com.nullpointerworks.generate.clazz;

import com.nullpointerworks.generate.Additional;

public class Method extends Constructor
{
	private String type = "";
	
	public Method() 
	{
		super();
	}
	
	public Method(String t, String n)
	{
		super(n);
		setType(t);
	}
	
	public void setType(String t) {type=t;}
	public String getType() {return type;}
	
	public String getReturns() 
	{
		var adds = getAdditionals();
		for (Additional d : adds)
		{
			if (d.getType().equalsIgnoreCase("returns"))
			{
				return d.getText();
			}
		}
		return "";
	}
}
