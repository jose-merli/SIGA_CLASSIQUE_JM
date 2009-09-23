
package com.siga.administracion.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.administracion.form.AuditoriaUsuariosForm;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditoriaUsuariosAction extends MasterAction 
{
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		HashMap databackup = new HashMap();
		
	 	if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
			databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
			Paginador paginador = (Paginador)databackup.get("paginador");
			Vector datos=new Vector();
						
			//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
			String pagina = (String)request.getParameter("pagina");
						
			if (paginador!=null){	
				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}
				else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}
			}	
			databackup.put("paginador",paginador);
			databackup.put("datos",datos);
		}
	 	else {	
	  	    databackup = new HashMap();
			
			/////////////////////////////////////////
	  	    // Realizamos la consulta
			Paginador resultado = null;
			Vector datos = null;

			AuditoriaUsuariosForm form = (AuditoriaUsuariosForm) formulario;
			CenHistoricoAdm adm = new CenHistoricoAdm (this.getUserBean(request));
			resultado  = adm.getAuditoriaUsuariosConPaginador(this.getIDInstitucion(request), form);
			databackup.put("paginador",resultado);
			if (resultado!=null){ 
			   datos = resultado.obtenerPagina(1);
			   databackup.put("datos",datos);
			   request.getSession().setAttribute("DATAPAGINADOR",databackup);
			} 
	 	}
		return "resultado";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		request.setAttribute("MODO_MOSTRAR", "ver");
		return this.mostrar(mapping, formulario, request, response);
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		request.setAttribute("MODO_MOSTRAR", "editar");
		return this.mostrar(mapping, formulario, request, response);
	}

	private String mostrar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		
		String idInstitucion = (String)ocultos.get(0);
		String idPersona     = (String)ocultos.get(1);
		String idHistorico   = (String)ocultos.get(2);
		
		CenHistoricoAdm adm = new CenHistoricoAdm (this.getUserBean(request));
		Vector v = adm.getAuditoriaUsuarios (idInstitucion, idPersona, idHistorico);
		if (v != null && v.size() == 1) 
			request.getSession().setAttribute("DATABACKUP", (Hashtable)v.get(0));
				
		return "datos";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			AuditoriaUsuariosForm f = (AuditoriaUsuariosForm) formulario;

			Hashtable hOld = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATABACKUP");

			Hashtable hNew = new Hashtable ();
			// Claves
			UtilidadesHash.set (hNew, CenHistoricoBean.C_IDINSTITUCION, UtilidadesHash.getString(hOld, CenHistoricoBean.C_IDINSTITUCION));
			UtilidadesHash.set (hNew, CenHistoricoBean.C_IDPERSONA,     UtilidadesHash.getString(hOld, CenHistoricoBean.C_IDPERSONA));
			UtilidadesHash.set (hNew, CenHistoricoBean.C_IDHISTORICO,   UtilidadesHash.getString(hOld, CenHistoricoBean.C_IDHISTORICO));

			// Datos actualizados
			UtilidadesHash.set (hNew, CenHistoricoBean.C_FECHAEFECTIVA, GstDate.getApplicationFormatDate(this.getLenguaje(request),f.getFechaEfectiva()));
			UtilidadesHash.set (hNew, CenHistoricoBean.C_FECHAENTRADA,  GstDate.getApplicationFormatDate(this.getLenguaje(request),f.getFechaEntrada()));
			UtilidadesHash.set (hNew, CenHistoricoBean.C_MOTIVO,        f.getMotivo());

			CenHistoricoAdm adm = new CenHistoricoAdm (this.getUserBean(request));
			if (adm.update(hNew, hOld))
				return this.exitoModalSinRefresco("messages.updated.success", request);
			else 
				throw new ClsExceptions ("messages.updated.error");
		}
		catch (Exception e) {
			throw new ClsExceptions ("messages.updated.error");
		}
	}
}