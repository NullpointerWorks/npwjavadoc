package com.nullpointerworks.scanner2;

import java.io.IOException;
import java.util.List;

import com.nullpointerworks.scanner2.builder.CodeBuilder;
import com.nullpointerworks.scanner2.builder.CommentBuilder;
import com.nullpointerworks.scanner2.builder.ItemType;
import com.nullpointerworks.scanner2.builder.Modifier;
import com.nullpointerworks.scanner2.builder.SourceType;
import com.nullpointerworks.scanner2.builder.Visibility;
import com.nullpointerworks.scanner2.builder.Package;

import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.Log;
import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

public class SourceParser implements ISourceParser
{
	/*
	 * document building
	 */
	private StringBuilder tokenBuilder;
	private Document doc;
	private Element root;
	private final String outFile;
	
	public SourceParser(String outFile)
	{
		this.outFile=outFile;
		doc = new Document();
		root = new Element("source");
		doc.setRootElement(root);
		tokenBuilder = new StringBuilder();
	}
	
	private void parseBuilder(CodeBuilder builder) 
	{
		/*
		 * 
		 */
		builder.inferTypeInfo();
		
		/*
		 * if parsing package info
		 */
		if (builder.getPackage() == Package.PACKAGE)
		{
			isPackage(builder, root);			
			return;
		}
		
		/*
		 * detect if its a source file 
		 */
		if (builder.getSourceType() != SourceType.NULL)
		{
			isSourceInfo(builder, root);
			return;
		}
		
		
		
		
	}
	
	private void isPackage(CodeBuilder builder, Element root) 
	{
		Element elPack = new Element("package");
		
		/*
		 * name
		 */
		List<String> uid = builder.getUnidentified();
		if (uid.size()==1)
		{
			/*
			 * for package info, only one token should be present and should be the name
			 */
			String n = uid.get(0);
			elPack.addChild( new Element("name").setText(n) );
		}
		else
		{
			Log.err("Error: Package information incomplete");
		}
		
		root.addChild(elPack);
	}

	private void isSourceInfo(CodeBuilder builder, Element root) 
	{
		Element elementType = new Element("type");
		
		/*
		 * source type
		 */
		String st = builder.getSourceType().toString();
		elementType.addChild( new Element("sourcetype").setText(st) );
		
		/*
		 * visibility
		 */
		if (builder.getVisibility() != Visibility.NULL)
		{
			String v = builder.getVisibility().toString();
			elementType.addChild( new Element("visibility").setText(v) );
		}
		
		/*
		 * modifiers
		 */
		List<Modifier> mods = builder.getModifier();
		if (mods.size()>0)
		{
			String m = mods.get(0).toString();
			elementType.addChild( new Element("modifier").setText(m) );
		}
		
		/*
		 * name
		 */
		List<String> uid = builder.getUnidentified();
		if (uid.size()>0)
		{
			/*
			 * when source info is found, the first token should be the name
			 */
			String n = uid.get(0);
			elementType.addChild( new Element("name").setText(n) );
		}
		else
		{
			Log.err("Error: Source information incomplete");
		}
		
		/*
		 * extending
		 */
		List<String> ext = builder.getExtended();
		if (ext.size()>0)
		{
			for (String e : ext)
			{
				elementType.addChild( new Element("extends").setText(e) );
			}
		}
		
		/*
		 * implementing
		 */
		List<String> imp = builder.getImplemented();
		if (imp.size()>0)
		{
			for (String e : imp)
			{
				elementType.addChild( new Element("implements").setText(e) );
			}
		}
		
		root.addChild(elementType);
	}
	
