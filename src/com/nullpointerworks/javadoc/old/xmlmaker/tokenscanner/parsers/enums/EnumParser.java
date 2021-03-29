package com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.javadoc.old.parse.java.AbstractSourceParser;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

/**
 * 
 */
public class EnumParser extends AbstractSourceParser
{
	/*
	 * comment block signifier
	 */
	final String C_BLOCK_START = "/**";
	final String C_START = "/*";
	final String C_END = "*/";
	
	/*
	 * document building
	 */
	private Document doc;
	private Element root;
	private final String file;
	
	/**
	 * 
	 */
	public EnumParser(Document doc, String file)
	{
		super();
		this.file = file;
		this.doc = doc;
		this.root = doc.getRootElement();
		tbuilders = new ArrayList<TokenGroup>();
		tbuilder = new TokenGroup();
		curlyBraceCount = 1;
	}
	
	// ============================================================================================
	
	private List<TokenGroup> tbuilders;
	private TokenGroup tbuilder;
	private int curlyBraceCount;
	
	@Override
	public void nextToken(String token) 
	{
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		
		if (equals(token,"}") && curlyBraceCount==0)
		{
			parseBuilder();
			resetBuilder();
			parseBuilderList();
			return;
		}
		
		if (curlyBraceCount>1) return;
		
		if (equals(token,C_BLOCK_START))
		{
			resetBuilder();
			return;
		}
		
		if (equals(token,C_END))
		{
			resetBuilder();
			return;
		}
		
		if (equals(token,"}"))
		{
			parseBuilder();
			resetBuilder();
			return;
		}
		
		if (equals(token,","))
		{
			parseBuilder();
			resetBuilder();
			return;
		}
		
		if (equals(token,";"))
		{
			parseBuilder();
			resetBuilder();
			return;
		}
		
		tbuilder.addToken(token);
	}
	
	private void parseBuilder() 
	{
		if (tbuilder.getSize()>0) tbuilders.add(tbuilder);
	}
	
	private void resetBuilder() 
	{
		tbuilder = new TokenGroup();
	}
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
	
	// ============================================================================================
	
	private void parseBuilderList() 
	{
		for (TokenGroup tg : tbuilders)
		{
			Log.out( "> " + tg.getString() );
			List<String> tokens = tg.getTokens();
			for (String t : tokens) nextGroupToken(t);
			nextGroupToken("\n");
			
			
		}
	}
	
	public void nextGroupToken(String token) 
	{
		if (equals(token,"\n"))
		{
			
			return;
		}
		
		
		
		
		
		
		
		
	}
	
	private void writeToFile(Document doc, String outFile) 
	{
		/*
		 * write to XML file
		 */
		String name = FileUtil.getFileNameFromPath(outFile);
		String path = "xml/" + name + ".xml";
		try 
		{
			DocumentIO.write(doc, path, FormatBuilder.getPrettyFormat());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
