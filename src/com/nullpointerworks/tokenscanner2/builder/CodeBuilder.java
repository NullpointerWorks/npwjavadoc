package com.nullpointerworks.tokenscanner2.builder;

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
	public void setVisibility(String v) {this.visibility=getVisibility(v);}
	private Visibility getVisibility(String token) 
	{
		if (token.equalsIgnoreCase("public")) return Visibility.PUBLIC;
		if (token.equalsIgnoreCase("protected")) return Visibility.PROTECTED;
		if (token.equalsIgnoreCase("private")) return Visibility.PRIVATE;
		return Visibility.NULL;
	}
	
	/*
	 * module, interface, class, annotation or enum
	 */
	private SourceType sourceType;
	public boolean hasSourceType() {return sourceType != SourceType.NULL;}
	public SourceType getSourceType() {return sourceType;}
	public void setSourceType(SourceType st) {this.sourceType=st;}
	public void setSourceType(String st) {this.sourceType=getSourceType(st);}
	private SourceType getSourceType(String token) 
	{
		if (token.equalsIgnoreCase("interface")) return SourceType.INTERFACE;
		if (token.equalsIgnoreCase("class")) return SourceType.CLASS;
		if (token.equalsIgnoreCase("enum")) return SourceType.ENUM;
		if (token.equalsIgnoreCase("@interface")) return SourceType.ANNOTATION;
		if (token.equalsIgnoreCase("module")) return SourceType.MODULE;
		return SourceType.NULL;
	}
	
	/*
	 * static, abstract, final or nothing
	 */
	private List<Modifier> modifiers;
	public boolean hasModifier() {return modifiers.size()>0;}
	public List<Modifier> getModifier() {return modifiers;}
	public void setModifier(Modifier modifier) {if (!modifiers.contains(modifier)) modifiers.add(modifier);}
	public boolean hasModifier(Modifier m) {return modifiers.contains(m);}
	public void setModifier(String modifier) 
	{
		setModifier(getModifier(modifier));
	}
	private Modifier getModifier(String token) 
	{
		if (token.equalsIgnoreCase("static")) return Modifier.STATIC;
		if (token.equalsIgnoreCase("final")) return Modifier.FINAL;
		if (token.equalsIgnoreCase("abstract")) return Modifier.ABSTRACT;
		if (token.equalsIgnoreCase("strictfp")) return Modifier.STRICTFP;
		if (token.equalsIgnoreCase("default")) return Modifier.DEFAULT;
		return Modifier.NULL;
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
	
	/*
	 * parameters
	 */
	private boolean parameterCapable = false;
	public boolean isParameterCapable() {return parameterCapable;}
	public void setParameterCapable(boolean p) {parameterCapable=p;}
	private List<CodeBuilder> params;
	public boolean hasParameters() {return params.size()>0;}
	public List<CodeBuilder> getParameters() {return params;}
	public void setParameter(CodeBuilder param)
	{
		param.inferTypeInfo();
		params.add(param);
		parameterCapable = true;
	}
	
	/*
	 * annotation
	 */
	private Annotation lastAnnotation;
	private List<Annotation> annotation;
	public List<Annotation> getAnnotation() {return annotation;}
	public void setAnnotation(String ann) 
	{
		lastAnnotation = new Annotation(ann);
		annotation.add( lastAnnotation );
	}
	public void setAnnotation(String ann, String...params) {annotation.add( new Annotation(ann,params) );}
	public void setAnnotationParameters(List<String> params) 
	{
		lastAnnotation.setParameters(params);
	}
	
	/*
	 * template
	 */
	private List<String> templates;
	public List<String> getTemplate() {return templates;}
	public void setTemplate(String ex) {templates.add(ex);}
	
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
		annotation 		= new ArrayList<Annotation>();
		templates 		= new ArrayList<String>();
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
		
		isArray = false;
		parameterCapable = false;
		params.clear();
		
		annotation.clear();
		templates.clear();
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
				if (isPackage != Package.NULL) { }
				else
				if (sourceType != SourceType.NULL) { }
				else
				{
					itemType = ItemType.FIELD;
				}
			}
			else
			{
				// try to find data type, if not found, probably a constructor
				if (unidentified.size() == 1)
				{
					itemType = ItemType.CONSTRUCTOR;
				}
				else
				if (unidentified.size() == 2)
				{
					itemType = ItemType.METHOD;
				}
			}
		}
	}
	
}
