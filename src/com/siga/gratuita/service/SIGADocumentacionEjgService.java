package com.siga.gratuita.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.gen.impl.FicherosServiceImpl;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsDocumentacionEJGBean;
import com.siga.beans.ScsDocumentacionEJGExtendedBean;
import com.siga.beans.ScsDocumentoEJGAdm;
import com.siga.beans.ScsDocumentoEJGBean;
import com.siga.beans.ScsTipoDocumentoEJGBean;
import com.siga.general.service.SIGAFicherosService;
import com.siga.gratuita.vos.SIGADocumentacionEjgVo;

/**
 * @author Carlos Ruano Martínez
 * @date 10/06/2015
 * 
 *       Ser Campeón no es una Meta, es una Actitud
 * 
 */

public class SIGADocumentacionEjgService {
	
	private static Logger log = Logger.getLogger(FicherosServiceImpl.class);

	public SIGADocumentacionEjgVo getDocumentacionEjg(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.getDocumentacionEjg");
		ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm(usrBean);
		try {
			ScsDocumentacionEJGBean scsDocumentacionejg = ejgDocuAdm.select(documentacionEjgVo);
			String numEjg = documentacionEjgVo.getNumEjg();
			documentacionEjgVo = this.getSigaDocEJG2Vo(scsDocumentacionejg);
			documentacionEjgVo.setNumEjg(numEjg);
			if (scsDocumentacionejg.getPresentador() != null && !scsDocumentacionejg.getPresentador().equals(""))
				documentacionEjgVo.setIdPresentador("IDPERSONAJG_" + scsDocumentacionejg.getPresentador());
			else
				documentacionEjgVo.setIdPresentador("IDMAESTROPRESENTADOR_" + scsDocumentacionejg.getIdPresentadorMaestro());

			if (documentacionEjgVo.getIdFichero() != null) {
				SIGAFicherosService ficherosService = new SIGAFicherosService();
				FicheroVo ficheroVo = new FicheroVo();
				ficheroVo.setIdfichero(documentacionEjgVo.getIdFichero());
				ficheroVo.setIdinstitucion(documentacionEjgVo.getIdInstitucion().shortValue());
				ficheroVo = ficherosService.getFichero(ficheroVo, usrBean);
				documentacionEjgVo.setNombreArchivo(ficheroVo.getNombre());
				documentacionEjgVo.setDirectorioArchivo(ficheroVo.getDirectorio());
				documentacionEjgVo.setDescripcionArchivo(ficheroVo.getDescripcion());
				documentacionEjgVo.setExtensionArchivo(ficheroVo.getExtension());
				documentacionEjgVo.setFechaArchivo(ficheroVo.getFechamodificacion());
			}
			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.getDocumentacionEjg");

		} catch (ClsExceptions e) {
			throw new BusinessException("Error al update un getDocumentacionEjg.", e);
		}

		return documentacionEjgVo;
	}
	
	public void update(SIGADocumentacionEjgVo documentacionEjgVoOld, SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.update");
		ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm (usrBean);
		
		try {
			ejgDocuAdm.delete(documentacionEjgVoOld);
			if (documentacionEjgVo.isBorrarFichero()) {
				documentacionEjgVo.setIdFichero(null);
			}
			
			ejgDocuAdm.insert(documentacionEjgVo);
			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.update");
		} catch (ClsExceptions e) {
			throw new BusinessException("Error al update un fichero.", e);
		}
	}

	public void uploadFile(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.uploadFile");
		SIGAFicherosService ficherosService = new SIGAFicherosService();
		FicheroVo ficheroVo = new FicheroVo();

		try {
			String directorioFichero = getDirectorioFichero(documentacionEjgVo);
			ficheroVo.setDirectorio(directorioFichero);
			ficheroVo.setDescripcion(documentacionEjgVo.getDescripcionArchivo());
			ficheroVo.setIdinstitucion(documentacionEjgVo.getIdInstitucion().shortValue());
			ficheroVo.setFichero(documentacionEjgVo.getFichero());
			ficheroVo.setExtension(documentacionEjgVo.getExtensionArchivo());

			ficheroVo.setUsumodificacion(documentacionEjgVo.getUsuMod());
			ficheroVo.setFechamodificacion(new Date());
			ficherosService.insert(ficheroVo, usrBean);
			SIGAServicesHelper.uploadFichero(ficheroVo.getDirectorio(), ficheroVo.getNombre(), ficheroVo.getFichero());
			documentacionEjgVo.setIdFichero(ficheroVo.getIdfichero());
			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.uploadFile");

		} catch (Exception e) {
			throw new BusinessException("Error al uploadFile un fichero.", e);
		}
	}

	public void borrarFichero(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.borrarFichero");
		ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm(usrBean);

		try {
			documentacionEjgVo.setIdFichero(null);
			ejgDocuAdm.updateDirect(documentacionEjgVo);
			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.borrarFichero");
		} catch (ClsExceptions e) {
			throw new BusinessException("Error al borrarFichero un fichero.", e);
		}
	}

