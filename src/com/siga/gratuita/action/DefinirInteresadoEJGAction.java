//Clase: DefinirInteresadoEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirInteresadoEJGForm;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirInteresadoEJGAction extends MasterAction {	
	
	/**
	 * Not implemented	  
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		return null;
	}

	/**
	 * Not Implemented 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			Vector resultado = null;
			Vector resultadoTF = new Vector();
			String idinstitucion = "";
			String idpersona = "";
			UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirInteresadoEJGForm miForm = (DefinirInteresadoEJGForm)formulario;
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
			return "inicio";
		}

	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
						
		return null;
	}

	/**
	 * Not Implemented 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		return null;
	}

	/**
	 * Not Implemented 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		HttpSession ses = request.getSession();
		UserTransaction tx=null;		
		Hashtable miHash = new Hashtable();		
		DefinirInteresadoEJGForm miForm = (DefinirInteresadoEJGForm)formulario;
		Vector resultado = new Vector();
		ScsPersonaJGBean persona = new ScsPersonaJGBean();
		String forward = "";
		
		try {
		
			miHash = miForm.getDatos();
			// Modif.Carlos
			String elNif = (String)miHash.get("NIF");
			while  (elNif.length() < 9) elNif = "0" + elNif;
			miHash.put("NIF",elNif);
			// Fin Modif.Carlos

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
			//	Si no es así significa que manos a insertar los datos de una persona
			else 
			{
				ScsPersonaJGAdm admBeanPersona =  new ScsPersonaJGAdm(this.getUserBean(request));
				ScsEJGAdm admBeanEJG =  new ScsEJGAdm(this.getUserBean(request));
				Vector ejg = new Vector();
								
				miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
				/* Rellenamos un Hashtable con los datos de la clave del EJG para recuperar la información, ya que tenemos que modificar la tabla para anhadirle el
				 * identificador el interesado del EJG
				 */
				Hashtable hashEJG = (Hashtable)request.getSession().getAttribute("DATOEJG");			
				
				// Realizamos la consulta
				ejg = admBeanEJG.selectPorClave(hashEJG);								
				
				String seleccionado = (String) ses.getAttribute("seleccionado");
				ses.removeAttribute("seleccionado");	
				if ((seleccionado == null || !seleccionado.equals("si")) && (miForm.getExistia().toString().equals("0"))) {
					admBeanPersona.prepararInsert(miHash);
					tx=usr.getTransaction();
					tx.begin();
					if (admBeanPersona.insert(miHash))
			        {					
			            // Realizamo un update en la tabal EJG con el identificador de la PersonaJG. En hashEJG almacenamos los datos antiguos (el hashbackup)		            
						hashEJG = (Hashtable)((ScsEJGBean)ejg.get(0)).getOriginalHash().clone();
			            // En los datos que hemos del SOJ que hemos tenido al hacer la consulta le metemos el valor del IdPersonaJG 
			            ((ScsEJGBean)ejg.get(0)).getOriginalHash().put(ScsEJGBean.C_IDPERSONAJG,miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString());
			            // Se hace el update
			            admBeanEJG.update(((ScsEJGBean)ejg.get(0)).getOriginalHash(),hashEJG);
			            // Finalmente hay que insertar en la tabla SCS_UNIDADFAMILIAR. Primero rellamos la hash con la que hacemos la inserción con los campos que faltan
			            miHash.put("IDTIPOEJG", hashEJG.get("IDTIPOEJG"));
			            miHash.put("ANIO", hashEJG.get("ANIO"));
			            miHash.put("NUMERO", hashEJG.get("NUMERO"));
			            miHash.put("ENCALIDADDE","SOLICITANTE");
			            miHash.put("OBSERVACIONES","");			            
			            ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			            if (admUnidadFamiliar.insert(miHash)) forward = "exitoRefresco";
			            else forward ="error";
			            
			            // Ahora en DATOSSOJ devolvemos los campos necesarios
			            hashEJG.clear();
			            hashEJG.put(ScsEJGBean.C_IDPERSONAJG, ((ScsEJGBean)ejg.get(0)).getOriginalHash().get(ScsEJGBean.C_IDPERSONAJG) );
			            request.getSession().setAttribute("DATABACKUP",hashEJG);		            
			        }	        
			        else forward = "error";
				} else {					
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
					admBeanPersona.updateDirect(miHash,null,campos);
		            // Realizamo un update en la tabla EJG con el identificador de la PersonaJG. En hashEJG almacenamos los datos antiguos (el hashbackup)		            
					hashEJG = (Hashtable)((ScsEJGBean)ejg.get(0)).getOriginalHash().clone();
		            // En los datos que hemos del SOJ que hemos tenido al hacer la consulta le metemos el valor del IdPersonaJG 
		            ((ScsEJGBean)ejg.get(0)).getOriginalHash().put(ScsEJGBean.C_IDPERSONAJG,miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString());
		            // Se hace el update
		            admBeanEJG.update(((ScsEJGBean)ejg.get(0)).getOriginalHash(),hashEJG);
				    // Insertamos en la tabla SCS_UNIDADFAMILIAR.		            
		            ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
		            // Finalmente hay que insertar en la tabla SCS_UNIDADFAMILIAR. Primero rellamos la hash con la que hacemos la inserción con los campos que faltan
		            miHash.put("IDTIPOEJG", hashEJG.get("IDTIPOEJG"));
		            miHash.put("ANIO", hashEJG.get("ANIO"));
		            miHash.put("NUMERO", hashEJG.get("NUMERO"));
		            miHash.put("ENCALIDADDE","SOLICITANTE");
		            miHash.put("OBSERVACIONES","");
		            if (admUnidadFamiliar.insert(miHash)) forward = "exitoRefresco";
		            else forward ="error";
				}
				
				tx.commit();
				
				/* Se almacena en DATABACKUP el registro recién modificado puesto que permanecemos en la pantalla
				   de mantenimiento y sino se volviese a realizar alguna modificación daría un error.
				*/
				resultado.clear();
				resultado = admBeanPersona.selectPorClave(miHash);
				persona = (ScsPersonaJGBean)resultado.get(0);
				Hashtable hashBkp = admBeanPersona.beanToHashTable(persona);
				hashBkp.put("IDPERSONAJG",hashEJG.get("IDPERSONAJG").toString());
				
				resultado.clear();			
				
				request.getSession().setAttribute("resultadoTelefonos",resultado);
				request.getSession().setAttribute("accion",request.getSession().getAttribute("accion"));
				request.getSession().setAttribute("DATABACKUP",hashBkp);			

			}		
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
        
		if (forward.equalsIgnoreCase("exitoModal")) return exitoModal("messages.inserted.success",request);
		else if (forward.equalsIgnoreCase("error")) return exitoModal("messages.inserted.error",request);
		else return exitoRefresco("messages.inserted.success",request);	
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
					
			DefinirInteresadoEJGForm miForm = (DefinirInteresadoEJGForm) formulario;
			ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
			ScsUnidadFamiliarEJGAdm admBeanUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
			
			Hashtable miHash = new Hashtable();	
			String forward = "";
			
			try {	
				
				miHash = miForm.getDatos();
				
				// Convertimos la fecha al formato adecuado
				miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));

				Hashtable hashBkp = new Hashtable(), hashTemporal = new Hashtable();
				hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");		
				
				tx=usr.getTransaction();
				tx.begin();
				
				// Campos que se van a actualizar
				String[] campos = {ScsPersonaJGBean.C_NIF,
								   ScsPersonaJGBean.C_NOMBRE,
								   ScsPersonaJGBean.C_APELLIDO1,
								   ScsPersonaJGBean.C_APELLIDO2,
								   ScsPersonaJGBean.C_CODIGOPOSTAL,
								   ScsPersonaJGBean.C_DIRECCION,
								   ScsPersonaJGBean.C_IDPAIS,
								   ScsPersonaJGBean.C_IDPROVINCIA,
								   ScsPersonaJGBean.C_IDPOBLACION,
								   ScsPersonaJGBean.C_FECHANACIMIENTO,
								   ScsPersonaJGBean.C_ESTADOCIVIL,
								   ScsPersonaJGBean.C_REGIMENCONYUGAL,
						  		};
				
				if (admBean.updateDirect(miHash,null,campos)) {
					//Ahora tenemos que modificar en la tabla SCS_UNIDADFAMILIAR. Primero en hashBkp ponemos la información que había en la pantalla					
					hashTemporal = ((ScsUnidadFamiliarEJGBean)((Vector)admBeanUnidad.selectPorClave(miHash)).get(0)).getOriginalHash();
					if (hashBkp.containsKey("DESCRIPCIONINGRESOSANUALES")) {
						hashTemporal.put("DESCRIPCIONINGRESOSANUALES",hashBkp.get("DESCRIPCIONINGRESOSANUALES"));
						hashTemporal.put("IMPORTEINGRESOSANUALES",hashBkp.get("IMPORTEINGRESOSANUALES"));
					}
					if (hashBkp.containsKey("BIENESINMUEBLES")) {
						hashTemporal.put("BIENESINMUEBLES",hashBkp.get("BIENESINMUEBLES"));
						hashTemporal.put("IMPORTEBIENESINMUEBLES",hashBkp.get("IMPORTEBIENESINMUEBLES"));
					}
					if (hashBkp.containsKey("BIENESMUEBLES")) {
						hashTemporal.put("BIENESMUEBLES",hashBkp.get("BIENESMUEBLES"));
						hashTemporal.put("IMPORTEBIENESMUEBLES",hashBkp.get("IMPORTEBIENESMUEBLES"));
					}
					if (hashBkp.containsKey("OTROSBIENES")) {
						hashTemporal.put("OTROSBIENES",hashBkp.get("OTROSBIENES"));
						hashTemporal.put("IMPORTEOTROSBIENES",hashBkp.get("IMPORTEOTROSBIENES"));
					}
					if (admBeanUnidad.update(miHash,hashTemporal)) forward = "exitoRefresco";	        
				}
				
		        else forward = "error";				
				tx.commit();
				/* Se almacena en DATABACKUP el registro recién modificado puesto que permanecemos en la pantalla
				   de mantenimiento y sino se volviese a realizar alguna modificación daría un error.
				*/
				Vector resultado = admBean.selectPorClave(miHash);
				ScsPersonaJGBean persona = (ScsPersonaJGBean)resultado.get(0);
				
				/* Ahora vamos a recuperar de la base de datos los teléfonos, para eso previamente borramos el vector de resultados */
				resultado.clear();
				/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
				String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA);
				resultado = admBeanTlf.selectGenerico(sql);
				
				request.getSession().setAttribute("resultadoTelefonos",resultado);
				request.getSession().setAttribute("accion","editar");
				request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(persona));			
			} catch (Exception e) {
				   throwExcp("messages.updated.error",e,tx);
			}			
	        
			if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.updated.success",request);
			else return exito("messages.updated.error",request);

		}

	/**
	 * Not Implemented 
	 */

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}

	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		/* "DATABACKUP" se usa habitualmente por lo que la borramos para evitar posibles errores */
		request.getSession().removeAttribute("DATABACKUP");
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try {
		
	 		ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
	 		ScsUnidadFamiliarEJGAdm admBeanUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request)); 		
			ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
			
			ScsEJGAdm admBeanEJG =  new ScsEJGAdm(this.getUserBean(request));
			Hashtable miHash = new Hashtable();
			String idPersonaJG = "";
			Vector resultado = new Vector();
			/* Borramos de la sesión el listado de teléfonos por si tuviese algún valor de alguna consulta previa que no diese lugar
			 * a error */		
			request.getSession().removeAttribute("resultadoTelefonos");
			
			/* Si se entra inicialmente en la pestanha */
			try {
				miHash.put(ScsEJGBean.C_IDINSTITUCION,request.getParameter(ScsEJGBean.C_IDINSTITUCION));
				miHash.put(ScsEJGBean.C_IDTIPOEJG,request.getParameter(ScsEJGBean.C_IDTIPOEJG));
				miHash.put(ScsEJGBean.C_ANIO,request.getParameter(ScsEJGBean.C_ANIO));
				miHash.put(ScsEJGBean.C_NUMERO,request.getParameter(ScsEJGBean.C_NUMERO));
				request.getSession().setAttribute("DATOEJG",miHash);
			}			
			/* Si se ha hecho un refresco se recuperan los datos de la variable de sesión DATOEJG*/
			catch (Exception e){
				Hashtable hash2 = (Hashtable) request.getSession().getAttribute("DATOEJG");		
				miHash.put(ScsEJGBean.C_IDINSTITUCION,usr.getLocation());
				miHash.put(ScsEJGBean.C_IDTIPOEJG,hash2.get(ScsEJGBean.C_IDTIPOEJG));
				miHash.put(ScsEJGBean.C_ANIO,hash2.get(ScsEJGBean.C_ANIO));
				miHash.put(ScsEJGBean.C_NUMERO,hash2.get(ScsEJGBean.C_NUMERO));
			}			
			try {
				Vector personaJG = admBeanEJG.selectPorClave(miHash);
				if (!personaJG.isEmpty()) idPersonaJG = ((ScsEJGBean)personaJG.get(0)).getIdPersonaJG().toString();
			} catch (Exception e) {};		
			
			if (idPersonaJG != "") {
			
				/* Almacenamos en la hash el idPersona */			
				miHash.put(ScsPersonaJGBean.C_IDPERSONA,idPersonaJG);
				
				/* Recuperamos de la base de datos los valores originales */
				resultado = admBean.selectPorClave(miHash);
				ScsPersonaJGBean persona = (ScsPersonaJGBean)resultado.get(0);
				resultado = admBeanUnidad.selectPorClave(miHash);
				ScsUnidadFamiliarEJGBean unidadFamiliar = null;
				// Puede que el usuario no exista en la tabla UNIDADFAMILIAR y por ello no habría devuelto resultados
				if (!resultado.isEmpty()) unidadFamiliar = (ScsUnidadFamiliarEJGBean)resultado.get(0);
				
				/* Ahora vamos a recuperar de la base de datos los teléfonos, para eso previamente borramos el vector de resultados */
				resultado.clear();
				/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
				String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);
				resultado = admBeanTlf.selectGenerico(sql);				
								
				Hashtable miHash2 = persona.getOriginalHash();
				
				// Comprobamos si la consulta a UNIDADFAMILIAR devolvió resultados
				if (unidadFamiliar != null) 
				{	
					miHash2.put("ANIO",unidadFamiliar.getAnio());
					miHash2.put("NUMERO",unidadFamiliar.getNumero());
					miHash2.put("IDTIPOEJG",unidadFamiliar.getIdTipoEJG());
					
					if (unidadFamiliar.getIngresosAnuales() != null) {
						miHash2.put("DESCRIPCIONINGRESOSANUALES",unidadFamiliar.getDescripcionIngresosAnuales());
						UtilidadesHash.set(miHash2,"IMPORTEINGRESOSANUALES",unidadFamiliar.getIngresosAnuales());
					};
					if (unidadFamiliar.getImoporteBienesInmuebles() != null) {
						miHash2.put("BIENESINMUEBLES",unidadFamiliar.getBienesInmuebles());
						UtilidadesHash.set(miHash2,"IMPORTEBIENESINMUEBLES",unidadFamiliar.getImoporteBienesInmuebles());
					}
					if (unidadFamiliar.getImoporteBienesMuebles() != null) {
						miHash2.put("BIENESMUEBLES",unidadFamiliar.getBienesMuebles());
						UtilidadesHash.set(miHash2,"IMPORTEBIENESMUEBLES",unidadFamiliar.getImoporteBienesMuebles());
					}
					if (unidadFamiliar.getImporteOtrosBienes() != null) {
						miHash2.put("OTROSBIENES",unidadFamiliar.getOtrosBienes());
						UtilidadesHash.set(miHash2,"IMPORTEOTROSBIENES",unidadFamiliar.getImporteOtrosBienes());
					};
					/* En DATABACKUP almacenmos la hash con los valores */
					request.getSession().setAttribute("DATABACKUP",miHash2);
				} else request.getSession().setAttribute("DATABACKUP",miHash);
											   
				request.getSession().setAttribute("resultadoTelefonos",resultado);
				request.getSession().setAttribute("accion",request.getSession().getAttribute("accion"));
			}
			else {
				// resultado irá vacío, así nos garantizamos que no falle al no encontrar en la sesión ese parámetro
				request.getSession().setAttribute("resultadoTelefonos", resultado);
				/* en la hash almacenamos una cadena vacía para el idPersona */						
				miHash.put(ScsSOJBean.C_IDPERSONA,"");
				/* En la jps destino recuperamos el DATABACKUP, así que por seguridad lo borramos para que no pueda dar error si tuviese algo 
				 * esa variable y por tanto nos diese lugar a errores */ 
				request.getSession().removeAttribute("DATABACKUP");
				
			}
			/* Pasamos por sesión el la hash con los datos del SOJ */
			request.getSession().removeAttribute("DATOSSOJ");
			request.getSession().setAttribute("DATOSSOJ",miHash);
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