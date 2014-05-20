/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 21-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.ArrayList;
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
import com.siga.general.EjecucionPLs;
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
				
			} else if (accion.equalsIgnoreCase("cambiarFechasFichero")){
				mapDestino = cambiarFechasFichero(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("informeRemesa")){
				mapDestino = informeRemesa(miForm, request);
				
			} else if (accion.equalsIgnoreCase("buscarInit")){
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscar(mapping, miForm, request, response);
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
			/*	String select = " SELECT "+FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.C_IDFACTURA+
			" FROM "+FacFacturaBean.T_NOMBRETABLA+
			" WHERE "+FacFacturaBean.C_IDINSTITUCION+"="+usr.getLocation()+
			" AND f_siga_estadosfactura ("+FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.C_IDFACTURA+") = 3";
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm(usuario);
			
			Vector v = facturaAdm.selectGenerico(select);
			*/
			
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
			
			Vector ocultos 			= new Vector();		
			ocultos 				= (Vector)form.getDatosTablaOcultos(0);			
			//idDisqueteCargos 		= (String)ocultos.elementAt(0);			
			nombreFichero 			= (String)ocultos.elementAt(1);	
			idInstitucion			= this.getIDInstitucion(request).toString();			
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		//response.setHeader("Content-disposition","attachment; filename=" + nombreFichero);
		//return sPrefijoDownload + pathFichero;
		
		//creamos la lista de ficheros adjuntos
    	List<File> lista = new ArrayList<File>();    	
    	File directorioFicheros= new File(pathFichero + File.separator + idInstitucion);
    	
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
    	
    	pathFichero += File.separator + idInstitucion + File.separator + nombreFichero+".zip";
    	File filezip = SIGAServicesHelper.doZip(pathFichero, lista);		
		
		request.setAttribute("nombreFichero", nombreFichero+".zip");
		request.setAttribute("rutaFichero", pathFichero);
		request.setAttribute("borrarFichero", "true");		
		
		return "descargaFichero";
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
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String keyPath 				= "facturacion.directorioBancosOracle";			
		String pathFichero			= "";		
		String idInstitucion		= "";
		UserTransaction tx			= null;		
		String resultadoFinal[] = new String[1];
		
		try{	
			tx = usr.getTransactionPesada(); 
			tx.begin();
			Integer usuario = this.getUserName(request);						
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			pathFichero 			= p.returnProperty(keyPath);
			idInstitucion			= this.getIDInstitucion(request).toString();
			
			String sBarra 			= "";
			if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
			if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			
			pathFichero += sBarra + idInstitucion;
			
			FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;
			String fechaEntrega = form.getFechaEntrega();
			
			String fechaUnica="", fechaRecibosPrimeros="", fechaRecibosRecurrentes="", fechaRecibosCOR1="", fechaRecibosB2B="";
			if (form.getFechaTipoUnica().equals("1")) {
				fechaUnica = form.getFechaUnica();
			} else { 
				fechaRecibosPrimeros = form.getFechaFRST();
				fechaRecibosRecurrentes = form.getFechaRCUR();
				fechaRecibosCOR1 = form.getFechaCOR1();
				fechaRecibosB2B = form.getFechaB2B();
			}			
			
			// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales
			FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));	
			if (!adm.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaUnica, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, form.getFechaTipoUnica(), null)) {
				throw new SIGAException("facturacion.ficheroBancarioPagos.errorMandatos.mensajeFechas");
			}				
			
			// Se envían a banco para su renegociación
			Object[] param_in_banco = new Object[12];
			param_in_banco[0] = idInstitucion;
			param_in_banco[1] = "";
			param_in_banco[2] = "";
			
			if (fechaEntrega != null && !fechaEntrega.equals("") && fechaEntrega.length()==10) {
				try {
					fechaEntrega = fechaEntrega.substring(6,10) + fechaEntrega.substring(3,5) + fechaEntrega.substring(0,2); // AAAAMMDD
				} catch (Exception e){
					fechaEntrega = "";
				}
			}
			param_in_banco[3] = fechaEntrega;		
			
			if (fechaUnica != null && !fechaUnica.equals("") && fechaUnica.length()==10) {
				try { 
					fechaUnica = fechaUnica.substring(6,10) + fechaUnica.substring(3,5) + fechaUnica.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaUnica = "";
				}
			}
			param_in_banco[4] = fechaUnica;			
			
			if (fechaRecibosPrimeros != null && !fechaRecibosPrimeros.equals("") && fechaRecibosPrimeros.length()==10) {
				try { 
					fechaRecibosPrimeros = fechaRecibosPrimeros.substring(6,10) + fechaRecibosPrimeros.substring(3,5) + fechaRecibosPrimeros.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosPrimeros = "";
				}		
			}
			param_in_banco[5] = fechaRecibosPrimeros;
			
			if (fechaRecibosRecurrentes != null && !fechaRecibosRecurrentes.equals("") && fechaRecibosRecurrentes.length()==10) {
				try { 
					fechaRecibosRecurrentes = fechaRecibosRecurrentes.substring(6,10) + fechaRecibosRecurrentes.substring(3,5) + fechaRecibosRecurrentes.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosRecurrentes = "";
				}
			}
			param_in_banco[6] = fechaRecibosRecurrentes;
			
			if (fechaRecibosCOR1 != null && !fechaRecibosCOR1.equals("") && fechaRecibosCOR1.length()==10) {
				try { 
					fechaRecibosCOR1 = fechaRecibosCOR1.substring(6,10) + fechaRecibosCOR1.substring(3,5) + fechaRecibosCOR1.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosCOR1 = "";
				}	
			}
			param_in_banco[7] = fechaRecibosCOR1;
			
			if (fechaRecibosB2B != null && !fechaRecibosB2B.equals("") && fechaRecibosB2B.length()==10) {
				try { 
					fechaRecibosB2B = fechaRecibosB2B.substring(6,10) + fechaRecibosB2B.substring(3,5) + fechaRecibosB2B.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosB2B = "";
				}		
			}
			param_in_banco[8] = fechaRecibosB2B;
			
			param_in_banco[9] = pathFichero;
			param_in_banco[10] = usuario.toString();
			param_in_banco[11] =this.getUserBean(request).getLanguage();
						
			String resultado[] = new String[3];
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
			String codretorno = resultado[1];
			
			// Da error cuando no obtiene el mandato (5412) 
			if (codretorno.equals("5412")) {
				throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.sinMandato");
				
			} else {
				// Da error cuando el mandato no viene firmado (5413) 
				if (codretorno.equals("5413")) {
					throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.sinFirma");
					
				} else {
					// Da error cuando la direccion de facturacion del acreedor no existe (5414) 
					if (codretorno.equals("5414")) {
						throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.direccionFacturacionAcreedor");
						
					} else {
						// Da error cuando la direccion de facturacion del deudor no existe (5415) 
						if (codretorno.equals("5415")) {
							throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.direccionFacturacionDeudor");
							
						} else {
							if (!codretorno.equals("0")){
								throw new SIGAException("censo.fichaCliente.bancos.mandatos.error.generacionFicheros");
							}							
						}						
					}					
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
	
	protected String informeRemesa (MasterForm formulario, HttpServletRequest request) throws SIGAException 
	{
		FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;

		Vector ocultos 			= (Vector)form.getDatosTablaOcultos(0);			
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
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();							
			
			/** CR7 - Control de fechas de presentación y cargo en ficheros SEPA **/
			GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
			String habilesUnicaCargos = admParametros.getValor(idInstitucion, "FAC", "DIAS_HABILES_UNICA_CARGOS", "7");
			String habilesPrimerosRecibos = admParametros.getValor(idInstitucion, "FAC", "DIAS_HABILES_PRIMEROS_RECIBOS", "7");
			String habilesRecibosRecurrentes = admParametros.getValor(idInstitucion, "FAC", "DIAS_HABILES_RECIBOS_RECURRENTES", "4");
			String habilesRecibosCOR1 = admParametros.getValor(idInstitucion, "FAC", "DIAS_HABILES_RECIBOS_COR1", "3");
			String habilesRecibosB2B = admParametros.getValor(idInstitucion, "FAC", "DIAS_HABILES_RECIBOS_B2B", "3");
			
			String fechaActual = GstDate.getHoyJsp(); // Obtengo la fecha actual
			// String fechaPresentacion = EjecucionPLs.ejecutarSumarDiasHabiles(fechaActual, "1"); // Fecha actual + 1
			String fechaPresentacion = fechaActual; // INC_12343_SIGA y INC_12345_SIGA solicitan la fecha actual como permitida 
			
			String fechaUnicaCargos = EjecucionPLs.ejecutarSumarDiasHabiles(fechaPresentacion,habilesUnicaCargos);
			String fechaPrimerosRecibos = EjecucionPLs.ejecutarSumarDiasHabiles(fechaPresentacion,habilesPrimerosRecibos);
			String fechaRecibosRecurrentes = EjecucionPLs.ejecutarSumarDiasHabiles(fechaPresentacion,habilesRecibosRecurrentes);
			String fechaRecibosCOR1 = EjecucionPLs.ejecutarSumarDiasHabiles(fechaPresentacion,habilesRecibosCOR1);
			String fechaRecibosB2B = EjecucionPLs.ejecutarSumarDiasHabiles(fechaPresentacion,habilesRecibosB2B);
			
			request.setAttribute("radio","1"); // El radio seleccionado será Unica
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaUnicaCargos",fechaUnicaCargos);
			request.setAttribute("fechaPrimerosRecibos",fechaPrimerosRecibos);
			request.setAttribute("fechaRecibosRecurrentes",fechaRecibosRecurrentes);
			request.setAttribute("fechaRecibosCOR1",fechaRecibosCOR1);
			request.setAttribute("fechaRecibosB2B",fechaRecibosB2B);
			
			request.setAttribute("habilesUnicaCargos",habilesUnicaCargos);
			request.setAttribute("habilesPrimerosRecibos",habilesPrimerosRecibos);
			request.setAttribute("habilesRecibosRecurrentes",habilesRecibosRecurrentes);
			request.setAttribute("habilesRecibosCOR1",habilesRecibosCOR1);
			request.setAttribute("habilesRecibosB2B",habilesRecibosB2B);			
			
			String idDisqueteCargo = form.getIdDisqueteCargo();
			String nombreFichero = form.getNombreFichero();
			if (idDisqueteCargo==null || idDisqueteCargo.equals("") || nombreFichero == null || nombreFichero.equals("")) {
				Vector ocultos = (Vector)form.getDatosTablaOcultos(0);			
				idDisqueteCargo = (String)ocultos.elementAt(0);
				nombreFichero = (String)ocultos.elementAt(1);
			}
			
			request.setAttribute("idDisqueteCargo", idDisqueteCargo);
			request.setAttribute("nombreFichero", nombreFichero);
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return result;
	}
	
	/**
	 * Funcion que cambia las fechas de un fichero
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String cambiarFechasFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {	
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		String keyPath 				= "facturacion.directorioBancosOracle";			
		String pathFichero			= "";		
		String idInstitucion		= "";
		UserTransaction tx			= null;
		
		try{	
			tx = usr.getTransaction(); 
			tx.begin();
			Integer usuario = this.getUserName(request);						
			
		    ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			pathFichero = p.returnProperty(keyPath);
			idInstitucion = this.getIDInstitucion(request).toString();
			
			String sBarra = "";
			if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
			if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			
			pathFichero += sBarra + idInstitucion;
									
			FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;
			String fechaEntrega = form.getFechaEntrega();
			
			String fechaUnica="", fechaRecibosPrimeros="", fechaRecibosRecurrentes="", fechaRecibosCOR1="", fechaRecibosB2B="";
			if (form.getFechaTipoUnica().equals("1")) {
				fechaUnica = form.getFechaUnica();
			} else { 
				fechaRecibosPrimeros = form.getFechaFRST();
				fechaRecibosRecurrentes = form.getFechaRCUR();
				fechaRecibosCOR1 = form.getFechaCOR1();
				fechaRecibosB2B = form.getFechaB2B();
			}
			
			// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales
			FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));	
			if (!adm.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaUnica, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, form.getFechaTipoUnica(), null)) {
				throw new SIGAException("facturacion.ficheroBancarioPagos.errorMandatos.mensajeFechas");
			}			
			
			// Se envían los parametros para modificar las fechas del fichero
			Object[] param_in_banco = new Object[11];
			param_in_banco[0] = idInstitucion;
			param_in_banco[1] = form.getIdDisqueteCargo();
			
			if (fechaEntrega != null && !fechaEntrega.equals("") && fechaEntrega.length()==10) {
				try {
					fechaEntrega = fechaEntrega.substring(6,10) + fechaEntrega.substring(3,5) + fechaEntrega.substring(0,2); // AAAAMMDD
				} catch (Exception e){
					fechaEntrega = "";
				}
			}
			param_in_banco[2] = fechaEntrega;
			
			if (fechaUnica != null && !fechaUnica.equals("") && fechaUnica.length()==10) {
				try { 
					fechaUnica = fechaUnica.substring(6,10) + fechaUnica.substring(3,5) + fechaUnica.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaUnica = "";
				}
			}
			param_in_banco[3] = fechaUnica;
			
			if (fechaRecibosPrimeros != null && !fechaRecibosPrimeros.equals("") && fechaRecibosPrimeros.length()==10) {
				try { 
					fechaRecibosPrimeros = fechaRecibosPrimeros.substring(6,10) + fechaRecibosPrimeros.substring(3,5) + fechaRecibosPrimeros.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosPrimeros = "";
				}		
			}
			param_in_banco[4] = fechaRecibosPrimeros;
			
			if (fechaRecibosRecurrentes != null && !fechaRecibosRecurrentes.equals("") && fechaRecibosRecurrentes.length()==10) {
				try { 
					fechaRecibosRecurrentes = fechaRecibosRecurrentes.substring(6,10) + fechaRecibosRecurrentes.substring(3,5) + fechaRecibosRecurrentes.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosRecurrentes = "";
				}
			}
			param_in_banco[5] = fechaRecibosRecurrentes;
			
			if (fechaRecibosCOR1 != null && !fechaRecibosCOR1.equals("") && fechaRecibosCOR1.length()==10) {
				try { 
					fechaRecibosCOR1 = fechaRecibosCOR1.substring(6,10) + fechaRecibosCOR1.substring(3,5) + fechaRecibosCOR1.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosCOR1 = "";
				}	
			}
			param_in_banco[6] = fechaRecibosCOR1;
			
			if (fechaRecibosB2B != null && !fechaRecibosB2B.equals("") && fechaRecibosB2B.length()==10) {
				try { 
					fechaRecibosB2B = fechaRecibosB2B.substring(6,10) + fechaRecibosB2B.substring(3,5) + fechaRecibosB2B.substring(0,2); // AAAAMMDD 
				} catch (Exception e){
					fechaRecibosB2B = "";
				}		
			}
			param_in_banco[7] = fechaRecibosB2B;
			
			param_in_banco[8] = usuario.toString();
			param_in_banco[9] = pathFichero;			
			param_in_banco[10] = form.getNombreFichero();
			
			String resultado[] = new String[2];
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.CambiarFechasPresentacion(?,?,?,?,?,?,?,?,?,?,?,?,?)}", 2, param_in_banco);			
			if (resultado == null || !resultado[0].equals("0")) {
				throw new SIGAException("messages.updated.error");
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

}