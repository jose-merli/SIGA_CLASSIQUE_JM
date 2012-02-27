
package com.siga.censo.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;
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
 * @author Carlos Ruano
 * @since 13/01/2012
 */
public class Direccion {
	
	private CenDireccionesBean beanDireccion = null;
	private boolean confirmacionPregunta;
	private String tipoPregunta = "";
	
	public Direccion (){
		
	}

	public static Direccion insertar (CenDireccionesBean beanDir, String tiposDir, String motivoHis, HttpServletRequest request,UsrBean usr) throws SIGAException {
		
		// Variables generales
		Direccion dir = new Direccion();		
		String idDireccionesCensoWeb="";
		
		// Variables Adm
		CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(usr);			
		CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();
		CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (usr);
		
		// Datos para las preguntas al usuario
		String modificarPreferencias = null;
		String modificarDireccionesCensoWeb = null;
		String control = null;
		
		try {			
			
			// Obteniendo datos del formulario
			Long idPersona = beanDir.getIdPersona ();
			String idDireccionesPreferentes="";
			Integer idInstitucionPersona = beanDir.getIdInstitucion ();
			Long idDireccion = beanDir.getIdDireccion ();
			
			// Informacion sobre tipos direccion
			String tipos[] = tiposDir.split (",");
			
			//Solo se utiliza el parametro request cuando se necesita confirmacion por parte del usuario
			if(request != null){
				String preferente = beanDir.getPreferente();
				String tiposdireciones="";		
				
				for (int i=0; i < tipos.length; i++){
					tiposdireciones+=tipos[i];				
				}				
				
				if (request.getParameter("modificarPreferencias")!=null){
					modificarPreferencias = request.getParameter("modificarPreferencias");
				}
				if (request.getParameter("modificarDireccionesCensoWeb")!=null){
					modificarDireccionesCensoWeb = request.getParameter("modificarDireccionesCensoWeb");
				} // fin datos preguntas
				if (request.getParameter("control")!=null){
					control = request.getParameter("control");
				}
				
				if (modificarPreferencias!=null && modificarPreferencias.equals("1")){
					idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
				} else {
					//comprobando que no existen dos direcciones con igual campo preferente
					idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString(), idInstitucionPersona.toString (), preferente, idDireccion, request);
					if(!idDireccionesPreferentes.equals("")){
						request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("control", "0");
						dir.setConfirmacionPregunta(true);
						dir.setTipoPregunta("preguntaCambioPreferencia");
						return dir;
					} 	
				}
				
				//comprobando que el cliente no tenga ya una direccion de tipo guardia si es asi no se permite anyadir la direccion
				if (modificarDireccionesCensoWeb!=null && modificarDireccionesCensoWeb.equals("1")){
					idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");
				}else {		
					idDireccionesCensoWeb=direccionesAdm.obtenerTipodireccionCensoWeb(idPersona.toString(),idInstitucionPersona.toString (), tiposdireciones, idDireccion, request);			  
					if  (!idDireccionesCensoWeb.equals("")){			
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("control", "1");
						request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						dir.setConfirmacionPregunta(true);
						dir.setTipoPregunta("preguntaCambioTipoDireccion");
						return dir;
					}
				}
			}
			
			// Comprobar tipo direccion
			comprobarTipoDireccion(tipos, beanDir, direccionesAdm, dir, control, idDireccionesCensoWeb, idPersona, idInstitucionPersona, idDireccionesPreferentes, tipoDirAdm, modificarPreferencias, modificarDireccionesCensoWeb);
			
			//estableciendo los datos del tipo de direccion
			CenDireccionTipoDireccionBean vBeanTipoDir [] = establecerTipoDireccion(tipos);
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo (motivoHis);
						
			//insertando la direccion
			if (! direccionesAdm.insertarConHistorico (beanDir, vBeanTipoDir, beanHis, usr.getLanguage()))
				throw new SIGAException (direccionesAdm.getError());
			
			//insertando en la cola de modificacion de datos para Consejos
			insertarModificacionConsejo(beanDir,usr, ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION);
		
		}catch (SIGAException e) {
			throw e;
			
		} catch(Exception e){
			throw new SIGAException ("messages.general.error");
		}
		
