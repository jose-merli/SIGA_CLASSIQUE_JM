package com.siga.censo.ws.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESESTADOENVIO;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_TIPO_ENVIO;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvio;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsPagina;
import org.redabogacia.sigaservices.app.services.cen.EcomCenWsEnvioService;
import org.redabogacia.sigaservices.app.services.ecom.EcColaService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.FileHelper;
import com.siga.censo.ws.form.NuevaRemesaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import es.satec.businessManager.BusinessManager;

public class NuevaRemesaAction extends MasterAction {
	private static final Logger log = Logger.getLogger(NuevaRemesaAction.class);
	
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		log.debug("NuevaRemesaAction.executeInternal() - INICIO"); 

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
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("insertar")) {
					log.debug("NuevaRemesaAction.actualizaWS() - insertar"); 
					mapDestino = insertar(mapping, miForm, request, response);
				
				} else if (accion.equalsIgnoreCase("actualizaWS")) {
					log.debug("NuevaRemesaAction.actualizaWS() - actualizaWS"); 
					mapDestino = actualizaWS(mapping, miForm, request, response);
				}
			
				if (mapDestino == null) {
					// mapDestino = "exception";
					if (miForm.getModal().equalsIgnoreCase("TRUE")) {
						request.setAttribute("exceptionTarget", "parent.modal");
					}
	
					throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
				}
	
			} catch (SIGAException es) {
							throw es;
			} catch (Exception e) {
					throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
			}
		log.debug("NuevaRemesaAction.executeInternal() - FIN"); 
		
		return mapping.findForward(mapDestino);
	}
	
	
	private String actualizaWS(ActionMapping mapping, MasterForm masterForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Short idcol = null;
		try {
			log.debug("NuevaRemesaAction.actualizaWS() - INICIO"); 
			NuevaRemesaForm form = (NuevaRemesaForm) masterForm;
			String idColegio=form.getIdColegioActualizar();
			
			if(idColegio!=null){
				idcol=Short.valueOf(idColegio);
			}
			
			// Si el parametro de proxy está activo hay que introducirlo en la cola de censoWS
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			String activo = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.CEN_WS_PROXY_ACTIVO, MODULO.CEN);
			
			if(AppConstants.DB_TRUE.equals(activo)){
				EcColaService ecColaService = (EcColaService) BusinessManager.getInstance().getService(EcColaService.class);
				if (ecColaService.insertaColaCargaCenso(idcol, false) != 1) {
					throw new Exception("No se ha podido insertar correctamente en la cola de censoWS para el colegio " + idcol);
				}
			}else{
				EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
				if (ecomColaService.insertaColaCargaCenso(idcol, false) != 1) {
					throw new Exception("No se ha podido insertar correctamente en la cola para el colegio " + idcol);
				}
			}	
		} catch (Exception e) {
			log.error("Error al insertar en la cola para el colegio " + idcol, e);
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);			
		}
		
		log.debug("NuevaRemesaAction.actualizaWS() - FIN"); 
		return exitoRefresco("messages.success.censo.peticion", request);
	}
	
	
	protected synchronized String insertar(ActionMapping mapping, MasterForm masterForm, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		
		try {
			NuevaRemesaForm form = (NuevaRemesaForm) masterForm;			
			FormFile formFile = form.getFile();
			
			if (formFile.getFileSize() == 0){
				throw new SIGAException("message.cajg.ficheroValido"); 
			}
			short idinstitucion = Short.valueOf(form.getIdColegioInsertar());
			Date fechaExportacion = AppConstants.DATE_FORMAT.parse(form.getFechaExportacion());		
			DateFormat dateFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT_YYYYMMDDHHMMSS_ENVIO_STRING);
			String numeroPeticion = dateFormat.format(new Date());
			EcomCenWsEnvio ecomCenWsEnvio = crearEnvio(idinstitucion, numeroPeticion, fechaExportacion);			
			EcomCenWsPagina ecomCenWsPagina = crearPagina();
			ecomCenWsEnvio.setIdtipoenvio(ECOM_CEN_TIPO_ENVIO.TIPO_ENVIO_EXCEL.getCodigo());
			File rutaAlmacen = getRutaAlmacenFichero(idinstitucion, numeroPeticion);
			File file = new File(rutaAlmacen, formFile.getFileName());
			guardaFichero(file, formFile.getInputStream());
			
			
			EcomCenWsEnvioService ecomCenWsEnvioService = (EcomCenWsEnvioService) getBusinessManager().getService(EcomCenWsEnvioService.class);
			ecomCenWsEnvioService.subirFichero(ecomCenWsEnvio, ecomCenWsPagina, file.getAbsolutePath());
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		
		return exitoRefresco("messages.inserted.success.nuevoFicheroExcel", request);
	}
	
	
	
	private EcomCenWsPagina crearPagina() {
		//guardamos la pagina que en este caso es única
		EcomCenWsPagina ecomCenWsPagina = new EcomCenWsPagina();
		ecomCenWsPagina.setNumpagina((short)1);			
		ecomCenWsPagina.setFechapeticion(new Date());			
		ecomCenWsPagina.setFechacreacion(new Date());
		ecomCenWsPagina.setFechamodificacion(new Date());
		ecomCenWsPagina.setUsumodificacion(AppConstants.USUMODIFICACIONAUTO);
		
		return ecomCenWsPagina;
	}

	private EcomCenWsEnvio crearEnvio(short idinstitucion, String numeroPeticion, Date fechaExportacion) {
		EcomCenWsEnvio ecomCenWsEnvio = new EcomCenWsEnvio();
		ecomCenWsEnvio.setIdinstitucion(idinstitucion);
		ecomCenWsEnvio.setNumeropeticion(numeroPeticion);
		ecomCenWsEnvio.setFechaexportacion(fechaExportacion);
		ecomCenWsEnvio.setFechacreacion(new Date());
		ecomCenWsEnvio.setTotalpaginas((short)1);
		ecomCenWsEnvio.setConerrores((short)0);
		ecomCenWsEnvio.setFechamodificacion(new Date());
		ecomCenWsEnvio.setIdestadoenvio(ECOM_CEN_MAESESTADOENVIO.ANALIZANDO.getCodigo());
		ecomCenWsEnvio.setUsumodificacion(AppConstants.USUMODIFICACIONAUTO);
		
		return ecomCenWsEnvio;
	}

	private File getRutaAlmacenFichero(short idinstitucion, String numeroPeticion) {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cen.cargaExcel.ficheros.path");
		
		File parentFile = new File(rutaAlmacen, String.valueOf(idinstitucion));
		parentFile = new File(parentFile, numeroPeticion);
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
}
