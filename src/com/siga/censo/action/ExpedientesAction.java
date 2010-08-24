/*
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * modified by miguel.villegas 16-02-2005
 * 
 */
package com.siga.censo.action;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.*;
import com.siga.censo.form.ExpedientesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpedientesAction extends MasterAction{


	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("solicitarModificacion")){
					mapDestino = solicitarModificacion(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("editarSolicitud")){
					mapDestino = editarSolicitud(mapping, miForm, request, response);
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}	
	
	/** 
	 *  Funcion que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{		
			String accion = (String)request.getParameter("accion");
			
			// Vemos si venimos de nueva sociedad o nuevo no colegiado de tipo personal:
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
				 (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals("") )) {
				request.setAttribute("modoVolver",accion);
				return "clienteNoExiste";
			}
			
			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion"));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(), idPersona.longValue());

			CenPersonaBean personaBean = personaAdm.getIdentificador(idPersona);
			ExpExpedienteAdm expedientesAdm = new ExpExpedienteAdm(this.getUserBean(request));
						
			Vector v=null;
			String nombre = null;
			String numero = "";
			String estadoColegial = "";
			if (personaBean==null){
				nombre="";
			}
			else{
				CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
				numero = colegiadoAdm.getIdentificadorColegiado(bean);
				nombre = personaBean.getNombre() + " ";
				if(personaBean.getApellido1()!=null && !personaBean.getApellido1().equals("#NA")){
					nombre =nombre+ personaBean.getApellido1() + " ";
				}
				if(personaBean.getApellido2()!=null && !personaBean.getApellido2().equals("#NA")){
					nombre =nombre+ personaBean.getApellido2();
				}
				v = expedientesAdm.selectExpedientesCliente(idPersona.longValue(), idInstitucionPersona.intValue());
				estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucionPersona));
			}	
			
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("vDatos", v);	
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("estadoColegial", estadoColegial);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return "inicio";
	}

	/** 
	 *  Funcion que implementa la edicion de una solicitud
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editarSolicitud(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		try {		
			Vector ocultos = new Vector();
			// Obtengo el formulario
			ExpedientesForm form = (ExpedientesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			// Paso los parametros pertinentes
			request.setAttribute("nombre", form.getNombre());
			request.setAttribute("numero", form.getNumero());
			request.setAttribute("idPersona", (String)ocultos.get(0));
			request.setAttribute("idInstitucion", (String)ocultos.get(1));
			request.setAttribute("idInstitucionTipoExpediente", (String)ocultos.get(2));	
			request.setAttribute("idTipoExpediente", (String)ocultos.get(5));
			request.setAttribute("numeroExpediente", (String)ocultos.get(3));
			request.setAttribute("anhoExpediente", (String)ocultos.get(4));
			result="solicitarModificacion";
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		return (result);		
	}
		
	
	/** 
	 *  Funcion que implementa la solicitud de una modificacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String solicitarModificacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";				
		UserTransaction tx = null;
		
		try {		
			Vector original;
			boolean correcto=true;
			Hashtable hash = new Hashtable();

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ExpSolicitudBorradoAdm admin=new ExpSolicitudBorradoAdm(this.getUserBean(request));
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			
			// Obtengo los datos del formulario
			ExpedientesForm miForm = (ExpedientesForm)formulario;
			
			// Cargo la tabla hash con los valores del formulario para insertar en EXP_SOLICITUDBORRADO
			hash.put(ExpSolicitudBorradoBean.C_IDINSTITUCION,miForm.getidInstitucion());
			hash.put(ExpSolicitudBorradoBean.C_IDPERSONA,miForm.getidPersona());
			hash.put(ExpSolicitudBorradoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,miForm.getidInstitucionTipoExpediente());
			hash.put(ExpSolicitudBorradoBean.C_IDTIPOEXPEDIENTE,miForm.getidTipoExpediente());
			hash.put(ExpSolicitudBorradoBean.C_NUMEROEXPEDIENTE,miForm.getNumeroExpediente());
			hash.put(ExpSolicitudBorradoBean.C_ANIOEXPEDIENTE,miForm.getFechaExpediente());
			hash.put(ExpSolicitudBorradoBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
			hash.put(ExpSolicitudBorradoBean.C_FECHAALTA,"sysdate");
			hash.put(ExpSolicitudBorradoBean.C_MOTIVO,miForm.getMotivo());
			hash.put(ExpSolicitudBorradoBean.C_IDSOLICITUD,admin.getNuevoId().toString());

			// Comienzo la transaccion
			tx.begin();
			// inserto el bean (registro) en la BBDDs
			correcto=admin.insert(hash);			
			if (correcto){
				tx.commit();
				result = exitoModal("messages.censo.solicitudes.exito",request);				
			}		
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}					
		return (result);		
	}
}
