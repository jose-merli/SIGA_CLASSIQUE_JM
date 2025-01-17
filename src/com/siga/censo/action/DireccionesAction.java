
package com.siga.censo.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenSoliModiDireccionesAdm;
import com.siga.beans.CenSoliModiDireccionesBean;
import com.siga.beans.CenTipoDireccionAdm;
import com.siga.beans.CenTipoDireccionBean;
import com.siga.censo.form.DireccionesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * @author nuria.rgonzalez
 * @since 23-11-2004
 * @version adrian.ayala - 03-06-2008 - revision de insertar(), modificar() y 
 *   borrar() para asegurar el correcto funcionamiento de 
 *   la insercion en cola para la copia de direcciones de colegiados a Consejos
 */
public class DireccionesAction extends MasterAction
{
	/** 
	 * Funcion que atiende a las peticiones. Segun el valor del 
	 *   parametro modo del formulario ejecuta distintas acciones
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, 
											 ActionForm formulario, 
											 HttpServletRequest request, 
											 HttpServletResponse response)
			throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null)
				return mapping.findForward(mapDestino);
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);						
			} else if (accion.equalsIgnoreCase("solicitarModificacion")) {
				mapDestino = solicitarModificacion(mapping, miForm, request, response);
			} else if(accion.equalsIgnoreCase("insertarModificacion")) {
				mapDestino = insertarModificacion(mapping, miForm, request, response);
			} else if(accion.equalsIgnoreCase("guardarInsertarHistorico")) {
				mapDestino = borrarInsertar(mapping, miForm, request, response);	
			} else if (accion.equalsIgnoreCase("modificarDireccion")) {
				mapDestino = modificarDireccion(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("duplicar")) {
				mapDestino = duplicarDireccion(mapping, miForm, request, response);
			}
			else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			        request.setAttribute("exceptionTarget", "parent.modal");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("Error en la Aplicaci�n",e);
		}
	} //executeInternal()

	/** 
	 * Funcion que atiende la accion abrir. 
	 *   Por defecto se abre el forward 'inicio'
	 * 
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
							HttpServletResponse response)
			throws SIGAException
	{
		try
		{
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			String accion = (String)request.getParameter("accion");
			
			// Vemos si venimos de nueva sociedad o nuevo no colegiado de tipo personal:
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
				 (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals("") )) {
				request.setAttribute("modoVolver",accion);
				return "clienteNoExiste";
			}
			
			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion"));
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(), idPersona.longValue());
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			
			Vector v = null;
			String nombre = null;
			String numero = "";
			String estadoColegial="";
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			numero = colegiadoAdm.getIdentificadorColegiado(bean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucionPersona));
			
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(((DireccionesForm)formulario).getIncluirRegistrosConBajaLogica());
			request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
			
			v = clienteAdm.getDirecciones(idPersona,idInstitucionPersona, bIncluirRegistrosConBajaLogica);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("accion", accion);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("estadoColegial", estadoColegial);
			request.setAttribute("numero", numero);
			request.setAttribute("vDatos", v);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);			
		}
		return "inicio";
	} //abrir()

	protected String editarYver(MasterForm formulario, HttpServletRequest request, String modo) throws SIGAException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);
		DireccionesForm form = (DireccionesForm) formulario;

		try {
			// obteniendo identificacion de la direccion desde el formulario
			Vector ocultos = form.getDatosTablaOcultos(0);
			Long idPersona = form.getIDPersona();
			Long idDireccion = Long.valueOf((String) ocultos.elementAt(0));
			Integer idInstitucionPersona = form.getIDInstitucion();

			// Controles generales
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(this.getUserName(request), user, idInstitucionPersona, idPersona);
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm(user);
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request), user, idInstitucionPersona, idPersona);
			Hashtable hDatosDireccion = clienteAdm.getDirecciones(idPersona, idInstitucionPersona, idDireccion, modo.equalsIgnoreCase("ver"));

			// obteniendo todos los tipos existentes en SIGA
			Vector vTipos = cenTipoDirAdm.select("");
			Hashtable hTipoDir = new Hashtable();
			if (vTipos != null && vTipos.size() > 0) {
				for (int i = 0; i < vTipos.size(); i++) {
					CenTipoDireccionBean tipoDir = (CenTipoDireccionBean) vTipos.get(i);
					hTipoDir.put(tipoDir.getIdTipoDireccion(), "N");
				}
			}
			request.setAttribute("vTipos", vTipos);

			// obteniendo los tipos de esta direccion
			Vector vTiposDireccion = new Vector();
			Hashtable hTipoDirSel = new Hashtable();
			// compruebo al visibilidad con CenClienteAdm que ha utilizado su constructor de visibilidad
			if (clienteAdm.compruebaVisibilidadCampo(CenDireccionTipoDireccionBean.T_NOMBRETABLA, CenDireccionTipoDireccionBean.C_IDTIPODIRECCION)) {
				Hashtable pkDireccion = new Hashtable();
				UtilidadesHash.set(pkDireccion, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucionPersona);
				UtilidadesHash.set(pkDireccion, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
				UtilidadesHash.set(pkDireccion, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
				vTiposDireccion = tipoDirAdm.select(pkDireccion);
				if (vTiposDireccion != null) {
					for (int i = 0; i < vTiposDireccion.size(); i++) {
						CenDireccionTipoDireccionBean tipoDirSel = (CenDireccionTipoDireccionBean) vTiposDireccion.get(i);
						hTipoDirSel.put(tipoDirSel.getIdTipoDireccion(), "S");
					}
				}
			}
			hDatosDireccion.put(CenTipoDireccionBean.C_IDTIPODIRECCION, vTiposDireccion);

			// pasando los tipos de esta direccion
			Enumeration clavesDir = hTipoDirSel.keys();
			while (clavesDir.hasMoreElements()) {
				Integer clave = (Integer) clavesDir.nextElement();
				if (hTipoDir.containsKey(clave))
					hTipoDir.put(clave, "S");
			}
			request.setAttribute("TipoDirecciones", hTipoDir);

			// pasando las preferencias de la direccion
			String fax = "false", mail = "false", correo = "false", sms = "false";
			String preferente = UtilidadesHash.getString(hDatosDireccion, CenDireccionesBean.C_PREFERENTE);
			if (preferente.indexOf("E") >= 0)
				mail = "true";
			if (preferente.indexOf("F") >= 0)
				fax = "true";
			if (preferente.indexOf("C") >= 0)
				correo = "true";
			if (preferente.indexOf("S") >= 0)
				sms = "true";
			request.setAttribute("preferenteFax", fax);
			request.setAttribute("preferenteCorreo", correo);
			request.setAttribute("preferenteMail", mail);
			request.setAttribute("preferenteSms", sms);

			// pasando el tipo de persona de no colegiado ("1": Persona fisica)
			CenNoColegiadoAdm noColAdm = new CenNoColegiadoAdm(user);
			String tipoCliente = noColAdm.getTipoPersonaNoColegiado(idPersona, idInstitucionPersona);
			request.setAttribute("tipoCliente", tipoCliente);

			// pasando los datos de la direccion y otros parametros
			request.getSession().setAttribute("DATABACKUP", hDatosDireccion);
			request.setAttribute("accion", request.getParameter("accion"));
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		return modo;
	} // editarYver()
	
	/** 
	 * Funcion que atiende la accion editar
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar (ActionMapping mapping, 
							 MasterForm formulario, 
							 HttpServletRequest request, 
							 HttpServletResponse response)
			throws SIGAException
	{
		return editarYver(formulario, request, "editar");
	} //editar()
	
	/** 
	 * Funcion que atiende la accion ver
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver (ActionMapping mapping, 
						  MasterForm formulario, 
						  HttpServletRequest request, 
						  HttpServletResponse response)
			throws SIGAException
	{
		return editarYver(formulario, request, "ver");
	} //ver()
	
	/**
	 * Saca la ventana de una nueva direccion con los datos de otra direccion cargados
	 * 
	 * @param mapping
	 * @param miForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);			
		DireccionesForm miForm = (DireccionesForm) formulario;
		String modo = "nuevo";
 		
		try
		{
			// obteniendo los datos del formulario
			Long idPersona = miForm.getIDPersona();
			Integer idInstitucionPersona = miForm.getIDInstitucion();
			
			// pasando todos los tipos existentes en SIGA
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (user);
			Vector vTipos = cenTipoDirAdm.select("");
			request.setAttribute("vTipos", vTipos);
			
			// pasando el tipo de persona de no colegiado ("1": Persona fisica)
			CenNoColegiadoAdm noColAdm = new CenNoColegiadoAdm(user);
			String tipoCliente = noColAdm.getTipoPersonaNoColegiado(idPersona, idInstitucionPersona);
			request.setAttribute("tipoCliente", tipoCliente);
			
			// pasando los parametros generales de la direccion
			request.setAttribute("modoConsulta", modo);			
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, null);
		}
		return modo;
	} //nuevo()
	
	/** 
	 * Funcion que atiende la accion nuevo
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String duplicarDireccion (ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response)
			throws SIGAException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);			
		DireccionesForm miForm = (DireccionesForm) formulario;
		String modo = "duplicar";
 		
		try
		{
			// obteniendo los datos del formulario
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Long idPersona = miForm.getIDPersona();
			Long idDireccion = Long.valueOf((String) ocultos.elementAt(0));
			Integer idInstitucionPersona = miForm.getIDInstitucion();
			
			// pasando todos los tipos existentes en SIGA
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (user);
			Vector vTipos = cenTipoDirAdm.select("");
			request.setAttribute("vTipos", vTipos);
			
			// pasando el tipo de persona de no colegiado ("1": Persona fisica)
			CenNoColegiadoAdm noColAdm = new CenNoColegiadoAdm(user);
			String tipoCliente = noColAdm.getTipoPersonaNoColegiado(idPersona, idInstitucionPersona);
			request.setAttribute("tipoCliente", tipoCliente);

			// pasando los datos de la direccion y otros parametros
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request), user, idInstitucionPersona, idPersona);
			Hashtable hDatosDireccion = clienteAdm.getDirecciones(idPersona, idInstitucionPersona, idDireccion, modo.equalsIgnoreCase("ver"));
			request.getSession().setAttribute("DATABACKUP", hDatosDireccion);
			
			// pasando los parametros generales de la direccion
			request.setAttribute("modoConsulta", modo);			
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, null);
		}
		return modo;
	} //duplicarDireccion()
	
	////////// INI - Metodos de apoyo a editar, ver, nuevo y duplicar //////////
	
	////////// FIN - Metodos de apoyo a editar, ver, nuevo y duplicar //////////
	
	/** 
	 * Funcion que anyade una direccion de cliente
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar (ActionMapping mapping, 
							   MasterForm formulario, 
							   HttpServletRequest request, 
							   HttpServletResponse response)
			throws SIGAException
	{
		//Variables generales
		String rc = "";
		UserTransaction t = null; 
		
		try
		{
			
			//obteniendo datos del formulario
			DireccionesForm miForm = (DireccionesForm) formulario;
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			UsrBean usr = this.getUserBean (request);
		
			t = usr.getTransactionPesada();
			t.begin ();
			
			//Estableciendo los datos sobre el tipo de direccion
			String tiposDir = "";
			if (request.getParameter ("idTipoDireccionNew") != null && 
					!request.getParameter ("idTipoDireccionNew").equals (""))
			{
				int posUltimaComa = request.getParameter ("idTipoDireccionNew").lastIndexOf (",");
				if (posUltimaComa > -1)
					tiposDir = (String) request.getParameter ("idTipoDireccionNew").substring (0, posUltimaComa);
			}
			
			//estableciendo los datos de la direccion (desde el formulario 
			// de la interfaz principalmente) para la posterior insercion
			poblarObjeto(miForm,beanDir);
				
			String motivo = "";
			if(miForm.getMotivo ()!=null){
				motivo = miForm.getMotivo ();	
			}			
			
			// Se llama a la interfaz Direccion para insertar una nueva direccion
			Direccion dirAux = Direccion.insertar(beanDir, tiposDir, motivo,Direccion.getListaDireccionesObligatorias(miForm.getTipoAcceso()), request, usr);

			//Si se necesita confirmaci�n por parte del usuario se realiza una peticion de pregunta
			if(dirAux.isConfirmacionPregunta()){
				request.setAttribute("modo", "insertar");
				t.rollback();
				return dirAux.getTipoPregunta();
			}
			
			/*
			 //Si existe alg�n fallo en la inserci�n se llama al metodo exito con el error correspondiente
			if(dirAux.isFallo()){
				t.rollback();
				return exito(dirAux.getMsgError(), request);
			}
			*/
			
