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
		
		/*
		 * when the parser confirmed it's a field
		 */
		if (builder.getItemType() == ItemType.FIELD)
		{
			isFieldInfo(builder, root);
			return;
		}
		
		/*
		 * parse methods and constructors
		 */
		if (builder.getItemType() == ItemType.METHOD)
		{
			isMethodInfo(builder, root);
			return;
		}
		if (builder.getItemType() == ItemType.CONSTRUCTOR)
		{
			isConstructorInfo(builder, root);
			return;
		}
		
		
		
		
		
	}
	
	private void isConstructorInfo(CodeBuilder builder, Element root) 
	{
		/*
		 * make xml element
		 */
		Element elMethod = new Element("constructor");
		
		/*
		 * visibility
		 * only ad the user available constructors
		 */
		var vis = builder.getVisibility();
		if (vis == Visibility.PUBLIC || vis == Visibility.PROTECTED)
		{
			String v = builder.getVisibility().toString();
			elMethod.addChild( new Element("visibility").setText(v) );
		}
		else
		{
			return;
		}
		
		/*
		 * name
		 */
		List<String> uid = builder.getUnidentified();
		if (uid.size()>0)
		{
			String n = uid.get(0);
			elMethod.addChild( new Element("name").setText(n) );
		}
		
		/*
		 * parameters
		 */
		List<CodeBuilder> params = builder.getParameters();
		for (CodeBuilder param : params)
		{
			List<String> puid = param.getUnidentified();
			
			/*
			 * make parameter xml element
			 */
			Element elParam = new Element("param");
			
			String n = puid.get(0);
			elParam.addChild( new Element("type").setText(n) );
			
			n = puid.get(1);
			elParam.addChild( new Element("name").setText(n) );
			
			/*
			 * templates
			 */
			List<String> tmp = param.getTemplate();
			if (tmp.size()>0)
			{
				for (String e : tmp)
				{
					elParam.addChild( new Element("template").setText(e) );
				}
			}
			
			elMethod.addChild(elParam);
		}
		
		/*
		 * throws
		 */
		List<String> thrw = builder.getThrowing();
		for (String t : thrw)
		{
			elMethod.addChild( new Element("throws").setText(t) );
		}
		
		root.addChild(elMethod);
	}
	
	private void isMethodInfo(CodeBuilder builder, Element root) 
	{
		/*
		 * interface methods are always public when visibility is undefined
		 */
		if (sourceType == SourceType.INTERFACE)
		{
			builder.setVisibility( Visibility.PUBLIC );
		}
		
		/*
		 * make xml element
		 */
		Element elMethod = new Element("method");
		
		/*
		 * visibility
		 */
		if (builder.getVisibility() != Visibility.NULL)
		{
			String v = builder.getVisibility().toString();
			elMethod.addChild( new Element("visibility").setText(v) );
		}
		
		/*
		 * modifiers
		 */
		List<Modifier> mods = builder.getModifier();
		if (mods.size()>0)
		{
			for (Modifier mod : mods)
			{
				String m = mod.toString();
				elMethod.addChild( new Element("modifier").setText(m) );
			}
		}
		
		/*
		 * name
		 */
		List<String> uid = builder.getUnidentified();
		if (uid.size()>1)
		{
			String arr = builder.isArray()?"[]":"";
			String n = uid.get(0);
			elMethod.addChild( new Element("type").setText(n+arr) );

			n = uid.get(1);
			elMethod.addChild( new Element("name").setText(n) );
			
			/*
			 * if a value is present, why not add it to the XML
			 */
			if (uid.size()>2)
			{
				n = uid.get(2);
				elMethod.addChild( new Element("value").setText(n) );
			}
		}
		
		/*
		 * parameters
		 */
		List<CodeBuilder> params = builder.getParameters();
		for (CodeBuilder param : params)
		{
			List<String> puid = param.getUnidentified();
			
			/*
			 * make parameter xml element
			 */
			Element elParam = new Element("param");
			
			String n = puid.get(0);
			elParam.addChild( new Element("type").setText(n) );
			
			n = puid.get(1);
			elParam.addChild( new Element("name").setText(n) );
			
			/*
			 * templates
			 */
			List<String> tmp = param.getTemplate();
			if (tmp.size()>0)
			{
				for (String e : tmp)
				{
					elParam.addChild( new Element("template").setText(e) );
				}
			}
			
			elMethod.addChild(elParam);
		}
		
		/*
		 * throws
		 */
		List<String> thrw = builder.getThrowing();
		for (String t : thrw)
		{
			elMethod.addChild( new Element("throws").setText(t) );
		}
		
		root.addChild(elMethod);
	}
	
	private void isFieldInfo(CodeBuilder builder, Element root) 
	{
		/*
		 * variable syntax for interfaces are always "public static final" and value 
		 * assigned. Skip privacy check if it's an interface.
		 */
		if (sourceType == SourceType.INTERFACE)
		{
			builder.setVisibility( Visibility.PUBLIC );
			builder.setModifier( Modifier.STATIC );
			builder.setModifier( Modifier.FINAL );
		}
		
		/*
		 * if the variable is not public or protected, thus invisible for users, skip
		 */
		var vis = builder.getVisibility();
		if (vis != Visibility.PUBLIC && vis != Visibility.PROTECTED)
		{
			return;
		}
		
		/*
		 * make xml element
		 */
		Element elField = new Element("field");
		
		/*
		 * visibility
		 */
		if (builder.getVisibility() != Visibility.NULL)
		{
			String v = builder.getVisibility().toString();
			elField.addChild( new Element("visibility").setText(v) );
		}
		
		/*
		 * modifiers
		 */
		List<Modifier> mods = builder.getModifier();
		if (mods.size()>0)
		{
			for (Modifier mod : mods)
			{
				String m = mod.toString();
				elField.addChild( new Element("modifier").setText(m) );
			}
		}
		
		/*
		 * name
		 */
		List<String> uid = builder.getUnidentified();
		if (uid.size()>1)
		{
			String arr = builder.isArray()?"[]":"";
			String n = uid.get(0);
			elField.addChild( new Element("type").setText(n+arr) );

			n = uid.get(1);
			elField.addChild( new Element("name").setText(n) );
			
			/*
			 * if a value is present, why not add it to the XML
			 */
			if (uid.size()==3)
			{
				n = uid.get(2);
				elField.addChild( new Element("value").setText(n) );
			}
		}
		
		/*
		 * templates
		 */
		List<String> tmp = builder.getTemplate();
		if (tmp.size()>0)
		{
			for (String e : tmp)
			{
				elField.addChild( new Element("template").setText(e) );
			}
		}
		
		root.addChild(elField);
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
		 * keep track of the file source type.
		 */
		sourceType = builder.getSourceType();
		String st = sourceType.toString();
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
			for (Modifier mod : mods)
			{
				String m = mod.toString();
				elementType.addChild( new Element("modifier").setText(m) );
			}
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
		 * templates
		 */
		List<String> tmp = builder.getTemplate();
		if (tmp.size()>0)
		{
			for (String e : tmp)
			{
				elementType.addChild( new Element("template").setText(e) );
			}
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
	final String C_START = "/*";
	final String C_END = "*/";
	
	/*
	 * code building
	 */
	private SourceType sourceType = SourceType.NULL;
	private CodeBuilder codeBuilder = new CodeBuilder();
	private CodeBuilder parambuilder = new CodeBuilder();
	private CommentBuilder commentBuilder = new CommentBuilder();
	private boolean hasParameterBranch = false;
	private boolean hasThrowingBranch = false;
	private boolean hasTemplateBranch = false;
	private boolean hasCommentBranch = false;
	private boolean hasPackageBranch = false;
	private boolean hasSourceBranch = false;
	private boolean isImplementing = false;
	private boolean isExtending = false;
	private boolean isThrowing = false;
	
	private void resetBuilder(CodeBuilder builder)
	{
		builder.reset();
		commentBuilder.reset();
		hasParameterBranch = false;
		hasThrowingBranch = false;
		hasTemplateBranch = false;
		hasCommentBranch = false;
		hasPackageBranch = false;
		hasSourceBranch = false;
		isImplementing = false;
		isExtending = false;
		isThrowing = false;
	}
	
	@Override
	public void nextToken(String token)
	{
		doProcessToken(codeBuilder, commentBuilder, token);
	}
	
	/*
	 * takes a token and decides what to do with it
	 */
	private int curlyBraceCount = 0;
	private void doProcessToken(CodeBuilder builder, CommentBuilder cbuilder, String token) 
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
			resetBuilder(builder);
			return;
		}
		if (isEndOfSource(token))
		{
			writeToFile();
			resetBuilder(builder);
			return;
		}
		
		/*
		 * detect when parsing code inside a code block. skip code inside code blocks
		 */
		if (curlyBraceCount>1) return;
		
		/*
		 * detect commentary blocks. check this before checking other code. 
		 * commentary can contain code examples. we don't want to falsely 
		 * trigger the CodeBuilder.
		 */
		if (equals(token,C_BLOCK_START))
		{
			hasCommentBranch = true;
			return;
		}
		if (hasCommentBranch) hasCommentBranch = !equals(token,C_END);
		if (hasCommentBranch)
		{
			doCommentaryBranch(cbuilder, token);
			return;
		}
		else
		{
			/*
			 * end of comment markers outside of the commentary block get a reset. Skip parsing
			 */
			if (equals(token,C_END))
			{
				resetBuilder(builder);
				return;
			}
		}
		
		/*
		 * detect annotations
		 * 
		 * TODO
		 * 
		 */
		if (token.startsWith("@"))
		{
			//Log.out("annotation: "+token);
			return;
		}
		
		/*
		 * detect end of an instruction ;
		 */
		if (isEndOfInstruction(token))
		{
			parseBuilder(builder);
			resetBuilder(builder);
			return;
		}
		
		/*
		 * check for data assignment. when found, set type and name.
		 * variable values should be mentioned in commentary. Otherwise 
		 * auto-complete will notify it's value
		 */
		if (equals(token,"="))
		{
			builder.setItemType(ItemType.FIELD);
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
		 * 
		 * TODO
		 */
		if (equals(token,"import"))
		{
			builder.setPackage(Package.IMPORT);
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
		 * detect method/constructor/annotation/enum signifier
		 * variables don't have ()
		 */
		if (equals(token,"("))
		{
			builder.setParameterCapable(true);
			hasParameterBranch = true;
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
		if (hasParameterBranch)
		{
			doParameterBranch(builder, token);
			return;
		}
		if (hasThrowingBranch)
		{
			doThrowingBranch(builder, token);
			return;
		}
		
		/*
		 * check template.
		 * after the item has been identified as a field, don't scan for templates again.
		 */
		if (builder.getItemType() != ItemType.FIELD) 
		{
			if (equals(token,">"))
			{
				hasTemplateBranch = false;
				return;
			}
			if (equals(token,"<"))
			{
				hasTemplateBranch = true;
				return;
			}
			if (hasTemplateBranch)
			{
				if (equals(token,",")) return;
				builder.setTemplate(token);
				return;
			}
		}
		
		/*
		 * check for array markers, or misc markers
		 */
		if (equals(token,"["))
		{
			builder.setArray(true);
			return;
		}
		if (equals(token,"]")) return;
		if (equals(token,"}")) return;
		//if (equals(token,")")) return;
		
		/*
		 * add unidentified marker
		 */
		builder.setUnidentified(token);
	}

	/*
	 * parse exception throwing tokens
	 */
	private void doThrowingBranch(CodeBuilder builder, String token) 
	{
		/*
		 * if a marker, skip
		 */
		//if (equals(token,")")) return;
		if (equals(token,",")) return;
		
		/*
		 * check exception throws
		 */
		if (equals(token,"throws"))
		{
			isThrowing = true;
			return;
		}
		if (isThrowing)
		{
			builder.setThrowing(token);
			return;
		}
		
		builder.setUnidentified(token);
	}

	/*
	 * build parameters for methods and constructors
	 */
	private void doParameterBranch(CodeBuilder builder, String token) 
	{
		/*
		 * check template
		 */
		if (equals(token,">"))
		{
			hasTemplateBranch = false;
			return;
		}
		if (equals(token,"<"))
		{
			hasTemplateBranch = true;
			return;
		}
		if (hasTemplateBranch)
		{
			if (equals(token,",")) return;
			parambuilder.setTemplate(token);
			return;
		}
		
		/*
		 * marks the end of parameters
		 */
		if (equals(token,")")) 
		{
			if (parambuilder.getUnidentified().size()>0) 
			{
				builder.setParameter(parambuilder);
				parambuilder = new CodeBuilder();
			}
			hasParameterBranch = false;
			hasThrowingBranch = true;
			return;
		}
		
		/*
		 * marks parameter separation
		 */
		if (equals(token,",")) 
		{
			builder.setParameter(parambuilder);
			parambuilder = new CodeBuilder();
			return;
		}
		
		//Log.out("param: "+token);
		parambuilder.setItemType(ItemType.PARAMETER);
		parambuilder.setUnidentified(token);
	}
	
	/*
	 * detect source type and extensions/implementations
	 */
	private void doSourceBranch(CodeBuilder builder, String token) 
	{
		/*
		 * if a separator, skip
		 */
		if (equals(token,",")) return;
		
		/*
		 * check template
		 */
		if (equals(token,">"))
		{
			hasTemplateBranch = false;
			return;
		}
		if (equals(token,"<"))
		{
			hasTemplateBranch = true;
			return;
		}
		
		/*
		 * check implementations first
		 */
		if (equals(token,"implements"))
		{
			isImplementing = true;
			return;
		}
		if (!hasTemplateBranch)
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
		if (!hasTemplateBranch)
		if (isExtending)
		{
			builder.setExtended(token);
			return;
		}
		
		/*
		 * add source templates
		 */
		if (hasTemplateBranch)
		{
			builder.setTemplate(token);
			return;
		}
		
		builder.setUnidentified(token);
	}
	
	/*
	 * TODO
	 * 
	 * 
	 * 
	 * 
	 */
	private void doCommentaryBranch(CommentBuilder cbuilder, String token) 
	{
		
		
		
		
		
		
		
		//Log.out(token);
		
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
}
