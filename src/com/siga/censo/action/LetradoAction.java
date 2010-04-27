package com.siga.censo.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ActionButtonsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.SearchButtonsConstants;
import com.siga.censo.form.BusquedaClientesForm;
import com.siga.censo.form.BusquedaLetradosForm;
import com.siga.censo.service.LetradoService;
import com.siga.censo.vos.LetradoVO;
import com.siga.censo.vos.NombreVO;
import com.siga.comun.action.PagedSortedAction;
import com.siga.comun.vos.Vo;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.action.InformesGenericosAction;
import com.siga.informes.form.InformesGenericosForm;

import es.satec.businessManager.BusinessManager;


public class LetradoAction extends PagedSortedAction {

	private static final String INICIO_FORWARD="inicio";

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public String inicio(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response)   
	throws SIGAException {

		formulario.setIdInstitucion(getUserBean(request).getLocation());
		formulario.setBusquedaExacta(true);
		formulario.setBotonesBusqueda(SearchButtonsConstants.BUSCAR);
		formulario.setBotonesAccion(ActionButtonsConstants.GENERAR_EXCELS,ActionButtonsConstants.COMUNICAR);
		return INICIO_FORWARD;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public String buscar(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response)   
	throws SIGAException {

		BusinessManager bm = getBusinessManager();
		executeServiceMethod(formulario, bm.getService(LetradoService.class),formulario.getAccion());

		formulario.setBotonesBusqueda(SearchButtonsConstants.BUSCAR);
		formulario.setBotonesAccion(ActionButtonsConstants.GENERAR_EXCELS,ActionButtonsConstants.COMUNICAR);

		return INICIO_FORWARD;
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
	public String ver(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		LetradoVO letrado = new LetradoVO();
		letrado.setId(formulario.getId());
		
		// La pantalla de Datos generales de un letrado aun no se ha pasado a la nueva estructura. 
		// Por lo tanto, hay que seguir llamando al action correspondiente con
		// los datos en la forma en que este los espera.
		// Esto nos va a ocurrir por toda la aplicacion mientras vayamos cambiandola
		StringBuffer datos = new StringBuffer();
		datos.append(letrado.getIdPersona());
		datos.append(",");
		datos.append(letrado.getIdInstitucion());
		datos.append(",");
		datos.append("LETRADO");
		datos.append(",");
		datos.append("%");

		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datos.toString());
		request.getSession().setAttribute("CenBusquedaClientesTipo","BUSQUEDA_LETRADO");

		return new BusquedaClientesAction().ver(mapping, clientesForm, request, response);
	}

	public String editar(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		LetradoVO letrado = new LetradoVO();
		letrado.setId(formulario.getId());
		
		// La pantalla de Datos generales de un letrado aun no se ha pasado a la nueva estructura. 
		// Por lo tanto, hay que seguir llamando al action correspondiente con
		// los datos en la forma en que este los espera.
		// Esto nos va a ocurrir por toda la aplicacion mientras vayamos cambiándola
		StringBuffer datos = new StringBuffer();
		datos.append(letrado.getIdPersona());
		datos.append(",");
		datos.append(letrado.getIdInstitucion());
		datos.append(",");
		datos.append("LETRADO");
		datos.append(",");
		datos.append("%");
		
		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datos.toString());
		request.getSession().setAttribute("CenBusquedaClientesTipo","BUSQUEDA_LETRADO");

		return new BusquedaClientesAction().editar(mapping, clientesForm, request, response);
	}
	
