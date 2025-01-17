
package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColaCambioLetradoAdm;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.general.SIGAException;


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

	public static Direccion insertar (CenDireccionesBean beanDir, String tiposDir, String motivoHis,List<Integer> tiposDireccionAValidarIntegers, HttpServletRequest request,UsrBean usr) throws SIGAException {
		
		// Variables generales
		Direccion dir = new Direccion();		
		String idDireccionesCensoWeb="";
		String idDireccionesTraspasoOJ="";
		String idDireccionesFacturacion="";
		
		CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (usr);
		CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(usr);
		
		// Datos para las preguntas al usuario
		String modificarPreferencias = null;
		String modificarDireccionesCensoWeb = null;
		String modificarDireccionesTraspasoOJ = null;
		String modificarDireccionesFacturacion = null;
		String control = null;
		
		try {			
			
			// Obteniendo datos del formulario
			Long idPersona = beanDir.getIdPersona ();
			String idDireccionesPreferentes="";
			Integer idInstitucionPersona = beanDir.getIdInstitucion ();
			Long idDireccion = beanDir.getIdDireccion ();
			
			// Informacion sobre tipos direccion
			String tipos[] = {};
			if(!"".equals(tiposDir)) {
				tipos = tiposDir.split (",");
			}
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
				if (request.getParameter("modificarDireccionesTraspasoOJ")!=null){
					modificarDireccionesTraspasoOJ = request.getParameter("modificarDireccionesTraspasoOJ");
				} // fin datos preguntas
				
				
				if (request.getParameter("modificarDireccionesFacturacion")!=null){
					modificarDireccionesFacturacion = request.getParameter("modificarDireccionesFacturacion");
				}
				if (request.getParameter("control")!=null){
					control = request.getParameter("control");
				}
				
				if (modificarPreferencias!=null && modificarPreferencias.equals("1")){
					idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
				} else {
					//comprobando que no existen dos direcciones con igual campo preferente
					idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString(), idInstitucionPersona.toString (), preferente, idDireccion);
					if(!idDireccionesPreferentes.equals("")){
						request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						request.setAttribute("idDireccionesFacturacion", idDireccionesFacturacion);
						request.setAttribute("idDireccionesTraspasoOJ", idDireccionesTraspasoOJ);
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("control", "0");
						dir.setConfirmacionPregunta(true);
						dir.setTipoPregunta("preguntaCambioPreferencia");
						return dir;
					} 	
				}
				
				//comprobando que el cliente no tenga ya una direccion de tipo guardia si es asi no se permite anyadir la direccion
				
				if ((modificarDireccionesCensoWeb!=null && modificarDireccionesCensoWeb.equals("1")) ||(modificarDireccionesFacturacion!=null && modificarDireccionesFacturacion.equals("1")) || (modificarDireccionesTraspasoOJ!=null && modificarDireccionesTraspasoOJ.equals("1")) ){
					idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");
					idDireccionesTraspasoOJ=request.getParameter("idDireccionesTraspasoOJ");
					idDireccionesFacturacion=request.getParameter("idDireccionesFacturacion");
					
				}else {		
					idDireccionesCensoWeb=direccionesAdm.obtenerTipoDireccion(idPersona.toString(),idInstitucionPersona.toString (), tiposdireciones, idDireccion,ClsConstants.TIPO_DIRECCION_CENSOWEB);			  
					idDireccionesTraspasoOJ=direccionesAdm.obtenerTipoDireccion(idPersona.toString(),idInstitucionPersona.toString (), tiposdireciones, idDireccion,ClsConstants.TIPO_DIRECCION_TRASPASO_OJ);
					idDireccionesFacturacion=direccionesAdm.obtenerTipoDireccion(idPersona.toString (),idInstitucionPersona.toString (), tiposdireciones, idDireccion,ClsConstants.TIPO_DIRECCION_FACTURACION);
					if  (!idDireccionesCensoWeb.equals("") ||!idDireccionesFacturacion.equals("")||!idDireccionesTraspasoOJ.equals("")){			
						request.setAttribute("idDireccionesFacturacion", idDireccionesFacturacion);			
						request.setAttribute("idDireccionesTraspasoOJ", idDireccionesTraspasoOJ);
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
			comprobarTipoDireccion(tipos, beanDir, direccionesAdm, dir, control, idDireccionesCensoWeb, idPersona, idInstitucionPersona, idDireccionesPreferentes, tipoDirAdm, modificarPreferencias, modificarDireccionesCensoWeb,modificarDireccionesFacturacion,idDireccionesFacturacion,modificarDireccionesTraspasoOJ,idDireccionesTraspasoOJ,tiposDireccionAValidarIntegers);
			
			//estableciendo los datos del tipo de direccion
			CenDireccionTipoDireccionBean vBeanTipoDir [] = establecerTipoDireccion(tipos);
			
			//insertando la direccion
			if (! direccionesAdm.insertarConHistorico (beanDir, vBeanTipoDir, motivoHis,tiposDireccionAValidarIntegers, usr.getLanguage()))
				throw new SIGAException (direccionesAdm.getError());
			
			
			//insertando en la cola de modificacion de datos para Consejos
			insertarModificacionConsejo(beanDir,usr, ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION);

			// JPT: Modificaciones para los anexos de los mandatos en SEPA
			// Recorro todos los tipos de direcciones actuales
			for (int i=0; i<vBeanTipoDir.length; i++){
				
				// Compruebo que tenga activado el tipo de facturacion
				if (vBeanTipoDir[i].getIdTipoDireccion().equals(ClsConstants.TIPO_DIRECCION_FACTURACION)) {
					
					// Se realiza el proceso de revision de anexos para SEPA
					Object[] paramAnexos = new Object[4];
					paramAnexos[0] = beanDir.getIdInstitucion().toString();
					paramAnexos[1] = beanDir.getIdPersona().toString();
					paramAnexos[2] = usr.getUserName();
					paramAnexos[3] = usr.getLanguage();
					
					String resultado[] = new String[2];
					resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.RevisarAnexos(?,?,?,?,?,?)}", 2, paramAnexos);
					if (resultado == null || !resultado[0].equals("0")) {
						throw new ClsExceptions ("Error al insertar los anexos de las cuentas");
					}						
				}
			}
		
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
	public static Direccion actualizar (CenDireccionesBean beanDir, String tiposDir, String motivoHis,List<Integer> tiposDireccionAValidarIntegers,HttpServletRequest request,UsrBean usr) throws SIGAException {
		
		
		
		//Variables generales
		Direccion dir = new Direccion();	
		String idDireccionesCensoWeb="";
		String idDireccionesFacturacion="";
		String idDireccionesTraspasoOJ="";

		try	{
			
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (usr);
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm(usr);			
			CenDireccionTipoDireccionBean vBeanTipoDir [] = null;
			
			Long idPersona = beanDir.getIdPersona ();
			Integer idInstitucionPersona = beanDir.getIdInstitucion ();
			Long idDireccion = beanDir.getIdDireccion ();
			
			String idDireccionesPreferentes="";
			
			//Informacion sobre tipos direccion
			String tipos[] = tiposDir.split (",");	
			
			// Datos para las pregunats al usuario
			String modificarPreferencias = null;
			String modificarDireccionesCensoWeb = null;
			String modificarDireccionesFacturacion = null;
			String modificarDireccionesTraspasoOJ = null;
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
				if (request.getParameter("modificarDireccionesTraspasoOJ")!=null){
					modificarDireccionesTraspasoOJ = request.getParameter("modificarDireccionesTraspasoOJ");
				}
				if (request.getParameter("modificarDireccionesFacturacion")!=null){
					modificarDireccionesFacturacion = request.getParameter("modificarDireccionesFacturacion");
				}
				if (request.getParameter("control")!=null){
					control = request.getParameter("control");
				}
				
				if (modificarPreferencias!=null && modificarPreferencias.equals("1")){
					idDireccionesPreferentes=request.getParameter("idDireccionesPreferentes");
				}else {			
					//comprobando que no existen dos direcciones con igual campo preferente
					idDireccionesPreferentes=direccionesAdm.obtenerPreferenteDirecciones (idPersona.toString (),idInstitucionPersona.toString (), preferente, idDireccion);
				    if (!idDireccionesPreferentes.equals("")){
					    request.setAttribute("idDireccionesPreferentes", idDireccionesPreferentes);
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("idDireccionesFacturacion", idDireccionesFacturacion);
						request.setAttribute("idDireccionesTraspasoOJ", idDireccionesTraspasoOJ);
						
						request.setAttribute("control", "0");
						dir.setConfirmacionPregunta(true);
						dir.setTipoPregunta("preguntaCambioPreferencia");
						return dir;
				     }  
				 
				}
				//comprobando que el cliente no tenga ya una direccion de tipo guardia si es asi no se permite anyadir la direccion
				
				//esta es la parte que comprobra si tiene una direcci�n de tipo censoweb.
		
				if ((modificarDireccionesCensoWeb!=null && modificarDireccionesCensoWeb.equals("1")) ||(modificarDireccionesFacturacion!=null && modificarDireccionesFacturacion.equals("1"))||(modificarDireccionesTraspasoOJ!=null && modificarDireccionesTraspasoOJ.equals("1")) ){
					idDireccionesCensoWeb=request.getParameter("idDireccionesCensoWeb");				
					idDireccionesFacturacion=request.getParameter("idDireccionesFacturacion");
					idDireccionesTraspasoOJ=request.getParameter("idDireccionesTraspasoOJ");
				
				}else {		
					idDireccionesCensoWeb=direccionesAdm.obtenerTipoDireccion(idPersona.toString (),idInstitucionPersona.toString (), tiposdireciones, idDireccion,ClsConstants.TIPO_DIRECCION_CENSOWEB);			  
					idDireccionesTraspasoOJ=direccionesAdm.obtenerTipoDireccion(idPersona.toString (),idInstitucionPersona.toString (), tiposdireciones, idDireccion,ClsConstants.TIPO_DIRECCION_TRASPASO_OJ);
					idDireccionesFacturacion=direccionesAdm.obtenerTipoDireccion(idPersona.toString (),idInstitucionPersona.toString (), tiposdireciones, idDireccion,ClsConstants.TIPO_DIRECCION_FACTURACION);
					if  (!idDireccionesCensoWeb.equals("") ||!idDireccionesFacturacion.equals("")||!idDireccionesTraspasoOJ.equals("")){			
						request.setAttribute("idDireccionesCensoWeb", idDireccionesCensoWeb);
						request.setAttribute("idDireccionesFacturacion", idDireccionesFacturacion);
						request.setAttribute("idDireccionesTraspasoOJ", idDireccionesTraspasoOJ);
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
				comprobarTipoDireccion(tipos, beanDir, direccionesAdm, dir, control, idDireccionesCensoWeb, idPersona, idInstitucionPersona, idDireccionesPreferentes, tipoDirAdm, modificarPreferencias, modificarDireccionesCensoWeb,modificarDireccionesFacturacion,idDireccionesFacturacion,modificarDireccionesTraspasoOJ,idDireccionesTraspasoOJ,tiposDireccionAValidarIntegers);

				//estableciendo los datos del tipo de direccion
				vBeanTipoDir = establecerTipoDireccion(tipos);
			}
			
			
			/* CR7 - INC_11983_SIGA
			   Si el par�metro motivoHis es NULL no se inserta un registro en hist�rico ya que no se quiere guardar esta informaci�n 
			   en CenHistorico la primera vez que se incorpora un colegiado. Si sale alg�n caso mas en el que no se quiera guardar
			   esta informaci�n, habr� que poner a NULL el par�metro motivoHis 
			*/	
			CenHistoricoBean beanHis = null;
			if(motivoHis != null){
				//estableciendo los datos del Historico
				beanHis = new CenHistoricoBean ();
				beanHis.setMotivo (motivoHis);
			}			
			
			
			//Actualizando la direccion
			if (!direccionesAdm.updateConHistorico (beanDir, vBeanTipoDir, beanHis,tiposDireccionAValidarIntegers, usr.getLanguage()))
				throw new SIGAException (direccionesAdm.getError ());
			
			//insertando en la cola de modificacion de datos para Consejos
			insertarModificacionConsejo(beanDir,usr, ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION);		
				
			// Recorro todos los tipos de direcciones actuales
			if (vBeanTipoDir!=null) {			
				for (int i=0; i<vBeanTipoDir.length; i++){
					
					// Compruebo que tenga activado el tipo de facturacion
					if (vBeanTipoDir[i]!=null && 
							vBeanTipoDir[i].getIdTipoDireccion()!=null && 
							vBeanTipoDir[i].getIdTipoDireccion().equals(ClsConstants.TIPO_DIRECCION_FACTURACION)) {
						
						// JPT: Modificaciones para los anexos de los mandatos en SEPA
						boolean cambio = (beanDir.getDomicilio()!=null && !beanDir.getDomicilio().equals(beanDir.getOriginalHash().get(CenDireccionesBean.C_DOMICILIO))) ||
											(beanDir.getCodigoPostal()!=null && !beanDir.getCodigoPostal().equals(beanDir.getOriginalHash().get(CenDireccionesBean.C_CODIGOPOSTAL))) ||
											(beanDir.getIdPais()!=null && !beanDir.getIdPais().equals(beanDir.getOriginalHash().get(CenDireccionesBean.C_IDPAIS))) ||
											(beanDir.getIdProvincia()!=null && !beanDir.getIdProvincia().equals(beanDir.getOriginalHash().get(CenDireccionesBean.C_IDPROVINCIA))) ||
											(beanDir.getIdPoblacion()!=null && !beanDir.getIdPoblacion().equals(beanDir.getOriginalHash().get(CenDireccionesBean.C_IDPOBLACION))) ||
											(beanDir.getPoblacionExtranjera()!=null && !beanDir.getPoblacionExtranjera().equals(beanDir.getOriginalHash().get(CenDireccionesBean.C_POBLACIONEXTRANJERA)));
						
						// JPT: Si no hay cambio, hay que mirar si antes existia el check de facturacion
						if (!cambio) {
							cambio = true;
							Vector vBeanTipoDirOriginal = (Vector) beanDir.getOriginalHash().get(CenDireccionTipoDireccionBean.C_IDTIPODIRECCION);
							for (int j=0; j<vBeanTipoDirOriginal.size(); j++) {
								CenDireccionTipoDireccionBean objDirOriginal = (CenDireccionTipoDireccionBean) vBeanTipoDirOriginal.get(j);
								if (objDirOriginal.getIdTipoDireccion().equals(ClsConstants.TIPO_DIRECCION_FACTURACION)) {
									cambio = false; // Si aparece es que no ha cambiado nada
								}
							}							
						}						
						
						if (cambio) {						
						
							// Se realiza el proceso de revision de anexos para SEPA
							Object[] paramAnexos = new Object[4];
							paramAnexos[0] = beanDir.getIdInstitucion().toString();
							paramAnexos[1] = beanDir.getIdPersona().toString();
							paramAnexos[2] = usr.getUserName();
							paramAnexos[3] = usr.getLanguage();
							
							String resultado[] = new String[2];
							resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.RevisarAnexos(?,?,?,?,?,?)}", 2, paramAnexos);
							if (resultado == null || !resultado[0].equals("0")) {
								throw new ClsExceptions ("Error al insertar los anexos de las cuentas");
							}						
						}
					}
				}
			}
			
		} catch (SIGAException e) {
			throw e;
			
		} catch(Exception e){
			throw new SIGAException ("messages.general.error"+e.toString());
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
	protected boolean borrar (CenDireccionesBean beanDir,List<Integer> tiposDireccionAValidarIntegers,HttpServletRequest request,UsrBean usr) throws SIGAException {
		//Variables generales
		
		try	{
			
			Long idPersona = beanDir.getIdPersona ();
			Integer idInstitucion = beanDir.getIdInstitucion ();
			Long idDireccion = beanDir.getIdDireccion();
			
			CenDireccionesAdm admDir = new CenDireccionesAdm (usr);
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (claves, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
			
			//borrando la direccion en BD
			if (!admDir.deleteConHistorico (claves, null, true,tiposDireccionAValidarIntegers))
				throw new SIGAException (admDir.getError ());
			
			insertarModificacionConsejo(beanDir, usr, ClsConstants.COLA_CAMBIO_LETRADO_BORRADO_DIRECCION);

		}catch (SIGAException e) {
			throw e;
			
		} catch(Exception e){
			throw new SIGAException ("messages.general.error");
		}
		
		return true;
	} //borrar()
	

	private static void comprobarTipoDireccion(String [] tipos,CenDireccionesBean beanDir, CenDireccionesAdm direccionesAdm, Direccion dir, String control, 
			String idDireccionesCensoWeb, Long idPersona, Integer idInstitucionPersona, String idDireccionesPreferentes, CenDireccionTipoDireccionAdm tipoDirAdm, 
			String modificarPreferencias, 
			String modificarDireccionesCensoWeb, String modificarDireccionesFacturacion, String idDireccionesFacturacion, String modificarDireccionesTraspasoOJ, String idDireccionesTraspasoOJ,
			List<Integer> tiposDireccionAValidarIntegers) throws SIGAException, ClsExceptions, IllegalStateException, SecurityException, SystemException{
	
		String preferenteModif = "";
		int j=0;
		if(beanDir.getPreferente()!=null){
			preferenteModif = parsearPreferenteModificado(beanDir.getPreferente());
		}
		
		String stipo;
		int itipo;
		for (int i=0; i < tipos.length; i++){
			stipo = tipos[i];
			itipo = Integer.parseInt(stipo);
			
			if(itipo == ClsConstants.TIPO_DIRECCION_GUARDIA)	{
				Row row = direccionesAdm.comprobarTipoDireccion(beanDir.getIdInstitucion().toString(), beanDir.getIdPersona().toString(), stipo);
				if (row != null) {
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
					
					direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,tiposDireccionAValidarIntegers);
				}

			}else if (itipo == ClsConstants.TIPO_DIRECCION_CENSOWEB || itipo == ClsConstants.TIPO_DIRECCION_TRASPASO_OJ || itipo == ClsConstants.TIPO_DIRECCION_FACTURACION){//direcci�n de tipo censoweb
				String idDirecciones, modificarDirecciones;
				switch (itipo) {
				case ClsConstants.TIPO_DIRECCION_CENSOWEB:
					idDirecciones = idDireccionesCensoWeb;
					modificarDirecciones = modificarDireccionesCensoWeb;
					break;
				case ClsConstants.TIPO_DIRECCION_TRASPASO_OJ:
					idDirecciones = idDireccionesTraspasoOJ;
					modificarDirecciones = modificarDireccionesTraspasoOJ;
					break;
				case ClsConstants.TIPO_DIRECCION_FACTURACION:
					idDirecciones = idDireccionesFacturacion;
					modificarDirecciones = modificarDireccionesFacturacion;
					break;
				default:
					throw (new ClsExceptions("Este caso no puede darse porque estamos dentro del if y se obliga alguno de los casos del switch"));
				}
				
				cambioDirecciones(beanDir, stipo, idDirecciones, tipoDirAdm, direccionesAdm);
				if ((modificarPreferencias!=null && modificarPreferencias.equals("1")) || (modificarDirecciones!=null && modificarDirecciones.equals("1"))){
					
					if (control != null && control.equals("0")){
						if (!preferenteModif.equals("")){
							
							direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,tiposDireccionAValidarIntegers);	
						}
					}else{ 
						cambioDirecciones (beanDir, stipo, idDirecciones, tipoDirAdm, direccionesAdm);
					}
				}
				
			}else { 
				if (!preferenteModif.equals("")){
					 if ((!idDireccionesCensoWeb.equals("") ||!idDireccionesTraspasoOJ.equals("")) &&(!idDireccionesPreferentes.equals(""))){
						 direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,tiposDireccionAValidarIntegers);
					 }
				
					 if (control != null && control.equals("0")){ // Igual vale 0.....
						  direccionesAdm.modificarDireccionesPreferentes(idPersona, idInstitucionPersona.toString (), idDireccionesPreferentes, preferenteModif,tiposDireccionAValidarIntegers);
					 }
				}
			}
		}
	}
	
	/**
	 * Este metodo comprueba que los tipos de una nueva direccion (pasada como parametro) 
	 * cumplen con las unicidades de tipos de las direcciones existentes.
	 * 
	 * @param dirNueva
	 * @throws ClsExceptions 
	 */
	public static ArrayList<String> revisarTiposEnDireccionesExistentes(String idInstitucion, String idPersona, List<String> tiposAinsertar, UsrBean usr) throws ClsExceptions
	{
		ArrayList<String> tiposValidos = new ArrayList<String>();

		List<Integer> alTiposDireccionUnicos = Arrays.asList(CenDireccionTipoDireccionBean.tiposDireccionUnicos);
		CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);

		for (String tipo : tiposAinsertar) {
			if (!alTiposDireccionUnicos.contains(Integer.valueOf(tipo)) || dirAdm.getDireccionTipoExacto(idInstitucion, idPersona, tipo) == null) {
				tiposValidos.add(tipo);
			}
		}

		return tiposValidos;
	}
	
	/**
	 * Este metodo comprueba que los tipos de una nueva direccion (pasada como parametro) 
	 * cumplen con las unicidades de tipos de las direcciones existentes.
	 * 
	 * @param dirNueva
	 * @throws ClsExceptions 
	 */
	public static String revisarPreferenciasEnDireccionesExistentes(String idInstitucion, String idPersona, String preferenciasAinsertar, UsrBean usr) throws ClsExceptions {
		StringBuilder preferenciasValidas = new StringBuilder();
		
		CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);
		
		char preferencia;
		for (int i = 0; i < preferenciasAinsertar.length(); i++) {
			preferencia = preferenciasAinsertar.charAt(i);
			if (dirAdm.getDireccionPreferenciaExacta(idInstitucion, idPersona, preferencia) == null) {
				preferenciasValidas.append(preferencia);
			}
		}
		
		return preferenciasValidas.toString();
	}
	
	public static CenDireccionTipoDireccionBean[] establecerTipoDireccion(String [] tipos){
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
	
	

	public static String parsearPreferenteModificado (String preferente){
		String valor = "";		
		for(int j=0;j<preferente.length(); j++){
			valor = preferente.charAt(j) + "#" + valor;		 
		}
		return valor;
	} //parsearPreferenteModificado()
		
	/**
	 * Devuelve el la lista los ids de los tipos de direccion que son obligatorios para cada tipo de cliente
	 * @param tipoCliente: 8 Nocolegiado, 16 letrado, Colegiado
	 * @return
	 */
	
	public static List<Integer> getListaDireccionesObligatorias(String tipoCliente){
		
		if(tipoCliente==null)
			return null;
		List<Integer> tiposDireccionAValidarIntegers = new ArrayList<Integer>();
		// Ninguna restriccion para el no colegiado
		if(tipoCliente.equals(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO) 
				|| tipoCliente.equals(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO) 
				|| tipoCliente.equals(ClsConstants.TIPO_ACCESO_PESTANAS_FACTURACION_NOCOLEGIADO)){
			
		}else if(tipoCliente.equals(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO)){
			//Para el letrado la unica restriccion es que tenga drecion de censo web
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_CENSOWEB);
//			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_TRASPASO_OJ);
		}else if(tipoCliente.equals(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO)){
			//Para los colegiados deben tener
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_FACTURACION);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_CENSOWEB);
			//En metodo que las validad mira si es ejerciente o no ejerciente para validar la de despacho y la de guia judicial
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_DESPACHO);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_TRASPASO_OJ);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_GUIA);	
		}else{
			//Para no identyificados ponemos la restriccion maxima, por si acaso
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_FACTURACION);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_CENSOWEB);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_DESPACHO);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_TRASPASO_OJ);
			tiposDireccionAValidarIntegers.add(ClsConstants.TIPO_DIRECCION_GUIA);	
		}
		return tiposDireccionAValidarIntegers;
		
	}
	
	protected static void cambioDirecciones(CenDireccionesBean beanDir,
			String tipoDireccion,
			String idDirecciones,
			CenDireccionTipoDireccionAdm tipoDirAdm,
			CenDireccionesAdm direccionesAdm) throws SIGAException
	{
		// Variables de la clave de la direccion
		String idInstitucion, idPersona, idDireccion;

		// Variables temporales de consulta
		String where;
		Vector vDirecciones, vTiposDireccion;
		CenDireccionesBean direccionesBean;
		CenDireccionTipoDireccionBean tipoDireccionBean;

		try {
			// obteniendo los datos del cliente
			idInstitucion = beanDir.getIdInstitucion().toString();
			idPersona = beanDir.getIdPersona().toString();

			// comprobando si el cliente tiene alguna direccion con ese tipo
			Row row = direccionesAdm.comprobarTipoDireccion(idInstitucion, idPersona, tipoDireccion);
			if (row == null)
				return;

			// comprobando si hay algo en los parametros
			if (idDirecciones.equals(""))
				return;

			// Para cada una de las direcciones pasadas como parametro
			String[] idDir = idDirecciones.split("@");
			for (int m = 0; m < idDir.length; m++) { // for de direcciones
				idDireccion = idDir[m];

				// consultando los tipos de la direccion
				where = " where CEN_DIRECCION_TIPODIRECCION.idpersona = " + idPersona;
				where += " AND CEN_DIRECCION_TIPODIRECCION.idinstitucion =" + idInstitucion;
				where += " AND CEN_DIRECCION_TIPODIRECCION.iddireccion =" + idDireccion;
				vTiposDireccion = tipoDirAdm.select(where);

				// si la direccion que estamos mirando solo tiene el tipo en cuestion
				// entonces damos de baja la direccion
				if (vTiposDireccion.size() == 1) {

					where = " where CEN_DIRECCIONES.idpersona = " + idPersona;
					where += " AND CEN_DIRECCIONES.idinstitucion =" + idInstitucion;
					where += " AND CEN_DIRECCIONES.iddireccion =" + idDireccion;
					vDirecciones = direccionesAdm.select(where);

					direccionesBean = (CenDireccionesBean) vDirecciones.elementAt(0);
					direccionesBean.setFechaBaja("SYSDATE");
					String datosCambiar[] = new String[1];
					datosCambiar[0] = CenDireccionesBean.C_FECHABAJA;
					if (!direccionesAdm.updateDirect(direccionesBean, direccionesAdm.getClavesBean(), datosCambiar)) {
						throw new ClsExceptions(direccionesAdm.getError());
					}

					// si la direccion que estamos mirando tiene otros tipos ademas del tipo en cuestion
					// entonces le quitamos el tipo en cuestion a la direccion que estamos mirando
				} else {
	          		Hashtable hashPKTipoDireccion = new Hashtable();
	          		UtilidadesHash.set(hashPKTipoDireccion, CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
	          		UtilidadesHash.set(hashPKTipoDireccion, CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
	          		UtilidadesHash.set(hashPKTipoDireccion, CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccion);
	          		UtilidadesHash.set(hashPKTipoDireccion, CenDireccionTipoDireccionBean.C_IDTIPODIRECCION, tipoDireccion);
	          		vTiposDireccion = tipoDirAdm.selectForUpdate(hashPKTipoDireccion);
	          		
					tipoDireccionBean = (CenDireccionTipoDireccionBean) vTiposDireccion.get(0);
					if (!tipoDirAdm.delete(tipoDireccionBean)) {
						throw new ClsExceptions(tipoDirAdm.getError());
					}
				}
			} // for de direcciones

		} catch (Exception e) {
			throw new SIGAException("messages.general.error");
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
