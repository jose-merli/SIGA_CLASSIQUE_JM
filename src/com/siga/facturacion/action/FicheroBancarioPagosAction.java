/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 21-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.autogen.model.CenBancos;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.beans.FacDisqueteCargosAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.form.FicheroBancarioPagosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * Clase action para Descargar los ficheros bancarios.<br/>
 * Gestiona abrir y descargar Ficheros
 * @version david.sanchezp: cambios para pedir y tener en cuenta la fecha de cargo.
 */
public class FicheroBancarioPagosAction extends MasterAction{
	
	/** 	
	 *  *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("")){
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);	
					
			} else if (accion.equalsIgnoreCase("abrir")){
				miForm.reset(mapping,request);
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);					
				
			} else if (accion.equalsIgnoreCase("download")){
				mapDestino = download(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("generarFichero")){
				mapDestino = generarFichero(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("regenerarFicherosDisqueteCargos")){
				mapDestino = regenerarFicherosDisqueteCargos(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("informeRemesa")){
				mapDestino = informeRemesa(miForm, request);
				
			} else if (accion.equalsIgnoreCase("buscarInit")){
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscar(mapping, miForm, request, response);
				
			}else if (accion.equalsIgnoreCase("getAjaxFechasFicheroBancario")){
				getAjaxFechasFicheroBancario (mapping, miForm, request, response);
				return null;
					
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	
			{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
			
			
		}catch (SIGAException es) { 
			throw es; 
		}catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		} 
		
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="abrir";

		try{
			FicheroBancarioPagosForm form 	= (FicheroBancarioPagosForm)formulario;
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();			
			form.setIdInstitucion(idInstitucion);
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancosConCuentasBancarias(new Integer(idInstitucion));
			request.setAttribute("listaBancos", bancosList);
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return result;
	}		
	
	/** 
	 *  Funcion que atiende la accion abrir. Muestra todas las facturas programadas cuyas Fecha Real de Generación es null 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();			
			FicheroBancarioPagosForm form 	= (FicheroBancarioPagosForm)formulario;
			form.setIdInstitucion(idInstitucion);
			
			HashMap databackup=new HashMap();				
			if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
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
		  	    databackup=new HashMap();
					
		  	    FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));			
		  	    PaginadorCaseSensitive ficheros = adm.getDatosFichero(form);
				
				databackup.put("paginador", ficheros);
				if (ficheros!=null){ 
					Vector datos = ficheros.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				} 
			}		
			
	
			/*
			FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));			
			Vector vDatos = adm.getDatosFichero(idInstitucion);
			request.getSession().setAttribute("DATABACKUP", vDatos);
			*/	
			
			//Calculo si necesitara la fecha de cargo antes de generar los ficheros:	
			// ACG: no hacemos esta consulta que ralentiza enormemente la ejecucion de esta funcion. De esta forma en la jsp se
			// solicitará siempre la fecha de cargo con lo que en el fichero generado el campo fecha aparecerá siempre relleno
			String tieneFechaCargo = "SI";
			
