package com.nullpointerworks.generate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.generate.mod.Exported;
import com.nullpointerworks.generate.mod.Required;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class ModuleMaker
{
	private String name = "";
	private String desc = "";
	private String vers = "";
	private String auth = "";
	private List<Exported> exports;
	private List<Required> required;
	
	public ModuleMaker() 
	{
		exports = new ArrayList<Exported>();
		required = new ArrayList<Required>();
	}

	public void setName(String n) 
	{
		name=n;
	}

	public void setVersion(String v) 
	{
		vers=v;
	}

	public void setAuthor(String a) 
	{
		auth=a;
	}
	
	public void setDescription(String[] lines) 
	{
		desc = "";
		for (String l : lines)
		{
			desc += "                    "+l+"<br/>\r\n";
		}
	}

	public void setExport(String f, String p)
	{
		exports.add( new Exported(f,p) );
	}

	public void setRequired(String mod, String name)
	{
		required.add( new Required(mod,name) );
	}
	
	// ============================================================
	//
	// ============================================================
	
	private String makeHead()
	{
		String head = 
		"<!DOCTYPE html>\r\n"+
		"<html>\r\n" + 
		"    <head>\r\n" + 
		"        <title>"+name+" - API Reference - Nullpointer Works</title>\r\n" + 
		"        <meta charset=\"utf-8\"/>\r\n" + 
		"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\r\n" + 
		"    	<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\"/>\r\n" + 
		"    	<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/layout.css\"/>\r\n" + 
		"    </head>\r\n" +
		"    <body>\r\n" + 
		"        <div class=\"container\">\r\n" + 
		"            <div class=\"header vdark petrol-font\">\r\n" + 
		"                Module "+name+"\r\n" + 
		"            </div>\r\n";
		return head;
	}
	
	// ============================================================
	//
	// ============================================================
	
	private String makeDescription()
	{
		String descript = 
		"            <!-- desc -->\r\n" + 
		"            <div class=\"content midlight\">\r\n" + 
		"                <div class=\"desc\">\r\n" + 
		desc +
		"                </div>\r\n" + 
		"                <div class=\"desc mark\">Version:<div class=\"marktext\">"+vers+"</div></div>\r\n" + 
		"                <div class=\"desc mark\">Author:<div class=\"marktext\">"+auth+"</div></div>\r\n" + 
		"            </div>\r\n";
		return descript;
	}
	
	// ============================================================
	//
	// ============================================================
	
	private String makePackageList()
	{
		if (exports.size() < 1) return "";
		
		String spackage = 
		"            <!-- package section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Packages</div>\r\n" + 
		"                <div class=\"header small yellow\">Exports</div>\r\n" + 
		makeExported() + 
		"            </div>\r\n";
		return spackage;
	}
	
	private String makeExported()
	{
		String exp = 
		"                <!-- exported -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Package</strong></div>\r\n" + 
		"                    </div>\r\n";
		
		for (Exported e : exports)
		{
			String export = 
			"                    <div class=\"rTableRow\">\r\n" + 
			"                        <div class=\"rTableCell\"><a href=\""+e.getFileName()+"\">"+e.getPackageName()+"</a></div>\r\n" + 
			"                    </div>\r\n";
			exp += export;
		}
		
		exp += 
		"                </div>\r\n";
		return exp;
	}
	
	// ============================================================
	//
	// ============================================================
	
	private String makeModuleList()
	{
		if (required.size() < 1) return "";
		
		String modlist = 
		"            <!-- module section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Modules</div>\r\n" + 
		"                <div class=\"header small yellow\">Requires</div>\r\n" + 
		makeRequired()+
		"            </div>\r\n";
		
		return modlist;
	}
	
	private String makeRequired()
	{
		String req = 
		"                <!-- required -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:20%;\"><strong>Modifier</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Module</strong></div>\r\n" + 
		"                    </div>\r\n";
		
		for (Required r : required)
		{
			req+=
			"                    <div class=\"rTableRow\">\r\n" + 
			"                        <div class=\"rTableCell\">"+r.getModifier()+"</div>\r\n" + 
			"                        <div class=\"rTableCell\">"+r.getName()+"</div>\r\n" + 
			"                    </div>\r\n";
		}
		
		req += 
		"                </div>\r\n";
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
		tf.addLine(makeDescription());
		tf.addLine(makePackageList());
		tf.addLine(makeModuleList());
		tf.addLine(makeEnd());
		tf.setEncoding("UTF-8");
		TextFileParser.write(path, tf);
	}
}
