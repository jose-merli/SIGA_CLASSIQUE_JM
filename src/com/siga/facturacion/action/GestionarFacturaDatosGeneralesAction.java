/*
 * Created on 15-mar-2005
 * Modified by miguel.villegas 12/04/05  Insercion metodos 'imprimirFactura()', 'generacionFicheroFOP()'
 * 										 'sustitucionCamposPlantillaFactura()'y 'sustitucionEtiqueta()' 
 * 										 y demás relacionadas con obtencion informes 
 *
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.facturacion.form.GestionarFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;

/**
 * @author daniel.campos
 *
 */
public class GestionarFacturaDatosGeneralesAction extends MasterAction{

	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("imprimirFactura")){
					mapDestino = imprimirFactura(mapping, miForm, request, response);
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new SIGAException("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try{
			// Obtengo el UserBean y los diferentes parametros recibidos
			String accion =  (String)request.getParameter("accion");
			String idFactura = (String)request.getParameter("idFactura");
			Integer idInstitucion = new Integer((String)request.getParameter("idInstitucion"));
			String volver = null;
			try {				
				if (request.getParameter("botonVolver")==null)
					volver = "NO";
				else
					volver = (String)request.getParameter("botonVolver");
			} catch (Exception e){
				volver = "NO";
			}

			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
			Hashtable factura = facturaAdm.getFacturaDatosGenerales(idInstitucion, idFactura, this.getLenguaje(request));
			
			request.getSession().setAttribute("DATABACKUP", factura);
			request.setAttribute("modoRegistroBusqueda", accion);
			request.setAttribute("volver", volver);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return "inicio";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		UserTransaction t = null;
		
		try {		 				
			GestionarFacturaForm miForm = (GestionarFacturaForm)formulario;
	
			Hashtable facturaOriginal = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			Hashtable facturaModificada = (Hashtable) facturaOriginal.clone();

			// Fijo los nuevos datos
			UtilidadesHash.set(facturaModificada, FacFacturaBean.C_OBSERVACIONES, miForm.getDatosGeneralesObservaciones());
			UtilidadesHash.set(facturaModificada, FacFacturaBean.C_OBSERVINFORME, miForm.getDatosGeneralesObservinforme());
			
			t = this.getUserBean(request).getTransaction();
			t.begin();	
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
			if (facturaAdm.update(facturaModificada, facturaOriginal)){
				t.commit();
			}
			else {
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},new ClsExceptions("messages.updated.error"),t);
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, t); 
		}				
		return exito("messages.updated.success", request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String imprimirFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		File ficFOP=null;
		File ficPDF=null;
		boolean correcto=false;
		Vector desglose=null;
		String idInstitucion="";
		String idFactura="";
		int tamanho=27;
		
		try {
			// Obtener los datos necesarios a mostrar en la factura
			Hashtable facturaOrigen = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			idFactura=(String)facturaOrigen.get(FacFacturaBean.C_IDFACTURA);
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			String institucion =usr.getLocation();
			
			// RGG 15/02/2007 CAMBIOS PARA INFORME MASTER REPOR
			InformeFactura inf=new InformeFactura(usr);
			
			// PDM Antes de generar la factura comprobamos si existe la direccion del receptor de la factura
			FacFacturaAdm facturaAdm= new FacFacturaAdm(this.getUserBean(request));
			CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.getUserBean(request));
			Hashtable datosFactura =new Hashtable();
			datosFactura.put(FacFacturaBean.C_IDINSTITUCION,institucion);
			datosFactura.put(FacFacturaBean.C_IDFACTURA,idFactura);
			Vector vFactura=facturaAdm.selectByPK(datosFactura);
			FacFacturaBean idPersona=(FacFacturaBean)vFactura.get(0);
			String idPersonaFactura=idPersona.getIdPersona().toString();
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
 			Hashtable htCol = admCol.obtenerDatosColegiado(institucion,idPersonaFactura,this.getUserBean(request).getLanguage());
 			String nColegiado = "";
 			if (htCol!=null && htCol.size()>0) {
 			    nColegiado = (String)UtilidadesHash.getString(htCol,"NCOLEGIADO_LETRADO");
 			}			 
			 
			Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersonaFactura,institucion,""+ClsConstants.TIPO_DIRECCION_FACTURACION);
			if (direccion.size()==0){
			    // Si no hay direccion de despacho (porque es un no colegiado), miramos su direccion de correo
			 	direccion=direccionAdm.getEntradaDireccionEspecifica(idPersonaFactura,institucion,""+ClsConstants.TIPO_DIRECCION_DESPACHO);
		    }
			
			if (direccion.size()==0){							// jbd // inc8271 // En vez de mirar primero la direccion de despacho y luego la de censo se comprueba primero la facturacion
				direccion=direccionAdm.getEntradaDireccionEspecifica(idPersonaFactura,institucion,""+ClsConstants.TIPO_DIRECCION_CENSOWEB);
			}
			
		
			if (direccion.size()>0 || (direccion.size()==0 && request.getParameter("generarFactura")!=null && request.getParameter("generarFactura").equals("1"))){

			    ClsLogging.writeFileLog("ANTES DE LA GENERACION DE LA FACTURA. ",10);
				File filePDF = inf.generarFactura(request,usr.getLanguage().toUpperCase(),institucion,idFactura,nColegiado);
	
				if (filePDF==null) {
					throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");				
				} else {
				    ClsLogging.writeFileLog("DESPUES DE LA GENERACION DE LA FACTURA: "+filePDF.getAbsolutePath(),10);
				    ClsLogging.writeFileLog("Existe el fichero: "+((filePDF.exists())?"SI":"NO"),10);
				}
	//
				FacFacturaAdm facfactura=new FacFacturaAdm(usr);
				facfactura.firmarPDF(filePDF,institucion);
				
				request.setAttribute("nombreFichero", filePDF.getName());
				request.setAttribute("rutaFichero", filePDF.getPath());			
	
	
				
			}else{
			    // si no existe la direccion sacamos un mensaje informando de la situacion antes de generar la factura
				return "ValidarDireccion";
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e,null); 
		}				
		request.setAttribute("generacionOK","OK");
		return "descarga";
	}
	
	
}
