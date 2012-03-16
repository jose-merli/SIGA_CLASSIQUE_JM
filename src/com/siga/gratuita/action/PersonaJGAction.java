/**
 * Action para los mantenimientos de persona JG
 * @author AtosOrigin SAE - S233735
 * @since 23-03-2006
 */
package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenTipoIdentificacionAdm;
import com.siga.beans.CenTipoIdentificacionBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsBeneficiarioSOJAdm;
import com.siga.beans.ScsBeneficiarioSOJBean;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsContrariosAsistenciaBean;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsContrariosEJGAdm;
import com.siga.beans.ScsContrariosEJGBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.PersonaJGForm;
import com.siga.ws.CajgConfiguracion;

public class PersonaJGAction extends MasterAction {
	
	// Atributos
	/** valor de parametro concepto = AsistenciaAsistido */
	public static String ASISTENCIA_ASISTIDO = "AsistenciaAsistido";
	/** valor de parametro concepto = AsistenciaRepresentante */
	public static String ASISTENCIA_REPRESENTANTE = "AsistenciaRepresentante";
	/** valor de parametro concepto = AsistenciaContrarios */
	public static String ASISTENCIA_CONTRARIOS = "AsistenciaContrarios";
	public static String EJG_CONTRARIOS = "EjgContrarios";
	/** valor de parametro concepto = DesignacionInteresado */
	public static String DESIGNACION_INTERESADO = "DesignacionInteresado";
	/** valor de parametro concepto = DesignacionContrarios */
	public static String DESIGNACION_CONTRARIOS = "DesignacionContrarios";
	/** valor de parametro concepto = DesignacionRepresentante */
	public static String DESIGNACION_REPRESENTANTE = "DesignacionRepresentante";
	/** valor de parametro concepto = SOJRepresentante */
	public static String SOJ_REPRESENTANTE = "SOJRepresentante";
	/** valor de parametro concepto = SOJ */
	public static String SOJ = "SOJ";
	/** valor de parametro concepto = EJGRepresentante */
	public static String EJG_REPRESENTANTE = "EJGRepresentante";
	/** valor de parametro concepto = EJG */
	public static String EJG = "EJG";
	/** valor de parametro concepto = EJGUnidadFamiliar */
	public static String EJG_UNIDADFAMILIAR = "EJGUnidadFamiliar";
	/** valor de parametro concepto = PersonaJG */
	public static String PERSONAJG = "PersonaJG";

	
	/** valor de parametro pantalla = Modal */
	public static String MODAL = "Modal";
	/** valor de parametro pantalla = Pestana */
	public static String PESTANA = "Pestana";
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario 
	 *  ejecuta distintas acciones
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
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						// El por defecto sera el de las pestanhas
						mapDestino = abrirPestana(mapping, miForm, request, response);
						break; 
					}else if ( accion.equalsIgnoreCase("getAjaxTipoIdentificacion")){
						getAjaxTipoIdentificacion(mapping, miForm, request, response);
						return null;						
					} else if (accion.equalsIgnoreCase("Ver")){  
						mapDestino = Ver(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("Editar")){
						mapDestino = Editar(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("Borrar")){
						mapDestino = Borrar(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirModal")){
						mapDestino = abrirModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirModalAsuntos")){
						mapDestino = abrirModalAsuntos(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirPestana")){
						mapDestino = abrirPestana(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarNIF")){
						mapDestino = buscarNIF(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("getAjaxBusquedaNIF")){
						getAjaxBusquedaNIF(mapping, miForm, request, response);
						return null;
					} else if (accion.equalsIgnoreCase("getAjaxExisteNIF")){
						getAjaxExisteNIF(mapping, miForm, request, response);
						return null;						
					} else if ( accion.equalsIgnoreCase("guardarEJG") ||
								accion.equalsIgnoreCase("guardarSOJ") ||
								accion.equalsIgnoreCase("guardarDesigna") ||
								accion.equalsIgnoreCase("guardarAsistencia") ||
								accion.equalsIgnoreCase("guardarContrariosEjg")||
								accion.equalsIgnoreCase("guardarPersona")){
						mapDestino = guardarPersonaJG(mapping, miForm, request, response);
					} else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	/**
	 * Metodo que implementa el modo Borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String Borrar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
			
			// para la clave de la persona JG
			String idInstitucionJG="", idPersonaJG="";
			// para la clave de la persona JG (caso de representante)
			String idInstitucionPER="", idPersonaPER="";
			// para la clave de EJG
			String idInstitucionEJG="", idTipoEJG="", idAnioEJG="", idNumeroEJG="";
			// para la clave de SOJ
			String idInstitucionSOJ="", idTipoSOJ="", idAnioSOJ="", idNumeroSOJ="";
			// para la clave de Designa
			String idInstitucionDesigna="", idTurnoDesigna="", idAnioDesigna="", idNumeroDesigna="";
			// para la clave de Asistencia
			String idInstitucionAsistencia="", idAnioAsistencia="", idNumeroAsistencia="";

	     	// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);

			// obtener Concepto
			String concepto = (String)vOcultos.get(0);
			// obtener titulos
			String titulo = (String)vOcultos.get(1);
			String localizacion = (String)vOcultos.get(2);
			// obtener Accion
			//String accion = (String)vOcultos.get(3);
			// obtener Pantalla
			//String pantalla = (String)vOcultos.get(4);
			//if (pantalla!=null && pantalla.equals(this.PESTANA)) {
			//	this.abrirPestana(mapping, formulario, request, response);
			//}
			
			// obtener Persona
			idInstitucionJG = (String)vOcultos.get(4);
			idPersonaJG = (String)vOcultos.get(5);
			
			// Comienzo control de transacciones 
			tx = user.getTransaction();			
			tx.begin();
			
			if (concepto==null) {
				throw new ClsExceptions("Falta de parámetro obligatorio (concepto)");
			} else {
				if (concepto.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
					idInstitucionDesigna=(String)vOcultos.get(6);
					idTurnoDesigna=(String)vOcultos.get(7);
					idAnioDesigna=(String)vOcultos.get(8);
					idNumeroDesigna=(String)vOcultos.get(9);
					// busco para borrar
					ScsContrariosDesignaAdm adm = new ScsContrariosDesignaAdm(this.getUserBean(request));
					Hashtable ht = new Hashtable();
					ht.put(ScsContrariosDesignaBean.C_IDINSTITUCION,idInstitucionDesigna);
					ht.put(ScsContrariosDesignaBean.C_IDTURNO,idTurnoDesigna);
					ht.put(ScsContrariosDesignaBean.C_ANIO,idAnioDesigna);
					ht.put(ScsContrariosDesignaBean.C_NUMERO,idNumeroDesigna);
					ht.put(ScsContrariosDesignaBean.C_IDPERSONA,idPersonaJG);
					if (!adm.delete(ht)) {
						throw new ClsExceptions("Error al borrar contrarios designa");
					}
					
				} else
					if (concepto.equals(PersonaJGAction.DESIGNACION_INTERESADO)) {
						idInstitucionDesigna=(String)vOcultos.get(6);
						idTurnoDesigna=(String)vOcultos.get(7);
						idAnioDesigna=(String)vOcultos.get(8);
						idNumeroDesigna=(String)vOcultos.get(9);
						// busco para borrar
						ScsDefendidosDesignaAdm adm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
						Hashtable ht = new Hashtable();
						ht.put(ScsDefendidosDesignaBean.C_IDINSTITUCION,idInstitucionDesigna);
						ht.put(ScsDefendidosDesignaBean.C_IDTURNO,idTurnoDesigna);
						ht.put(ScsDefendidosDesignaBean.C_ANIO,idAnioDesigna);
						ht.put(ScsDefendidosDesignaBean.C_NUMERO,idNumeroDesigna);
						ht.put(ScsDefendidosDesignaBean.C_IDPERSONA,idPersonaJG);
						if (!adm.delete(ht)) {
							throw new ClsExceptions("Error al borrar interesados designa");
						}
						
					} else
						if(concepto.equals(PersonaJGAction.EJG_CONTRARIOS)) {
							idInstitucionEJG = (String)vOcultos.get(6);
							idPersonaJG =(String)vOcultos.get(5);
							
							idNumeroEJG = (String)vOcultos.get(8);
							idAnioEJG = (String)vOcultos.get(7);
							idTipoEJG =(String)vOcultos.get(9);
							
							ScsContrariosEJGBean beanContrariosEJG = new ScsContrariosEJGBean();
							ScsContrariosEJGAdm admContrariosEJG = new ScsContrariosEJGAdm(this.getUserBean(request));
							beanContrariosEJG.setAnio(new Integer(idAnioEJG));
							beanContrariosEJG.setIdPersona(new Integer(idPersonaJG));
							beanContrariosEJG.setIdInstitucion(new Integer(idInstitucionEJG));
							beanContrariosEJG.setNumero(new Integer(idNumeroEJG));
							beanContrariosEJG.setIdTipoEJG(new Integer(idTipoEJG));
										
							
							if(!admContrariosEJG.delete(beanContrariosEJG)){
								throw new ClsExceptions("Error al borrar contrarios ejg");
							}
							
						}else
						if (concepto.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) {
							idInstitucionAsistencia=(String)vOcultos.get(6);
							idAnioAsistencia=(String)vOcultos.get(7);
							idNumeroAsistencia=(String)vOcultos.get(8);
							// nada
						} else
						if (concepto.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
							idInstitucionAsistencia=(String)vOcultos.get(6);
							idAnioAsistencia=(String)vOcultos.get(7);
							idNumeroAsistencia=(String)vOcultos.get(8);
							// busco para borrar
							ScsContrariosAsistenciaAdm adm = new ScsContrariosAsistenciaAdm(this.getUserBean(request));
							Hashtable ht = new Hashtable();
							ht.put(ScsContrariosAsistenciaBean.C_IDINSTITUCION,idInstitucionAsistencia);
							ht.put(ScsContrariosAsistenciaBean.C_ANIO,idAnioAsistencia);
							ht.put(ScsContrariosAsistenciaBean.C_NUMERO,idNumeroAsistencia);
							ht.put(ScsContrariosAsistenciaBean.C_IDPERSONA,idPersonaJG);
							if (!adm.delete(ht)) {
								throw new ClsExceptions("Error al borrar contrarios asistencia");
							}
						} else
								if (concepto.equals(PersonaJGAction.EJG)) {
									idInstitucionEJG=(String)vOcultos.get(6);
									idTipoEJG=(String)vOcultos.get(7);
									idAnioEJG=(String)vOcultos.get(8);
									idNumeroEJG=(String)vOcultos.get(9);
								} else
									if (concepto.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
										idInstitucionEJG=(String)vOcultos.get(6);
										idTipoEJG=(String)vOcultos.get(7);
										idAnioEJG=(String)vOcultos.get(8);
										idNumeroEJG=(String)vOcultos.get(9);
										// busco para borrar
										ScsUnidadFamiliarEJGAdm adm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
										Hashtable ht = new Hashtable();
										ht.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,idInstitucionEJG);
										ht.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,idTipoEJG);
										ht.put(ScsUnidadFamiliarEJGBean.C_ANIO,idAnioEJG);
										ht.put(ScsUnidadFamiliarEJGBean.C_NUMERO,idNumeroEJG);
										ht.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,idPersonaJG);
										
										ScsDocumentacionEJGAdm scsDocumentacionEJGAdm = new ScsDocumentacionEJGAdm(getUserBean(request));
										scsDocumentacionEJGAdm.deleteDocumentacionPresentador(idInstitucionEJG, idTipoEJG, idAnioEJG, idNumeroEJG, idPersonaJG);
										
										if (!adm.delete(ht)) {
											throw new ClsExceptions("Error al borrar Unidad familiar");
										}
									} else
				if (concepto.equals(PersonaJGAction.SOJ)) {
					idInstitucionSOJ=(String)vOcultos.get(6);
					idTipoSOJ=(String)vOcultos.get(7);
					idAnioSOJ=(String)vOcultos.get(8);
					idNumeroSOJ=(String)vOcultos.get(9);
				} else
				if (concepto.equals(PersonaJGAction.PERSONAJG)) {
					idInstitucionPER=(String)vOcultos.get(6);
					idPersonaPER=(String)vOcultos.get(7);
					// No busco si se ha actualizado el valor de idpersona
					// porque es de tipo modal
				} else {
					throw new ClsExceptions("El valor del parametro (concepto) no es adecuado");
				}
			}

			boolean altaJG=false;
	     	if (idInstitucionJG==null || idInstitucionJG.trim().equals("") || idPersonaJG==null || idPersonaJG.trim().equals("")) {
	     		altaJG=true;
	     	}
			
	     	tx.commit();
	     	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		return this.exitoRefresco("messages.updated.success",request);
	}
	
	/**
	 * Metodo que implementa el modo Ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String Ver (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
			miform.setAccionE("ver");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
     	return this.abrirModal(mapping,formulario,request,response);
	}

	/**
	 * Metodo que implementa el modo Editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String Editar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
			miform.setAccionE("editar");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
     	return this.abrirModal(mapping,formulario,request,response);
	}
	
	/**
	 * Metodo que implementa el modo abrirModal
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirModal (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
	     	String ididioma=user.getLanguage();
			
			// para la clave de la persona JG
			String idInstitucionJG="", idPersonaJG="";
			// para la clave de la persona JG (caso de representante)
			String idInstitucionPER="", idPersonaPER="";
			// para la clave de EJG
			String idInstitucionEJG="", idTipoEJG="", idAnioEJG="", idNumeroEJG="";
			// para la clave de SOJ
			String idInstitucionSOJ="", idTipoSOJ="", idAnioSOJ="", idNumeroSOJ="";
			// para la clave de Designa
			String idInstitucionDesigna="", idTurnoDesigna="", idAnioDesigna="", idNumeroDesigna="";
			// para la clave de Asistencia
			String idInstitucionAsistencia="", idAnioAsistencia="", idNumeroAsistencia="";

	     	// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);

			// obtener Concepto
			String concepto = (String)vOcultos.get(0);
			// obtener titulos
			String titulo = (String)vOcultos.get(1);
			String localizacion = (String)vOcultos.get(2);
			// obtener Accion
			//String accion = (String)vOcultos.get(3);
			// obtener Pantalla
			//String pantalla = (String)vOcultos.get(4);
			//if (pantalla!=null && pantalla.equals(this.PESTANA)) {
			//	this.abrirPestana(mapping, formulario, request, response);
			//}
			
			// obtener Persona
			idInstitucionJG = (String)vOcultos.get(4);
			idPersonaJG = (String)vOcultos.get(5);
			
			if (concepto==null) {
				throw new ClsExceptions("Falta de parámetro obligatorio (concepto)");
			} else {
				if (concepto.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
					idInstitucionDesigna=(String)vOcultos.get(6);
					idTurnoDesigna=(String)vOcultos.get(7);
					idAnioDesigna=(String)vOcultos.get(8);
					idNumeroDesigna=(String)vOcultos.get(9);
					// No busco si se ha actualizado el valor de idpersona
					// porque es de tipo modal
					
				} else
					if (concepto.equals(PersonaJGAction.DESIGNACION_INTERESADO)) {
						idInstitucionDesigna=(String)vOcultos.get(6);
						idTurnoDesigna=(String)vOcultos.get(7);
						idAnioDesigna=(String)vOcultos.get(8);
						idNumeroDesigna=(String)vOcultos.get(9);
						// No busco si se ha actualizado el valor de idpersona
						// porque es de tipo modal
						
					} else
						if (concepto.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) {
							idInstitucionAsistencia=(String)vOcultos.get(6);
							idAnioAsistencia=(String)vOcultos.get(7);
							idNumeroAsistencia=(String)vOcultos.get(8);
							// busco si se ha actualizado el valor de idpersonaJG
							ScsAsistenciasAdm adm = new ScsAsistenciasAdm(this.getUserBean(request));
							Hashtable ht = new Hashtable();
							ht.put(ScsAsistenciasBean.C_IDINSTITUCION,idInstitucionAsistencia);
							ht.put(ScsAsistenciasBean.C_ANIO,idAnioAsistencia);
							ht.put(ScsAsistenciasBean.C_NUMERO,idNumeroAsistencia);
							Vector v = adm.selectByPK(ht);
							if (v!=null && v.size()>0) {
								ScsAsistenciasBean b = (ScsAsistenciasBean) v.get(0);
								if (b.getIdPersonaJG()!=null) {
									idInstitucionJG = b.getIdInstitucion().toString();
									idPersonaJG = b.getIdPersonaJG().toString();
								}
							}
						}else 
							if (concepto.equals(PersonaJGAction.EJG_CONTRARIOS)) {
							idInstitucionEJG=(String)vOcultos.get(6);
							idAnioEJG=(String)vOcultos.get(7);
							idNumeroEJG=(String)vOcultos.get(8);
							idTipoEJG=(String)vOcultos.get(9);
							
					     }	else
							if (concepto.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
								idInstitucionAsistencia=(String)vOcultos.get(6);
								idAnioAsistencia=(String)vOcultos.get(7);
								idNumeroAsistencia=(String)vOcultos.get(8);
								// No busco si se ha actualizado el valor de idpersona
								// porque es de tipo modal
							} else
								if (concepto.equals(PersonaJGAction.EJG)) {
									idInstitucionEJG=(String)vOcultos.get(6);
									idTipoEJG=(String)vOcultos.get(7);
									idAnioEJG=(String)vOcultos.get(8);
									idNumeroEJG=(String)vOcultos.get(9);
									// busco si se ha actualizado el valor de idpersonaJG
									ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
									Hashtable ht = new Hashtable();
									ht.put(ScsEJGBean.C_IDINSTITUCION,idInstitucionEJG);
									ht.put(ScsEJGBean.C_IDTIPOEJG,idTipoEJG);
									ht.put(ScsEJGBean.C_ANIO,idAnioEJG);
									ht.put(ScsEJGBean.C_NUMERO,idNumeroEJG);
									Vector v = adm.selectByPK(ht);
									if (v!=null && v.size()>0) {
										ScsEJGBean b = (ScsEJGBean) v.get(0);
										if (b.getIdPersonaJG()!=null) {
											idInstitucionJG = b.getIdInstitucion().toString();
											idPersonaJG = b.getIdPersonaJG().toString();
										}
									}
								} else
									if (concepto.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
										idInstitucionEJG=(String)vOcultos.get(6);
										idTipoEJG=(String)vOcultos.get(7);
										idAnioEJG=(String)vOcultos.get(8);
										idNumeroEJG=(String)vOcultos.get(9);
										// No busco si se ha actualizado el valor de idpersona
										// porque es de tipo modal
									} else
				if (concepto.equals(PersonaJGAction.SOJ)) {
					idInstitucionSOJ=(String)vOcultos.get(6);
					idTipoSOJ=(String)vOcultos.get(7);
					idAnioSOJ=(String)vOcultos.get(8);
					idNumeroSOJ=(String)vOcultos.get(9);
					// busco si se ha actualizado el valor de idpersonaJG
					ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(this.getUserBean(request));
					Hashtable ht = new Hashtable();
					ht.put(ScsSOJBean.C_IDINSTITUCION,idInstitucionSOJ);
					ht.put(ScsSOJBean.C_IDTIPOSOJ,idTipoSOJ);
					ht.put(ScsSOJBean.C_ANIO,idAnioSOJ);
					ht.put(ScsSOJBean.C_NUMERO,idNumeroSOJ);
					Vector v = adm.selectByPK(ht);
					if (v!=null && v.size()>0) {
						ScsSOJBean b = (ScsSOJBean) v.get(0);
						if (b.getIdPersonaJG()!=null) {
							idInstitucionJG = b.getIdInstitucion().toString();
							idPersonaJG = b.getIdPersonaJG().toString();
						}
					}
				} else
				if (concepto.equals(PersonaJGAction.PERSONAJG)) {
					idInstitucionPER=(String)vOcultos.get(6);
					idPersonaPER=(String)vOcultos.get(7);
					// No busco si se ha actualizado el valor de idpersona
					// porque es de tipo modal
				} else {
					throw new ClsExceptions("El valor del parametro (concepto) no es adecuado");
				}
			}

			boolean altaJG=false;
	     	if (idInstitucionJG==null || idInstitucionJG.trim().equals("") || idPersonaJG==null || idPersonaJG.trim().equals("")) {
	     		altaJG=true;
	     	}
			

	     	// carga del form
			// si viene el tipo de pantalla se respeta. Si no se pone el adecuado
	     	if (miform.getPantallaE()!=null && miform.getPantallaE().equals("P")) {
				miform.setPantalla("P");
	     	} else {
				miform.setPantalla("M");
	     	}
	     	miform.setConceptoE(concepto);
	     	miform.setLocalizacionE(localizacion);
	     	//miform.setAccionE(accion);

	     	miform.setIdInstitucionJG(idInstitucionJG);
	     	miform.setIdPersonaJG(idPersonaJG);
	     	
	     	miform.setIdInstitucionDES(idInstitucionDesigna);
	     	miform.setIdTurnoDES(idTurnoDesigna);
	     	miform.setAnioDES(idAnioDesigna);
	     	miform.setNumeroDES(idNumeroDesigna);
	     	
	     	miform.setIdInstitucionASI(idInstitucionAsistencia);
	     	miform.setAnioASI(idAnioAsistencia);
	     	miform.setNumeroASI(idNumeroAsistencia);
	     	
	     	miform.setIdInstitucionEJG(idInstitucionEJG);
	     	miform.setIdTipoEJG(idTipoEJG);
	     	miform.setAnioEJG(idAnioEJG);
	     	miform.setNumeroEJG(idNumeroEJG);

	     	miform.setIdInstitucionSOJ(idInstitucionSOJ);
	     	miform.setIdTipoSOJ(idTipoSOJ);
	     	miform.setAnioSOJ(idAnioSOJ);
	     	miform.setNumeroSOJ(idNumeroSOJ);

	     	miform.setIdInstitucionPER(idInstitucionPER);
	     	miform.setIdPersonaPER(idPersonaPER);

	     	miform = this.rellenarFormulario(miform,request);
     	
	     	CenTipoIdentificacionAdm tipoIdentificacionAdm = new CenTipoIdentificacionAdm(user);
		List<CenTipoIdentificacionBean>  alTipos = tipoIdentificacionAdm.getTipoPersona(ididioma);
		if(alTipos==null){
			alTipos = new ArrayList<CenTipoIdentificacionBean>();				}
		
		miform.setTipos(alTipos);	
		//Para recuperar el tipo idpersona Juridica o Fisica si hay un dato ya guardado.
		String idTipoPersona= miform.getIdTipoPersona();
		if((idTipoPersona==null)||(miform.getIdTipoPersona().equals("F"))){			
			List<CenTipoIdentificacionBean>  identificadores = tipoIdentificacionAdm.getTiposIdentificacion(ididioma);
			if(identificadores==null){				
				identificadores = new ArrayList<CenTipoIdentificacionBean>();		
			}
				miform.setIdentificadores(identificadores);
		}else{
			List<CenTipoIdentificacionBean> alTipoIdentificaciones = null;
					alTipoIdentificaciones = tipoIdentificacionAdm.getTiposIdentificaciones(ididioma, miform.getIdTipoPersona().toString());
						if(alTipoIdentificaciones==null){
							alTipoIdentificaciones = new ArrayList<CenTipoIdentificacionBean>();
			
						}
						miform.setIdentificadores(alTipoIdentificaciones);
		}
			
			// indico si es modal
			//request.setAttribute("personaJGpantalla","M");
	     	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "personaJG";
	}

	/**
	 * Metodo que implementa el modo abrirPestana
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirPestana (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
	     	String ididioma=user.getLanguage();
			
			// para la clave de la persona JG
			String idInstitucionJG="", idPersonaJG="";
			// para la clave de la persona JG (caso de representante)
			String idInstitucionPER="", idPersonaPER="";
			// para la clave de EJG
			String idInstitucionEJG="", idTipoEJG="", anioEJG="", numeroEJG="";
			// para la clave de SOJ
			String idInstitucionSOJ="", idTipoSOJ="", anioSOJ="", numeroSOJ="";
			// para la clave de Designa
			String idInstitucionDesigna="", idTurnoDesigna="", anioDesigna="", numeroDesigna="";
			// para la clave de Asistencia
			String idInstitucionAsistencia="", anioAsistencia="", numeroAsistencia="";

	     	// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			//Vector vOcultos = miform.getDatosTablaOcultos(0);

			// obtener Concepto
			String concepto = request.getParameter("conceptoE");
			


			// obtener titulos
			String titulo = request.getParameter("tituloE");
			String localizacion = request.getParameter("localizacionE");
			// obtener Accion
			String accion = request.getParameter("accionE");
			// obtener Pantalla
			//String pantalla = request.getParameter("pantallaE");
			//if (pantalla!=null && pantalla.equals(this.MODAL)) {
			//	this.abrirModal(mapping, formulario, request, response);
			//}
			// obtener Persona
			idInstitucionJG = request.getParameter("idInstitucionJG");
			idPersonaJG = request.getParameter("idPersonaJG");
			
			//Obtener el tipo PCAJG
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(idInstitucionJG));					
			  request.setAttribute("pcajgActivo", tipoCAJG);
			ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request));
			if(idPersonaJG!=null && idPersonaJG.toString().trim().equals("")){
				String minusvaliaDefecto = perAdm.getMinusvaliaDefecto(idInstitucionJG);					
				  request.setAttribute("minusvaliaDefecto", minusvaliaDefecto);
			}else
				request.setAttribute("valiaDefecto", null);
			  
			if (concepto==null) {
				throw new ClsExceptions("Falta de parámetro obligatorio (concepto)");
			} else {
				if (concepto.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) ) {
					idInstitucionDesigna=request.getParameter("idInstitucionDES");
					idTurnoDesigna=request.getParameter("idTurnoDES");
					anioDesigna=request.getParameter("anioDES");
					numeroDesigna=request.getParameter("numeroDES");
					// No busco si se ha actualizado el valor de idpersona
					// porque es de tipo modal
				} else
					if (concepto.equals(PersonaJGAction.DESIGNACION_INTERESADO) ) {
						idInstitucionDesigna=request.getParameter("idInstitucionDES");
						idTurnoDesigna=request.getParameter("idTurnoDES");
						anioDesigna=request.getParameter("anioDES");
						numeroDesigna=request.getParameter("numeroDES");
						// No busco si se ha actualizado el valor de idpersona
						// porque es de tipo modal
					} else
					if (concepto.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
						idInstitucionAsistencia=request.getParameter("idInstitucionASI");
						anioAsistencia=request.getParameter("anioASI");
						numeroAsistencia=request.getParameter("numeroASI");
						// No busco si se ha actualizado el valor de idpersona
						// porque es de tipo modal
					} else
					if (concepto.equals(PersonaJGAction.EJG_CONTRARIOS)) {
							idInstitucionEJG=request.getParameter("idInstitucionEJG");
							anioEJG=request.getParameter("anioEJG");
							numeroEJG=request.getParameter("numeroEJG");
							idTipoEJG=request.getParameter("idTipoEJG");
							// No busco si se ha actualizado el valor de idpersona
							// porque es de tipo modal
					} else
						if (concepto.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) {
							idInstitucionAsistencia=request.getParameter("idInstitucionASI");
							anioAsistencia=request.getParameter("anioASI");
							numeroAsistencia=request.getParameter("numeroASI");
							// busco si se ha actualizado el valor de idpersonaJG
							ScsAsistenciasAdm adm = new ScsAsistenciasAdm(this.getUserBean(request));
							Hashtable ht = new Hashtable();
							ht.put(ScsAsistenciasBean.C_IDINSTITUCION,idInstitucionAsistencia);
							ht.put(ScsAsistenciasBean.C_ANIO,anioAsistencia);
							ht.put(ScsAsistenciasBean.C_NUMERO,numeroAsistencia);
							Vector v = adm.selectByPK(ht);
							if (v!=null && v.size()>0) {
								ScsAsistenciasBean b = (ScsAsistenciasBean) v.get(0);
								if (b.getIdPersonaJG()!=null) {
									idInstitucionJG = b.getIdInstitucion().toString();
									idPersonaJG = b.getIdPersonaJG().toString();
								}
							}
						} else
					if (concepto.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
						idInstitucionEJG=request.getParameter("idInstitucionEJG");
						idTipoEJG=request.getParameter("idTipoEJG");
						anioEJG=request.getParameter("anioEJG");
						numeroEJG=request.getParameter("numeroEJG");
						// No busco si se ha actualizado el valor de idpersona
						// porque es de tipo modal
						
						// Buscamos los datos del interesado dado de alta en EJG para precargar sus datos de dirección
						ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
						Hashtable ht = new Hashtable();
						ht.put(ScsEJGBean.C_IDINSTITUCION,idInstitucionEJG);
						ht.put(ScsEJGBean.C_IDTIPOEJG,idTipoEJG);
						ht.put(ScsEJGBean.C_ANIO,anioEJG);
						ht.put(ScsEJGBean.C_NUMERO,numeroEJG);
						Vector v = adm.selectByPK(ht);
						if (v!=null && v.size()>0) {
							ScsEJGBean b = (ScsEJGBean) v.get(0);
							if (b.getIdPersonaJG()!=null) {
								String idPersonaJGSol = b.getIdPersonaJG().toString();
								// Obtenemos los datos de esta persona
							ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm(this.getUserBean(request));
							Hashtable hPersona = new Hashtable();
							hPersona.put(ScsPersonaJGBean.C_IDINSTITUCION,idInstitucionEJG);
							hPersona.put(ScsPersonaJGBean.C_IDPERSONA,idPersonaJGSol);
								Vector vPersona = personaAdm.selectByPK(hPersona);
								ScsPersonaJGBean personaBean = (ScsPersonaJGBean) vPersona.get(0);
								miform.setDireccion(personaBean.getDireccion());
								miform.setCp(personaBean.getCodigoPostal());
								miform.setProvincia(personaBean.getIdProvincia());
								miform.setPoblacion(personaBean.getIdPoblacion());
								miform.setNacionalidad(personaBean.getIdPais());
								miform.setExisteDomicilio(personaBean.getExisteDomicilio());
							}
						}
					} else
						if (concepto.equals(PersonaJGAction.EJG)) {
							idInstitucionEJG=request.getParameter("idInstitucionEJG");
							idTipoEJG=request.getParameter("idTipoEJG");
							anioEJG=request.getParameter("anioEJG");
							numeroEJG=request.getParameter("numeroEJG");
							// busco si se ha actualizado el valor de idpersonaJG
							ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
							Hashtable ht = new Hashtable();
							ht.put(ScsEJGBean.C_IDINSTITUCION,idInstitucionEJG);
							ht.put(ScsEJGBean.C_IDTIPOEJG,idTipoEJG);
							ht.put(ScsEJGBean.C_ANIO,anioEJG);
							ht.put(ScsEJGBean.C_NUMERO,numeroEJG);
							Vector v = adm.selectByPK(ht);
							if (v!=null && v.size()>0) {
								ScsEJGBean b = (ScsEJGBean) v.get(0);
								if (b.getIdPersonaJG()!=null) {
									idInstitucionJG = b.getIdInstitucion().toString();
									idPersonaJG = b.getIdPersonaJG().toString();
								}
							}
						} else
				if (concepto.equals(PersonaJGAction.SOJ)) {
					idInstitucionSOJ=request.getParameter("idInstitucionSOJ");
					idTipoSOJ=request.getParameter("idTipoSOJ");
					anioSOJ=request.getParameter("anioSOJ");
					numeroSOJ=request.getParameter("numeroSOJ");
					// busco si se ha actualizado el valor de idpersonaJG
					ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(this.getUserBean(request));
					Hashtable ht = new Hashtable();
					ht.put(ScsSOJBean.C_IDINSTITUCION,idInstitucionSOJ);
					ht.put(ScsSOJBean.C_IDTIPOSOJ,idTipoSOJ);
					ht.put(ScsSOJBean.C_ANIO,anioSOJ);
					ht.put(ScsSOJBean.C_NUMERO,numeroSOJ);
					Vector v = adm.selectByPK(ht);
					if (v!=null && v.size()>0) {
						ScsSOJBean b = (ScsSOJBean) v.get(0);
						if ((b.getIdPersonaJG()!=null && !b.getIdPersonaJG().equals("") && !b.getIdPersonaJG().equals("null") )) {
							idInstitucionJG = b.getIdInstitucion().toString();
							idPersonaJG = b.getIdPersonaJG().toString();
						}
					}
					
					ScsBeneficiarioSOJAdm admBenef = new ScsBeneficiarioSOJAdm(this.getUserBean(request));
					Hashtable htBenef = new Hashtable();
					ht.put(ScsBeneficiarioSOJBean.C_IDINSTITUCION,idInstitucionSOJ);
					ht.put(ScsBeneficiarioSOJBean.C_IDPERSONA,idPersonaJG);
					Vector vBenef = admBenef.selectByPK(ht);
					if (vBenef!=null && vBenef.size()>0) {
						ScsBeneficiarioSOJBean bBenef = (ScsBeneficiarioSOJBean) vBenef.get(0);
						miform.setTipoConoce(String.valueOf(bBenef.getIdTipoConoce()));
						miform.setTipoGrupoLaboral(String.valueOf(bBenef.getIdTipoGrupoLab()));
						miform.setNumVecesSOJ(String.valueOf(bBenef.getNVecesSOJ()));
						miform.setChkPideJG(bBenef.getSolicitaJG());
						miform.setChkSolicitaInfoJG(bBenef.getSolicitaInfoSOJ());
					}
				} else 
				if (concepto.equals(PersonaJGAction.PERSONAJG)) {
					idInstitucionPER=request.getParameter("idInstitucionPER");
					idPersonaPER=request.getParameter("idPersonaPER");
					// No busco si se ha actualizado el valor de idpersona
					// porque es de tipo modal
				} else {
					throw new ClsExceptions("El valor del parametro (concepto) no es adecuado");
				}
			}

			boolean altaJG=false;
	     	if (idInstitucionJG==null || idInstitucionJG.trim().equals("") || idPersonaJG==null || idPersonaJG.trim().equals("")) {
	     		altaJG=true;
	     	}

			// carga del form
			// si viene el tipo de pantalla se respeta. Si no se pone el adecuado
	     	if (miform.getPantallaE()!=null && miform.getPantallaE().equals("M")) {
				miform.setPantalla("M");
	     	} else {
				miform.setPantalla("P");
	     	}
	     	miform.setConceptoE(concepto);
	     	miform.setLocalizacionE(localizacion);
	     	miform.setAccionE(accion);
	     	miform.setIdInstitucionJG(idInstitucionJG);
	     	miform.setIdPersonaJG(idPersonaJG);
	     	
	     	miform.setIdInstitucionDES(idInstitucionDesigna);
	     	miform.setIdTurnoDES(idTurnoDesigna);
	     	miform.setAnioDES(anioDesigna);
	     	miform.setNumeroDES(numeroDesigna);
	     	
	     	miform.setIdInstitucionASI(idInstitucionAsistencia);
	     	miform.setAnioASI(anioAsistencia);
	     	miform.setNumeroASI(numeroAsistencia);
	     	
	     	miform.setIdInstitucionEJG(idInstitucionEJG);
	     	miform.setIdTipoEJG(idTipoEJG);
	     	miform.setAnioEJG(anioEJG);
	     	miform.setNumeroEJG(numeroEJG);

	     	miform.setIdInstitucionSOJ(idInstitucionSOJ);
	     	miform.setIdTipoSOJ(idTipoSOJ);
	     	miform.setAnioSOJ(anioSOJ);
	     	miform.setNumeroSOJ(numeroSOJ);

	     	miform.setIdInstitucionPER(idInstitucionPER);
	     	miform.setIdPersonaPER(idPersonaPER);

	     	miform = this.rellenarFormulario(miform,request);  
	     	
		    CenTipoIdentificacionAdm tipoIdentificacionAdm = new CenTipoIdentificacionAdm(user);
			List<CenTipoIdentificacionBean>  alTipos = tipoIdentificacionAdm.getTipoPersona(ididioma);
			if(alTipos==null){
				alTipos = new ArrayList<CenTipoIdentificacionBean>();
			}
			
			miform.setTipos(alTipos);	
			//Para recuperar el tipo idpersona Juridica o Fisica si hay un dato ya guardado.
			String idTipoPersona= miform.getIdTipoPersona();
			if((idTipoPersona==null)||(miform.getIdTipoPersona().equals(ClsConstants.TIPO_PERSONA_FISICA))){			
				List<CenTipoIdentificacionBean>  identificadores = tipoIdentificacionAdm.getTiposIdentificacion(ididioma);
				if(identificadores==null){				
					identificadores = new ArrayList<CenTipoIdentificacionBean>();		
				}
					miform.setIdentificadores(identificadores);
			}else{
				List<CenTipoIdentificacionBean> alTipoIdentificaciones = null;
						alTipoIdentificaciones = tipoIdentificacionAdm.getTiposIdentificaciones(ididioma, miform.getIdTipoPersona().toString());
							if(alTipoIdentificaciones==null){
								alTipoIdentificaciones = new ArrayList<CenTipoIdentificacionBean>();
				
							}
							miform.setIdentificadores(alTipoIdentificaciones);
			}
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "personaJG";
	}

	/**
	 * Metodo que implementa el modo buscarNIF
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarNIF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
	     	Object objDataBackup =  request.getSession().getAttribute("DATABACKUP");
	     	Hashtable dataBackup = null;
	     	if(objDataBackup!=null){
		     	if(objDataBackup instanceof Hashtable){
		     		dataBackup =  (Hashtable)objDataBackup;
		     	}else{
		     		dataBackup = new Hashtable();
		     		request.getSession().setAttribute("DATABACKUP", dataBackup);
		     	}
	     	}else{
	     		dataBackup = new Hashtable();
	     		request.getSession().setAttribute("DATABACKUP", dataBackup);
	     	}
     		

	     	// recupero el idpersona anterior
	     	Hashtable hashAnt = null;
	     	if (miform.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
	     		hashAnt = (Hashtable)dataBackup.get("PERSONAPERSONA");
	     	} else {
	     		hashAnt = (Hashtable)dataBackup.get(ScsPersonaJGBean.T_NOMBRETABLA);
	     	}

			// Consultamos las personas con el nif indicado
	     	Vector resultadoNIF1 = new Vector();
			ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
			String elNif = miform.getNIdentificacion().toUpperCase();
			String tipoNif = miform.getTipoId();
			String nuevoNif =formateaNif(elNif,tipoNif);
			miform.setNIdentificacion(nuevoNif);
			ScsPersonaJGBean person = new ScsPersonaJGBean();
			person.setNif(nuevoNif);
			person.setTipoIdentificacion(tipoNif);
			person.setNombre("");
			person.setApellido1("");
			person.setApellido2("");
			person.setDireccion("");
			person.setExisteDomicilio("S");
			person.setCodigoPostal("");
			person.setCorreoElectronico("");
			person.setFax("");
			person.setEdad("");			
			resultadoNIF1.add(person);
			request.setAttribute("resultadoNIF", resultadoNIF1);
			//Quitamos caracteres no alfanumericos, 
			// anhadimos 0 por delante hasta completar los 20 caracteres maximo
			// y lo dejamos todo en mayusculas
			// La configuracion del sistema tambien ignora acentos y demas en las letras
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),nuevoNif);
			codigos.put(new Integer(2),user.getLocation());
			String where = 
				" where upper(lpad(regexp_replace(nif, '[^[:alnum:]]', ''), 20, '0')) = " +
				"       upper(lpad(regexp_replace(:1, '[^[:alnum:]]', ''), 20, '0')) " +
				"   and idinstitucion=:2";
			Vector resultadoNIF = admBean.selectBind(where,codigos);
			
			// RGG 18-04-2006 actualizo el databackup para que no me de error el update
			if (resultadoNIF!=null && resultadoNIF.size()>0) {
			
				Hashtable hash = new Hashtable();

				ScsPersonaJGBean perBean = (ScsPersonaJGBean) resultadoNIF.get(0);
				if (perBean.getIdRepresentanteJG()!=null) {
					ScsPersonaJGAdm adm = new ScsPersonaJGAdm(this.getUserBean(request));
					request.setAttribute("nombreRepresentante",adm.getNombreApellidos(perBean.getIdRepresentanteJG().toString(),perBean.getIdInstitucion().toString()));
				}
				// lo guardamos en el databuckup
				// OJO, utilizo setForCompare porque traspaso beans, y para mi caso, que es luego compararlo
				// en el update necesito que si me viene un nulo, se escriba el elemento con un blanco.
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDINSTITUCION,perBean.getIdInstitucion().toString());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPERSONA,perBean.getIdPersona().toString());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NIF,perBean.getNif().toString());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NOMBRE,perBean.getNombre());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO1,perBean.getApellido1());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO2,perBean.getApellido2());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_DIRECCION,perBean.getDireccion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CODIGOPOSTAL,perBean.getCodigoPostal());						
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FECHANACIMIENTO,perBean.getFechaNacimiento());			
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROFESION,perBean.getIdProfesion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDMINUSVALIA,perBean.getIdMinusvalia());				
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPAIS,perBean.getIdPais());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROVINCIA,perBean.getIdProvincia());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPOBLACION,perBean.getIdPoblacion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ESTADOCIVIL,perBean.getIdEstadoCivil());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL,perBean.getRegimenConyugal());			 
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_TIPOPERSONAJG,perBean.getTipo());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,perBean.getTipoIdentificacion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ENCALIDADDE,perBean.getEnCalidadDe());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_OBSERVACIONES,perBean.getObservaciones());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG,perBean.getIdRepresentanteJG());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_SEXO,perBean.getSexo());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_HIJOS,perBean.getHijos());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EDAD,perBean.getEdad());				
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FAX,perBean.getFax());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EXISTEDOMICILIO,perBean.getExisteDomicilio());	
				if(perBean.getCorreoElectronico()!=null)
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CORREOELECTRONICO,perBean.getCorreoElectronico().trim());	
				
				// cuelgo el anterior
				if (hashAnt!=null) {
					String idPersonaAnt= (String) hashAnt.get(ScsPersonaJGBean.C_IDPERSONA);
					dataBackup.put("idPersonaAnt",idPersonaAnt);
				}
				
				//dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,hash);
				if (miform.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
					dataBackup.put("PERSONAPERSONA",hash);
				} else {
					dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,hash);
				}
				
				// borro del databakcup los datos que no son de la persona
				//dataBackup.remove(ScsDefendidosDesignaBean.T_NOMBRETABLA);
				//dataBackup.remove(ScsContrariosDesignaBean.T_NOMBRETABLA);
				//dataBackup.remove(ScsContrariosAsistenciaBean.T_NOMBRETABLA);
				//dataBackup.remove(ScsUnidadFamiliarEJGBean.T_NOMBRETABLA);			
				request.getSession().setAttribute("DATABACKUP",dataBackup);
			
			}
			
			
			//LMSP Se redirige a un jsp que se carga en el frame oculto, y hace todo lo demás :)
			if(resultadoNIF.size()!=0)
				request.setAttribute("resultadoNIF",resultadoNIF);
			
			if (miform.getNombreObjetoDestino() != null) {
				request.setAttribute("NombreObjetoDestino", miform.getNombreObjetoDestino());
			}
			
				
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "buscarNIF";
	}
	private String formateaNif(String nif, String tipo){
		String nif1="";
		if(nif==null)
			return "";
		//letras correctas para un nif
        String caracteres = "TRWAGMYFPDXBNJZSQVHLCKE";
        // quito el guion si lo hay
        String nif_sin_guion = nif.replaceAll("-","");
        nif_sin_guion = nif.replaceAll(" ","");
        //quito espacios en blanco
        
        nif = nif_sin_guion.trim();
        if(nif.equals(""))
        	return "";
        //calculo la longitud
        int longitud_nif = nif.length();
        String expresion="[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]";
        Pattern patron = null;
        Matcher coincidencias_inicio = null;
        Matcher coincidencias_fin = null;
        patron = Pattern.compile(expresion);
        String inicio=nif.substring(0,1);
        String fin=nif.substring(longitud_nif - 1);
        coincidencias_inicio = patron.matcher(inicio);
        coincidencias_fin = patron.matcher(fin);
        try{
						/************** nif x-9999999 ************************/
	        if( tipo==null || !tipo.trim().equals("10")){
	        	return nif;
	        }else if (coincidencias_inicio.find()){
				        //guardo el nif sin la letra
				        nif1 = nif.substring(1,longitud_nif);
				        //algoritmo
				        while(nif1.length()<7)
				        	nif1="0"+nif1;
				        int nif2 =  Integer.parseInt(nif1);
				        int posicion = (nif2 % 23)+1;
				        String caracter = caracteres.substring(posicion-1,posicion);
				        
				        return inicio+nif1;
				}
				/************** nif 9999999-x ************************/
				else if (coincidencias_fin.find()){
				        //guardo el nif sin la letra
				        nif1 = nif.substring(0,longitud_nif-1);
				        //algoritmo
				        while(nif1.length()<7)
				        	nif1="0"+nif1;
				        int nif2 =  Integer.parseInt(nif1);
				        int posicion = (nif2 % 23)+1;
				        String caracter = caracteres.substring(posicion-1,posicion);
			                return nif1+fin;
				        
				}else{
					nif1 = nif.substring(0,longitud_nif);
			        //algoritmo
			        while(nif1.length()<7)
			        	nif1="0"+nif1;
			        /*
			        int nif2 =  Integer.parseInt(nif1);
			        int posicion = (nif2 % 23)+1;
			        String caracter = caracteres.substring(posicion-1,posicion);*/
			        return nif1;
				}
        }
		catch(Exception e) {
			return nif;
		}
		
	}

	/**
	 * Metodo que carga los datos del formulario segun las claves que se les pase
	 * @param  formulario -  Action Form asociado a este Action
	 * @return  formulario  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	private PersonaJGForm rellenarFormulario (PersonaJGForm miform, HttpServletRequest request) throws ClsExceptions  
	{
		Hashtable dataBackup = new Hashtable();
		Hashtable hash = new Hashtable();
		UsrBean user = new UsrBean();
		String ididioma=user.getLanguage();
		try {
			// El siguiente condigo comentado no tiene sentido en este metodo,
			//pues aqui no se lee DATABACKUP, solo se escribe.
			// Además da error en determinados casos.
			//dataBackup = (Hashtable) request.getSession().
			//		getAttribute("DATABACKUP");
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		}
		catch(Exception e) {
			throw new ClsExceptions
					(e, "ERROR>> Al recoger el USRBEAN de la Sesion. " +
							e.getMessage());
		}
		
		try {
			//if (miform.getAccionE().equals("nuevo")) {
			//	miform.setIdPersonaJG("");
			//}

			Integer idRepresentanteJG = null;
			
			// Datos Persona
			if (miform.getIdInstitucionJG()!=null && (miform.getIdPersonaJG()!=null && !miform.getIdPersonaJG().equals("null") && !miform.getIdPersonaJG().equals(""))) {
				hash = new Hashtable();
				hash.put(ScsPersonaJGBean.C_IDINSTITUCION,miform.getIdInstitucionJG());
				hash.put(ScsPersonaJGBean.C_IDPERSONA,miform.getIdPersonaJG());
				ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request)); 
				Vector v = perAdm.selectByPK(hash);
				if (v!=null && v.size()>0) {
					ScsPersonaJGBean perBean = (ScsPersonaJGBean) v.get(0);
					
					// lo guardamos en el databuckup
					// OJO, utilizo setForCompare porque traspaso beans, y para mi caso, que es luego compararlo
					// en el update necesito que si me viene un nulo, se escriba el elemento con un blanco.
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDINSTITUCION,perBean.getIdInstitucion().toString());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPERSONA,perBean.getIdPersona().toString());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NIF,perBean.getNif().toString());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NOMBRE,perBean.getNombre());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO1,perBean.getApellido1());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO2,perBean.getApellido2());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_DIRECCION,perBean.getDireccion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CODIGOPOSTAL,perBean.getCodigoPostal());						
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FECHANACIMIENTO,perBean.getFechaNacimiento());			
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROFESION,perBean.getIdProfesion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDMINUSVALIA,perBean.getIdMinusvalia());					
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPAIS,perBean.getIdPais());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROVINCIA,perBean.getIdProvincia());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPOBLACION,perBean.getIdPoblacion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ESTADOCIVIL,perBean.getIdEstadoCivil());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL,perBean.getRegimenConyugal());			 
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_TIPOPERSONAJG,perBean.getTipo());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,perBean.getTipoIdentificacion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ENCALIDADDE,perBean.getEnCalidadDe());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_OBSERVACIONES,perBean.getObservaciones());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG,perBean.getIdRepresentanteJG());	
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_SEXO,perBean.getSexo());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDIOMA,perBean.getIdioma());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_HIJOS,perBean.getHijos());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EDAD,perBean.getEdad());					
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FAX,perBean.getFax());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CORREOELECTRONICO,perBean.getCorreoElectronico());	
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EXISTEDOMICILIO,perBean.getExisteDomicilio());	
					
					idRepresentanteJG=perBean.getIdRepresentanteJG();
					
				// pdm recuperamos la descripcion del tipo de identificacion y de la poblacion porque no se pintaban
					
					CenTipoIdentificacionAdm tipoIdent=new CenTipoIdentificacionAdm(this.getUserBean(request));
					if (perBean.getTipoIdentificacion()!=null && !perBean.getTipoIdentificacion().equals("")){
						String where=" WHERE "+CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION+" = "+perBean.getTipoIdentificacion();
						Vector v1=tipoIdent.select(where);
						CenTipoIdentificacionBean tipoIdentBean = (CenTipoIdentificacionBean) v1.get(0);
						request.setAttribute("identificacion",tipoIdentBean.getDescripcion());
					}else{
						request.setAttribute("identificacion","");	
					}
					
					if (!perBean.getIdPoblacion().trim().equals("")) {
						CenPoblacionesAdm poblacion=new CenPoblacionesAdm(this.getUserBean(request));
						String where1=" WHERE "+CenPoblacionesBean.C_IDPOBLACION+" = "+perBean.getIdPoblacion();
						Vector v2=poblacion.select(where1);
						CenPoblacionesBean poblacionBean = (CenPoblacionesBean) v2.get(0);
						request.setAttribute("poblacion",poblacionBean.getNombre());
					} else {
						request.setAttribute("poblacion","");
					}
    			//
					
					// cuando viene de personaJG hay que guardarlo en otro sitio en el databackup
					if (miform.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
						dataBackup.put("PERSONAPERSONA",hash);
					} else {
						dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,hash);
					}
					
					// indicamos que la persona existe
					miform.setNuevo("1"); 
					
					miform.setIdTipoPersona(perBean.getTipo());										
					miform.setTipoId(perBean.getTipoIdentificacion());
					miform.setNIdentificacion(perBean.getNif());
					miform.setNombre(perBean.getNombre());
					miform.setApellido1(perBean.getApellido1());
					miform.setApellido2(perBean.getApellido2());
					miform.setDireccion(perBean.getDireccion());
					miform.setCp(perBean.getCodigoPostal());
					miform.setProvincia(perBean.getIdProvincia());
					miform.setPoblacion(perBean.getIdPoblacion());
					miform.setExisteDomicilio(perBean.getExisteDomicilio());
					miform.setPoblacion(perBean.getIdPoblacion());
					miform.setNacionalidad(perBean.getIdPais());
					miform.setSexo(perBean.getSexo());
					miform.setIdioma(perBean.getIdioma());
					if (perBean.getHijos() != null){
						miform.setHijos(perBean.getHijos());
					}
					if (perBean.getEdad() != null){
						miform.setEdad(perBean.getEdad());
					}
					if (perBean.getFechaNacimiento()!=null) {
						miform.setFechaNac(GstDate.getFormatedDateShort(user.getLanguage(),perBean.getFechaNacimiento()));
					}
					if (perBean.getIdEstadoCivil()!=null) {
						miform.setEstadoCivil(perBean.getIdEstadoCivil().toString());
					}
					miform.setRegimenConyugal(perBean.getRegimenConyugal());
					if (perBean.getIdProfesion()!=null) {
						miform.setProfesion(perBean.getIdProfesion().toString());
					}
					if (perBean.getIdMinusvalia()!=null) {
						miform.setMinusvalia(perBean.getIdMinusvalia().toString());
					}					
					if (!miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_CONTRARIOS) && !miform.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
						// para el caso de desgina contrarios, el nombre sale de la tabal contrarios designa
						if (perBean.getIdRepresentanteJG()!=null) {
							miform.setIdRepresentanteJG(perBean.getIdRepresentanteJG().toString());
							ScsPersonaJGAdm adm = new ScsPersonaJGAdm(this.getUserBean(request));
							miform.setRepresentante(adm.getNombreApellidos(perBean.getIdRepresentanteJG().toString(),perBean.getIdInstitucion().toString()));
						}
					}
					miform.setObservaciones(perBean.getObservaciones());
					
										
					//Se recupara una lista de los telefonos de la personajg
					
					ScsTelefonosPersonaJGAdm admTelefonosJG = new ScsTelefonosPersonaJGAdm(this.getUserBean(request));					
					if(perBean.getIdPersona()!=null){
							List<ScsTelefonosPersonaJGBean> listaTelefonos = admTelefonosJG.getListadoTelefonosPersonaJG(perBean.getIdPersona().toString(), perBean.getIdInstitucion().toString());
							if(listaTelefonos==null){
								listaTelefonos = new ArrayList<ScsTelefonosPersonaJGBean>();			
							}								
							miform.setTelefonos(listaTelefonos);
						}
					miform.setFax(perBean.getFax());	
				    miform.setCorreoElectronico(perBean.getCorreoElectronico().trim());	
					    
				}

				if (!miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_CONTRARIOS) && !miform.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
					if (idRepresentanteJG!=null) {
						// busco el nombe y apellidos del representante
						Hashtable hashRep = new Hashtable();
						hashRep.put(ScsPersonaJGBean.C_IDINSTITUCION,miform.getIdInstitucionJG());
						hashRep.put(ScsPersonaJGBean.C_IDPERSONA,idRepresentanteJG);
						ScsPersonaJGAdm perAdm2 = new ScsPersonaJGAdm(this.getUserBean(request)); 
						Vector v2 = perAdm2.selectByPK(hashRep);
						if (v2!=null && v2.size()>0) {
							ScsPersonaJGBean perBean = (ScsPersonaJGBean) v2.get(0);
							String nomRep = perBean.getNombre() + " " + perBean.getApellido1() + " " + perBean.getApellido2();
							request.setAttribute("nombreRepresentanteJG",nomRep);
						}
					}
				}			
			}
			
			if (miform.getConceptoE()==null) {
				throw new ClsExceptions("Falta de parámetro obligatorio (concepto)");
			} else {
				if (miform.getConceptoE().equals(PersonaJGAction.EJG_CONTRARIOS) ) {
					//  9.1.2009 INC_CAT_8
					if (miform.getIdPersonaJG()!=null) {

						hash = new Hashtable();
						hash.put(ScsContrariosEJGBean.C_IDINSTITUCION,miform.getIdInstitucionEJG());
						hash.put(ScsContrariosEJGBean.C_ANIO,miform.getAnioEJG());
						hash.put(ScsContrariosEJGBean.C_NUMERO,miform.getNumeroEJG());
						hash.put(ScsContrariosEJGBean.C_IDPERSONA,miform.getIdPersonaJG());
						hash.put(ScsContrariosEJGBean.C_IDTIPOEJG,miform.getIdTipoEJG());
						ScsContrariosEJGAdm unidadFAdm = new ScsContrariosEJGAdm(this.getUserBean(request)); 
						Vector v = unidadFAdm.selectByPK(hash);
						if (v!=null && v.size()>0) {
							ScsContrariosEJGBean ufBean = (ScsContrariosEJGBean) v.get(0);
							// lo guardamos en el databuckup
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_ANIO,ufBean.getAnio());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_FECHAMODIFICACION,ufBean.getFechaMod());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDINSTITUCION,ufBean.getIdInstitucion());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDPERSONA,ufBean.getIdPersona());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_NUMERO,ufBean.getNumero());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_OBSERVACIONES,ufBean.getObservaciones());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_USUMODIFICACION,ufBean.getUsuMod());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDINSTITUCION_PROCU,ufBean.getIdInstitucionProcurador());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDPROCURADOR,ufBean.getIdProcurador());	
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDABOGADOCONTRARIOEJG,ufBean.getIdAbogadoContrarioEjg());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_NOMBREABOGADOCONTRARIOEJG,ufBean.getNombreAbogadoContrarioEjg());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDREPRESENTANTEEJG,ufBean.getIdRepresentanteEjg());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_NOMBREREPRESENTANTEEJG,ufBean.getNombreRepresentanteEjg());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_IDREPRESENTANTEEJG,ufBean.getIdRepresentanteEjg());
							UtilidadesHash.setForCompare(hash, ScsContrariosEJGBean.C_NOMBREREPRESENTANTEEJG,ufBean.getNombreRepresentanteEjg());							
							dataBackup.put(ScsContrariosEJGBean.T_NOMBRETABLA,hash);						
							
							
							if (ufBean.getIdAbogadoContrarioEjg()!=null && !ufBean.getIdAbogadoContrarioEjg().equals("")) {								
								miform.setIdAbogadoContrarioEJG(ufBean.getIdAbogadoContrarioEjg());
								Vector numeroColegiado = new Vector();
								ScsContrariosEJGAdm contrariosEjgAdm = new ScsContrariosEJGAdm(this.getUserBean(request));
								numeroColegiado= contrariosEjgAdm.selectGenerico(contrariosEjgAdm.getNcolegiadoAbogado(ufBean.getIdAbogadoContrarioEjg()));
								if (numeroColegiado.size()>0){
									String numeroColegiadoAbogado= UtilidadesHash.getString((Hashtable)numeroColegiado.get(0),"NCOLEGIADOABOGADO");										
									miform.setNcolegiadoContrario(numeroColegiadoAbogado);
								}
							}else{
							    miform.setIdAbogadoContrarioEJG("");
							    miform.setNcolegiadoContrario(" ");
							    }	
							
							if (ufBean.getNombreAbogadoContrarioEjg()!=null && !ufBean.getNombreAbogadoContrarioEjg().equals("")) {								
									miform.setAbogadoContrarioEJG(ufBean.getNombreAbogadoContrarioEjg());
							}else{
							     miform.setAbogadoContrarioEJG("");
							    }
							
							miform.setIdRepresentanteJG(ufBean.getIdRepresentanteEjg());
							miform.setRepresentante(ufBean.getNombreRepresentanteEjg());
							
							
							miform.setObservaciones(ufBean.getObservaciones());
							if (ufBean.getIdProcurador()!=null) {
								miform.setIdProcurador(ufBean.getIdProcurador().toString()+ "," + ufBean.getIdInstitucionProcurador().toString());
							}
							
							int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(miform.getIdInstitucionJG()));					
						    request.setAttribute("pcajgActivo", tipoCAJG);
							
						} else {
							dataBackup.remove(ScsContrariosDesignaBean.T_NOMBRETABLA);
						}
					}
				}else
					
				
				if (miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_INTERESADO) ) {
					if (miform.getIdPersonaJG()!=null) {						
						hash = new Hashtable();
						hash.put(ScsDefendidosDesignaBean.C_IDINSTITUCION,miform.getIdInstitucionDES());
						hash.put(ScsDefendidosDesignaBean.C_IDTURNO,miform.getIdTurnoDES());
						hash.put(ScsDefendidosDesignaBean.C_ANIO,miform.getAnioDES());
						hash.put(ScsDefendidosDesignaBean.C_NUMERO,miform.getNumeroDES());
						hash.put(ScsDefendidosDesignaBean.C_IDPERSONA,miform.getIdPersonaJG());
						ScsDefendidosDesignaAdm unidadFAdm = new ScsDefendidosDesignaAdm(this.getUserBean(request)); 
						Vector v = unidadFAdm.select(hash);
						if (v!=null && v.size()>0) {
							ScsDefendidosDesignaBean ufBean = (ScsDefendidosDesignaBean) v.get(0);
							// lo guardamos en el databuckup
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_ANIO,ufBean.getAnio());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE,ufBean.getNombreRepresentante());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_FECHAMODIFICACION,ufBean.getFechaMod());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_IDINSTITUCION,ufBean.getIdInstitucion());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_IDTURNO,ufBean.getIdTurno());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_NUMERO,ufBean.getNumero());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_OBSERVACIONES,ufBean.getObservaciones());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_IDPERSONA,ufBean.getIdPersona());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_USUMODIFICACION,ufBean.getUsuMod());
							UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_CALIDAD,ufBean.getCalidad());
							
							if (ufBean.getIdTipoenCalidad()!=null){								
								UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_IDTIPOENCALIDAD,ufBean.getIdTipoenCalidad());
								UtilidadesHash.setForCompare(hash, ScsDefendidosDesignaBean.C_CALIDADIDINSTITUCION,ufBean.getCalidadIdinstitucion());
							}
							
							dataBackup.put(ScsDefendidosDesignaBean.T_NOMBRETABLA,hash);	
							

							if (ufBean.getIdTipoenCalidad()!=null){	
								miform.setCalidad(ufBean.getIdTipoenCalidad().toString());
								miform.setIdTipoenCalidad(ufBean.getIdTipoenCalidad().toString());
								miform.setCalidadIdinstitucion(ufBean.getCalidadIdinstitucion().toString());
							}
							
							int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(miform.getIdInstitucionDES()));					
						    request.setAttribute("pcajgActivo", tipoCAJG);
					
						} else {
							dataBackup.remove(ScsDefendidosDesignaBean.T_NOMBRETABLA);
						}
					}
					  int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(miform.getIdInstitucionDES()));					
						    request.setAttribute("pcajgActivo", tipoCAJG);
				} else
					if (miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
						
						if (miform.getIdPersonaJG()!=null) {
							
							hash = new Hashtable();
							hash.put(ScsContrariosDesignaBean.C_IDINSTITUCION,miform.getIdInstitucionDES());
							hash.put(ScsContrariosDesignaBean.C_IDTURNO,miform.getIdTurnoDES());
							hash.put(ScsContrariosDesignaBean.C_ANIO,miform.getAnioDES());
							hash.put(ScsContrariosDesignaBean.C_NUMERO,miform.getNumeroDES());
							hash.put(ScsContrariosDesignaBean.C_IDPERSONA,miform.getIdPersonaJG());
							ScsContrariosDesignaAdm unidadFAdm = new ScsContrariosDesignaAdm(this.getUserBean(request)); 
							Vector v = unidadFAdm.selectByPK(hash);
							if (v!=null && v.size()>0) {
								ScsContrariosDesignaBean ufBean = (ScsContrariosDesignaBean) v.get(0);
								// lo guardamos en el databuckup
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_ANIO,ufBean.getAnio());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE,ufBean.getNombreRepresentante());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDREPRESENTANTELEGAL,ufBean.getIdRepresentanteLegal());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_FECHAMODIFICACION,ufBean.getFechaMod());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDINSTITUCION,ufBean.getIdInstitucion());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDTURNO,ufBean.getIdTurno());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_NUMERO,ufBean.getNumero());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_OBSERVACIONES,ufBean.getObservaciones());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDPERSONA,ufBean.getIdPersona());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_USUMODIFICACION,ufBean.getUsuMod());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR,ufBean.getIdInstitucionProcurador());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDPROCURADOR,ufBean.getIdProcurador());
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_NOMBREABOGADOCONTRARIO,ufBean.getnombreAbogadoContrario());	
								UtilidadesHash.setForCompare(hash, ScsContrariosDesignaBean.C_IDABOGADOCONTRARIO,ufBean.getIdAbogadoContrario());	
								dataBackup.put(ScsContrariosDesignaBean.T_NOMBRETABLA,hash);						
								
								
								miform.setObservaciones(ufBean.getObservaciones());
								if (ufBean.getIdProcurador()!=null) {
									miform.setIdProcurador(ufBean.getIdProcurador().toString()+ "," + ufBean.getIdInstitucionProcurador().toString());
								}
								miform.setIdPersonaRepresentante(ufBean.getIdRepresentanteLegal());
								if (ufBean.getIdRepresentanteLegal()!=null && !ufBean.getIdRepresentanteLegal().equals("")) {
									CenPersonaAdm adm = new CenPersonaAdm(this.getUserBean(request));
									miform.setRepresentante(adm.obtenerNombreApellidos(ufBean.getIdRepresentanteLegal()));
									Vector nColegiadoRepresentante = new Vector();
									ScsContrariosDesignaAdm AbogadoReresententeAdm = new ScsContrariosDesignaAdm (this.getUserBean(request));									
									nColegiadoRepresentante= AbogadoReresententeAdm.selectGenerico(AbogadoReresententeAdm.getNcolegiadoRepresentante(ufBean.getIdRepresentanteLegal()));
									if (nColegiadoRepresentante.size()>0){
										String numeroColegiadoRepresentante= UtilidadesHash.getString((Hashtable)nColegiadoRepresentante.get(0),"NCOLEGIADOABOGADO");										
										miform.setNcolegiadoRepresentante(numeroColegiadoRepresentante);
									}
								} else {
										miform.setRepresentante(" ");
										miform.setNcolegiadoRepresentante("");
									  }
									
								if (ufBean.getIdAbogadoContrario()!=null && !ufBean.getIdAbogadoContrario().equals("")) {
									CenPersonaAdm adm = new CenPersonaAdm(this.getUserBean(request));
									miform.setAbogadoContrario(adm.obtenerNombreApellidos(ufBean.getIdAbogadoContrario()));
									miform.setIdPersonaContrario(ufBean.getIdAbogadoContrario());
									Vector numeroColegiado = new Vector();
									ScsContrariosEJGAdm contrariosEjgAdm = new ScsContrariosEJGAdm(this.getUserBean(request));
									numeroColegiado= contrariosEjgAdm.selectGenerico(contrariosEjgAdm.getNcolegiadoAbogado(ufBean.getIdAbogadoContrario()));
									if (numeroColegiado.size()>0){
										String numeroColegiadoAbogado= UtilidadesHash.getString((Hashtable)numeroColegiado.get(0),"NCOLEGIADOABOGADO");										
										miform.setNcolegiadoContrario(numeroColegiadoAbogado);
									}									
								} else {
									miform.setAbogadoContrario("");
									miform.setNcolegiadoContrario(" ");
									miform.setIdPersonaContrario("");
								}
								
								int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(miform.getIdInstitucionDES()));					
								request.setAttribute("pcajgActivo", tipoCAJG);
								
							} else {
								dataBackup.remove(ScsContrariosDesignaBean.T_NOMBRETABLA);
							}
						}
					} else
						if (miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
							if (miform.getIdPersonaJG()!=null) {
								hash = new Hashtable();
								hash.put(ScsContrariosAsistenciaBean.C_IDINSTITUCION,miform.getIdInstitucionASI());
								hash.put(ScsContrariosAsistenciaBean.C_ANIO,miform.getAnioASI());
								hash.put(ScsContrariosAsistenciaBean.C_NUMERO,miform.getNumeroASI());
								hash.put(ScsContrariosAsistenciaBean.C_IDPERSONA,miform.getIdPersonaJG());
								ScsContrariosAsistenciaAdm unidadFAdm = new ScsContrariosAsistenciaAdm(this.getUserBean(request)); 
								Vector v = unidadFAdm.selectByPK(hash);
								if (v!=null && v.size()>0) {
									ScsContrariosAsistenciaBean ufBean = (ScsContrariosAsistenciaBean) v.get(0);
									// lo guardamos en el databuckup
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_IDINSTITUCION,ufBean.getIdInstitucion());
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_NUMERO,ufBean.getNumero());
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_ANIO,ufBean.getAnio());
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_IDPERSONA,ufBean.getIdPersona());
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_OBSERVACIONES,ufBean.getObservaciones());
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_FECHAMODIFICACION,ufBean.getFechaMod());
									UtilidadesHash.setForCompare(hash, ScsContrariosAsistenciaBean.C_USUMODIFICACION,ufBean.getUsuMod());
									dataBackup.put(ScsContrariosAsistenciaBean.T_NOMBRETABLA,hash);
									
									
									miform.setObservaciones(ufBean.getObservaciones());
								} else {
									dataBackup.remove(ScsContrariosAsistenciaBean.T_NOMBRETABLA);
								}
							}
				} else
				if (miform.getConceptoE().equals(PersonaJGAction.EJG) || miform.getConceptoE().equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
					if (miform.getIdPersonaJG()!=null && !miform.getIdPersonaJG().equals("")) {
						hash = new Hashtable();
						hash.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,miform.getIdInstitucionEJG());
						hash.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,miform.getIdTipoEJG());
						hash.put(ScsUnidadFamiliarEJGBean.C_ANIO,miform.getAnioEJG());
						hash.put(ScsUnidadFamiliarEJGBean.C_NUMERO,miform.getNumeroEJG());
						hash.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,miform.getIdPersonaJG());
						ScsUnidadFamiliarEJGAdm unidadFAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request)); 
						Vector v = unidadFAdm.selectByPK(hash);
						if (v!=null && v.size()>0) {
							ScsUnidadFamiliarEJGBean ufBean = (ScsUnidadFamiliarEJGBean) v.get(0);
							// lo guardamos en el databuckup
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,ufBean.getIdInstitucion());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_ANIO,ufBean.getAnio());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_NUMERO,ufBean.getNumero());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,ufBean.getIdTipoEJG());				
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IDPERSONA,ufBean.getIdPersona());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,ufBean.getObservaciones());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,ufBean.getEnCalidadDe());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IDPARENTESCO,ufBean.getIdParentesco());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,ufBean.getSolicitante());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,ufBean.getDescripcionIngresosAnuales());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,ufBean.getIngresosAnuales());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,ufBean.getBienesInmuebles());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,ufBean.getImoporteBienesInmuebles());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,ufBean.getBienesMuebles());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,ufBean.getImoporteBienesMuebles());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_OTROSBIENES,ufBean.getOtrosBienes());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,ufBean.getImporteOtrosBienes());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB,ufBean.getTipoGrupoLab());
							UtilidadesHash.setForCompare(hash,ScsUnidadFamiliarEJGBean.C_TIPOINGRESO,ufBean.getTipoIngreso());
							dataBackup.put(ScsUnidadFamiliarEJGBean.T_NOMBRETABLA,hash);
							
							if (ufBean.getImoporteBienesInmuebles()!=null)
								miform.setImporteBienesInmuebles(ufBean.getImoporteBienesInmuebles().toString());
							miform.setBienesInmuebles(ufBean.getBienesInmuebles());
							if (ufBean.getImoporteBienesMuebles()!=null)
								miform.setImporteBienesMuebles(ufBean.getImoporteBienesMuebles().toString());
							miform.setBienesMuebles(ufBean.getBienesMuebles());
							if (ufBean.getIngresosAnuales()!=null) {
								miform.setImporteIngresosAnuales(ufBean.getIngresosAnuales().toString());
								miform.setIngresosAnuales(ufBean.getDescripcionIngresosAnuales().toString());
							}
							if (ufBean.getImporteOtrosBienes()!=null)
								miform.setImporteOtrosBienes(ufBean.getImporteOtrosBienes().toString());
							miform.setOtrosBienes(ufBean.getOtrosBienes());
							miform.setUnidadObservaciones(ufBean.getObservaciones());
							miform.setEnCalidadDeLibre(ufBean.getEnCalidadDe());
							if (ufBean.getTipoGrupoLab()!=null)
							  miform.setTipoGrupoLaboral(ufBean.getTipoGrupoLab().toString());
							if (ufBean.getTipoIngreso()!=null)
								miform.setTipoIngreso(ufBean.getTipoIngreso().toString());
							if (ufBean.getIdParentesco()!=null)
								  miform.setParentesco(ufBean.getIdParentesco().toString());
							
							if (ufBean.getSolicitante()!=null)
								  miform.setSolicitante(ufBean.getSolicitante().toString());
						} else {
							dataBackup.remove(ScsUnidadFamiliarEJGBean.T_NOMBRETABLA);
						}
					}
					//jbd 19/10/2009 Recuperamos el estado de activacion PCAJG
					int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(miform.getIdInstitucionEJG()));
					//jbd 19/01/2010 Ahora debe ser numerico para distinguir entre los distintos tipos de CAJG 
					/*if (tipoCAJG>1){
						request.setAttribute("pcajgActivo","true");
					}else{
						request.setAttribute("pcajgActivo","false");
					}*/
					request.setAttribute("pcajgActivo", tipoCAJG);

				} else
					if (miform.getConceptoE().equals(PersonaJGAction.SOJ)) {

				} else
					if (miform.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
						// Recuperamos el parametro repPCAJG que indica que es representante y necesita direccion
						request.setAttribute("pcajgActivo",request.getParameter("repPCAJG"));
						if (miform.getIdRepresentanteJG()!=null) {
							hash = new Hashtable();
							hash.put(ScsPersonaJGBean.C_IDINSTITUCION,miform.getIdInstitucionJG());
							hash.put(ScsPersonaJGBean.C_IDPERSONA,miform.getIdRepresentanteJG());
							ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request)); 
							Vector v = perAdm.selectByPK(hash);
							
							
							if (v!=null && v.size()>0) {
								ScsPersonaJGBean perBean = (ScsPersonaJGBean) v.get(0);
								
								// lo guardamos en el databuckup
								// OJO, utilizo setForCompare porque traspaso beans, y para mi caso, que es luego compararlo
								// en el update necesito que si me viene un nulo, se escriba el elemento con un blanco.
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDINSTITUCION,perBean.getIdInstitucion().toString());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPERSONA,perBean.getIdPersona().toString());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NIF,perBean.getNif().toString());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NOMBRE,perBean.getNombre());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO1,perBean.getApellido1());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO2,perBean.getApellido2());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_DIRECCION,perBean.getDireccion());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CODIGOPOSTAL,perBean.getCodigoPostal());						
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FECHANACIMIENTO,perBean.getFechaNacimiento());			
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROFESION,perBean.getIdProfesion());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPAIS,perBean.getIdPais());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROVINCIA,perBean.getIdProvincia());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPOBLACION,perBean.getIdPoblacion());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ESTADOCIVIL,perBean.getIdEstadoCivil());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL,perBean.getRegimenConyugal());			 
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_TIPOPERSONAJG,perBean.getTipo());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,perBean.getTipoIdentificacion());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ENCALIDADDE,perBean.getEnCalidadDe());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_OBSERVACIONES,perBean.getObservaciones());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG,perBean.getIdRepresentanteJG());		
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean. C_IDREPRESENTANTEJG,perBean.getIdRepresentanteJG());
								UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EXISTEDOMICILIO,perBean.getExisteDomicilio());	
								
								
								idRepresentanteJG=perBean.getIdRepresentanteJG();
								// cuando viene de personaJG hay que guardarlo en otro sitio en el databackup
								dataBackup.put("PERSONAPERSONA",hash);
							    
								// indicamos quela persona existe
								miform.setNuevo("1"); 
								
								miform.setTipo(perBean.getTipo());
								miform.setTipoId(perBean.getTipoIdentificacion());
								miform.setNIdentificacion(perBean.getNif());
								miform.setNombre(perBean.getNombre());
								miform.setApellido1(perBean.getApellido1());
								miform.setApellido2(perBean.getApellido2());
								miform.setDireccion(perBean.getDireccion());
								miform.setExisteDomicilio(perBean.getExisteDomicilio());
								miform.setCp(perBean.getCodigoPostal());
								miform.setProvincia(perBean.getIdProvincia());
								miform.setPoblacion(perBean.getIdPoblacion());
								miform.setNacionalidad(perBean.getIdPais());
								if (perBean.getFechaNacimiento()!=null) {
									miform.setFechaNac(GstDate.getFormatedDateShort(user.getLanguage(),perBean.getFechaNacimiento()));
								}
								if (perBean.getIdEstadoCivil()!=null) {
									miform.setEstadoCivil(perBean.getIdEstadoCivil().toString());
								}
								miform.setRegimenConyugal(perBean.getRegimenConyugal());
								if (perBean.getIdProfesion()!=null) {
									miform.setProfesion(perBean.getIdProfesion().toString());
								}
								if (perBean.getIdMinusvalia()!=null) {
									miform.setMinusvalia(perBean.getIdMinusvalia().toString());
								}
								miform.setObservaciones(perBean.getObservaciones());
							}							
							
						}					
						

				} else {
					if (miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_ASISTIDO) ) {

				} else
					throw new ClsExceptions("El valor del parametro (concepto) no es adecuado");
				}
			}

			// lo guardamos en session para luego actualizar
			request.getSession().setAttribute("DATABACKUP", dataBackup);

		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error rellenando el form para mostrar. " + e.getMessage());
		}
		return miform;
	}

	
	
	/**
	 * Metodo que implementa el guardado de una persona para luego crear la relacion pertinente
	 * con un asunto (ejg, soj,...)
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarPersonaJG (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		String mapDestino = ""; // La salida del metodo de la relacion
		ScsTelefonosPersonaJGAdm admTelefonosJG =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));								
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		PersonaJGForm miform = (PersonaJGForm)formulario;
		MasterForm form = (MasterForm)formulario;
		String accion = request.getParameter("accionE");
		String relacion = form.getModo();
		String action = (String)request.getServletPath();

		try {

	     	boolean nuevaPersona = false;
	     	// Datos Persona
			Hashtable persona= new Hashtable();
			ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request));
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDINSTITUCION,user.getLocation());

			// Si tenemos el idPersona es porque ya existia la persona asi que la actualizamos,
			// si no creamos una nueva
			if (miform.getIdPersonaJG()!=null && !miform.getIdPersonaJG().equalsIgnoreCase("")){
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPERSONA,miform.getIdPersonaJG().toString());
			} else {
				nuevaPersona = true;
				persona = perAdm.prepararInsert(persona);
				miform.setIdPersonaJG((String)persona.get(ScsPersonaJGBean.C_IDPERSONA));
			}
			
			// DATOS DE LA PERSONAJG
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDINSTITUCION,user.getLocation());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPERSONA,miform.getIdPersonaJG().toString());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_NIF,miform.getNIdentificacion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_NOMBRE,miform.getNombre());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_APELLIDO1,miform.getApellido1());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_APELLIDO2,miform.getApellido2());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate("",miform.getFechaNac()));
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROFESION,miform.getProfesion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDMINUSVALIA,miform.getMinusvalia());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_ESTADOCIVIL,miform.getEstadoCivil());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_REGIMENCONYUGAL,miform.getRegimenConyugal());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_TIPOPERSONAJG,miform.getIdTipoPersona());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,miform.getTipoId());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_ENCALIDADDE,miform.getEnCalidadDe());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_OBSERVACIONES,miform.getObservaciones());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDREPRESENTANTEJG,miform.getIdRepresentanteJG());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_SEXO,miform.getSexo());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDIOMA,miform.getIdioma());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_HIJOS,miform.getHijos());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_EDAD,miform.getEdad());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_FAX,miform.getFax());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_CORREOELECTRONICO,miform.getCorreoElectronico());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_EXISTEDOMICILIO,miform.getExisteDomicilio());			
			if (miform.getDireccion()==null)
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_DIRECCION,"");
			else
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_DIRECCION,miform.getDireccion());
			if(miform.getCp()==null)
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_CODIGOPOSTAL,"");
			else
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_CODIGOPOSTAL,miform.getCp());

			if(miform.getProvincia()==null)
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROVINCIA,"");
			else		
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROVINCIA,miform.getProvincia());
			if(miform.getPoblacion()==null)
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPOBLACION,"");
			else
				UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPOBLACION,miform.getPoblacion());
			
	     	
			// recojo el databackup
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			String anio="";
			String numero="";
			String tipo="";
			if((action.equalsIgnoreCase("/JGR_AsistidoAsistenciaLetrado.do"))){
				// Caso especial para el asistido desde la ficha colegial 
				anio = miform.getAnioASI();
				numero = miform.getNumeroASI();
				// Recuperamos las relaciones con otros asuntos
				Vector relaciones = perAdm.getRelacionesPersonaJGAsistencia(miform.getIdPersonaJG(), user.getLocation(), tipo, anio, numero);
				// La busqueda es distinta porque no queremos que salgan los asuntos relacionados con la asistencia
				if(relaciones!=null && relaciones.size()>0){
					// Si encontramos alguna relacion tendremos que crear una persona nueva para no machacar los datos de la antigua
					nuevaPersona = true;
					persona = perAdm.prepararInsert(persona);
					miform.setIdPersonaJG((String)persona.get(ScsPersonaJGBean.C_IDPERSONA));
				}// Si no hay mas relaciones continuamos para update
			}else if((miform.getAccionGuardar()!=null && miform.getAccionGuardar().equalsIgnoreCase("insert"))){
				nuevaPersona = true;
				persona = perAdm.prepararInsert(persona);
				miform.setIdPersonaJG((String)persona.get(ScsPersonaJGBean.C_IDPERSONA));		
			}else if(miform.getAccionGuardar()==null || miform.getAccionGuardar().equalsIgnoreCase("")){
				if (relacion.equalsIgnoreCase("guardarEJG")||
					relacion.equalsIgnoreCase("guardarContrariosEjg")){
					anio = miform.getAnioEJG();
					numero = miform.getNumeroEJG();
					tipo = miform.getIdTipoEJG();
				} else if (relacion.equalsIgnoreCase("guardarSOJ")){
					anio = miform.getAnioSOJ();
					numero = miform.getNumeroSOJ();
					tipo = miform.getIdTipoSOJ();
				} else if (relacion.equalsIgnoreCase("guardarDesigna")){
					anio = miform.getAnioDES();
					numero = miform.getNumeroDES();
					tipo = miform.getIdTurnoDES();
				} else if (relacion.equalsIgnoreCase("guardarAsistencia")){
					anio = miform.getAnioASI();
					numero = miform.getNumeroASI();
					tipo = "";
				}
				Vector relaciones = perAdm.getRelacionesPersonaJG(miform.getIdPersonaJG(), user.getLocation(), relacion, tipo, anio, numero);
				if(relaciones!=null && relaciones.size()>0){
					miform.setModoGuardar(miform.getModo());
					miform.setAccionGuardar("-");
					return  "asuntosPersonaJG";
				}
			}// continuamos para update
			
			// INSERTAR PERSONA JG SI PROCEDE
			request.setAttribute("nuevaPersona", nuevaPersona);
			// Sacado del if // calculo de la persona anterior, que solo funcionara cuando exista persona anterior
			Hashtable oldPer = (Hashtable) dataBackup.get(ScsPersonaJGBean.T_NOMBRETABLA);
			// Esto es solo para relacionar personas con personas (representante y esas cosas)
			if(oldPer==null){
				oldPer = (Hashtable) dataBackup.get("PERSONAPERSONA");
			}
			
			// Comienzo control de transacciones 
			tx = user.getTransaction();			
			tx.begin();
			
			if (!nuevaPersona){
				// YA existia // Update Persona
				if (!perAdm.update(persona, oldPer)) {
					throw new ClsExceptions("Error en update persona. " + perAdm.getError());
				}
			} else {
				// NO existia // Insert Persona
				if (!perAdm.insert(persona)) {
					throw new ClsExceptions("Error en insert persona. " + perAdm.getError());
				}
			}
			
			// Una vez hecho esto continuaremos con el resto de acciones que depende
			// del tipo de relacion que estemos guardando
			
			if (relacion.equalsIgnoreCase("guardarEJG")){
				mapDestino = guardarRelacionEJG(mapping, form, request, response);
			} else if (relacion.equalsIgnoreCase("guardarSOJ")){
				mapDestino = guardarRelacionSOJ(mapping, form, request, response);
			} else if (relacion.equalsIgnoreCase("guardarDesigna")){
				mapDestino = guardarRelacionDesigna(mapping, form, request, response);
			} else if (relacion.equalsIgnoreCase("guardarAsistencia")){
				mapDestino = guardarRelacionAsistencia(mapping, form, request, response);
			} else if (relacion.equalsIgnoreCase("guardarContrariosEjg")){
				mapDestino = guardarRelacionContrariosEjg(mapping, form, request, response);	
			} else if (relacion.equalsIgnoreCase("guardarPersona")){
				mapDestino = guardarRelacionPersona(mapping, form, request, response);
			}
			
			// fin de la transaccion
			tx.commit();
			
	    }catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		// Cerramos la transaccion principal y abrimos una nueva para la insercion de telefonos
	    
	    try {  /*se añade numeros de telefonos para una personaJG.*/
			tx = user.getTransaction();			
			tx.begin();
			
			/** Inicio se añade numeros de telefonos para una personaJG.**/
			String lTelefonos="";
			Hashtable miHash =	new Hashtable();					
			miHash.put(ScsTelefonosPersonaJGBean.C_IDINSTITUCION, user.getLocation());
			miHash.put(ScsTelefonosPersonaJGBean.C_IDPERSONA, miform.getIdPersonaJG());								
			lTelefonos=miform.getlNumerosTelefonos();
			
			/* Comprobamos que la lista de telefonos no venga vacia del formularo.*/								 
			if (!lTelefonos.equals("")){									
				List<ScsTelefonosPersonaJGBean> listaTelefonos = admTelefonosJG.getListadoTelefonosPersonaJG(miform.getIdPersonaJG().toString(), user.getLocation().toString());
				  /*Comprobamos que la persona tenga una lista de telefonos para borrar esta lista y posteriormente insertar 
				   * la lTelefonos que ha insertado el usuario.*/
				if(listaTelefonos!=null){
					try {											
							String sql=admTelefonosJG.deleteTelefonos(miHash);
							admTelefonosJG.deleteSQL(sql);												
						} catch (Exception e) {
							   throwExcp("messages.deleted.error",e,tx);
						} 
				}				
				//Recorremos la lista de los telefonos para posteriormente guardar los telefonos.
				GstStringTokenizer tokens = new GstStringTokenizer(lTelefonos,"%%%");  
			    while(tokens.hasMoreTokens()){  
			    	String fila = tokens.nextToken();								    	
			    	if (fila != null && !fila.equals("")) {
			    		
			    		StringTokenizer celdas = new StringTokenizer(fila, "$$~");
			    		String nombreTelefono="";
						String numeroTelefono="";
						String preferenteSms="";
			    		for (int j = 0; celdas.hasMoreElements(); j++) {
			    			String celda = celdas.nextToken();									    			
			    			String[] registro = celda.split("=");
			    			String key = registro[0];
							String value = null;
							
							if(registro.length==2)
								value = registro[1];
							  
							if(key.equals("nombreTelefonoJG")){						
									if(value!=null)
										 nombreTelefono=value;
									else
										nombreTelefono="";
							}
							else if(key.equals("numeroTelefonoJG")){
									if(value!=null)
										 numeroTelefono=value;
									else
										this.exitoModalSinRefresco("el numero",request);
							
							}else if(key.equals("preferenteSms")){
								if(value!=null)
									 preferenteSms=value;
							}
							
			    		}								    		
			    		miHash.put(ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO, nombreTelefono);
			    		miHash.put(ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO, numeroTelefono);
			    		miHash.put(ScsTelefonosPersonaJGBean.C_FECHAMODIFICACION, "sysdate");
			    		miHash.put(ScsTelefonosPersonaJGBean.C_USUMODIFICACION, user.getUserName());
			    		miHash.put(ScsTelefonosPersonaJGBean.C_PREFERENTESMS, preferenteSms);
			    		
			    		//se comprueba el idtelefono para verificar y poner el maximo idtelefono al insertar.
			    		Hashtable htCol = admTelefonosJG.prepararInsert(miHash);	
			    		String maximo = (String)htCol.get("IDTELEFONO");								    		
			    		if (maximo.equals("1")){
			    			miHash.put(ScsTelefonosPersonaJGBean.C_IDTELEFONO,"1");
			    			
			    		}else
			    			miHash.put(ScsTelefonosPersonaJGBean.C_IDTELEFONO,maximo);
			    		//se insertan los telefonos que tenga personajg
			    		
			    		if(!numeroTelefono.trim().equals("")){
			    		if (!admTelefonosJG.insert(miHash)) {
				    			throw new ClsExceptions("Error en insert telefonopersona. " + admTelefonosJG.getError());								    			
				    	}
			    		}
			    		
			    	}
			    	 
			     }  
			} else {
				List<ScsTelefonosPersonaJGBean> listaTelefonos = admTelefonosJG.getListadoTelefonosPersonaJG(miform.getIdPersonaJG().toString(),user.getLocation().toString());
				  /*Comprobamos que la persona tenga una lista de telefonos para borrar esta lista y posteriormente insertar 
				   * la lTelefonos que ha insertado el usuario.*/
				if(listaTelefonos!=null){
					try {											
							String sql=admTelefonosJG.deleteTelefonos(miHash);
							admTelefonosJG.deleteSQL(sql);												
						} catch (Exception e) {
							   throwExcp("messages.deleted.error",e,tx);
						} 
				}				
			}
 
			/**Fin se añade numeros de telefonos para una personaJG.**/
			// fin de la transaccion
			tx.commit();
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},new ClsExceptions(e, "Excepcion en insertTelefono."),tx);
		}
		return mapDestino;
		//return this.exitoModal("messages.updated.success",request);
		
	}

	/**
	 * Guarda la relacion de una personaJG con un EJG, creando la relacion directa con el EJG
	 * y a traves de la unidad familiar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarRelacionEJG (
			ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		String result = this.exito("error.general.yanoexiste",request);
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;
	     	String accion = request.getParameter("accionE");
	     	

	     	// Creamos la clave del EJG
			String idInstitucionEJG=miform.getIdInstitucionEJG();
			String idTipoEJG=miform.getIdTipoEJG();
			String anioEJG=miform.getAnioEJG();
			String numeroEJG=miform.getNumeroEJG();
			
			boolean nuevaPersona = UtilidadesString.stringToBoolean(request.getAttribute("nuevaPersona").toString());

			// Colocamos los datos de la Unidad Familiar
			Hashtable unidadFamiliarBean = new Hashtable();
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,miform.getIdInstitucionJG());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_ANIO,miform.getAnioEJG());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_NUMERO,miform.getNumeroEJG());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,miform.getIdTipoEJG());				
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDPERSONA,miform.getIdPersonaJG());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,miform.getUnidadObservaciones());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,miform.getEnCalidadDeLibre());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDPARENTESCO,miform.getParentesco());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_TIPOINGRESO,miform.getTipoIngreso());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,miform.getIngresosAnuales());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,miform.getImporteIngresosAnuales());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,miform.getBienesInmuebles());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,miform.getImporteBienesInmuebles());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,miform.getBienesMuebles());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,miform.getImporteBienesMuebles());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_OTROSBIENES,miform.getOtrosBienes());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,miform.getImporteOtrosBienes());						 
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,miform.getImporteOtrosBienes());	
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB,miform.getTipoGrupoLaboral());
			if (miform.getConceptoE().equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
				boolean checkSolicitante  = UtilidadesString.stringToBoolean(miform.getSolicitante());
				if (checkSolicitante){
					UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,"1");
				}else{
					UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,"0");
				}
			}else{
				UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,"1");
			}

	     	
			// recojo el databackup
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			
			// RELACIONARLO CON EL EJG (UPDATE NORMAL)
			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
			String idPersonaAnterior = "";
			Hashtable ht = new Hashtable();
			ht.put(ScsEJGBean.C_IDINSTITUCION,idInstitucionEJG);
			ht.put(ScsEJGBean.C_IDTIPOEJG,idTipoEJG);
			ht.put(ScsEJGBean.C_ANIO,anioEJG);
			ht.put(ScsEJGBean.C_NUMERO,numeroEJG);
			Vector v = admEJG.selectByPK(ht);
			
			// Recupero el EJG y compruebo sus datos
			// Lo que hacemos es recuperar el idpersona anterior o dejarlo a null
			if (v!=null && v.size()>0) {
				ScsEJGBean beanEJG = (ScsEJGBean) v.get(0);
				// Interesado
				if (miform.getConceptoE().equals(PersonaJGAction.EJG)) {
					// compruebo si ha cambiado el id persona para la relacion
					if (beanEJG.getIdPersonaJG()!=null && !beanEJG.getIdPersonaJG().toString().equalsIgnoreCase("")) {
						// guardo el idpersona anterior para buscar las relaciones y actualizarlas
						idPersonaAnterior = beanEJG.getIdPersonaJG().toString();
					}else{
						idPersonaAnterior = null;
					}
				// Unidad familiar
				}else if (miform.getConceptoE().equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
					// en este caso lo cojo del databackup de unidad familiar, si existe
					Hashtable oldUF = (Hashtable) dataBackup.get(ScsUnidadFamiliarEJGBean.T_NOMBRETABLA);
					if (oldUF!=null) {
						idPersonaAnterior = (String) oldUF.get(ScsUnidadFamiliarEJGBean.C_IDPERSONA);
					} else {
						idPersonaAnterior = null;
					}				
				
				}
				// Solamente para el interesado se actualiza el EJG
				if (miform.getConceptoE().equals(PersonaJGAction.EJG)) {
					// actualizo la personaJG, que sera el interesado en este caso. 
					beanEJG.setIdPersonaJG(new Integer(miform.getIdPersonaJG()));
					if (!admEJG.updateDirect(beanEJG)) {
						throw new ClsExceptions("Error en updateEJG. " + admEJG.getError());
					}
				}
			
				// INSERTAR O ACTUALIZAR UNIDAD FAMILIAR EJG (RELACIONADO)
				ScsUnidadFamiliarEJGAdm ufAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
				// Si tenemos una persona anterior es porque estamos actualizando a alguien
				// si no hay persona anterior hacemos un insert de la nueva
				if ((idPersonaAnterior!=null)&&(!idPersonaAnterior.equalsIgnoreCase(""))) {
	
					// Si existe una persona anterior la vamos a borrar
					Hashtable borrar = new Hashtable();
					borrar.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,miform.getIdInstitucionEJG());
					borrar.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,miform.getIdTipoEJG());
					borrar.put(ScsUnidadFamiliarEJGBean.C_ANIO,miform.getAnioEJG());
					borrar.put(ScsUnidadFamiliarEJGBean.C_NUMERO,miform.getNumeroEJG());
					borrar.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,idPersonaAnterior);
									
					ScsDocumentacionEJGAdm scsDocumentacionEJGAdm = new ScsDocumentacionEJGAdm(getUserBean(request));
	
					Vector vuf = ufAdm.selectByPK(unidadFamiliarBean);
					// Comprobamos que la persona tenga una relacion existente con el ejg para actualizar o insertar/borrar
					if (vuf!=null && vuf.size()>0) {
					    // UPDATE
					    if (!ufAdm.updateDirect(unidadFamiliarBean,null,ufAdm.getCamposBean())) {
							throw new ClsExceptions("Error en update unidad familiar. " + ufAdm.getError());
						}
					} else {
					    // INSERT
						// Insertamos el nuevo y borramos el viejo
					    if (!ufAdm.insert(unidadFamiliarBean)) {
							throw new ClsExceptions("Error en insert unidad familiar. " + ufAdm.getError());
						}
					    // Update de la documentacion de la persona antigua a la nueva
						scsDocumentacionEJGAdm.copiaDocumentacionPresentador(idInstitucionEJG, idTipoEJG, miform.getAnioEJG(), miform.getNumeroEJG(), idPersonaAnterior, miform.getIdPersonaJG());
					    if (!ufAdm.delete(borrar)) {
							throw new ClsExceptions("Error en delete unidad familiar. " + ufAdm.getError());
						}
					}
				} else {
					// Insert unidad familiar con los nuevos valores
					if (!ufAdm.insert(unidadFamiliarBean)) {
						throw new ClsExceptions("Error en insert unidad familiar. " + ufAdm.getError());
					}
				    
				}
	
			if(miform.getConceptoE().equals(PersonaJGAction.EJG)) {
					request.removeAttribute("sinrefresco");
					result = this.exitoRefresco("messages.updated.success",request);
					} else {					
					request.removeAttribute("sinrefresco");
					result = this.exitoModal("messages.updated.success",request);
				}
			}
			
				
		}
		catch (Exception e) {

			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return result;
	}	
	
	/**
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarRelacionSOJ (
			ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		String result = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;

	     	// clave del SOJ
			String idInstitucionSOJ=miform.getIdInstitucionSOJ();
			String idTipoSOJ=miform.getIdTipoSOJ();
			String anioSOJ=miform.getAnioSOJ();
			String numeroSOJ=miform.getNumeroSOJ(); 	

			boolean nuevaPersona = UtilidadesString.stringToBoolean(request.getAttribute("nuevaPersona").toString());

			if (nuevaPersona) {
				
				Hashtable htBeneficiario = new Hashtable();
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_IDINSTITUCION,idInstitucionSOJ);
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_IDPERSONA,miform.getIdPersonaJG());
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_IDTIPOCONOCE,miform.getTipoConoce());
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_IDTIPOGRUPOLAB,miform.getTipoGrupoLaboral());
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_NUMVECESSOJ,miform.getNumVecesSOJ());
				
				ScsBeneficiarioSOJAdm admBenefSOJ = new ScsBeneficiarioSOJAdm(this.getUserBean(request));
				boolean bPideJG  = UtilidadesString.stringToBoolean(miform.getChkPideJG());
				boolean bsolicitaInfoJG  = UtilidadesString.stringToBoolean(miform.getChkSolicitaInfoJG());
				if (bPideJG){
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAJG,ClsConstants.DB_TRUE);
				}else{
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAJG,ClsConstants.DB_FALSE);
				}
				
				boolean bSolicitaInfoJG  = UtilidadesString.stringToBoolean(miform.getChkSolicitaInfoJG());
				if (bsolicitaInfoJG){
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAINFOJG,ClsConstants.DB_TRUE);
				}else{
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAINFOJG,ClsConstants.DB_FALSE);
				}
				
				if (!admBenefSOJ.insert(htBeneficiario)) {
					throw new ClsExceptions("Error en insert BeneficiarioSOJ. " + admBenefSOJ.getError());
				}
				
			} else {
				
				Hashtable htBeneficiario = new Hashtable();
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_IDINSTITUCION,idInstitucionSOJ);
				htBeneficiario.put(ScsBeneficiarioSOJBean.C_IDPERSONA,miform.getIdPersonaJG());
				UtilidadesHash.set(htBeneficiario,ScsBeneficiarioSOJBean.C_IDTIPOCONOCE,miform.getTipoConoce());
				UtilidadesHash.set(htBeneficiario,ScsBeneficiarioSOJBean.C_IDTIPOGRUPOLAB,miform.getTipoGrupoLaboral());
				UtilidadesHash.set(htBeneficiario,ScsBeneficiarioSOJBean.C_NUMVECESSOJ,miform.getNumVecesSOJ());
				
				ScsBeneficiarioSOJAdm admBenefSOJ = new ScsBeneficiarioSOJAdm(this.getUserBean(request));
				boolean bPideJG  = UtilidadesString.stringToBoolean(miform.getChkPideJG());
				boolean bsolicitaInfoJG  = UtilidadesString.stringToBoolean(miform.getChkSolicitaInfoJG());
				if (bPideJG){
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAJG,ClsConstants.DB_TRUE);
				}else{
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAJG,ClsConstants.DB_FALSE);
				}
				
				boolean bSolicitaInfoJG  = UtilidadesString.stringToBoolean(miform.getChkSolicitaInfoJG());
				if (bsolicitaInfoJG){
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAINFOJG,ClsConstants.DB_TRUE);
				}else{
					htBeneficiario.put(ScsBeneficiarioSOJBean.C_SOLICITAINFOJG,ClsConstants.DB_FALSE);
				}
				
				String campos[] = {ScsBeneficiarioSOJBean.C_IDTIPOCONOCE,
						ScsBeneficiarioSOJBean.C_IDTIPOGRUPOLAB,
						ScsBeneficiarioSOJBean.C_NUMVECESSOJ,
						ScsBeneficiarioSOJBean.C_SOLICITAJG,
						ScsBeneficiarioSOJBean.C_SOLICITAINFOJG};
				if (!admBenefSOJ.delete(htBeneficiario)) {
					;
				} 	
				if (!admBenefSOJ.insert(htBeneficiario)) {
					throw new ClsExceptions("Error al actuar BeneficiarioSOJ. " + admBenefSOJ.getError());
				}

			}
			
			// RELACIONARLO CON EL SOJ (UPDATE NORMAL)
			ScsDefinirSOJAdm admSOJ= new ScsDefinirSOJAdm(this.getUserBean(request));
			String idPersonaAnterior = "";
			Hashtable ht = new Hashtable();
			ht.put(ScsSOJBean.C_IDINSTITUCION,idInstitucionSOJ);
			ht.put(ScsSOJBean.C_IDTIPOSOJ,idTipoSOJ);
			ht.put(ScsSOJBean.C_ANIO,anioSOJ);
			ht.put(ScsSOJBean.C_NUMERO,numeroSOJ);
			Vector v = admSOJ.selectByPK(ht);
			if (v!=null && v.size()>0) {
				ScsSOJBean beanSOJ= (ScsSOJBean) v.get(0);
				// actualizo la personaJG, que sera el interesado en este caso. 
				beanSOJ.setIdPersonaJG(new Integer(miform.getIdPersonaJG()));
				if (!admSOJ.updateDirect(beanSOJ)) {
					throw new ClsExceptions("Error en update SOJ. " + admSOJ.getError());
				}
			}

		    result = this.exitoRefresco("messages.updated.success",request);
	     	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return result;
	}	
	
	/**
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarRelacionDesigna (
			ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		String result = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;

	     	// clave de la designa
			String idInstitucionDES=miform.getIdInstitucionDES();
			String idTurnoDES=miform.getIdTurnoDES();
			String anioDES=miform.getAnioDES();
			String numeroDES=miform.getNumeroDES();
			Hashtable defendidosDesignaHash = new Hashtable();
			if(miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_INTERESADO)) {
				
				// CREAR SCS_DEFENDIDOSDESIGNA
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_IDINSTITUCION,miform.getIdInstitucionDES());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_ANIO,miform.getAnioDES());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_NUMERO,miform.getNumeroDES());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_IDTURNO,miform.getIdTurnoDES());				
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_IDPERSONA,miform.getIdPersonaJG());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_CALIDAD,miform.getIdTipoenCalidad());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE,miform.getRepresentante());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_OBSERVACIONES,miform.getObservaciones());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_IDTIPOENCALIDAD,miform.getIdTipoenCalidad());
				UtilidadesHash.set(defendidosDesignaHash,ScsDefendidosDesignaBean.C_CALIDADIDINSTITUCION,user.getLocation());				
			}
			
			Hashtable contrariosDesignaHash = new Hashtable();
			if(miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
				
				// CREAR SCS_CONTRARIOSDESIGNA
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDINSTITUCION,miform.getIdInstitucionDES());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_ANIO,miform.getAnioDES());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_NUMERO,miform.getNumeroDES());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDTURNO,miform.getIdTurnoDES());				
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDPERSONA,miform.getIdPersonaJG());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_NOMBREABOGADOCONTRARIO,miform.getAbogadoContrario());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDABOGADOCONTRARIO,miform.getIdPersonaContrario());
				// separar el valor del procurador y su institucion
				if (miform.getIdProcurador()!=null && !miform.getIdProcurador().trim().equals("")) {
					String id_proc = miform.getIdProcurador().substring(0,miform.getIdProcurador().indexOf(","));
					String institucion_proc = miform.getIdProcurador().substring(miform.getIdProcurador().indexOf(",")+1,miform.getIdProcurador().length());
				
					UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR,institucion_proc);
					UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDPROCURADOR,id_proc);
				}
 				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_IDREPRESENTANTELEGAL,miform.getIdPersonaRepresentante());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE,miform.getRepresentante());
				UtilidadesHash.set(contrariosDesignaHash,ScsContrariosDesignaBean.C_OBSERVACIONES,miform.getObservaciones());

			}
			     	
			// recojo el databackup
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			
			if(miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_INTERESADO)) {
				// INSERTAR O ACTUALIZAR INTERESADOS DESIGNA SI PROCEDE (RELACIONADO)
				ScsDefendidosDesignaAdm adm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
				// busco si era nueva con el idpersona anterior del databackup																																	
				Hashtable buscar = new Hashtable();
				buscar.put(ScsDefendidosDesignaBean.C_IDINSTITUCION,miform.getIdInstitucionDES());
				buscar.put(ScsDefendidosDesignaBean.C_IDTURNO,miform.getIdTurnoDES());
				buscar.put(ScsDefendidosDesignaBean.C_ANIO,miform.getAnioDES());
				buscar.put(ScsDefendidosDesignaBean.C_NUMERO,miform.getNumeroDES());
				buscar.put(ScsDefendidosDesignaBean.C_IDPERSONA,miform.getIdPersonaJG());
				
				//Hashtable oldPer = (Hashtable) dataBackup.get(ScsPersonaJGBean.T_NOMBRETABLA);
				//buscar.put(ScsDefendidosDesignaBean.C_IDPERSONA,oldPer.get(ScsPersonaJGBean.C_IDPERSONA));
				Vector v = adm.selectByPK(buscar);
				if (v!=null && v.size()>0) {
					// EXISTE
					if (miform.getAccionE().equals("editar")) {
						
						
						// hay que comprobar si el antiguo no es el mismo, en ese caso se actualiza
						String  idPersonaAnt = miform.getIdPersonaJG();//(String) dataBackup.get("idPersonaAnt");
						if (idPersonaAnt!=null) {
							if (idPersonaAnt.equals(miform.getIdPersonaJG())) {
								// estamos tocando el mismo registro, se actualiza
								if (!adm.updateDirect(defendidosDesignaHash,null,null)) {
									throw new ClsExceptions("Error en update SOJ. " + adm.getError());
								}
							} else {
								// ya existe el elemento
								throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
							}
						}
					} else {
						// ya existe el elemento
						throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
					}
				} else {
					// NO EXISTE
					// si no existe y estamos en editar, entonces se esta actualizando la persona en el 
					// defendido por lo que se actualiza
					if (miform.getAccionE().equals("editar")) {
						// primero borro el anterior y luego inserto el nuevo
						Hashtable old = (Hashtable) dataBackup.get(ScsDefendidosDesignaBean.T_NOMBRETABLA);
						if (old!=null) {
							if (!adm.delete(old)) {
								throw new ClsExceptions("Error en borrar. " + adm.getError());
							}
						} 
					}
					// Insert con los nuevos valores
					if (!adm.insert(defendidosDesignaHash)) {
						throw new ClsExceptions("Error en insert. " + adm.getError());
					}
				}
			}
			
			if(miform.getConceptoE().equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
				// INSERTAR O ACTUALIZAR INTERESADOS DESIGNA SI PROCEDE (RELACIONADO)
				ScsContrariosDesignaAdm adm = new ScsContrariosDesignaAdm(this.getUserBean(request));
				// busco si era nueva con el idpersona anterior del databackup																																	
				Hashtable buscar = new Hashtable();
				buscar.put(ScsContrariosDesignaBean.C_IDINSTITUCION,miform.getIdInstitucionDES());
				buscar.put(ScsContrariosDesignaBean.C_IDTURNO,miform.getIdTurnoDES());
				buscar.put(ScsContrariosDesignaBean.C_ANIO,miform.getAnioDES());
				buscar.put(ScsContrariosDesignaBean.C_NUMERO,miform.getNumeroDES());
				buscar.put(ScsContrariosDesignaBean.C_IDPERSONA,miform.getIdPersonaJG());
				Vector v = adm.selectByPK(buscar);
				if (v!=null && v.size()>0) {
					// EXISTE
					if (miform.getAccionE().equals("editar")) {
							// estamos tocando el mismo registro, se actualiza
							if (!adm.updateDirect(contrariosDesignaHash,null,null)) {
								throw new ClsExceptions("Error en update SOJ. " + adm.getError());
							}
					} else {
						// ya existe el elemento
						throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
					}
				} else {
					// NO EXISTE
					// si no existe y estamos en editar, entonces se esta actualizando la persona en el 
					// defendido por lo que se actualiza
					if (miform.getAccionE().equals("editar")) {
						// primero borro el anterior y luego inserto el nuevo
						Hashtable old = (Hashtable) dataBackup.get(ScsContrariosDesignaBean.T_NOMBRETABLA);
						if (old!=null) {
							if (!adm.delete(old)) {
								throw new ClsExceptions("Error en borrar. " + adm.getError());
							}
						} 
					}
					// Insert con los nuevos valores
					if (!adm.insert(contrariosDesignaHash)) {
						throw new ClsExceptions("Error en insert. " + adm.getError());
					}
				}
			}			 
			result = this.exitoModal("messages.updated.success",request);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return result;
	}	
	
	/**
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarRelacionAsistencia (
			ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
			String result = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;

	     	// clave de la asistencia
			String idInstitucionASI=miform.getIdInstitucionASI();
			String anioASI=miform.getAnioASI();
			String numeroASI=miform.getNumeroASI();

	     	// Datos Persona
			Hashtable persona= new Hashtable();
			
			ScsContrariosAsistenciaAdm contrariosAsistenciaAdm= new ScsContrariosAsistenciaAdm(this.getUserBean(request));
			Hashtable contrarioAsistenciaHash = new Hashtable();
			if(miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
				
				// CREAR SCS_CONTRARIOSASISTENCIA
				UtilidadesHash.set(contrarioAsistenciaHash,ScsContrariosAsistenciaBean.C_IDINSTITUCION,miform.getIdInstitucionASI());
				UtilidadesHash.set(contrarioAsistenciaHash,ScsContrariosAsistenciaBean.C_ANIO,miform.getAnioASI());
				UtilidadesHash.set(contrarioAsistenciaHash,ScsContrariosAsistenciaBean.C_NUMERO,miform.getNumeroASI());
				UtilidadesHash.set(contrarioAsistenciaHash,ScsContrariosAsistenciaBean.C_IDPERSONA,miform.getIdPersonaJG());
				UtilidadesHash.set(contrarioAsistenciaHash,ScsContrariosAsistenciaBean.C_OBSERVACIONES,miform.getObservaciones());

			}	     	

			// recojo el databackup
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");

			// RELACIONARLO CON LA ASISTENCIA (UPDATE NORMAL)
			ScsAsistenciasAdm admASI= new ScsAsistenciasAdm(this.getUserBean(request));
			String idPersonaAnterior = "";
			Hashtable ht = new Hashtable();
			ht.put(ScsAsistenciasBean.C_IDINSTITUCION,idInstitucionASI);
			ht.put(ScsAsistenciasBean.C_ANIO,anioASI);
			ht.put(ScsAsistenciasBean.C_NUMERO,numeroASI);
			Vector v = admASI.selectByPK(ht);
			if (v!=null && v.size()>0) {
				ScsAsistenciasBean beanASI= (ScsAsistenciasBean) v.get(0);
				if (miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) {
					// compruebo si ha cambiado el id persona para la relacion
					if (beanASI.getIdPersonaJG()!=null && !beanASI.getIdPersonaJG().equals(new Integer(miform.getIdPersonaJG()))) {
						// guardo el idpersona anterior para buscar las relaciones y actualizarlas
						idPersonaAnterior = beanASI.getIdPersonaJG().toString();
					} else {
						// si no el que borrare sera el mismo, el actual
						idPersonaAnterior = miform.getIdPersonaJG();
					}
				} else 
				if (miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
					// en este caso lo cojo del databackup de asistenciacontrario, si esxiste
					Hashtable oldUF = (Hashtable) dataBackup.get(ScsContrariosAsistenciaBean.T_NOMBRETABLA);
					if (oldUF!=null) {
						idPersonaAnterior = (String) oldUF.get(ScsContrariosAsistenciaBean.C_IDPERSONA);
					} else {
						idPersonaAnterior = null;
					}
				}
				
				
				if (miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) {
					// actualizo la personaJG, que sera el interesado en este caso. 
					beanASI.setIdPersonaJG(new Integer(miform.getIdPersonaJG()));
					if (!admASI.updateDirect(beanASI)) {
						throw new ClsExceptions("Error en update Asistencia. " + admASI.getError());
					}
				}
			}
			
			
			if(miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {

				// INSERTAR O ACTUALIZAR INTERESADOS DESIGNA SI PROCEDE (RELACIONADO)
				ScsContrariosAsistenciaAdm adm = new ScsContrariosAsistenciaAdm (this.getUserBean(request));
				// busco si era nueva con el idpersona anterior del databackup																																	
				Hashtable buscar = new Hashtable();
				buscar.put(ScsContrariosAsistenciaBean.C_IDINSTITUCION,miform.getIdInstitucionASI());
				buscar.put(ScsContrariosAsistenciaBean.C_ANIO,miform.getAnioASI());
				buscar.put(ScsContrariosAsistenciaBean.C_NUMERO,miform.getNumeroASI());
				buscar.put(ScsContrariosAsistenciaBean.C_IDPERSONA,miform.getIdPersonaJG());
				
				Vector v2= adm.selectByPK(buscar);
				if (v2!=null && v2.size()>0) { 
					// EXISTE
					if (miform.getAccionE().equals("editar")) {
						// hay que comprobar si el antiguo no es el mismo, en ese caso se actualiza
						String  idPersonaAnt = "";
						Hashtable oldUF = (Hashtable) dataBackup.get(ScsContrariosAsistenciaBean.T_NOMBRETABLA);
						if (oldUF!=null) {
							idPersonaAnt = (String) oldUF.get(ScsContrariosAsistenciaBean.C_IDPERSONA);
						} else {
							idPersonaAnt = null;
						}
						if (idPersonaAnt!=null && idPersonaAnt.equals(miform.getIdPersonaJG())) { 
							// estamos tocando el mismo registro, se actualiza
							if (!adm.updateDirect(contrarioAsistenciaHash,null,null)) {
								throw new ClsExceptions("Error en update SOJ. " + adm.getError());
							}
						} else {
							// ya existe el elemento
							throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
						}
					} else {
						// ya existe el elemento
						throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
					}
				} else {
					// NO EXISTE
					// si no existe y estamos en editar, entonces se esta actualizando la persona en el 
					// defendido por lo que se actualiza
					if (miform.getAccionE().equals("editar")) {
						// primero borro el anterior y luego inserto el nuevo
						
						
						Hashtable  old = (Hashtable) dataBackup.get(ScsContrariosAsistenciaBean.T_NOMBRETABLA);
						
						if (old!=null) {
							if (!adm.delete(old)) {
								throw new ClsExceptions("Error en borrar. " + adm.getError());
							}
						} 
					}
					// Insert con los nuevos valores
					if (!adm.insert(contrarioAsistenciaHash)) {
						throw new ClsExceptions("Error en insert. " + adm.getError());
					}
				}
			}			

			if(miform.getConceptoE().equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {				
				result = this.exitoModal("messages.updated.success",request);
			} else {			
				request.removeAttribute("sinrefresco");
				result = this.exitoRefresco("messages.updated.success",request);
			}				
				
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return result;
	}
	
	/**
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarRelacionContrariosEjg (
			ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
			String result = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;

	     	// clave de la asistencia
			String idInstitucionEJG=miform.getIdInstitucionEJG();
			String anioEJG=miform.getAnioEJG();
			String numeroEJG=miform.getNumeroEJG();
			
			ScsContrariosEJGAdm contrariosejgAdm= new ScsContrariosEJGAdm(this.getUserBean(request));
			Hashtable contrarioEjgHash = new Hashtable();
			
				
				// CREAR SCS_CONTRARIOSASISTENCIA
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDINSTITUCION,miform.getIdInstitucionEJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_ANIO,miform.getAnioEJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_NUMERO,miform.getNumeroEJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDTIPOEJG,miform.getIdTipoEJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDPERSONA,miform.getIdPersonaJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_OBSERVACIONES,miform.getObservaciones());

				// jbd 9.1.2009 INC_CAT_8
			    if (miform.getIdProcurador()!=null && !miform.getIdProcurador().trim().equals("")) {
					String id_proc = miform.getIdProcurador().substring(0,miform.getIdProcurador().indexOf(","));
					String institucion_proc = miform.getIdProcurador().substring(miform.getIdProcurador().indexOf(",")+1,miform.getIdProcurador().length());
				
					UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDINSTITUCION_PROCU,institucion_proc);
					UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDPROCURADOR,id_proc);
				}
			    
			    UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDABOGADOCONTRARIOEJG,miform.getIdAbogadoContrarioEJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_NOMBREABOGADOCONTRARIOEJG,miform.getAbogadoContrarioEJG());
				
			    UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_IDREPRESENTANTEEJG,miform.getIdRepresentanteJG());
				UtilidadesHash.set(contrarioEjgHash,ScsContrariosEJGBean.C_NOMBREREPRESENTANTEEJG,miform.getRepresentante());

			// recojo el databackup
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");

			// INSERTAR O ACTUALIZAR INTERESADOS DESIGNA SI PROCEDE (RELACIONADO)
				ScsContrariosEJGAdm adm = new ScsContrariosEJGAdm (this.getUserBean(request));
				// busco si era nueva con el idpersona anterior del databackup																																	
				Hashtable buscar = new Hashtable();
				buscar.put(ScsContrariosEJGBean.C_IDINSTITUCION,miform.getIdInstitucionEJG());
				buscar.put(ScsContrariosEJGBean.C_ANIO,miform.getAnioEJG());
				buscar.put(ScsContrariosEJGBean.C_NUMERO,miform.getNumeroEJG());
				buscar.put(ScsContrariosEJGBean.C_IDTIPOEJG,miform.getIdTipoEJG());
				buscar.put(ScsContrariosAsistenciaBean.C_IDPERSONA,miform.getIdPersonaJG());
				
				Vector v2= adm.selectByPK(buscar);
				if (v2!=null && v2.size()>0) { 
					// EXISTE
					if (miform.getAccionE().equals("editar")) {
						// hay que comprobar si el antiguo no es el mismo, en ese caso se actualiza
						String  idPersonaAnt = (String) dataBackup.get("idPersonaAnt");
						if (idPersonaAnt==null || (idPersonaAnt!=null && idPersonaAnt.equals(miform.getIdPersonaJG()))) { 
							// estamos tocando el mismo registro, se actualiza
							if (!adm.updateDirect(contrarioEjgHash,null,null)) {
								throw new ClsExceptions("Error en update SOJ. " + adm.getError());
							}
						} else {
							// ya existe el elemento
							throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
						}
					} else {
						// ya existe el elemento
						throw new SIGAException("gratuita.personaJG.mensaje.yaExiste");
					}
				} else {
					// NO EXISTE
					// si no existe y estamos en editar, entonces se esta actualizando la persona en el 
					// defendido por lo que se actualiza
					if (miform.getAccionE().equals("editar")) {
						// primero borro el anterior y luego inserto el nuevo
						
						
						Hashtable  old = (Hashtable) dataBackup.get(ScsContrariosEJGBean.T_NOMBRETABLA);
						
						if (old!=null) {
							if (!adm.delete(old)) {
								throw new ClsExceptions("Error en borrar. " + adm.getError());
							}
						} 
					}
					// Insert con los nuevos valores
					if (!adm.insert(contrarioEjgHash)) {
						throw new ClsExceptions("Error en insert. " + adm.getError());
					}
				}

			result = this.exitoModal("messages.updated.success",request);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return result;
	}

	/**
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarRelacionPersona (
			ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		String result = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	PersonaJGForm miform = (PersonaJGForm)formulario;

	     	// clave de la persona padre
	     	// hay que crear en el form las claves de la persona padre.
			String idInstitucionPER=miform.getIdInstitucionPER();
			String idPersonaPER=miform.getIdPersonaPER();

			// recojo el databackup
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			// lo cojo de personapersona porque es  actualizar personajg
			Hashtable oldPer = (Hashtable) dataBackup.get("PERSONAPERSONA");
		
			// relacionarla con la otra persona. (UPDATE NORMAL)
	     	// relacionarla con idPersonaJGRel, que es el de la ventana hija.
			ScsPersonaJGAdm admPer = new ScsPersonaJGAdm(this.getUserBean(request));
			String idPersonaAnterior = "";
			Hashtable ht = new Hashtable();
			ht.put(ScsPersonaJGBean.C_IDINSTITUCION,idInstitucionPER);
			ht.put(ScsPersonaJGBean.C_IDPERSONA,idPersonaPER);
			Vector v = admPer.selectByPK(ht);
			if (v!=null && v.size()>0) {
				ScsPersonaJGBean beanPer = (ScsPersonaJGBean) v.get(0);
				// actualizo la personaJG, que sera el interesado en este caso. 
				beanPer.setIdRepresentanteJG(new Integer(miform.getIdPersonaJG()));
				if (!admPer.updateDirect(beanPer)) {
					throw new ClsExceptions("Error en update persona. " + admPer.getError());
				}
				// Actualizamos el databackup de la persona padre
				// lo cojo de personaJG porque es  actualizar el padre
				//Hashtable oldPer2 = (Hashtable) dataBackup.get(ScsPersonaJGBean.T_NOMBRETABLA);
				Hashtable oldPer2 = (Hashtable) dataBackup.get(ScsPersonaJGBean.T_NOMBRETABLA);
				
				if (oldPer2!=null){
				
				}else{
					 oldPer2=new Hashtable();	
				}
				oldPer2.put(ScsPersonaJGBean.C_IDREPRESENTANTEJG,beanPer.getIdRepresentanteJG());
				dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,oldPer2);
				request.getSession().setAttribute("DATABACKUP",dataBackup);
			
			}
			
			// OJO, aqui debe ir una ventana que devuelva los valores de la persona.
			request.setAttribute("mensaje","messages.updated.success");
			request.setAttribute("modal","");
			
			// debe devolver los datos y hacer que la ventana se refresque para que vuelva a coger los datos del form
			// de la ventana de abajo.
			request.setAttribute("idPersonaSeleccionado",miform.getIdPersonaJG());
			request.setAttribute("nombrePersonaSeleccionado",miform.getNombre() + " " + miform.getApellido1() + " " + miform.getApellido2());
			
		} 
		catch (Exception e) {
			throwExcp ("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "seleccionPersonaJG";
	}
			
	protected String abrirModalAsuntos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request));
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		PersonaJGForm miform = (PersonaJGForm)formulario;
		Vector relaciones=new Vector();
		String nombreAnterior="";
		String relacion= miform.getModoGuardar();
		try {
			String anio="";
			String numero="";
			String tipo="";
			if (relacion.equalsIgnoreCase("guardarEJG")||
				relacion.equalsIgnoreCase("guardarContrariosEjg")){
				anio = miform.getAnioEJG();
				numero = miform.getNumeroEJG();
				tipo = miform.getIdTipoEJG();
			} else if (relacion.equalsIgnoreCase("guardarSOJ")){
				anio = miform.getAnioSOJ();
				numero = miform.getNumeroSOJ();
				tipo = miform.getIdTipoSOJ();
			} else if (relacion.equalsIgnoreCase("guardarDesigna")){
				anio = miform.getAnioDES();
				numero = miform.getNumeroDES();
				tipo = miform.getIdTurnoDES();
			} else if (relacion.equalsIgnoreCase("guardarAsistencia")){
				anio = miform.getAnioASI();
				numero = miform.getNumeroASI();
				tipo = "";
			}
			relaciones = perAdm.getRelacionesPersonaJG(miform.getIdPersonaJG(), user.getLocation(), relacion, tipo, anio, numero);
			nombreAnterior = perAdm.getNombreApellidos(miform.getIdPersonaJG(), user.getLocation());
		} catch (ClsExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(relaciones!=null && relaciones.size()>0){
			// TODO return Nuevo parametro para mostrar la modal esa rara
			//System.out.println("Tiene " +relaciones.size()+ " relaciones");
			miform.setAsuntos(relaciones);
			miform.setNombreAnterior(nombreAnterior);
		}
		return "asuntosPersonaJGModal";
	}
	
	protected void getAjaxTipoIdentificacion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		PersonaJGForm miForm = (PersonaJGForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String ididioma=usrBean.getLanguage();	

		List<CenTipoIdentificacionBean> alTipoIdentificaciones = null;
		if (miForm.getIdTipoPersona()!=null && !miForm.getIdTipoPersona().equals("")){
				CenTipoIdentificacionAdm tipoIdentificacionAdm = new CenTipoIdentificacionAdm(usrBean);			
			alTipoIdentificaciones = tipoIdentificacionAdm.getTiposIdentificaciones(ididioma, miForm.getIdTipoPersona().toString());
		
		}
		
		if(alTipoIdentificaciones==null){
			alTipoIdentificaciones = new ArrayList<CenTipoIdentificacionBean>();
			
		}
		respuestaAjax(new AjaxCollectionXmlBuilder<CenTipoIdentificacionBean>(), alTipoIdentificaciones,response);
		
	}
	
	/**
	 * Metodo que implementa el modo buscarNIF
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected void getAjaxBusquedaNIF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		
		List listaParametros = new ArrayList();
		
		try {
			String nif = request.getParameter("NIdentificacion").trim();
			String conceptoE = request.getParameter("conceptoE").trim();
			listaParametros.add(nif);
					
			if(nif!=null && !nif.equals("")){			
				UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		     	PersonaJGForm miform = (PersonaJGForm)formulario;
		     	Object objDataBackup =  request.getSession().getAttribute("DATABACKUP");
		     	ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
		     	Hashtable dataBackup = null;
		     	if(objDataBackup!=null){
			     	if(objDataBackup instanceof Hashtable){
			     		dataBackup =  (Hashtable)objDataBackup;
			     	}else{
			     		dataBackup = new Hashtable();
			     		request.getSession().setAttribute("DATABACKUP", dataBackup);
			     	}
		     	}else{
		     		dataBackup = new Hashtable();
		     		request.getSession().setAttribute("DATABACKUP", dataBackup);
		     	}
	     		
	
		     	// recupero el idpersona anterior
		     	Hashtable hashAnt = null;
		     	if (conceptoE.equals(PersonaJGAction.PERSONAJG)) {
		     		hashAnt = (Hashtable)dataBackup.get("PERSONAPERSONA");
		     	} else {
		     		hashAnt = (Hashtable)dataBackup.get(ScsPersonaJGBean.T_NOMBRETABLA);
		     	}
	
				//Quitamos caracteres no alfanumericos, 
				// anhadimos 0 por delante hasta completar los 20 caracteres maximo
				// y lo dejamos todo en mayusculas
				// La configuracion del sistema tambien ignora acentos y demas en las letras
				Hashtable codigos = new Hashtable();
				codigos.put(new Integer(1),nif);
				codigos.put(new Integer(2),user.getLocation());
				String where = 
					" where upper(lpad(regexp_replace(nif, '[^[:alnum:]]', ''), 20, '0')) = " +
					"       upper(lpad(regexp_replace(:1, '[^[:alnum:]]', ''), 20, '0')) " +
					"   and idinstitucion=:2";
				Vector resultadoNIF = admBean.selectBind(where,codigos);
				
				// RGG 18-04-2006 actualizo el databackup para que no me de error el update
				if (resultadoNIF!=null && resultadoNIF.size()>0) {
				
					Hashtable hash = new Hashtable();
	
					ScsPersonaJGBean perBean = (ScsPersonaJGBean) resultadoNIF.get(0);
					if (perBean.getIdRepresentanteJG()!=null) {
						ScsPersonaJGAdm adm = new ScsPersonaJGAdm(this.getUserBean(request));
						request.setAttribute("nombreRepresentante",adm.getNombreApellidos(perBean.getIdRepresentanteJG().toString(),perBean.getIdInstitucion().toString()));
					}
					
					// lo guardamos en el databuckup
					// OJO, utilizo setForCompare porque traspaso beans, y para mi caso, que es luego compararlo
					// en el update necesito que si me viene un nulo, se escriba el elemento con un blanco.
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDINSTITUCION,perBean.getIdInstitucion().toString());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPERSONA,perBean.getIdPersona().toString());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NIF,perBean.getNif().toString());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NOMBRE,perBean.getNombre());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO1,perBean.getApellido1());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO2,perBean.getApellido2());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_DIRECCION,perBean.getDireccion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CODIGOPOSTAL,perBean.getCodigoPostal());						
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FECHANACIMIENTO,perBean.getFechaNacimiento());			
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROFESION,perBean.getIdProfesion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDMINUSVALIA,perBean.getIdMinusvalia());				
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPAIS,perBean.getIdPais());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROVINCIA,perBean.getIdProvincia());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPOBLACION,perBean.getIdPoblacion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ESTADOCIVIL,perBean.getIdEstadoCivil());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL,perBean.getRegimenConyugal());			 
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_TIPOPERSONAJG,perBean.getTipo());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,perBean.getTipoIdentificacion());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ENCALIDADDE,perBean.getEnCalidadDe());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_OBSERVACIONES,perBean.getObservaciones());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG,perBean.getIdRepresentanteJG());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_SEXO,perBean.getSexo());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_HIJOS,perBean.getHijos());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EDAD,perBean.getEdad());				
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FAX,perBean.getFax());
					UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EXISTEDOMICILIO,perBean.getExisteDomicilio());	
					if(perBean.getCorreoElectronico()!=null)
						UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CORREOELECTRONICO,perBean.getCorreoElectronico().trim());	
					
					//Rellenando los parametros de ajax
					listaParametros = new ArrayList();
					listaParametros.add(perBean.getNif().toString());
					listaParametros.add(perBean.getIdInstitucion().toString());
					listaParametros.add(perBean.getIdPersona().toString());
					listaParametros.add(perBean.getNombre());
					listaParametros.add(perBean.getApellido1());
					listaParametros.add(perBean.getApellido2());
					listaParametros.add(perBean.getDireccion());
					listaParametros.add(perBean.getCodigoPostal());						
					if(!perBean.getFechaNacimiento().equals("")){
						listaParametros.add(UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(user.getLanguage(),perBean.getFechaNacimiento())));
					}else{
						listaParametros.add("");
					}
				
					if(perBean.getIdMinusvalia()!=null)
						listaParametros.add(perBean.getIdMinusvalia().toString());
					else
						listaParametros.add("");					
					listaParametros.add(perBean.getIdProvincia());
					listaParametros.add(perBean.getIdPoblacion());
					if(perBean.getIdEstadoCivil()!=null)
						listaParametros.add(perBean.getIdEstadoCivil().toString());
					else
						listaParametros.add("");	
					listaParametros.add(perBean.getRegimenConyugal());			 
					listaParametros.add(perBean.getTipo());
					listaParametros.add(perBean.getTipoIdentificacion());
					if(perBean.getIdRepresentanteJG()!=null)
						listaParametros.add(perBean.getIdRepresentanteJG().toString());
					else
						listaParametros.add("");
					
					listaParametros.add(perBean.getIdPais());
					listaParametros.add(perBean.getSexo());
					listaParametros.add(perBean.getEdad());				
					listaParametros.add(perBean.getFax());
					if(perBean.getCorreoElectronico()!=null)
						listaParametros.add(perBean.getCorreoElectronico().trim());
					else
						listaParametros.add("");	
					
					
					//Atributos propios de cada tipo PERSONAJG
					listaParametros.add(perBean.getIdioma());						
					if(perBean.getIdProfesion()!=null)
						listaParametros.add(perBean.getIdProfesion().toString());
					else
						listaParametros.add("");
					listaParametros.add(perBean.getExisteDomicilio());
					listaParametros.add(perBean.getEnCalidadDe());
					listaParametros.add(perBean.getHijos());
	
					
					// cuelgo el anterior
					if (hashAnt!=null) {
						String idPersonaAnt= (String) hashAnt.get(ScsPersonaJGBean.C_IDPERSONA);
						dataBackup.put("idPersonaAnt",idPersonaAnt);
					}
					
					if (conceptoE.equals(PersonaJGAction.PERSONAJG)) {
						dataBackup.put("PERSONAPERSONA",hash);
					} else {
						dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,hash);
					}
							
					
					//LMSP Se redirige a un jsp que se carga en el frame oculto, y hace todo lo demás :)
					request.getSession().setAttribute("DATABACKUP",dataBackup);
					request.setAttribute("resultadoNIF",resultadoNIF);
					
				}				
				
				/*if (miform.getNombreObjetoDestino() != null) {
					request.setAttribute("NombreObjetoDestino", miform.getNombreObjetoDestino());
				}*/
				
				if (listaParametros != null && listaParametros.size() > 0){
					respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);
				}
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
	}	
	
	/**
	 * Metodo que implementa el modo buscarNIF
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected void getAjaxExisteNIF (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		List listaParametros = new ArrayList();
		try {
			
			String nif = request.getParameter("NIdentificacion").trim();
					
			if(nif!=null && !nif.equals("")){			
				UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		     	ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));

				Hashtable codigos = new Hashtable();
				codigos.put(new Integer(1),nif);
				codigos.put(new Integer(2),user.getLocation());
				String where = 
					" where upper(lpad(regexp_replace(nif, '[^[:alnum:]]', ''), 20, '0')) = " +
					"       upper(lpad(regexp_replace(:1, '[^[:alnum:]]', ''), 20, '0')) " +
					"   and idinstitucion=:2";
				Vector resultadoNIF = admBean.selectBind(where,codigos);
				
				if (resultadoNIF!=null && resultadoNIF.size()>0) {
					ScsPersonaJGBean perBean = (ScsPersonaJGBean) resultadoNIF.get(0);
					listaParametros.add("1"); //Existe esa persona
				}else{
					listaParametros.add("0"); //No existe esa persona
				}
				
			}else{
				listaParametros.add("0"); //No existe esa persona
			}
			
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
	}		

}