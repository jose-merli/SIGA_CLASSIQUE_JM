/**
 * @author jose.barrientos
 *
 * @version 19-10-2011
 */

package com.siga.gratuita.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.services.scs.EjgActaBusinessService;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.vo.scs.EjgActaVo;
import org.redabogacia.sigaservices.app.vo.scs.EjgVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsActaComisionAdm;
import com.siga.beans.ScsActaComisionBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.beans.ScsEstadoEJGBean;
import com.siga.censo.form.service.EjgActaVoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActaComisionForm;
import com.siga.gratuita.form.EjgActaForm;

import es.satec.businessManager.BusinessManager;


/**
 * @author jose.barrientos
 *
 */
public class ActaComisionAction extends MasterAction{


	/** 
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	@Override
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscar")){
					// En el buscar inicial borramos el paginador para que se haga la busqueda siempre
					ActaComisionForm form = (ActaComisionForm)miForm;
					form.reset(new String[]{"datosPaginador"});
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPor(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscarPor")){
					mapDestino = buscarPor(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("modificar")){
					mapDestino = modificar(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("ver")){
					mapDestino = editar(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("edicionMasiva")){
					mapDestino = edicionMasiva(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("volver")){
					mapDestino = volver(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("updateMasivo")){
					mapDestino = updateMasivo(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("procesarRetirados")){
					mapDestino = procesarRetirados(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("geJQuerytNumActaComision")){
					geJQuerytNumActaComision(mapping, miForm, request, response);
					return null;
				
				}else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null){ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE")){
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
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
	 * Funcion que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	@Override
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ActaComisionForm actaForm = (ActaComisionForm)formulario;
		actaForm.reset();
		request.getSession().removeAttribute("DATOSFORMULARIO");
		return "inicio";
	}
	
	/** 
	 * Funcion que implementa el modo volver.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	private String volver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		request.setAttribute("buscarLista", "1");
		return "inicio";
	}
	
	/** 
	 * Funcion que implementa el modo nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 * @throws ClsExceptions 
	 */	
	@Override
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ActaComisionForm actasForm = (ActaComisionForm) formulario;
		actasForm.reset();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsActaComisionAdm actasAdm = new ScsActaComisionAdm(usr);
		String idInstitucion = usr.getLocation();
		Date hoy = new Date();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
		String anio = sdf2.format(hoy);
		int numActa =1;
		try {
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
	    	   
			String sufijosActa = genParametrosService.getValorParametroWithNull(Short.valueOf(idInstitucion), PARAMETRO.CAJG_SUFIJO_ACTAS,MODULO.SCS);
			if(sufijosActa!=null){
				
				String sufijos[] = sufijosActa.split(",");
				if(sufijos!=null && sufijos.length>0){
					numActa = actasAdm.getNuevoNumActa(idInstitucion,anio,sufijos[0]);
					request.setAttribute("sufijos",sufijos );
				}else{
					numActa = actasAdm.getNuevoNumActa(idInstitucion,anio);
					request.setAttribute("sufijos",new String[]{} );
				}
			}else{
				numActa = actasAdm.getNuevoNumActa(idInstitucion,anio);
				request.setAttribute("sufijos",new String[]{} );
			}
			
			actasForm.setAnioActa(anio);
			
			actasForm.setNumActa(String.valueOf(numActa));
		} catch (ClsExceptions e) {
			throw new SIGAException(e);
		}
		
