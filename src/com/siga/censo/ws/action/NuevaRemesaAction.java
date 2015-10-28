package com.siga.censo.ws.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESESTADOENVIO;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_TIPO_ENVIO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvio;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsPagina;
import org.redabogacia.sigaservices.app.services.cen.EcomCenWsEnvioService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.ws.form.NuevaRemesaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class NuevaRemesaAction extends MasterAction {
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
		parentFile.mkdirs();
		
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
