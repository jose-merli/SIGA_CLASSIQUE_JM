//VERSIONES
//David Sanchez Pina
//01/02/2007

package com.siga.censo.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenNoColegiadoActividadBean;
import com.siga.beans.CenNoColegiadoActividadAdm;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.censo.form.ActividadProfesionalForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author davidsp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActividadProfesionalAction extends MasterAction {

	
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
	protected String guardarGrupoFijo (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String idPersona=null, idInstitucion=null, idActividadProfesional=null, idInstitucionActividad=null, grupos=null;
		UsrBean user = null;
		UserTransaction tx = null;
		
		
		try {
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ActividadProfesionalForm miform = (ActividadProfesionalForm)formulario;
			
			idInstitucion = miform.getIdInstitucion();
			idPersona = miform.getIdPersona();
			grupos = miform.getGrupos();
			idActividadProfesional = grupos.substring(0, grupos.indexOf("#"));
			idInstitucionActividad = grupos.substring(grupos.indexOf("#")+1);
			
			CenNoColegiadoActividadBean bean = new CenNoColegiadoActividadBean();
			bean.setIdActividadProfesional(new Integer(idActividadProfesional));
			bean.setIdInstitucion(new Integer(idInstitucion));
			bean.setidInstitucionActividad(new Integer(idInstitucionActividad));
			bean.setIdPersona(new Long(idPersona));

			CenNoColegiadoActividadAdm admGrupos = new CenNoColegiadoActividadAdm(this.getUserBean(request));
			if (admGrupos.insert(bean)) {
				CenHistoricoAdm admHist = new CenHistoricoAdm (this.getUserBean(request));
				admHist.insertCompleto((CenHistoricoBean)null, bean, CenHistoricoAdm.ACCION_INSERT, this.getLenguaje(request));
			}
			
//			 Lanzamos el proceso de revision de suscripciones del letrado 
//			 Comienzo control de transacciones
			tx = user.getTransactionPesada();
			tx.begin();	
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(idInstitucion,
																					  idPersona,
																					  "",
																					  ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0"))){
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			}	
			tx.commit();

			// No se usa
//			Hashtable hash = new Hashtable();
//			hash.put(CenGruposClienteClienteBean.C_IDINSTITUCION, idInstitucion);
//			hash.put(CenGruposClienteClienteBean.C_IDPERSONA, idPersona);
//			hash.put(CenGruposClienteClienteBean.C_IDGRUPO, idGrupoFijo);
//			hash.put(CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO, idInstitucionGrupo);
//			hash.put(CenGruposClienteClienteBean.C_FECHAMODIFICACION, "SYSDATE");
//			hash.put(CenGruposClienteClienteBean.C_USUMODIFICACION, user.getUserName());
//
//			//Recuperamos de sesion el registro editado:
//			request.getSession().setAttribute("DATABACKUP_GRUPOS", hash);
	    } 
		catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
		return exitoModal("messages.inserted.success", request);
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
	protected String buscarGrupoFijo (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward="listado", idPersona=null, idInstitucion=null, modoAnterior=null;
		UsrBean user = null;		
		Vector registros = null;
		
		try {
			ActividadProfesionalForm miform = (ActividadProfesionalForm)formulario;
			idInstitucion = miform.getIdInstitucion();
			idPersona = miform.getIdPersona();
			modoAnterior = miform.getModoAnterior();
			
			if (modoAnterior!=null && (modoAnterior.equalsIgnoreCase("nuevo") || modoAnterior.equalsIgnoreCase("nuevaSociedad")))
				registros = new Vector();
			else {
				//Traemos de base de datos los grupos:
				CenNoColegiadoActividadAdm admGrupos = new CenNoColegiadoActividadAdm(this.getUserBean(request));
				registros = admGrupos.busquedaGruposFijoPersona(idInstitucion, idPersona);
			}
			
			request.setAttribute("ACTIVIDADES", registros);
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
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ActividadProfesionalForm miform = (ActividadProfesionalForm)formulario;
			
			ocultos = miform.getDatosTablaOcultos(0);
			idGrupoFijo        = (String)ocultos.get(0);
			idInstitucionGrupo = (String)ocultos.get(1);
			idInstitucion      = miform.getIdInstitucion();
			idPersona          = miform.getIdPersona();
			
//			Hashtable hash = new Hashtable();
//			hash.put(CenGruposClienteClienteBean.C_IDINSTITUCION, idInstitucion);
//			hash.put(CenGruposClienteClienteBean.C_IDPERSONA, idPersona);
//			hash.put(CenGruposClienteClienteBean.C_IDGRUPO, idGrupoFijo);
//			hash.put(CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO, idInstitucionGrupo);

			CenNoColegiadoActividadBean bean = new CenNoColegiadoActividadBean();
			bean.setIdActividadProfesional(new Integer(idGrupoFijo));
			bean.setIdInstitucion(new Integer(idInstitucion));
			bean.setidInstitucionActividad(new Integer(idInstitucionGrupo));
			bean.setIdPersona(new Long(idPersona));

			CenNoColegiadoActividadAdm admGrupos = new CenNoColegiadoActividadAdm(this.getUserBean(request));
			if (admGrupos.delete(bean)) {
				CenHistoricoAdm admHist = new CenHistoricoAdm (this.getUserBean(request));
				admHist.insertCompleto((CenHistoricoBean)null, bean, CenHistoricoAdm.ACCION_DELETE, this.getLenguaje(request));
			}
			
//			 Lanzamos el proceso de revision de suscripciones del letrado 
//			 Comienzo control de transacciones
			tx = user.getTransactionPesada();
			tx.begin();	
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(idInstitucion,
																					  idPersona,
																					  "",
																					  ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			tx.commit();
	    } 
		catch (Exception e) {
		  throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
   	    }
		return exitoRefresco("messages.updated.success", request);
	}



}