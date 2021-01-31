package com.nullpointerworks.xmlmaker.factory;

import com.nullpointerworks.parse.java.ISourceParser;
import com.nullpointerworks.xmlmaker.tokenscanner.codebuilder.SourceType;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.AnnotationParser;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.ClassParser;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.InterfaceParser;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.ModuleParser;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.NullParser;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.enums.EnumParser;

import exp.nullpointerworks.xml.Document;

/*
 * factory method
 */
public final class ParserFactory implements IParserFactory
{
	@Override
	public ISourceParser getSourceParser(SourceType sourceType, Document doc, String file) 
	{
		switch(sourceType)
		{
		case INTERFACE:
			return new InterfaceParser(doc, file);
			
		case CLASS:
			return new ClassParser(doc, file);
			
		case ENUM:
			return new EnumParser(doc, file);
			
		case ANNOTATION:
			return new AnnotationParser(doc, file);
			
		case MODULE:
			return new ModuleParser(doc, file);
			
		default:
			return new NullParser();
		}
	}
	
}
