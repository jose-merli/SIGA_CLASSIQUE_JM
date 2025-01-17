package com.siga.censo.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenDatosCVAdm;
import com.siga.beans.CenDatosCVBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.BusquedaComisionesForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author PDM
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BusquedaComisionesAction extends MasterAction {

	
	
	protected ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

			String mapDestino = "exception";
			MasterForm miForm = null;
			
		try {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
			
				String	 accion = miForm.getModo();
			
				//La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("abrirVolver")){
					request.setAttribute("volver","volver");
					mapDestino = abrir(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscar")){
					mapDestino = buscar(mapping, miForm, request, response);							
				}else if (accion.equalsIgnoreCase("ver")){
					mapDestino = ver(mapping, miForm, request, response);	
				}else if (accion.equalsIgnoreCase("getAjaxGuardarCargos")){
					ClsLogging.writeFileLog("BUSQUEDACOMISIONES:getAjaxGuardarCargos", 10);
					mapDestino = getAjaxGuardarCargos(mapping, miForm, request, response);	
					
				}else if (accion.equalsIgnoreCase("modificar")){
					mapDestino = modificar(mapping, miForm, request, response);						
				}else if ( accion.equalsIgnoreCase("getAjaxColegiado")){
					ClsLogging.writeFileLog("BUSQUEDACOMISIONES:getAjaxColegiado", 10);
					mapDestino = getAjaxColegiado(mapping, miForm, request, response);
					return null;
				}else if ( accion.equalsIgnoreCase("getAjaxBusquedaCargos")){
					ClsLogging.writeFileLog("BUSQUEDACOMISIONES:getAjaxBusquedaCargos", 10);
					mapDestino = getAjaxBusquedaCargos(mapping, miForm, request, response);
//					return null;
				}else if ( accion.equalsIgnoreCase("getAjaxColegiadoIndividual")){
					ClsLogging.writeFileLog("BUSQUEDACOMISIONES:getAjaxColegiado", 10);
					mapDestino = getAjaxColegiadoIndividual(mapping, miForm, request, response);
					//return null;
				}else if ( accion.equalsIgnoreCase("getAjaxPrimerCargo")){
					ClsLogging.writeFileLog("BUSQUEDACOMISIONES:getAjaxPrimerCargo", 10);
					mapDestino = getAjaxPrimerCargo (mapping, miForm, request, response);
					
				}else {
					return super.executeInternal(mapping, formulario, request, response);
				}

		// Redireccionamos el flujo a la JSP correspondiente
		if (mapDestino == null)			{ 
			throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
		}
		
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
}


			
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idInstitucion=user.getLocation();
		int idInstitu= new Integer(idInstitucion).intValue();
		String visibilidad = CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
		request.setAttribute("CenInstitucionesVisibles",visibilidad);
		BusquedaComisionesForm miForm = (BusquedaComisionesForm) formulario;
		java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String fecha = formador.format(new Date());
		miForm.setFechaCargo(fecha);
		miForm.setIdInstitucion(idInstitucion);
		String parametro[] = new String[2];
		parametro[0] = (String)user.getLocation();
		parametro[1] = (String)user.getLanguage().toUpperCase();
		request.setAttribute("parambdcomisiones",parametro);
		request.setAttribute("idInstitucionCargo",miForm.getIdInstitucionCargo());
		request.setAttribute("nombreColegiado",miForm.getNombreColegiado());
		request.setAttribute("numColegiado",miForm.getNumeroColegiado());
		request.setAttribute("cargos",miForm.getCargos());
		
/*		if(idInstitucion!="2000")
			return "inicio";
		else 
			return "inicioJunta";*/
		if(idInstitu!=2000)
			return "inicio";
		else
			return "inicioJunta";
	}
	
	@SuppressWarnings("unchecked")
	protected String getAjaxColegiado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BusquedaComisionesForm miForm = (BusquedaComisionesForm) formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		Hashtable<String, Object> htCliente = null;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String numeroColegiado = miForm.getNumeroColegiado();
		ClsLogging.writeFileLog("BUSQUEDA COMISIONES:getAjaxColegiado.numeroColegiado:"+numeroColegiado+"/", 10);
		String insti= miForm.getIdInstitucionCargo();//idInstitucionCargo
		if(insti==null || insti.equals(""))
			insti=user.getLocation();
		if(numeroColegiado!= null && !numeroColegiado.equals("")){
			CenClienteAdm admCli = new CenClienteAdm(this.getUserBean(request) );
			Vector<Hashtable<String, Object>> vClientes = admCli.getClientePorNColegiado(insti,numeroColegiado);
			if(vClientes!=null &&vClientes.size()>0)
				htCliente = vClientes.get(0);
		}
		String nombreColegiado = null;
		String idPersona = null;
		
		if(htCliente!=null){
			numeroColegiado = (String)htCliente.get("NCOLEGIADO");
			nombreColegiado = (String)htCliente.get("NOMCOLEGIADO");
			idPersona = (String)htCliente.get("IDPERSONA");
			miForm.setNombreColegiado(nombreColegiado);
			miForm.setNumeroColegiado(numeroColegiado);

		}else{
			nombreColegiado = "";
			idPersona = "";
			numeroColegiado = "";

		}
		
		List listaParametros = new ArrayList();
		listaParametros.add(idPersona);
		listaParametros.add(numeroColegiado);
		listaParametros.add(nombreColegiado);
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );

