/*
 * VERSIONES:
 * RGG 03/01/2007 Creación
 */

package com.siga.facturacion.action;

import java.io.File;
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
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.informes.form.MantenimientoInformesForm;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.form.DevolucionesManualesForm;


/**
 * Clase Action de struts para el mantenimiento de devoluciones manuales<b>
 * Implementa las diferentes acciones del caso de uso.
 * @author RGG AtosOrigin
 */
public class DevolucionesManualesAction extends MasterAction{

	/**
	 * Implementa la accion de mostrar el mantenimiento de devoluciones manuales, con el form inicializado  
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form=(DevolucionesManualesForm) formulario;
			
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
			FacMotivoDevolucionAdm motivosAdm = new FacMotivoDevolucionAdm(this.getUserBean(request));
			Vector motivos = motivosAdm.select();
			if (motivos==null || motivos.size()==0) {
				form.setHayMotivos("0");
			} else {
				form.setHayMotivos("1");
			}

			// limpio la session
			request.getSession().removeAttribute("FacturaVolver");

		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "abrir";
	}
	
	/**
	 * Implementa la accion de mostrar el mantenimiento de devoluciones manuales  
	 */
	protected String abrirConParametros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form=(DevolucionesManualesForm) formulario;
			// DevolucionesManualesForm form = (DevolucionesManualesForm)request.getSession().getAttribute("DevolucionesManualesForm");
			
			// obtenemos el nombre del titular
			if (!form.getTitular().equals("")) {
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				form.setNombreTitular(personaAdm.obtenerNombreApellidos(form.getTitular()));
			}

