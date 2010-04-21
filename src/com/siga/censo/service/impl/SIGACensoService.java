package com.siga.censo.service.impl;

import java.util.List;

import com.ibatis.dao.client.DaoException;
import com.siga.censo.dao.CensoDao;
import com.siga.censo.service.CensoService;
import com.siga.comun.vos.InstitucionVO;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.BusinessService;
import es.satec.businessManager.template.DaoBusinessServiceTemplate;


public class SIGACensoService extends DaoBusinessServiceTemplate implements CensoService {

	public SIGACensoService(BusinessManager businessManager) {
		super(businessManager);
	}


	public List<InstitucionVO> getColegiosNoConsejo(String idInstitucion) throws SIGAException {
		List<InstitucionVO> lista = null;

		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getColegiosNoConsejo(idInstitucion);
		} catch (DaoException de){
			throw new SIGAException (de);
		}

		return lista;
	}

	public List<InstitucionVO> getColegiosDeConsejo(String idInstitucion) throws SIGAException {
		List<InstitucionVO> lista = null;

		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getColegiosDeConsejo(idInstitucion);
		} catch (DaoException de){
			throw new SIGAException (de);
		}

		return lista;
	}


	public Object executeService(Object... parameters) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}


	public List<ValueKeyVO> getGruposFijos(String idioma, String idInstitucion)
			throws SIGAException {
		List<ValueKeyVO> lista = null;

		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getGruposFijos(idioma, idInstitucion);
		} catch (DaoException de){
			throw new SIGAException (de);
		}

		return lista;
	}

	public List<ValueKeyVO> getTipoColegiacion(String idioma) throws SIGAException {
		List<ValueKeyVO> lista = null;
		
		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getTipoColegiacion(idioma);
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		
		return lista;
	}
	
	public List<ValueKeyVO> getTiposCV(String idioma)throws SIGAException {
		List<ValueKeyVO> lista = null;
		
		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getTiposCV(idioma);
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		
		return lista;
	}
	
	public List<ValueKeyVO> getComisiones(String idioma, String idInstitucion, String idTipoCV) throws SIGAException {
		List<ValueKeyVO> lista = null;
		
		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getComisiones(idioma, idInstitucion, idTipoCV);
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		
		return lista;
	}
	
	public List<ValueKeyVO> getCargos(String idioma, String idInstitucion, String idTipoCV) throws SIGAException {
		List<ValueKeyVO> lista = null;
		
		CensoDao dao = (CensoDao)getDaoManager().getDao(CensoDao.class);
		try {
			lista = dao.getCargos(idioma, idInstitucion, idTipoCV);
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		
		return lista;
	}
	
}
