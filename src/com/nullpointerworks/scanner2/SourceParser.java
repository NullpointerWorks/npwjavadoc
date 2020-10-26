package com.nullpointerworks.scanner2;

import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;

public class SourceParser 
{
	private StringBuilder tokenBuilder;
	private Document doc;
	private Element root;
	
	public SourceParser()
	{
		doc = new Document();
		root = new Element("source");
		doc.setRootElement(root);
		tokenBuilder = new StringBuilder();
	}

	public void characterStream(String line) 
	{
		if (line.startsWith("//")) return;
		
		int leng = line.length();
		if (leng < 1) return;
		
		nextCharacter(" "); // acts as a line separator
		for (int i=0; i<leng; i++)
		{
			String character = line.substring(i, i+1);
			nextCharacter(character);
		}
	}
	
	public void nextCharacter(String character)
	{
		if (!character.equalsIgnoreCase(" "))
		{
			tokenBuilder.append(character);
		}
		else
		{
			String token = tokenBuilder.toString();
			
			// print token for debugging
			Log.out(token);
			
			
			
			
			
			
			// reset builder
			tokenBuilder.setLength(0);
		}
	}
	
	
	
}
