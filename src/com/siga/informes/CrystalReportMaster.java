
/*
 * Created on 15-nov-2007
 *
 */

/**
 * @author danielc
 *
 */

package com.siga.informes;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crystaldecisions.reports.sdk.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.application.OpenReportOptions;
import com.crystaldecisions.sdk.occa.report.data.Field;
import com.crystaldecisions.sdk.occa.report.data.Fields;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class CrystalReportMaster 
{
	public static File generarPDF (String sFicheroReport, String sFicheroPDF, Hashtable datosASustituir)
	{
		return CrystalReportMaster.generarFichero(sFicheroReport, sFicheroPDF, datosASustituir, ReportExportFormat.PDF);
	}

	public static File generarPDF (String sFicheroReport, File sFicheroPDF, Hashtable datosASustituir) throws Exception {
		return CrystalReportMaster.generarFichero(sFicheroReport, sFicheroPDF, datosASustituir, ReportExportFormat.PDF);
	}

	public static File generarMExcel (String sFicheroReport, String sFicheroExcel, Hashtable datosASustituir)
	{
		return CrystalReportMaster.generarFichero(sFicheroReport, sFicheroExcel, datosASustituir, ReportExportFormat.MSExcel);
	}

	public static File generarMWord (String sFicheroReport, String sFicheroWord, Hashtable datosASustituir)
	{
		return CrystalReportMaster.generarFichero(sFicheroReport, sFicheroWord, datosASustituir, ReportExportFormat.MSWord);
	}

	public static File generarRTF (String sFicheroReport, String sFicheroRFT, Hashtable datosASustituir)
	{
		return CrystalReportMaster.generarFichero(sFicheroReport, sFicheroRFT, datosASustituir, ReportExportFormat.RTF);
	}

	private static File generarFichero (String sFicheroReport, File sFicheroSalida, Hashtable datos, ReportExportFormat formatoExportacion) throws Exception {
//		System.out.println("CrystalReport: Comienzo a generar fichero: \n   Entrada:" + sFicheroReport + "\n   salida: " + sFicheroSalida);

		File fichero=null;

		// Abrimos el report			
		ReportClientDocument docReport = new ReportClientDocument();			
		docReport.open(sFicheroReport, OpenReportOptions.openAsReadOnly.value());

		// Establecemos las varibles dinamicas
		if (!CrystalReportMaster.sustituirVariablesDinamicas (docReport, datos))
			throw new SIGAException ("No se pudo sustituir las variables dinamicas en el informe."); 

			// Establecemos los parametros de conexion con BD
		Hashtable conf = leerParametrosConfiguracion();
			
//			System.out.println("CrystalReport: usuario: " + UtilidadesHash.getString(conf, "USER") + " y password:" + UtilidadesHash.getString(conf, "PWD"));

		docReport.getDatabaseController().logon(UtilidadesHash.getString(conf, "USER"), UtilidadesHash.getString(conf, "PWD"));

			// Exportamos el report a PDF
		ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream)docReport.getPrintOutputController().export(formatoExportacion);
		CrystalReportMaster.writeToFileSystem(byteArrayInputStream, sFicheroSalida);
		byteArrayInputStream.close();

		// Cerramos el report
		docReport.close();

		return fichero;
	}
	
	private static File generarFichero (String sFicheroReport, String sFicheroSalida, Hashtable datos, ReportExportFormat formatoExportacion) {
//		System.out.println("CrystalReport: Comienzo a generar fichero: \n   Entrada:" + sFicheroReport + "\n   salida: " + sFicheroSalida);

		File fichero = null;
		try {
			// Abrimos el report			
			ReportClientDocument docReport = new ReportClientDocument();			
			docReport.open(sFicheroReport, OpenReportOptions.openAsReadOnly.value());

			// Establecemos las varibles dinamicas
			if (!CrystalReportMaster.sustituirVariablesDinamicas (docReport, datos))
				return null; 

			// Establecemos los parametros de conexion con BD
			Hashtable conf = leerParametrosConfiguracion();
			
//			System.out.println("CrystalReport: usuario: " + UtilidadesHash.getString(conf, "USER") + " y password:" + UtilidadesHash.getString(conf, "PWD"));

			docReport.getDatabaseController().logon(UtilidadesHash.getString(conf, "USER"), UtilidadesHash.getString(conf, "PWD"));

			// Exportamos el report a PDF
			ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream)docReport.getPrintOutputController().export(formatoExportacion);
			fichero = CrystalReportMaster.writeToFileSystem(byteArrayInputStream, sFicheroSalida);
			byteArrayInputStream.close();

			// Cerramos el report
			docReport.close();
		}
		catch(ReportSDKException ex) {
			ex.printStackTrace();
			fichero = null;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			fichero = null;
		}
	
		return fichero;
	}
	
	private static boolean sustituirVariablesDinamicas (ReportClientDocument docReport, Hashtable datos) 
	{
		try {
			if (docReport == null)
				return true;
			
			Fields parametros = docReport.getDataDefController().getDataDefinition().getParameterFields();
			
			if (parametros.size() > 0 &&  datos == null)
				return false;
			
			for (int i = 0; i < parametros.size(); i++) {
				Field parametro = (Field)parametros.get(i);
				String nombreParametro = parametro.getName();
				Object valor = datos.get(nombreParametro);
				docReport.getDataDefController().getParameterFieldController().setCurrentValue("", nombreParametro, valor);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static File writeToFileSystem(InputStream is, File file) throws Exception{
	    FileOutputStream fileOutputStream = null;
	    ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byte byteArray[] = new byte[is.available()];
	
			fileOutputStream = new FileOutputStream(file);
	
			byteArrayOutputStream = new ByteArrayOutputStream( is.available());
			int x = is.read(byteArray, 0, is.available());
	
			byteArrayOutputStream.write(byteArray, 0, x);
			byteArrayOutputStream.writeTo(fileOutputStream);
	
			is.close();
			byteArrayOutputStream.close();
			fileOutputStream.close();
			return file;
		} 
		catch (Exception e) {
			e.printStackTrace();
		} finally {
		    try {
		        is.close();
				byteArrayOutputStream.close();
				fileOutputStream.close();
		    } catch (Exception eee) {}
		}
		return null;
	}

	private static File writeToFileSystem(ByteArrayInputStream byteArrayInputStream, String sFicheroAGenerar) throws Exception 
	{
	    FileOutputStream fileOutputStream = null;
	    ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byte byteArray[] = new byte[byteArrayInputStream.available()];
	
			File file = new File(sFicheroAGenerar);
			fileOutputStream = new FileOutputStream(file);
	
			byteArrayOutputStream = new ByteArrayOutputStream( byteArrayInputStream.available());
			int x = byteArrayInputStream.read(byteArray, 0, byteArrayInputStream.available());
	
			byteArrayOutputStream.write(byteArray, 0, x);
			byteArrayOutputStream.writeTo(fileOutputStream);
	
			byteArrayInputStream.close();
			byteArrayOutputStream.close();
			fileOutputStream.close();
			return file;
		} 
		catch (Exception e) {
			e.printStackTrace();
		} finally {
		    try {
		        byteArrayInputStream.close();
				byteArrayOutputStream.close();
				fileOutputStream.close();
		    } catch (Exception eee) {}
		}
		return null;
	}

	private static Hashtable leerParametrosConfiguracion () 
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );
			DocumentBuilder builder = factory.newDocumentBuilder();
				   
