package com.nullpointerworks.tokenscanner.parsers.enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.tokenscanner.parsers.AbstractSourceParser;
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
	private int curlyBraceCount = 1;
	
	/**
	 * 
	 */
	public EnumParser(Document doc, String file)
	{
		super();
		this.file = file;
		this.doc = doc;
		this.root = doc.getRootElement();
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
	
	// ============================================================================================
	
	private List<TokenBuilder> tbuilders = new ArrayList<TokenBuilder>();
	private TokenBuilder tbuilder = new TokenBuilder();
	
	@Override
	public void nextToken(String token) 
	{
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		
		if (equals(token,"}") && curlyBraceCount==0)
		{
			parseBuilder();
			parseBuilderList();
			return;
		}
		
		if (curlyBraceCount>1) return;
		
		if (equals(token,C_BLOCK_START))
		{
			parseBuilder();
			return;
		}
		
		if (equals(token,C_END))
		{
			parseBuilder();
			return;
		}
		
		if (equals(token,"}"))
		{
			parseBuilder();
			return;
		}
		
		if (equals(token,","))
		{
			parseBuilder();
			return;
		}
		
		if (equals(token,";"))
		{
			parseBuilder();
			return;
		}
		
		tbuilder.addToken(token);
	}

	private void parseBuilder() 
	{
		if (tbuilder.getSize()>0) tbuilders.add(tbuilder);
		tbuilder = new TokenBuilder();
	}
	
	private void parseBuilderList() 
	{
		for (TokenBuilder tb : tbuilders)
		{
			Log.out( "> " + tb.getString() );
		}
	}
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
}