	public File getFile(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.getFile");
		StringBuffer pathFichero = new StringBuffer(documentacionEjgVo.getDirectorioArchivo());
		pathFichero.append(File.separator);
		pathFichero.append(documentacionEjgVo.getNombreArchivo());
		File file = new File(pathFichero.toString());
		log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.getFile");
		return file;
	}

	public void deleteFile(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.deleteFile");
		SIGAFicherosService ficherosService = new SIGAFicherosService();
		FicheroVo ficheroVo = new FicheroVo();
		ficheroVo.setIdinstitucion(documentacionEjgVo.getIdInstitucion().shortValue());
		ficheroVo.setIdfichero(documentacionEjgVo.getIdFichero());
		ficheroVo = ficherosService.getFichero(ficheroVo, usrBean);
		ficherosService.delete(ficheroVo, usrBean);
		log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.deleteFile");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.gratuita.service.DocumentacionEjgService#insert(org.redabogacia.sigaservices.app.vo.scs.SIGADocumentacionEjgVo)
	 */
	public void insert(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.insert");
		ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm(usrBean);
		ScsDocumentoEJGAdm docAdm = new ScsDocumentoEJGAdm(usrBean);
		try {
			if (documentacionEjgVo.getIdDocumento() == null) {
				Hashtable miHash = new Hashtable();
				miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION, documentacionEjgVo.getIdInstitucion().toString());
				miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, documentacionEjgVo.getIdTipoDocumento());
				List<ScsDocumentoEJGBean> documentoejgs = docAdm.getListadoDocumentosEjg(miHash);
				for (ScsDocumentoEJGBean scsDocumentoejg : documentoejgs) {
					documentacionEjgVo.setIdDocumentacion(ejgDocuAdm.getNuevoId());
					documentacionEjgVo.setIdDocumento(scsDocumentoejg.getIdDocumentoEJG().toString());
					documentacionEjgVo.setComisionAJG( usrBean.isComision()?(short)1:(short)0);
					ejgDocuAdm.insert(documentacionEjgVo);
				}

			} else {
				ScsDocumentacionEJGBean documentacionejg = documentacionEjgVo;
				documentacionejg.setIdDocumentacion(ejgDocuAdm.getNuevoId());
				documentacionejg.setComisionAJG( usrBean.isComision()?(short)1:(short)0);
				ejgDocuAdm.insert(documentacionejg);
				documentacionEjgVo.setIdDocumentacion(documentacionejg.getIdDocumentacion());
			}
			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.insert");

		} catch (ClsExceptions e) {
			throw new BusinessException("Error al insert ScsDocumentacionEJGBean.", e);
		}
	}

	/**
	 * Hay que sobreescribir este metodo cada vez que haya un tipo distinto de docuemntacion
	 * 
	 * @param documentacionEjgVo
	 * @param pathFicheros
	 * @return
	 */
	private String getDirectorioFichero(SIGADocumentacionEjgVo documentacionEjgVo) {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.getDirectorioFichero");
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFicheros = rp.returnProperty("gen.ficheros.path");
		StringBuffer directorioFichero = new StringBuffer(pathFicheros);
		directorioFichero.append(documentacionEjgVo.getIdInstitucion());
		directorioFichero.append(File.separator);
		String directorio = rp.returnProperty("scs.ficheros.expedientesJG");
		directorioFichero.append(directorio);
		directorioFichero.append(File.separator);
		directorioFichero.append(documentacionEjgVo.getAnio());
		directorioFichero.append("_");
		directorioFichero.append(documentacionEjgVo.getNumEjg());
		log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.getDirectorioFichero");
		return directorioFichero.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.gratuita.service.DocumentacionEjgService#delete(org.redabogacia.sigaservices.app.vo.scs.SIGADocumentacionEjgVo)
	 */
	public void borrar(SIGADocumentacionEjgVo documentacionEjgVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.borrar");
		ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm(usrBean);

		try {
			ScsDocumentacionEJGBean documentacionejg = ejgDocuAdm.select(documentacionEjgVo);
			documentacionEjgVo.setIdFichero(documentacionejg.getIdFichero());
			ejgDocuAdm.delete(documentacionEjgVo);

			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.borrar");
		} catch (ClsExceptions e) {
			throw new BusinessException("Error al borrar ScsDocumentacionEJGBean.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redabogacia.sigaservices.app.services.scs.DocumentacionEjgService#getListadoDocumentacionEJG(org.redabogacia.sigaservices.app.vo.scs.SIGADocumentacionEjgVo)
	 */
	public List<SIGADocumentacionEjgVo> getListadoDocumentacionEJG(SIGADocumentacionEjgVo documentacionEjgVo, String codIdioma, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.DocumentacionEjgServiceImpl.getListadoDocumentacionEJG");
		ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm(usrBean);
		List<SIGADocumentacionEjgVo> documentacionEjgVos = new ArrayList<SIGADocumentacionEjgVo>();
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("idInstitucion", documentacionEjgVo.getIdInstitucion());
		params.put("anio", documentacionEjgVo.getAnio());
		params.put("idTipoEJG", documentacionEjgVo.getIdTipoEJG());
		params.put("numero", documentacionEjgVo.getNumero());
		params.put("codIdioma", codIdioma);

		try {
			List<ScsDocumentacionEJGExtendedBean> scsDocumentacionEJGExtendedList = ejgDocuAdm.getListadoDocumentacionEJG(params);
			for (ScsDocumentacionEJGExtendedBean scsDocumentacionEJGExtended : scsDocumentacionEJGExtendedList) {
				SIGADocumentacionEjgVo documentacionEjgVo2 = this.getSigaDocEJG2Vo(scsDocumentacionEJGExtended.getDocumentacionEJGBean());
				documentacionEjgVo2.setIdPresentador(scsDocumentacionEJGExtended.getIdPresentador());
				documentacionEjgVo2.setDescPresentador(scsDocumentacionEJGExtended.getDescPresentador());
				documentacionEjgVo2.setDocumentoAbreviatura(scsDocumentacionEJGExtended.getDocumentoAbreviatura());
				if(scsDocumentacionEJGExtended.getComisionAJG()!=null && !scsDocumentacionEJGExtended.getComisionAJG().equals(""))
					documentacionEjgVo2.setComisionAJG(new Short(scsDocumentacionEJGExtended.getComisionAJG()));
				documentacionEjgVo2.setNumIntercambiosOk(scsDocumentacionEJGExtended.getNumIntercambiosOk());
				documentacionEjgVos.add(documentacionEjgVo2);

			}
			log.info(dateLog + ":fin.DocumentacionEjgServiceImpl.getListadoDocumentacionEJG");
		
		} catch (ClsExceptions e) {
			throw new BusinessException("Error al ejectuar getListadoDocumentacionEJG.", e);
		}
		
		return documentacionEjgVos;
	}
	
	private SIGADocumentacionEjgVo getSigaDocEJG2Vo(ScsDocumentacionEJGBean objectDb) throws ClsExceptions {
		SIGADocumentacionEjgVo documentacionEjgVo = new SIGADocumentacionEjgVo();
		
		if (objectDb.getIdInstitucion() != null && !objectDb.getIdInstitucion().equals("")) 
			documentacionEjgVo.setIdInstitucion(objectDb.getIdInstitucion());
		if (objectDb.getAnio() != null && !objectDb.getAnio().equals(""))
			documentacionEjgVo.setAnio(objectDb.getAnio());
		if (objectDb.getIdTipoEJG() != null && !objectDb.getIdTipoEJG().equals(""))
			documentacionEjgVo.setIdTipoEJG(objectDb.getIdTipoEJG());
		if (objectDb.getNumero() != null && !objectDb.getNumero().equals(""))
			documentacionEjgVo.setNumero(objectDb.getNumero());
		if (objectDb.getIdDocumentacion() != null && !objectDb.getIdDocumentacion().equals(""))
			documentacionEjgVo.setIdDocumentacion(objectDb.getIdDocumentacion());
		if (objectDb.getIdDocumento() != null && !objectDb.getIdDocumento().equals(""))
			documentacionEjgVo.setIdDocumento(objectDb.getIdDocumento());
		if (objectDb.getIdTipoDocumento() != null && !objectDb.getIdTipoDocumento().equals(""))
			documentacionEjgVo.setIdTipoDocumento(objectDb.getIdTipoDocumento());
		if (objectDb.getPresentador() != null && !objectDb.getPresentador().equals(""))
			documentacionEjgVo.setPresentador(objectDb.getPresentador());
		if (objectDb.getIdFichero() != null && !objectDb.getIdFichero().equals(""))
			documentacionEjgVo.setIdFichero(objectDb.getIdFichero());
		if (objectDb.getFechaEntrega() != null && !objectDb.getFechaEntrega().equals("")) 
			documentacionEjgVo.setFechaEntrega(GstDate.getFormatedDateShort("ES",objectDb.getFechaEntrega()));
		if (objectDb.getFechaLimite() != null && !objectDb.getFechaLimite().equals("")) 
			documentacionEjgVo.setFechaLimite(GstDate.getFormatedDateShort("ES",objectDb.getFechaLimite()));
		if (objectDb.getRegEntrada() != null && !objectDb.getRegEntrada().equals(""))
			documentacionEjgVo.setRegEntrada(objectDb.getRegEntrada());
		if (objectDb.getRegSalida() != null && !objectDb.getRegSalida().equals(""))
			documentacionEjgVo.setRegSalida(objectDb.getRegSalida());
		if (objectDb.getDocumentacion() != null && !objectDb.getDocumentacion().equals(""))
			documentacionEjgVo.setDocumentacion(objectDb.getDocumentacion());
		if(objectDb.getComisionAJG()!=null && !objectDb.getComisionAJG().equals(""))
			documentacionEjgVo.setComisionAJG(new Short(objectDb.getComisionAJG()));
		if(objectDb.getNumero()!=null && !objectDb.getComisionAJG().equals(""))
			documentacionEjgVo.setComisionAJG(new Short(objectDb.getComisionAJG()));
		
		return documentacionEjgVo;
	}	

}
