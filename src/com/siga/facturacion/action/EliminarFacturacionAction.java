/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 15-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacFacturacionSuscripcionAdm;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.PysCompraAdm;
import com.siga.certificados.Plantilla;
import com.siga.facturacion.form.EliminarFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;



/**
 * Clase action para Eliminar la facturacion.<br/>
 * Gestiona abrir y borrar Facturas
 */
public class EliminarFacturacionAction extends MasterAction{
	/** 
	 *  Funcion que atiende la accion abrir. Muestra todas las facturas programadas cuyas Fecha Real de Generación es null 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{					
			Integer idInstitucion	= this.getIDInstitucion(request);			
			Integer usuario			= this.getUserName(request);
			String fechaInicial 	= "01/01/2000";
			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NULL";
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAREALGENERACION + ">= TO_DATE ('" + fechaInicial + "', 'DD/MM/YYYY')";
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAREALGENERACION};
			
			Vector vDatos = adm.selectDatosFacturacion(sWhere, orden);
			request.getSession().setAttribute("DATABACKUP", vDatos);	

			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
	return "abrir";
	}

	/** 
	 *  Funcion que atiende la accion borrar. Genera las facturas programadas 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;	
		try{
			tx = this.getUserBean(request).getTransactionPesada();
			
			EliminarFacturacionForm form 				= (EliminarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm adm 			= new FacFacturacionProgramadaAdm(this.getUserBean(request));
			FacFacturacionProgramadaBean bean 			= new FacFacturacionProgramadaBean();
			FacFacturaAdm admFactura 					= new FacFacturaAdm(this.getUserBean(request));
			FacLineaFacturaAdm admLinea 				= new FacLineaFacturaAdm(this.getUserBean(request));
			FacFacturacionSuscripcionAdm admSuscripcion = new FacFacturacionSuscripcionAdm(this.getUserBean(request));
			PysCompraAdm admCompra 						= new PysCompraAdm(this.getUserBean(request));	
			
			Vector ocultos 				= (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion 	= (String)ocultos.elementAt(0);			
			String idProgramacion 		= (String)ocultos.elementAt(1);
			String idInstitucion		= this.getIDInstitucion(request).toString();
			String idFactura 			= null;
			Long numeroLinea 			= null;
			
			tx.begin();	
	
				Object[] param_in = new Object[4];
				param_in[0] = idInstitucion;
				param_in[1] = idSerieFacturacion;
				param_in[2] = idProgramacion;
				param_in[3] = this.getUserName(request).toString();
				String resultado[] = new String[2];
				resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.ELIMINARFACTURACION(?,?,?,?,?,?)}",2, param_in);
				String codretorno = resultado[0];
				if (!codretorno.equals("0")) {
					
					if (codretorno.equals("-1")) {
						// No existe la facturacion
						throw new SIGAException("messages.facturacion.facturacionNoExiste");
					} else
					if (codretorno.equals("-2")) {
						// La facturacion está bloqueada
						throw new SIGAException("messages.facturacion.generacionEnProceso");
					} else {
						// Error general
						throw new ClsExceptions("Error en ejecución del PL PKG_SIGA_FACTURACION.ELIMINARFACTURACION. Cod:ORA-"+codretorno+" Desc:"+resultado[1]);
					}
					
				} else {
					
					// ok
			
			 		// RGG 05/02/2007 ELIMINAMOS LOS FICHERO DE LOG ASOCIADOS A LA PROGRAMACION
				    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//					ReadProperties p = new ReadProperties ("SIGA.properties");
					String pathFichero 		= p.returnProperty("facturacion.directorioFisicoLogProgramacion");
		    		String sBarra = "";
		    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
		    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
					String nombreFichero = "LOG_CONFIRM_FAC_"+idInstitucion+"_"+idSerieFacturacion+"_"+idProgramacion+".log.xls"; 
					File fichero = new File(pathFichero+sBarra+idInstitucion+sBarra+nombreFichero);
					if (fichero.exists()) {
						fichero.delete();
					}	
					//Borramos los PDFs en caso de que los haya	
					String pathFicheroPDF = p.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+p.returnProperty("facturacion.directorioFacturaPDFJava");
					String idserieidprogramacion = idSerieFacturacion+"_" + idProgramacion;
					pathFicheroPDF += sBarra+idInstitucion+sBarra+idserieidprogramacion;
					File ficheroPDF = new File(pathFicheroPDF);
					if (ficheroPDF.exists()) {
						Plantilla.borrarDirectorio(ficheroPDF);
					}
					
					
				}
					
				tx.commit();					

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
	
		return exitoRefresco("messages.deleted.success", request);
	}



}
