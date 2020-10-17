package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.PackageMaker;

public class PackageCoreBuffer implements GenericMaker
{
	public static void build(String path)
	{
		new PackageCoreBuffer().make(path);
	}
	
	// ============================================================
	// PACKAGE
	// ============================================================
	
	public void make(String path)
	{
		PackageMaker pm = new PackageMaker();
		pm.setSourceModule("module-core.html", "libnpw.core");
		pm.setPackageName("com.nullpointerworks.core.buffer");
		
		// classes
		//pm.setClass("class-abstractbuffer.html", "AbstractBuffer",
		//		"Buffer object abstraction for all libnpw-core buffers.");
		
		pm.setClass("class-booleanbuffer.html", "BooleanBuffer",
				"A buffer implementation that contains a \"boolean\" array.");
		
		pm.setClass("class-bytebuffer.html", "ByteBuffer",
				"A buffer implementation that contains a \"byte\" array.");
		
		pm.setClass("class-doublebuffer.html", "DoubleBuffer",
				"A buffer implementation that contains a \"double\" array.");
		
		pm.setClass("class-floatbuffer.html", "FloatBuffer",
				"A buffer implementation that contains a \"float\" array.");
		
		pm.setClass("class-intbuffer.html", "IntBuffer",
				"A buffer implementation that contains an \"int\" array.");
		
		pm.setClass("class-longbuffer.html", "LongBuffer",
				"A buffer implementation that contains a \"long\" array.");
		
		pm.setClass("class-shortbuffer.html", "ShortBuffer",
				"A buffer implementation that contains a \"short\" array.");
		
		try
		{
			pm.save(path+"pack-core-buffer.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
