/**
 * Clase action para la visualizacion y mantenimiento de los datos de facturaci�n para una Instituci�n.<br/>
 * Gestiona la edicion, consulta y mantenimiento de la ejecuci�n de facturaciones.  
 */

package com.siga.facturacionSJCS.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ESTADO_FACTURACION;
import org.redabogacia.sigaservices.app.AppConstants.FCS_MAESTROESTADOS_ENVIO_FACT;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.FcsFacturacionEstadoEnvio;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActErrorCam;
import org.redabogacia.sigaservices.app.autogen.model.VPcajgAlcActErrorCam;
import org.redabogacia.sigaservices.app.services.caj.XuntaEnviosJEService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.fac.PcajgAlcActService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FcsEstadosFacturacionBean;
import com.siga.beans.FcsFactEstadosFacturacionAdm;
import com.siga.beans.FcsFactEstadosFacturacionBean;
import com.siga.beans.FcsFactGrupoFactHitoAdm;
import com.siga.beans.FcsFactGrupoFactHitoBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.FcsHitoGeneralAdm;
import com.siga.beans.FcsHitoGeneralBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.DatosGeneralesFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformePersonalizable;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;
import com.siga.ws.CajgConfiguracion;
import com.siga.ws.InformeXML;
import com.siga.ws.JustificacionEconomicaWS;
import com.siga.ws.i2003.je.AlcalaJE;

import es.satec.businessManager.BusinessManager;


public class DatosGeneralesFacturacionAction extends MasterAction {
	
	private static Boolean alguienEjecutando=Boolean.FALSE;
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		String accion = (String)request.getParameter("accion");
		try{
			if((accion != null)&&(accion.equalsIgnoreCase("nuevo"))){
				return mapping.findForward(this.nuevo(mapping, miForm, request, response));
			}
			if ((accion != null)&&((accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("consulta")))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("insertarCriterio"))){
				return mapping.findForward(this.insertarCriterio(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("nuevoCriterio"))){
				return mapping.findForward(this.nuevoCriterio(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("listaConsejo"))){
				return mapping.findForward(this.listaParaConsejo(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("ejecutarFacturacion"))){
				return mapping.findForward(this.ejecutarFacturacion(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("ejecutarRegularizacion"))){
				return mapping.findForward(this.ejecutarRegularizacion(mapping, miForm, request, response));
			}
			if ((miForm.getModo() != null) && (miForm.getModo().equalsIgnoreCase("download"))){
				return mapping.findForward(this.download(mapping, miForm, request, response));
			}
			if ((miForm.getModo() != null) && (miForm.getModo().equalsIgnoreCase("descargarLog"))){
				return mapping.findForward(this.descargarLog(mapping, miForm, request, response));
			}
			if ((accion != null)&&(accion.equalsIgnoreCase("downloadMultiple"))){
				return mapping.findForward(this.generarDescargarExcelFacturacionMultiple(mapping, miForm, request, response));
			}
			if ((accion != null)&&(accion.equalsIgnoreCase("descargarFichero"))){
				return mapping.findForward(this.descargarFichero(mapping, miForm, request, response));
			}
			if ((accion != null)&&(accion.equalsIgnoreCase("descargaFicheroGlobal"))){
				return mapping.findForward(this.descargarFichero(mapping, miForm, request, response));
			}
			if ((miForm.getModo() != null) && (miForm.getModo().equalsIgnoreCase("descargaFicheroFact"))){
				return mapping.findForward(this.descargarFicheroFact(mapping, miForm, request, response));
			}
			
			if ((miForm.getModo() != null) && (miForm.getModo().equalsIgnoreCase("descargarInformeIncidencias"))){
				return mapping.findForward(this.descargarInformeIncidencias(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("accionXuntaEnvioReintegro"))){
				return mapping.findForward(this.accionXuntaEnvioReintegro(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("accionXuntaEnvioJustificacion"))){
				return mapping.findForward(this.accionXuntaEnvioJustificacion(mapping, miForm, request, response));
			}
			
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("subirFicheroCAM"))){
				return mapping.findForward(this.subirFicheroCAM(mapping, miForm, request, response));
			}
			
			

			return super.executeInternal(mapping, formulario, request, response);
		}
		catch(SIGAException e){
			throw e;
		}
		catch(Exception e){
			return mapping.findForward("exception");
		}
	}
	
	private String subirFicheroCAM(ActionMapping mapping, MasterForm masterForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) masterForm;			
			FormFile formFile = miform.getFile();
			
			if (formFile.getFileSize() == 0){
				throw new SIGAException("message.cajg.ficheroValido"); 
			}
			
			//creamos el registro de cola para la operaci�n
			EcomCola ecomCola = new EcomCola();
			ecomCola.setIdinstitucion(Short.parseShort(miform.getIdInstitucion()));
			ecomCola.setIdoperacion(OPERACION.PCAJG_ALCALA_JE_FICHERO_ERROR.getId());
			
			
			Map<String, String> mapa = new HashMap<String, String>();
			mapa.put(PcajgAlcActErrorCam.C_IDFACTURACION, miform.getIdFacturacion());
			
			File rutaAlmacen = getRutaAlmacenFichero(miform.getIdInstitucion(), miform.getIdFacturacion());
			File file = new File(rutaAlmacen, formFile.getFileName());
			guardaFichero(file, formFile.getInputStream());
			
			mapa.put(AppConstants.PCAJG_ALC_CAM_PATH, file.getAbsolutePath());
			
			EcomColaService ecomColaService = (EcomColaService) getBusinessManager().getService(EcomColaService.class);
			ecomColaService.insertaColaConParametros(ecomCola, mapa);
						
			insertaEstadoFacturacion(request, miform.getIdInstitucion(), miform.getIdFacturacion());
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, null);
		}
		
