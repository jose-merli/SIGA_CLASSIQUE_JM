/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */

/**
 * Clase action para el alta de abonos.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento de los abonos.  
 */

package com.siga.facturacion.action;


import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.AltaAbonosForm;
import java.util.*;

import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;


public class AltaAbonosAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {

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
				}else if (accion.equalsIgnoreCase("confirmarFactura")){
					mapDestino = confirmarFactura(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("anularFacturas")){
					mapDestino = anularFacturas(mapping, miForm, request, response);
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
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String result="abrir";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			FacAbonoAdm abonoAdm =new FacAbonoAdm(this.getUserBean(request)); 
			String idAbono=abonoAdm.getNuevoID(idInstitucion).toString();
			// Obtenemos el nuevo numero de abono
			GestorContadores gc = new GestorContadores(this.getUserBean(request));
			Hashtable contadorTablaHash=gc.getContador(new Integer(idInstitucion),ClsConstants.FAC_ABONOS);
			String numeroAbonoSugerido=gc.getNuevoContadorConPrefijoSufijo(contadorTablaHash);
		  	
		  
		  
			
			String fecha=UtilidadesBDAdm.getFechaBD("");
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("FECHA", fecha);
			request.setAttribute("IDABONO", idAbono);
			request.setAttribute("NUMEROABONO",numeroAbonoSugerido);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
	return result;
	}
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrirAvanzada";
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;
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

		PysFormaPagoBean bean=new PysFormaPagoBean ();
		String result="nuevo";
		try{						
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);									
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		
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
		String result="";
		boolean abonoInsertado=false;
		boolean abonoMasivo=false;
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Obtengo los datos del formulario
			AltaAbonosForm miForm = (AltaAbonosForm)formulario;
			//inserto el abono
			abonoInsertado=this.insertarNuevoAbono(miForm,usr,abonoMasivo);
			
			result=exitoModal("messages.updated.success",request);
		 
		}catch (Exception e){
			if((e instanceof SIGAException)||(e instanceof ClsExceptions))
				throwExcp (e.getMessage(),new String[] {"modulo.facturacion"},e,null); 
			else
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 			
		}	
	
		return result;

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
	 *  Funcion que atiende la accion confirmarFactura.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String confirmarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="nuevaRecarga";
		Vector facturas = new Vector();
		String numfactura ="";
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();

			CenPersonaAdm admP = new CenPersonaAdm(user);
			AltaAbonosForm form = (AltaAbonosForm) formulario;
			if (!form.getNumFactura().equalsIgnoreCase("")){
				FacFacturaAdm facturaAdm =new FacFacturaAdm(this.getUserBean(request)); 
				if(form.getNumFactura()!=null)
				 numfactura =form.getNumFactura().trim().toUpperCase();
				facturas=facturaAdm.getFacturaPorNumero(form.getIdInstitucion(),numfactura,false);
				if (!facturas.isEmpty()){
					facturas=facturaAdm.getFacturaPorNumero(form.getIdInstitucion(),numfactura,true);
					if(facturas.isEmpty()){
						request.setAttribute("IDFACTURA","");
						request.setAttribute("FECHAFACTURA","");
						request.setAttribute("ESTADO","");
						request.setAttribute("IDPERSONA","");
						request.setAttribute("NOMBREPERSONA","");
						request.setAttribute("MSGERROR","messages.abonos.facturaConAbonoRectficativo");
						return result;
					}
					Row entrada=(Row)facturas.firstElement();
					Hashtable registro = entrada.getRow();
					
					/* RGG se obtiene directamente en la funcion
					// Obtengo el estado de la factura
					ConsPLFacturacion cpl = new ConsPLFacturacion(new Integer(user.getUserName()),user.getLanguage());
					String estado=cpl.obtenerEstadoFacAbo(new Integer(idInstitucion).intValue(),new Long((String)registro.get(FacFacturaBean.C_IDFACTURA)).longValue(),ConsPLFacturacion.FACTURA);
					*/

					// Paso de parametros empleando request
					request.setAttribute("IDFACTURA",(String)registro.get(FacFacturaBean.C_IDFACTURA));
					request.setAttribute("FECHAFACTURA", (String)registro.get(FacFacturaBean.C_FECHAEMISION));
					//request.setAttribute("ESTADO", estado);
					request.setAttribute("ESTADO", (String)registro.get("DESCESTADO"));
					request.setAttribute("IDPERSONA", (String)registro.get(FacFacturaBean.C_IDPERSONA));
					request.setAttribute("NOMBREPERSONA", admP.obtenerNombreApellidos((String)registro.get(FacFacturaBean.C_IDPERSONA)));
				}
				else{
					request.setAttribute("MSGERROR","messages.abonos.noExisteFactura");
					request.setAttribute("IDFACTURA","");
					request.setAttribute("FECHAFACTURA","");
					request.setAttribute("ESTADO","");
					request.setAttribute("IDPERSONA","");
					request.setAttribute("NOMBREPERSONA","");
					return result;
				}
			}
			else{
				request.setAttribute("IDFACTURA","");
				request.setAttribute("FECHAFACTURA","");
				request.setAttribute("ESTADO","");
				request.setAttribute("IDPERSONA","");
				request.setAttribute("NOMBREPERSONA","");
			}

		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
	return result;
	}
	/** 
	 *  MJM: Abonos Masivos
	 *  Funcion que atiende la accion anularFacturas desde el 
	 *  mantenimiento de facturas.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String anularFacturas (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
	try {
		
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
		String idInstitucion=user.getLocation();
		FacFacturaAdm facturaAdm=new FacFacturaAdm(user);
		
		AltaAbonosForm form = (AltaAbonosForm) formulario;
		String datosFacturas = form.getFacturas();
		Integer factNoTratadas=0;
		Integer factTratadas=0;
		Integer factSeleccionadas=0;
		
		if (datosFacturas != null && !datosFacturas.equals("")) {
			String arrayFacturas[] = datosFacturas.split(";");

			if (arrayFacturas.length<1) {
				throw new SIGAException("Error al obtener las facturas");
			};
			
			factSeleccionadas=arrayFacturas.length;
			
			for (int i=0; i<arrayFacturas.length; i++) {
			
				String datosFactura = arrayFacturas[i];
				
				String idFactura= datosFactura.substring(0, datosFactura.indexOf("||"));
				String numfactura= datosFactura.substring(datosFactura.indexOf("||")+2, datosFactura.length()).trim().toUpperCase();

				//comprobaciones si solamente hay marcada una factura se muestra el error, sino se procesan todas y se muestra el contador 
				//de las que no se han actualizado
				//Se comprueba si existe la factura
				Vector facturas=facturaAdm.getFacturaPorNumero(idInstitucion,numfactura,false);
				if (!facturas.isEmpty()){
					//Se comprueba si la factura tiene un abono rectificativo
					facturas=facturaAdm.getFacturaPorNumero(idInstitucion,numfactura,true);
					if(facturas.isEmpty()){
						
						if(arrayFacturas.length==1)
							throw new SIGAException("messages.abonos.facturaConAbonoRectficativo"); 

					//Se crea un abono para cada una de las facturas
					}else{
						
						form.setIdInstitucion(idInstitucion);
						form.setNumFactura(numfactura);
						form.setIdFactura(idFactura);
						boolean abonoMasivo=true;
						
						if(arrayFacturas.length==1)
							abonoMasivo=false;
							
						boolean abonoInsertado=this.insertarNuevoAbono(form,user,abonoMasivo);
						
						if(abonoInsertado)
							factTratadas++;
					
					}
					
				}else{
					if(arrayFacturas.length==1)
						throw new SIGAException("messages.abonos.noExisteFactura");
					
				}
			}
	}
		
	String tipoAlert="";
	String mensaje="";
	
	factNoTratadas=factNoTratadas-factTratadas;
	
	
	 if (factNoTratadas==0) {
    	// No hay errores
    	String[] datos = {""+factTratadas,""+factSeleccionadas};
    	tipoAlert="success";
    	mensaje = UtilidadesString.getMensaje("facturacion.anulacion.mensaje.FacturasAnuladas", datos, user.getLanguage());
    
    } else {
    	tipoAlert=factTratadas==0?"error":"warning";
    	// Hay errores en la generacion de los abonos
    	String[] datos = {""+factTratadas,""+factSeleccionadas};
    	mensaje = UtilidadesString.getMensaje("facturacion.anulacion.mensaje.FacturasAnuladas", datos, user.getLanguage());
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
	 *  Método para insertar abonos, se crea un nuevo método común a los abonos de una factura 
	 *  y anulación masiva de facturas.
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  usr -  usuario
	 * @param  abonoMasivo -  indicador que tiene valor true si tratamos abonos masivos desde 
	 * 						  mantenimiento de facturas y false si estamos anulando una factura 
	 * 						  desde alta de abonos.
	 *       
	 * @return  true si el abono se ha insertado correctamente y false sino se ha insertado correctamente  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected boolean insertarNuevoAbono(AltaAbonosForm miForm,UsrBean usr, boolean abonoMasivo) throws ClsExceptions, SIGAException{
		
		boolean abonoInsertado=false;
		
		String result="";
		UserTransaction tx = null;
		boolean correcto=true;
		Hashtable hash = new Hashtable();

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			
			FacAbonoAdm admin=new FacAbonoAdm(usr);
			FacLineaAbonoAdm adminLA=new FacLineaAbonoAdm(usr);
			FacFacturaAdm adminF=new FacFacturaAdm(usr);
			FacLineaFacturaAdm adminLF=new FacLineaFacturaAdm(usr);
			GestorContadores gc = new GestorContadores(usr);
			FacPagoAbonoEfectivoAdm adminPAE=new FacPagoAbonoEfectivoAdm(usr);
			FacPagosPorCajaAdm adminPPC=new FacPagosPorCajaAdm(usr);
			FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
			Hashtable contadorTablaHash= null;
			
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada();
			//BNS: INC_10519_SIGA Comenzamos la transacción antes de las comprobaciones y bloqueamos las tablas implicadas
			// para que nadie pueda insertar entre medias
			tx.begin();
			admin.lockTable();
			adminPAE.lockTable();
			adminPPC.lockTable();
			facturaAdm.lockTable();
			adminLA.lockTable();
			adminF.lockTable();
		
			
			String numfactura="";
			// Compruebo que la factura asociada exista y este ligada a la misma persona //ESTO SOBRARIA YA QUE SE HACE EN LA VENTANA PREVIA
			if(miForm.getNumFactura()!=null)
				 numfactura =miForm.getNumFactura().trim().toUpperCase();
			//BNS INC_10519_SIGA COMPROBAMOS TAMBIEN QUE NO ESTÁ YA ABONADA
			Vector asociados = adminF.getFacturaPorNumero(miForm.getIdInstitucion(),numfactura,true);
			if (asociados.isEmpty()){

				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					try {tx.rollback();}catch (Exception e) {}
				}
				
				if(!abonoMasivo)
					throw new SIGAException("facturacion.altaAbonos.literal.facturaNoAsociada");
				else
					return abonoInsertado=false;
				
				
			}
			
			//AbonosPagosAction abonosPagos=new AbonosPagosAction();
			Hashtable datosPagoFactura=new Hashtable();			
			
			//String numFactura=miForm.getNumFactura();
			Long idAbono=admin.getNuevoID(usr.getLocation());
			
			// Cargo la tabla hash con los valores del formulario para insertar en PYS_PRODUCTOSINSTITUCION
			hash.put(FacAbonoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			hash.put(FacAbonoBean.C_IDABONO,idAbono);
			hash.put(FacAbonoBean.C_MOTIVOS,UtilidadesString.getMensajeIdioma(usr,"facturacion.altaAbonos.literal.devolucion") + " " + miForm.getMotivos());
			hash.put(FacAbonoBean.C_FECHA,"SYSDATE");
			hash.put(FacAbonoBean.C_CONTABILIZADA,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
			hash.put(FacAbonoBean.C_IDFACTURA,miForm.getIdFactura());
			
			//mhg - INC_09854_SIGA 
			String idPersonaDeudor = (String)((Row)asociados.firstElement()).getRow().get("IDPERSONADEUDOR");
			String idPersona = (String)((Row)asociados.firstElement()).getRow().get("IDPERSONA");
			
			boolean isPersonaDeudora = true;
			if(idPersonaDeudor==null || idPersonaDeudor.equals("")){
				isPersonaDeudora = false;
			} else {
				hash.put(FacAbonoBean.C_IDPERSONADEUDOR,idPersonaDeudor);
			}
			
			hash.put(FacAbonoBean.C_IDPERSONA,idPersona);			
			contadorTablaHash=gc.getContador(new Integer(usr.getLocation()),ClsConstants.FAC_ABONOS);
			String numeroAbono=gc.getNuevoContadorConPrefijoSufijo(contadorTablaHash);
			hash.put(FacAbonoBean.C_NUMEROABONO,numeroAbono);
			hash.put(FacAbonoBean.C_OBSERVACIONES,UtilidadesString.getMensajeIdioma(usr.getLanguage(),"messages.informes.abono.mensajeFactura")+" "+numfactura);
			
			
			//JTA comprobamos si la factura tiene numero de cuenta y en tal caso es que el pago por banco.
			//Entonces metemos el numero de cuenta al abono para que quede pendiente de abonar por banco.
			
			//BEGIN BNS OBTENGO LA FACTURA Y SUS DATOS
			double importeCompensado = 0; 
			double importeTotal = 0;
			Hashtable hashFactura=adminF.getFacturaDatosGeneralesTotalFactura(new Integer(miForm.getIdInstitucion()),miForm.getIdFactura(), usr.getLanguage());
			// IMPORTE COMPENSADO
			if (hashFactura != null) {
				String totalPendientePagar = UtilidadesHash.getString(hashFactura,"TOTAL_PENDIENTEPAGAR");
				if (totalPendientePagar !=null){
					totalPendientePagar = totalPendientePagar.trim();
					importeCompensado = new Double(totalPendientePagar).doubleValue();
				}
				String totalConIva = UtilidadesHash.getString(hashFactura,"TOTAL_FACTURA");
				if (totalConIva !=null){
					totalConIva = totalConIva.trim();
					importeTotal = new Double(totalConIva).doubleValue();
				}
				
			}
			
						
			//CR7 - INC_11904_SIGA
			hash.put(FacAbonoBean.C_IDCUENTA,"");
			hash.put(FacAbonoBean.C_IDCUENTADEUDOR,"");
			
			// Inserto el abono
			if (admin.insert(hash)){
				
			    // Obtengo el abono insertado
				Hashtable htA = new Hashtable();
				htA.put(FacAbonoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				htA.put(FacAbonoBean.C_IDABONO,idAbono);
				Vector vAbono = admin.selectByPK(htA);
				FacAbonoBean bAbono = null;
				if (vAbono!=null && vAbono.size()>0) {
				    bAbono = (FacAbonoBean) vAbono.get(0);
				}
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				//Meter la linea en la pestaña Pagos del abono "Compensacion con factura x"				
				hashFactura=adminF.getFacturaDatosGeneralesTotalFactura(new Integer(miForm.getIdInstitucion()),miForm.getIdFactura(), usr.getLanguage());
				String total="";
				if (hashFactura != null) {
					total = UtilidadesHash.getString(hashFactura,"TOTAL_PENDIENTEPAGAR");
				}			
				//String totalPagado = UtilidadesHash.getString(h,"TOTALPAGADO");
				if(total!=null && !total.trim().equals("")&& Double.parseDouble(total.trim())!=0){
					Long idPagoAbono=adminPAE.getNuevoID(miForm.getIdInstitucion(),String.valueOf(idAbono));
	
					// Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago abono efectivo)
					Hashtable datosPagoAbono=new Hashtable();
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDABONO, idAbono);
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IDPAGOABONO,idPagoAbono);
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_IMPORTE, total);
	
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_FECHA, "sysdate");
					datosPagoAbono.put(FacPagoAbonoEfectivoBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					correcto=adminPAE.insert(datosPagoAbono);
				
					//Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago por caja de la factura)
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDINSTITUCION, miForm.getIdInstitucion());
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDFACTURA, miForm.getIdFactura());
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOPORCAJA,adminPPC.getNuevoID(new Integer(miForm.getIdInstitucion()),miForm.getIdFactura()));
					datosPagoFactura.put(FacPagosPorCajaBean.C_FECHA, "sysdate");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TARJETA, "N");
					datosPagoFactura.put(FacPagosPorCajaBean.C_TIPOAPUNTE, FacPagosPorCajaBean.tipoApunteCompensado); // compensacion
					//datosPagoFactura.put(FacPagosPorCajaBean.C_OBSERVACIONES, "Compensado con abono nº "+abono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_OBSERVACIONES, UtilidadesString.getMensajeIdioma(usr,"messages.abonos.literal.compensa")+ " " + numeroAbono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_CONTABILIZADO,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IMPORTE, total);
					
					importeCompensado = new Double(total).doubleValue();
					
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDABONO, idAbono);
					datosPagoFactura.put(FacPagosPorCajaBean.C_IDPAGOABONO,idPagoAbono);
				
					correcto=adminPPC.insert(datosPagoFactura);
					


				    if (correcto) {
				        
						// ahora tenemos que actualizar los importes y estado de la factura.
					    FacFacturaBean facturaBean = null;						
					    Hashtable ht2 = new Hashtable();
					    ht2.put(FacFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					    ht2.put(FacFacturaBean.C_IDFACTURA,miForm.getIdFactura());
					    Vector v = facturaAdm.selectByPK(ht2);
					    if (v!=null && v.size()>0) {
					        facturaBean = (FacFacturaBean) v.get(0);
					        
					        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
					        facturaBean.setImpTotalCompensado(new Double(facturaBean.getImpTotalCompensado().doubleValue()+new Double(total).doubleValue()));
					        facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()+new Double(total).doubleValue()));
					        facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()-new Double(total).doubleValue()));
					        
					        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO, pero esta vez es fijo a ANULADO (8)
					        facturaBean.setEstado(new Integer("8"));
							
					        if (!facturaAdm.update(facturaBean)) {
					           
					        	if (Status.STATUS_ACTIVE  == tx.getStatus()){
					        	   try {tx.rollback();}catch (Exception e) {}
					            }
					        	
					        	if(!abonoMasivo)
					        		throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
					        	else
					        		return abonoInsertado=false;
					        }
							
					    } else {
					        if (Status.STATUS_ACTIVE  == tx.getStatus()){
					        	try {tx.rollback();}catch (Exception e) {}
					        }
					    	
					    	if(!abonoMasivo)
					        	throw new ClsExceptions("No se ha encontrado la factura buscada: "+miForm.getIdInstitucion()+ " "+miForm.getIdFactura());
					        else
					        	return abonoInsertado=false;
					    }

				    }				
				}
	
					

				////////////////////////////////////////////////////////////////////////////////////////////////////////				
				Hashtable ht = new Hashtable();
				ht.put(FacLineaFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				ht.put(FacLineaFacturaBean.C_IDFACTURA,miForm.getIdFactura());
				Vector vLineasFactura = adminLF.select(ht);
				FacLineaAbonoAdm lineaAbonoAdm = new FacLineaAbonoAdm(usr);
				int numlinea = 1;
			    // Inserto las lineas
				for (int i=0; vLineasFactura!=null && i<vLineasFactura.size();i++) {
					FacLineaFacturaBean blf=(FacLineaFacturaBean)vLineasFactura.get(i);
	
					Hashtable htL = new Hashtable();
				    
					// Cargo la tabla hash con los valores del formulario para insertar en PYS_PRODUCTOSINSTITUCION
				    htL.put(FacLineaAbonoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				    htL.put(FacLineaAbonoBean.C_IDABONO,(Long)hash.get(FacAbonoBean.C_IDABONO));
				    htL.put(FacLineaAbonoBean.C_CANTIDAD,blf.getCantidad());
				    htL.put(FacLineaAbonoBean.C_DESCRIPCIONLINEA,blf.getDescripcion());
				    htL.put(FacLineaAbonoBean.C_IDFACTURA,miForm.getIdFactura());
				    htL.put(FacLineaAbonoBean.C_IVA,blf.getIva());
					htL.put(FacLineaAbonoBean.C_LINEAFACTURA,blf.getNumeroLinea());

					htL.put(FacLineaAbonoBean.C_NUMEROLINEA,((Long) lineaAbonoAdm.getNuevoID(miForm.getIdInstitucion(), ((Long)hash.get(FacAbonoBean.C_IDABONO)).toString())).toString());
					htL.put(FacLineaAbonoBean.C_PRECIOUNITARIO,blf.getPrecioUnitario());
					numlinea++;

					if (!adminLA.insert(htL)){
						
						if (Status.STATUS_ACTIVE  == tx.getStatus()){
					       try {tx.rollback();}catch (Exception e) {}
					    }
						
						if(!abonoMasivo)
							throw new ClsExceptions("Error en insertar linea abono Devolucion: "+adminLA.getError());
						else
					        return abonoInsertado=false;
					}
	
				}
	
				// RGG 29/05/2009 Cambio de funciones de abono
				if (correcto){
					bAbono.setImpTotal(importeTotal);
				    bAbono.setImpPendientePorAbonar(new Double(importeTotal-importeCompensado));
				    bAbono.setImpTotalAbonado(new Double(importeCompensado));
				    bAbono.setImpTotalAbonadoEfectivo(new Double(0));
				    bAbono.setImpTotalAbonadoPorBanco(new Double(0));
				    bAbono.setImpTotalIva(new Double(UtilidadesHash.getString(hashFactura,"TOTAL_IVA")));
				    bAbono.setImpTotalNeto(new Double(UtilidadesHash.getString(hashFactura,"TOTAL_NETO")));
				    if ((importeTotal-importeCompensado)<=0) {
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
				    if (!admin.update(bAbono)){
					    
				    	if (Status.STATUS_ACTIVE  == tx.getStatus()){
				    		try {tx.rollback();}catch (Exception e) {}
					    }
				    	
				    	if(!abonoMasivo)
					    	throw new ClsExceptions("Error al actualizar estado e importes del abono: "+admin.getError());
					    else
					        return abonoInsertado=false;
					}

				    // Obtengo el abono insertado
					Hashtable htF = new Hashtable();
					htF.put(FacFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					htF.put(FacFacturaBean.C_IDFACTURA,miForm.getIdFactura());
					Vector vFactura = adminF.selectByPK(htF);
					FacFacturaBean bFactura = null;
					if (vFactura!=null && vFactura.size()>0) {
					    bFactura = (FacFacturaBean) vFactura.get(0);
					}
				    // RGG 29/05/2009 Cambio de funciones de factura
			        // Anulada
			        bFactura.setEstado(new Integer(8));
			        if (!adminF.update(bFactura)){
					    
			        	if (Status.STATUS_ACTIVE  == tx.getStatus()){
					       try {tx.rollback();}catch (Exception e) {}
					    }

			        	if(!abonoMasivo)
					    	throw new ClsExceptions("Error al actualizar estado e importes de la factura: "+adminF.getError());
					    else
					        return abonoInsertado=false;
					}
				}				
	
				//Actualizamos el contador de abonos en la tabla ADM_CONTADOR
				gc.setContador(contadorTablaHash,gc.getNuevoContador(contadorTablaHash));
				
				tx.commit();				
				abonoInsertado=true;		
			
			} else {
			    if(!abonoMasivo)
			    	throw new ClsExceptions("Error en insertar abono Devolucion: "+admin.getError());
			    else
					return abonoInsertado=false;
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}		

		return abonoInsertado;

	}

}
