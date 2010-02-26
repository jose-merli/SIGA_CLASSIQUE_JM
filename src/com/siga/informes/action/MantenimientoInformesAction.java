
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.RetencionesIRPFForm;
import com.siga.informes.InformeCertificadoPago;
import com.siga.informes.InformeColegiadosPagos;
import com.siga.informes.InformeFichaFacturacion;
import com.siga.informes.InformeFichaPago;
import com.siga.informes.form.MantenimientoInformesForm;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Ppagos, Facturaciones.
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MantenimientoInformesAction extends MasterAction {
	
	/** 
	 * Método que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try { 
			miForm = (MasterForm) formulario;
			String modo = miForm.getModo();
			
			if (modo != null) {
				//GENERAR INFORMES
				if (modo.equalsIgnoreCase("generarFichaPago")){
					//mapDestino = generarFichaPago(mapping, miForm, request, response);
					InformeFichaPago inf=new InformeFichaPago();
					mapDestino=inf.generarFichaPago(mapping,formulario,request,response);
					return mapping.findForward(mapDestino);
				} else if (modo.equalsIgnoreCase("generarFichaFacturacion")){
					//mapDestino = generarFichaFacturacion(mapping, miForm, request, response);
					InformeFichaFacturacion inf=new InformeFichaFacturacion();
					mapDestino=inf.generarFichaFacturacion(mapping,formulario,request,response);
					return mapping.findForward(mapDestino);
				}else if (modo.equalsIgnoreCase("generarInformeFacturacion")){
					InformeFichaFacturacion inf=new InformeFichaFacturacion();
					mapDestino=inf.generarInformeFacturacion(mapping,formulario,request,response);
					return mapping.findForward(mapDestino); 
				} /*else if (modo.equalsIgnoreCase("generarColegiadoPago")){
					//mapDestino = generarColegiadoPago(mapping, miForm, request, response);
					InformeColegiadosPagos inf=new InformeColegiadosPagos();
					mapDestino=inf.generarColegiadoPago(mapping,formulario,request,response);
					//Controlamos si debemos avisar del error:
					if (mapDestino.equals("error"))
						mapDestino = exitoModalSinRefresco("gratuita.retenciones.noResultados",request);
					return mapping.findForward(mapDestino);
				} */else if (modo.equalsIgnoreCase("generarCertificadoPago")){
					//mapDestino = generarCertificadoPago(mapping, miForm, request, response);
					InformeCertificadoPago inf=new InformeCertificadoPago();
					mapDestino=inf.generarCertificadoPago(mapping,formulario,request,response);
					return mapping.findForward(mapDestino);
					//DESCARGAR
				} else if (miForm.getModo().equalsIgnoreCase("generarInformeIRPF")){
					return mapping.findForward(this.generarInformeIRPF(mapping, miForm, request, response));
					
					
				} else if (modo.equalsIgnoreCase("descargar")){
					mapDestino = descargar(mapping, miForm, request, response);
					return mapping.findForward(mapDestino);
				} else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			} else
				return super.executeInternal(mapping,formulario,request,response);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacionSJCS"});
		}
	}
	
	
	/**
	 * Metodo que permite la descarga de un fichero.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String descargar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		
		File fichero = null;
		String rutaFichero = null;
		MantenimientoInformesForm miform = null;
		
		try {
			//Obtenemos el formulario y sus datos:
			miform = (MantenimientoInformesForm)formulario;
			rutaFichero = miform.getRutaFichero();
			fichero = new File(rutaFichero);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero","true");

			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFichero";	
	}
	protected String generarInformeIRPF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		RetencionesIRPFForm miform = (RetencionesIRPFForm)formulario;
		
		String salida = "generaInformeIRPF";
		return salida;
	}
	
}