package com.nullpointerworks.tokenscanner;

import com.nullpointerworks.tokenscanner.builder.SourceType;
import com.nullpointerworks.tokenscanner.parsers.AnnotationParser;
import com.nullpointerworks.tokenscanner.parsers.ClassParser;
import com.nullpointerworks.tokenscanner.parsers.EmptyParser;
import com.nullpointerworks.tokenscanner.parsers.EnumParser;
import com.nullpointerworks.tokenscanner.parsers.InterfaceParser;
import com.nullpointerworks.tokenscanner.parsers.ModuleParser;

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
			return new EmptyParser();
			
		}
	}
	
}
