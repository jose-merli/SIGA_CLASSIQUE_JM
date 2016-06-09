/*
 * Created on 07-mar-2005
 *
 */
package com.siga.facturacion.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.envios.Documento;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.facturacion.form.BusquedaFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;

/**
 * @author daniel.campos
 *
 */
public class BusquedaFacturaAction extends MasterAction {

	final String[] clavesBusqueda={FacFacturaBean.C_IDFACTURA,FacFacturaBean.C_NUMEROFACTURA,FacFacturaBean.C_ESTADO};
	
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
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					miForm.reset(mapping,request);
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);						
				}
				else if (accion.equalsIgnoreCase("buscarInit")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPor(mapping, miForm, request, response); 
				}else if (accion.equalsIgnoreCase("descargaFacturas")){
					mapDestino = descargaFacturas(mapping, miForm, request, response); 
				}
				
				else {
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		} 
		return mapping.findForward(mapDestino);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			// Si no vengo del boton volver reseteo los formulario
			BusquedaFacturaForm miFormSession = (BusquedaFacturaForm)request.getSession().getAttribute("BusquedaFacturaForm");
			BusquedaFacturaForm miForm = (BusquedaFacturaForm)formulario;
			String buscar = request.getParameter("botonVolver_Buscar");
			if (buscar==null) buscar = request.getParameter("buscar"); // Si vuelve de envios
			if (miFormSession != null) {
				if (buscar == null){
					// Reseteo los formularios
					miFormSession.reset(mapping,request);
					miForm.reset(mapping,request);
				}
			
				request.setAttribute("idSereFacturacionAnteriorBusqueda", miFormSession.getBuscarIdSerieFacturacion());
				request.setAttribute("deudor", miFormSession.getDeudor());
				request.setAttribute("idEstado", miFormSession.getBuscarIdEstado());
			}
			request.setAttribute("botonVolver_Buscar", buscar);
			request.setAttribute("idInstitucion", this.getIDInstitucion(request));
			
			// Establezco el valor para el boton volver 
			request.getSession().setAttribute("CenBusquedaClientesTipo","BF"); // busqueda factura
			
			// Calculo la fecha desde hace dos anios
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if (calendar.get(Calendar.MONTH)==Calendar.FEBRUARY && calendar.get(Calendar.DAY_OF_MONTH)==29) {
				calendar.set(Calendar.DAY_OF_MONTH, 28);
			}
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-2);
			
			SimpleDateFormat formateo = new SimpleDateFormat("dd/MM/yyyy");
			String fechaDesde = formateo.format(calendar.getTime());			
			
			if (miForm != null) {
				String fechaDesdeFormulario = miForm.getBuscarFechaDesde();
				if (fechaDesdeFormulario==null || fechaDesdeFormulario.equals("")) {
					miForm.setBuscarFechaDesde(fechaDesde); 
				} else {
					fechaDesde = fechaDesdeFormulario;
				}				
			}
			
			if (miFormSession != null) {
				miFormSession.setBuscarFechaDesde(fechaDesde);
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, null); 
		}
		return super.abrir(mapping, formulario, request, response);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			BusquedaFacturaForm miForm = (BusquedaFacturaForm) formulario;
			FacFacturaAdm admFactura = new FacFacturaAdm(this.getUserBean(request));
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			
			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miForm.getSeleccionarTodos()!=null 
				&& !miForm.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = miForm.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miForm.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			
			HashMap databackup = miForm.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null&&!isSeleccionarTodos){
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();

				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");

				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				
				datos = actualizarFacturaciones(admFactura,abonoAdm, this.getIDInstitucion(request),this.getUserBean(request).getLanguage(), datos);
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	
				
				databackup=new HashMap();

				//obtengo datos de la consulta 			
				PaginadorBind resultado = null;
				resultado = admFactura.getFacturas (miForm, this.getIDInstitucion(request),this.getLenguaje(request));
				Vector datos = null;

				databackup.put("paginador",resultado);
				
				if (resultado!=null && resultado.getNumeroTotalRegistros()>0){ 
							
					
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados_aux = new ArrayList((Collection)admFactura.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						ArrayList clavesRegSeleccinados =new ArrayList();
						
						for (int r=0;r<clavesRegSeleccinados_aux.size();r++)
						{
							Hashtable clavesRegSeleccinadosH = (Hashtable) clavesRegSeleccinados_aux.get(r);
							//Las facturas en revisión y anuladas no se pueden anular, las anuladas por estar anuladas ya y las de en revisión por no tener número de factura
							if((UtilidadesHash.getInteger(clavesRegSeleccinadosH, FacFacturaBean.C_ESTADO)!=8)&&(UtilidadesHash.getInteger(clavesRegSeleccinadosH, FacFacturaBean.C_ESTADO)!=7))
								//Para anular una factura tiene que tener número de factura
								if((UtilidadesHash.getString(clavesRegSeleccinadosH, FacFacturaBean.C_NUMEROFACTURA)!=null)&&(!UtilidadesHash.getString(clavesRegSeleccinadosH, FacFacturaBean.C_NUMEROFACTURA).isEmpty()))
									clavesRegSeleccinados.add(clavesRegSeleccinados_aux.get(r));
							
						}
						
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miForm.setRegistrosSeleccionados(clavesRegSeleccinados);

						int pagina;
						try{
							pagina = Integer.parseInt(miForm.getSeleccionarTodos());
						}catch (Exception e) {
							// Con esto evitamos un error cuando se recupera una pagina y hemos "perdido" la pagina actual
							// cargamos la primera y no evitamos mostrar un error
							pagina = 1;
						}
						datos = resultado.obtenerPagina(pagina);
						miForm.setSeleccionarTodos("");
						
					}else{				
						miForm.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}

					datos = actualizarFacturaciones(admFactura,abonoAdm, this.getIDInstitucion(request),this.getUserBean(request).getLanguage(), datos);
					databackup.put("datos",datos);

				}else{
					resultado = null;
					miForm.setRegistrosSeleccionados(new ArrayList());
				}  
				miForm.setDatosPaginador(databackup);

			}			

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, null); 
		}
		return "buscarPor";
	}
	
	private Vector actualizarFacturaciones(FacFacturaAdm admFac,FacAbonoAdm admAbono, Integer idInstitucion, String idioma, Vector datos) throws SIGAException,ClsExceptions{
		
		
		Hashtable<String,Object> htAbono = new Hashtable<String,Object>();
		htAbono.put(FacAbonoBean.C_IDINSTITUCION, idInstitucion);
		for (int i=0;i<datos.size();i++) 
		{
			Row fila = (Row)datos.get(i);
			
			Hashtable registro = fila.getRow();

			String idFactura = UtilidadesHash.getString(registro, FacFacturaBean.C_IDFACTURA);
			String idEstado = UtilidadesHash.getString(registro, FacFacturaBean.C_ESTADO);
			String estado = UtilidadesHash.getString(registro, "DESC_ESTADO_ORIGINAL");
			htAbono.put(FacAbonoBean.C_IDFACTURA,idFactura);
			
			//Cogemos el total de la factura
			Double total = new Double(UtilidadesHash.getString(registro, "IMPTOTAL"));
			//Miramos la descripcion del estado de la factura
			String sEstado =  UtilidadesString.getMensajeIdioma(idioma, estado);
			if(idEstado.equals("8")){
				
				Vector vAbonos = admAbono.select(htAbono);
				
				// JPT: Las facturas devueltas por comision, generan facturas anuladas sin abono, porque se crea una factura nueva
				if (vAbonos!=null && vAbonos.size()==1) {
					//Es unique key por lo que habra solo un registro
					FacAbonoBean beanAbono = (FacAbonoBean)vAbonos.get(0);
				 
					sEstado += " ("+beanAbono.getNumeroAbono()+")";
				}
			}
			
			registro.put("DESCRIPCION_ESTADO", sEstado);
			registro.put("TOTAL", ""+total);
		}
		
		return datos;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try {
			BusquedaFacturaForm miForm = (BusquedaFacturaForm) formulario;
	
			Vector fila = miForm.getDatosTablaOcultos(0);
			String idInstitucion = (String)fila.get(0);
			String idFactura = (String)fila.get(1);
			String idPersona = (String)fila.get(2);

			Hashtable<String,String> datosFac = new Hashtable<String,String>();
			
			datosFac.put("accion", "editar");
			datosFac.put("idFactura", idFactura);
			datosFac.put("idInstitucion", idInstitucion);
			datosFac.put("idPersona", idPersona);
			datosFac.put("botonVolver", "SI");
	
			request.setAttribute("datosFacturas", datosFac);		
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null);
		}
		return "administrarPestanas";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			BusquedaFacturaForm miForm = (BusquedaFacturaForm) formulario;
	
			Vector fila = miForm.getDatosTablaOcultos(0);
			String idInstitucion = (String)fila.get(0);
			String idFactura = (String)fila.get(1);
			String idPersona = (String)fila.get(2);

			Hashtable<String,String> datosFac = new Hashtable<String,String>();
			
			UtilidadesHash.set(datosFac,"accion", "ver");
			UtilidadesHash.set(datosFac,"idFactura", idFactura);
			UtilidadesHash.set(datosFac,"idInstitucion", idInstitucion);
			UtilidadesHash.set(datosFac,"idPersona", idPersona);
			UtilidadesHash.set(datosFac,"botonVolver", "SI");
	
			request.setAttribute("datosFacturas", datosFac);		
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null);
		}
		return "administrarPestanas";
	}
	
	protected String descargaFacturas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");			
			CenPersonaAdm personaAdm = new CenPersonaAdm(user);
			String idInstitucion = user.getLocation();
			
			FacFacturaAdm admFacFactura = new FacFacturaAdm(user);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(user);
			CenDireccionesAdm admCenDirecciones = new CenDireccionesAdm(user);
			InformeFactura infFactura = new InformeFactura(user);
			
			BusquedaFacturaForm form = (BusquedaFacturaForm) formulario;
			String datosFacturas = form.getFacturas();
			
			StringBuilder errores = new StringBuilder("");
			List<Documento> documentosList = new ArrayList<Documento>();
			if (datosFacturas != null && !datosFacturas.equals("")) {
				String arrayFacturas[] = datosFacturas.split(";");
				if (arrayFacturas.length < 1) {
					throw new SIGAException("Error al obtener las facturas");
				}

				for (int i = 0; i < arrayFacturas.length; i++) {
					StringBuilder datosSolicitudError = null;
					File filePDF = null;
					String datosFactura = arrayFacturas[i];

					String idFactura = datosFactura.substring(0, datosFactura.indexOf("||"));
					String numfactura = datosFactura.substring(datosFactura.indexOf("||") + 2, datosFactura.lastIndexOf("||"));
					
					Hashtable<String,Object> hFacFactura = new Hashtable<String,Object>();
					hFacFactura.put(FacFacturaBean.C_IDINSTITUCION, idInstitucion);
					hFacFactura.put(FacFacturaBean.C_IDFACTURA, idFactura);
					
					FacFacturaBean bFacFactura = (FacFacturaBean) admFacFactura.selectByPK(hFacFactura).firstElement();					
					String idPersonaFactura = bFacFactura.getIdPersona().toString();
					
					Hashtable direccion = admCenDirecciones.getEntradaDireccionEspecifica(idPersonaFactura, idInstitucion, "" + ClsConstants.TIPO_DIRECCION_FACTURACION);
					if (direccion.size() == 0) {
						// Si no hay direccion de despacho (porque es un no colegiado), miramos su direccion de correo
						direccion = admCenDirecciones.getEntradaDireccionEspecifica(idPersonaFactura, idInstitucion, "" + ClsConstants.TIPO_DIRECCION_DESPACHO);
					}
					
					if (direccion.size() == 0) {
						direccion = admCenDirecciones.getEntradaDireccionEspecifica(idPersonaFactura, idInstitucion, "" + ClsConstants.TIPO_DIRECCION_CENSOWEB);
					}
					
					if (direccion.size() > 0) {
						ClsLogging.writeFileLog("ANTES DE LA GENERACION DE LA FACTURA. ", 10);
						filePDF = infFactura.generarPdfFacturaFirmada(request, bFacFactura, false);
						
						if (filePDF == null) {
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Num. Factura:");
							datosSolicitudError.append(numfactura);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							
						} else {
							filePDF.deleteOnExit();
							ClsLogging.writeFileLog("DESPUES DE LA GENERACION DE LA FACTURA: " + filePDF.getAbsolutePath(), 10);
							ClsLogging.writeFileLog("Existe el fichero: " + ((filePDF.exists()) ? "SI" : "NO"), 10);
						}
						
					} else {
						datosSolicitudError = new StringBuilder();
						datosSolicitudError.append("[Num. Factura:");
						datosSolicitudError.append(numfactura);
						datosSolicitudError.append("],");
						
						errores.append(datosSolicitudError);
					}
					
					if (datosSolicitudError == null) {
						String nombreColegiado = personaAdm.obtenerNombreApellidos(idPersonaFactura);
						Documento documento = new Documento(filePDF, filePDF.getName());
						if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
							nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
						}else{
							nombreColegiado="";
						}
						
						
						String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + bFacFactura.getIdSerieFacturacion() +
								" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " + bFacFactura.getIdInstitucion();
									
						Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
												
				
						if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
							FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
						
							switch (beanSerieFacturacion.getIdNombreDescargaPDF()) {
							case 1:
								documento.setDescripcion(documento.getDescripcion());
								break;
							case 2:
								//Quitamos la extensión y añadimos el nombre más la extensión
								String[] separacionExtensionDelFichero = documento.getDescripcion().split(Pattern.quote("."));
								String[] separacionNombreColegiado = nombreColegiado.split("-");
								documento.setDescripcion(separacionExtensionDelFichero[0] + "-"+separacionNombreColegiado[0]+"."+separacionExtensionDelFichero[1]);
								break;
							case 3:
								documento.setDescripcion(nombreColegiado+documento.getDescripcion());
								break;
		
							default:
								documento.setDescripcion(nombreColegiado+documento.getDescripcion());
								break;
							}
						}else{
							documento.setDescripcion(nombreColegiado+documento.getDescripcion());
						}
						documentosList.add(documento);
					}
				}
			}
			
			if (documentosList.size() > 0) {
				if(documentosList.size()==1){
					request.setAttribute("nombreFichero", documentosList.get(0).getDescripcion());
					request.setAttribute("rutaFichero",  documentosList.get(0).getDocumento().getPath());
					
				} else {
					File fichero = documentosList.get(0).getDocumento();					 					
					File directorio = fichero.getParentFile();
					directorio.mkdirs();
					if (!directorio.exists()) {
						throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
					} else {
						if (!directorio.canWrite()) {
							throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
						}
					}

					StringBuilder sRutaZip = new StringBuilder();
					sRutaZip.append(directorio.getPath()); // Elimina el nombre del fichero
					sRutaZip.append(ClsConstants.FILE_SEP);
					sRutaZip.append("Facturas");
					sRutaZip.append(GstDate.parseDateToString(new Date(),"yyyyMMdd_hhmm",new Locale("ES")));
					sRutaZip.append(".zip");
					
					File filezip = UtilidadesFicheros.doZip(sRutaZip.toString(), documentosList);
					filezip.deleteOnExit();
					
		    		// Eliminacion de los pdfs firmados, el documento excel de la facturacion y su carpeta
		    		for (int i=0; i<documentosList.size(); i++) {
		    			File ficheroPdfFirmado = documentosList.get(i).getDocumento();
		    			ficheroPdfFirmado.delete(); // Elimina los pdfs firmados
		    		}
		    		if (directorio!=null && directorio.isDirectory() && directorio.list().length==0) {
		    			directorio.delete(); // borra el directorio de las firmas
					}					
				
					request.setAttribute("nombreFichero", filezip.getName());
					request.setAttribute("rutaFichero", filezip.getPath());
				}
				
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK", "OK");

			} else {
				ClsLogging.writeFileLog("No hay ficheros que descargar ", 3);
			}
			
			if (!errores.toString().equals(""))
				request.setAttribute("avisoFicherosNoGenerado", errores.toString());

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacion" }, e, null);
		}
		
		return "descarga";
	}
	
	
	protected String descargaFacturasNew(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = userBean.getTransactionLigera();
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;

		try {

			// Obtenemos el certificado
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
			StringBuilder pathDirectorioTemporal = new StringBuilder(admSolicitud.getRutaCertificadoDirectorioBD(2000));
			pathDirectorioTemporal.append(ClsConstants.FILE_SEP);
			pathDirectorioTemporal.append("tmp");
			List<Documento> documentosList = new ArrayList<Documento>();
			List<CerSolicitudCertificadosBean> cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
			StringBuilder errores = new StringBuilder("");
			String[] lineas = form.getIdsParaEnviar().split(";");
			for (int i = 0; i < lineas.length; i++) {
				StringBuilder datosSolicitudError = null;
				try {
					String[] campos = lineas[i].split("%%");
					if (campos.length > 1) {
						String idInstitucion = campos[6];

						String idSolicitud = campos[1];

						Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
						htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
						htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

						Vector vDatos = admSolicitud.selectByPK(htSolicitud);

						CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

						File certificadoFile = admSolicitud.recuperarCertificado(beanSolicitud);
						
						

						if (certificadoFile == null || !certificadoFile.exists()) {
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Institucion:");
							datosSolicitudError.append(idInstitucion);
							datosSolicitudError.append(", solicitud:");
							datosSolicitudError.append(idSolicitud);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							throw new SIGAException("messages.general.error.ficheroNoExiste");
						}
						
						
						
						beanSolicitud.setFechaDescarga("SYSDATE");
						Documento certificado = new Documento(certificadoFile,admSolicitud.getNombreFicheroSalida(beanSolicitud));
						documentosList.add(certificado);
						beanSolicitud.setCertificado(certificado);
						cerSolicitudCertificadosBeans.add(beanSolicitud);
						

						
					}

				} catch (SIGAException e) {
					ClsLogging.writeFileLog("Error descarga Certificado" + datosSolicitudError.toString() + " Error: " + e.getLiteral(userBean.getLanguage()), 3);
				}
			}
			tx.begin();
			for (CerSolicitudCertificadosBean cerSolicitudCertificadosBean : cerSolicitudCertificadosBeans) {

				if (!admSolicitud.updateDirect(cerSolicitudCertificadosBean)) {
					throw new ClsExceptions("Error al actualizar la fecha de descarga: " + admSolicitud.getError());
				}
			}
			tx.commit();
			
			if (documentosList.size() > 0) {
				
				StringBuilder pathZip = new StringBuilder(pathDirectorioTemporal);
				File directorio = new File(pathZip.toString());
				if (!directorio.exists())
					directorio.createNewFile();
				pathZip.append(File.separatorChar);
				pathZip.append("Certificados.zip");

				File filezip = UtilidadesFicheros.doZip(pathZip.toString(), documentosList);
				filezip.deleteOnExit();
				request.setAttribute("nombreFichero", filezip.getName());
				request.setAttribute("rutaFichero", filezip.getPath());
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK", "OK");

			} else {
				ClsLogging.writeFileLog("No hay ficheros que descargar ", 3);
			}
			if (!errores.toString().equals(""))
				request.setAttribute("avisoFicherosNoGenerado", errores.toString());
			return "descarga";

		} catch (Exception e) {
			this.throwExcp("messages.general.error", new String[] { "modulo.envios" }, e, tx);
		}
		return "exception";
	}
	
}
