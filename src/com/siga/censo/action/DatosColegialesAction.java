
package com.siga.censo.action;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColaCambioLetradoAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDatosColegialesEstadoAdm;
import com.siga.beans.CenDatosColegialesEstadoBean;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenEstadoColegialBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenTiposSeguroAdm;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.censo.form.DatosColegialesForm;
import com.siga.censo.form.HistoricoForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Clase action para la visualizacion y mantenimiento de los datos colegiales
 * generales. Gestiona la edicion, borrado, consulta y mantenimiento
 * 
 * @author miguel.villegas
 * @since 28-12-2004
 * @version 2008-06-02 - adrian.ayala - revision de modificarDatos(), 
 *   para asegurar el correcto funcionamiento de la insercion en cola 
 *   para la copia de direcciones de colegiados a Consejos
 */
public class DatosColegialesAction extends MasterAction {

	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	
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
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir") || accion.equalsIgnoreCase("abrirEditarNColegiado")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("modificardatos")){
					mapDestino = modificarDatos(mapping, miForm, request, response);
				}else if ( accion.equalsIgnoreCase("getAjaxInscripciones")){
						getAjaxInscripciones (request, response);
						return null;
				}else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
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
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String numero = "";
		String nombre = "";
		String result = "abrir";
		CenColegiadoBean datosColegiales;		
		Vector datosEstado;		
		Vector datosSeguro = null;
		Object remitente = null;
		boolean esResidente = true;

		try {
			DatosColegialesForm miForm = (DatosColegialesForm) formulario;
			String pestanasituacion=request.getParameter("pestanaSituacion");
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();

			String accion;
			Long idPersona;
			Integer idInstPers;
			// Si he cambiado el tipo de colegiacion de comunitario a espanol se permite modificar el numero de colegiado. Vengo de editar
			if (miForm.getModo() != null && miForm.getModo().equalsIgnoreCase("abrirEditarNColegiado")) {
			    accion = "editar";
			    idPersona  = new Long (miForm.getIdPersona());
				idInstPers = new Integer (miForm.getIdInstitucion());
				request.setAttribute("editarNColegiado", "1");
			}
			else {
			  if (miForm.getModo() != null && miForm.getModo().equalsIgnoreCase("abrir")) {// Si se hace cualquier otra modificacion no se tiene que dejar
			  	                                                                           // modificar el numero de colegiado. Vengo de editar
					 accion = "editar";
					 idPersona  = new Long (miForm.getIdPersona());
					 idInstPers = new Integer (miForm.getIdInstitucion());
			  }else{ 
				accion = (String)request.getParameter("accion");

				// RGG 02-02-2005 Para controlar si estamos en alta
				if (accion.equals("nuevo") || (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals(""))) {
					return "clienteNoExiste";
				}

				// RGG 05-10-2005 para editar el ncolegiado si vienes desde solicitud incorporacion
				String editarNColegiado = (String)request.getParameter("editarNColegiado");
				if (editarNColegiado!=null) {
					request.setAttribute("editarNColegiado", editarNColegiado);
				}
				
				// Obtengo el identificador de persona, la accion y el identificador de institucion del cliente
				idPersona  = new Long(request.getParameter("idPersona"));
				idInstPers = new Integer(request.getParameter("idInstitucion"));
			  }	
			}
			
			String idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();

			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserName(request),user,idInstPers.intValue(),idPersona.longValue());

			//CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserName(request));		
			CenTiposSeguroAdm admSeguro = new CenTiposSeguroAdm(this.getUserBean(request));		

			// Obtengo informacion del colegiado
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
			datosColegiales=colegiadoAdm.getDatosColegiales(idPersona,idInstPers);
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			datosEstado=colegiadoAdm.getEstadosColegiales(idPersona,idInstPers, this.getLenguaje(request));
			if (datosColegiales.getIdTipoSeguro()!=null){
				datosSeguro=admSeguro.obtenerEntradaTiposSeguro(datosColegiales.getIdTipoSeguro().toString());
			}

			// Comprueba si el usuario reside en algun otro colegio
			if (idPersona.compareTo(new Long(1))>0){
				esResidente=colegiadoAdm.getResidenciaColegio(idPersona.toString(),idInstitucionPersona);
			}	
			
			if (esResidente){
				request.setAttribute("RESIDENTE", ClsConstants.DB_TRUE);
			}
			else{
				request.setAttribute("RESIDENTE", ClsConstants.DB_FALSE);
			}
			
			// Si la operacion puede implicar modificacion realizo una copia de los datos originales
			// para el proceso de modificacion en la BBDD
			if (accion.equalsIgnoreCase("edicion")||accion.equalsIgnoreCase("editar")){
				request.getSession().setAttribute("DATABACKUP",datosColegiales);
				if (datosEstado.size()<1)
					request.getSession().setAttribute("DATABACKUP_EST",new Row());
				else
					request.getSession().setAttribute("DATABACKUP_EST",datosEstado.firstElement());
			}			
			// in5755 // Recuperamos el estado colegial para mostrarlo en la cabecera
			CenClienteAdm admCli = new CenClienteAdm(this.getUserBean(request));
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);			
			request.setAttribute("ACCION", accion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
			
			String	estadoColegial=admCli.getEstadoColegial(idPersona.toString(), idInstitucionPersona);
			if(estadoColegial==null) 
				estadoColegial="";
			
			request.setAttribute("ESTADOCOLEGIAL",estadoColegial );
			
			request.setAttribute("DATCOLEGIAL", datosColegiales);
			request.setAttribute("DATESTADO", datosEstado);
			request.setAttribute("DATSEGURO", datosSeguro);
			request.setAttribute("PESTANASITUACION", pestanasituacion);
		
			// Paso el modo
			if (accion.equalsIgnoreCase("edicion")||accion.equalsIgnoreCase("editar")){
				remitente=(Object)"modificar";
			}
			else{
				remitente=(Object)"consulta";				
			}
			request.setAttribute("modelo",remitente);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 									
		return result;
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "editar";
		try { 
			DatosColegialesForm form = (DatosColegialesForm) formulario;

			Vector ocultos = new Vector();
			CenDatosColegialesEstadoBean infoEntrada=new CenDatosColegialesEstadoBean();		
		
			int idInstitucionPersona = new Integer(form.getIdInstitucion()).intValue();
			long idPersona = new Long(form.getIdPersona()).longValue();
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			CenDatosColegialesEstadoAdm admin=new CenDatosColegialesEstadoAdm(this.getUserName(request),user,idInstitucionPersona,idPersona);			
			
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)formulario.getDatosTablaOcultos(0);
			
			// Obtener los datos colegiales del usuario
			infoEntrada=(CenDatosColegialesEstadoBean)admin.obtenerEntradaEstadoColegial((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2));			
			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", infoEntrada);
			request.setAttribute("NOMBRE", (String)ocultos.get(3));
			request.setAttribute("NUMERO", (String)ocultos.get(4));			
			
			// Paso el origen			
			Object remitente=(Object)"modificar";
			request.setAttribute("modelo",remitente);			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		return (result);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result = "ver";
		return result;
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
						
		String result="nuevo";

		try{
			String nombre="";
			String numero="";		
			CenColegiadoBean datosColegiales;		
			 			
			// Obtengo los datos del formulario (idPersona e idInstitucion)
			DatosColegialesForm miForm = (DatosColegialesForm)formulario;
			String idPersona=miForm.getIdPersona();
			String idInstitucion=miForm.getIdInstitucion();			

			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));			

			// Obtengo informacion del colegiado
			nombre = personaAdm.obtenerNombreApellidos(idPersona);
			datosColegiales=colegiadoAdm.getDatosColegiales(Long.valueOf(idPersona),Integer.valueOf(idInstitucion));
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);			
			
			String idEstado = "";
			try {
			    Vector v = colegiadoAdm.getEstadosColegiales(new Long(idPersona), new Integer(idInstitucion), this.getLenguaje(request));
			    idEstado = UtilidadesHash.getString (((Row)v.get(0)).getRow(), CenEstadoColegialBean.C_IDESTADO);
			}
			catch (Exception e) {
			    idEstado = "";
            }
			
			//Fecha actual
			String fechaEstado = UtilidadesBDAdm.getFechaBD("");
			
			CenDatosColegialesEstadoBean infoEntrada=new CenDatosColegialesEstadoBean();
			infoEntrada.setFechaEstado(fechaEstado);
			request.setAttribute("container", infoEntrada);
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA",idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);			
			request.setAttribute("ID_ESTADO_ULTIMO", idEstado);			
			
			// Paso el origen
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);									 
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}			
		return result;
	}

	/**
	 * Inserta un nuevo estado colegial en el sistema.
	 * 
	 * Ademas, realiza las siguientes comprobaciones:
	 *  a. que el nuevo estado sea posterior al existente anterior
	 *  b. cambio de estado de un colegiado comunitario
	 *  c. si tiene cosas pendientes de SJCS (Guardias y Designas)
	 * 
	 * @param mapping - Mapeo de los struts
	 * @param formulario - Action Form asociado a este Action
	 * @param request - objeto llamada HTTP
	 * @param response - objeto respuesta HTTP
	 * @return String - Destino del action
	 * @exception SIGAException - En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles generales
		UsrBean usr = this.getUserBean(request);
		String idioma = usr.getLanguage();
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		CenClienteAdm admCliente = new CenClienteAdm(usr);
		CenColegiadoAdm admColegiado = new CenColegiadoAdm(usr);
		CenDatosColegialesEstadoAdm admEstados = new CenDatosColegialesEstadoAdm(usr);
		UserTransaction tx = null;
		String result = "exception";

		// Variables
		String idinstitucion;
		String idpersona;
		String fechaEstado;
		String motivo;

		int pendienteSJCS;

		try {
			// obteniendo datos del formulario
			idinstitucion = miForm.getIdInstitucion();
			idpersona = miForm.getIdPersona();
			fechaEstado = miForm.getFechaEstado();
			motivo = miForm.getMotivo();

			// obteniendo los valores originales del registro de estado colegial anterior
			Hashtable original;
			String fechaEstadoOrigen = "";
			if (request.getSession().getAttribute("DATABACKUP_EST") != null) {
				original = ((Row) request.getSession().getAttribute("DATABACKUP_EST")).getRow();
				if (original != null)
					fechaEstadoOrigen = (String) original.get(CenDatosColegialesEstadoBean.C_FECHAESTADO);
			} else {
				original = new Hashtable();
			}

			// a. comprobando que el nuevo estado sea posterior al existente anterior
			if (!fechaEstadoOrigen.equals("")) {
				SimpleDateFormat formatoForm = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				Date dateNueva = formatoForm.parse(fechaEstado);
				SimpleDateFormat formatoInterno = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				Date dateOrigen = formatoInterno.parse(fechaEstadoOrigen);
				String fechaOriSinHoras = formatoForm.format(dateOrigen);
				Date dateOriSinHoras = formatoForm.parse(fechaOriSinHoras);
				if (dateNueva.before(dateOriSinHoras) || dateNueva.equals(dateOriSinHoras))
					return exito("messages.general.error.fechaPosteriorActual1", request);
			}

			// b. controlando cambio de estado de un colegiado comunitario
			Hashtable clave = new Hashtable();
			clave.put(CenColegiadoBean.C_IDPERSONA, idpersona);
			clave.put(CenColegiadoBean.C_IDINSTITUCION, idinstitucion);
			Vector v = admColegiado.selectByPK(clave);
			if (v != null && v.size() > 0) {
				CenColegiadoBean beanCol = (CenColegiadoBean) v.get(0);

				if (original != null) {
					if (beanCol.getComunitario().equals(ClsConstants.DB_TRUE)
							&& miForm.getCmbEstadoColegial().equalsIgnoreCase(
									String.valueOf(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE))) {
						throw new SIGAException("messages.censo.comunitarioEstadoColegial.error");
					}
				}
			}

			// c. comprobando si tiene cosas pendientes de SJCS (Guardias y Designas)
			pendienteSJCS = admCliente.tieneTrabajosSJCSPendientes(new Long(idpersona), new Integer(idinstitucion),null,null);
			if (pendienteSJCS == 3)
				throw new SIGAException(admCliente.getError());

			// anyadiendo la hora a la fecha del estado para permitir varios estados en el mismo dia
			fechaEstado = UtilidadesString.formatoFecha(fechaEstado + " " + UtilidadesBDAdm.getHoraBD(),
					"dd/MM/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss");

			// preparando datos para insertar el estado colegial en el sistema
			Hashtable hashEstado = miForm.getDatos();
			hashEstado.put(CenDatosColegialesEstadoBean.C_IDINSTITUCION, idinstitucion);
			hashEstado.put(CenDatosColegialesEstadoBean.C_IDPERSONA, idpersona);
			hashEstado.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, fechaEstado);
			hashEstado.put(CenDatosColegialesEstadoBean.C_OBSERVACIONES, hashEstado.get(CenDatosColegialesEstadoBean.C_OBSERVACIONES) + " " + motivo);

			boolean bDesdeCGAE = false;
			if (this.getIDInstitucion(request) == 2000){
				bDesdeCGAE = true;
			}
			
			// iniciando transaccion
			tx = usr.getTransactionPesada();
			tx.begin();

			// Si devuelve 2 es porque se ha relizado todo correctamente excepto la llamada al servicio web de revision de letrado.
			int isInsercionCorrecta = admEstados.insertaEstadoColegial(hashEstado, bDesdeCGAE, idioma);
			
			switch (isInsercionCorrecta) {
				case 0:
					
					throw new SIGAException(admEstados.getError());
				case 1:case 2:
					
				
					// terminando transaccion
					tx.commit();
					String[] parametros = {"","","",""};
					// informando de fin correcto y de cosas SJCS pendientes
					
					parametros[0] = "success";
					parametros[1] = UtilidadesString.getMensajeIdioma(usr, "messages.updated.success");
					if (pendienteSJCS == 1 || pendienteSJCS == 2){
						parametros[2] = UtilidadesString.getMensajeIdioma(usr,"messages.censo.estadosColegiales.avisoTareasPendientes");
						parametros[0] = "notice";
						
					}
					if(isInsercionCorrecta==2){
						parametros[3] =  UtilidadesString.getMensajeIdioma(usr,"messages.bajacolegial.errorNotificacionAca");
						parametros[0] = "error";
					}else if(hashEstado.get("RESPUESTA_ACA")!=null){
						parametros[3] =  UtilidadesString.getMensajeIdioma(usr,(String)hashEstado.get("RESPUESTA_ACA"));
						parametros[0] = "notice";
						
					}
					request.setAttribute("parametrosArray", parametros);
					request.setAttribute("modal", "");
					result="exitoParametros";
					break;
			}		
			
		} catch (Exception e) {
			result = "error";
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
		}

		return (result);
	} // insertar()

	/** 
	 * Recibe orden de guardar los cambios realizados en los datos colegiales
	 * (recordar que no incluyen el historico de estados colegiales)
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String - Destino del action  
	 * @exception  SIGAException - En cualquier caso de error
	 */
	protected String modificarDatos (ActionMapping mapping, 
									 MasterForm formulario, 
									 HttpServletRequest request, 
									 HttpServletResponse response) 
			throws SIGAException
	{
		String result = "error";
		UserTransaction tx = null;
		try {
			//obteniendo datos generales y usuario
			Hashtable hashOriginal = new Hashtable();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
            String pestanasituacion = request.getParameter("pestanaSituacion");
            
			//creando manejadores para acceder a las BBDD
			CenColegiadoBean original;
			CenColegiadoAdm admin=new CenColegiadoAdm(this.getUserBean(request));
			CenHistoricoAdm adminHist=new CenHistoricoAdm(this.getUserBean(request));
 			
			//obteniendo los datos del formulario
			DatosColegialesForm miForm = (DatosColegialesForm)formulario;
			
			//cargando la tabla hash con los valores del formulario 
			//para insertar en BD
			Hashtable hash = miForm.getDatos();
			hash.put(CenColegiadoBean.C_IDPERSONA, miForm.getIdPersona());
			hash.put(CenColegiadoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			
			//cargando una nueva tabla hash para insertar en la tabla de historico
			Hashtable hashHist = new Hashtable();
			hashHist.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());
			//BEGIN BNS FILA 300
			if (this.getIDInstitucion(request) == 2000)
				hashHist.put(CenHistoricoBean.C_IDINSTITUCION, "2000");
			else
			//END BNS FILA 300
				hashHist.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			hashHist.put(CenHistoricoBean.C_MOTIVO, miForm.getMotivo());
			hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer
					(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES).toString());
			
			//arreglando formatos
			hash=admin.prepararFormatosFechas(hash);
			hash=admin.controlFormatosCheck(hash);
			
			//cargando una hastable con los valores originales del registro 
			//sobre el que se realizara la modificacion						
			original=(CenColegiadoBean)request.getSession().getAttribute("DATABACKUP");
			
			//preparando la hash para modificar
			hashOriginal=admin.prepararHashtable(original);
			
			//guardando la situacion previa de COMUNITARIO
			String comunitarioAnterior=(String)hashOriginal.get(CenColegiadoBean.C_COMUNITARIO);
			
			//iniciando control de transacciones
			tx = usr.getTransactionPesada();
			tx.begin();	
			
			//obteniendo nuevo IDHISTORICO para auditoria			
			hashHist.put(CenHistoricoBean.C_IDHISTORICO,adminHist.getNuevoID(hashHist).toString());
			
			
			//lanzando el proceso de revision de suscripciones del colegiado
			String resultado[] = EjecucionPLs.
					ejecutarPL_RevisionSuscripcionesLetrado
							(miForm.getIdInstitucion(), 
							 miForm.getIdPersona(), 
							 miForm.getFechaEstado(), 
							 ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL " +
						"PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO"+resultado[1]);
			
			//Bloque para insertar en la cola de modificacion de datos 
			//de letrado solo si hay cambio en el valor del check residente
			{
				//obteniendo la residencia de antes y ahora
				String residenteAntes = UtilidadesHash.getString
						(hashOriginal, CenColegiadoBean.C_SITUACIONRESIDENTE);
				if (residenteAntes == null) residenteAntes = new String("");
				
				String residenteAhora = UtilidadesHash.getString
						(hash, CenColegiadoBean.C_SITUACIONRESIDENTE);
				if (residenteAhora == null) residenteAhora = new String("");
				
				//si no ha cambiado la residencia no haremos nada
				if (! residenteAhora.equals(residenteAntes)) 
				{
					//obteniendo las direcciones del colegiado
					Hashtable h = new Hashtable();
					UtilidadesHash.set (h,CenDireccionTipoDireccionBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					UtilidadesHash.set (h,CenDireccionTipoDireccionBean.C_IDPERSONA,miForm.getIdPersona());
					UtilidadesHash.set (h,CenDireccionTipoDireccionBean.C_IDTIPODIRECCION,new Integer(ClsConstants.TIPO_DIRECCION_CENSOWEB));
					CenDireccionTipoDireccionAdm direccionAdm =	new CenDireccionTipoDireccionAdm(this.getUserBean(request));
					
					Vector vDir = direccionAdm.select(h);
					if (vDir != null) 
					{
						//obteniendo control para cola de cambios
						CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm(this.getUserBean(request));
						int tipoModificacion;
						
						//para cada direccion, se ordena una actualizacion 
						//para Consejos:
						// a) si se para a Residente, se actualiza o inserta
						// b) si se pasa a No residente, hay que borrar
						for (int ii = 0; ii < vDir.size(); ii++) 
						{
							if (residenteAntes.equals("0") && residenteAhora.equals("1"))
								tipoModificacion = ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION;
							else
								tipoModificacion = ClsConstants.COLA_CAMBIO_LETRADO_BORRADO_DIRECCION;
							
							CenDireccionTipoDireccionBean b=(CenDireccionTipoDireccionBean)vDir.get(ii);
							if (!colaAdm.insertarCambioEnCola(tipoModificacion, 
									b.getIdInstitucion(), b.getIdPersona(), b.getIdDireccion()))
								throw new SIGAException (colaAdm.getError());
						} //for
					} //if (vDir != null)
					//06/03/2017 - R1603_0029: Si se pasa de residente a no residente y viceversa llamamos al servicio de ACA para que se tenga constancia de ello.
					if(residenteAhora.equals("0")){
						String llamadaReport= null;
						try {
							CenDatosColegialesEstadoAdm cenDatosColegialesEstadoAdm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
							llamadaReport = cenDatosColegialesEstadoAdm.llamadaWebServiceAcaRevisionLetrado(Long.valueOf(miForm.getIdPersona()), Short.valueOf(miForm.getIdInstitucion()));	
						} catch (BusinessException e) {
							llamadaReport = e.getMessage();
							tx.rollback();
							return exitoRefresco(e.getMessage() + "\n Cambios NO realizados",request);
						}
						hash.put("RESPUESTA_ACA", llamadaReport);
						hashHist.put(CenHistoricoBean.C_OBSERVACIONES, llamadaReport);
					}
				} //if (! residenteAhora.equals(residenteAntes))
			} //bloque
			//ejecutando la modificacion
			if (! admin.modificacionConHistorico (hash, hashOriginal, hashHist,
					this.getLenguaje (request)))
				return (result);

			
			//confirmando los cambios en BD
			tx.commit();
			if(hash.get("RESPUESTA_ACA")!=null){
				 request.setAttribute("mensaje",hash.get("RESPUESTA_ACA"));
				 result = exitoRefresco((String)hash.get("RESPUESTA_ACA") + "\n Cambios realizados correctamente",request);
			}else{
				result = exitoRefresco("messages.updated.success",request);
			}
			
			//actualizando la pantalla en funcion del cambio en 
			//check Comunitario
			String comunitarioAhora = miForm.getComunitario();
			if (comunitarioAhora == null) comunitarioAhora = new String(ClsConstants.DB_FALSE);
			if (comunitarioAnterior.equals(ClsConstants.DB_TRUE) && 
				!comunitarioAhora.equals(ClsConstants.DB_TRUE))
			{
				request.setAttribute("idPersona", miForm.getIdPersona());
				request.setAttribute("idInstitucion", miForm.getIdInstitucion());
				request.setAttribute("pestanaSituacion", pestanasituacion);
				request.setAttribute("mensaje","messages.updated.success");
				return "exitoConEditarNColegiado"; 
			}
		} catch (SIGAException es) {
			es.setSubLiteral("");
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, tx);
		
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
		}
		return (result);
	} //modificarDatos ()
	
	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error"; 		
		UserTransaction tx = null;
				
		try {		
			Hashtable original;		

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenDatosColegialesEstadoAdm admin=new CenDatosColegialesEstadoAdm(this.getUserBean(request));
 			
			// Obtengo los datos del formulario
			DatosColegialesForm miForm = (DatosColegialesForm)formulario;

			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = miForm.getDatos();
			hash.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());			
			hash.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			hash.put(CenHistoricoBean.C_MOTIVO, miForm.getMotivo());
									
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion	
			if (request.getSession().getAttribute("DATABACKUP_EST")!=null){
				original=((Row)request.getSession().getAttribute("DATABACKUP_EST")).getRow();
		    }else{
					original=new Hashtable();
			}
			
			original.remove(CenEstadoColegialBean.C_DESCRIPCION);
			
			//Control de las fechas:
			// PDM: Recogemos todos los estados colegiales por los que ha pasado la persona
			// Si solamente tuviera un estado colegial y quisieramos modificar su fecha de estado,
			// no se permite introducir una fecha de estado anterior a la que estamos modificando.
			// Si hubiera mas de un estado colegial, la fecha de estado modificada no puede ser anterior
			// a la fecha del estado anterior al que estamos modificando.
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));	
			 Vector v = colegiadoAdm.getEstadosColegiales(new Long(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), this.getLenguaje(request));
			 String valorFechaOri="";
			 if (v!=null && v.size()>1){//solo se valida la fecha si hubiera mas de un estado colegial
			 	 valorFechaOri=((Row)v.get(1)).getString(CenDatosColegialesEstadoBean.C_FECHAESTADO);
			 
			 
				//String fechaEstadoOri = (String)original.get(CenDatosColegialesEstadoBean.C_FECHAESTADO);
				String fechaEstadoOri = valorFechaOri;
				String fechaEstadoNew = (String)hash.get(CenDatosColegialesEstadoBean.C_FECHAESTADO);
				
				java.text.SimpleDateFormat sdfNew = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);			
				Date dateNew = sdfNew.parse(fechaEstadoNew);
				
				java.text.SimpleDateFormat sdfOri = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				Date dateOri = sdfOri.parse(fechaEstadoOri);
				String fechaOriSinHoras = sdfNew.format(dateOri);
				Date dateOriSinHoras = sdfNew.parse(fechaOriSinHoras);
	
				if (dateNew.before(dateOriSinHoras)||dateNew.equals(dateOriSinHoras)) 
					return exito("messages.general.error.fechaPosteriorActual1",request);
				
				
			 
			}	
			
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada();
			tx.begin();	

			
			boolean bDesdeGGAE = (this.getIDInstitucion(request) == 2000);
			if (admin.modificacionConHistorico(hash,original, bDesdeGGAE)){

			    // Lanzamos el proceso de revision de ANTICIPOS 
				String resultado1[] = EjecucionPLs.ejecutarPL_RevisionAnticiposLetrado(miForm.getIdInstitucion(),
																						  miForm.getIdPersona(),
																						  ""+this.getUserName(request));
				if ((resultado1 == null) || (!resultado1[0].equals("0")))
					throw new ClsExceptions ("Error al ejecutar el PL PROC_SIGA_ACT_ANTICIPOSCLIENTE ");
				
				
				
				// Lanzamos el proceso de revision de suscripciones del letrado 
				String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(miForm.getIdInstitucion(),
																						  miForm.getIdPersona(),
																						  miForm.getFechaEstado(),
																						  ""+this.getUserName(request));
				if ((resultado == null) || (!resultado[0].equals("0")))
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO"+resultado[1]);

				tx.commit();
				result=exitoModal("messages.updated.success",request);
			}	
			else{
				throw new SIGAException (admin.getError());
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}
		return (result);
	}
	
	/**
	 * Borra un estado colegial del sistema
	 * 
	 * Ademas, realiza las siguientes comprobaciones:
	 *  a. que el nuevo estado sea posterior al existente anterior
	 *  b. cambio de estado de un colegiado comunitario
	 *  c. si tiene cosas pendientes de SJCS (Guardias y Designas)
	 * 
	 * @param mapping - Mapeo de los struts
	 * @param formulario - Action Form asociado a este Action
	 * @param request - objeto llamada HTTP
	 * @param response - objeto respuesta HTTP
	 * @return String - Destino del action
	 * @exception SIGAException - En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		
		UsrBean usr = this.getUserBean(request);
		CenDatosColegialesEstadoAdm admEstados=new CenDatosColegialesEstadoAdm(usr);			
		
		
		String result;

		try {
			// obteniendo datos del formulario			
			DatosColegialesForm miForm = (DatosColegialesForm)formulario;
			Vector camposOcultos = miForm.getDatosTablaOcultos(0);
						
			String message=admEstados.eliminarEstadoColegiado(miForm.getIdInstitucion(),miForm.getIdPersona(),(String) camposOcultos.get(2));
			//Volvemos a restaurar direcciones.

			CenDireccionesBean beanDir = new CenDireccionesBean ();
			
			beanDir.setIdPersona (Long.valueOf(miForm.getIdPersona()));
			beanDir.setIdInstitucion (Integer.valueOf(miForm.getIdInstitucion()));
			
			//Se inserta en la cola de modificacion de datos para Consejos
			admEstados.insertarModificacionConsejo(beanDir,usr, ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION);
		
			
			
			if(message.contains("error"))
				result=exito(message,request);
			else
				result=exitoRefresco(message,request);

		} catch (Exception e) {
			result = "error";
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}

		return result;
	} // borrar()
	
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";
		
		try {
			// Obtengo idPersona e idInstitucion
			String idPersona = ((Long)request.getSession().getAttribute("IDPERSONA")).toString();		
			String idInstitucion = (String) request.getSession().getAttribute("IDINSTITUCIONPERSONA");
			
			// Manejadores para el formulario y el acceso a las BBDDs
			HistoricoForm form = (HistoricoForm) formulario;
			CenHistoricoAdm admin=new CenHistoricoAdm(this.getUserBean(request));

			Vector vect=new Vector();

			// Obtengo las entradas del historico para la busqueda indicada en el formulario
			vect = admin.getHistorico(idPersona,idInstitucion,form.getCmbCambioHistorico(),form.getFechaInicio(),form.getFechaFin(),form.getMotivo());

			// Paso la busqueda como parametro en el request 
			request.setAttribute("container", vect);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);
	}
	
	protected void getAjaxInscripciones (HttpServletRequest request, HttpServletResponse response) 
										throws SIGAException ,Exception{
		
		try {
			
			JSONObject json = new JSONObject();	
			Integer inscripciones=0;	
			
			String idPersona = request.getParameter("idPersona");	
			String idInstitucion = request.getParameter("idInstitucion");	
			ScsInscripcionTurnoAdm insTurnoAdm= new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionGuardiaAdm insGuardiaAdm= new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			
			Vector<ScsInscripcionTurnoBean> listaInscripcionesTurno = insTurnoAdm.getInscripcionesTurnosSinBajaPersona(idInstitucion, idPersona);
			
			if(listaInscripcionesTurno!=null)
				inscripciones=listaInscripcionesTurno.size();

			json.put("inscripciones", inscripciones);
			response.setContentType("text/x-json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 		
			
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
	}

	
}
