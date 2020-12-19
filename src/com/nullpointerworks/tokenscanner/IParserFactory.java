package com.nullpointerworks.tokenscanner;

import com.nullpointerworks.tokenscanner.builder.SourceType;

import exp.nullpointerworks.xml.Document;

public interface IParserFactory
{
	public ISourceParser getSourceParser(SourceType st, Document doc, String file);
}
