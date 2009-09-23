/*
 * Created on 16-ene-2008
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.siga.servlets;

import java.io.File;
import java.util.Hashtable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.aspose.words.License;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.GenParametrosAdm;
import com.siga.informes.CrystalReportMaster;
import com.siga.informes.MasterWords;

/**
 * @author danielc
 *
 */

//public class SIGASvlInicializacionesSecundarias extends HttpServlet 
//{
//	public void init() throws ServletException 
//	{
//	    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
//	    ClsLogging.writeFileLogWithoutSession("  Arrancando Inicializaciones Secundarias ", 3);
//
//	    this.inicializarCrystal();
//	    
//	    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
//	}
//  
//	public void destroy() 
//	{
//		super.destroy();
//	}
//}

public final class SIGASvlInicializacionesSecundarias implements ServletContextListener  
{
	public void contextInitialized(ServletContextEvent arg0) 
	{
	    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
	    ClsLogging.writeFileLogWithoutSession("  Arrancando Inicializaciones Secundarias ", 3);
	    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);

	    //this.inicializarCrystal();
	    this.inicializarWords();
	}

	private void inicializarCrystal() 
	{
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	    ClsLogging.writeFileLogWithoutSession("  <> CrystalReport: Inicializando ...", 3);
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);

	    String pathAplicacion = "";
	    try {
	    	UsrBean us = new UsrBean();
		    us.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
		    GenParametrosAdm adm = new GenParametrosAdm (us);
		    pathAplicacion = adm.getValor("0", "CEN", "PATH_APP", ClsConstants.RES_DIR);
	    }
	    catch (Exception e) {
	    	pathAplicacion = ClsConstants.RES_DIR;
		}
	    
	    String fIn  = pathAplicacion + ClsConstants.FILE_SEP + "html" + ClsConstants.FILE_SEP +"jsp"+ ClsConstants.FILE_SEP + "general" + ClsConstants.FILE_SEP + "inicializacionCrystal.rpt", 
			   fOut = pathAplicacion + ClsConstants.FILE_SEP + "html" + ClsConstants.FILE_SEP +"jsp"+ ClsConstants.FILE_SEP + "general" + ClsConstants.FILE_SEP + "l23p57r22a15d31.pdf";
	    
	    Hashtable parametros = new Hashtable();
	    UtilidadesHash.set(parametros, "idlenguaje", "1");
	    
	    File f = CrystalReportMaster.generarPDF(fIn, fOut, parametros);
	    if (f == null) {    
		    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	    	ClsLogging.writeFileLogWithoutSession("  <> CrystalReport: Error en la inicializacion", 3);
		    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
		    return;
	    }
	    if (f.exists()) f.delete();
	    
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	    ClsLogging.writeFileLogWithoutSession("  <> CrystalReport: Inicializacion Completada", 3);
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	}
	
	private void inicializarWords() 
	{
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	    ClsLogging.writeFileLogWithoutSession("  <> Aspose.Words: Inicializando ...", 3);
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);

	    
	    License license = new License();
	    try {
		    UsrBean us = new UsrBean();
		    us.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
		    GenParametrosAdm adm = new GenParametrosAdm (us);
		    String pathAplicacion = adm.getValor("0", "CEN", "PATH_APP", ClsConstants.RES_DIR);
	        license.setLicense(pathAplicacion+ File.separator + "WEB-INF"+ File.separator + "lib"+ File.separator + "Aspose.Words.lic");
	    } catch (Exception e) {
		    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	        ClsLogging.writeFileLogWithoutSession("Error en licencia de Aspose ", 3);
	        e.printStackTrace();
		    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	    }
	    


	    MasterWords.precargaInformes();
		
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	    ClsLogging.writeFileLogWithoutSession("  <> Aspose.Words: Inicializacion Completada", 3);
	    ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {}
}

