package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Method;
import com.nullpointerworks.generate.func.Parameter;

public class DrawCanvas implements GenericMaker
{
	public static void build(String path)
	{
		new DrawCanvas().make(path);
	}
	
	// ============================================================
	// FILE DrawCanvas
	// ============================================================
	
	public void make(String path)
	{
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core.html", "com.nullpointerworks.core");
		fm.setFileName(Java.INTERFACE, "DrawCanvas");
		fm.setDescription("Drawing interface for a libnpw.core window.");
		
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");
		
		fm.addMethod(makeWidth());
		fm.addMethod(makeHeight());
		fm.addMethod(makeSwap());
		fm.addMethod(makeComponent());
		
		try
		{
			fm.save(path+"inter-drawcanvas.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	Method makeWidth()
	{
		Method width = new Method("int","width");
		width.setDescription("Returns the width in pixels of the drawing canvas.");
		width.setReturns("the width in pixels");
		width.setSince("1.0.0");
		return width;
	}
	
	Method makeHeight()
	{
		Method height = new Method("int","height");
		height.setDescription("Returns the height in pixels of the drawing canvas.");
		height.setReturns("the height in pixels");
		height.setSince("1.0.0");
		return height;
	}
	
	Method makeSwap()
	{
		Method swap = new Method("void","swap");
		var p1 = new Parameter("int[]","pixels", "an integer array for the same size as the rendering surface");
		swap.setParameter( p1 );
		swap.setDescription("Swap the content of the provided array with the content held by the drawing canvas. The swapped content will be displayed when new frames are requested. Each integer is an ARGB color at 8 bit depth.");
		swap.setSince("1.0.0");
		return swap;
	}
	
	Method makeComponent()
	{
		Method component = new Method("Component","component");
		component.setDescription("Returns a java.awt.Component to which the canvas is drawing on. It can be added as a UI element to a JFrame for display.");
		component.setReturns("a UI component for a window to display");
		component.setSince("1.0.0");
		return component;
	}
}
