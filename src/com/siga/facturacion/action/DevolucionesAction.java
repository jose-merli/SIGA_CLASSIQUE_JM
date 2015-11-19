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
				mapDestino = renegociarCobrosRecobros(mapping, miForm, request, response);
			
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
			Vector ocultos = form.getDatosTablaOcultos(0);
			
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
			Vector ocultos = form.getDatosTablaOcultos(0);
			
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
	
	/**
	 * Funcion que calcula las formas de renegociacion en Facturación>Gestión de Cobros y Recobros
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String renegociarCobrosRecobros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DevolucionesForm form = (DevolucionesForm) formulario;
			FacFacturaAdm facturaAdm = new FacFacturaAdm(user);
			
			Integer idInstitucion = new Integer(user.getLocation());			
			String datosFacturas = form.getDatosFacturas();
			String ultimaFechaPagosFacturas = "";
			
			if (datosFacturas != null) {
				String strFacturas[] = datosFacturas.split("##");
				
				if (strFacturas.length<1) {
					throw new SIGAException("Error al no obtener facturas");
				};
				
				// JPT: Oracle no admite listas de más de mil elementos
				if (strFacturas.length>1000) {
					throw new SIGAException("facturacion.consultamorosos.error.renegociarMilFacturas");
				};
								
				// Obtiene los datos de la primera factura
				Hashtable hFactura = new Hashtable();
				UtilidadesHash.set(hFactura, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(hFactura, FacFacturaBean.C_IDFACTURA, strFacturas[0]);		
				
				Vector vFactura = facturaAdm.selectByPK(hFactura);
				
				if (vFactura==null || vFactura.size()<1) {
					throw new SIGAException("Error al no obtener datos de la factura " + strFacturas[0]);				
				}
				
				// JPT: Encuentra la factura
				FacFacturaBean beanFactura = (FacFacturaBean) vFactura.get(0);
				request.setAttribute("beanFactura", beanFactura);
				
				Integer numeroPersonasFactura = 0;				
				if (strFacturas.length==1) {
					Integer numeroFacturasPorBanco = 0;
					
					// JPT: Comprueba si es una factura por banco y tiene cuenta
					if (beanFactura.getEstado() != new Integer(ClsConstants.ESTADO_FACTURA_CAJA) && 
							(
								(beanFactura.getIdCuentaDeudor() != null && !beanFactura.getIdCuentaDeudor().equals("")) || 
								(beanFactura.getIdCuenta() != null && !beanFactura.getIdCuenta().equals(""))
							)) {
					
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
						
						CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (user);
						Vector vCuentas = cuentasAdm.selectByPK(hCuentaBancaria);
						if (vCuentas != null && vCuentas.size() == 1) {
							CenCuentasBancariasBean beanCuentaBancaria = (CenCuentasBancariasBean) vCuentas.get(0);
							request.setAttribute("beanCuentaBancaria", beanCuentaBancaria);										
						}
						
						numeroFacturasPorBanco = 1;
					}
										
					numeroPersonasFactura = 1;
					
					// JPT: Indica el numero de facturas por banco en caso de ser una sola factura
					request.setAttribute("numeroFacturasPorBanco", numeroFacturasPorBanco);
					
				} else {
					// JPT: Consulta las personas de las facturas
					numeroPersonasFactura = facturaAdm.getSelectPersonas(idInstitucion, strFacturas);									
				}
				
				// Solo se comprueba para una factura a renegociar
				if (strFacturas.length==1) {		
					
					// JPT: Recorre todas las facturas marcadas y calcula el listado de facturas
					String listaIdsFacturas = "";
					for (int i=0; i<strFacturas.length; i++) {
						String datosFactura = strFacturas[i];
						
						if (datosFactura != null && !datosFactura.equals("")) {
							String arrayFactura[] = datosFactura.split("%%");
							
							if (arrayFactura.length<1) {
								throw new SIGAException("Error al obtener la factura");
							};
							
							String idFactura = arrayFactura[0];
							
							if (i==0) {
								listaIdsFacturas += idFactura;						
							} else {
								listaIdsFacturas += "," + idFactura;
							}
						}
					}				
					
					// JPT: Obtiene la ultima fecha de pago de una lista de facturas
					FacPagosPorCajaAdm admPagosPorCaja = new FacPagosPorCajaAdm(user);
					ultimaFechaPagosFacturas = admPagosPorCaja.getUltimaFechaPagosFacturas(idInstitucion, listaIdsFacturas);
					if (ultimaFechaPagosFacturas==null || ultimaFechaPagosFacturas.equals("")) {
						throw new SIGAException("Error al no obtener la última fecha de los pagos de las facturas");
					}										
				}					
				
				request.setAttribute("ultimaFechaPagosFacturas", ultimaFechaPagosFacturas);
				
				// JPT: Indica el numero de facturas seleccionadas
				request.setAttribute("numeroFacturas", new Integer(strFacturas.length));
				
				// JPT: Indica el numero de personas en las facturas
				request.setAttribute("numeroPersonasFactura", numeroPersonasFactura);
				
				if (numeroPersonasFactura == 1) { 
					// JPT - Renegociacion 118: Obtiene el identificador de la persona
					Long idPersona = (beanFactura.getIdCuentaDeudor()!=null && !beanFactura.getIdCuentaDeudor().equals("")) ? beanFactura.getIdPersonaDeudor() : beanFactura.getIdPersona();
					
					// JPT - Renegociacion 118: Consulta las cuentas bancarias activas de cargos de la persona
					CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(user);
					List listaCuentasCargoActivas = cuentasBancariasAdm.getCuentasCargo(idPersona, idInstitucion);
					request.setAttribute("numeroCuentasPersona", listaCuentasCargoActivas.size());
				}				
				
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
	 * Funcion que realiza la devolucion (Facturacion > Fichero de Devoluciones > Gestion de Devoluciones)
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
		String[] codigosErrorFormato = {"20000", "5399", "5402"};
		String codretorno;
		String resultado[];

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
		    		
		    		boolean esXML = false;
		    		boolean controlarDocument = true;
		    		
		    		String nombreFicheroDevoluciones = ficheroOriginal.getFileName();
		    		if (nombreFicheroDevoluciones.toUpperCase().endsWith("XML")) {
		    			esXML = true;
		    		}
		    		
		    		String linea = "";		    		
		    		while (linea!=null && linea.equals("")) {
		    			linea = rdr.readLine();
		    			if (linea!=null) {
		    				linea = linea.trim();
		    			}
		    		}
		    		
		    		while (linea!=null) {
		    			
			    		/** Jorge PT (11/11/2015):
			    		 * Este codigo es necesario para el tratamiento de los ficheros xml.
			    		 * Porque en la version SIGA_121 se necesitaba crear el fichero en java y sobreescribirlo en pl (para quitar los atributos XMLNS de la etiqueta Document).
			    		 * Se han detectado dos problemas en pl que podemos evitar desde java:
			    		 * - Cuando viene todo el xml en una linea muy grande (37.XXX), no funciona bien el pl, aunque trabaja con funciones CLOB, por debajo debe trabajar con VARCHAR2.
			    		 * - Cuando crea un fichero desde java se crea con usuario root, y cuando se sobreescribe desde pl se utiliza un usuario de oracle, con lo que en pl no es el propietario del fichero.
			    		 * - Al crear un fichero el propietario tiene permisos de lectura y escritura, pero para el resto tiene permisos de solo lectura. 
			    		 */
		    			
		    			String lineaFichero = linea;
		    			
		    			// Control que valida si es un fichero XML
		    			if (!esXML && linea.indexOf("<")>=0) {
		    				esXML = true;
		    			}
			    		
		    			// Control que realiza una serie de cambios cuando es XML
			    		if (esXML) {	
			    			
			    			// Comienzo a buscar por la primera letra
			    			int buscador = 0;
			    			
			    			// Control de longitud de linea
			    			while (buscador < linea.length()) {
			    				
			    				// Busco < (principio de etiqueta)
			    				buscador = linea.indexOf("<", buscador);
			    				
			    				// Si no tiene etiqueta pinto la linea
			    				if (buscador < 0) {		    					
			    					break;
			    					
			    				}
			    				
			    				// Si tiene < pasamos de letra
			    				buscador++;
			    				
			    				// Comprueba que tenga por lo menos alguna letra mas
			    				if (linea.length() < buscador) {
			    					break;
			    				}
			    				
			    				// Obtengo la siguiente letra al <
			    				char letra = linea.charAt(buscador);
			    					
			    				// Si no encuentra </ es que es una apertura de etiqueta
			    				if (letra != '/') { 
			    					
			    					final String etiquetaDocument = "DOCUMENT"; 
			    					
			    					// Control de si hay que validar la etiqueta DOCUMENT
			    					if (controlarDocument && linea.length() > buscador + etiquetaDocument.length()) {
			    						
				    					// Obtengo el nombre de la etiqueta
				    					String buscaDocument = linea.substring(buscador, buscador + etiquetaDocument.length());
				    					
				    					// Compruebo si la etiqueta es DOCUMENT
				    					if (buscaDocument.equalsIgnoreCase(etiquetaDocument)) {
				    						
				    						// Hay que buscar el final de la etiqueta DOCUMENT
						    				int buscadorDocument = linea.indexOf(">", buscador + etiquetaDocument.length());
						    							
						    				// Encuento el final de la etiqueta DOCUMENT
						    				if (buscadorDocument > 0) {
						    					
						    					// Elimino los atributos de la etiqueta DOCUMENT
						    					linea = linea.substring(0, buscador + etiquetaDocument.length()) + linea.substring(buscadorDocument);
						    					
						    					// Indico que hay que buscar despues de la etiqueta DOCUMENT
						    					buscador += etiquetaDocument.length();
						    					
						    					// Indicamos que ya hemos controlado la etiqueta DOCUMENT
						    					controlarDocument = false;
						    				}
				    					}
			    					}
			    					
			    					// Pasamos a la siguiente letra
			    					buscador++;
			    					continue;
			    				}
			    					
			    				// Encuentro </ y buscamos el final de la etiqueta
			    				buscador = linea.indexOf(">", buscador);
			    							
			    				// Encuento el final de la etiqueta </...>
			    				if (buscador<0) {
			    					break;
			    				}
			    				
			    				// Pasamos a la siguiente letra >
			    				buscador++;
			    				
			    				// ponemos un retorno de linea al finalizar cada etiqueta final, porque asi evitamos un xml en una linea inmensa
			    				lineaFichero = linea.substring(0, buscador);
			    				
			    				// Escribimos la linea
			    				out.write(lineaFichero);
			    				out.write("\n");
			    				
			    				// Eliminamos los datos escritos
			    				linea = linea.substring(buscador);
			    				
			    				// Volvemos a empezar
			    				buscador = 0;
				    		}
			    			
			    			// Guardamos la linea tal como esta ahora
			    			lineaFichero = linea;
			    		} // FIN WHILE		    			
		    			
			    		// Comprueba si queda algo por escribir de la linea
			    		lineaFichero = lineaFichero.trim();
		    			if (!lineaFichero.equals("")) {
		    				
		    				// Escribimos la linea 
		    				out.write(lineaFichero);
		    				out.write("\n");
		    			}
		    			
		    			// Obtenemos la siguiente linea
		    			linea = "";		    		
			    		while (linea!=null && linea.equals("")) {
			    			linea = rdr.readLine();
			    			if (linea!=null) {
			    				linea = linea.trim();
			    			}
			    		}
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
			resultado = actualizacionTablasDevoluciones(miForm.getIdInstitucion(), rutaOracle, identificador + ".d19", user.getLanguageInstitucion(), user.getUserName());
			codretorno = resultado[0];
			String fechaDevolucion = resultado[2];
			
			Facturacion facturacion = new Facturacion(user);
			if (codretorno.equalsIgnoreCase("0")){
				String nuevaFormaPago 	= miForm.getDatosPagosRenegociarNuevaFormaPago();
				
				if(nuevaFormaPago!=null && !nuevaFormaPago.equals("noRenegociarAutomaticamente")){ // Aplica la cuenta bancaria activa
					Vector factDevueltasVector = devolucionesAdm.getFacturasDevueltasEnDisquete(idInstitucion, identificador);
					
					for (int i = 0; i < factDevueltasVector.size(); i++) {
						Row fila = (Row) factDevueltasVector.get(i);
	            		Hashtable htFila = fila.getRow();	            		
	            		String recibo = UtilidadesHash.getString(htFila, FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO);
	            		
	            		FacFacturaBean facturaBean = null;
	            		FacFacturaBean facturaBeanComision = null;
						if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){							
							Hashtable filtroLineasHashtable = new Hashtable(); 
							filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,idInstitucion);
							filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,identificador);
							filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,recibo);
							
							FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
							Vector lineasDevolucion = admLDDB.select(filtroLineasHashtable);	
							
							FacLineaDevoluDisqBancoBean lineaDevolucion =(FacLineaDevoluDisqBancoBean)lineasDevolucion.get(0);
							facturaBeanComision = facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), user, fechaDevolucion);							
						}
						
						// Obtiene el bean de la factura a renegociar
						FacFacturaAdm facturaAdm = new FacFacturaAdm(user);
						if (facturaBeanComision!=null) {
							facturaBean = facturaBeanComision;
						} else {							
		            		facturaBean = (FacFacturaBean) facturaAdm.hashTableToBean(htFila);
						}
	            		
						facturacion.insertarRenegociar(
								facturaBean, 
								nuevaFormaPago, 
								null, 
								miForm.getDatosPagosRenegociarObservaciones(), 
								miForm.getDatosRenegociarFecha(), 
								true);
					}
					
				} else {
					if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
				    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion="+miForm.getComisiones(),8);
				    	
						// Identificamos los disquetes devueltos asociados al fichero de devoluciones
						FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
						Vector<FacLineaDevoluDisqBancoBean> vDevoluciones = admLDDB.obtenerDevoluciones(idInstitucion, identificador, true);
						
						// Aplicamos la comision a cada devolucion
						for (int d=0; d<vDevoluciones.size(); d++) {
							FacLineaDevoluDisqBancoBean lineaDevolucion = (FacLineaDevoluDisqBancoBean)vDevoluciones.get(d);
							facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), user, fechaDevolucion);
						}
					}
				}
				
			} else if (codretorno.equals("5420")) {
				tx.rollback();		
				request.setAttribute("mensaje", resultado[1]);
				return "nuevo";					
				
			} else if(codretorno.equalsIgnoreCase("5397")){ // El fichero no se ha encontrado.
				tx.rollback();
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
				return "mostrarVentana";
				
			} else if (codretorno.equals("5404")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.fechaDevolucion");
				return "nuevo";
				
			} else if (codretorno.equals("5405")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.importeDevolucion");
				return "nuevo";				
				
			} else if (codretorno.equalsIgnoreCase("5406")){ // IBAN diferente al usado en el adeudo
				tx.rollback();	
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorBanco");
				return "nuevo";
				
			} else if (codretorno.equalsIgnoreCase("5407")){ // No encuentra la factura en FAC_FACTURA y FAC_FACTURAINCLUIDAENDISQUETE
				tx.rollback();	
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorFactura");
				return "nuevo";								
				
			} else if(Arrays.asList(codigosErrorFormato).contains(codretorno)){ // "20000", "5399", "5402"
				tx.rollback();		
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorFormato");
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
					
					request.setAttribute("parametrosArray", resultado);
					request.setAttribute("modal","");							
					return "exitoParametros";
					
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
		
		String[] codigosErrorFormato = {"20000", "5399", "5402"};
		String codretorno;
		String resultado[];
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
			resultado = actualizacionTablasDevoluciones(miForm.getIdInstitucion(), rutaOracle, identificador + ".d19", user.getLanguageInstitucion(), user.getUserName());
			codretorno = resultado[0];
			String fechaDevolucion = resultado[2];			
			
			Facturacion facturacion = new Facturacion(user);
			if (codretorno.equalsIgnoreCase("0")){
				String nuevaFormaPago 	= miForm.getDatosPagosRenegociarNuevaFormaPago();
				
				if(nuevaFormaPago!=null && !nuevaFormaPago.equals("noRenegociarAutomaticamente")){ // Aplica la cuenta bancaria activa
					Vector factDevueltasVector = devolucionesAdm.getFacturasDevueltasEnDisquete(idInstitucion, identificador);
					
					for (int i = 0; i < factDevueltasVector.size(); i++) {
						Row fila = (Row) factDevueltasVector.get(i);
	            		Hashtable htFila = fila.getRow();
	            		String recibo = UtilidadesHash.getString(htFila, FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO);
	            		
	            		FacFacturaBean facturaBean = null;
	            		FacFacturaBean facturaBeanComision = null;
						if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){							
							Hashtable filtroLineasHashtable = new Hashtable(); 
							filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,idInstitucion);
							filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,identificador);
							filtroLineasHashtable.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,recibo);
							
							FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
							Vector lineasDevolucion = admLDDB.select(filtroLineasHashtable);		
							
							FacLineaDevoluDisqBancoBean lineaDevolucion =(FacLineaDevoluDisqBancoBean)lineasDevolucion.get(0);
							facturaBeanComision = facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), user, fechaDevolucion);
						}
						
						// Obtiene el bean de la factura a renegociar
						FacFacturaAdm facturaAdm = new FacFacturaAdm(user);
						if (facturaBeanComision!=null) {
							facturaBean = facturaBeanComision;
						} else {							
		            		facturaBean = (FacFacturaBean) facturaAdm.hashTableToBean(htFila);
						}
						
						facturacion.insertarRenegociar(
								facturaBean, 
								nuevaFormaPago, 
								null, 
								miForm.getDatosPagosRenegociarObservaciones(), 
								miForm.getDatosRenegociarFecha(), 
								true);
					}				
					
				} else {
					if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
				    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion="+miForm.getComisiones(),8);
				    	
						// Identificamos los disquetes devueltos asociados al fichero de devoluciones
						FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
						Vector<FacLineaDevoluDisqBancoBean> vDevoluciones = admLDDB.obtenerDevoluciones(idInstitucion, identificador, true);
						
						// Aplicamos la comision a cada devolucion
						for (int d=0; d<vDevoluciones.size(); d++) {
							FacLineaDevoluDisqBancoBean lineaDevolucion = (FacLineaDevoluDisqBancoBean)vDevoluciones.get(d);
							facturacion.aplicarComisionAFactura (idInstitucion, lineaDevolucion, miForm.getComisiones(), user, fechaDevolucion);
						}				    	
					}
				}	
				
			} else if (codretorno.equals("5420")) {
				tx.rollback();		
				request.setAttribute("mensaje", resultado[1]);
				return "nuevo";					
				
			} else if(codretorno.equalsIgnoreCase("5397")){ // El fichero no se ha encontrado.
				tx.rollback();
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
				return "mostrarVentana";
				
			} else if (codretorno.equals("5404")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.fechaDevolucion");
				return "nuevo";
				
			} else if (codretorno.equals("5405")) {
				tx.rollback();		
				request.setAttribute("mensaje", "facturacion.devolucionManual.error.importeDevolucion");
				return "nuevo";				
				
			} else if (codretorno.equalsIgnoreCase("5406")){ // IBAN diferente al usado en el adeudo
				tx.rollback();	
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorBanco");
				return "nuevo";
				
			} else if (codretorno.equalsIgnoreCase("5407")){ // No encuentra la factura en FAC_FACTURA y FAC_FACTURAINCLUIDAENDISQUETE
				tx.rollback();	
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorFactura");
				return "nuevo";								
				
			} else if(Arrays.asList(codigosErrorFormato).contains(codretorno)){ // "20000", "5399", "5402"
				tx.rollback();		
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.errorFormato");
				return "nuevo";		
				
			} else {
				correcto=false;
			}
			
			if (correcto){					
				tx.commit();
				
				request.setAttribute("parametrosArray", resultado);
				request.setAttribute("modal","");							
				return "exitoParametros";			
				
			} else {
				tx.rollback();		
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
				return "mostrarVentana";
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
	 * Funcion que realiza una llamada a la PL PKG_SIGA_CARGOS.DEVOLUCIONES
	 * @param institucion
	 * @param path
	 * @param fichero
	 * @param idioma
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	protected String[] actualizacionTablasDevoluciones(String institucion, String path, String fichero, String idioma, String usuario) throws ClsExceptions {	
		String resultado[] = new String[3];
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
		    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.DEVOLUCIONES(?,?,?,?,?,?,?,?)}", 3, param_in);
		    	codretorno = resultado[0];
			}
			
		} catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en actualizacionTableroDevoluciones.  Proc:PKG_SIGA_CARGOS.DEVOLUCIONES "+resultado[1]);
		}
		
		return resultado;
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
	 * @param listaFacturas = idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...
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
	    	param_in[1] = listaFacturas; // idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...
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