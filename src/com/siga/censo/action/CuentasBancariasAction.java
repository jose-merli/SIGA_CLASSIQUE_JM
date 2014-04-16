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
import com.siga.beans.CenMandatosCuentasBancariasAdm;
import com.siga.beans.CenMandatosCuentasBancariasBean;
import com.siga.beans.CenPaisAdm;
import com.siga.beans.CenPaisBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenSolicModiCuentasAdm;
import com.siga.beans.CenSolicModiCuentasBean;
import com.siga.censo.form.CuentasBancariasForm;
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
					
				} else if (accion.equalsIgnoreCase("informacionCuentaBancaria")){
					mapDestino = this.informacionCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("listadoMandatosCuentaBancaria")){
					mapDestino = this.listadoMandatosCuentaBancaria(mapping, miForm, request, response);				
					
				} else if ( accion.equalsIgnoreCase("getAjaxBanco")){
					getAjaxBanco (mapping, miForm, request, response);
					return null;	
					
				} else if ( accion.equalsIgnoreCase("getAjaxBancoBIC")){
					getAjaxBancoBIC (mapping, miForm, request, response);
					return null;
					
				} else if ( accion.equalsIgnoreCase("getAjaxCargaInicialBancoBIC")){
					getAjaxCargaInicialBancoBIC (mapping, miForm, request, response);
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
		String modo = "gestionarCuentaBancaria";			
		try {
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);							

			Integer idCuenta = Integer.valueOf((String)ocultos.elementAt(0));			
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(2));			
			String sociedad = (String) ocultos.elementAt(3);											
			
			// JPT: Remueve la variable de sesion que sirve para guardar y anadir a historico
			request.getSession().removeAttribute("idCuentaHistorico");

			Hashtable hashCuentasBancarias = new Hashtable();			
			hashCuentasBancarias.put("nombrePersona", request.getParameter("nombreUsuario"));
			hashCuentasBancarias.put("numero", request.getParameter("numeroUsuario"));			
			hashCuentasBancarias.put("idPersona", idPersona);
			hashCuentasBancarias.put("idInstitucion", idInstitucionPersona);
			hashCuentasBancarias.put("sociedad", sociedad);
			hashCuentasBancarias.put("modoConsulta", "editar");
			hashCuentasBancarias.put("idCuenta", idCuenta);	
			
			request.setAttribute("hashCuentasBancarias", hashCuentasBancarias);
			request.setAttribute("modosCuentasBancarias", form.getModos());	
			
			request.getSession().setAttribute("idCuenta", idCuenta);
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "gestionarCuentaBancaria";
		try {
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;			
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);				
			
			Integer idCuenta = Integer.valueOf((String)ocultos.elementAt(0));			
			Long idPersona = Long.valueOf((String)ocultos.elementAt(1));
			Integer idInstitucionPersona = Integer.valueOf((String)ocultos.elementAt(2));
			String sociedad = (String)ocultos.elementAt(3);
			
			// JPT: Remueve la variable de sesion que sirve para guardar y anadir a historico
			request.getSession().removeAttribute("idCuentaHistorico");			
			
			Hashtable hashCuentasBancarias = new Hashtable();			
			hashCuentasBancarias.put("nombrePersona", request.getParameter("nombreUsuario"));
			hashCuentasBancarias.put("numero", request.getParameter("numeroUsuario"));			
			hashCuentasBancarias.put("idPersona", idPersona);
			hashCuentasBancarias.put("idInstitucion", idInstitucionPersona);
			hashCuentasBancarias.put("sociedad", sociedad);
			hashCuentasBancarias.put("modoConsulta", "ver");
			hashCuentasBancarias.put("idCuenta", idCuenta);	
			
			request.setAttribute("hashCuentasBancarias", hashCuentasBancarias);
			request.setAttribute("modosCuentasBancarias", form.getModos());	
			
			request.getSession().setAttribute("idCuenta", idCuenta);
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return modo;			
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "gestionarCuentaBancaria";
 		try	{
 			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;
 			
			// JPT: Remueve la variable de sesion que sirve para guardar y anadir a historico
			request.getSession().removeAttribute("idCuentaHistorico");			
			
			Hashtable hashCuentasBancarias = new Hashtable();			
			hashCuentasBancarias.put("nombrePersona", request.getParameter("nombreUsuario"));
			hashCuentasBancarias.put("numero", request.getParameter("numeroUsuario"));					
			hashCuentasBancarias.put("idPersona", miForm.getIdPersona());
			hashCuentasBancarias.put("idInstitucion", miForm.getIdInstitucion());
			hashCuentasBancarias.put("modoConsulta", "nuevo");
			
			request.setAttribute("hashCuentasBancarias", hashCuentasBancarias);
			request.setAttribute("modosCuentasBancarias", miForm.getModos());	
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

			// Fijamos los datos de la cuenta
			CenCuentasBancariasBean beanCuentas  = new CenCuentasBancariasBean ();
			if(miForm.getAbonoSJCS().booleanValue())
				beanCuentas.setAbonoSJCS(ClsConstants.DB_TRUE);			
			else
				beanCuentas.setAbonoSJCS(ClsConstants.DB_FALSE);
			beanCuentas.setAbonoCargo(this.validarTipoCuenta(miForm.getCuentaAbono(), miForm.getCuentaCargo()));
			
			//Rellenamos el codigo del banco e insertamos un banco extranjero
			if(miForm.getIBAN()!=null && !miForm.getIBAN().equals("")){
				if(!miForm.getIBAN().substring(0,2).equals("ES")){
					CenBancosAdm bancosAdm = new CenBancosAdm(this.getUserBean(request));
					CenBancosBean bancosBean = bancosAdm.existeBancoExtranjero(miForm.getBIC());
					if(bancosBean == null){
						CenPaisAdm paisAdm = new CenPaisAdm(this.getUserBean(request));
						CenPaisBean paisBean = paisAdm.getPaisByCodIso(miForm.getIBAN().substring(0,2));
						bancosBean = bancosAdm.insertarBancoExtranjero(paisBean.getIdPais(), miForm.getBIC());
					}
					
					beanCuentas.setCbo_Codigo(bancosBean.getCodigo());
					beanCuentas.setCodigoSucursal(null);
					beanCuentas.setDigitoControl(null);		
					beanCuentas.setNumeroCuenta(null);					
				
				} else {
					beanCuentas.setCbo_Codigo(miForm.getIBAN().substring(4,8));
					beanCuentas.setCodigoSucursal(miForm.getIBAN().substring(8,12));
					beanCuentas.setDigitoControl(miForm.getIBAN().substring(12,14));		
					beanCuentas.setNumeroCuenta(miForm.getIBAN().substring(14));
				}

			} else { // SE DEJA LO QUE HAY
				beanCuentas.setCodigoSucursal(miForm.getCodigoSucursal());
				beanCuentas.setDigitoControl(miForm.getDigitoControl());		
				beanCuentas.setNumeroCuenta(miForm.getNumeroCuenta());
			}
			
			beanCuentas.setIban(miForm.getIBAN());
			beanCuentas.setFechaBaja(null);
			beanCuentas.setIdInstitucion(miForm.getIdInstitucion());
			beanCuentas.setIdPersona(miForm.getIdPersona());
			beanCuentas.setCuentaContable(miForm.getCuentaContable());
			beanCuentas.setTitular(miForm.getTitular());
			
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			t.begin();
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			if (!cuentasAdm.insertarConHistorico(beanCuentas, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (cuentasAdm.getError());
			}
			
			Integer iResult = cuentasAdm.revisionesCuentas(beanCuentas, this.getUserName(request), this.getUserBean(request),true);

			t.commit();
			
			// JPT: Guardo el nuevo identificador de cuenta en la sesion
			request.getSession().setAttribute("idCuentaHistorico", beanCuentas.getIdCuenta());
			
		} catch (ClsExceptions e){
			try {
				if (t!=null) {
					t.rollback();
				}
			
			} catch (Exception el) {
				e.printStackTrace();
			}			
			
			throw new SIGAException("messages.censo.cuentasBancarias.errorCuentaBancaria");			
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}		

		return exitoRefresco("messages.inserted.success", request);
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
			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;				
			
			// JPT: Compruebo si existe la variable de sesion que sirve para guardar y anadir a historico, o bien obtengo el dato del formulario
			Integer iIdCuenta = (Integer) request.getSession().getAttribute("idCuentaHistorico");
			if (iIdCuenta == null) {
				iIdCuenta = miForm.getIdCuenta();
			}			
			
			// Fijamos los datos de la direccion
			CenCuentasBancariasBean beanCuentas = new CenCuentasBancariasBean();
			if(miForm.getAbonoSJCS().booleanValue())
				beanCuentas.setAbonoSJCS(ClsConstants.DB_TRUE);			
			else
				beanCuentas.setAbonoSJCS(ClsConstants.DB_FALSE);
			beanCuentas.setAbonoCargo(this.validarTipoCuenta(miForm.getCuentaAbono(), miForm.getCuentaCargo()));
			beanCuentas.setIban(miForm.getIBAN());
			beanCuentas.setCbo_Codigo(miForm.getCbo_Codigo());
			beanCuentas.setCodigoSucursal(miForm.getCodigoSucursal());
			beanCuentas.setNumeroCuenta(miForm.getNumeroCuenta());
			beanCuentas.setDigitoControl(miForm.getDigitoControl());
			beanCuentas.setFechaBaja(null);
			beanCuentas.setIdCuenta(iIdCuenta);
			beanCuentas.setIdInstitucion(miForm.getIdInstitucion());
			beanCuentas.setIdPersona(miForm.getIdPersona());
			beanCuentas.setCuentaContable(miForm.getCuentaContable());
			beanCuentas.setTitular(miForm.getTitular());
			String abonoCargoOrig=(String)request.getParameter("abonoCargoOrig");
			
			// JPT: Obtiene los datos de la cuenta original sin modificar
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request), this.getUserBean(request), miForm.getIdInstitucion().intValue(), miForm.getIdPersona().longValue());
			Hashtable hashCuentasBancarias = clienteAdm.getCuentasBancarias(miForm.getIdPersona(), miForm.getIdInstitucion(), iIdCuenta);		
			beanCuentas.setOriginalHash(hashCuentasBancarias);			
			
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());	
			
			t = this.getUserBean(request).getTransactionPesada();
			t.begin();
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			int iUpdateConHistoricoYfecBajResult = cuentasAdm.updateConHistoricoYfecBaj(beanCuentas, beanHis, this.getUserName(request), this.getUserBean(request), abonoCargoOrig, this.getLenguaje(request));
			if (iUpdateConHistoricoYfecBajResult < 0) {
				throw new SIGAException (cuentasAdm.getError());
			}
			
			t.commit();
			
			// JPT: Guardo el nuevo identificador de cuenta en la sesion
			request.getSession().setAttribute("idCuentaHistorico", beanCuentas.getIdCuenta());
			
			if (iUpdateConHistoricoYfecBajResult == 1)
				sResult = "messages.updated.borrarCuenta";
			
			else if (iUpdateConHistoricoYfecBajResult == 2)
				sResult = "messages.updated.actualizarCuenta";
			
			else 
				sResult = "messages.updated.success";

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

		return exitoRefresco(sResult, request);
	}
	// END BNS
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction t = null;
		String retorno = "messages.updated.success";
		try {			
			CuentasBancariasForm miForm = (CuentasBancariasForm) formulario;

			// JPT: Compruebo si existe la variable de sesion que sirve para guardar y anadir a historico, o bien obtengo el dato del formulario
			Integer iIdCuenta = (Integer) request.getSession().getAttribute("idCuentaHistorico");
			if (iIdCuenta == null) {
				iIdCuenta = miForm.getIdCuenta();
			}				

			// Fijamos los datos de la direccion
			CenCuentasBancariasBean beanCuentas = new CenCuentasBancariasBean();
			if(miForm.getAbonoSJCS().booleanValue())
				beanCuentas.setAbonoSJCS(ClsConstants.DB_TRUE);			
			else
				beanCuentas.setAbonoSJCS(ClsConstants.DB_FALSE);
			beanCuentas.setAbonoCargo(this.validarTipoCuenta(miForm.getCuentaAbono(), miForm.getCuentaCargo()));
			beanCuentas.setIban(miForm.getIBAN());
			beanCuentas.setCbo_Codigo(miForm.getCbo_Codigo());
			beanCuentas.setCodigoSucursal(miForm.getCodigoSucursal());
			beanCuentas.setDigitoControl(miForm.getDigitoControl());
			beanCuentas.setNumeroCuenta(miForm.getNumeroCuenta());
			beanCuentas.setFechaBaja(null);
			beanCuentas.setIdCuenta(iIdCuenta);
			beanCuentas.setIdInstitucion(miForm.getIdInstitucion());
			beanCuentas.setIdPersona(miForm.getIdPersona());
			beanCuentas.setCuentaContable(miForm.getCuentaContable());
			beanCuentas.setTitular(miForm.getTitular());			
							
			// JPT: Obtiene los datos de la cuenta original sin modificar
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request), this.getUserBean(request), miForm.getIdInstitucion().intValue(), miForm.getIdPersona().longValue());
			Hashtable hashCuentasBancarias = clienteAdm.getCuentasBancarias(miForm.getIdPersona(), miForm.getIdInstitucion(), iIdCuenta);		
			beanCuentas.setOriginalHash(hashCuentasBancarias);
			
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			t = this.getUserBean(request).getTransactionPesada();
			t.begin();
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			if (!cuentasAdm.updateConHistorico(beanCuentas, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (cuentasAdm.getError());
			}
			
			int iResult = cuentasAdm.revisionesCuentas(beanCuentas, this.getUserName(request), this.getUserBean(request),true);
			
			t.commit();

			if(iResult==1)
				retorno = "messages.updated.borrarCuenta";
			else if(iResult==2)
				retorno = "messages.updated.actualizarCuenta";			
			
		} catch (ClsExceptions e){
			try {
				if (t!=null) {
					t.rollback();
				}
			} catch (Exception el) {
				e.printStackTrace();
			}			
			
			throw new SIGAException("messages.censo.cuentasBancarias.errorSucursal");	
				
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}

		return exitoRefresco(retorno, request);
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

			CenCuentasBancariasAdm cuentaAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			Hashtable clavesCuentas = new Hashtable();
			
			UtilidadesHash.set (clavesCuentas, CenCuentasBancariasBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (clavesCuentas, CenCuentasBancariasBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set (clavesCuentas, CenCuentasBancariasBean.C_IDCUENTA, idCuenta);

			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
	
			t.begin();
			if (!cuentaAdm.deleteConHistorico(clavesCuentas, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (cuentaAdm.getError());
			}
			
			CenCuentasBancariasBean beanCuentas  = new CenCuentasBancariasBean ();
			
			beanCuentas.setIdInstitucion(idInstitucion);
			beanCuentas.setIdPersona(idPersona);
			beanCuentas.setIdCuenta(idCuenta);
			
			int iResult = cuentaAdm.revisionesCuentas(beanCuentas, this.getUserName(request), this.getUserBean(request),false);
			
			t.commit();

			if(iResult==1)
				return exitoRefresco("messages.updated.borrarCuenta", request);
			else if(iResult==2)
				return exitoRefresco("messages.updated.actualizarCuenta", request);			
			
		} catch (Exception e) {
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
		try {	
			t = this.getUserBean(request).getTransaction();	
			
			CuentasBancariasForm form = (CuentasBancariasForm) formulario;				
			CenSolicModiCuentasAdm adm = new CenSolicModiCuentasAdm(getUserBean(request));
			
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
			
			// Rellenamos el codigo del banco e insertamos un banco extranjero
			String iban = form.getIBAN();
			if(iban!=null && !iban.equals("")){
				if(!iban.substring(0,2).equals("ES")){
					CenBancosAdm bancosAdm = new CenBancosAdm(this.getUserBean(request));
					CenBancosBean bancosBean = bancosAdm.existeBancoExtranjero(form.getBIC());
					if(bancosBean == null){
						CenPaisAdm paisAdm = new CenPaisAdm(this.getUserBean(request));
						CenPaisBean paisBean = paisAdm.getPaisByCodIso(iban.substring(0,2));
						bancosBean = bancosAdm.insertarBancoExtranjero(paisBean.getIdPais(), form.getBIC());
					}
					bean.setCbo_Codigo(bancosBean.getCodigo());
					bean.setCodigoSucursal(null);
					bean.setDigitoControl(null);		
					bean.setNumeroCuenta(null);				
				
				}else{
					bean.setCbo_Codigo(iban.substring(4,8));
					bean.setCodigoSucursal(iban.substring(8,12));
					bean.setDigitoControl(iban.substring(12,14));		
					bean.setNumeroCuenta(iban.substring(14));					
				}
				
			} else{
				bean.setCodigoSucursal(form.getCodigoSucursal());
				bean.setDigitoControl(form.getDigitoControl());		
				bean.setNumeroCuenta(form.getNumeroCuenta());
			}
			
			bean.setIban(iban);
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
	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void getAjaxBancoBIC(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException ,Exception {
		
		CenBancosAdm bancosAdm = new CenBancosAdm(this.getUserBean(request));
		CenPaisAdm paisAdm = new CenPaisAdm(this.getUserBean(request));
		JSONObject json = new JSONObject();	
		String iban = (String)request.getParameter("iban");
		if (iban==null || iban.trim().equalsIgnoreCase(""))
			throw new SIGAException("El codigo IBAN es incorrecto");
		
		if(iban.length() >= 2 && paisAdm.isLongitudCorrectaIBAN(iban.substring(0,2),iban.length())){
			json.put("pais", iban.substring(0,2));
			
			//Comprobamos si es Español
			if(iban.substring(0,2).equals("ES")){
				CenBancosBean bancoBean = bancosAdm.getBancoIBAN(iban.substring(4,8));	
				if(bancoBean!=null){
					json.put("banco", bancoBean.getJSONObject());
				}
			} 
		}
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void getAjaxCargaInicialBancoBIC(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException ,Exception {
		
		CenBancosAdm bancosAdm = new CenBancosAdm(this.getUserBean(request));
		CenBancosBean bancoBean = null;
		JSONObject json = new JSONObject();	
		String iban = (String)request.getParameter("iban");
		String cbo = (String)request.getParameter("codigo");
		if (iban==null || iban.trim().equalsIgnoreCase(""))
			throw new SIGAException("El codigo IBAN es incorrecto");

		if(iban.substring(0,2).equals("ES")){
			bancoBean = bancosAdm.getBancoIBAN(iban.substring(4,8));	
		} else{			
			bancoBean = bancosAdm.getBancoIBAN(cbo);
		}
				
		if(bancoBean!=null){
			json.put("banco", bancoBean.getJSONObject());
		}
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 
	}	

	/**
	 * Obtengo la informacion de la cuenta bancaria
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String informacionCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");			
				
		//String sNombrePersona = (String) request.getParameter("nombrePersona");
		//String sNumero = (String) request.getParameter("numero");
		String sModo = (String) request.getParameter("modoConsulta");
		//String sSociedad = (String) request.getParameter("sociedad");
		
		// JPT: Compruebo si existe la variable de sesion que sirve para guardar y anadir a historico, o bien obtengo el dato de los parametros
		Integer iIdCuenta = (Integer) request.getSession().getAttribute("idCuentaHistorico");
		
		/*
		 * * JPT: Solo obtengo datos cuando hay cuenta:
		 * 1. Modo consulta
		 * 2. Modo edicion
		 * 3. Cuando acaba de crear una nueva cuenta
		 */
		if (sModo.equalsIgnoreCase("ver") || sModo.equalsIgnoreCase("editar") || iIdCuenta!= null) {
			
			String sIdPersona = (String) request.getParameter("idPersona");
			Long lIdPersona = Long.valueOf(sIdPersona);
			
			String sIdInstitucion = (String) request.getParameter("idInstitucion");
			Integer iIdInstitucion = Integer.valueOf(sIdInstitucion);			
							
			if (iIdCuenta == null) {
				String sIdCuenta = (String) request.getParameter("idCuenta");
				if (sIdCuenta != null)
					iIdCuenta = Integer.valueOf(sIdCuenta);
			}
			
			if (iIdCuenta != null) {						
				try{		
					CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request), user, iIdInstitucion.intValue(), lIdPersona.longValue());
					Hashtable hashCuentasBancarias = clienteAdm.getCuentasBancarias(lIdPersona, iIdInstitucion, iIdCuenta);			
					request.setAttribute("hashCuentasBancarias", hashCuentasBancarias);
				} catch (Exception e) {
					throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
				}
			}
		}
		
		return "informacionCuentaBancaria";		
	}
	
	/**
	 * Obtengo un listado de los mandatos de la cuenta bancaria
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String listadoMandatosCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");			
					
			//String sNombrePersona = (String) request.getParameter("nombrePersona");
			//String sNumero = (String) request.getParameter("numero");
			String sModo = (String) request.getParameter("modoConsulta");
			//String sSociedad = (String) request.getParameter("sociedad");
			
			// JPT: Compruebo si existe la variable de sesion que sirve para guardar y anadir a historico, o bien obtengo el dato de los parametros
			Integer iIdCuenta = (Integer) request.getSession().getAttribute("idCuentaHistorico");
			
			// JPT: Si tiene el modo nuevo y acaba de anadir una cuenta se considera que esta en modo edicion
			if (sModo.equals("nuevo") && iIdCuenta != null) {
				sModo = "editar";
			} 			
			request.setAttribute("modoListadoMandatos", sModo);						
			
			/*
			 * * JPT: Solo obtengo datos cuando hay cuenta:
			 * 1. Modo consulta
			 * 2. Modo edicion
			 * 3. Cuando acaba de crear una nueva cuenta
			 */
			if (sModo.equalsIgnoreCase("ver") || sModo.equalsIgnoreCase("editar")) {
				
				String sIdPersona = (String) request.getParameter("idPersona");
				Long lIdPersona = Long.valueOf(sIdPersona);
				
				String sIdInstitucion = (String) request.getParameter("idInstitucion");
				Integer iIdInstitucion = Integer.valueOf(sIdInstitucion);			
								
				if (iIdCuenta == null) {
					String sIdCuenta = (String) request.getParameter("idCuenta");
					if (sIdCuenta != null)
						iIdCuenta = Integer.valueOf(sIdCuenta);
				}
				
				if (iIdCuenta != null)  {
					CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usuario);
					Vector<CenMandatosCuentasBancariasBean> vMandatos = mandatosAdm.obtenerMandatos(iIdInstitucion, lIdPersona, iIdCuenta);  
					request.setAttribute("vListadoMandatos", vMandatos);
				}			
			}				
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}			
		
		return "listadoMandatosCuentaBancaria";		
	}
}