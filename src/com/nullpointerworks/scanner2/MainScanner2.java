package com.nullpointerworks.scanner2;

import java.io.FileNotFoundException;

import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainScanner2 
{

	public static void main(String[] args) 
	{
		args = new String[] {"src/com/nullpointerworks/scanner/ScanTestInterface.java"};
		new MainScanner2(args);
	}
	
	private SourceParser parser;
	
	public MainScanner2(String[] args) 
	{
		for (String f : args)
		{
			Log.out("file: "+f);
			
			/*
			 * prepare
			 */
			parser = new SourceParser();
			
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
				String line = lines[i].trim();
				parser.characterStream(line);
			}
		}
	}
}
