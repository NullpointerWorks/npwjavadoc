package com.nullpointerworks.javadoc.old.htmlmaker.generator.mod;

public class Exported
{
	private final String fileName;
	private final String packageName;
	
	public Exported(String f, String p)
	{
		fileName = f;
		packageName = p;
	}
	public String getFileName(){return fileName;}
	public String getPackageName(){return packageName;}
}
