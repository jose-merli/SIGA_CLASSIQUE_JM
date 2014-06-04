/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 10-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.facturacion.form.GenerarFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action para generar de series de Facturacion.<br/>
 * Gestiona abrir y generar Facturas
 */
public class GenerarFacturacionAction extends MasterAction{
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);	
				
			}else if (accion.equalsIgnoreCase("generarFactura")){
				mapDestino = generarFactura(mapping, miForm, request, response);
			}
			else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 				
			{ 				
				if (miForm.getModal().equalsIgnoreCase("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}			    
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}			
			
		 }catch (SIGAException es) { 
		    throw es; 
		 }catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		 } 
		 
		   return mapping.findForward(mapDestino);   	
	}
	
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
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NULL";
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			Vector vDatos = adm.selectDatosFacturacion(sWhere, orden);
			request.getSession().setAttribute("DATABACKUP", vDatos);	

			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
	return "abrir";
	}

	/** 
	 *  Funcion que atiende la accion generarFactura. Genera las facturas programadas 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String generarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String cont = "";
		String idFactura;
		String sCantidad 	= "0";
		float fCantidad 	= 0;
		float fCantidadAux 	= 0;
		UsrBean usr			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		String lenguaje 	= usr.getLanguage();		
		UserTransaction tx	= null;
		
		try {
			GenerarFacturacionForm form  = (GenerarFacturacionForm)formulario;
			FacFacturaAdm admFactura = new FacFacturaAdm(this.getUserBean(request));
			FacFacturacionProgramadaAdm prgFactura = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String usuMod			= (String)ocultos.elementAt(2);
			String idInstitucion	= this.getIDInstitucion(request).toString();	
			Object[] param_in = new Object[7];

			// BLOQUEAMOS LA PROGRAMACION
			tx = usr.getTransactionPesada(); 
			tx.begin();
			Hashtable ht = new Hashtable();
			ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
			ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion);
			ht.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,idProgramacion);
			Vector v = prgFactura.selectByPK(ht);
			FacFacturacionProgramadaBean b = null;
			if (v!=null && v.size()>0) {
				b = (FacFacturacionProgramadaBean) v.get(0);
				if (b.getLocked().equals("1")) {
					throw new SIGAException("messages.facturacion.generacionEnProceso");
				}
				b.setLocked("1");
				prgFactura.updateDirect(b);
			}
			tx.commit();
			
			try {
				//tx = usr.getTransaction(); 
				tx.begin();
				String idPrevision="0"; 
	        	param_in[0] = idInstitucion;
	        	param_in[1] = idSerieFacturacion;
	        	param_in[2] = idProgramacion;
	        	param_in[3] = this.getUserBean(request).getLanguageInstitucion();		// idioma
	        	param_in[4] = "";		// IDPETICION
	        	param_in[5] = usuMod;
	        	param_in[6] = idPrevision;
	        	String resultado[] = new String[2];
	        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?,?)}", 2, param_in);
	        	String codretorno = resultado[0];
	        	if (!codretorno.equals("0")){
	        		 throw new ClsExceptions ("Error al generar la Facturación: " + resultado[0] + " - "+ resultado[1]);
	        	}

				ht = new Hashtable();
				ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
				ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion);
				ht.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,idProgramacion);
				v = prgFactura.selectByPK(ht);
				b = null;
				if (v!=null && v.size()>0) {
					b = (FacFacturacionProgramadaBean) v.get(0);
					b.setLocked("0");
					prgFactura.updateDirect(b);
				}

	        	tx.commit();

			} catch (Exception in) {
				tx.rollback();
				
				// DESBLOQUEAMOS LA PROGRAMACION
				//tx = usr.getTransaction(); 
				tx.begin();
				ht = new Hashtable();
				ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
				ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion);
				ht.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,idProgramacion);
				v = prgFactura.selectByPK(ht);
				b = null;
				if (v!=null && v.size()>0) {
					b = (FacFacturacionProgramadaBean) v.get(0);
					b.setLocked("0");
					prgFactura.updateDirect(b);
				}
				tx.commit();
				
				throw in;
			}


			
			// Obtenemos el numero de facturas actualizadas y el importe .
//			String select =	"SELECT count(*) CUENTA, sum(PKG_SIGA_TOTALESFACTURA.TOTAL(IDINSTITUCION, IDFACTURA)) as IMPORTE " + 
			String select =	"SELECT count(*) CUENTA, sum(IMPTOTAL) as IMPORTE " + 
			" from  " +FacFacturaBean.T_NOMBRETABLA+
			" where " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion;
			select += " and ";
			select += FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;
			select += " and ";
			select += FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + " = " + idProgramacion;
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(select)) {
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				cont = (String) aux.get("CUENTA");
				sCantidad = (String) aux.get("IMPORTE");
			}
			
			String mensaje = "facturacion.generarFacturacion.mensaje.generacionFacturacionOK";
	
			//Simbolo de Euro al final:
			String[] datos = {cont, sCantidad.concat(" Euros ")};
			mensaje = UtilidadesString.getMensaje(mensaje, datos, lenguaje);
			request.setAttribute("mensaje",mensaje);	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
		return "exitoConString"; 
	}
}
