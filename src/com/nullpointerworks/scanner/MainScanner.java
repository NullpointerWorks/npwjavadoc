package com.nullpointerworks.scanner;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.Text;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

public class MainScanner 
{
	static final String JAVA_GIT = "D:/Development/Java/workspaces/git/";
	public static void main(String[] args) 
	{
		//args = new String[] {JAVA_GIT+"libcore/src/module-info.java"};
		//args = new String[] {JAVA_GIT+"libcore/src/com/nullpointerworks/core/DrawCanvas.java"};
		args = new String[] {"src/com/nullpointerworks/scanner/ScanTestInterface.java", 
							 "src/com/nullpointerworks/scanner/ScanTestClass.java", 
							 "src/com/nullpointerworks/scanner/ScanTestEnum.java"};
		
		new MainScanner(args);
	}
	
	boolean isCommentary;
	boolean isModule;
	boolean isInterface;
	boolean isClass;
	boolean isEnum;
	boolean isField;
	boolean isMethod;
	Document doc;
	Element root;
	
	public MainScanner(String[] args) 
	{
		for (String file : args)
		{
			doc = new Document();
			root = new Element("source");
			doc.setRootElement(root);
			
			isCommentary = false;
			isModule = false;
			isInterface = false;
			isClass = false;
			isEnum = false;
			isField = false;
			isMethod = false;
			
			scanFile(file);
		}
	}
	
