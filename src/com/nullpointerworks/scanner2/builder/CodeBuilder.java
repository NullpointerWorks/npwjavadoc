package com.nullpointerworks.scanner2.builder;

import java.util.ArrayList;
import java.util.List;

public class CodeBuilder 
{
	/*
	 * public, protected, private or nothing
	 */
	Visibility visibility;
	
	/*
	 * static, abstract, final or nothing
	 */
	Modifier modifiers;

	/*
	 * field, method, interface, class or enum
	 */
	SourceType sourceType;
	
	/*
	 * might return something
	 */
	String returnType;
	
	/*
	 * name of the field, method, interface, class or enum
	 */
	String codeName;
	
	/*
	 * extends
	 */
	List<String> extensions;
	
	/*
	 * implements
	 */
	List<String> implmentations;
	
	/*
	 * throws
	 */
	List<String> throwing;
	
	public CodeBuilder()
	{
		visibility = Visibility.NOTHING;
		modifiers = Modifier.NOTHING;
		sourceType = SourceType.NULL;
		returnType = "";
		codeName = "";
		extensions = new ArrayList<String>();
		implmentations = new ArrayList<String>();
		throwing = new ArrayList<String>();
	}
}
