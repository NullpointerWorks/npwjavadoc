package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Constructor;
import com.nullpointerworks.generate.clazz.Method;
import com.nullpointerworks.generate.func.Parameter;

public class Key implements GenericMaker
{
	public static void build(String path)
	{
		new Key().make(path);
	}
	
	public void make(String path)
	{
		
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core-input.html", "com.nullpointerworks.core.input");
		fm.setFileName(Java.CLASS, "Key");
		fm.setDescription("Contains static some integer ASCII members which are of common usage in games. This is not a comprehensive collection of all ASCII/UTF character codes, just the ones that are common such as letters (A-Z) and numbers (0-9), some control keys (CTRL, ALT, ESC, etc.), arrow keys and F1-10 keys.");
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");
		
		fm.addConstructor(getConstructor1());

		fm.addMethod(getWidth());
		fm.addMethod(getHeight());
		fm.addMethod(getComponent());
		fm.addMethod(getSwap());
		fm.addMethod(getPaint());
		
		try
		{
			fm.save(path+"class-panelcanvas.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	Constructor getConstructor1()
	{
		Constructor c = new Constructor("PanelCanvas");
		var p1 = new Parameter("int", "width", "the width of the drawing surface");
		var p2 = new Parameter("int", "height", "the height of the drawing surface");
		c.setParameter(p1);
		c.setParameter(p2);
		c.setDescription("Creates a drawing surface of the given dimensions. It will accepts colors as integers in ARGB format at 8 bit depth.");
		c.setSince("1.0.0");
		return c;
	}
	
	Method getWidth()
	{
		Method m = new Method("int", "width");
		m.setDescription("Override - <a href=\"inter-drawcanvas.html\">DrawCanvas</a>");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getHeight()
	{
		Method m = new Method("int", "height");
		m.setDescription("Override - <a href=\"inter-drawcanvas.html\">DrawCanvas</a>");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getComponent()
	{
		Method m = new Method("Component", "component");
		m.setDescription("Override - <a href=\"inter-drawcanvas.html\">DrawCanvas</a>");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getSwap()
	{
		Method m = new Method("void", "swap");
		m.setParameter("int[]", "pix", "");
		m.setDescription("Override - <a href=\"inter-drawcanvas.html\">DrawCanvas</a>");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getPaint()
	{
		Method m = new Method("void", "paint");
		m.setParameter("Graphics", "g", "");
		m.setDescription("Override - javax.swing.JPanel");
		m.setSince("1.0.0");
		return m;
	}
	
}