//			File fichero = new File (ClsConstants.RES_DIR + ClsConstants.RES_PROP_DOMAIN + "/classes/CRConfig.xml");
//			if (!fichero.exists()) {
//				return null;
//			}
//			Document documento = builder.parse(fichero);
			
			InputStream is=SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.CRCONFIG);
			if (is==null)
				return null;
			Document documento=builder.parse(is);
			Node root = documento.getFirstChild();
			   
			Hashtable h = new Hashtable();
			String ruta1[] = {"Javaserver-configuration", "JDBC", "JDBCUserName"};
			Node nodo = findNodoXML(ruta1, root);
			if (nodo != null)
				UtilidadesHash.set(h, "USER", nodo.getNodeValue().trim());
			
			String ruta2[] = {"Javaserver-configuration", "JDBC", "JDBCUserPwd"};
			nodo = findNodoXML(ruta2, root);
			if (nodo != null) {
				String clave = nodo.getNodeValue().trim();
				UtilidadesHash.set(h, "PWD", clave);
			}
			return h;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Node findNodoXML (String s[], Node padre) 
	{
		if (s == null || s.length == 0) 
			return padre.getFirstChild();
		
		NodeList listaNodosHijos = padre.getChildNodes();
		for (int i=0; i < listaNodosHijos.getLength(); i++) {
			Node hijo = listaNodosHijos.item(i);
			if (hijo.getNodeName().equalsIgnoreCase(s[0])) {
				String a[] = new String[s.length-1];
				for (int k = 0; k < s.length-1; k++) a[k] = s[k+1];
				return findNodoXML (a, hijo);
			}
		}
		return null;
	}

	private static void printInfoConexion (ReportClientDocument doc, String entrada, String salida) 
	{
	    BufferedWriter writer = null;
		try {
			CrystalReportConexionInfoDisplay report_displayer1 = new CrystalReportConexionInfoDisplay(doc);
			StringBuffer result = new StringBuffer();
			
			//Begin a main table to display both reportA and reportB's properties in.
			result.append("<table width='100%' height='100%'>");
			result.append("<tr align='center' valign='top'>");
			result.append("<td>");
			result.append(report_displayer1.getTable(entrada)); //List the connection properties of Report 1.
			result.append("</td>");
			result.append("</tr>");	
			result.append("</table>");
					
			//Write the result to the output file.
			writer = new BufferedWriter(new FileWriter(salida+".html"));
			writer.write(result.toString());
			writer.close();
			
			System.out.println("CrystalReportMaster: Connection information obtained and successfully written to " + salida + ".html");			
		}
		catch(ReportSDKException ex) {
			ex.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();			
		} finally {
		    try {
		        writer.close();
		    } catch (Exception eee) {}
		}
	}
	

