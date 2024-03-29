package com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers;

import com.nullpointerworks.javadoc.old.parse.java.AbstractSourceParser;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

/**
 * 
 */
public class AnnotationParser extends AbstractSourceParser
{
	/*
	 * document building
	 */
	private Document doc;
	private Element root;
	private final String outFile;
	
	private int curlyBraceCount = 0;
	
	/**
	 * 
	 */
	public AnnotationParser(Document doc, String file)
	{
		super();
		this.outFile = file;
		this.doc = doc;
		this.root = doc.getRootElement();
	}
	
	@Override
	public void nextToken(String token) 
	{
		/*
		 * keep track of embedded code blocks
		 */
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		
		
	}
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
}
