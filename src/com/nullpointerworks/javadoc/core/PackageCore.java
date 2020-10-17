package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.PackageMaker;

public class PackageCore implements GenericMaker
{
	public static void build(String path)
	{
		new PackageCore().make(path);
	}
	
	// ============================================================
	// PACKAGE
	// ============================================================
	
	public void make(String path)
	{
		PackageMaker pm = new PackageMaker();
		pm.setSourceModule("module-core.html", "libnpw.core");
		pm.setPackageName("com.nullpointerworks.core");
		
		// interfaces
		pm.setInterface("inter-drawcanvas.html",
						"DrawCanvas",
						"A rendering interface that provides a handle for a Window to display.");
		
		// classes
		pm.setClass("class-monitor.html",
					"Monitor",
					"Provides access the host PC's graphics environment to gain access to its connected monitors.");
		
		pm.setClass("class-panelcanvas.html",
					"PanelCanvas",
					"A javax.swing.JPanel implementation of the DrawCanvas that can be used for software-rendering graphics.");
		
		pm.setClass("class-window.html",
					"Window",
					"A container for a javax.swing.JFrame which serves as the primary window handler.");
		
		// enumerations
		pm.setEnum( "enum-windowmode.html",
					"WindowMode",
					"Contains three windowing modes used by the Window class.");
		
		try
		{
			pm.save(path+"pack-core.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