		return dir; 
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
	public static Direccion actualizar (CenDireccionesBean beanDir, String tiposDir, String motivoHis,HttpServletRequest request,UsrBean usr) throws SIGAException {
		
		//Variables generales
		Direccion dir = new Direccion();	
		String idDireccionesCensoWeb="";

		try	{
			
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (usr);
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(usr);			
			CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();
			CenDireccionTipoDireccionBean vBeanTipoDir [] = null;
			
			Long idPersona = beanDir.getIdPersona ();
			Long idDireccionAntes=beanDir.getIdDireccion ();
			Integer idInstitucionPersona = beanDir.getIdInstitucion ();
			Long idDireccion = beanDir.getIdDireccion ();
			
			String idDireccionesPreferentes="";
			
			//Informacion sobre tipos direccion
			String tipos[] = tiposDir.split (",");	
			
			// Datos para las pregunats al usuario
			String modificarPreferencias = null;
			String modificarDireccionesCensoWeb = null;
			String control = null;
			
			//Solo se utiliza el parametro reques cuando se necesita confirmacion por parte del usuario
			if(request != null){
				String preferente = beanDir.getPreferente();
				String tiposdireciones="";		
				
				for (int i=0; i < tipos.length; i++){
					tiposdireciones+=tipos[i];				
				}
				
				if (request.getParameter("modificarPreferencias")!=null){
					modificarPreferencias = request.getParameter("modificarPreferencias");
				}
				if (request.getParameter("modificarDireccionesCensoWeb")!=null){
					modificarDireccionesCensoWeb = request.getParameter("modificarDireccionesCensoWeb");
				} // fin datos preguntas
				if (request.getParameter("control")!=null){
					control = request.getParameter("control");
				}
				
				if (modificarPreferencias!=null && modificarPreferencias.equals("1")){
					idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
				}else {			
					//comprobando que no existen dos direcciones con igual campo preferente
					idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString (),idInstitucionPersona.toString (), preferente, idDireccion, request);
				    if (!idDireccionesPreferentes.equals("")){
					    request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("control", "0");
						dir.setConfirmacionPregunta(true);
						dir.setTipoPregunta("preguntaCambioPreferencia");
						return dir;
				     }  
				 
				}
				//comprobando que el cliente no tenga ya una direccion de tipo guardia si es asi no se permite anyadir la direccion
				
				//esta es la parte que comprobra si tiene una dirección de tipo censoweb.
		
				if ((modificarDireccionesCensoWeb!=null && modificarDireccionesCensoWeb.equals("1")) || (modificarPreferencias==null && modificarPreferencias.equals("0"))){
					idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");				
				
				}else {		
					idDireccionesCensoWeb=direccionesAdm.obtenerTipodireccionCensoWeb(idPersona.toString (),idInstitucionPersona.toString (), tiposdireciones, idDireccion, request);			  
					if  (!idDireccionesCensoWeb.equals("")){			
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("control", "1");
						request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						dir.setConfirmacionPregunta(true);
						dir.setTipoPregunta("preguntaCambioTipoDireccion");
						return dir;
					}
				}
			}
						
			if(!tiposDir.equals("")){
				// Comprobar tipo direccion
				comprobarTipoDireccion(tipos, beanDir, direccionesAdm, dir, control, idDireccionesCensoWeb, idPersona, idInstitucionPersona, idDireccionesPreferentes, tipoDirAdm, modificarPreferencias, modificarDireccionesCensoWeb);

				//estableciendo los datos del tipo de direccion
				vBeanTipoDir = establecerTipoDireccion(tipos);
			}
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo (motivoHis);
			
			//Actualizando la direccion
			if (!direccionesAdm.updateConHistorico (beanDir, vBeanTipoDir, beanHis, usr.getLanguage()))
				throw new SIGAException (direccionesAdm.getError ());
			
			//insertando en la cola de modificacion de datos para Consejos
			insertarModificacionConsejo(beanDir,usr, ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION);
			
		}catch (SIGAException e) {
			throw e;
			
		} catch(Exception e){
			throw new SIGAException ("messages.general.error");
		}
		
