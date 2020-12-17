package com.nullpointerworks.tokenscanner2;

import com.nullpointerworks.tokenscanner2.parsers.AnnotationParser;
import com.nullpointerworks.tokenscanner2.parsers.ClassParser;
import com.nullpointerworks.tokenscanner2.parsers.EmptyParser;
import com.nullpointerworks.tokenscanner2.parsers.EnumParser;
import com.nullpointerworks.tokenscanner2.parsers.ISourceParser;
import com.nullpointerworks.tokenscanner2.parsers.InterfaceParser;
import com.nullpointerworks.tokenscanner2.parsers.ModuleParser;

import exp.nullpointerworks.xml.Document;

public final class ParserFactory 
{
	/**
	 * 
	 */
	public static ISourceParser getEmptyParser() 
	{
		return new EmptyParser();
	}
	
	/**
	 * 
	 */
	public static ISourceParser getInterfaceParser(Document doc, String file) 
	{
		return new InterfaceParser(doc, file);
	}
	
	/**
	 * 
	 */
	public static ISourceParser getClassParser(Document doc, String file) 
	{
		return new ClassParser(doc, file);
	}
	
	/**
	 * 
	 */
	public static ISourceParser getEnumParser(Document doc, String file) 
	{
		return new EnumParser(doc, file);
	}
	
	/**
	 * 
	 */
	public static ISourceParser getAnnotationParser(Document doc, String file) 
	{
		return new AnnotationParser(doc, file);
	}
	
	/**
	 * 
	 */
	public static ISourceParser getModuleParser(Document doc, String file) 
	{
		return new ModuleParser(doc, file);
	}
	
}
