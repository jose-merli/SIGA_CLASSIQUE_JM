//VERSIONES:
//raul.ggonzalez 21-04-2006 Creacion

/**
* @version 21-04-2006
*/
package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.gratuita.form.BusquedaPersonaJGForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
* Clase action del caso de uso BUSCAR PERSONA JG
* @author AtosOrigin 21-04-2006
*/
public class BusquedaPersonaJGAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("enviar")){
						// enviarCliente
						mapDestino = enviar(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarPersonaInit")){
						// buscarPersona
						borrarPaginadorModal(mapping, miForm, request, response);
						mapDestino = buscarPor(mapping, miForm, request, response);	
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
	 * Metodo que implementa el modo abrir
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
							HttpServletResponse response) throws SIGAException
	{
		String destino = "";
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			BusquedaPersonaJGForm miFormulario = (BusquedaPersonaJGForm)formulario;
			miFormulario.setIdInstitucion(user.getLocation());
			
			destino="abrir";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}

	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String destino = "";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			 
			 HashMap databackup=new HashMap();
			
			 	if (request.getSession().getAttribute("DATAPAGINADORMODAL")!=null){ 
			 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADORMODAL");
				     PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				     Vector datos=new Vector();
				
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				
				 
				
			 if (paginador!=null){	
				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}
			 }	
				
				
				
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
				
					
				
				
		  }else{	
			//obtengo datos de la consulta 			
			databackup=new HashMap();
			PaginadorBind resultado = null;
			Vector datos = null;
			// casting del formulario
			BusquedaPersonaJGForm miFormulario = (BusquedaPersonaJGForm)formulario;
			miFormulario.setIdInstitucion(user.getLocation());
			
			ScsPersonaJGAdm personaJG = new ScsPersonaJGAdm(this.getUserBean(request));
			 resultado = personaJG.getPersonas(miFormulario);
			 databackup.put("paginador",resultado);
				if (resultado!=null){ 
				   datos = resultado.obtenerPagina(1);
				   databackup.put("datos",datos);
				   request.getSession().setAttribute("DATAPAGINADORMODAL",databackup);
				}   
		  
			request.setAttribute("ScsResultadoBusquedaPersonasJG",resultado);
		  }					
			destino="resultado";
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
	     }catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
		 return destino;
	}
	
	

	/**
	 * Metodo que implementa el modo enviarCliente
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			BusquedaPersonaJGForm miform = (BusquedaPersonaJGForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			BusquedaPersonaJGForm miFormulario = (BusquedaPersonaJGForm)formulario;
			miFormulario.setIdInstitucion(user.getLocation());
			
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);

			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion = miFormulario.getIdInstitucion();

			ScsPersonaJGAdm personaJG = new ScsPersonaJGAdm(this.getUserBean(request));
			Hashtable criterios = new Hashtable();
			criterios.put(ScsPersonaJGBean.C_IDPERSONA,idPersona);
			criterios.put(ScsPersonaJGBean.C_IDINSTITUCION,idInstitucion);
			Vector v = personaJG.selectByPK(criterios);
			if (v!=null && v.size()>0) {
				ScsPersonaJGBean bean = (ScsPersonaJGBean) v.get(0);
				if (bean.getIdRepresentanteJG()!=null) {
					request.setAttribute("nombreRepresentante",personaJG.getNombreApellidos(bean.getIdRepresentanteJG().toString(),bean.getIdInstitucion().toString()));
				}	
	
				request.setAttribute("datosPersonaJGModal", bean);		

				
				// RGG 18-04-2006 actualizo el databackup para que no me de error el update
				Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
				Hashtable hash = new Hashtable();
				// lo guardamos en el databuckup
				// OJO, utilizo setForCompare porque traspaso beans, y para mi caso, que es luego compararlo
				// en el update necesito que si me viene un nulo, se escriba el elemento con un blanco.
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDINSTITUCION,bean.getIdInstitucion().toString());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPERSONA,bean.getIdPersona().toString());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NIF,bean.getNif().toString());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_NOMBRE,bean.getNombre());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO1,bean.getApellido1());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_APELLIDO2,bean.getApellido2());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_DIRECCION,bean.getDireccion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CODIGOPOSTAL,bean.getCodigoPostal());						
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FECHANACIMIENTO,bean.getFechaNacimiento());			
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROFESION,bean.getIdProfesion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDMINUSVALIA,bean.getIdMinusvalia());				
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPAIS,bean.getIdPais());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPROVINCIA,bean.getIdProvincia());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDPOBLACION,bean.getIdPoblacion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ESTADOCIVIL,bean.getIdEstadoCivil());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL,bean.getRegimenConyugal());			 
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_TIPOPERSONAJG,bean.getTipo());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,bean.getTipoIdentificacion());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_ENCALIDADDE,bean.getEnCalidadDe());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_OBSERVACIONES,bean.getObservaciones());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG,bean.getIdRepresentanteJG());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_HIJOS,bean.getHijos());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_EDAD,bean.getEdad());				
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_FAX,bean.getFax());
				UtilidadesHash.setForCompare(hash,ScsPersonaJGBean.C_CORREOELECTRONICO,bean.getCorreoElectronico().trim());
				
				//dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,hash);
				if (miFormulario.getConceptoE().equals(PersonaJGAction.PERSONAJG)) {
					dataBackup.put("PERSONAPERSONA",hash);
				} else {
					dataBackup.put(ScsPersonaJGBean.T_NOMBRETABLA,hash);
				}
				request.getSession().setAttribute("DATABACKUP",dataBackup);
			}
			
			
			destino="seleccion";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}
	
	protected void borrarPaginadorModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//Cada vez que se da al boton buscar, se borra el paginador guardado en sesion para luego cargarlo con nuevos criterios
        String destino=""; 
		request.getSession().removeAttribute("DATAPAGINADORMODAL");
		
	}
	
	
}
