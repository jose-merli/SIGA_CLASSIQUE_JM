/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * <p>Clase que gestiona los grupos de clientes dinámicos.</p>
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
import com.siga.facturacion.form.GruposDinamicosForm;

public class GruposDinamicosAction extends MasterAction{

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
	 * Muestra la pestanha de Tipos de Productos de la pantalla de mantenimiento.
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
				FacGrupCritIncluidosEnSerieAdm admGDin = new FacGrupCritIncluidosEnSerieAdm(this.getUserBean(request));
				String where = 	" where ";
				where += FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+ FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION+"="+idInstitucion+
			 	     	" and "+
						FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+ FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
				
				Vector datosGDin = admGDin.selectTabla(where);
				request.setAttribute("datosGDin", datosGDin);
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
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		// TODO Auto-generated method stub
		return null;	
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		// TODO Auto-generated method stub
		return null;
	}

	 /**
	   * Se va a mostrar una ventana modal para poder insertar un nuevo
	   * grupo de cliente dinámico.
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return "nuevoGrupoDinamico";
	}
	
	/**
	   * Ejecuta un sentencia INSERT en la Base de Datos para grupos de clientes dinámicos.
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
			UsrBean usrBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usrBean.getTransaction();
						
			GruposDinamicosForm formGDin = (GruposDinamicosForm) formulario;
			
			FacGrupCritIncluidosEnSerieAdm admGDin = new FacGrupCritIncluidosEnSerieAdm(this.getUserBean(request));
			FacGrupCritIncluidosEnSerieBean beanGDin = new FacGrupCritIncluidosEnSerieBean();
	
			
			Integer idInstitucion = new Integer(usrBean.getLocation());
			Long idSerieFacturacion = formGDin.getIdSerieFacturacion();
			
			beanGDin.setIdInstitucion(idInstitucion);
			beanGDin.setIdSerieFacturacion(idSerieFacturacion);
			String id[] = formGDin.getIdGruposCriterios().split(",");
			String idGruposCriterios = id[0];
			String idInstitucionGrup = id[1];
			beanGDin.setIdGruposCriterios(new Integer(idGruposCriterios));
			beanGDin.setIdInstitucionGrup(new Integer(idInstitucionGrup));

			tx.begin();
			boolean result = admGDin.insert(beanGDin);
			if (result)
			{
				request.setAttribute("mensaje","messages.inserted.success");
				tx.commit();
			}
			else
			{
				request.setAttribute("mensaje","messages.general.error");
				tx.rollback();
			}
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 
				
		// Refrescamos la tabla con los valores insertados
		
		// Como la inserción se está haciendo en la ventana modal:
		request.setAttribute("modal", "1");
		
	    return "exito";			
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		// TODO Auto-generated method stub
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
		
			GruposDinamicosForm form = (GruposDinamicosForm)formulario;
			Vector ocultos = form.getDatosTablaOcultos(0);		
			
			Hashtable miHash = new Hashtable();

			FacGrupCritIncluidosEnSerieAdm admGDin =  new FacGrupCritIncluidosEnSerieAdm(this.getUserBean(request));				
			miHash.put(FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION,(ocultos.get(1)));
			miHash.put(FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS,(ocultos.get(2)));
			miHash.put(FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP,(ocultos.get(3)));
			
							
			tx = usr.getTransaction();
			tx.begin();
			if (admGDin.delete(miHash))
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

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
}
