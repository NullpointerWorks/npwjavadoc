package com.nullpointerworks.xmlmaker.tokenscanner.parsers;

import java.io.IOException;
import java.util.List;

import com.nullpointerworks.java.AbstractSourceParser;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.Log;
import com.nullpointerworks.xmlmaker.tokenscanner.codebuilder.CodeBuilder;
import com.nullpointerworks.xmlmaker.tokenscanner.codebuilder.ItemType;
import com.nullpointerworks.xmlmaker.tokenscanner.codebuilder.Modifier;
import com.nullpointerworks.xmlmaker.tokenscanner.codebuilder.Visibility;

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
	
	private void parseBuilder(CodeBuilder builder) 
	{
		builder.inferTypeInfo();
		
		if (builder.getItemType() == ItemType.ENUM)
		{
			isEnumValue(builder, root);
			return;
		}
		
		if (builder.getItemType() == ItemType.FIELD)
		{
			return;
		}
		
		
		
		
	}
	
	private void isEnumValue(CodeBuilder builder, Element root) 
	{
		Element elEnum = new Element("enum");
		
		if (builder.getVisibility() != Visibility.NULL)
		{
			String v = builder.getVisibility().toString();
			elEnum.addChild( new Element("visibility").setText(v) );
		}
		
		List<Modifier> mods = builder.getModifier();
		if (mods.size()>0)
		{
			for (Modifier mod : mods)
			{
				String m = mod.toString();
				elEnum.addChild( new Element("modifier").setText(m) );
			}
		}
		
		List<String> uid = builder.getUnidentified();
		int leng = uid.size();
		if (leng>0)
		{
			String n = uid.get(0);
			elEnum.addChild( new Element("name").setText(n) );
		}
		
		
		
		
		
		root.addChild(elEnum);
	}
	

	// ============================================================================================
	
	/*
	 * code builder
	 */
	CodeBuilder parambuilder = new CodeBuilder();
	CodeBuilder builder = new CodeBuilder();
	boolean hasCommentBranch = false;
	boolean hasParameterBranch = false;
	
	private void resetBuilder(CodeBuilder builder) 
	{
		builder.reset();
		hasCommentBranch = false;
		hasParameterBranch = false;
	}
	
	@Override
	public void nextToken(String token) 
	{
		if (equals(token,"{")) curlyBraceCount++;
		if (equals(token,"}")) curlyBraceCount--;
		
		if (isEndOfDeclaration(token))
		{
			parseBuilder(builder);
			resetBuilder(builder);
			return;
		}
		if (isEndOfSource(token))
		{
			parseBuilder(builder);
			resetBuilder(builder);
			writeToFile(doc, file);
			return;
		}
		
		if (curlyBraceCount>1) return;
		
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
				resetBuilder(builder);
				return;
			}
		}
		
		if (isEndOfInstruction(token))
		{
			parseBuilder(builder);
			resetBuilder(builder);
			return;
		}
		
		if (equals(token,","))
		{
			builder.setItemType(ItemType.ENUM);
			builder.setVisibility(Visibility.PUBLIC);
			builder.setModifier(Modifier.STATIC);
			builder.setModifier(Modifier.FINAL);
			
			parseBuilder(builder);
			resetBuilder(builder);
			return;
		}
		
		if (equals(token,"("))
		{
			builder.setParameterCapable(true);
			hasParameterBranch = true;
			return;
		}
		if (equals(token,")"))
		{
			hasParameterBranch = false;
			return;
		}
		if (hasParameterBranch)
		{
			
			return;
		}
		
		
		
		
		
		
		
		
		
		
		if (equals(token,"}")) return;
		
		Log.out(token);
		builder.setUnidentified(token);
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
