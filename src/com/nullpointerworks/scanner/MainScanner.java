package com.nullpointerworks.scanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Constructor;
import com.nullpointerworks.generate.clazz.Method;
import com.nullpointerworks.generate.func.Parameter;
import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.DocumentBuilder;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.XMLParseException;

public class MainScanner 
{
	//private static final String JAVA_GIT 	= "D:/Development/Java/workspaces/git";
	private static final String JAVA_GIT 	= "F:/Development/Java/workspace/git";
	private static final String CORE 		= JAVA_GIT+"/libcore/src/com/nullpointerworks/core";
	
	public static void main(String[] args) 
	{
		//args = new String[] {JAVA_GIT+"libcore/src/module-info.java"};
		//args = new String[] {JAVA_GIT+"libcore/src/com/nullpointerworks/core/Monitor.java"};
		
		/*
		args = new String[] {"src/com/nullpointerworks/scanner/ScanTestInterface.java", 
							 "src/com/nullpointerworks/scanner/ScanTestClass.java", 
							 "src/com/nullpointerworks/scanner/ScanTestEnum.java"};
		//*/
		
		//args = new String[] {CORE+"/DrawCanvas.java"};
		//args = new String[] {CORE+"/Monitor.java"};
		args = new String[] {CORE+"/PanelCanvas.java"};
		//args = new String[] {CORE+"/Window.java"};
		//args = new String[] {CORE+"/WindowMode.java"};
		new MainScanner(args);
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
	
	/* ====================================================
	 * individual file scan
	 * ==================================================== */
	
	public MainScanner(String[] args)
	{
		SourceScanner sc = new SourceScanner(PACK);
		
		for (String f : args)
		{
			String xml = sc.scanFile(f);
			makeWebFile(xml);
		}
	}
	
	public void makeWebFile(String file) 
	{
		String absPath = file.replace("\\", "/");
		Log.out(absPath);
		
		var builder = DocumentBuilder.getDOMLoader();
		Document doc = null;
		
		try 
		{
			doc = builder.parse(absPath);
		} 
		catch (FileNotFoundException | XMLParseException e) 
		{
			e.printStackTrace();
		}
		if (doc == null)
		{
			return; // throw error
		}
		
		Element root = doc.getRootElement();
		String module = root.getChild("module").getText();
		String pack = root.getChild("package").getText();
		Element comment = root.getChild("commentary");
		Element version = comment.getChild("version");
		Element author = comment.getChild("author");
		Element since = comment.getChild("since");
		
		Element code = root.getChild("code");
		String type = code.getChild("type").getText();
		String name = code.getChild("name").getText();
		String[] info = getPackageInfo(pack);
		
		Java jtype = Java.CLASS;
		if (type.equalsIgnoreCase("interface")) jtype = Java.INTERFACE;
		if (type.equalsIgnoreCase("enum")) jtype = Java.ENUM;
		
		FileMaker fm = new FileMaker();
		fm.setSourceModule(info[3], module);
		fm.setSourcePackage(info[1], pack);
		fm.setFileName(jtype, name);
		fm.setDescription(comment.getText());
		if (version!=null)	fm.setVersion( version.getText() );
		if (since!=null)	fm.setSince( since.getText() );
		if (author!=null)	fm.setAuthor( author.getText() );
		
		/*
		 * loop through comments, field and methods
		 */
		List<Element> elements = code.getChildren();
		for (int i=0,l=elements.size(); i<l; i++)
		{
			Element child = elements.get(i);
			
			/*
			 * skip type and name. this was done before the element loop
			 */
			if (child.getName().equalsIgnoreCase("type")) continue;
			if (child.getName().equalsIgnoreCase("name")) continue;
			
			/*
			 * if constructor
			 */
			if (child.getName().equalsIgnoreCase("constructor"))
			{
				Constructor c = parseConstructor(child);
				fm.addConstructor(c);
				continue;
			}
			
			/*
			 * if method
			 */
			if (child.getName().equalsIgnoreCase("method"))
			{
				Method m = parseMethod(child);
				fm.addMethod(m);
				continue;
			}
		}
		
		try
		{
			fm.save("core/"+type+"-"+name.toLowerCase()+".html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
	private Method parseMethod(Element child) 
	{
		Method method = new Method();
		List<Element> method_children = child.getChildren();
		for (Element method_child : method_children)
		{
			String child_name = method_child.getName();
			
			/*
			 * set commentary
			 */
			if (child_name.equalsIgnoreCase("commentary"))
			{
				method.setDescription( method_child.getText() );
				continue;
			}
			
			/*
			 * set name
			 */
			if (child_name.equalsIgnoreCase("name"))
			{
				method.setName( method_child.getText() );
				continue;
			}
			
			/*
			 * set modifiers
			 */
			if (child_name.equalsIgnoreCase("modifiers"))
			{
				String mods = method_child.getText();
				method.setModifier(mods);
				continue;
			}
			
			/*
			 * set a parameter
			 */
			if (child_name.equalsIgnoreCase("param"))
			{
				Parameter p = parseParam(method_child);
				method.setParameter(p);
				continue;
			}
			
			/*
			 * return type
			 */
			if (child_name.equalsIgnoreCase("returns"))
			{
				String type = method_child.getChild("type").getText();
				if (!type.equalsIgnoreCase("void"))
				{
					Element el_returns = method_child.getChild("comment");
					if (el_returns!=null) method.setReturns(el_returns.getText());
				}
				method.setType(type);
			}
			
			/*
			 * @since
			 */
			if (child_name.equalsIgnoreCase("since"))
			{
				method.setSince(method_child.getText());
			}
			
			/*
			 * @author
			 */
			if (child_name.equalsIgnoreCase("author"))
			{
				method.setAuthor(method_child.getText());
			}
			
			/*
			 * @version
			 */
			if (child_name.equalsIgnoreCase("version"))
			{
				method.setVersion(method_child.getText());
			}
			
			/*
			 * @override
			 */
			if (child_name.equalsIgnoreCase("override"))
			{
				method.setAdditional("Override",method_child.getText());
			}
		}
		
		return method;
	}

	/*
	 * 
	 */
	private Constructor parseConstructor(Element child) 
	{
		Constructor constructor = new Constructor();
		List<Element> constructor_children = child.getChildren();
		for (Element constructor_child : constructor_children)
		{
			String child_name = constructor_child.getName();
			
			/*
			 * set commentary
			 */
			if (child_name.equalsIgnoreCase("commentary"))
			{
				constructor.setDescription( constructor_child.getText() );
				continue;
			}
			
			/*
			 * set name
			 */
			if (child_name.equalsIgnoreCase("name"))
			{
				constructor.setName( constructor_child.getText() );
				continue;
			}
			
			/*
			 * set modifiers
			 */
			if (child_name.equalsIgnoreCase("modifiers"))
			{
				String mods = constructor_child.getText();
				constructor.setModifier(mods);
				continue;
			}
			
			/*
			 * set a parameter
			 */
			if (child_name.equalsIgnoreCase("param"))
			{
				Parameter p = parseParam(constructor_child);
				constructor.setParameter(p);
				continue;
			}
			
			
			
			
			
			
		}
		return constructor;
	}
	
	/*
	 * 
	 */
	private Parameter parseParam(Element constructor_child) 
	{
		Parameter p = new Parameter();
		List<Element> param_children = constructor_child.getChildren();
		for (Element param_child : param_children)
		{
			String child_name = param_child.getName();
			
			/*
			 * set type
			 */
			if (child_name.equalsIgnoreCase("type"))
			{
				p.setType(param_child.getText());
				continue;
			}

			/*
			 * set type
			 */
			if (child_name.equalsIgnoreCase("name"))
			{
				p.setName(param_child.getText());
				continue;
			}
			
			/*
			 * set type
			 */
			if (child_name.equalsIgnoreCase("comment"))
			{
				p.setDescription(param_child.getText());
				continue;
			}
		}
		return p;
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
}