			/*	
			 String tieneFechaCargo = "NO";
			if (v!=null && v.size()>0)
				tieneFechaCargo = "SI";
	    */		
			request.setAttribute("TIENEFECHACARGO",tieneFechaCargo);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		return "listado";
	}
	
	/** 
	 *  Funcion que atiende la accion download. Descarga los ficheros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 * @throws ClsExceptions 
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		
		//	String keyFichero 		= "facturacion.directorioBancosJava";				
		String directorioFisico = "facturacion.directorioFisicoPagosBancosJava";
		String directorio 		= "facturacion.directorioPagosBancosJava";
		String nombreFichero 	= "";
		String pathFichero		= "";
		//String idDisqueteCargos	= "";
		String idInstitucion	= "";
		
		try{		
			//Integer usuario = this.getUserName(request);
			
			FicheroBancarioPagosForm form 		= (FicheroBancarioPagosForm)formulario;
			//FacDisqueteCargosAdm adm 			= new FacDisqueteCargosAdm(usuario);
			//FacDisqueteCargosBean beanDisquete	= new FacDisqueteCargosBean();
			//FacFacturaAdm admFactura = new FacFacturaAdm(usuario);
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 	= new ReadProperties ("SIGA.properties");
			pathFichero 		= p.returnProperty(directorioFisico) + p.returnProperty(directorio);			
			//String nombreFichero 	= p.returnProperty(keyFichero);						
			
			Vector ocultos = form.getDatosTablaOcultos(0);			
			//idDisqueteCargos 		= (String)ocultos.elementAt(0);			
			nombreFichero 			= (String)ocultos.elementAt(1);	
			idInstitucion			= this.getIDInstitucion(request).toString();	
			pathFichero += File.separator + idInstitucion;
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		//response.setHeader("Content-disposition","attachment; filename=" + nombreFichero);
		//return sPrefijoDownload + pathFichero;
		
		//creamos la lista de ficheros adjuntos
    	List<File> lista = new ArrayList<File>();    	
    	File directorioFicheros= new File(pathFichero);
    	
    	//Se buscan todos los ficheros que coincidan con el nombre del fichero
    	if(directorioFicheros.exists()){
	    	File[] ficheros = directorioFicheros.listFiles();
	    	String nombreFicheroListadoSinExtension, nombreFicheroGeneradoSinExtension;
	    	for (int x=0; x<ficheros.length; x++){
				nombreFicheroListadoSinExtension = (ficheros[x].getName().indexOf(".") > 0) ? ficheros[x].getName().substring(0, ficheros[x].getName().indexOf(".")) 
																							: ficheros[x].getName();
				nombreFicheroGeneradoSinExtension = (nombreFichero.indexOf(".") > 0) ? nombreFichero.substring(0, nombreFichero.indexOf(".")) 
																					 : nombreFichero;
	    		if(nombreFicheroGeneradoSinExtension.equalsIgnoreCase(nombreFicheroListadoSinExtension)){
	    			lista.add(ficheros[x]);
	    		}	    		
	    	}
    	} 
    	 
    	if(lista.size() <= 0){
    		throw new SIGAException("No se ha encontrado el fichero generado");
    	}
    	
    	// devolviendo el fichero: (ZIP si hay varios)
    	if (lista.size() == 1) {    		
    		request.setAttribute("nombreFichero", lista.get(0).getName());
    		pathFichero += File.separator + lista.get(0).getName();
    	} else {
	    	pathFichero += File.separator + nombreFichero + ".zip";
	    	File filezip = SIGAServicesHelper.doZip(pathFichero, lista);		
			request.setAttribute("nombreFichero", nombreFichero+".zip");
			request.setAttribute("borrarFichero", "true");
    	}
		request.setAttribute("rutaFichero", pathFichero);				
		
		return "descargaFichero";
	}	
	
	public static ArrayList<String> prepararParametrosParaGenerarFichero(FicheroBancarioPagosForm form, UsrBean usr) throws ClsExceptions, SIGAException {
		ArrayList<String> param_in_banco = new ArrayList();
		
		String idInstitucion = usr.getLocation();
		
		String keyPath = "facturacion.directorioBancosOracle";			
	    ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPath);
		String sBarra 			= "";
		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";
		// pathFichero puede contener o bien una ruta absoluta (sBarra estara relleno) o bien el nombre de un Directory (sBarra estara vacio)
		pathFichero += sBarra + idInstitucion;
		
		String fechaEntrega = "";
		String fechaRecibosPrimeros = "";
		String fechaRecibosRecurrentes = "";
		String fechaRecibosCOR1 = "";
		String fechaRecibosB2B = "";
		if (form != null) {
			fechaEntrega = form.getFechaEntrega();
			fechaRecibosPrimeros = form.getFechaFRST();
			fechaRecibosRecurrentes = form.getFechaRCUR();
			fechaRecibosCOR1 = form.getFechaCOR1();
			fechaRecibosB2B = form.getFechaB2B();
			
			// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales
			FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(usr);	
			if (!adm.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, null)) {
				throw new SIGAException("facturacion.ficheroBancarioPagos.errorMandatos.mensajeFechas");
			}				
		}
		
		// Se envían a banco para su renegociación
		param_in_banco.add(idInstitucion);
		
		if (fechaEntrega != null && !fechaEntrega.equals("") && fechaEntrega.length()==10) {
			try {
				fechaEntrega = fechaEntrega.substring(6,10) + fechaEntrega.substring(3,5) + fechaEntrega.substring(0,2); // AAAAMMDD
			} catch (Exception e){
				fechaEntrega = "";
			}
		}
		param_in_banco.add(fechaEntrega);		
		
		if (fechaRecibosPrimeros != null && !fechaRecibosPrimeros.equals("") && fechaRecibosPrimeros.length()==10) {
			try { 
				fechaRecibosPrimeros = fechaRecibosPrimeros.substring(6,10) + fechaRecibosPrimeros.substring(3,5) + fechaRecibosPrimeros.substring(0,2); // AAAAMMDD 
			} catch (Exception e){
				fechaRecibosPrimeros = "";
			}		
		}
		param_in_banco.add(fechaRecibosPrimeros);
		
		if (fechaRecibosRecurrentes != null && !fechaRecibosRecurrentes.equals("") && fechaRecibosRecurrentes.length()==10) {
			try { 
				fechaRecibosRecurrentes = fechaRecibosRecurrentes.substring(6,10) + fechaRecibosRecurrentes.substring(3,5) + fechaRecibosRecurrentes.substring(0,2); // AAAAMMDD 
			} catch (Exception e){
				fechaRecibosRecurrentes = "";
			}
		}
		param_in_banco.add(fechaRecibosRecurrentes);
		
		if (fechaRecibosCOR1 != null && !fechaRecibosCOR1.equals("") && fechaRecibosCOR1.length()==10) {
			try { 
				fechaRecibosCOR1 = fechaRecibosCOR1.substring(6,10) + fechaRecibosCOR1.substring(3,5) + fechaRecibosCOR1.substring(0,2); // AAAAMMDD 
			} catch (Exception e){
				fechaRecibosCOR1 = "";
			}	
		}
		param_in_banco.add(fechaRecibosCOR1);
		
		if (fechaRecibosB2B != null && !fechaRecibosB2B.equals("") && fechaRecibosB2B.length()==10) {
			try { 
				fechaRecibosB2B = fechaRecibosB2B.substring(6,10) + fechaRecibosB2B.substring(3,5) + fechaRecibosB2B.substring(0,2); // AAAAMMDD 
			} catch (Exception e){
				fechaRecibosB2B = "";
			}		
		}
		param_in_banco.add(fechaRecibosB2B);
		
		param_in_banco.add(pathFichero);
		param_in_banco.add(usr.getUserName());
		param_in_banco.add(usr.getLanguage());
		
		return param_in_banco;
	}
	/** 
	 *  Funcion que atiende la accion generarFichero. Genera los ficheros de Renegociación
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String generarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = this.getUserBean(request);
		UserTransaction tx = null;		
		String resultadoFinal[] = new String[1];
		FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;
		
		try {
			// preparando llamada al paquete para la generacion del fichero
			ArrayList<String> param_in = prepararParametrosParaGenerarFichero(form, usr);
			param_in.add(""); //p_Idseriefacturacion
			param_in.add(""); //p_Idprogramacion
			Object[] param_in_banco = param_in.toArray();
			String[] resultado = new String[3];
			
			// comenzando transaccion
			tx = usr.getTransactionPesada(); 
			tx.begin();
			
			// ejecutando el PL que generara los ficheros
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
			
			String[] codigosErrorFormato = {"5412", "5413", "5414", "5415", "5416", "5417", "5418", "5421", "5422"};
			if(Arrays.asList(codigosErrorFormato).contains(resultado[1])){
				throw new SIGAException(resultado[2]);
			} else {
				if (!resultado[1].equals("0")){
					throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.generacionFicheros");
				}							
			}	

			tx.commit();
			resultadoFinal[0] = resultado[0];
			
		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.general.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx);  	  	
		}
				
		request.setAttribute("parametrosArray", resultadoFinal);
		request.setAttribute("modal","");
				 
		return "exitoParametros";
	}
	
	/**
	 * Funcion que regenera los ficheros de un disquete de cargos
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String regenerarFicherosDisqueteCargos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {	
		UsrBean usr = this.getUserBean(request);
		UserTransaction tx = null;
		FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;
		
		String keyPath1 = "facturacion.directorioFisicoAbonosBancosJava";			
		String keyPath2 = "facturacion.directorioPagosBancosJava";			
		String pathFichero = "";		
		String idInstitucion = usr.getLocation();
		String idDisqueteCargos = form.getIdDisqueteCargo();
		
		try{
			// preparando llamada al paquete para la generacion del fichero
			ArrayList<String> param_in = prepararParametrosParaGenerarFichero(form, usr);
			param_in.add(idDisqueteCargos);
			Object[] param_in_banco = param_in.toArray();
			String[] resultado = new String[3];
			
			// obteniendo la ruta de los ficheros de pagos
		    ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			pathFichero = p.returnProperty(keyPath1);
			
			String sBarra = "";
			if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
			if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			
			pathFichero += sBarra + p.returnProperty(keyPath2) + sBarra + idInstitucion;

			// comenzando transaccion
			tx = usr.getTransactionPesada(); 
			tx.begin();
			
	    	// borrando todos los ficheros que contengan el identificador del disquete de cargos
			File directorioFicheros = new File(pathFichero);
	    	if (directorioFicheros.exists() && directorioFicheros.isDirectory()){
		    	File[] ficheros = directorioFicheros.listFiles();
		    	for (int x=0; x<ficheros.length; x++){
		    		String nombreFichero = ficheros[x].getName();
		    		if (nombreFichero.startsWith(idDisqueteCargos + ".")) {
		    			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						nombreFichero = sdf.format(new Date()) + "_" + nombreFichero;
		    			
		    			File newFile = new File(directorioFicheros, nombreFichero);
		    			ficheros[x].renameTo(newFile);
		    			//ficheros[x].delete(); JPT (06/05/2015): No se borran por miedo, prefiero generar una copia
		    		}	    		
		    	}
	    	} 
			
			// ejecutando el PL que generara los ficheros
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.Regenerar_Presentacion(?,?,?,?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);	
		
			String[] codigosErrorFormato = {"5412", "5413", "5414", "5415", "5416", "5417", "5418", "5421", "5422"};
			if(Arrays.asList(codigosErrorFormato).contains(resultado[1])){
				throw new SIGAException(resultado[2]);
			} else {
				if (!resultado[1].equals("0")){
					throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.generacionFicheros");
				}							
			}
			
			tx.commit();
			
		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.updated.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);			
						
		} catch (Exception e) { 
			throwExcp("messages.updated.error", new String[] {"modulo.facturacion"}, e, tx);  	  	
		}
		
		return exitoModal("messages.updated.success", request);
	}
	
	protected String informeRemesa (MasterForm formulario, HttpServletRequest request) throws SIGAException 
	{
		FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;

		Vector ocultos 			= form.getDatosTablaOcultos(0);			
		String idDisqueteCargo 	= (String)ocultos.elementAt(0);	
		String idInstitucion	= this.getIDInstitucion(request).toString();			

		FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));
		Hashtable h = adm.getInformeRemesa(idInstitucion, idDisqueteCargo);
		if (h != null) {
			
			request.setAttribute("datosImpreso", h);
			
//			request.setAttribute("importeTotal",         UtilidadesHash.getString(h,"importeTotal"));
//			request.setAttribute("numOrdenes",           UtilidadesHash.getString(h,"numOrdenes"));
//			request.setAttribute("numRegistros",         UtilidadesHash.getString(h,"numRegistros"));
//			request.setAttribute("fechaCreacionFichero", UtilidadesHash.getString(h,"fechaCreacionFichero"));
//			request.setAttribute("fechaEmisionOrdenes",  UtilidadesHash.getString(h,"fechaEmisionOrdenes"));
//			request.setAttribute("nombreInstitucion",    UtilidadesHash.getString(h,"nombreInstitucion"));
//			request.setAttribute("codigoOrdenante",      UtilidadesHash.getString(h,"codigoOrdenante"));
//			request.setAttribute("cuentaAbono",          UtilidadesHash.getString(h,"cuentaAbono"));
		}
		
		return "informeRemesa"; 
	}		
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="editar";

		try{
			FicheroBancarioPagosForm form 	= (FicheroBancarioPagosForm)formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion = user.getLocation();							
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			String fechaPresentacion = GstDate.getHoyJsp(); // Obtengo la fecha actual
			
			FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(user);	
			Hashtable<String,String> fechas = admDisqueteCargos.getFechasCargo (idInstitucion, fechaPresentacion);
			
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			
			request.setAttribute("fechaPrimerosRecibos",fechas.get("fechaPrimerosRecibos").toString());
			request.setAttribute("fechaRecibosRecurrentes",fechas.get("fechaRecibosRecurrentes").toString());
			request.setAttribute("fechaRecibosCOR1",fechas.get("fechaRecibosCOR1").toString());
			request.setAttribute("fechaRecibosB2B",fechas.get("fechaRecibosB2B").toString());
			
			request.setAttribute("habilesPrimerosRecibos",fechas.get("habilesPrimerosRecibos").toString());
			request.setAttribute("habilesRecibosRecurrentes",fechas.get("habilesRecibosRecurrentes").toString());
			request.setAttribute("habilesRecibosCOR1",fechas.get("habilesRecibosCOR1").toString());
			request.setAttribute("habilesRecibosB2B",fechas.get("habilesRecibosB2B").toString());
			
			request.setAttribute("accionInit","FAC_DisqueteCargos");
			
			String idDisqueteCargo = form.getIdDisqueteCargo();
			String nombreFichero = form.getNombreFichero();
			if (idDisqueteCargo==null || idDisqueteCargo.equals("") || nombreFichero == null || nombreFichero.equals("")) {
				Vector ocultos = form.getDatosTablaOcultos(0);			
				idDisqueteCargo = (String)ocultos.elementAt(0);
				nombreFichero = (String)ocultos.elementAt(1);
			}
			
			request.setAttribute("idDisqueteCargo", idDisqueteCargo);
			request.setAttribute("nombreFichero", nombreFichero);
			
			// obtengo el parametro general 'SEPA_TIPO_FICHEROS'
			GenParametrosAdm admParametros = new GenParametrosAdm(user);
			String tiposFicherosAdeudo = admParametros.getValor(idInstitucion, "FAC", "SEPA_TIPO_FICHEROS", "0"); // Por defecto solo n1914
			request.setAttribute("tiposFicherosAdeudo", tiposFicherosAdeudo);
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return result;
	}
	
	/**
	 * Recarga las fechas del fichero bancario cuando se carga la de presentación
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void getAjaxFechasFicheroBancario(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, Exception {
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idInstitucion = user.getLocation();

		FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(user);	
		Hashtable<String,String> fechas = admDisqueteCargos.getFechasCargo(idInstitucion, (String)request.getParameter("fechaPresentacion"));

		JSONObject json = new JSONObject();	
		
		json.put("fechaPrimerosRecibos", fechas.get("fechaPrimerosRecibos").toString());
		json.put("fechaRecibosRecurrentes", fechas.get("fechaRecibosRecurrentes").toString());
		json.put("fechaRecibosCOR1", fechas.get("fechaRecibosCOR1").toString());
		json.put("fechaRecibosB2B", fechas.get("fechaRecibosB2B").toString());
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 
	}	
}