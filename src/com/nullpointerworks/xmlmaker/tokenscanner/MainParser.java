package com.nullpointerworks.xmlmaker.tokenscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.parse.java.ISourceParser;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;
import com.nullpointerworks.xmlmaker.tokenscanner.parsers.PrimarySourceParser;

public class MainParser 
{
	private static final String GIT_CORE = "D:\\Development\\Java\\workspaces\\git\\libcore\\src\\";
	public static void main(String[] args) {new MainParser(args);}
	
	public MainParser(String[] args) 
	{

		/*
		args = new String[] 
		{
			//"src/com/nullpointerworks/examples/ExampleInterface.java"
			"src/com/nullpointerworks/examples/ExampleClass.java"
			//"src/com/nullpointerworks/examples/ExampleEnum.java"
			//,"src/com/nullpointerworks/examples/ExampleEnum2.java"
			//,"src/com/nullpointerworks/examples/ExampleEnum3.java"
		};
		//*/
		
		List<String> list = new ArrayList<String>();
		parseDirectory(GIT_CORE, list);
		parseFiles(list);
		
	}
	
	private void parseDirectory(String args, List<String> list) 
	{
		File f = new File(args);
		if (!f.exists()) return;
		if (!f.isDirectory()) return;
		
		parseDirectory(f, list);
		
		for (String file : list)
		{
			System.out.println(file);
		}
	}
	
	private void parseDirectory(File dir, List<String> list) 
	{
		File[] files = dir.listFiles();
		for (int i=0,l=files.length; i<l; i++)
		{
			File file = files[i];
			if (file.isDirectory()) 
			{
				parseDirectory(file, list);
			}
			else
			{
				list.add( file.getAbsolutePath() );
			}
		}
	}
	
	private void parseFiles(List<String> files) 
	{
		for (String f : files)
		{
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
				continue;
			}
			if (tf==null) continue;
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
