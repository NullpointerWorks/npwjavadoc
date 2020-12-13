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
	ItemName codeName;
	
	/*
	 * field or enum value, if any
	 */
	ItemValue value;
	
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
		codeName 	= ItemName.NULL;
		value 		= ItemValue.NULL;
		params 			= new ArrayList<CodeBuilder>();
		extensions 		= new ArrayList<String>();
		implementations	= new ArrayList<String>();
		throwing 		= new ArrayList<String>();
	}
	
	public Visibility getVisibility() {return visibility;}
	public void setVisibility(Visibility v) {this.visibility=v;}

	public SourceType getSourceType() {return sourceType;}
	public void setSourceType(SourceType st) {this.sourceType=st;}
	
	public List<Modifier> getModifier() {return modifiers;}
	public void setModifier(Modifier modifier) 
	{
		if (!modifiers.contains(modifier))
		{
			modifiers.add(modifier);
		}
	}
	
	public ItemName getItemName() {return codeName;}
	public void setItemName(ItemName cn) {this.codeName=cn;}
	
	
	
	
	
	
	
	
}
