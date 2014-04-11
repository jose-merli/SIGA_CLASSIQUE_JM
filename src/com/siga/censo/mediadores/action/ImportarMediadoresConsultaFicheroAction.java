package com.siga.censo.mediadores.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorCsv;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorCsvExample;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorFicherocsv;
import org.redabogacia.sigaservices.app.services.cen.MediadoresService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.mediadores.form.MediadoresImportConsultaFicheroForm;
import com.siga.censo.mediadores.form.MediadoresImportConsultaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;

public class ImportarMediadoresConsultaFicheroAction extends MasterAction {
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_MEDIADORES_IMPORT_CONSULTA_FICHERO";
	
	protected String abrir (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
						
			request.getSession().removeAttribute(DATAPAGINADOR);			
			MediadoresImportConsultaFicheroForm mediadoresImportConsultaFicheroForm = (MediadoresImportConsultaFicheroForm) formulario;
			MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
			CenMediadorFicherocsv cenMediadorFicherocsv = mediadoresService.selectCenMediadorFicherocsv(mediadoresImportConsultaFicheroForm.getIdmediadorficherocsv());
			if (cenMediadorFicherocsv == null) {
				throw new BusinessException("No se ha encontrado el fichero con id = " + mediadoresImportConsultaFicheroForm.getIdmediadorficherocsv());
			}
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
			String nombreInstitucion = institucionAdm.getNombreInstitucion(String.valueOf(cenMediadorFicherocsv.getIdinstitucion()));
			mediadoresImportConsultaFicheroForm.setNombreColegio(nombreInstitucion);
			
			mediadoresImportConsultaFicheroForm.setFechaCarga(GstDate.getFormatedDateLong(getUserBean(request).getLanguage(), cenMediadorFicherocsv.getFechamodificacion()));
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
		
	}
	
	protected String buscar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		request.getSession().removeAttribute(DATAPAGINADOR);
		return buscarPor(mapping, formulario, request, response);
	}
	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {	
		
		try {
						
			HashMap databackup = new HashMap();		

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<CenMediadorCsv> paginador = (PaginadorVector<CenMediadorCsv>) databackup.get("paginador");
				List<CenMediadorCsv> datos = new ArrayList<CenMediadorCsv>();

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
				
				MediadoresImportConsultaFicheroForm mediadoresImportConsultaFicheroForm = (MediadoresImportConsultaFicheroForm) formulario;
				if (mediadoresImportConsultaFicheroForm.getIdmediadorficherocsv() == null) {
					throw new IllegalArgumentException("Idmediadorficherocsv no puede ser nulo");					
				}
										
				MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
				CenMediadorCsvExample cenMediadorFicherocsvExample = new CenMediadorCsvExample();
				CenMediadorCsvExample.Criteria criteria = cenMediadorFicherocsvExample.createCriteria();
				criteria.andIdmediadorficherocsvEqualTo(mediadoresImportConsultaFicheroForm.getIdmediadorficherocsv());
								
				cenMediadorFicherocsvExample.orderByFilacsv();
				
				List<CenMediadorCsv> datos = mediadoresService.selectCenMediadorCsv(cenMediadorFicherocsvExample);
				
				PaginadorVector<CenMediadorCsv> paginador = new PaginadorVector(datos);
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
