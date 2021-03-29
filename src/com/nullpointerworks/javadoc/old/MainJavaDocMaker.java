package com.nullpointerworks.javadoc.old;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.javadoc.old.htmlmaker.generator.FileMaker;
import com.nullpointerworks.javadoc.old.parse.java.ISourceParser;
import com.nullpointerworks.javadoc.old.xmlmaker.tokenscanner.parsers.PrimarySourceParser;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.XMLParseException;
import exp.nullpointerworks.xml.io.DocumentIO;

public class MainJavaDocMaker 
{
	private static final String GIT_CORE = "D:\\Development\\Java\\workspaces\\git\\libcore\\src\\";
	public static void main(String[] args) {new MainJavaDocMaker(args);}
	
	public MainJavaDocMaker(String[] args) 
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
		
		File f = new File(GIT_CORE);
		if (!f.exists()) return;
		if (!f.isDirectory()) return;
		parseDirectory(f, list, true);
		
		for (String file : list)
		{
			System.out.println(file);
		}
		
		//parseFiles(list);
		
		generateHTML("xml/", "html/");
		
	}
	
	private void generateHTML(String src, String dst) 
	{
		File f = new File(src);
		if (!f.exists()) return;
		if (!f.isDirectory()) return;
		
		File[] files = f.listFiles();
		for (int i=0,l=files.length; i<l; i++)
		{
			File file = files[i];
			System.out.println( file.getAbsolutePath() );
			
			Document doc = null;
			try 
			{
				doc = DocumentIO.read( file.getAbsolutePath() );
			} 
			catch (XMLParseException | IOException ex) 
			{
				ex.printStackTrace();
				continue;
			}
			if (doc==null) continue;
			
			
			
			
			
			
			FileMaker fm = new FileMaker();
			
			
			try 
			{
				fm.save(dst+file.getName()+".html");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	private void parseDirectory(File dir, List<String> list, boolean traverseSub) 
	{
		File[] files = dir.listFiles();
		for (int i=0,l=files.length; i<l; i++)
		{
			File file = files[i];
			if (traverseSub && file.isDirectory()) 
			{
				parseDirectory(file, list, traverseSub);
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
			
			for (int i=0,l=lines.length; i<l; i++)
			{
				String line = lines[i];
				parser.nextLine(line);
			}
		}
	}
}