	private void writeToFile() 
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
			nextToken(token);
			tokenBuilder.setLength(0);// reset builder
			return;
		}
		
		/*
		 * add character to token
		 */
		tokenBuilder.append(character);
	}
	
	// ============================================================================================
	
	/*
	 * comment block signifier
	 */
	final String C_BLOCK_START = "/**";
	final String C_BLOCK_END = "*/";
	
	/*
	 * code building
	 */
	private CodeBuilder codeBuilder = new CodeBuilder();
	private CommentBuilder commentBuilder = new CommentBuilder();
	private boolean hasCommentBranch = false;
	private boolean hasPackageBranch = false;
	private boolean hasSourceBranch = false;
	private boolean hasFieldBranch = false;
	private boolean isImplementing = false;
	private boolean isExtending = false;
	
	private void resetBuilder(CodeBuilder builder)
	{
		builder.reset();
		hasCommentBranch = false;
		hasPackageBranch = false;
		hasSourceBranch = false;
		hasFieldBranch = false;
		isImplementing = false;
		isExtending = false;
	}
	
	@Override
	public void nextToken(String token)
	{
		doProcessToken(codeBuilder, token);
	}
	
	/*
	 * takes a token and decides what to do with it
	 */
	private int curlyBraceCount = 0;
	private void doProcessToken(CodeBuilder builder, String token) 
	{
		/*
		 * detect when parsing code inside a code block. skip code inside code blocks
		 */
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		if (curlyBraceCount>1) return;
		
		/*
		 * if the end of a piece of code has been detected, parse the CodeBuilder.
		 * if the end of the file has been detected, write the XML file
		 */
		if (isEndOfSource(token))
		{
			writeToFile();
			resetBuilder(builder);
			return;
		}
		if (isEndOfCode(token))
		{
			parseBuilder(builder);
			resetBuilder(builder);
			return;
		}
		
		/*
		 * detect commentary blocks. check this before checking other code. 
		 * commentary can contain code examples. we don't want to falsely 
		 * trigger the CodeBuilder.
		 */
		if (token.equalsIgnoreCase(C_BLOCK_START))
		{
			hasCommentBranch = true;
			return;
		}
		if (hasCommentBranch)
		{
			hasCommentBranch = !token.equalsIgnoreCase(C_BLOCK_END);
			doCommentaryBranch(commentBuilder, token);
			return;
		}
		
		/*
		 * started a new file
		 */
		if (equals(token,"package"))
		{
			builder.setPackage(Package.PACKAGE);
			hasPackageBranch = true;
			return;
		}
		
		/*
		 * the file imports classes
		 */
		if (equals(token,"import"))
		{
			return;
		}
		
		/*
		 * something visible has been detected
		 */
		boolean isVisibility = isVisibility(token);
		if (isVisibility)
		{
			builder.setVisibility( getVisibility(token) );
			return;
		}
		
		/*
		 * if token is a modifier
		 */
		boolean isModifier = isModifier(token);
		if (isModifier)
		{
			builder.setModifier( getModifier(token) );
			return;
		}
		
		/*
		 * detect java source file type
		 */
		boolean isSourceType = isSourceType(token);
		if (isSourceType)
		{
			hasSourceBranch = true;
			builder.setSourceType( getSourceType(token) );
			return;
		}
		
		/*
		 * check for data assignment. when found, set type and name
		 */
		if (equals(token,"="))
		{
			hasFieldBranch = true;
			builder.setItemType(ItemType.FIELD);
			return;
		}
		
		/*
		 * detect method/constructor signifier
		 */
		if (equals(token,"("))
		{
			builder.setItemType(ItemType.METHOD_CONSTRUCTOR);
			return;
		}
		
		/*
		 * branch off when item identification has been determined
		 */
		if (hasPackageBranch)
		{
			builder.setUnidentified(token);
			return;
		}
		if (hasSourceBranch)
		{
			doSourceBranch(builder, token);
			return;
		}
		if (hasFieldBranch)
		{
			
			return;
		}
		//*/
		
		builder.setUnidentified(token);
	}
	
	/*
	 * 
	 */
	private void doCommentaryBranch(CommentBuilder builder, String token) 
	{
		
	}
	
	/*
	 * 
	 */
	private void doSourceBranch(CodeBuilder builder, String token) 
	{
		/*
		 * if a separator, skip
		 */
		if (equals(token,",")) return;
		
		/*
		 * check implementations first
		 */
		if (equals(token,"implements"))
		{
			isImplementing = true;
			return;
		}
		if (isImplementing)
		{
			builder.setImplemented(token);
			return;
		}
		
		/*
		 * check extensions
		 */
		if (equals(token,"extends"))
		{
			isExtending = true;
			return;
		}
		if (isExtending)
		{
			builder.setExtended(token);
			return;
		}
		
		builder.setUnidentified(token);
	}

	/*
	 * code end. { and } are also used by internal block code
	 */
	private boolean isEndOfCode(String token) 
	{
		if (token.equalsIgnoreCase(")")) return true;
		if (token.equalsIgnoreCase(";")) return true;
		if (token.equalsIgnoreCase("{") && curlyBraceCount<2) return true;
		return false;
	}
	
	private boolean isEndOfSource(String token) 
	{
		if (token.equalsIgnoreCase("}") && curlyBraceCount==0) return true;
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
		if (token.equalsIgnoreCase("module")) return true;
		return false;
	}
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
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
}
