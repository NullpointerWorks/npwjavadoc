package com.nullpointerworks.scanner2;

import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

public class SourceParser implements ISourceParser
{
	/*
	 * comment block signifier
	 */
	final String C_BLOCK_START = "/**";
	final String C_BLOCK_END = "*/";
	
	
	/*
	 * document building
	 */
	private StringBuilder tokenBuilder;
	private Document doc;
	private Element root;
	
	public SourceParser()
	{
		doc = new Document();
		root = new Element("source");
		doc.setRootElement(root);
		tokenBuilder = new StringBuilder();
	}
	
	// ============================================================================================
	
	@Override
	public void nextLine(String line)
	{
		/*
		 * trim whitespace
		 */
		line = line.trim();
		
		/*
		 * if the line is a comment line, skip
		 */
		if (line.startsWith("//")) return;
		
		/*
		 * if the line has no text, skip
		 */
		int leng = line.length();
		if (leng < 1) return;
		
		/*
		 * preprocessor: tokenize all code markers
		 */
		String preproc = doPreprocessor( line );
		leng = preproc.length();
		
		/*
		 * feed all characters in the line
		 */
		for (int i=0; i<leng; i++)
		{
			String character = preproc.substring(i, i+1);
			nextCharacter(character);
		}
	}
	
	/*
	 * tokenize important code markers before parsing
	 */
	private String doPreprocessor(String line) 
	{
		line = line.replace("(", " ( ");
		line = line.replace(")", " ) ");

		line = line.replace("{", " { ");
		line = line.replace("}", " } ");
		
		line = line.replace("[", " [ ");
		line = line.replace("]", " ] ");
		
		line = line.replace("=", " = ");
		line = line.replace(",", " , ");
		line = line.replace(";", " ; ");
		
		return line.trim().replaceAll("\\s+", " ")+" ";
	}
	
	// ============================================================================================
	
	@Override
	public void nextCharacter(String character)
	{
		/*
		 * store the passing characters into a string until a special marker is detected.
		 * A special marker could be:
		 * - space
		 * - braces of any type, (), {}, []
		 * - end of code line ;
		 */
		if (character.equalsIgnoreCase(" "))
		{
			String token = tokenBuilder.toString();
			
			
			
			
			tokenBuilder.setLength(0);// reset builder
			return;
		}
		
		/*
		 * add character to token
		 */
		tokenBuilder.append(character);
	}
	
}
