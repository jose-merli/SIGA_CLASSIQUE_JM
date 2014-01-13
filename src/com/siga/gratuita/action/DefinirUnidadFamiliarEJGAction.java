//Clase: DefinirUnidadFamiliarEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.beans.ScsMaestroEstadosEJGBean;
import com.siga.beans.ScsParentescoAdm;
import com.siga.beans.ScsParentescoBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.eejg.InformacionEconomicaEjg;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm;
import com.siga.gratuita.service.EejgService;
import com.siga.gui.processTree.SIGAPTConstants;

import es.satec.businessManager.BusinessManager;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirUnidadFamiliarEJGAction extends MasterAction {	
//	private static BusinessManager businessManager=null;
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		String mapDestino =null;
		miForm = (MasterForm) formulario;
		try{
//			if(getBusinessManager()==null)
//				businessManager = BusinessManager.getInstance(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.ATOS_BUSINESS_CONFIG));
//			
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
				
			}else{
				if (miForm.getModo().equalsIgnoreCase("editar2")) 
					mapDestino =  this.editar2(mapping, miForm, request,response);
				else if(miForm.getModo().equalsIgnoreCase("solicitarEejg")){
					mapDestino = this.solicitarEejg(mapping, miForm, request,response);
				}else if(miForm.getModo().equalsIgnoreCase("descargaEejg")){
					mapDestino = this.descargaEejg(mapping, miForm, request,response);
				}else if(miForm.getModo().equalsIgnoreCase("descargaEejgMasivo")){
					mapDestino = this.descargaEejgMasivo(mapping, miForm, request,response);
				}else if(miForm.getModo().equalsIgnoreCase("descargaMultiplesEejg")){
					mapDestino = this.descargaMultiplesEejg(mapping, miForm, request,response);
				}else if(miForm.getModo().equalsIgnoreCase("simulaWebService")){
					mapDestino = this.simulaWebService(mapping, miForm, request,response);
				}
				else{
					return super.executeInternal(mapping,
						      formulario,
						      request, 
						      response);
					
				}
				
				
			}
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}

	
	/**
	 * Not implemented 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		/* "DATABACKUP" y "resultadoTelefonos" se usan habitualmente así que en primero lugar borramos esta variable*/		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSSOJ");
		request.getSession().removeAttribute("resultadoTelefonos");
		request.getSession().setAttribute("accion","editar");
		
		try {
			String consulta = "";
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
			DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm)formulario;
			ScsUnidadFamiliarEJGAdm unidadAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			ScsTelefonosPersonaJGAdm admBeanTlf = new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
			Hashtable miHash = new Hashtable();
			miHash.put("ANIO", miForm.getAnio());
			miHash.put("NUMERO", miForm.getNumero());
			miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
			miHash.put("IDINSTITUCION", usr.getLocation());
			miHash.put("IDPERSONA", formulario.getDatosTablaOcultos(0).get(0));
			
			consulta = "select * from scs_unidadfamiliarejg familia, scs_personajg persona where familia.IDINSTITUCION = " + miHash.get("IDINSTITUCION") + " and familia.ANIO = " + miHash.get("ANIO") + 
					   " and familia.IDTIPOEJG = " + miHash.get("IDTIPOEJG") + " and familia.NUMERO = " + miHash.get("NUMERO") + " and familia.IDPERSONA = persona.IDPERSONA and persona.IDPERSONA = " + miHash.get("IDPERSONA") +
					   " and familia.IDINSTITUCION = persona.IDINSTITUCION";
			
			Vector unidad = (Vector)unidadAdm.selectGenerico(consulta);
			
			request.getSession().setAttribute("DATABACKUP", (Hashtable)unidad.get(0));
			request.getSession().setAttribute("DATOSSOJ", (Hashtable)unidad.get(0));
			
			unidad.clear();
			
			/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
			String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);							
			unidad = admBeanTlf.selectGenerico(sql);			
			request.getSession().setAttribute("resultadoTelefonos",unidad);
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "insertar";
	
	}
	
	protected String editar2(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		String idinstitucion = "";
		String idpersona = "";
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm)formulario;
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		try
		{
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
			/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
			String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + usr.getLocation() + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miForm.getIdPersona();
			resultadoTF = admBeanTlf.selectGenerico(sql);
			request.setAttribute("resultadoTF",resultadoTF);
			request.setAttribute("resultado",persona);
			request.getSession().setAttribute("resultadoTelefonos",resultadoTF);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "insertar";
	}
	

	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		/* "DATABACKUP" y "resultadoTelefonos" se usan habitualmente así que en primero lugar borramos esta variable*/		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSSOJ");
		request.getSession().removeAttribute("resultadoTelefonos");
		
		try {
			String consulta = "";
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
			DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm)formulario;
			ScsUnidadFamiliarEJGAdm unidadAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			ScsTelefonosPersonaJGAdm admBeanTlf = new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
			Hashtable miHash = new Hashtable();
			miHash.put("ANIO", miForm.getAnio());
			miHash.put("NUMERO", miForm.getNumero());
			miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
			miHash.put("IDINSTITUCION", usr.getLocation());
			miHash.put("IDPERSONA", formulario.getDatosTablaOcultos(0).get(0));
			
			consulta = "select * from scs_unidadfamiliarejg familia, scs_personajg persona where familia.IDINSTITUCION = " + miHash.get("IDINSTITUCION") + " and familia.ANIO = " + miHash.get("ANIO") + 
			   		   " and familia.IDTIPOEJG = " + miHash.get("IDTIPOEJG") + " and familia.NUMERO = " + miHash.get("NUMERO") + " and familia.IDPERSONA = persona.IDPERSONA and persona.IDPERSONA = " + miHash.get("IDPERSONA") +
			           " and familia.IDINSTITUCION = persona.IDINSTITUCION";
			
			Vector unidad = (Vector)unidadAdm.selectGenerico(consulta);
			
			request.getSession().setAttribute("accion","ver");
			request.getSession().setAttribute("DATABACKUP", (Hashtable)unidad.get(0));
			unidad.clear();
			/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
			String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);							
			unidad = admBeanTlf.selectGenerico(sql);			
			request.getSession().setAttribute("resultadoTelefonos",unidad);			
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "insertar";
	}

	/**
	 * Not implemented 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm)formulario;
		Hashtable miHash = new Hashtable();
		Vector telefonos = new Vector();
		miHash.put("ANIO", miForm.getAnio());
		miHash.put("NUMERO", miForm.getNumero());
		miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
		
		request.getSession().setAttribute("DATABACKUP", miHash);
		request.getSession().setAttribute("resultadoTelefonos",telefonos);
		request.getSession().setAttribute("DATOSSOJ", miHash);
		
		return "insertar";
	}

	/**
	 * Not implemented 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		HttpSession ses = request.getSession();
		UserTransaction tx=null;		
		Hashtable miHash = new Hashtable();		
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm)formulario;
		Vector resultado = new Vector();
		ScsPersonaJGBean persona = new ScsPersonaJGBean();
		String forward = "";
		
		try {
		
			miHash = miForm.getDatos();

			// Si vamos a insertar un teléfono no vendrán los datos de la persona en la hash			
			if (!miHash.containsKey(ScsPersonaJGBean.C_NOMBRE))
			{				
				ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
				admBeanTlf.prepararInsert(miHash);		
				tx=usr.getTransaction();
				tx.begin();
				
				if (admBeanTlf.insert(miHash))
		        {				
					tx.commit();
					/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
					String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);
					resultado.clear();					
					resultado = admBeanTlf.selectGenerico(sql);
					request.getSession().removeAttribute("resultadoTelefonos");
					request.getSession().setAttribute("resultadoTelefonos",resultado);
					forward = "exitoModal";
		        }
			}
			//	Si no es así significa que vamos a insertar los datos de una persona
			else 
			{
				ScsPersonaJGAdm admBeanPersona =  new ScsPersonaJGAdm(this.getUserBean(request));
				ScsEJGAdm admBeanEJG =  new ScsEJGAdm(this.getUserBean(request));
				Vector ejg = new Vector();				
								
				miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
				
				String seleccionado = (String) ses.getAttribute("seleccionado");
				ses.removeAttribute("seleccionado");	
				if ((seleccionado == null || !seleccionado.equals("si")) && (miForm.getExistia().toString().equals("0"))) {
					try {
						admBeanPersona.prepararInsert(miHash);
						tx=usr.getTransaction();
						tx.begin();					
						if (admBeanPersona.insert(miHash))
				        {					
				            // Insertamos en la tabla SCS_UNIDADFAMILIAR.		            
				            ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
				            	if (admUnidadFamiliar.insert(miHash)) forward = "exitoModal";
				            	else forward ="error";
				        }	        									
				        else forward = "error";
		            }catch (Exception e){
		            	forward ="error";
		            }
				} else  {
					tx=usr.getTransaction();
					tx.begin();
					String[] campos = {ScsPersonaJGBean.C_IDINSTITUCION,
									   ScsPersonaJGBean.C_IDPERSONA,
									   ScsPersonaJGBean.C_NIF,
									   ScsPersonaJGBean.C_NOMBRE,
									   ScsPersonaJGBean.C_APELLIDO1,
									   ScsPersonaJGBean.C_APELLIDO2,
									   ScsPersonaJGBean.C_DIRECCION,
									   ScsPersonaJGBean.C_CODIGOPOSTAL,
									   ScsPersonaJGBean.C_FECHANACIMIENTO,									   
									   ScsPersonaJGBean.C_IDPROFESION,
									   ScsPersonaJGBean.C_IDPAIS,
									   ScsPersonaJGBean.C_IDPROVINCIA,
									   ScsPersonaJGBean.C_IDPOBLACION,
									   ScsPersonaJGBean.C_ESTADOCIVIL,
									   ScsPersonaJGBean.C_REGIMENCONYUGAL,
									  };
					try {
						admBeanPersona.updateDirect(miHash,null,campos);
					    // Insertamos en la tabla SCS_UNIDADFAMILIAR.		            
			            ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			            if (admUnidadFamiliar.insert(miHash)) forward = "exitoModal";
			            else forward ="error";
		            } catch (Exception e){
		            	forward ="error";
		            }
				}
				
				if (forward.equalsIgnoreCase("error")) 
					tx.rollback();
				else {
					tx.commit();
				
					/* Se almacena en DATABACKUP el registro recién modificado puesto que permanecemos en la pantalla
					   de mantenimiento y sino se volviese a realizar alguna modificación daría un error.
					*/
					resultado.clear();
					resultado = admBeanPersona.selectPorClave(miHash);
					persona = (ScsPersonaJGBean)resultado.get(0);
					
					resultado.clear();			
					
					request.getSession().setAttribute("resultadoTelefonos",resultado);
					request.getSession().setAttribute("accion",request.getSession().getAttribute("accion"));
					request.getSession().setAttribute("DATABACKUP",admBeanPersona.beanToHashTable(persona));
					request.getSession().setAttribute("DATOSSOJ",admBeanPersona.beanToHashTable(persona));
				}
			}		
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
        
		if (forward.equalsIgnoreCase("exitoModal")) {
			request.setAttribute("modal","1");
			return exitoModal("messages.inserted.success",request);
		}
		else if (forward.equalsIgnoreCase("error")) 
				return exito("messages.inserted.error",request);
			else 
				return exitoRefresco("messages.inserted.success",request);		
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;		
		Hashtable miHash = new Hashtable(), hashOld = new Hashtable();		
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm)formulario;		
		ScsPersonaJGBean persona = new ScsPersonaJGBean();		
		String forward = "";
		
		try {
		
			miHash = miForm.getDatos();
			ScsPersonaJGAdm admBeanPersona =  new ScsPersonaJGAdm(this.getUserBean(request));
			ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			Vector ejg = new Vector();
			Vector unidad = new Vector();
						
			miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
			
			tx=usr.getTransaction();
			tx.begin();
			hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			/* En hashOld tenemos datos de dos tablas. Para obtener los de cada tabla se pasa la hash al bean de la respectiva tabla. Y a continuación se vuelve a pasar el
			 * a una hashtable y así hemos dejado sólo los campos necesarios
			 */ 
			hashOld = admBeanPersona.beanToHashTable((ScsPersonaJGBean)admBeanPersona.hashTableToBean(hashOld));
			hashOld.remove("IDPROFESION");
			hashOld.put("IDPROFESION","");
			// Se va a comparar lo antiguo con lo viejo, ya que en la bd el pais, provincia, poblacion
			// esta con ceros a la izquierda, hay que ponerlos.
			String pais 		= (String)hashOld.get("IDPAIS");
			String provincia 	= (String)hashOld.get("IDPROVINCIA");
			String poblacion 	= (String)hashOld.get("IDPOBLACION");
			while(pais.length()<3) pais = "0"+pais;
			if (provincia!=null)
				while(provincia.length()<2) provincia = "0"+provincia;
			else
				provincia = "";
			if (poblacion!=null)
				while(poblacion.length()<6) poblacion = "0"+poblacion;
			else
				poblacion = "";
			hashOld.put("IDPAIS",pais);
			hashOld.put("IDPROVINCIA",provincia);
			hashOld.put("IDPOBLACION",poblacion);
			if (admBeanPersona.update(miHash,hashOld))
	        {					
				hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				hashOld = admUnidadFamiliar.beanToHashTable((ScsUnidadFamiliarEJGBean)admUnidadFamiliar.hashTableToBean(hashOld));
	            // Insertamos en la tabla SCS_UNIDADFAMILIAR.	            
	            if (admUnidadFamiliar.update(miHash,hashOld)) forward = "exitoModal";
	            else forward ="error";	            		            
	        }	        
	        else
	        {
	            forward = "error";
	        }	
			tx.commit();
					
		} catch (Exception e) {
			   throwExcp("messages.updated.error",e,tx);
		}		
        
		if (forward.equalsIgnoreCase("exitoModal")) 
			return exitoModal("messages.updated.success",request);
		else if (forward.equalsIgnoreCase("error")) 
			return exito("messages.inserted.error",request);
		else 
			return exitoRefresco("messages.updated.success",request);

	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
    
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsUnidadFamiliarEJGAdm admBean =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		
		try {	
			
			miHash.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,miForm.getIdTipoEJG());
			miHash.put(ScsUnidadFamiliarEJGBean.C_ANIO,miForm.getAnio());
			miHash.put(ScsUnidadFamiliarEJGBean.C_NUMERO,miForm.getNumero());
			miHash.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,miForm.getDatosTablaOcultos(0).get(0));
			
			tx=usr.getTransaction();
			tx.begin();
			admBean.delete(miHash);		    
			tx.commit();
			
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		}
		
		return exitoRefresco("messages.deleted.success",request);
	}

	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		
		try {
			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
			Hashtable<String, Object> miHash = new Hashtable<String, Object>();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario;
			
			String accion = (String)request.getSession().getAttribute("accion");
			miForm.setModo(accion);
			
			if(request.getParameter("ANIO")!=null){
				miHash.put("ANIO",request.getParameter("ANIO").toString());
				miHash.put("NUMERO",request.getParameter("NUMERO").toString());
				miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
				miHash.put("IDINSTITUCION",usr.getLocation().toString());
				miForm.setAnio(request.getParameter("ANIO"));
				miForm.setNumero(request.getParameter("NUMERO"));
				miForm.setIdTipoEJG(request.getParameter("IDTIPOEJG"));
				miForm.setIdInstitucion(request.getParameter("IDINSTITUCION"));
			}else{
				miHash.put("ANIO",miForm.getAnio());
				miHash.put("NUMERO",miForm.getNumero());
				miHash.put("IDTIPOEJG",miForm.getIdTipoEJG());
				miHash.put("IDINSTITUCION",usr.getLocation());				
			}
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			String eejg = paramAdm.getValor (usr.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_EEJG, "");
			String accesoEEJGSt=usr.getPermisoProceso("JGR_UnidadFamiliarEEJG");
			boolean accesoEEJG = accesoEEJGSt!=null && (accesoEEJGSt.equalsIgnoreCase(SIGAConstants.ACCESS_FULL));
			Boolean isPermisoEejg = new Boolean((eejg!=null && eejg.equalsIgnoreCase(ClsConstants.DB_TRUE)));
			miForm.setPermisoEejg(isPermisoEejg&&accesoEEJG);
			//seteamos si es comision pcaj
			miForm.setEsComision(usr.isComision());
			
			Vector<ScsEJGBean> vEjg = admEJG.selectByPK(miHash);
			ScsEJGBean ejg = null;
			try{
				ejg = vEjg.get(0);
				
			}catch (Exception e) {
				throwExcp("error.general.yanoexiste",e,null);
			}
			
			
			
//			P_INSTITUCION IN SCS_EJG.IDINSTITUCION%type,
//            P_IDTIPOEJG   IN SCS_EJG.IDTIPOEJG%type,
//            P_ANIO        IN SCS_EJG.ANIO%type,
//            P_NUMERO
			Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
			htCodigos.put(new Integer(1),miForm.getIdInstitucion());
			htCodigos.put(new Integer(2),miForm.getIdTipoEJG());
			htCodigos.put(new Integer(3),miForm.getAnio());
			htCodigos.put(new Integer(4),miForm.getNumero());
			try {
				String idEstadoEjg = EjecucionPLs.ejecutarFuncion(htCodigos, "F_SIGA_GET_IDULTIMOESTADOEJG");
				if(idEstadoEjg!=null && !idEstadoEjg.equalsIgnoreCase("")){
					ejg.setIdEstadoEjg(Short.valueOf(idEstadoEjg));
					ScsEstadoEJGAdm  estadoEJGAdm = new ScsEstadoEJGAdm(usr);
					ScsMaestroEstadosEJGBean estadoEJG =  estadoEJGAdm.getEstadoEjg(ejg.getIdEstadoEjg(), usr.getLanguage());
					ejg.setMaestroEstadoEJG(estadoEJG);
				}
			} catch (Exception e) {
				ejg.setIdEstadoEjg(null);
				ejg.setMaestroEstadoEJG(null);
			}
			
			miForm.setEjg(ejg);
			if (ejg.getIdPersonaJG()!=null ){
				ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(usr);;

				miForm = admUnidadFamiliar.getUnidadFamiliar(miForm, usr);
				
				
			}
			BusinessManager bm = getBusinessManager();
			EejgService eEjgS = (EejgService)bm.getService(EejgService.class);
			List<ScsEejgPeticionesBean> peticionesEejgBeans = eEjgS.getPeticionesEejg(ejg, usr);
			miForm.setPeticionesEejg(peticionesEejgBeans);
			request.setAttribute("EJG_UNIDADFAMILIAR", PersonaJGAction.EJG_UNIDADFAMILIAR);
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "inicio";		
	}
	
	protected String descargaEejg(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario;
		Vector vCampos = miForm.getDatosTablaOcultos(0);
		String idPersonaJG = (String) vCampos.get(0);
		String idInstitucionEJG = (String) vCampos.get(1);
		String idTipoEJG = (String) vCampos.get(2);
		String anio = (String) vCampos.get(3);
		String numero = (String) vCampos.get(4);
//		String idXml = (String) vCampos.get(5);
//		String idioma = (String) vCampos.get(6);
		String idPeticion = (String) vCampos.get(5);
		miForm.setIdInstitucion(idInstitucionEJG);
		miForm.setIdPersona(idPersonaJG);
		miForm.setIdTipoEJG(idTipoEJG);
		miForm.setAnio(anio);
		miForm.setNumero(numero);
		
		
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String salida = "";
		try {
			ScsEejgPeticionesAdm admPeticionEejg = new ScsEejgPeticionesAdm(usr);
			Hashtable<String, Object> htPK = new Hashtable<String, Object>();
			htPK.put(ScsEejgPeticionesBean.C_IDPETICION, idPeticion);
			Vector vPeticionEejg = admPeticionEejg.selectByPK(htPK);
			ScsEejgPeticionesBean peticionEejg = (ScsEejgPeticionesBean) vPeticionEejg.get(0);
			if(peticionEejg.getIdXml()!=null && !peticionEejg.getIdXml().equals(""))
				miForm.setIdXml(peticionEejg.getIdXml().toString());
			miForm.setIdioma(peticionEejg.getIdioma());
			
			ScsUnidadFamiliarEJGAdm admUnidadFam = new ScsUnidadFamiliarEJGAdm(usr);
			htPK.clear();
			htPK.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,idPersonaJG );
			htPK.put(ScsUnidadFamiliarEJGBean.C_ANIO, anio);
			htPK.put(ScsUnidadFamiliarEJGBean.C_NUMERO, numero);
			htPK.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,idInstitucionEJG );
			htPK.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,idTipoEJG );
			Vector vUF = admUnidadFam.selectByPK(htPK);
			
			ScsParentescoBean parentesco = null;
			if(vUF!=null && vUF.size()>0){
				ScsUnidadFamiliarEJGBean auxUnidadFamiliarVo = (ScsUnidadFamiliarEJGBean) vUF.get(0);
				if(auxUnidadFamiliarVo.getIdParentesco()!=null){
					ScsParentescoAdm admParentesco = new ScsParentescoAdm(usr);
					htPK = new Hashtable<String, Object>();
					htPK.put(ScsParentescoBean.C_IDINSTITUCION,idInstitucionEJG );
					htPK.put(ScsParentescoBean.C_IDPARENTESCO, auxUnidadFamiliarVo.getIdParentesco());
					parentesco = admParentesco.getParentesco(htPK,usr.getLanguage());
				}
				
			}
			if(parentesco==null){
				parentesco = new ScsParentescoBean();
				String literalSolicitante = UtilidadesString.getMensajeIdioma(usr,"gratuita.busquedaEJG.literal.solicitante");
				parentesco.setDescripcion(literalSolicitante);
				ClsLogging.writeFileLog("El parentesco se desconoce ya que ha cambiado la personaJG. Le ponemos solicitante", 10);
				
			}
			miForm.setParentesco(parentesco);
			
			
			
			if(miForm.getIdXml()==null ||miForm.getIdXml().equals(""))
				throw new SIGAException("messages.general.error");
			
			BusinessManager bm = getBusinessManager();
			EejgService eEjgS = (EejgService)bm.getService(EejgService.class);
			ScsUnidadFamiliarEJGBean unidadFamiliarVo  = miForm.getUnidadFamiliarEjgVo();
			unidadFamiliarVo.setPeticionEejg(peticionEejg);
			Map<Integer, Map<String, String>> mapInformeEejg = eEjgS.getDatosInformeEejg(unidadFamiliarVo,usr);
			File fichero = eEjgS.getInformeEejg(mapInformeEejg, usr);
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "true");			
				request.setAttribute("generacionOK","OK");
				salida= "descarga";
			}
			else{
				return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
			}
		}
		catch (Exception e) { 
			ClsLogging.writeFileLog("!!!!!!!!!!!!!!!ERROR EN DEFINIR UNIDAD FAMILIAR.DescargaEejg "+e.toString(), 10);
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	protected String simulaWebService(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InformacionEconomicaEjg esto = new InformacionEconomicaEjg();
		try {
			esto.tratarSolicitudesEejg();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "inicio";
	}
	
	
	protected String descargaMultiplesEejg(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario;
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String salida = "";
		try {
			BusinessManager bm = getBusinessManager();
			EejgService eEjgS = (EejgService)bm.getService(EejgService.class);
			Map<Integer, Map<String, String>> mapInformeEejg = eEjgS.getDatosInformeEejgMultiplesEjg(miForm.getDatosInforme(),usr);
			if(mapInformeEejg==null || mapInformeEejg.size()==0){
				//mhg - Inc 9/08/2012
				//return exitoModalSinRefresco("gratuita.eejg.message.ningunInforme", request);
				return exito("gratuita.eejg.message.ningunInforme", request);
			}else{
				File fichero = eEjgS.getInformeEejg(mapInformeEejg, usr);
				
				if(fichero!= null){
					request.setAttribute("nombreFichero", fichero.getName());
					request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
					request.setAttribute("borrarFichero", "true");			
					request.setAttribute("generacionOK","OK");
					salida= "descarga";
				}
				else{
					return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
				}
			}
		}
		catch (Exception e) { 
			ClsLogging.writeFileLog("!!!!!!!!!!!!!!!ERROR EN DEFINIR UNIDAD FAMILIAR.descargaMultiplesEejg "+e.toString(), 10);
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	
	protected String descargaEejgMasivo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario;
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String salida = "";
		try {
			List<ScsUnidadFamiliarEJGBean> alUnidadFamiliar = new ArrayList<ScsUnidadFamiliarEJGBean>();
			int lengthDatosTabla = miForm.getDatosTabla().size();
			Hashtable<String, Object> htPK = null;
			for (int i = 0; i < lengthDatosTabla ; i++) {
				Vector vCampos = miForm.getDatosTablaOcultos(i);
				String idPersonaJG = (String) vCampos.get(0);
				String idInstitucionEJG = (String) vCampos.get(1);
				String idTipoEJG = (String) vCampos.get(2);
				String anio = (String) vCampos.get(3);
				String numero = (String) vCampos.get(4);
				String idPeticion = (String) vCampos.get(5);
				
				ScsEejgPeticionesAdm admPeticionEejg = new ScsEejgPeticionesAdm(usr);
				htPK = new Hashtable<String, Object>();
				htPK.put(ScsEejgPeticionesBean.C_IDPETICION, idPeticion);
				Vector vPeticionEejg = admPeticionEejg.selectByPK(htPK);
				ScsEejgPeticionesBean peticionEejg = (ScsEejgPeticionesBean) vPeticionEejg.get(0);
				if(peticionEejg.getIdXml()!=null && !peticionEejg.getIdXml().equals(""))
					miForm.setIdXml(peticionEejg.getIdXml().toString());
				miForm.setIdioma(peticionEejg.getIdioma());
				
				ScsUnidadFamiliarEJGAdm admUnidadFam = new ScsUnidadFamiliarEJGAdm(usr);
				htPK.clear();
				htPK = new Hashtable<String, Object>();
				htPK.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,idPersonaJG );
				htPK.put(ScsUnidadFamiliarEJGBean.C_ANIO, anio);
				htPK.put(ScsUnidadFamiliarEJGBean.C_NUMERO, numero);
				htPK.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,idInstitucionEJG );
				htPK.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,idTipoEJG );
				Vector vUF = admUnidadFam.selectByPK(htPK);
				ScsUnidadFamiliarEJGBean unidadFamiliarVo = (ScsUnidadFamiliarEJGBean) vUF.get(0);
				ScsParentescoBean parentesco = null;
				if(unidadFamiliarVo.getIdParentesco()!=null){
					ScsParentescoAdm admParentesco = new ScsParentescoAdm(usr);
					htPK = new Hashtable<String, Object>();
					htPK.put(ScsParentescoBean.C_IDINSTITUCION,idInstitucionEJG );
					htPK.put(ScsParentescoBean.C_IDPARENTESCO, unidadFamiliarVo.getIdParentesco());
					parentesco = admParentesco.getParentesco(htPK,usr.getLanguage());
					
				}else{
					parentesco = new ScsParentescoBean();
					String literalSolicitante = UtilidadesString.getMensajeIdioma(usr,"gratuita.busquedaEJG.literal.solicitante");
					parentesco.setDescripcion(literalSolicitante);
					
					
				}
				unidadFamiliarVo.setParentesco(parentesco);
				unidadFamiliarVo.setPeticionEejg(peticionEejg);
				
				ScsPersonaJGBean personaJG = new ScsPersonaJGBean();
				personaJG.setIdPersona(unidadFamiliarVo.getIdPersona());
				unidadFamiliarVo.setPersonaJG(personaJG);
				
				
				alUnidadFamiliar.add(unidadFamiliarVo);
//				alUnidadFamiliar.add(unidadFamiliarForm.getUnidadFamiliarEjgVo());
				
				
			}
			BusinessManager bm = getBusinessManager();
			EejgService eEjgS = (EejgService)bm.getService(EejgService.class);
			Map<Integer, Map<String, String>> mapInformeEejg = eEjgS.getDatosInformeEejgMasivo(alUnidadFamiliar,usr);
			File fichero = eEjgS.getInformeEejg(mapInformeEejg, usr);
			
			
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "true");			
				request.setAttribute("generacionOK","OK");
				salida= "descarga";
			}
			else{
				return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
			}
		}
		catch (Exception e) { 
			ClsLogging.writeFileLog("!!!!!!!!!!!!!!!ERROR EN DEFINIR UNIDAD FAMILIAR.DescargaEejgMasivo "+e.toString(), 10);
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	
	protected String solicitarEejg(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario;
		Vector vCampos = miForm.getDatosTablaOcultos(0);
		String idPersonaJG = (String) vCampos.get(0);
		String idinstitucion = (String) vCampos.get(1);
		String idTipoEJG = (String) vCampos.get(2);
		String anio = (String) vCampos.get(3);
		String numero = (String) vCampos.get(4);
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsEejgPeticionesBean peticionEejg = new ScsEejgPeticionesBean();
		peticionEejg.setIdInstitucion(Integer.parseInt(idinstitucion));
		peticionEejg.setIdTipoEjg(Integer.parseInt(idTipoEJG));
		peticionEejg.setAnio(Integer.parseInt(anio));
		peticionEejg.setNumero(Integer.parseInt(numero));
		peticionEejg.setIdPersona(Long.parseLong(idPersonaJG));
		StringBuffer languaje = new StringBuffer(usr.getLanguageExt().toLowerCase());
		languaje.append("_ES");
		peticionEejg.setIdioma(languaje.toString());
		
		
		
		HttpSession session = (HttpSession) request.getSession();
		UsrBean usrBean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		String tipoAcceso = usrBean.getAccessType();
		if (!tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL)) {	
			ClsLogging.writeFileLog("Acceso denegado",request,3);
			return exitoRefresco("messages.error.accesoDenegado", request);
		}
		BusinessManager bm = getBusinessManager();
		EejgService eEjgS = (EejgService)bm.getService(EejgService.class);
		
		try {
			eEjgS.insertarPeticionEejg(peticionEejg,usr);	
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return exitoRefresco("messages.inserted.success",request); 
	}
	
	/** 
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}
//	public static BusinessManager getBusinessManager() {
//		return businessManager;
//	}
}