		return dir; 

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
	protected boolean borrar (CenDireccionesBean beanDir,HttpServletRequest request,UsrBean usr) throws SIGAException {
		//Variables generales
		String rc = "";
		
		try	{
			
			Long idPersona = beanDir.getIdPersona ();
			Integer idInstitucion = beanDir.getIdInstitucion ();
			Long idDireccion = beanDir.getIdDireccion();
			
			CenDireccionesAdm admDir = new CenDireccionesAdm (usr);
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
			
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
 			
			//borrando la direccion en BD
			if (!admDir.deleteConHistorico (claves, beanHis, usr.getLanguage(), true))
				throw new SIGAException (admDir.getError ());
			
			insertarModificacionConsejo(beanDir, usr, ClsConstants.COLA_CAMBIO_LETRADO_BORRADO_DIRECCION);

		}catch (SIGAException e) {
			throw e;
			
		} catch(Exception e){
			throw new SIGAException ("messages.general.error");
		}
		
		return true;
	} //borrar()
	

	private static void comprobarTipoDireccion(String [] tipos,CenDireccionesBean beanDir, CenDireccionesAdm direccionesAdm, Direccion dir, String control, String idDireccionesCensoWeb, Long idPersona, Integer idInstitucionPersona, String idDireccionesPreferentes, CenDireccionTipoDireccionAdm tipoDirAdm, String modificarPreferencias, String modificarDireccionesCensoWeb) throws SIGAException, ClsExceptions, IllegalStateException, SecurityException, SystemException{
	
		RowsContainer rc3 = new RowsContainer(); 
		Hashtable result= new Hashtable();
		String preferenteModif = "";
		int j=0;
		if(beanDir.getPreferente()!=null){
			preferenteModif = parsearPreferenteModificado(beanDir.getPreferente());
		}
		
		for (int i=0; i < tipos.length; i++){
			String tipo=tipos[i].toString();
			
			if(new Integer(tipos[i]).intValue() == ClsConstants.TIPO_DIRECCION_GUARDIA)	{
				String sql = direccionesAdm.comprobarTipoDireccion(tipo, beanDir.getIdInstitucion().toString(), beanDir.getIdPersona().toString());						
				RowsContainer rc1 = new RowsContainer ();
				if (rc1.query (sql)){
					if (rc1.size () >= 1){
						Row row = (Row) rc1.get (j);
						int idDireccionAhora = Integer.parseInt ((String) row.getValue(CenDireccionTipoDireccionBean.C_IDDIRECCION));
						j++;
						if(beanDir.getIdDireccion()!= null){
							if(beanDir.getIdDireccion() != new Integer (idDireccionAhora).longValue ())
								throw new SIGAException ("messages.inserted.error.ExisteYaGuardia");
						}else{
							throw new SIGAException ("messages.inserted.error.ExisteYaGuardia");
						}
					}	
					
					if (!idDireccionesPreferentes.equals("")) {
						direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif);
					}
				}

			}else if (new Integer (tipos[i]).intValue () == ClsConstants.TIPO_DIRECCION_CENSOWEB){//dirección de tipo censoweb					
				String sql1 = direccionesAdm.comprobarTipoDireccion(tipo, beanDir.getIdInstitucion().toString(), beanDir.getIdPersona().toString());
				cambiodireccioncensoweb (beanDir,i,sql1, tipo, idDireccionesCensoWeb, tipoDirAdm, direccionesAdm);
				if ((modificarPreferencias!=null && modificarPreferencias.equals("1")) || (modificarDireccionesCensoWeb!=null && modificarDireccionesCensoWeb.equals("1"))){
					
					if (control != null && control.equals("0")){
						if (!preferenteModif.equals("")){
							direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif);	
						}
					}else{ 
						cambiodireccioncensoweb (beanDir,i,sql1, tipo, idDireccionesCensoWeb, tipoDirAdm, direccionesAdm);
					}
				}
			} else if (new Integer(tipos[i]).intValue() == ClsConstants.TIPO_DIRECCION_FACTURACION) {
				// jbd 	// La solucion de Carlos estaba bien para inserciones, pero no para actualizaciones de la direccion de facturacion, decia que ya existia
						// Lo que hago es pasar la direccion actual si es una edicion o null si es nueva y tieneDireccionFacturacion hace el resto
				boolean tieneDirFacturacion = tipoDirAdm.tieneDireccionFacturacion(beanDir.getIdInstitucion().toString(), beanDir.getIdPersona().toString(), beanDir.getIdDireccion()==null?null:beanDir.getIdDireccion().toString());
				if (tieneDirFacturacion) {
					throw new SIGAException ("messages.directions.duplicatedFact");
				}
				
				if (!preferenteModif.equals("")){
					 if (!idDireccionesCensoWeb.equals("") &&(!idDireccionesPreferentes.equals(""))){
						 direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif);
					 }
				
					 if (control != null && control.equals("0")){ // Igual vale 0.....
						  direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif);
					 }
				}				
				
			}else { 
				if (!preferenteModif.equals("")){
					 if (!idDireccionesCensoWeb.equals("") &&(!idDireccionesPreferentes.equals(""))){
						 direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif);
					 }
				
					 if (control != null && control.equals("0")){ // Igual vale 0.....
						  direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif);
					 }
				}
			}
		}
	}
	
	private static void formarDireccion(CenDireccionesBean beanDir, DireccionesForm miForm) throws SIGAException{
		
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
		beanDir.setPreferente (campoPreferenteBooleanToString
				(miForm.getPreferenteMail (), 
				miForm.getPreferenteCorreo (), 
				miForm.getPreferenteFax (),
				miForm.getPreferenteSms ()));
		beanDir.setTelefono1 (miForm.getTelefono1 ());
		beanDir.setTelefono2 (miForm.getTelefono2 ());
	}
	
	private static CenDireccionTipoDireccionBean[] establecerTipoDireccion(String [] tipos){
		int numTipos = tipos.length;
		CenDireccionTipoDireccionBean vBeanTipoDir [] = new CenDireccionTipoDireccionBean [numTipos];
		for (int i=0; i < numTipos; i++) {
			CenDireccionTipoDireccionBean b = new CenDireccionTipoDireccionBean ();
			b.setIdTipoDireccion (new Integer (tipos[i]));
			vBeanTipoDir[i] = b;
		}
		
		return vBeanTipoDir;
	}
	
	private static void insertarModificacionConsejo(CenDireccionesBean beanDir, UsrBean usr, int accionCola) throws SIGAException{
		CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (usr);
		if (!colaAdm.insertarCambioEnCola (accionCola, beanDir.getIdInstitucion (), beanDir.getIdPersona (), beanDir.getIdDireccion ()))
			throw new SIGAException (colaAdm.getError ());
	}
	
	/**
	 * Fucnion que devuleve un string que correspode a los datos almacenados en B.D.
	 * 
	 * @param mail
	 * @param correo
	 * @param fax
	 * @param sms
	 * @return cadena
	 */
	private static String campoPreferenteBooleanToString (Boolean mail, Boolean correo, Boolean fax, Boolean sms) throws SIGAException {
		String valor = "";
		
		try	{
			if (mail.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO;
			if (fax.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_FAX;
			if (correo.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREO;
			if (sms.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_SMS;
		
		} catch (Exception e) {
			throw new SIGAException ("messages.general.error");
		}
		return valor;
	} //campoPreferenteBooleanToString()
	
	
	private static String campoPreferenteBooleanToStringSeparados (Boolean mail, Boolean correo, Boolean fax, Boolean sms) throws SIGAException {
		String valor = "";
		
		try {
			if (mail.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO+"#";			
			if (fax.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_FAX+"#";
			if (correo.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREO+"#";
			if (sms.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_SMS;
		} catch (Exception e) {
			throw new SIGAException ("messages.general.error");
		}
		return valor;
	} //campoPreferenteBooleanToString()
	
	private static String parsearPreferenteModificado (String preferente){
		String valor = "";		
		for(int j=0;j<preferente.length(); j++){
			valor = preferente.charAt(j) + "#" + valor;		 
		}
		return valor;
	} //parsearPreferenteModificado()
		
	
	
	
	
	protected static void cambiodireccioncensoweb (CenDireccionesBean beanDir,int i,String sql,String tipo,String idDireccionesCensoWeb, CenDireccionTipoDireccionAdm tipoDirAdm, CenDireccionesAdm direccionesAdm)	throws SIGAException
	{
		RowsContainer rc2 = new RowsContainer(); 
		RowsContainer rc3 = new RowsContainer(); 
		Hashtable result= new Hashtable();	
		
		CenDireccionTipoDireccionBean tipodirBean = new CenDireccionTipoDireccionBean();
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
						for (int m=0; m<idDir.length; m++){
							  //modificar dando de baja logica.
							  String whereSancion =" where CEN_DIRECCION_TIPODIRECCION.idpersona = "+ beanDir.getIdPersona().toString();
					          whereSancion +=" AND CEN_DIRECCION_TIPODIRECCION.idinstitucion ="+beanDir.getIdInstitucion().toString();
					          whereSancion +=" AND CEN_DIRECCION_TIPODIRECCION.iddireccion ="+idDir[m];
					          
					          Vector direccionestipos= tipoDirAdm.select(whereSancion);               
					          int numerodirecciones=direccionestipos.size();				          
					        
					          if (numerodirecciones==1){
					        	  
					        	  String whereSancion1 =" where CEN_DIRECCIONES.idpersona = "+ beanDir.getIdPersona().toString();
					        	  whereSancion1 +=" AND CEN_DIRECCIONES.idinstitucion ="+beanDir.getIdInstitucion().toString();
					        	  whereSancion1 +=" AND CEN_DIRECCIONES.iddireccion ="+idDir[m];
					
					        	  Vector direcciones= direccionesAdm.select(whereSancion1);               
					        	  int nSanciones=direcciones.size();
		
					        	  for(int l=0;l<direcciones.size();l++) {  
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
				          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDINSTITUCION, beanDir.getIdInstitucion().toString());
				          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDPERSONA, beanDir.getIdPersona().toString());
				          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDir[m]);
				          		UtilidadesHash.set(clave, CenDireccionTipoDireccionBean.C_IDTIPODIRECCION, tipo);
					
				          		Vector v = tipoDirAdm.selectForUpdate(clave);
				          		for (int n = 0; n < v.size() && (!error); n++) {
				          			CenDireccionTipoDireccionBean b = (CenDireccionTipoDireccionBean) v.get(n);
				          			if (!tipoDirAdm.delete(b)) {
				          				error = true;
				          			}
				          		}
				          	}
						}								        	  
					}								
				}
			}
			
		} catch(Exception e){
			throw new SIGAException ("messages.general.error");
		}
	}

	public CenDireccionesBean getBeanDireccion() {
		return beanDireccion;
	}

	public void setBeanDireccion(CenDireccionesBean beanDireccion) {
		this.beanDireccion = beanDireccion;
	}

	public boolean isConfirmacionPregunta() {
		return confirmacionPregunta;
	}

	public void setConfirmacionPregunta(boolean confirmacionPregunta) {
		this.confirmacionPregunta = confirmacionPregunta;
	}

	public String getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(String tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}
	
}
