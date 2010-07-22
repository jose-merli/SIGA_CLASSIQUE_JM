
package com.siga.censo.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.beans.*;
import com.siga.censo.form.DireccionesForm;
import com.siga.envios.form.RemitentesForm;
import com.siga.general.*;


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
			throw new SIGAException("Error en la Aplicación",e);
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
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			numero = colegiadoAdm.getIdentificadorColegiado(bean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(((DireccionesForm)formulario).getIncluirRegistrosConBajaLogica());
			request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
			
			v = clienteAdm.getDirecciones(idPersona,idInstitucionPersona, bIncluirRegistrosConBajaLogica);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("accion", accion);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("vDatos", v);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);			
		}
		return "inicio";
	} //abrir()
	
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
		String modo = "editar";
		
		try 
		{
			DireccionesForm form = (DireccionesForm) formulario;
			Vector ocultos = form.getDatosTablaOcultos(0);	
			String accion = (String)request.getParameter("accion");				
			Long idPersona = form.getIDPersona();
			Long idDireccion = Long.valueOf((String)ocultos.elementAt(0));
			Integer idInstitucionPersona = form.getIDInstitucion();
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			CenClienteAdm clienteAdm =  new CenClienteAdm(this.getUserName(request),
					user,idInstitucionPersona.intValue(), idPersona.longValue());
			Hashtable hash = clienteAdm.getDirecciones(idPersona, idInstitucionPersona, idDireccion);
			
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm (this.getUserName(request),
					this.getUserBean(request),idInstitucionPersona.intValue(),idPersona.longValue());
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (this.getUserBean(request));
			Vector vTipos = new Vector();
			vTipos=cenTipoDirAdm.select("");
			Hashtable hTipoDir=new Hashtable();
			Hashtable hTipoDirSel=new Hashtable();
			if ( (vTipos != null) && (vTipos.size() > 0) )
				for (int i = 1; i <= vTipos.size(); i++) {
					CenTipoDireccionBean tipoDir = (CenTipoDireccionBean) vTipos.get(i-1);
					hTipoDir.put(tipoDir.getIdTipoDireccion(),"N");
				}
			request.setAttribute("vTipos",vTipos);
		    
			Hashtable claves = new Hashtable();
			
			Vector v = new Vector();
			// compruebo al visibilidad con CenClienteAdm que ha utilizado su constructor de visibilidad
			if (clienteAdm.compruebaVisibilidadCampo(CenDireccionTipoDireccionBean.T_NOMBRETABLA, CenDireccionTipoDireccionBean.C_IDTIPODIRECCION)) {
				UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucionPersona);
				UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
				UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
				v = tipoDirAdm.select(claves);
				if ( (v != null) && (v.size() > 0) ) {
	   			    for (int i = 1; i <= v.size(); i++) {
	   			    	CenDireccionTipoDireccionBean tipoDirSel = (CenDireccionTipoDireccionBean) v.get(i-1);
				     	hTipoDirSel.put(tipoDirSel.getIdTipoDireccion(),"S");
	   			    }
				}    
			}
			hash.put(CenTipoDireccionBean.C_IDTIPODIRECCION, v);
			Integer clave=null;
			
			Enumeration clavesDir = hTipoDirSel.keys();
		    while (clavesDir.hasMoreElements()) {
		    	clave = (Integer)clavesDir.nextElement();
		    	if (hTipoDir.containsKey(clave))
					hTipoDir.put(clave,"S");
		    }
			
			request.setAttribute("TipoDirecciones",hTipoDir);
			
			String fax = "false", mail = "false", correo  = "false", sms = "false";
			String preferente = UtilidadesHash.getString(hash, CenDireccionesBean.C_PREFERENTE);
			if (preferente.indexOf("E") >= 0) mail   = "true";
			if (preferente.indexOf("F") >= 0) fax    = "true";
			if (preferente.indexOf("C") >= 0) correo = "true";
			if (preferente.indexOf("S") >= 0) sms    = "true";
			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);
			request.setAttribute("preferenteFax", fax);
			request.setAttribute("preferenteCorreo", correo);
			request.setAttribute("preferenteMail", mail);
			request.setAttribute("preferenteSms", sms);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return modo;
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
		String modo = "ver";
		
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
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request), user, 
					idInstitucionPersona.intValue(), idPersona.longValue());
			Hashtable hash = clienteAdm.getDirecciones(idPersona,idInstitucionPersona,idDireccion,true);
			
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm (user);
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (this.getUserBean(request));
			Vector vTipos = new Vector();
			vTipos=cenTipoDirAdm.select("");
			Hashtable hTipoDir=new Hashtable();
			Hashtable hTipoDirSel=new Hashtable();
			if ( (vTipos != null) && (vTipos.size() > 0) ) {
				for (int i = 1; i <= vTipos.size(); i++) {
					CenTipoDireccionBean tipoDir = (CenTipoDireccionBean) vTipos.get(i-1);
					hTipoDir.put(tipoDir.getIdTipoDireccion(),"N");
				}
			}   
			request.setAttribute("vTipos",vTipos);
			Hashtable claves = new Hashtable();
			Vector v = new Vector();
			// compruebo al visibilidad con CenClienteAdm que ha utilizado su constructor de visibilidad
			if (clienteAdm.compruebaVisibilidadCampo(CenDireccionTipoDireccionBean.T_NOMBRETABLA, 
					CenDireccionTipoDireccionBean.C_IDTIPODIRECCION))
			{
				UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucionPersona);
				UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
				UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
				v = tipoDirAdm.select(claves);
				if ( (v != null) && (v.size() > 0) ) {
	   			    for (int i = 1; i <= v.size(); i++) {
	   			    	CenDireccionTipoDireccionBean tipoDirSel = (CenDireccionTipoDireccionBean) v.get(i-1);
				     	hTipoDirSel.put(tipoDirSel.getIdTipoDireccion(),"S");
	   			    }
				}    
			}
			hash.put(CenTipoDireccionBean.C_IDTIPODIRECCION, v);
			Integer clave=null;
			
			Enumeration clavesDir = hTipoDirSel.keys();
		    while (clavesDir.hasMoreElements()) {
		    	clave = (Integer)clavesDir.nextElement();
		    	if (hTipoDir.containsKey(clave)){
					hTipoDir.put(clave,"S");
				}
		    }
			
			request.setAttribute("TipoDirecciones",hTipoDir);
			
			String fax = "false", mail = "false", correo  = "false", sms  = "false";
			String preferente = UtilidadesHash.getString(hash, CenDireccionesBean.C_PREFERENTE);
			if (preferente.indexOf("E") >= 0) mail   = "true";
			if (preferente.indexOf("F") >= 0) fax    = "true";
			if (preferente.indexOf("C") >= 0) correo = "true";
			if (preferente.indexOf("S") >= 0) sms    = "true";
			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);
			request.setAttribute("preferenteFax", fax);
			request.setAttribute("preferenteCorreo", correo);
			request.setAttribute("preferenteMail", mail);
			request.setAttribute("preferenteSms", sms);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, null);
		}
		return modo;
	} //ver()
	
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
	protected String nuevo (ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response)
			throws SIGAException
	{
		String modo = "nuevo";
 		
		try
		{
 			DireccionesForm miForm = (DireccionesForm) formulario;
			request.setAttribute("modoConsulta", modo);			
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("idPersona", miForm.getIDPersona());
			request.setAttribute("idInstitucion", miForm.getIDInstitucion());
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (this.getUserBean(request));
			Vector vTipos = new Vector();
			vTipos=cenTipoDirAdm.select("");
			Hashtable hTipoDir=new Hashtable();
			if ( (vTipos != null) && (vTipos.size() > 0) ) {
   				for (int i = 1; i <= vTipos.size(); i++) {
   					CenTipoDireccionBean tipoDir = (CenTipoDireccionBean) vTipos.get(i-1);
   					hTipoDir.put(tipoDir.getIdTipoDireccion(),"N");
   				}
   			}   
			request.setAttribute("vTipos",vTipos);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, null);
		}
		return modo;
	} //nuevo()
	
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
		String idDireccionesCensoWeb="";
		
		try
		{
			
			
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(this.getUserBean (request));			
			CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();
			//Recordar que ya existe un Trigger en BD que no permite insertar 
			//  mas de una direccion de correo por colegiado
			
			//obteniendo datos del formulario
			DireccionesForm miForm = (DireccionesForm) formulario;
			Long idPersona = miForm.getIDPersona ();
			String idDireccionesPreferentes="";
			Integer idInstitucionPersona = miForm.getIDInstitucion ();
			String preferente = this.campoPreferenteBooleanToString (miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo(), miForm.getPreferenteFax (), miForm.getPreferenteSms ());
			String preferenteModif=this.campoPreferenteBooleanToStringSeparados(miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), miForm.getPreferenteFax (), miForm.getPreferenteSms ());
			Long idDireccion = miForm.getIdDireccion ();
			
			//obteniendo adm de BD de direcciones
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean (request));
			CenDireccionesBean direccionesBean = new CenDireccionesBean();
		
			
			//comprobando que no existen dos direcciones con igual campo preferente
			/*if (! direccionesAdm.comprobarPreferenteDirecciones (idPersona.toString (), 
					idInstitucionPersona.toString (), preferente, idDireccion, request))
				throw new SIGAException (direccionesAdm.getError ());*/
			
			
			t = this.getUserBean (request).getTransactionPesada();
			t.begin ();
			
			//digito de control, para saber si viene de la jsp de  preguntapreferente.jsp o preguntacensoweb.jsp
			String control=request.getParameter("control");
			
			if (request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")){
				idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
			}else {
			//comprobando que no existen dos direcciones con igual campo preferente
			 idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString (), 
					idInstitucionPersona.toString (), preferente, idDireccion, request);
			  if  (!idDireccionesPreferentes.equals("")){
				request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
				request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
				request.setAttribute("control", "0");
				request.setAttribute("modo", "insertar");
				t.rollback();
				 return "preguntaCambioPreferencia";
			  } 	
			}
			
			//comprobando que el cliente no tenga ya una direccion de tipo guardia
			// si es asi no se permite anyadir la direccion
			String tiposDir = "";
			if (request.getParameter ("idTipoDireccionNew") != null && 
					!request.getParameter ("idTipoDireccionNew").equals (""))
			{
				int posUltimaComa = request.getParameter ("idTipoDireccionNew").lastIndexOf (",");
				if (posUltimaComa > -1)
					tiposDir = (String) request.getParameter ("idTipoDireccionNew").substring (0, posUltimaComa);
			}
			
			String tipos[] = tiposDir.split (",");
			
				String tiposdireciones="";
			for (int i=0; i < tipos.length; i++){
				tiposdireciones+=tipos[i];				
			}
			if (request.getParameter("modificarDireccionesCensoWeb")!=null && request.getParameter("modificarDireccionesCensoWeb").equals("1")){
				idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");
			}else {		
				idDireccionesCensoWeb=direccionesAdm.obtenerTipodireccionCensoWeb(idPersona.toString (), 
					idInstitucionPersona.toString (), tiposdireciones, idDireccion, request);			  
				if  (!idDireccionesCensoWeb.equals("")){			
					request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
					request.setAttribute("control", "1");
					request.setAttribute("modo", "insertar");	
					request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
					t.rollback();
				return "preguntaCambioTipoDireccion";
			  }
			}			
			
		
			RowsContainer rc3 = new RowsContainer(); 
			Hashtable result= new Hashtable();
			
			for (int i=0; i < tipos.length; i++)
			{
				String tipo=tipos[i].toString();
				
				if(new Integer(tipos[i]).intValue() == ClsConstants.TIPO_DIRECCION_GUARDIA)
				{
					String sql = direccionesAdm.comprobarTipoDireccion(tipo, miForm.getIDInstitucion().toString(), miForm.getIDPersona().toString());						
					RowsContainer rc1 = new RowsContainer ();
					if (rc1.query (sql))
						if (rc1.size () >= 1){
							t.rollback();
							return exito("messages.inserted.error.ExisteYaGuardia", request);
						}	
				}else if (new Integer (tipos[i]).intValue () == ClsConstants.TIPO_DIRECCION_CORREO){//dirección de tipo censoweb					
					String sql1 = direccionesAdm.comprobarTipoDireccion(tipo, miForm.getIDInstitucion().toString(), miForm.getIDPersona().toString());
					cambiodireccioncensoweb (miForm,i,sql1, tipo, idDireccionesCensoWeb, request);
					if ((request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")) || (request.getParameter("modificarDireccionesCensoWeb")!=null && request.getParameter("modificarDireccionesCensoWeb").equals("1"))){
						
						if ( request.getParameter("control").equals("1")){
						if (!preferenteModif.equals("")){
								direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);	
							}
						}else cambiodireccioncensoweb (miForm,i,sql1, tipo, idDireccionesCensoWeb, request);
					}
					
				}else { if (!preferenteModif.equals("")){
							 if (!idDireccionesCensoWeb.equals("") &&(!idDireccionesPreferentes.equals(""))){
								 direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
							 }
							 if (control.equals("0")){
								  direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
								 
							 }
							 }
						}
						
					
				
		} //for
			
			//estableciendo los datos de la direccion (desde el formulario 
			// de la interfaz principalmente) para la posterior insercion
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			beanDir.setCodigoPostal (miForm.getCodigoPostal ());
			beanDir.setCorreoElectronico (miForm.getCorreoElectronico ());
			beanDir.setDomicilio (miForm.getDomicilio ());
			beanDir.setFax1 (miForm.getFax1 ());
			beanDir.setFax2 (miForm.getFax2 ());
			beanDir.setFechaBaja (miForm.getFechaBaja ());
			beanDir.setIdInstitucion (miForm.getIDInstitucion ());
			beanDir.setIdPais (miForm.getPais ());
			if (miForm.getPais ().equals ("")) {
				miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
			}
			if (miForm.getPais().equals (ClsConstants.ID_PAIS_ESPANA)) {
				beanDir.setIdPoblacion (miForm.getPoblacion ());
				beanDir.setIdProvincia (miForm.getProvincia ());
				beanDir.setPoblacionExtranjera ("");
			} else {
				beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
				beanDir.setIdPoblacion ("");
				beanDir.setIdProvincia ("");
			}
			beanDir.setIdPersona (miForm.getIDPersona ());
			beanDir.setMovil (miForm.getMovil ());
			beanDir.setPaginaweb (miForm.getPaginaWeb ());
			beanDir.setPreferente (this.campoPreferenteBooleanToString
					(miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), 
					miForm.getPreferenteFax (),
					miForm.getPreferenteSms ()));
			beanDir.setTelefono1 (miForm.getTelefono1 ());
			beanDir.setTelefono2 (miForm.getTelefono2 ());
			
			//estableciendo los datos del tipo de direccion
			int numTipos = tipos.length;
			CenDireccionTipoDireccionBean vBeanTipoDir [] = new CenDireccionTipoDireccionBean [numTipos];
			for (int i=0; i < numTipos; i++) {
				CenDireccionTipoDireccionBean b = new CenDireccionTipoDireccionBean ();
				b.setIdTipoDireccion (new Integer (tipos[i]));
				vBeanTipoDir[i] = b;
			}
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo (miForm.getMotivo ());
			
			//iniciando la transaccion para modificar en la BD
			
			
			//insertando la direccion
			if (! direccionesAdm.insertarConHistorico (beanDir, vBeanTipoDir, beanHis, this.getLenguaje (request)))
				throw new SIGAException (direccionesAdm.getError());
			
			
			//insertando en la cola de modificacion de datos para Consejos
			CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (this.getUserBean (request));
			if (! colaAdm.insertarCambioEnCola (ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION, 
					beanDir.getIdInstitucion (), beanDir.getIdPersona (), beanDir.getIdDireccion ()))
				throw new SIGAException (colaAdm.getError ());
			
			//confirmando las modificaciones de BD
			t.commit();
			
			if(miForm.getVieneDe()!=null && miForm.getVieneDe().equals("1")){
				miForm.setIdDireccion(beanDir.getIdDireccion());
				return enviarNuevaDireccion(mapping, miForm, request, response);
			}
			rc = exitoModal("messages.inserted.success", request);
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
		String idDireccionesCensoWeb="";
		

		try
		{
			//Recordar que ya existe un Trigger en BD que no permite insertar 
			//  mas de una direccion de correo por colegiado
			
			//obteniendo datos del formulario
			DireccionesForm miForm = (DireccionesForm) formulario;
			//iniciando la transaccion para modificaciones en BD
			t = this.getUserBean (request).getTransactionPesada();
			t.begin ();
			
			Long idPersona = miForm.getIDPersona ();
			Integer idInstitucionPersona = miForm.getIDInstitucion ();
			String preferente = this.campoPreferenteBooleanToString (miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), miForm.getPreferenteFax (), miForm.getPreferenteSms ());
			String preferenteModif=this.campoPreferenteBooleanToStringSeparados(miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), miForm.getPreferenteFax (), miForm.getPreferenteSms ());
			Long idDireccion = miForm.getIdDireccion ();
			Long idDireccionAntes=miForm.getIdDireccion();
			String idDireccionesPreferentes="";
			
			
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(this.getUserBean (request));			
			CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();
			
			
			//obteniendo control de bd de direcciones
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean (request));
			CenDireccionesBean direccionesBean = new CenDireccionesBean();
			//digito de control para saber si viene de la pagina de preferentes o de censoweb ya que son dos preguntas distintas.
		    String control=request.getParameter("control");
			if (request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")){
				idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
			}else {			
				//comprobando que no existen dos direcciones con igual campo preferente
				idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString (), 
					idInstitucionPersona.toString (), preferente, idDireccion, request);
			   if  (!idDireccionesPreferentes.equals("")){	
				  
				request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
				request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
				request.setAttribute("control", "0");
				request.setAttribute("modo", "modificarDireccion");
				t.rollback();
				 return "preguntaCambioPreferencia";
			     }  
			
			 
			}
			//comprobando que el cliente no tenga ya una direccion de tipo guardia
			// si es asi no se permite anyadir la direccion
			
			//esta es la parte que comprobra si tiene una dirección de tipo censoweb.
			String tiposDir = "";
				if (request.getParameter ("idTipoDireccionNew") != null && 
					!request.getParameter ("idTipoDireccionNew").equals (""))
					{
					int posUltimaComa = request.getParameter ("idTipoDireccionNew").lastIndexOf (",");				
					if (posUltimaComa > -1)
					tiposDir = (String) request.getParameter ("idTipoDireccionNew").substring (0, posUltimaComa);				   
					}
					String tipos[] = tiposDir.split (",");
			
					String tiposdireciones="";
					for (int i=0; i < tipos.length; i++){
						tiposdireciones+=tipos[i];				
					}					
					
			
					if ((request.getParameter("modificarDireccionesCensoWeb")!=null && request.getParameter("modificarDireccionesCensoWeb").equals("1")) || (request.getParameter("modificarPreferencias")==null && request.getParameter("modificarPreferencias").equals("0"))){
						idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");				
					}else {		
						idDireccionesCensoWeb=direccionesAdm.obtenerTipodireccionCensoWeb(idPersona.toString (), 
								idInstitucionPersona.toString (), tiposdireciones, idDireccion, request);			  
						if  (!idDireccionesCensoWeb.equals("")){			
							request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
							request.setAttribute("control", "1");
							request.setAttribute("modo", "modificarDireccion");	
							request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
							t.rollback();
							return "preguntaCambioTipoDireccion";
						}
					}
						
					
				//fin de comprobación de censoweb
			
			RowsContainer rc2 = new RowsContainer(); 
			RowsContainer rc3 = new RowsContainer(); 
			Hashtable result= new Hashtable();
			int j=0;
			for (int i=0; i < tipos.length; i++)				
			{	
				String tipo=tipos[i].toString();
				
				if (new Integer (tipos[i]).intValue () == ClsConstants.TIPO_DIRECCION_GUARDIA)
				{
					
					String sql = direccionesAdm.comprobarTipoDireccion(tipo, miForm.getIDInstitucion().toString(), miForm.getIDPersona().toString());					
					if (rc2.query(sql)) {
						if (rc2.size()>=1) {
							Row idDireccion1 = (Row) rc2.get (j);
							int idDireccionAhora = Integer.parseInt ((String) idDireccion1.getValue
									(CenDireccionTipoDireccionBean.C_IDDIRECCION));
							j++;
							if(idDireccionAntes.longValue () != new Integer (idDireccionAhora).longValue ()){
								t.rollback();
								return exito("messages.inserted.error.ExisteYaGuardia", request);
							}	
						}
					}  
				}else if (new Integer (tipos[i]).intValue () == ClsConstants.TIPO_DIRECCION_CORREO){						
					String sql1 = direccionesAdm.comprobarTipoDireccion(tipo, miForm.getIDInstitucion().toString(), miForm.getIDPersona().toString());
					cambiodireccioncensoweb (miForm,i,sql1, tipo, idDireccionesCensoWeb, request);
					if ((request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")) || (request.getParameter("modificarDireccionesCensoWeb")!=null && request.getParameter("modificarDireccionesCensoWeb").equals("1"))){
						
						if ( request.getParameter("control").equals("0")){
						if (!preferenteModif.equals("")){
								direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);	
							}
						}else cambiodireccioncensoweb (miForm,i,sql1, tipo, idDireccionesCensoWeb, request);
					}
				}else { if (!preferenteModif.equals("")){
							 if (!idDireccionesCensoWeb.equals("") &&(!idDireccionesPreferentes.equals(""))){
								 direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
							 }
							 if (control.equals("0")){
								  direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
								 
							 }
							 }
						}
						
					
			
				 
			} //for
			
			//estableciendo los datos de la direccion (desde el formulario 
			// de la interfaz principalmente) para la posterior insercion
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			beanDir.setCodigoPostal (miForm.getCodigoPostal ());
			beanDir.setCorreoElectronico (miForm.getCorreoElectronico ());
			beanDir.setDomicilio (miForm.getDomicilio ());
			beanDir.setFax1 (miForm.getFax1 ());
			beanDir.setFax2 (miForm.getFax2 ());
			beanDir.setFechaBaja (miForm.getFechaBaja ());
			beanDir.setIdPais (miForm.getPais ());
			if (miForm.getPais ().equals ("")) {
				miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
			}
			if (miForm.getPais ().equals (ClsConstants.ID_PAIS_ESPANA)) {
				beanDir.setIdPoblacion (miForm.getPoblacion ());
				beanDir.setIdProvincia (miForm.getProvincia ());
				beanDir.setPoblacionExtranjera ("");
			} else {
				beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
				beanDir.setIdPoblacion ("");
				beanDir.setIdProvincia ("");
			}
			beanDir.setMovil (miForm.getMovil ());
			beanDir.setPaginaweb (miForm.getPaginaWeb ());
			beanDir.setPreferente (this.campoPreferenteBooleanToString 
					(miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), 
					miForm.getPreferenteFax (), 
					miForm.getPreferenteSms ()));
			beanDir.setTelefono1 (miForm.getTelefono1 ());
			beanDir.setTelefono2 (miForm.getTelefono2 ());
			beanDir.setIdPersona (miForm.getIDPersona ());
			beanDir.setIdInstitucion (miForm.getIDInstitucion ());
			beanDir.setIdDireccion (miForm.getIdDireccion ());
			beanDir.setOriginalHash ((Hashtable) request.getSession ().getAttribute ("DATABACKUP"));
			
			//estableciendo los datos del tipo de direccion
			int numTipos = tipos.length;
			CenDireccionTipoDireccionBean vBeanTipoDir [] = new CenDireccionTipoDireccionBean [numTipos];
			for (int i = 0; i < numTipos; i++) {
				CenDireccionTipoDireccionBean b = new CenDireccionTipoDireccionBean ();
				b.setIdTipoDireccion (new Integer (tipos[i]));				
				vBeanTipoDir[i] = b;
			}
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo (miForm.getMotivo ());
			
			
			
			//insertando la direccion en BD
			if (!direccionesAdm.updateConHistorico 
					(beanDir, vBeanTipoDir, beanHis, this.getLenguaje (request)))
				throw new SIGAException (direccionesAdm.getError ());
			
			//insertando en la cola de modificacion de datos para Consejos
			CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (this.getUserBean (request));
			if (! colaAdm.insertarCambioEnCola 
					(ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION,
					beanDir.getIdInstitucion (), beanDir.getIdPersona (), beanDir.getIdDireccion ()))
				throw new SIGAException (colaAdm.getError ());
			
			//confirmando los cambios en BD
			t.commit();
			
			//saliendo
			rc = exitoModal("messages.updated.success", request);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, t);
		}
		return rc; 
	} //modificar()

	private String comprobarTipoDireccion(String tipo, String string,
			String string2) {
		// TODO Auto-generated method stub
		return null;
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
	protected String borrar (ActionMapping mapping, 
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
			//Recordar que un trigger hace comprobaciones para no borrar 
			// direccion de Correo ni de despacho o guia cuando sea necesario
			
			//obteniendo los datos del formulatio
			DireccionesForm miForm = (DireccionesForm) formulario;
			Long idPersona = miForm.getIDPersona ();
			Long idDireccion;
			// Se comprueba en el formulario porque se puede venir de sitios distintos
			if (miForm.getIdDireccion()!=null)
				idDireccion = miForm.getIdDireccion();
			else
				idDireccion = new Long ((String) miForm.getDatosTablaOcultos (0).get (0));
			Integer idInstitucion = miForm.getIDInstitucion ();
			
			//estableciendo los datos clave para borrar direccion
			CenDireccionesAdm admDir = new CenDireccionesAdm (this.getUserBean (request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
			 CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm(this.getUserBean(request));
			
			//iniciando la transaccion para modificar en BD
			t = this.getUserBean (request).getTransaction ();
			t.begin();
 			
			//borrando la direccion en BD
			if (!admDir.deleteConHistorico (claves, beanHis, this.getLenguaje (request), true))
				throw new SIGAException (admDir.getError ());
			
			//insertando en la cola de modificacion de datos para Consejos
			if (! colaAdm.insertarCambioEnCola 
					(ClsConstants.COLA_CAMBIO_LETRADO_BORRADO_DIRECCION, 
					idInstitucion, idPersona, idDireccion))
				throw new SIGAException (colaAdm.getError ());
			
			//confirmando los cambios en BD
			t.commit();
			
			//saliendo
			rc = exitoRefresco("messages.deleted.success", request);
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
			throws SIGAException
	{
		String modo = "";
		UserTransaction t = null;
		
		try
		{	
			t = this.getUserBean(request).getTransaction();
			
			DireccionesForm form = (DireccionesForm) formulario;		
			CenSoliModiDireccionesAdm adm = new CenSoliModiDireccionesAdm(this.getUserBean(request));
			CenDireccionesAdm admDir = new CenDireccionesAdm(this.getUserBean(request));
			//String idDireccionesPreferentes="";
			modo = "insertarModificacion";
			/*String preferenteModif=this.campoPreferenteBooleanToStringSeparados(form.getPreferenteMail (), 
					form.getPreferenteCorreo (), form.getPreferenteFax (), form.getPreferenteSms ());*/
			
			// compruebo que no existen dos direcciones que tengan igual
			// el campo preferente
			Long idPersona = form.getIDPersona();
			Integer idInstitucionPersona = form.getIDInstitucion();
			String preferente = this.campoPreferenteBooleanToString(form.getPreferenteMail(), form.getPreferenteCorreo(), form.getPreferenteFax(), form.getPreferenteSms ());
			Long idDireccion = form.getIdDireccion();
			if (!admDir.comprobarPreferenteDirecciones(idPersona.toString(),idInstitucionPersona.toString(),preferente,idDireccion,request)) {
				throw new SIGAException(admDir.getError());
			}
			
      /* CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean (request));
			
			if (request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")){
				idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
				adm.solicitarModificacionDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
			}else {
			//comprobando que no existen dos direcciones con igual campo preferente
			 idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString (), 
					idInstitucionPersona.toString (), preferente, idDireccion, request);
			  if  (!idDireccionesPreferentes.equals("")){
			
				request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
				request.setAttribute("modo", "insertarModificacion");
				t.rollback();
				 return "preguntaCambioPreferenciaSolicitud";
			  } 	
			}
			if (!admDir.comprobarDireccionTipoDireccion(idPersona.toString(),idInstitucionPersona.toString(),idDireccion.toString(),form)) {
				throw new SIGAException("messages.obligatorioPostal.error");
			}*/
			
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
			if (!bean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
				bean.setIdProvincia("");
				bean.setIdPoblacion("");
			}
			bean.setPreferente(this.campoPreferenteBooleanToString(form.getPreferenteMail(), 
					form.getPreferenteCorreo(), form.getPreferenteFax(), form.getPreferenteSms ()));
			bean.setIdEstadoSolic(new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
			bean.setFechaAlta("sysdate");
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
	
	
	private String campoPreferenteBooleanToStringSeparados (Boolean mail, 
			   Boolean correo, 
			   Boolean fax,
			   Boolean sms) throws SIGAException{
		String valor = "";
		
		try
		{
		if (mail.booleanValue())
		valor += ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO+"#";
		if (fax.booleanValue())
		valor += ClsConstants.TIPO_PREFERENTE_FAX+"#";
		if (correo.booleanValue())
		valor += ClsConstants.TIPO_PREFERENTE_CORREO+"#";
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
	protected String modificar (ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response)
	throws SIGAException, ClsExceptions{
		String rc = "";
		DireccionesForm miForm = (DireccionesForm) formulario;
		GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean (request));
		//String idInstitucion = miForm.getIDInstitucion().toString();
		
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
		String idDireccionesCensoWeb="";
		
		try
		{
			//Recordar que un trigger hace comprobaciones para no borrar 
			// direccion de Correo ni de despacho o guia cuando sea necesario
			
			//obteniendo los datos del formulatio
			DireccionesForm miForm = (DireccionesForm) formulario;
			Long idPersona = miForm.getIDPersona ();
			Long idDireccion;
			String idDireccionesPreferentes="";
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(this.getUserBean (request));			
			CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();
			 CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm(this.getUserBean(request));
			CenDireccionesBean direccionesBean = new CenDireccionesBean();
			//obteniendo datos del formulario
			Integer idInstitucionPersona = miForm.getIDInstitucion ();
			String preferente = this.campoPreferenteBooleanToString (miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo(), miForm.getPreferenteFax (), miForm.getPreferenteSms ());
			String preferenteModif=this.campoPreferenteBooleanToStringSeparados(miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), miForm.getPreferenteFax (), miForm.getPreferenteSms ());
			
			// Se comprueba en el formulario porque se puede venir de sitios distintos
			if (miForm.getIdDireccion()!=null)
				idDireccion = miForm.getIdDireccion();
			else
				idDireccion = new Long ((String) miForm.getDatosTablaOcultos (0).get (0));
			Integer idInstitucion = miForm.getIDInstitucion ();
			
			//estableciendo los datos clave para borrar direccion
			CenDireccionesAdm admDir = new CenDireccionesAdm (this.getUserBean (request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
			
			//iniciando la transaccion para modificar en BD
			t = this.getUserBean (request).getTransactionPesada();
			t.begin();
 			
			
			//obteniendo adm de BD de direcciones
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean (request));
			//digito de control para saber si viene de la pagina de preferentes o de censoweb ya que son dos preguntas distintas.
		    String control=request.getParameter("control");
			
			if (request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")){
				idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
			//	direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
			}else {
			//comprobando que no existen dos direcciones con igual campo preferente
			 idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString (), 
					idInstitucionPersona.toString (), preferente, idDireccion, request);
			  if  (!idDireccionesPreferentes.equals("")){
			    request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
				request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
				request.setAttribute("control", "0");
				request.setAttribute("modo", "guardarInsertarHistorico");
				t.rollback();
				return "preguntaCambioPreferencia";
			} 	
			}
			
			//borrando la direccion en BD
			if (!admDir.deleteConHistorico (claves, beanHis, this.getLenguaje (request), false))
				throw new SIGAException (admDir.getError ());
			
			//insertando en la cola de modificacion de datos para Consejos
			if (! colaAdm.insertarCambioEnCola 
					(ClsConstants.COLA_CAMBIO_LETRADO_BORRADO_DIRECCION, 
					idInstitucion, idPersona, idDireccion))
				throw new SIGAException (colaAdm.getError ());
			
			//Recordar que ya existe un Trigger en BD que no permite insertar 
			//  mas de una direccion de correo por colegiado
			
			//comprobando que el cliente no tenga ya una direccion de tipo guardia
			// si es asi no se permite anyadir la direccion
			String tiposDir = "";
			if (request.getParameter ("idTipoDireccionNew") != null && 
					!request.getParameter ("idTipoDireccionNew").equals (""))
			{
				int posUltimaComa = request.getParameter ("idTipoDireccionNew").lastIndexOf (",");
				if (posUltimaComa > -1)
					tiposDir = (String) request.getParameter ("idTipoDireccionNew").substring (0, posUltimaComa);
			}
			
			String tipos[] = tiposDir.split (",");
			
			String tiposdireciones="";
			for (int i=0; i < tipos.length; i++){
				tiposdireciones+=tipos[i];				
			}			
		
			
			if (request.getParameter("modificarDireccionesCensoWeb")!=null && request.getParameter("modificarDireccionesCensoWeb").equals("1")){
				idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");				
			}else {		
				idDireccionesCensoWeb=direccionesAdm.obtenerTipodireccionCensoWeb(idPersona.toString (), 
					idInstitucionPersona.toString (), tiposdireciones, idDireccion, request);			  
				if  (!idDireccionesCensoWeb.equals("")){	
					
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("control", "1");
						request.setAttribute("modo", "guardarInsertarHistorico");	
						request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						t.rollback();
						return "preguntaCambioTipoDireccion";
			 }
			}			
			
			RowsContainer rc2 = new RowsContainer(); 
			RowsContainer rc3 = new RowsContainer(); 
			Hashtable result= new Hashtable();
			int j=0;
			for (int i=0; i < tipos.length; i++)
			{
				 String tipo=tipos[i].toString();
				 
				if(new Integer(tipos[i]).intValue() == ClsConstants.TIPO_DIRECCION_GUARDIA)
				{
					String sql = direccionesAdm.comprobarTipoDireccion(tipo, miForm.getIDInstitucion().toString(), miForm.getIDPersona().toString());						
					RowsContainer rc1 = new RowsContainer ();
					if (rc1.query (sql))
						if (rc1.size () >= 1){
							t.rollback();
							return exito("messages.inserted.error.ExisteYaGuardia", request);
						}	
					}else if (new Integer (tipos[i]).intValue () == ClsConstants.TIPO_DIRECCION_CORREO){						
					String sql1 = direccionesAdm.comprobarTipoDireccion(tipo, miForm.getIDInstitucion().toString(), miForm.getIDPersona().toString());
					cambiodireccioncensoweb (miForm,i,sql1, tipo, idDireccionesCensoWeb, request);
					if ((request.getParameter("modificarPreferencias")!=null && request.getParameter("modificarPreferencias").equals("1")) || (request.getParameter("modificarDireccionesCensoWeb")!=null && request.getParameter("modificarDireccionesCensoWeb").equals("1"))){
						
						if ( request.getParameter("control").equals("0")){
						if (!preferenteModif.equals("")){
								direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);	
							}
						}else cambiodireccioncensoweb (miForm,i,sql1, tipo, idDireccionesCensoWeb, request);
					}
				}else { if (!preferenteModif.equals("")){
							 if (!idDireccionesCensoWeb.equals("") &&(!idDireccionesPreferentes.equals(""))){
								 direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
							 }
							 if (control.equals("0")){
								  direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,request);
								 
							 }
							 }
						}
						
			} //for
			
			//estableciendo los datos de la direccion (desde el formulario 
			// de la interfaz principalmente) para la posterior insercion
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			beanDir.setCodigoPostal (miForm.getCodigoPostal ());
			beanDir.setCorreoElectronico (miForm.getCorreoElectronico ());
			beanDir.setDomicilio (miForm.getDomicilio ());
			beanDir.setFax1 (miForm.getFax1 ());
			beanDir.setFax2 (miForm.getFax2 ());
			beanDir.setFechaBaja (miForm.getFechaBaja ());
			beanDir.setIdInstitucion (miForm.getIDInstitucion ());
			beanDir.setIdPais (miForm.getPais ());
			if (miForm.getPais ().equals ("")) {
				miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
			}
			if (miForm.getPais().equals (ClsConstants.ID_PAIS_ESPANA)) {
				beanDir.setIdPoblacion (miForm.getPoblacion ());
				beanDir.setIdProvincia (miForm.getProvincia ());
				beanDir.setPoblacionExtranjera ("");
			} else {
				beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
				beanDir.setIdPoblacion ("");
				beanDir.setIdProvincia ("");
			}
			beanDir.setIdPersona (miForm.getIDPersona ());
			beanDir.setMovil (miForm.getMovil ());
			beanDir.setPaginaweb (miForm.getPaginaWeb ());
			beanDir.setPreferente (this.campoPreferenteBooleanToString
					(miForm.getPreferenteMail (), 
					miForm.getPreferenteCorreo (), 
					miForm.getPreferenteFax (),
					miForm.getPreferenteSms ()));
			beanDir.setTelefono1 (miForm.getTelefono1 ());
			beanDir.setTelefono2 (miForm.getTelefono2 ());
			
			//estableciendo los datos del tipo de direccion
			int numTipos = tipos.length;
			CenDireccionTipoDireccionBean vBeanTipoDir [] = new CenDireccionTipoDireccionBean [numTipos];
			for (int i=0; i < numTipos; i++) {
				CenDireccionTipoDireccionBean b = new CenDireccionTipoDireccionBean ();
				b.setIdTipoDireccion (new Integer (tipos[i]));
				vBeanTipoDir[i] = b;
			}
			
			//estableciendo los datos del Historico
			beanHis.setMotivo (miForm.getMotivo ());
			
			//insertando la direccion
			if (! direccionesAdm.insertarConHistorico (beanDir, vBeanTipoDir, beanHis, this.getLenguaje (request)))
				throw new SIGAException (direccionesAdm.getError());
			
			
			//insertando en la cola de modificacion de datos para Consejos
			if (! colaAdm.insertarCambioEnCola (ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION, 
					beanDir.getIdInstitucion (), beanDir.getIdPersona (), beanDir.getIdDireccion ()))
				throw new SIGAException (colaAdm.getError ());
			
			//confirmando las modificaciones de BD
			t.commit();
			
			//saliendo
			rc = exitoModal("messages.updated.success", request);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, t);
		}
		return rc;
	} //borrar()
	protected String enviarNuevaDireccion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
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
	
	protected void cambiodireccioncensoweb (MasterForm formulario,int i,String sql,String tipo,String idDireccionesCensoWeb, HttpServletRequest request)	throws SIGAException
	{
		DireccionesForm miForm = (DireccionesForm) formulario;
		RowsContainer rc2 = new RowsContainer(); 
		RowsContainer rc3 = new RowsContainer(); 
		Hashtable result= new Hashtable();	
		
		CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(this.getUserBean (request));			
		CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();

		CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean (request));
		CenDireccionesBean direccionesBean = new CenDireccionesBean();
			
		try{
		if (rc3.query(sql)) {
						if (rc3.size()>=1) {
							
								int tamaño=rc3.size();
								Row r=(Row)rc3.get(i);
								result.putAll(r.getRow());												
									
								//Borramos todos los tipos de esa direccion
									String[] idDir;
									idDir=idDireccionesCensoWeb.split("@");
									
									boolean error = false;
				
									  if (!idDireccionesCensoWeb.equals("")){
										CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm(this.getUserBean(request));
										for (int m=0; m<idDir.length; m++){
										//modificar dando de baja logica.
										  String whereSancion =" where CEN_DIRECCION_TIPODIRECCION.idpersona = "+ miForm.getIDPersona().toString();
								          whereSancion +=" AND CEN_DIRECCION_TIPODIRECCION.idinstitucion ="+miForm.getIDInstitucion().toString();
								          whereSancion +=" AND CEN_DIRECCION_TIPODIRECCION.iddireccion ="+idDir[m];
								          
								          Vector direccionestipos= tipoDirAdm.select(whereSancion);               
								          int numerodirecciones=direccionestipos.size();
								          
								        
								          if (numerodirecciones==1){
								        	  
								        	  String whereSancion1 =" where CEN_DIRECCIONES.idpersona = "+ miForm.getIDPersona().toString();
								        	  whereSancion1 +=" AND CEN_DIRECCIONES.idinstitucion ="+miForm.getIDInstitucion().toString();
								        	  whereSancion1 +=" AND CEN_DIRECCIONES.iddireccion ="+idDir[m];
									
								
								        	  Vector direcciones= direccionesAdm.select(whereSancion1);               
								        	  int nSanciones=direcciones.size();
								        	  for(int l=0;l<direcciones.size();l++)
								        	  	{  
								        	  
								        		  direccionesBean = (CenDireccionesBean)direcciones.elementAt(l);			
								        		  direccionesBean.setFechaBaja("SYSDATE");      	 
								          			String datosCambiar[] = new String[1];
								          			datosCambiar[0]=direccionesBean.C_FECHABAJA;								          		
								          			if(!direccionesAdm.updateDirect(direccionesBean,direccionesAdm.getClavesBean(),datosCambiar))
								          				throw new ClsExceptions(direccionesAdm.getError());
								        	  		}
								        	  
								          	}else{							
								          		  /***
								          			Eliminar el idtipodireccion =3 de la iddirección ya que sera la dirección de tipo censoweb 
								          			que queremos que no sea esta sino la actual en la que estamos
								          		 ***/ 
								          		Hashtable clave = new Hashtable();
								          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDINSTITUCION, miForm.getIDInstitucion().toString());
								          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDPERSONA, miForm.getIDPersona().toString());
								          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDir[m]);
								          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDTIPODIRECCION, tipo);
									
								          		Vector v = admTipoDir.selectForUpdate(clave);
								          		for (int n = 0; n < v.size() && (!error); n++) {
								          			CenDireccionTipoDireccionBean b = (CenDireccionTipoDireccionBean) v.get(n);
								          			if (!admTipoDir.delete(b)) {
								          				error = true;
											     }
								          		}
								        }
									}
								        	  
							  }
								
						}
					}
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e, null);
		}
	}
		
}
