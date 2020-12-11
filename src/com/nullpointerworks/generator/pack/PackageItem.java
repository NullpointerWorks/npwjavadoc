package com.nullpointerworks.generator.pack;

public class PackageItem
{
	private final String fileName;
	private final String itemName;
	private final String itemDesc;
	
	public PackageItem(String f, String n, String d)
	{
		fileName = f;
		itemName = n;
		itemDesc = d;
	}
	
	public String getFileName(){return fileName;}
	public String getItemName(){return itemName;}
	public String getDescription(){return itemDesc;}
}
