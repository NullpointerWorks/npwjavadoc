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
	 * interface, class, annotation or enum
	 */
	SourceType sourceType;
	
	/*
	 * static, abstract, final or nothing
	 */
	List<Modifier> modifiers;
	
	/*
	 * field, constructor, method
	 * the type is inferred after information has been gathered
	 */
	ItemType itemType;
	
	/*
	 * field type or method return type
	 */
	DataType dataType;

	/*
	 * name of the field, method, interface, class or enum
	 */
	String codeName;
	
	/*
	 * field or enum value, if any
	 */
	String value;
	
	/*
	 * parameters
	 */
	List<CodeBuilder> params;
	
	/*
	 * extends
	 */
	List<String> extensions;
	
	/*
	 * implements
	 */
	List<String> implementations;
	
	/*
	 * throws
	 */
	List<String> throwing;
	
	public CodeBuilder()
	{
		visibility 	= Visibility.NULL;
		sourceType 	= SourceType.NULL;
		modifiers 	= new ArrayList<Modifier>();
		itemType 	= ItemType.NULL;
		dataType 	= DataType.NULL;
		codeName 	= "";
		value 		= "";
		params 			= new ArrayList<CodeBuilder>();
		extensions 		= new ArrayList<String>();
		implementations	= new ArrayList<String>();
		throwing 		= new ArrayList<String>();
	}
	
	public void setVisibility(Visibility visibility) 
	{
		this.visibility=visibility;
	}

	public void setSourceType(SourceType sourceType) 
	{
		this.sourceType=sourceType;
	}

	public void setModifier(Modifier modifier) 
	{
		modifiers.add(modifier);
	}
	
	
	
	
	
	
	
}
