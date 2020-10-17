package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.ModuleMaker;

public class ModuleCore implements GenericMaker
{
	public static void build(String path)
	{
		new ModuleCore().make(path);
	}
	
	// ============================================================
	// MODULE
	// ============================================================
	
	public void make(String path)
	{
		String[] desc = 
		{
			"Creative Commons - Attribution, Share Alike 4.0",
			"Nullpointer Works (2019)",
			"Use of this library is subject to license terms."
		};
		
		ModuleMaker mm = new ModuleMaker();
		mm.setName("libnpw.core");
		mm.setVersion("1.0.0");
		mm.setAuthor("Michiel Drost - Nullpointer Works");
		mm.setDescription(desc);
		
		mm.setExport("pack-core.html", 
					 "com.nullpointerworks.core");
		
		mm.setExport("pack-core-buffer.html", 
					 "com.nullpointerworks.core.buffer");
		
		mm.setExport("pack-core-buffer-concurrency.html", 
					 "com.nullpointerworks.core.buffer.concurrency");
		
		mm.setExport("pack-core-input.html", 
					 "com.nullpointerworks.core.input");
		
		mm.setRequired("transitive",
					   "java.desktop");
		
		try
		{
			mm.save(path+"module-core.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
