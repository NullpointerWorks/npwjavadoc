package com.nullpointerworks.tokenscanner.parsers;

/**
 * 
 */
public interface ISourceParser 
{
	/**
	 * 
	 */
	public void nextLine(String line);
	
	/**
	 * 
	 */
	public void nextCharacter(String character);
	
	/**
	 * 
	 */
	public void nextToken(String token);
}
