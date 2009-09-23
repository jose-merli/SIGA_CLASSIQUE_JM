/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * <p>Clase que gestiona los grupos de clientes fijos.</p>
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
import com.siga.facturacion.form.GruposFijosForm;

public class GruposFijosAction extends MasterAction{
	
	/**
	 * Muestra la pestanha de Grupos Fijos de la pantalla de mantenimiento.
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
				FacTipoCliIncluidoEnSerieFacAdm admGFijo = new FacTipoCliIncluidoEnSerieFacAdm(this.getUserBean(request));
				String where = 	" where ";
				where += FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+ FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION+"="+idInstitucion+
			 	 	 	" and "+
						FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+ FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
				
				Vector datosGFij = admGFijo.selectTabla(where,user.getLanguage());
				request.setAttribute("datosGFij", datosGFij);
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
	

	/**
	   * Se va a mostrar una ventana modal para poder insertar un nuevo
	   * grupo de cliente fijo.
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
		
		return "nuevoGrupoFijo";
	}
	
	/**
	   * Ejecuta un sentencia INSERT en la Base de Datos para grupos de clientes fijos.
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
						
			GruposFijosForm formGFij = (GruposFijosForm) formulario;
						
			FacTipoCliIncluidoEnSerieFacAdm admGFijo = new FacTipoCliIncluidoEnSerieFacAdm(this.getUserBean(request));
			FacTipoCliIncluidoEnSerieFacBean beanGFijo = new FacTipoCliIncluidoEnSerieFacBean();
			
			Integer idInstitucion = new Integer(formGFij.getIdInstitucion());
			Long idSerieFacturacion = new Long(formGFij.getIdSerieFacturacion());
					
			beanGFijo.setIdInstitucion(idInstitucion);
			beanGFijo.setIdSerieFacturacion(idSerieFacturacion);

			String valorIdGrupo = formGFij.getIdGrupo();
			String idGrupo = valorIdGrupo.substring(0,valorIdGrupo.indexOf(","));
			String idInstitucionGrupo = valorIdGrupo.substring(valorIdGrupo.indexOf(",")+1);
			beanGFijo.setIdGrupo(new Integer(idGrupo));
			beanGFijo.setIdInstitucionGrupo(new Integer(idInstitucionGrupo));
			
			tx.begin();
			boolean result = admGFijo.insert(beanGFijo);
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
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 
				
		// Refrescamos la tabla con los valores insertados
		
		// Como la inserción se está haciendo en la ventana modal:
		request.setAttribute("modal", "1");

	    return "exito";
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
			
			GruposFijosForm form = (GruposFijosForm)formulario;
			Vector ocultos = form.getDatosTablaOcultos(0);		
			
			Hashtable miHash = new Hashtable();
			
			FacTipoCliIncluidoEnSerieFacAdm admGFij =  new FacTipoCliIncluidoEnSerieFacAdm(this.getUserBean(request));				
			miHash.put(FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION,usr.getLocation());			
			miHash.put(FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION,(ocultos.get(1)));
			miHash.put(FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO,(ocultos.get(2)));
			miHash.put(FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO,ocultos.get(3));	
			
			tx = usr.getTransaction();
			tx.begin();
			if (admGFij.delete(miHash))
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

}
