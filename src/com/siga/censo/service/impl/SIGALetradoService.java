package com.siga.censo.service.impl;

import java.util.List;

import com.ibatis.dao.client.DaoException;
import com.siga.censo.dao.ColegiadoDao;
import com.siga.censo.dao.LetradoDao;
import com.siga.censo.service.LetradoService;
import com.siga.censo.vos.NombreVO;
import com.siga.comun.ValueListHandler;
import com.siga.comun.ValueListIterator;
import com.siga.comun.daos.PagedDao;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.DaoBusinessServiceTemplate;

/**
 */
public class SIGALetradoService
extends DaoBusinessServiceTemplate implements LetradoService{

	public SIGALetradoService(BusinessManager businessManager) {
		super(businessManager);
	}

	/**
	 * 
	 * @param ncolegiado
	 */
	public List<Integer> getNColegiado(int ncolegiado){
		return null;
	}

	/**
	 * 
	 * @param nif
	 */
	public List<String> getNifList(String nif){
		return null;
	}

	/**
	 * 
	 * @param cp
	 */
	public List<String> getCodigoPostal(int cp){
		return null;
	}

	/**
	 * 
	 * @param telefono
	 */
	public List<String> getTelefono(int telefono){
		return null;
	}

	/**
	 * 
	 * @param email
	 */
	public List<String> getCorreoElectronico(String email){
		return null;
	}


	/**
	 * 
	 * @param apellido2
	 * @param apellido1
	 * @param nombre
	 */
	public  List<NombreVO> getNombre(NombreVO nombre){
		return null;
	}

	/**
	 * 
	 * @param apellido2
	 * @param apellido1
	 * @param nombre
	 */
	public List<NombreVO> getApellido1(NombreVO nombre){
		return null;
	}

	/**
	 * 
	 * @param apellido2
	 * @param apellido1
	 * @param nombre
	 */
	public  List<NombreVO> getApellido2(NombreVO nombre){
		return null;

	}



	public Object executeService(Object... parameters) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Vo> buscar(Vo cliente) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Vo> buscar(Vo vo, PagedVo pagedVo,	SortedVo sortedVo) throws SIGAException {
		List<Vo> lista = null;

		PagedDao dao = (PagedDao)getDaoManager().getDao(LetradoDao.class);
		ValueListIterator vli=new ValueListHandler(dao, vo, sortedVo, pagedVo);
		try {
			pagedVo.setTotalListSize(vli.getTotalTableSize());
			lista = vli.getPage(pagedVo.getPage().intValue());
		} catch (DaoException de){
			throw new SIGAException (de);
		}

		return lista;
	}

	public List<Vo> buscar(Vo vo, String idioma, String idInstitucion)
	throws SIGAException {
		List<Vo> lista = null;

		LetradoDao dao = (LetradoDao)getDaoManager().getDao(LetradoDao.class);
		try {
			lista = dao.getAll(vo,idioma,idInstitucion);
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		return lista;
	}
}
