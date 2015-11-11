package com.siga.censo.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenSancionAdm;
import com.siga.beans.CenSancionBean;
import com.siga.censo.form.SancionesLetradoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso SANCIONES DEL LETRADO
 * @author RGG
 */
public class SancionesLetradoAction extends MasterAction 
{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */

	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("abrirVolver")) {
				request.setAttribute("volver", "volver");
				mapDestino = volver(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("fecha")) {
				mapDestino = fecha(mapping, miForm, request, response);
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				if (miForm.getModal().equalsIgnoreCase("TRUE")) {
					request.setAttribute("exceptionTarget", "parent.modal");
				}
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}
		
		return mapping.findForward(mapDestino);
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm) formulario;
			miform.clear();
			String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
			request.setAttribute("tienepermiso", tienepermisoArchivo);
			miform.reset(mapping, request);
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
			request.getSession().removeAttribute(identificadorFormularioBusqueda);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		
		return "inicio";
	}

	private String volver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			SancionesLetradoForm miForm = (SancionesLetradoForm) formulario;
			miForm.clear();
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
			SancionesLetradoForm sancionesForm = (SancionesLetradoForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
			if (sancionesForm != null) {
				miForm.setNombreInstitucionBuscar(sancionesForm.getNombreInstitucionBuscar());
				miForm.setTipoSancionBuscar(sancionesForm.getTipoSancionBuscar());
				miForm.setRefCGAE(sancionesForm.getRefCGAE());
				miForm.setRefColegio(sancionesForm.getRefColegio());
				miForm.setColegiadoBuscar(sancionesForm.getColegiadoBuscar());
				miForm.setChkRehabilitado(sancionesForm.getChkRehabilitado());
				miForm.setMostrarTiposFechas(sancionesForm.getMostrarTiposFechas());
				miForm.setFechaInicioBuscar(sancionesForm.getFechaInicioBuscar());
				miForm.setFechaFinBuscar(sancionesForm.getFechaFinBuscar());
				miForm.setMostrarSanciones(sancionesForm.getMostrarSanciones());
				miForm.setFechaInicioArchivada(sancionesForm.getFechaInicioArchivada());
				miForm.setFechaFinArchivada(sancionesForm.getFechaFinArchivada());
			}
			String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
			request.setAttribute("tienepermiso", tienepermisoArchivo);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		
		return "inicio";
	}

	/**
	 * Metodo que implementa el modo buscar desde la ficha colegian (Situacion Colegial)
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm) formulario;
			String accion = (String) request.getParameter("accion");
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));
			Vector resultado = admSancion.getSancionesLetrado(miform.getIdPersona(), miform.getIdInstitucionAlta());
			String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
			request.setAttribute("tienepermisoArchivo", tienepermisoArchivo);

			// RGG indico que estamos en la pestaña de letrado (Datos de colegiacion)
			request.setAttribute("datosColegiacion", "1");
			request.setAttribute("personaColegiacion", miform.getIdPersona());
			request.setAttribute("institucionColegiacion", miform.getIdInstitucionAlta());
			request.setAttribute("ACCION", accion);
			request.setAttribute("resultado", resultado);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		
		return "resultados";
	}	
	
	/**
	 * Metodo que implementa el modo buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean user = null;
		UserTransaction tx = null;

		try {
			SancionesLetradoForm miform = (SancionesLetradoForm) formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion = user.getLocation();
			String username = user.getUserName();
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));

			HashMap databackup = new HashMap();

			String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
			request.setAttribute("tienepermisoArchivo", tienepermisoArchivo);
			databackup.put("tienepermisoArchivo", tienepermisoArchivo);
			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String) request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {
						// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				databackup = new HashMap();

				// obtengo datos de la consulta
				Paginador resultado = null;
				Vector datos = null;

				String tipobusqueda = "";
				if (miform.getMostrarSanciones() != null) {
					tipobusqueda = ClsConstants.COMBO_MOSTRAR_ARCHIVADAS;
				} else
					tipobusqueda = ClsConstants.COMBO_MOSTRAR_SINARCHIVAR;

				// se Obtienen los datos de las consulta para recuperar los datos de las sanciones archivadas o sin archivar.
				resultado = admSancion.getSancionesBuscar(miform, user.getLocation(), tipobusqueda);

				databackup.put("paginador", resultado);
				if (resultado != null) {
					datos = resultado.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				}
			}
			
			request.setAttribute("miform", miform);
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
			request.getSession().setAttribute(identificadorFormularioBusqueda, miform.clone());			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
		}
		
		return "resultados";
	}
	
	/**
	 * Metodo que implementa el modo modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		String salida = "";
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion = usr.getLocation();
			String username = usr.getUserName();
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));

			// Obtengo los datos del formulario
			SancionesLetradoForm miForm = (SancionesLetradoForm) formulario;
			boolean checkFirmeza = UtilidadesString.stringToBoolean(miForm.getChkFirmeza());
			boolean checkRehabilitado = UtilidadesString.stringToBoolean(miForm.getChkRehabilitado());
			boolean checkArchivada = UtilidadesString.stringToBoolean(miForm.getChkArchivada());
		    
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, miForm.getIdSancion());						
			UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, miForm.getTipoSancion());
			UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, usr.getLocation());						
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, miForm.getNombreInstitucion());						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaAcuerdo()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaFin()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, (miForm.getFirmeza()!=null && !miForm.getFirmeza().equals(""))?GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFirmeza()):"");						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaInicio()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, (miForm.getRehabilitado()!=null && !miForm.getRehabilitado().equals(""))?GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getRehabilitado()):"");						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaResolucion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaImposicion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, miForm.getObservaciones());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, miForm.getRefCGAE());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, miForm.getRefColegio());						
			UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, miForm.getTexto());
			
			String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
			if (tienepermisoArchivo.equals("1")) {
				boolean checkArchivada1 = UtilidadesString.stringToBoolean(miForm.getChkArchivada());
				UtilidadesHash.set(hash, CenSancionBean.C_FECHAARCHIVADA, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaArchivada()));
				if (checkArchivada1) {
					UtilidadesHash.set(hash, CenSancionBean.C_CHKARCHIVADA, "1");
				} else {
					UtilidadesHash.set(hash, CenSancionBean.C_CHKARCHIVADA, "0");
				}
			} else {
				UtilidadesHash.set(hash, CenSancionBean.C_CHKARCHIVADA, "0");
			}


			if (checkFirmeza) {
				if(ClsConstants.esConsejoGeneral(idinstitucion)){
					UtilidadesHash.set(hash, CenSancionBean.C_CHKFIRMEZA, "1");
					
				/** Se comprueba si están rellenas la fecha inicio y fin de firmeza SOLO si es colegio **/
				}else if (ClsConstants.esColegio(idinstitucion) && miForm.getFechaInicio() != null && !miForm.getFechaInicio().equals("") && miForm.getFechaFin() != null && !miForm.getFechaFin().equals("")) {
					UtilidadesHash.set(hash, CenSancionBean.C_CHKFIRMEZA, "1");
					
				} else {
					throw new SIGAException("censo.sancionesLetrado.error.fechasInicioFinFirmeza");
				}				
			} else {
				UtilidadesHash.set(hash, CenSancionBean.C_CHKFIRMEZA, "0");
			}
			
			if (checkRehabilitado) {
				UtilidadesHash.set(hash, CenSancionBean.C_CHKREHABILITADO, "1");
			} else {
				UtilidadesHash.set(hash, CenSancionBean.C_CHKREHABILITADO, "0");
			}
			
			Hashtable hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			boolean checkFirmezaOriginal = UtilidadesString.stringToBoolean((String)hashOriginal.get("CHKFIRMEZA"));

			/** Comienzo control de transacciones **/
			tx = usr.getTransaction();
			tx.begin();		
			
			/** ACTUALIZAMOS EL REGISTRO EN CEN_SANCION EN EL COLEGIO **/
			if (!admSancion.updateDirect(hash, admSancion.getClavesBean(), admSancion.getCamposBean())) {
				throw new ClsExceptions("Error al realizar el update: " + admSancion.getError());
			}				
			
			/** CR - TRASPASO DE DATOS AL CGAE SOLO SI SE HA MODIFICACDO EL CHECK DE FIRMEZA**/
			if (checkFirmezaOriginal != checkFirmeza) {
				Hashtable hashCGAE = (Hashtable) hash.clone();
				// Guardamos la informacion del idSancionOrigen para actualizar sus cambios en el colegio
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDSANCIONORIGEN, miForm.getIdSancion());
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDINSTITUCIONORIGEN, idinstitucion);
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDINSTITUCION, ClsConstants.INSTITUCION_CGAE);
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_FECHAENVIADO, "");
				
				/** Lo primero es comprobar si existe ya el registro o no en el CGAE **/
				Hashtable hashBusqueda = new Hashtable();
				hashBusqueda.put(CenSancionBean.C_IDPERSONA, miForm.getIdPersona());
				hashBusqueda.put(CenSancionBean.C_IDSANCIONORIGEN, miForm.getIdSancion()); 	
				hashBusqueda.put(CenSancionBean.C_IDINSTITUCIONORIGEN, idinstitucion);				
				Vector v = admSancion.select(hashBusqueda);
				CenSancionBean b = null;
				String idSancionCGAE = null;
				if (v != null && v.size() > 0) {
					b = (CenSancionBean) v.get(0);
					if (b.getIdSancion() != null) {
						idSancionCGAE = String.valueOf(b.getIdSancion());
						UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDSANCION, idSancionCGAE);
					}
				}
				
				switch (cumpleCondicionesTraspasoCGAE(miForm,idinstitucion)) {
					case 1: // SE HACE TRASPASO AL CGAE CON NUEVA FIRMEZA
						UtilidadesHash.set(hashCGAE, CenSancionBean.C_FECHATRASPASO, "SYSDATE");
						if (idSancionCGAE != null) { // Si existe un registro en el CGAE hay que borrar la fechaEnviado y actualizar los campos
							if (!admSancion.updateDirect(hashCGAE, admSancion.getClavesBean(), admSancion.getCamposBean())){
								throw new ClsExceptions("Error al realizar el update en CGAE (Traspaso de Datos): " + admSancion.getError());
							}
							
						} else { // Si NO existe un registro en el CGAE se crea el registro 
							idSancionCGAE = admSancion.getNuevoId(String.valueOf(ClsConstants.INSTITUCION_CGAE));
							UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDSANCION, idSancionCGAE);
							if (!admSancion.insert(hashCGAE)) {
								throw new ClsExceptions("Error al realizar el insert en CGAE (Traspaso de Datos): " + admSancion.getError());
							}
						}
						break;
	
					case 2: // SE HA DE BORRAR LA FIRMEZA TAMBIEN EN EL CGAE
						UtilidadesHash.set(hashCGAE, CenSancionBean.C_FECHATRASPASO, b.getFechaTraspaso());
						if (!admSancion.updateDirect(hashCGAE, admSancion.getClavesBean(), admSancion.getCamposBean())) {
							throw new ClsExceptions("Error al realizar el update en CGAE (Borrado de firmeza en Traspaso de Datos): " + admSancion.getError());
						}						
						break;
	
					default:// NO SE HACE NADA
						break;
				}
			}
			
			tx.commit();			
			
			/** ACTUALIZAMOS EL DATABACKUP **/
			request.removeAttribute("DATABACKUP");
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", "modificar");
			salida = this.exito("messages.updated.success",request);
			
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
		
	   return salida;	
	}

	/**
	 * @param miForm
	 * @param idinstitucion 
	 * @return
	 * @throws SIGAException 
	 */
	private int cumpleCondicionesTraspasoCGAE(SancionesLetradoForm miForm, String idInstitucion) throws SIGAException {
		/** ACCION -1: NO HAY QUE HACER TRASASO AL CGAE **/
		int accion = -1;
		boolean checkFirmeza = UtilidadesString.stringToBoolean(miForm.getChkFirmeza());
		
		// Solo se traspasa para instituciones que no sean consejos
		if(!ClsConstants.esColegio(idInstitucion)){
			return -1;
		}
		
		//1ª Condicion: Sancion <> Amonestaciones y Apercibimientos
		if(miForm.getTipoSancion() != null && !miForm.getTipoSancion().equals(String.valueOf(ClsConstants.TIPO_SANCION_APERCIBIMIENTO))){
			//2ª Condicion: Sancion sea firme
			if (checkFirmeza) {
				// 2.1 Condicion: Obligatorio Fecha Firmeza SOLO en colegios
				if (miForm.getFirmeza() != null && !miForm.getFirmeza().equals("")) {
					// 2.2 Condicion: Obligatorio Fecha Inicio y Fecha Fin
					if (miForm.getFechaInicio() != null && !miForm.getFechaInicio().equals("") && miForm.getFechaFin() != null && !miForm.getFechaFin().equals("")) {
						/** ACCION 1: SE HACE TRASPASO AL CGAE CON NUEVA FIRMEZA **/
						accion = 1;
					} else {
						throw new SIGAException("censo.sancionesLetrado.error.fechasInicioFinFirmeza");
					}
				} else {
					throw new SIGAException("censo.sancionesLetrado.error.fechaFirmeza");
				}
				
			} else {
				/** ACCION 2: SE HA DE BORRAR LA FIRMEZA TAMBIEN EN EL CGAE **/
				accion = 2;
			}
		}
		return accion;
	}

	/**
	 * Metodo que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Hashtable hashOriginal = new Hashtable();
		UserTransaction tx = null;
		String salida = "";
		// Obtengo los datos del formulario
		SancionesLetradoForm miForm = (SancionesLetradoForm) formulario;
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion = usr.getLocation();
			String username = usr.getUserName();

			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));

			boolean checkFirmeza = UtilidadesString.stringToBoolean(miForm.getChkFirmeza());
			boolean checkRehabilitado = UtilidadesString.stringToBoolean(miForm.getChkRehabilitado());

			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = new Hashtable();
			String idSancion = admSancion.getNuevoId(idinstitucion);
			miForm.setIdSancion(idSancion);
			UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, idSancion);						
			UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, miForm.getTipoSancion());
			UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, idinstitucion);						
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, miForm.getNombreInstitucion());						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaAcuerdo()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaFin()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFirmeza()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaInicio()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getRehabilitado()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaResolucion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaImposicion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, miForm.getObservaciones());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, miForm.getRefCGAE());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, miForm.getRefColegio());	
			UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, miForm.getTexto());	
			
			String tienepermisoArchivo=this.getTienePermisoArchivacion(mapping, request);
			
			if (tienepermisoArchivo.equals("1")){
				boolean checkArchivada  = UtilidadesString.stringToBoolean(miForm.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaArchivada()));
				if  (checkArchivada){
					UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"1");	
				}else{
					UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"0");
				}
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"0");
			}
			
			
			if (checkFirmeza){
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA,"1");	
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA,"0");
			}
			if (checkRehabilitado){
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO,"1");	
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO,"0");
			}
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
			
			/** INSERTAMOS EL REGISTRO EN CEN_SANCION EN EL COLEGIO **/
			if (!admSancion.insert(hash)) {
				throw new ClsExceptions("Error al realizar el insertar Sancion Letrado: " + admSancion.getError());
			}			
		
			/** CR - TRASPASO DE DATOS AL CGAE SOLO SI SE HA ACTIVADO EL CHECK DE FIRMEZA**/
			if (checkFirmeza && cumpleCondicionesTraspasoCGAE(miForm, idinstitucion) > 0) {
				Hashtable hashCGAE = (Hashtable) hash.clone();
				String idSancionCGAE = admSancion.getNuevoId(String.valueOf(ClsConstants.INSTITUCION_CGAE));
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDSANCION, idSancionCGAE);				
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDINSTITUCION, ClsConstants.INSTITUCION_CGAE);
				// Metemos en el hash de la sancion del colegio la referencia al registro traspasado al CGAE y la fecha de traspaso
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_FECHATRASPASO, "SYSDATE");
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDSANCIONORIGEN, idSancion);
				UtilidadesHash.set(hashCGAE, CenSancionBean.C_IDINSTITUCIONORIGEN, idinstitucion);
				if (!admSancion.insert(hashCGAE)) {
					throw new ClsExceptions("Error al realizar el insert en CGAE (Traspaso de Datos): " + admSancion.getError());
				}
			}
			
			tx.commit();			
			request.getSession().setAttribute("DATABACKUP",hash);
			
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
		
	   return exito("messages.inserted.success", request);
	}
	
	/**
	 * Metodo que implementa el modo borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Hashtable hashOriginal = new Hashtable();
		UserTransaction tx = null;
		String salida = "";
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));

			// Obtengo los datos del formulario
			SancionesLetradoForm miForm = (SancionesLetradoForm) formulario;

			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash, CenSancionBean.C_IDPERSONA, (String) miForm.getDatosTablaOcultos(0).get(0));
			UtilidadesHash.set(hash, CenSancionBean.C_IDSANCION, (String) miForm.getDatosTablaOcultos(0).get(1));
			UtilidadesHash.set(hash, CenSancionBean.C_IDINSTITUCION, usr.getLocation());
			Vector v = admSancion.selectByPK(hash);
			CenSancionBean b = null;
			if (v != null && v.size() > 0) {
				b = (CenSancionBean) v.get(0);
			} else {
				throw new ClsExceptions("Error al obtener el elemento para borrarlo: NO EXISTE");
			}

			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();

			if (!admSancion.delete(b)) {
				throw new ClsExceptions("Error al realizar el borrado: " + admSancion.getError());
			}

			tx.commit();
			salida = this.exitoRefresco("messages.updated.success", request);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
		}
		
		return salida;
	}
	
	/**
	 * Metodo que implementa el modo nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean user = null;
		UserTransaction tx = null;

		try {
			SancionesLetradoForm miform = (SancionesLetradoForm) formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion = user.getLocation();
			String username = user.getUserName();
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));
			String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
			// RGG indico si estamos en pestaña de datos de colegiacion o no
			request.setAttribute("pestanaColegiacion", request.getParameter("pestanaColegiacion"));
			request.setAttribute("personaColegiacion", request.getParameter("personaColegiacion"));
			request.setAttribute("institucionColegiacion", request.getParameter("institucionColegiacion"));
			request.setAttribute("tienepermiso", tienepermisoArchivo);
			request.setAttribute("accion", "insertar");
			
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
			request.getSession().removeAttribute(identificadorFormularioBusqueda);
			request.getSession().setAttribute(identificadorFormularioBusqueda, miform.clone());				

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
		}

		return "editar";
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
		UsrBean user = null;
		UserTransaction tx = null;

		try {
			SancionesLetradoForm miform = (SancionesLetradoForm) formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion= user.getLocation();
            String username=user.getUserName();
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hashF = new Hashtable();
			if(miform.getDatosTablaOcultos(0) != null){
				hashF.put(CenSancionBean.C_IDPERSONA, (String)miform.getDatosTablaOcultos(0).get(0));
				hashF.put(CenSancionBean.C_IDSANCION, (String)miform.getDatosTablaOcultos(0).get(1));
			} else {
				hashF.put(CenSancionBean.C_IDPERSONA, miform.getIdPersona());
				Hashtable databackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
				hashF.put(CenSancionBean.C_IDSANCION, (String) databackup.get(CenSancionBean.C_IDSANCION));
			}
			hashF.put(CenSancionBean.C_IDINSTITUCION, idinstitucion); 
			Vector v = admSancion.select(hashF);
			CenSancionBean b = null;
			if (v!=null && v.size()>0) {
				b = (CenSancionBean) v.get(0);
				Hashtable hash = new Hashtable();
				UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, String.valueOf(b.getIdSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, String.valueOf(b.getIdTipoSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, String.valueOf(b.getIdInstitucionSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, b.getRefColegio());
				UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, b.getRefCGAE());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, b.getFechaAcuerdo());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, b.getFechaFin());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, b.getFechaFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, b.getFechaInicio());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, b.getFechaRehabilitado());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, b.getFechaResolucion());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, b.getFechaImposicion());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA, b.getChkFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO, b.getChkRehabilitado());
				UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, b.getTexto());
				UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, b.getObservaciones());				
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA, b.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, b.getFechaArchivada());
				
				String tienepermisoArchivo = this.getTienePermisoArchivacion(mapping, request);
				request.removeAttribute("DATABACKUP");
				request.getSession().setAttribute("DATABACKUP",hash);	
				String pestCenso = (String) request.getParameter("pestanaColegiacion");
				request.setAttribute("pestanaColegiacion", pestCenso);				
				request.setAttribute("tienepermiso",tienepermisoArchivo);
				request.setAttribute("registro",hash);
				request.setAttribute("accion", "modificar");
			} else {
				throw new ClsExceptions("Error al obtener el elemento para borrarlo: NO EXISTE");
			}
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		
	    return "editar";
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
		UsrBean user = null;
		UserTransaction tx = null;

		try {
			SancionesLetradoForm miform = (SancionesLetradoForm) formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String username = user.getUserName();

			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hashF = new Hashtable();
			hashF.put(CenSancionBean.C_IDPERSONA, (String)miform.getDatosTablaOcultos(0).get(0));
			hashF.put(CenSancionBean.C_IDSANCION, (String)miform.getDatosTablaOcultos(0).get(1)); 	
			hashF.put(CenSancionBean.C_IDINSTITUCION, (String)miform.getDatosTablaOcultos(0).get(2)); 
			Vector v = admSancion.select(hashF);
			CenSancionBean b = null;
			if (v!=null && v.size()>0) {
				b = (CenSancionBean) v.get(0);
				Hashtable hash = new Hashtable();
				UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, String.valueOf(b.getIdSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, String.valueOf(b.getIdTipoSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, String.valueOf(b.getIdInstitucionSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, b.getRefColegio());
				UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, b.getRefCGAE());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, b.getFechaAcuerdo());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, b.getFechaFin());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, b.getFechaFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, b.getFechaInicio());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, b.getFechaRehabilitado());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, b.getFechaResolucion());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, b.getFechaImposicion());
				UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, b.getTexto());
				UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, b.getObservaciones());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA, b.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, b.getFechaArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA, b.getChkFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO, b.getChkRehabilitado());
				
				String tienepermisoArchivo= this.getTienePermisoArchivacion(mapping, request);	
				String pestCenso = (String) request.getParameter("pestanaColegiacion");
				request.setAttribute("pestanaColegiacion", pestCenso);
				request.setAttribute("tienepermiso",tienepermisoArchivo);
				request.setAttribute("modo", "Ver");
				request.setAttribute("registro",hash);
			} else {
				throw new ClsExceptions("Error al obtener el elemento para borrarlo: NO EXISTE");
			}
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		
	    return "editar";
	}
	
	
	/**
	 * Devuelve los datos necesarios para la insercion de un nuevo estado
	 */
	protected String fecha(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		request.setAttribute("modo", "archivar");
		return "archivar";
	}
	
	/**
	 *Se Verifica que el usuario tiene permiso de Archivazión.
	 * @param form Formulario con los criterios
	 * @return se muestra un resultado con un numero si tiene permiso.
	 * @throws ClsExceptions
	 */
	public String getTienePermisoArchivacion(ActionMapping mapping, HttpServletRequest request) throws ClsExceptions {
		String permiso = "1";
		try {

			String accessEnvio = testAccess(request.getContextPath() + "/CEN_SancionesArchivadas.do", null, request);
			if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				permiso = "0";
			}
			// Hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath() + mapping.getPath() + ".do", null, request);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar getTienePermisoArchivación.");
		}
		return permiso;
	} // getTienePermisoArchivación()

}