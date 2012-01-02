/*
 * Created on Dec 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.censo.form.DatosCVForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.businessobjects.report.web.json.JSONObject;
/**
 * @author nuria.rgonzalez
 */
public class DatosCVAction extends MasterAction{

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

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
				} else if (accion.equalsIgnoreCase("solicitarModificacion")){
					mapDestino = solicitarModificacion(mapping, miForm, request, response);
				} else if(accion.equalsIgnoreCase("insertarModificacion")){
					mapDestino = insertarModificacion(mapping, miForm, request, response);
				} else if(accion.equalsIgnoreCase("solicitarNuevo")){
					mapDestino = solicitarNuevo(mapping, miForm, request, response);
				} else if(accion.equalsIgnoreCase("verModal")){
					mapDestino = verModal(mapping, miForm, request, response);
				} else if ( accion.equalsIgnoreCase("getAjaxBorrarCargo")){
					ClsLogging.writeFileLog("DatosCVAction:getAjaxBorrarCargo", 10);
					mapDestino = borrarRegistro(mapping, miForm, request, response);
					return null;
				}else if ( accion.equalsIgnoreCase("obtenerColegiado")){
					ClsLogging.writeFileLog("DatosCVAction:obtenerColegiado", 10);
					mapDestino = obtenerColegiado(mapping, miForm, request, response);
					return null;
				 }else if(accion.equalsIgnoreCase("editarModal")){
					mapDestino = editarModal(mapping, miForm, request, response);
				} else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)			{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
	
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}


	}

	/** Funcion abrir
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	*/

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
		
			String accion = (String)request.getParameter("accion");
			
			// Vemos si venimos de nueva sociedad o nuevo no colegiado de tipo personal:
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
				 (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals("") )) {
				request.setAttribute("modoVolver",accion);
				return "clienteNoExiste";
			}

			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion"));

			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));				
	
			Vector v=null;
			Vector vCliente=null;
			String nombre = null;
			String numero = "";
			String estadoColegial = "";
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			numero = colegiadoAdm.getIdentificadorColegiado(bean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucionPersona));
//			String mensaje = "";
//			if(nombre!=""){
//				String sWhere = " where " + CenClienteBean.C_IDINSTITUCION + "=" + idInstitucion;
//				sWhere += " and " + CenClienteBean.C_IDPERSONA + "=" + idPersona;
//				vCliente = clienteAdm.select(sWhere);
//				if(vCliente.size()>0){
//					numero = clienteAdm.obtenerNumero(idPersona,idInstitucionPersona);			
//					v = clienteAdm.getDatosCV(idPersona,idInstitucionPersona);	
//				}else{
//					mensaje="messages.updated.notFound";
//				}				
//			}else{
//				mensaje="messages.updated.notFound";
//			}
			
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(((DatosCVForm)formulario).getIncluirRegistrosConBajaLogica());
			request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);

			v = clienteAdm.getDatosCV(idPersona,idInstitucionPersona, bIncluirRegistrosConBajaLogica);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("accion", accion);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("vDatos", v);
			request.setAttribute("estadoColegial", estadoColegial);
			int idInstitu= new Integer(idInstitucion).intValue();
			if(idInstitu!=2000)
				request.setAttribute("esJunta", "N");
			else
				request.setAttribute("esJunta", "S");
			
			
			request.setAttribute("idInstitucionCargo","");

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return "inicio";
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		try{
			Vector ocultos = new Vector();
			DatosCVForm form= (DatosCVForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = form.getIdPersona();
			Integer idInstitucionPersona = form.getIDInstitucion();
			String accion = (String)request.getParameter("accion");		
			Integer idCV = Integer.valueOf((String)ocultos.elementAt(0));
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCV(idPersona,idInstitucionPersona,idCV);
	
//			request.setAttribute("hDatos", hash);			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);	
			String idInstitucion=user.getLocation();
			int idInstitu= new Integer(idInstitucion).intValue();
			if(idInstitu!=2000){
				request.setAttribute("esJunta", "N");
				request.setAttribute("idInstitucionCargo", "");
			}else{
				request.setAttribute("esJunta", "S");
				request.setAttribute("idInstitucionCargo", (String)hash.get("IDINSTITUCIONCARGO"));
			}
			request.setAttribute("mantenimiento", "");
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}
	protected String editarModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		try{
			
			DatosCVForm form= (DatosCVForm) formulario;
			Long idPersona = form.getIdPersona();
			
			Integer idInstitucionCargo = form.getIDInstitucionCargo();
			Integer idInstitucionPersona = form.getIDInstitucion();
			String accion = (String)request.getParameter("accion");		
			Integer idCV =form.getIDCV();
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String mantenimiento= (String)request.getParameter("mantenimiento");
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCVJunta(idPersona,idInstitucionPersona,idInstitucionCargo, idCV);
	
//			request.setAttribute("hDatos", hash);			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona",  request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);	
			request.setAttribute("esJunta", "S");
			request.setAttribute("idInstitucionCargo", idInstitucionCargo);
			request.setAttribute("mantenimiento", mantenimiento);
			

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		try{
			Vector ocultos = new Vector();
			DatosCVForm form= (DatosCVForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = form.getIdPersona(); 
			Integer idInstitucionPersona = form.getIDInstitucion();
			String accion = (String)request.getParameter("accion");	
			Integer idCV = Integer.valueOf((String)ocultos.elementAt(0));

			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCV(idPersona,idInstitucionPersona,idCV);
			

			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);		
			String idInstitucion=user.getLocation();
			int idInstitu= new Integer(idInstitucion).intValue();
			if(idInstitu!=2000)
				request.setAttribute("esJunta", "N");
			else
				request.setAttribute("esJunta", "S");
			request.setAttribute("idInstitucionCargo", "");		
			request.setAttribute("mantenimiento", "");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	
	protected String verModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		try{
			Vector ocultos = new Vector();
			DatosCVForm form= (DatosCVForm) formulario;
			
			Long idPersona = form.getIdPersona(); 
			Integer idInstitucionPersona = form.getIDInstitucion();
			Integer idInstitucionCargo = form.getIDInstitucionCargo();
			String accion = (String)request.getParameter("accion");	
			Integer idCV = Integer.valueOf(form.getIDCV());
			String mantenimiento= (String)request.getParameter("mantenimiento");
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCVJunta(idPersona,idInstitucionPersona, idInstitucionCargo,idCV);
			

			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);		
			request.setAttribute("esJunta", "S");
			request.setAttribute("idInstitucionCargo", idInstitucionCargo);
			request.setAttribute("mantenimiento", mantenimiento);			
			
			}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "nuevo";
 		try
		{
 			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
 			DatosCVForm miForm = (DatosCVForm) formulario;
			request.setAttribute("modoConsulta", modo);			
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("idPersona", miForm.getIdPersona());
			request.setAttribute("idInstitucion", miForm.getIDInstitucion());
			String idInstitucion=user.getLocation();
			int idInstitu= new Integer(idInstitucion).intValue();
			if(idInstitu!=2000){
				request.setAttribute("esJunta", "N");
				request.setAttribute("numero", request.getParameter("numeroUsuario"));
			}else{
				request.setAttribute("esJunta", "S");
				request.setAttribute("numero", "");
			}
			request.setAttribute("idInstitucionCargo", "");
			request.setAttribute("mantenimiento", "");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			DatosCVForm miForm = (DatosCVForm) formulario;
			Integer idTipoCVSubtipo1 = null;
			Integer idInstitucionSubtipo1=null;
			Integer idTipoCVSubtipo2=null;
			Integer idInstitucionSubtipo2=null;
			
			 String[] datosCVSubtipo1;
			 if (miForm.getIdTipoCVSubtipo1()!=null && !miForm.getIdTipoCVSubtipo1().equals("")){
			  datosCVSubtipo1=miForm.getIdTipoCVSubtipo1().toString().split("@");
			  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
			  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
			 }
			 if (miForm.getIdTipoCVSubtipo2()!=null && !miForm.getIdTipoCVSubtipo2().equals("")){ 
				 String[] datosCVSubtipo2;
				 datosCVSubtipo2=miForm.getIdTipoCVSubtipo2().toString().split("@");
				 idTipoCVSubtipo2=new Integer(datosCVSubtipo2[0]);
				 idInstitucionSubtipo2=new Integer(datosCVSubtipo2[1]);
			 }

			// Fijamos los datos del C.V.
			CenDatosCVBean beanCV  = new CenDatosCVBean ();
			
			if (miForm.getCertificado().booleanValue()) {
				beanCV.setCertificado(ClsConstants.DB_TRUE);
			} else {
				beanCV.setCertificado(ClsConstants.DB_FALSE);
			}
			beanCV.setCreditos(miForm.getCreditos());
			beanCV.setDescripcion(miForm.getDescripcion());
			if (miForm.getFechaFin()!=null && !miForm.getFechaFin().equals("")){
				beanCV.setFechaFin(miForm.getFechaFin());
				
			}else{
				beanCV.setFechaFin("");
			}
			beanCV.setFechaInicio(miForm.getFechaInicio());
			beanCV.setFechaMovimiento(miForm.getFechaMovimiento());
			beanCV.setIdInstitucion(miForm.getIDInstitucion());
			beanCV.setIdPersona(miForm.getIdPersona());
			beanCV.setIdTipoCV(miForm.getTipoApunte());
			if (idTipoCVSubtipo1!=null && !idTipoCVSubtipo1.equals("")){
				  beanCV.setIdTipoCVSubtipo1(idTipoCVSubtipo1.toString());
				  beanCV.setIdInstitucion_subt1(idInstitucionSubtipo1);
			}else{
					beanCV.setIdTipoCVSubtipo1("");
					beanCV.setIdInstitucion_subt1(null);
			}
			if(idTipoCVSubtipo2!=null && !idTipoCVSubtipo2.equals("")){
				 beanCV.setIdTipoCVSubtipo2(idTipoCVSubtipo2.toString());
				 beanCV.setIdInstitucion_subt2(idInstitucionSubtipo2);
			}else{
					beanCV.setIdTipoCVSubtipo2("");
					beanCV.setIdInstitucion_subt2(null);
			}
			beanCV.setIdInstitucionCargo(miForm.getIDInstitucionCargo());
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			CenDatosCVAdm admDatosCV = new CenDatosCVAdm (this.getUserBean(request));
			t.begin();
			if (!admDatosCV.insertarConHistorico(beanCV, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (admDatosCV.getError());
			}
			t.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return exitoModal("messages.inserted.success", request);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			DatosCVForm miForm = (DatosCVForm) formulario;
			Hashtable hash = miForm.getDatos();
			Integer idTipoCVSubtipo1 = null;
			Integer idInstitucionSubtipo1=null;
			Integer idTipoCVSubtipo2=null;
			Integer idInstitucionSubtipo2=null;

			// Actualizamos los datos
			CenDatosCVBean beanCV = new CenDatosCVBean();
			if (miForm.getCertificado().booleanValue()) {
				beanCV.setCertificado(ClsConstants.DB_TRUE);
			} else {
				beanCV.setCertificado(ClsConstants.DB_FALSE);
			}
			String[] datosCVSubtipo1;
			if (miForm.getIdTipoCVSubtipo1()!=null && !miForm.getIdTipoCVSubtipo1().equals("")){
			 datosCVSubtipo1=miForm.getIdTipoCVSubtipo1().toString().split("@");
			  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
			  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
			} 
			if (miForm.getIdTipoCVSubtipo2()!=null && !miForm.getIdTipoCVSubtipo2().equals("")){ 
			 String[] datosCVSubtipo2;
			 datosCVSubtipo2=miForm.getIdTipoCVSubtipo2().toString().split("@");
			  idTipoCVSubtipo2=new Integer(datosCVSubtipo2[0]);
			  idInstitucionSubtipo2=new Integer(datosCVSubtipo2[1]);
			}
			beanCV.setCreditos(miForm.getCreditos());
			beanCV.setDescripcion(miForm.getDescripcion());
			if (miForm.getFechaFin()!=null && !miForm.getFechaFin().equals("")){
				beanCV.setFechaFin(miForm.getFechaFin());
				
			}else{
				beanCV.setFechaFin("");
			}
			
			
			// inc7029 // beanCV.setFechaInicio(miForm.getFechaInicio());
			if (miForm.getFechaInicio()!=null && !miForm.getFechaInicio().equals("")){
				beanCV.setFechaInicio(miForm.getFechaInicio());
				
			}else{
				beanCV.setFechaInicio("");
			}
			
			if (miForm.getFechaMovimiento()!=null && !miForm.getFechaMovimiento().equals("")){
				beanCV.setFechaMovimiento(miForm.getFechaMovimiento());
				
			}else{
				beanCV.setFechaMovimiento("");
			}
			//beanCV.setFechaMovimiento(miForm.getFechaMovimiento());
			
			beanCV.setIdCV(miForm.getIDCV());
			beanCV.setIdInstitucion(miForm.getIDInstitucion());
			beanCV.setIdInstitucionCargo(miForm.getIDInstitucionCargo());
			beanCV.setIdPersona(miForm.getIdPersona());
			beanCV.setIdTipoCV(miForm.getTipoApunte());
			if (idTipoCVSubtipo1!=null && !idTipoCVSubtipo1.equals("")){
			  beanCV.setIdTipoCVSubtipo1(idTipoCVSubtipo1.toString());
			  beanCV.setIdInstitucion_subt1(idInstitucionSubtipo1);
			}else{
				beanCV.setIdTipoCVSubtipo1("");
				beanCV.setIdInstitucion_subt1(null);
			}
			if(idTipoCVSubtipo2!=null && !idTipoCVSubtipo2.equals("")){
			 beanCV.setIdTipoCVSubtipo2(idTipoCVSubtipo2.toString());
			 beanCV.setIdInstitucion_subt2(idInstitucionSubtipo2);
			}else{
				beanCV.setIdTipoCVSubtipo2("");
				beanCV.setIdInstitucion_subt2(null);
			}
			
			beanCV.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP"));

			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			CenDatosCVAdm admDatosCV = new CenDatosCVAdm (this.getUserBean(request));

			t.begin();
			if (!admDatosCV.updateConHistorico(beanCV, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (admDatosCV.getError());
			}
			t.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return exitoModal("messages.updated.success", request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			DatosCVForm miForm = (DatosCVForm) formulario;

			Integer idCV = new Integer((String) miForm.getDatosTablaOcultos(0).get(0));
			String idInstitucionCargo = (String) miForm.getDatosTablaOcultos(0).get(1);			

			CenDatosCVAdm admDatosCV = new CenDatosCVAdm (this.getUserBean(request));
			Hashtable clavesCV = new Hashtable();
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDINSTITUCION, miForm.getIDInstitucion());
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDINSTITUCIONCARGO,idInstitucionCargo);
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDCV, idCV);

			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
	
			t.begin();
			if (!admDatosCV.deleteConHistorico(clavesCV, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (admDatosCV.getError());
			}
			t.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return exitoRefresco("messages.deleted.success", request);
	}			

	/** 
	 *  Funcion que atiende la accion solicitarModificacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String solicitarModificacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String modo = "solicitarModificacion";
		try{
			Vector ocultos = new Vector();
			DatosCVForm form= (DatosCVForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = form.getIdPersona();
			Integer idInstitucionPersona = form.getIDInstitucion();
			String accion = (String)request.getParameter("accion");		
			Integer idCV = Integer.valueOf((String)ocultos.elementAt(0));
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getDatosCV(idPersona,idInstitucionPersona,idCV);
			
			request.setAttribute("hDatos", hash);	
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("idPersona", idPersona);			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}
		
	protected String insertarModificacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		UserTransaction t = null;
		String modo = "insertarModificacion";
		try {
			t = this.getUserBean(request).getTransaction();
					
			DatosCVForm form= (DatosCVForm) formulario;					
			CenSolicitudModificacionCVAdm adm = new CenSolicitudModificacionCVAdm(this.getUserBean(request));

			
			t.begin();	
			CenSolicitudModificacionCVBean bean = getDatos(form, request);			
			if(!adm.insert(bean)){
				throw new SIGAException (adm.getError());
			}
			t.commit();
			modo = exitoModal("messages.censo.solicitudes.exito",request);
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return modo;
	}
	
	protected String solicitarNuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
 {
		String modo = "solicitarNuevo";			
		try{
			Vector ocultos = new Vector();
			DatosCVForm form= (DatosCVForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);			
			String accion = (String)request.getParameter("accion");
			
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona",  form.getIdPersona());	
			request.setAttribute("idInstitucion", form.getIDInstitucion());
			request.setAttribute("modoConsulta", modo);	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;				
	}

	/**
	 * 
	 * @param form
	 * @param request
	 * @return
	 * @throws SIGAException
	 */
	protected CenSolicitudModificacionCVBean getDatos(DatosCVForm form, HttpServletRequest request) throws SIGAException {
		CenSolicitudModificacionCVBean bean = null;
		Integer idTipoCVSubtipo1 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idTipoCVSubtipo2=null;
		Integer idInstitucionSubtipo2=null;
		try {
			bean = new CenSolicitudModificacionCVBean();
			CenSolicitudModificacionCVAdm adm = new CenSolicitudModificacionCVAdm(this.getUserBean(request));
			bean.setIdSolicitud(adm.getNuevoId());
			bean.setMotivo(form.getMotivo());
			bean.setFechaInicio(form.getFechaInicio());
			String[] datosCVSubtipo1;
			if (form.getIdTipoCVSubtipo1()!=null && !form.getIdTipoCVSubtipo1().equals("")){
			 datosCVSubtipo1=form.getIdTipoCVSubtipo1().toString().split("@");
			  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
			  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
			} 
			if (form.getIdTipoCVSubtipo2()!=null && !form.getIdTipoCVSubtipo2().equals("")){ 
			 String[] datosCVSubtipo2;
			 datosCVSubtipo2=form.getIdTipoCVSubtipo2().toString().split("@");
			  idTipoCVSubtipo2=new Integer(datosCVSubtipo2[0]);
			  idInstitucionSubtipo2=new Integer(datosCVSubtipo2[1]);
			}
			if (idTipoCVSubtipo1!=null && !idTipoCVSubtipo1.equals("")){
				  bean.setIdTipoCVSubtipo1(idTipoCVSubtipo1.toString());
				  bean.setIdInstitucion_subt1(idInstitucionSubtipo1);
				}else{
					bean.setIdTipoCVSubtipo1("");
					bean.setIdInstitucion_subt1(null);
				}
				if(idTipoCVSubtipo2!=null && !idTipoCVSubtipo2.equals("")){
				 bean.setIdTipoCVSubtipo2(idTipoCVSubtipo2.toString());
				 bean.setIdInstitucion_subt2(idInstitucionSubtipo2);
				}else{
					bean.setIdTipoCVSubtipo2("");
					bean.setIdInstitucion_subt2(null);
				}
				
			bean.setFechaFin(form.getFechaFin());
			bean.setDescripcion(form.getDescripcion());
			bean.setIdPersona(Long.valueOf((String)request.getParameter("idPersona")));		
			bean.setIdInstitucion(Integer.valueOf((String)request.getParameter("idInstitucion")));
			if(!((String)request.getParameter("idCV")).equals("")){
				bean.setIdCV(Integer.valueOf((String)request.getParameter("idCV")));
			}		
			bean.setIdTipoCV(form.getTipoApunte());	
			bean.setIdEstadoSolic(new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
			bean.setFechaAlta("sysdate");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return bean;
	}
	@SuppressWarnings("unchecked")
	protected String borrarRegistro (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			DatosCVForm form= (DatosCVForm) formulario;
			
			String idPersona =form.getIdPerson(); 
			Integer idInstitucionPersona = form.getIDInstitucionCargo();
			Integer idInstitucion = form.getIDInstitucion();
			Integer idCV = Integer.valueOf(form.getIDCV());
			ClsLogging.writeFileLog("BUSQUEDA COMISIONES:getAjaxBorrarCargo.idPersona:"+idPersona+"/", 10);
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenDatosCVAdm admDatosCV = new CenDatosCVAdm (this.getUserBean(request));
			Hashtable clavesCV = new Hashtable();
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDPERSONA, new Long(idPersona));
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDCV, idCV);
			UtilidadesHash.set (clavesCV, CenDatosCVBean.C_IDINSTITUCIONCARGO, idInstitucionPersona);
			JSONObject json = new JSONObject();
			t.begin();
			if (!admDatosCV.eliminarCargo(clavesCV)) {
				throw new SIGAException (admDatosCV.getError());
			}
			t.commit();
			

			json.put("existe", "false");
			
			List listaParametros = new ArrayList();
			
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );


		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return "completado";
	}

	@SuppressWarnings("unchecked")
	protected String obtenerColegiado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			DatosCVForm form= (DatosCVForm) formulario;
			
			Long idPersona = form.getIdPersona(); 
			Integer idInstitucionPersona = form.getIDInstitucionCargo();
			ClsLogging.writeFileLog("BUSQUEDA COMISIONES:getAjaxBorrarCargo.idPersona:"+idPersona+"/", 10);
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenColegiadoAdm admCol =  new CenColegiadoAdm(this.getUserBean(request));
			String numcol=admCol.getIdentificadorColegiado(admCol.getDatosColegiales(new Long(idPersona),new Integer(idInstitucionPersona)));
			
			List listaParametros = new ArrayList();
			listaParametros.add(numcol);
		
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );


		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}
		return "completado";
	}
	
}