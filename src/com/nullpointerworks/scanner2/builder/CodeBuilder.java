package com.nullpointerworks.scanner2.builder;

import java.util.ArrayList;
import java.util.List;

public class CodeBuilder 
{
	/*
	 * package signal
	 */
	private Package isPackage;
	public boolean hasPackage() {return isPackage != Package.NULL;}
	public Package getPackage() {return isPackage;}
	public void setPackage(Package pack) {isPackage = pack;}
	
	/*
	 * public, protected, private or nothing
	 */
	private Visibility visibility;
	public boolean hasVisibility() {return visibility != Visibility.NULL;}
	public Visibility getVisibility() {return visibility;}
	public void setVisibility(Visibility v) {this.visibility=v;}
	
	/*
	 * module, interface, class, annotation or enum
	 */
	private SourceType sourceType;
	public boolean hasSourceType() {return sourceType != SourceType.NULL;}
	public SourceType getSourceType() {return sourceType;}
	public void setSourceType(SourceType st) {this.sourceType=st;}
	
	/*
	 * static, abstract, final or nothing
	 */
	private List<Modifier> modifiers;
	public boolean hasModifier() {return modifiers.size()>0;}
	public List<Modifier> getModifier() {return modifiers;}
	public void setModifier(Modifier modifier) 
	{
		if (!modifiers.contains(modifier)) modifiers.add(modifier);
	}
	
	/*
	 * unidentified pieces of information. for example names and custom data types
	 */
	private List<String> unidentified;
	public List<String> getUnidentified() {return unidentified;}
	public void setUnidentified(String uid) 
	{
		unidentified.add(uid);
	}
	
	/*
	 * field type or method return type
	 */
	private boolean isArray;
	public boolean isArray() {return isArray;}
	public void setArray(boolean b) {isArray=b;}
	
	
	
	private String dataType;

	/*
	 * name of the field, method, interface, class or enum
	 */
	private String codeName;
	
	/*
	 * field or enum value, if any
	 */
	private ItemValue value;
	public boolean hasItemValue() {return value != ItemValue.NULL;}
	public ItemValue getItemValue() {return value;}
	public void setItemValue(ItemValue v) {value = v;}
	
	/*
	 * parameters
	 */
	private boolean parameterCapable = false;
	public boolean isParameterCapable() {return parameterCapable;}
	public void setParameterCapable(boolean p) {parameterCapable=p;}
	private List<CodeBuilder> params;
	public boolean hasParameters() {return params.size()>0;}
	public void setParameter(CodeBuilder param)
	{
		
		
		parameterCapable = true;
	}
	
	/*
	 * extends
	 */
	private List<String> extensions;
	public List<String> getExtended() {return extensions;}
	public void setExtended(String ex) {extensions.add(ex);}
	
	private boolean isExtending;
	public boolean isExtending() {return isExtending;}
	public void isExtending(boolean e) {isExtending = e;}
	
	/*
	 * implements
	 */
	private List<String> implementations;
	public List<String> getImplemented() {return implementations;}
	public void setImplemented(String imp) {implementations.add(imp);}
	
	boolean isImplementing;
	public boolean isImplementing() {return isImplementing;}
	public void isImplementing(boolean i) {isImplementing = i;}
	
	/*
	 * throws
	 */
	private List<String> throwing;
	public List<String> getThrowing() {return throwing;}
	public void setThrowing(String th) {throwing.add(th);}
	
	boolean isThrowing;
	public boolean isThrowing() {return isThrowing;}
	public void isThrowing(boolean t) {isThrowing = t;}
	
	/*
	 * field, constructor, method
	 * the type can be inferred after sufficient information has been gathered
	 */
	private ItemType itemType;
	public ItemType getItemType() {return itemType;}
	public void setItemType(ItemType it) {this.itemType=it;}
	
	public CodeBuilder()
	{
		modifiers 		= new ArrayList<Modifier>();
		unidentified	= new ArrayList<String>();
		params 			= new ArrayList<CodeBuilder>();
		extensions 		= new ArrayList<String>();
		implementations	= new ArrayList<String>();
		throwing 		= new ArrayList<String>();
		reset();
	}
	
	public void reset()
	{
		isPackage = Package.NULL;
		visibility = Visibility.NULL;
		sourceType = SourceType.NULL;
		modifiers.clear();
		unidentified.clear();
		
		dataType = "";
		isArray = false;
		codeName = "";
		value = ItemValue.NULL;
		parameterCapable = false;
		params.clear();
		
		extensions.clear();
		isExtending = false;
		implementations.clear();
		isImplementing = false;
		throwing.clear();
		isThrowing = false;
		
		itemType = ItemType.NULL;
	}
	
	/*
	 * 
	 */
	public void inferTypeInfo() 
	{
		/*
		 * 
		 */
		if (itemType == ItemType.NULL)
		{
			if (!parameterCapable)
			{
				itemType = ItemType.FIELD;
			}
			
			
			
		}
		
		
		
		/*
		 * if already set to FIELD, then = sign was detected
		 */
		if (itemType == ItemType.FIELD)
		{
			if (unidentified.size()>1)
			{
				dataType = unidentified.get(0);
				codeName = unidentified.get(1);
			}
		}
		else
		if (itemType == ItemType.METHOD)
		{
			// try to find data type, if not found, probably a constructor
			
		}
		else
		if (itemType == ItemType.CONSTRUCTOR)
		{
			
		}
		
		
		
	}
	
	
	
}
