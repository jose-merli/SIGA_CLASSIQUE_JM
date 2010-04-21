package com.siga.censo.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.siga.censo.dao.CensoDao;
import com.siga.comun.vos.ValueKeyVO;

public class IbatisCensoDao extends SqlMapDaoTemplate implements CensoDao {
	
	public IbatisCensoDao(DaoManager daoManager) {
		super(daoManager);
	}

	@SuppressWarnings("unchecked")
	public List getColegiosNoConsejo(String idInstitucion) throws DaoException {
		Map params=new HashMap();
		params.put("idInstitucion", idInstitucion);

		try {
			return getSqlMapExecutor().queryForList("Censo.selectColegiosNoConsejo", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener instituciones.", se);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List getColegiosDeConsejo(String idInstitucion) throws DaoException {
		Map params=new HashMap();
		params.put("idInstitucion", idInstitucion);

		try {
			return getSqlMapExecutor().queryForList("Censo.selectColegiosDeConsejo", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener instituciones.", se);
		}
	}

	public List<ValueKeyVO> getGruposFijos(String idioma, String idInstitucion) {
		Map params=new HashMap();
		params.put("idInstitucion", idInstitucion);
		params.put("idioma", idioma);

		try {
			return getSqlMapExecutor().queryForList("Censo.gruposFijos", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener gruposFijos.", se);
		}
	}
	
	public List<ValueKeyVO> getTipoColegiacion(String idioma) {
		Map params=new HashMap();
		params.put("idioma", idioma);
		
		try {
			return getSqlMapExecutor().queryForList("Censo.tipoColegiacion", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener gruposFijos.", se);
		}
	}
	
	public List<ValueKeyVO> getTiposCV(String idioma) {
		Map params=new HashMap();
		params.put("idioma", idioma);
		
		try {
			return getSqlMapExecutor().queryForList("Censo.tiposCV", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener gruposFijos.", se);
		}
	}

	public List<ValueKeyVO> getComisiones(String idioma, String idInstitucion, String idTipoCV) {
		Map params=new HashMap();
		params.put("idioma", idioma);
		params.put("idInstitucion", idInstitucion);
		params.put("idTipoCV", idTipoCV);
		
		try {
			return getSqlMapExecutor().queryForList("Censo.comisiones", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener comisiones.", se);
		}
	}
	public List<ValueKeyVO> getCargos(String idioma, String idInstitucion, String idTipoCV) {
		Map params=new HashMap();
		params.put("idioma", idioma);
		params.put("idInstitucion", idInstitucion);
		params.put("idTipoCV", idTipoCV);
		
		try {
			return getSqlMapExecutor().queryForList("Censo.cargos", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener cargos.", se);
		}
	}
}
