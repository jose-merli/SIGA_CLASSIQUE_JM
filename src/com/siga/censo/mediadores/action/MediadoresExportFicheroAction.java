package com.siga.censo.mediadores.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorExportfichero;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorXml;
import org.redabogacia.sigaservices.app.services.cen.MediadoresService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.censo.mediadores.form.MediadoresFicheroForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class MediadoresExportFicheroAction extends MasterAction {

	public static final String DATAPAGINADOR = "DATAPAGINADOR_MEDIADORES_FICHERO";
	
	protected String ver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request,	HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		MediadoresFicheroForm mediadoresFicheroForm = (MediadoresFicheroForm) formulario;		
		Vector ocultos = mediadoresFicheroForm.getDatosTablaOcultos(0);
		if (ocultos != null && ocultos.size() > 0) {
			mediadoresFicheroForm.setIdmediadorexportfichero(Long.valueOf(ocultos.get(0).toString()));
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("idmediadorexportfichero", Long.valueOf(ocultos.get(0).toString()));
			request.setAttribute("exportFicheroParam", mapa);
		}
				
		return "ver";
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {

		try {
			MediadoresFicheroForm mediadoresFicheroForm = (MediadoresFicheroForm) formulario;
			request.getSession().removeAttribute(DATAPAGINADOR);
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
			CenMediadorExportfichero cenMediadorExportfichero = mediadoresService.getCenMediadorExportfichero(mediadoresFicheroForm.getIdmediadorexportfichero());
			
			mediadoresFicheroForm.setNombrefichero(cenMediadorExportfichero.getNombrefichero());
			mediadoresFicheroForm.setIdtipoexport(cenMediadorExportfichero.getIdtipoexport());
			
			
			if (cenMediadorExportfichero.getFechacreacion() != null) {
				String fecha = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateLong(usr.getLanguage(), cenMediadorExportfichero.getFechacreacion()));
				mediadoresFicheroForm.setFechacreacion(fecha);	
			}
			
			
			if (AppConstants.CEN_MEDIADOR_TIPOEXPORT.G.name().equals(cenMediadorExportfichero.getIdtipoexport())) {
				mediadoresFicheroForm.setTipoExport(UtilidadesString.getMensajeIdioma(usr, "censo.mediador.literal.generado"));
			} else if (AppConstants.CEN_MEDIADOR_TIPOEXPORT.S.name().equals(cenMediadorExportfichero.getIdtipoexport())) {
				mediadoresFicheroForm.setTipoExport(UtilidadesString.getMensajeIdioma(usr, "censo.mediador.literal.sincronizado"));
			}
			

		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.censo" }, e, null);
		}
		return "inicio";

	}

	
	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {	
		
		try {
						
			HashMap databackup = new HashMap();		

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<CenMediadorXml> paginador = (PaginadorVector<CenMediadorXml>) databackup.get("paginador");
				List<CenMediadorXml> datos = new ArrayList<CenMediadorXml>();

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
				
				MediadoresFicheroForm mediadoresFicheroForm = (MediadoresFicheroForm) formulario;				
				MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);				
				List<CenMediadorXml> datos = mediadoresService.getListaCenMediadorXml(mediadoresFicheroForm.getIdmediadorexportfichero());
				
				PaginadorVector<CenMediadorXml> paginador = new PaginadorVector(datos);
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
}
