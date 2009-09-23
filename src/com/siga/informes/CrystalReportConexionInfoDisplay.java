package com.siga.informes;

import com.crystaldecisions.reports.sdk.*;
import com.crystaldecisions.sdk.occa.report.data.*;
import com.crystaldecisions.sdk.occa.report.lib.*;

/*********************** class ConnInfoDisplayer ***********************
 * 
 * Utility class that is used to display all the Connection Info properties from the first table
 * of the given report in a nice HTML based table.
 *   
 ***********************************************************************/
class CrystalReportConexionInfoDisplay {
	
	private ReportClientDocument reportClientDoc;
	
	/************************************************************************
	 * 
	 * Constructor - Save the reportClientDocument to be displayed
	 *               
	 ***********************************************************************/	
	CrystalReportConexionInfoDisplay(ReportClientDocument reportClientDoc)
	{
		this.reportClientDoc = reportClientDoc;
	}

	/************************************************************************
	 * 
	 * Return the report name section of the table for the main report.
	 *               
	 ***********************************************************************/
	private String report_name_section(ReportClientDocument reportClientDoc, String theReportName) throws ReportSDKException {	
		String reportName = theReportName;
		String result = "";
		result += "<tr>";
		result += "<td colspan=2 align=center bgcolor='#0C0C6E'><strong><font color='white'>Main Report: " + reportName + "</font></strong></td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td colspan=2 bgcolor='#CFD0E2'>&nbsp;</td>";
		result += "</tr>";	
		return result;
	}

	/************************************************************************
	 * 
	 * Return the report name section of the table for the subreport.
	 *               
	 ***********************************************************************/
	private String table_properties_section(ITable table) {
		String result = "";
		result += "<tr>";
		result += "<td colspan=2 bgcolor='#0C0C6E'><strong><font color='white'>Table Properties</font></strong></td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td bgcolor='#CFD0E2'>Name</td><td bgcolor='#F1EFE2'>" + table.getName() + "</td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td bgcolor='#CFD0E2'>Alias</td><td bgcolor='#F1EFE2'>" + table.getAlias() + "</td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td bgcolor='#CFD0E2'>Qualified Name</td><td bgcolor='#F1EFE2'>" + table.getQualifiedName() + "</td>";
		result += "</tr>";
		return result;
	}

	/************************************************************************
	 * 
	 * Return the connection infos name section.
	 *               
	 ***********************************************************************/
	private String conn_info_properties_section(IConnectionInfo connectionInfo) {
		
		String result = "";
		//list out the general Connection properties
		result += "<tr>";
		result += "<td colspan=2 bgcolor='#0C0C6E'><strong><font color='white'>ConnectionInfo Properties</font></strong></td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td bgcolor='#CFD0E2'>User Name</td><td bgcolor='#F1EFE2'>" + connectionInfo.getUserName() + "</td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td bgcolor='#CFD0E2'>Password</td><td bgcolor='#F1EFE2'>" + connectionInfo.getPassword() + "</td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td bgcolor='#CFD0E2'>Kind</td><td bgcolor='#F1EFE2'>" + connectionInfo.getKind() + "</td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td colspan=2 bgcolor='#0C0C6E'><strong><font color='white'>ConnectionInfo Atttribute Properties</font></strong></td>";
		result += "</tr>";
		return result;
		
	}
	
	/************************************************************************
	 * 
	 * Return the property bag infos name section.  This function will
	 * recursively go through any property bags that are contained in this
	 * property bag.
	 *               
	 ***********************************************************************/
	private String property_bag_section(PropertyBag oPropertyBag, String indent) {
		
		int i;
	    IStrings prop_ids = null;
	    String result = "";

        prop_ids = oPropertyBag.getPropertyIDs();

	    String prop_name = "";
	    
        for (i=0;i < prop_ids.size(); i++) {
        	
        	if ((oPropertyBag.get(prop_ids.get(i))).getClass() != oPropertyBag.getClass()) {

        		prop_name = (String)prop_ids.getString(i);
				String prop_val = (String)oPropertyBag.getStringValue(prop_ids.get(i));
				result += "<tr><td bgcolor='#CFD0E2'>" + prop_name + "</td><td bgcolor='#F1EFE2'>" + prop_val + "</td></tr>";
				  
			} else if ((oPropertyBag.get(prop_ids.get(i))).getClass() == oPropertyBag.getClass() ) {

				prop_name = (String)prop_ids.get(i);
				result += "<td colspan=2 bgcolor='#CFD0E2'><i>" + prop_name + " Properties</i></td>";
                                result += this.property_bag_section ((PropertyBag)oPropertyBag.get(prop_ids.get(i)), "&nbsp&nbsp&nbsp&nbsp&nbsp");
			} else {
				  result += " ***** " + (oPropertyBag.get(prop_ids.get(i))).getClass();
			}
        	
	    }
        
        return result;
        
	}
	