	public String generaExcel(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	 throws ClsExceptions, SIGAException  {

		StringBuffer datosBuf = new StringBuffer();
		LetradoVO letrado = new LetradoVO();
		if (BusquedaLetradosForm.SELECT_ALL_TRUE.equals(formulario.getSelectAll())){
			List<Vo> lista = getAllPk(formulario, request);
			for(Vo vo: lista){
				letrado = (LetradoVO)vo; 
				datosBuf.append(letrado.getIdPersona());
				datosBuf.append(",");
				datosBuf.append(letrado.getIdInstitucion());
				datosBuf.append(",");
				datosBuf.append("2");
				datosBuf.append("#");
			}				
		}
		else{
			for(String pk: formulario.getSelectedElements()){
				letrado.setId(pk);
				datosBuf.append(letrado.getIdPersona());
				datosBuf.append(",");
				datosBuf.append(letrado.getIdInstitucion());
				datosBuf.append(",");
				datosBuf.append("2");
				datosBuf.append("#");
			}
		}
		BusquedaClientesForm clientesForm = new BusquedaClientesForm();
		clientesForm.setTablaDatosDinamicosD(datosBuf.toString());
		
		return new BusquedaClientesAction().generaExcel(mapping, clientesForm, request, response);
	}
	
	public String comunicar(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {

		InformesGenericosForm informesForm = new InformesGenericosForm();
		String datos = request.getParameter("datosInforme");
		if (datos == null){
			LetradoVO letrado = new LetradoVO();
			letrado.setId(formulario.getId());
			datos = "idPersona=="+letrado.getIdPersona()+ "##idInstitucion==" +letrado.getIdInstitucion() ; 
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
		request.setAttribute("action","/CEN_BusquedaLetradosNew.do");
		request.setAttribute("form","BusquedaLetradosForm");
		request.setAttribute("accion","comunicar");

		if (forward.getName().equals("seleccionPlantillas")){
			return "seleccionPlantillasNew";
		}
		else if (forward.getName().equals("seleccionPlantillasModal")){
			return "seleccionPlantillasModalNew";
		}

		return forward.getName();
	}

	public String comunicarVarios(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {


		InformesGenericosForm informesForm = new InformesGenericosForm();
		String datos = request.getParameter("datosInforme");
		String descargar = request.getParameter("descargar");
		if (descargar == null)
			descargar = "1";
		if (datos == null){
			StringBuffer datosBuf = new StringBuffer();

			int contador = 0;
			if (BusquedaLetradosForm.SELECT_ALL_TRUE.equals(formulario.getSelectAll())){
				List<Vo> lista = getAllPk(formulario, request);
				contador = lista.size();
				for(Vo vo: lista){
					LetradoVO letrado = (LetradoVO)vo; 
					datosBuf.append("idPersona==");
					datosBuf.append(letrado.getIdPersona());
					datosBuf.append("##idInstitucion==");
					datosBuf.append(letrado.getIdInstitucion());
					datosBuf.append("%%%");
				}				
			}
			else{
				List<String> lista = formulario.getSelectedElements();
				contador = lista.size();
				for(String pk: lista){
					if (!"".equals(pk)){
						LetradoVO letrado = new LetradoVO();
						letrado.setId(pk);
						datosBuf.append("idPersona==");
						datosBuf.append(letrado.getIdPersona());
						datosBuf.append("##idInstitucion==");
						datosBuf.append(letrado.getIdInstitucion());
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
		request.setAttribute("action","/CEN_BusquedaLetradosNew.do");
		request.setAttribute("form","BusquedaLetradosForm");
		request.setAttribute("accion","comunicarVarios");

		if (forward.getName().equals("seleccionPlantillas")){
			return "seleccionPlantillasNew";
		}
		else if (forward.getName().equals("seleccionPlantillasModal")){
			return "seleccionPlantillasModalNew";
		}

		return forward.getName();
	}


	private List<Vo> getAllPk(BusquedaLetradosForm formulario, HttpServletRequest request) throws SIGAException {
		//Obtiene el servicio que se va a ejecutar
		LetradoService service = (LetradoService) getBusinessManager().getService(LetradoService.class);
		//llama el metodo del servicio
		List<Vo> lista = service.buscar(formulario.toVO(), 
				getUserBean(request).getLanguage(), getUserBean(request).getLocation());
		return lista;
	}
	
/*	
	public String comunicarVarios(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {
		
		StringBuffer datos = new StringBuffer();
		LetradoVO letrado = new LetradoVO();
		for(String pk: formulario.getSelectedElements()){
			letrado.setId(pk);
			datos.append("idPersona==");
			datos.append(letrado.getIdPersona());
			datos.append("##idInstitucion==");
			datos.append(letrado.getIdInstitucion());
			datos.append("%%%");
		}
		
		InformesGenericosForm informesForm = new InformesGenericosForm();
		informesForm.setTablaDatosDinamicosD(datos.toString());
		informesForm.setIdInstitucion(getUserBean(request).getLocation());
		informesForm.setIdInforme("");
		informesForm.setIdTipoInforme("CENSO");
		informesForm.setDatosInforme(datos.toString());
		informesForm.setSeleccionados("0");
		informesForm.setEnviar("1");
		informesForm.setTipoPersonas("2");
		if(formulario.getSelectedElements().size()>50){
			informesForm.setDescargar("0");
		}
		else{
			informesForm.setDescargar("1");
		}
		
		ActionForward forward = new InformesGenericosAction().executeInternal(mapping, informesForm, request, response);
		return forward.getName();
	}

	public String comunicar(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions, SIGAException  {
		
		StringBuffer datos = new StringBuffer();
		LetradoVO letrado = new LetradoVO();
		letrado.setId(formulario.getId());

		datos.append("idPersona==");
		datos.append(letrado.getIdPersona());
		datos.append("##idInstitucion==");
		datos.append(letrado.getIdInstitucion());
		
		InformesGenericosForm informesForm = new InformesGenericosForm();
		informesForm.setTablaDatosDinamicosD(datos.toString());
		informesForm.setIdInstitucion(getUserBean(request).getLocation());
		informesForm.setIdInforme("");
		informesForm.setIdTipoInforme("CENSO");
		informesForm.setDatosInforme(datos.toString());
		informesForm.setSeleccionados("0");
		informesForm.setEnviar("1");
		informesForm.setTipoPersonas("2");
		informesForm.setDescargar("1");
		
		ActionForward forward = new InformesGenericosAction().executeInternal(mapping, informesForm, request, response);
		return forward.getName();
	}
*/

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
	 * @throws IOException 
	 */
	public void ajaxGetNifList(ActionMapping mapping, BusquedaLetradosForm formulario, HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<String> listaNifs = new ArrayList<String>();
		listaNifs.add("11");
		listaNifs.add("222");
		listaNifs.add("3333");
		respuestaAjaxString(new AjaxXmlBuilder(), listaNifs, response);
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