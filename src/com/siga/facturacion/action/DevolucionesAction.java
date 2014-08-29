/*
 * VERSIONES:
 * 
 * miguel.villegas - 22-12-2005 - Creacion
 *	
 */

/**
 * Clase action para el tratamiento de devoluciones.<br/>
 * Gestiona las devoluciones de facturas  
 */

package com.siga.facturacion.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.autogen.model.CenBancos;
import org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.FacDisqueteDevolucionesAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturaIncluidaEnDisqueteBean;
import com.siga.beans.FacLineaDevoluDisqBancoAdm;
import com.siga.beans.FacLineaDevoluDisqBancoBean;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.form.DevolucionesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


public class DevolucionesAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
				
			String accion = miForm.getModo();
				 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("")) {
				request.getSession().removeAttribute("DATAPAGINADOR");
				DevolucionesForm form = (DevolucionesForm) formulario;
				form.reset(mapping,request);
				mapDestino = abrir(mapping, form, request, response);						
			
			} else if (accion.equalsIgnoreCase("abrir")) {
				miForm.reset(mapping,request);
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);		
			
			} else if (accion.equalsIgnoreCase("abrirVolver")) {
				mapDestino = abrir(mapping, miForm, request, response);							
			
			} else if (accion.equalsIgnoreCase("editarFactura")) {
				mapDestino = editarFactura(mapping, miForm, request, response);
			
			} else if (accion.equalsIgnoreCase("reintentar")) {
				mapDestino = reintentar(mapping, miForm, request, response);					
			
			} else if (accion.equalsIgnoreCase("anular")) {
				mapDestino = anular(mapping, miForm, request, response);
			
			} else if (accion.equalsIgnoreCase("renegociarCobrosRecobros")) {
				mapDestino = renegociar(mapping, miForm, request, response);
			
			} else if (accion.equalsIgnoreCase("buscarInit")) {
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscar(mapping, miForm, request, response);
			
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
			    if (miForm.getModal().equalsIgnoreCase("TRUE")) {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} catch (SIGAException es) { 
			throw es;
			
		} catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		
		return mapping.findForward(mapDestino);
	}
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request -  objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		try {
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DevolucionesForm form = (DevolucionesForm) formulario;
			String idInstitucion = user.getLocation();			
			
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","DEV"); // busqueda normal
				
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);
				
			if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				HashMap databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
			     PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)databackup.get("paginador");
			     Vector datos=new Vector();
					
			     //Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				if (paginador!=null) {					
					String pagina = (String)request.getParameter("pagina");				
					if (pagina!=null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
					
			} else {	
				HashMap databackup = new HashMap();
					
				// Obtengo los diferentes disquetes de devoluciones 			
				FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(user);
				PaginadorCaseSensitive devoluciones = devolucionesAdm.getDevoluciones(form);
				
				databackup.put("paginador", devoluciones);
				if (devoluciones!=null){ 
					Vector datos = devoluciones.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				} 
			}		
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return "listado";
	}
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrir";
		
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();			
			
			DevolucionesForm form = (DevolucionesForm) formulario;
			form.setIdInstitucion(idInstitucion);
			
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancosConCuentasBancarias(new Integer(idInstitucion));
			request.setAttribute("listaBancos", bancosList);
			
			if(request.getParameter("buscar") != null)
				request.setAttribute("buscar", request.getParameter("buscar"));
	
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return "abrir";
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
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
			DevolucionesForm form = (DevolucionesForm) formulario;
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			// Obtengo la informacion relacionada con los abonos
			FacLineaDevoluDisqBancoAdm devolucionAdm = new FacLineaDevoluDisqBancoAdm(user);
			Vector desgloseDevolucion = devolucionAdm.getDesgloseDevolucion((String)ocultos.get(0),(String)ocultos.get(1));
					
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", ocultos.get(0));
			request.setAttribute("container", desgloseDevolucion);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		return "editar";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			DevolucionesForm form = (DevolucionesForm) formulario;
		
			// Obtengo valores del formulario y los estructuro
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			Hashtable datosAbonos=new Hashtable();
			datosAbonos.put("accion","consulta");
			datosAbonos.put("idAbono",ocultos.get(0));
			datosAbonos.put("idInstitucion",ocultos.get(1));
			
			// Paso de parametros a las pestanhas
			request.setAttribute("datosAbonos", datosAbonos);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		
		return "ver";
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
		try{		
			DevolucionesForm form = (DevolucionesForm) formulario;
			String idInstitucion=form.getIdInstitucion();
			
			// Paso de parametros
			request.setAttribute("IDINSTITUCION", idInstitucion);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		
		return "nuevo";
	}
	
	protected String renegociar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DevolucionesForm form = (DevolucionesForm) formulario;
			FacFacturaAdm facturaAdm = new FacFacturaAdm(user);
			
			Integer idInstitucion = new Integer(user.getLocation());			
			String datosFacturas = form.getDatosFacturas();
			
			if (datosFacturas != null) {
				String strFacturas[] = datosFacturas.split("##");
				
				if (strFacturas.length<1) {
					throw new SIGAException("Error al no obtener facturas");
				};
												
				// JPT: Recorre todas las facturas marcadas y calcula el listado de facturas
				String listaIdsFacturas = "(";
				for (int i=0; i<strFacturas.length; i++) {
					String idFactura = strFacturas[i];					
					
					
					// Solo para la primera factura indico sus datos
					if (i == 0) {				
						Hashtable hFactura = new Hashtable();
						UtilidadesHash.set(hFactura, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
						UtilidadesHash.set(hFactura, FacFacturaBean.C_IDFACTURA, idFactura);
						
						Vector vFactura = facturaAdm.select(hFactura);
						if (vFactura!=null && vFactura.size()>0) {
							FacFacturaBean beanFactura = (FacFacturaBean) vFactura.get(0);
							request.setAttribute("beanFactura", beanFactura);
														
							if ((beanFactura.getIdCuentaDeudor() != null && !beanFactura.getIdCuentaDeudor().equals("")) || 
									(beanFactura.getIdCuenta() != null && !beanFactura.getIdCuenta().equals(""))) {
							
								// JPT: Obtiene la cuenta bancaria de la factura
								Hashtable hCuentaBancaria = new Hashtable();
								UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDINSTITUCION, beanFactura.getIdInstitucion());
								if (beanFactura.getIdCuentaDeudor() != null && !beanFactura.getIdCuentaDeudor().equals("")){
									UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDCUENTA, beanFactura.getIdCuentaDeudor());
									UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDPERSONA, beanFactura.getIdPersonaDeudor());
								}else{
									UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDCUENTA, beanFactura.getIdCuenta());
									UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDPERSONA, beanFactura.getIdPersona());
								}
								
								CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
								Vector vCuentas = cuentasAdm.select(hCuentaBancaria);
								if (vCuentas != null && vCuentas.size() == 1) {
									CenCuentasBancariasBean beanCuentaBancaria = (CenCuentasBancariasBean) vCuentas.get(0);
									request.setAttribute("beanCuentaBancaria", beanCuentaBancaria);
								}
							}
							
						} else {
							throw new SIGAException("Error al no obtener datos de la factura " + idFactura);
						}
						
						listaIdsFacturas += idFactura;
						
					} else {
						listaIdsFacturas += "," + idFactura;
					}					
				}
				listaIdsFacturas += ")";
				
				// JPT: Obtiene la ultima fecha de pago de una lista de facturas
				FacPagosPorCajaAdm admPagosPorCaja = new FacPagosPorCajaAdm(user);
				String ultimaFechaPagosFactura = admPagosPorCaja.getUltimaFechaPagosFacturas(idInstitucion, listaIdsFacturas);
				if (ultimaFechaPagosFactura==null || ultimaFechaPagosFactura.equals("")) {
					throw new SIGAException("Error al no obtener la última fecha de los pagos de las facturas");
				}
				request.setAttribute("ultimaFechaPagosFactura", ultimaFechaPagosFactura);
				
				// JPT: Indica el numero de facturas seleccionadas
				request.setAttribute("numeroFacturas", new Integer(strFacturas.length));
				
				// JPT: Consulta las personas de la factura
				Integer numeroPersonasFactura = facturaAdm.getSelectPersonas(idInstitucion, strFacturas);
				request.setAttribute("numeroPersonasFactura", numeroPersonasFactura);
				
				if (numeroPersonasFactura == 1) { 
					// JPT: Consulta el numero de cuentas bancarias activas de cargos de las personas de las facturas (en principio es para una persona)
					Integer numeroCuentasPersona = facturaAdm.getCuentasActivasPersona(idInstitucion, listaIdsFacturas);
					request.setAttribute("numeroCuentasPersona", numeroCuentasPersona);					
				}				
				
				// JPT: Obtiene el numero de facturas por banco
				Integer numeroFacturasPorBanco = facturaAdm.getNumeroFacturasPorBanco(idInstitucion, strFacturas);
				request.setAttribute("numeroFacturasPorBanco", numeroFacturasPorBanco);				

				request.setAttribute("datosFacturas", datosFacturas);
				
			} else {
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorLectura");
				return "nuevo";
			}
		
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		
		return "renegociar";
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
		String result = "abrir";
		UserTransaction tx 	= null;
		
		String idInstitucion= "";
		String identificador= "";
		String nombreFichero= "";		
		boolean correcto = true;		
		String codigoError = "5397";	// Código de error, el fichero no se ha encontrado.
		String[] codigosErrorFormato = {"20000", "5399", "5402"};
		String codigoErrorBanco = "-100"; // Código de error, EL FICHERO CARGADO ES DE UN BANCO DIFERENTE AL USADO POR EL COLEGIO PARA EMITIR SUS DOMICILIACIONES
		String codretorno;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");						
			idInstitucion = user.getLocation();	
						
			// Gestion de nombres de ficheros del servidor y de oracle
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);			
		    String rutaServidor = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + rp.returnProperty("facturacion.directorioDevolucionesJava");
		    String rutaOracle = rp.returnProperty("facturacion.directorioDevolucionesOracle");

			// Obtengo los datos del formulario			
			FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(user);
			DevolucionesForm miForm = (DevolucionesForm)formulario;			

			ClsLogging.writeFileLog("Aplicar Comisiones de devolucion="+miForm.getComisiones(),8);
			
			// Obtengo un nuevo identificador para la devolucion
			identificador = devolucionesAdm.getNuevoID(idInstitucion).toString();	
			
			// Obtenemos la ruta completa del servidor donde vamos a generar el fichero	
     		rutaServidor += File.separator + idInstitucion;
     		nombreFichero = rutaServidor + File.separator +identificador+".d19";
     		
     		// Obtenemos la ruta completa de Oracle.
     		String barra 	= "";
    		if (rutaOracle.indexOf("/") > -1) 
    			barra = "/"; 
    		if (rutaOracle.indexOf("\\") > -1) 
    			barra = "\\";        		
    		rutaOracle 	+= barra + idInstitucion + barra;

    		// tratamiento del fichero de ficheroOriginalgrafia
		    FormFile ficheroOriginal = miForm.getRuta();
		    if(ficheroOriginal==null || ficheroOriginal.getFileSize()<1){
		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
		    	
		    } else {
		    	InputStream stream =null;
	    		BufferedReader rdr = null;
	    		BufferedWriter out = null;
		    	try {			
		    		//retrieve the file data
		    		stream = ficheroOriginal.getInputStream();
		    		//write the file to the file specified
		    		File camino = new File (rutaServidor);
		    		camino.mkdirs();

		    		rdr = new BufferedReader(new InputStreamReader(stream));
		    		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreFichero),"ISO-8859-1"));

		    		String line = rdr.readLine();
		    		
		    		while (line!=null) {
			    		out.write(line);
			    		out.write("\n");
			    		line = rdr.readLine();
		    		}
		    		
		    	} catch (FileNotFoundException fnfe) {
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
					
		    	} catch (IOException ioe) {
					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
					
		    	} finally	{
		    		// close the stream
		    		stream.close();
		    		out.close();
		    		rdr.close();
		    	}
		    }
		    
		    // Comienzo control de transacciones
			tx = user.getTransactionPesada();
			tx.begin();		

			// Llamada a PL     		
			codretorno = actualizacionTablasDevoluciones(miForm.getIdInstitucion(), rutaOracle, identificador + ".d19", user.getLanguageInstitucion(), user.getUserName());
			boolean isTodasRenegociadas = true;
			Facturacion facturacion = new Facturacion(user);
			if (codretorno.equalsIgnoreCase("0")){
				String nuevaFormaPago 	= miForm.getDatosPagosRenegociarNuevaFormaPago();
				
				if(nuevaFormaPago!=null && !nuevaFormaPago.equals("noRenegociarAutomaticamente")){ // Aplica la cuenta bancaria activa
					Vector factDevueltasVector = devolucionesAdm.getFacturasDevueltasEnDisquete(new Integer(idInstitucion), new Integer(identificador));
					
					for (int i = 0; i < factDevueltasVector.size(); i++) {
						Row fila = (Row) factDevueltasVector.get(i);
	            		Hashtable<String, Object> htFila=fila.getRow();
	            		String idFactura = UtilidadesHash.getString(htFila, FacFacturaBean.C_IDFACTURA);
	            		Integer estadoFactura = UtilidadesHash.getInteger(htFila, FacFacturaBean.C_ESTADO);
	            		Double impTotalPorPagar = UtilidadesHash.getDouble(htFila, FacFacturaBean.C_IMPTOTALPORPAGAR);
	            		String recibo = UtilidadesHash.getString(htFila, FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO);
	            		Hashtable htCuenta = new Hashtable();;
						try {
							//El idCuenta modifica en este metodo asi que sera a esta cuenta la que se aplicara la comision
							facturacion.insertarRenegociar(new Integer(idInstitucion), idFactura, estadoFactura, nuevaFormaPago, null, impTotalPorPagar, miForm.getDatosPagosRenegociarObservaciones(), "", true, htCuenta);
							
						} catch (SIGAException e) {
							isTodasRenegociadas = false;
							continue;
							
						} finally {
							if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
								FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
								Hashtable filtroLineasHashtable = new Hashtable(); 
								filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,idInstitucion);
								filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,identificador);
								filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,recibo);
								Vector lineasDevolucion = admLDDB.selectByPK(filtroLineasHashtable);	
								
								FacLineaDevoluDisqBancoBean lineaDevolucion =(FacLineaDevoluDisqBancoBean)lineasDevolucion.get(0);
								String idCuenta = null;
								if(htCuenta!=null && htCuenta.get("idCuenta")!=null)
									idCuenta = (String)htCuenta.get("idCuenta");
								
								correcto = facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), idCuenta, user, true);
								if(!correcto){
									codretorno = "-32";
									break;
								}
							}
							
						}

					}
				}else{
					if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
				    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion="+miForm.getComisiones(),8);
				    	
						// Identificamos los disquetes devueltos asociados al fichero de devoluciones
						FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
						Vector vDevoluciones = admLDDB.obtenerDevolucionesFichero(idInstitucion, identificador);
						
						// Aplicamos la comision a cada devolusion
						for (int d=0; d<vDevoluciones.size(); d++) {
							FacLineaDevoluDisqBancoBean lineaDevolucion = (FacLineaDevoluDisqBancoBean)vDevoluciones.get(d);
							if (!facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), null, user, false)) {
								codretorno = "-32";
								break;			
							}
						}
					}
				}
				
			} else if(codretorno.equalsIgnoreCase(codigoError)){
				tx.rollback();
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
				return "mostrarVentana";
				
			} else 	if (codretorno.equals("5404")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.fechaDevolucion");
				return "nuevo";
				
			} else 	if (codretorno.equals("5405")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.importeDevolucion"); // No tiene recurso
				return "nuevo";				
				
			} else if(Arrays.asList(codigosErrorFormato).contains(codretorno)){
				tx.rollback();		
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorFormato");
				return "nuevo";
				
			} else if (codretorno.equalsIgnoreCase(codigoErrorBanco)){
				tx.rollback();	
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorBanco"); // No tiene recurso
				return "nuevo";
				
			} else {
				correcto=false;
			}
			
			if (codretorno.equalsIgnoreCase("-32")){
				tx.rollback();
				request.setAttribute("mensaje","facturacion.devolucion.error.aplicarComision");
				return "nuevo";
				
			} else { 			
				if (correcto){					
					tx.commit();
	
					if (isTodasRenegociadas) {
						result=exitoModal("facturacion.nuevoFichero.literal.procesoCorrecto",request);
						
					} else {
						result=exitoModal("facturacion.renegociar.aviso.noTodasRenegociadas",request);
					}
					
				} else {
					tx.rollback();		
					request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorLectura");
					return "nuevo";
				}
			}
			
		}catch (Exception e) { 
			try {
				borrarFichero(identificador, idInstitucion);
			} catch(Exception ex){
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, ex, tx); 
			}
			
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
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
		return "modificar";		
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
		return "borrar";
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
		return "listar";
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
	protected String editarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DevolucionesForm form = (DevolucionesForm) formulario;
			
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);
					
			// Paso de parametros empleando request
			Hashtable datosFac = new Hashtable();
			UtilidadesHash.set(datosFac,"accion", "editar");
			UtilidadesHash.set(datosFac,"idFactura", (String)ocultos.get(1));
			UtilidadesHash.set(datosFac,"idInstitucion", (String)ocultos.get(0));

			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, (String)ocultos.get(0));
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, (String)ocultos.get(1));
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm (user);
			Vector v = facturaAdm.selectByPK(claves);			
			if (v != null && v.size()>0) {
				FacFacturaBean facturaBean = (FacFacturaBean) v.get(0);
				UtilidadesHash.set(datosFac,"idPersona", facturaBean.getIdPersona());
			}
			
			// Paso de parametros a las pestanhas
			request.setAttribute("datosFacturas", datosFac);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);			
		}
		
		return "editarFactura";
	}
	/** 
	 *  Funcion que reintenta insertar el fichero 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String reintentar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result = "abrir";
		UserTransaction tx = null;		
		String identificador = "";		
		boolean correcto = true;
		
		String codigoError = "5397";	// Código de error, el fichero no se ha encontrado.
		String[] codigosErrorFormato = {"20000", "5399", "5402"};
		String codigoErrorBanco = "-100"; // Código de error, EL FICHERO CARGADO ES DE UN BANCO DIFERENTE AL USADO POR EL COLEGIO PARA EMITIR SUS DOMICILIACIONES
		String codretorno;
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");							
			String idInstitucion = user.getLocation();	
						
			// Gestion del nombre del fichero de oracle
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    String rutaOracle = rp.returnProperty("facturacion.directorioDevolucionesOracle");
		    			
			// Obtengo los datos del formulario			
			FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(user);
			DevolucionesForm miForm = (DevolucionesForm)formulario;			
			
     		// Obtenemos la ruta completa de Oracle.
     		String barra 	= "";
    		if (rutaOracle.indexOf("/") > -1) 
    			barra = "/"; 
    		if (rutaOracle.indexOf("\\") > -1) 
    			barra = "\\";        		
    		rutaOracle 	+= barra + idInstitucion + barra;
    		
    		// Comienzo la transaccion
    		tx = user.getTransactionPesada(); 		
			tx.begin();
			
			// Obtengo un nuevo identificador para la devolucion
			identificador = devolucionesAdm.getNuevoID(idInstitucion).toString();
			
			// Llamada a PL			
			codretorno = actualizacionTablasDevoluciones(miForm.getIdInstitucion(), rutaOracle, identificador + ".d19", user.getLanguageInstitucion(), user.getUserName());
			
			boolean isTodasRenegociadas = true;
			Facturacion facturacion = new Facturacion(user);
			if (codretorno.equalsIgnoreCase("0")){
				String nuevaFormaPago 	= miForm.getDatosPagosRenegociarNuevaFormaPago();
				
				if(nuevaFormaPago!=null && !nuevaFormaPago.equals("noRenegociarAutomaticamente")){ // Aplica la cuenta bancaria activa
					Vector factDevueltasVector = devolucionesAdm.getFacturasDevueltasEnDisquete(new Integer(idInstitucion), new Integer(identificador));
					
					for (int i = 0; i < factDevueltasVector.size(); i++) {
						Row fila = (Row) factDevueltasVector.get(i);
	            		Hashtable<String, Object> htFila=fila.getRow();
	            		String idFactura = UtilidadesHash.getString(htFila, FacFacturaBean.C_IDFACTURA);
	            		Integer estadoFactura = UtilidadesHash.getInteger(htFila, FacFacturaBean.C_ESTADO);
	            		Double impTotalPorPagar = UtilidadesHash.getDouble(htFila, FacFacturaBean.C_IMPTOTALPORPAGAR);
	            		String recibo = UtilidadesHash.getString(htFila, FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO);
	            		Hashtable htCuenta = new Hashtable();
						try {
							//El idCuenta modifica en este metodo asi que sera a esta cuenta la que se aplicara la comision
							facturacion.insertarRenegociar(new Integer(idInstitucion), idFactura, estadoFactura, nuevaFormaPago, null, impTotalPorPagar, miForm.getDatosPagosRenegociarObservaciones(), "", true, htCuenta);
							
						} catch (SIGAException e) {
							isTodasRenegociadas = false;
							continue;
							
						} finally {
							if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
								FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
								Hashtable filtroLineasHashtable = new Hashtable(); 
								filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,idInstitucion);
								filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,identificador);
								filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,recibo);
								Vector lineasDevolucion = admLDDB.select(filtroLineasHashtable);		
								
								FacLineaDevoluDisqBancoBean lineaDevolucion =(FacLineaDevoluDisqBancoBean)lineasDevolucion.get(0);
								String idCuenta = null;
								if(htCuenta!=null && htCuenta.get("idCuenta")!=null)
									idCuenta = (String)htCuenta.get("idCuenta");
								
								correcto = facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), idCuenta, user, true);
								if(!correcto){
									codretorno = "-32";
									break;
								}
							}							
						}
					}
				}else{
					if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
				    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion="+miForm.getComisiones(),8);
				    	
						// Identificamos los disquetes devueltos asociados al fichero de devoluciones
						FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
						Vector vDevoluciones = admLDDB.obtenerDevolucionesFichero(idInstitucion, identificador);
						
						// Aplicamos la comision a cada devolusion
						for (int d=0; d<vDevoluciones.size(); d++) {
							FacLineaDevoluDisqBancoBean lineaDevolucion = (FacLineaDevoluDisqBancoBean)vDevoluciones.get(d);
							
							correcto = facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), null, user, false);
							if(!correcto){
								codretorno = "-32";
								break;
							}
						}				    	
					}
				}	
				
			} else if(codretorno.equalsIgnoreCase(codigoError)){
				tx.rollback();
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
				return "mostrarVentana";
				
			} else 	if (codretorno.equals("5404")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.fechaDevolucion");
				return "nuevo";
				
			} else 	if (codretorno.equals("5405")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.importeDevolucion");
				return "nuevo";				
				
			} else if(Arrays.asList(codigosErrorFormato).contains(codretorno)){
				tx.rollback();		
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorFormato");
				return "nuevo";
				
			} else if (codretorno.equalsIgnoreCase(codigoErrorBanco)){
				tx.rollback();	
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorBanco");
				return "nuevo";
				
			} else {
				correcto=false;
			}
			
			if (codretorno.equalsIgnoreCase("-32")){
				tx.rollback();
				request.setAttribute("mensaje","facturacion.devolucion.error.aplicarComision");
				return "nuevo";
				
			} else { 			
				if (correcto){					
					tx.commit();
	
					if (isTodasRenegociadas) {
						result=exitoRefresco("facturacion.nuevoFichero.literal.procesoCorrecto",request);
						
					} else {
						result=exitoRefresco("facturacion.renegociar.aviso.noTodasRenegociadas",request);
					}
					
				} else {
					tx.rollback();		
					request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
					return "mostrarVentana";
				}
			}
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}		

		return result;					     	
	}

	/** 
	 *  Funcion que borra el fichero 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String anular(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");							
			String idInstitucion = user.getLocation();
			
			// Obtengo los datos del formulario			
			FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(user);
				
			String identificador = devolucionesAdm.getNuevoID(idInstitucion).toString();				
    		borrarFichero(identificador, idInstitucion);
	    		
 		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
 		return "abrir";  
	}

	/** 
	 *  Funcion que realiza una llamada a la PL PKG_SIGA_CARGOS.DEVOLUCIONES
	 * @param  String - identificador institucion
	 * @param  path -  ruta hasta el fichero
	 * @param  fichero - nombre del fichero 
	 * @param  usuario - identificador del usuario  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String actualizacionTablasDevoluciones(String institucion, String path, String fichero, String idioma, String usuario) throws ClsExceptions {	
		String resultado[] = new String[2];
		String codigoError_FicNoEncontrado = "5397";	// Código de error, el fichero no se ha encontrado.
		String codretorno  = codigoError_FicNoEncontrado;
		try	{			
			int i=0;
			while (i<3 && codretorno.equalsIgnoreCase(codigoError_FicNoEncontrado)){
				i++;
				Thread.sleep(1000);
				Object[] param_in = new Object[5];
		    	param_in[0] = institucion;
		    	param_in[1] = path;
		    	param_in[2] = fichero;
		    	param_in[3] = idioma;
		    	param_in[4] = usuario;
		    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.DEVOLUCIONES(?,?,?,?,?,?,?)}", 2, param_in);
		    	codretorno = resultado[0]; 
			}
			
		} catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en actualizacionTableroDevoluciones.  Proc:PKG_SIGA_CARGOS.DEVOLUCIONES "+resultado[1]);
		}
		
		return codretorno;
	}
	
	protected void borrarFichero(String identificador, String idInstitucion) throws ClsExceptions {	
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	    String rutaServidor  = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + rp.returnProperty("facturacion.directorioDevolucionesJava");
		
		// Obtenemos la ruta completa del servidor donde vamos a generar el fichero	  
 		rutaServidor += File.separator + idInstitucion;
 		String nombreFichero = rutaServidor + File.separator + identificador + ".d19";
 		
 		File ficJava = new File(nombreFichero);
 		if (ficJava.exists()){		 
 			ficJava.delete();
 		}
	}
	
	
    /* (non-Javadoc)
     * @see com.siga.general.MasterAction#download(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
		    DevolucionesForm form = (DevolucionesForm)formulario;
	        
	        Vector vOcultos = form.getDatosTablaOcultos(0);
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();

		    // Nombre del fichero
	 		String ficheroDownload = ((String)vOcultos.elementAt(2)).trim();
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    String rutaFicheroDownload = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + 
		    							 rp.returnProperty("facturacion.directorioDevolucionesJava") +
		    							 File.separator + idInstitucion +
		    							 File.separator + ficheroDownload;

		    request.setAttribute("nombreFichero", ficheroDownload);
		    request.setAttribute("rutaFichero", rutaFicheroDownload);
		    
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		
		return "descargaFichero";
    }
    	
	/**
	 * Funcion que realiza una llamada a la PL PKG_SIGA_CARGOS.DevolucionesManuales
	 * @param institucion
	 * @param listaFacturas
	 * @param fechaDevolucion
	 * @param user
	 * @return
	 * @throws ClsExceptions
	 */
	protected String[] devolucionManual(String institucion, String listaFacturas, String fechaDevolucion, UsrBean user) throws ClsExceptions {	
		String resultado[] = new String[3];
		try	{			
			Object[] param_in = new Object[5];
	    	param_in[0] = institucion;
	    	param_in[1] = listaFacturas;
	    	param_in[2] = fechaDevolucion;
	    	param_in[3] = user.getLanguageInstitucion();
	    	param_in[4] = user.getUserName();
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.DevolucionesManuales(?,?,?,?,?,?,?,?)}", 3, param_in);
	    	
		} catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en devolucionManual. Proc:PKG_SIGA_CARGOS.DevolucionesManuales " + resultado[2]);
		}
		
		return resultado;
	}    
}