package com.nullpointerworks.generate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.generate.clazz.Constructor;
import com.nullpointerworks.generate.clazz.Field;
import com.nullpointerworks.generate.clazz.Method;
import com.nullpointerworks.generate.enums.EnumField;
import com.nullpointerworks.generate.func.Parameter;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class FileMaker
{
	private String sourceModuleFile = "";
	private String sourceModule = "";
	private String sourcePackageFile = "";
	private String sourcePackage = "";
	private String fileName = "";
	private Java type;
	private String desc = "";
	private List<Field> fields;
	private List<EnumField> enumfields;
	private List<Method> methods;
	private List<Constructor> constr;
	private List<Additional> adds;
	
	public FileMaker()
	{
		fields = new ArrayList<Field>();
		enumfields = new ArrayList<EnumField>();
		constr = new ArrayList<Constructor>();
		methods = new ArrayList<Method>();
		adds = new ArrayList<Additional>();
	}
	
	public void setSourceModule(String fn, String n) 
	{
		sourceModuleFile = fn;
		sourceModule=n;
	}
	
	public void setSourcePackage(String fn, String n) 
	{
		sourcePackageFile = fn;
		sourcePackage=n;
	}
	
	public void setFileName(String fn) 
	{
		fileName = fn;
	}
	
	public void setFileType(Java typ) 
	{
		type=typ;
	}
	
	public void setSince(String s) 
	{
		setAdditional("Since",s);
	}
	
	public void setVersion(String r) 
	{
		setAdditional("Version",r);
	}
	
	public void setAuthor(String a) 
	{
		setAdditional("Author",a);
	}
	
	public void setAdditional(String t, String s) 
	{
		adds.add( new Additional(t,s) );
	}
	
	public void setDescription(String d) 
	{
		desc=d;
	}

	public void addField(EnumField ef)
	{
		enumfields.add(ef);
	}
	
	public void addField(Field f)
	{
		fields.add(f);
	}
	
	public void addConstructor(Constructor c)
	{
		constr.add(c);
	}

	public void addMethod(Method m)
	{
		methods.add(m);
	}
	
	private boolean hasFieldModifier(List<Field> fields)
	{
		for (Field m : fields)
			if (!m.getModifier().equals("")) return true;
		return false;
	}
	
	private boolean hasConstructorModifier(List<Constructor> constr)
	{
		for (Constructor m : constr)
			if (!m.getModifier().equals("")) return true;
		return false;
	}
	
	private boolean hasMethodModifier(List<Method> methods)
	{
		for (Method m : methods)
			if (!m.getModifier().equals("")) return true;
		return false;
	}
	
	// ============================================================
	//
	// ============================================================
	
	public void save(String path) throws IOException
	{
		TextFile tf = new TextFile();
		tf.addLine(makeHead());
		tf.addLine(makeDescription());
		
		tf.addLine(makeEnumFieldSummary());
		tf.addLine(makeEnumFieldDetails());
		
		tf.addLine(makeFieldSummary());
		tf.addLine(makeFieldDetails());
		
		tf.addLine(makeConstructorSummary());
		tf.addLine(makeConstructorDetails());
		
		tf.addLine(makeMethodSummary());
		tf.addLine(makeMethodDetails());
		
		tf.addLine(makeEnd());
		tf.setEncoding("UTF-8");
		TextFileParser.write(path, tf);
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
		"        <title>"+fileName+" - API Reference - Nullpointer Works</title>\r\n" + 
		"        <meta charset=\"utf-8\"/>\r\n" + 
		"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\r\n" + 
		"    	<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\"/>\r\n" + 
		"    	<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/layout.css\"/>\r\n" + 
		"    </head>\r\n" + 
		"    <body>\r\n" + 
		"        <div class=\"container\">\r\n" + 
		"            <div class=\"header small vdark petrol-font\">\r\n" + 
		"                Module <a href=\""+sourceModuleFile+"\">"+sourceModule+"</a>\r\n" + 
		"            </div>\r\n" + 
		"            <div class=\"header small vdark petrol-font\">\r\n" + 
		"                Package <a href=\""+sourcePackageFile+"\">"+sourcePackage+"</a>\r\n" + 
		"            </div>\r\n" + 
		"            <div class=\"header vdark petrol-font\">\r\n" + 
		"                "+type.getString()+" "+fileName+"\r\n" + 
		"            </div>\r\n";
		
		return head;
	}
	
	private String makeDescription()
	{
		String descript = 
		"            <!-- desc -->\r\n" + 
		"            <div class=\"content midlight\">\r\n" + 
		"                <div class=\"desc\">\r\n" + 
		desc + "\r\n" +
		"                </div>\r\n";
		
		for (Additional a : adds)
		{
			descript +=
			"                <div class=\"desc mark\">"+a.getType()+":<div class=\"marktext\">"+a.getText()+"</div></div>\r\n";
		}
		
		descript +=
		"            </div>\r\n";
		return descript;
	}

	// ============================================================
	// TODO Enum Field generation
	// ============================================================
	
	private String makeEnumFieldSummary()
	{
		if (enumfields.size() < 1) return "";
		
		String summ = 
		"            <!-- enum field summary section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Enumeration Summary</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:100%;\"><strong>Field</strong></div>\r\n" + 
		"                    </div>\r\n";
		summ += makeEnumFieldSummaryList();
		summ +=
		"                </div>\r\n" + 
		"            </div>\r\n";
		return summ;
	}
	
	private String makeEnumFieldSummaryList()
	{
		String summ = "";
		
		for (EnumField f : enumfields)
		{
			String field = 		
			"                    <div class=\"rTableRow\">\r\n" + 
			"                        <div class=\"rTableCell\">"+f.getName()+"</div>\r\n" + 
			"                    </div>\r\n";
			summ+=field;
		}
		return summ;
	}
	
	// ============================================================

	private String makeEnumFieldDetails()
	{
		if (enumfields.size() < 1) return "";
		
		String details = 
		"            <!-- field details section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Field Detail</div>\r\n";
		details += makeEnumFieldDetailsList();
		details +=
		"            </div>\r\n";
		return details;
	}
	
	private String makeEnumFieldDetailsList()
	{
		String summ = "";
		
		for (int i=0,l=enumfields.size(); i<l; i++)
		{
			Field f = enumfields.get(i);
			String details = 		
			"                <div class=\"header small blue "+ ( (i!=0)?"spacer":"" ) +"\">"+f.getName()+"</div>\r\n" + 
			"                <div class=\"content midlight\">\r\n" + 
			"                    <div class=\"desc\">\r\n" + 
			"                        " + f.getDescription() + "\r\n" +
			"                    </div>\r\n";
			
			List<Additional> adds = f.getAdditionals();
			if (adds.size() > 0)
			{
				details += "                    <div class=\"desc\">\r\n";
				for (Additional a : adds)
				{
					details +=
					"                        <div class=\"desc mark\">"+a.getType()+":<div class=\"marktext\">"+a.getText()+"</div></div>\r\n";;
				}
				details += "                    </div>\r\n";
			}
			
			details += "                </div>\r\n";
			summ += details;
		}
		return summ;
	}
	
	// ============================================================
	// TODO Field generation
	// ============================================================
	
	private String makeFieldSummary()
	{
		if (fields.size() < 1) return "";
		boolean hasModifier = hasFieldModifier(fields);
		
		String summ = 
		"            <!-- field summary section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Field Summary</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n";
		
		if (hasModifier)
		{
			summ +=
			"                        <div class=\"rTableHead\" style=\"width:15%;\"><strong>Modifier</strong></div>\r\n";
		}
		
		summ +=
		"                        <div class=\"rTableHead\" style=\"width:15%;\"><strong>Type</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:40%;\"><strong>Field</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Value</strong></div>\r\n" + 
		"                    </div>\r\n";
		summ += makeFieldSummaryList();
		summ +=
		"                </div>\r\n" + 
		"            </div>\r\n";
		return summ;
	}
	
	private String makeFieldSummaryList()
	{
		String summ = "";
		boolean hasModifier = hasFieldModifier(fields);
		
		for (Field f : fields)
		{
			String field = 		
			"                    <div class=\"rTableRow\">\r\n";
			
			if (hasModifier)
			{
				field += "                        <div class=\"rTableCell\">"+f.getModifier()+"</div>\r\n";
			}
			
			field += 
			"                        <div class=\"rTableCell\">"+f.getType()+"</div>\r\n" + 
			"                        <div class=\"rTableCell\">"+f.getName()+"</div>\r\n" + 
			"                        <div class=\"rTableCell\">"+f.getValue()+"</div>\r\n" + 
			"                    </div>\r\n";
			summ+=field;
		}
		return summ;
	}
	
	// ============================================================

	private String makeFieldDetails()
	{
		if (fields.size() < 1) return "";
		
		String details = 
		"            <!-- field details section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Field Detail</div>\r\n";
		details += makeFieldDetailsList();
		details +=
		"            </div>\r\n";
		return details;
	}
	
	private String makeFieldDetailsList()
	{
		String summ = "";
		boolean hasModifier = hasFieldModifier(fields);
		
		for (int i=0,l=fields.size(); i<l; i++)
		{
			Field f = fields.get(i);
			String details = 		
			"                <div class=\"header small blue "+ ( (i!=0)?"spacer":"" ) +"\">"+( hasModifier?f.getModifier()+" ":"" )+f.getType()+" "+f.getName()+"</div>\r\n" + 
			"                <div class=\"content midlight\">\r\n" + 
			"                    <div class=\"desc\">\r\n" + 
			"                        " + f.getDescription() + "\r\n" +
			"                    </div>\r\n";
			
			List<Additional> adds = f.getAdditionals();
			if (adds.size() > 0)
			{
				details += "                    <div class=\"desc\">\r\n";
				for (Additional a : adds)
				{
					details +=
					"                        <div class=\"desc mark\">"+a.getType()+":<div class=\"marktext\">"+a.getText()+"</div></div>\r\n";;
				}
				details += "                    </div>\r\n";
			}
			
			details += "                </div>\r\n";
			summ += details;
		}
		return summ;
	}
	
	// ============================================================
	// TODO Constructor generation
	// ============================================================
	
	private String makeConstructorSummary()
	{
		if (constr.size() < 1) return "";
		String summ = 
		"            <!-- method summary section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Constructor Summary</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:15%;\"><strong>Constructor</strong></div>\r\n" + 
		"                    </div>\r\n";
		summ += makeConstructorSummaryList();
		summ +=
		"                </div>\r\n" + 
		"            </div>\r\n";
		return summ;
	}
	
	private String makeConstructorSummaryList()
	{
		String summ = "";
		for (Constructor m : constr)
		{
			String method = 		
			"                    <div class=\"rTableRow\">\r\n" + 
			"                        <div class=\"rTableCell\">"+m.getSimpleName()+"</div>\r\n" + 
			"                    </div>\r\n";
			summ += method;
		}
		return summ;
	}
	
	// ============================================================
	
	private String makeConstructorDetails()
	{
		if (constr.size() < 1) return "";
		
		String details = 
		"            <!-- method details section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Constructor Detail</div>\r\n";
		details += makeConstructorDetailsList();
		details +=
		"            </div>\r\n";
		return details;
	}
	
	private String makeConstructorDetailsList()
	{
		boolean hasModifier = hasConstructorModifier(constr);
		String summ = "";
		for (int i=0,l=constr.size(); i<l; i++)
		{
			Constructor m = constr.get(i);
			
			String details = 		
			"                <div class=\"header small blue "+ ( (i!=0)?"spacer":"" ) +"\">"+( hasModifier?m.getModifier()+" ":"" )+m.getComplexName()+"</div>\r\n" + 
			"                <div class=\"content midlight\">\r\n" + 
			"                    <div class=\"desc\">\r\n" + 
			"                        " + m.getDescription() + "\r\n" +
			"                    </div>\r\n";
			
			List<Parameter> params = m.getParameters();
			List<Additional> adds = m.getAdditionals();
			if (params.size() + adds.size() > 0)
			{
				details += "                    <div class=\"desc\">\r\n";
				
				// parameters
				if (params.size() > 0)
				{
					details += 
					"                        <div class=\"desc mark\">Parameters:";
					
					for (Parameter p : params)
					{
						details +=
						"<div class=\"marktext\">"+p.getName()+" - "+p.getDescription()+"</div>\r\n";
					}
					details += "</div>\r\n";
				}
				
				// additional information, version, since, return, etc.
				if (adds.size() > 0)
				{
					for (Additional a : adds)
					{
						details +=
						"                        <div class=\"desc mark\">"+a.getType()+":<div class=\"marktext\">"+a.getText()+"</div></div>\r\n";
					}
				}

				details += "                    </div>\r\n";
			}
			
			details += "                </div>\r\n";
			summ += details;
		}
		return summ;
	}
	
	// ============================================================
	// TODO Method generation
	// ============================================================
	
	private String makeMethodSummary()
	{
		if (methods.size() < 1) return "";
		boolean hasModifier = hasMethodModifier(methods);
		
		String summ = 
		"            <!-- method summary section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Method Summary</div>\r\n" + 
		"                <div class=\"header small yellow\">Summary</div>\r\n" + 
		"                <!-- summary -->\r\n" + 
		"                <div class=\"rTable\">\r\n" + 
		"                    <div class=\"rTableRow\">\r\n";
		
		if (hasModifier)
		summ += "                        <div class=\"rTableHead\" style=\"width:15%;\"><strong>Modifier</strong></div>\r\n";
		
		summ +=
		"                        <div class=\"rTableHead\" style=\"width:15%;\"><strong>Type</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\" style=\"width:15%;\"><strong>Method</strong></div>\r\n" + 
		"                        <div class=\"rTableHead\"><strong>Returns</strong></div>\r\n" + 
		"                    </div>\r\n";
		summ += makeMethodSummaryList();
		summ +=
		"                </div>\r\n" + 
		"            </div>\r\n";
		return summ;
	}
	
	private String makeMethodSummaryList()
	{
		boolean hasModifier = hasMethodModifier(methods);
		String summ = "";
		for (Method m : methods)
		{
			String method = 		
			"                    <div class=\"rTableRow\">\r\n";
			
			if (hasModifier)
			method += 
			"                        <div class=\"rTableCell\">"+m.getModifier()+"</div>\r\n";
			
			method += 
			"                        <div class=\"rTableCell\">"+m.getType()+"</div>\r\n" + 
			"                        <div class=\"rTableCell\">"+m.getSimpleName()+"</div>\r\n" + 
			"                        <div class=\"rTableCell\">"+m.getReturns()+"</div>\r\n" + 
			"                    </div>\r\n";
			summ += method;
		}
		return summ;
	}
	
	// ============================================================
	
	private String makeMethodDetails()
	{
		if (methods.size() < 1) return "";
		
		String details = 
		"            <!-- method details section -->\r\n" + 
		"            <div class=\"section dark\">\r\n" + 
		"                <div class=\"sectiontitle\">Method Detail</div>\r\n";
		details += makeMethodDetailsList();
		details +=
		"            </div>\r\n";
		return details;
	}
	
	private String makeMethodDetailsList()
	{
		boolean hasModifier = hasMethodModifier(methods);
		String summ = "";
		for (int i=0,l=methods.size(); i<l; i++)
		{
			Method m = methods.get(i);
			
			String details = 		
			"                <div class=\"header small blue "+ ( (i!=0)?"spacer":"" ) +"\">"+( hasModifier?m.getModifier()+" ":"" )+m.getType()+" "+m.getComplexName()+"</div>\r\n" + 
			"                <div class=\"content midlight\">\r\n" + 
			"                    <div class=\"desc\">\r\n" + 
			"                        " + m.getDescription() + "\r\n" +
			"                    </div>\r\n";
			
			List<Parameter> params = m.getParameters();
			List<Additional> adds = m.getAdditionals();
			if (params.size() + adds.size() > 0)
			{
				details += "                    <div class=\"desc\">\r\n";
				
				// parameters
				if (params.size() > 0)
				{
					details += 
					"                        <div class=\"desc mark\">Parameters:";
					
					for (Parameter p : params)
					{
						details +=
						"<div class=\"marktext\">"+p.getName()+" - "+p.getDescription()+"</div></div>\r\n";
					}
					
				}
				
				// additional information, version, since, return, etc.
				if (adds.size() > 0)
				{
					for (Additional a : adds)
					{
						details +=
						"                        <div class=\"desc mark\">"+a.getType()+":<div class=\"marktext\">"+a.getText()+"</div></div>\r\n";
					}
				}

				details += "                    </div>\r\n";
			}
			
			details += "                </div>\r\n";
			summ += details;
		}
		return summ;
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
}
