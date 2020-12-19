package com.nullpointerworks.tokenscanner.factory;

import com.nullpointerworks.tokenscanner.builder.SourceType;
import com.nullpointerworks.tokenscanner.parsers.ISourceParser;

import exp.nullpointerworks.xml.Document;

public interface IParserFactory
{
	public ISourceParser getSourceParser(SourceType st, Document doc, String file);
}
