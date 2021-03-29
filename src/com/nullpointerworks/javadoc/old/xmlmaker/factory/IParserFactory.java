package com.nullpointerworks.javadoc.old.xmlmaker.factory;

import com.nullpointerworks.javadoc.old.parse.java.ISourceParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.codebuilder.SourceType;

import exp.nullpointerworks.xml.Document;

public interface IParserFactory
{
	public ISourceParser getSourceParser(SourceType st, Document doc, String file);
}
