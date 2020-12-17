package com.nullpointerworks.tokenscanner;

import java.io.FileNotFoundException;

import com.nullpointerworks.tokenscanner.parsers.ISourceParser;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainParser2 
{
	public static void main(String[] args) 
	{
		args = new String[] 
		{
			//"src/com/nullpointerworks/examples/ExampleInterface.java"
			//,"src/com/nullpointerworks/examples/ExampleClass.java"
			"src/com/nullpointerworks/examples/ExampleEnum.java"
		};
		new MainParser2(args);
	}
	
	public MainParser2(String[] args) 
	{
		for (String f : args)
		{
			/*
			 * prepare
			 */
			String n = FileUtil.getFileNameFromPath(f);
			ISourceParser parser = new SourceParser2(n);
			
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
