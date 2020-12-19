package com.nullpointerworks.tokenscanner.parsers;

import com.nullpointerworks.tokenscanner.ISourceParser;

/**
 * 
 */
public class EmptyParser implements ISourceParser
{
	/**
	 * 
	 */
	public EmptyParser()
	{
		
	}
	
	@Override
	public void nextToken(String token) 
	{
		
	}

	@Override
	public void nextLine(String line) 
	{
		
	}

	@Override
	public void nextCharacter(String character) 
	{
		
	}
}
