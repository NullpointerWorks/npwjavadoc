package com.nullpointerworks.scanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.nullpointerworks.generator.FileMaker;
import com.nullpointerworks.generator.Java;
import com.nullpointerworks.generator.clazz.Constructor;
import com.nullpointerworks.generator.clazz.Field;
import com.nullpointerworks.generator.clazz.Method;
import com.nullpointerworks.generator.func.Parameter;
import com.nullpointerworks.util.Log;

import exp.nullpointerworks.xml.Document;
import exp.nullpointerworks.xml.DocumentBuilder;
import exp.nullpointerworks.xml.Element;
import exp.nullpointerworks.xml.XMLParseException;

public class MainScanner 
{
	static final String JAVA_GIT 	= "D:/Development/Java/workspaces/git";
	//static final String JAVA_GIT 	= "F:/Development/Java/workspace/git";
	static final String CORE 		= JAVA_GIT+"/libcore/src/com/nullpointerworks/core";
	
	public static void main(String[] args) 
	{
		//args = new String[] {CORE+"/DrawCanvas.java"};
		MainScanner ms = new MainScanner();
		//ms.parseSourceFile(CORE+"/DrawCanvas.java");
		//ms.parseSourceFile(CORE+"/Monitor.java");
		//ms.parseSourceFile(CORE+"/PanelCanvas.java");
		//ms.parseSourceFile(CORE+"/Window.java");
		//ms.parseSourceFile(CORE+"/WindowMode.java");
		
		
		ms.parseSourceFile("src/com/nullpointerworks/examples/ExampleInterface.java");
		ms.parseSourceFile("src/com/nullpointerworks/examples/ExampleClass.java");
		ms.parseSourceFile("src/com/nullpointerworks/examples/ExampleEnum.java");
		//ms.parseSourceFile("src/com/nullpointerworks/examples/ExampleEnum2.java");
		
	}
	
	/* ====================================================
	 * individual file scan
	 * ==================================================== */
	
	/*
	 * package preset
	 */
	private final String[][] PACK = 
	{
		{"com.nullpointerworks.color", 		"pack-color.html", "libnpw.color", "module-color.html"},
		{"com.nullpointerworks.core", 		"pack-core.html", "libnpw.core", "module-core.html"},
		{"com.nullpointerworks.game", 		"pack-game.html", "libnpw.game", "module-game.html"},
		{"com.nullpointerworks.graphics", 	"pack-graphics.html", "libnpw.graphics", "module-graphics.html"},
		{"exp.nullpointerworks.http", 		"pack-http.html", "libnpw.http", "module-http.html"},
		{"com.nullpointerworks.j2d", 		"pack-j2d.html", "libnpw.j2d", "module-j2d.html"},
		{"com.nullpointerworks.math", 		"pack-math.html", "libnpw.math", "module-math.html"},
		{"com.nullpointerworks.util", 		"pack-util.html", "libnpw.util", "module-util.html"},
		{"exp.nullpointerworks.xml", 		"pack-xml.html", "libnpw.xml", "module-xml.html"},
	};
	
	public MainScanner() {}
	public MainScanner(String[] args)
	{
		for (String f : args)
		{
			parseSourceFile(f);
		}
	}
	
	public void parseSourceFile(String file)
	{
		SourceScanner sc = new SourceScanner(PACK);
		String xml = sc.scanFile(file);
		//makeWebFile(xml);
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
		
		/*
		 * TODO clean up the code below
		 */
		Element root = doc.getRootElement();
		String module = root.getChild("module").getText();
		String pack = root.getChild("package").getText();
		
		Element comment = root.getChild("commentary");
		Element version = comment.getChild("version");
		Element author = comment.getChild("author");
		Element since = comment.getChild("since");
		
		Element code = root.getChild("code");
		String[] info = getPackageInfo(pack);
		
		FileMaker fm = new FileMaker();
		fm.setSourceModule(info[3], module);
		fm.setSourcePackage(info[1], pack);
		fm.setDescription(comment.getText());
		if (version!=null)	fm.setVersion( version.getText() );
		if (since!=null)	fm.setSince( since.getText() );
		if (author!=null)	fm.setAuthor( author.getText() );
		
		/*
		 * used later when writing the web file
		 */
		String name = "";
		String type = "";
		
		/*
		 * loop through comments, field and methods
		 */
		List<Element> elements = code.getChildren();
		for (int i=0,l=elements.size(); i<l; i++)
		{
			Element child = elements.get(i);
			String child_name = child.getName();
			
			/*
			 * get file name
			 */
			if (child_name.equalsIgnoreCase("name")) 
			{
				name = child.getText();
				fm.setFileName(name);
				continue;
			}
			
			/*
			 * get source type
			 */
			if (child_name.equalsIgnoreCase("type")) 
			{
				type = child.getText();
				Java jtype = Java.CLASS;
				if (type.equalsIgnoreCase("interface")) 
					jtype = Java.INTERFACE;
				if (type.equalsIgnoreCase("enum")) 
					jtype = Java.ENUM;
				if (type.equalsIgnoreCase("@interface")) 
					jtype = Java.ANNOTATION;
				fm.setFileType(jtype);
				continue;
			}
			
			/*
			 * if constructor
			 */
			if (child_name.equalsIgnoreCase("constructor"))
			{
				Constructor c = parseConstructor(child);
				fm.addConstructor(c);
				continue;
			}
			
			/*
			 * if method
			 */
			if (child_name.equalsIgnoreCase("method"))
			{
				Method m = parseMethod(child);
				fm.addMethod(m);
				continue;
			}
			
			/*
			 * if field
			 */
			if (child_name.equalsIgnoreCase("field"))
			{
				Field m = parseField(child);
				fm.addField(m);
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
			
			/*
			 * @since
			 */
			if (child_name.equalsIgnoreCase("since"))
			{
				constructor.setSince(constructor_child.getText());
			}
			
			/*
			 * @author
			 */
			if (child_name.equalsIgnoreCase("author"))
			{
				constructor.setAuthor(constructor_child.getText());
			}
			
			/*
			 * @version
			 */
			if (child_name.equalsIgnoreCase("version"))
			{
				constructor.setVersion(constructor_child.getText());
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
	
	/*
	 * 
	 */
	private Field parseField(Element child) 
	{
		Field f = new Field();
		List<Element> field_children = child.getChildren();
		for (Element field_child : field_children)
		{
			String child_name = field_child.getName();

			/*
			 * set name
			 */
			if (child_name.equalsIgnoreCase("name"))
			{
				f.setName( field_child.getText() );
				continue;
			}
			
			/*
			 * set type
			 */
			if (child_name.equalsIgnoreCase("type"))
			{
				f.setType( field_child.getText() );
				continue;
			}
			
			/*
			 * set data
			 */
			if (child_name.equalsIgnoreCase("data"))
			{
				f.setValue( field_child.getText() );
				continue;
			}
			
			/*
			 * set modifiers
			 */
			if (child_name.equalsIgnoreCase("modifiers"))
			{
				f.setModifier( field_child.getText() );
				continue;
			}
		}
		return f;
	}
	
	/*
	 * 
	 */
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
