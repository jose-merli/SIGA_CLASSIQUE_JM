//VERSIONES
//David Sanchez Pina
//01/02/2007

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRemesaEstadosBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.EstadosRemesaForm;

/**
 * @author davidsp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EstadosRemesaAction extends MasterAction {

	
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			String accion = miForm.getModo();
		
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("borrar")){
				mapDestino = borrar(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("nuevo")){
				mapDestino = nuevoGrupoFijo(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("insertar")){
				mapDestino = guardarGrupoFijo(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("buscar")){
				mapDestino = buscarGrupoFijo(mapping, miForm, request, response);		
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}			
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		return mapping.findForward(mapDestino);
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
	protected String nuevoGrupoFijo (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		return "abrirModal";
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
	protected String guardarGrupoFijo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		String idRemesa = null, idInstitucion = null, idEstado = null, grupos = null, fechaEstado = null;
		UsrBean user = null;
		UserTransaction tx = null;

		try {
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			EstadosRemesaForm miform = (EstadosRemesaForm) formulario;

			idInstitucion = miform.getIdInstitucion();
			idRemesa = miform.getIdRemesa();
			idEstado = miform.getIdEstado();
			String fechaNewBD =GstDate.getApplicationFormatDate("", miform.getFechaEstado());
			// String hora =UtilidadesString.formatoFecha(new
			// Date(),"HH:mm:ss");
			// String hora =UtilidadesBDAdm.getHoraBD();
			// fechaNewBD=fechaNewBD+" "+hora;
//			fechaNewBD=UtilidadesString.formatoFecha(fechaNewBD,"dd/MM/yyyy","yyyy/MM/dd");
			
			// fechaEstado=GstDate.getApplicationFormatDate("",
			// miform.getFechaEstado());

			CajgRemesaEstadosBean bean = new CajgRemesaEstadosBean();
			bean.setIdInstitucion(new Integer(idInstitucion));
			bean.setIdestado(new Integer(idEstado));
			bean.setIdRemesa(new Integer(idRemesa));
			bean.setFecharemesa(fechaNewBD);

			CajgRemesaEstadosAdm admEstados = new CajgRemesaEstadosAdm(this.getUserBean(request));

			tx = user.getTransactionPesada();
			tx.begin();
			admEstados.insert(bean);
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		return exitoModal("messages.inserted.success", request);
	}
	
	/**
	 * Metodo que implementa el modo abrir
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String buscarGrupoFijo (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward="listado", idRemesa=null, idInstitucion=null, modoAnterior=null;
		UsrBean user = null;		
		Vector registros = null;
		
		try {
			EstadosRemesaForm miform = (EstadosRemesaForm)formulario;
			idInstitucion = miform.getIdInstitucion();
			idRemesa = miform.getIdRemesa();
			modoAnterior = miform.getModoAnterior();
			
			if (modoAnterior!=null && (modoAnterior.equalsIgnoreCase("nuevo") || modoAnterior.equalsIgnoreCase("nuevaSociedad")))
				registros = new Vector();
			else {
				//Traemos de base de datos los grupos:
				CajgRemesaEstadosAdm admEstados = new CajgRemesaEstadosAdm(this.getUserBean(request));
				registros = admEstados.busquedaEstadosRemesa(idInstitucion, idRemesa);
			}
			
			request.setAttribute("ESTADOS", registros);
			
	    } catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
		return forward;
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
	protected String borrar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String idPersona=null, idInstitucion=null, idGrupoFijo=null, idInstitucionGrupo=null;
		UsrBean user = null;
		Vector ocultos = null;
		UserTransaction tx = null;
		try {
			EstadosRemesaForm miform = (EstadosRemesaForm)formulario;
			ocultos = miform.getDatosTablaOcultos(0);
			Vector visibles = miform.getDatosTablaVisibles(0);
			String idremesa=((String)ocultos.get(0));
			String idinstitucion=((String)ocultos.get(1));
			String idestado=((String)ocultos.get(2));
			String fechaEstado=GstDate.getApplicationFormatDate("", (String)ocultos.get(3));
			
			Hashtable miHash=new Hashtable();
			miHash.put("IDINSTITUCION", idinstitucion);
			miHash.put("IDREMESA", idremesa);
			miHash.put("IDESTADO", idestado);
			miHash.put("FECHAREMESA", fechaEstado);

			CajgRemesaEstadosAdm admEstados = new CajgRemesaEstadosAdm(this.getUserBean(request));
			admEstados.delete(miHash);
			
	    } 
		catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
		return exitoRefresco("messages.updated.success", request);
	}
	protected String editar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		EstadosRemesaForm miform = (EstadosRemesaForm)formulario;
		Vector ocultos = miform.getDatosTablaOcultos(0);
		Vector visibles = miform.getDatosTablaVisibles(0);
		miform.setIdRemesa((String)ocultos.get(0));
		miform.setIdInstitucion((String)ocultos.get(1));
		miform.setIdEstado((String)ocultos.get(2));
		miform.setFechaEstado((String)ocultos.get(3));
		Hashtable anterior = new Hashtable();
		anterior.put("IDINSTITUCION", (String)ocultos.get(1));
		anterior.put("IDREMESA", (String)ocultos.get(0));
		anterior.put("IDESTADO", (String)ocultos.get(2));
		anterior.put("FECHAESTADO", (String)ocultos.get(3));
		request.setAttribute("MODO_AUX", "modificar");
		request.setAttribute("ANTERIOR", anterior);
		
		
		
		return "abrirModal";
	}
	
	protected String modificar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String idRemesa=null, idInstitucion=null, idEstado=null,  grupos=null, fechaEstado=null;
		UsrBean user = null;
		UserTransaction tx = null;
		
		
		try {
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			EstadosRemesaForm miform = (EstadosRemesaForm)formulario;
			
			Hashtable anterior=(Hashtable) request.getAttribute("anterior");
			String idanterior=(String)request.getParameter("idEstadoAnterior");
			String fechaanterior=(String) request.getParameter("fechaEstadoAnterior");
			idInstitucion = miform.getIdInstitucion();
			idRemesa = miform.getIdRemesa();
			idEstado = miform.getIdEstado();
			fechaEstado=GstDate.getApplicationFormatDate("", miform.getFechaEstado());
			
			
			CajgRemesaEstadosBean bean = new CajgRemesaEstadosBean();
			bean.setIdInstitucion(new Integer(idInstitucion));
			bean.setIdestado(new Integer(idEstado));
			bean.setIdRemesa(new Integer(idRemesa));
			bean.setFecharemesa(fechaEstado);
			
			CajgRemesaEstadosBean beananterior = new CajgRemesaEstadosBean();
			beananterior.setIdInstitucion(new Integer(idInstitucion));
			beananterior.setIdestado(new Integer(idanterior));
			beananterior.setIdRemesa(new Integer(idRemesa));
			beananterior.setFecharemesa(fechaanterior);

			CajgRemesaEstadosAdm admEstados = new CajgRemesaEstadosAdm(this.getUserBean(request));
		
	
			tx = user.getTransactionPesada();
			tx.begin();	
				String sql= "update CAJG_REMESAESTADOS set idestado="+idEstado+", fecharemesa='"+miform.getFechaEstado()+"'" +
						    " where idinstitucion="+idInstitucion+" and idremesa="+idRemesa+"" +
						    " and idestado="+idanterior+" and fecharemesa='"+fechaanterior+"'";
				ClsMngBBDD.executeUpdate(sql);
			tx.commit();

	    } 
		catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
		return exitoModal("messages.inserted.success", request);
	}



}