			// comprobamos la existencia de Motivos
			FacMotivoDevolucionAdm motivosAdm = new FacMotivoDevolucionAdm(this.getUserBean(request));
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
			
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "abrir";
	}
	
	/**
	 * Implementa la accion de crear el fichero de devoluciones manuales e insertarlo en BBDD tras obtener las cabeceras  
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{

		UserTransaction tx 	= null;

		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form=(DevolucionesManualesForm) formulario;
			
			// obtengo los datos para generar el fichero
			String aplicaComisiones = form.getAplicarComisiones();
			String banco = form.getBanco();
			String fechaDevolucion = form.getFechaDevolucion();
			String recibos = form.getRecibos();

			
			
			// Comienzo control de transacciones
			tx = user.getTransactionPesada(); 			

			// Comienzo la transaccion
			tx.begin();		
			
			// genero el fichero
	 		FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(this.getUserBean(request));
	 		String identificador = devolucionesAdm.getNuevoID(idInstitucion).toString();

		    // Generación del fichero
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp 	 = new ReadProperties("SIGA.properties");			
		    String rutaServidor  = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + rp.returnProperty("facturacion.directorioDevolucionesJava");
	 		
		    String barra 	= "";
    		if (rutaServidor.indexOf("/") > -1) barra = "/"; 
    		if (rutaServidor.indexOf("\\") > -1) barra = "\\";        		
    		rutaServidor 	+= barra + idInstitucion ;
		    
	 		String nombreFichero = rutaServidor+ barra +"Manual-"+UtilidadesString.formatoFecha(form.getFechaDevolucion(),"dd/MM/yyyy","ddMMyy")+"-"+form.getBanco()+"-"+identificador+".d19";

			FacFacturaIncluidaEnDisqueteAdm facdisq = new FacFacturaIncluidaEnDisqueteAdm(user);
			File fichero = facdisq.crearFicheroDevoluciones(banco, fechaDevolucion, aplicaComisiones, recibos, idInstitucion, identificador, nombreFichero);
	
			if (fichero!=null) {
				// actualizo mediante el fichero. COntrol de codigos de errores segun la funcion.
				DevolucionesAction devoluciones = new DevolucionesAction();
				String ret = devoluciones.actualizacionTablasDevoluciones(idInstitucion,rutaServidor,fichero.getName(),user.getLanguageInstitucion(),user.getUserName());
				if (ret.equals("0")) {
					// Aplicacion de comisiones
					if (aplicaComisiones!=null && aplicaComisiones.equalsIgnoreCase(ClsConstants.DB_TRUE)){
				    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion="+identificador,8);
				    	Facturacion facturacion = new Facturacion(this.getUserBean(request));
				    	boolean ok=facturacion.aplicarComisiones(idInstitucion,identificador,aplicaComisiones,this.getUserBean(request));
				    	if (!ok) {
				    		throw new ClsExceptions("Fichero de devoluciones manuales: Error al aplicar devoluciones.");
				    	}
				    }
				} else	if (ret.equals("5397")) {
					throw new ClsExceptions("Fichero de devoluciones manuales: El fichero no se ha encontrado.");
				} else 	if (ret.equals("5402")) {
					throw new ClsExceptions("Fichero de devoluciones manuales: Formato del fichero incorrecto.");
				} else 
				{
					throw new ClsExceptions("Fichero de devoluciones manuales: Error en el proceso de actualicacion de tablas de devolucion. RETORNO: "+ret);
				}  
			} else {
				throw new ClsExceptions("Problemas al generar el fichero de devoluciones manuales.");
			}
			tx.commit();				


			
			// RGG 04/01/2007: Si se decide descargar el fichero ademas de procesarlo, se debe descargar fichero
			// mediante el proceso de download parecido a lo que ocurre en las descarga de cartas a
			// interesados de EJG. Este metodo primero muestra un mensaje en una jsp y es la jsp quien 
			// finalmente llama mediante el nombre y path del fichero al download. Aunque en justicia 
			// gratuita esta fallando ahora mismo no parece tener relacion con esa manera de trabajar.
			request.setAttribute("generacionOK","1");
			//request.setAttribute("rutaFichero",fichero.getPath());
			//request.setAttribute("borrarFichero","false");
			
			// RGG 08/02/2007 Fianlmente se va a descargar los ficheros desde la ventana de devoluciones por fichero.
		}catch (SIGAException e) { 
			throw e;
		
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		} 	

		return "exitoDevolucion";
		
	}

	/**
	 * Implementa la accion de procesar las devoluciones manuales mediante el paso previo de obtener por pantalla los datos de cabecera de devolucion.  
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form=(DevolucionesManualesForm) formulario;

			// inicializo el form para esta pantalla
			form.setAplicarComisiones("");
			form.setBanco("");
			form.setFechaDevolucion("");
			
			// AQUI METER SI LA INSTITUCION TIENE COMISION
			CenInstitucionAdm admIns = new CenInstitucionAdm(this.getUserBean(request));
			String tiene = new Boolean(admIns.tieneProductoComision(idInstitucion)).toString();
			request.setAttribute("TIENEPRODUCTOCOMISION",tiene);

		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "nuevo";
	}


	/**
	 * Implementa la accion de buscar los recibos de banco de las facturas emitidas segun criterios de busqueda.  
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form=(DevolucionesManualesForm) formulario;
			
			
			// obtengo los recibos segun busqueda
			FacFacturaIncluidaEnDisqueteAdm recibosAdm = new FacFacturaIncluidaEnDisqueteAdm(this.getUserBean(request));
			 HashMap databackup=new HashMap();
				
				 	if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
				 		PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)databackup.get("paginador");
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
					
					
					
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
					
						
					
					
			  }else{	
					
			  	    databackup=new HashMap();
					
					//obtengo datos de la consulta 			
			  	  PaginadorCaseSensitive recibos = null;
				Vector datos = null;
			 recibos = recibosAdm.getRecibosParaDevolucion(idInstitucion,form.getFechaCargoDesde(),form.getFechaCargoHasta(),form.getNumeroRecibo(),form.getTitular(),form.getNumeroRemesa(), form.getNumeroFactura(), form.getDestinatario());
			databackup.put("paginador",recibos);
			if (recibos!=null){ 
			   datos = recibos.obtenerPagina(1);
			   databackup.put("datos",datos);
			   request.getSession().setAttribute("DATAPAGINADOR",databackup);
			} 
			  }
			//request.setAttribute("recibos",recibos);
			
			// obtengo el motivo por defecto de parametros.
			GenParametrosAdm param = new GenParametrosAdm(this.getUserBean(request));
			String motivo = param.getValor(idInstitucion,"FAC","MOTIVO_DEVOLUCION","8");
			request.setAttribute("motivoDevolucion",motivo);
			
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "listar";
	}

	/**
	 * Implementa la accion de ver la factura asociada  
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			DevolucionesManualesForm form=(DevolucionesManualesForm) formulario;

			// obtengo los datos OCULTOS: idfactura
			Vector vOcultos = (Vector)form.getDatosTablaOcultos(0);

			String idFactura = (String)vOcultos.get(0); 
			String accion = "ver";
			
			// preparo las pestanhas
			Hashtable datosFac = new Hashtable();
			UtilidadesHash.set(datosFac,"accion", accion);
			UtilidadesHash.set(datosFac,"idFactura", idFactura);
			UtilidadesHash.set(datosFac,"idInstitucion", idInstitucion);

			Hashtable datos = new Hashtable();
			UtilidadesHash.set(datos,"IDFACTURA", idFactura);
			UtilidadesHash.set(datos,"IDINSTITUCION", idInstitucion);
			FacFacturaAdm admf = new FacFacturaAdm(this.getUserBean(request));
			Vector v = admf.selectByPK(datos);
			if (v!=null && v.size()>0) {
			    FacFacturaBean b = (FacFacturaBean) v.get(0);
			    UtilidadesHash.set(datosFac,"idPersona", b.getIdPersona().toString());
			}

			
			// Paso de parametros a las pestanhas
			request.setAttribute("datosFacturas", datosFac);		
			
			// pongo de donde vengo para poder volver
			request.getSession().setAttribute("CenBusquedaClientesTipo","DEV_MANUAL");
			
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	

		return "verFactura";
	}


	/**
	 * Implementa la accion de descargar un fichero  
	 */
	protected String download (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		
		File fichero = null;
		String rutaFichero = null;
		MantenimientoInformesForm miform = null;
		
		try {
			//Obtenemos el formulario y sus datos:
			miform = (MantenimientoInformesForm)formulario;
			rutaFichero = miform.getRutaFichero();
			fichero = new File(rutaFichero);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFichero";	
	}
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

			//La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);						
			}else if (accion.equalsIgnoreCase("abrirConParametros")){
				mapDestino = abrirConParametros(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("buscar")){
				mapDestino = buscar(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("download")){
				mapDestino = download(mapping, miForm, request, response);					
			}else if (accion.equalsIgnoreCase("insertar")){
				mapDestino = insertar(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("modificar")){
				mapDestino = modificar(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("ver")){
				mapDestino = ver(mapping, miForm, request, response);
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}
	
}