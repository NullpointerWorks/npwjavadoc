package com.nullpointerworks.tokenscanner;

import java.io.FileNotFoundException;

import com.nullpointerworks.tokenscanner.parsers.ISourceParser;
import com.nullpointerworks.tokenscanner.parsers.PrimarySourceParser;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainParser 
{
	public static void main(String[] args) 
	{
		args = new String[] 
		{
			//"src/com/nullpointerworks/examples/ExampleInterface.java"
			//,"src/com/nullpointerworks/examples/ExampleClass.java"
			"src/com/nullpointerworks/examples/ExampleEnum.java"
			//,"src/com/nullpointerworks/examples/ExampleEnum2.java"
			//,"src/com/nullpointerworks/examples/ExampleEnum3.java"
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
			String n = FileUtil.getFileNameFromPath(f);
			ISourceParser parser = new PrimarySourceParser(n);
			
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
