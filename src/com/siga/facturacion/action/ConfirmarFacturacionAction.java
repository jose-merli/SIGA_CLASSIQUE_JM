/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 14-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.FacDisqueteCargosAdm;
import com.siga.beans.FacEstadoConfirmFactAdm;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.form.ConfirmarFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;


/**
 * Clase action para confirmar la facturacion.<br/>
 * Gestiona abrir y confirmar Facturas
 * @version david.sanchezp: cambios para pedir y tener en cuenta la fecha de cargo.
 */
public class ConfirmarFacturacionAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("abrirVolver")){
					request.setAttribute("volver","s");			
					ConfirmarFacturacionForm confirmarFacturacionForm = (ConfirmarFacturacionForm) miForm;
					request.setAttribute("estadoConfirmacion", confirmarFacturacionForm.getEstadoConfirmacion());
					mapDestino = abrir(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("descargaLog")){
					mapDestino = descargaLog(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("descargarInformeGeneracion")){
					mapDestino = descargarInformeGeneracion(mapping, miForm, request, response);					
				} else if (accion.equalsIgnoreCase("archivarFactura")){
					mapDestino = archivarFactura(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("enviar")){
					mapDestino = enviarFacturas(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("consultarfactura")){
					mapDestino = consultarFacturas(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("editarFechas")){
					mapDestino = editarFechas(mapping, miForm, request, response);					
				}else if (accion.equalsIgnoreCase("buscarInit")){
					borrarPaginador(request, paginador);
					mapDestino = buscarPor(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscarPor")){
					mapDestino = buscarPor(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("borrar")){
					mapDestino = borrar(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("generarNuevoFicheroAdeudo")){
					mapDestino = generarNuevoFicheroAdeudo (mapping, miForm, request, response);	
				}else if (accion.equalsIgnoreCase("getAjaxFechasFicheroBancario")){
					getAjaxFechasFicheroBancario (mapping, miForm, request, response);
					return null;				
				}else if (accion.equalsIgnoreCase("getAjaxFechasFicheroBancario")){
					getAjaxFechasFicheroBancario (mapping, miForm, request, response);
					return null;
				}else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 				
			{ 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}			
			
		 }catch (SIGAException es) { 
		    throw es; 
		 }catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		 } 
		 
		   return mapping.findForward(mapDestino);   	
	}
	
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		ConfirmarFacturacionForm confirmarFacturacionForm = (ConfirmarFacturacionForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		FacFacturacionProgramadaAdm facturacionProgramadaAdm = new FacFacturacionProgramadaAdm(usrBean);
		FacEstadoConfirmFactAdm admEstados = new FacEstadoConfirmFactAdm(usrBean);
		Hashtable htEstados = admEstados.getEstadosConfirmacionFacturacion(usrBean.getLanguage());
		request.setAttribute("ESTADOS",htEstados);
		
		try {
			HashMap databackup=getPaginador(request, this.paginador);
			if (databackup!=null){ 

				Paginador paginador = (Paginador)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					Vector datos=new Vector();
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
					request.setAttribute("datos", datos);
				}else{
					databackup.put("datos",new Vector());
					request.setAttribute("datos", new Vector());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, this.paginador, databackup);
				}	
			}else{	
				databackup=new HashMap();
				 Paginador paginador = facturacionProgramadaAdm.getProgramacioneFacturacionPaginador(confirmarFacturacionForm);
				if (paginador!=null&& paginador.getNumeroTotalRegistros()>0){
					int totalRegistros = paginador.getNumeroTotalRegistros();
					databackup.put("paginador",paginador);
					Vector datos = paginador.obtenerPagina(1);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("datos",datos);
					request.setAttribute("datos", datos);
					setPaginador(request, this.paginador, databackup);
				}else{
					databackup.put("datos",new Vector());
					request.setAttribute("datos", new Vector());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, this.paginador, databackup);
				} 	
			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "resultados";
	}
	
	/** 
	 *  Funcion que atiende la accion buscar.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ConfirmarFacturacionForm miForm = null;

		try {
			miForm = (ConfirmarFacturacionForm) formulario;

			//Debo calcular si necesita o no la fecha de cargo por cada registro enviado.
			Integer idInstitucion	= this.getIDInstitucion(request);			
			String fechaInicial 	= "01/01/2000";
			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FacEstadoConfirmFactAdm admEstados = new FacEstadoConfirmFactAdm(user);
			Hashtable htEstados = admEstados.getEstadosConfirmacionFacturacion(user.getLanguage());
			request.setAttribute("ESTADOS",htEstados);
			
			//Este select interno si devuelve un numero > 0 querra decir que debo pedir la fecha de cargo
			String selectInterno = "SELECT count(*) FROM "+FacFacturaBean.T_NOMBRETABLA+" fac "+
								   " WHERE fac."+FacFacturaBean.C_IDINSTITUCION+" = facProg."+FacFacturacionProgramadaBean.C_IDINSTITUCION+
								   " AND fac."+FacFacturaBean.C_IDSERIEFACTURACION+" = facProg."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+
								   " AND fac."+FacFacturaBean.C_IDPROGRAMACION+" = facProg."+FacFacturacionProgramadaBean.C_IDPROGRAMACION+
								   " AND fac."+FacFacturaBean.C_IDFORMAPAGO+"="+ClsConstants.TIPO_FORMAPAGO_FACTURA;
								   
			String select = "SELECT facProg.*, "+
							"("+selectInterno+") AS FECHACARGO,"+
							" serieFac."+FacSerieFacturacionBean.C_NOMBREABREVIADO+
							" FROM "+FacFacturacionProgramadaBean.T_NOMBRETABLA+" facProg, "+
								    FacSerieFacturacionBean.T_NOMBRETABLA+" serieFac"+
							" WHERE facProg." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion+
							//" and facProg."+FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NULL"+
							" and facProg."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL "+
							" and facProg."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION + ">= TO_DATE ('" + fechaInicial + "', 'DD/MM/YYYY')"+
							//JOIN:
							" AND facProg."+FacFacturacionProgramadaBean.C_IDINSTITUCION+"= serieFac."+FacSerieFacturacionBean.C_IDINSTITUCION+
							" AND facProg."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+"= serieFac."+FacSerieFacturacionBean.C_IDSERIEFACTURACION;
							
			// filtros CONFIRMACION
			if (!miForm.getEstadoConfirmacion().trim().equals("")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION+"="+miForm.getEstadoConfirmacion();
			}
			if (!miForm.getEstadoPDF().trim().equals("")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOPDF+"="+miForm.getEstadoPDF();
			}
			if (!miForm.getEstadoEnvios().trim().equals("")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOENVIO+"="+miForm.getEstadoEnvios();
			}
			if (miForm.getArchivadas()!=null && miForm.getArchivadas().trim().equals("1")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_ARCHIVARFACT+"='1'";
			} else {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_ARCHIVARFACT+"='0'";
			} 
			
			select += " ORDER BY "+FacFacturacionProgramadaBean.C_FECHAREALGENERACION+" DESC";
			
			Vector vDatos = adm.selectGenerico(select);
			request.getSession().setAttribute("DATABACKUP", vDatos);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "resultados";
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
		try {
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm) formulario;
			SimpleDateFormat formateo = new SimpleDateFormat("dd/MM/yyyy");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if(calendar.get(Calendar.MONTH)==Calendar.FEBRUARY && calendar.get(Calendar.DAY_OF_MONTH)==29){
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1);
				calendar.set(Calendar.DAY_OF_MONTH, 28);
			}else{
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1);	
			}
			String fechaAnioPasado = 	formateo.format(calendar.getTime());
			form.setFechaDesdePrevistaGeneracion(fechaAnioPasado);
			form.setFechaDesdeGeneracion(fechaAnioPasado);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "abrir";
	}

	
	/**
	 * Descarga el fichero zip de la carpeta temporal. Si no existe, muestra un mensaje al usuario indicándolo.
	 */
	protected String download(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {
		String sNombreFichero = "";
		String sRutaTemporal = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm) formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = user.getLocation();
			String idSerieFacturacion = (String) vOcultos.elementAt(0);
			String idProgramacion=(String) vOcultos.elementAt(1);

		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    sRutaTemporal = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava") +
			rp.returnProperty("facturacion.directorioFacturaPDFJava");

			sNombreFichero = idSerieFacturacion+"_"+idProgramacion;
			String sExtension = ".zip";
			sNombreFichero += sExtension;
			sRutaTemporal += File.separator + idInstitucion+ File.separator;

			//Control de que no exista el fichero a descargar:
			File ficheroZip = new File(sRutaTemporal+sNombreFichero);
			if (!ficheroZip.exists()){
				FacFacturaAdm facturas = new FacFacturaAdm(user);
				Vector resultado = facturas.getSerieFacturacionConfirmada(idInstitucion, idSerieFacturacion, idProgramacion);
				if(!resultado.isEmpty()){
					//Se accede por clave referenciada de la tabla que hace join por lo que todos los registro tienen el mismo estado de generacion de pdf. Cogemos por tanto el estado del priemr registro
					Hashtable hashPrimeraFacturaSerieProgramacion = (Hashtable)resultado.get(0); 
					String estadoPDF  = UtilidadesHash.getString(hashPrimeraFacturaSerieProgramacion,FacFacturacionProgramadaBean.C_IDESTADOPDF);
					if(estadoPDF.equals(FacEstadoConfirmFactBean.PDF_PROGRAMADA.toString())||estadoPDF.equals(FacEstadoConfirmFactBean.PDF_PROCESANDO.toString())){
						String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.PDFFacturaYaProgramada");
						return exitoRefresco(mensaje,request);
					}else{
						ClsLogging.writeFileLog("NO EXISTE EL ZIP, SE PASA A PROGRAMAR SU GENERACION",10);
						generarFacturaSolo(mapping, formulario, request, response);
						
						Hashtable datosProceso = new Hashtable();
						
						Hashtable datosHashtable = new Hashtable();
						datosHashtable.put("idInstitucion",idInstitucion);
						datosHashtable.put("idSerieFacturacion",idSerieFacturacion);
						datosHashtable.put("idProgramacion",idProgramacion);
						
						datosProceso.put(SIGASvlProcesoAutomaticoRapido.htNombreProceso,SIGASvlProcesoAutomaticoRapido.procesoIndividualConfirmacionFacturacion);
						datosProceso.put(SIGASvlProcesoAutomaticoRapido.htNombreDatosHashtable, datosHashtable);
						
						SIGASvlProcesoAutomaticoRapido.NotificarAhora(datosProceso);
						
						String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.generacionPDFFacturaProgramada");
						
						return exitoRefresco(mensaje,request);
					}
				}
				else{
					String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.descargaFacturas"); 			
					return exitoRefresco(mensaje,request);					
				}
			}

			request.setAttribute("nombreFichero", sNombreFichero);
			request.setAttribute("rutaFichero", sRutaTemporal+sNombreFichero);
		}catch(SIGAException e){	
			throw e;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error");
		}

		return "descargaFichero";
	}
	
	/** 
	 * Atiende la accion archivarFactura.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String archivarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mensaje = "";
		
		UserTransaction tx = null;	
		
		try {
			tx = this.getUserBean(request).getTransaction();
			
			ConfirmarFacturacionForm form 	= (ConfirmarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm admProgra = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			tx.begin();
			
			Hashtable hash = new Hashtable();
			hash.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
			hash.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion);
			hash.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,idProgramacion);
			Vector v = admProgra.selectByPK(hash);
			FacFacturacionProgramadaBean be = null;
			if (v!=null && v.size()>0) {
				be = (FacFacturacionProgramadaBean) v.get(0);
			}
			if (be!=null) {
				if (be.getArchivarFact().trim().equals("1")) {
					be.setArchivarFact("0");
					mensaje="messages.facturacion.desarchivar";
				} else {
					be.setArchivarFact("1");
					mensaje="messages.facturacion.archivar";
				}
				if (!admProgra.updateDirect(be)) {
					throw new ClsExceptions("Error al actualizar archviar: "+admProgra.getError());
				}
			}
			
			tx.commit();
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}

		return this.exitoRefresco(mensaje,request); 
	}
	
	/** 
	 * Abre la ventana de la fecha de cargo. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String generarNuevoFicheroAdeudo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			String idInstitucion	 = this.getIDInstitucion(request).toString();
			String fechaActual = GstDate.getHoyJsp(); // Obtengo la fecha actual
			String fechaPresentacion = fechaActual;		
			
			// Calcula las fechas para el fichero bancario a partir de la fecha de presentación
			FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(this.getUserBean(request));	
			HashMap fechas=admDisqueteCargos.getFechasCargo (idInstitucion, fechaPresentacion);
			
			// Carga las fechas en la request
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaPrimerosRecibos",fechas.get("fechaPrimerosRecibos").toString());
			request.setAttribute("fechaRecibosRecurrentes",fechas.get("fechaRecibosRecurrentes").toString());
			request.setAttribute("fechaRecibosCOR1",fechas.get("fechaRecibosCOR1").toString());
			request.setAttribute("fechaRecibosB2B",fechas.get("fechaRecibosB2B").toString());
			
			request.setAttribute("habilesPrimerosRecibos",fechas.get("habilesPrimerosRecibos").toString());
			request.setAttribute("habilesRecibosRecurrentes",fechas.get("habilesRecibosRecurrentes").toString());
			request.setAttribute("habilesRecibosCOR1",fechas.get("habilesRecibosCOR1").toString());
			request.setAttribute("habilesRecibosB2B",fechas.get("habilesRecibosB2B").toString());
			
			// Esto sirve para indicar el action que se ha utilizado para utilizar AJAX posteriormente
			request.setAttribute("accionInit", "FAC_ConfirmarFacturacion");
		
		} catch (Exception e) {
			throw new SIGAException("messages.general.error");
		}
	
		return "abrirFechaCargo";
	}	
	
	/** 
	 * Abre la ventana para crear un nuevo mantenimiento de facturación. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();
			
			// Obtiene los parametros necesarios para la configuracion de las fechas del fichero bancario 
			FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(user);	
			HashMap fechas = admDisqueteCargos.getParametrosFechasCargo(idInstitucion);
			
			request.setAttribute("habilesPrimerosRecibos",fechas.get("habilesPrimerosRecibos").toString());
			request.setAttribute("habilesRecibosRecurrentes",fechas.get("habilesRecibosRecurrentes").toString());
			request.setAttribute("habilesRecibosCOR1",fechas.get("habilesRecibosCOR1").toString());
			request.setAttribute("habilesRecibosB2B",fechas.get("habilesRecibosB2B").toString());			
			
			// Esto sirve para indicar el action que se ha utilizado para utilizar AJAX posteriormente
			request.setAttribute("accionInit", "FAC_ConfirmarFacturacion");
			
			// Igual que programacion
			HttpSession ses = request.getSession();
			request.getSession().setAttribute("DATABACKUP", null);	
			ses.setAttribute("ModoAction", "nuevaPrevision");
		
		} catch (Exception e) {
			throw new SIGAException("messages.general.error");
		}
	
		return "editarFechas";
	}

	/** 
	 * Atiende la accion descargaLog.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String descargaLog(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;	
		
		try {
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String pathFichero 		= p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			
			ConfirmarFacturacionForm form 	= (ConfirmarFacturacionForm)formulario;
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();
			String logFichero = (String)ocultos.elementAt(5); 
			
			File fichero = new File(pathFichero+sBarra+idInstitucion+sBarra+logFichero);
			if (!fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoErrores");
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}

		return "descargaFichero"; 
	}
	
	/**
	 * Marca una facturación programada confirmada para que el proceso background de 
	 * generación de facturas genere los pdf de las facturas.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String generarFacturaSolo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm)formulario;
			
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion); 
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion); 
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion); 

			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));

			Vector v = adm.selectByPK(h);
			if (v == null || v.size() != 1) {
				throw new ClsExceptions("Facturacion programada no encontrada");
			}
			
			FacFacturacionProgramadaBean bean = (FacFacturacionProgramadaBean) v.get(0);
			bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PROGRAMADA);
			bean.setGenerarPDF("1");
			bean.setRealizarEnvio("0");
			
			if (!adm.updateDirect(bean)) {
				throw new ClsExceptions("Error al actualizar estados de la programación: "+adm.getError());
			}

		}
		catch (ClsExceptions e) {
			return this.exitoRefresco(e.getMsg(),request);
		}
		catch (Exception e) {
			return this.error("messages.general.error",new ClsExceptions(e.getMessage()),request);
		}
		return this.exitoRefresco("messages.facturacion.generacionPDFFacturaProgramada", request); 
	}
	
	/**
	 * Genera las facturas individuales y las envia a los destinatarios correspondientes
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String enviarFacturas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		
		String sNombreFichero = "";
		String sRutaTemporal = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm) formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = user.getLocation();
			String idSerieFacturacion = (String) vOcultos.elementAt(0);
			String idProgramacion=(String) vOcultos.elementAt(1);
			FacFacturaAdm facturas = new FacFacturaAdm(user);
			Vector resultado = facturas.getSerieFacturacionConfirmada(idInstitucion, idSerieFacturacion, idProgramacion);
			if(!resultado.isEmpty()){
				//Se accede por clave referenciada de la tabla que hace join por lo 
				//que todos los registro tienen el mismo estado de generacion de pdf.
				//Cogemos por tanto el estado del priemr registro
				Hashtable hashPrimeraFacturaSerieProgramacion = (Hashtable)resultado.get(0); 
				String estadoPDF  = UtilidadesHash.getString(hashPrimeraFacturaSerieProgramacion,FacFacturacionProgramadaBean.C_IDESTADOPDF);
				if(estadoPDF.equals(FacEstadoConfirmFactBean.PDF_PROGRAMADA.toString())||estadoPDF.equals(FacEstadoConfirmFactBean.PDF_PROCESANDO.toString())){
					String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.PDFFacturaYaProgramada");
					return exitoRefresco(mensaje,request);
				}else{
					ClsLogging.writeFileLog("NO EXISTE EL ZIP, SE PASA A PROGRAMAR SU GENERACION",10);
					generarFacturaSolo(mapping, formulario, request, response);
					
					FacFacturacionProgramadaBean factBean = new FacFacturacionProgramadaBean();
					factBean.setIdInstitucion(Integer.valueOf(idInstitucion));
					factBean.setIdSerieFacturacion(Long.valueOf(idSerieFacturacion));
					factBean.setIdProgramacion(Long.valueOf(idProgramacion));
					factBean.setRealizarEnvio("1");
					factBean.setGenerarPDF("1");
					factBean.setEnvio("1");
					Hashtable hash = new Hashtable();
					hash.put("REQUEST", request);
					hash.put("proceso", factBean);
					
					SIGASvlProcesoAutomaticoRapido.NotificarAhora(hash);
				}
			}else{
			}
			
		}catch(SIGAException e){	
			throw e;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error");
		}
		return this.exitoRefresco("messages.envioRealizado.success", request);
		
	}
	
	protected String consultarFacturas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {				
			ConfirmarFacturacionForm form 	= (ConfirmarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			HttpSession ses = request.getSession();
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			Long idSerieFacturacion = Long.valueOf((String)ocultos.elementAt(0));			
			Long idProgramacion 	= Long.valueOf((String)ocultos.elementAt(1));	
			Integer idInstitucion	= this.getIDInstitucion(request);
									
			String sWhere=" WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion + 	
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + idProgramacion;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			Vector vDatos = adm.selectDatosFacturacion(sWhere, orden);
			
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			Hashtable hash = (Hashtable) vDatos.get(0);			
			String fechaPresentacion = GstDate.getFormatedDateShort("es",(String)hash.get("FECHAPRESENTACION"));			
			String fechaPrimerosRecibos = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSPRIMEROS"));
			String fechaRecibosRecurrentes = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSRECURRENTES"));
			String fechaRecibosCOR1 = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSCOR1"));
			String fechaRecibosB2B = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSB2B"));
			
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaPrimerosRecibos",fechaPrimerosRecibos);
			request.setAttribute("fechaRecibosRecurrentes",fechaRecibosRecurrentes);
			request.setAttribute("fechaRecibosCOR1",fechaRecibosCOR1);
			request.setAttribute("fechaRecibosB2B",fechaRecibosB2B);				
			request.setAttribute("accionInit","FAC_ConfirmarFacturacion");
			
			request.getSession().setAttribute("DATABACKUP", vDatos);	
			ses.setAttribute("ModoAction","ver");
			
			
			/** CR - Obtener informacion de factura en estado confirmada para mostrarselo en la ventan de consulta **/
			Vector datosInformeFac = null;
			if(hash.get("IDESTADOCONFIRMACION") != null && ((String)hash.get("IDESTADOCONFIRMACION")).equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA.toString())){			
				String sql = "	SELECT f_siga_getrecurso(FP.DESCRIPCION, 1)	AS FORMA_PAGO,   	"+
							 "     F_SIGA_CALCULAFORMATO(SUM(fac.imptotal)) AS IMPORTE,			"+
							 "      COUNT(*) AS NUM_FACTURAS									"+
							 " 	FROM PYS_FORMAPAGO FP, FAC_FACTURA FAC, FAC_FACTURACIONPROGRAMADA PROG	"+
							 " 	WHERE FP.IDFORMAPAGO = FAC.IDFORMAPAGO						"+
							 "  	AND FAC.IDINSTITUCION = PROG.IDINSTITUCION				"+
							 " 		AND FAC.IDSERIEFACTURACION = PROG.IDSERIEFACTURACION	"+
							 "		AND FAC.IDPROGRAMACION = PROG.IDPROGRAMACION			"+
							 "		AND PROG.IDSERIEFACTURACION = " + idSerieFacturacion     +
							 "		AND PROG.IDPROGRAMACION = 	  " + idProgramacion		 +
							 " 		AND PROG.IDINSTITUCION =      " + idInstitucion			 +
							 " GROUP BY FP.DESCRIPCION	";
				datosInformeFac = adm.selectGenerico(sql);
			}
			request.setAttribute("datosInformeFac",datosInformeFac);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		
		return "ver";

	}
	
