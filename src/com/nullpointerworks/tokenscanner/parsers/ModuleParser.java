package com.nullpointerworks.tokenscanner.parsers;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

/**
 * 
 */
public class ModuleParser extends AbstractSourceParser
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
	public ModuleParser(Document doc, String file)
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
