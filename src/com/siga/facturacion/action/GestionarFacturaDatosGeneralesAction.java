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
import java.util.regex.Pattern;

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
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
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
			
			} else if (accion.equalsIgnoreCase("imprimirFactura") || accion.equalsIgnoreCase("descargarFacturaSinRegenerarPDF")){
				mapDestino = imprimirFactura(mapping, miForm, request, response);
			
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
			    if (miForm.getModal().equalsIgnoreCase("TRUE")) {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} catch (SIGAException es) { 
			throw es;
			
		} catch (Exception e) { 
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
		try {
			// Obtener los datos necesarios a mostrar en la factura
			Hashtable facturaOrigen = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			String idFactura = (String)facturaOrigen.get(FacFacturaBean.C_IDFACTURA);
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String institucion = usr.getLocation();
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
			InformeFactura infFactura = new InformeFactura(usr);			
			CenDireccionesAdm admCenDirecciones = new CenDireccionesAdm(usr);
			FacFacturaAdm admFacFactura = new FacFacturaAdm(usr);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usr);
			
			// PDM Antes de generar la factura comprobamos si existe la direccion del receptor de la factura
			Hashtable<String,String> hFacFactura = new Hashtable<String,String>();			
			hFacFactura.put(FacFacturaBean.C_IDINSTITUCION, institucion);
			hFacFactura.put(FacFacturaBean.C_IDFACTURA, idFactura);
			
			FacFacturaBean bFacFactura = (FacFacturaBean) admFacFactura.selectByPK(hFacFactura).firstElement();			
			String idPersonaFactura = bFacFactura.getIdPersona().toString();
			 
			Hashtable<String,Object> direccion = admCenDirecciones.getEntradaDireccionEspecifica(idPersonaFactura, institucion, ""+ClsConstants.TIPO_DIRECCION_FACTURACION);
			if (direccion.size()==0) {
			    // Si no hay direccion de despacho (porque es un no colegiado), miramos su direccion de correo
			 	direccion = admCenDirecciones.getEntradaDireccionEspecifica(idPersonaFactura, institucion, ""+ClsConstants.TIPO_DIRECCION_DESPACHO);
		    }
			
			if (direccion.size()==0) {							
				// jbd // inc8271 // En vez de mirar primero la direccion de despacho y luego la de censo se comprueba primero la facturacion
				direccion = admCenDirecciones.getEntradaDireccionEspecifica(idPersonaFactura, institucion, ""+ClsConstants.TIPO_DIRECCION_CENSOWEB);
			}
		
			if (direccion.size()>0 || (direccion.size()==0 && request.getParameter("generarFactura")!=null && request.getParameter("generarFactura").equals("1"))) {
				
				String accion = formulario.getModo();
				boolean bRegenerar = (accion!=null && accion.equalsIgnoreCase("imprimirFactura")); // true=RegeneraPdfFacturaSinFirmar; false=descargaFacturaSinRegenerarPDF (GeneraPdfFacturaSinFirmar si no existe)

			    ClsLogging.writeFileLog("ANTES DE LA GENERACION DE LA FACTURA. ",10);
				File filePDF = infFactura.generarPdfFacturaFirmada(request, bFacFactura, bRegenerar);
				
				// Siempre se debe borrar el fichero pdf con la firma
				String nombreColegiado = personaAdm.obtenerNombreApellidos(idPersonaFactura);
				if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
					nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
				}else{
					nombreColegiado="";
				}
				
				String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + bFacFactura.getIdSerieFacturacion() +
						" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " +bFacFactura.getIdInstitucion();
							
				Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
										
				String[] nombreFicherosarrays;
				if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
					FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
				
					switch (beanSerieFacturacion.getIdNombreDescargaPDF()) {
					case 1:
						nombreFicherosarrays = filePDF.getName().split("-",2);
						request.setAttribute("nombreFichero",nombreFicherosarrays[1]);
						break;
					case 2:
						//Quitamos la extensión y añadimos el nombre más la extensión
						String[] separacionExtensionDelFichero = filePDF.getName().split(Pattern.quote("."));
						String[] separacionNombreColegiado = nombreColegiado.split("-");
						nombreFicherosarrays = separacionExtensionDelFichero[0].split("-",2);
						request.setAttribute("nombreFichero",nombreFicherosarrays[1] + "-"+separacionNombreColegiado[0]+"."+separacionExtensionDelFichero[1]);
						break;
					case 3:
						nombreFicherosarrays =filePDF.getName().split("-",2);
						request.setAttribute("nombreFichero",nombreColegiado+nombreFicherosarrays[1]);
						break;

					default:
						nombreFicherosarrays =filePDF.getName().split("-",2);
						request.setAttribute("nombreFichero",nombreColegiado+ nombreFicherosarrays[1]);
						break;
					}
				}else{
					nombreFicherosarrays =filePDF.getName().split("-",2);
					request.setAttribute("nombreFichero",nombreColegiado+ nombreFicherosarrays[1]);
				}
				
				
				// Siempre se debe borrar el fichero pdf con la firma
				request.setAttribute("rutaFichero", filePDF.getPath());			
				
			} else {
			    // si no existe la direccion sacamos un mensaje informando de la situacion antes de generar la factura
				return "ValidarDireccion";
			}
			
		} catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e,null); 
		}
		
		request.setAttribute("generacionOK","OK");
		return "descarga";
	}
}