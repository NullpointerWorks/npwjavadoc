package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.enums.EnumField;

public class WindowMode implements GenericMaker
{
	public static void build(String path)
	{
		new WindowMode().make(path);
	}
	
	final String desc = 
			"This enumeration contains three modes for a libnpw-core Window. " + 
			"Items include; WINDOWED, BORDERLESS, FULLSCREEN and BORDERLESSFULL mode.";
	
	public void make(String path)
	{
		
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core.html", "com.nullpointerworks.core");
		fm.setFileName(Java.ENUM, "WindowMode");
		fm.setDescription(desc);
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");

		fm.addField(getWINDOWED());
		fm.addField(getBORDERLESS());
		fm.addField(getFULLSCREEN());
		fm.addField(getBORDERLESSFULL());
		
		try
		{
			fm.save(path+"enum-windowmode.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	EnumField getWINDOWED()
	{
		String desc = 
				"Make a window display with a surrounding frame. The " + 
				"layout and design of the frame is determined by " + 
				"the host OS.";
		EnumField f = new EnumField("WINDOWED");
		f.setDescription(desc);
		f.setSince("1.0.0");
		return f;
	}
	
	EnumField getBORDERLESS()
	{
		String desc = 
				"Make a window appear without a surrounding frame. " + 
				"This means there's no interface to close the window " + 
				"other than forcing the main thread to stop. Be sure " + 
				"to have a way to do this in your application.";
		EnumField f = new EnumField("BORDERLESS");
		f.setDescription(desc);
		f.setSince("1.0.0");
		return f;
	}
	
	EnumField getFULLSCREEN()
	{
		String desc = 
				"Make a window appear without a surrounding frame " + 
				"and have the dimensions to fit the target monitor. " + 
				"Since there's no frame, there's no interface to " + 
				"close the window other than forcing the main thread " + 
				"to stop. Be sure to have a way to do this in your " + 
				"application. Fullscreen mode support depends on " + 
				"the host PC's graphics card. Some PC's may not " + 
				"allow for fullscreen.";
		EnumField f = new EnumField("FULLSCREEN");
		f.setDescription(desc);
		f.setSince("1.0.0");
		return f;
	}
	
	EnumField getBORDERLESSFULL()
	{
		String desc = 
				"A blend of fullscreen and borderless mode. Make " + 
				"a window appear without a surrounding frame and " + 
				"have the dimensions to fit the target monitor. " + 
				"Since there's no frame, there's no interface to " + 
				"close the window other than forcing the main thread " + 
				"to stop. Be sure to have a way to do this in your " + 
				"application. Since this mode does not require graphics " + 
				"card support directly, it's often faster established " + 
				"than fullscreen mode.";
		EnumField f = new EnumField("BORDERLESSFULL");
		f.setDescription(desc);
		f.setSince("1.0.0");
		return f;
	}
}
