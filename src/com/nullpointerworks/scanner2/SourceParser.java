package com.nullpointerworks.scanner2;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

public class SourceParser 
{
	private boolean isCommentary;
	private boolean isModule;
	private boolean isInterface;
	private boolean isClass;
	private boolean isEnum;
	private boolean isField;
	private boolean isMethod;
	
	private StringBuilder tokenBuilder;
	private Document doc;
	private Element root;
	
	public SourceParser()
	{
		/*
		 * prepare
		 */
		doc = new Document();
		root = new Element("source");
		doc.setRootElement(root);
		isCommentary = false;
		isModule = false;
		isInterface = false;
		isClass = false;
		isEnum = false;
		isField = false;
		isMethod = false;
		
		tokenBuilder = new StringBuilder();
	}
	
	public void nextCharacter(String character)
	{
		
		
		
		
	}
	
	
	
}
