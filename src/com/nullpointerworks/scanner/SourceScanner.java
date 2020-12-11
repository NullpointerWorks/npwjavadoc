package com.nullpointerworks.scanner;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.Text;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

public class SourceScanner 
{
	private boolean isCommentary;
	private boolean isModule;
	private boolean isInterface;
	private boolean isClass;
	private boolean isEnum;
	private Document doc;
	private Element root;
	
	public SourceScanner(String[][] pack)
	{
		this.PACK = pack;
	}
	
	/*
	 * scans a file for commentary and determines if its a class, interface or enum
	 */
	public String scanFile(String args)
	{
		/*
		 * prepare
		 */
		doc = new Document();
		root = new Element("source");
		doc.setRootElement(root);
		isCommentary = false;
		isModule = false;
		isInterface = false;
		isClass = false;
		isEnum = false;
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
		if (tf==null) return null;
		String[] lines = tf.getLines();
		
		/*
		 * store filename
		 */
		int lastindex = args.lastIndexOf("/");
		String filename = args.substring(lastindex+1, args.length()-5);
		//root.addChild(new Element("name").setText(filename));
		
		/*
		 * loop through source code
		 */
		for (int i=0,l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			
			/*
			 * check for package and source module
			 */
			if (line.startsWith("package "))
			{
				String pack = line.substring( 8, line.length()-1 );
				String[] info = getPackageInfo(pack);
				root.addChild(new Element("module").setText(info[2]));
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
				String moduleName = line.substring(7);
				construct.addChild(new Element("name").setText(moduleName));
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
					construct = new Element("code");
					construct.addChild( new Element("type").setText("class"));
					
					/*
					 * implements first
					 */
					String[] implementation = pub.split("implements");
					if (implementation.length > 1)
					{
						String imp = implementation[1].trim();
						String[] list = imp.split(",");
						for (String im : list)
						{
							Element el = new Element("implements");
							el.setText(im.trim());
							construct.addChild(el);
						}
					}
					pub = implementation[0].trim();
					
					/*
					 * then extends
					 */
					String[] extension = pub.split("extends");
					if (extension.length > 1)
					{
						String ext = extension[1].trim();
						String[] list = ext.split(",");
						for (String extd : list)
						{
							Element el = new Element("extends");
							el.setText(extd.trim());
							construct.addChild(el);
						}
					}
					pub = extension[0].trim();
					
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
					construct = new Element("code");
					construct.addChild( new Element("type").setText("interface"));
					
					/*
					 * interfaces cannot implement
					 */
					String[] extension = pub.split("extends");
					if (extension.length > 1)
					{
						String ext = extension[1].trim();
						String[] list = ext.split(",");
						for (String extd : list)
						{
							Element el = new Element("extends");
							el.setText(extd.trim());
							construct.addChild(el);
						}
					}
					pub = extension[0].trim();
					
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
					construct = new Element("code");
					construct.addChild( new Element("type").setText("enum"));
					// enums cannot extend nor implement
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
					String export = name.substring(0,index);
					exp.setText(export);
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
		String name = FileUtil.getFileNameFromPath(filename);
		String path = "xml/" + name + ".xml";
		try 
		{
			DocumentIO.write(doc, path, FormatBuilder.getPrettyFormat());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return path;
	}
	
	/*
	 * package preset
	 */
	private final String[][] PACK;
	private String[] getPackageInfo(String packName)
	{
		for (String[] info : PACK)
		{
			if (packName.equalsIgnoreCase(info[0]))
			{
				return info;
			}
		}
		return new String[] {"","","",""};
	}
	
	private boolean isModifier(String token)
	{
		if (token.equals("public")) return true;
		if (token.equals("protected")) return true;
		return false;
	}
	
	private boolean validLettering(String line)
	{
		return line.matches("^[a-zA-Z_0-9]+$");
	}

	/*
	 * used to check annotations inside the comment block
	 */
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
			Element param = new Element("param");
			String paramline = line.substring(6).trim();
			String paramname = paramline.split(" ")[0];
			paramline = paramline.substring(paramname.length());
			//param.setText(paramline);
			param.addChild(new Element("name").setText(paramname));
			param.addChild(new Element("text").setText(paramline));
			
			construct.addChild(param);
		}
		else
		if (line.startsWith("@see"))
		{
			Element author = new Element("see");
			author.setText(line.substring(4).trim());
			construct.addChild(author);
		}
		else
		if (line.startsWith("@override"))
		{
			Element over = new Element("override");
			over.setText(line.substring(9).trim());
			construct.addChild(over);
		}
		else
		{
			if (line.length()>0)
			{
				if (construct.getText()!=null)
				{
					String text = construct.getText();
					if (!text.endsWith(" ")) text = text+" ";
					construct.setText(text+line);
				}
				else
					construct.addChild(new Text(line));
			}
				
		}
	}

	/*
	 * creates a parameter element
	 */
	private Element createParam(Element comm, String p) 
	{
		String[] tokens = p.split(" ");
		String type = tokens[0];
		String name = tokens[1];
		
		var el = new Element("param");
		el.addChild( new Element("type").setText(type) );
		el.addChild( new Element("name").setText(name) );
		
		/* 
		 * get commentary about the parameter
		 */
		if (comm!=null)
		{
			var list = comm.getChildren();
			for (Element param : list)
			{
				var pname = param.getChild("name");
				if (pname!=null)
				if (pname.getText().equalsIgnoreCase(name))
				{
					el.addChild( new Element("comment").setText(param.getChild("text").getText()) );
					break;
				}
			}
		}
		
		return el;
	}
	
	/*
	 * TODO: enum can have methods and fields. need to be implemented
	 * used to scan enum files
	 */
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
			else
			if (line.length()>0)
			{
				String[] enums = line.split(",");
				for (String e : enums)
				{
					if (!validLettering(e)) continue;
					Element en = new Element("value");
					en.setText(e);
					root.addChild(en);
				}
			}
		}
		return i;
	}
	
	/*
	 * used to scan classes and interfaces
	 */
	private int scanCodeBlock(Element root, int i, String[] lines) 
	{
		Element prev_comment = null;
		Element construct = null;
		int braceTracker = 0; // account for first opening brace
		for (int l=lines.length; i<l; i++)
		{
			String line = lines[i].trim();
			
			if (line.startsWith("//")) continue;
			if (line.contains("{")) braceTracker++;
			if (line.contains("}")) braceTracker--;
			if (braceTracker<0) break;
			
			/*
			 * commentary block
			 */
			if (line.startsWith("/**"))
			{
				isCommentary=true;
				construct = new Element("commentary");
			}
			if (isCommentary)
			{
				if (line.endsWith("*/"))
				{
					//root.addChild(construct);
					prev_comment = construct;
					construct = null;
					isCommentary = false;
					continue;
				}
				checkCommentLine(construct, line);
			}
			
			/*
			 * method scan
			 */
			if (line.contains("(") && line.contains(")") && braceTracker==1)
			{
				scanMethodLine(line, root, construct, prev_comment);
				prev_comment=null;
			}
			else
			/*
			 * field scan
			 */
			if (line.endsWith(";") && braceTracker==1)
			{
				scanFieldLine(line, root, construct, prev_comment);
				prev_comment=null;
			}
			
		}
		return i;
	}
	
	/**
	 * This method takes a line of code that indicates the start of a method. 
	 */
	private void scanMethodLine(String line, Element root, Element construct, Element commentary)
	{
		if (line.startsWith("private")) return;
		if (line.startsWith("@")) return;
		construct = new Element("method");
		
		/*
		 * add commentary
		 */
		if (commentary!=null)
		{
			// if commentary has text
			if (commentary.getText()!=null)
				construct.addChild( new Element("commentary").setText(commentary.getText()) );
		}
		
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
		
		/*
		 * is the return type
		 */
		if ( !isModifier(ret) )
		{
			Element returns = new Element("returns").addChild( new Element("type").setText(ret) );
			
			/*
			 * if the method has a non-void return type, get the commentary
			 */
			if (commentary!=null)
			{
				Element returns_el = commentary.getChild("return");
				if (returns_el != null)
				{
					String returns_comment = commentary.getChild("return").getText();
					returns.addChild( new Element("comment").setText(returns_comment) );
				}
			}
			construct.addChild(returns);
		}
		/*
		 * else, constructor
		 */
		else
		{
			construct.setName("constructor");
			construct.addChild(new Element("modifiers").setText(ret));
		}
		
		/*
		 * find modifiers
		 */
		if (lastindex > 0)
		{
			modifiers = modifiers.substring(0,lastindex);
			modifiers = modifiers.replace("public", "").trim();
			if (modifiers.length()>0)
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
				construct.addChild(createParam(commentary, p.trim()));
			}
		}
		
		/*
		 * check throws
		 */
		String throwers = tokens[1].substring(lastindex+1);
		throwers = throwers.trim();
		if (throwers.length()>7)
		{
			throwers = throwers.substring(7);
			String[] exceptions = throwers.split(",");
			for (String ex : exceptions)
			{
				construct.addChild(new Element("throws").setText(ex.trim()));
			}
		}
		
		/*
		 * add miscs.
		 */
		if (commentary!=null)
		{
			Element misc = commentary.getChild("since");
			if (misc!=null) construct.addChild( misc );
			misc = commentary.getChild("author");
			if (misc!=null) construct.addChild( misc );
			misc = commentary.getChild("version");
			if (misc!=null) construct.addChild( misc );
			misc = commentary.getChild("override");
			if (misc!=null) construct.addChild( misc );
		}
		
		root.addChild(construct);
	}
	
	/**
	 * 
	 */
	private void scanFieldLine(String line, Element root, Element construct, Element commentary) 
	{
		line = line.substring(0,line.length()-1);
		String[] fielddata = line.split("=");
		String fieldtype = fielddata[0].trim();
		
		int lastindex = fieldtype.lastIndexOf(" ");
		String varName = fieldtype.substring(lastindex+1);
		fieldtype = fieldtype.substring(0,lastindex);
		lastindex = fieldtype.lastIndexOf(" ");
		String varType = fieldtype.substring(lastindex+1);
		String varMod = fieldtype.substring(0,lastindex);
		
		if (varMod.contains("private")) return;
		
		construct = new Element("field");
		construct.addChild( new Element("name").setText(varName) );
		construct.addChild( new Element("type").setText(varType) );
		construct.addChild( new Element("modifiers").setText(varMod) );
		
		// parse data
		if (fielddata.length > 1)
		{
			String data = fielddata[1].trim();
			construct.addChild( new Element("data").setText(data) );
		}
		root.addChild(construct);
	}
	
}
