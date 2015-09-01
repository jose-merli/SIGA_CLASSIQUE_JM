package com.siga.general.service;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.autogen.model.GenFichero;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;
import org.redabogacia.sigaservices.app.vo.gen.service.FicheroVoService;
import org.redabogacia.sigaservices.app.vo.services.VoDbService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.GenFicheroAdm;
import com.siga.beans.GenFicheroBean;

/**
 * @author Carlos Ruano Martínez 
 * @date 10/06/2015
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class SIGAFicherosService {
	
	private static Logger log = Logger.getLogger(SIGAFicherosService.class);

	public void insert(FicheroVo ficheroVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.FicherosServiceImpl.insert");
		GenFicheroAdm genFicheroAdm = new GenFicheroAdm(usrBean);
		GenFicheroBean ficheroBean = new GenFicheroBean();
		VoDbService<FicheroVo, GenFichero> voService = new FicheroVoService();
		try {
			GenFichero genFichero = voService.getVo2Db(ficheroVo);
			genFichero.setFechamodificacion(new Date());
			genFichero.setUsumodificacion(ficheroVo.getUsumodificacion());
			genFichero.setIdfichero(genFicheroAdm.getNuevoId());
			ficheroBean.setFichero(genFichero);
			genFicheroAdm.insert(ficheroBean);

			FicheroVo auxFicheroVo = voService.getDb2Vo(genFichero);
			ficheroVo.setIdfichero(auxFicheroVo.getIdfichero());
			ficheroVo.setNombre(auxFicheroVo.getNombre());
			log.info(dateLog + ":fin.FicherosServiceImpl.insert");
		} catch (Exception e) {
			throw new BusinessException("Error al insertar un fichero.", e);
		}
	}

	public void updateNombreFichero(FicheroVo ficheroVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.FicherosServiceImpl.updateNombreFichero");
		GenFicheroAdm genFicheroAdm = new GenFicheroAdm(usrBean);
		GenFicheroBean ficheroBean = new GenFicheroBean();
		VoDbService<FicheroVo, GenFichero> voService = new FicheroVoService();

		try {
			GenFichero genFichero = voService.getVo2Db(ficheroVo);
			genFichero.setFechamodificacion(new Date());
			genFichero.setUsumodificacion(ficheroVo.getUsumodificacion());
			ficheroBean.setFichero(genFichero);
			genFicheroAdm.updateDirect(ficheroBean);
			log.info(dateLog + ":fin.FicherosServiceImpl.updateNombreFichero");
		} catch (Exception e) {
			throw new BusinessException("Error al update un fichero.", e);
		}
	}

	public void delete(FicheroVo ficheroVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.FicherosServiceImpl.delete");
		GenFicheroAdm genFicheroAdm = new GenFicheroAdm(usrBean);
		GenFicheroBean ficheroBean = new GenFicheroBean();
		VoDbService<FicheroVo, GenFichero> voService = new FicheroVoService();

		try {
			GenFichero genFichero = voService.getVo2Db(ficheroVo);
			ficheroBean.setFichero(genFichero);
			genFicheroAdm.delete(ficheroBean);
			SIGAServicesHelper.deleteFichero(ficheroVo.getDirectorio(), ficheroVo.getNombre());
			log.info(dateLog + ":fin.FicherosServiceImpl.delete");
		} catch (Exception e) {
			throw new BusinessException("Error al delete un fichero.", e);
		}
	}

	public FicheroVo getFichero(FicheroVo ficheroVo, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.FicherosServiceImpl.getFichero");
		GenFicheroAdm genFicheroAdm = new GenFicheroAdm(usrBean);
		GenFicheroBean ficheroBean = new GenFicheroBean();
		VoDbService<FicheroVo, GenFichero> voFicheroService = new FicheroVoService();
		try {
			GenFichero genFichero = voFicheroService.getVo2Db(ficheroVo);
			genFichero = genFicheroAdm.select(genFichero.getIdinstitucion(), genFichero.getIdfichero());
			ficheroVo = voFicheroService.getDb2Vo(genFichero);
			log.info(dateLog + ":fin.FicherosServiceImpl.getFichero");
		} catch (Exception e) {
			throw new BusinessException("Error al select un fichero.", e);
		}
		return ficheroVo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redabogacia.sigaservices.app.services.Service#delete(java.lang.Object)
	 */
	public int delete(GenFichero obj, UsrBean usrBean) throws BusinessException {
		Date dateLog = new Date();
		log.info(dateLog + ":inicio.FicherosServiceImpl.delete");
		FicheroVo ficheroVo = new FicheroVo();
		ficheroVo.setIdinstitucion(obj.getIdinstitucion());
		ficheroVo.setIdfichero(obj.getIdfichero());
		ficheroVo = getFichero(ficheroVo,usrBean);
		delete(ficheroVo,usrBean);
		log.info(dateLog + ":fin.FicherosServiceImpl.delete");
		return 0;
	}

}
