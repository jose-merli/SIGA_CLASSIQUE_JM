// VERSIONES:
// raul.ggonzalez 14-12-2004 Creacion
// miguel.villegas 11-01-2005 Incorpora "borrar"
// juan.grau 18-04-2005 Incorpora 'buscarPersona' y 'enviarPersona'
// aalg. 2012. Se modifica todo el funcionamiento de duplicados. Se hace que aparezca todo en una misma pantalla
// se pagina (se crea buscarPor)
// la exportación ya no es de lo que se muestra en pantalla sino que hay que hacer la selección de todos los que están en el paginador
// se añaden ver y editar para acceder a los datos del no colegiado o el letrado 
/**
 * @version 30/01/2006 (david.sanchezp): nuevo valor de pestanha.
 */
package com.siga.censo.action;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.PaginadorBind;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSancionAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.DuplicadosHelper;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class MantenimientoDuplicadosAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={CenClienteBean.C_IDINSTITUCION,CenClienteBean.C_IDPERSONA};

	private static Boolean alguienEjecutando=Boolean.FALSE;

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
				String accion = miForm.getModo();
				
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("inicio") || accion.equalsIgnoreCase("mantenimientoDuplicadosCertificados") ){
					//aalg: en el acceso inicial a la página de duplicados tienen que estar chequeados todos
					//aalg: añadido para el paginador
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					MantenimientoDuplicadosForm formDupl = (MantenimientoDuplicadosForm)miForm;
					formDupl.reset(mapping,request);
					formDupl.setApellido1("");
					formDupl.setApellido2("");
					formDupl.setCampoOrdenacion("apellidos");
					//En el caso de que venga de certificados hay que mantener el nif
					if(!accion.equalsIgnoreCase("mantenimientoDuplicadosCertificados"))
						formDupl.setNifcif("");
					formDupl.setNombre("");
					formDupl.setNumeroColegiado("");
					formDupl.setSentidoOrdenacion("asc");
					//formDupl.setTipoConexion("intersect");
					formDupl.setIdInstitucion("");
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("buscar")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					((MantenimientoDuplicadosForm)miForm).setDatosPaginador(null);
					mapDestino = buscar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("buscarPor")){
					mapDestino = buscarPor(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("gestionar")){
					mapDestino = gestionar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("aceptar")){
					mapDestino = combinar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("volver")){
					mapDestino = volver(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("export")){
					mapDestino = export(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("exportar")){
					mapDestino = exportar(mapping, miForm, request, response);
				}else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			}
			 miForm.setModo("");

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

	
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			// No hacemos nada
			//Devolvemos la lista de instituciones
			UsrBean usr = this.getUserBean(request);
			String parametros = usr.getLocation();
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr); 
			List<CenInstitucionBean> listadoInstituciones = admInstitucion.getNombreColegiosTodos(parametros);
			request.setAttribute("listadoInstituciones", listadoInstituciones);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}
	
	/**
	 * Metodo que implementa el modo cuando se vuelve de la pantalla de busqueda
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String volver (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			// Ponemos el formulario de busqueda otra vez para recuperar los filtros
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)request.getSession().getAttribute("duplicadosForm");
			request.getSession().setAttribute("mantenimientoDuplicadosForm", miFormulario);
			request.setAttribute("MantenimientoDuplicadosForm", miFormulario);
			formulario=miFormulario;
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}
	
	
	/**
	 * Metodo que implementa el modo buscar para realizar la busqueda de duplicados
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles generales
		UsrBean user = this.getUserBean(request);
		MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm) formulario;
		DuplicadosHelper helper = new DuplicadosHelper();
		
		// Variables generales
		String destino = "";
		
		try {

			PaginadorBind resultado = new PaginadorBind(helper.getPersonasSimilares(miFormulario));
			request.setAttribute("resultado", resultado);

			HashMap databackup = new HashMap();
			databackup.put("paginador", resultado);
			databackup.put("datos", resultado.obtenerPagina(1));
			miFormulario.setDatosPaginador(databackup);

			miFormulario.setRegistrosSeleccionados(new ArrayList());

			// calculando si se ha de mostrar los campos de colegio en funcion de si se busca por colegiado o por
			// persona
			String institucion = miFormulario.getIdInstitucion();
			institucion = (institucion == null || institucion.trim().equalsIgnoreCase("")) ? "" : institucion;
			String nColegiado = miFormulario.getNumeroColegiado();
			nColegiado = (nColegiado == null || nColegiado.trim().equalsIgnoreCase("")) ? "" : nColegiado;
			if (institucion.equalsIgnoreCase("") && nColegiado.equalsIgnoreCase("")) {
				request.setAttribute("mostarNColegiado", "0");
			} else {
				request.setAttribute("mostarNColegiado", "1");
			}

			destino = "resultado";

		} catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina", request);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		return destino;
	}

	/**
	 * Metodo que implementa el modo buscar para realizar la busqueda de duplicados
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("Seleccion");
			
			
			if (seleccionados != null ) {
				ArrayList alRegistros = actualizarSelecionados(clavesBusqueda,seleccionados, clavesRegSeleccinados);
				if (alRegistros != null) {
					clavesRegSeleccinados = alRegistros;
					miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
				}
			}
			
			request.getSession().setAttribute("duplicadosForm", miFormulario);

			DuplicadosHelper helper = new DuplicadosHelper();
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){ 
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				
				Vector datos=new Vector();
				if (paginador!=null){
					String pagina = (String)request.getParameter("pagina");
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}
			
			formulario=miFormulario;
			// calculando si se ha de mostrar los campos de colegio en funcion de si se busca por colegiado o por
			// persona
			String institucion = miFormulario.getIdInstitucion();
			institucion = (institucion == null || institucion.trim().equalsIgnoreCase("")) ? "" : institucion;
			String nColegiado = miFormulario.getNumeroColegiado();
			nColegiado = (nColegiado == null || nColegiado.trim().equalsIgnoreCase("")) ? "" : nColegiado;
			if (institucion.equalsIgnoreCase("") && nColegiado.equalsIgnoreCase("")) {
				request.setAttribute("mostarNColegiado", "0");
			} else {
				request.setAttribute("mostarNColegiado", "1");
			}
			destino="resultado";

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
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
	protected String gestionar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		Vector similares = new Vector();
		Vector colegiaciones = new Vector();
		UsrBean usr = this.getUserBean(request);
		CenPersonaAdm admPersona = new CenPersonaAdm(usr);
		CenClienteAdm admCliente = new CenClienteAdm(usr);
		CenColegiadoAdm admColeg = new CenColegiadoAdm(usr);
		CenSancionAdm admSancion = new CenSancionAdm(usr);
		CerSolicitudCertificadosAdm admCertificados = new CerSolicitudCertificadosAdm(usr);
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr); 
		// Vamos a leer los datos de la bbdd para cargarlos en la ventana de consulta de colegiaciones 
		try {
			Hashtable datos = new Hashtable();
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			//ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = null;
			//Comprobamos si entramos a este método desde un acceso inicial o desde el botón volver
			String valorVolver=miFormulario.getVolver();
			if(valorVolver != null && "SI".equalsIgnoreCase(valorVolver)){
				 seleccionados = (String) request.getSession().getAttribute("registrosSeleccionados");
			}else{
				//Si es la primera vez, almacenamos el valor de los seleccionados para luego tenerlos cuando demos a volver
				 seleccionados = request.getParameter("registrosSeleccionados");
				 request.getSession().removeAttribute("registrosSeleccionados");
				 request.getSession().setAttribute("registrosSeleccionados", seleccionados);
			}
			miFormulario.setVolver("NO");
			// Los seleccionados deberian ser 2, separados por comas
			if (seleccionados != null && !seleccionados.equalsIgnoreCase("")) {
				String[] registros = UtilidadesString.split(seleccionados, ",");
				for (int i=0; i<registros.length;i++){
					
					Hashtable hashPersona = new Hashtable(); // Aqui meteremos todos los datos de la persona
					
					// Las claves de cada registro estan separadas por ||
					String[] claves = UtilidadesString.split(registros[i], "||");
					String idInstitucion="";
					String idPersona="";
					Vector estadosPersona = new Vector();
					Vector colegiacionesPersona = new Vector();
					Row estado = null;
					if(claves.length>1){
						colegiaciones = new Vector();
						idInstitucion = claves[0];
						idPersona = claves[1]; 
						// Si la institucion es nula significa que estamos mirando una persona y necesitamos todas las colegiaciones
						if(idInstitucion==null || idInstitucion.equalsIgnoreCase("")|| idInstitucion.equalsIgnoreCase("null")){
							colegiaciones=admColeg.getColegiaciones(idPersona);
						}else{
							colegiaciones.add(idInstitucion);
						}
						String stColegiaciones = "";
						// Buscamos los datos de cada colegiacion
						for (int c=0; c<colegiaciones.size();c++){
							Hashtable hashColegiacion = new Hashtable(); // Aqui meteremos todos los datos de la colegiacion de la persona
							String idInstitucionCol=colegiaciones.get(c).toString();
							stColegiaciones+=idInstitucionCol+"_";
							CenColegiadoBean colegiacion = admColeg.getDatosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucionCol));
							if(colegiacion!=null && colegiacion.getFechaIncorporacion()!=null && !colegiacion.getFechaIncorporacion().equalsIgnoreCase("")){
								colegiacion.setFechaIncorporacion(UtilidadesString.formatoFecha(colegiacion.getFechaIncorporacion(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
								hashColegiacion.put("datosColegiacion", colegiacion);
							}
							hashColegiacion.put("institucionColegiacion", admInstitucion.getAbreviaturaInstitucion(idInstitucionCol));
							hashColegiacion.put("fechaProduccion", admInstitucion.getFechaEnProduccion (idInstitucionCol));
							
							Vector estadosColegio = admColeg.getEstadosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucionCol), "1");
							if(estadosColegio!=null && estadosColegio.size()>0){
								estado = (Row)estadosColegio.get(0);
								estado.getRow().put("FECHAESTADO", UtilidadesString.formatoFecha(estado.getRow().get("FECHAESTADO").toString(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
								hashColegiacion.put("estadoColegiacion", estado);
							}
							//hashColegiacion.put("certificados", admCertificados.get);
							
							colegiacionesPersona.add(hashColegiacion);
						}
						hashPersona.put("datosColegiales", colegiacionesPersona);
						hashPersona.put("colegiaciones", stColegiaciones);
						hashPersona.put("sanciones", admSancion.getSancionesLetrado(idPersona, String.valueOf(ClsConstants.INSTITUCION_CGAE)).size());
						hashPersona.put("certificados", admCertificados.getNumeroCertificados(String.valueOf(ClsConstants.INSTITUCION_CGAE),idPersona));
						
						CenPersonaBean beanP = admPersona.getPersonaPorId(idPersona);
						if(beanP.getFechaNacimiento()!=null && !beanP.getFechaNacimiento().equalsIgnoreCase("")){
							beanP.setFechaNacimiento(UtilidadesString.formatoFecha(beanP.getFechaNacimiento(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						}
						beanP.setFechaMod(UtilidadesString.formatoFecha(beanP.getFechaMod(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						hashPersona.put("datosPersonales", beanP);
						
						Vector vDirecciones = admCliente.getDirecciones(Long.valueOf(idPersona), ClsConstants.INSTITUCION_CGAE, false);
						for (int j=0; j<vDirecciones.size();j++){
							Hashtable dir = (Hashtable)vDirecciones.get(j);
							dir.put("FECHAMODIFICACION", UtilidadesString.formatoFecha(dir.get("FECHAMODIFICACION").toString(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						}
						hashPersona.put("datosDirecciones", vDirecciones);
						
						int cert = admCertificados.getNumeroCertificados(Integer.toString(ClsConstants.INSTITUCION_CGAE),idPersona);
						
					}else{
						idPersona = claves[0];
					}
					
					datos.put("persona"+i, hashPersona);
					datos.put("numeroPersonas", i+1);
				}
				
			}
			request.setAttribute("datos", datos);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "gestionar";
	}
	
	/**
	 * Combina a 2 personas en una unica teniendo en cuenta las preferencias del usuario
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 */
	protected String combinar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{

		UsrBean user = this.getUserBean(request);
		MantenimientoDuplicadosForm miForm = (MantenimientoDuplicadosForm) formulario;
		CenDireccionesAdm admDireccion = new CenDireccionesAdm(user);
		CenDireccionesBean beanDireccion = new CenDireccionesBean();
		CenHistoricoBean beanHistorico = new CenHistoricoBean();
		CenColegiadoAdm admColeg = new CenColegiadoAdm(user);
		CenPersonaAdm admPersona = new CenPersonaAdm(user);
		CenClienteAdm admCliente = new CenClienteAdm(user);
		CenInstitucionAdm admInst = new CenInstitucionAdm(user);
		CenHistoricoAdm admHistorico = new CenHistoricoAdm(user);

		String personaDestino = miForm.getIdPersonaDestino();
		String personaOrigen = miForm.getIdPersonaOrigen();
		String institucion = miForm.getIdInstOrigen();
		String msgError = "";
		Hashtable hashDireccionOriginal, hashDireccionDestino;
		int colegiacionesCopiadas = 0;
		String resul[];
		String acciones = "";

		UserTransaction tx = null;

		try {

			// jbd // Insertamos una copia de las direcciones que se quieran añadir, la original sera borrada en el
			// proceso PL
			// Recuperamos las direcciones
/*			String[] direcciones = miForm.getListaDirecciones().split(",");
			String[] direccion;
			String idInstitucion = String.valueOf(ClsConstants.INSTITUCION_CGAE), idPersona = "", idDireccion = "";
			beanDireccion.setIdInstitucion(Integer.valueOf(idInstitucion));
			// Cogemos la direccion que vamos a copiar
			beanDireccion.setIdPersona(Long.valueOf(personaDestino));

			if (direcciones.length > 0) {
				for (int i = 0; i < direcciones.length; i++) {
					direccion = direcciones[i].split("&&");
					if (direccion.length > 1) {
						idPersona = direccion[1];
						idDireccion = direccion[2];
						// Recuperamos la direccion Original que vamos a copiar
						hashDireccionOriginal = admDireccion.getEntradaDireccion(idPersona, idInstitucion, idDireccion);
						// La clonamos
						hashDireccionDestino = (Hashtable) hashDireccionOriginal.clone();
						// Le cambiamos las claves para insertar la nueva direccion
						hashDireccionDestino.put(CenDireccionesBean.C_IDINSTITUCION, idInstitucion);
						hashDireccionDestino.put(CenDireccionesBean.C_IDPERSONA, personaDestino);
						hashDireccionDestino.put(CenDireccionesBean.C_IDDIRECCION, admDireccion.getNuevoID(beanDireccion));
						// Insertamos la direccion destino
						admDireccion.insert(hashDireccionDestino);
					}
				}
			}
*/

			// Control de fusion de colegiados en el mismo colegio
			Vector listaColegiacionesPersonaOrigen = admColeg.getColegiaciones(personaOrigen);
			String stInstitucion;
			String nombreInstitucion;
			int intInstitucion;
			for(int i=0;i<listaColegiacionesPersonaOrigen.size();i++){
				// para cada colegiacion de la persona origen
				stInstitucion = listaColegiacionesPersonaOrigen.get(i).toString();
				nombreInstitucion = admInst.getAbreviaturaInstitucion(stInstitucion);
				intInstitucion = Integer.parseInt(stInstitucion);
				// Si se quiere fusionar un colegiado en el mismo colegio, solo lo permitimos al personal de IT o bien si el colegio no esta en produccion
				if (admColeg.existeColegiado(Long.parseLong(personaDestino), intInstitucion) != null && !tienePermisoFusionColegiosEnProduccion(mapping, request) && admInst.estaEnProduccion(stInstitucion)) {
					tx.rollback();
					throw new SIGAException("No está permitida la fusión por seguridad. El colegio "+nombreInstitucion+" usa SIGA y puede contener datos delicados.");
				}
			}
			
			tx = user.getTransactionPesada();
			tx.begin();

			String[] resultadoEjecucionPLs = EjecucionPLs.ejecutarPL_fusion(personaOrigen, personaDestino);
			if (! resultadoEjecucionPLs[0].equals("0")) {
				msgError= "Error en la fusión de las personas: ";
				msgError += resultadoEjecucionPLs[1];
				throw new Exception(msgError);
			}

			tx.commit();
			
			CenPersonaBean beanP = admPersona.getPersonaPorId(personaDestino);
			String msgSalida = "Fusión completada: se encuentran todos los datos de "+beanP.getNombreCompleto()+" en el registro con número de identificación "+beanP.getNIFCIF();
			request.setAttribute("mensaje", msgSalida);
			
		} catch (Exception e) {
			throwExcp(msgError, new String[] { "modulo.censo" }, e, tx);
		}
		
		return "exitoFusionar";
	}
	
	/**
	 * Abre la ventana de exportacion para que el proceso se haga en background
	 */
	protected String export(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		request.setAttribute("registrosSeleccionados", request.getParameter("registrosSeleccionados"));
		return "espera";
	}
	/**
	 * Exporta un listado de duplicados completo a Excel.
	 * Este listado se genera una vez al dia (esta comprobacion se controla simplemente con el nombre del fichero)
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String exportar(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd-MM-yyyy");

		try {
			String rutaFichero = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
					+ ClsConstants.FILE_SEP + rp.returnProperty("censo.duplicados.directorio");
			String fechaActual = sdfDateOnly.format(Calendar.getInstance().getTime());
			String nombreFichero = rutaFichero + ClsConstants.FILE_SEP + "duplicados_" + fechaActual;
			File ruta = new File(rutaFichero);
			ruta.mkdirs();

			if (isAlguienEjecutando()) {//TODO crear recurso de error propio
				throw new SIGAException(UtilidadesString.getMensajeIdioma(user, "mensaje.error.facturacionsjcs.wait"));
			}

			// comprobando si no existe ya el fichero
			File fichero = new File(nombreFichero + ".zip");
			if (fichero == null || !fichero.exists()) {
				fichero = generarExportacionXLS(nombreFichero, user);
			}
			if (fichero == null || !fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			} else {
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getPath());
			}

		} catch (SIGAException e) {
			throwExcp(e.getLiteral(), e, null);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, null);

		} finally {
			// ABRIMOS EL SEMAFORO. SE DEBE EJECUTAR SIEMPRE
			setNadieEjecutando();
		}

		return "descargaFichero";
	} // exportar()
	
	/**
	 * Genera un fichero de exportacion con todos los datos de personas duplicadas y lo devuelve para descarga
	 * 
	 * @param nombreFichero
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static File generarExportacionXLS(String nombreFichero, UsrBean user) throws Exception {
		DuplicadosHelper helper = new DuplicadosHelper();
		Vector<Hashtable<String, String>> personas;
		File ficheroGenerado, ficheroSalida;
		ArrayList<ArrayList<String>> datosPersona;

		// controles de acceso a datos
		CenPersonaAdm admPersona = new CenPersonaAdm(user);

		ArrayList<File> listaFicheros = new ArrayList<File>();
		for (int similaritud = 0; similaritud < DuplicadosHelper.similaritudes.length; similaritud++) {
			personas = helper.getPersonasSimilares(similaritud);

			// generando cada fichero
			ficheroGenerado = new File(nombreFichero + "_" + DuplicadosHelper.similaritudes[similaritud] + ".xls");
			ficheroGenerado.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(ficheroGenerado));

			// escribiendo las cabeceras
			datosPersona = helper.getDatosPersonaSimilar(null, user);
			for (Iterator<String> iterColumnas = datosPersona.get(0).iterator(); iterColumnas.hasNext();) {
				out.write(iterColumnas.next() + "\t");
			}
			out.newLine();

			// escribiendo los resultados
			for (Iterator<Hashtable<String, String>> iterPersonas = personas.iterator(); iterPersonas.hasNext();) {
				datosPersona = helper.getDatosPersonaSimilar(iterPersonas.next(), user);
				for (Iterator<ArrayList<String>> iterFilas = datosPersona.iterator(); iterFilas.hasNext();) {
					for (Iterator<String> iterColumnas = iterFilas.next().iterator(); iterColumnas.hasNext();) {
						out.write(iterColumnas.next() + "\t");
					}
	
					out.newLine();
				}
			}

			// cerrando el fichero
			out.close();
			listaFicheros.add(ficheroGenerado);
			ficheroGenerado.deleteOnExit();
		}

		if (listaFicheros.size() == 0) {
			throw new SIGAException("No se ha generado ningun informe");
		}
		ficheroSalida = MasterReport.doZip(listaFicheros, nombreFichero);
		
		return ficheroSalida;
	}
	
	/**
	 * Pregunta al semaforo de ejecucion si se puede pasar. Si se puede pasar, lo cierra.
	 * @return
	 */
	private boolean isAlguienEjecutando()
	{
		synchronized (alguienEjecutando) {
			if (!alguienEjecutando) {
				alguienEjecutando = Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Abre el semaforo para que otro proceso pueda entrar
	 */
	private void setNadieEjecutando()
	{
		synchronized (alguienEjecutando) {
			alguienEjecutando = Boolean.FALSE;
		}
	}
	
	/**
	 * Metodo que implementa el modo ver 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		
		try{	
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			miFormulario.setModo("");
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miFormulario.getDatosTablaOcultos(0);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion;
			if (vOcultos.size()>1)
				idInstitucion = (String)vOcultos.get(1);
			else // es un no colegiado que tiene que ser obligatoriamente de CGAE. 2000
				idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			if (vOcultos.size()==3)
				tipo = (String)vOcultos.get(2);
			
			String modo = "ver";
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			
			String[] pestanasOcultas=new String [1];
			
			// jbd // 12124 // Un no colegiado tiene que tener la institucion CGAE
			idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			}else{
				
			  
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));
				
				Hashtable  claveh=new Hashtable();
				claveh.put(CenClienteBean.C_IDPERSONA,idPersona);
				claveh.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
				
				
				Vector resultadoObj = clienteAdm.selectByPK(claveh);
				CenClienteBean obj = (CenClienteBean)resultadoObj.get(0);
				if (obj.getLetrado().equals(ClsConstants.DB_TRUE)){
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				}else{

				// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
				// obtengo los datos de nocolegiado
				Hashtable hashNoCol = new Hashtable();
				hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
				hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
				CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
				Vector v = nocolAdm.selectByPK(hashNoCol);
					if (v!=null && v.size()>0) {
						CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
						if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
							// SOCIEDAD
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
						} else {
							// PERSONAL
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
						}
					}			
			   }	
			 }
			
			if (tipoAcceso.intValue()==new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO).intValue()){
				  
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
			}else{
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
				  pestanasOcultas=new String [1];
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
		 		}
			}
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
          
			request.setAttribute("datosCliente", datosCliente);		
			
			request.getSession ().setAttribute ("CenBusquedaClientesTipo", "DUPLICADOS");  
			
			destino="administracion";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	 	 }
		 return destino;
    }
	
	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String elementoActivo="1";
		try {
			MantenimientoDuplicadosForm miform = (MantenimientoDuplicadosForm)formulario;
			miform.setModo("");
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion;
			if (vOcultos.size()>1)
				idInstitucion = (String)vOcultos.get(1);
			else // es un no colegiado que tiene que ser obligatoriamente de CGAE. 2000
				idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			if (vOcultos.size()==3)
				tipo = (String)vOcultos.get(2);
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String[] pestanasOcultas=new String [1];
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String verFichaLetrado = request.getParameter("verFichaLetrado");
			/*if (verFichaLetrado == null){
				verFichaLetrado = miform.getVerFichaLetrado();
			}*/
			
			// jbd // 12124 // Un no colegiado tiene que tener la institucion CGAE
			idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			
			if (verFichaLetrado!=null && verFichaLetrado.equals("1")){
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				elementoActivo="3";
				
			}else{
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			} else {
				//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));

				Hashtable  claveh=new Hashtable();
				claveh.put(CenClienteBean.C_IDPERSONA,idPersona);
				claveh.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
				
				
				Vector resultadoObj = clienteAdm.selectByPK(claveh);
				CenClienteBean obj = (CenClienteBean)resultadoObj.get(0);
				if (obj.getLetrado().equals(ClsConstants.DB_TRUE)){//si es letrado 
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				}else{//si no es letrado
					// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
					// obtengo los datos de nocolegiado
					Hashtable hashNoCol = new Hashtable();
					hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
					hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
					CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
					Vector v = nocolAdm.selectByPK(hashNoCol);
					if (v!=null && v.size()>0) {
						CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
						if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
							// SOCIEDAD
							if (nocolBean.getSociedadSJ().equals("1") || (nocolBean.getSociedadSP().equals("1")) ){
								tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
							}else{
								tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
							}
						} else {
							// PERSONAL
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
						}
					}
			  }	
				
			}
			}
			
			
			if (tipoAcceso.intValue()==new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO).intValue()){
				  
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
			}else{
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
				  pestanasOcultas=new String [1];
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
		 		}
			}
			String modo = "editar";
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			request.setAttribute("elementoActivo",elementoActivo);    
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
	
			request.setAttribute("datosCliente", datosCliente);		
			if(request.getParameter("volver") != null && "MD".equalsIgnoreCase(request.getParameter("volver"))){
				request.getSession ().setAttribute ("CenBusquedaClientesTipo", "MD");  
			}else{
				request.getSession ().setAttribute ("CenBusquedaClientesTipo", "DUPLICADOS");  
			}
			
			 
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}	
		return "administracion";
	}
	
	/**
	 * Metodo que implementa el modo abrirConParametros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action 
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {

			// obtengo la visibilidad para el user
			//String visibilidad = obtenerVisibilidadUsuario(request);
			//request.setAttribute("CenInstitucionesVisibles",visibilidad);

			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal
			
			
			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			UsrBean usr = this.getUserBean(request);
			String parametros = usr.getLocation();
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr); 
			List<CenInstitucionBean> listadoInstituciones = admInstitucion.getNombreColegiosTodos(parametros);
			request.setAttribute("listadoInstituciones", listadoInstituciones);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return "inicio";
	}
	
	/**
	 *Se Verifica que el usuario tiene permiso de Archivazión.
	 * @param form Formulario con los criterios
	 * @return se muestra un resultado con un numero si tiene permiso.
	 * @throws ClsExceptions
	 */
	public boolean tienePermisoFusionColegiosEnProduccion(ActionMapping mapping, HttpServletRequest request) throws ClsExceptions {
		boolean permiso = false;
		try {

			String accessEnvio = testAccess(request.getContextPath() + "/CEN_FusionColegiosEnProduccion.do", null, request);
			if (accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				permiso = true;
			}
			// Hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath() + mapping.getPath() + ".do", null, request);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener el permiso para fusionar personas en colegios en produccion.");
		}
		return permiso;
	} // tienePermisoFusionColegiosEnProduccion()

}
