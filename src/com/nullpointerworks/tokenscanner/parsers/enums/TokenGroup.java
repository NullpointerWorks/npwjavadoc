package com.nullpointerworks.tokenscanner.parsers.enums;

import java.util.List;
import java.util.ArrayList;

public class TokenGroup 
{
	private TokenGroup parent = null;
	private List<String> tokens;
	
	public TokenGroup()
	{
		this(null);
	}
	
	public TokenGroup(TokenGroup parent)
	{
		this.parent=parent;
		tokens = new ArrayList<String>();
	}
	
	public void addToken(String token)
	{
		tokens.add(token);
	}
	
	public List<String> getTokens()
	{
		return tokens;
	}
	
	public String getString()
	{
		String r = "";
		for (String s : tokens) r = r+" "+s;
		return r;
	}
	
	public TokenGroup getParent()
	{
		return parent;
	}

	public int getSize() 
	{
		return tokens.size();
	}
}
