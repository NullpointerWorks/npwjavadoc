package com.nullpointerworks.htmlmaker.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.htmlmaker.generator.pack.PackageItem;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class PackageMaker
{
	private String sourceModuleFile = "";
	private String sourceModule = "";
	private String packageName = "";
	
	private List<PackageItem> ifaces;
	private List<PackageItem> classes;
	private List<PackageItem> enums;
	
	public PackageMaker()
	{
		ifaces = new ArrayList<PackageItem>();
		classes = new ArrayList<PackageItem>();
		enums = new ArrayList<PackageItem>();
	}
	
	public void setSourceModule(String fn, String n) 
	{
		sourceModuleFile = fn;
		sourceModule=n;
	}
	
	public void setPackageName(String n) 
	{
		packageName=n;
	}
	
	public void setInterface(String file, String name, String desc)
	{
		ifaces.add( new PackageItem(file,name,desc) );
	}
	
	public void setClass(String file, String name, String desc)
	{
		classes.add( new PackageItem(file,name,desc) );
	}
	
	public void setEnum(String file, String name, String desc)
	{
		enums.add( new PackageItem(file,name,desc) );
	}
	
	// ============================================================
	//
	// ============================================================
	
	private String makeHead()
	{
		String head = 
		"<!DOCTYPE html>\r\n" + 
		"<html>\r\n" + 
		"    <head>\r\n" + 
		"        <title>"+packageName+" - API Reference - Nullpointer Works</title>\r\n" + 
		"        <meta charset=\"utf-8\"/>\r\n" + 
		"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\r\n" + 
		"    	<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\"/>\r\n" + 
		"    	<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/layout.css\"/>\r\n" + 
		"    </head>\r\n" + 
		"    <body>\r\n" + 
		"        <div class=\"container\">\r\n" + 
		"            <div class=\"header vdark small petrol-font\">\r\n" + 
		"                Module <a href=\""+sourceModuleFile+"\">"+sourceModule+"</a>\r\n" + 
		"            </div>\r\n" + 
		"            <div class=\"header vdark petrol-font\">\r\n" + 
		"                Package "+packageName+"\r\n" + 
		"            </div>\r\n";
		return head;
	}

	// ============================================================
	//
	// ============================================================
	
	private String makeInterfaceList()
	{
		if (ifaces.size() < 1) return "";
		
		String list = 
		"            <!-- interface section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Interfaces</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:20%;\"><strong>Interface</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Description</strong></div>\r\n" + 
		"                    </div>\r\n" + 
		makeItemList(ifaces)+
		"                </div>\r\n" +
		"            </div>\r\n";
		
		return list;
	}
	
	private String makeClassesList()
	{
		if (classes.size() < 1) return "";
		
		String list = 
		"            <!-- class section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Classes</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:20%;\"><strong>Class</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Description</strong></div>\r\n" + 
		"                    </div>\r\n" + 
		makeItemList(classes)+
		"                </div>\r\n" +
		"            </div>\r\n";
		
		return list;
	}
	
	private String makeEnumerationList()
	{
		if (enums.size() < 1) return "";
		
		String list = 
		"            <!-- enumeration section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Enumerations</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:20%;\"><strong>Enum</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Description</strong></div>\r\n" + 
		"                    </div>\r\n" + 
		makeItemList(enums)+
		"                </div>\r\n" +
		"            </div>\r\n";
		
		return list;
	}
	
	private String makeItemList(List<PackageItem> list)
	{
		String req = "";
		for (PackageItem p : list)
		{
			req+=
			"                    <div class=\"rTableRow\">\r\n" + 
			"                        <div class=\"rTableCell\"><a href=\""+p.getFileName()+"\">"+p.getItemName()+"</a></div>\r\n" + 
			"                        <div class=\"rTableCell\">"+p.getDescription()+"</div>\r\n" + 
			"                    </div>\r\n";
		}
		return req;
	}
	
	// ============================================================
	//
	// ============================================================
	
	private String makeEnd()
	{
		String end = 
		"            <!-- end -->\r\n" + 
		"        </div>\r\n" + 
		"    </body>\r\n" + 
		"</html>\r\n";
		return end;
	}
	
	// ============================================================
	//
	// ============================================================
	
	public void save(String path) throws IOException
	{
		TextFile tf = new TextFile();
		tf.addLine(makeHead());
		tf.addLine(makeInterfaceList());
		tf.addLine(makeClassesList());
		tf.addLine(makeEnumerationList());
		tf.addLine(makeEnd());
		tf.setEncoding("UTF-8");
		TextFileParser.write(path, tf);
	}
}
