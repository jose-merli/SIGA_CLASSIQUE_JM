
package com.siga.censo.action;


import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;

import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.InscripcionTurno;
import com.siga.censo.form.HistoricoForm;
import com.siga.censo.form.DatosColegialesForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
				} else {
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
			request.setAttribute("ESTADOCOLEGIAL",admCli.getEstadoColegial(idPersona.toString(), idInstitucion));
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
		String result;

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

			// generando datos de historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(motivo);
			int estado = new Integer(hashEstado.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString()).intValue();
			switch (estado) {
			case ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_COLEGIAL));
				break;
			case ClsConstants.ESTADO_COLEGIAL_EJERCIENTE:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_EJERCICIO));
				break;
			case ClsConstants.ESTADO_COLEGIAL_SINEJERCER:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_EJERCICIO));
				break;
			case ClsConstants.ESTADO_COLEGIAL_INHABILITACION:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_INHABILITACION));
				break;
			case ClsConstants.ESTADO_COLEGIAL_SUSPENSION:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_SUSPENSION));
				break;
			}

			// iniciando transaccion
			tx = usr.getTransactionPesada();
			tx.begin();

			// 1 y 2. guardando estado e historico
			if (admEstados.insercionConHistorico(hashEstado, beanHis, idioma)) {
				revisionesPorCambioEstadoColegial(idinstitucion, idpersona, Integer.toString(estado), miForm
						.getFechaEstado(), usr); // OJO: se pasa la fecha sin hora

				// terminando transaccion
				tx.commit();

				// informando de fin correcto y de cosas SJCS pendientes
				String message = (UtilidadesString.getMensajeIdioma(usr, "messages.updated.success"));
				if (pendienteSJCS == 1 || pendienteSJCS == 2)
					message += "\r\n"
							+ UtilidadesString.getMensajeIdioma(usr,
									"messages.censo.estadosColegiales.avisoTareasPendientes");
				result = exitoModal(message, request);
			} else {
				throw new SIGAException(admEstados.getError());
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
			hashHist.put(CenHistoricoBean.C_IDHISTORICO,adminHist.getNuevoID(hash).toString());
			
			//ejecutando la modificacion
			if (! admin.modificacionConHistorico (hash, hashOriginal, hashHist,
					this.getLenguaje (request)))
				return (result);

			
			//lanzando el proceso de revision de suscripciones del colegiado
			String resultado[] = EjecucionPLs.
					ejecutarPL_RevisionSuscripcionesLetrado
							(miForm.getIdInstitucion(), 
							 miForm.getIdPersona(), 
							 miForm.getFechaEstado(), 
							 ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL " +
						"PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			
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
					UtilidadesHash.set (h,CenDireccionTipoDireccionBean.C_IDTIPODIRECCION,new Integer(ClsConstants.TIPO_DIRECCION_CORREO));
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
				} //if (! residenteAhora.equals(residenteAntes))
			} //bloque
			
			//confirmando los cambios en BD
			tx.commit();
			
			
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
			
			result = exitoRefresco("messages.updated.success",request);
		}
		catch (Exception e) { 
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
			CenHistoricoAdm adminHist=new CenHistoricoAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			DatosColegialesForm miForm = (DatosColegialesForm)formulario;

			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = miForm.getDatos();
			hash.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());			
			hash.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());										
									
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion	
			if (request.getSession().getAttribute("DATABACKUP_EST")!=null){
				original=((Row)request.getSession().getAttribute("DATABACKUP_EST")).getRow();
		    }else{
					original=new Hashtable();
			}
			
			original.remove(CenEstadoColegialBean.C_DESCRIPCION);
			
			// Cargo una nueva tabla hash para insertar en la tabla de historico
			Hashtable hashHist = new Hashtable();			
			// Cargo los valores obtenidos del formulario referentes al historico			
			hashHist.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());			
			hashHist.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());			
			hashHist.put(CenHistoricoBean.C_MOTIVO, miForm.getMotivo());
			// Especifico el tipo de cambio del historico	
			if(hash.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString().equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL))){				
				hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_COLEGIAL).toString());
			}
			if(hash.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString().equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE))){				
				hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_EJERCICIO).toString());		
			}	
			if(hash.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString().equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_COLEGIAL_SINEJERCER))){				
				hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_EJERCICIO).toString());		
			}
			if(hash.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString().equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_COLEGIAL_INHABILITACION))){				
				hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_INHABILITACION).toString());		
			}
			if(hash.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString().equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_COLEGIAL_SUSPENSION))){				
				hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_SUSPENSION).toString());		
			}
						
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

			// Asigno el IDHISTORICO			
			hashHist.put(CenHistoricoBean.C_IDHISTORICO, adminHist.getNuevoID(hash).toString());			
			
			if (admin.modificacionConHistorico(hash,original,hashHist, this.getLenguaje(request))){

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
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");

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
		// Controles generales
		UsrBean usr = this.getUserBean(request);
		String idioma = usr.getLanguage();
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		CenClienteAdm admCliente = new CenClienteAdm(usr);
		CenColegiadoAdm admColegiado = new CenColegiadoAdm(usr);
		CenDatosColegialesEstadoAdm admEstados = new CenDatosColegialesEstadoAdm(usr);
		CenHistoricoAdm admHistorico = new CenHistoricoAdm(usr);
		UserTransaction tx = null;
		String result;

		// Variables
		Vector camposOcultos;
		String idinstitucion;
		String idpersona;

		int pendienteSJCS;

		try {
			// obteniendo datos del formulario
			camposOcultos = miForm.getDatosTablaOcultos(0);
			idinstitucion = miForm.getIdInstitucion();
			idpersona = miForm.getIdPersona();

			// a. comprobando si tiene cosas pendientes de SJCS (Guardias y Designas)
			pendienteSJCS = admCliente.tieneTrabajosSJCSPendientes(new Long(idpersona), new Integer(idinstitucion),null,null);
			if (pendienteSJCS == 3)
				throw new SIGAException(admCliente.getError());

			// obteniendo datos del estado
			Hashtable hash = new Hashtable();
			hash.put(CenDatosColegialesEstadoBean.C_IDPERSONA, idpersona);
			hash.put(CenDatosColegialesEstadoBean.C_IDINSTITUCION, idinstitucion);
			hash.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, (String) camposOcultos.get(2));

			// generando datos para el historico
			Hashtable hashHist = new Hashtable();
			hashHist.put(CenHistoricoBean.C_IDPERSONA, idpersona);
			hashHist.put(CenHistoricoBean.C_IDINSTITUCION, idinstitucion);
			hashHist.put(CenHistoricoBean.C_MOTIVO, ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
			hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(
					ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES).toString());
			hashHist.put(CenHistoricoBean.C_IDHISTORICO, admHistorico.getNuevoID(hash).toString());
			
			// iniciando transaccion
			tx = usr.getTransaction();
			tx.begin();

			// 1 y 2. borrando estado e insertando historico
			if (admEstados.borrarConHistorico(hash, hashHist, idioma)) {
				// obteniendo ultimo estado colegial
				Vector<Row> vEstados = admColegiado.getEstadosColegiales(new Long(idpersona), new Integer(idinstitucion), idioma);
				if (vEstados != null && vEstados.size() > 0) {
					Row ultimoEstado = vEstados.get(0);
					String estado = ultimoEstado.getString(CenDatosColegialesEstadoBean.C_IDESTADO);
					String fechaEstado = ultimoEstado.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO);
					
					// ejecutando revisiones
					revisionesPorCambioEstadoColegial(idinstitucion, idpersona, estado, fechaEstado, usr);
				}

				// terminando transaccion
				tx.commit();

				// informando de fin correcto y de cosas SJCS pendientes
				String message = (UtilidadesString.getMensajeIdioma(usr, "messages.deleted.success"));
				if (pendienteSJCS == 1 || pendienteSJCS == 2)
					message += "\r\n"
							+ UtilidadesString.getMensajeIdioma(usr,
									"messages.censo.estadosColegiales.avisoTareasPendientes");
				result = exitoRefresco(message, request);
			} else {
				throw new SIGAException(admEstados.getError());
			}
		} catch (Exception e) {
			result = "error";
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, tx);
		}

		return result;
	} // borrar()
	
	/**
	 *  Realiza revisiones sobre el colegiado a partir del estado pasado como parametro:
	 *  1. Revisar anticipos
	 *  2. Revisar suscripciones a servicios
	 *  3. Dar de baja en las colas de guardia y turno si el nuevo estado no es ejerciente
	 *  4. Revocar certificados si el nuevo estado es de baja
	 *  
	 * @param idinstitucion
	 * @param idpersona
	 * @param estado
	 * @param fechaEstado
	 * @param usr
	 * @throws ClsExceptions
	 * @throws ParseException
	 * @throws SIGAException
	 */
	protected void revisionesPorCambioEstadoColegial(String idinstitucion,
			String idpersona,
			String estado,
			String fechaEstado,
			UsrBean usr) throws ClsExceptions, ParseException, SIGAException
	{
		// Controles generales
		CenPersonaAdm admPersona = new CenPersonaAdm(usr);
		AdmCertificadosAdm admCertif = new AdmCertificadosAdm(usr);
		String usuario = usr.getUserName();

		// 1. revisando anticipos
		String resultado1[] = EjecucionPLs.ejecutarPL_RevisionAnticiposLetrado(idinstitucion, idpersona, usuario);
		if ((resultado1 == null) || (!resultado1[0].equals("0")))
			throw new ClsExceptions("Error al ejecutar el PL PROC_SIGA_ACT_ANTICIPOSCLIENTE ");

		// 2. revisando suscripciones a servicios
		try {
			fechaEstado=UtilidadesString.formatoFecha(fechaEstado, ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_ENGLISH);
		} catch (Exception e1) {
			// La fecha esta bien formada como dia/mes/ano
		}
		String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(idinstitucion, idpersona,
				fechaEstado, usuario);
		if ((resultado == null) || (!resultado[0].equals("0")))
			throw new ClsExceptions("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");

		// 3. Dar de baja en las colas de guardia y turno si el nuevo estado es de baja
		if (new Integer(estado).intValue() != ClsConstants.ESTADO_COLEGIAL_EJERCIENTE) {
			// TODO: mover a recurso
			String observacionesBaja = "Baja por cambio de estado colegial";

			Hashtable<String, Object> turnoHash = new Hashtable<String, Object>();
			UtilidadesHash.set(turnoHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idinstitucion);
			UtilidadesHash.set(turnoHash, ScsInscripcionTurnoBean.C_IDPERSONA, idpersona);
			UtilidadesHash.set(turnoHash, ScsInscripcionTurnoBean.C_FECHABAJA, "");
			// TODO: tambien hay que dar de baja si la fecha es posterior a la nueva fecha. Supongo que habra que
			// utilizar una busqueda especial
			Vector<ScsInscripcionTurnoBean> vTurnos = new ScsInscripcionTurnoAdm(usr).select(turnoHash);

			InscripcionTurno inscripcionTurno;
			for (int x = 0; x < vTurnos.size(); x++) {
				ScsInscripcionTurnoBean c = (ScsInscripcionTurnoBean) vTurnos.get(x);
				inscripcionTurno = InscripcionTurno.getInscripcionTurno(c.getIdInstitucion(), c.getIdTurno(), c
						.getIdPersona(), c.getFechaSolicitud(), usr, false);
				// jbd // inc7718 //Esta fecha tiene que ser larga
				try {
					fechaEstado=UtilidadesString.formatoFecha(fechaEstado, ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA);
				} catch (Exception e) {
					// Si ha fallado será porque el formato ya es el adecuado DATE_FORMAT_JAVA  
				}
				inscripcionTurno.solicitarBaja(fechaEstado, observacionesBaja,fechaEstado,observacionesBaja,c.getFechaValidacion(),"N",null, usr);
				//inscripcionTurno.validarBaja(fechaEstado, observacionesBaja, usr);
			}
		}

		// 4. revocando certificados
		if (new Integer(estado).intValue() > ClsConstants.ESTADO_COLEGIAL_EJERCIENTE) {
			String nif = admPersona.obtenerNIF(idpersona);
			admCertif.revocarCertificados(new Integer(idinstitucion), nif);
		}
	} // revisionesPorCambioEstadoColegial()
	
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
			vect = admin.getHistorico(idPersona,idInstitucion,form.getCmbCambioHistorico(),form.getFechaInicio(),form.getFechaFin());

			// Paso la busqueda como parametro en el request 
			request.setAttribute("container", vect);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);
	}

}
