package com.nullpointerworks.javadoc.core;

import java.io.IOException;

import com.nullpointerworks.generate.FileMaker;
import com.nullpointerworks.generate.GenericMaker;
import com.nullpointerworks.generate.Java;
import com.nullpointerworks.generate.clazz.Method;
import com.nullpointerworks.generate.clazz.Field;

public class Key implements GenericMaker
{
	public static void build(String path)
	{
		new Key().make(path);
	}
	
	public void make(String path)
	{
		FileMaker fm = new FileMaker();
		fm.setSourceModule("module-core.html", "libnpw.core");
		fm.setSourcePackage("pack-core-input.html", "com.nullpointerworks.core.input");
		fm.setFileName(Java.CLASS, "Key");
		fm.setDescription("Contains static some integer ASCII members which are of common usage in games. This is not a comprehensive collection of all ASCII/UTF character codes, just the ones that are common such as letters (A-Z) and numbers (0-9), some control keys (CTRL, ALT, ESC, etc.), arrow keys and F1-10 keys.");
		fm.setVersion("1.0.0");
		fm.setSince("1.0.0");
		fm.setAuthor("Michiel Drost - Nullpointer Works");

		fm.addMethod(getIsLetter());
		fm.addMethod(getIsNumber());
		fm.addMethod(getIsPunctuation());
		fm.addMethod(getToString());
		fm.addMethod(getShift());

		fm.addField(getERROR());
		fm.addField(getESC());
		fm.addField(getTAB());
		fm.addField(getSHIFT());
		fm.addField(getCTRL());
		fm.addField(getALT());
		fm.addField(getENTER());
		fm.addField(getSPACE());
		fm.addField(getPAUSE());
		fm.addField(getBACKSPACE());
		fm.addField(getF1());
		fm.addField(getF2());
		fm.addField(getF3());
		fm.addField(getF4());
		fm.addField(getF5());
		fm.addField(getF6());
		fm.addField(getF7());
		fm.addField(getF8());
		fm.addField(getF9());
		fm.addField(getF10());
		fm.addField(getDOWN());
		fm.addField(getRIGHT());
		fm.addField(getUP());
		fm.addField(getLEFT());
		
		fm.addField(getNum0());
		fm.addField(getNum1());
		fm.addField(getNum2());
		fm.addField(getNum3());
		fm.addField(getNum4());
		fm.addField(getNum5());
		fm.addField(getNum6());
		fm.addField(getNum7());
		fm.addField(getNum8());
		fm.addField(getNum9());
		
		try
		{
			fm.save(path+"class-key.html");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * fields
	 */
	Field getERROR()
	{
		Field f = new Field("int", "ERROR", "-1");
		f.setModifier("final static");
		f.setDescription("An error placeholder for unrecognized keystrokes.");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getESC()
	{
		Field f = new Field("int", "ESC", "27");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getTAB()
	{
		Field f = new Field("int", "TAB", "9");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getSHIFT()
	{
		Field f = new Field("int", "SHIFT", "16");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getCTRL()
	{
		Field f = new Field("int", "CTRL", "17");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getALT()
	{
		Field f = new Field("int", "ALT", "18");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getENTER()
	{
		Field f = new Field("int", "ENTER", "10");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getSPACE()
	{
		Field f = new Field("int", "SPACE", "32");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getPAUSE()
	{
		Field f = new Field("int", "PAUSE", "19");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getBACKSPACE()
	{
		Field f = new Field("int", "BACKSPACE", "8");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF1()
	{
		Field f = new Field("int", "F1", "112");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF2()
	{
		Field f = new Field("int", "F2", "113");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF3()
	{
		Field f = new Field("int", "F3", "114");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF4()
	{
		Field f = new Field("int", "F4", "115");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF5()
	{
		Field f = new Field("int", "F5", "116");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF6()
	{
		Field f = new Field("int", "F6", "117");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF7()
	{
		Field f = new Field("int", "F7", "118");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF8()
	{
		Field f = new Field("int", "F8", "118");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF9()
	{
		Field f = new Field("int", "F9", "120");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getF10()
	{
		Field f = new Field("int", "F10", "121");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getDOWN()
	{
		Field f = new Field("int", "DOWN", "40");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getRIGHT()
	{
		Field f = new Field("int", "RIGHT", "39");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getUP()
	{
		Field f = new Field("int", "UP", "38");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getLEFT()
	{
		Field f = new Field("int", "LEFT", "37");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum0()
	{
		Field f = new Field("int", "num0", "48");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum1()
	{
		Field f = new Field("int", "num1", "49");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum2()
	{
		Field f = new Field("int", "num2", "50");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum3()
	{
		Field f = new Field("int", "num3", "51");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum4()
	{
		Field f = new Field("int", "num4", "52");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum5()
	{
		Field f = new Field("int", "num5", "53");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum6()
	{
		Field f = new Field("int", "num6", "54");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum7()
	{
		Field f = new Field("int", "num7", "55");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum8()
	{
		Field f = new Field("int", "num8", "56");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	Field getNum9()
	{
		Field f = new Field("int", "num9", "57");
		f.setModifier("final static");
		f.setDescription("");
		f.setSince("1.0.0");
		return f;
	}
	
	
	
	
	
	/*
	 * methods
	 */
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
	
	Method getIsNumber()
	{
		Method m = new Method("boolean", "isNumber");
		m.setModifier("static");
		m.setDescription("Returns true if the given key code represents a number. The ASCII codes for numbers is from 48 until and including 57.");
		m.setParameter("int", "code", "the integer key code");
		m.setReturns("true if the given code is a number");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getIsPunctuation()
	{
		Method m = new Method("boolean", "isPunctuation");
		m.setModifier("static");
		m.setDescription("Returns true for the following non-shifted punctuation markers.");
		m.setParameter("int", "code", "the integer key code");
		m.setReturns("true for non-shifted punctuation markers");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getToString()
	{
		Method m = new Method("String", "toString");
		m.setModifier("static");
		m.setDescription("Returns the string representation of the given key code.");
		m.setReturns("the string representation of the given key code");
		m.setParameter("int", "code", "the integer key code");
		m.setSince("1.0.0");
		return m;
	}
	
	Method getShift()
	{
		Method m = new Method("int", "shift");
		m.setModifier("static");
		m.setDescription("Returns the shifted key code for the given character code if it can be shifted.");
		m.setReturns("the key code of the character when shifted");
		m.setParameter("int", "code", "the integer key code");
		m.setSince("1.0.0");
		return m;
	}
}
