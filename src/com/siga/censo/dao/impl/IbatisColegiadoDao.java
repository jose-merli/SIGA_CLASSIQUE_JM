package com.siga.censo.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.siga.censo.dao.ColegiadoDao;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;



public class IbatisColegiadoDao extends SqlMapDaoTemplate implements ColegiadoDao {
	private PagedVo paginador=new PagedVo();
	
	public IbatisColegiadoDao(DaoManager daoManager) {
		super(daoManager);
	}
	
	public void setPager(PagedVo paginador){
		this.paginador=paginador;
	}

	public List getPage(Vo colegiado) throws DaoException {
		return getPage(colegiado, new SortedVo());
	}
	
	public List getPage(Vo colegiado, SortedVo ordenacion) throws DaoException {
		return getPage(colegiado, ordenacion, new HashMap<Object, Object>());
	}
	
	public List getPage(Vo colegiado, SortedVo ordenacion, Map<Object,Object> dataMap) throws DaoException {
		Map params=new HashMap();
		params.put("colegiado", colegiado);
		params.put("paginador", paginador);
		params.put("ordenacion", ordenacion);
		params.putAll(dataMap);
		
		try {
			return getSqlMapExecutor().queryForList("Censo.paginaColegiados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener la pagina "+paginador.getPage()+" de la busqueda de colegiados.", se);
		}
	}

	public Integer getTotalSize(Vo colegiado) throws DaoException {
		return getTotalSize(colegiado,null);
	}

	public Integer getTotalSize(Vo colegiado, Map<Object,Object> map) throws DaoException {
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("colegiado", colegiado);
		params.put("paginador", paginador);
		params.putAll(map);

		try {
			return (Integer)getSqlMapExecutor().queryForObject("Censo.numeroColegiados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener el numero de colegiados.", se);
		}
	}
	
	
	public String getEstadoColegial(String idInstitucion, String idPersona, String idioma){
		Map params=new HashMap();
		params.put("idInstitucion", idInstitucion);
		params.put("idPersona", idPersona);
		params.put("idioma", idioma);

		try {
			return (String)getSqlMapExecutor().queryForObject("Censo.selectEstadoColegial", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener el estado colegial.", se);
		}
	}
	
	public List getAll(Vo colegiado, String idioma, String idInstitucion){
		Map params=new HashMap();
		params.put("idInstitucion", idInstitucion);
		params.put("idioma", idioma);
		params.put("colegiado", colegiado);

		try {
			return getSqlMapExecutor().queryForList("Censo.getAllColegiados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener los colegiados.", se);
		}
	}
	public Date getFechaEstadoColegial(Vo colegiado){
		Map params=new HashMap();
		params.put("colegiado", colegiado);
		
		try {
			return (Date)getSqlMapExecutor().queryForObject("Censo.fechaEstadoColegial", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener la fecha de estado coelgial.", se);
		}
	}
}
