package com.nullpointerworks.scanner;

public abstract class ScanTestClass implements ScanTestInterface
{
	
	/**
	 * a variable
	 */
	public static double testInteger1 = 0d;
	public static int testInteger2 = 0xff;
	public int testInteger3;
	
	/**
	 * 
	 */
	public ScanTestClass()
	{
		
	}
	
	protected ScanTestClass(int t)
	{
		
	}
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int var = 0;
	}
	
	/**
	 * 
	 */
	public abstract void someMethod();
	
	/**
	 * 
	 */
	public void someOtherMethod()
	{
		return;
	}
	
}
