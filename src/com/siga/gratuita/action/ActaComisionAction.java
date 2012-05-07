/**
 * @author jose.barrientos
 *
 * @version 19-10-2011
 */

package com.siga.gratuita.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.ScsActaComisionAdm;
import com.siga.beans.ScsActaComisionBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActaComisionForm;


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
				}else if (accion.equalsIgnoreCase("updateMasivo")){
					mapDestino = updateMasivo(mapping, miForm, request, response);
				} else {
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
		String anio = sdf2.format(hoy);
		int numActa;
		try {
			actasForm.setFechaResolucion(sdf.format(hoy));
			actasForm.setAnioActa(anio);
			numActa = actasAdm.getNuevoNumActa(idInstitucion,anio);
			actasForm.setNumeroActa(String.valueOf(numActa));
		} catch (ClsExceptions e) {
			throw new SIGAException(e);
		}
		
		return "nuevo";
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
			actaBean.setNumeroActa(actaForm.getNumeroActa());
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
	@Override
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		ActaComisionForm actaForm = (ActaComisionForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usr);
		Vector ocultos = actaForm.getDatosTablaOcultos(0);
		formulario = new MasterForm();
		Hashtable<String, String>  datosActa = null;
		Vector ejgsRelacionados = null;
		// Recuperamos la clave del acta
		String idActa		 = (String) ocultos.get(0);
		String idInstitucion = (String) ocultos.get(1);
		String anioActa		 = (String) ocultos.get(2);
		try {
			// Con la clave recuperamos sus datos
			datosActa = actaAdm.getDatosActa(idActa,anioActa,idInstitucion);
			ejgsRelacionados = actaAdm.getListadoEJGActa(idActa,anioActa, idInstitucion);
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		request.setAttribute("datosActa", datosActa);
		request.setAttribute("ejgsRelacionados", ejgsRelacionados);
		String accion = actaForm.getModo();
		if(accion!=null && accion.equalsIgnoreCase("ver")){
			request.setAttribute("modo", "consulta");
		}

		String informeUnico = ClsConstants.DB_TRUE;
		AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
		Vector informeBeans=adm.obtenerInformesTipo(this.getUserBean(request).getLocation(),"ACTAC",null, null);
		if(informeBeans!=null && informeBeans.size()>1){
			informeUnico = ClsConstants.DB_FALSE;
			
		}

		request.setAttribute("informeUnico", informeUnico);

		
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
		HashMap<String, String> miHash= new HashMap<String, String>();
		
		miHash.put(ScsActaComisionBean.C_ANIOACTA, actaForm.getAnioActa());
		miHash.put(ScsActaComisionBean.C_NUMEROACTA, actaForm.getNumeroActa());
		miHash.put(ScsActaComisionBean.C_FECHARESOLUCION, actaForm.getFechaResolucion());
		miHash.put(ScsActaComisionBean.C_FECHAREUNION, actaForm.getFechaReunion());
		miHash.put(ScsActaComisionBean.C_IDPRESIDENTE, actaForm.getIdPresidente());
		miHash.put(ScsActaComisionBean.C_IDSECRETARIO, actaForm.getIdSecretario());
		miHash.put(ScsActaComisionBean.C_IDINSTITUCION, usr.getLocation());
		
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
				resultado=actaAdm.getBusquedaActas((String)usr.getLocation(), miHash);
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
		
		UserTransaction tx=null;
		
		try {

			if(actaForm.getIdPresidente()!=null && !actaForm.getIdPresidente().equalsIgnoreCase(""))
				actaBean.setIdPresidente(Integer.valueOf(actaForm.getIdPresidente()));
			if(actaForm.getIdSecretario()!=null && !actaForm.getIdSecretario().equalsIgnoreCase(""))
				actaBean.setIdSecretario(Integer.valueOf(actaForm.getIdSecretario()));
			actaBean.setMiembrosComision(actaForm.getMiembros());
			actaBean.setObservaciones(actaForm.getObservaciones());
			actaBean.setNumeroActa(actaForm.getNumeroActa());
			
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
			
			// Campos clave
			actaBean.setIdActa(Integer.parseInt(actaForm.getIdActa()));
			actaBean.setIdInstitucion(Integer.valueOf(usr.getLocation()));
			actaBean.setAnioActa(Integer.valueOf(actaForm.getAnioActa()));
			
			tx = usr.getTransaction();		
			tx.begin();
			
			actaAdm.updateDirect(actaBean);
			
			tx.commit();
			
			// Una vez guardada el acta ya no nos interesa el form
			actaForm.reset();
			
		} catch (Exception e) {
			throw new SIGAException("Error al modificar el acta.",e);
		}
		
		return exitoModal("messages.updated.success", request);
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
	protected String edicionMasiva(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		ActaComisionForm form = (ActaComisionForm)formulario;
		request.setAttribute("seleccionados", form.getSeleccionados());
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
	protected String updateMasivo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		ActaComisionForm actaForm = (ActaComisionForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsEJGBean ejgBean = new ScsEJGBean();
		ScsEJGAdm ejgAdm = new ScsEJGAdm(usr);  
		String anioActa 	= actaForm.getAnioActa().equalsIgnoreCase("")?"null":actaForm.getAnioActa();
		String idActa 		= actaForm.getIdActa().equalsIgnoreCase("")?"null":actaForm.getIdActa();
		String idPonente 	= actaForm.getIdPonente().equalsIgnoreCase("")?"null":actaForm.getIdPonente();
		String idInstitucion 	= actaForm.getIdInstitucion().equalsIgnoreCase("")?"null":actaForm.getIdInstitucion();
		String idFundamentoJuridico 	= actaForm.getIdFundamentoJuridico().equalsIgnoreCase("")?"null":actaForm.getIdFundamentoJuridico();
		String idTipoRatificacionEJG 	= actaForm.getIdTipoRatificacionEJG().equalsIgnoreCase("")?"null":actaForm.getIdTipoRatificacionEJG();
		String[] seleccionados = actaForm.getSeleccionados().split("%%%");
		
		StringBuffer consulta = new StringBuffer();
		StringBuffer update = new StringBuffer();
		StringBuffer where = new StringBuffer();
		
		// Creamos la sentencia del update con todos los campos a actualizar
		consulta.append("update " + ScsEJGBean.T_NOMBRETABLA);
		consulta.append(" set ");
		
		if(actaForm.getGuardaActa()){
			update.append(" " + ScsEJGBean.C_IDACTA + "=" + idActa);
			update.append(" , " + ScsEJGBean.C_IDINSTITUCIONACTA + "=" + idInstitucion);
			update.append(" , " + ScsEJGBean.C_ANIOACTA + "=" + anioActa);
			update.append(" , " + ScsEJGBean.C_FECHARESOLUCIONCAJG + "= (select " + ScsActaComisionBean.C_FECHARESOLUCION + " from " + ScsActaComisionBean.T_NOMBRETABLA );
			update.append(" where " + ScsActaComisionBean.C_IDACTA + "=" + idActa);
			update.append(" and " + ScsActaComisionBean.C_IDINSTITUCION + "=" + idInstitucion);
			update.append(" and " + ScsActaComisionBean.C_ANIOACTA + "=" + anioActa + ") , ");
		}
		if(actaForm.getGuardaPonente()){
			update.append(ScsEJGBean.C_IDPONENTE + "=" + idPonente + " , ");
		}
		if(actaForm.getGuardaRatificacion()){
			update.append(ScsEJGBean.C_IDTIPORATIFICACIONEJG + "=" + idTipoRatificacionEJG + " , ");
		}
		if(actaForm.getGuardaFundamento()){
			update.append(ScsEJGBean.C_IDFUNDAMENTOJURIDICO + "=" + idFundamentoJuridico + " , ");
		}
		update.deleteCharAt(update.lastIndexOf(","));
		
		// Creamos el where, con la clave de los ejg que se van a modificar
		where.append(" where ");
		String numeroEJG, anioEJG, idInstitucionEJG, idTipoEJG;
		for(int i=0;i<seleccionados.length;i++){
			String[] claves = seleccionados[i].split("##");
			idInstitucionEJG = UtilidadesString.replaceAllIgnoreCase(claves[0], "==", "=");
			idTipoEJG 	= UtilidadesString.replaceAllIgnoreCase(claves[1], "==", "=");
			idTipoEJG	= UtilidadesString.replaceAllIgnoreCase(idTipoEJG, "idtipo", "idtipoejg");
			anioEJG		= UtilidadesString.replaceAllIgnoreCase(claves[2], "==", "=");
			numeroEJG	= UtilidadesString.replaceAllIgnoreCase(claves[3], "==", "=");
			// A�adimos el EJG al where
			where.append(" (" + idInstitucionEJG + " and " + anioEJG + " and " + idTipoEJG + " and " + numeroEJG + ") ");
			
			// Si no es el ultimo a�adimos un OR con el siguiente
			if((i+1)<seleccionados.length){
				where.append(" or ");
			}

		}
		try {
			ejgAdm.updateSQL(consulta.toString()+update.toString()+where.toString());
		} catch (ClsExceptions e) {
			throw new SIGAException("Error al realizar la actualizaci�n masiva de datos.",e);
		}
		
		return exitoModal("messages.updated.success", request);
	}
}
