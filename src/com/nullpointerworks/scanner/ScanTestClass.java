package com.nullpointerworks.scanner;

import java.io.IOException;

/**
 * 
 * @version 1.0.0
 * @since 1.0.0
 * @author Michiel
 */
public abstract class ScanTestClass extends Thread implements ScanTestInterface
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
	public void someOtherMethod() throws IOException
	{
		return;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private void somePrivateMethod()
	{
		return;
	}
	
}
