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
		args = new String[] {JAVA_GIT+"libcore/src/com/nullpointerworks/core/DrawCanvas.java"};
		
		new MainScanner(args);
	}

	boolean isCommentary;
	boolean isModule;
	boolean isInterface;
	Document doc;
	Element root;
	
	public MainScanner(String[] args) 
	{
		/*
		 * prepare xml document
		 */
		doc = new Document();
		root = new Element("source");
		doc.setRootElement(root);
		Element construct = null;
		isCommentary = false;
		isModule = false;
		isInterface = false;
		
		/*
		 * read text file
		 */
		TextFile tf = null;
		try 
		{
			tf = TextFileParser.file(args[0]);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		if (tf==null) return;
		String[] lines = tf.getLines();
		
		/*
		 * loop through source code
		 */
		for (int i=0,l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			
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
			 * module block
			 */
			if (line.startsWith("module "))
			{
				isModule = true;
				construct = new Element("module");
				construct.addChild(new Element("name").setText(line.substring(7)));
				root.addChild(construct);
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
			
			/*
			 * interface block
			 */
			if (line.startsWith("public interface "))
			{
				isInterface = true;
				construct = new Element("interface");
				construct.addChild(new Element("name").setText(line.substring(17)));
				root.addChild(construct);
			}
			if (isInterface)
			{
				i = scanInterface(construct, i, lines);
			}
			
			
			
			
			
			
		} // end line loop
		
		/*
		 * write to XML file
		 */
		try 
		{
			DocumentIO.write(doc, "test.xml", FormatBuilder.getPrettyFormat());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private int scanInterface(Element root, int i, String[] lines) 
	{
		Element construct = null;
		int braceTracker = 1; // account for first opening brace
		for (int l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			
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
			if ( hasPrimitive(line) )
			{
				
				
			}
			
		}
		return i;
	}

	private boolean hasPrimitive(String line) 
	{
		// primitive data types
		if (line.startsWith("void ")) return true;
		if (line.startsWith("char ")) return true;
		if (line.startsWith("byte ")) return true;
		if (line.startsWith("short ")) return true;
		if (line.startsWith("int ")) return true;
		if (line.startsWith("long ")) return true;
		if (line.startsWith("float ")) return true;
		if (line.startsWith("double ")) return true;
		
		// not really primitives, but still relevant
		if (line.startsWith("Void ")) return true;
		if (line.startsWith("Byte ")) return true;
		if (line.startsWith("Short ")) return true;
		if (line.startsWith("Integer ")) return true;
		if (line.startsWith("Long ")) return true;
		if (line.startsWith("Float ")) return true;
		if (line.startsWith("Double ")) return true;
		
		return false;
	}
	
	private void checkCommentLine(Element construct, String line) 
	{
		if (line.startsWith("/**"))
			line = line.substring(3);
		
		if (line.startsWith("* "))
			line = line.substring(2);
		
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
