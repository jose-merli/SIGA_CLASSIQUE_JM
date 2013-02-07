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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.batik.dom.util.HashTable;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSancionAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.DuplicadosHelper;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.BusquedaClientesForm;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class MantenimientoDuplicadosAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={CenClienteBean.C_IDINSTITUCION,CenClienteBean.C_IDPERSONA};
	
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
				
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("inicio")){
					//aalg: en el acceso inicial a la página de duplicados tienen que estar chequeados todos
					//aalg: añadido para el paginador
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					MantenimientoDuplicadosForm formDupl = (MantenimientoDuplicadosForm)miForm;
					formDupl.reset(mapping,request);
					formDupl.setApellido1("");
					formDupl.setApellido2("");
					formDupl.setCampoOrdenacion("apellidos");
					formDupl.setNifcif("");
					formDupl.setNombre("");
					formDupl.setNumeroColegiado("");
					formDupl.setSentidoOrdenacion("asc");
					formDupl.setTipoConexion("intersect");
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
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;

			String chequeados = (String)request.getParameter("valoresCheck");

			if (chequeados!=null){
				miFormulario.setChkApellidos((boolean)(chequeados.substring(0, 1).equals("1") ? true : false));
				miFormulario.setChkNombreApellidos((boolean)(chequeados.substring(1, 2).equals("1") ? true : false));
				miFormulario.setChkIdentificador((boolean)(chequeados.substring(2, 3).equals("1") ? true : false));
				miFormulario.setChkNumColegiado((boolean)(chequeados.substring(3, 4).equals("1") ? true : false));			
			}
			DuplicadosHelper helper = new DuplicadosHelper();
			//aalg: para añadir la paginación
			HashMap databackup = new HashMap();
							
			databackup=new HashMap();
			Vector datos = null;
			PaginadorBind resultado = null;
			
			resultado = new PaginadorBind(helper.getPersonasSimilares(miFormulario));
			request.setAttribute("resultado", resultado);
			databackup.put("paginador",resultado);
			
			datos = resultado.obtenerPagina(1);
			databackup.put("datos",datos);
			miFormulario.setDatosPaginador(databackup);
			miFormulario.setRegistrosSeleccionados(new ArrayList());
			formulario=miFormulario;
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
			String seleccionados = request.getParameter("registrosSeleccionados");
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
	protected String combinar(ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException{
		
		UsrBean user = this.getUserBean(request);
		MantenimientoDuplicadosForm miForm = (MantenimientoDuplicadosForm)formulario;
		CenDireccionesAdm admDireccion     = new CenDireccionesAdm(user);
		CenDireccionesBean beanDireccion   = new CenDireccionesBean();
		CenHistoricoBean beanHistorico     = new CenHistoricoBean();
		CenColegiadoAdm admColeg  = new CenColegiadoAdm(user);
		CenPersonaAdm admPersona  = new CenPersonaAdm(user);
		CenClienteAdm admCliente  = new CenClienteAdm(user);
		CenInstitucionAdm admInst = new CenInstitucionAdm(user);
		CenHistoricoAdm	  admHistorico = new CenHistoricoAdm(user);
		
		String personaDestino = miForm.getIdPersonaDestino();
		String personaOrigen = miForm.getIdPersonaOrigen();
		String institucion = miForm.getIdInstOrigen();
		String msgError = "";
		String msgSalida = "Se han copiado correctamente los datos de la persona";
		Hashtable hashDireccionOriginal, hashDireccionDestino ;
		int colegiacionesCopiadas = 0;
		String resul [];
		String acciones = "";
		
		UserTransaction tx = null;
		
		try {
			
			// jbd // Insertamos una copia de las direcciones que se quieran añadir, la original sera borrada en el proceso PL
			// Recuperamos las direcciones
			String[] direcciones = miForm.getListaDirecciones().split(","); 
			String[] direccion;
			String idInstitucion=String.valueOf(ClsConstants.INSTITUCION_CGAE), idPersona="", idDireccion="";
			beanDireccion.setIdInstitucion(Integer.valueOf(idInstitucion));
			// Cogemos la direccion que vamos a copiar 
			beanDireccion.setIdPersona(Long.valueOf(personaDestino));
			
			tx = this.getUserBean(request).getTransactionPesada();
			tx.begin();	
			
			if(direcciones.length>0){
				for (int i = 0; i < direcciones.length; i++) {
					direccion=direcciones[i].split("&&");
					if(direccion.length>1){
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
			
			Vector instCliente = admCliente.getClientes(personaOrigen);
			String stInstitucion = "";
			String nombreInstitucion = "";
			int intInstitucion;
			int institucionEscogida = -1;
			// muevePersona
			
			for(int i=0;i<instCliente.size();i++){	
			// Recorremos la lista de clientes del colegiado origen
				stInstitucion = instCliente.get(i).toString();
				intInstitucion = Integer.parseInt(stInstitucion);
				nombreInstitucion = admInst.getAbreviaturaInstitucion(stInstitucion);
				if(admCliente.existeCliente(Long.parseLong(personaDestino),intInstitucion )!=null){
				// Si la persona destino ya es cliente de la institucion ...
					if(intInstitucion<=3000&&intInstitucion>2000){
					// Si la institucion es un colegio damos error porque no se puede hacer
						tx.rollback();
						throw new SIGAException("No se puede realizar la fusión porque ya existe como colegiado o no colegiado en "+nombreInstitucion);
					}
				}else{
					msgError= "Error al copiar los datos del cliente de " + nombreInstitucion;
					resul = EjecucionPLs.ejecutarPL_copiaCliente(personaDestino, personaOrigen, stInstitucion);
					acciones+=resul[1];
				}
			}
			
			msgError= "Error al mover los datos de la persona";
			resul = EjecucionPLs.ejecutarPL_mueveDatosPersona(personaDestino, personaOrigen);
			acciones+=resul[1];
			
			for(int i=0;i<instCliente.size();i++){	
			// Recorremos la lista de clientes del colegiado origen
				stInstitucion = instCliente.get(i).toString();
				intInstitucion = Integer.parseInt(stInstitucion);
				if(institucionEscogida == -1){
					if(intInstitucion<=3000&&intInstitucion>2000){
						institucionEscogida=intInstitucion;
					}
				}
				nombreInstitucion = admInst.getAbreviaturaInstitucion(stInstitucion);
				if(admCliente.existeCliente(Long.parseLong(personaDestino),intInstitucion )==null){
					msgError= "Error al borrar los datos del cliente de " + nombreInstitucion;
					resul = EjecucionPLs.ejecutarPL_borraCliente(personaOrigen, stInstitucion);
					acciones+=resul[1];
				}
			}
			msgError= "Error al borrar los datos de la persona";
			resul = EjecucionPLs.ejecutarPL_borraPersona(personaOrigen); // borraPersona
			acciones+=resul[1];
			
			if(institucionEscogida != -1){
				String resultado[] = EjecucionPLs.ejecutarPL_ActualizarDatosLetrado(
						institucionEscogida, 
						Long.valueOf(personaDestino), 
						10, 
						null, 
						Integer.parseInt(String.valueOf(user.getUserName())));
				if(resultado[0].equals("-1")){
					throw new SIGAException("Error en ejecución de paquete PKG_SIGA_CENSO.ACTUALIZARDATOSLETRADO. Contacte con el Administrador.");
				}
			}
			
			// Modificamos la persona para que modifique la fechamodificacion
			admPersona.update(admPersona.getPersonaPorId(personaDestino));
			beanHistorico.setIdPersona(Long.parseLong(personaDestino));
			beanHistorico.setIdInstitucion(Integer.parseInt(user.getLocation()));
			beanHistorico.setIdHistorico(admHistorico.getNuevoID(beanHistorico));
			beanHistorico.setFechaEfectiva(GstDate.getHoyJsp());
			beanHistorico.setFechaEntrada(GstDate.getHoyJsp());
			beanHistorico.setMotivo("Se ha realizado un mantenimiento de duplicados sobre esta persona.");
			beanHistorico.setDescripcion(acciones);
			beanHistorico.setIdTipoCambio(10); // Datos Generales
			
			admHistorico.insertarRegistroHistorico(beanHistorico, user);
					
			tx.commit();
		} catch (Exception e) {
			throwExcp(msgError,new String[] {"modulo.censo"},e,tx);
		}
		return exitoModal(msgSalida, request);
	}

	
	/**
	 * Abre la ventana de exportacion para que el proceso se haga en background
	 */
	protected String export(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		request.setAttribute("registrosSeleccionados", request.getParameter("registrosSeleccionados"));
		return "espera";
	}
	/**
	 * Exporta los resultados de la busqueda a excel
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */

	protected String exportar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
        Vector datos = new Vector();
        
        CenPersonaBean beanPersona = new CenPersonaBean();
        Vector direccionesPersona = new Vector();
        Vector colegiacionesPersona = new Vector();
        Hashtable estadoColegial = new Hashtable();
        Row estadoColegialRw = new Row();
        Hashtable colegiacionHt = new Hashtable();
        
        Vector vec = new Vector();
        Hashtable per = new Hashtable();
        formulario.setModo("abrir");
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idInstitucion=user.getLocation();
	        MantenimientoDuplicadosForm form = (MantenimientoDuplicadosForm)formulario;
	        CenColegiadoAdm colegiadoAdm = null;
			DuplicadosHelper admDuplicados = null;
			Vector datosTabla = null;
			Hashtable datosPersona = new Hashtable();
			Hashtable tablaDatos = new Hashtable();
			String idTipoPersona = null;
			EnvEnviosAdm enviosAdm = new EnvEnviosAdm(user);
			//enviosAdm.getNewIdEnvio(user);
			//Vector resultadoBusqueda=form.getResultadoBusqueda();
			int seleccionados = Integer.parseInt(form.getSeleccionados());
			tablaDatos=this.datosPersonas(mapping, formulario, request, response);
			int personas = (Integer)tablaDatos.get("numeroPersonas");
	        for (int i = 0; i < seleccionados; i++) {
	        	per=new Hashtable();
	        	// Recuperamos los datos de la persona (una tabla hash inicialmente ideada para mostrarse en gestionarDuplicados.jsp)
	        	datosPersona = (Hashtable) tablaDatos.get("persona"+i);
	        	
	        	direccionesPersona = (Vector) datosPersona.get("datosDirecciones");
	        	colegiacionesPersona = (Vector) datosPersona.get("datosColegiales");
	        	beanPersona = (CenPersonaBean) datosPersona.get("datosPersonales");

	        	per.put("IDENTIFICADOR", beanPersona.getIdPersona());
	        	per.put("NOMBRE", beanPersona.getNombre());
	        	per.put("APELLIDO1", beanPersona.getApellido1());
	        	per.put("APELLIDO2",beanPersona.getApellido2());
	        	per.put("NIF_CIF", beanPersona.getNIFCIF());
	        	per.put("LUGAR_NACIMIENTO",beanPersona.getNaturalDe());
	        	per.put("FECHA_NACIMIENTO",beanPersona.getFechaNacimiento());
	        	per.put("SANCIONES", datosPersona.get("sanciones"));
	        	per.put("CERTIFICADOS", datosPersona.get("certificados"));
	        	per.put("ULTIMAMODIFICACION", beanPersona.getFechaMod());
	        	for(int col = 0; col<colegiacionesPersona.size(); col++){
	        		Hashtable perCol = new Hashtable();
	        		perCol= (Hashtable) per.clone();
	        		colegiacionHt = (Hashtable) colegiacionesPersona.get(col);
	        		CenColegiadoBean colBean = new CenColegiadoBean();
	        		colBean = (CenColegiadoBean) colegiacionHt.get("datosColegiacion");
	        		perCol.put("COLEGIO", colegiacionHt.get("institucionColegiacion"));
	        		perCol.put("NCOLEGIADO", colBean.getNColegiado()+colBean.getNComunitario());
	        		perCol.put("FECHAINCORPORACION", colBean.getFechaIncorporacion());
	        		if(colBean.getComunitario().equalsIgnoreCase("1")){
	        			perCol.put("INSCRIPCION", "Inscrito");
	        		}else{
	        			perCol.put("INSCRIPCION", " ");
	        		}
	        		if(colBean.getSituacionResidente().equalsIgnoreCase("1")){
	        			perCol.put("RESIDENCIA", "Residente");
	        		}else{
	        			perCol.put("RESIDENCIA", " ");
	        		}
	        		estadoColegialRw = new Row();
	        		estadoColegialRw = (Row) colegiacionHt.get("estadoColegiacion");
	        		if(estadoColegialRw != null){
	        			estadoColegial = estadoColegialRw.getRow();
		        		perCol.put("ESTADOCOLEGIAL", estadoColegial.get("DESCRIPCION"));
		        		perCol.put("FECHAESTADO", estadoColegial.get("FECHAESTADO"));
	        		}else{
	        			perCol.put("ESTADOCOLEGIAL", "Sin estado");
		        		perCol.put("FECHAESTADO", "-");
	        		}
	        		if(direccionesPersona.size()>0){
		        		for (int j=0; j<direccionesPersona.size();j++){
							Hashtable direccion = (Hashtable)direccionesPersona.get(j);
							if(direccion.get("CEN_TIPODIRECCION.DESCRIPCION")!=null && direccion.get("CEN_TIPODIRECCION.DESCRIPCION").toString().contains("Censo")){
								perCol.put("DIRECCION", ""+direccion.get("DOMICILIO")+", "+direccion.get("POBLACION")+ " (" +direccion.get("PROVINCIA")+")");
								perCol.put("EMAIL", ""+direccion.get("CORREOELECTRONICO"));
								perCol.put("TELEFONO", ""+direccion.get("TELEFONO1"));
								perCol.put("FAX", ""+direccion.get("FAX1"));
								perCol.put("MOVIL", ""+direccion.get("MOVIL"));
							}else{
								perCol.put("DIRECCION", "");
								perCol.put("EMAIL", "");
								perCol.put("TELEFONO", "");
								perCol.put("FAX", "");
								perCol.put("MOVIL", "");
							}
						}
	        		}else{
	        			perCol.put("DIRECCION", "");
						perCol.put("EMAIL", "");
						perCol.put("TELEFONO", "");
						perCol.put("FAX", "");
						perCol.put("MOVIL", "");
	        		}
	        		
	        		vec.add(perCol);
	        	}
	        	
			}
	        if(vec!=null)
	        	datos.addAll(vec);
	        String[] cabeceras = null;
	        String[] campos = null;
			cabeceras = new String[]{"IDENTIFICADOR", "NOMBRE", "APELLIDO1", "APELLIDO2", "NIF", 
					"LUGAR NACIMIENTO","FECHA NACIMIENTO", "SANCIONES", "CERTIFICADOS", "ULTIMA MODIFICACION", "COLEGIO", 
					"NUMERO COLEGIADO", "FECHA INCORPORACION", "ESTADO", "FECHA ESTADO", "INSCRIPCION", "RESIDENCIA", 
					"DIRECCION", "EMAIL", "TELEFONO", "FAX", "MOVIL"};	
			
			campos = new String[]{"IDENTIFICADOR", "NOMBRE", "APELLIDO1", "APELLIDO2", "NIF_CIF", 
					"LUGAR_NACIMIENTO","FECHA_NACIMIENTO", "SANCIONES", "CERTIFICADOS", "ULTIMAMODIFICACION", "COLEGIO", 
					"NCOLEGIADO", "FECHAINCORPORACION", "ESTADOCOLEGIAL", "FECHAESTADO", "INSCRIPCION", "RESIDENCIA",
					"DIRECCION", "EMAIL", "TELEFONO", "FAX", "MOVIL"};

			request.setAttribute("campos",campos);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", "duplicados"+"_"+UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmss"));
						
			
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "generaExcel";
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
	protected Hashtable datosPersonas ( ActionMapping mapping, 		
										MasterForm formulario, 
										HttpServletRequest request, 
										HttpServletResponse response) throws SIGAException
	{
		Vector similares = new Vector();
		Vector colegiaciones = new Vector();
		CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
		CenClienteAdm admCliente = new CenClienteAdm(this.getUserBean(request));
		CenColegiadoAdm admColeg = new CenColegiadoAdm(this.getUserBean(request));
		CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));
		CerSolicitudCertificadosAdm admCertificados = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request)); 
		Hashtable datos = new Hashtable();
		// Vamos a leer los datos de la bbdd para cargarlos en la ventana de consulta de colegiaciones 
		try {
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			//ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			//aalg-----------
			Vector vdatos=new Vector();
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			String seleccionados ="";
			PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
			for (int i=1;i<=paginador.getNumeroPaginas();i++){
				vdatos = paginador.obtenerPagina(i);
				for (int j=0;j<vdatos.size();j++){
					Hashtable hdatos = (Hashtable)vdatos.get(j);
					seleccionados += "null||" + (String)hdatos.get("IDPERSONA") + ",";
				}
			}
			seleccionados = seleccionados.substring(0, seleccionados.length()-1);
			
			//aalg-----------
			//String seleccionados = request.getParameter("registrosSeleccionados");
			if(seleccionados == null||seleccionados.equalsIgnoreCase(""))
				seleccionados = miFormulario.getSeleccion();
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
						
						Vector vDirecciones = admCliente.getDirecciones(Long.valueOf(idPersona), Integer.valueOf(2000), false);
						for (int j=0; j<vDirecciones.size();j++){
							Hashtable dir = (Hashtable)vDirecciones.get(j);
							dir.put("FECHAMODIFICACION", UtilidadesString.formatoFecha(dir.get("FECHAMODIFICACION").toString(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						
						}
						hashPersona.put("datosDirecciones", vDirecciones);
						
						int cert = admCertificados.getNumeroCertificados("2000",idPersona);
						
					}else{
						idPersona = claves[0];
					}
					
					datos.put("persona"+i, hashPersona);
					datos.put("numeroPersonas", i+1);
				}
				
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return datos;
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
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			}else{
				
			  
				//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
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
			
			request.getSession ().setAttribute ("CenBusquedaClientesTipo", "DUPLICADOS");  
			 
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
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return "inicio";
	}
}
