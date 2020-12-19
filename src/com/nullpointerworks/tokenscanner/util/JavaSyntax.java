package com.nullpointerworks.tokenscanner.util;

public final class JavaSyntax 
{
	/*
	 * 
	 */
	public static boolean isSourceType(String token) 
	{
		if (token.equalsIgnoreCase("interface")) return true;
		if (token.equalsIgnoreCase("class")) return true;
		if (token.equalsIgnoreCase("enum")) return true;
		if (token.equalsIgnoreCase("@interface")) return true;
		if (token.equalsIgnoreCase("module")) return true;
		return false;
	}
	
	/*
	 * package-private has no keyword
	 */
	public static boolean isVisibility(String token) 
	{
		if (token.equalsIgnoreCase("public")) return true;
		if (token.equalsIgnoreCase("protected")) return true;
		if (token.equalsIgnoreCase("private")) return true;
		return false;
	}
	
	/*
	 * 
	 */
	public static boolean isModifier(String token) 
	{
		if (token.equalsIgnoreCase("static")) return true;
		if (token.equalsIgnoreCase("final")) return true;
		if (token.equalsIgnoreCase("abstract")) return true;
		if (token.equalsIgnoreCase("strictfp")) return true;
		if (token.equalsIgnoreCase("default")) return true;
		return false;
	}
	
	/*
	 * 
	 */
	public static boolean isKnownKeyword(String token)
	{
		if (isSourceType(token)) return true;
		if (isVisibility(token)) return true;
		if (token.equalsIgnoreCase("extends")) return true;
		if (token.equalsIgnoreCase("implements")) return true;
		if (token.equalsIgnoreCase("import")) return true;
		if (token.equalsIgnoreCase("package")) return true;
		if (token.equalsIgnoreCase("new")) return true;
		if (token.equalsIgnoreCase("strictfp")) return true;
		if (token.equalsIgnoreCase("default")) return true;
		if (token.equalsIgnoreCase("abstract")) return true;
		return false;
	}
}
