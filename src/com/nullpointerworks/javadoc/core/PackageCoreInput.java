package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.PackageMaker;

public class PackageCoreInput implements GenericMaker
{
	public static void build(String path)
	{
		new PackageCoreInput().make(path);
	}
	
	// ============================================================
	// PACKAGE
	// ============================================================
	
	public void make(String path)
	{
		PackageMaker pm = new PackageMaker();
		pm.setSourceModule("module-core.html", "libnpw.core");
		pm.setPackageName("com.nullpointerworks.core.input");
		
		// classes
		pm.setClass("class-key.html",
				"Key",
				"Contains static some integer ASCII members which are of common usage in " + 
				"games. This is not a comprehensive collection of all ASCII/UTF character " + 
				"codes, just the ones that are common such as letters (A-Z) and numbers " + 
				"(0-9), some control keys (CTRL, ALT, ESC, etc.), arrow keys and F1-10 keys.");
		
		pm.setClass("class-keyboardinput.html",
					"KeyboardInput",
					"Input device for a keyboard. Implements java.awt.event.KeyListener interface.");
	
		pm.setClass("class-mouse.html",
					"Mouse",
					"A container of event codes for common mouse even triggers.");
		
		pm.setClass("class-mouseinput.html",
					"MouseInput",
					"Input device for a mouse. Implements java.awt.event.MouseListener, " + 
					"MouseMotionListener and MouseWheelListener. " + 
					"It keeps track of mouse buttons and the scrolling wheel." + 
					"interface.");
		
		try
		{
			pm.save(path+"pack-core-input.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