			//confirmando las modificaciones de BD
			t.commit();
			
			if(miForm.getVieneDe()!=null && miForm.getVieneDe().equals("1")){
				miForm.setIdDireccion(beanDir.getIdDireccion());
				return enviarNuevaDireccion(mapping, miForm, request, response);
			}
			
			rc = exitoModal("messages.inserted.success", request);
			
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, t);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, t);
		}
		
		return rc; 
	} //insertar()
	
	
	/** 
	 * Funcion que modifica una direccion de cliente
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificarDireccion (ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response)
			throws SIGAException
	{
		//Variables generales
		String rc = "";
		UserTransaction t = null;

		try {
			
			//obteniendo datos del formulario
			DireccionesForm miForm = (DireccionesForm) formulario;
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			UsrBean usr = this.getUserBean (request);
			
			t = usr.getTransactionPesada();
			t.begin ();
			
			//esta es la parte que comprobra si tiene una direcci�n de tipo censoweb.
			String tiposDir = "";
			if (request.getParameter ("idTipoDireccionNew") != null && !request.getParameter ("idTipoDireccionNew").equals ("")){
				int posUltimaComa = request.getParameter ("idTipoDireccionNew").lastIndexOf (",");				
				if (posUltimaComa > -1)
					tiposDir = (String) request.getParameter ("idTipoDireccionNew").substring (0, posUltimaComa);				   
			}
			
			//estableciendo los datos de la direccion (desde el formulario 
			// de la interfaz principalmente) para la posterior insercion
			poblarObjeto(miForm,beanDir);
			beanDir.setIdDireccion (miForm.getIdDireccion ());
			beanDir.setOriginalHash ((Hashtable) request.getSession ().getAttribute ("DATABACKUP"));
			// Se llama a la interfaz Direccion para insertar una nueva direccion
			Direccion dirAux = Direccion.actualizar(beanDir, tiposDir, miForm.getMotivo (),Direccion.getListaDireccionesObligatorias(miForm.getTipoAcceso()), request, usr);

			//Si se necesita confirmaci�n por parte del usuario se realiza una peticion de pregunta
			if(dirAux.isConfirmacionPregunta()){
				request.setAttribute("modo", "modificarDireccion");
				t.rollback();
				return dirAux.getTipoPregunta();
			}
			
			//Si existe alg�n fallo en la inserci�n se llama al metodo exito con el error correspondiente
			/*if(dirAux.isFallo()){
				t.rollback();
				return exito(dirAux.getMsgError(), request);
			}*/
			
			//confirmando los cambios en BD
			t.commit();
			
			//saliendo
			rc = exitoModal("messages.updated.success", request);
			
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, t);		
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, t);
		}
		
		return rc; 
	} //modificar()

	/** 
	 * Funcion que borra una direccion de cliente
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar (ActionMapping mapping, 
							 MasterForm formulario, 
							 HttpServletRequest request, 
							 HttpServletResponse response)
			throws SIGAException {
		
		//Variables generales
		String rc = "";
		UserTransaction t = null;
		
		try {
		
			//obteniendo los datos del formulatio
			DireccionesForm miForm = (DireccionesForm) formulario;
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			Direccion direccion = new Direccion();
			UsrBean usr = this.getUserBean (request);
			
			t = usr.getTransactionPesada();
			t.begin ();
			
			beanDir.setIdPersona (miForm.getIDPersona ());
			beanDir.setIdInstitucion (miForm.getIDInstitucion ());
			
			// Se comprueba en el formulario porque se puede venir de sitios distintos
			if (miForm.getIdDireccion()!=null)
				beanDir.setIdDireccion(miForm.getIdDireccion());
			else
				beanDir.setIdDireccion(new Long ((String) miForm.getDatosTablaOcultos (0).get (0)));
			
			
			//Se llama a la interfaz Direccion para realizar el borrado
			direccion.borrar(beanDir,Direccion.getListaDireccionesObligatorias(miForm.getTipoAcceso()), request, usr);
			
			//confirmando los cambios en BD
			t.commit();
			
			//saliendo
			rc = exitoRefresco("messages.deleted.success", request);
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, t);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, t);
		}
		return rc;
	} //borrar()
	
	/** 
	 * Funcion que atiende la accion solicitarModificacion
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String solicitarModificacion (ActionMapping mapping, 
											MasterForm formulario, 
											HttpServletRequest request, 
											HttpServletResponse response)
			throws SIGAException
	{
		String modo = "";
		
		try
		{
			Vector ocultos = new Vector();
			DireccionesForm form = (DireccionesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = form.getIDPersona();
			Integer idInstitucionPersona = form.getIDInstitucion();
			String accion = (String)request.getParameter("accion");				
			Long idDireccion = Long.valueOf((String)ocultos.elementAt(0));
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			CenClienteAdm clienteAdm =  new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(), idPersona.longValue());
			Hashtable hash = clienteAdm.getDirecciones(idPersona,idInstitucionPersona,idDireccion);
			
			modo = "solicitarModificacion";
			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			
			//CR - Queremos saber si se trata de un Colegiadoo de un No Colegiado. Y si es No colegiado si es tipo Personal (1) o Sociedad (0).
			String tipoCliente = "";
			CenNoColegiadoAdm noColAdm = new CenNoColegiadoAdm(user);
			CenNoColegiadoBean noColBean = noColAdm.existeNoColegiadoInstitucion(idPersona, idInstitucionPersona);
			if(noColBean!=null){
				tipoCliente = noColBean.getTipo();
			}
			request.setAttribute("tipoCliente", tipoCliente);		
			
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, null);
		}
		return modo;
	} //solicitarModificacion()
	
	/**
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String insertarModificacion (ActionMapping mapping, 
										   MasterForm formulario, 
										   HttpServletRequest request, 
										   HttpServletResponse response)
			throws SIGAException {
		
		String modo = "";
		String modificarPreferencias = "";
		UserTransaction t = null;
		
		try
		{	
			t = this.getUserBean(request).getTransaction();
			
			DireccionesForm form = (DireccionesForm) formulario;		
			CenSoliModiDireccionesAdm adm = new CenSoliModiDireccionesAdm(this.getUserBean(request));
			CenDireccionesAdm admDir = new CenDireccionesAdm(this.getUserBean(request));
			modo = "insertarModificacion";

			Long idPersona = form.getIDPersona();
			Integer idInstitucionPersona = form.getIDInstitucion();
			String preferente = this.campoPreferenteBooleanToString(form.getPreferenteMail(), form.getPreferenteCorreo(), form.getPreferenteFax(), form.getPreferenteSms ());
			Long idDireccion = form.getIdDireccion();
			if (request.getParameter("modificarPreferencias")!=null){
				modificarPreferencias = request.getParameter("modificarPreferencias");
			}
			if (modificarPreferencias != null && !modificarPreferencias.equals("1")){
				if (!admDir.comprobarPreferenteDirecciones(idPersona.toString(),idInstitucionPersona.toString(),preferente,idDireccion,request)) {	
					request.setAttribute("modo", "insertarModificacion");
					return "preguntaCambioPreferenciaSolicitud";
				}
			}
			
			t.begin();	
			CenSoliModiDireccionesBean bean = getDatos(form, request);
			if(!adm.insert(bean)){
				throw new SIGAException (adm.getError());
			}
			
			t.commit();
			modo = exitoModal("messages.censo.solicitudes.exito",request);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return modo;
	} //insertarModificacion()

	/**
	 * @param form
	 * @param request
	 * @return
	 * @throws ClsExceptions
	 */
	protected CenSoliModiDireccionesBean getDatos (DireccionesForm form, 
												   HttpServletRequest request)
			throws SIGAException
	{
		CenSoliModiDireccionesBean bean = null;
		
		try
		{
			bean = new CenSoliModiDireccionesBean();
			CenSoliModiDireccionesAdm adm = new CenSoliModiDireccionesAdm(this.getUserBean(request));
			bean.setIdSolicitud(adm.getNuevoId());
			bean.setIdInstitucion(form.getIDInstitucion());
			bean.setIdPersona(form.getIDPersona());
			bean.setIdDireccion(form.getIdDireccion());
			bean.setMotivo(form.getMotivo());
			bean.setCodigoPostal(form.getCodigoPostal());
			bean.setDomicilio(form.getDomicilio());
			bean.setTelefono1(form.getTelefono1());
			bean.setTelefono2(form.getTelefono2());
			bean.setMovil(form.getMovil());
			bean.setFax1(form.getFax1());
			bean.setFax2(form.getFax2());
			bean.setCorreoElectronico(form.getCorreoElectronico());
			bean.setPaginaweb(form.getPaginaWeb());
			bean.setIdPais((String)request.getParameter("pais"));
			bean.setIdProvincia((String)request.getParameter("provincia"));
			bean.setIdPoblacion((String)request.getParameter("poblacion"));
			bean.setPoblacionExtranjera((String)request.getParameter("poblacionExt"));
			if (!bean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA) && !bean.getIdPais().equals("")) {
				bean.setIdProvincia("");
				bean.setIdPoblacion("");
			}
			bean.setPreferente(this.campoPreferenteBooleanToString(form.getPreferenteMail(), 
					form.getPreferenteCorreo(), form.getPreferenteFax(), form.getPreferenteSms ()));
			bean.setIdEstadoSolic(new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
			bean.setFechaAlta("sysdate");
			if(form.getOtraProvincia()!= null){
				bean.setOtraProvincia(Integer.valueOf(form.getOtraProvincia()));
			}
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return bean;
	} //getDatos()
	
	/**
	 * Fucnion que devuleve un string que correspode a los datos almacenados en B.D.
	 * 
	 * @param mail
	 * @param correo
	 * @param fax
	 * @param sms
	 * @return cadena
	 */
	private String campoPreferenteBooleanToString (Boolean mail, 
												   Boolean correo, 
												   Boolean fax,
												   Boolean sms)
			throws SIGAException
	{
		String valor = "";
		
		try
		{
			if (mail.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO;
			if (fax.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_FAX;
			if (correo.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREO;
			if (sms.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_SMS;
		}
		catch (Exception e) {
			throwExcp ("messages.general.error", new String[] {"modulo.censo"}, e, null);
		}
		return valor;
	} //campoPreferenteBooleanToString()
	
	
	
	/**
	 * Funcion que modifica una direccion o la borra para crear otra y mantener el historico
	 * @throws ClsExceptions 
	 */
	protected String modificar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions{
		DireccionesForm miForm = (DireccionesForm) formulario;
		return modificarDireccion(mapping, miForm, request, response);
	}
	
	/** 
	 * Funcion que borra una direccion de cliente
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrarInsertar (ActionMapping mapping, 
							 MasterForm formulario, 
							 HttpServletRequest request, 
							 HttpServletResponse response)
			throws SIGAException
	{
		//Variables generales
		String rc = "";
		UserTransaction t = null;
		Long idDireccionAntigua;
		
		try
		{
			//obteniendo los datos del formulatio
			DireccionesForm miForm = (DireccionesForm) formulario;
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			Direccion direccion = new Direccion();
			UsrBean usr = this.getUserBean (request);
			
			t = usr.getTransactionPesada();
			t.begin ();
			
			/*  BORRADO CON HISTORICO  */
			
			// Se comprueba en el formulario porque se puede venir de sitios distintos
			if (miForm.getIdDireccion()!=null)
				beanDir.setIdDireccion(miForm.getIdDireccion());
			else
				beanDir.setIdDireccion(new Long ((String) miForm.getDatosTablaOcultos (0).get (0)));
			idDireccionAntigua = beanDir.getIdDireccion();
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);



			
			/*  A�ADIR CON HISTORICO  */
			
			//Estableciendo los datos sobre el tipo de direccion
			String tiposDir = "";
			if (request.getParameter ("idTipoDireccionNew") != null && 
					!request.getParameter ("idTipoDireccionNew").equals (""))
			{
				int posUltimaComa = request.getParameter ("idTipoDireccionNew").lastIndexOf (",");
				if (posUltimaComa > -1)
					tiposDir = (String) request.getParameter ("idTipoDireccionNew").substring (0, posUltimaComa);
			}
			
		
			//estableciendo los datos de la direccion (desde el formulario 
			// de la interfaz principalmente) para la posterior insercion
			poblarObjeto(miForm,beanDir);
			
			String motivo = "";
			if(miForm.getMotivo ()!=null){
				motivo = miForm.getMotivo ();	
			}
			
			List<Integer> tiposDireccionAValidarIntegers = Direccion.getListaDireccionesObligatorias(miForm.getTipoAcceso());
			// Se llama a la interfaz Direccion para insertar una nueva direccion
			Direccion dirAux = Direccion.insertar(beanDir, tiposDir, motivo,tiposDireccionAValidarIntegers, request, usr); 
			
			//Se llama a la interfaz Direccion para realizar el borrado de la direccion antigua
			beanDir.setIdDireccion(idDireccionAntigua);

			//Si se necesita confirmaci�n por parte del usuario se realiza una peticion de pregunta
			if(dirAux.isConfirmacionPregunta()){
				request.setAttribute("modo", "guardarInsertarHistorico");
				t.rollback();
				return dirAux.getTipoPregunta();
			}
			
			direccion.borrar(beanDir,tiposDireccionAValidarIntegers, request, usr);
			
			//confirmando las modificaciones de BD
			t.commit();
			
			//saliendo
			rc = exitoModal("messages.updated.success", request);
			
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, t);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, t);
		}
		return rc;
	} //borrar()
	
	
	protected String enviarNuevaDireccion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		try {

			DireccionesForm form = (DireccionesForm)formulario;
			
				
			Long idPersona = form.getIDPersona();
			Long idDireccion = form.getIdDireccion();
			Integer idInstitucionPersona = form.getIDInstitucion();
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			CenClienteAdm clienteAdm =  new CenClienteAdm(this.getUserName(request),
					user,idInstitucionPersona.intValue(), idPersona.longValue());
			Hashtable hash = clienteAdm.getDirecciones(idPersona, idInstitucionPersona, idDireccion);

			request.setAttribute("direccion", hash);		

					

		} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return "seleccion";
		}
	
	
	/**
	 * M�todo encargado de volcar la informaci�n del objeto tipo DireccionesForm al objeto tipo CenDireccionesBean
	 * @param miForm,beanDir
	 * @throws SIGAException
	 */
	private void poblarObjeto(DireccionesForm miForm,CenDireccionesBean beanDir)
			throws SIGAException {

		beanDir.setOtraProvincia(Integer.valueOf(miForm.getOtraProvincia()));
		beanDir.setIdPersona(miForm.getIDPersona());
		beanDir.setCodigoPostal(miForm.getCodigoPostal());
		beanDir.setCorreoElectronico(miForm.getCorreoElectronico());
		beanDir.setDomicilio(miForm.getDomicilio());
		beanDir.setFax1(miForm.getFax1());
		beanDir.setFax2(miForm.getFax2());
		beanDir.setFechaBaja(miForm.getFechaBaja());
		beanDir.setIdInstitucion(miForm.getIDInstitucion());
		beanDir.setIdPais(miForm.getPais());
		if (miForm.getPais().equals(ClsConstants.ID_PAIS_ESPANA)
				|| miForm.getPais().equals("")) {
			beanDir.setIdPoblacion(miForm.getPoblacion());
			beanDir.setIdProvincia(miForm.getIdProvinciaHidden());
			beanDir.setPoblacionExtranjera("");
		} else {
			beanDir.setPoblacionExtranjera(miForm.getPoblacionExt());
			beanDir.setIdPoblacion("");
			beanDir.setIdProvincia("");
		}
		beanDir.setMovil(miForm.getMovil());
		beanDir.setPaginaweb(miForm.getPaginaWeb());
		beanDir.setPreferente(this.campoPreferenteBooleanToString(
				miForm.getPreferenteMail(), miForm.getPreferenteCorreo(),
				miForm.getPreferenteFax(), miForm.getPreferenteSms()));
		beanDir.setTelefono1(miForm.getTelefono1());
		beanDir.setTelefono2(miForm.getTelefono2());

	}
	
}
