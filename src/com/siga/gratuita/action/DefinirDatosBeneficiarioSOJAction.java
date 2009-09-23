//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com - 27/01/2005 
//Ultima modificación: 27/01/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirPersonaJGForm;

public class DefinirDatosBeneficiarioSOJAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else return super.executeInternal(mapping, formulario, request, response); 
	}
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		

		return null;
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		String idinstitucion = "";
		String idpersona = "";
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirPersonaJGForm miForm = (DefinirPersonaJGForm)formulario;
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
			persona.setIdEstadoCivil(Integer.valueOf(miForm.getIdEstadoCivilAux()));
			persona.setRegimenConyugal(miForm.getRegimenConyugalAux());
			persona.setIdPais(miForm.getIdPaisAux());
			persona.setIdProvincia(miForm.getIdProvinciaAux());
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

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return null;
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
        
		return "insertarTelefono";
	}

	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;		
		Hashtable miHash = new Hashtable();		
		DefinirPersonaJGForm miForm = (DefinirPersonaJGForm)formulario;
		Vector resultado = new Vector();
		ScsPersonaJGBean persona = new ScsPersonaJGBean();
		String forward = "";
		
		try {
		
			miHash = miForm.getDatos();
			String elNif = (String)miHash.get("NIF");
			if(elNif!=null)
			{
				while  (elNif.length() < 9) elNif = "0" + elNif;
				miHash.put("NIF",elNif);
			}

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
					return exitoModal("messages.inserted.success",request);
		        }	        
		        else
		        {
		        	return exito("messages.inserted.error",request);
		        }			
			}
			//	Si no es así significa que manos a r los datos de una persona
			else 
			{
				ScsPersonaJGAdm admBeanPersona =  new ScsPersonaJGAdm(this.getUserBean(request));
				ScsDefinirSOJAdm admBeanSOJ =  new ScsDefinirSOJAdm(this.getUserBean(request));
				Vector soj = new Vector();
								
				miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
				/* Rellenamos un Hashtable con los datos de la clave de un SOJ para recuperar la inforamción, ya que tenemos que r la tabla para anhadirle el
				 * identificador el beneficiario del SOJ
				 */
				Hashtable hashSOJ = new Hashtable();
				hashSOJ = (Hashtable)request.getSession().getAttribute("DATOSSOJ");			
				
				// Realizamos la consulta
				soj = admBeanSOJ.selectPorClave(hashSOJ);								
				
				if (miForm.getExistia().toString().equals("0")) {
					tx=usr.getTransaction();
					tx.begin();
					admBeanPersona.prepararInsert(miHash);
					if (admBeanPersona.insert(miHash))
			        {				
			            forward = "exitoRefresco";		            
			            // Realizamo un update en la tabal SOJ con el identificador de la PersonaJG. En hashSOJ almacenamos los datos antiguos (el hashbackup)		            
			            hashSOJ = (Hashtable)((ScsSOJBean)soj.get(0)).getOriginalHash().clone();
			            // En los datos que hemos del SOJ que hemos tenido al hacer la consulta le metemos el valor del IdPersonaJG 
			            ((ScsSOJBean)soj.get(0)).getOriginalHash().put(ScsSOJBean.C_IDPERSONAJG,miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString());
			            // Y finalmente se hace el update
			            admBeanSOJ.update(((ScsSOJBean)soj.get(0)).getOriginalHash(),hashSOJ);
			            		            		            
			            hashSOJ.clear();
			            hashSOJ = (Hashtable)request.getSession().getAttribute("DATOSSOJ");
			            hashSOJ.put(ScsSOJBean.C_IDPERSONAJG, ((ScsSOJBean)soj.get(0)).getOriginalHash().get(ScsSOJBean.C_IDPERSONAJG) );
			            request.getSession().setAttribute("DATOSSOJ",hashSOJ);		            
			        }	        
			        else forward = "exito";			        
					tx.commit();
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
		            forward = "exitoRefresco";		            
		            // Realizamo un update en la tabal SOJ con el identificador de la PersonaJG. En hashSOJ almacenamos los datos antiguos (el hashbackup)		            
		            hashSOJ = (Hashtable)((ScsSOJBean)soj.get(0)).getOriginalHash().clone();		            
		            // En los datos que hemos del SOJ que hemos tenido al hacer la consulta le metemos el valor del IdPersonaJG 
		            Hashtable hashSOJNew = (Hashtable)hashSOJ.clone();
		            hashSOJNew.put(ScsSOJBean.C_IDPERSONAJG,miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString());
		            // Y finalmente se hace el update
		            admBeanSOJ.update(hashSOJNew,hashSOJ);		            		            		            
		            hashSOJ.clear();
		            hashSOJ = (Hashtable)request.getSession().getAttribute("DATOSSOJ");
		            hashSOJ.put(ScsSOJBean.C_IDPERSONAJG, ((ScsSOJBean)soj.get(0)).getOriginalHash().get(ScsSOJBean.C_IDPERSONAJG) );
		            request.getSession().setAttribute("DATOSSOJ",hashSOJ);
		            tx.commit();
				}
				
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

			}			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 		
        
		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.inserted.success",request);
		else return exito("messages.inserted.error",request);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		DefinirPersonaJGForm miForm = (DefinirPersonaJGForm) formulario;
		ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		String forward ="";
		Hashtable miHash = new Hashtable();			
		
		try {	
			
			miHash = miForm.getDatos();
			// Modif.Carlos
			String elNif = (String)miHash.get("NIF");
			while  (elNif.length() < 9) elNif = "0" + elNif;
			miHash.put("NIF",elNif);
			// Fin Modif.Carlos
			
			// Convertimos la fecha al formato adecuado
			miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
			
			Hashtable hashBkp = new Hashtable();
			hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			tx=usr.getTransaction();
			tx.begin();
			if (((String)hashBkp.get("IDPROFESION"))=="null") hashBkp.put("IDPROFESION","");
			request.getSession().removeAttribute("DATABACKUP");
			if (admBean.update(miHash,hashBkp)) forward = "exitoRefresco";
			else forward="exito";
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
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		request.setAttribute("hiddenframe","1");
        
		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.updated.success",request);
		else return exito("messages.updated.success",request);

	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsTelefonosPersonaJGAdm admBean =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();
		
		try {							
			miHash.put(ScsTelefonosPersonaJGBean.C_IDINSTITUCION,(ocultos.get(0)));			
			miHash.put(ScsTelefonosPersonaJGBean.C_IDPERSONA,(ocultos.get(1)));
			miHash.put(ScsTelefonosPersonaJGBean.C_IDTELEFONO,(ocultos.get(2)));
			
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.delete(miHash))
		    {
				tx.commit();
				/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
				String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);									
				Vector resultado = admBean.selectGenerico(sql);
				request.getSession().removeAttribute("resultadoTelefonos");
				request.getSession().setAttribute("resultadoTelefonos",resultado);				
				request.getSession().setAttribute("DATOSSOJ",miHash);
				
				forward = "exitoRefresco";
		    }		    
		    else forward = "exito";			
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		} 
		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.deleted.success",request);
		else return exito("messages.deleted.success",request);
	}
	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
 		ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		ScsDefinirSOJAdm admBeanSOJ =  new ScsDefinirSOJAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();		
		Vector resultado = new Vector();
		String idPersonaJG = "";
		/* Borramos de la sesión el listado de teléfonos por si tuviese algún valor de alguna consulta previa que no diese lugar
		 * a error */
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("resultadoTelefonos");
		/* Si se entra en la pestanha por primera vez se recuperan los datos de la request*/
		try {
			miHash.put(ScsSOJBean.C_IDINSTITUCION,request.getParameter(ScsSOJBean.C_IDINSTITUCION));
			miHash.put(ScsSOJBean.C_IDTIPOSOJ,request.getParameter(ScsSOJBean.C_IDTIPOSOJ));
			miHash.put(ScsSOJBean.C_ANIO,request.getParameter(ScsSOJBean.C_ANIO));
			miHash.put(ScsSOJBean.C_NUMERO,request.getParameter(ScsSOJBean.C_NUMERO));		
			request.getSession().setAttribute("DATOSOJ",miHash);
		}
		/* Si se ha hecho un refresco se recuperan los datos de la variable de sesión DATOSSOJ*/
		catch (Exception e){
			Hashtable hash2 = (Hashtable) request.getSession().getAttribute("DATOSOJ");		
			miHash.put(ScsSOJBean.C_IDINSTITUCION,hash2.get(ScsSOJBean.C_IDINSTITUCION));
			miHash.put(ScsSOJBean.C_IDTIPOSOJ,hash2.get(ScsSOJBean.C_IDTIPOSOJ));
			miHash.put(ScsSOJBean.C_ANIO,hash2.get(ScsSOJBean.C_ANIO));
			miHash.put(ScsSOJBean.C_NUMERO,hash2.get(ScsSOJBean.C_NUMERO));
		}
		try {
			Vector personaJG = new Vector();
			personaJG = admBeanSOJ.selectPorClave(miHash);
			if (!personaJG.isEmpty()) idPersonaJG = ((ScsSOJBean)personaJG.get(0)).getIdPersonaJG().toString();
		} catch (Exception e) {};		
		
		if (idPersonaJG != "") {
		
			/* Almacenamos en la hash el idPersona */			
			miHash.put(ScsPersonaJGBean.C_IDPERSONA,idPersonaJG);
			
			/* Recuperamos de la base de datos los valores originales */
			try {
				resultado = admBean.selectPorClave(miHash);
				ScsPersonaJGBean persona = (ScsPersonaJGBean)resultado.get(0);
			
				/* Ahora vamos a recuperar de la base de datos los teléfonos, para eso previamente borramos el vector de resultados */
				resultado.clear();
				/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
				String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);
				resultado = admBeanTlf.selectGenerico(sql);						
				/* En DATABACKUP almacenmos la hash con los valores */
				request.getSession().setAttribute("DATABACKUP",persona.getOriginalHash());											   
				request.getSession().setAttribute("resultadoTelefonos",resultado);
			} catch (Exception e) {
				   throwExcp("messages.general.error",e,null);
			}
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
		request.getSession().setAttribute("DATOSSOJ",miHash);		
		
		return "inicio";		
	}	

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		return "listadoTelefonos";
	}
}