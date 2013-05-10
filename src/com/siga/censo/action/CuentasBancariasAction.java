/*
 * Created on Dec 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
import org.json.JSONObject;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBancosAdm;
import com.siga.beans.CenBancosBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenSolicModiCuentasAdm;
import com.siga.beans.CenSolicModiCuentasBean;
import com.siga.censo.form.CuentasBancariasForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CuentasBancariasAction extends MasterAction{

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
			//BEGIN BNS 11/12/12 INCIDENCIA INC_08950_SIGA
				} else if(accion.equalsIgnoreCase("guardarInsertarHistorico")){
					mapDestino = guardarInsertarHistorico(mapping, miForm, request, response);
			//END BNS
				} else if ( accion.equalsIgnoreCase("getAjaxBanco")){
					getAjaxBanco (mapping, miForm, request, response);
					return null;
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 				
			 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}

	}	

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
			String estadoColegial="";
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			numero = colegiadoAdm.getIdentificadorColegiado(bean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucionPersona));
/*			String mensaje = "";
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			if(nombre!=""){
				String sWhere = " where " + CenClienteBean.C_IDINSTITUCION + "=" + idInstitucion;
				sWhere += " and " + CenClienteBean.C_IDPERSONA + "=" + idPersona;
				vCliente = clienteAdm.select(sWhere);
				if(vCliente.size()>0){
					numero = clienteAdm.obtenerNumero(idPersona,idInstitucionPersona);			
					v = clienteAdm.getCuentasBancarias(idPersona,idInstitucionPersona);	
				}else{
					mensaje="messages.updated.notFound";
				}				
			}else{
				mensaje="messages.updated.notFound";
			}	
	*/
			
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(((CuentasBancariasForm)formulario).getIncluirRegistrosConBajaLogica());
			request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
			v = clienteAdm.getCuentasBancarias(idPersona,idInstitucionPersona, bIncluirRegistrosConBajaLogica);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("accion", accion);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("vDatos", v);
			request.setAttribute("estadoColegial", estadoColegial);
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
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;
			
			Vector ocultos = new Vector();
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			

			Integer idCuenta = Integer.valueOf((String)ocultos.elementAt(0));			
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(2));
			
			String sociedad = (String)ocultos.elementAt(3);
			String accion = (String)request.getParameter("accion");	
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getCuentasBancarias(idPersona,idInstitucionPersona,idCuenta);			
			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);		
			request.setAttribute("modoConsulta", modo);
			request.setAttribute("sociedad", sociedad);			
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
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			Integer idCuenta = Integer.valueOf((String)ocultos.elementAt(0));			
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(2));
			String sociedad = (String)ocultos.elementAt(3);
			String accion = (String)request.getParameter("accion");	
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			Hashtable hash = clienteAdm.getCuentasBancarias(idPersona,idInstitucionPersona,idCuenta);
	
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("modoConsulta", modo);	
			request.setAttribute("sociedad", sociedad);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "nuevo";
 		try
		{
 			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;
			request.setAttribute("modoConsulta", modo);			
			request.setAttribute("idPersona", miForm.getIdPersona());			
			request.setAttribute("idInstitucion", miForm.getIdInstitucion());			
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransactionPesada();
			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;

			// Comprobamos si el cliente ya tiene una cuenta de tipo SJCS
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			if ((miForm.getAbonoSJCS().booleanValue()) &&
				(cuentasAdm.existeCuentaAbonoSJCS(miForm.getIdPersona(), miForm.getIdInstitucion()))) {
				return exito("messages.censo.existeAbonoSJCS", request);
			}

			// Fijamos los datos de la cuenta
			CenCuentasBancariasBean beanCuentas  = new CenCuentasBancariasBean ();
			if(miForm.getAbonoSJCS().booleanValue())beanCuentas.setAbonoSJCS(ClsConstants.DB_TRUE);			
			else									beanCuentas.setAbonoSJCS(ClsConstants.DB_FALSE);
			beanCuentas.setAbonoCargo(this.validarTipoCuenta(miForm.getCuentaAbono(), miForm.getCuentaCargo()));
			beanCuentas.setCbo_Codigo(miForm.getCbo_Codigo());
			beanCuentas.setCodigoSucursal(miForm.getCodigoSucursal());
			beanCuentas.setCuentaContable(miForm.getCuentaContable());
			beanCuentas.setDigitoControl(miForm.getDigitoControl());
			beanCuentas.setFechaBaja(null);
			beanCuentas.setIdInstitucion(miForm.getIdInstitucion());
			beanCuentas.setIdPersona(miForm.getIdPersona());
			beanCuentas.setNumeroCuenta(miForm.getNumeroCuenta());
			beanCuentas.setTitular(miForm.getTitular());
			
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			t.begin();
			if (!cuentasAdm.insertarConHistorico(beanCuentas, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (cuentasAdm.getError());
			}
			
			// Lanzamos el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(""+miForm.getIdInstitucion(),
																					  ""+miForm.getIdPersona(),
																					  "",
																					  ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");

			t.commit();
		}
		catch (ClsExceptions e){
			try {
				if (t!=null) {
					t.rollback();
				}
			} catch (Exception el) {
				e.printStackTrace();
			}			
				throw new SIGAException("messages.censo.cuentasBancarias.errorSucursal");			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}		

		return exitoModal("messages.inserted.success", request);
	}

	//BEGIN BNS 11/12/12 INCIDENCIA INC_08950_SIGA
	/*
	 * Método que guarda la modificación como un nuevo registro en la tabla de cuentas alimentando el histórico
	 * y mantiene el anterior con fecha de baja alimentando también el histórico
	 * @author BNS 11-12-12
	 * @version 1
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	private String guardarInsertarHistorico(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		String sResult = null;
		UserTransaction t = null;
		try{
			t = this.getUserBean(request).getTransactionPesada();
			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;
			
			// Comprobamos si el cliente ya tiene una cuenta de tipo SJCS
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			if ((miForm.getAbonoSJCS().booleanValue()) &&
				(cuentasAdm.existeCuentaAbonoSJCS(miForm.getIdPersona(), miForm.getIdInstitucion(), miForm.getIdCuenta()))) {
				sResult = exito("messages.censo.existeAbonoSJCS", request);
			} else {
				// Fijamos los datos de la direccion
				CenCuentasBancariasBean beanCuentas = new CenCuentasBancariasBean();
				if(miForm.getAbonoSJCS().booleanValue())
					beanCuentas.setAbonoSJCS(ClsConstants.DB_TRUE);			
				else
					beanCuentas.setAbonoSJCS(ClsConstants.DB_FALSE);
				beanCuentas.setAbonoCargo(this.validarTipoCuenta(miForm.getCuentaAbono(), miForm.getCuentaCargo()));
				beanCuentas.setCbo_Codigo(miForm.getCbo_Codigo());
				beanCuentas.setCodigoSucursal(miForm.getCodigoSucursal());
				beanCuentas.setCuentaContable(miForm.getCuentaContable());
				beanCuentas.setDigitoControl(miForm.getDigitoControl());
				beanCuentas.setFechaBaja(null);
				beanCuentas.setIdCuenta(miForm.getIdCuenta());
				beanCuentas.setIdInstitucion(miForm.getIdInstitucion());
				beanCuentas.setIdPersona(miForm.getIdPersona());
				beanCuentas.setNumeroCuenta(miForm.getNumeroCuenta());
				beanCuentas.setTitular(miForm.getTitular());
				beanCuentas.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP"));
				String abonoCargoOrig=(String)request.getParameter("abonoCargoOrig");
				
				// Fijamos los datos del Historico
				CenHistoricoBean beanHis = new CenHistoricoBean();
				beanHis.setMotivo(miForm.getMotivo());	
				
				t.begin();
				int iUpdateConHistoricoYfecBajResult = cuentasAdm.updateConHistoricoYfecBaj(beanCuentas, beanHis, this.getUserName(request), this.getUserBean(request), abonoCargoOrig, this.getLenguaje(request));
				if (iUpdateConHistoricoYfecBajResult < 0) {
					throw new SIGAException (cuentasAdm.getError());
				}
				
				t.commit();
				
				if (iUpdateConHistoricoYfecBajResult == 1)
					sResult = exitoModal("messages.updated.borrarCuenta", request);
				else if (iUpdateConHistoricoYfecBajResult == 2)
					sResult = exitoModal("messages.updated.actualizarCuenta", request);
				else 
					sResult = exitoModal("messages.updated.success", request);
			}
		} catch (ClsExceptions e){
			try {
				if (t!=null) {
					t.rollback();
				}
			} catch (Exception el) {
				e.printStackTrace();
			}			
				throw new SIGAException("messages.censo.cuentasBancarias.errorSucursal");			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}

		return sResult;
	}
	// END BNS
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransactionPesada();
			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;

			// Comprobamos si el cliente ya tiene una cuenta de tipo SJCS
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			if ((miForm.getAbonoSJCS().booleanValue()) &&
//				(cuentasAdm.existeCuentaAbonoSJCS(miForm.getIdPersona(), miForm.getIdInstitucion(), miForm.getCbo_Codigo(), miForm.getCodigoSucursal(), miForm.getDigitoControl(), miForm.getNumeroCuenta()))) {
				(cuentasAdm.existeCuentaAbonoSJCS(miForm.getIdPersona(), miForm.getIdInstitucion(), miForm.getIdCuenta()))) {

				return exito("messages.censo.existeAbonoSJCS", request);
			}

			// Fijamos los datos de la direccion
			CenCuentasBancariasBean beanCuentas = new CenCuentasBancariasBean();
			if(miForm.getAbonoSJCS().booleanValue())beanCuentas.setAbonoSJCS(ClsConstants.DB_TRUE);			
			else									beanCuentas.setAbonoSJCS(ClsConstants.DB_FALSE);
			beanCuentas.setAbonoCargo(this.validarTipoCuenta(miForm.getCuentaAbono(), miForm.getCuentaCargo()));
			beanCuentas.setCbo_Codigo(miForm.getCbo_Codigo());
			beanCuentas.setCodigoSucursal(miForm.getCodigoSucursal());
			beanCuentas.setCuentaContable(miForm.getCuentaContable());
			beanCuentas.setDigitoControl(miForm.getDigitoControl());
			beanCuentas.setFechaBaja(null);
			beanCuentas.setIdCuenta(miForm.getIdCuenta());
			beanCuentas.setIdInstitucion(miForm.getIdInstitucion());
			beanCuentas.setIdPersona(miForm.getIdPersona());
			beanCuentas.setNumeroCuenta(miForm.getNumeroCuenta());
			beanCuentas.setTitular(miForm.getTitular());
			beanCuentas.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP"));
			String abonoCargoOrig=(String)request.getParameter("abonoCargoOrig");
			
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			t.begin();
			if (!cuentasAdm.updateConHistorico(beanCuentas, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (cuentasAdm.getError());
			}
			
			// Lanzamos el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(""+miForm.getIdInstitucion(),
																					  ""+miForm.getIdPersona(),
																					  "",
																					  ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			
			
			CenCuentasBancariasAdm cuentaAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			
			String where=" WHERE "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+miForm.getIdInstitucion()+
			             " and "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDPERSONA+"="+miForm.getIdPersona()+
						 " and "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_FECHABAJA+" is null "+
						 " AND "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_ABONOCARGO +" IN ('T','C')";
			Vector v = cuentaAdm.select(where);
			
			if(!(abonoCargoOrig.equals(ClsConstants.TIPO_CUENTA_ABONO) && beanCuentas.getAbonoCargo().equals(ClsConstants.TIPO_CUENTA_ABONO))){
			  if (v.size() == 0) {//si ya no hay cuenta de cargo o de abonoCargo se pone la forma de pago en metalico
	
				String resultado1[] = EjecucionPLs.ejecutarPL_RevisionCuentaBanco(""+miForm.getIdInstitucion(),
						  ""+miForm.getIdPersona(),
						  ""+this.getUserName(request));
				if ((resultado1 == null) || (!resultado1[0].equals("0"))){
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_CUENTABANCO");
				}
				t.commit();
				return exitoModal("messages.updated.borrarCuenta", request);
			  }else{// Si hay cuenta de cargo o de abonoCargo se actualizara con la mas reciente
			  	String resultado1[] = EjecucionPLs.ejecutarPL_ActualizarCuentaBanco(""+miForm.getIdInstitucion(),
						  ""+miForm.getIdPersona(),
						  ""+miForm.getIdCuenta(),
						  ""+this.getUserName(request));
				if ((resultado1 == null) || (!resultado1[0].equals("0"))){
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_ACTUALIZAR_CUENTABANCO");
				}	
				t.commit();
				return exitoModal("messages.updated.actualizarCuenta", request);
			  }
			}

			t.commit();
		}
		catch (ClsExceptions e){
			try {
				if (t!=null) {
					t.rollback();
				}
			} catch (Exception el) {
				e.printStackTrace();
			}			
				throw new SIGAException("messages.censo.cuentasBancarias.errorSucursal");			
		}catch (Exception e) {
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
			t = this.getUserBean(request).getTransactionPesada();
			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;

			Long 	idPersona 		= miForm.getIdPersona();
			Integer idInstitucion 	= miForm.getIdInstitucion();
			Integer idCuenta 		= new Integer((String) miForm.getDatosTablaOcultos(0).get(0));
			String TipoCuenta=(String)miForm.getDatosTablaVisibles(0).get(1);

			CenCuentasBancariasAdm cuentaAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			Hashtable clavesCuentas = new Hashtable();
			
			UtilidadesHash.set (clavesCuentas, CenCuentasBancariasBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (clavesCuentas, CenCuentasBancariasBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set (clavesCuentas, CenCuentasBancariasBean.C_IDCUENTA, idCuenta);
			String where=" WHERE "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion+
			             " and "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona+
						 " and "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_FECHABAJA+" is null "+
						 " AND "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_ABONOCARGO +" IN ('T','C','A')";
						 
			Vector v = cuentaAdm.select(where);
//			Vector v = cuentaAdm.select(claves);
//			if (v.size() != 1) {
//				return error("messages.deleted.error", new ClsExceptions (UtilidadesString.getMensajeIdioma(this.getUserBean(request), "messages.deleted.error")), request);
//			}
//			CenCuentasBancariasBean beanCuenta = (CenCuentasBancariasBean) v.get(0);
//			beanCuenta.setFechaBaja("sysdate");

			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
	
			t.begin();
			if (!cuentaAdm.deleteConHistorico(clavesCuentas, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (cuentaAdm.getError());
			}
			
			// Lanzamos el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(""+miForm.getIdInstitucion(),
					  ""+miForm.getIdPersona(),
					  "",
					  ""+this.getUserName(request));
			
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			
			
			// INC_6218 Para los abonos tambien se comprueba si hay que actualizar
			// el idcuenta y el estado
//		    if (!TipoCuenta.equalsIgnoreCase("Abono")){
			 if (v.size() == 1) {//si solo tiene una cuenta y la borramos
				String resultado1[] = EjecucionPLs.ejecutarPL_RevisionCuentaBanco(""+miForm.getIdInstitucion(),
																				  ""+miForm.getIdPersona(),
																				  ""+this.getUserName(request)); 
					if ((resultado1 == null) || (!resultado1[0].equals("0"))){
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_CUENTABANCO");
					}
					t.commit();
					return exitoRefresco("messages.updated.borrarCuenta", request);
			 }else{
				if (v.size() > 1){// Si hay mas de una cuenta de cargo o de abonoCargo se actualizara con la mas reciente
			  	String resultado1[] = EjecucionPLs.ejecutarPL_ActualizarCuentaBanco(""+miForm.getIdInstitucion(),
						  ""+miForm.getIdPersona(),
						  ""+idCuenta,
						  ""+this.getUserName(request));
				if ((resultado1 == null) || (!resultado1[0].equals("0"))){
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_ACTUALIZAR_CUENTABANCO");
				}	
				t.commit();
				return exitoRefresco("messages.updated.actualizarCuenta", request);
			  
				}	
			 }
//			}
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
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			Vector ocultos = new Vector();
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Integer idCuenta = Integer.valueOf((String)ocultos.elementAt(0));			
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(2));
			String sociedad = (String)ocultos.elementAt(3);
			String accion = (String)request.getParameter("accion");				
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());;
			Hashtable hash = clienteAdm.getCuentasBancarias(idPersona,idInstitucionPersona,idCuenta);

			request.setAttribute("hDatos", hash);			
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("sociedad", sociedad);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}
	
	protected String insertarModificacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction t = null;
		try
		{	
			t = this.getUserBean(request).getTransaction();	
			
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;				
			CenSolicModiCuentasAdm adm = new CenSolicModiCuentasAdm(getUserBean(request));

			String modo = "insertarModificacion";
			
			t.begin();	
			CenSolicModiCuentasBean bean = getDatos(form, request);
			if(!adm.insert(bean)){
				throw new SIGAException (adm.getError());
			}
			t.commit();
		} 
		catch (ClsExceptions e){
			try {
				if (t!=null) {
					t.rollback();
				}
			} catch (Exception el) {
				e.printStackTrace();
			}			
				throw new SIGAException("messages.censo.cuentasBancarias.errorSucursal");			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}		

		return exitoModalSinRefresco("messages.censo.solicitudes.exito",request);
	}
	
	protected CenSolicModiCuentasBean getDatos(CuentasBancariasForm form, HttpServletRequest request) throws SIGAException {
		CenSolicModiCuentasBean bean = null;
		try {
			bean = new CenSolicModiCuentasBean();
			CenSolicModiCuentasAdm adm = new CenSolicModiCuentasAdm(getUserBean(request));
			bean.setIdSolicitud(adm.getNuevoId());
			bean.setMotivo(form.getMotivo());
			bean.setAbonoCargo(validarTipoCuenta(form.getCuentaAbono(), form.getCuentaCargo()));
		
			if(form.getAbonoSJCS().booleanValue()){			
				bean.setAbonoSJCS(ClsConstants.DB_TRUE);			
			}else{
				bean.setAbonoSJCS(ClsConstants.DB_FALSE);
			}
			bean.setCbo_Codigo(form.getCbo_Codigo());
			bean.setCodigoSucursal(form.getCodigoSucursal());
			bean.setDigitoControl(form.getDigitoControl());		
			bean.setNumeroCuenta(form.getNumeroCuenta());
			bean.setTitular(form.getTitular());		
			bean.setIdInstitucion(form.getIdInstitucion());
			bean.setIdPersona(form.getIdPersona());
			bean.setIdCuenta(form.getIdCuenta());
			bean.setIdEstadoSolic(new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
			bean.setFechaAlta("sysdate");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return bean;
	}

	/**
	 * 
	 * @param abono
	 * @param cargo
	 * @return
	 * @throws SIGAException
	 */
	String validarTipoCuenta (Boolean abono, Boolean cargo) throws SIGAException{
		try {
			if (abono.booleanValue() && cargo.booleanValue()) return ClsConstants.TIPO_CUENTA_ABONO_CARGO;
			if (abono.booleanValue()) return ClsConstants.TIPO_CUENTA_ABONO;
			if (cargo.booleanValue()) return ClsConstants.TIPO_CUENTA_CARGO;
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws ClsExceptions
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void getAjaxBanco (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {
		
		CenBancosAdm bancosAdm = new CenBancosAdm(this.getUserBean(request));
		
		String idBanco = (String)request.getParameter("idBanco");
		if (idBanco==null||idBanco.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador del banco");	
				
		CenBancosBean bancoBean = bancosAdm.getBanco(idBanco);
		
		JSONObject json = new JSONObject();				
		json.put("banco", bancoBean.getJSONObject());
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
	}	
}
