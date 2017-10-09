package com.siga.ws.c3009;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.autogen.model.CajgRemesaestados;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.helper.ftp.FtpPcajgAbstract;
import org.redabogacia.sigaservices.app.helper.ftp.FtpPcajgFactory;
import org.redabogacia.sigaservices.app.services.caj.CajgEjgRemesaService;
import org.redabogacia.sigaservices.app.services.caj.CajgRemesaEstadosService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.jcraft.jsch.JSchException;
import com.siga.ws.PCAJGConstantes;
import com.siga.ws.SIGAWSClientAbstract;

import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author jorgeta
 * 
 */
public class PCAJGEnviaXmlFTP extends SIGAWSClientAbstract implements
		PCAJGConstantes {

	public void execute() throws Exception {

		UsrBean usr = getUsrBean();
		int idInstitucion = getIdInstitucion();
//		UserTransaction tx = usr.getTransactionPesada();

		FtpPcajgAbstract ftpPcajgAbstract = null;

		try {

			List<File> files = getXmlFile(idInstitucion, getIdRemesa());
			// hacemos commit por los posibles errores de validación del xml con
			// el xsd
			BusinessManager.getInstance().startTransaction();
			if (files != null && files.size() > 0) {
//				tx.begin();
				CajgRemesaEstadosService cajgRemesaEstadosService = (CajgRemesaEstadosService) BusinessManager.getInstance().getService(CajgRemesaEstadosService.class); 
				CajgRemesaestados cajgRemesaEstados = new CajgRemesaestados();
				cajgRemesaEstados.setIdinstitucion((short)idInstitucion);
				cajgRemesaEstados.setIdremesa((long)getIdRemesa());
				cajgRemesaEstados.setFecharemesa(new Date());
				cajgRemesaEstados.setIdestado(AppConstants.ESTADOS_REMESA.ESTADO_REMESA_ENVIADA.getCodigo());
				cajgRemesaEstadosService.insert(cajgRemesaEstados);
				escribeLogRemesa("Cambiado el estado a enviado al servidor FTP");
				
				// si todo ha ido bien subimos los ficheros
				ftpPcajgAbstract = FtpPcajgFactory
						.getInstance((short) getIdInstitucion());
				escribeLogRemesa("Conectando al servidor FTP");
				ftpPcajgAbstract.connect();

				for (File file : files) {
					FileInputStream fis = new FileInputStream(file);
					escribeLogRemesa("Subiendo XML generado al servidor FTP");
					ftpPcajgAbstract.upload(file.getName(), fis);
					fis.close();
					escribeLogRemesa("El archivo se ha subido correctamente al servidor FTP");
				}
				CajgEjgRemesaService cajgEjgRemesaService = (CajgEjgRemesaService)BusinessManager.getInstance().getService(CajgEjgRemesaService.class);
				cajgEjgRemesaService.insertaEstadoEjgsRemesa(Long.valueOf(getIdRemesa()), Short.parseShort(""+getIdInstitucion()), AppConstants.ESTADOS_EJG.REMITIDO_COMISION);
				//Insertamos en la cola el servicio para obtener los ejgs marcados como errorneso por la herramienta Atlante de Canarioas
				EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
				
				EcomCola ecomColaAcuse = new EcomCola();
				ecomColaAcuse.setIdinstitucion((short)getIdInstitucion());
				ecomColaAcuse.setIdoperacion(OPERACION.ATLANTE_ACUSES_ERRONEOS.getId());
				
				Map<String, String> mapa = new HashMap<String, String>();
				mapa.put(IDREMESA, ""+getIdRemesa());
				ecomColaService.insertaColaConParametros(ecomColaAcuse, mapa);
				
//				Ahora vamos a insertar los no erroneos para que se ejecuten dentro de dos horas. En la operaionc si no se han revisado todos, 
				ecomColaAcuse = new EcomCola();
				ecomColaAcuse.setIdinstitucion((short)getIdInstitucion());
				ecomColaAcuse.setIdoperacion(OPERACION.ATLANTE_ACUSES_NOERRONEOS.getId());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date()); // Configuramos la fecha que se recibe
     		    calendar.add(Calendar.HOUR, 2);  // numero de horas a añadir, o restar en caso de horas<0
				ecomColaAcuse.setFechaejecucion(calendar.getTime());
				ecomColaService.insertaColaConParametros(ecomColaAcuse, mapa);
				
				BusinessManager.getInstance().commitTransaction();
			}

		} catch (JSchException e) {
			escribeLogRemesa("Se ha producido un error de conexión con el servidor FTP en la institución "
					+ getIdInstitucion());
			ClsLogging.writeFileLogError(
					"Se ha producido un error en el envío FTP para la institución "
							+ getIdInstitucion(), e, 3);
		} catch (Exception e) {
			ClsLogging.writeFileLogError(
					"Error en al generar y enviar el fichero xml para la institución "
							+ getIdInstitucion(), e, 3);
			throw e;
		} finally {
			if (ftpPcajgAbstract != null) {
				ftpPcajgAbstract.disconnect();
			}
			BusinessManager.getInstance().endTransaction();
		}

	}

}