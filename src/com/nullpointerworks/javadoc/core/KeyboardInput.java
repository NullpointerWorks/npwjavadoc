package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Constructor;
import com.nullpointerworks.generate.clazz.Method;

public class KeyboardInput implements GenericMaker
{
	public static void build(String path)
	{
		new KeyboardInput().make(path);
	}
	
	public void make(String path)
	{
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core-input.html", "com.nullpointerworks.core.input");
		fm.setFileName(Java.CLASS, "KeyboardInput");
		fm.setDescription("Contains static some integer ASCII members which are of common usage in games. This is not a comprehensive collection of all ASCII/UTF character codes, just the ones that are common such as letters (A-Z) and numbers (0-9), some control keys (CTRL, ALT, ESC, etc.), arrow keys and F1-10 keys.");
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");
		
		fm.addConstructor(getConstructor1());
		
		fm.addMethod(getIsLetter());
		
		try
		{
			fm.save(path+"class-keyboardinput.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	Constructor getConstructor1()
	{
		Constructor c = new Constructor();
		
		return c;
	}
	
	Method getIsLetter()
	{
		Method m = new Method("boolean", "isLetter");
		m.setModifier("static");
		m.setDescription("Returns true if the given key code is a letter in the alphabet. The ASCII codes for letters is from 65 until and including 90 for upper case letters, and from 97 until and including 122 for lower case letters.");
		m.setParameter("int", "code", "the integer key code");
		m.setReturns("true if the given code is a letter");
		m.setSince("1.0.0");
		return m;
	}
	
	
	
	
	
	
	
}