	protected String editarFechas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="editarFechas";
		try{
			HttpSession ses = request.getSession();
			UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();

			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm)formulario;
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);			
			String idSerieFacturacion, idProgramacion;
			
			if (ocultos!=null) {			
				idSerieFacturacion = (String)ocultos.elementAt(0);			
				idProgramacion 	= (String)ocultos.elementAt(1);
			} else {
				idSerieFacturacion = form.getIdSerieFacturacion();			
				idProgramacion 	= form.getIdProgramacion();
				if(idProgramacion.equals("")){//Veimos despues de realizar una insercion
					idProgramacion = String.valueOf(ses.getAttribute("idProgramacion"));
					ses.removeAttribute("idProgramacion");
				}
			}
			
			String sWhere=" WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion + 	
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + idProgramacion;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};

			FacFacturacionProgramadaAdm admFacturacionProgramada = new FacFacturacionProgramadaAdm(user);
			Vector vDatos = admFacturacionProgramada.selectDatosFacturacion(sWhere, orden);			
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			Hashtable hash = (Hashtable) vDatos.get(0);
			GenParametrosAdm admParametros = new GenParametrosAdm(user);
			String fechaPresentacion = GstDate.getFormatedDateShort("es",(String)hash.get("FECHAPRESENTACION"));
			String fechaPrimerosRecibos = "";
			String fechaRecibosRecurrentes = "";
			String fechaRecibosCOR1 = "";
			String fechaRecibosB2B = "";
					
			FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(user);	

			// Obtiene los parametros necesarios para la configuracion de las fechas del fichero bancario
			HashMap fechas = admDisqueteCargos.getParametrosFechasCargo(idInstitucion);
			
			if (fechaPresentacion!=null && !fechaPresentacion.isEmpty()) {
				fechaPrimerosRecibos = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSPRIMEROS"));
				fechaRecibosRecurrentes = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSRECURRENTES"));
				fechaRecibosCOR1 = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSCOR1"));
				fechaRecibosB2B = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSB2B"));
			}
			
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaPrimerosRecibos",fechaPrimerosRecibos);
			request.setAttribute("fechaRecibosRecurrentes",fechaRecibosRecurrentes);
			request.setAttribute("fechaRecibosCOR1",fechaRecibosCOR1);
			request.setAttribute("fechaRecibosB2B",fechaRecibosB2B);

			request.setAttribute("habilesPrimerosRecibos",fechas.get("habilesPrimerosRecibos").toString());
			request.setAttribute("habilesRecibosRecurrentes",fechas.get("habilesRecibosRecurrentes").toString());
			request.setAttribute("habilesRecibosCOR1",fechas.get("habilesRecibosCOR1").toString());
			request.setAttribute("habilesRecibosB2B",fechas.get("habilesRecibosB2B").toString());
			
			request.setAttribute("idSerieFacturacion",idSerieFacturacion);
			request.setAttribute("idProgramacion",idProgramacion);
			request.getSession().setAttribute("DATABACKUP", vDatos);
			ses.setAttribute("ModoAction","editar");
			request.setAttribute("accionInit","FAC_ConfirmarFacturacion");			
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return result;
	}
	
	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;	
		ConfirmarFacturacionForm form = (ConfirmarFacturacionForm)formulario;
		String salida = "";
		HttpSession ses = request.getSession();
		
		try
		{	
			tx = this.getUserBean(request).getTransaction();			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			{	// Comprobamos si existe ese nombre para la institucion. Debe ser unico
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDINSTITUCION, this.getIDInstitucion(request));
				UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_DESCRIPCION,   form.getDescripcionProgramacion());
				Vector v = adm.select(h);
				if ((v != null) && (v.size() > 0)) {
					throw new SIGAException(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "facturacion.seriesFacturacion.error.descripcionDuplicada"));
				}
			}
			
			tx.begin();	

			FacFacturacionProgramadaBean bean = getDatos(form, request);

			// Obtenemos el idProgramacion
			bean.setIdProgramacion(adm.getNuevoID(bean));
			form.setIdProgramacion(bean.getIdProgramacion().toString());
			
			// tratamiento de estados de la programacion 
			bean.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.GENERACION_PROGRAMADA);
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			String fechaPrevistaConfirmacion = form.getFechaPrevistaConfirmacion();			
			if (fechaPrevistaConfirmacion!=null && !fechaPrevistaConfirmacion.equals("")) {
				// yyyy/MM/dd HH:mm:ss
				Date dFechaPrevistaConfirmacion = GstDate.convertirFechaHora(fechaPrevistaConfirmacion);
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				fechaPrevistaConfirmacion = sdf.format(dFechaPrevistaConfirmacion); // Fecha con formato dd/MM/yyyy	
			}
			
			String idInstitucion = this.getIDInstitucion(request).toString();
			String fechaEntrega = form.getFechaPresentacion();
			bean.setFechaPresentacion(GstDate.getApplicationFormatDate("en", fechaEntrega));
			
			String fechaRecibosPrimeros = form.getFechaRecibosPrimeros();
			String fechaRecibosRecurrentes = form.getFechaRecibosRecurrentes();
			String fechaRecibosCOR1 = form.getFechaRecibosCOR1();
			String fechaRecibosB2B = form.getFechaRecibosB2B();
			bean.setFechaRecibosPrimeros(GstDate.getApplicationFormatDate("en", fechaRecibosPrimeros));
			bean.setFechaRecibosRecurrentes(GstDate.getApplicationFormatDate("en", fechaRecibosRecurrentes));
			bean.setFechaRecibosCOR1(GstDate.getApplicationFormatDate("en", fechaRecibosCOR1));
			bean.setFechaRecibosB2B(GstDate.getApplicationFormatDate("en", fechaRecibosB2B));
			
			if (fechaEntrega!=null && !fechaEntrega.equals("")) {
				// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales			
				FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(this.getUserBean(request));	
				if (!admDisqueteCargos.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, fechaPrevistaConfirmacion)) {
					throw new SIGAException("facturacion.ficheroBancarioPagos.errorMandatos.mensajeFechas");
				}
			}
			
			// Comprobaciones antes de confirmacion 
			Vector ret = adm.comprobarRecursosProgramacion(bean);
			
			// tratamiento del mensaje
			String mensaje = "messages.inserted.success"; 			
			if (ret.size()>0) {
				mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.avisos");
				for (int i=0;i<ret.size();i++) {
					mensaje += "\n" + UtilidadesString.getMensajeIdioma(this.getUserBean(request),(String)ret.get(i));
				}
			} 
			
			salida = exitoRefresco(mensaje,request);

			// RGG 20/11/2007 añadimos los campos nuevos de contabilidad
			FacSerieFacturacionAdm admS = new FacSerieFacturacionAdm(this.getUserBean(request));
			Hashtable ht = new Hashtable();
			ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION, bean.getIdInstitucion());
			ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, bean.getIdSerieFacturacion());
			Vector v = admS.selectByPK(ht);
			if (v!=null && v.size()>0) {
			    FacSerieFacturacionBean bSF = (FacSerieFacturacionBean) v.get(0);
			    bean.setConfDeudor(bSF.getConfigDeudor());
			    bean.setConfIngresos(bSF.getConfigIngresos());
			    bean.setCtaClientes(bSF.getCuentaClientes());
			    bean.setCtaIngresos(bSF.getCuentaIngresos());
			}
			
			// Insertamos el nuevo registro.
			if(!adm.insert(bean)){
				throw new SIGAException (adm.getError());
			}			
			tx.commit();
			ses.setAttribute("ModoAction","editar");
			ses.setAttribute("idProgramacion",bean.getIdProgramacion());
			
		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.general.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);				

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
		return salida;				
	}

	/**
	 * Funcion para obtener los datos a insertar en BD
	 * @param form -  Action Form asociado a este Action
	 * @param request - objeto llamada HTTP 
	 * @return FacFacturacionProgramadaBean contiene los datos a insertar en BD
	 * @throws SIGAException
	 */
	protected FacFacturacionProgramadaBean getDatos(ConfirmarFacturacionForm form, HttpServletRequest request) throws SIGAException {
		FacFacturacionProgramadaBean bean = null;
		String idTipoPlantillaMail = "";
		try {
			bean = new FacFacturacionProgramadaBean();
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setIdSerieFacturacion(new Long(form.getIdSerieFacturacion()));
			bean.setFechaInicioProductos(form.getFechaInicialProducto());
			bean.setFechaFinProductos(form.getFechaFinalProducto());
			bean.setFechaInicioServicios(form.getFechaInicialServicio());
			bean.setFechaFinServicios(form.getFechaFinalServicio());
			String sFechaProgramacion = UtilidadesString.formatoFecha(new Date(),"yyyy/MM/dd HH:mm:ss"); 
			bean.setFechaProgramacion(sFechaProgramacion);
			bean.setArchivarFact("0");
			bean.setVisible("S");
			
			// tratamos las fechas con minutos y segundos
			String aux = "";
			String auxfechaCargo="";

			aux = form.getFechaPrevistaGeneracion().substring(0,form.getFechaPrevistaGeneracion().length()-9) + " " + ((form.getHorasGeneracion().trim().equals(""))?"00":form.getHorasGeneracion())+":"+((form.getMinutosGeneracion().trim().equals(""))?"00":form.getMinutosGeneracion())+":00";
			bean.setFechaPrevistaGeneracion(aux);			

			if (form.getFechaPrevistaConfirmacion()!=null && !form.getFechaPrevistaConfirmacion().equals("")) {
				aux = form.getFechaPrevistaConfirmacion().substring(0,form.getFechaPrevistaConfirmacion().length()-9) + " " + ((form.getHorasConfirmacion().trim().equals(""))?"00":form.getHorasConfirmacion())+":"+((form.getMinutosConfirmacion().trim().equals(""))?"00":form.getMinutosConfirmacion())+":00";
				bean.setFechaPrevistaConfirmacion(aux);			
			}else{
				bean.setFechaPrevistaConfirmacion("");		
			}
			
			if (form.getFechaCargo()!=null && !form.getFechaCargo().equals("")) {							
				bean.setFechaCargo(form.getFechaCargo());
			}else{
				bean.setFechaCargo("");
			}

			
			bean.setGenerarPDF((form.getGenerarPDF()!=null)?"1":"0");			
			bean.setEnvio((form.getEnviarFacturas()!=null)?"1":"0");

			if(form.getIdTipoPlantillaMail()!=null && !form.getIdTipoPlantillaMail().equals("")){
				idTipoPlantillaMail = form.getIdTipoPlantillaMail().split(",")[0];
				bean.setIdTipoPlantillaMail(Integer.parseInt(idTipoPlantillaMail));
				bean.setIdTipoEnvios(1);
			} else{
				bean.setIdTipoPlantillaMail(null);
				bean.setIdTipoEnvios(null);
			}
			
			if (bean.getEnvio().equals("1"))
				bean.setGenerarPDF("1");
			bean.setDescripcion(form.getDescripcionProgramacion());
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, null);
		}
		return bean;
	}	

	/** 
	 *  Funcion que atiende la accion modificarr. Permiote modificar los datos del registro seleccionado 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;	
		ConfirmarFacturacionForm form = (ConfirmarFacturacionForm)formulario;
		String salida="";
		
		try
		{	
			tx = this.getUserBean(request).getTransaction();			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			FacFacturacionProgramadaBean bean = getDatos(form, request);
			Enumeration en = ((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			Hashtable hash = (Hashtable)en.nextElement();
			
			{	// Comprobamos si existe ese nombre para la institucion. Debe ser unico
				String where="";
				where =" where "+FacFacturacionProgramadaBean.C_IDINSTITUCION+"="+this.getIDInstitucion(request)+
			       " and "+ FacFacturacionProgramadaBean.C_DESCRIPCION+"='"+form.getDescripcionProgramacion()+"'"+
				   " and "+FacFacturacionProgramadaBean.C_IDPROGRAMACION +" not in (select "+FacFacturacionProgramadaBean.C_IDPROGRAMACION +
                       "																from "+FacFacturacionProgramadaBean.T_NOMBRETABLA+
					   "																where "+FacFacturacionProgramadaBean.C_IDINSTITUCION+"="+this.getIDInstitucion(request)+
				       "                                                                  and "+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+"="+bean.getIdSerieFacturacion()+
					   " 															      and "+FacFacturacionProgramadaBean.C_IDPROGRAMACION+"="+UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION)+")";																																		                                                                      
				
				Vector v = adm.select(where);
				if ((v != null) && (v.size() > 0)) {
					throw new SIGAException(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "facturacion.seriesFacturacion.error.descripcionDuplicada"));
				}
			}
			
			tx.begin();			
			
			// Recogemos la hashOriginal							
			bean.setOriginalHash(hash);
			bean.setIdProgramacion(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION));
			bean.setNombrefichero(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_NOMBREFICHERO));
			bean.setLogerror(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_LOGERROR));

			// para que no se pierda la hora de programacion (Creacion)
			bean.setFechaProgramacion(null);
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			String fechaPrevistaConfirmacion = form.getFechaPrevistaConfirmacion();			
			if (fechaPrevistaConfirmacion!=null && !fechaPrevistaConfirmacion.equals("")) {
				// yyyy/MM/dd HH:mm:ss
				Date dFechaPrevistaConfirmacion = GstDate.convertirFechaHora(fechaPrevistaConfirmacion);
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				fechaPrevistaConfirmacion = sdf.format(dFechaPrevistaConfirmacion); // Fecha con formato dd/MM/yyyy	
			}	
			
			String idInstitucion = this.getIDInstitucion(request).toString();
			String fechaEntrega = form.getFechaPresentacion();
			bean.setFechaPresentacion(GstDate.getApplicationFormatDate("en", fechaEntrega));
			
			String fechaRecibosPrimeros = form.getFechaRecibosPrimeros();
			String fechaRecibosRecurrentes = form.getFechaRecibosRecurrentes();
			String fechaRecibosCOR1 = form.getFechaRecibosCOR1();
			String fechaRecibosB2B = form.getFechaRecibosB2B();
			bean.setFechaRecibosPrimeros(GstDate.getApplicationFormatDate("en", fechaRecibosPrimeros));
			bean.setFechaRecibosRecurrentes(GstDate.getApplicationFormatDate("en", fechaRecibosRecurrentes));
			bean.setFechaRecibosCOR1(GstDate.getApplicationFormatDate("en", fechaRecibosCOR1));
			bean.setFechaRecibosB2B(GstDate.getApplicationFormatDate("en", fechaRecibosB2B));
			
			if (fechaEntrega!=null && !fechaEntrega.equals("")) {
				// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales
				FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(this.getUserBean(request));	
				if (!admDisqueteCargos.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, fechaPrevistaConfirmacion)) {
					throw new SIGAException("facturacion.ficheroBancarioPagos.errorMandatos.mensajeFechas");
				}
			}
			
			// tratamiento de estados de la programacion 
			bean = adm.tratamientoEstadosProgramacion(bean);
			
			// Comprobaciones antes de confirmacion 
			Vector ret = adm.comprobarRecursosProgramacion(bean);
			
			// tratamiento del mensaje
			String mensaje = "messages.updated.success"; 			
			if (ret.size()>0) {
				mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.avisos");
				for (int i=0;i<ret.size();i++) {
					mensaje += "\n" + UtilidadesString.getMensajeIdioma(this.getUserBean(request),(String)ret.get(i));
				}
			} 
			
			// Modificamos el registro.
			if(!adm.update(bean)){
				throw new SIGAException (adm.getError());
			}			
			tx.commit();
			
			
			salida = exitoRefresco(mensaje,request);
			
		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.general.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);					
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
		return salida;		
	}		
	
	/**
	 * Se va a realizar la descarga del fichero (DOWNLOAD).
	 * 
	 * @param ActionMapping
	 *            mapping Mapeador de las acciones.
	 * @param MasterForm
	 *            formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest
	 *            request: información de entrada de la pagina original.
	 * @param HttpServletResponse
	 *            response: información de salida para la pagina destino.
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String descargarInformeGeneracion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)	throws SIGAException {
		String sRutaJava = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm) formulario;
			Vector vOcultos = (Vector)form.getDatosTablaOcultos(0);
			String idSerieFacturacion = (String)vOcultos.elementAt(0);			
			String idProgramacion 	= (String)vOcultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();
			String nombreFichero = (String)vOcultos.elementAt(4);

		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			sRutaJava = rp.returnProperty("facturacion.directorioPrevisionesJava");
			String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
			sRutaJava = sRutaFisicaJava + File.separator + sRutaJava;

			sRutaJava += File.separator + idInstitucion	+ File.separator + nombreFichero;

			//Control de que no exista el fichero a descargar:
			File tmp = new File(sRutaJava);
			if(tmp==null || !tmp.exists()){
				Facturacion facturacion = new Facturacion(user);
				nombreFichero = facturacion.generarInformeGeneracion(idInstitucion, idSerieFacturacion, idProgramacion);
				if (nombreFichero == null ||nombreFichero.length() == 0) {
					throw new SIGAException("messages.general.error.ficheroNoExiste"); 
				} else {
					// GUARDAMOS EL NOMBRE EN BBDD PARA NO VOLVER A GENERAR EL INFORME DE ESTA FACTURA
					FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
					Hashtable<String,Object> hashEstado = new Hashtable<String,Object>();
					UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion);
			    	UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
			    	UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion);
					String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, FacFacturacionProgramadaBean.C_IDPROGRAMACION};
			    	String[] camposInforme = {FacFacturacionProgramadaBean.C_NOMBREFICHERO};
					UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_NOMBREFICHERO, nombreFichero);
					if (!adm.updateDirect(hashEstado, claves, camposInforme)) {
						throw new Exception("Error al actualizar el registro en FacFacturacionProgramadaAdm");
					}					
				}
			}
			
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", sRutaJava);
			
		} catch (Exception e) {
			throwExcp("messages.general.error",	new String[] { "modulo.facturacion.previsionesFacturacion" }, e, null);
		}

		return "descargaFichero";
	}	

	/** 
	 *  Funcion que atiende la accion borrar. Genera las facturas programadas 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;	
		try{
			tx = this.getUserBean(request).getTransactionPesada();
			
			ConfirmarFacturacionForm form 				= (ConfirmarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm adm 			= new FacFacturacionProgramadaAdm(this.getUserBean(request));
			FacFacturacionProgramadaBean bean 			= new FacFacturacionProgramadaBean();
			Vector ocultos 				= (Vector)form.getDatosTablaOcultos(0);			
			String idSerieFacturacion 	= (String)ocultos.elementAt(0);			
			String idProgramacion 		= (String)ocultos.elementAt(1);
			String idInstitucion		= this.getIDInstitucion(request).toString();
			
            Hashtable ht = new Hashtable();
			ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion);
			ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion);
			ht.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
			Vector v = adm.selectByPK(ht);
			bean = (FacFacturacionProgramadaBean) v.get(0);
			String logError = bean.getLogerror(); 
			
			tx.begin();	
	
			Object[] param_in = new Object[4];
			param_in[0] = idInstitucion;
			param_in[1] = idSerieFacturacion;
			param_in[2] = idProgramacion;
			param_in[3] = this.getUserName(request).toString();
			String resultado[] = new String[2];
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.ELIMINARFACTURACION(?,?,?,?,?,?)}",2, param_in);
			String codretorno = resultado[0];
			if (!codretorno.equals("0")) {
				
				if (codretorno.equals("-1")) {
					// No existe la facturacion
					throw new SIGAException("messages.facturacion.facturacionNoExiste");
				} else
				if (codretorno.equals("-2")) {
					// La facturacion está bloqueada
					throw new SIGAException("messages.facturacion.generacionEnProceso");
				} else {
					// Error general
					throw new ClsExceptions("Error en ejecución del PL PKG_SIGA_FACTURACION.ELIMINARFACTURACION. Cod:ORA-"+codretorno+" Desc:"+resultado[1]);
				}
				
			} else {
				
		 		// RGG 05/02/2007 ELIMINAMOS LOS FICHERO DE LOG ASOCIADOS A LA PROGRAMACION
			    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String pathFichero 		= p.returnProperty("facturacion.directorioFisicoLogProgramacion");
	    		String sBarra = "";
	    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
	    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
				
				File fichero = new File(pathFichero+sBarra+idInstitucion+sBarra+logError);
				if (fichero.exists()) {
					fichero.delete();
				}	
				//Borramos los PDFs en caso de que los haya	
				String pathFicheroPDF = p.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+p.returnProperty("facturacion.directorioFacturaPDFJava");
				String idserieidprogramacion = idSerieFacturacion+"_" + idProgramacion;
				pathFicheroPDF += sBarra+idInstitucion+sBarra+idserieidprogramacion;
				File ficheroPDF = new File(pathFicheroPDF);
				if (ficheroPDF.exists()) {
					Plantilla.borrarDirectorio(ficheroPDF);
				}
			}
				
			tx.commit();					
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
	
		return exitoRefresco("messages.deleted.success", request);
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
	protected void getAjaxFechasFicheroBancario(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException ,Exception {
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idInstitucion = user.getLocation();
		
		FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(user);	
		HashMap fechas=admDisqueteCargos.getFechasCargo (idInstitucion, (String)request.getParameter("fechaPresentacion"));

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