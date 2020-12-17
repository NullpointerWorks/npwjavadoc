package com.nullpointerworks.tokenscanner;

import com.nullpointerworks.tokenscanner.parsers.AnnotationParser;
import com.nullpointerworks.tokenscanner.parsers.ClassParser;
import com.nullpointerworks.tokenscanner.parsers.EmptyParser;
import com.nullpointerworks.tokenscanner.parsers.EnumParser;
import com.nullpointerworks.tokenscanner.parsers.ISourceParser;
import com.nullpointerworks.tokenscanner.parsers.InterfaceParser;
import com.nullpointerworks.tokenscanner.parsers.ModuleParser;

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
