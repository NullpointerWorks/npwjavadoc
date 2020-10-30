package com.nullpointerworks.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.Text;
import exp.nullpointerworks.xml.format.FormatBuilder;
import exp.nullpointerworks.xml.io.DocumentIO;

public class MainScanner 
{
	static final String JAVA_GIT = "D:/Development/Java/workspaces/git/libcore/src";
	static final String JAVA_XML = "D:/Development/Web/workspaces/git/Build/core/";
	static final String JAVA_WEB = "D:/Development/Web/workspaces/git/NullpointerWorksAPI/core/";
	
	static final String CORE = "D:/Development/Java/workspaces/git/libcore/src/com/nullpointerworks/core";
	static final String CORE_OUT = "D:/Development/Web/workspaces/git/Build/core";
	
	public static void main(String[] args) 
	{
		//args = new String[] {JAVA_GIT+"libcore/src/module-info.java"};
		//args = new String[] {JAVA_GIT+"libcore/src/com/nullpointerworks/core/Monitor.java"};
		
		/*
		args = new String[] {"src/com/nullpointerworks/scanner/ScanTestInterface.java", 
							 "src/com/nullpointerworks/scanner/ScanTestClass.java", 
							 "src/com/nullpointerworks/scanner/ScanTestEnum.java"};
		//*/
		
		
		
		args = new String[] {CORE+"/DrawCanvas.java"};
		new MainScanner(args);
		
		
		
		//new MainScanner(JAVA_GIT);
	}
	
	/*
	 * package preset
	 */
	private final String[][] PACK = 
	{
		{"com.nullpointerworks.color", "pack-color.html", "libnpw.color", "module-color.html"},
		{"com.nullpointerworks.core", "pack-core.html", "libnpw.core", "module-core.html"},
		{"com.nullpointerworks.game", "pack-game.html", "libnpw.game", "module-game.html"},
		{"com.nullpointerworks.graphics", "pack-graphics.html", "libnpw.graphics", "module-graphics.html"},
		{"exp.nullpointerworks.http", "pack-http.html", "libnpw.http", "module-http.html"},
		{"com.nullpointerworks.j2d", "pack-j2d.html", "libnpw.j2d", "module-j2d.html"},
		{"com.nullpointerworks.math", "pack-math.html", "libnpw.math", "module-math.html"},
		{"com.nullpointerworks.util", "pack-util.html", "libnpw.util", "module-util.html"},
		{"exp.nullpointerworks.xml", "pack-xml.html", "libnpw.xml", "module-xml.html"},
	};
	
	private boolean isCommentary;
	private boolean isModule;
	private boolean isInterface;
	private boolean isClass;
	private boolean isEnum;
	private Document doc;
	private Element root;
	
	/* ====================================================
	 * module source path scan
	 * ==================================================== */
	
	public MainScanner(String modulePath)
	{
		sourceScanner(modulePath);
		//webGenerator(JAVA_XML, JAVA_WEB);
	}
	
	public void sourceScanner(String modulePath)
	{
		/*
		 * generate XML
		 */
		File dir = new File(modulePath);
		File[] files = dir.listFiles();
		for (File f : files)
		{
			if (f.isFile())
			{
				String absPath = f.getAbsolutePath().replace("\\", "/");
				if (!absPath.endsWith(".java")) continue;
				//Log.out(absPath);
				scanFile(absPath);
			}
			else
			{
				sourceScanner(modulePath+"/"+f.getName());
			}
		}
	}
	
	public void webGenerator(String xmlPath, String webPath)
	{
		File dir = new File(xmlPath);
		File[] files = dir.listFiles();
		
		/*
		 * generate HTML
		 */
		for (File f : files)
		{
			if (f.isFile())
			{
				String absPath = f.getAbsolutePath().replace("\\", "/");
				Log.out(absPath);
				
				//int lastindex = absPath.lastIndexOf("/");
				//String filename = absPath.substring(lastindex+1, absPath.length()-5);
				
				FileMaker fm = new FileMaker();
				String type = "inter";
				String name = "drawcanvas";
				
				try
				{
					fm.save(webPath+type+"-"+name+".html");
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/* ====================================================
	 * individual file scan
	 * ==================================================== */
	
	public MainScanner(String[] args)
	{
		for (String f : args)
		{
			String xml = scanFile(f);
			
			
			
			
		}
	}
	
	public void makeWebFile(String file) 
	{
		
		
		
		String absPath = file.replace("\\", "/");
		Log.out(absPath);
		
		//int lastindex = absPath.lastIndexOf("/");
		//String filename = absPath.substring(lastindex+1, absPath.length()-5);
		
		FileMaker fm = new FileMaker();
		
		
		
		
		
		
		
		
		
		String type = "inter";
		String name = "drawcanvas";
		try
		{
			fm.save(type+"-"+name+".html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
		root.addChild(new Element("name").setText(filename));
		
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
					construct = new Element("class");
					
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
					construct = new Element("interface");
					
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
					construct = new Element("enum");
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
					//exported.add(export); // add to memory
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
		String path = CORE_OUT + "/" + name + ".xml";
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
			if (line.contains("(") && line.contains(")") && braceTracker==1)
			{
				if (line.startsWith("private")) continue;
				if (line.startsWith("@")) continue;
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
				
				// is the return type
				if ( !isModifier(ret) )
				{
					construct.addChild(new Element("returns").setText(ret));
				}
				// else, constructor
				else
				{
					construct.setName("constructor");
					construct.addChild(new Element("modifiers").setText(ret));
				}
				
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
						construct.addChild(new Element("param").setText(p.trim()));
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
				
				root.addChild(construct);
			}
			else
			/*
			 * field scan
			 */
			if (line.endsWith(";") && braceTracker==1)
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
				
				if (varMod.contains("private")) continue;
				
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
		return i;
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
}
