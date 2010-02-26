

package com.siga.censo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenComponentesBean;
import com.siga.beans.CenDatosCVAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenSolicitModifDatosBasicosBean;

import com.siga.censo.form.BusquedaComisionesForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author PDM
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BusquedaComisionesAction extends MasterAction {

			
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idInstitucion=user.getLocation();
		
		String visibilidad = CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
		request.setAttribute("CenInstitucionesVisibles",visibilidad);
		
		return "inicio";
	}
	
		
	/**
	 * Metodo que implementa el modo buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
			
		Vector registros = null;
		BusquedaComisionesForm miFormulario = (BusquedaComisionesForm)formulario;
		UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
		&& !miFormulario.getSeleccionarTodos().equals("");
		
		
		if(!isSeleccionarTodos){
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("Seleccion");
		}
		
		
		try 
		{       CenDatosCVAdm datosComisionesCV= new CenDatosCVAdm(this.getUserBean(request));
		        
			    registros=datosComisionesCV.buscarComisiones(miFormulario);
			
			request.setAttribute("COMISIONES", registros);
	    } catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
		return "busqueda";
	}
	
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		
		try{
			Vector ocultos = new Vector();
			BusquedaComisionesForm form= (BusquedaComisionesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(0));
			String accion = (String)request.getParameter("accion");		
			Integer idCV = Integer.valueOf((String)ocultos.elementAt(2));
			String ncolegiado=String.valueOf((String)ocultos.elementAt(6));
			//String nombreUsuario=(String)ocultos.elementAt(6);

			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenPersonaAdm personaAdm = new CenPersonaAdm(user);
			String nombreUsuario = personaAdm.obtenerApellidos1(idPersona.toString());
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCV(idPersona,idInstitucionPersona,idCV);
	
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", nombreUsuario);
			request.setAttribute("numero",ncolegiado);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		
		/*
		try{
			Vector ocultos = new Vector();
			BusquedaComisionesForm form= (BusquedaComisionesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(0));
			String accion = (String)request.getParameter("accion");		
			Integer idCV = Integer.valueOf((String)ocultos.elementAt(2));
			String ncolegiado=String.valueOf((String)ocultos.elementAt(7));
			String nombreUsuario=(String)ocultos.elementAt(6);

			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCV(idPersona,idInstitucionPersona,idCV);
	
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", nombreUsuario);
			request.setAttribute("numero",ncolegiado);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}*/
		return modo;
	}



}