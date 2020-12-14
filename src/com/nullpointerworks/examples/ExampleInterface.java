package com.nullpointerworks.examples;

/** 
This interface serves as a scan test to see if all aspects of an interface are observed.
@author Michiel
*/
public interface ExampleInterface extends Runnable
{
	/**
	 * interface variables are automatically public, static and final.
	 * no other modifiers are permitted
	 */
	int myVariable = 0;
	
	/**
	 * public method
	 */
	public void myPublicMethod();
	
	/**
	 * abstract method
	 */
	abstract void myPublicMethod2();
	
	/**
	 * no modifiers are also public by default
	 * @param i testing parameter
	 */
	void myPublicMethod3(int i);
	
	/**
	 * my default method
	 */
	default void myDefaultMethod() 
	{
		
	}
	
	/*
	 * my default method with bad commentary marker, but with a parameter
	 */
	default void myDefaultMethod2(int i, int j) 
	{
		
	}
	
	/**
	 * my default method with a return type
	 */
	default String myDefaultMethod3() 
	{
		return "";
	}
	
	/**
	 * my default strict floating-point method with a return type
	 */
	default strictfp String myDefaultMethod4() 
	{
		return "";
	}
	
}