package com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers;

import java.util.List;

import com.nullpointerworks.javadoc.old.parse.java.AbstractSourceParser;
import com.nullpointerworks.javadoc.old.parse.java.ISourceParser;
import com.nullpointerworks.javadoc.old.parse.java.JavaSyntax;
import com.nullpointerworks.javadoc.old.xmlmaker.factory.IParserFactory;
import com.nullpointerworks.javadoc.old.xmlmaker.factory.ParserFactory;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Annotation;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.CodeBuilder;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Modifier;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Package;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.SourceType;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.Visibility;
import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

public class PrimarySourceParser extends AbstractSourceParser
{
	/*
	 * document building
	 */
	private Document doc;
	private Element root;
	private final String file;
	private IParserFactory parserFactory = null;
	private ISourceParser parser = null;
	private SourceType sourceType = SourceType.NULL;
	
	public PrimarySourceParser(String file) 
	{
		super();
		this.file = file;
		this.doc = new Document();
		this.root = doc.getRootElement("source");
		parserFactory = new ParserFactory();
	}
	
	private boolean equals(String s, String c)
	{
		return s.equalsIgnoreCase(c);
	}
	
	// ============================================================================================
	
	private void parseBuilder(CodeBuilder builder) 
	{
		/*
		 * if parsing package info
		 */
		if (builder.getPackage() == Package.PACKAGE)
		{
			isPackage(builder, root);			
			return;
		}
		
		/*
		 * if import package info
		 */
		if (builder.getPackage() == Package.IMPORT)
		{
			isImport(builder, root);			
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
	
	/*
	 * 
	 */
	private void isImport(CodeBuilder builder, Element root) 
	{
		Element elPack = new Element("import");
		
		List<String> uid = builder.getUnidentified();
		if (uid.size()==1)
		{
			/*
			 * for import info, only one token should be present and should be the name
			 */
			String n = uid.get(0);
			elPack.setText(n);
		}
		else
		{
			Log.err("Error: Import information incomplete");
		}
		
		root.addChild(elPack);
	}

	/*
	 * 
	 */
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
			elPack.setText(n);
		}
		else
		{
			Log.err("Error: Package information incomplete");
		}
		
		root.addChild(elPack);
	}
	
	/*
	 * 
	 */
	private void isSourceInfo(CodeBuilder builder, Element root) 
	{
		Element elementType = new Element("source");

		/*
		 * annotations
		 */
		var anno = builder.getAnnotation();
		if (anno.size()>0)
		{
			for (Annotation ann : anno)
			{
				String m = ann.getString();
				elementType.addChild( new Element("annotation").setText(m) );
			}
		}
		
		/*
		 * visibility
		 */
		if (builder.getVisibility() != Visibility.NULL)
		{
			String v = builder.getVisibility().toString();
			elementType.addChild( new Element("visibility").setText(v) );
		}
		
		/*
		 * source type
		 * keep track of the file source type.
		 */
		String st = builder.getSourceType().toString();
		elementType.addChild( new Element("sourcetype").setText(st) );
		
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
	
	// ============================================================================================
	
	/*
	 * comment block signifier
	 */
	private final String C_BLOCK_START = "/**";
	private final String C_START = "/*";
	private final String C_END = "*/";
	
	/*
	 * code builder
	 */
	private CodeBuilder builder = new CodeBuilder();
	private boolean hasCommentBranch = false;
	private boolean hasTemplateBranch = false;
	private boolean isImplementing = false;
	private boolean isExtending = false;

	private void resetBuilder() 
	{
		builder.reset();
		hasCommentBranch = false;
		hasTemplateBranch = false;
		isImplementing = false;
		isExtending = false;
	}
		
	@Override
	public void nextToken(String token) 
	{
		/*
		 * redirect token to parser if available
		 */
		if (parser != null)
		{
			parser.nextToken(token);
			return;
		}
		
		/*
		 * choose a branch
		 */
		if (hasTemplateBranch)
		{
			doTemplateBranch(builder, token);
			return;
		}
		if (isImplementing)
		{
			if (doImplementingBranch(builder, token)) return;
		}
		if (isExtending)
		{
			if (doExtendingBranch(builder, token)) return;
		}
		
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
			if (equals(token,C_END))
			{
				resetBuilder();
				return;
			}
		}
		
		if (equals(token,";"))
		{
			parseBuilder(builder);
			resetBuilder();
			return;
		}
		
		if (equals(token,"package"))
		{
			builder.setPackage(Package.PACKAGE);
			return;
		}
		
		if (equals(token,"import"))
		{
			builder.setPackage(Package.IMPORT);
			return;
		}
		
		if (JavaSyntax.isVisibility(token))
		{
			builder.setVisibility(token);
			return;
		}
		
		if (JavaSyntax.isModifier(token))
		{
			builder.setModifier(token);
			return;
		}
		
		if (JavaSyntax.isSourceType(token))
		{
			builder.setSourceType(token);
			sourceType = builder.getSourceType();
			return;
		}
		
		/*
		 * detect annotations. make sure this is done after commentary scan. 
		 * comments can have annotations that are not applicable to source code
		 */
		if (token.startsWith("@"))
		{
			builder.setAnnotation(token);
			return;
		}
		
		if (equals(token,"<"))
		{
			hasTemplateBranch = true;
			return;
		}
		
		if (equals(token,"extends"))
		{
			isExtending = true;
			return;
		}
		
		if (equals(token,"implements"))
		{
			isImplementing = true;
			return;
		}
		
		/*
		 * found first code block. 
		 * this marks the end of the prolog.
		 * determine and set parser template 
		 */
		if (equals(token,"{"))
		{
			parseBuilder(builder);
			resetBuilder();
			parser = parserFactory.getSourceParser( sourceType, doc, file );
			return;
		}
		
		/*
		 * add an unidentified token. 
		 * these are usually names for;
		 * - method
		 * - field
		 * - import
		 * - package
		 * - data types
		 * - template types
		 */
		builder.setUnidentified(token);
	}
	
	/*
	 * 
	 */
	private boolean skipTemplate = false;
	private boolean doImplementingBranch(CodeBuilder builder, String token) 
	{
		if (equals(token,"{")) 
		{
			isImplementing = false;
			return false;
		}
		if (equals(token,",")) return true;
		
		/*
		 * check template
		 */
		if (equals(token,"<"))
		{
			skipTemplate = true;
			return true;
		}
		if (equals(token,">"))
		{
			skipTemplate = false;
			return true;
		}
		if (skipTemplate) return true;
		
		builder.setImplemented(token);
		return true;
	}
	
	/*
	 * 
	 */
	private boolean doExtendingBranch(CodeBuilder builder, String token) 
	{
		if (equals(token,"{"))
		{
			isExtending = false;
			return false;
		}
		if (equals(token,",")) return true;
		if (equals(token,"implements"))
		{
			isExtending = false;
			isImplementing = true;
			return true;
		}
		builder.setExtended(token);
		return true;
	}

	/*
	 * 
	 */
	private void doTemplateBranch(CodeBuilder builder2, String token) 
	{
		/*
		 * if a separator, skip
		 */
		if (equals(token,",")) return;
		
		/*
		 * end template
		 */
		if (equals(token,">"))
		{
			hasTemplateBranch = false;
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
	}
}
