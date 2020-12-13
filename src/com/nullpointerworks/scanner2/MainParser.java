package com.nullpointerworks.scanner2;

import java.io.FileNotFoundException;

import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainParser 
{
	public static void main(String[] args) 
	{
		args = new String[] 
		{
			"src/com/nullpointerworks/examples/ExampleInterface.java"
		};
		new MainParser(args);
	}
	
	private ISourceParser parser;
	
	public MainParser(String[] args) 
	{
		for (String f : args)
		{
			/*
			 * prepare
			 */
			parser = new SourceParser("ExampleInterface");
			
			/*
			 * read text file
			 */
			TextFile tf = null;
			try 
			{
				tf = TextFileParser.file(f);
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			if (tf==null) return;
			String[] lines = tf.getLines();
			
			/*
			 * parse text
			 */
			for (int i=0,l=lines.length; i<l; i++)
			{
				String line = lines[i];
				parser.nextLine(line);
			}
		}
	}
}
