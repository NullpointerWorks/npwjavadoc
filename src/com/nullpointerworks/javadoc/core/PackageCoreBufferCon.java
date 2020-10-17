package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.PackageMaker;

public class PackageCoreBufferCon implements GenericMaker
{
	public static void build(String path)
	{
		new PackageCoreBufferCon().make(path);
	}
	
	// ============================================================
	// PACKAGE
	// ============================================================
	
	public void make(String path)
	{
		PackageMaker pm = new PackageMaker();
		pm.setSourceModule("module-core.html", "libnpw.core");
		pm.setPackageName("com.nullpointerworks.core.buffer.concurrency");
		
		// classes
		pm.setClass("class-syncbooleanbuffer.html", "SyncBooleanBuffer",
				"A thread safe implementation of the BooleanBuffer.");
		
		pm.setClass("class-syncbytebuffer.html", "SyncByteBuffer",
				"A thread safe implementation of the ByteBuffer.");
		
		pm.setClass("class-syncdoublebuffer.html", "SyncDoubleBuffer",
				"A thread safe implementation of the DoubleBuffer.");
		
		pm.setClass("class-syncfloatbuffer.html", "SyncFloatBuffer",
				"A thread safe implementation of the FloatBuffer.");
		
		pm.setClass("class-syncintbuffer.html", "SyncIntBuffer",
				"A thread safe implementation of the IntBuffer.");
		
		pm.setClass("class-synclongbuffer.html", "SyncLongBuffer",
				"A thread safe implementation of the LongBuffer.");
		
		pm.setClass("class-syncshortbuffer.html", "SyncShortBuffer",
				"A thread safe implementation of the ShortBuffer.");
		
		try
		{
			pm.save(path+"pack-core-buffer-concurrency.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
