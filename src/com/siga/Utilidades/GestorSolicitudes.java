/*
 * Created on 14-feb-2005
 *
 */
package com.siga.Utilidades;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsGestionAutomaticaLog;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.beans.CenSolModiFacturacionServicioAdm;
import com.siga.beans.CenSoliModiDireccionesAdm;
import com.siga.beans.CenSolicModiCuentasAdm;
import com.siga.beans.CenSolicitModifDatosBasicosAdm;
import com.siga.beans.CenSolicitudModificacionCVAdm;
import com.siga.beans.CenSolModiFacturacionServicioBean;
import com.siga.beans.CenSoliModiDireccionesBean;
import com.siga.beans.CenSolicModiCuentasBean;
import com.siga.beans.CenSolicitModifDatosBasicosBean;
import com.siga.beans.CenSolicitudModificacionCVBean;
import com.siga.beans.ExpSolicitudBorradoAdm;
import com.siga.beans.ExpSolicitudBorradoBean;

/**
 * @author miguel.villegas
 */

/**
 * Clase GestorSolicitudes
 * <BR>
 * Realiza automaticamente la gestion de solicitudes
 */

public class GestorSolicitudes 
{

	/** 
	 * Gestiona automaticamente todas las solicitudes pendientes de una determinada institucion
	 * @param  institucion - identificador institucion
	 * @param  usuario - userBean involucrado 
	 * @return  boolean - si se ha llevado a cabo la operacion  
	 */	
	public boolean GestionAutomaticaSolicitudes (Integer institucion, String idioma) throws ClsExceptions {
		
		boolean resultado=true;
		boolean correcto;
		Vector vector=new Vector();
		Enumeration listaSolicitudes;
		Hashtable solicitud = new Hashtable();
		
		UsrBean userBean = UsrBean.UsrBeanAutomatico(institucion.toString());
		userBean.setLanguage(idioma);
		
		UserTransaction tx = null;
		
		try{			
			
			// Comienzo control de transacciones
			tx = userBean.getTransactionPesada();
			
			// Gestion de solicitudes de DATOS GENERALES 
			CenSolicitModifDatosBasicosAdm adminDB = new CenSolicitModifDatosBasicosAdm(userBean);
			vector=adminDB.getSolicitudes(institucion.toString(),String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE),"","");
			listaSolicitudes=vector.elements();
			while(listaSolicitudes.hasMoreElements()){
				try {
					solicitud = ((Row)listaSolicitudes.nextElement()).getRow();
					tx.begin();					
					Date ini = new Date();
					
					correcto=adminDB.procesarSolicitud((String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO), idioma);
					
					Date fin = new Date();
					// Control de transacciones largas
					if ((fin.getTime()-ini.getTime())>3000) {
					    Date dat = Calendar.getInstance().getTime();
					    SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
					    String fecha = sdfLong.format(dat);
					    ClsLogging.writeFileLog(fecha + ",==> SIGA: Control de tiempo de transacciones (>3 seg.),Proceso de cola letrados,"+userBean.getLocation()+",automatico,"+new Long((fin.getTime()-ini.getTime())).toString(),10);
					}
					if (correcto){							
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS GENERALES","Solicitud procesada");
						tx.commit();
					}
					else{
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS GENERALES","Solicitud no procesada");
						tx.rollback();
					}
				} catch (Exception e) {
					ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS GENERALES","Solicitud no procesada");
					try {
						tx.rollback();
					} catch (Exception ee) {
						ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> Realizando rollback ",ee,3);
					}
				}
			}	
			
			// Gestion de solicitudes de DATOS BANCARIOS
			CenSoliModiDireccionesAdm adminDir = new CenSoliModiDireccionesAdm(userBean); 
			vector=adminDir.getSolicitudes(institucion.toString(),String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE),"","");
			listaSolicitudes=vector.elements();
			while(listaSolicitudes.hasMoreElements()){
				try {
					solicitud = ((Row)listaSolicitudes.nextElement()).getRow();
					tx.begin();					
					correcto=adminDir.procesarSolicitud((String)solicitud.get(CenSoliModiDireccionesBean.C_IDSOLICITUD), idioma);
					if (correcto){							
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS BANCARIOS","Solicitud procesada");
						tx.commit();
					}
					else{
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS BANCARIOS","Solicitud no procesada");
						tx.rollback();
					}
				} catch (Exception e) {
					ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS BANCARIOS","Solicitud no procesada");
					try {
						tx.rollback();
					} catch (Exception ee) {
						ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> Realizando rollback ",ee,3);
					}
				}
			}
			