		return exitoRefresco("messages.inserted.success.ficheroErrorActuacionesCAM", request);
	}

	private void insertaEstadoFacturacion(HttpServletRequest request, String idInstitucion, String idFacturacion) throws Exception {
		
		FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(this.getUserBean(request));
		String estadoActualFacturacion = admEstado.getIdEstadoFacturacion(idInstitucion, idFacturacion);
		
		//comprobamos que la facturacion se encuentra ejecutada o no validada o rechazada
		int estadoActualFac = Integer.parseInt(estadoActualFacturacion);
		
		if (estadoActualFac == ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo() ) {
			UserTransaction tx = getUserBean(request).getTransaction();
			tx.begin();
			FcsFactEstadosFacturacionBean beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdInstitucion(Integer.parseInt(idInstitucion));
			beanEstado.setIdFacturacion(Integer.parseInt(idFacturacion));
			beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo()));
			beanEstado.setFechaEstado("SYSDATE");
			beanEstado.setIdOrdenEstado(new Integer(admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion)));
			//Thread.sleep(1000);
			admEstado.insert(beanEstado);
			tx.commit();
		}
		
		
	}

	private File getRutaAlmacenFichero(String idInstitucion, String idFacturacion) {
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("gen.ficheros.path");
		
		File parentFile = new File(rutaAlmacen, "erroresActuacionesCAM");
		parentFile = new File(rutaAlmacen, idInstitucion);
		parentFile = new File(parentFile, idFacturacion);
		FileHelper.mkdirs(parentFile.getAbsolutePath());
		
		return parentFile;
	}
	
	private void guardaFichero(File file, InputStream is) throws Exception {
		OutputStream bos = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		
		is.close();
		bos.flush();
		bos.close();
	}

	private String accionXuntaEnvioJustificacion(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return accionXuntaEnvios(miForm, request, OPERACION.XUNTA_ENVIO_JUSTIFICACION);
	}

	private String accionXuntaEnvioReintegro(ActionMapping mapping,	MasterForm masterForm, HttpServletRequest request,	HttpServletResponse response) throws SIGAException {
		return accionXuntaEnvios(masterForm, request, OPERACION.XUNTA_ENVIO_REINTEGROS);
	}
	
	private String accionXuntaEnvios(MasterForm masterForm, HttpServletRequest request, OPERACION operacion) throws SIGAException {
		String mensajePantalla = "messages.facturacion.envioEnProceso";
		
		try {
			
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) masterForm;
			String idFacturacion = (String)miform.getIdFacturacion();
			String idinstitucion = miform.getIdInstitucion();
			
			XuntaEnviosJEService xuntaEnviosJEService = (XuntaEnviosJEService) BusinessManager.getInstance().getService(XuntaEnviosJEService.class);
			
			if (OPERACION.XUNTA_ENVIO_REINTEGROS.equals(operacion)) {
				xuntaEnviosJEService.envioReintegros(Short.valueOf(idinstitucion), Integer.valueOf(idFacturacion));
			} else if (OPERACION.XUNTA_ENVIO_JUSTIFICACION.equals(operacion)) {
				xuntaEnviosJEService.envioJustificacion(Short.valueOf(idinstitucion), Integer.valueOf(idFacturacion));
			} else {
				throw new IllegalArgumentException("Operaci�n no soportada. Revisar la operaci�n que debe realizarse");
			}
			
			
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("idFacturacion", idFacturacion);
			datosFacturacion.put("idInstitucion", idinstitucion);
	//		datosFacturacion.put("accion", "Edicion");
			request.setAttribute("datosFacturacion",datosFacturacion);
			
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"}, e, null);
		}
		
		return exitoRefresco(mensajePantalla,request);
	}

	/** 
	 *  Funcion que atiende la accion listaParaConsejo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String listaParaConsejo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//recogemos del formulario el idFacturacion
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		String idFacturacion = (String)miform.getIdFacturacion();
		UserTransaction  tx = null;
		String forward = null;
		String[] resultadoPL = null;
		String mensajePantalla = "messages.updated.success";
		
		try {
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
								
			FcsFactEstadosFacturacionAdm fcsFactEstadosFacturacionAdm = new FcsFactEstadosFacturacionAdm(usr);
			String estadoActualFacturacion = fcsFactEstadosFacturacionAdm.getIdEstadoFacturacion(miform.getIdInstitucion(), idFacturacion);
						
			
			//comprobamos que la facturacion se encuentra ejecutada o no validada o rechazada
			int estadoActualFac = Integer.parseInt(estadoActualFacturacion);
			
			if (estadoActualFac != ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo()												
					&& estadoActualFac != ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo()
					&& estadoActualFac != ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_DISPONIBLE.getCodigo()
					&& estadoActualFac != ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_ACEPTADO.getCodigo()
					) {
				throw new ClsExceptions("Ha ocurrido un error al cerrar la facturaci�n. No se puede cerrar la facturaci�n porque el estado actual no es correcto.");
			}
						
			int estadoFuturo = ESTADO_FACTURACION.ESTADO_FACTURACION_LISTA_CONSEJO.getCodigo();
			//SI TIENE CONFIGURADO EL WEBSERVICE HACEMOS LA LLAMADA
			InformeXML informeXML = JustificacionEconomicaWS.getInstance(miform.getIdInstitucion());
			if (informeXML != null) {
				informeXML.envioWS(miform.getIdInstitucion(), idFacturacion);
				estadoFuturo = ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_EN_PROCESO.getCodigo();
				mensajePantalla = "messages.facturacion.envioEnProceso";
			}
			
			tx = usr.getTransaction();
			tx.begin();			
			
			String idOrdenEstado = fcsFactEstadosFacturacionAdm.getIdordenestadoMaximo(miform.getIdInstitucion(), idFacturacion);
			FcsFactEstadosFacturacionBean fcsFactEstadosFacturacionBean = new FcsFactEstadosFacturacionBean();
			fcsFactEstadosFacturacionBean.setIdInstitucion(Integer.parseInt(miform.getIdInstitucion()));
			fcsFactEstadosFacturacionBean.setIdFacturacion(Integer.parseInt(idFacturacion));
			fcsFactEstadosFacturacionBean.setIdEstadoFacturacion(estadoFuturo);
			fcsFactEstadosFacturacionBean.setIdOrdenEstado(Integer.parseInt(idOrdenEstado));
			fcsFactEstadosFacturacionBean.setFechaEstado("SYSDATE");
			
			fcsFactEstadosFacturacionAdm.insert(fcsFactEstadosFacturacionBean);
			
			forward = exitoRefresco(mensajePantalla,request);//"refrescar";
			//le pasamos los parametros a las pestanhas
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("idFacturacion", idFacturacion);
			datosFacturacion.put("idInstitucion", (String)usr.getLocation());
			datosFacturacion.put("accion", "Edicion");
			request.setAttribute("datosFacturacion",datosFacturacion);

			tx.commit();
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		
		return forward;
	}
	
	
	/** 
	 *  Funcion que atiende la accion abrirAvanzada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
		
		Vector vHitos = new Vector();
		
		try{
			vHitos = (Vector)facturacionAdm.getCriteriosFacturacion(usr.getLocation(), miform.getIdFacturacion());
		}
		catch(Exception e) { }
		
		request.getSession().setAttribute("vHito", vHitos);
		request.setAttribute("modo","Edicion");
		return "refrescarCriterios";
	}
	
	/** 
	 *  Funcion que atiende la accion nuevoCriterio.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevoCriterio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		//Recuperamos el USRBEAN
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		try{
			//		Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
			request.setAttribute("nombreInstitucion", nombreInstitucion);
			request.setAttribute("idFacturacion", miform.getIdFacturacion());
			String aux = request.getParameter("prevision");
			if (aux!=null) {
				request.setAttribute("prevision", "S");
			}
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
		}
		catch(Exception e){}
		return "nuevoCriterio";
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
		String forward="inicio";
		
		//Recogemos los par�metros de las pestanhas 
		String idInstitucion = (String)request.getParameter("idInstitucion");
		String idFacturacion = (String)request.getParameter("idFacturacion");
		String accion = (String)request.getParameter("accion");
		String borrar = (String)request.getParameter("borrarFact");		
		//Datos de salida
		Vector resultado = new Vector(); //datos a mostrar
		Vector hitos = new Vector();
		Hashtable h=new Hashtable();
		boolean hayDetalle = false;
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//Creamos una clausula where que nos servir� para consultar por idFacturacion e idInstitucion
		String consultaFact = " where idInstitucion = " + idInstitucion +" and idFacturacion =" + idFacturacion + " ";
		
		try 
		{
			
			//Traemos de base de datos la factura seleccionada, con los datos recogidos de la pestanha
			FcsFacturacionJGAdm facturaAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			FcsFacturacionJGBean facturaBean = (FcsFacturacionJGBean)((Vector)facturaAdm.select(consultaFact)).get(0);
			
			if (facturaBean.getIdFacturacion_regulariza()!=null) {
				String consultaFact2 = " where idInstitucion = " + idInstitucion +" and idFacturacion =" + facturaBean.getIdFacturacion_regulariza() + " "; 
				FcsFacturacionJGBean facturaBeanOriginal = (FcsFacturacionJGBean)((Vector)facturaAdm.select(consultaFact2)).get(0);
				request.setAttribute("NOMBREFACTURACION", facturaBeanOriginal.getNombre());
				
			}
			
			if (facturaBean.getRegularizacion().equals(ClsConstants.DB_TRUE)) {
				request.setAttribute("FcsRegularizacion","1");
			}
			
			//Recuperamos el Estado de la Factura
			Hashtable nombreEstadoBean = (Hashtable)facturaAdm.getEstadoFacturacion(idInstitucion, idFacturacion);
			
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(idInstitucion);
			
			//Consultamos los criterios de facturacion
			Vector vHito = (Vector)facturaAdm.getCriteriosFacturacion(idInstitucion, idFacturacion);
			
			//Pasamos la factura seleccionada ,el modo, el nombre de la institucion ,
			//la descripcion del estado , el identificador de la partida presupuestaria,
			//el vector con los hitos a facturar y el vector de los grupos de facturacion
			request.getSession().setAttribute("DATABACKUP",facturaBean); 
			request.setAttribute("modo",accion);
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			request.setAttribute("estado",(String)nombreEstadoBean.get(FcsEstadosFacturacionBean.C_DESCRIPCION));
			request.setAttribute("fechaEstado",(String)nombreEstadoBean.get(FcsFactEstadosFacturacionBean.C_FECHAESTADO));
//			request.setAttribute("importe",partidaBean.getImportePartida().toString());
			//request.getSession().setAttribute("idFacturacion",((Integer)facturaBean.getIdFacturacion()).toString());
			//el idFacturacion, idInstitucion y modo
			request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idFacturacion",idFacturacion);
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			request.setAttribute("strutTrans", usr.getStrutsTrans());
			

			//esta variable es para mostrar en los criterios el boton de borrado
			request.getSession().setAttribute("estado",(String)nombreEstadoBean.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION));
			request.setAttribute("idEstado",UtilidadesHash.getInteger(nombreEstadoBean, FcsEstadosFacturacionBean.C_IDESTADOFACTURACION));
			
			//para los env�os de la Xunta tambi�n recuperamos el estado
			XuntaEnviosJEService xuntaEnviosJEService = (XuntaEnviosJEService) BusinessManager.getInstance().getService(XuntaEnviosJEService.class);						
			FCS_MAESTROESTADOS_ENVIO_FACT ultimoEstadoEnvio = xuntaEnviosJEService.getUltimoEstadoEnvioFacturacion(Short.valueOf(idInstitucion), Integer.valueOf(idFacturacion));
			
			request.setAttribute(FcsFacturacionEstadoEnvio.C_IDESTADOENVIOFACTURACION, ultimoEstadoEnvio);
			
			generarSeleccionInformeCAM(request, (DatosGeneralesFacturacionForm)formulario, idInstitucion, idFacturacion);
			
			// para ver si ya se ha ejecutado
			request.setAttribute("yaHaSidoEjecutada",new Boolean(facturaAdm.yaHaSidoEjecutada(idInstitucion,idFacturacion)));
			if(borrar !=null && borrar.equalsIgnoreCase("S"))
				request.setAttribute("borrar",new Boolean(true));
			else
				request.setAttribute("borrar",new Boolean(false));
			
		//para el iframe de abajo pasamos el modo por session, se borrar� en la jsp
			if (accion!=null)request.getSession().setAttribute("Modo",accion);
			request.setAttribute("vHito",vHito);
			
			
		//comprobando el estado de la facturacion
		try {
			String estado = (String) ((Hashtable) (new FcsFacturacionJGAdm(usr))
					.getEstadoFacturacion(idInstitucion, idFacturacion))
					.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION);
			
			if (estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo())) 
					|| estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_LISTA_CONSEJO.getCodigo()))
					|| estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_EN_PROCESO.getCodigo()))
					|| estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo()))
					|| estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_DISPONIBLE.getCodigo()))
					|| estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_ACEPTADO.getCodigo())))
				hayDetalle = true;
			else
				hayDetalle = false;
		}
		catch (Exception e) {
			hayDetalle = false;
		}
		
		if (hayDetalle){
			try 
			{
				//obteniendo los detalles de la facturacion
				resultado = (Vector) this.getDetalleFacturacion(idInstitucion, idFacturacion, request);
				
				//obteniendo la descripcion de los hitos
				hitos = (Vector)(new FcsHitoGeneralAdm(usr)).select();
				for(int i=0;i<hitos.size();i++)
				{
					FcsHitoGeneralBean bean=(FcsHitoGeneralBean)hitos.elementAt(i);
					h.put(bean.getIdHitoGeneral().toString(),bean.getDescripcion());
				}
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: DatosDetalleFacturacionAction"+e.getMessage(),e,3);
				resultado = new Vector();
			}
			catch(Exception e)
			{	
				ClsLogging.writeFileLogError("Error: DatosDetalleFacturacionAction"+e.getMessage(),e,3);
				throwExcp("Error: DatosDetalleFacturacionAction",e,null);
			}
		
			//pasamos el resultado por la request, y tambien el nombre de la instituci�n, y 
			request.getSession().setAttribute("resultado", resultado);
			request.getSession().setAttribute("hitos", h);
			request.getSession().setAttribute("hayDetalle", "1");
			if (usr.getStrutsTrans().equals("FCS_MantenimientoPrevisiones")) {
				request.getSession().setAttribute("prevision", "S");
			} else if (usr.getStrutsTrans().equals("CEN_MantenimientoFacturacion")) {
				request.getSession().setAttribute("prevision", "N");
			}
		}
		else {
			request.getSession().setAttribute("hayDetalle","0");
		}
			
			
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		} 									
		return forward;
	}
	
	private void generarSeleccionInformeCAM(HttpServletRequest request, DatosGeneralesFacturacionForm datosGeneralesFacturacionForm, String idInstitucion, String idFacturacion) throws Exception, ClsExceptions {
		
		if (idInstitucion != null && CajgConfiguracion.TIPO_CAJG_TXT_ALCALA == CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion))) {
			PcajgAlcActService pcajgAlcActService = (PcajgAlcActService) getBusinessManager().getService(PcajgAlcActService.class);
			List<VPcajgAlcActErrorCam> listaErroresCAM = pcajgAlcActService.getListaErroresCAMporFacturacion(Short.parseShort(idInstitucion), Integer.parseInt(idFacturacion));
			if (listaErroresCAM != null && listaErroresCAM.size() > 0) {
				request.setAttribute(VPcajgAlcActErrorCam.T_V_PCAJG_ALC_ACT_ERROR_CAM, listaErroresCAM);
				datosGeneralesFacturacionForm.setTipoFicheroCAMRadio(AlcalaJE.TIPO_FICHERO_CAM.NINGUNO.name());
			}
		}
		
	}

	protected Vector getDetalleFacturacion (String idInstitucion, String idFacturacion, HttpServletRequest request) throws ClsExceptions
	{
		//resultado final, vector de Hashtables
		Vector resultado = new Vector();
		FcsFacturacionJGAdm facturacionJGAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		
		try{
			
			// obtenemos los valores de los importes de oficio, guardia, soj y ejg
			Hashtable claves = new Hashtable();
			claves.put(FcsFacturacionJGBean.C_IDFACTURACION,idFacturacion);
			claves.put(FcsFacturacionJGBean.C_IDINSTITUCION,idInstitucion);
			resultado = facturacionJGAdm.selectByPK(claves);
			
			
		}catch(Exception e){
			com.atos.utils.ClsLogging.writeFileLogError("Consulta en DatosDetalleFacturacionAction.getDetalleFacturacion SQL:"+e.getMessage(),e, 3);
		}
		
		
		return resultado;
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
		return mapSinDesarrollar;
	}
	
	/** 
	 * Funcion que implementa la accion ejecutarFacturacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ejecutarFacturacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		String salida = "";
		
		try {			
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
			String idFacturacion = miform.getIdFacturacion();
			String idInstitucion = usr.getLocation();
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(usr);
			
			//Si no se incluido ninguna agrupaci�n
			if(request.getSession().getAttribute("existeAgrupacion").toString().contentEquals("N")){
				throw new SIGAException("messages.facturacionSJCS.no.incluida.agrupacion");
			}
			
			//CR7 - Al igual que en Guardar se comprueba que la facturacion no se solape con otra ya existente
			FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
			facturaPrueba.setFechaDesde((String)miform.getFechaInicio());
			facturaPrueba.setFechaHasta((String)miform.getFechaFin());
			facturaPrueba.setIdInstitucion(Integer.valueOf(idInstitucion));
			facturaPrueba.setIdFacturacion(Integer.valueOf(idFacturacion));
			Integer valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			boolean solapamiento = facturacionAdm.existeFacturacionMismoPerdiodo(facturaPrueba,null,valorPcajgActivo);
			
			//Si se solapan, paramos la insercion y volvemos avisando del error sin refrescar:
			if (solapamiento) {
			    salida = exito("messages.factSJCS.facturacionMismoPeriodo",request);
			
			} else {			
				// CR7 - Guardamos los datos de fechas y nombre antes de ejecutar la facturacion
				tx = usr.getTransaction();
				tx.begin();
				Hashtable datosEntrada = (Hashtable)miform.getDatos();
				FcsFacturacionJGBean facturacionOldBean = (FcsFacturacionJGBean)request.getSession().getAttribute("DATABACKUP");
				facturacionOldBean.setNombre((String)datosEntrada.get("NOMBRE"));
				facturacionOldBean.setFechaDesde(GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHADESDE")));
				facturacionOldBean.setFechaHasta(GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHAHASTA")));
				facturacionAdm.update(facturacionOldBean);
				tx.commit();
				
				FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(usr);
				String  estado = (String) ((Hashtable) (new FcsFacturacionJGAdm(usr)).getEstadoFacturacion(idInstitucion, idFacturacion)).get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION);
				if (estado!=null && estado.equals(String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo())) && usr.getStrutsTrans().equalsIgnoreCase("CEN_MantenimientoFacturacion")){
						volverGenerarFacturacion ( mapping,  formulario,  request,  response, tx);
				}
							
				String idOrdenEstado= admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion);		
				
				tx = usr.getTransactionPesada();
				tx.begin();
				// admFac.ejecutarFacturacion(idInstitucion,idFacturacion,tx);			
				FcsFactEstadosFacturacionBean beanEstado = null;
				beanEstado = new FcsFactEstadosFacturacionBean();
				beanEstado.setIdInstitucion(new Integer(idInstitucion));
				beanEstado.setIdFacturacion(new Integer(idFacturacion));
				beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_PROGRAMADA.getCodigo()));
				beanEstado.setFechaEstado("SYSDATE");
				beanEstado.setIdOrdenEstado(new Integer(idOrdenEstado));
				admEstado.insert(beanEstado);
				tx.commit();
			    // Notificaci�n
				request.setAttribute("modal", null);
				SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoFacturacionSJCS);
				
				salida = this.exitoRefresco("messages.facturacionSJCS.programada",request);
				request.getSession().removeAttribute("existeAgrupacion");
			}
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		
		return salida;
	}
	
	
	protected void volverGenerarFacturacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, UserTransaction tx)
			throws SIGAException
	{
		// Controles generales
		UsrBean usr = this.getUserBean(request);
		FcsFacturacionJGAdm fcsFacturacionJGAdm = new FcsFacturacionJGAdm(usr);

		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;
		Vector criteriosFacturacion;
		
		try {
			// obteniendo los datos del formulario
			String idInstitucion = miform.getIdInstitucion();
			String idFacturacion = miform.getIdFacturacion();

			// guardando de forma temporal los criterios de la facturacion
			criteriosFacturacion = guardarCriterio( mapping,  formulario,  request,  response);
			
			// borrando toda la facturacion
			fcsFacturacionJGAdm.borrarFacturacion(idInstitucion, idFacturacion, usr);

			//insertando el registro de la facturacion
			insertar(mapping, formulario, request, response);
			
			// volviendo a anyadir los criterios de la facturacion que se borraron
			if (criteriosFacturacion!=null && criteriosFacturacion.size()>0) {
				FcsFactGrupoFactHitoBean grupoFactHitoBean;
				for (int i = 0; i < criteriosFacturacion.size(); i++) {
					grupoFactHitoBean = (FcsFactGrupoFactHitoBean) criteriosFacturacion.get(i);
					miform.setHito(grupoFactHitoBean.getIdHitoGeneral().toString());
					miform.setGrupoF(grupoFactHitoBean.getIdGrupoFacturacion().toString());
					insertarCriterio (mapping, miform, request, response);	
				}	
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, tx);
		}
	} // volverGenerarFacturacion()
	
	protected Vector guardarCriterio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		FcsFactGrupoFactHitoAdm criterioAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));
		
		UserTransaction tx = null;
		Vector resultadoConsulta = null;
		try{
		    if (!usr.getStrutsTrans().equalsIgnoreCase("FCS_MantenimientoPrevisiones")) {
		    	resultadoConsulta = FcsFactGrupoFactHitoAdm.guardarCriterio((String) miform.getIdFacturacion(), (String) usr.getLocation());
			}
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return resultadoConsulta;
	}
	
	/** 
	 *  Funcion que implementa la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
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
		
		String forward="inicio";
		String nombreInstitucion = "";
		try{
			//Recogemos el UsrBean
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			request.setAttribute("strutTrans", usr.getStrutsTrans());	
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//pasamos por el request el modo y el nombre De la institucion
		request.setAttribute("modo","nuevo");
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		return forward;
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
		
		UserTransaction tx = null;
		DatosGeneralesFacturacionForm miform = null;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		FcsFactEstadosFacturacionAdm estadosAdm = new FcsFactEstadosFacturacionAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Hashtable datos = new Hashtable(); 
		String forward="error";
		try
		{
			miform = (DatosGeneralesFacturacionForm)formulario;
			datos = (Hashtable)miform.getDatos();

			tx = usr.getTransaction();
			tx.begin();

			facturacionAdm.insertar(datos, usr, usr.getLocation(), miform.getFechaInicio(), miform.getFechaFin(), usr.getUserName());
			
			tx.commit();
			
			//pasamos los datos a la pestanha
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("idFacturacion", (String)datos.get("IDFACTURACION"));
			datosFacturacion.put("idInstitucion", (String)usr.getLocation());
			datosFacturacion.put("accion", "Edicion");
			request.setAttribute("datosFacturacion",datosFacturacion);
			forward= "exitoFacturacion";
			request.setAttribute("mensaje","messages.inserted.success");
			
		}
		catch (Exception e)
		{
			throwExcp("messages.general.error", new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		
		//return "refrescar";
		return forward;
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
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		Hashtable datos = (Hashtable)miform.getDatos();
		String forward = null;
		UserTransaction tx = null;
		try 
		{
		    tx = usr.getTransaction();
		    
			FcsFacturacionJGBean facturacionOldBean = (FcsFacturacionJGBean)request.getSession().getAttribute("DATABACKUP");
			
			tx.begin();
			
			//preparamos la nueva facturacion
			Hashtable datosEntrada = (Hashtable)miform.getDatos();
			
			facturacionOldBean.setNombre((String)datosEntrada.get("NOMBRE"));
			facturacionOldBean.setFechaDesde(GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHADESDE")));
			facturacionOldBean.setFechaHasta(GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHAHASTA")));
			
			//comprobamos que la facturacion no se solape con otra ya existente
			FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
			facturaPrueba.setFechaDesde((String)miform.getFechaInicio());
			facturaPrueba.setFechaHasta((String)miform.getFechaFin());
			facturaPrueba.setIdInstitucion(Integer.valueOf((String)usr.getLocation()));
			facturaPrueba.setIdFacturacion(Integer.valueOf((String)datos.get("IDFACTURACION")));
			Integer valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			boolean solapamiento = facturacionAdm.existeFacturacionMismoPerdiodo(facturaPrueba,null,valorPcajgActivo);
			
			//Si se solapan, paramos la insercion y volvemos avisando del error sin refrescar:
			if (solapamiento) 
			    forward = exito("messages.factSJCS.facturacionMismoPeriodo",request);
			//Si no hay solapamiento modificamos:
			else {	
				//actualizamos
//				facturacionAdm.update(facturacionNew, facturacionOld);
				facturacionAdm.update(facturacionOldBean);
				
				//para despues de modificar, sino no se recoge desde la pestanha el modo, idFacturacion ni idInstitucion (de las pestanhas)
				request.getSession().setAttribute("Modo","edicion");
				request.getSession().setAttribute("idFacturacion",(String)miform.getIdFacturacion());
				request.getSession().setAttribute("idInstitucion",(String)miform.getIdInstitucion());
				forward = exitoRefresco("messages.updated.success",request);
			}
			tx.commit();
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
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
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		try 
		{
			if (ocultos!=null)
			{
				return this.borrarCriterio(mapping, formulario, request, response);
			}
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}					
		return exitoRefresco("messages.deleted.error", request);					
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
		
		String forward="listar";
		
		try 
		{
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}
		return (forward);
	}
	
	
	/** 
	 *  Funcion que implementa la accion borrarCriterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrarCriterio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Hashtable aBorrar = new Hashtable();
		UserTransaction tx = null;
		try{
		    tx = usr.getTransaction();
		    tx.begin();
			//preparamos el registro a borrar
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDINSTITUCION, usr.getLocation());
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDFACTURACION, (String)ocultos.get(0));
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION, (String)ocultos.get(1));
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDHITOGENERAL, (String)ocultos.get(2));
			
			//borramos
			FcsFactGrupoFactHitoAdm criterioAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));
			criterioAdm.delete(aBorrar);
			tx.commit();
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		return exitoRefresco("messages.deleted.success",request);
	}
	
	/** 
	 *  Funcion que implementa la accion insertarCriterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertarCriterio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		FcsFactGrupoFactHitoAdm criterioAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));
		
		UserTransaction tx = null;
		try{
		    tx = usr.getTransaction();
		    tx.begin();
		    
			// vemos si venimos de previsiones
		    String prevision = "";
		    if (usr.getStrutsTrans().equalsIgnoreCase("FCS_MantenimientoPrevisiones")) {
				prevision = "S";
			} else if (usr.getStrutsTrans().equalsIgnoreCase("CEN_MantenimientoFacturacion")) {
				prevision = "N";	
			}
			
			
			//preparamos el nuevo registro
			FcsFactGrupoFactHitoBean beanCriterio = new FcsFactGrupoFactHitoBean();
			beanCriterio.setFechaMod("sysdate");
			beanCriterio.setIdFacturacion(new Integer(miform.getIdFacturacion()));
			beanCriterio.setIdGrupoFacturacion(new Integer(miform.getGrupoF()));
			beanCriterio.setIdHitoGeneral(new Integer(miform.getHito()));
			beanCriterio.setIdInstitucion(new Integer(usr.getLocation()));
			beanCriterio.setUsuMod(new Integer(usr.getUserName()));
			
			
			
			Hashtable nuevoCriterio = new Hashtable();
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_FECHAMODIFICACION, "sysdate");
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDFACTURACION, (String)miform.getIdFacturacion());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION, (String) miform.getGrupoF());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDHITOGENERAL, (String) miform.getHito());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDINSTITUCION, (String)usr.getLocation());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_USUMODIFICACION, (String)usr.getUserName());
			if(miform.getConvenio()!=null)
				nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_FACTCONVENIO, (String) miform.getConvenio());
			
			Integer valorPcajgActivo = null;
			// RGG 25/04/2005 Si venimos de prevision no comprobamos esto
			if (prevision==null || !prevision.equals("S")) {
				valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
				
				//comprobamos que no exista un criterio igual
				String consulta = " where " + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + "=" + (String) miform.getGrupoF() +
				" and " + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + "=" + (String) miform.getHito() + 
				" and " + FcsFactGrupoFactHitoBean.C_IDFACTURACION + "=" + (String) miform.getIdFacturacion() +
				" and " + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + "=" + (String) usr.getLocation();
				if(valorPcajgActivo==CajgConfiguracion.TIPO_CAJG_TXT_ALCALA && miform.getConvenio()!=null && !miform.getConvenio().equalsIgnoreCase("")) {
					consulta += " and nvl(" + FcsFactGrupoFactHitoBean.C_FACTCONVENIO + ", "+(String)miform.getConvenio()+")=" + (String)miform.getConvenio();
				}
				Vector resultadoConsulta = criterioAdm.select(consulta);
				if (resultadoConsulta.size()>0)
					throw new SIGAException("messages.factSJCS.error.insertarCriterioFacturacion"); 
			}
			
			//comprobamos que la facturacion no se solape con otra ya existente
			FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			
			//consultamos la facturacion
			String consultaFact = " where idinstitucion=" + (String)usr.getLocation() + " and idfacturacion = " + (String) miform.getIdFacturacion() + " ";
			facturaPrueba = (FcsFacturacionJGBean)((Vector)facturacionAdm.select(consultaFact)).get(0);
			
			// RGG 25/04/2005 Si venimos de prevision no comprobamos esto
			if (prevision==null || !prevision.equals("S")) {
				if(valorPcajgActivo==null)
					valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
				if(valorPcajgActivo==CajgConfiguracion.TIPO_CAJG_TXT_ALCALA && miform.getConvenio()!=null && !miform.getConvenio().equalsIgnoreCase("") ) {
					beanCriterio.setFactConvenio(Short.valueOf(miform.getConvenio()));
				}
				
				boolean solapamiento = facturacionAdm.existeFacturacionMismoPerdiodo(facturaPrueba,beanCriterio,valorPcajgActivo);
				
				//si se solapan, paramos la insercion
				if (solapamiento) 
					throw new SIGAException("messages.factSJCS.facturacionMismoPeriodo"); 
			}
			
			//insertamos
			criterioAdm.insert(nuevoCriterio);
			
			tx.commit();
			
			//pasamos el modo que se borrar� en la p�gina de consultas criterios
			request.getSession().setAttribute("Modo","edicion");
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return exitoModal("messages.inserted.success",request);
	}
	
	
	/** 
	 * Funcion que implementa la accion ejecutarRegularizacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ejecutarRegularizacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;
		String salida = "";
		
		try 
		{ 
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(usr);
			
			tx = usr.getTransactionPesada();
			
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
			String idFacturacion = miform.getIdFacturacion();
			String idInstitucion = usr.getLocation(); 
			String idOrdenEstado= admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion);
			tx.begin();
			// admFac.ejecutarFacturacion(idInstitucion,idFacturacion,tx);
			
			FcsFactEstadosFacturacionBean beanEstado = null;
			beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdInstitucion(new Integer(idInstitucion));
			beanEstado.setIdFacturacion(new Integer(idFacturacion));
			beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_PROGRAMADA.getCodigo()));
			beanEstado.setFechaEstado("SYSDATE");
			beanEstado.setIdOrdenEstado(new Integer(idOrdenEstado));
			admEstado.insert(beanEstado);
			tx.commit();
		    // Notificaci�n
			
			SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapidoFacturacion);
			
			salida = this.exitoRefresco("messages.facturacionSJCS.programadaRegularizacion",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}					
		return salida;
	}
	
	
	/**
	 * Metodo que implementa el modo download
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String download (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		File fichero = null;
		String sNombreFichero = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// borro el formulario en session de Avanzada
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;
			
			String idFacturacion = "" + miform.getIdFacturacionDownload();
			String idInstitucion = "" + miform.getIdInstitucionDownload();
			String idPago 		 = "" + miform.getIdPagoDownload();
			String idPersona     = "" + miform.getIdPersonaDownload();
			
			String tipoConcepto = miform.getTipoDownload();
			if (tipoConcepto == null) {
				throw new SIGAException("Par�metro obligatorio");
			}
			
			Hashtable nombresFichero = null;
			
			if((idPago == null || idPago.equals("")) && (idPersona == null || idPersona.equals(""))) { 
				nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(new Integer(idInstitucion), new Integer(idFacturacion), user);
			}
			else {
				if((idPago != null || !idPago.equals("")) && (idPersona == null || idPersona.equals(""))) {
					nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosPago(new Integer(idInstitucion), new Integer(idFacturacion), new Integer(idPago), null, user);
				}
				else {
					nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosPago(new Integer(idInstitucion), new Integer(idFacturacion), new Integer(idPago), new Long(idPersona), user);
				}
			}
			
			
			if (tipoConcepto.equalsIgnoreCase("O")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_TURNO); 	
			} 
			else if (tipoConcepto.equalsIgnoreCase("G")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_GUARDIA); 	
			} 
			else if (tipoConcepto.equalsIgnoreCase("E")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_EJG); 	
			} 
			else if (tipoConcepto.equalsIgnoreCase("S")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_SOJ); 	
			}
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String sDirectorio = paramAdm.getValor(user.getLocation(),"FCS","PATH_PREVISIONES",null);
			
			String sNombreCompletoFichero = sDirectorio + File.separator + sNombreFichero;
			fichero = new File(sNombreCompletoFichero);
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
	
	/**
	 * Genera los ficheros excel resultado de la union de los datos de las facturaciones cuyas fechas desde/hasta
	 * est�n coprendidas en las fechas desde/hasta de las facturaciones inicial y final.
	 * Muestra la pantalla de descarga de esos ficheros
	 *
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String descargarLog (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{

		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;
			String idInstitucion =  miform.getIdInstitucion();
			String idFacturacion =  miform.getIdFacturacion();

			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String pathFicheros = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES_BD", null);

			String sNombreFichero = pathFicheros + File.separator + "LOG_ERROR_" + idInstitucion + "_" + idFacturacion + ".log";

			//Genera el zip de los ficheros generados enteriormente
			File fichero=new File(sNombreFichero);
			
			//Se pasan los parametros necesarios al servlet de descarga
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero","true");
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);	
		}
		return "descargaFichero";
	}

	protected String generarDescargarExcelFacturacionMultiple (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{

		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;

		String idInstitucion = user.getLocation(); 
		String idFacturacionIni = miform.getIdFacturacionIniDownload().toString(); 
		String idFacturacionFin = miform.getIdFacturacionFinDownload().toString();

		try {
			//Genera los ficheros excel para el intervalo de facturaciones
			UtilidadesFacturacionSJCS.generarFicherosFacturacionMultiple(new Integer(idInstitucion), new Integer(idFacturacionIni), new Integer(idFacturacionFin), user);
			
			//Genera el zip de los ficheros generados enteriormente
			File fichero = creaZipFacturacionMultiple(idInstitucion, idFacturacionIni, idFacturacionFin, user);
			
			//Se pasan los parametros necesarios al servlet de descarga
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero","true");
			
		} catch (ClsExceptions e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);	}
		return "descargaFicheroPrevio";
	}

	
	/**
	 * Crea el zip de los ficheros de facturaci�n generados para las facturaciones entre
	 * <code>idFacturacionIni</code> y <code>idFacturacionFin</code>
	 * 
	 * @param idInstitucion
	 * @param idFacturacionIni
	 * @param idFacturacionFin
	 * @throws ClsExceptions
	 */
	private File creaZipFacturacionMultiple(String idInstitucion, String idFacturacionIni, String idFacturacionFin, UsrBean user) throws ClsExceptions	{
		ClsLogging.writeFileLog("DatosGeneralesFacturacionAction.creaZipFacturacionMultiple",10);
		
		File ficZip = null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;

		GenParametrosAdm paramAdm = new GenParametrosAdm(user);
		String sPathFicheros = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES_BD", null);

		try {
			File pathFicheros = new File(sPathFicheros);
			//Se recuperan s�lo los ficheros del intervalo de facturaci�n
			String aux = "_" + idInstitucion + "_" + idFacturacionIni + "-" + idFacturacionFin;
			final String aux2 = aux + ".XLS";
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.matches(".*"+aux2);
				}
			};
			File [] ficherosPDF = pathFicheros.listFiles(filter);
			ficZip = new File(sPathFicheros +  File.separator +"FACTURACION_MULTIPLE" + aux + ".zip");
			if (ficherosPDF != null && ficherosPDF.length > 0) {
	
				if (ficZip.exists()) {
				    ficZip.delete();
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: el fichero zip ya existia. Se elimina",10);
				}
				
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<ficherosPDF.length; i++)
				{
				    File auxFile = (File)ficherosPDF[i];
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: fichero numero "+i+" longitud="+auxFile.length(),10);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(auxFile.getName());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						
						fis.close();
						outTemp.closeEntry();
					}
					//elimina el fichero temporal con las facturaciones concatenadas
					auxFile.delete();
				}
			    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: ok ",10);
				
				outTemp.close();
			}
			else{
				return null;
			}
			
		} catch (FileNotFoundException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} catch (IOException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		}
		finally {
			try {
				if (outTemp != null)
					outTemp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ficZip;
	}

	/**
	 * Metodo que permite la descarga de un fichero.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String descargarFichero (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		
		try {
			//Obtenemos el formulario y sus datos:
			String nombreFichero = (String)request.getParameter("nombreFichero");
			String rutaFichero = (String)request.getParameter("rutaFichero");
			File fichero = new File(rutaFichero);
			
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", rutaFichero);
			
			
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFichero";	
	}
	
	private String descargarInformeIncidencias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
			String idFacturacion = (String)miform.getIdFacturacion();
			File file = InformeXML.getFileInformeIncidencias(miform.getIdInstitucion(), idFacturacion);			

			if(file==null || !file.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}

			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getPath());
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		return "descargaFichero";
	}
	
	
	
	protected String descargarFicheroFact(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {		
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;
			FcsFacturacionJGAdm fact = new FcsFacturacionJGAdm(user); 
			String idInstitucion =  miform.getIdInstitucion();
			String idFacturacion =  miform.getIdFacturacion();
			Vector vf = fact.select("where idInstitucion ="+idInstitucion+" and idfacturacion="+idFacturacion);
			FcsFacturacionJGBean beanOriginal = (FcsFacturacionJGBean) vf.get(0);	
			String nombreFichero = beanOriginal.getNombreFisico();
			InformePersonalizable informePersonalizable = new InformePersonalizable();
			File fichero = null;
			
			if (isAlguienEjecutando()){			
				throw new SIGAException(UtilidadesString.getMensajeIdioma(user,"mensaje.error.facturacionsjcs.wait"));			
			}
				
			try {
				ArrayList filtrosInforme = fact.getFiltrosInforme(idInstitucion,idFacturacion);				
				fichero=new File(nombreFichero);
				
				//Si el nombre f�sico del dichero no se ha guardado antes, se actualiza en bbdd
				if(fichero==null || !fichero.exists()){				
					informePersonalizable.setIdFacturacion(idFacturacion);
					informePersonalizable.setTipoFicheroCAM(miform.getTipoFicheroCAMRadio());
					fichero = informePersonalizable.getFicheroGenerado(user,  InformePersonalizable.I_INFORMEFACTSJCS,null, filtrosInforme);
					if (!informePersonalizable.isEliminarFichero()) {
						nombreFichero = fichero.getPath();
						FcsFacturacionJGBean bean = new FcsFacturacionJGBean();
						bean.setIdInstitucion(Integer.parseInt(idInstitucion));
						bean.setIdFacturacion(Integer.parseInt(idFacturacion));
						bean.setNombreFisico(nombreFichero);
						fact.update(bean,beanOriginal);
	
						if (fichero == null || !fichero.exists()) {							
							throw new SIGAException("messages.general.error.ficheroNoExiste");
						}
					}
				}
		
			} finally {
				//ABRIMOS EL SEMAFORO. SE DEBE EJECUTAR SIEMPRE
				setNadieEjecutando();
			}			
			
			if (informePersonalizable.isEliminarFichero()) {
				request.setAttribute("borrarFichero", "true");
			}
			
			if (fichero == null || !fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			}else{
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getPath());
			}
			
		} catch (SIGAException e) {
			throwExcp(e.getLiteral(),e, null);
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}
		
		return "descargaFichero";
	}
	
	public boolean isAlguienEjecutando(){
		synchronized(alguienEjecutando){
			if (!alguienEjecutando){
				alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}
	
	private void setNadieEjecutando(){
		synchronized(alguienEjecutando){
			alguienEjecutando=Boolean.FALSE;
		}
	}	

}