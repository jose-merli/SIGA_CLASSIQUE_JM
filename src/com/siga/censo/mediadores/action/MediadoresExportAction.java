package com.siga.censo.mediadores.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorExportfichero;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorExportficheroExample;
import org.redabogacia.sigaservices.app.services.cen.MediadoresService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.censo.mediadores.form.MediadoresExportForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class MediadoresExportAction extends MasterAction {

public static final String DATAPAGINADOR = "DATAPAGINADOR_MEDIADORES_EXPORT";
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
			if (miForm != null) {
				return super.executeInternal(mapping,formulario,request,response);				
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			MediadoresExportForm mediadoresExportForm = (MediadoresExportForm) formulario;						
			request.getSession().removeAttribute(DATAPAGINADOR);
						
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
		
	}
	
	
	protected synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mensaje = "messages.generarFichero.correcta";

		try {
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			MediadoresExportForm mediadoresExportForm = (MediadoresExportForm) formulario;	
			
			request.getSession().removeAttribute(DATAPAGINADOR);			
			MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
			CenMediadorExportfichero cenMediadorExportfichero = mediadoresService.creaExport();
						
			if (cenMediadorExportfichero != null && cenMediadorExportfichero.getIdmediadorexportfichero() != null) {	
				mensaje = "messages.generarFichero.correcta";
				request.setAttribute(CenMediadorExportfichero.C_IDMEDIADOREXPORTFICHERO, cenMediadorExportfichero.getIdmediadorexportfichero());
			} else {
				mensaje = "messages.generarFichero.noCambios";
			}
			
			buscarPor(mapping, formulario, request, response);
			request.setAttribute("mensaje", mensaje);

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}

		return "resultado";
	}
	
	/** 
	 *  Funcion que atiende la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		
		try {
						
			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<CenMediadorExportfichero> paginador = (PaginadorVector<CenMediadorExportfichero>) databackup.get("paginador");
				List<CenMediadorExportfichero> datos = new ArrayList<CenMediadorExportfichero>();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
						// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {	
				
				MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
				CenMediadorExportficheroExample cenMediadorExportficheroExample = new CenMediadorExportficheroExample();
				cenMediadorExportficheroExample.orderByFechacreacionDESC();
				List<CenMediadorExportfichero> datos = mediadoresService.selectCenMediadorExportfichero(cenMediadorExportficheroExample);
				
				PaginadorVector<CenMediadorExportfichero> paginador = new PaginadorVector(datos);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute(DATAPAGINADOR, databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}
			}				
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "resultado";
		
	}
	
	/** 
	 *  Funcion que atiende la accion download
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String download (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		MediadoresExportForm mediadoresExportForm = (MediadoresExportForm) formulario;	
							
		MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
		File file = mediadoresService.generaXML(mediadoresExportForm.getIdmediadorexportfichero());		
		
		if (file != null && file.exists()) {				
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getAbsolutePath());			
			request.setAttribute("borrarFichero", "true");			
		}
		
		return "descargaFichero";
	}
}