		return "nuevo";
	}

	protected void geJQuerytNumActaComision(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, JSONException, IOException {
		UsrBean usr = this.getUserBean(request);
		String anioActa = request.getParameter("anioActa");
		String sufijo = request.getParameter("sufijo");
		JSONObject json = new JSONObject();
		ScsActaComisionAdm actasAdm = new ScsActaComisionAdm(usr);
		int numActa = actasAdm.getNuevoNumActa(usr.getLocation(), anioActa, sufijo);

		json.put("numActa", numActa);

		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
		response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString());

	}
	/** 
	 * Funcion que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @throws SIGAException 
	 * @throws ClsExceptions 
	 */	
	@Override
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ActaComisionForm actaForm = (ActaComisionForm) formulario;
		ScsActaComisionBean actaBean = new ScsActaComisionBean();
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		
		UserTransaction tx=null;
		
		
		
		try {

			if(actaForm.getIdPresidente()!=null && !actaForm.getIdPresidente().equalsIgnoreCase(""))
				actaBean.setIdPresidente(Integer.valueOf(actaForm.getIdPresidente()));
			if(actaForm.getIdSecretario()!=null && !actaForm.getIdSecretario().equalsIgnoreCase(""))
				actaBean.setIdSecretario(Integer.valueOf(actaForm.getIdSecretario()));
			actaBean.setMiembrosComision(actaForm.getMiembros());
			actaBean.setObservaciones(actaForm.getObservaciones());
			actaBean.setPendientes(actaForm.getPendientes());
			
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			
			
			
			if( actaForm.getFechaResolucion()!=null && 
				!actaForm.getFechaResolucion().equalsIgnoreCase("")){
				actaBean.setFechaResolucionCAJG(UtilidadesString.formatoFecha(actaForm.getFechaResolucion(), ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA) );
			}
			if( actaForm.getFechaReunion()!=null && 
				!actaForm.getFechaReunion().equalsIgnoreCase("")){
				actaBean.setFechaReunion(UtilidadesString.formatoFecha(actaForm.getFechaReunion(), ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA) );
			}
			if(actaForm.getHoraFin()!=null && actaForm.getMinuFin()!=null && 
				!actaForm.getHoraFin().equalsIgnoreCase("") && 
				!actaForm.getMinuFin().equalsIgnoreCase("") ){
				Calendar horaFin = Calendar.getInstance();
				horaFin.set(Calendar.HOUR_OF_DAY, Integer.valueOf(actaForm.getHoraFin()));
				horaFin.set(Calendar.MINUTE, Integer.valueOf(actaForm.getMinuFin()));
				actaBean.setHoraFinReunion(sdf.format(horaFin.getTime()));
			}
			if(actaForm.getHoraIni()!=null && actaForm.getMinuIni()!=null && 
				!actaForm.getHoraIni().equalsIgnoreCase("") && 
				!actaForm.getMinuIni().equalsIgnoreCase("") ){
				Calendar horaInicio = Calendar.getInstance();
				horaInicio.set(Calendar.HOUR_OF_DAY, Integer.valueOf(actaForm.getHoraIni()));
				horaInicio.set(Calendar.MINUTE, Integer.valueOf(actaForm.getMinuIni()));
				actaBean.setHoraInicioReunion(sdf.format(horaInicio.getTime()));
			}
			tx = usr.getTransaction();		
			tx.begin();
			
			actaBean.setIdActa(actaAdm.getNuevoIdActa(usr.getLocation(), actaForm.getAnioActa()));
			actaBean.setIdInstitucion(Integer.valueOf(usr.getLocation()));
			StringBuilder numeroActa = new StringBuilder(actaForm.getNumActa());
			if(!actaForm.getSufijoNumActa().equals("")){
				numeroActa.append(actaForm.getSufijoNumActa());
				
			}
			actaBean.setNumeroActa(numeroActa.toString().trim());
			actaBean.setAnioActa(Integer.valueOf(actaForm.getAnioActa()));
			
			actaAdm.insert(actaBean);
			
			tx.commit();
			
		} catch (Exception e) {
			throw new SIGAException("Error al crear el nuevo acta.",e);
		}
		
		return exitoModal("messages.inserted.success", request);
	}
	
	/** 
	 * Funcion que implementa el modo editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 * @throws ClsExceptions 
	 */	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		ActaComisionForm actaForm = (ActaComisionForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		String idActa = "";
		String idInstitucion = "";
		String anioActa = "";
		Hashtable<String, String> datosActa = null;
//		Vector ejgsRelacionados = null;
		try {
			// Si editamos desde la lista los datos vendran en ocultos
			Vector ocultos = actaForm.getDatosTablaOcultos(0);
			formulario = new MasterForm();
			// Recuperamos la clave del acta
			idActa = (String) ocultos.get(0);
			idInstitucion = (String) ocultos.get(1);
			anioActa = (String) ocultos.get(2);

		} catch (Exception e) {
			// Si editamos desde la la pesta�a de resolucion los datos vendran
			// en formulario
			idActa = actaForm.getIdActa();
			idInstitucion = actaForm.getIdInstitucion();
			anioActa = actaForm.getAnioActa();
		}

		EjgActaForm ejgActaForm = new EjgActaForm();
		ejgActaForm.setIdInstitucionActa(idInstitucion);
		ejgActaForm.setIdActa(idActa);
		ejgActaForm.setAnioActa(anioActa);
		ejgActaForm.setLongitudNumEjg(longitudNumEjg);

		EjgActaBusinessService ejgActaBusinessService = (EjgActaBusinessService) getBusinessManager().getService(EjgActaBusinessService.class);
		try {
			// Con la clave recuperamos sus datos
			List<EjgActaForm> ejgActaForms = new EjgActaVoUiService().getVo2FormList(ejgActaBusinessService.getList(new EjgActaVoUiService().getForm2Vo(ejgActaForm)));

			datosActa = actaAdm.getDatosActa(idActa, anioActa, idInstitucion);
//			ejgsRelacionados = actaAdm.getListadoEJGActa(idActa, anioActa, idInstitucion, longitudNumEjg);

			request.setAttribute("datosActa", datosActa);
			request.setAttribute("ejgsActaFormList", ejgActaForms);
			//request.setAttribute("ejgsRelacionados", ejgsRelacionados);
			String accion = actaForm.getModo();
			if (accion != null && accion.equalsIgnoreCase("ver")) {
				request.setAttribute("modo", "consulta");
				request.setAttribute("modoActa", "ver");

			} else if (accion != null && accion.equalsIgnoreCase("editar")) {
				request.setAttribute("modo", "editar");
				request.setAttribute("modoActa", "editar");
			}

			String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			Vector informeBeans = adm.obtenerInformesTipo(this.getUserBean(request).getLocation(), "ACTAC", null, null);
			if (informeBeans != null && informeBeans.size() > 1) {
				informeUnico = ClsConstants.DB_FALSE;

			}

			request.setAttribute("informeUnico", informeUnico);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return "editar";
	}

	/**
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	@Override
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {

		ScsActaComisionAdm actaAdm=new ScsActaComisionAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ActaComisionForm actaForm =(ActaComisionForm)formulario;
		HashMap miHash= new HashMap();
		
		miHash.put(ScsActaComisionBean.C_ANIOACTA, actaForm.getAnioActa());
		miHash.put(ScsActaComisionBean.C_NUMEROACTA, actaForm.getNumeroActa());
		miHash.put(ScsActaComisionBean.C_FECHARESOLUCION, actaForm.getFechaResolucion());
		miHash.put(ScsActaComisionBean.C_FECHAREUNION, actaForm.getFechaReunion());
		miHash.put(ScsActaComisionBean.C_IDPRESIDENTE, actaForm.getIdPresidente());
		miHash.put(ScsActaComisionBean.C_IDSECRETARIO, actaForm.getIdSecretario());
		miHash.put(ScsActaComisionBean.C_IDINSTITUCION, usr.getIdInstitucionComision());
		
		String consulta= "";
		Vector datos = new Vector();

		try {
			
			HashMap databackup = (HashMap) actaForm.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){
				Paginador paginador = (Paginador)databackup.get("paginador");

				//Si no es la primera llamada, obtengo la p�gina del request y la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();

				Paginador resultado = null;
				resultado=actaAdm.getBusquedaActas( miHash);
				if (resultado!=null){ 
					//Si no es la primera llamada, obtengo la p�gina del request y la busco con el paginador
					String pagina = request.getParameter("pagina");
					if(pagina!=null && !pagina.equalsIgnoreCase("")){
						datos = resultado.obtenerPagina(Integer.parseInt(pagina));
					}else{
						datos = resultado.obtenerPagina(1);
					}
				}
				databackup.put("paginador",resultado);
				databackup.put("datos",datos);
				request.setAttribute("datos", datos);
				
				actaForm.setDatosPaginador(databackup);
				
			}
		
			// En "DATOSFORMULARIO" almacenamos los datos de la busqueda			
			miHash.put("BUSQUEDAREALIZADA","1");
			request.getSession().setAttribute("DATOSFORMULARIO",miHash);	
			request.getSession().setAttribute("BUSQUEDAREALIZADA", consulta.toString());

		}catch (Exception e) {
			throw new SIGAException("Ha ocurrido un error al realizar la busqueda.",e);
		}

		return "resultado";
	}
	
	/** 
	 * Funcion que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @throws SIGAException 
	 * @throws ClsExceptions 
	 */	
	@Override
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ActaComisionForm actaForm = (ActaComisionForm) formulario;
		ScsActaComisionBean actaBean = new ScsActaComisionBean();
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
		ScsEJGAdm ejgAdm = new ScsEJGAdm(usr);
		UserTransaction tx=null;
		//Requisito 2. Se modificara la fecha de resolucion cuando no queden expedientes sin resolver. Esto a su vez depende del parametro GEN_PARAM_VALIDAR_OBLIGATORIEDAD_RESOLUCION
		String detalleEjgsNoResueltos = null;
		try {

			// Campos clave
			actaBean.setIdActa(Integer.parseInt(actaForm.getIdActa()));
			actaBean.setIdInstitucion(Integer.valueOf(usr.getLocation()));
			actaBean.setAnioActa(Integer.valueOf(actaForm.getAnioActa()));
			
			
			if(actaForm.getIdPresidente()!=null && !actaForm.getIdPresidente().equalsIgnoreCase(""))
				actaBean.setIdPresidente(Integer.valueOf(actaForm.getIdPresidente()));
			if(actaForm.getIdSecretario()!=null && !actaForm.getIdSecretario().equalsIgnoreCase(""))
				actaBean.setIdSecretario(Integer.valueOf(actaForm.getIdSecretario()));
			actaBean.setMiembrosComision(actaForm.getMiembros());
			actaBean.setObservaciones(actaForm.getObservaciones());
			actaBean.setNumeroActa(actaForm.getNumeroActa());
			actaBean.setPendientes(actaForm.getPendientes());
			
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			
			if( actaForm.getFechaResolucion()!=null && 
				!actaForm.getFechaResolucion().equalsIgnoreCase("")){
				actaBean.setFechaResolucionCAJG(UtilidadesString.formatoFecha(actaForm.getFechaResolucion(), ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA) );
			}
			Hashtable actaOld = new Hashtable();
			actaOld = actaAdm.getDatosActa(actaBean.getIdActa().toString(), actaBean.getAnioActa().toString(),actaBean.getIdInstitucion().toString());
			
			if( actaForm.getFechaReunion()!=null && 
				!actaForm.getFechaReunion().equalsIgnoreCase("")){
				actaBean.setFechaReunion(UtilidadesString.formatoFecha(actaForm.getFechaReunion(), ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA) );
			}
			if(actaForm.getHoraFin()!=null && actaForm.getMinuFin()!=null && 
				!actaForm.getHoraFin().equalsIgnoreCase("") && 
				!actaForm.getMinuFin().equalsIgnoreCase("") ){
				Calendar horaFin = Calendar.getInstance();
				horaFin.set(Calendar.HOUR_OF_DAY, Integer.valueOf(actaForm.getHoraFin()));
				horaFin.set(Calendar.MINUTE, Integer.valueOf(actaForm.getMinuFin()));
				actaBean.setHoraFinReunion(sdf.format(horaFin.getTime()));
			}
			if(actaForm.getHoraIni()!=null && actaForm.getMinuIni()!=null && 
				!actaForm.getHoraIni().equalsIgnoreCase("") && 
				!actaForm.getMinuIni().equalsIgnoreCase("") ){
				Calendar horaInicio = Calendar.getInstance();
				horaInicio.set(Calendar.HOUR_OF_DAY, Integer.valueOf(actaForm.getHoraIni()));
				horaInicio.set(Calendar.MINUTE, Integer.valueOf(actaForm.getMinuIni()));
				actaBean.setHoraInicioReunion(sdf.format(horaInicio.getTime()));
			}
			
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			String validarObligatoriedadResolucion = paramAdm.getValor (usr.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_VALIDAR_OBLIGATORIEDAD_RESOLUCION, "0");
			String fechaResOld = (String)actaOld.get(ScsActaComisionBean.C_FECHARESOLUCION);
			StringBuffer sql = null;
			StringBuilder ejgActaBuilder = null; 
			//si la fecha de resolucion es nueva o si la modifica
			
			Vector ejgsActualizarVector = null;
			
			if(((fechaResOld==null||fechaResOld.equalsIgnoreCase("")) &&
					(actaBean.getFechaResolucionCAJG()!=null&&!actaBean.getFechaResolucionCAJG().equalsIgnoreCase("")))||(fechaResOld!=null &&
					actaBean.getFechaResolucionCAJG()!=null&&GstDate.compararFechas(actaBean.getFechaResolucionCAJG(), fechaResOld) !=0 ) ){
				//
//				detalleEjgsPtesRetirar = getEJGsPtesRetirar(actaBean,usr,longitudNumEjg);
				if(validarObligatoriedadResolucion!=null && validarObligatoriedadResolucion.equals(ClsConstants.DB_TRUE)){
					detalleEjgsNoResueltos =  getEJGsActaSinResolucion(actaBean,usr,longitudNumEjg);
				}
				// Si no hay ejg pendientes de retirar y estan todos con resolucion y fundamento actualizamos la fecha de resolucion, si no dejamos la antigua
				if(detalleEjgsNoResueltos==null){
					sql = new StringBuffer();
					sql.append("update " + ScsEJGBean.T_NOMBRETABLA+ " set ");
					sql.append(ScsEJGBean.C_FECHARESOLUCIONCAJG+ " = TO_DATE('" + actaBean.getFechaResolucionCAJG() + "', 'YYYY/MM/DD HH24:MI:SS')"); 
					sql.append(" where " + ScsEJGBean.C_IDACTA + " = " + actaBean.getIdActa());
					sql.append(" and " + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + actaBean.getIdInstitucion());
					sql.append(" and " + ScsEJGBean.C_ANIOACTA + " = " + actaBean.getAnioActa());
					
					
					//Actualizamos la resolucion del hostorico
					ejgActaBuilder = new StringBuilder();
					ejgActaBuilder.append(" UPDATE SCS_EJG_ACTA EJGACTA ");
					ejgActaBuilder.append(" SET (EJGACTA.IDTIPORATIFICACIONEJG, EJGACTA.IDFUNDAMENTOJURIDICO) ");
					       
					ejgActaBuilder.append(" = (SELECT EJG.IDTIPORATIFICACIONEJG, EJG.IDFUNDAMENTOJURIDICO ");
					ejgActaBuilder.append(" FROM SCS_EJG EJG ");
					ejgActaBuilder.append(" WHERE EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION ");
					ejgActaBuilder.append(" AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG ");
					ejgActaBuilder.append(" AND EJGACTA.ANIOEJG = EJG.ANIO ");
					ejgActaBuilder.append(" AND EJGACTA.NUMEROEJG = EJG.NUMERO) ");

					ejgActaBuilder.append(" WHERE EJGACTA.IDINSTITUCIONACTA =  ");
					ejgActaBuilder.append(actaBean.getIdInstitucion());
					ejgActaBuilder.append(" AND EJGACTA.ANIOACTA =  ");
					ejgActaBuilder.append(actaBean.getAnioActa());
					ejgActaBuilder.append(" AND EJGACTA.IDACTA =  ");
					ejgActaBuilder.append(actaBean.getIdActa());
					
					
					
				}else{
					actaBean.setFechaResolucionCAJG(fechaResOld);
				}
				
			}else if(actaBean.getFechaResolucionCAJG()==null||actaBean.getFechaResolucionCAJG().equalsIgnoreCase("")){
				String expedientesRepetidosEnActasAbiertas = actaAdm.getExpedientesRepetidosEnActasAbiertas(actaBean, usr);
				
				
				
				if(expedientesRepetidosEnActasAbiertas!=null){
					StringBuilder descr = new StringBuilder();
					
					descr.append(UtilidadesString.getMensajeIdioma(usr, "messages.acta.error.expotrosactas.lista"));
					descr.append("\n");
					descr.append(expedientesRepetidosEnActasAbiertas);
					
					descr.append(UtilidadesString.getMensajeIdioma(usr, "messages.acta.error.expotrosactas.solucion"));
					throw new BusinessException(descr.toString());
				}
				
				ejgsActualizarVector = actaAdm.getEJGsEnActaParaActualizar(actaBean.getIdInstitucion(), actaBean.getIdActa(), actaBean.getAnioActa());
				
				sql = new StringBuffer();
				sql.append("update " + ScsEJGBean.T_NOMBRETABLA+ " set ");
				sql.append(ScsEJGBean.C_FECHARESOLUCIONCAJG+ " = null "); 
				sql.append(" where " + ScsEJGBean.C_IDACTA + " = " + actaBean.getIdActa());
				sql.append(" and " + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + actaBean.getIdInstitucion());
				sql.append(" and " + ScsEJGBean.C_ANIOACTA + " = " + actaBean.getAnioActa());
				
				ejgActaBuilder = new StringBuilder();
				ejgActaBuilder.append(" UPDATE SCS_EJG_ACTA EJGACTA ");
				ejgActaBuilder.append(" SET EJGACTA.IDTIPORATIFICACIONEJG = null, EJGACTA.IDFUNDAMENTOJURIDICO= null ");
				ejgActaBuilder.append(" WHERE EJGACTA.IDINSTITUCIONACTA =  ");
				ejgActaBuilder.append(actaBean.getIdInstitucion());
				ejgActaBuilder.append(" AND EJGACTA.ANIOACTA =  ");
				ejgActaBuilder.append(actaBean.getAnioActa());
				ejgActaBuilder.append(" AND EJGACTA.IDACTA =  ");
				ejgActaBuilder.append(actaBean.getIdActa());
				
			}
			
			tx = usr.getTransaction();		
			tx.begin();
			actaAdm.updateDirect(actaBean);
			if(sql!=null){
				// Hay que actualizar los EJGs del acta
				ejgAdm.updateSQL(sql.toString());
				if(ejgActaBuilder!=null)
					ejgAdm.updateSQL(ejgActaBuilder.toString());
				if(ejgsActualizarVector!=null && ejgsActualizarVector.size()>0){
					for (int i = 0; i < ejgsActualizarVector.size(); i++) {
						Hashtable ejgHashtable = (Hashtable) ejgsActualizarVector.get(i);
						ejgAdm.actalizaActaEjgSinDatoActa(ejgHashtable, actaBean.getIdInstitucion(), actaBean.getIdActa(), actaBean.getAnioActa());
						
					}
					
				}
				
			}
			
			tx.commit();
			
			// Una vez guardada el acta ya no nos interesa el form
			actaForm.reset();
			
		}  catch (BusinessException e) {
			return errorRefresco(e.getMessage(),new ClsExceptions(e.getMessage()), request);
		}catch (Exception e) {
			throw new SIGAException("Error al modificar el acta.",e);
		}
		if(detalleEjgsNoResueltos!=null){
			StringBuffer descripcion = new StringBuffer("");
			
			
			if(detalleEjgsNoResueltos!=null){
				if(!descripcion.equals(""))
					descripcion.append("\n");
				descripcion.append(UtilidadesString.getMensajeIdioma(usr, "messages.acta.error.expsinresolucvion.lista"));
				descripcion.append("\n");
				descripcion.append(detalleEjgsNoResueltos);
				descripcion.append("\n");
				
				descripcion.append(UtilidadesString.getMensajeIdioma(usr, "messages.acta.error.expsinresolucvion.solucion"));
				
			}
			descripcion.append("\n");
			
			descripcion.append(UtilidadesString.getMensajeIdioma(usr, "messages.acta.error.expsinresolucvion.updatedko"));
			
			descripcion.append("\n");
			descripcion.append(UtilidadesString.getMensajeIdioma(usr, "messages.acta.error.expsinresolucvion.updatedok"));
			return errorRefresco(descripcion.toString(),new ClsExceptions(descripcion.toString()), request);
			
		}
		return exitoRefresco("messages.updated.success", request);
	}
	
	private String getEJGsActaSinResolucion(ScsActaComisionBean actaBean,UsrBean usr,String longitudNumEjg ) throws ClsExceptions{
		String idActa		 = actaBean.getIdActa().toString();
		String idInstitucion = actaBean.getIdInstitucion().toString();
		String anioActa		 = actaBean.getAnioActa().toString();
		StringBuffer detalleEjgsNoResueltos = new StringBuffer("");
		EjgActaBusinessService ejgActaBusinessService = (EjgActaBusinessService) getBusinessManager().getService(EjgActaBusinessService.class);
		EjgActaVo ejgActaVo = new EjgActaVo(); 
		ejgActaVo.setIdacta(Long.valueOf(idActa));
		ejgActaVo.setIdinstitucionacta(Short.valueOf(idInstitucion));
		ejgActaVo.setAnioacta(Short.valueOf(anioActa));
		ejgActaVo.setLongitudNumEjg(longitudNumEjg);
		
		
		List<EjgActaVo> actaVos = ejgActaBusinessService.getList(ejgActaVo);
		
		
		for (EjgActaVo ejgActaVo2 : actaVos) {
			if(ejgActaVo2.getIdtiporatificacionejg()==null || ejgActaVo2.getIdfundamentojuridico() ==null){
				detalleEjgsNoResueltos.append(ejgActaVo2.getDescripcionEjg());
				detalleEjgsNoResueltos.append(", ");
			}
		}
		//quitamos la come del final
		if(!detalleEjgsNoResueltos.equals("")&&detalleEjgsNoResueltos.length()>0){
			detalleEjgsNoResueltos= detalleEjgsNoResueltos.delete(detalleEjgsNoResueltos.length()-2,detalleEjgsNoResueltos.length());
			return detalleEjgsNoResueltos.toString();
		}
		else
			return null;
		
	}
	
	private String getEJGsPtesRetirar(ScsActaComisionBean actaBean,UsrBean usr,String longitudNumEjg) throws  SIGAException, ClsExceptions{
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		StringBuffer detalleEjgsPteRetirar = new StringBuffer("");
		Vector ejgsPteRetirar = actaAdm.getEJGsRetirados(actaBean.getIdInstitucion(),actaBean.getIdActa(), actaBean.getAnioActa(),longitudNumEjg);
		for (int i = 0; i < ejgsPteRetirar.size(); i++) {
			Hashtable row = (Hashtable) ejgsPteRetirar.get(i);
				detalleEjgsPteRetirar.append((String)row.get("ANIO"));
				detalleEjgsPteRetirar.append("/");
				detalleEjgsPteRetirar.append((String)row.get("NUMEJG"));
				detalleEjgsPteRetirar.append(", ");
		}
		if(!detalleEjgsPteRetirar.equals("")&&detalleEjgsPteRetirar.length()>0){
			detalleEjgsPteRetirar= detalleEjgsPteRetirar.delete(detalleEjgsPteRetirar.length()-2,detalleEjgsPteRetirar.length());
			return detalleEjgsPteRetirar.toString();
		}
		else
			return null;
		
		
		
	}
	
	
	
	
	
	/** 
	 * Funcion que implementa el modo editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	@Override
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ActaComisionForm actaForm = (ActaComisionForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		Vector ocultos = actaForm.getDatosTablaOcultos(0);
		ScsActaComisionBean actaBean = new ScsActaComisionBean();
		// Recuperamos la clave del acta
		actaBean.setIdActa(Integer.parseInt((String) ocultos.get(0)));
		actaBean.setIdInstitucion(Integer.parseInt((String) ocultos.get(1)));
		actaBean.setAnioActa(Integer.parseInt((String) ocultos.get(2)));
		try {
			// Con la clave borramos el acta
			actaAdm.delete(actaBean);
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return  exitoRefresco("messages.deleted.success", request);
	}
	
	/** 
	 * Funcion que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @throws ClsExceptions 
	 */	
	protected String edicionMasiva(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ActaComisionForm form = (ActaComisionForm)formulario;
		request.setAttribute("seleccionados", form.getSeleccionados());
		GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
		try {
			String validarObligatoriedadResolucion = paramAdm.getValor (usr.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_VALIDAR_OBLIGATORIEDAD_RESOLUCION, "0");
			request.setAttribute("ISOBLIGATORIORESOLUCION",validarObligatoriedadResolucion.equals(ClsConstants.DB_TRUE)?true:false);
		} catch (Exception e) {
			throwExcp("Error al recuperar el parametro VALIDAR_OBLIGATORIEDAD_RESOLUCION",e,null);
		}
		
		
		form.reset();
		return "edicionMasiva";
	}
	
	/** 
	 * Funcion que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @throws SIGAException 
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 * @throws ClsExceptions 
	 */	              
	protected String updateMasivo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ActaComisionForm actaForm = (ActaComisionForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String anioActa = actaForm.getAnioActa().equalsIgnoreCase("") ? null : actaForm.getAnioActa();
		String idActa = actaForm.getIdActa().equalsIgnoreCase("") ? null : actaForm.getIdActa();
		String idPonente = actaForm.getIdPonente().equalsIgnoreCase("") ? null : actaForm.getIdPonente();
		String idInstitucion = actaForm.getIdInstitucion().equalsIgnoreCase("") ? null : actaForm.getIdInstitucion();
		String idFundamentoJuridico = actaForm.getIdFundamentoJuridico().equalsIgnoreCase("") ? null : actaForm.getIdFundamentoJuridico();
		String idTipoRatificacionEJG = actaForm.getIdTipoRatificacionEJG().equalsIgnoreCase("") ? null : actaForm.getIdTipoRatificacionEJG();

		String[] seleccionados = actaForm.getSeleccionados().split("%%%");

		try {

			List<EjgVo> ejgVoList = new ArrayList<EjgVo>();
			EjgVo ejgVo = null;
			List<String> camposGuardarList = null;
			for (int i = 0; i < seleccionados.length; i++) {
				String[] claves = seleccionados[i].split("##");

				ejgVo = new EjgVo();
				ejgVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
				ejgVo.setIdinstitucion(Short.valueOf(claves[0].split("idinstitucion==")[1]));
				ejgVo.setIdtipoejg(Short.valueOf(claves[1].split("idtipo==")[1]));
				ejgVo.setNumero(Long.valueOf(claves[3].split("numero==")[1]));
				ejgVo.setAnio(Short.valueOf(claves[2].split("anio==")[1]));

				camposGuardarList = new ArrayList<String>();
				if (actaForm.getGuardaActa()) {
					if(idActa!=null){
						ejgVo.setIdacta(Long.valueOf(idActa));
						ejgVo.setIdinstitucionacta(Short.valueOf(idInstitucion));
						ejgVo.setAnioacta(Short.valueOf(anioActa));
					}
					camposGuardarList.add("Acta");
				}
				if (actaForm.getGuardaPonente()) {
					camposGuardarList.add("Ponente");

					if (actaForm.getFechaPresentacionPonente() != null && !actaForm.getFechaPresentacionPonente().equalsIgnoreCase(""))
						ejgVo.setFechapresentacionponente(GstDate.convertirFecha(actaForm.getFechaPresentacionPonente(), "dd/MM/yyyy"));

					else
						ejgVo.setFechapresentacionponente(null);

					if (idPonente != null) {
						ejgVo.setIdponente(Integer.valueOf(idPonente));
						ejgVo.setIdinstitucionponente(Short.valueOf(idInstitucion));
					}

				}
				if (actaForm.getGuardaRatificacion()) {
					camposGuardarList.add("Resolucion");
					if (idTipoRatificacionEJG != null)
						ejgVo.setIdtiporatificacionejg(Short.valueOf(idTipoRatificacionEJG));
				}
				if (actaForm.getGuardaFundamento()) {
					camposGuardarList.add("Fundamento");
					if (idTipoRatificacionEJG != null)
						ejgVo.setIdfundamentojuridico(Short.valueOf(idFundamentoJuridico));

				}

				ejgVoList.add(ejgVo);
			}

			EjgService ejgService = (EjgService) getBusinessManager().getService(EjgService.class);
			ejgService.updateMasivoByCajg(camposGuardarList, ejgVoList);

			// ejgAdm.updateSQL(consulta.toString()+update.toString()+where.toString());
		} catch (Exception e) {
			throw new SIGAException("Error al realizar la actualizaci�n masiva de datos.", e);
		}

		return exitoModal("messages.updated.success", request);
	}
	
	/** 
	 * Funcion que procesa los EJGs del acta que quedan pendientes y se deben eliminar del acta
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String procesarRetirados(ActionMapping mapping, MasterForm miForm,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ActaComisionForm actaForm = (ActaComisionForm) miForm;
		ScsActaComisionBean actaBean = new ScsActaComisionBean();
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
		ScsEstadoEJGAdm estadoAdm = new ScsEstadoEJGAdm(usr);
		Vector listadoEJGs = new Vector<Hashtable>();
		
		String listaRetirados = "";
		UserTransaction tx= null;
		
		try {

			// Campos clave
			actaBean.setIdActa(Integer.parseInt(actaForm.getIdActa()));
			actaBean.setIdInstitucion(Integer.valueOf(usr.getLocation()));
			actaBean.setAnioActa(Integer.valueOf(actaForm.getAnioActa()));
			
			tx = usr.getTransaction ();
			tx.begin();
			
			// Recuperamos la lista de ejgs a retirar y los retiramos del acta
			listadoEJGs = actaAdm.updateEJGsRetirados(actaBean.getIdInstitucion(),actaBean.getIdActa(), actaBean.getAnioActa(),longitudNumEjg);
			
			for (Object element : listadoEJGs) {

				// Recuperamos el EJG de la lista y lo preparamos para a�adirle un estado nuevo
				Hashtable ejg = (Hashtable)element;
				ejg.put(ScsEstadoEJGBean.C_FECHAINICIO, UtilidadesString.getTimeStamp(ClsConstants.DATE_FORMAT_LONG_ENGLISH));
				ejg.put(ScsEstadoEJGBean.C_AUTOMATICO,ClsConstants.DB_TRUE);
				estadoAdm.prepararInsert (ejg);
				
				// Como observacion diremos a que acta pertenecia
				ejg.put(ScsEstadoEJGBean.C_OBSERVACIONES,UtilidadesString.getMensajeIdioma(usr.getLanguageInstitucion(),"sjcs.actas.observacionAlRetirar") + actaForm.getAnioActa()+"/"+actaForm.getNumeroActa());
				
				// escribimos el ejg en la lista de retirados
				listaRetirados+=ejg.get(ScsEJGBean.C_ANIO)+"/"+ejg.get(ScsEJGBean.C_NUMEJG);
				//para todos menos el ultimo pondremos una coma
				if(!element.equals(listadoEJGs.lastElement()))
					listaRetirados+=", ";
				
				// Dependiendo de la resolucion/ratificacion a�adimos un estado nuevo
				String ratificacion = (String) ejg.get(ScsEJGBean.C_IDTIPORATIFICACIONEJG);
				if(ratificacion.equalsIgnoreCase("4")){ //resolucion - Pendiente CAJG - Otros
					ejg.put(ScsEstadoEJGBean.C_IDESTADOEJG,9); //estado - Remitido Comisi�n
				}else if(ratificacion.equalsIgnoreCase("6")){ //resolucion - Devuelto al Colegio
					ejg.put(ScsEstadoEJGBean.C_IDESTADOEJG,21); // estado - Devuelto al colegio
				}
				
				// insertamos el estado
				estadoAdm.insert (ejg);
			}
			
			// finalmente a�adimos la lista de pendientes al texto que ya existiese y guardamos el acta
			String pendientes = actaForm.getPendientes() + " " + listaRetirados;
			actaBean.setPendientes(pendientes.trim());
			actaAdm.updateDirect(actaAdm.beanToHashTable(actaBean),actaAdm.getClavesBean() , new String[]{ScsActaComisionBean.C_PENDIENTES});
			
			tx.commit();
			
		}catch (Exception e){
			throwExcp("Error al procesar los EJGs retirados del acta.", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoRefresco("messages.updated.success", request);
	}
}