/*
 * 
 * //			{ // Prueba 1.1: OK --> CRConfig.xml con la base de desarrollo --> Cambiamos a local con "logon("uscgae2", "uscgae2")"
//			// Establecemos las varibles dinamicas
//			CrystalReportMaster.sustituirVariablesDinamicas (docReport, datos); 
//		}

//			{ // Prueba 2 NO FUNCIONA --> CRConfig.xml con la base de desarrollo --> Cambiamos a local
//				// Establecemos las varibles dinamicas
//				CrystalReportMaster.sustituirVariablesDinamicas (docReport, datos); 
//
//				connectDataBase(docReport);
//			}
			
			{ // EXTRA
			
//			printInfoConexion (docReport, sFicheroReport, sFicheroSalida+"_Antes");
			
			// Establecemos las varibles dinamicas
//			CrystalReportMaster.sustituirVariablesDinamicas (docReport, datos); 

//			refrescarDocumento(sFicheroReport);
			
			// Establecemos los parametros de conexion con BD
//			docReport.getDatabaseController().logon("uscgae2", "uscgae2");
			}

 * 	
	private static void connectDataBase (ReportClientDocument reportClientDoc) 
	{
		try {
			//Switch all tables on the main report.  See utility method below.
			switch_tables(reportClientDoc.getDatabaseController());
			
			//Perform the same operation against all tables in the subreport as well.
			IStrings subreportNames = reportClientDoc.getSubreportController().getSubreportNames();
			
			//Set the datasource for all the subreports.
			for (int i = 0; i < subreportNames.size(); i++ ) {
				ISubreportClientDocument subreportClientDoc = reportClientDoc.getSubreportController().getSubreport(subreportNames.getString(i));
				
				//Switch tables for each subreport in the report using the same connection information.  See utility
				//method below.
				switch_tables(subreportClientDoc.getDatabaseController());
			}	
		}
		catch(ReportSDKException ex) {	
			System.out.println(ex);
		}
		catch(Exception ex) {
			System.out.println(ex);			
		}
	}
		
	private static void switch_tables(DatabaseController databaseController) throws ReportSDKException 
	{
		final String DBUSERNAME = "uscgae2";
		final String DBPASSWORD = "uscgae2";
		final String SERVERNAME = "SIGADES"; 
		final String CONNECTION_STRING = "Use JDBC=b(true);Connection URL=s(jdbc:oracle:thin:@192.168.11.55:1521:SIGADES);" +
										 "Database Class Name=s(oracle.jdbc.driver.OracleDriver);JNDI Datasource Name=s();Server=s(SIGADES);" +
										 "User ID=s(uscgae2);Password=;Trusted_Connection=b(false);" +
										 "JDBC Connection String=s(!oracle.jdbc.driver.OracleDriver!jdbc:oracle:thin:{userid}/{password}@192.168.11.55:1521:SIGADES!ServerType=5!QuoteChar=\");" +
										 "Generic JDBC Driver Behavior=s(No)";

		final String URI = "!oracle.jdbc.driver.OracleDriver!jdbc:oracle:thin:{userid}/{password}@192.168.11.55:1521:SIGADES!ServerType=5!QuoteChar=\"";
		final String DATABASE_DLL = "crdb_jdbc.dll";
		
		
//			final String TABLE_NAME_QUALIFIER = "xtreme.dbo.";
//			final String DATABASE_NAME = "Xtreme";
		
		//Obtain collection of tables from this database controller.
		Tables tables = databaseController.getDatabase().getTables();

		//Set the datasource for all main report tables.
		for (int i = 0; i < tables.size(); i++) {

			ITable table = tables.getTable(i);

			//Keep existing name and alias.
			table.setName(table.getName());
			table.setAlias(table.getAlias());
							
			//Change properties that are different from the original datasource.
//			table.setQualifiedName(TABLE_NAME_QUALIFIER + table.getName());
			
			//Change connection information properties.
			IConnectionInfo connectionInfo = table.getConnectionInfo();
							
			//Set new table connection property attributes.
			PropertyBag propertyBag = new PropertyBag();
			
			//Overwrite any existing properties with updated values.
			propertyBag.put("Trusted_Connection", "false");
			propertyBag.put("Server Name", SERVERNAME); //Optional property.
			propertyBag.put("Connection String", CONNECTION_STRING);
//			propertyBag.put("Database Name", DATABASE_NAME);
			propertyBag.put("Service", SERVERNAME);
			propertyBag.put("Server Type", "JDBC (JNDI)");
			propertyBag.put("URI", URI);
			propertyBag.put("Use JDBC", "true");
			propertyBag.put("Database DLL", DATABASE_DLL);
					
			connectionInfo.setAttributes(propertyBag);
			
			//Set database username and password.
			//NOTE: Even if these the username and password properties don't change when switching databases, the 
			//database password is *not* saved in the report and must be set at runtime if the database is secured.  
			connectionInfo.setUserName(DBUSERNAME);
			connectionInfo.setPassword(DBPASSWORD);
			connectionInfo.setKind(ConnectionInfoKind.SQL);
							
			table.setConnectionInfo(connectionInfo);
			
			//Update old table in the report with the new table.
			databaseController.setTableLocation(table, tables.getTable(i));
			
		}
	}
	*/
}
