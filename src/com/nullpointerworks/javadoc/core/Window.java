package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Constructor;
import com.nullpointerworks.generate.clazz.Method;
import com.nullpointerworks.generate.func.Parameter;

public class Window implements GenericMaker
{
	public static void build(String path)
	{
		new Window().make(path);
	}
	
	final String desc = 
			"A container for a javax.swing.JFrame which serves as the primary window " + 
			"handler. The Window does not extend any other subclass. To gain access to " + 
			"the underlying frame use; window.getFrame();";
	
	public void make(String path)
	{
		
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core.html", "com.nullpointerworks.core");
		fm.setFileName(Java.CLASS, "Window");
		fm.setDescription(desc);
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");
		
		fm.addConstructor(getConstructor1());
		fm.addConstructor(getConstructor2());
		fm.addConstructor(getConstructor3());
		fm.addConstructor(getConstructor4());
		fm.addConstructor(getConstructor5());
		
		fm.addMethod(getSwap());
		fm.addMethod(getVisible());
		fm.addMethod(getWindowMode());
		fm.addMethod(getSetDrawCanvas());
		fm.addMethod(getIcon());
		fm.addMethod(getIcon2());
		fm.addMethod(getInputDevice());
		fm.addMethod(getInputDevice2());
		fm.addMethod(getWindowListener());
		fm.addMethod(getDrawCanvas());
		fm.addMethod(getFrame());
		fm.addMethod(getMouse());
		fm.addMethod(getKeyboard());
		fm.addMethod(getWidth());
		fm.addMethod(getHeight());
		fm.addMethod(getTitle());
		
		try
		{
			fm.save(path+"class-window.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	Constructor getConstructor1()
	{
		String desc = "Create a window with the given internal dimensions. " + 
				"By default, this window will have no title, be set " + 
				"in windowed mode and appear on monitor ID 1.";
		
		Constructor c = new Constructor("Window");
		var p1 = new Parameter("int", "width", "the width of the display");
		var p2 = new Parameter("int", "height", "the height of the display");
		c.setParameter(p1);
		c.setParameter(p2);
		c.setDescription(desc);
		c.setSince("1.0.0");
		return c;
	}
	
	Constructor getConstructor2()
	{
		String desc = "Create a window with the given internal dimensions and title. " + 
				"By default, this window will have no title, be set " + 
				"in windowed mode and appear on monitor ID 1.";
		
		Constructor c = new Constructor("Window");
		var p1 = new Parameter("int", "width", "the width of the display");
		var p2 = new Parameter("int", "height", "the height of the display");
		var p3 = new Parameter("String", "title", "the window title");
		c.setParameter(p1);
		c.setParameter(p2);
		c.setParameter(p3);
		c.setDescription(desc);
		c.setSince("1.0.0");
		return c;
	}
	
	Constructor getConstructor3()
	{
		String desc = "Create a window with the given internal dimensions, title and windowing mode. " + 
				"By default, this window will have no title, be set " + 
				"in windowed mode and appear on monitor ID 1.";
		
		Constructor c = new Constructor("Window");
		var p1 = new Parameter("int", "width", "the width of the display");
		var p2 = new Parameter("int", "height", "the height of the display");
		var p3 = new Parameter("String", "title", "the window title");
		var p4 = new Parameter("WindowMode", "mode", "the windowing mode");
		c.setParameter(p1);
		c.setParameter(p2);
		c.setParameter(p3);
		c.setParameter(p4);
		c.setDescription(desc);
		c.setSince("1.0.0");
		return c;
	}
	
	Constructor getConstructor4()
	{
		String desc = "Create a window with the dimensions of the target monitor. " + 
				"This constructor is meant for fullscreen type modes only. " + 
				"When a non-fullscreen type mode is used, the dimensions " + 
				"will default to 800 by 600 pixels.";
		
		Constructor c = new Constructor("Window");
		var p1 = new Parameter("String", "title", "the window title");
		var p2 = new Parameter("WindowMode", "mode", "the windowing mode");
		var p3 = new Parameter("Monitor", "monitor", "the target monitor");
		c.setParameter(p1);
		c.setParameter(p2);
		c.setParameter(p3);
		c.setDescription(desc);
		c.setSince("1.0.0");
		return c;
	}
	
	Constructor getConstructor5()
	{
		String desc = "Create a window with the given internal dimensions, " + 
				"title, windowing mode and target monitor.";
		
		Constructor c = new Constructor("Window");
		var p1 = new Parameter("int", "width", "the width of the display");
		var p2 = new Parameter("int", "height", "the height of the display");
		var p3 = new Parameter("String", "title", "the window title");
		var p4 = new Parameter("WindowMode", "mode", "the windowing mode");
		var p5 = new Parameter("Monitor", "monitor", "the target monitor");
		c.setParameter(p1);
		c.setParameter(p2);
		c.setParameter(p3);
		c.setParameter(p4);
		c.setParameter(p5);
		c.setDescription(desc);
		c.setSince("1.0.0");
		return c;
	}
	
	Method getSwap()
	{
		String desc = "Swap the integer content of the given array to the " + 
				"display buffer. It's up to the user to make sure that " + 
				"the length of the array matches the area of the display." + 
				"Each integer is assumed to follow ARGB order at a bit " + 
				"depth of 8 bits.";
		
		Method m = new Method("void", "swap");
		m.setParameter("int[]", "pixels", "the array of integers that represent colors");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getVisible()
	{
		String desc = "Delegate to display the internal JFrame object. Set " + 
				"true to make the window appear, false to hide it.";
		
		Method m = new Method("void", "setVisible");
		m.setParameter("boolean", "show", "to show, or not to show");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getWindowMode()
	{
		String desc = "Set the windowing mode for this window.";
		
		Method m = new Method("void", "setWindowMode");
		m.setParameter("WindowMode", "mode", "determines if a window will be framed and what dimensions it might take");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getSetDrawCanvas()
	{
		String desc = "This method sets a DrawCanvas object as the primary " + 
				"recipient to render to. When creating a new window, this " + 
				"class creates a PanelCanvas instance as the primary " + 
				"rendering recipient. Use this method only if you want " + 
				"the window to utilize non-standard rendering code.";
		
		Method m = new Method("void", "setDrawCanvas");
		m.setParameter("DrawCanves", "canvas", "the drawing canvas to be used in this window");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getIcon()
	{
		String desc = "Provide an IntBuffer object that contains the ARGB image " + 
				"data to be set as the icon for the window. No icon will " + 
				"be set if the input image is null.";
		
		Method m = new Method("void", "setIcon");
		m.setParameter("IntBuffer", "image", "the integer buffer to be used");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getIcon2()
	{
		String desc = "Provide an input stream linking to image data to be used " + 
				"as the icon for the window.";
		
		Method m = new Method("void", "setIcon");
		m.setParameter("InputStream", "stream", "input stream linking to the image data");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getInputDevice()
	{
		String desc = "Add a mouse input to the window to read user input. The " + 
				"MouseInput will be attached as a listener to the DrawCanvas. " + 
				"This input device will be moved to another canvas when " + 
				"swapping to another canvas.";
		
		Method m = new Method("void", "addInputDevice");
		m.setParameter("MouseInput", "mouseinput", "the mouse input object");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getInputDevice2()
	{
		String desc = "Add a keyboard input to the window to read user input. The " + 
				"KeyboardInput will be added as a listener to the internal " + 
				"frame. This input device will be moved to another frame when " + 
				"creating a new window.";
		
		Method m = new Method("void", "addInputDevice");
		m.setParameter("KeyboardInput", "keyboardinput", "the keyboard input object");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getWindowListener()
	{
		String desc = 
				"Add a java.awt.event.WindowListener to the internal frame. " + 
				"When creating a new window, all window listeners will be " + 
				"added to the new window. This method is intended to be used " + 
				"by the libnpw.game library which has a LoopAdapter that " + 
				"track the state of the application and inform the window.";
		
		Method m = new Method("void", "addWindowListener");
		m.setParameter("WindowListener", "windowlistener", "the window listener to be added");
		m.setDescription(desc);
		m.setSince("1.0.0");
		return m;
	}
	
	Method getDrawCanvas()
	{
		Method m = new Method("DrawCanvas", "getDrawCanvas");
		m.setDescription("Returns the DrawCanvas rendering surface.");
		m.setReturns("the DrawCanvas rendering surface");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getFrame()
	{
		Method m = new Method("JFrame", "getFrame");
		m.setDescription("Returns the JFrame which is the primary windowing container.");
		m.setReturns("the JFrame which is the primary windowing container");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getMouse()
	{
		Method m = new Method("MouseInput", "getMouse");
		m.setDescription("Returns the MouseInput object attached to the window.");
		m.setReturns("the MouseInput object attached to the window");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getKeyboard()
	{
		Method m = new Method("KeyboardInput", "getKeyboard");
		m.setDescription("Returns the KeyboardInput object attached to the window.");
		m.setReturns("the KeyboardInput object attached to the window");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getWidth()
	{
		Method m = new Method("int", "getWidth");
		m.setDescription("Returns the internal rendering width of the window.");
		m.setReturns("the internal rendering width of the window");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getHeight()
	{
		Method m = new Method("int", "getHeight");
		m.setDescription("Returns the internal rendering height of the window.");
		m.setReturns("the internal rendering height of the window");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getTitle()
	{
		Method m = new Method("String", "getTitle");
		m.setDescription("Returns the title of the window.");
		m.setReturns("the title of the window");
		m.setSince("1.0.0");
		return m;
	}
}
