package com.nullpointerworks.javadoc.old.xmlmaker.examples;

import java.util.List;

/**
 * Testing class for file parsing.
 * @version 1.0.0
 * @author Michiel
 */
//@SuppressWarnings("unused")
public abstract class ExampleClass extends Object implements ExampleInterface<String,String>, Runnable
{
	// =====================================================
	// FIELDS
	// =====================================================
	
	/**
	 * some variation among variable declaration
	 */
	public final Float PI = 3.1415F;
	
	public Float PI2;
	
	public static Float TAU = 6.283F;
	
	private Float E = 2.17F;
	
	Float SQ2 = 1.414F;
	
	protected Float PHI = 1.618F;
	
	public String[] myArray = {"hi","hey"};
	
	// =====================================================
	// CONSTRUCTORS
	// =====================================================
	
	/**
	 * public constructor
	 * @since 1.0.0
	 */
	public ExampleClass()
	{
		
	}
	
	/**
	 * package private constructor. will not be parsed
	 */
	ExampleClass(int i)
	{
		
	}
	
	/**
	 * private constructor, will not be parsed
	 */
	private ExampleClass(int i, int j)
	{
		
	}
	
	/**
	 * protected constructor
	 * @param i integer param
	 * @param j integer param
	 * @param k integer param
	 */
	protected ExampleClass(int i, List<Integer> j, int k)
	{
		
	}
	
	// =====================================================
	// METHODS
	// =====================================================
	
	@Override
	public void myPublicMethod() 
	{
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void myPublicMethod3(int i) 
	{
		
	}
}
