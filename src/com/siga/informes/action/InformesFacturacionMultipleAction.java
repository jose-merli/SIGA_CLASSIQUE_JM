/*
 * VERSIONES:
 *	
 */

package com.siga.informes.action;

import java.io.File;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.atos.utils.*;
import com.siga.general.*;
import com.siga.informes.InformeFacturasEmitidas;



public class InformesFacturacionMultipleAction extends MasterAction 
{
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException 
	{
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
			}
			else if (accion.equalsIgnoreCase("generarInforme")){
				mapDestino = generarInforme(mapping, miForm, request, response);
			}
			else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}

	/**
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String generarInformeOld(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    String salida = "";
		try {
			File fichero = null;

//			{	// Crystal
//				String idInstitucion = "" + this.getIDInstitucion(request);
//				String fDesde = UtilidadesHash.getString(formulario.getDatos(), "FECHA_DESDE");
//				String fHasta = UtilidadesHash.getString(formulario.getDatos(), "FECHA_HASTA");
//
//				Hashtable parametros = new Hashtable();
//
//				UtilidadesHash.set (parametros, "idinstitucion", idInstitucion);
//				UtilidadesHash.set (parametros, "fecha_inicio", fDesde);	// "02/02/2005"
//				UtilidadesHash.set (parametros, "fecha_fin",    fHasta);
//
////				String informe = "/Datos/plantillas/informes_genericos/2040/prueba/LibroDeFacturasMia.rpt";
////				fichero = CrystalReportMaster.generarPDF(informe, "/Datos/plantillas/informes_genericos/2040/prueba/salidaCrystalLibroDeFacturasMia.pdf", parametros);
//
//				// windows
//				String informe = "C:/Datos/plantillas/listado_facturas_emitidas/2040/LibroDeFacturasMia.rpt";
//				fichero = CrystalReportMaster.generarPDF(informe, "C://salidaSIGA_BD.pdf", parametros);
//			}

			{	// FOP
				InformeFacturasEmitidas informe = new InformeFacturasEmitidas (this.getUserBean(request));
				fichero = informe.generarListadoFacturasEmitidasOld(request, formulario.getDatos());
			}
			
			/*if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida = "descargaFichero";
			}
			else{
				throw new SIGAException("facturacion.informes.facturasEmitidas.generarInforme.error");
			}*/
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida= "descarga";
			}
			else{
				return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	protected String generarInforme(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    String salida = "";
		try {
			File fichero = null;

//			{	// Crystal
//				String idInstitucion = "" + this.getIDInstitucion(request);
//				String fDesde = UtilidadesHash.getString(formulario.getDatos(), "FECHA_DESDE");
//				String fHasta = UtilidadesHash.getString(formulario.getDatos(), "FECHA_HASTA");
//
//				Hashtable parametros = new Hashtable();
//
//				UtilidadesHash.set (parametros, "idinstitucion", idInstitucion);
//				UtilidadesHash.set (parametros, "fecha_inicio", fDesde);	// "02/02/2005"
//				UtilidadesHash.set (parametros, "fecha_fin",    fHasta);
//
////				String informe = "/Datos/plantillas/informes_genericos/2040/prueba/LibroDeFacturasMia.rpt";
////				fichero = CrystalReportMaster.generarPDF(informe, "/Datos/plantillas/informes_genericos/2040/prueba/salidaCrystalLibroDeFacturasMia.pdf", parametros);
//
//				// windows
//				String informe = "C:/Datos/plantillas/listado_facturas_emitidas/2040/LibroDeFacturasMia.rpt";
//				fichero = CrystalReportMaster.generarPDF(informe, "C://salidaSIGA_BD.pdf", parametros);
//			}

			{	// FOP
				InformeFacturasEmitidas informe = new InformeFacturasEmitidas (this.getUserBean(request));
				fichero = informe.generarListadoFacturasEmitidasPdfFromXmlToFoXsl(request, formulario.getDatos());
				
			}
			
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida = "descargaFichero";
			}
			else{
				throw new SIGAException("facturacion.informes.facturasEmitidas.generarInforme.error");
			}
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida= "descarga";
			}
			else{
				return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	

}

