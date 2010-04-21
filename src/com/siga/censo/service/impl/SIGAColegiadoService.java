package com.siga.censo.service.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoException;
import com.siga.censo.dao.ColegiadoDao;
import com.siga.censo.service.ColegiadoService;
import com.siga.censo.vos.ColegiadoVO;
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
public class SIGAColegiadoService 
extends DaoBusinessServiceTemplate implements ColegiadoService {

	public SIGAColegiadoService(BusinessManager businessManager) {
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
	public List<NombreVO> getNombre(NombreVO nombre){
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
	public List<NombreVO> getApellido2(NombreVO nombre){
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


	public List<Vo> buscar(Vo vo, PagedVo pagedVo, SortedVo sortedVo, Map<Object,Object> map) throws SIGAException {
		List<Vo> lista = null;

		PagedDao dao = (PagedDao)getDaoManager().getDao(ColegiadoDao.class);
		ValueListIterator vli=new ValueListHandler(dao, vo, sortedVo, pagedVo, map );
		try {
			pagedVo.setTotalListSize(vli.getTotalTableSize());
			lista = vli.getPage(pagedVo.getPage().intValue());
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		return lista;
	}

	public List<Vo> buscar(Vo vo, PagedVo pagedVo, SortedVo sortedVo,
			String idioma, String idInstitucion) throws SIGAException {
		Map<Object,Object> map = new Hashtable<Object,Object>();
		map.put("idInstitucion",idInstitucion);
		List<Vo> lista = buscar(vo,pagedVo,sortedVo,map);
		
		ColegiadoDao colegiadoDao = (ColegiadoDao)getDaoManager().getDao(ColegiadoDao.class);

		for (Vo voItem: lista){
			ColegiadoVO colegiado = (ColegiadoVO) voItem;
			String estadoColegial = colegiadoDao.getEstadoColegial(
					colegiado.getIdInstitucion(), colegiado.getIdPersona(), idioma);
			colegiado.setDescEstadoColegial(estadoColegial);
		}

		return lista;
	}

	public List<Vo> buscar(Vo vo, PagedVo pagedVo, SortedVo sortedVo)
			throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Vo> buscar(Vo vo, String idioma, String idInstitucion)
			throws SIGAException {
		List<Vo> lista = null;

		ColegiadoDao dao = (ColegiadoDao)getDaoManager().getDao(ColegiadoDao.class);
		try {
			lista = dao.getAll(vo,idioma,idInstitucion);
		} catch (DaoException de){
			throw new SIGAException (de);
		}
		return lista;
	}

}
