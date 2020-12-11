package com.nullpointerworks.examples;

/**
 * 
 * This interface serves as a scan test to see if all aspects of an interface are observed.
 * 
 * @author Michiel
 */
public interface ExampleInterface 
{
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
	 */
	void myPublicMethod3();
	
	/**
	 * my default method
	 */
	default void myDefaultMethod() 
	{
		
	}
	
	/*
	 * my default method with bad commentary marker
	 */
	default void myDefaultMethod2() 
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
