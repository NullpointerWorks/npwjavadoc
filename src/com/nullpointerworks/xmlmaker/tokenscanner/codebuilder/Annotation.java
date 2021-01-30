package com.nullpointerworks.xmlmaker.tokenscanner.codebuilder;

import java.util.List;

public class Annotation 
{
	private String ann = "";
	private String[] params = {};
	
	public Annotation(String ann) 
	{
		this.ann=ann;
	}

	public Annotation(String ann, String[] params) 
	{
		this(ann);
		setParameters(params);
	}
	
	public String getString()
	{
		String ret = ann;
		if (params.length>0)
		{
			ret+="(";
			for (int i=0,l=params.length; i<l;i++)
			{
				String p = params[i];
				ret = ret+p+((i+1==l)?"":",");
			}
			ret+=")";
		}
		
		return ret;
	}
	
	public void setParameters(String[] params) 
	{
		this.params=params;
	}
	
	public void setParameters(List<String> params) 
	{
		setParameters( params.toArray( new String[params.size()] ) );
	}
}
