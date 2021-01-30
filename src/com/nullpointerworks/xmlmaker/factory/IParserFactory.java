package com.nullpointerworks.xmlmaker.factory;

import com.nullpointerworks.parse.java.ISourceParser;
import com.nullpointerworks.xmlmaker.tokenscanner.codebuilder.SourceType;

import exp.nullpointerworks.xml.Document;

public interface IParserFactory
{
	public ISourceParser getSourceParser(SourceType st, Document doc, String file);
}
