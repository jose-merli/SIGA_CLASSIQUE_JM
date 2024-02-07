package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.env.ComunicacionesVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.TransformBeanToForm;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.comun.VoUiService;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.envios.form.EntradaEnviosForm;
import com.siga.envios.service.SalidaEnviosService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ComunicacionesForm;
import com.siga.gratuita.form.service.ComunicacionesVoService;

import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author jorgeta 
 * @date   01/08/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class ComunicacionesAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else{
			if(miForm.getModo()!=null && miForm.getModo().equalsIgnoreCase("listadoComunicacionesAjax"))
				return  mapping.findForward(listadoComunicacionesAjax(mapping, miForm, request, response));
			else
				return super.executeInternal(mapping, formulario, request, response);
		}
	}
	

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		if(mapping.getPath()!=null && mapping.getPath().equalsIgnoreCase("/CEN_Comunicaciones")){
			return abrirComunicacionesFicha(mapping, formulario, request, response);
		}else{
			if(mapping.getParameter()!=null && mapping.getParameter().equals("EJG")){
				return abrirEJG(mapping, formulario, request, response);
			}else {
				return abrirDesignacion(mapping, formulario, request, response);
			}
		}
	}
	protected String abrirComunicacionesFicha(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usrBean = this.getUserBean(request);
		String forward = "inicio";
		try {
			
			Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
			String idInstitucion = request.getParameter("idInstitucion").toString();
			
			String idPersona = request.getParameter("idPersona").toString();
			String accion = (String)request.getParameter("accion");
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
					 (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals("") )) {
					request.setAttribute("modoVolver",accion);
					return "clienteNoExiste";
			}
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String nombre = personaAdm.obtenerNombreApellidos(idPersona);
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales( Long.valueOf(idPersona),Integer.valueOf(idInstitucion));
			String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			String estado = clienteAdm.getEstadoColegial(idPersona,idInstitucion);
		
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			datosColegiado = new Hashtable();
			datosColegiado.put("NOMBRECOLEGIADO",nombre);
			datosColegiado.put("NUMEROCOLEGIADO",numero);
			datosColegiado.put("ESTADOCOLEGIAL",estado!=null?estado:"");
			request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
				
			
			String nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
			String numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
			String estadoColegial = (String)datosColegiado.get("ESTADOCOLEGIAL");
			StringBuilder descripcioncolegiado = new StringBuilder();
			descripcioncolegiado.append(nombrePestanha);
			descripcioncolegiado.append(" ");
			descripcioncolegiado.append(UtilidadesString.getMensajeIdioma(usrBean, "censo.fichaCliente.literal.colegiado"));
			descripcioncolegiado.append(" ");
			descripcioncolegiado.append(numeroPestanha);
			descripcioncolegiado.append(" ");
			descripcioncolegiado.append("(");
			if(estadoColegial!=null && !estadoColegial.equals("")){
				descripcioncolegiado.append(estadoColegial);
			}else{
				descripcioncolegiado.append(UtilidadesString.getMensajeIdioma(usrBean, "censo.busquedaClientes.literal.sinEstadoColegial"));
			}
			descripcioncolegiado.append(")");
			request.setAttribute("descripcionColegiado", descripcioncolegiado.toString());
			request.setAttribute("busquedaVolver", "N");
			return forward;
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
			throw new SIGAException(error,e);
			
		}
		
	}
	protected String listadoComunicacionesAjax(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usrBean = this.getUserBean(request);
		ComunicacionesForm comunicacionesForm = (ComunicacionesForm)formulario;
		
		String idInstitucion = request.getParameter("idInstitucion").toString();
		String idPersona = request.getParameter("idPersona").toString();
		
		String forward = "listado";
	    String pagina = request.getParameter("pagina");
        BusinessManager businessManager = getBusinessManager();
        SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
        
        VoUiService<ComunicacionesForm, ComunicacionesVo> voService = new ComunicacionesVoService();
        List<ComunicacionesForm> comunicacionesForms = null;
        
		
		try {
			int rowNumPageSize = 0;
			int page = 1;
			String registrosPorPagina = null;
			Short numSolicitudAceptadas = null;
			if(pagina ==null){
				ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				registrosPorPagina = properties.returnProperty("paginador.registrosPorPagina", true);
				rowNumPageSize = Integer.parseInt(registrosPorPagina);
				numSolicitudAceptadas = salidaEnviosService.getNumComunicacionesLetrado(Long.valueOf(idPersona), Short.valueOf(idInstitucion));
				request.setAttribute("paginaSeleccionada", page);
				request.setAttribute("totalRegistros", numSolicitudAceptadas.toString());
				request.setAttribute("registrosPorPagina", registrosPorPagina);
			}else{
				page = Integer.parseInt(request.getParameter("pagina"));
				request.setAttribute("paginaSeleccionada", page);
				numSolicitudAceptadas = Short.valueOf(request.getParameter("totalRegistros"));
				request.setAttribute("totalRegistros", request.getParameter("totalRegistros"));
				registrosPorPagina = request.getParameter("registrosPorPagina");
				request.setAttribute("registrosPorPagina",registrosPorPagina );
				rowNumPageSize = Integer.parseInt(registrosPorPagina);
			}
			
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
			HashMap<String, String> paginadorHashMap = new HashMap<String, String>();
			paginadorHashMap.put("pagina", String.valueOf(page));
			paginadorHashMap.put("totalRegistros", numSolicitudAceptadas.toString());
			paginadorHashMap.put("registrosPorPagina", registrosPorPagina);
			comunicacionesForm.setDatosPaginador(paginadorHashMap);
			
			request.getSession().setAttribute(identificadorFormularioBusqueda,comunicacionesForm.clone());
			
			int rowNumStart = ((page - 1) * rowNumPageSize);
			
			
			comunicacionesForms =  voService.getVo2FormList(salidaEnviosService.getComunicacionesLetrado(Long.valueOf(idPersona),usrBean.getLanguage(),Short.valueOf(idInstitucion) ,rowNumStart,rowNumPageSize));	
			request.setAttribute("mensajeSuccess", "");
			request.setAttribute("comunicaciones", comunicacionesForms);
			
			
			/*List<ComunicacionesForm> comunicaciones = new ArrayList<ComunicacionesForm>();
			ComunicacionesForm comunicacion = new ComunicacionesForm();
			comunicacion.setIdEnvio("45454545454");
			comunicacion.setTipo("Documentacion");
			comunicacion.setFecha("15/06/2016 12:45:35");
			comunicacion.setNombre("Comunicaciones de Oficio");
			comunicacion.setAsunto("Designa provisional 2016/ 00021");
			List<String> documentos = new ArrayList<String>();
			documentos.add("cartaOficioCliente3_1684-0_00021_2016_843_2016_2014_0_20160712022245447.doc");
			documentos.add("cartaOficioCliente3_1684-0_00021_2016_843_2016_2014_1_20160712022245447.doc");
			comunicacion.setDocumentos(documentos);
			comunicaciones.add(comunicacion);
			comunicaciones.add(comunicacion);
			
			request.setAttribute("comunicaciones", comunicaciones);
*/
			
			
			
			return forward;
		}catch (Exception e){
			comunicacionesForms = new ArrayList<ComunicacionesForm>();
			request.setAttribute("comunicaciones", comunicacionesForms);
			String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
			request.setAttribute("mensajeSuccess", error);
			throw new SIGAException(error,e);
			
		}
		
	}
	
	
	
	protected String abrirEJG(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		
		UsrBean usrBean = this.getUserBean(request);
		
		
//		comunicacionesSalida
		ComunicacionesForm comunicacionesForm = (ComunicacionesForm)formulario;
		String accion = (String)request.getSession().getAttribute("accion");
		comunicacionesForm.setModo(accion);
		
		if(request.getParameter("ANIO")!=null && !request.getParameter("ANIO").toString().equals("")){
			comunicacionesForm.setEjgAnio(request.getParameter("ANIO").toString());
			comunicacionesForm.setEjgIdInstitucion(request.getParameter("IDINSTITUCION").toString());
			comunicacionesForm.setEjgIdTipo(request.getParameter("IDTIPOEJG").toString());
			comunicacionesForm.setEjgNumero(request.getParameter("NUMERO").toString());
			comunicacionesForm.setSolicitante(request.getParameter("solicitante").toString());
			comunicacionesForm.setEjgNumEjg(request.getParameter("ejgNumEjg").toString());
			comunicacionesForm.setAnio(comunicacionesForm.getEjgAnio());
			comunicacionesForm.setCodigoDesignaNumEJG(comunicacionesForm.getEjgNumEjg());
		}
		
		String forward = "inicio";
		List<EntradaEnviosForm> comunicacionesEntrada = new ArrayList<EntradaEnviosForm>();
		List<DefinirEnviosForm> comunicacionesSalida = new ArrayList<DefinirEnviosForm>();

		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			comunicacionesForm.setComisionAJG(userBean.isComision()?"1":"0");
			
			
			
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			List<EnvEnvios> salidaEnvios = salidaEnviosService.getComunicacionesEJG(Integer.parseInt(comunicacionesForm.getEjgIdInstitucion()),
					Short.parseShort(comunicacionesForm.getEjgAnio()),Short.parseShort(comunicacionesForm.getEjgIdTipo()),Integer.parseInt(comunicacionesForm.getEjgNumero()),
					Short.parseShort(comunicacionesForm.getComisionAJG()));
			
			for(EnvEnvios salida: salidaEnvios){
				DefinirEnviosForm salidaEnviosForm = TransformBeanToForm.getSalidaEnviosForm(salida);
				salidaEnviosForm.setEstado(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getEstado(), userBean));
				salidaEnviosForm.setTipoEnvio(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getTipoEnvio(), userBean));
				
				salidaEnviosForm.setModo(comunicacionesForm.getModo());
				comunicacionesSalida.add(salidaEnviosForm);
			}
			GenParametrosAdm paramAdm = new GenParametrosAdm (userBean);
			String prefijoExpedienteCajg = paramAdm.getValor (comunicacionesForm.getEjgIdInstitucion(), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
			request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
			//Miramos si tienem permiso de envio sya que si no tiene no le vamos a mostrar los iconos de envios
			request.setAttribute("PERMISOENVIOS",ClsConstants.DB_TRUE);
			String accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
			if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				//Miramos si tienen acceso a las comunicaciones de designaiones o ejgs, en tal caso se les permite enviar
				request.setAttribute("PERMISOENVIOS",ClsConstants.DB_FALSE);

			}
			//hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		request.setAttribute("comunicacionesEntrada", comunicacionesEntrada);     
		request.setAttribute("comunicacionesSalida", comunicacionesSalida);
		
		return forward;
	}
	
	protected String abrirDesignacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		
		UsrBean usrBean = this.getUserBean(request);
		
		
