package com.nullpointerworks.parse.java;

/**
 * 
 */
public abstract class AbstractSourceParser implements ISourceParser 
{
	private StringBuilder tokenBuilder;
	
	/**
	 * 
	 */
	public AbstractSourceParser()
	{
		tokenBuilder = new StringBuilder();
	}
	
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
		 * feed all tokens in the line
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
		 * template
		 */
		line = line.replace("<", " < ");
		line = line.replace(">", " > ");
		
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
			nextToken(token);
			tokenBuilder.setLength(0);// reset builder
			return;
		}
		
		/*
		 * add character to token
		 */
		tokenBuilder.append(character);
	}
	
	@Override
	public abstract void nextToken(String token);
}
