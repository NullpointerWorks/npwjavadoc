package com.nullpointerworks.scanner2;

import java.io.FileNotFoundException;

import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainParser 
{
	public static void main(String[] args) 
	{
		args = new String[] 
		{
			"src/com/nullpointerworks/examples/ExampleInterface.java"
			//"src/com/nullpointerworks/examples/ExampleClass.java"
		};
		new MainParser(args);
	}
	
	public MainParser(String[] args) 
	{
		for (String f : args)
		{
			/*
			 * prepare
			 */
			String name = FileUtil.getFileNameFromPath(f);
			ISourceParser parser = new SourceParser(name);
			
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
