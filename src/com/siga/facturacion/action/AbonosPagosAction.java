/*
 * VERSIONES:
 * 
 * miguel.villegas - 16-03-2005 - Creacion
 *	
 */

/**
 * Clase action para el mantenimiento de pagos de los abonos.<br/>
 * Gestiona la edicion, consulta y borrado de las lineas de los abonos  
 */

package com.siga.facturacion.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacPagoAbonoEfectivoAdm;
import com.siga.beans.FacPagoAbonoEfectivoBean;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.beans.FacPagosPorCajaBean;
import com.siga.facturacion.form.AbonosPagosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class AbonosPagosAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
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
				}else if (accion.equalsIgnoreCase("pagarCaja")){
					mapDestino = pagarCaja(mapping, miForm, request, response);					
				}else if (accion.equalsIgnoreCase("pagarBanco")){
					mapDestino = pagarBanco(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("datosImpresion")){
					mapDestino = datosImpresion(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("realizarPagoBanco")){
					mapDestino = realizarPagoBanco(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("realizarPagoCaja")){
					mapDestino = realizarPagoCaja(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("compensarFactura")){
					mapDestino = compensarFactura(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("compensacionFacturaManual")){
					mapDestino = compensacionFacturaManual(mapping, miForm, request, response);
				}
				else if (accion.equalsIgnoreCase("compensarFacturaManual")){
					mapDestino = compensarFacturaManual(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("validarFacturaCompensacionManual")){
					mapDestino = validarFacturaCompensacionManual(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("pagarVariosAbonos")){
					mapDestino = pagarVariosAbonos(mapping, miForm, request, response);
				}else if ( accion.equalsIgnoreCase("existeCuentaSJCSAjax")){
					existeCuentaSJCSAjax (mapping, miForm, request, response);
					return null;	
				}else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new SIGAException("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		Vector seleccionados= new Vector();
		Vector totales= new Vector();

		try {
			String volver = null;
			if (request.getParameter("botonVolver")!=null )
				volver = (String)request.getParameter("botonVolver");
			else
				volver = "NO";
			
			// Obtengo el UserBean y los diferentes parametros recibidos
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String accion = (String)request.getParameter("accion");
			String idAbono = (String)request.getParameter("idAbono");
			String idFactura = (String)request.getParameter("idfactura");
			String idInstitucionAbono = (String)request.getParameter("idInstitucion");
			
			String idInstitucion=user.getLocation();

			// Obtengo manejadores para accesos a las BBDDs y obtengo la informacion buscada
			FacAbonoAdm adm = new FacAbonoAdm(this.getUserBean(request));
			seleccionados=adm.getDatosPagosAbono(idInstitucionAbono,idAbono);
			totales=adm.getTotalesPagos(idInstitucionAbono,idAbono);

			//idPagoJG:Esto es para saber si estamos en abonos de SJCS en Censo > Ficha > Turno Oficio > Facturaciones > Pagos
			request.setAttribute("idPagoJG", ((Row)seleccionados.firstElement()).getRow().get("IDPAGOSJG"));
			
			// Paso de parametros empleando request
			request.setAttribute("IDABONO", idAbono);
			request.setAttribute("IDFACTURA", idFactura);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("ACCION", accion);
			request.setAttribute("container", seleccionados);
			request.setAttribute("totales", totales);
			request.setAttribute("volver", volver);				
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrirAvanzada";
		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="editar";
			
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="ver";

		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="nuevo";
			
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="insertar";

		return (result);
	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="modificar";

		return (result);		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="borrar";
		
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";
		
		return (result);
				
	}

	/** 
	 *  Funcion que implementa la accion pagarCaja
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String pagarCaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="";

		try {
			result="pagarCaja";
			AbonosPagosForm form = (AbonosPagosForm) formulario;
			
			// Paso valores asociados al registro			
			request.setAttribute("IDABONO", form.getIdAbono());
			request.setAttribute("IDFACTURA", form.getIdFactura());
			request.setAttribute("IDINSTITUCION", form.getIdInstitucion());
			request.setAttribute("PAGOPENDIENTE", form.getPagoPendiente());
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}			
		return (result);
	}
	
	/** 
	 *  Funcion que implementa la accion pagarCaja
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String pagarBanco(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="";

		try {
			Vector entrada=new Vector();		
			Hashtable registro=new Hashtable();

			result="pagarBanco";
			AbonosPagosForm form = (AbonosPagosForm) formulario;
			FacAbonoAdm admAbono = new FacAbonoAdm(this.getUserBean(request));
			
			entrada = admAbono.getAbono(form.getIdInstitucion(),form.getIdAbono());
			registro = ((Row)entrada.firstElement()).getRow();
			
			// Paso valores asociados al registro			
			request.setAttribute("IDABONO", form.getIdAbono());
			request.setAttribute("IDFACTURA", form.getIdFactura());
			request.setAttribute("IDINSTITUCION", form.getIdInstitucion());
			request.setAttribute("PAGOPENDIENTE", form.getPagoPendiente());
			request.setAttribute("IDPERSONA", registro.get(FacAbonoBean.C_IDPERSONA));
			
			String sql = "SELECT " + CenCuentasBancariasBean.C_IDCUENTA + " AS ID, " +
							" F_SIGA_FORMATOIBAN(" + CenCuentasBancariasBean.C_IBAN + ") as DESCRIPCION " +
						" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + 
						" WHERE " + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + form.getIdInstitucion() +
							" AND " + CenCuentasBancariasBean.C_IDPERSONA + " = " + registro.get(FacAbonoBean.C_IDPERSONA) +					
							" AND " + CenCuentasBancariasBean.C_FECHABAJA + " IS NULL" +
							(registro.get(FacAbonoBean.C_IDPAGOSJG)!=null && !registro.get(FacAbonoBean.C_IDPAGOSJG).toString().equals("") 
								? " AND " + CenCuentasBancariasBean.C_ABONOSJCS + " = '1'" 
								: " AND " + CenCuentasBancariasBean.C_ABONOCARGO + " IN ('A','T')") +
						" ORDER BY DESCRIPCION";
					
			CenCuentasBancariasAdm cuentasAdm=new CenCuentasBancariasAdm(this.getUserBean(request));
			Vector vCuentas=cuentasAdm.selectGenerico(sql);
			
			if(vCuentas.size()==0)
				throw new SIGAException("facturacion.abonos.error.noExisteCuenta");
			
			List cuentasListFinal= new ArrayList();
			Hashtable cuentash=new Hashtable();
			for (int c = 0; c < vCuentas.size(); c++){
				cuentash = (Hashtable) vCuentas.get(c);
				cuentasListFinal.add(cuentash);
			}
	
			request.setAttribute("listaCuentas", cuentasListFinal);
			Hashtable cuentaSelh = (Hashtable) vCuentas.get(0);
			request.setAttribute("idCuentaSel", cuentaSelh.get("ID"));
			
		} catch (Exception e) { 
			if (e instanceof SIGAException || e instanceof ClsExceptions)
				throwExcp (e.getMessage(),new String[] {"modulo.facturacion"},e,null); 
			else
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 			
		}
		
		return (result);
	}
	
	/** 
	 *  Funcion que implementa la accion pagarCaja
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String datosImpresion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="";

		try {
			Vector ocultos=new Vector();
			Vector entrada=new Vector();
			Vector entradaAbono=new Vector();
			String idPersona="";
			String nombrePersona="";
			String nombreInstitucion="";
			String numeroAbono="";
			result="datosImpresion";
			AbonosPagosForm form = (AbonosPagosForm) formulario;
			FacPagoAbonoEfectivoAdm admin=new FacPagoAbonoEfectivoAdm(this.getUserBean(request));
			FacAbonoAdm adminAbono=new FacAbonoAdm(this.getUserBean(request));
			CenInstitucionAdm adminInstitucion=new CenInstitucionAdm(this.getUserBean(request));
			CenPersonaAdm adminPersona=new CenPersonaAdm(this.getUserBean(request));
		
			// Obtener valores a mostrar en la vista previa a la impresion
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
			entrada=admin.getPagoAbonoEfectivo((String)ocultos.get(1),(String)ocultos.get(0),(String)ocultos.get(2));
			entradaAbono=adminAbono.getAbono((String)ocultos.get(1),(String)ocultos.get(0));
			idPersona=(String)((Row)entradaAbono.firstElement()).getRow().get(FacAbonoBean.C_IDPERSONA);
			numeroAbono=(String)((Row)entradaAbono.firstElement()).getRow().get(FacAbonoBean.C_NUMEROABONO);			
			nombrePersona=adminPersona.obtenerNombreApellidos(idPersona);
			nombreInstitucion=adminInstitucion.getNombreInstitucion((String)ocultos.get(1));
						
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", entrada);
			request.setAttribute("nombrePersona", nombrePersona);
			request.setAttribute("numeroAbono", numeroAbono);			
			request.setAttribute("nombreInstitucion", nombreInstitucion);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}			
		return (result);
	}
	
	/** 
	 *  Funcion que implementa la accion realizarPagoBanco
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String realizarPagoBanco(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		boolean pagoVariosAbonos=false;
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Obtengo los datos del formulario
			AbonosPagosForm miForm = (AbonosPagosForm) formulario;
			
			//Inserto el pago por banco
			this.insertarPagoBanco(miForm,usr,pagoVariosAbonos);

			result=exitoModal("facturacion.abonosPagos.datosPagoAbono.abonoRealizado",request);
		} 
		catch (Exception e) { 
			if((e instanceof SIGAException)||(e instanceof ClsExceptions))
				throwExcp (e.getMessage(),new String[] {"modulo.facturacion"},e,null); 
			else
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 	
		}
		return (result);		
	}
	
	/** 
	 *  Funcion que implementa la accion realizarPagoCaja
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String realizarPagoCaja(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String result = "insertar";
		boolean pagoVariosAbonos=false;

		try {

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Obtengo los datos del formulario
			AbonosPagosForm miForm = (AbonosPagosForm) formulario;

			//Inserto el pago por caja
			this.insertarPagoCaja(miForm,usr,pagoVariosAbonos);
			
			result = exitoModal(
					"facturacion.abonosPagos.datosPagoAbono.abonoRealizado",
					request);

		} catch (Exception e) {
			
			if((e instanceof SIGAException)||(e instanceof ClsExceptions))
				throwExcp (e.getMessage(),new String[] {"modulo.facturacion"},e,null); 
			else
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 	
		}
		return (result);
	}
	
	/** 
	 *  Funcion que implementa la compensación de facturas comenzando por aquella que pueda tener asociada el abono
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String compensarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="";
		UserTransaction tx = null;
		double compensado= 0;
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
 			
			// Obtengo los datos del formulario
			AbonosPagosForm miForm = (AbonosPagosForm)formulario;
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();
			
			// Obtengo la cantidad compensada
			compensado=cantidadCompensada(miForm.getIdInstitucion(),miForm.getIdAbono(),this.getUserBean(request));
						
			if (compensado>=0){
				if (compensado==0){
					result=exitoRefresco("facturacion.abonosPagos.datosPagoAbono.errorCompensacion",request);
				}
				else{
					result=exitoRefresco("facturacion.abonosPagos.datosPagoAbono.abonoRealizado",request);
				}	
			}	
			tx.commit();
		} 
		catch (Exception e) { 
			throwExcp("facturacion.abonosPagos.datosPagoAbono.errorAbono",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (result);
	}
	protected String compensacionFacturaManual(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="";

		try {
			Vector entrada=new Vector();		
			Hashtable registro=new Hashtable();

			result="dialogoCompensacionFacturaManual";
			AbonosPagosForm form = (AbonosPagosForm) formulario;
			FacAbonoAdm adm = new FacAbonoAdm(this.getUserBean(request));
			CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
			
			
			entrada=adm.getAbono(form.getIdInstitucion(),form.getIdAbono());
			
			registro=((Row)entrada.firstElement()).getRow();
			// Paso valores asociados al registro			
			request.setAttribute(FacAbonoBean.C_IDABONO, form.getIdAbono());
			request.setAttribute(FacAbonoBean.C_IDINSTITUCION, form.getIdInstitucion());
			request.setAttribute("PAGOPENDIENTE", form.getPagoPendiente());
			request.setAttribute(FacAbonoBean.C_IDPERSONA, registro.get(FacAbonoBean.C_IDPERSONA));
			request.setAttribute(FacAbonoBean.C_FECHA, registro.get(FacAbonoBean.C_FECHA));
			Hashtable htPersona = new Hashtable();
			htPersona.put(CenPersonaBean.C_IDPERSONA,(String) registro.get(FacAbonoBean.C_IDPERSONA));
			Vector vPersonas = admPersona.selectByPK(htPersona);
			//Como entramos por PK solo hay un registro
			CenPersonaBean personaBean = (CenPersonaBean) vPersonas.get(0);
			
			StringBuffer nombre =  new StringBuffer();
			if(personaBean.getNombre()!=null){
				nombre.append(" ");
				nombre.append(personaBean.getNombre());
				
			}
			if(personaBean.getApellido1()!=null){
				nombre.append(" ");
				nombre.append(personaBean.getApellido1());
				
			}
			if(personaBean.getApellido2()!=null){
				nombre.append(" ");
				nombre.append(personaBean.getApellido2());
				
			}
				
			
			request.setAttribute("CLIENTE", nombre.toString());
			request.setAttribute(FacAbonoBean.C_NUMEROABONO, registro.get(FacAbonoBean.C_NUMEROABONO));
			//Obtenemos los datos de la factura rectificativa
			/*
			Hashtable htFactura = new Hashtable();
			htFactura.put(FacFacturaBean.C_IDFACTURA, idFactura);
			htFactura.put(FacFacturaBean.C_IDINSTITUCION, form.getIdInstitucion());
			Vector vFacturas = admFactura.selectByPK(htFactura);
			//Como entramos por PK sola hoy un registro
			FacFacturaBean facturaBean = (FacFacturaBean) vFacturas.get(0);
			request.setAttribute(FacFacturaBean.C_NUMEROFACTURA, facturaBean.getNumeroFactura());
			request.setAttribute(FacFacturaBean.C_FECHAEMISION, facturaBean.getFechaEmision());
			*/


			
			
			
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}			
		return (result);
		
		
	}
	protected String compensarFacturaManual(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="";
		UserTransaction tx = null;
		double compensado= 0;
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
 			
			// Obtengo los datos del formulario
			AbonosPagosForm miForm = (AbonosPagosForm)formulario;
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();
			
			// Obtengo la cantidad compensada
			double importeFactura =  UtilidadesNumero.getDouble(miForm.getImporteFacturaCompensadora());
			
			compensado=compensarFactura(miForm.getIdInstitucion(),miForm.getIdAbono(),miForm.getIdFacturaCompensadora(),importeFactura,this.getUserBean(request));
						
			if (compensado>=0){
				if (compensado==0){
					result=exitoModal("facturacion.abonosPagos.datosPagoAbono.errorCompensacion",request);
				}
				else{
					result=exitoModal("facturacion.abonosPagos.datosPagoAbono.abonoRealizado",request);
				}	
			}	
			tx.commit();
		} 
		catch (Exception e) { 
			throwExcp("facturacion.abonosPagos.datosPagoAbono.errorAbono",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (result);

	}
	
	
	
	
	

	
	
	/** 
	 * Obtiene la cantidad compensada abono-facturas pendientes 
	 * @param  institucion - Identificador de la institucion
	 * @param  abono - Identificador del abono
	 * @param  usuario - Identificador del usuario que realiza la operacion
	 * @return Double - Cantidad compensada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public static double cantidadCompensada(String institucion, String abono, UsrBean userBean) throws ClsExceptions{
		
		double resultado=0;
		Hashtable datosAbono=new Hashtable();
		Vector facturasPendientes=new Vector();
		double cantidadPendiente=0;
		double cantidadOriginal=0;
		String idPersona="";
		boolean correcto=true;
		
		try {		 				
			// Creo manejadores para acceder a las BBDD
			FacAbonoAdm adminAbono=new FacAbonoAdm(userBean);
			FacFacturaAdm adminFactura=new FacFacturaAdm(userBean);
			FacPagoAbonoEfectivoAdm adminPAE=new FacPagoAbonoEfectivoAdm(userBean);
			FacPagosPorCajaAdm adminPPC=new FacPagosPorCajaAdm(userBean);
			
			// Obtengo datos generales del abono
			datosAbono=((Row)adminAbono.getTotalesPagos(institucion,abono).firstElement()).getRow();
			idPersona=(String)datosAbono.get(FacAbonoBean.C_IDPERSONA);
			cantidadPendiente=new Double((String)datosAbono.get("PENDIENTE")).doubleValue();
			cantidadOriginal=cantidadPendiente;
			
			// Obtengo factura asociada del abono si procede
			Hashtable refAbono=((Row)adminAbono.getAbono(institucion,abono).firstElement()).getRow();
			String facturaAsociada=(String)refAbono.get(FacAbonoBean.C_IDFACTURA);
			facturasPendientes=adminFactura.getFacturasPendientes(institucion,idPersona,facturaAsociada,cantidadPendiente);
			
			
			// Recorro la lista de facturas pendientes
			Enumeration listaFacturas=facturasPendientes.elements();
			double importeFactura=0;
			while (correcto && (cantidadPendiente>0) && (listaFacturas.hasMoreElements())){
				Hashtable temporal=((Row)listaFacturas.nextElement()).getRow();
				Hashtable datosPagoFactura=new Hashtable();
				// Mientras disponga de cantidad pendiente compenso/pago facturas 
				importeFactura=new Double((String)temporal.get("PENDIENTE")).doubleValue();
				
				// Restriccion impuesta por inconsistencia de la BBDD de pruebas
				if (importeFactura>=0){
					
					// Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago abono efectivo)
					Hashtable datosPagoAbono=new Hashtable();
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDINSTITUCION, institucion);
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDABONO, abono);
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDPAGOABONO,adminPAE.getNuevoID(institucion,abono));
					if (importeFactura>cantidadPendiente){
						datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IMPORTE, new Double(cantidadPendiente).toString());
					}
					else{
						datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IMPORTE, new Double(importeFactura).toString());
					}
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_FECHA, "sysdate");
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					double importeCompensado = new Double((String)datosPagoAbono.get(FacPagoAbonoEfectivoBean.C_IMPORTE)).doubleValue();
					correcto=adminPAE.insert(datosPagoAbono);
					

					
					// RGG 29/05/2009 Cambio de funciones de abono
					if (correcto){
					    
						// Obtengo el abono insertado
						Hashtable htA = new Hashtable();
						htA.put(FacAbonoBean.C_IDINSTITUCION,institucion);
						htA.put(FacAbonoBean.C_IDABONO,abono);
						Vector vAbono = adminAbono.selectByPK(htA);
						FacAbonoBean bAbono = null;
						if (vAbono!=null && vAbono.size()>0) {
						    bAbono = (FacAbonoBean) vAbono.get(0);
						}
						
						Double impPendientePorAbonar= UtilidadesNumero.redondea(bAbono.getImpPendientePorAbonar().doubleValue()-importeCompensado,2);
					    bAbono.setImpPendientePorAbonar(new Double(impPendientePorAbonar));
					    Double impTotalAbonado= UtilidadesNumero.redondea(bAbono.getImpTotalAbonado().doubleValue()+importeCompensado,2);
					    bAbono.setImpTotalAbonado(new Double(impTotalAbonado));
					    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
					        // pagado
					        bAbono.setEstado(new Integer(1));
					    } else {
					        if (bAbono.getIdCuenta()!=null) {
					            // pendiente pago banco
						        bAbono.setEstado(new Integer(5));
					        } else {
					            // pendiente pago caja
						        bAbono.setEstado(new Integer(6));
					        }
					    }
					    if (!adminAbono.update(bAbono)){
						    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
						}
					} else {
					    throw new ClsExceptions("Error al actualizar el pago compensado del abono: "+adminPAE.getError());
					}
					
					// Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago por caja de la factura)
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDINSTITUCION, institucion);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDFACTURA, (String)temporal.get(FacFacturaBean.C_IDFACTURA));
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOPORCAJA,adminPPC.getNuevoID(new Integer(institucion),(String)temporal.get(FacFacturaBean.C_IDFACTURA)));
					datosPagoFactura.put(FacPagosPorCajaBean.C_FECHA, "sysdate");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TARJETA, "N");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TIPOAPUNTE, FacPagosPorCajaBean.tipoApunteCompensado); // compensación
					//datosPagoFactura.put(FacPagosPorCajaBean.C_OBSERVACIONES, "Compensado con abono nº "+abono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_OBSERVACIONES, UtilidadesString.getMensajeIdioma(userBean,"messages.abonos.literal.compensa")+ " " + (String)refAbono.get(FacAbonoBean.C_NUMEROABONO));
					datosPagoFactura.put(FacPagosPorCajaBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					if (importeFactura>cantidadPendiente){
						datosPagoFactura.put(FacPagosPorCajaBean.C_IMPORTE, new Double(cantidadPendiente).toString());
						cantidadPendiente=0;
					}
					else{
						datosPagoFactura.put(FacPagosPorCajaBean.C_IMPORTE, new Double(importeFactura).toString());
						cantidadPendiente-=importeFactura;
					}
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDABONO, abono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOABONO,datosPagoAbono.get(FacPagoAbonoEfectivoBean.C_IDPAGOABONO));
					correcto=adminPPC.insert(datosPagoFactura);
					// Actualizo estado del abono
					if (correcto){
					    
					    FacFacturaBean facturaBean = null;
						FacFacturaAdm facturaAdm = new FacFacturaAdm(userBean);
					    Hashtable ht = new Hashtable();
					    ht.put(FacFacturaBean.C_IDINSTITUCION,institucion);
					    ht.put(FacFacturaBean.C_IDFACTURA,(String)temporal.get(FacFacturaBean.C_IDFACTURA));
					    Vector v = facturaAdm.selectByPK(ht);
					    if (v!=null && v.size()>0) {
					        facturaBean = (FacFacturaBean) v.get(0);
					        
					        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
					        Double impTotalCompensadoFac= UtilidadesNumero.redondea(facturaBean.getImpTotalCompensado().doubleValue()+(new Double((String)datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(),2);
					        Double impTotalPagadoFac= UtilidadesNumero.redondea(facturaBean.getImpTotalPagado().doubleValue()+(new Double((String)datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(),2);
					        Double impTotalPorPagarFac= UtilidadesNumero.redondea(facturaBean.getImpTotalPorPagar().doubleValue()-(new Double((String)datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(),2);
					        
					        facturaBean.setImpTotalCompensado(new Double(impTotalCompensadoFac));
					        facturaBean.setImpTotalPagado(new Double(impTotalPagadoFac));
					        facturaBean.setImpTotalPorPagar(new Double(impTotalPorPagarFac));
					        
					        if (facturaAdm.update(facturaBean)) {
						        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
								facturaAdm.consultarActNuevoEstadoFactura(facturaBean, new Integer(userBean.getUserName()),true);
					        } else {
					            throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
					        }
							
										    
					    } else {
					        throw new ClsExceptions("No se ha encontrado la factura buscada: "+institucion);
					    }
					    
//					    // actualizamos el estado del abono
//						ConsPLFacturacion plFacturacion=new ConsPLFacturacion();
//						plFacturacion.actualizarEstadoAbono(new Integer(institucion),new Long(abono), new Integer(userBean.getUserName()));
					}
				}	
			}
			// calculo cuanto dinero se ha compensado
			resultado=cantidadOriginal-cantidadPendiente;
			UtilidadesNumero.redondea(resultado, 2);
			
		} 
		catch (Exception e) { 
			throw new ClsExceptions (e, "Error al compensar abono-facturas"); 
		}
		return (resultado);
	}
	public static double compensarFactura(String institucion, String abono, String idFactura, double importeFactura , UsrBean userBean) throws ClsExceptions{
		
		double resultado=0;
		Hashtable datosAbono=new Hashtable();
		double cantidadPendiente=0;
		double cantidadOriginal=0;
		boolean correcto=true;
		
		try {		 				
			// Creo manejadores para acceder a las BBDD
			FacAbonoAdm adminAbono=new FacAbonoAdm(userBean);
			FacPagoAbonoEfectivoAdm adminPAE=new FacPagoAbonoEfectivoAdm(userBean);
			FacPagosPorCajaAdm adminPPC=new FacPagosPorCajaAdm(userBean);
			
			// Obtengo datos generales del abono
			datosAbono=((Row)adminAbono.getTotalesPagos(institucion,abono).firstElement()).getRow();
			cantidadPendiente=new Double((String)datosAbono.get("PENDIENTE")).doubleValue();
			cantidadOriginal=cantidadPendiente;
			
			// Obtengo factura asociada del abono si procede
			Hashtable refAbono=((Row)adminAbono.getAbono(institucion,abono).firstElement()).getRow();

				Hashtable datosPagoFactura=new Hashtable();
				// Mientras disponga de cantidad pendiente compenso/pago facturas 
				// Restriccion impuesta por inconsistencia de la BBDD de pruebas
				if (importeFactura>=0){
					
					// Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago abono efectivo)
					Hashtable datosPagoAbono=new Hashtable();
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDINSTITUCION, institucion);
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDABONO, abono);
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDPAGOABONO,adminPAE.getNuevoID(institucion,abono));
					if (importeFactura>cantidadPendiente){
						datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IMPORTE, new Double(cantidadPendiente).toString());
					}
					else{
						datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IMPORTE, new Double(importeFactura).toString());
					}
					
					double importeCompensado = new Double((String)datosPagoAbono.get(FacPagoAbonoEfectivoBean.C_IMPORTE)).doubleValue();
					
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_FECHA, "sysdate");
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					correcto=adminPAE.insert(datosPagoAbono);

					
					// RGG 29/05/2009 Cambio de funciones de abono
					if (correcto){
					    
						// Obtengo el abono insertado
						Hashtable htA = new Hashtable();
						htA.put(FacAbonoBean.C_IDINSTITUCION,institucion);
						htA.put(FacAbonoBean.C_IDABONO,abono);
						Vector vAbono = adminAbono.selectByPK(htA);
						FacAbonoBean bAbono = null;
						if (vAbono!=null && vAbono.size()>0) {
						    bAbono = (FacAbonoBean) vAbono.get(0);
						}
						
					    Double impPendientePorAbonar= UtilidadesNumero.redondea(bAbono.getImpPendientePorAbonar().doubleValue()-importeCompensado,2);
					    Double impTotalAbonado= UtilidadesNumero.redondea(bAbono.getImpTotalAbonado().doubleValue()+importeCompensado,2);

						bAbono.setImpPendientePorAbonar(new Double(impPendientePorAbonar));
					    bAbono.setImpTotalAbonado(new Double(impTotalAbonado));
					    bAbono.setImpTotalAbonadoEfectivo(new Double(importeCompensado));
					    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
					        // pagado
					        bAbono.setEstado(new Integer(1));
					    } else {
					        if (bAbono.getIdCuenta()!=null) {
					            // pendiente pago banco
						        bAbono.setEstado(new Integer(5));
					        } else {
					            // pendiente pago caja
						        bAbono.setEstado(new Integer(6));
					        }
					    }
					    if (!adminAbono.update(bAbono)){
						    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
						}
					} else {
					    throw new ClsExceptions("Error al actualizar el pago compensado del abono: "+adminPAE.getError());
					}
					
					// Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago por caja de la factura)
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDINSTITUCION, institucion);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDFACTURA, idFactura);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOPORCAJA,adminPPC.getNuevoID(new Integer(institucion),idFactura));
					datosPagoFactura.put(FacPagosPorCajaBean.C_FECHA, "sysdate");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TARJETA, "N");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TIPOAPUNTE, FacPagosPorCajaBean.tipoApunteCompensado); // compensación
					//datosPagoFactura.put(FacPagosPorCajaBean.C_OBSERVACIONES, "Compensado con abono nº "+abono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_OBSERVACIONES, UtilidadesString.getMensajeIdioma(userBean,"messages.abonos.literal.compensa")+ " " + (String)refAbono.get(FacAbonoBean.C_NUMEROABONO));
					datosPagoFactura.put(FacPagosPorCajaBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					if (importeFactura>cantidadPendiente){
						datosPagoFactura.put(FacPagosPorCajaBean.C_IMPORTE, new Double(cantidadPendiente).toString());
						cantidadPendiente=0;
					}
					else{
						datosPagoFactura.put(FacPagosPorCajaBean.C_IMPORTE, new Double(importeFactura).toString());
						cantidadPendiente-=importeFactura;
					}
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDABONO, abono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOABONO,datosPagoAbono.get(FacPagoAbonoEfectivoBean.C_IDPAGOABONO));
					correcto=adminPPC.insert(datosPagoFactura);
					// Actualizo estado del abono
					if (correcto){
					    
					    FacFacturaBean facturaBean = null;
						FacFacturaAdm facturaAdm = new FacFacturaAdm(userBean);
					    Hashtable ht = new Hashtable();
					    ht.put(FacFacturaBean.C_IDINSTITUCION,institucion);
					    ht.put(FacFacturaBean.C_IDFACTURA,idFactura);
					    Vector v = facturaAdm.selectByPK(ht);
					    if (v!=null && v.size()>0) {
					        facturaBean = (FacFacturaBean) v.get(0);
					        
					        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
					        Double impTotalCompensadoFac= UtilidadesNumero.redondea(facturaBean.getImpTotalCompensado().doubleValue()+(new Double((String)datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(),2);
					        Double impTotalPagadoFac= UtilidadesNumero.redondea(facturaBean.getImpTotalPagado().doubleValue()+(new Double((String)datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(),2);
					        Double impTotalPorPagarFac= UtilidadesNumero.redondea(facturaBean.getImpTotalPorPagar().doubleValue()-(new Double((String)datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(),2);

					        facturaBean.setImpTotalCompensado(new Double(impTotalCompensadoFac));
					        facturaBean.setImpTotalPagado(new Double(impTotalPagadoFac));
					        facturaBean.setImpTotalPorPagar(new Double(impTotalPorPagarFac));
					        
					        if (facturaAdm.update(facturaBean)) {
						        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
								facturaAdm.consultarActNuevoEstadoFactura(facturaBean, new Integer(userBean.getUserName()),true);
					        } else {
					            throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
					        }
							
										    
					    } else {
					        throw new ClsExceptions("No se ha encontrado la factura buscada: "+institucion+ " "+idFactura);
					    }					    
					    
//						ConsPLFacturacion plFacturacion=new ConsPLFacturacion();
//						plFacturacion.actualizarEstadoAbono(new Integer(institucion),new Long(abono), new Integer(userBean.getUserName()));
					}
				}	
			
			// calculo cuanto dinero se ha compensado
			resultado=cantidadOriginal-cantidadPendiente;
			
		} 
		catch (Exception e) { 
			throw new ClsExceptions (e, "Error al compensar abono-facturas"); 
		}
		return (resultado);
	}
	
	protected String validarFacturaCompensacionManual(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="mostrarDatosFactura";
		Vector facturas = new Vector();
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			//UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
						
			AbonosPagosForm form = (AbonosPagosForm) formulario;
			String idPersonaAbono =  form.getIdPersona();
			if (!form.getNumFacturaCompensadora().equalsIgnoreCase("")){
				FacFacturaAdm facturaAdm =new FacFacturaAdm(this.getUserBean(request)); 
				facturas=facturaAdm.getFacturaPorNumero(form.getIdInstitucion(),form.getNumFacturaCompensadora().trim().toUpperCase(),false);
				if (!facturas.isEmpty()){
					
					/*facturas=facturaAdm.getFacturaPorNumero(form.getIdInstitucion(),form.getNumFacturaCompensadora(),true);
					if(facturas.isEmpty()){
						request.setAttribute("IDFACTURA","");
						request.setAttribute("FECHAFACTURA","");
						request.setAttribute("ESTADO","");
						request.setAttribute("IMPORTEFACTURA","");
						//request.setAttribute("NOMBREPERSONA","");
						
						request.setAttribute("MSGERROR","messages.abonos.facturaConAbonoRectficativo");
						return result;
					}*/
					Row entrada=(Row)facturas.firstElement();
					Hashtable registro = entrada.getRow();
					String idFactura = (String)registro.get(FacFacturaBean.C_IDFACTURA);
					
					String idPersonaFactura = (String)registro.get(FacFacturaBean.C_IDPERSONA);
					if(idPersonaFactura!=null && !idPersonaFactura.equals(idPersonaAbono)){
						request.setAttribute("MSGERROR","messages.abonos.compensacionManual.personasIguales");
						request.setAttribute("IDFACTURA","");
						request.setAttribute("FECHAFACTURA","");
						request.setAttribute("ESTADO","");
						request.setAttribute("IMPORTEFACTURA","");
						return result;
					}
					
					
					Double importeFactura = facturaAdm.getPkgSigaTotalesFactura(FacFacturaBean.C_IMPTOTALPORPAGAR,form.getIdInstitucion(),idFactura);
					if(importeFactura.compareTo(new Double(0))==0){
						request.setAttribute("MSGERROR","messages.abonos.compensacionManual.sinImporteAPagar");
						request.setAttribute("IDFACTURA","");
						request.setAttribute("FECHAFACTURA","");
						request.setAttribute("ESTADO","");
						request.setAttribute("IMPORTEFACTURA","");
						return result;
					}
					// Paso de parametros empleando request
					request.setAttribute("IDFACTURA",idFactura);
					request.setAttribute("FECHAFACTURA", (String)registro.get(FacFacturaBean.C_FECHAEMISION));
					//request.setAttribute("ESTADO", estado);
					request.setAttribute("ESTADO", (String)registro.get("DESCESTADO"));
					request.setAttribute("IMPORTEFACTURA", UtilidadesString.formatoImporte(importeFactura.doubleValue()));
					//request.setAttribute("IDPERSONA", (String)registro.get(FacFacturaBean.C_IDPERSONA));
					//request.setAttribute("NOMBREPERSONA", admP.obtenerNombreApellidos((String)registro.get(FacFacturaBean.C_IDPERSONA)));
				}
				else{
					request.setAttribute("MSGERROR","messages.abonos.compensacionManual.noExisteFactura");
					request.setAttribute("IDFACTURA","");
					request.setAttribute("FECHAFACTURA","");
					request.setAttribute("ESTADO","");
					request.setAttribute("IMPORTEFACTURA","");
					return result;
					//request.setAttribute("NOMBREPERSONA","");
				}
			}
			else{
				request.setAttribute("IDFACTURA","");
				request.setAttribute("FECHAFACTURA","");
				request.setAttribute("ESTADO","");
				request.setAttribute("IMPORTEFACTURA","");
				return result;
				//request.setAttribute("NOMBREPERSONA","");
			}

		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
	return result;
	}

	/** 
	 *  MJM: Abonos Masivos
	 *  Funcion que atiende la accion pagar varios abonos a la vez
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	
	protected String pagarVariosAbonos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
	try {
		
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
		String idInstitucion=user.getLocation();
		FacAbonoAdm abonoAdm=new FacAbonoAdm(user);
		
		AbonosPagosForm form = (AbonosPagosForm) formulario;
		String datosAbonos = form.getAbonos();
		Integer abonosNoTratados=0;
		Integer abonosTratados=0;
		Integer abonosSeleccionados=0;
		boolean pagoVariosAbonos=true;
		FacAbonoAdm abAdm=new FacAbonoAdm(user);
		CenCuentasBancariasAdm cuentasAdm=new CenCuentasBancariasAdm(user);
		
		if (datosAbonos != null && !datosAbonos.equals("")) {
			String arrayAbonos[] = datosAbonos.split(";");

			if (arrayAbonos.length<1) {
				throw new SIGAException("Error al obtener las facturas");
			};
			
			abonosSeleccionados=arrayAbonos.length;
			
			for (int i=0; i<arrayAbonos.length; i++) {
			
				String idAbono = arrayAbonos[i];
			
				if(arrayAbonos.length==1)
					pagoVariosAbonos=false;
				
				//Recupero el importe pendiente por abonar
				Hashtable htA = new Hashtable();
				htA.put(FacAbonoBean.C_IDINSTITUCION, idInstitucion);
				htA.put(FacAbonoBean.C_IDABONO, idAbono);
				Vector vAbono = abAdm.selectByPK(htA);
				FacAbonoBean bAbono = null;
				if (vAbono != null && vAbono.size() > 0) {
					bAbono = (FacAbonoBean) vAbono.get(0);
				}

				//Si el abono ya ha sido pagado 
				if(bAbono.getEstado()==1){
					if(arrayAbonos.length==1)
							throw new SIGAException("facturacion.abonos.error.abonoPagado");
				
				}else{
					//Si el importe del abono es cero
					if(bAbono.getImpTotalNeto().doubleValue() <=0){
						if(arrayAbonos.length==1)
								throw new SIGAException("facturacion.abonos.error.abonoSinImporte");
					
					}else{
					
						//Si no tiene importe pendiente
						if(bAbono.getImpPendientePorAbonar().doubleValue() <=0){
							if(arrayAbonos.length==1)
									throw new SIGAException("facturacion.abonos.error.importePendiente");
						}else{
							form.setIdInstitucion(idInstitucion);
							form.setIdAbono(idAbono);
							
							if((bAbono.getIdFactura()!=null)||(!bAbono.getIdFactura().isEmpty()))	
								form.setIdFactura(bAbono.getIdFactura());
							
							if(form.TipoPago().equals("caja"))
							{
								//Si se seleccionó un abono solamente el importe lo introduce el usuario,
								//si hay varios se hará el pago el abono por el importe pendiente que tenga el abono
								if(form.getImporte().isEmpty())
									form.setImporte(bAbono.getImpPendientePorAbonar().toString());
								
								//Inserto el pago por caja
								boolean abonoPagado=this.insertarPagoCaja(form,user,pagoVariosAbonos);
								
								if(abonoPagado)
									abonosTratados++;
							}else{
							
								//Se obtiene la cuenta, si la persona tiene más de una cuenta no se realiza el pago
								//Los abonos SJCS se pagarán a la cuenta SJCS 
								//Los abonos no SJCS se pagarán a la cuenta no SJCS
								if(bAbono.getIdPagosJG()==null)
									bAbono.setIdPagosJG(0);
								
								String  where=" Where ";
										where +=CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion;
										where +=" AND "+CenCuentasBancariasBean.C_IDPERSONA+"="+bAbono.getIdPersona();
										where +=" AND "+CenCuentasBancariasBean.C_ABONOSJCS+"="+bAbono.getIdPagosJG();
										where +=" AND "+CenCuentasBancariasBean.C_FECHABAJA+" IS NULL";
										where +=" AND "+CenCuentasBancariasBean.C_ABONOCARGO+" IN('A','T')";
								
								Vector vCuentas=cuentasAdm.select(where);
								
								if (vCuentas.size() >0) {
									
									if(vCuentas.size()>1)
									{
										if(arrayAbonos.length==1)
											throw new SIGAException("facturacion.abonos.error.masDeUnaCuenta");
										
									}else{
										
										CenCuentasBancariasBean bCuentas = (CenCuentasBancariasBean) vCuentas.get(0);
										
										form.setIdPersona(bAbono.getIdPersona().toString());
										form.setNumeroCuenta(bCuentas.getIdCuenta().toString());
										
										//Inserto el pago por banco
										boolean abonoPagado=this.insertarPagoBanco(form,user,pagoVariosAbonos);
										
										if(abonoPagado)
											abonosTratados++;
									}
									
								
								//No tiene cuentas asociadas
								}else{
									if(arrayAbonos.length==1)
										throw new SIGAException("facturacion.abonos.error.noExisteCuenta");
								}

							}
							
						}
					}
				}
			}
	}
		
	String tipoAlert="";
	String mensaje="";
	
	abonosNoTratados=abonosNoTratados-abonosTratados;
	
	
	 if (abonosTratados==abonosSeleccionados) {
    	// No hay errores
    	String[] datos = {""+abonosTratados,""+abonosSeleccionados};
    	tipoAlert="success";
    	mensaje = UtilidadesString.getMensaje("facturacion.abonos.mensaje.abonosPagados", datos, user.getLanguage());
    
    } else {
    	tipoAlert=abonosTratados==0?"error":"warning";
    	// Hay errores en la generacion de los abonos
    	String[] datos = {""+abonosTratados,""+abonosSeleccionados};
    	mensaje = UtilidadesString.getMensaje("facturacion.abonos.mensaje.abonosPagados", datos, user.getLanguage());
    }
	 
	request.setAttribute("estiloMensaje",tipoAlert);	
	request.setAttribute("mensaje",mensaje);	

	}catch (Exception e){
		if((e instanceof SIGAException)||(e instanceof ClsExceptions))
			throwExcp (e.getMessage(),new String[] {"modulo.facturacion"},e,null); 
		else
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 			
	}

	 return "exitoConString";
		
	
}
	
	/** 
	 *  MJM: Abonos Masivos
	 *  Método para insertar pagos de abonos por caja, se crea un nuevo método común al pago de un abono 
	 *  y pago masivo de abonos por caja.
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  usr -  usuario
	 * @param  abonoMasivo -  indicador que tiene valor true si tratamos pagos masivos desde
	 * 						  la búsqueda general de abonos y false si estamos pagando un abono 
	 * 						  desde la pestaña de pagos de abonos
	 *       
	 * @return  true si el abono se ha insertado correctamente y false sino se ha insertado correctamente  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected boolean insertarPagoCaja(AbonosPagosForm miForm,UsrBean usr, boolean pagoVariosAbonos) throws ClsExceptions, SIGAException{
		
		boolean pagoInsertado=false;
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		Hashtable datosPagoFactura = new Hashtable();
		Double impTotalFactura = null;
		Double impTotalPagadoFactura = null;
		
		try{
			FacPagoAbonoEfectivoAdm admin = new FacPagoAbonoEfectivoAdm(usr);
			FacPagosPorCajaAdm adminPPC = new FacPagosPorCajaAdm(usr);
			FacFacturaAdm adminFactura = new FacFacturaAdm(usr);
			FacAbonoAdm adminAbono = new FacAbonoAdm(usr);
		
		
			// Cargo la tabla hash con los valores del formulario para insertar
			// en la BBDD
			hash.put(FacPagoAbonoEfectivoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			hash.put(FacPagoAbonoEfectivoBean.C_IDABONO, miForm.getIdAbono());
			hash.put(FacPagoAbonoEfectivoBean.C_IDPAGOABONO,admin.getNuevoID(miForm.getIdInstitucion(),miForm.getIdAbono()));
			hash.put(FacPagoAbonoEfectivoBean.C_IMPORTE, miForm.getImporte());
			hash.put(FacPagoAbonoEfectivoBean.C_FECHA, "sysdate");
			hash.put(FacPagoAbonoEfectivoBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
	
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();
	
			if (admin.insert(hash)) {
				// RGG 29/05/2009 Cambio de funciones de abono
				// Obtengo el abono insertado
				FacAbonoAdm adminA = new FacAbonoAdm(usr);
				Hashtable htA = new Hashtable();
				htA.put(FacAbonoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				htA.put(FacAbonoBean.C_IDABONO, miForm.getIdAbono());
				Vector vAbono = adminA.selectByPK(htA);
				FacAbonoBean bAbono = null;
				if (vAbono != null && vAbono.size() > 0) {
					bAbono = (FacAbonoBean) vAbono.get(0);
				}
				
				Double impPendientePorAbonar = UtilidadesNumero.redondea(bAbono.getImpPendientePorAbonar().doubleValue()- new Double(miForm.getImporte()).doubleValue(), 2);
				Double impTotalAbonado = UtilidadesNumero.redondea(bAbono.getImpTotalAbonado().doubleValue()+ new Double(miForm.getImporte()).doubleValue(), 2);
				Double impTotalAbonadoEfectivo = UtilidadesNumero.redondea(bAbono.getImpTotalAbonadoEfectivo().doubleValue()+ new Double(miForm.getImporte()).doubleValue(), 2);
	
				bAbono.setImpPendientePorAbonar(new Double(impPendientePorAbonar));
				bAbono.setImpTotalAbonado(new Double(impTotalAbonado));
				bAbono.setImpTotalAbonadoEfectivo(new Double(impTotalAbonadoEfectivo));
				
				if (bAbono.getImpPendientePorAbonar().doubleValue() <= 0) {
					// pagado
					bAbono.setEstado(new Integer(1));
				} else {
					if (bAbono.getIdCuenta() != null) {
						// pendiente pago banco
						bAbono.setEstado(new Integer(5));
					} else {
						// pendiente pago caja
						bAbono.setEstado(new Integer(6));
					}
				}
				if (!adminA.update(bAbono)) {
					
					if (Status.STATUS_ACTIVE  == tx.getStatus()){
					    try {tx.rollback();}catch (Exception e) {}
					}
					
					if(!pagoVariosAbonos)
						throw new ClsExceptions("Error al actualizar estado e importes del abono: "+ adminA.getError());
					else
						return pagoInsertado=false;
				}
	
			} else {
				
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					try {tx.rollback();}catch (Exception e) {}
				}
				
				if(!pagoVariosAbonos)
					throw new ClsExceptions("Error al actualizar el pago efectivo del abono: "+ admin.getError());
				else
					return pagoInsertado=false;
			}
	
			Vector vFactura = (Vector) adminFactura.getImpFactura(miForm.getIdInstitucion().toString(), miForm.getIdFactura().toString());
	
			FacFacturaBean bAFactura = null;
	
			if (vFactura != null && vFactura.size() > 0) {
				impTotalFactura = Double.valueOf((String) ((Row) vFactura.firstElement()).getRow().get(FacFacturaBean.C_IMPTOTAL));
				impTotalPagadoFactura = Double.valueOf((String) ((Row) vFactura.firstElement()).getRow().get(FacFacturaBean.C_IMPTOTALPAGADO));
			}
	
			if (vFactura != null && vFactura.size() > 0) {
	
				Double impPteFac = UtilidadesNumero.redondea(impTotalFactura - impTotalPagadoFactura, 2);
	
				if (impPteFac > 0) {
					// Cargo la tabla hash con los valores del formulario para
					// insertar en la BBDD (pago por caja de la factura)
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDFACTURA,miForm.getIdFactura());
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOPORCAJA,adminPPC.getNuevoID(new Integer(miForm.getIdInstitucion()),miForm.getIdFactura()));
					datosPagoFactura.put(FacPagosPorCajaBean.C_FECHA, "sysdate");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TARJETA, "N");
					datosPagoFactura.put(FacPagosPorCajaBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IMPORTE,miForm.getImporte());
	
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDABONO,miForm.getIdAbono());
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOABONO,hash.get(FacPagoAbonoEfectivoBean.C_IDPAGOABONO));
	
					if (adminPPC.insert(datosPagoFactura)) {
						FacFacturaBean facturaBean = null;
						FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
						Hashtable ht = new Hashtable();
						ht.put(FacFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
						ht.put(FacFacturaBean.C_IDFACTURA,miForm.getIdFactura());
						Vector v = facturaAdm.selectByPK(ht);
						if (v != null && v.size() > 0) {
							facturaBean = (FacFacturaBean) v.get(0);
	
							// AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
							Double impTotalCompensadoFac = UtilidadesNumero.redondea(facturaBean.getImpTotalCompensado().doubleValue()+ (new Double((String) datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(), 2);
							Double impTotalPagadoFac = UtilidadesNumero.redondea(facturaBean.getImpTotalPagado().doubleValue()+ (new Double((String) datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(), 2);
							Double impTotalPorPagarFac = UtilidadesNumero.redondea(facturaBean.getImpTotalPorPagar().doubleValue()- (new Double((String) datosPagoFactura.get(FacPagosPorCajaBean.C_IMPORTE))).doubleValue(), 2);
	
							facturaBean.setImpTotalCompensado(new Double(impTotalCompensadoFac));
							facturaBean.setImpTotalPagado(new Double(impTotalPagadoFac));
							facturaBean.setImpTotalPorPagar(new Double(impTotalPorPagarFac));
	
							if (facturaAdm.update(facturaBean)) {
								// AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
								facturaAdm.consultarActNuevoEstadoFactura(facturaBean,new Integer(usr
												.getUserName()),true);
							} else {
								
								if (Status.STATUS_ACTIVE  == tx.getStatus()){
								    try {tx.rollback();}catch (Exception e) {}
								}
								
								if(!pagoVariosAbonos)
									throw new ClsExceptions("Error al actualizar los importes de la factura: "
												+ facturaAdm.getError());
								else
									return pagoInsertado=false;
							}
	
						} else {
							
							if (Status.STATUS_ACTIVE  == tx.getStatus()){
								try {tx.rollback();}catch (Exception e) {}
							}
							
							if(!pagoVariosAbonos)
								throw new ClsExceptions(
										"No se ha encontrado la factura buscada: "+ miForm.getIdInstitucion() + " "
												+ miForm.getIdFacturaCompensadora());
							else
								return pagoInsertado=false;
						}
					}
				} else {
	
					FacFacturaBean facturaBean = null;
					FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
					Hashtable ht = new Hashtable();
					ht.put(FacFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					ht.put(FacFacturaBean.C_IDFACTURA, miForm.getIdFactura());
					Vector v = facturaAdm.selectByPK(ht);
					if (v != null && v.size() > 0) {
						facturaBean = (FacFacturaBean) v.get(0);
	
						// AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
						Double impTotalCompensadoFac = UtilidadesNumero.redondea(facturaBean.getImpTotalCompensado().doubleValue()+ (new Double((String) miForm.getImporte())).doubleValue(), 2);	
						facturaBean.setImpTotalCompensado(new Double(impTotalCompensadoFac));
						facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()));
						facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()));
	
						boolean correcto = facturaAdm.update(facturaBean);
	
						if (!correcto) {
							// No se ha realizado correctamente la actualizacion
							// de la factura
							if (Status.STATUS_ACTIVE  == tx.getStatus()){
								try {tx.rollback();}catch (Exception e) {}
							}
							
							if(!pagoVariosAbonos)
								throw new ClsExceptions("Error al actualizar los importes de la factura: "
											+ facturaAdm.getError());
							else
								return pagoInsertado=false;
						}
	
					} else {
						
						if (Status.STATUS_ACTIVE  == tx.getStatus()){
							try {tx.rollback();}catch (Exception e) {}
						}
						
						if(!pagoVariosAbonos)
							throw new ClsExceptions(
									"No se ha encontrado la factura buscada: "+ miForm.getIdInstitucion() + " "
											+ miForm.getIdFacturaCompensadora());
						else
							return pagoInsertado=false;
					}
	
				}
			}
			tx.commit();
			pagoInsertado=true;
		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}	
	
	return pagoInsertado;
	}
	
	
	/** 
	 *  MJM: Abonos Masivos
	 *  Método para insertar pagos de abonos por banco, se crea un nuevo método común al pago de un abono 
	 *  y pago masivo de abonos por banco.
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  usr -  usuario
	 * @param  abonoMasivo -  indicador que tiene valor true si tratamos pagos masivos desde
	 * 						  la búsqueda general de abonos y false si estamos pagando un abono 
	 * 						  desde la pestaña de pagos de abonos
	 *       
	 * @return  true si el abono se ha insertado correctamente y false sino se ha insertado correctamente  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected boolean insertarPagoBanco(AbonosPagosForm miForm,UsrBean usr, boolean pagoVariosAbonos) throws ClsExceptions, SIGAException{
		
		boolean pagoInsertado=false;
		
		String result="error";
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			FacAbonoAdm admin=new FacAbonoAdm(usr);			
 		
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=((Row)(admin.getAbono(miForm.getIdInstitucion(),miForm.getIdAbono()).firstElement())).getRow();
			hash=(Hashtable)hashOriginal.clone();
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			hash.put(FacAbonoBean.C_IDCUENTA, miForm.getNumeroCuenta());
			
			// RGG 29/06/2009 Cambio de funciones de abonos
			if (miForm.getNumeroCuenta()==null || miForm.getNumeroCuenta().trim().equals("")) {
				hash.put(FacAbonoBean.C_ESTADO, "6"); // pendiente pagar por caja 
			} else {
			    hash.put(FacAbonoBean.C_ESTADO, "5"); // pendiente pagar por banco
			}

			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
			
			if (!admin.update(hash,hashOriginal)){
			    if (Status.STATUS_ACTIVE  == tx.getStatus()){
					try {tx.rollback();}catch (Exception e) {}
				}
			    
				if(!pagoVariosAbonos)
			    	throw new ClsExceptions("Error al actualizar el abono: "+admin.getError());
			    else
			    	pagoInsertado=false;
			}
			
			tx.commit();
			pagoInsertado=true;
		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
	
	return pagoInsertado;
	}
	
	protected void existeCuentaSJCSAjax (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {

			FacAbonoAdm adm = new FacAbonoAdm(this.getUserBean(request));
			String idInstitucion=request.getParameter("idInstitucion").toString();
			Vector entrada=adm.getAbono(idInstitucion,request.getParameter("idAbono").toString());
			Hashtable registro=((Row)entrada.firstElement()).getRow();
			String idPersona=registro.get(FacAbonoBean.C_IDPERSONA).toString();
			
			String  select = "SELECT "+CenCuentasBancariasBean.C_IDCUENTA+" AS ID ,";
			        select +=" F_SIGA_FORMATOIBAN("+CenCuentasBancariasBean.C_IBAN+") as DESCRIPCION ";
			        select +=" FROM "+CenCuentasBancariasBean.T_NOMBRETABLA;
			        select +=" WHERE ";
					select +=CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion;
					select +=" AND "+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona;
					select +=" AND "+CenCuentasBancariasBean.C_ABONOSJCS+"="+Integer.parseInt(request.getParameter("idPagoJG"));
					select +=" AND "+CenCuentasBancariasBean.C_FECHABAJA+" IS NULL";
					select +=" AND "+CenCuentasBancariasBean.C_ABONOCARGO+" IN('A','T')";
					select +=" ORDER BY DESCRIPCION";
					
			CenCuentasBancariasAdm cuentasAdm=new CenCuentasBancariasAdm(this.getUserBean(request));
			Vector vCuentas=cuentasAdm.selectGenerico(select);
			JSONObject json = new JSONObject();
			if(vCuentas.size()==0)
				json.put("msgCuentaSJCS","1");

			// json.
			response.setContentType("text/x-json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
		    response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
	}	
}
