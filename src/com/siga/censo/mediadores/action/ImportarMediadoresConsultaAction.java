package com.siga.censo.mediadores.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorFicherocsv;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorFicherocsvExample;
import org.redabogacia.sigaservices.app.autogen.model.CenMediadorFicherocsv;
import org.redabogacia.sigaservices.app.services.cen.MediadoresService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.mediadores.form.MediadoresFicheroForm;
import com.siga.censo.mediadores.form.MediadoresImportConsultaForm;
import com.siga.censo.mediadores.form.MediadoresImportForm;
import com.siga.censo.service.CensoService;
import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class ImportarMediadoresConsultaAction extends MasterAction {
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_MEDIADORES_IMPORT_CONSULTA";




	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
						
			request.getSession().removeAttribute(DATAPAGINADOR);
			
			MediadoresImportConsultaForm mediadoresImportConsultaForm = (MediadoresImportConsultaForm) formulario;
			mediadoresImportConsultaForm.setIdColegio(getUserBean(request).getLocation());
			mediadoresImportConsultaForm.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
		
	}
	
	private List<InstitucionVO> getColegiosDependientes(String idInstitucion) throws SIGAException{
		List<InstitucionVO> instituciones = null;
		//Si la institucion conectada es General se recuperan todos los colegios (no los consejos)
		if (institucionEsGeneral(idInstitucion)){
			CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
			instituciones = service.getColegiosNoConsejo(idInstitucion);
		}
		//Si la institucion no conectada es un Consejo, se recuperan sus colegios dependientes
		else if (institucionEsConsejo(idInstitucion)){
			CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
			instituciones = service.getColegiosDeConsejo(idInstitucion);
		}
		return instituciones;
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
				PaginadorVector<CenMediadorFicherocsv> paginador = (PaginadorVector<CenMediadorFicherocsv>) databackup.get("paginador");
				List<CenMediadorFicherocsv> datos = new ArrayList<CenMediadorFicherocsv>();

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
				
				MediadoresImportConsultaForm mediadoresImportConsultaForm = (MediadoresImportConsultaForm) formulario;				
				MediadoresService mediadoresService = (MediadoresService) BusinessManager.getInstance().getService(MediadoresService.class);
				CenMediadorFicherocsvExample cenMediadorFicherocsvExample = new CenMediadorFicherocsvExample();
				CenMediadorFicherocsvExample.Criteria criteria = cenMediadorFicherocsvExample.createCriteria();
				
				if (mediadoresImportConsultaForm.getIdColegio() != null && !mediadoresImportConsultaForm.getIdColegio().trim().equals("")) {
					criteria.andIdinstitucionEqualTo(Short.valueOf(mediadoresImportConsultaForm.getIdColegio()));
				}
				
				if (mediadoresImportConsultaForm.getFechaCargaDesde() != null && !mediadoresImportConsultaForm.getFechaCargaDesde().trim().equals("")) {
					criteria.andFechamodificacionGreaterThan(GstDate.convertirFechaDiaMesAnio(mediadoresImportConsultaForm.getFechaCargaDesde()));
				}
				
				if (mediadoresImportConsultaForm.getFechaCargaHasta() != null && !mediadoresImportConsultaForm.getFechaCargaHasta().trim().equals("")) {
					criteria.andFechamodificacionLessThan(GstDate.convertirFechaDiaMesAnio(mediadoresImportConsultaForm.getFechaCargaHasta()));
				}
				
				cenMediadorFicherocsvExample.orderByFechamodificacionDESC();
				
				List<CenMediadorFicherocsv> datos = mediadoresService.selectCenMediadorFicherocsv(cenMediadorFicherocsvExample);
				
				PaginadorVector<CenMediadorFicherocsv> paginador = new PaginadorVector(datos);
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
	
	protected String ver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request,	HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
						
			MediadoresImportConsultaForm mediadoresImportConsultaForm = (MediadoresImportConsultaForm) formulario;		
			Vector ocultos = mediadoresImportConsultaForm.getDatosTablaOcultos(0);
			if (ocultos != null && ocultos.size() > 0) {
				mediadoresImportConsultaForm.setIdmediadorficherocsv(Long.valueOf(ocultos.get(0).toString()));
				Map<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("idmediadorficherocsv", Long.valueOf(ocultos.get(0).toString()));
				request.setAttribute("ficheroCSVParam", mapa);
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "ver";
		
	}

}