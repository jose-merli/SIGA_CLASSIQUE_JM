//Clase: DefinirUnidadFamiliarEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirUnidadFamiliarEJGAction extends MasterAction {	
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}else{
				if (miForm.getModo().equalsIgnoreCase("editar2")) 
					return mapping.findForward( this.editar2(mapping, miForm, request,response));
				//if (miForm.getModo().equalsIgnoreCase("nuevoTelefono")) 
					//return mapping.findForward( this.nuevo(mapping, miForm, request,response));
				return super.executeInternal(mapping, formulario, request, response);
			}
		}
		catch(Exception e){
			return mapping.findForward("exception");
		}
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
		
			/* "DATABACKUP" y "resultadoTelefonos" se usan habitualmente así que en primero lugar borramos esta variable*/		
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().removeAttribute("resultadoTelefonos");			
			
			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
			ScsUnidadFamiliarEJGAdm admBean =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));		
			Vector v = new Vector ();
			Hashtable miHash = new Hashtable();
			
			/* En "resultadoTelefonos" se le pasa un vector vacío */
			request.getSession().setAttribute("resultadoTelefonos", v);
			
			String consulta="";
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
			
			try {
				miHash.put("ANIO",request.getParameter("ANIO").toString());
				miHash.put("NUMERO",request.getParameter("NUMERO").toString());
				miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
				miHash.put("IDINSTITUCION",usr.getLocation().toString());
			} catch (Exception e){
				DefinirUnidadFamiliarEJGForm miForm = (DefinirUnidadFamiliarEJGForm) formulario;				
				miHash.put("ANIO",miForm.getAnio());
				miHash.put("NUMERO",miForm.getNumero());
				miHash.put("IDTIPOEJG",miForm.getIdTipoEJG());
				miHash.put("IDINSTITUCION",usr.getLocation());				
			}
			request.setAttribute("DATOSEJG",miHash);
			v = admEJG.selectPorClave(miHash);
			Vector sumaValores = new Vector();
			Hashtable sumaVal = new Hashtable();
			
			String idSolicitante = "";
			if (!v.isEmpty()){
				if (admEJG.beanToHashTable((ScsEJGBean)v.get(0)).containsKey("IDPERSONAJG")) //{
					
					idSolicitante = admEJG.beanToHashTable((ScsEJGBean)v.get(0)).get("IDPERSONAJG").toString(); 
					consulta = "SELECT familia.*, persona.NIF, persona.NOMBRE, persona.APELLIDO1, persona.APELLIDO2, (select F_SIGA_GETRECURSO(parentesco.descripcion,"+this.getUserBean(request).getLanguage()+") from scs_parentesco parentesco where parentesco.idinstitucion=" + miHash.get("IDINSTITUCION")+" and parentesco.idparentesco=familia.idparentesco) PARENTESCO  FROM SCS_UNIDADFAMILIAREJG familia, SCS_PERSONAJG persona " + 
							   "WHERE familia.IDINSTITUCION = " + miHash.get("IDINSTITUCION") + " AND familia.IDTIPOEJG = " + miHash.get("IDTIPOEJG") + " AND familia.ANIO = " + miHash.get("ANIO") +
							   " AND familia.NUMERO = " + miHash.get("NUMERO") + " AND familia.IDPERSONA = persona.IDPERSONA AND persona.IDINSTITUCION = familia.IDINSTITUCION" ;
					
					v = admBean.selectGenerico(consulta);
					
					consulta = " SELECT SUM(IMPORTEINGRESOSANUALES ) SUMAINGRESOS, SUM(IMPORTEBIENESINMUEBLES ) SUMAINMUEBLES, SUM(IMPORTEBIENESMUEBLES) SUMAMUEBLES, SUM(IMPORTEOTROSBIENES) SUMAOTROS FROM SCS_UNIDADFAMILIAREJG " +
							   "WHERE IDINSTITUCION = " + miHash.get("IDINSTITUCION") + " AND IDTIPOEJG = " + miHash.get("IDTIPOEJG") + " AND ANIO = " + miHash.get("ANIO") + " AND NUMERO = " + miHash.get("NUMERO");
			
					sumaValores = admBean.selectGenerico(consulta);
					if (!sumaValores.isEmpty()) {sumaVal = (Hashtable)sumaValores.get(0);}
				
				//}
				//else v.clear(); Cuidado con esto. 
				//Modif. pq si el EJG se crea desde la pantalla "nuevo", no se inserta personajg, 
				//con lo que la unidad familiar creada para el ejg, se elimina con la instruccion v.clear().
			}
			// Pasamos por la request el resultado de la busqueda y el identificador del solicitante
			request.setAttribute("resultado",v);
			request.setAttribute("sumavalores",sumaVal);
			request.setAttribute("idSolicitante",idSolicitante); 
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "inicio";		
	}
	
	/** 
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}
}