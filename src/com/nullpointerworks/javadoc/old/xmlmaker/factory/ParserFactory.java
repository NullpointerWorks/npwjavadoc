package com.nullpointerworks.javadoc.old.xmlmaker.factory;

import com.nullpointerworks.javadoc.old.parse.java.ISourceParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.SourceType;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.AnnotationParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.ClassParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.InterfaceParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.ModuleParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.NullParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.enums.EnumParser;

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