/*		ClsLogging.writeFileLog("BUSQUEDA COMISIONES:getAjaxColegiado.nombreColegiado:"+nombreColegiado+"/", 10);
		
		 response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
		
		 response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); */
		return "completado";
	}
	
	@SuppressWarnings("unchecked")
	protected String getAjaxColegiadoIndividual (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BusquedaComisionesForm miForm = (BusquedaComisionesForm) formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		Hashtable<String, Object> htCliente = null;
		String numeroColegiadoN = miForm.getNumeroColegiadoN();
		String nombreColegiadoN = miForm.getNombreColegiadoN();
		String apellidosColegiadoN = miForm.getApellidosColegiadoN();
		String multiple="";
		Vector<Hashtable<String, Object>> vClientes = new  Vector<Hashtable<String, Object>>();
		ClsLogging.writeFileLog("BUSQUEDA COMISIONES:getAjaxColegiadoIndividual.numeroColegiado:"+numeroColegiadoN+"/", 10);
		String insti= miForm.getIdInstitucionCargo();//idInstitucionCargo
		if(numeroColegiadoN!= null && !numeroColegiadoN.equals("")){
			CenClienteAdm admCli = new CenClienteAdm(this.getUserBean(request) );
			vClientes = admCli.getClientePorNColegiado2(insti,numeroColegiadoN.trim());
	
		}else if(apellidosColegiadoN!= null && !apellidosColegiadoN.equals("")){
			CenClienteAdm admCli = new CenClienteAdm(this.getUserBean(request) );
			vClientes = admCli.getClientePorNombreColegiado(insti,nombreColegiadoN, apellidosColegiadoN.trim());
		}
		if(vClientes==null || vClientes.size()==0){
			multiple="N";
		}else if(vClientes.size()>1){
			multiple="S";
		}else if(vClientes.size()==1){
			htCliente = vClientes.get(0);
			multiple="N";
		}
		String idPersonaN = null;
		
		if(htCliente!=null){
			numeroColegiadoN = (String)htCliente.get("NCOLEGIADO");
			nombreColegiadoN = (String)htCliente.get("NOMBRECOLEGIADO");
			apellidosColegiadoN = (String)htCliente.get("APECOLEGIADO");
			idPersonaN = (String)htCliente.get("IDPERSONA");
			miForm.setNombreColegiadoN(nombreColegiadoN);
			miForm.setNumeroColegiadoN(numeroColegiadoN);
			miForm.setApellidosColegiadoN(apellidosColegiadoN);			
			miForm.setIdPersonaN(idPersonaN);
		
			
		}else{
			if(!multiple.equals("S")){
				nombreColegiadoN = "";
				apellidosColegiadoN ="";
				idPersonaN = "";
				numeroColegiadoN = "";
			}
		}
		
		List listaParametros = new ArrayList();
		listaParametros.add(idPersonaN);
		listaParametros.add(numeroColegiadoN);
		listaParametros.add(nombreColegiadoN);
		listaParametros.add(apellidosColegiadoN);
		listaParametros.add(miForm.getNumeroN());
		listaParametros.add(multiple);		
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
 
		return "completado";
	}

	/**
	 * Metodo que implementa el modo buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
			
		Vector registros = null;
		BusquedaComisionesForm miFormulario = (BusquedaComisionesForm)formulario;
		UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
		&& !miFormulario.getSeleccionarTodos().equals("");
		
		
		if(!isSeleccionarTodos){
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("Seleccion");
		}
		
		
		try 
		{   
			CenDatosCVAdm datosComisionesCV= new CenDatosCVAdm(this.getUserBean(request));
		    
			registros=datosComisionesCV.buscarComisiones(miFormulario);
			request.setAttribute("COMISIONES", registros);
			
			
			
	    } catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
	    String idInstitucion=user.getLocation();
		int idInstitu= new Integer(idInstitucion).intValue();
		if(idInstitu!=2000)
			return "busqueda";
		else
			return "busquedaJunta";
		
	}
	
	protected String modificar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
			
		List<CenDatosCVBean> registros = null;
		BusquedaComisionesForm miFormulario = (BusquedaComisionesForm)formulario;
		UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
		&& !miFormulario.getSeleccionarTodos().equals("");
		
		try 
		{       
				CenDatosCVAdm datosComisionesCV= new CenDatosCVAdm(this.getUserBean(request));
		        registros=(List<CenDatosCVBean>) datosComisionesCV.buscarComisionesJunta(miFormulario);
		        miFormulario.setComisiones(registros);
				request.setAttribute("COMISIONES", registros);
				request.setAttribute("idInstitucionCargo", miFormulario.getIdInstitucionCargo());
	    } catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
	    String idInstitucion=user.getLocation();
		int idInstitu= new Integer(idInstitucion).intValue();
		if(idInstitu!=2000)
			return "modificar";
		else
			return "modificarCargoJunta";

	}
	

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		
		try{
			Vector ocultos = new Vector();
			BusquedaComisionesForm form= (BusquedaComisionesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(0));
			String accion = (String)request.getParameter("accion");		
			Integer idCV = Integer.valueOf((String)ocultos.elementAt(2));
			String ncolegiado=String.valueOf((String)ocultos.elementAt(6));


			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenPersonaAdm personaAdm = new CenPersonaAdm(user);
			String nombreUsuario = personaAdm.obtenerNombreApellidos(idPersona.toString());
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCV(idPersona,idInstitucionPersona,idCV);
	
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", nombreUsuario);
			request.setAttribute("numero",ncolegiado);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		
		return modo;
	}
	
	/**
	 * Esta funcion se encarga de guardar las altas y las actualizaciones de registros de cargos.
	 * Recibe una cadena con todos los registros y la procesa
	 * 
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 *
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	@SuppressWarnings("unchecked")
	protected String getAjaxGuardarCargos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles
		UsrBean user = this.getUserBean(request);
		CenDatosCVAdm admDatosCV = new CenDatosCVAdm(user);
		
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		BusquedaComisionesForm form = (BusquedaComisionesForm) formulario;
		UserTransaction tx = user.getTransaction();
		Calendar fecha = Calendar.getInstance();
		
		try {
			GenParametrosAdm paramAdm = new GenParametrosAdm(user);
			final String IDTIPOCV_JUNTASGOBIERNO = paramAdm.getValor(user.getLocation(), "CEN", ClsConstants.GEN_PARAM_IDTIPOCV_JUNTASGOBIERNO, "");
			String todosLosDatos = form.getDatosCargos();
			String[] elementos = todosLosDatos.split("#@#");
			
			// Actualizacion de cargos
			if (elementos[0] != null) {
				StringTokenizer listaCargosParaActualizar = new StringTokenizer(elementos[0], "%%%");
				while (listaCargosParaActualizar.hasMoreTokens()) {
					
					// obteniendo los datos del cargo desde la estructura
					StringTokenizer cargoParaActualizar = new StringTokenizer(listaCargosParaActualizar.nextToken(), ",");
					String colegiado = cargoParaActualizar.nextToken();
					String IDCV = cargoParaActualizar.nextToken();
					String fechaFin = cargoParaActualizar.nextToken();
					String idpersona = cargoParaActualizar.nextToken();
					String actualiza = (String) cargoParaActualizar.nextToken();

					try {
						// cargando los datos para actualizar
						CenDatosCVBean beanCV = new CenDatosCVBean();
						Hashtable datosOriginales = admDatosCV.selectDatosCVJunta(new Long(idpersona), new Integer(user.getLocation()), new Integer(form.getIdInstitucionCargo()), new Integer(IDCV));
						beanCV.setOriginalHash(datosOriginales);
						beanCV.setIdCV(new Integer(IDCV));
						beanCV.setIdInstitucion(new Integer(user.getLocation()));
						beanCV.setIdPersona(new Long(idpersona));
						beanCV.setIdInstitucionCargo(new Integer(form.getIdInstitucionCargo()));
						if (actualiza.equalsIgnoreCase("S")) {
							fecha.setTime(dateFormat.parse(fechaFin));
							beanCV.setFechaFin(sdf.format(fecha.getTime()));
						}

						// actualizando en BD
						tx = user.getTransaction();
						tx.begin();
						if (!admDatosCV.updatefecha(beanCV)) {
							throw new SIGAException(admDatosCV.getError());
						}
						tx.commit();
					} catch (Exception e) { // si falla un cargo, seguimos con los demas
						throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
					}
				}
			}
			
			// Alta de cargos
			if (elementos.length > 1 && elementos[1] != null) {
				StringTokenizer listaCargosParaAlta = new StringTokenizer(elementos[1], "%%%");
				while (listaCargosParaAlta.hasMoreTokens()) {
					
					// obteniendo los datos del cargo desde la estructura
					StringTokenizer cargoParaAlta = new StringTokenizer(listaCargosParaAlta.nextToken(), ",");
					StringTokenizer tipoCargo = new StringTokenizer(cargoParaAlta.nextToken(), "@");
					String idtipocvsubtipo1 = tipoCargo.nextToken();
					String idinstitucion1 = tipoCargo.nextToken();
					String fechaInicio = cargoParaAlta.nextToken();
					String idPersona = cargoParaAlta.nextToken();
					
					try {
						// cargando los datos para el alta
						CenDatosCVBean beanCV = new CenDatosCVBean();
						beanCV.setIdInstitucion(new Integer(user.getLocation()));
						beanCV.setIdInstitucionCargo(new Integer(form.getIdInstitucionCargo()));
						beanCV.setCertificado(ClsConstants.DB_FALSE);
						beanCV.setFechaFin("");
						fecha.setTime(dateFormat.parse(fechaInicio));
						beanCV.setFechaInicio(sdf.format(fecha.getTime()));
						beanCV.setFechaMovimiento(sdf.format(fecha.getTime()));
						beanCV.setIdTipoCV(new Integer(IDTIPOCV_JUNTASGOBIERNO));
						beanCV.setIdPersona(new Long(idPersona));
						beanCV.setIdTipoCVSubtipo1(idtipocvsubtipo1);
						beanCV.setIdInstitucion_subt1(new Integer(idinstitucion1));
						
						// insertando en BD
						tx = user.getTransaction();
						tx.begin();
						if (!admDatosCV.insertar(beanCV, new CenHistoricoBean(), user.getLanguage())) {
							throw new SIGAException(admDatosCV.getError());
						}
						tx.commit();
					} catch (Exception e) { // si falla un cargo, seguimos con los demas
						throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
					}
				}
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		
		return "busquedaJunta";
	} //getAjaxGuardarCargos()
	
	@SuppressWarnings("unchecked")
	protected String getAjaxBusquedaCargos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		List<CenDatosCVBean> registros = null;
		BusquedaComisionesForm miFormulario = (BusquedaComisionesForm)formulario;
		UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
		&& !miFormulario.getSeleccionarTodos().equals("");
		
		try 
		{       
				CenDatosCVAdm datosComisionesCV= new CenDatosCVAdm(this.getUserBean(request));
		        registros=(List<CenDatosCVBean>) datosComisionesCV.buscarComisionesJunta(miFormulario);
		        miFormulario.setComisiones(registros);
				request.setAttribute("COMISIONES", registros);
				request.setAttribute("idInstitucionCargo", miFormulario.getIdInstitucionCargo());
				if(registros==null || registros.size()==0){
					miFormulario.setMsgAviso(UtilidadesString.getMensajeIdioma(user,"censo.comisiones.noDatos"));
				}else{
					miFormulario.setMsgAviso("");
				}
				
	    } catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
		  miFormulario.setMsgError(e.toString());
   	    }
		return "busquedaJunta";
	}
	
	protected String getAjaxPrimerCargo (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		BusquedaComisionesForm miForm = (BusquedaComisionesForm) formulario;
		//Recogemos el parametro enviado por ajax
	
		try {
			
			
	        List<CenDatosCVBean> alComision = new ArrayList<CenDatosCVBean>();
	        CenDatosCVBean comisionBean = new CenDatosCVBean();
	        comisionBean.setApellidos("");
	        comisionBean.setNombre("");
	        comisionBean.setCargo("");
	        comisionBean.setIdPersona(null);
	        comisionBean.setIdCV(null);
	        comisionBean.setFechaInicio("");
						
	        alComision.add(comisionBean);
			miForm.setComisiones(alComision);
			
		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return "busquedaJunta";
	}


}