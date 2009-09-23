/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * <p>Clase que gestiona los clientes.</p>
 */

package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.DestinatariosIndividualesForm;

public class DestinatariosIndividualesAction extends MasterAction{

	/** 
	 *  Funcion que atiende la accion abrirConParametros
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * 
	 * @return  String  Destino del action
	 *   
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
										  	 MasterForm formulario, 
											 HttpServletRequest request, 
											 HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}
	
	/**
	 * Muestra la pestanha de Destinatarios Individuales de la pantalla de mantenimiento.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String forward = "";
		
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
		
			if (idSerieFacturacion == null)
			{
				forward = "serieFacturacionNoExiste";
			}
			else
			{
				FacClienIncluidoEnSerieFacturAdm admDInd = new FacClienIncluidoEnSerieFacturAdm(user);
				String where = 	" where ";
				where += FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+ FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION+"="+idInstitucion+
			 	     	" and "+
						FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+ FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
				
				Vector datosDInd = admDInd.selectTabla(where);
				request.setAttribute("datosDInd", datosDInd);
				request.getSession().setAttribute("idSerieFacturacion", idSerieFacturacion);
				request.setAttribute("mensaje", "false");
				
				forward = "inicio";
			}
		} 
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 	
		
		return forward;
	}
	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		return null;	
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		return null;
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return null;
	}
	
	/**
	   * Ejecuta un sentencia INSERT en la Base de Datos para destinatarios individuales.
	   *    
	   * @param  mapping - Mapeo de los struts
	   * @param  formulario -  Action Form asociado a este Action
	   * @param  request - objeto llamada HTTP 
	   * @param  response - objeto respuesta HTTP
	   * 
	   * @return  String que indicará la siguiente acción a llevar a cabo
	   *   
	   * @exception  ClsExceptions  En cualquier caso de error
	   */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try
		{
			UserTransaction tx = null;
			tx = ((UsrBean)request.getSession().getAttribute("USRBEAN")).getTransaction();
			
			DestinatariosIndividualesForm formDInd = (DestinatariosIndividualesForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			Long idPersona = formDInd.getIdPersona();
			String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
			
			FacClienIncluidoEnSerieFacturAdm admDInd =  new FacClienIncluidoEnSerieFacturAdm(this.getUserBean(request));
			String where1 = " Where ";
			where1 += FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+ FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION+"="+idInstitucion+
				 	" and "+
					FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+ FacClienIncluidoEnSerieFacturBean.C_IDPERSONA+"="+idPersona+
					" and "+
					FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+ FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
			Vector datosDInd = admDInd.select(where1);
			
			if (datosDInd==null || datosDInd.size()==0)
			{
				Hashtable miHash = new Hashtable();
				miHash.put(FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION,idInstitucion);
				miHash.put(FacClienIncluidoEnSerieFacturBean.C_IDPERSONA,idPersona);
				miHash.put(FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION,idSerieFacturacion);
				
				tx.begin();
				boolean result = admDInd.insert(miHash);
				if (result)
				{
					request.setAttribute("mensaje","messages.inserted.success");
					tx.commit();
				}
				else
				{
					request.setAttribute("mensaje","messages.inserted.error");
					tx.rollback();
				}
			}
			else
			{
				request.setAttribute("mensaje","facturacion.destinatariosIndividuales.mensaje.existeDestinatarioIndividual");
			}
		} 
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 
				
		// Refrescamos la tabla con los valores insertados
		return "exito";	
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
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
	 *  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			UserTransaction tx = null;
			
			DestinatariosIndividualesForm form = (DestinatariosIndividualesForm)formulario;
			Vector ocultos = form.getDatosTablaOcultos(0);		
			
			Hashtable miHash = new Hashtable();
			
			FacClienIncluidoEnSerieFacturAdm admDInd =  new FacClienIncluidoEnSerieFacturAdm(this.getUserBean(request));				
			miHash.put(FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(FacClienIncluidoEnSerieFacturBean.C_IDPERSONA,(ocultos.get(0)));
			miHash.put(FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION,(ocultos.get(1)));
									
			tx = usr.getTransaction();
			tx.begin();
			if (admDInd.delete(miHash))
		    {
		        request.setAttribute("mensaje","messages.deleted.success");
		        tx.commit();
		    }
		    else
		    {
		        request.setAttribute("mensaje","error.messages.deleted");
		        tx.rollback();
		    }
		} 
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 
		
		// Refrescamos la tabla
		return "exito";
	}

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
}