//		comunicacionesSalida
		ComunicacionesForm comunicacionesForm = (ComunicacionesForm)formulario;
		String accion = (String)request.getSession().getAttribute("Modo");
		comunicacionesForm.setModo(accion);
		/*	UtilidadesHash.set(resultado,ScsDesignaBean.C_ANIO, 				(String)request.getParameter("ANIO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_NUMERO, 				(String)request.getParameter("NUMERO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDTURNO,				(String)request.getParameter("IDTURNO"));*/
		if(request.getParameter("ANIO")!=null && !request.getParameter("ANIO").toString().equals("")){
			comunicacionesForm.setDesignaAnio(request.getParameter("ANIO").toString());
			comunicacionesForm.setDesignaIdInstitucion(request.getParameter("IDINSTITUCION").toString());
			comunicacionesForm.setDesignaIdTurno(request.getParameter("IDTURNO").toString());
			comunicacionesForm.setDesignaNumero(request.getParameter("NUMERO").toString());
			comunicacionesForm.setAnio(comunicacionesForm.getDesignaAnio());
			comunicacionesForm.setCodigoDesignaNumEJG(request.getParameter("designaCodigo")!=null?request.getParameter("designaCodigo"):request.getParameter("DESIGNACODIGO"));
			comunicacionesForm.setSolicitante(request.getParameter("solicitante").toString());
		}
		
		String forward = "inicio";
		List<EntradaEnviosForm> comunicacionesEntrada = new ArrayList<EntradaEnviosForm>();
		List<DefinirEnviosForm> comunicacionesSalida = new ArrayList<DefinirEnviosForm>();

		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			
			
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			comunicacionesForm.setComisionAJG(userBean.isComision()?"1":"0");
			
			
			
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			List<EnvEnvios> salidaEnvios = salidaEnviosService.getComunicacionesDesigna(Integer.parseInt(comunicacionesForm.getDesignaIdInstitucion()),
					Short.parseShort(comunicacionesForm.getDesignaAnio()),Short.parseShort(comunicacionesForm.getDesignaIdTurno()),Integer.parseInt(comunicacionesForm.getDesignaNumero())
					,Short.parseShort(comunicacionesForm.getComisionAJG()));
			
			for(EnvEnvios salida: salidaEnvios){
				DefinirEnviosForm salidaEnviosForm = TransformBeanToForm.getSalidaEnviosForm(salida);
				salidaEnviosForm.setEstado(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getEstado(), userBean));
				salidaEnviosForm.setTipoEnvio(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getTipoEnvio(), userBean));
				
				salidaEnviosForm.setModo(comunicacionesForm.getModo());
				comunicacionesSalida.add(salidaEnviosForm);
			}
			
			//Miramos si tienem permiso de envio sya que si no tiene no le vamos a mostrar los iconos de envios
			request.setAttribute("PERMISOENVIOS",ClsConstants.DB_TRUE);
			String accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
			if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				//Miramos si tienen acceso a las comunicaciones de designaiones o ejgs, en tal caso se les permite enviar
				request.setAttribute("PERMISOENVIOS",ClsConstants.DB_FALSE);

			}
			//hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);			
			

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		request.setAttribute("comunicacionesEntrada", comunicacionesEntrada);     
		request.setAttribute("comunicacionesSalida", comunicacionesSalida);
		
		return forward;
	}
	
	
}