/*
 * VERSIONES:
 * RGG 03/01/2007 Creación
 */

package com.siga.facturacion.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

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
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturaIncluidaEnDisqueteAdm;
import com.siga.beans.FacFacturaIncluidaEnDisqueteBean;
import com.siga.beans.FacLineaDevoluDisqBancoAdm;
import com.siga.beans.FacLineaDevoluDisqBancoBean;
import com.siga.beans.FacMotivoDevolucionAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.form.DevolucionesManualesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.form.MantenimientoInformesForm;

/**
 * Clase Action de struts para el mantenimiento de devoluciones manuales<b>
 * Implementa las diferentes acciones del caso de uso.
 * @author RGG AtosOrigin
 */
public class DevolucionesManualesAction extends MasterAction{
	// Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2) de la super clase(MasterAction)
	final String[] clavesBusqueda = {
			FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,
			FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,
			FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA,
			FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO,
			"IDMOTIVO"};

	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			//La primera vez que se carga el formulario o Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				DevolucionesManualesForm formDevoluciones = (DevolucionesManualesForm) miForm;
				formDevoluciones.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				formDevoluciones.reset(mapping,request);
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);						
				
			} else if (accion.equalsIgnoreCase("abrirConParametros")) {
				mapDestino = abrirConParametros(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("buscarInicio")) {
				DevolucionesManualesForm formDevoluciones = (DevolucionesManualesForm) miForm;
				formDevoluciones.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscar(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("download")) {
				mapDestino = download(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("insertar")) {
				mapDestino = insertar(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("modificar")) {
				mapDestino = modificar(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("ver")) {
				mapDestino = ver(mapping, miForm, request, response);
				
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
	 * Implementa la accion de mostrar el mantenimiento de devoluciones manuales, con el form inicializado  
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			DevolucionesManualesForm form = (DevolucionesManualesForm) formulario;
			
			// inicializo el form
			form.setAplicarComisiones("");
			form.setBanco("");
			form.setFechaCargoDesde("");
			form.setFechaDevolucion("");
			form.setNombreTitular("");
			form.setNombreDestinatario("");
			form.setFechaDevolucion("");
			form.setFechaCargoHasta("");
			form.setTitular("");
			form.setDestinatario("");
			form.setNumeroRecibo("");
			form.setNumeroRemesa("");
			form.setNumeroFactura("");
			form.setHayMotivos("");
			form.setRecibos("");
			
			// que no busque directamente
			request.removeAttribute("buscar");
						
			// comprobamos la existencia de Motivos
			FacMotivoDevolucionAdm motivosAdm = new FacMotivoDevolucionAdm(user);
			Vector motivos = motivosAdm.select();
			if (motivos==null || motivos.size()==0) {
				form.setHayMotivos("0");
			} else {
				form.setHayMotivos("1");
			}

			// limpio la session
			request.getSession().removeAttribute("FacturaVolver");

		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "abrir";
	}
	
	/**
	 * Implementa la accion de mostrar el mantenimiento de devoluciones manuales  
	 */
	protected String abrirConParametros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			DevolucionesManualesForm form = (DevolucionesManualesForm) formulario;

			// comprobamos la existencia de Motivos
			FacMotivoDevolucionAdm motivosAdm = new FacMotivoDevolucionAdm(user);
			Vector motivos = motivosAdm.select();
			if (motivos==null || motivos.size()==0) {
				form.setHayMotivos("0");
			} else {
				form.setHayMotivos("1");
			}

			// que SI busque directamente
			request.setAttribute("buscar","1");
			request.setAttribute("buscarPaginador","true");

			// limpio la session
			request.getSession().removeAttribute("FacturaVolver");
			
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "abrir";
	}
	
	/**
	 * Implemente la accion de la devolucion manual de una factura
	 * 
	 * Valores = idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx 	= null;

		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form = (DevolucionesManualesForm) formulario;
			
			// obtengo los datos para generar el fichero
			String aplicaComisiones = form.getAplicarComisiones();
			String fechaDevolucion = form.getFechaDevolucion();
			String fechaDevolucionHora = form.getFechaDevolucion(); 
			String recibos = form.getRecibos(); // idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...
			
			if (fechaDevolucion != null && !fechaDevolucion.equals("") && fechaDevolucion.length()==10) {
				try { 
					fechaDevolucionHora = GstDate.getApplicationFormatDate("", fechaDevolucion);
					fechaDevolucion = fechaDevolucion.substring(6,10) + fechaDevolucion.substring(3,5) + fechaDevolucion.substring(0,2); // AAAAMMDD 					
				} catch (Exception e){
					fechaDevolucionHora = "";
					fechaDevolucion = "";
				}		
			}			
			
			// Comienzo control de transacciones
			tx = user.getTransactionPesada(); 			

			// Comienzo la transaccion
			tx.begin();		

			// actualizo mediante el fichero. COntrol de codigos de errores segun la funcion.
			DevolucionesAction devoluciones = new DevolucionesAction();
			String[] retornoDevolucionManual = devoluciones.devolucionManual(idInstitucion, recibos, fechaDevolucion, user);
			
			if (retornoDevolucionManual[0].equals("0")) {

				// Aplicacion de comisiones
				if (aplicaComisiones!=null && aplicaComisiones.equalsIgnoreCase(ClsConstants.DB_TRUE)){
					
					String [] aListaIdDisquetesDevolucion = retornoDevolucionManual[2].split(";");
					for (String sIdDisquetesDevolucion : aListaIdDisquetesDevolucion) {
				    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion=" + sIdDisquetesDevolucion, 8);
				    	Facturacion facturacion = new Facturacion(user);
				    	
						// Identificamos los disquetes devueltos asociados al fichero de devoluciones
						FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(user);
						FacLineaDevoluDisqBancoBean beanDevolucion = admLDDB.obtenerDevolucionManual(idInstitucion, sIdDisquetesDevolucion);
						
						// Aplicamos la comision a cada devolucion
						facturacion.aplicarComisionAFactura (idInstitucion, beanDevolucion, aplicaComisiones, user, fechaDevolucionHora);
					}				
			    }
				
			} else 	if (retornoDevolucionManual[0].equals("5404")) {
				throw new SIGAException("facturacion.devolucionManual.error.fechaDevolucion");
								
			} else  {
				throw new ClsExceptions("Fichero de devoluciones manuales: Error en el proceso de actualicacion de tablas de devolucion. RETORNO: " + retornoDevolucionManual);
			}  
			
			tx.commit();				

		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.general.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);
		
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		} 	

		return exitoRefresco("messages.updated.success",request);
		
	}

	/**
	 * Implementa la accion de buscar los recibos de banco de las facturas emitidas segun criterios de busqueda.
	 * 
	 * Seleccionados = idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form = (DevolucionesManualesForm) formulario;
			boolean isSeleccionarTodos = form.getSeleccionarTodos()!=null && !form.getSeleccionarTodos().equals("");			
			
			ArrayList clavesRegSeleccinados = new ArrayList();
			String seleccionados = request.getParameter("Seleccion"); // idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...
			
			// Inicialmente pongo todos los elementos deseleccionados
			form.setRegistrosSeleccionados(new ArrayList());
			
			if (seleccionados!=null && !seleccionados.equals("")) {
				ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda, seleccionados, clavesRegSeleccinados);
				if (alRegistros != null) {
					clavesRegSeleccinados = alRegistros;
					form.setRegistrosSeleccionados(clavesRegSeleccinados);
				}
			}			
			
			// Obtiene los motivos de devolucion
			FacMotivoDevolucionAdm admMotivoDevolucion = new FacMotivoDevolucionAdm(user);
			Vector vMotivos = admMotivoDevolucion.obtenerMotivosDevolucion(user.getLanguage());
			request.setAttribute("vMotivos", vMotivos);		
			
			// obtengo el motivo por defecto de parametros.
			GenParametrosAdm admParametros = new GenParametrosAdm(user);
			String motivo = admParametros.getValor(idInstitucion,"FAC","MOTIVO_DEVOLUCION","8");
			request.setAttribute("motivoDevolucion", motivo);			
				
			HashMap<String,Object> databackup = form.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null && !isSeleccionarTodos){ 
				PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)databackup.get("paginador");
				Vector datos=new Vector();
					
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
					
				if (paginador!=null){	
					int paginaInt;
					try{
						paginaInt = Integer.parseInt(pagina);
					}catch (Exception e) {
						// Con esto evitamos un error cuando se recupera una pagina y hemos "perdido" la pagina actual cargamos la primera y no evitamos mostrar un error
						paginaInt = 1;
					}					
					
					if (pagina!=null){
						datos = paginador.obtenerPagina(paginaInt);
					} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina(paginador.getPaginaActual());
					}
				}	
					
				databackup.put("paginador", paginador);
				databackup.put("datos", datos);
					
			} else {						
				databackup = new HashMap();
				Vector datos = null;
				
				FacFacturaIncluidaEnDisqueteAdm admFacturaIncluidaEnDisquete = new FacFacturaIncluidaEnDisqueteAdm(user);
				PaginadorCaseSensitive recibos = admFacturaIncluidaEnDisquete.getRecibosParaDevolucion(idInstitucion,form.getFechaCargoDesde(),form.getFechaCargoHasta(),form.getNumeroRecibo(),form.getTitular(),form.getNumeroRemesa(), form.getNumeroFactura(), form.getDestinatario());
				databackup.put("paginador",recibos);
				
				if (recibos!=null){ 
					if(isSeleccionarTodos){
						clavesRegSeleccinados = new ArrayList((Collection) admFacturaIncluidaEnDisquete.selectGenericoNLS(recibos.getQueryInicio())); // Query completa
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);						
						form.setRegistrosSeleccionados(clavesRegSeleccinados);
						if (form.getSeleccionarTodos()!=null && !form.getSeleccionarTodos().equals("")) {
							datos = recibos.obtenerPagina(Integer.parseInt(form.getSeleccionarTodos()));
						} else {
							datos = recibos.obtenerPagina(1);
						}
						form.setSeleccionarTodos("");
						
					} else {
						datos = recibos.obtenerPagina(1);
					}
					databackup.put("datos",datos);
				}  
				form.setDatosPaginador(databackup);
			}		
			
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "listar";
	}

	/**
	 * Implementa la accion de ver la factura asociada  
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form = (DevolucionesManualesForm) formulario;

			// obtengo los datos OCULTOS: idfactura
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idFactura = (String)vOcultos.get(0); 
			
			// preparo las pestanhas
			Hashtable<String,String> datosFac = new Hashtable<String,String>();
			UtilidadesHash.set(datosFac,"accion", "ver");
			UtilidadesHash.set(datosFac,"idFactura", idFactura);
			UtilidadesHash.set(datosFac,"idInstitucion", idInstitucion);

			Hashtable<String,String> datos = new Hashtable<String,String>();
			UtilidadesHash.set(datos,"IDFACTURA", idFactura);
			UtilidadesHash.set(datos,"IDINSTITUCION", idInstitucion);
			
			FacFacturaAdm admf = new FacFacturaAdm(user);			
			Vector v = admf.selectByPK(datos);
			if (v!=null && v.size()>0) {
			    FacFacturaBean b = (FacFacturaBean) v.get(0);
			    UtilidadesHash.set(datosFac, "idPersona", b.getIdPersona().toString());
			}
			
			// Paso de parametros a las pestanhas
			request.setAttribute("datosFacturas", datosFac);		
			
			// pongo de donde vengo para poder volver
			request.getSession().setAttribute("CenBusquedaClientesTipo","DEV_MANUAL");
			
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "verFactura";
	}


	/**
	 * Implementa la accion de descargar un fichero  
	 */
	protected String download (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{		
		try {
			//Obtenemos el formulario y sus datos:
			MantenimientoInformesForm miform = (MantenimientoInformesForm)formulario;
			String rutaFichero = miform.getRutaFichero();
			File fichero = new File(rutaFichero);
			if (fichero==null || !fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		
		return "descargaFichero";	
	}
	
}