	/************************************************************************
	 * 
	 * Return the section that includes the reports connection including 
	 * the Connection Infos direct properties, and any property bags it 
	 * contains.
	 *               
	 ***********************************************************************/
	private String connection_section(IConnectionInfo connectionInfo) {
		
		String connInfoTable = "";

		connInfoTable += conn_info_properties_section(connectionInfo);		
		
		PropertyBag oPropertyBag = connectionInfo.getAttributes();
		connInfoTable += property_bag_section(oPropertyBag, "");

		return connInfoTable;
		
	}
	
	/************************************************************************
	 * 
	 * Return the table of Connection Properties as a String to be displayed.
	 *               
	 ***********************************************************************/
	private String table_section(ITable table) throws ReportSDKException {
		
		String connInfoTable = "";
	
		connInfoTable += table_properties_section(table);

		IConnectionInfo connectionInfo = (IConnectionInfo)table.getConnectionInfo();
		connInfoTable += connection_section(connectionInfo);
		
		return connInfoTable;
		
	}

	/************************************************************************
	 * 
	 * Return the report name section of the table for the subreport.
	 *               
	 ***********************************************************************/
	private String subreport_name_section(String subreportName) throws ReportSDKException
	{	
		String result = "";
		result += "<tr>";
		result += "<td colspan=2 bgcolor='#CFD0E2'>&nbsp;</td>";
		result += "</tr>";	
		result += "<tr>";
		result += "<td colspan=2 align=center bgcolor='#0C0C6E'><strong><font color='white'>" + "Subreport: " + subreportName + "</font></strong></td>";
		result += "</tr>";
		result += "<tr>";
		result += "<td colspan=2 bgcolor='#CFD0E2'>&nbsp;</td>";
		result += "</tr>";	
		return result;
	}
	
	/************************************************************************
	 * 
	 * Return the subreport section.  This includes the entire subreport
	 * and should be called once for each subreport that needs to be 
	 * displayed.
	 *               
	 ***********************************************************************/
	private String subreport_section(ISubreportClientDocument subreportClientDoc, String subreportName) throws ReportSDKException
	{
		String result = "";
		//Get the first table from the report.  We will display it's connection properties.
		result += "<table border=0 cellspacing=1>";
		result += subreport_name_section(subreportName);
	
		for (int i = 0; i < reportClientDoc.getDatabaseController().getDatabase().getTables().size(); i++)
		{
			Table table = (Table)reportClientDoc.getDatabaseController().getDatabase().getTables().getTable(i);
			result += table_section(table);
		}

		result += "</table>";
		return result;
		
	}

	/************************************************************************
	 * 
	 * Return the main report section.  There should be only one main report
	 * section per ConnInfoDisplayer.
	 *               
	 ***********************************************************************/
	private String mainreport_section(ReportClientDocument reportClientDoc, String theReportName) throws ReportSDKException {
	
		String reportName = theReportName;
		String result = "";
		//Get the first table from the report.  We will display it's connection properties.

		result += "<table border=0 cellspacing=1>";
		result += report_name_section(reportClientDoc, reportName);	
		
		for (int i = 0; i < reportClientDoc.getDatabaseController().getDatabase().getTables().size(); i++) {
			ITable table = (ITable)reportClientDoc.getDatabaseController().getDatabase().getTables().getTable(i);
			result += table_section(table);
		}
		
		result += "</table>";
		
		return result;
		
	}

	/************************************************************************
	 * 
	 * Return an HTML based table of all the connection properties in the
	 * reportClientDoc that was passed in to the contructor.
	 *               
	 ***********************************************************************/	
	public String getTable(String theReportName) throws ReportSDKException
	{
		String reportName = theReportName;
		String result = "";
	
		result += mainreport_section(reportClientDoc, reportName);

		IStrings subreport_names = reportClientDoc.getSubreportController().getSubreportNames();

		for (int i = 0; i < subreport_names.size(); i++) {
			String subreport_name = subreport_names.getString(i);
			result += subreport_section(reportClientDoc.getSubreportController().getSubreport(subreport_name), subreport_name);
		}
				
		return result;
	}

} //End of ConnInfoDisplayer utility class.
