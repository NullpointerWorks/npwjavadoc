package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Method;

public class Monitor implements GenericMaker
{
	public static void build(String path)
	{
		new Monitor().make(path);
	}

	public void make(String path)
	{
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core.html", "com.nullpointerworks.core");
		fm.setFileName(Java.CLASS, "Monitor");
		fm.setDescription("Provides access the host PC's graphics environment to gain access to its connected monitors.");
		
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");
		
		fm.addMethod(getDisplay());
		fm.addMethod(getDisplay2());
		fm.addMethod(getGraphicsEnvironment());
		fm.addMethod(getGraphicsDevice());
		fm.addMethod(getDisplayMode());
		fm.addMethod(getWidth());
		fm.addMethod(getHeight());
		fm.addMethod(getDeviceID());
		
		try
		{
			fm.save(path+"class-monitor.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	Field getTestField()
	{
		Field f = new Field("int","testInteger","42");
		f.setModifier("static");
		f.setDescription("The answer to life, the universe, and everything.");
		return f;
	}//*/
	
	Method getDisplay()
	{
		Method m = new Method("Monitor", "getDisplay");
		m.setParameter("int", "id", "the monitor display identity on the host system. Provide the identity - 1");
		m.setModifier("static");
		m.setDescription("Returns a Monitor object with the details of the selected display device.");
		m.setReturns("a Monitor object with the details of the selected display device");
		m.setSince("1.0.0");
		return m;
	}

	Method getDisplay2()
	{
		Method m = new Method("Monitor[]", "getDisplay");
		m.setModifier("static");
		m.setDescription("Returns an array of Monitor objects for each connected monitor.");
		m.setReturns("an array of Monitor objects for each connected monitor");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getGraphicsEnvironment()
	{
		Method m = new Method("GraphicsEnvironment", "getGraphicsEnvironment");
		m.setDescription("Returns the host's graphics environment associated with the monitor.");
		m.setReturns("the host's graphics environment associated with the monitor");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getGraphicsDevice()
	{
		Method m = new Method("GraphicsDevice", "getGraphicsDevice");
		m.setDescription("Returns the host's graphics device associated with the monitor.");
		m.setReturns("the host's graphics device associated with the monitor");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getDisplayMode()
	{
		Method m = new Method("DisplayMode", "getDisplayMode");
		m.setDescription("Returns the display mode for this monitor. This contains bit depth, width, height, etc.");
		m.setReturns("the display mode for this monitor");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getWidth()
	{
		Method m = new Method("int", "getWidth");
		m.setDescription("Returns the width in pixels of the display.");
		m.setReturns("the width in pixels");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getHeight()
	{
		Method m = new Method("int", "getHeight");
		m.setDescription("Returns the height in pixels of the display.");
		m.setReturns("the height in pixels");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getDeviceID()
	{
		Method m = new Method("int", "getDeviceID");
		m.setDescription("Returns the device ID on the host OS.");
		m.setReturns("the device ID on the host OS");
		m.setSince("1.0.0");
		return m;
	}
}
