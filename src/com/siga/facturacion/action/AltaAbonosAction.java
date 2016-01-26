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


import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturacionSuscripcionAdm;
import com.siga.beans.FacLineaAbonoAdm;
import com.siga.beans.FacLineaAbonoBean;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.FacLineaFacturaBean;
import com.siga.beans.FacPagoAbonoEfectivoAdm;
import com.siga.beans.FacPagoAbonoEfectivoBean;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.beans.FacPagosPorCajaBean;
import com.siga.facturacion.form.AltaAbonosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


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
		boolean abonoMasivo=false;
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Obtengo los datos del formulario
			AltaAbonosForm miForm = (AltaAbonosForm)formulario;
			//inserto el abono
			this.insertarNuevoAbono(miForm,usr,abonoMasivo,null);
			
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

			CenPersonaAdm admP = new CenPersonaAdm(user);
			AltaAbonosForm form = (AltaAbonosForm) formulario;
			if (!form.getNumFactura().equalsIgnoreCase("")){
				FacFacturaAdm facturaAdm =new FacFacturaAdm(this.getUserBean(request)); 
				if(form.getNumFactura()!=null)
				 numfactura =form.getNumFactura().trim().toUpperCase();
				facturas=facturaAdm.getFacturaPorNumero(form.getIdInstitucion(),numfactura, "",false);
				if (!facturas.isEmpty()){
					facturas=facturaAdm.getFacturaPorNumero(form.getIdInstitucion(),numfactura, "",true);
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
					//Si la factura está anulada pero no tiene abono asociado no se puede incluir en un abono
					//(caso de fac. origen de fac. integración comisión bancaria)
					if(Integer.parseInt(registro.get(FacFacturaBean.C_ESTADO).toString())==8){
						request.setAttribute("MSGERROR","messages.abonos.facturaAnuladaComisionBancaria");
						request.setAttribute("IDFACTURA","");
						request.setAttribute("FECHAFACTURA","");
						request.setAttribute("ESTADO","");
						request.setAttribute("IDPERSONA","");
						request.setAttribute("NOMBREPERSONA","");
						return result;
					
					}
					// Paso de parametros empleando request
					request.setAttribute("IDFACTURA",(String)registro.get(FacFacturaBean.C_IDFACTURA));
					request.setAttribute("FECHAFACTURA", (String)registro.get(FacFacturaBean.C_FECHAEMISION));
					//request.setAttribute("ESTADO", estado);
					request.setAttribute("ESTADO", (String)registro.get("DESCESTADO"));
					request.setAttribute("IDPERSONA", (String)registro.get(FacFacturaBean.C_IDPERSONA));
					request.setAttribute("NOMBREPERSONA", admP.obtenerNombreApellidos((String)registro.get(FacFacturaBean.C_IDPERSONA)));
				}
				else{
					request.setAttribute("MSGERROR","facturacion.altaAbonos.literal.facturaNoAsociada");
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
	protected String anularFacturas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");			
			String idInstitucion = user.getLocation();
			FacFacturaAdm admFacFactura = new FacFacturaAdm(user);			
			
			AltaAbonosForm form = (AltaAbonosForm) formulario;
			String datosFacturas = form.getFacturas();
			Integer factNoTratadas=0, factTratadas=0, factSeleccionadas=0;
			
			if (datosFacturas != null && !datosFacturas.equals("")) {
				String arrayFacturas[] = datosFacturas.split(";");
	
				if (arrayFacturas.length<1) {
					throw new SIGAException("Error al obtener las facturas");
				};
				
				factSeleccionadas = arrayFacturas.length;
				
				for (int i=0; i<arrayFacturas.length; i++) {
				
					String datosFactura = arrayFacturas[i];
					
					String idFactura = datosFactura.substring(0, datosFactura.indexOf("||"));
					String numfactura = datosFactura.substring(datosFactura.indexOf("||") + 2, datosFactura.lastIndexOf("||"));
	
					//comprobaciones si solamente hay marcada una factura se muestra el error, sino se procesan todas y se muestra el contador de las que no se han actualizado
					//Se comprueba si existe la factura
					Vector facturas = admFacFactura.getFacturaPorNumero(idInstitucion, numfactura, "", false);					
					if (!facturas.isEmpty()){
	
						//Se comprueba si la factura tiene un abono rectificativo
						facturas = admFacFactura.getFacturaPorNumero(idInstitucion, numfactura,  "", true);
						if (facturas.isEmpty()) {
							
							if(arrayFacturas.length==1)
								throw new SIGAException("messages.abonos.facturaConAbonoRectficativo"); 
						
						} else {							
							//Si la factura está anulada no se puede volver a anular (caso de facturas con comisión->la factura origen queda anulada sin abono)
							Integer estado = Integer.parseInt(datosFactura.substring(datosFactura.lastIndexOf("||") + 2, datosFactura.length()));
						
							//Sino está anulada se crea un abono para cada una de las facturas
							if (estado!=8) {
								form.setIdInstitucion(idInstitucion);
								form.setNumFactura(numfactura);
								form.setIdFactura(idFactura);
								boolean abonoMasivo=true;
								
								if(arrayFacturas.length==1)
									abonoMasivo=false;
									
								boolean abonoInsertado = this.insertarNuevoAbono(form, user, abonoMasivo,null);
								
								if (abonoInsertado) {
									factTratadas++;
								}
								
							} else {
								if(arrayFacturas.length==1) {
									throw new SIGAException("messages.abonos.facturaAnuladaComisionBancaria");
								}
							}
						}
						
						
					//La factura no tiene número de factura asociado (estado de factura: en revisión)	
					} else {						
						if (arrayFacturas.length==1) {
							throw new SIGAException("facturacion.altaAbonos.literal.facturaNoAsociada");
						}
					}
				}
			}
			
			String tipoAlert="", mensaje="";		
			factNoTratadas = factNoTratadas - factTratadas;
			String[] datos = {factTratadas.toString(), factSeleccionadas.toString()};
		
			if (factTratadas == factSeleccionadas) {
				// No hay errores
				tipoAlert = "success";
				mensaje = UtilidadesString.getMensaje("facturacion.anulacion.mensaje.FacturasAnuladas", datos, user.getLanguage());
	    
			} else {
				tipoAlert = (factTratadas == 0 ? "error" : "warning");
				// Hay errores en la generacion de los abonos
				mensaje = UtilidadesString.getMensaje("facturacion.anulacion.mensaje.FacturasAnuladas", datos, user.getLanguage());
			}
		 
			request.setAttribute("estiloMensaje",tipoAlert);	
			request.setAttribute("mensaje",mensaje);	
	
		} catch (Exception e) {
			if (e instanceof SIGAException || e instanceof ClsExceptions) {
				throwExcp (e.getMessage(),new String[] {"modulo.facturacion"},e,null); 
			} else {
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
			}
		}
	
		 return "exitoConString";
	}
	
	public boolean insertarNuevoAbono(String idInstitucion,String idFactura,String motivos,UsrBean usr, UserTransaction txPrev) throws ClsExceptions, SIGAException{
		AltaAbonosForm miForm = new AltaAbonosForm();
		miForm.setIdInstitucion(idInstitucion);
		miForm.setIdFactura(idFactura);
		miForm.setMotivos(motivos);
		return this.insertarNuevoAbono(miForm,usr,false,txPrev);
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
	protected boolean insertarNuevoAbono(AltaAbonosForm miForm,UsrBean usr, boolean abonoMasivo, UserTransaction txPrev) throws ClsExceptions, SIGAException{
		boolean abonoInsertado = false;
		UserTransaction tx = null;
		boolean correcto = true;
		Hashtable<String, Object> hash = new Hashtable<String, Object>();

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD			
			FacAbonoAdm admFacAbono = new FacAbonoAdm(usr);
			FacFacturaAdm admFacFactura = new FacFacturaAdm(usr);
			FacFacturacionSuscripcionAdm admFacFacturacionSuscripcion = new FacFacturacionSuscripcionAdm(usr);
			FacLineaAbonoAdm admFacLineaAbono = new FacLineaAbonoAdm(usr);
			FacLineaFacturaAdm admFacLineaFactura = new FacLineaFacturaAdm(usr);
			FacPagoAbonoEfectivoAdm admFacPagoAbonoEfectivo = new FacPagoAbonoEfectivoAdm(usr);
			FacPagosPorCajaAdm admFacPagosPorCaja = new FacPagosPorCajaAdm(usr);
			
			GestorContadores gc = new GestorContadores(usr);
			Hashtable contadorTablaHash = null;
			
			// Comienzo control de transacciones
			if(txPrev == null){
				tx = usr.getTransactionPesada();
				tx.begin();
			} else {
				tx = txPrev;
			}
			
			//BNS: INC_10519_SIGA Comenzamos la transacción antes de las comprobaciones y bloqueamos las tablas implicadas para que nadie pueda insertar entre medias
			admFacAbono.lockTable();
			admFacPagoAbonoEfectivo.lockTable();
			admFacPagosPorCaja.lockTable();
			admFacLineaAbono.lockTable();
			admFacFactura.lockTable();
			
			String numfactura="";
			Vector<Row> asociados = null;			

			// Compruebo que la factura asociada exista y este ligada a la misma persona (Primero la buscamos pr num factura y si no por idfactura
			if (miForm.getNumFactura() != null && !miForm.getNumFactura().equals("")) {
				numfactura = miForm.getNumFactura().trim().toUpperCase();
				asociados = admFacFactura.getFacturaPorNumero(miForm.getIdInstitucion(), numfactura, "", true);
			} else {
				asociados = admFacFactura.getFacturaPorNumero(miForm.getIdInstitucion(), null, miForm.getIdFactura(), true);
			}
				 
			if (asociados.isEmpty()){
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					try {tx.rollback();}catch (Exception e) {}
				}
				
				if (!abonoMasivo)
					throw new SIGAException("facturacion.altaAbonos.literal.facturaNoAsociada");
				else
					return abonoInsertado=false;
			}
			
			/** Si realizamos la busqueda por IDFACTURA, hay que rellenar el NUMFACTURA **/
			if(numfactura.equals(""))
				numfactura = ((String)((Row)asociados.firstElement()).getRow().get("NUMEROFACTURA")).trim().toUpperCase();
			
			// Cargo la tabla hash con los valores del formulario para insertar en PYS_PRODUCTOSINSTITUCION
			Long idAbono = admFacAbono.getNuevoID(usr.getLocation());
			hash.put(FacAbonoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			hash.put(FacAbonoBean.C_IDABONO,idAbono);
			hash.put(FacAbonoBean.C_MOTIVOS,UtilidadesString.getMensajeIdioma(usr,"facturacion.altaAbonos.literal.devolucion") + " " + miForm.getMotivos());
			hash.put(FacAbonoBean.C_FECHA,"SYSDATE");
			hash.put(FacAbonoBean.C_CONTABILIZADA,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
			hash.put(FacAbonoBean.C_IDFACTURA,miForm.getIdFactura());
			
			//mhg - INC_09854_SIGA 
			String idPersonaDeudor = (String)((Row)asociados.firstElement()).getRow().get("IDPERSONADEUDOR");
			String idPersona = (String)((Row)asociados.firstElement()).getRow().get("IDPERSONA");
			
			if (idPersonaDeudor!=null && !idPersonaDeudor.equals("")) {
				hash.put(FacAbonoBean.C_IDPERSONADEUDOR,idPersonaDeudor);
			}
			
			hash.put(FacAbonoBean.C_IDPERSONA,idPersona);			
			contadorTablaHash = gc.getContador(new Integer(usr.getLocation()), ClsConstants.FAC_ABONOS);
			String numeroAbono = gc.getNuevoContadorConPrefijoSufijo(contadorTablaHash);
			hash.put(FacAbonoBean.C_NUMEROABONO,numeroAbono);
			hash.put(FacAbonoBean.C_OBSERVACIONES,UtilidadesString.getMensajeIdioma(usr.getLanguage(),"messages.informes.abono.mensajeFactura")+" "+numfactura);
			
			
			//JTA comprobamos si la factura tiene numero de cuenta y en tal caso es que el pago por banco.
			//Entonces metemos el numero de cuenta al abono para que quede pendiente de abonar por banco.
			
			//BEGIN BNS OBTENGO LA FACTURA Y SUS DATOS
			double importeCompensado = 0; 
			double importeTotal = 0;
			Hashtable hashFactura = admFacFactura.getFacturaDatosGeneralesTotalFactura(new Integer(miForm.getIdInstitucion()), miForm.getIdFactura(), usr.getLanguage());
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
			if (admFacAbono.insert(hash)) {
				
			    // Obtengo el abono insertado
				Hashtable<String, Object> hFacAbono = new Hashtable<String, Object>();
				hFacAbono.put(FacAbonoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				hFacAbono.put(FacAbonoBean.C_IDABONO, idAbono);
				
				Vector<FacAbonoBean> vFacAbono = admFacAbono.selectByPK(hFacAbono);				
				FacAbonoBean beanFacAbono = null;
				if (vFacAbono!=null && vFacAbono.size()>0) {
					beanFacAbono = (FacAbonoBean) vFacAbono.get(0);
				}
				
				//Meter la linea en la pestaña Pagos del abono "Compensacion con factura x"				
				hashFactura = admFacFactura.getFacturaDatosGeneralesTotalFactura(new Integer(miForm.getIdInstitucion()), miForm.getIdFactura(), usr.getLanguage());
				String total = "";
				if (hashFactura != null) {
					total = UtilidadesHash.getString(hashFactura,"TOTAL_PENDIENTEPAGAR");
				}			
								
				if(total!=null && !total.trim().equals("") && Double.parseDouble(total.trim())!=0){
					Long idPagoAbono = admFacPagoAbonoEfectivo.getNuevoID(miForm.getIdInstitucion(), String.valueOf(idAbono));
	
					// Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago abono efectivo)
					Hashtable<String, Object> hFacPagoAbonoEfectivo = new Hashtable<String, Object>();
					hFacPagoAbonoEfectivo.put(FacPagoAbonoEfectivoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
					hFacPagoAbonoEfectivo.put(FacPagoAbonoEfectivoBean.C_IDABONO, idAbono);
					hFacPagoAbonoEfectivo.put(FacPagoAbonoEfectivoBean.C_IDPAGOABONO, idPagoAbono);
					hFacPagoAbonoEfectivo.put(FacPagoAbonoEfectivoBean.C_IMPORTE, total);
					hFacPagoAbonoEfectivo.put(FacPagoAbonoEfectivoBean.C_FECHA, "sysdate");
					hFacPagoAbonoEfectivo.put(FacPagoAbonoEfectivoBean.C_CONTABILIZADO, ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					correcto = admFacPagoAbonoEfectivo.insert(hFacPagoAbonoEfectivo);
				
					//Cargo la tabla hash con los valores del formulario para insertar en la BBDD (pago por caja de la factura)
					importeCompensado = new Double(total).doubleValue();
					Hashtable<String, Object> hFacPagosPorCaja = new Hashtable<String, Object>();
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_IDINSTITUCION, miForm.getIdInstitucion());
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_IDFACTURA, miForm.getIdFactura());
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_IDPAGOPORCAJA,admFacPagosPorCaja.getNuevoID(new Integer(miForm.getIdInstitucion()),miForm.getIdFactura()));
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_FECHA, "sysdate");
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_TARJETA, "N");
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_TIPOAPUNTE, FacPagosPorCajaBean.tipoApunteCompensado); // compensacion
					//hFacPagosPorCaja.put(FacPagosPorCajaBean.C_OBSERVACIONES, "Compensado con abono nº "+abono);
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_OBSERVACIONES, UtilidadesString.getMensajeIdioma(usr, "messages.abonos.literal.compensa") + " " + numeroAbono);
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_CONTABILIZADO, ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_IMPORTE, total);
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_IDABONO, idAbono);
					hFacPagosPorCaja.put(FacPagosPorCajaBean.C_IDPAGOABONO, idPagoAbono);
					correcto = admFacPagosPorCaja.insert(hFacPagosPorCaja);

				    if (correcto) {
				        
						// ahora tenemos que actualizar los importes y estado de la factura.					    
					    Hashtable<String,String> hFacFactura = new Hashtable<String,String>();
					    hFacFactura.put(FacFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					    hFacFactura.put(FacFacturaBean.C_IDFACTURA,miForm.getIdFactura());
					    
					    Vector<FacFacturaBean> vFacFactura = admFacFactura.selectByPK(hFacFactura);
					    FacFacturaBean beanFacFactura = null;
					    if (vFacFactura!=null && vFacFactura.size()>0) {
					    	beanFacFactura = (FacFacturaBean) vFacFactura.get(0);
					        
					        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
					    	beanFacFactura.setImpTotalCompensado(new Double(beanFacFactura.getImpTotalCompensado().doubleValue() + new Double(total).doubleValue()));
					    	beanFacFactura.setImpTotalPagado(new Double(beanFacFactura.getImpTotalPagado().doubleValue() + new Double(total).doubleValue()));
					    	beanFacFactura.setImpTotalPorPagar(new Double(beanFacFactura.getImpTotalPorPagar().doubleValue() - new Double(total).doubleValue()));
					        
					        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO, pero esta vez es fijo a ANULADO (8)
					    	beanFacFactura.setEstado(new Integer(8));
							
					        if (!admFacFactura.update(beanFacFactura)) {
					           
					        	if (Status.STATUS_ACTIVE  == tx.getStatus()){
					        	   try {tx.rollback();}catch (Exception e) {}
					            }
					        	
					        	if(!abonoMasivo)
					        		throw new ClsExceptions("Error al actualizar los importes de la factura: "+admFacFactura.getError());
					        	else
					        		return abonoInsertado = false;
					        }
					        
					        // Si tiene servicios, mueve FAC_FACTURACIONSUSCRIPCION a FAC_FACTURACIONSUSCRIPCION_ANU
							if (!admFacFacturacionSuscripcion.moverFacturacionSuscripcion(miForm.getIdInstitucion(), miForm.getIdFactura())) {
								throw new SIGAException("Error al obtener las mover el registro de FAC_FACTURACIONSUSCRIPCION a FAC_FACTURACIONSUSCRIPCION_ANU");
							}
							
					    } else {
					        if (Status.STATUS_ACTIVE  == tx.getStatus()){
					        	try {tx.rollback();}catch (Exception e) {}
					        }
					    	
					    	if(!abonoMasivo)
					        	throw new ClsExceptions("No se ha encontrado la factura buscada: "+miForm.getIdInstitucion()+ " "+miForm.getIdFactura());
					        else
					        	return abonoInsertado = false;
					    }

				    }				
				}

				////////////////////////////////////////////////////////////////////////////////////////////////////////				
				Hashtable<String,String> hFacLineaFactura = new Hashtable<String,String>();
				hFacLineaFactura.put(FacLineaFacturaBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				hFacLineaFactura.put(FacLineaFacturaBean.C_IDFACTURA, miForm.getIdFactura());				
				Vector<FacLineaFacturaBean> vFacLineaFactura = admFacLineaFactura.select(hFacLineaFactura);
				
			    // Inserto las lineas
				for (int i=0; vFacLineaFactura!=null && i<vFacLineaFactura.size();i++) {
					FacLineaFacturaBean bFacLineaFactura = (FacLineaFacturaBean) vFacLineaFactura.get(i);
	
					Hashtable<String,Object> hFacLineaAbono = new Hashtable<String,Object>();
				    
					// Cargo la tabla hash con los valores del formulario para insertar en PYS_PRODUCTOSINSTITUCION
					hFacLineaAbono.put(FacLineaAbonoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
					hFacLineaAbono.put(FacLineaAbonoBean.C_IDABONO, (Long)hash.get(FacAbonoBean.C_IDABONO));
					hFacLineaAbono.put(FacLineaAbonoBean.C_CANTIDAD, bFacLineaFactura.getCantidad());
					hFacLineaAbono.put(FacLineaAbonoBean.C_DESCRIPCIONLINEA, bFacLineaFactura.getDescripcion());
					hFacLineaAbono.put(FacLineaAbonoBean.C_IDFACTURA, miForm.getIdFactura());
					hFacLineaAbono.put(FacLineaAbonoBean.C_IVA, bFacLineaFactura.getIva());
				    hFacLineaAbono.put(FacLineaAbonoBean.C_LINEAFACTURA, bFacLineaFactura.getNumeroLinea());
				    hFacLineaAbono.put(FacLineaAbonoBean.C_NUMEROLINEA, ((Long) admFacLineaAbono.getNuevoID(miForm.getIdInstitucion(), ((Long)hash.get(FacAbonoBean.C_IDABONO)).toString())).toString());
				    hFacLineaAbono.put(FacLineaAbonoBean.C_PRECIOUNITARIO, bFacLineaFactura.getPrecioUnitario());

					if (!admFacLineaAbono.insert(hFacLineaAbono)){
						
						if (Status.STATUS_ACTIVE  == tx.getStatus()){
					       try {tx.rollback();}catch (Exception e) {}
					    }
						
						if (!abonoMasivo)
							throw new ClsExceptions("Error en insertar linea abono Devolucion: "+admFacLineaAbono.getError());
						else
					        return abonoInsertado=false;
					}
				}
	
				// RGG 29/05/2009 Cambio de funciones de abono
				if (correcto) {
					beanFacAbono.setImpTotal(importeTotal);
					beanFacAbono.setImpPendientePorAbonar(new Double(importeTotal-importeCompensado));
					beanFacAbono.setImpTotalAbonado(new Double(importeCompensado));
					beanFacAbono.setImpTotalAbonadoEfectivo(new Double(0));
					beanFacAbono.setImpTotalAbonadoPorBanco(new Double(0));
					beanFacAbono.setImpTotalIva(new Double(UtilidadesHash.getString(hashFactura,"TOTAL_IVA")));
					beanFacAbono.setImpTotalNeto(new Double(UtilidadesHash.getString(hashFactura,"TOTAL_NETO")));
				    
					if ((importeTotal-importeCompensado)<=0) {
				        // pagado
				    	beanFacAbono.setEstado(new Integer(1));
				    } else {
				        if (beanFacAbono.getIdCuenta()!=null) {
				            // pendiente pago banco
				        	beanFacAbono.setEstado(new Integer(5));
				        } else {
				            // pendiente pago caja
				        	beanFacAbono.setEstado(new Integer(6));
				        }
				    }
				    
				    if (!admFacAbono.update(beanFacAbono)){
					    
				    	if (Status.STATUS_ACTIVE  == tx.getStatus()){
				    		try {tx.rollback();}catch (Exception e) {}
					    }
				    	
				    	if(!abonoMasivo)
					    	throw new ClsExceptions("Error al actualizar estado e importes del abono: "+admFacAbono.getError());
					    else
					        return abonoInsertado=false;
					}

				    // Obtengo el abono insertado
					Hashtable<String,String> hFacFactura = new Hashtable<String,String>();
					hFacFactura.put(FacFacturaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					hFacFactura.put(FacFacturaBean.C_IDFACTURA,miForm.getIdFactura());
					Vector<FacFacturaBean> vFacFactura = admFacFactura.selectByPK(hFacFactura);
					
					FacFacturaBean bFacFactura = null;
					if (vFacFactura!=null && vFacFactura.size()>0) {
						bFacFactura = (FacFacturaBean) vFacFactura.get(0);
					}
				    // RGG 29/05/2009 Cambio de funciones de factura
			        // Anulada
					bFacFactura.setEstado(new Integer(8));
			        if (!admFacFactura.update(bFacFactura)){
					    
			        	if (Status.STATUS_ACTIVE  == tx.getStatus()){
					       try {tx.rollback();}catch (Exception e) {}
					    }

			        	if(!abonoMasivo)
					    	throw new ClsExceptions("Error al actualizar estado e importes de la factura: "+admFacFactura.getError());
					    else
					        return abonoInsertado=false;
					}
			        
			        // Si tiene servicios, mueve FAC_FACTURACIONSUSCRIPCION a FAC_FACTURACIONSUSCRIPCION_ANU
					if (!admFacFacturacionSuscripcion.moverFacturacionSuscripcion(miForm.getIdInstitucion(), miForm.getIdFactura())) {
						throw new SIGAException("Error al obtener las mover el registro de FAC_FACTURACIONSUSCRIPCION a FAC_FACTURACIONSUSCRIPCION_ANU");
					}
				}				
	
				// Actualizamos el contador de abonos en la tabla ADM_CONTADOR
				gc.setContador(contadorTablaHash, gc.getNuevoContador(contadorTablaHash));

				if (txPrev == null)
					tx.commit();

				abonoInsertado = true;

			} else {
				if (!abonoMasivo)
					throw new ClsExceptions("Error en insertar abono Devolucion: " + admFacAbono.getError());
				else
					return abonoInsertado = false;
			}
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacion" }, e, tx);
		}

		return abonoInsertado;
	}
}