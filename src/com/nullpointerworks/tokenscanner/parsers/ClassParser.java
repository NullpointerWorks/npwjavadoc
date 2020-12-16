package com.nullpointerworks.tokenscanner.parsers;

import com.nullpointerworks.tokenscanner.AbstractSourceParser;

/**
 * 
 */
public class ClassParser extends AbstractSourceParser
{
	private int curlyBraceCount = 0;
	
	
	
	/**
	 * 
	 */
	public ClassParser()
	{
		super();
		
	}
	
	@Override
	public void nextToken(String token) 
	{
		/*
		 * keep track of embedded code blocks
		 */
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		
		
	}
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
}
