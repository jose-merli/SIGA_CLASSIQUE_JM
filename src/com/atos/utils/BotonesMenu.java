/*
 * Created on 27-feb-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.atos.utils;

import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BotonesMenu {

    
	//Obtenemos el path para el boton Cerrar Sesion del menu
	public static String getPathCerrarSesion(String idInstitucion) throws SIGAException{
		String path = "";
		if (idInstitucion==null) idInstitucion="2000";
		
		try {
		    GenParametrosAdm paramAdm = new GenParametrosAdm(UsrBean.UsrBeanAutomatico(idInstitucion));
		    path = paramAdm.getValor(idInstitucion, "GEN", "PATH_INICIO_SESION", "");
			
		} catch (Exception e){
			path = "";
		}		
		return path;
	}
	
	//Obtenemos el path para el boton Ayuda del menu
	public static String getPathAyuda(String idInstitucion){
		String path = "";
		if (idInstitucion==null) idInstitucion="2000";
		
		try {
			GenParametrosAdm paramAdm = new GenParametrosAdm(UsrBean.UsrBeanAutomatico(idInstitucion));
			path = paramAdm.getValor("" + idInstitucion, "GEN", "PATH_AYUDA", "");
		} catch (Exception e){
			path = "";
		}		
		return path;
	}
	
	//Obtenemos el path para el boton Versiones del menu
	public static String getPathVersiones(String idInstitucion){
		String path = "";
		if (idInstitucion==null) idInstitucion="2000";
		
		try {
			GenParametrosAdm paramAdm = new GenParametrosAdm(UsrBean.UsrBeanAutomatico(idInstitucion));
			path = paramAdm.getValor("" + idInstitucion, "GEN", "PATH_VERSIONES", "");
		} catch (Exception e){
			path = "";
		}		
		return path;
	}
	
}
