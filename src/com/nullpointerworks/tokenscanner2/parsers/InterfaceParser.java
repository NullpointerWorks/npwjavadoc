package com.nullpointerworks.tokenscanner2.parsers;

import java.io.IOException;

import com.nullpointerworks.tokenscanner2.builder.CodeBuilder;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

/**
 * 
 */
public class InterfaceParser extends AbstractSourceParser
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
	public InterfaceParser(Document doc, String file)
	{
		super();
		this.file = file;
		this.doc = doc;
		this.root = doc.getRootElement();
	}
	
	private void parseBuilder(CodeBuilder builder) 
	{
		
		
		
		
		
		
		
		
		
	}
	
	// ============================================================================================
	
	/*
	 * code builder
	 */
	CodeBuilder builder = new CodeBuilder();
	boolean hasCommentBranch = false;
	boolean hasTemplateBranch = false;

	private void resetBuilder() 
	{
		builder.reset();
		hasCommentBranch = false;
		hasTemplateBranch = false;
	}
	
	@Override
	public void nextToken(String token) 
	{
		/*
		 * keep track of embedded code blocks
		 */
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		
		/*
		 * if the end of a piece of code has been detected, parse the CodeBuilder.
		 * if the end of the file has been detected, write the XML file
		 */
		if (isEndOfDeclaration(token))
		{
			parseBuilder(builder);
			resetBuilder();
			return;
		}
		if (isEndOfSource(token))
		{
			writeToFile(doc, file);
			resetBuilder();
			return;
		}
		
		
		
		
		
		
		Log.out(token);
		
		
	}
	
	// ============================================================================================
	
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
	
	/*
	 * code end. { and } are also used by internal block code
	 */
	private boolean isEndOfInstruction(String token) 
	{
		if (token.equalsIgnoreCase(";")) return true;
		return false;
	}
	
	private boolean isEndOfDeclaration(String token) 
	{
		if (token.equalsIgnoreCase("{") && curlyBraceCount<3) return true;
		return false;
	}
	
	private boolean isEndOfSource(String token) 
	{
		if (token.equalsIgnoreCase("}") && curlyBraceCount==0) return true;
		return false;
	}
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
}
