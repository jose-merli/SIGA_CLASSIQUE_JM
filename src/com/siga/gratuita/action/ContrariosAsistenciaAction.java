package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsContrariosAsistenciaBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ContrariosAsistenciaForm;


/**
 * Action de Contrarios de Asistencias.
 * @author david.sanchezp
 * @since 02/03/2006
 */
public class ContrariosAsistenciaAction extends MasterAction {

	/** 
	 * Método que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
		
		String sEsFichaColegial = request.getParameter("esFichaColegial");
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);

		try { 
			miForm = (MasterForm) formulario;
			
			String accion = miForm.getModo();

	  		// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("buscarContrarios")){
				mapDestino = buscarContrarios(mapping, miForm, request, response);
			} else {
				return super.executeInternal(mapping,
						      formulario,
						      request, 
						      response);
			}			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		return mapping.findForward(mapDestino);
	}	

	/** 
	 * Método que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		String anio=null, numero=null, modoPestanha=null;

		//Tomo los datos de la pestanha:
		anio 	= request.getParameter("ANIO");
		numero 	= request.getParameter("NUMERO");
		modoPestanha	= request.getParameter("MODO");
		
		// Mandamos estos datos en el request:
		request.setAttribute("anio", anio);
		request.setAttribute("numero", numero);
		request.setAttribute("modoPestanha", modoPestanha);
		
		return "inicio";
	}
	
	
	/** 
	 * Método que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		//Accion: edicion/consulta o ver/nuevo
		request.setAttribute("accion", "editar");
		return this.mostrarDatos(formulario, request);
	}

	/** 
	 * Método que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		//Accion: edicion/consulta o ver/nuevo
		request.setAttribute("accion", "ver");
		return this.mostrarDatos(formulario, request);
	}	
	
	//Muestra los datos en la modal de contrarios de asistencias.
	private String mostrarDatos(MasterForm formulario, HttpServletRequest request) throws SIGAException {
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) formulario;		
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		ScsPersonaJGAdm admPersonaJG = new ScsPersonaJGAdm(this.getUserBean(request));
		ScsContrariosAsistenciaAdm admContrariosAsistencia = new ScsContrariosAsistenciaAdm(this.getUserBean(request));

		try {
			ScsPersonaJGBean persona = new ScsPersonaJGBean();
			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);
			//IDPERSONA:
			String idPersona = (String)vOcultos.get(0);
			String where = " where "+ScsPersonaJGBean.C_IDINSTITUCION+"="+usr.getLocation()+
						   " and "+ScsPersonaJGBean.C_IDPERSONA+"="+idPersona;
			Vector vPersonaJG = admPersonaJG.select(where);
			persona = (ScsPersonaJGBean)vPersonaJG.get(0);
			//Mandamos la personaJG en el request:	
			request.setAttribute("beanPersonaJG",persona);
			
			//Consultamos las observaciones asociadas:
			where = " where "+ScsContrariosAsistenciaBean.C_IDINSTITUCION+"="+usr.getLocation()+
				    " and "+ScsContrariosAsistenciaBean.C_ANIO+"="+miForm.getAnio()+
				    " and "+ScsContrariosAsistenciaBean.C_NUMERO+"="+miForm.getNumero()+				    
				    " and "+ScsContrariosAsistenciaBean.C_IDPERSONA+"="+idPersona;
			Vector vContrarioAistencia = admContrariosAsistencia.select(where);
			ScsContrariosAsistenciaBean beanContrarioAsistencia = (ScsContrariosAsistenciaBean)vContrarioAistencia.get(0);			
			miForm.setObservaciones(beanContrarioAsistencia.getObservaciones());
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "editar";
	}
	
	/** 
	 * Método que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		//Accion: edicion/consulta o ver/nuevo
		request.setAttribute("accion", "nuevo");
		return "editar";
	}

	/** 
	 * Método que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String path = mapping.getPath();
		String forward = "";
		UserTransaction tx = null;

		try {
			Hashtable hash = new Hashtable();
			Hashtable hashContrarioAsistencia = new Hashtable();
			ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) formulario;
			ScsPersonaJGAdm admPersonaJG =  new ScsPersonaJGAdm(this.getUserBean(request));
			ScsContrariosAsistenciaAdm admContrariosAsistencias =  new ScsContrariosAsistenciaAdm(this.getUserBean(request));
			
			// Rellenamos con 0 el NIF:
			String elNif = miForm.getNif();
			while  (elNif.length() < 9) elNif = "0" + elNif;

			// Comprobamos si existe el nif introducido
			String where = " where NIF = '"+elNif+"'";
			Vector resultadoNIF = admPersonaJG.select(where);

			// Campos clave para luego modificar la personaJG:
			String[] claves =   {ScsPersonaJGBean.C_IDPERSONA,
							 	 ScsPersonaJGBean.C_IDINSTITUCION};
			
			// Campos a modificar de la personaJG:
			String[] campos =  {ScsPersonaJGBean.C_NIF,
								ScsPersonaJGBean.C_NOMBRE,
								ScsPersonaJGBean.C_APELLIDO1,
								ScsPersonaJGBean.C_APELLIDO2,
								ScsPersonaJGBean.C_DIRECCION,
								ScsPersonaJGBean.C_CODIGOPOSTAL,
								ScsPersonaJGBean.C_FECHANACIMIENTO,
								ScsPersonaJGBean.C_IDPAIS,
								ScsPersonaJGBean.C_IDPROVINCIA,
								ScsPersonaJGBean.C_IDPOBLACION,
								ScsPersonaJGBean.C_ESTADOCIVIL,
								ScsPersonaJGBean.C_REGIMENCONYUGAL};			
			
			// Valores de los campos de la personaJG:
			hash.put(ScsPersonaJGBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsPersonaJGBean.C_NIF,elNif);
			hash.put(ScsPersonaJGBean.C_NOMBRE,miForm.getNombre());
			hash.put(ScsPersonaJGBean.C_APELLIDO1,miForm.getApellido1());
			hash.put(ScsPersonaJGBean.C_APELLIDO2,miForm.getApellido2());
			hash.put(ScsPersonaJGBean.C_DIRECCION,miForm.getDireccion());
			hash.put(ScsPersonaJGBean.C_CODIGOPOSTAL,miForm.getCodigoPostal());
			hash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaNacimiento()));
			hash.put(ScsPersonaJGBean.C_IDPAIS,miForm.getIdPais());
			hash.put(ScsPersonaJGBean.C_IDPROVINCIA,miForm.getIdProvincia());
			hash.put(ScsPersonaJGBean.C_IDPOBLACION,miForm.getIdPoblacion());
			hash.put(ScsPersonaJGBean.C_ESTADOCIVIL,miForm.getIdEstadoCivil());
			hash.put(ScsPersonaJGBean.C_REGIMENCONYUGAL,miForm.getRegimenConyugal());

			// Campos a insertar:
			hashContrarioAsistencia.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());
			hashContrarioAsistencia.put(ScsAsistenciasBean.C_ANIO,miForm.getAnio());
			hashContrarioAsistencia.put(ScsAsistenciasBean.C_NUMERO,miForm.getNumero());			
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_OBSERVACIONES, miForm.getObservaciones());
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_FECHAMODIFICACION,"SYSDATE");
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_USUMODIFICACION,usr.getUserName());
			
			tx = usr.getTransaction(); 
			tx.begin();

			// Hay que ver si es una nueva persona o una existente. 
			// Si es nueva se inserta, sino, se pone el id existente y se modifican sus datos.
			String idPersonaJG = "";
			if(resultadoNIF.isEmpty()) { // Si no existe el NIF en ScsPersonaJG se inserta
				String select = "SELECT MAX("+ScsPersonaJGBean.C_IDPERSONA+")+1 AS MAXVALOR FROM "+ScsPersonaJGBean.T_NOMBRETABLA;
				Vector resultado=(Vector)admPersonaJG.selectGenerico(select);
				idPersonaJG = (String) ((Hashtable) resultado.get(0)).get("MAXVALOR");
				hash.put(ScsPersonaJGBean.C_IDPERSONA,idPersonaJG);
				admPersonaJG.insert(hash);
			} else { // Existe ese NIF, actualizo lo que introduzca para esa personaJG:
				idPersonaJG = miForm.getIdPersona();
				hash.put(ScsPersonaJGBean.C_IDPERSONA,idPersonaJG);
				admPersonaJG.updateDirect(hash, claves, campos);
			}

			// Anhado el idPersonaJG:
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_IDPERSONA,idPersonaJG);
			
			admContrariosAsistencias.insert(hashContrarioAsistencia);			
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success",request);
	}

	/** 
	 * Método que atiende la accion modificar
	 * <br> Nota: No es editable el vampo NIF luego la persona JG es con IDPERSONA fijo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException  {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;
		String idPersonaJG = null;
		
		try {
			Hashtable hash = new Hashtable();
			Hashtable hashContrarioAsistencia = new Hashtable();
			ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) formulario;
			ScsPersonaJGAdm admPersonaJG =  new ScsPersonaJGAdm(this.getUserBean(request));
			ScsContrariosAsistenciaAdm admContrariosAsistencia =  new ScsContrariosAsistenciaAdm(this.getUserBean(request));
			
			// Rellenamos con 0 el NIF:
			String elNif = miForm.getNif();
			while  (elNif.length() < 9) elNif = "0" + elNif;
			
			// Campos clave para luego modificar la personaJG:
			String[] claves =   {ScsPersonaJGBean.C_IDPERSONA,
							 	 ScsPersonaJGBean.C_IDINSTITUCION};
			
			// Campos a modificar
			String[] campos =  {ScsPersonaJGBean.C_NIF,
								ScsPersonaJGBean.C_NOMBRE,
								ScsPersonaJGBean.C_APELLIDO1,
								ScsPersonaJGBean.C_APELLIDO2,
								ScsPersonaJGBean.C_DIRECCION,
								ScsPersonaJGBean.C_CODIGOPOSTAL,
								ScsPersonaJGBean.C_FECHANACIMIENTO,
								ScsPersonaJGBean.C_IDPAIS,
								ScsPersonaJGBean.C_IDPROVINCIA,
								ScsPersonaJGBean.C_IDPOBLACION,
								ScsPersonaJGBean.C_ESTADOCIVIL,
								ScsPersonaJGBean.C_REGIMENCONYUGAL};

			// Campos a clave
			hash.put(ScsPersonaJGBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsPersonaJGBean.C_IDPERSONA,miForm.getIdPersona());
			// Resto de campos:
			hash.put(ScsPersonaJGBean.C_NIF,elNif);
			hash.put(ScsPersonaJGBean.C_NOMBRE,miForm.getNombre());
			hash.put(ScsPersonaJGBean.C_APELLIDO1,miForm.getApellido1());
			hash.put(ScsPersonaJGBean.C_APELLIDO2,miForm.getApellido2());
			hash.put(ScsPersonaJGBean.C_DIRECCION,miForm.getDireccion());
			hash.put(ScsPersonaJGBean.C_CODIGOPOSTAL,miForm.getCodigoPostal());
			hash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaNacimiento()));
			hash.put(ScsPersonaJGBean.C_IDPAIS,miForm.getIdPais());
			hash.put(ScsPersonaJGBean.C_IDPROVINCIA,miForm.getIdProvincia());
			hash.put(ScsPersonaJGBean.C_IDPOBLACION,miForm.getIdPoblacion());
			hash.put(ScsPersonaJGBean.C_ESTADOCIVIL,miForm.getIdEstadoCivil());
			hash.put(ScsPersonaJGBean.C_REGIMENCONYUGAL,miForm.getRegimenConyugal());
					
			// Campos clave para luego modificar el Contrario:
			String[] clavesContrario =   {ScsContrariosAsistenciaBean.C_IDINSTITUCION,
										  ScsContrariosAsistenciaBean.C_ANIO,
										  ScsContrariosAsistenciaBean.C_NUMERO,
										  ScsContrariosAsistenciaBean.C_IDPERSONA};							 	 
			
			// Campos a modificar del contrario de la asistencia:
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_ANIO,miForm.getAnio());
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_NUMERO,miForm.getNumero());
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_IDPERSONA, miForm.getIdPersona());
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_OBSERVACIONES, miForm.getObservaciones());
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_FECHAMODIFICACION, "SYSDATE");
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_USUMODIFICACION, usr.getUserName());

			// Campos campos para luego modificar el Contrario:
			String[] camposContrario =   {ScsContrariosAsistenciaBean.C_OBSERVACIONES};
			
			tx = usr.getTransaction(); 
			
			tx.begin();			
			// Modificamos los datos de la personaJG:
			admPersonaJG.updateDirect(hash, claves, campos);
			
			// Modificamos el contrario de la asistencia:
			//Insertamos el nuevo idPersona:
			hashContrarioAsistencia.put(ScsContrariosAsistenciaBean.C_IDPERSONA, miForm.getIdPersona());
			admContrariosAsistencia.updateDirect(hashContrarioAsistencia, clavesContrario, camposContrario);
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.updated.success", request);
	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		UsrBean usr = null;
		UserTransaction tx = null;
		
		try {
 			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction(); 
			
			ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) formulario;
			ScsContrariosAsistenciaAdm admContrariosAsistencias = new ScsContrariosAsistenciaAdm(this.getUserBean(request));
			ScsTelefonosPersonaJGAdm admTelefonos = new ScsTelefonosPersonaJGAdm(this.getUserBean(request)); 
			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);			
			String anio = miForm.getAnio();			
			String numero = miForm.getNumero();
			String idPersona = (String)vOcultos.get(0);
			String idInstitucion = usr.getLocation();
			
			Hashtable hash = new Hashtable();
			hash.put(ScsContrariosAsistenciaBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ScsContrariosAsistenciaBean.C_IDPERSONA,idPersona);
			hash.put(ScsContrariosAsistenciaBean.C_ANIO,anio);
			hash.put(ScsContrariosAsistenciaBean.C_NUMERO,numero);
			
			tx.begin();			
			admContrariosAsistencias.delete(hash);
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.deleted.error",e,tx); 
		} 
		return exitoRefresco("messages.deleted.success", request);
	}

	// Realiza la busqueda de contrarios
	private String buscarContrarios	(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException  {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) formulario;
		
		// Obtenemos el turno seleccionado para la consulta
		String anio 	= miForm.getAnio();
		String numero 	= miForm.getNumero();
		String modoPestanha 	= miForm.getModoPestanha();
		String idInstitucion = usr.getLocation(); 

		ScsContrariosAsistenciaAdm admContrariosAsistencias = new ScsContrariosAsistenciaAdm(this.getUserBean(request));
		Vector vContrarios = admContrariosAsistencias.getContrarios(idInstitucion, anio, numero);
		
		request.setAttribute("vContrariosAsistencias", vContrarios);
		return "listado";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		UsrBean usr = null;
		try
		{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) formulario;
			ScsPersonaJGAdm admPersonaJG =  new ScsPersonaJGAdm(this.getUserBean(request));
			
			// Consultamos las personas con el nif indicado			
			// NIF: le anhadimos 0 por delante hasta completar los 9 carácteres
			String elNif = miForm.getNif().toUpperCase();
			while  (elNif.length() < 9) elNif = "0" + elNif;
			String where = " where upper(NIF) = '"+elNif+"' AND IDINSTITUCION ="+usr.getLocation();
			Vector resultadoNIF = admPersonaJG.select(where);
			
			//LMSP Se redirige a un jsp que se carga en el frame oculto, y hace todo lo demás :)
			request.setAttribute("resultadoNIF",resultadoNIF);			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "abrirBusquedaOculta";
	}	
	
}