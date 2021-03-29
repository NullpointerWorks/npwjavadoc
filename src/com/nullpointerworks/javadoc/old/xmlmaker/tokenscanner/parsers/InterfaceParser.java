package com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers;

import java.io.IOException;
import java.util.List;

import com.nullpointerworks.javadoc.old.parse.java.AbstractSourceParser;
import com.nullpointerworks.javadoc.old.parse.java.JavaSyntax;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Annotation;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.CodeBuilder;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.ItemType;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Modifier;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Visibility;
import com.nullpointerworks.util.FileUtil;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

/**
 * TODO
 * - commentary
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
		builder.inferTypeInfo();
		
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
	}
	
	/*
	 * 
	 */
	private void isMethodInfo(CodeBuilder builder, Element root) 
	{
		/*
		 * interface methods are always public when visibility is undefined
		 * non-default methods (i.e. without a body) are always abstract
		 */
		builder.setVisibility( Visibility.PUBLIC );
		if (!builder.hasModifier(Modifier.DEFAULT))
		{
			builder.setModifier(Modifier.ABSTRACT);
		}
		
		/*
		 * make xml element
		 */
		Element elMethod = new Element("method");
		
		/*
		 * annotations
		 */
		List<Annotation> anns = builder.getAnnotation();
		if (anns.size()>0)
		{
			for (Annotation ann: anns)
			{
				String m = ann.getString();
				elMethod.addChild( new Element("annotation").setText(m) );
			}
		}
		
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
	
	/*
	 * 
	 */
	private void isFieldInfo(CodeBuilder builder, Element root) 
	{
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
	
	// ============================================================================================
	
	/*
	 * code builder
	 */
	CodeBuilder parambuilder = new CodeBuilder();
	CodeBuilder builder = new CodeBuilder();
	boolean isThrowing = false;
	boolean hasCommentBranch = false;
	boolean hasTemplateBranch = false;
	boolean hasThrowingBranch = false;
	boolean hasParameterBranch = false;
	boolean hasAnnotationBranch = false;

	private void resetBuilder(CodeBuilder builder) 
	{
		builder.reset();
		isThrowing = false;
		hasCommentBranch = false;
		hasTemplateBranch = false;
		hasThrowingBranch = false;
		hasParameterBranch = false;
		hasAnnotationBranch = false;
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
			resetBuilder(builder);
			return;
		}
		if (isEndOfSource(token))
		{
			writeToFile(doc, file);
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
			//doCommentaryBranch(cbuilder, token);
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
		 * auto-complete will notify it's value. 
		 * interface variables are always "public static final"
		 */
		if (equals(token,"="))
		{
			builder.setVisibility(Visibility.PUBLIC);
			builder.setModifier(Modifier.STATIC);
			builder.setModifier(Modifier.FINAL);
			builder.setItemType(ItemType.FIELD);
			return;
		}
		
		/*
		 * something visible has been detected
		 */
		boolean isVisibility = JavaSyntax.isVisibility(token);
		if (isVisibility)
		{
			builder.setVisibility(token);
			return;
		}
		
		/*
		 * if token is a modifier
		 */
		boolean isModifier = JavaSyntax.isModifier(token);
		if (isModifier)
		{
			builder.setModifier(token);
			return;
		}
		
		/*
		 * detect method/annotation signifier
		 * variables don't have ()
		 * interfaces don't have constructors
		 */
		if (equals(token,"("))
		{
			//builder.setItemType(ItemType.METHOD);
			builder.setParameterCapable(true);
			hasParameterBranch = true;
			return;
		}
		if (equals(token,"throws"))
		{
			hasThrowingBranch = true;
			return;
		}

		/*
		 * check for annotations
		 */
		if (token.startsWith("@"))
		{
			builder.setAnnotation(token);
			hasAnnotationBranch = true;
			return;
		}
		
		/*
		 * do branching
		 */
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
		 * check for array or miscellaneous markers
		 */
		if (equals(token,"["))
		{
			builder.setArray(true);
			return;
		}
		if (equals(token,"]")) return;
		if (equals(token,"}")) return;
		
		builder.setUnidentified(token);
	}
	
	// ============================================================================================
	
	/*
	 * parse exception throwing tokens
	 */
	private void doThrowingBranch(CodeBuilder builder, String token) 
	{
		/*
		 * if a marker, skip
		 */
		if (equals(token,",")) return;
		
		builder.setThrowing(token);
	}
	
	/*
	 * build parameters for methods and constructors
	 */
	private void doParameterBranch(CodeBuilder builder, String token) 
	{
		/*
		 * 
		 */
		if (hasAnnotationBranch)
		{
			if (equals(token,",")) return;
			
			if (equals(token,")"))
			{
				if (parambuilder.getUnidentified().size()>0) 
				{
					builder.setAnnotationParameters(parambuilder.getUnidentified());
					parambuilder = new CodeBuilder();
				}
				
				hasAnnotationBranch = false;
				hasParameterBranch = false;
				return;
			}
			
			parambuilder.setUnidentified(token);
			return;
		}
		
		/*
		 * check template
		 */
		if (equals(token,"<"))
		{
			hasTemplateBranch = true;
			return;
		}
		if (equals(token,">"))
		{
			hasTemplateBranch = false;
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
		
		parambuilder.setItemType(ItemType.PARAMETER);
		parambuilder.setUnidentified(token);
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
