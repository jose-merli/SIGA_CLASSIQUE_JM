package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AsistenciasForm;

/**
 * @author carlos.vidal
 * @since 3/2/2005
 * @version david.sanchezp: uso del action de telefonosJG.
 */

public class AsisRespAsistenciaAction extends MasterAction {
	
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		AsistenciasForm miForm 	= (AsistenciasForm)formulario;
		String path = mapping.getPath();

		// Obtenemos el turno seleccionado para la consulta
		String anio 	= request.getParameter("ANIO");
		String numero 	= request.getParameter("NUMERO");
		if(anio == null || numero == null) {
			anio = (String) request.getSession().getAttribute("anioRes");
			numero = (String) request.getSession().getAttribute("numeroRes");
			request.getSession().removeAttribute("anioRes");
			request.getSession().removeAttribute("numeroRes");
		}
		
		// Obtenemos el turno seleccionado para la consulta
		String select = "SELECT IDPERSONAJG ,IDPERSONACOLEGIADO FROM SCS_ASISTENCIA A where IDINSTITUCION = "+usr.getLocation()+
						" AND ANIO = "+anio+" AND NUMERO = "+numero;
		
		ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		resultado=(Vector)asistencias.ejecutaSelect(select);
		String IDPERSONAJG = "";
		String IDPERSONA_REPRESENTANTE = "";
		IDPERSONAJG = (String) ((Hashtable) resultado.get(0)).get("IDPERSONAJG");
		IDPERSONA_REPRESENTANTE = (String) ((Hashtable) resultado.get(0)).get("IDPERSONA_REPRESENTANTE");
		ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		
		try {
			request.setAttribute("anio",anio);
			request.setAttribute("numero",numero);
			request.getSession().removeAttribute("DATOSSOJ");
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().removeAttribute("resultadoTelefonos");
			
			if(path.equals("/JGR_ResponsableAsistencia")) {
				if(IDPERSONA_REPRESENTANTE!=null && !IDPERSONA_REPRESENTANTE.equals("")) {
					request.getSession().setAttribute("DATOSSOJ",miHash);
					request.getSession().setAttribute("DATABACKUP",miHash);
					/* Recuperamos del request los parámetros pasados a las pestanhas */
					miHash.put(ScsPersonaJGBean.C_IDINSTITUCION,usr.getLocation());
					miHash.put(ScsPersonaJGBean.C_IDPERSONA,IDPERSONA_REPRESENTANTE);		
					/* Recuperamos de la base de datos los valores originales */
					resultado = admBean.selectPorClave(miHash);
					request.setAttribute("resultado",(ScsPersonaJGBean)resultado.get(0));
					ScsPersonaJGBean persona = (ScsPersonaJGBean)resultado.get(0);
					/* Ahora vamos a recuperar de la base de datos los teléfonos, para eso previamente borramos el vector de resultados */
					resultado.clear();
					/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
					String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA);
					resultadoTF = admBeanTlf.selectGenerico(sql);
					request.setAttribute("resultadoTF",resultadoTF);
				}
				request.setAttribute("ACTION","/JGR_ResponsableAsistencia.do");
				request.setAttribute("TITULO","gratuita.asisResp.literal.tituloResp");
				request.setAttribute("LOCALIZACION","gratuita.asisResp.literal.localizacionResp");
			} else if(path.equals("/JGR_AsistidoAsistencia")) {
				if(IDPERSONAJG!=null && !IDPERSONAJG.equals("")) {
					request.getSession().setAttribute("DATOSSOJ",miHash);
					request.getSession().setAttribute("DATABACKUP",miHash);
					/* Recuperamos del request los parámetros pasados a las pestanhas */
					miHash.put(ScsPersonaJGBean.C_IDINSTITUCION,usr.getLocation());
					miHash.put(ScsPersonaJGBean.C_IDPERSONA,IDPERSONAJG);		
					/* Recuperamos de la base de datos los valores originales */
					resultado = admBean.selectPorClave(miHash);
					request.setAttribute("resultado",(ScsPersonaJGBean)resultado.get(0));
					/* Ahora vamos a recuperar de la base de datos los teléfonos, para eso previamente borramos el vector de resultados */
					resultado.clear();
					/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
					String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA);
					resultadoTF = admBeanTlf.selectGenerico(sql);
					request.setAttribute("resultadoTF",resultadoTF);
				}
				request.setAttribute("ACTION","/JGR_AsistidoAsistencia.do");
				request.setAttribute("TITULO","gratuita.asisResp.literal.tituloAsis");
				request.setAttribute("LOCALIZACION","gratuita.asisResp.literal.localizacionAsis");
			}			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "editar";
	}
	
	/** 
	 *  Funcion que atiende la accion editar
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
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		String idinstitucion = "";
		String idpersona = "";
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		AsistenciasForm miForm 	= (AsistenciasForm)formulario;
		String path = mapping.getPath();
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));

		try {
			ScsPersonaJGBean persona = new ScsPersonaJGBean();
			// Metemos los valores.
			persona.setIdPersona(Integer.valueOf(miForm.getIdPersona()));
			persona.setNif(miForm.getNif()); 
			persona.setNombre(miForm.getNombre()); 
			persona.setApellido1(miForm.getApellido1());  
			persona.setApellido2(miForm.getApellido2()); 
			persona.setDireccion(miForm.getDireccion()); 
			persona.setCodigoPostal(miForm.getCodigoPostal());
			// Hay que pasar de formato dd/mm/yyyy a yyyy/mm/aa hh:mm:ss
			String fn = miForm.getFechaNacimiento();
			fn = fn.substring(6,10)+"/"+fn.substring(3,5)+"/"+fn.substring(0,2)+ " 00:00:00";
			persona.setFechaNacimiento(fn);
			if (miForm.getIdEstadoCivilAux()!=null && !miForm.getIdEstadoCivilAux().equalsIgnoreCase("NULL"))
				persona.setIdEstadoCivil(Integer.valueOf(miForm.getIdEstadoCivilAux()));
			if(miForm.getRegimenConyugalAux()!=null && !miForm.getRegimenConyugalAux().equalsIgnoreCase("NULL"))
				persona.setRegimenConyugal(miForm.getRegimenConyugalAux());
			persona.setIdPais(miForm.getIdPaisAux());
			if (miForm.getIdProvinciaAux()!=null && !miForm.getIdProvinciaAux().equalsIgnoreCase("NULL"))
				persona.setIdProvincia(miForm.getIdProvinciaAux());
			if (miForm.getIdPoblacionAux()!=null && !miForm.getIdPoblacionAux().equalsIgnoreCase("NULL"))
				persona.setIdPoblacion(miForm.getIdPoblacionAux());
			request.setAttribute("resultado",persona);
			/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
			String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + usr.getLocation() + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miForm.getIdPersona();
			resultadoTF = admBeanTlf.selectGenerico(sql);
			request.setAttribute("resultadoTF",resultadoTF);
	
			if(path.equals("/JGR_ResponsableAsistencia")) {
				request.setAttribute("ACTION","/JGR_ResponsableAsistencia.do");
				request.setAttribute("TITULO","gratuita.asisResp.literal.tituloResp");
				request.setAttribute("LOCALIZACION","gratuita.asisResp.literal.localizacionResp");
			} else if(path.equals("/JGR_AsistidoAsistencia")) {
				request.setAttribute("ACTION","/JGR_AsistidoAsistencia.do");
				request.setAttribute("TITULO","gratuita.asisResp.literal.tituloAsis");
				request.setAttribute("LOCALIZACION","gratuita.asisResp.literal.localizacionAsis");
			}
			request.setAttribute("anio",miForm.getAnio());
			request.setAttribute("numero",miForm.getNumero());
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		String forward = "";
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		try
		{
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			// Consultamos las personas con el nif indicado
			ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
			String elNif = miForm.getNif().toUpperCase();
			
			//Le anhadimos 0 por delante hasta completar los 9 carácteres
			while  (elNif.length() < 9) elNif = "0" + elNif;
			String where = " where upper(NIF) = '"+elNif+"' AND IDINSTITUCION ="+usr.getLocation();
			Vector resultadoNIF = admBean.select(where);
			
			//LMSP Se redirige a un jsp que se carga en el frame oculto, y hace todo lo demás :)
			request.setAttribute("resultadoNIF",resultadoNIF);
			forward = "abrirBusquedaOculta";
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**Funcion que transforma los datos de entrada para poder hacer la insercion a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos){
		return datos;
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
	    return "Ver";
	}

	/** 
	 *  Funcion que atiende la accion nuevo
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
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion insertar
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
		
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		String path = mapping.getPath();
		String forward = "";
		UserTransaction tx = null;

		try {
			Hashtable hash = new Hashtable();
			Hashtable hashAsis = new Hashtable();
			AsistenciasForm miForm = (AsistenciasForm) formulario;
			ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
			ScsAsistenciasAdm asisAdm =  new ScsAsistenciasAdm(this.getUserBean(request));
			String elNif = miForm.getNif();
			while  (elNif.length() < 9) elNif = "0" + elNif;

			// Comprobamos si existe el nif introducido
			String where = " where NIF = '"+elNif+"'";
			Vector resultadoNIF = admBean.select(where);

			// Campos a clave
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
			// SCS_ASISTENCIA.PERSONAJG con el IDPERSONAJG
			// Campos a clave
			hashAsis.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());
			hashAsis.put(ScsAsistenciasBean.C_ANIO,miForm.getAnio());
			hashAsis.put(ScsAsistenciasBean.C_NUMERO,miForm.getNumero());
			tx = usr.getTransaction(); 
			tx.begin();

			// Hay que ver si es una nueva persona o una existente. Si es nueva se inserta, sino, se pone el id
			// existente.
			String id = "";
			if(((resultadoNIF == null || resultadoNIF.size()==0)) || (miForm.getNuevo()!= null && !miForm.getNuevo().equals("0"))) {
				String select = "SELECT MAX(IDPERSONA)+1 maxvalor FROM scs_personajg";
				Vector resultado=(Vector)admBean.selectGenerico(select);
				id = (String) ((Hashtable) resultado.get(0)).get("MAXVALOR");
				hash.put(ScsPersonaJGBean.C_IDPERSONA,id);
				admBean.insert(hash);
			} else {
				id = miForm.getIdPersona();
			}

			if(path.equals("/JGR_ResponsableAsistencia")) {
				// Campos a modificar
				String[] campos = {ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE};
				hashAsis.put(ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE,id);
				asisAdm.updateDirect(hashAsis,null,campos);
			} else if(path.equals("/JGR_AsistidoAsistencia")) {
				// Campos a modificar
				String[] campos = {ScsAsistenciasBean.C_IDPERSONAJG};
				hashAsis.put(ScsAsistenciasBean.C_IDPERSONAJG,id);
				asisAdm.updateDirect(hashAsis,null,campos);
			}
			tx.commit();
			forward = "exito";
			request.setAttribute("mensaje","messages.inserted.success");
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return forward;
	}

	/** 
	 *  Funcion que atiende la accion modificar
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
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		String path = mapping.getPath();
		String forward = "";
		UserTransaction tx = null;
		
		try {
			Hashtable hash = new Hashtable();
			Hashtable hashAsis = new Hashtable();
			AsistenciasForm miForm = (AsistenciasForm) formulario;
			ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
			ScsAsistenciasAdm asisAdm =  new ScsAsistenciasAdm(this.getUserBean(request));
			// Modificamos el registro. Tablas a modificar. SCS_PERSONAJG y SCS_ASISTENCIA
			// Comprobamos si existe el nif introducido
			String elNif = miForm.getNif();
			while  (elNif.length() < 9) elNif = "0" + elNif;
			String where = " where NIF = '"+elNif+"'";
			Vector resultadoNIF = admBean.select(where);
			//Modificamos SCS_PERSONAJG Y SCS_ASISTENCIA.PERSONAJG con el IDPERSONAJG
			// Campos a clave
			hash.put(ScsPersonaJGBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsPersonaJGBean.C_IDPERSONA,miForm.getIdPersona());
			// Campos a modificar
			String[] campos = {ScsPersonaJGBean.C_NIF,
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
			// SCS_ASISTENCIA.PERSONAJG con el IDPERSONAJG
			// Campos a clave
			hashAsis.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());
			hashAsis.put(ScsAsistenciasBean.C_ANIO,miForm.getAnio());
			hashAsis.put(ScsAsistenciasBean.C_NUMERO,miForm.getNumero());
			tx = usr.getTransaction(); 
			
			tx.begin();
			if((resultadoNIF != null && resultadoNIF.size()>0) && (miForm.getNuevo()!= null && !miForm.getNuevo().equals("1"))) {
				admBean.updateDirect(hash,null,campos);
				if(path.equals("/JGR_ResponsableAsistencia")) {
					// Campos a modificar
					String[] campos1 = {ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE};
					hashAsis.put(ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE,miForm.getIdPersona());
					asisAdm.updateDirect(hashAsis,null,campos1);
				} else if(path.equals("/JGR_AsistidoAsistencia")) {
					// Campos a modificar
					String[] campos1 = {ScsAsistenciasBean.C_IDPERSONAJG};
					hashAsis.put(ScsAsistenciasBean.C_IDPERSONAJG,miForm.getIdPersona());
					asisAdm.updateDirect(hashAsis,null,campos1);
				}
			} else {
				String select = "SELECT MAX(IDPERSONA)+1 maxvalor FROM scs_personajg";
				Vector resultado=(Vector)admBean.selectGenerico(select);
				String id = (String) ((Hashtable) resultado.get(0)).get("MAXVALOR");
				//Obtenemos el idpersona mayor y le sumamos uno.
				if(path.equals("/JGR_ResponsableAsistencia"))
				{
					//hashAsis.put(ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE,id);
					hash.put(ScsPersonaJGBean.C_IDPERSONA,id);
				} else if(path.equals("/JGR_AsistidoAsistencia")) {
					//hashAsis.put(ScsAsistenciasBean.C_IDPERSONAJG,id);
					hash.put(ScsPersonaJGBean.C_IDPERSONA,id);
				}
				
				//Insertamos PERSONAJG Y Modificamos SCS_ASISTENCIA.PERSONAJG con el IDPERSONAJG insertado
				admBean.insert(hash);
				hashAsis.put(ScsAsistenciasBean.C_IDPERSONAJG,id);
				if(path.equals("/JGR_ResponsableAsistencia")) {
					// Campos a modificar
					String[] campos1 = {ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE};
					hashAsis.put(ScsAsistenciasBean.C_IDPERSONA_REPRESENTANTE,id);
					asisAdm.updateDirect(hashAsis,null,campos1);
				} else if(path.equals("/JGR_AsistidoAsistencia")) {
					// Campos a modificar
					String[] campos1 = {ScsAsistenciasBean.C_IDPERSONAJG};
					hashAsis.put(ScsAsistenciasBean.C_IDPERSONAJG,id);
					asisAdm.updateDirect(hashAsis,null,campos1);
				}
			}
			tx.commit();
			forward = "exito";
			request.setAttribute("mensaje","messages.updated.success");
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return forward;
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
			
		return "exito";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		String forward = "abrirBusquedaModal";
		
		try {
			HttpSession ses = request.getSession();
			Vector resultadoNIF = (Vector)ses.getAttribute("resultadoNIF");
			ses.removeAttribute("resultadoNIF");
			request.setAttribute("resultadoNIF",resultadoNIF);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

}