			// Gestion de solicitudes de datos sobre CV
			CenSolicModiCuentasAdm adminCB = new CenSolicModiCuentasAdm(userBean);
			vector=adminCB.getSolicitudes(institucion.toString(),String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE),"","");
			listaSolicitudes=vector.elements();
			while(listaSolicitudes.hasMoreElements()){
				try {
					solicitud = ((Row)listaSolicitudes.nextElement()).getRow();
					tx.begin();					
					correcto=adminCB.procesarSolicitud((String)solicitud.get(CenSolicModiCuentasBean.C_IDSOLICITUD),new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO), idioma);
					if (correcto){							
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS CV","Solicitud procesada");
						tx.commit();
					}
					else{
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS CV","Solicitud no procesada");
						tx.rollback();
					}
				} catch (Exception e) {
					ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS CV","Solicitud no procesada");
					try {
						tx.rollback();
					} catch (Exception ee) {
						ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> Realizando rollback ",ee,3);
					}
				}
			}
			
			// Gestion de solicitudes de datos sobre DIRECCIONES
			CenSolicitudModificacionCVAdm adminCV = new CenSolicitudModificacionCVAdm(userBean);
			vector=adminCV.getSolicitudes(institucion.toString(),String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE),"","");
			listaSolicitudes=vector.elements();
			while(listaSolicitudes.hasMoreElements()){
				try {
					solicitud = ((Row)listaSolicitudes.nextElement()).getRow();
					tx.begin();					
					correcto=adminCV.procesarSolicitud((String)solicitud.get(CenSolicitudModificacionCVBean.C_IDSOLICITUD),new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO), idioma);
					if (correcto){							
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS DIRECCIONES","Solicitud procesada");
						tx.commit();
					}
					else{
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS DIRECCIONES","Solicitud no procesada");
						tx.rollback();
					}
				} catch (Exception e) {
					ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS DIRECCIONES","Solicitud no procesada");
					try {
						tx.rollback();
					} catch (Exception ee) {
						ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> Realizando rollback ",ee,3);
					}
				}
			}

			// Gestion de solicitudes de datos de EXPEDIENTES
			ExpSolicitudBorradoAdm adminExp = new ExpSolicitudBorradoAdm(userBean);
			vector=adminExp.getSolicitudes(institucion.toString(),String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE),"","");
			listaSolicitudes=vector.elements();
			while (listaSolicitudes.hasMoreElements()){					
				try {
					solicitud = ((Row)listaSolicitudes.nextElement()).getRow();
					tx.begin();					
					correcto=adminExp.procesarSolicitud((String)solicitud.get(ExpSolicitudBorradoBean.C_IDSOLICITUD),new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO), idioma);
					if (correcto){
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(ExpSolicitudBorradoBean.C_IDSOLICITUD),"SOLICITUD DATOS EXPEDIENTES","Solicitud procesada");
						tx.commit();
						}
					else{
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(ExpSolicitudBorradoBean.C_IDSOLICITUD),"SOLICITUD DATOS EXPEDIENTES","Solicitud no procesada");
						tx.rollback();
					}				
				} catch (Exception e) {
					ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(ExpSolicitudBorradoBean.C_IDSOLICITUD),"SOLICITUD DATOS EXPEDIENTES","Solicitud no procesada");
					try {
						tx.rollback();
					} catch (Exception ee) {
						ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> Realizando rollback ",ee,3);
					}
				}
			}

			// Gestion de solicitudes de datos de FACTURACION
			CenSolModiFacturacionServicioAdm adminFact = new CenSolModiFacturacionServicioAdm(userBean);
			vector=adminFact.getSolicitudes(institucion.toString(),String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE),"","");
			listaSolicitudes=vector.elements();
			while(listaSolicitudes.hasMoreElements()){
				try {
					solicitud = ((Row)listaSolicitudes.nextElement()).getRow();
					tx.begin();					
					correcto=adminFact.procesarSolicitud((String)solicitud.get(CenSolModiFacturacionServicioBean.C_IDSOLICITUD),idioma);
					if (correcto){							
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS FACTURACION","Solicitud procesada");
						tx.commit();
					}
					else{
						ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS FACTURACION","Solicitud no procesada");
						tx.rollback();
					}
				} catch (Exception e) {
					ClsGestionAutomaticaLog.escribirLogGestionAutomatica(institucion.toString(),(String)solicitud.get(CenSolicitModifDatosBasicosBean.C_IDSOLICITUD),"SOLICITUD DATOS FACTURACION","Solicitud no procesada");
					try {
						tx.rollback();
					} catch (Exception ee) {
						ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> Realizando rollback ",ee,3);
					}
				}
			}
			
		}
		catch(Exception ee){
			ClsLogging.writeFileLogError("Error en GestionAutomaticaSolicitudes -> " + ee.getMessage(),ee,3);
			throw new ClsExceptions(ee,"Error en GestionAutomaticaSolicitudes -> " + ee.getMessage());		
		}		
		return resultado;
	}
	
}
