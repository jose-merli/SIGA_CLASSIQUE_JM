package com.siga.censo.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.siga.censo.dao.LetradoDao;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;



public class IbatisLetradoDao extends SqlMapDaoTemplate implements LetradoDao {
	private PagedVo paginador=new PagedVo();
	
	public IbatisLetradoDao(DaoManager daoManager) {
		super(daoManager);
	}
	
	public void setPager(PagedVo paginador){
		this.paginador=paginador;
	}

	public List getPage(Vo letrado) throws DaoException {
		return getPage(letrado, new SortedVo());
	}
	
	public List getPage(Vo letrado, SortedVo ordenacion) throws DaoException {
		return getPage(letrado, new SortedVo(), new HashMap<Object, Object>());
	}

	public List getPage(Vo letrado, SortedVo ordenacion, Map<Object, Object> dataMap)
	throws DaoException {
		Map params=new HashMap();
		params.put("letrado", letrado);
		params.put("paginador", paginador);
		params.put("ordenacion", ordenacion);
		params.putAll(dataMap);
		
		try {
			return getSqlMapExecutor().queryForList("Censo.paginaLetrados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener la pagina "+paginador.getPage()+" de la busqueda de letrados.", se);
		}
	}

	public Integer getTotalSize(Vo letrado) throws DaoException {
		return getTotalSize(letrado,null);
	}

	public Integer getTotalSize(Vo vo, Map<Object, Object> map)
			throws DaoException {
		Map params=new HashMap();
		params.put("letrado", vo);
		params.put("paginador", paginador);

		try {
			return (Integer)getSqlMapExecutor().queryForObject("Censo.numeroLetrados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener el numero de letrados.", se);
		}

	}

	public List getAll(Vo letrado, String idioma, String idInstitucion){
		Map params=new HashMap();
		params.put("idInstitucion", idInstitucion);
		params.put("idioma", idioma);
		params.put("letrado", letrado);

		try {
			return getSqlMapExecutor().queryForList("Censo.getAllLetrados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener el obtener los letrados.", se);
		}
	}
}