	public void scanFile(String args)
	{
		Element construct = null;
		
		/*
		 * read text file
		 */
		TextFile tf = null;
		try 
		{
			tf = TextFileParser.file(args);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		if (tf==null) return;
		String[] lines = tf.getLines();
		
		/*
		 * store filename
		 */
		int lastindex = args.lastIndexOf("/");
		String filename = args.substring(lastindex+1);
		root.addChild(new Element("name").setText(filename));
		
		/*
		 * loop through source code
		 */
		for (int i=0,l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			
			/*
			 * check for package
			 */
			if (line.startsWith("package "))
			{
				String pack = line.substring( 8, line.length()-1 );
				root.addChild(new Element("package").setText(pack));
			}
			
			/*
			 * commentary block
			 */
			if (line.startsWith("/**"))
			{
				isCommentary=true;
				construct = new Element("commentary");
				root.addChild(construct);
			}
			
			/*
			 * module block
			 */
			if (line.startsWith("module "))
			{
				isModule = true;
				construct = new Element("module");
				construct.addChild(new Element("name").setText(line.substring(7)));
				root.addChild(construct);
			}
			
			if (line.startsWith("public "))
			{
				String pub = line.substring(7);
				/*
				 * class block
				 */
				if (pub.contains("class "))
				{
					isClass = true;
					construct = new Element("class");
					int lastIndex = pub.lastIndexOf(" ")+1;
					construct.addChild(new Element("name").setText(pub.substring(lastIndex)));
					
					int firstIndex = pub.indexOf(" ");
					construct.addChild(new Element("modifiers").setText(pub.substring(0,firstIndex)));
					
					root.addChild(construct);
				}
				
				/*
				 * interface block
				 */
				if (pub.contains("interface "))
				{
					isInterface = true;
					construct = new Element("interface");
					int lastIndex = pub.lastIndexOf(" ")+1;
					construct.addChild(new Element("name").setText(pub.substring(lastIndex)));
					root.addChild(construct);
				}
				
				/*
				 * enum block
				 */
				if (pub.contains("enum "))
				{
					isEnum = true;
					construct = new Element("enum");
					int lastIndex = line.lastIndexOf(" ")+1;
					construct.addChild(new Element("name").setText(line.substring(lastIndex)));
					root.addChild(construct);
				}
				
			}
			
			if (isCommentary)
			{
				if (line.endsWith("*/"))
				{
					construct = null;
					isCommentary = false;
					continue;
				}
				checkCommentLine(construct, line);
			}
			if (isModule)
			{
				if (line.endsWith("}"))
				{
					construct = null;
					isModule = false;
				}
				
				if (line.startsWith("requires "))
				{
					Element req = new Element("requires");
					String name = line.substring(9);
					int index = name.indexOf(";");
					req.setText(name.substring(0,index));
					construct.addChild(req);
				}
				else
				if (line.startsWith("exports "))
				{
					Element exp = new Element("exports");
					String name = line.substring(8);
					int index = name.indexOf(";");
					exp.setText(name.substring(0,index));
					construct.addChild(exp);
				}
			}
			if (isInterface)
			{
				i = scanCodeBlock(construct, i, lines);
				isInterface = false;
			}
			if (isClass)
			{
				i = scanCodeBlock(construct, i, lines);
				isClass = false;
			}
			if (isEnum)
			{
				i = scanEnum(construct, i, lines);
				isEnum = false;
			}
			
		} // end line loop
		
		/*
		 * write to XML file
		 */
		try 
		{
			DocumentIO.write(doc, filename+".xml", FormatBuilder.getPrettyFormat());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private int scanEnum(Element root, int i, String[] lines) 
	{
		Element construct = null;
		for (int l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			if (line.startsWith("//")) continue;
			
			/*
			 * commentary block
			 */
			if (line.startsWith("/**"))
			{
				isCommentary = true;
				construct = new Element("commentary");
				root.addChild(construct);
			}
			if (isCommentary)
			{
				if (line.endsWith("*/"))
				{
					construct = null;
					isCommentary = false;
					continue;
				}
				checkCommentLine(construct, line);
			}
			
			
			
			
		}
		return i;
	}

	private int scanCodeBlock(Element root, int i, String[] lines) 
	{
		Element construct = null;
		int braceTracker = 1; // account for first opening brace
		for (int l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			
			if (line.startsWith("//")) continue;
			if (line.contains("{")) braceTracker++;
			if (line.contains("}")) braceTracker--;
			if (braceTracker<1) break;
			
			/*
			 * commentary block
			 */
			if (line.startsWith("/**"))
			{
				isCommentary=true;
				construct = new Element("commentary");
				root.addChild(construct);
			}
			if (isCommentary)
			{
				if (line.endsWith("*/"))
				{
					construct = null;
					isCommentary = false;
					continue;
				}
				checkCommentLine(construct, line);
			}
			
			/*
			 * method scan
			 */
			if (line.contains("(") && line.contains(")"))
			{
				isMethod = true;
				construct = new Element("method");
				String[] tokens = line.split("\\(");
				
				/*
				 * check modifiers
				 */
				String modifiers = tokens[0];
				// find method name
				int lastindex = modifiers.lastIndexOf(" ");
				String name = modifiers.substring(lastindex+1);
				construct.addChild(new Element("name").setText(name));
				
				// find method return type
				modifiers = modifiers.substring(0,lastindex).trim();
				lastindex = modifiers.lastIndexOf(" ");
				String ret = modifiers.substring(lastindex+1);
				construct.addChild(new Element("returns").setText(ret));
				
				// find modifiers
				if (lastindex > 0)
				{
					modifiers = modifiers.substring(0,lastindex);
					construct.addChild(new Element("modifiers").setText(modifiers));
				}
				
				/*
				 * check parameters
				 */
				lastindex = tokens[1].indexOf(")");
				String parameters = tokens[1].substring(0,lastindex);
				if (parameters.length()>0)
				{
					String[] params = parameters.split(",");
					for (String p : params)
					{
						construct.addChild(new Element("param").setText(p));
					}
				}
				root.addChild(construct);
			}
			
		}
		return i;
	}
	
	private void checkCommentLine(Element construct, String line) 
	{
		if (line.startsWith("/**"))
			line = line.substring(3);
		
		if (line.startsWith("* "))
			line = line.substring(2);
		else
		if (line.startsWith("*"))
			line = line.substring(1);
		
		if (line.contains("<br>"))
			line = line.replace("<br>", "");
		
		if (line.startsWith("@version"))
		{
			Element version = new Element("version");
			version.setText(line.substring(8).trim());
			construct.addChild(version);
		}
		else
		if (line.startsWith("@author"))
		{
			Element author = new Element("author");
			author.setText(line.substring(7).trim());
			construct.addChild(author);
		}
		else
		if (line.startsWith("@since"))
		{
			Element author = new Element("since");
			author.setText(line.substring(6).trim());
			construct.addChild(author);
		}
		else
		if (line.startsWith("@return"))
		{
			Element author = new Element("return");
			author.setText(line.substring(7).trim());
			construct.addChild(author);
		}
		else
		if (line.startsWith("@param"))
		{
			Element author = new Element("param");
			author.setText(line.substring(6).trim());
			construct.addChild(author);
		}
		else
		if (line.startsWith("@see"))
		{
			Element author = new Element("see");
			author.setText(line.substring(4).trim());
			construct.addChild(author);
		}
		else
		{
			if (line.length()>0)
				construct.addChild(new Text(line));
		}
	}
}

