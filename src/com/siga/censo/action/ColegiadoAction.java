package com.siga.censo.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ActionButtonsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.SearchButtonsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxMultipleCollectionXmlBuilder;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.form.BusquedaClientesForm;
import com.siga.censo.form.BusquedaColegiadosForm;
import com.siga.censo.form.BusquedaLetradosForm;
import com.siga.censo.service.CensoService;
import com.siga.censo.service.ColegiadoService;
import com.siga.censo.vos.ColegiadoVO;
import com.siga.censo.vos.NombreVO;
import com.siga.comun.action.PagedSortedAction;
import com.siga.comun.vos.InstitucionVO;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.comun.vos.Vo;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.action.InformesGenericosAction;
import com.siga.informes.form.InformesGenericosForm;


public class ColegiadoAction extends PagedSortedAction {

	private static final String INICIO_FORWARD="inicio";
	private static final String INICIO_AVANZADA="avanzada";
	private static final String RES_BUSQ_AVANZADA="resBusqAvanzada";
	private static final String AVANZADA="AVANZADA";
	private static final String SIMPLE="SIMPLE";

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 * @throws ClsExceptions 
	 */
	public String inicio(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response)   
	throws SIGAException, ClsExceptions {
		CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
		String nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(getUserBean(request).getLocation());
		formulario.setNombreInstitucion(nombreInstitucionAcceso);
		formulario.setIdInstitucion(getUserBean(request).getLocation());

		formulario.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
		formulario.setBusquedaExacta(true);
		formulario.setBotonesBusqueda(
				SearchButtonsConstants.BUSCAR,SearchButtonsConstants.LIMPIAR,SearchButtonsConstants.AVANZADA);
		// jbd Quito los botones de accion para que no aparezcan al principio
		// formulario.setBotonesAccion(ActionButtonsConstants.GENERAR_EXCELS,ActionButtonsConstants.COMUNICAR);
		formulario.setTipoBusqueda(SIMPLE);

		return INICIO_FORWARD;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 * @throws ClsExceptions 
	 */
	public String abrirAvanzada(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response)   
	throws SIGAException, ClsExceptions {

		CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
		String nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(getUserBean(request).getLocation());
		formulario.setNombreInstitucion(nombreInstitucionAcceso);
		formulario.setIdInstitucion(getUserBean(request).getLocation());
		
		formulario.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
		formulario.setListaGruposFijos(getListaGruposFijos(getUserBean(request)));
		formulario.setListaTipoColegiacion(getListaTipoColegiacion(getUserBean(request)));
		formulario.setListaTipoApunteCV(getListaTiposCV(getUserBean(request)));
		formulario.setTipoBusqueda(AVANZADA);
		formulario.setBusquedaExacta(true);
		formulario.setBotonesBusqueda(SearchButtonsConstants.BUSCAR,SearchButtonsConstants.SIMPLE);
		formulario.setBotonesAccion(ActionButtonsConstants.GENERAR_EXCELS,ActionButtonsConstants.COMUNICAR);
		return INICIO_AVANZADA;
	}


	public String buscar(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response)   
	throws SIGAException, ClsExceptions {

		//se recupera el vo de paginacion para poder actualizar el tamaño total
		//de la lista devuelta en el bean de paginacion del formulario
		PagedVo pagedVo =  formulario.toPagedVo();
		SortedVo sortedVo = formulario.toSortedVo();

		//Obtiene el servicio que se va a ejecutar
		ColegiadoService service = (ColegiadoService) getBusinessManager().getService(ColegiadoService.class);
		//llama el metodo del servicio
		List<Vo> lista = service.buscar(formulario.toVO(), pagedVo, sortedVo, 
				getUserBean(request).getLanguage(), getUserBean(request).getLocation());

		//actualiza la lista de resultados y su tamaño para que el display tag se muestre correctamente
		formulario.setTable(lista);
		formulario.fromPagedVo(pagedVo);
		formulario.fromSortedVo(sortedVo);

		service.updateTelefonosColegiados(lista);
		actualizaFechaEstadoColegial(lista);
		formulario.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
		formulario.setBotonesBusqueda(
				SearchButtonsConstants.BUSCAR,SearchButtonsConstants.LIMPIAR,SearchButtonsConstants.AVANZADA);
		if (!lista.isEmpty()){
			formulario.setBotonesAccion(ActionButtonsConstants.GENERAR_EXCELS,ActionButtonsConstants.COMUNICAR);
		}
		formulario.setTipoBusqueda(SIMPLE);

		return INICIO_FORWARD;
	}


	private void actualizaFechaEstadoColegial(List<Vo> lista) throws SIGAException {
		ColegiadoService service = (ColegiadoService) getBusinessManager().getService(ColegiadoService.class);
		for (Vo vo : lista){
			ColegiadoVO colegiado = (ColegiadoVO) vo;
			colegiado.setFechaEstadoColegial(service.getFechaEstadoColegial(vo));
		}
	}

	public String buscarAvanzada(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response)   
	throws SIGAException, ClsExceptions {
		formulario.setBotonesAccion(ActionButtonsConstants.VOLVER);
		buscar(mapping, formulario, request, response);
		//		formulario.setListaGruposFijos(getListaGruposFijos(getUserBean(request)));
		//		formulario.setListaTipoColegiacion(getListaTipoColegiacion(getUserBean(request)));
		//		formulario.setListaTipoApunteCV(getListaTiposCV(getUserBean(request)));
		formulario.setTipoBusqueda(AVANZADA);
		return RES_BUSQ_AVANZADA;
	}

	/**
	 * recupera los datos de un letrado a partir de su id
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	public String ver(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ColegiadoVO colegiado = new ColegiadoVO();
		colegiado.setId(formulario.getId());

		// La pantalla de Datos generales de un colegiado aun no se ha pasado a la nueva estructura. 
		// Por lo tanto, hay que seguir llamando al action correspondiente con
		// los datos en la forma en que este los espera.
		// Esto nos va a ocurrir por toda la aplicacion mientras vayamos cambiandola
		StringBuffer datos = new StringBuffer();
		datos.append(colegiado.getIdPersona());
		datos.append(",");
		datos.append(colegiado.getIdInstitucion());
		datos.append(",");
		datos.append("NINGUNO");
		datos.append(",");
		datos.append("1");
		datos.append(",");
		datos.append("%");

		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datos.toString());

		setCenBusquedaClientesTipo(formulario, request);

		return new BusquedaClientesAction().ver(mapping, clientesForm, request, response);
	}

	private void setCenBusquedaClientesTipo(BusquedaColegiadosForm formulario,
			HttpServletRequest request) {
		if (SIMPLE.equals(formulario.getTipoBusqueda()))
			request.getSession().setAttribute("CenBusquedaClientesTipo","BUSQUEDA_COLEGIADO");
		else
			request.getSession().setAttribute("CenBusquedaClientesTipo","BUSQUEDA_COLEGIADO_AV");
	}

	public String editar(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ColegiadoVO colegiado = new ColegiadoVO();
		colegiado.setId(formulario.getId());

		// La pantalla de Datos generales de un colegiado aun no se ha pasado a la nueva estructura. 
		// Por lo tanto, hay que seguir llamando al action correspondiente con
		// los datos en la forma en que este los espera.
		// Esto nos va a ocurrir por toda la aplicacion mientras vayamos cambiándola
		StringBuffer datos = new StringBuffer();
		datos.append(colegiado.getIdPersona());
		datos.append(",");
		datos.append(colegiado.getIdInstitucion());
		datos.append(",");
		datos.append("NINGUNO");
		datos.append(",");
		datos.append("1");
		datos.append(",");
		datos.append("%");

		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datos.toString());
		setCenBusquedaClientesTipo(formulario, request);

		return new BusquedaClientesAction().editar(mapping, clientesForm, request, response);
	}

	public String informacionLetrado(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ColegiadoVO colegiado = new ColegiadoVO();
		colegiado.setId(formulario.getId());

		// La pantalla de Datos generales de un colegiado aun no se ha pasado a la nueva estructura. 
		// Por lo tanto, hay que seguir llamando al action correspondiente con
		// los datos en la forma en que este los espera.
		// Esto nos va a ocurrir por toda la aplicacion mientras vayamos cambiándola
		StringBuffer datos = new StringBuffer();
		datos.append(colegiado.getIdPersona());
		datos.append(",");
		datos.append(colegiado.getIdInstitucion());
		datos.append(",");
		datos.append("LETRADO");
		datos.append(",");
		datos.append("%");

		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datos.toString());
		clientesForm.setVerFichaLetrado("1");
		setCenBusquedaClientesTipo(formulario, request);

		return new BusquedaClientesAction().editar(mapping, clientesForm, request, response);
	}

	public String generaExcel(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {

		StringBuffer datosBuf = new StringBuffer();
		ColegiadoVO colegiado = new ColegiadoVO();
		if (BusquedaLetradosForm.SELECT_ALL_TRUE.equals(formulario.getSelectAll())){
			List<Vo> lista = getAllPk(formulario, request);
			for(Vo vo: lista){
				colegiado = (ColegiadoVO)vo; 
				datosBuf.append(colegiado.getIdPersona());
				datosBuf.append(",");
				datosBuf.append(colegiado.getIdInstitucion());
				datosBuf.append(",");
				datosBuf.append("2");
				datosBuf.append("#");
			}				
		}
		else{
			for(String pk: formulario.getSelectedElements()){
				colegiado.setId(pk);
				datosBuf.append(colegiado.getIdPersona());
				datosBuf.append(",");
				datosBuf.append(colegiado.getIdInstitucion());
				datosBuf.append(",");
				datosBuf.append("2");
				datosBuf.append("#");
			}
		}

		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datosBuf.toString());

		return new BusquedaClientesAction().generaExcel(mapping, clientesForm, request, response);
	}

	public String comunicar(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {

		InformesGenericosForm informesForm = new InformesGenericosForm();
		String datos = request.getParameter("datosInforme");
		if (datos == null){
			ColegiadoVO colegiado = new ColegiadoVO();
			colegiado.setId(formulario.getId());
			datos = "idPersona=="+colegiado.getIdPersona()+ "##idInstitucion==" +colegiado.getIdInstitucion() ; 
		}

		informesForm.setIdInstitucion(getUserBean(request).getLocation());
		informesForm.setIdInforme(request.getParameter("idInforme"));
		informesForm.setIdTipoInforme("CENSO");
		if (request.getParameter("seleccionados")==null)
			informesForm.setSeleccionados("0");
		else
			informesForm.setSeleccionados(request.getParameter("seleccionados"));
		informesForm.setEnviar("1");
		informesForm.setDescargar("1");
		informesForm.setTipoPersonas("2");
		informesForm.setDatosInforme(datos);

		ActionForward forward = new InformesGenericosAction().executeInternal(mapping, informesForm, request, response);
		request.setAttribute("IdInstitucion",informesForm.getIdInstitucion());
		request.setAttribute("idInforme",informesForm.getIdInforme());
		request.setAttribute("idTipoInforme",informesForm.getIdTipoInforme());
		request.setAttribute("datosInforme",informesForm.getDatosInforme());
		request.setAttribute("enviar",informesForm.getEnviar());
		request.setAttribute("descargar",informesForm.getDescargar());
		request.setAttribute("tipoPersonas",informesForm.getTipoPersonas());
		request.setAttribute("clavesIteracion",informesForm.getClavesIteracion());
		request.setAttribute("action","/CEN_BusquedaColegiados.do");
		request.setAttribute("form","BusquedaColegiadosForm");
		request.setAttribute("accion","comunicar");

		if (forward.getName().equals("seleccionPlantillas")){
			return "seleccionPlantillasNew";
		}
		else if (forward.getName().equals("seleccionPlantillasModal")){
			return "seleccionPlantillasModalNew";
		}

		return forward.getName();
	}

	public String comunicarVarios(ActionMapping mapping, BusquedaColegiadosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {


		InformesGenericosForm informesForm = new InformesGenericosForm();
		String datos = request.getParameter("datosInforme");
		String descargar = request.getParameter("descargar");
		if (descargar == null)
			descargar = "1";
		if (datos == null){
			StringBuffer datosBuf = new StringBuffer();

			int contador = 0;
			if (BusquedaColegiadosForm.SELECT_ALL_TRUE.equals(formulario.getSelectAll())){
				List<Vo> lista = getAllPk(formulario, request);
				contador = lista.size();
				for(Vo vo: lista){
					ColegiadoVO colegiado = (ColegiadoVO)vo; 
					datosBuf.append("idPersona==");
					datosBuf.append(colegiado.getIdPersona());
					datosBuf.append("##idInstitucion==");
					datosBuf.append(colegiado.getIdInstitucion());
					datosBuf.append("%%%");
				}				
			}
			else{
				List<String> lista = formulario.getSelectedElements();
				contador = lista.size();
				for(String pk: lista){
					if (!"".equals(pk)){
						ColegiadoVO colegiado = new ColegiadoVO();
						colegiado.setId(pk);
						datosBuf.append("idPersona==");
						datosBuf.append(colegiado.getIdPersona());
						datosBuf.append("##idInstitucion==");
						datosBuf.append(colegiado.getIdInstitucion());
						datosBuf.append("%%%");
					}
				}
			}
			datos = datosBuf.toString();
			if(contador>50){
				descargar = "0";
			}

		}

		informesForm.setIdInstitucion(getUserBean(request).getLocation());
		informesForm.setIdInforme(request.getParameter("idInforme"));
		informesForm.setIdTipoInforme("CENSO");
		if (request.getParameter("seleccionados")==null)
			informesForm.setSeleccionados("0");
		else
			informesForm.setSeleccionados(request.getParameter("seleccionados"));
		informesForm.setEnviar("1");
		informesForm.setDescargar(descargar);
		informesForm.setTipoPersonas("2");
		informesForm.setDatosInforme(datos);

		ActionForward forward = new InformesGenericosAction().executeInternal(mapping, informesForm, request, response);
		request.setAttribute("IdInstitucion",informesForm.getIdInstitucion());
		request.setAttribute("idInforme",informesForm.getIdInforme());
		request.setAttribute("idTipoInforme",informesForm.getIdTipoInforme());
		request.setAttribute("datosInforme",informesForm.getDatosInforme());
		request.setAttribute("enviar",informesForm.getEnviar());
		request.setAttribute("descargar",informesForm.getDescargar());
		request.setAttribute("tipoPersonas",informesForm.getTipoPersonas());
		request.setAttribute("clavesIteracion",informesForm.getClavesIteracion());
		request.setAttribute("action","/CEN_BusquedaColegiados.do");
		request.setAttribute("form","BusquedaColegiadosForm");
		request.setAttribute("accion","comunicarVarios");

		if (forward.getName().equals("seleccionPlantillas")){
			return "seleccionPlantillasNew";
		}
		else if (forward.getName().equals("seleccionPlantillasModal")){
			return "seleccionPlantillasModalNew";
		}

		return forward.getName();
	}


	private List<Vo> getAllPk(BusquedaColegiadosForm formulario, HttpServletRequest request) throws SIGAException {
		//Obtiene el servicio que se va a ejecutar
		ColegiadoService service = (ColegiadoService) getBusinessManager().getService(ColegiadoService.class);
		//llama el metodo del servicio
		List<Vo> lista = service.buscar(formulario.toVO(), 
				getUserBean(request).getLanguage(), getUserBean(request).getLocation());
		return lista;
	}

	/**
	 * Recuera los grupos fijos asociados a la institucion e idioma del usuario conectado
	 * @param userBean
	 * @return
	 * @throws SIGAException
	 */
	private List<ValueKeyVO> getListaGruposFijos(UsrBean userBean) throws SIGAException{
		CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
		List<ValueKeyVO> listaGruposFijos = service.getGruposFijos(userBean.getLanguage(), userBean.getLocation());
		return listaGruposFijos;
	}

	/**
	 * Recuera los grupos fijos asociados a la institucion e idioma del usuario conectado
	 * @param userBean
	 * @return
	 * @throws SIGAException
	 */
	private List<ValueKeyVO> getListaTipoColegiacion(UsrBean userBean) throws SIGAException{
		CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
		List<ValueKeyVO> listaTipoColegiacion = service.getTipoColegiacion(userBean.getLanguage());
		return listaTipoColegiacion;
	}

	/**
	 * Recuera los grupos fijos asociados a la institucion e idioma del usuario conectado
	 * @param userBean
	 * @return
	 * @throws SIGAException
	 */
	private List<ValueKeyVO> getListaTiposCV(UsrBean userBean) throws SIGAException{
		CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
		List<ValueKeyVO> listaTiposCV = service.getTiposCV(userBean.getLanguage());
		return listaTiposCV;
	}

	private List<ValueKeyVO> getComisiones(String idioma, String idInstitucion, String idTipoCV) throws SIGAException{
		CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
		List<ValueKeyVO> listaTiposCV = service.getComisiones(idioma, idInstitucion, idTipoCV);
		return listaTiposCV;
	}

	private List<ValueKeyVO> getCargos(String idioma, String idInstitucion, String idTipoCV) throws SIGAException{
		CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
		List<ValueKeyVO> listaTiposCV = service.getCargos(idioma, idInstitucion, idTipoCV);
		return listaTiposCV;
	}

	@SuppressWarnings("unchecked")
	public void ajaxGetComisionesCargos(ActionMapping mapping, 		
			BusquedaColegiadosForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException, IOException{

		String idioma = getUserBean(request).getLanguage();
		String idInstitucion = getUserBean(request).getLocation();
		List listaParametros = new ArrayList();

		List<ValueKeyVO> listaComisiones = new ArrayList<ValueKeyVO>();
		listaComisiones.add(new ValueKeyVO("",""));
		listaComisiones.addAll(getComisiones(idioma, idInstitucion, formulario.getTipoApunteCV()));
		listaParametros.add(listaComisiones);

		List<ValueKeyVO> listaCargos = new ArrayList<ValueKeyVO>();
		listaCargos.add(new ValueKeyVO("",""));
		listaCargos.addAll(getCargos(idioma, idInstitucion, formulario.getTipoApunteCV()));
		listaParametros.add(listaCargos);

		respuestaAjax(new AjaxMultipleCollectionXmlBuilder<ValueKeyVO>(), listaParametros,response);
	}

	/**
	 * Recupera los colegios asociados a la institucion <code>idInstitucion</code>
	 * @param idInstitucion Institucion para la cual se buscan sus colegios asociados
	 * @return Una lista con los colegios dependientes de la institucion, o <code>null</code> si la institucion
	 * no tiene colegios dependientes, es decir, si es un Colegio y no un Consejo.
	 * @throws SIGAException 
	 */
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
			instituciones = service.getColegiosNoConsejo(idInstitucion);
		}
		return instituciones;
	}


	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<Integer> ajaxGetNColegiado(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetNifList(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetCodigoPostal(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<ColegiadoVO> buscar(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetTelefono(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetCorreoElectronico(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<NombreVO> ajaxGetNombre(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<NombreVO> ajaxGetApellido1(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<NombreVO> ajaxGetApellido2(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

}