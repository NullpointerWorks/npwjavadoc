package com.nullpointerworks.scanner2;

import com.nullpointerworks.scanner2.builder.CodeBuilder;
import com.nullpointerworks.scanner2.builder.Modifier;
import com.nullpointerworks.scanner2.builder.SourceType;
import com.nullpointerworks.scanner2.builder.Visibility;
import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

public class SourceParser implements ISourceParser
{
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
		/*
		 * method parameters
		 */
		line = line.replace("(", " ( ");
		line = line.replace(")", " ) ");
		
		/*
		 * code blocks
		 */
		line = line.replace("{", " { ");
		line = line.replace("}", " } ");
		
		/*
		 * arrays
		 */
		line = line.replace("[", " [ ");
		line = line.replace("]", " ] ");
		
		/*
		 * other
		 */
		line = line.replace("//", " // ");
		//line = line.replace("*", " * ");
		line = line.replace("=", " = ");
		line = line.replace(",", " , ");
		line = line.replace(";", " ; ");
		
		/*
		 * convert duplicate spaces into as single space, add a line-end space at the end
		 */
		line = line.trim();
		return line.replaceAll("\\s+", " ")+"\n";
	}
	
	// ============================================================================================
	
	@Override
	public void nextCharacter(String character)
	{
		boolean newLine = character.equalsIgnoreCase("\n");
		boolean whiteSpace = character.equalsIgnoreCase(" ");
		
		/*
		 * do something when a new line is detected
		 * some code is newline sensitive, like comments
		 */
		if (newLine)
		{
			
		}
		
		/*
		 * store the passing characters into a string until a special marker is detected.
		 * A special marker could be:
		 * - space
		 * - braces of any type, (), {}, []
		 * - end of code line ;
		 */
		if (newLine || whiteSpace)
		{
			String token = tokenBuilder.toString();
			doProcessToken(token);
			tokenBuilder.setLength(0);// reset builder
			return;
		}
		
		/*
		 * add character to token
		 */
		tokenBuilder.append(character);
	}

	/*
	 * comment block signifier
	 */
	final String C_BLOCK_START = "/**";
	final String C_BLOCK_END = "*/";
	
	/*
	 * code building
	 */
	private CodeBuilder codeBuilder;
	
	
	/*
	 * takes a token and decides what to do with it
	 */
	private void doProcessToken(String token) 
	{
		Log.out(token);
		
		/*
		 * started a new file
		 */
		if (token.equalsIgnoreCase("package"))
		{
			return;
		}
		
		/*
		 * the file imports classes
		 */
		if (token.equalsIgnoreCase("import"))
		{
			return;
		}
		
		/*
		 * if the end of a piece of code has been detected, parse the CodeBuilder
		 */
		if (isEndOfCode(token))
		{
			
		}
		
		boolean isVisibility = isVisibility(token);
		boolean isSourceType = isSourceType(token);
		boolean isModifier = isModifier(token);
		
		/*
		 * if a modifier or visibility has been detected, start recording code
		 */
		if (isVisibility || isModifier)
		{
			codeBuilder = new CodeBuilder();
		}
		
		/*
		 * something visible has been detected
		 */
		if (isVisibility)
		{
			codeBuilder.setVisibility( getVisibility(token) );
			return;
		}
		
		/*
		 * detect java source file type
		 */
		if (isSourceType)
		{
			codeBuilder.setSourceType( getSourceType(token) );
			return;
		}
		
		/*
		 * 
		 */
		if (isModifier)
		{
			codeBuilder.setModifier( getModifier(token) );
			return;
		}
		
	}
	
	/*
	 * code end. } are also present at internal block code
	 */
	private boolean isEndOfCode(String token) 
	{
		if (token.equalsIgnoreCase(";")) return true;
		if (token.equalsIgnoreCase("}")) return true;
		return false;
	}

	/*
	 * package-private has no keyword
	 */
	private boolean isVisibility(String token) 
	{
		if (token.equalsIgnoreCase("public")) return true;
		if (token.equalsIgnoreCase("protected")) return true;
		if (token.equalsIgnoreCase("private")) return true;
		return false;
	}
	private Visibility getVisibility(String token) 
	{
		if (token.equalsIgnoreCase("public")) return Visibility.PUBLIC;
		if (token.equalsIgnoreCase("protected")) return Visibility.PROTECTED;
		if (token.equalsIgnoreCase("private")) return Visibility.PRIVATE;
		return Visibility.NULL;
	}
	
	/*
	 * 
	 */
	private boolean isSourceType(String token) 
	{
		if (token.equalsIgnoreCase("interface")) return true;
		if (token.equalsIgnoreCase("class")) return true;
		if (token.equalsIgnoreCase("enum")) return true;
		if (token.equalsIgnoreCase("@interface")) return true;
		return false;
	}
	private SourceType getSourceType(String token) 
	{
		if (token.equalsIgnoreCase("interface")) return SourceType.INTERFACE;
		if (token.equalsIgnoreCase("class")) return SourceType.CLASS;
		if (token.equalsIgnoreCase("enum")) return SourceType.ENUM;
		if (token.equalsIgnoreCase("@interface")) return SourceType.ANNOTATION;
		return SourceType.NULL;
	}
	
	/*
	 * 
	 */
	private boolean isModifier(String token) 
	{
		if (token.equalsIgnoreCase("static")) return true;
		if (token.equalsIgnoreCase("final")) return true;
		if (token.equalsIgnoreCase("abstract")) return true;
		if (token.equalsIgnoreCase("strictfp")) return true;
		if (token.equalsIgnoreCase("default")) return true;
		return false;
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
}
